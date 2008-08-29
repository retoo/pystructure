#!/usr/bin/python

import glob
import os

style = "trac"

inputs = glob.glob("*.py")
inputs += glob.glob("*.java")

for input in inputs:
    output = input + ".rtf"
    cmd = "pygmentize -O style=" + style + " -o " + output + " " + input
    os.system(cmd)
