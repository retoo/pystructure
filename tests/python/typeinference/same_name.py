class A(object):
    def method(self):
        return 1

A().method() ## type int


class A(object):
    def method(self):
        return "test"

A().method() ## type str
