public class Bicycle {
	public void drive(Person person, Location location) {
		this.setLocation(location);
		person.setLocation(location);
	}
}