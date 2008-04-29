class A(object):
    def get(self):
        # float from attributes_user
        self.attribute ## type float|int
        print self.attribute
        return self.attribute

    def set(self, value):
        self.attribute = value

a = A()
a.set(1)
a.get() ## type int

# * Context example: *
# Kinda strange example, the question is how the instance context affects
# other evaluators

class Context(object):
    def method(self, par):
        if random():
            self.attr = par
    
        self.another()
        return self.attr
        
    def another(self):
        self.attr = self.method(1)
        
    def another_method(self):
        return self.attr

c = Context()
c.method("x") # FIXME: doesnt work atm because of the call context # type str
c.method(1)  # FIXME: doesnt work atm because of the call context# type int
c.another_method() ## type int|str

c.attr ## type int|str
