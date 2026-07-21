#!/usr/bin/env python3
"""Şifahane v4.0.1 release belgesi ve teslim sözleşmeleri."""
from pathlib import Path
import re

ROOT = Path(__file__).resolve().parents[1]
required = [
    'README.md', 'CHANGELOG.md',
    'docs/ARCHITECTURE.md', 'docs/PERFORMANCE_BUDGET.md',
    'docs/ANDROID_12_16_CIHAZ_MATRISI.md', 'docs/ALARM_TEST_MATRIX.md',
    'docs/RELEASE_CHECKLIST.md',
    'docs/Sifahane-v4.0-Islevsel-Kabul-Matrisi.md',
    'docs/Sifahane-v4.0-Codex-109-Madde-Durum-Matrisi.md',
    'docs/Sifahane-v4.0-Degisiklik-ve-Test-Raporu.md',
    'docs/Sifahane-v4.0-Kalan-Dis-Dogrulamalar.md',
    'test-results/ANDROID_BUILD_ATTEMPT.txt',
]
for rel in required:
    path = ROOT / rel
    assert path.is_file() and path.stat().st_size > 100, f'Eksik/boş release belgesi: {rel}'

readme = (ROOT / 'README.md').read_text(encoding='utf-8')
gradle = (ROOT / 'app/build.gradle.kts').read_text(encoding='utf-8')
assert '# Şifahane v4.0' in readme
assert 'versionName = "4.0.1"' in gradle and 'versionCode = 400001' in gradle

plan = (ROOT / 'docs/Sifahane-v4.0-Codex-109-Madde-Durum-Matrisi.md').read_text(encoding='utf-8')
ids = re.findall(r'^\| ([A-Z]\d{2}) \|', plan, re.M)
assert len(ids) == 109 and len(set(ids)) == 109, f'109 madde matrisi eksik/tekrarlı: {len(ids)}'

functional = (ROOT / 'docs/Sifahane-v4.0-Islevsel-Kabul-Matrisi.md').read_text(encoding='utf-8')
assert 'Toplam işlevsel kontrol satırı: **277**' in functional
for forbidden in ('Version Name 3.4.0', 'Version Code 340', 'Uygulama sürümü v3.4.0'):
    assert forbidden not in functional, f'Sürüm satırı işlevsel matrise sızdı: {forbidden}'

attempt = (ROOT / 'test-results/ANDROID_BUILD_ATTEMPT.txt').read_text(encoding='utf-8')
assert 'UnknownHostException: downloads.gradle.org' in attempt
assert 'Çıkış kodu: 1' in attempt
print('RELEASE_DOCUMENTATION_CHECK_OK')
