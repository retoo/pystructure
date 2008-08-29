from goods import Apple, Banana
 
class Container(object):
    def set(c): self.content = c
    def get(): return self.content
 
apples = Container()
bananas = Container()
 
apples.set(Apple())
bananas.set(Banana())
 
a = apples.get()
b = bananas.get()