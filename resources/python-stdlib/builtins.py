class object():
    pass


class _iterator(object):

    def __init__(self, element):
        self.element = element
    
    def __iter__(self):
        return self
    
    def next(self):
        return self.element


class list(object):

    def append(self, element):
        self.element = element

    def count(self, element):
        return 1

    def extend(self, other):
        self.element = other[0]

    def index(self, element):
        return 1

    def insert(self, index, element):
        self.element = element

    def pop(self):
        return self.element

    def remove(self):
        pass

    def reverse(self):
        pass

    def sort(self):
        pass

    def __getitem__(self, index):
        return self.element

    def __getslice__(self, i, j):
        return self

    def __iter__(self):
        return _iterator(self.element)


class dict(object):

    def __setitem__(self, key, value):
        self.dict_key = key
        self.dict_value = value

    def __getitem__(self, key):
        return self.dict_value
