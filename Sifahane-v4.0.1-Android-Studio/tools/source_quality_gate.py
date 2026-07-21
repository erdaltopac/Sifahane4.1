#!/usr/bin/env python3
"""Android SDK bulunmadan çalışabilen kaynak kalite kapısı."""
from __future__ import annotations
import json, os, re, subprocess, sys, tempfile, tomllib
from pathlib import Path
from xml.etree import ElementTree

ROOT = Path(__file__).resolve().parents[1]
os.chdir(ROOT)

def run(*args, **kwargs):
    print('+', ' '.join(map(str,args)))
    subprocess.run(args, check=True, **kwargs)

# Yapısal dosyalar
for path in Path('app/src/main/res').rglob('*.xml'):
    ElementTree.parse(path)
for path in [Path('gradle/libs.versions.toml')]:
    tomllib.loads(path.read_text())
json.loads(Path('app/schemas/com.hazerfen.sifahane.data.AppDatabase/11.json').read_text())

main_sources = '\n'.join(p.read_text(errors='ignore') for p in Path('app/src/main').rglob('*') if p.suffix in {'.kt','.xml'})
assert '12345' not in main_sources, 'Kaynakta varsayılan 12345 PIN bulundu'
assert 'applicationId = "com.hazerfen.sifahane"' in Path('app/build.gradle.kts').read_text()
assert 'allowBackup="false"' in Path('app/src/main/AndroidManifest.xml').read_text()

run(sys.executable, 'tools/validate_schemas.py')
run(sys.executable, 'tools/validate_migration_v11.py')
run(sys.executable, 'tools/accessibility_static_check.py')
run(sys.executable, 'tools/kotlin_static_analysis.py')
run(sys.executable, 'tools/functional_acceptance_check.py')
run(sys.executable, 'tools/release_documentation_check.py')

sources = [
    'app/src/main/java/com/hazerfen/sifahane/security/FirstRunSecurityPolicy.kt',
    'app/src/main/java/com/hazerfen/sifahane/alarm/AlarmDeliveryPolicy.kt',
    'app/src/main/java/com/hazerfen/sifahane/alarm/MedicationAlarmKind.kt',
    'app/src/main/java/com/hazerfen/sifahane/alarm/AlarmGroupingPolicy.kt',
    'app/src/main/java/com/hazerfen/sifahane/alarm/AlarmTimePolicy.kt',
    'app/src/main/java/com/hazerfen/sifahane/data/DomainTypes.kt',
    'app/src/main/java/com/hazerfen/sifahane/data/DoseActionPolicy.kt',
    'app/src/main/java/com/hazerfen/sifahane/validation/HealthDataValidator.kt',
    'app/src/main/java/com/hazerfen/sifahane/backup/BackupArchivePolicy.kt',
    'app/src/main/java/com/hazerfen/sifahane/backup/BackupCipherCore.kt',
    'app/src/main/java/com/hazerfen/sifahane/backup/BackupValidationException.kt',
    'app/src/main/java/com/hazerfen/sifahane/export/SimpleXlsxWriter.kt',
    'tools/PureQualityGate.kt',
]
with tempfile.TemporaryDirectory(prefix='sifahane-quality-') as temp:
    jar = Path(temp) / 'quality.jar'
    run('kotlinc', *sources, '-include-runtime', '-d', str(jar))
    run('java', '-jar', str(jar))

# Hızlı Kotlin yapısal denetimi: yorum ve dizgileri kaldırdıktan sonra ayraç dengesi.
# Tam Kotlin/Compose tür denetimi Android Studio/Gradle kapısında çalışır.
def strip_kotlin(text: str) -> str:
    out=[]; i=0; state='code'; quote=''
    while i < len(text):
        two=text[i:i+2]
        if state == 'code':
            if two == '//': state='line'; i+=2; out.extend('  '); continue
            if two == '/*': state='block'; i+=2; out.extend('  '); continue
            if text.startswith('"""', i): state='triple'; i+=3; out.extend('   '); continue
            if text[i] in {'"', "'"}: state='string'; quote=text[i]; out.append(' '); i+=1; continue
            out.append(text[i]); i+=1; continue
        if state == 'line':
            if text[i] == '\n': state='code'; out.append('\n')
            else: out.append(' ')
            i+=1; continue
        if state == 'block':
            if two == '*/': state='code'; out.extend('  '); i+=2
            else: out.append('\n' if text[i]=='\n' else ' '); i+=1
            continue
        if state == 'triple':
            if text.startswith('"""', i): state='code'; out.extend('   '); i+=3
            else: out.append('\n' if text[i]=='\n' else ' '); i+=1
            continue
        if state == 'string':
            if text[i] == '\\': out.extend('  '); i+=2; continue
            if text[i] == quote: state='code'
            out.append('\n' if text[i]=='\n' else ' '); i+=1
    if state in {'block','triple','string'}:
        raise AssertionError(f'Kapanmamış Kotlin yapı: {state}')
    return ''.join(out)

pairs={')':'(',']':'[','}':'{'}
for path in Path('app/src').rglob('*.kt'):
    stack=[]
    for line_no,line in enumerate(strip_kotlin(path.read_text()).splitlines(),1):
        for ch in line:
            if ch in '([{': stack.append((ch,line_no))
            elif ch in ')]}':
                assert stack and stack[-1][0] == pairs[ch], f'{path}:{line_no} ayraç hatası'
                stack.pop()
    assert not stack, f'{path}:{stack[-1][1]} kapanmamış ayraç {stack[-1][0]}'

if (ROOT / '.git').exists():
    run('git', 'diff', '--check')
else:
    print('GIT_DIFF_CHECK_SKIPPED_NO_REPOSITORY')
print('SOURCE_QUALITY_GATE_OK')
