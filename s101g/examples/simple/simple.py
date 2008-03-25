
class Foo(object):
  def set(self, value):
    self.field = value
            
  def get(self):
    return self.field
                    
a = Foo()
a.set("hello world")
z = a.get()
print z
z