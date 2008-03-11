#def foo(x):
#    x # FIXME I BREAK type int|str
#    return x
#foo(1)
#foo("x")


# This test case breaks if caching is activated (rschuett 2008.03.11)
class A():
    def method(self, x):
        x ## type int|str

A().method(1)
A().method("x")