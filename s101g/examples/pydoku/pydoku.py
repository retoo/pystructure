#!/usr/bin/python

import os
from sudoku.board import Board

board = Board("board5.txt")
board
board.fields
n = board.pre_solve()
print board
board.solve()
print board




# 0.143
