package database;

import java.util.HashMap;
import java.util.Map;

import annotations.low;

public class Row {

	@low Map<String, String> row;
	
	public Row() {
		row = new HashMap<String, String>();
	}
	
	@low public @low String getEntry(@low String key) {
		return row.get(key);
	}
	
	@low public void setEntry(@low String key, @low String value) {
		row.put(key, value);
	}
}
