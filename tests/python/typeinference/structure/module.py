class A(object):
    def __init__(self, argument):
        self.attribute = argument

    def foo(self):
        def bar():
            print "huhu"
        bar()

class B(object):
    class C(object):
        pass

def toplevel():
    pass
