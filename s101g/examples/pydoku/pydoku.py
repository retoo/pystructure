#!/usr/bin/python

# Copyright (C) 2008  Reto Schuettel, Robin Stocker

# This file is part of Pydoku.
# 
# Pydoku is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
# 
# Pydoku is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
# 
# You should have received a copy of the GNU General Public License
# along with Pydoku.  If not, see <http://www.gnu.org/licenses/>.

import os
from sudoku.board import Board

board = Board("board6.txt")

n = board.pre_solve()
print board
board.solve()
print board

board.fields


# 0.143
