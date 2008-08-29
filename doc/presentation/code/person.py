# person.py

class Person(object):
    def __init__(self, name, home):
        self.name = name
        self.location = home

def main():
    from vehicles import Bicycle
    from locations import stockholm, oslo
    annika = Person("Annika", oslo)
    bicycle = Bicycle(oslo)
    bicycle.drive(annika, stockholm)