# This test case breaks if caching is activated (rschuett 2008.03.11)
class A():
    def method(self, x):
        x ## type int|str

A().method(1)
A().method("x")


# The following test breaks because of the caching in DefinitionTypeEvaluator.

def func(arg):
    x = arg
    x # # type float|int
    return x

func(42) # # type int
func(3.14) # # type float
