class Foo(object):
    def bar(arg):
        x = arg
        x # Typ von x?

foo = Foo()
foo.bar(42)