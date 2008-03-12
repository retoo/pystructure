# This test case breaks if caching is activated (rschuett 2008.03.11)
class A():
    def method(self, x):
        x ## type int|str

A().method(1)
A().method("x")