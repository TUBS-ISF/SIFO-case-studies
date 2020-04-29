package database;

import annotations.highOne;
import annotations.highTwo;
import annotations.low;

public class GUI {
	
	@highOne Database databaseOne = new Database("one");
	@highTwo Database databaseTwo = new Database("two");
	
	@low public void printEntry(@low String entry) {
		System.out.println(entry);
	}
}
