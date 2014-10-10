#!/bin/sh
find . -name CMakeFiles -o -name cmake_install.cmake -o -name CMakeCache.txt -exec rm -fR {} \;
