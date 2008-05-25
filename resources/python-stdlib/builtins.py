class object():
    pass


class list(object):

    def append(self, element):
        self._list_element = element

    def count(self, element):
        return 1

    def extend(self, other):
        self._list_element = other[0]

    def index(self, element):
        return 1

    def insert(self, index, element):
        self._list_element = element

    def pop(self):
        return self._list_element

    def remove(self):
        pass

    def reverse(self):
        pass

    def sort(self):
        pass

    def __getitem__(self, index):
        return self._list_element

    def __getslice__(self, i, j):
        return self

    def __iter__(self):
        # The correct solution would be to do this:
        #   return _iterator(self._list_element)
        # But the problem with this is that the iterator will be the same for
        # different lists, because the constructor call is the same, which is
        # bad because the element types would be merged for all lists.
        return self

    def next(self):
        return self._list_element


class dict(object):

    def __setitem__(self, key, value):
        self._dict_key = key
        self._dict_value = value

    def __getitem__(self, key):
        return self._dict_value


class set(object):
    
    def __init__(self, iterable):
        self._set_element = iterable.__iter__().next()

    def add(self, element):
        self._set_element = element

    def clear(self):
        pass

    def copy(self):
        return self

    def difference(self, other):
        return self

    def difference_update(self, other):
        pass

    def discard(self):
        pass

    def intersection(self, other):
        return self

    def intersection_update(self, other):
        pass

    def issubset(self, other):
        return False

    def issuperset(self, other):
        return False

    def pop(self):
        return self._set_element

    def remove(self, element):
        pass

    def symmetric_difference(self, other):
        # Doesn't work yet because constructor call is the same for all sets
        # s = set()
        # s.add(self.pop())
        # s.add(other.pop())
        return self

    def symmetric_difference_update(self, other):
        self.add(other.pop())

    def union(self, other):
        # Doesn't work yet because constructor call is the same for all sets
        # s = set()
        # s.add(self.pop())
        # s.add(other.pop())
        return self

    def update(self, other):
        self.add(other.pop())


class range(object):

    def __init__(self, arg1, arg2=None, arg3=None):
        pass

    def __iter__(self):
        return self

    def next(self):
        return 1


class enumerate(object):

    def __init__(self, iterable):
        self._enumerate_element = iterable.__iter__().next()

    def __iter__(self):
        return self

    def next(self):
        return (1, self._enumerate_element)
