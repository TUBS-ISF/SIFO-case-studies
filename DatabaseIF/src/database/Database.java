package database;

import java.util.ArrayList;
import java.util.List;

import annotations.low;

public class Database {

	@low String name;
	@low List<Row> rows;
	
	public Database(@low String name) {
		this.name = name;
		rows = new ArrayList<Row>();
	}
	
	@low public @low String getName() {
		return name;
	}

	@low public void setName(@low String name) {
		this.name = name;
	}
	
	@low public @low Row addRow() {
		@low Row row = new Row();
		rows.add(row);
		return row;
	}
	
	@low public void removeRow(@low int index) {
		rows.remove(index);
	}
		
	@low public void setEntry(@low String key, @low String value, @low int rowNumber) {
		Row row;
		if (rowNumber < 0 || rowNumber >= rows.size()) {
			row = addRow();
		} else {
			row = rows.get(rowNumber);
		}
		
		row.setEntry(key, value);
	}
	
	@low public @low String getEntry(@low String key, @low int rowNumber) {
		Row row;
		if (rowNumber < 0 || rowNumber >= rows.size()) {
			return null;
		} else {
			row = rows.get(rowNumber);
		}
		
		return row.getEntry(key);
	}
	
	@low public List<String> getAllEntries(@low String key) {
		@low int i = 0;
		@low List<String> result = new ArrayList<String>();
		while (i < rows.size()) {
			@low String nextValue = rows.get(i).getEntry(key);
			result.add(nextValue);
		}
		return result;
	}
}
