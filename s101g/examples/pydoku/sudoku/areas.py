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

class Area(object):
    def __init__(self, i):
        self.i = i
        self.fields = []
        self.available_numbers = set(range(1, 10))
        
    def append(self, field):
        self.fields.append(field)
        
    def __iter__(self):
        return self.fields.__iter__()
    
    def __str__(self):
        return str(self.i)
