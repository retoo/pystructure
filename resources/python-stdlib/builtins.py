class object():
    pass

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
