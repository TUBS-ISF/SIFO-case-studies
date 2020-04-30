package database;

import java.util.HashMap;
import java.util.Map;

import annotations.low;
import annotations.mut;
import annotations.read;

public class Row {

	@low @mut Map<String, String> row;
	
	public Row() {
		row = new HashMap<String, String>();
	}
	
	@low @read public @low String getEntry(@low String key) {
		return row.get(key);
	}
	
	@low @mut public void setEntry(@low String key, @low String value) {
		row.put(key, value);
	}
}
