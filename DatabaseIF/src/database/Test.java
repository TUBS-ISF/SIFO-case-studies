package database;

import annotations.high;
import annotations.highOne;
import annotations.low;
import annotations.mut;

public class Test {

	static void test() {
		@low @mut GUI gui = new GUI();
		gui.databaseOne.setEntry("person", "hans", -1);
		gui.databaseTwo.setEntry("person", "peter", -1);
		@highOne String name = gui.databaseOne.getEntry("person", 0);
		gui.databaseTwo.setEntry("person", name, 0); //not typable as name is highOne and databaseTwo is highTwo
													 //The entries of both databases can not interfere
		gui.printEntry(declassify(name));
	}

	private static @low String declassify(@high String value) {
		return value;
	}
}
