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
class B(): 
    def y(self):
        return "x"
    def x(self):
        return 5.5
class C(A, B):
    pass

C ## mro C,A,B
B ## mro B
A ## mro A

C().x() ## type int
C().y() ## type str


class E(object): pass
class F(E): pass
class G(E): pass
class H(E): pass
class I(F, G): pass
class J(H, G): pass
class K(I, J): pass

E ## mro E
F ## mro F,E
G ## mro G,E
H ## mro H,E
I ## mro I,F,G,E
J ## mro J,H,G,E
K ## mro K,I,F,J,H,G,E