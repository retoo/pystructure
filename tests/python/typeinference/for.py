a = list()
a.append(1)
a.append(2)

# type is float|int for now, because the iterator is constructed in __iter__
# and so has the same constructor call as other list iterators.
a.__iter__().next() ## type float|int

for element in a:
    element # # type int
