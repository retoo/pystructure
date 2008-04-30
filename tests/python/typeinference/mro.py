
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
    names = [k.__name__ for k in mro]
    names.remove("object")
    return ",".join(names)

classes = [definition for name, definition in locals().items() if isinstance(definition, type)]
classes.sort()

for klass in classes:
    print klass.__name__ + " #" + "# mro " + mro(klass)

# GENERATED #
E ## mro E
F ## mro F,E
G ## mro G,E
H ## mro H,E
I ## mro I,F,G,E
J ## mro J,G,H,E
K ## mro K,I,F,J,G,H,E
Boat ## mro Boat
DayBoat ## mro DayBoat,Boat
WheelBoat ## mro WheelBoat,Boat
EngineLess ## mro EngineLess,DayBoat,Boat
SmallMultihull ## mro SmallMultihull,DayBoat,Boat
PedalWheelBoat ## mro PedalWheelBoat,EngineLess,DayBoat,WheelBoat,Boat
SmallCatamaran ## mro SmallCatamaran,SmallMultihull,DayBoat,Boat
Pedalo ## mro Pedalo,PedalWheelBoat,EngineLess,SmallCatamaran,SmallMultihull,DayBoat,WheelBoat,Boat
Pane ## mro Pane
ScrollingMixin ## mro ScrollingMixin
EditingMixin ## mro EditingMixin
ScrollablePane ## mro ScrollablePane,Pane,ScrollingMixin
EditablePane ## mro EditablePane,Pane,EditingMixin
EditableScrollablePane ## mro EditableScrollablePane,ScrollablePane,EditablePane,Pane,ScrollingMixin,EditingMixin
A ## mro A
B ## mro B
C ## mro C
X ## mro X,A
Y ## mro Y,A
Z ## mro Z,X,B,Y,A,C
# GENERATED #