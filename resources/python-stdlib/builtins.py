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


def range(arg1, arg2=None, arg3=None):
    return [1]
