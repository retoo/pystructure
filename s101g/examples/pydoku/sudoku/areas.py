
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
