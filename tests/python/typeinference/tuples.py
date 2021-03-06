a, b = 1, 3.14

a ## type int
b ## type float

c = d, (e, f) = a, ("test", True)

c ## type tuple
d ## type int
e ## type str
f ## type bool


g = (1, 3.1)
h, i = g

g ## type tuple
h ## type int
i ## type float


def func():
    return 1.1, "str"

j = func()
k, l = func()
j ## type tuple
k ## type float
l ## type str


# Test that only the tuple elements of returns with the right number of elements are considered
def bad(a):
    if a:
        return 1, 1.2, "s"
    else:
        return 1, 2

m, n, o = bad(True)
m ## type int
n ## type float|int
o ## type str


p = (42, 3.14, "test")
p[0] ## type int
p[1] ## type float
p[2] ## type str
p[-1] ## type str
p[-2] ## type float
p[-3] ## type int


def return_tuple_of_arguments(arg1, arg2):
    return (arg1, arg2)

return_tuple_of_arguments(1, 2)[0] ## type int
return_tuple_of_arguments(3.14, 1.1)[0] ## type float
