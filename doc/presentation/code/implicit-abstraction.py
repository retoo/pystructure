class Vehicle(object): pass
class Plane(Vehicle): pass
class Car(Vehicle): pass

class JunkProcess(object): 
    def scrap(self, vehicle):
        print "Scrapping " + str(vehicle)
        # ... tear the vehicle apart
        
car = Car()
plane = Plane()

j = JunkProcess()

j.scrap(plane)
j.scrap(car)