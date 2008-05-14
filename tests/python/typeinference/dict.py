a = dict()
a["str"] = 1
a["str"] ## type int

b = dict()
b[1], b[2] = 42, 3.14
b[0] ## type float|int

c = {}
c[1] = 3.14
c[1] ## type float

d = {1: 3.14, 2: "test"}
d[1] ## type float|str
