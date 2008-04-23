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

## Context example
# Kinda strange example, the question is how the instance context affects
# other evaluators

class Context(object):
    def method(self, par):
        if rand():
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


class Two:
    def setup(self, o):
        if very_complex_function_which_we_cant_analyse_right_now_or_so_more_or_less_random():
            o.attr = One()
        else:
            o.attr = Two()
        
        o.attr.foobar(o)

    def foobar(self, z):
        z.attr = One()

class One:
    def __init__(self):
        z = C()
        z.setup(self)
        
    def foobar(self, z):
        z.attr = 2
        
        
obj = One()
obj.attr # Check me # type asdf

        
