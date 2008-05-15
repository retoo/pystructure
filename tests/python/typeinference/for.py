a = list()
a.append(1)
a.append(2)

for element in a:
    # type is float|int for now, because the iterator is constructed in
    # __iter__ and so has the same constructor call as other list iterators.
    element ## type float|int
