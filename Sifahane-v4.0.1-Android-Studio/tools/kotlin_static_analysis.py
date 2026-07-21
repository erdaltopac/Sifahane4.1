#!/usr/bin/env python3
"""Deterministic Kotlin source checks that run without Android SDK."""
from pathlib import Path
import re

ROOT = Path(__file__).resolve().parents[1]
source_root = ROOT / 'app/src/main/java'
errors: list[str] = []

for path in source_root.rglob('*.kt'):
    text = path.read_text(errors='strict')
    rel = path.relative_to(ROOT)
    lines = text.splitlines()
    for number, line in enumerate(lines, 1):
        if line.rstrip() != line:
            errors.append(f'{rel}:{number}: trailing whitespace')
        if '\t' in line:
            errors.append(f'{rel}:{number}: tab character')
    for forbidden in ('GlobalScope.', 'Thread.sleep(', 'runBlocking {', 'printStackTrace('):
        if forbidden in text:
            errors.append(f'{rel}: forbidden blocking/unstructured call: {forbidden}')
    # Calendar.time is a Java synthetic property and can be shadowed by a local
    # Compose state named `time`. Require the explicit setter in receiver lambdas.
    if re.search(r'Calendar\.getInstance\(\)\.apply\s*\{[^}]*\btime\s*=', text, re.S):
        errors.append(f'{rel}: Calendar.time assignment may be shadowed; use setTime(...)')
    imports = re.findall(r'^import\s+([^\n]+)$', text, re.M)
    simple: dict[str, str] = {}
    for item in imports:
        if item.endswith('.*') or ' as ' in item:
            continue
        name = item.rsplit('.', 1)[-1]
        previous = simple.get(name)
        if previous and previous != item:
            errors.append(f'{rel}: conflicting simple import {name}: {previous} / {item}')
        simple[name] = item

# Architecture budget: MainActivity is now shell/auth/shared UI; feature files stay bounded.
main_lines = len((source_root / 'com/hazerfen/sifahane/MainActivity.kt').read_text().splitlines())
if main_lines > 4500:
    errors.append(f'MainActivity.kt exceeds shell budget: {main_lines} > 4500 lines')
for path in (source_root / 'com/hazerfen/sifahane/features').glob('*.kt'):
    count = len(path.read_text().splitlines())
    if count > 2600:
        errors.append(f'{path.name} exceeds feature budget: {count} > 2600 lines')

if errors:
    raise SystemExit('KOTLIN_STATIC_ANALYSIS_FAILED\n' + '\n'.join(errors))
print('KOTLIN_STATIC_ANALYSIS_OK')
