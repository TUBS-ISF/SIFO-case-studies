package database;

import annotations.highOne;
import annotations.highTwo;
import annotations.low;
import annotations.mut;
import annotations.read;

public class GUI {
	
	@highOne @mut Database databaseOne = new Database("one");
	@highTwo @mut Database databaseTwo = new Database("two");
	
	@low @read public void printEntry(@low String entry) {
		System.out.println(entry);
	}
}
