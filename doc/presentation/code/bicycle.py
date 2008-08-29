# vehicles.py

class Bicycle(object):
    def __init__(self, location):
        self.location = location

    def drive(self, person, location):
        self.location = location
        person.location = location
