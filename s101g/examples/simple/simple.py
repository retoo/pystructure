class Attributes(object):
    def __init__(self, arg):
        self.attr_a = arg
        self.attr_b = 1
        
    def update(self):
        self.attr_b = 2


a = Attributes()
a.update

a.attr_b
a.attr_b
a.attr_a
a.attr_b

