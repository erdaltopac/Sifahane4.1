#!/usr/bin/env python3
"""Şifahane'nin kritik erişilebilirlik ve güvenlik UI sözleşmelerini denetler."""
from pathlib import Path
import re

ROOT = Path(__file__).resolve().parents[1]
main = (ROOT / 'app/src/main/java/com/hazerfen/sifahane/MainActivity.kt').read_text()
settings = (ROOT / 'app/src/main/java/com/hazerfen/sifahane/features/SettingsFeature.kt').read_text()
all_kt = '\n'.join(p.read_text(errors='ignore') for p in (ROOT / 'app/src/main/java').rglob('*.kt'))

# Kilit ekranı sırası: desen, biyometri, yönetici şifresi, geri ve iptal.
labels = ['Text("Desen Kilidi")', 'Text("Biyometrik Giriş")', 'Text("Yönetici Şifresi")']
pos = [main.find(label) for label in labels]
assert all(p >= 0 for p in pos), 'Kilit ekranında zorunlu giriş seçeneklerinden biri eksik'
assert pos == sorted(pos), 'Kilit ekranı giriş seçenekleri beklenen sırada değil'
lock_window = main[pos[0]:pos[-1] + 3000]
assert 'Text("Geri")' in lock_window, 'Kilit ekranında Geri düğmesi eksik'
assert 'Text("İptal")' in lock_window, 'Kilit ekranında İptal düğmesi eksik'

# İzin ekranında tek genel sistem ayarı düğmesi olmalı.
assert settings.count('Text("SİSTEM AYARLARINA GİT")') == 1, 'Genel sistem ayarı düğmesi tek değil'
for old in ('TÜMÜNÜ AÇ', 'EKSİKLERİ YÖNET', 'TÜMÜNÜ KAPATMAK İÇİN'):
    assert old not in settings.upper(), f'Eski yinelenen izin düğmesi metni bulundu: {old}'

# Kritik form ve ekleme düğmelerinde bilinen 48 dp altı sabitler bulunmamalı.
critical_files = [
    ROOT / 'app/src/main/java/com/hazerfen/sifahane/features/EditorsFeature.kt',
    ROOT / 'app/src/main/java/com/hazerfen/sifahane/features/MedicationFeature.kt',
]
for path in critical_files:
    text = path.read_text()
    for literal in ('.height(44.dp)', '.size(42.dp)', '.heightIn(min = 44.dp'):
        assert literal not in text, f'{path.name}: 48 dp altı kritik hedef bulundu: {literal}'

# Yeni kullanıcı akışı erişilebilir UI testlerinde sabit etiketlerle doğrulanabilir olmalı.
for tag in ('first_run_setup', 'first_run_pattern', 'first_run_save'):
    assert tag in all_kt, f'İlk kurulum test etiketi eksik: {tag}'

# Yazı ölçeği en az %200 ek büyütmeye izin vermeli; sistem ölçeği ayrıca korunur.
theme_prefs = (ROOT / 'app/src/main/java/com/hazerfen/sifahane/ui/ThemePreferences.kt').read_text()
assert '2.0f' in theme_prefs, 'Uygulama içi yazı ölçeği %200 sınırını desteklemiyor'

print('ACCESSIBILITY_STATIC_CHECK_OK')
