def a(x):
    x ## type int|str
    if random():
        return b(x)
    else:
        return x
        
def b(x):
    x ## type int|str
    if random():
        return a(x)
    else:
        return x


a("x") ## type str
b(1)   ## type int


def c():
    if random():
        return c()
    else:
        return ""
    
c() ## type str
