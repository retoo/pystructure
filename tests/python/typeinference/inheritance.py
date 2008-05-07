class Base(object):
    def __init__(self):
        self.value = ""

    def foo(self):
        return 42
        
    def set_value(self):
        self.value = 1

class Sub(Base):
    pass

o = Sub()
o.set_value()
o.value # # type int

b = Base()
b.value # # type str
o.foo() # # type int

class DeepThought(object):
    def answer(self):
        return 42
        
    def saying(self):
        return ""
        
class God(DeepThought):
    def saying(self):
        return 4.2

class MrBundesrat(God): pass
class Sektionsleiter(MrBundesrat): pass

Sektionsleiter().answer() # # type int
Sektionsleiter().saying() # # type float
DeepThought().saying() # # type str

class A():
    def x(self):
        return 1
class B():
    def y(self):
        return "x"
    def x(self):
        return 5.5
class C(A, B):
    pass

C # # mro C,A,B
B # # mro B
A # # mro A

C().x() # # type int
C().y() # # type str


class CBase(object):
    def __init__(self, x):
        x ## type int
        self.value = ""
        
class CSub(CBase):
    pass
    
x = CSub(1)
x.value ## type str

