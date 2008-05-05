
class E(object): pass
class F(E): pass
class G(E): pass
class H(E): pass
class I(F, G): pass
class J(G, H): pass
class K(I, J): pass

class Boat(object): 
    def foo(self):
        return "x"
        
class DayBoat(Boat): pass
class WheelBoat(Boat): pass
class EngineLess(DayBoat): pass
class SmallMultihull(DayBoat): pass
class PedalWheelBoat(EngineLess,WheelBoat): pass
class SmallCatamaran(SmallMultihull): pass
class Pedalo(PedalWheelBoat,SmallCatamaran): pass

x = Pedalo()
x.foo() ## type str

class Pane(object): pass
class ScrollingMixin(object): pass
class EditingMixin(object): pass
class ScrollablePane(Pane,ScrollingMixin): pass
class EditablePane(Pane,EditingMixin): pass
class EditableScrollablePane(ScrollablePane,EditablePane): pass

class A(object): pass
class B(object): pass
class C(object): pass
class X(A): pass
class Y(A): pass
class Z(X,B,Y,C): pass

def mro(klass):
    mro = klass.__mro__
    return ",".join(k.__name__ for k in mro)

classes = [definition for name, definition in locals().items() if isinstance(definition, type)]
classes.sort()

for klass in classes:
    print klass.__name__ + " #" + "# mro " + mro(klass)

# GENERATED #
E ## mro E,object
F ## mro F,E,object
G ## mro G,E,object
H ## mro H,E,object
I ## mro I,F,G,E,object
J ## mro J,G,H,E,object
K ## mro K,I,F,J,G,H,E,object
Boat ## mro Boat,object
DayBoat ## mro DayBoat,Boat,object
WheelBoat ## mro WheelBoat,Boat,object
EngineLess ## mro EngineLess,DayBoat,Boat,object
SmallMultihull ## mro SmallMultihull,DayBoat,Boat,object
PedalWheelBoat ## mro PedalWheelBoat,EngineLess,DayBoat,WheelBoat,Boat,object
SmallCatamaran ## mro SmallCatamaran,SmallMultihull,DayBoat,Boat,object
Pedalo ## mro Pedalo,PedalWheelBoat,EngineLess,SmallCatamaran,SmallMultihull,DayBoat,WheelBoat,Boat,object
Pane ## mro Pane,object
ScrollingMixin ## mro ScrollingMixin,object
EditingMixin ## mro EditingMixin,object
ScrollablePane ## mro ScrollablePane,Pane,ScrollingMixin,object
EditablePane ## mro EditablePane,Pane,EditingMixin,object
EditableScrollablePane ## mro EditableScrollablePane,ScrollablePane,EditablePane,Pane,ScrollingMixin,EditingMixin,object
A ## mro A,object
B ## mro B,object
C ## mro C,object
X ## mro X,A,object
Y ## mro Y,A,object
Z ## mro Z,X,B,Y,A,C,object
# GENERATED #
