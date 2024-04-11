import subprocess
import os
import shutil
from pathlib import Path

REPO = str(Path(__file__).resolve().parents[1])
OUT_DIR = '{}/annotator-out/'.format(REPO)
ANNOTATOR_JAR = "/var/core.jar".format(str(Path.home()))


def prepare():
    os.makedirs(OUT_DIR, exist_ok=True)
    with open('{}/paths.tsv'.format(OUT_DIR), 'w') as o:
        o.write("{}\t{}\n".format('{}/checker.xml'.format(OUT_DIR), '{}/scanner.xml'.format(OUT_DIR)))


def run_annotator():
    prepare()
    commands = []
    commands += ["java", "-jar", ANNOTATOR_JAR]
    commands += ['-d', OUT_DIR]
    commands += ['-bc', 'cd {} && ./build.sh'.format(REPO)]
    commands += ['-cp', '{}/paths.tsv'.format(OUT_DIR)]
    commands += ['-i', 'edu.Initializer']
    commands += ['-n', 'com.taint.tainting.qual.RUntainted']
    commands += ['-cn', 'UCRTaint']
    commands += ["--depth", "20"]
    # Uncomment to see build output
    # commands += ['-rboserr']
    # Comment to inject root at a time
    commands += ['-ch']
    # Uncomment to disable cache
    # commands += ['-dc']
    # Uncomment to disable outer loop
    # commands += ['-dol']
    # Uncomment to disable parallel processing
    # commands += ['--disable-parallel-processing']

    subprocess.call(commands)


run_annotator()
