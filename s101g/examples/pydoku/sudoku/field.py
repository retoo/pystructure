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

import itertools

class Field(object):
    def __init__(self, index):
        self.index = index
        self.row_nr = index / 9
        self.col_nr = index % 9
        self._value = None
        
    
    def set_value(self, value):
        # XXX: Difficult! references Area?
        for area in [self.row, self.col, self.block]:
            if self._value is not None:
                area.available_numbers.add(self._value)
            
            if value is not None:
                area.available_numbers.remove(value)
            
        for field in self._neighbours():
            field.update_available_numbers()
            
        self._value = value
        
    def get_value(self):
        return self._value
        
    value = property(get_value, set_value)
    
    def isempty(self):
        return self.value == None

    def _neighbours(self):
        return itertools.chain(self.row, self.col, self.block)
        
    def update_available_numbers(self):
        self.available_numbers = self.row.available_numbers & self.col.available_numbers & self.block.available_numbers
        self.available_numbers_count = len(self.available_numbers)
            
    def possible_only_here(self):
        #print "Check ", repr(self)
        only_here = self.available_numbers
        
        for areas in [self.block, self.row, self.col]:
            for field in areas:
                if field is self or not field.isempty():
                    continue
                
                # print " ", only_here, "vs", field, field.available_numbers
                only_here = only_here.difference(field.available_numbers)
                
                if not only_here:
                    break
            
            if only_here:
                return only_here.pop()

        return None
            
    def __cmp__(self, other):
        return cmp(self.available_numbers_count, other.available_numbers_count)
        
    def __str__(self):        
        return str(self.value) if not self.isempty() else "_"

    def __repr__(self):
        return str(self.row_nr) + ":" + str(self.col_nr) + " " + str(self)