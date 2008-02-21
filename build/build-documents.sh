#!/bin/bash

SRC=doc
OUT=report
COMPILER=build/compile-tex.sh
#for FILE in $(cd $SRC; find . -type f -name '*.tex'); do 
#	OUT_FILE=$(basename $FILE .tex)
#	OUT_DIR=$(dirname $FILE)
#	OUT_PATH="$OUT/$OUT_DIR/$OUT_FILE.pdf"
#	echo $SRC/$FILE $OUT_PATH
#done

for FILE in $(cat doc/DOCUMENTS); do 
	$COMPILER $FILE
done
