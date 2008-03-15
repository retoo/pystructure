from .field import Field
from .areas import Area


class Board(object):
    def __init__(self, filename):
        self.filename = filename
        self.fields = list()

        self.rows = list()
        self.cols = list()
        self.blocks = list()
        
        for i in range(9*9):
            self.fields.append(Field(i))
        
        self.interconnect()
        self.load_file()
    
    def load_file(self):
        board_file = file(self.filename)

        i = 0        
        for line in board_file:
            for char in line:
                if char.isspace():
                    continue
        
                self.fields[i].value = int(char) if char.isdigit() else None
                
                i += 1
    
    def interconnect(self):
        for i in range(9):
            self.rows.append(Area(i))
            self.cols.append(Area(i))
            self.blocks.append(Area(i))
            
        for i, field in enumerate(self.fields):
            field = self.fields[i]
            row = field.row_nr
            col = field.col_nr
            block_row = row / 3
            block_col = col / 3
            block = block_row * 3 + block_col
            
            self.rows[row].append(field)
            self.cols[col].append(field)
            self.blocks[block].append(field)
            
            field.row   = self.rows[row]
            field.col   = self.cols[col]
            field.block = self.blocks[block]
            
    def pre_solve(self):
        found_by_presolve = 0
        open_issues = True
        while open_issues:
            open_issues = False
            
            for field in self.empty_fields():
                if field.available_numbers_count == 1:
                    # print "only one possible in field " + repr(field)
                    field.value = list(field.available_numbers)[0]
                    open_issues = True
                    found_by_presolve += 1
                else:
                    nr = field.possible_only_here()
                    
                    if nr is not None:
                        # print "Nr " + str(nr) + " only possible in field " + repr(field)
                        field.value = nr
                        open_issues = True
                        found_by_presolve += 1

        return found_by_presolve
            
    def solve(self):      
        empty_fields = self.empty_fields()
        
        if not empty_fields:
            return True
        
        #empty_fields.sort()

        #field = min(empty_fields, key=Field.available_numbers)
        
        field = empty_fields[0]
        
        available_numbers = field.available_numbers
        
        if not available_numbers:
            return False

        for nr in available_numbers:
            field.value = nr
            ret = self.solve()
            
            if ret:
                return True
        
        field.value = None
        
        return False
   
    def empty_fields(self):
        return [field for field in self.fields if field.isempty()]
        
    def __str__(self):
        out = ""
        for i, field in enumerate(self.fields):
            if i % 9 == 0:
                out += "\n"
                if (i / 9) % 3 == 0:
                    out += "\n"
            elif i % 3 == 0:
                out += "  "
                
            out += str(field)
            out += " "
            
        out += "\n"
        out += "Empty fields: " + str(len(self.empty_fields())) + "\n"
        
        return out