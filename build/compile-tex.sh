#!/bin/bash

if [ -z "$1" ]; then
  echo "Usage: $0 <tex-file>"
  exit 99
fi

SRCPATH=$PWD/$1
SRCDIR=$(dirname $SRCPATH)

DOCUMENT=$(basename $SRCPATH .tex)

DSTFILE=$DOCUMENT.pdf
DSTPATH=$(dirname $SRCPATH)/$DSTFILE

if [ ! -e "$SRCPATH" ]; then
  echo "Oops, srcfile doesn't exist"
  exit 10
fi

#if [ -e "$DSTPATH" -a ! "$SRCPATH" -nt "$DSTPATH" ]; then
#  echo "$DSTPATH is already up to date"
#  exit 0
#fi


TMPDIR=$(mktemp -d)

echo "Temporary folder is $TMPDIR"

echo "Convert $SRCPATH to $DSTFILE"

OPTS="-interaction=nonstopmode -file-line-error-style"

cp -a $SRCDIR/* $TMPDIR

# Use the newer Koma-Script classes in the prepended path
export TEXINPUTS=/opt/koma-script:

cd $TMPDIR
pdflatex $OPTS $DOCUMENT
bibtex $DOCUMENT
pdflatex $OPTS $DOCUMENT
pdflatex $OPTS $DOCUMENT

cp $TMPDIR/$DSTFILE $DSTPATH

rm -Rf $TMPDIR
