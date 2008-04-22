class Container(object):
    def set(self, element):
        self.element = element
    
    def get(self):
        return self.element

c1 = Container()
c1.element = 42
c1.element ## type int

c2 = Container()
c2.element = "hi"
c2.element ## type str

# doesn't yet work

c3 = Container()
c3.set(3.14)
c3.get() # # type float

c4 = Container()
c4.set(True)
c4.get() # # type bool


class PrefilledContainer(object):
    def __init__(self):
        self.element = 3.14

    def modify(self):
        self.element = 3

pc = PrefilledContainer()
pc.modify()
pc.element ## type float|int
