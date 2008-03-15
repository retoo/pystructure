#!/usr/bin/python

import os
from sudoku.board import Board

board = Board("board5.txt")

board.fields
n = board.pre_solve()
print board
board.solve()
print board

board.fields


# 0.143
