class Base(object):
    def foo(self):
        return 42

class Sub(Base):
    pass
    
        
o = Sub()

o.foo() ## type int

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

Sektionsleiter().answer() ## type int
Sektionsleiter().saying() ## type float
DeepThought().saying() ## type str

class A():
    def x(self):
        return 1
class B(): pass
class C(A, B): pass

C().x() ## type int
