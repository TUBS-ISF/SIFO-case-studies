package database;

import java.util.ArrayList;
import java.util.List;

import annotations.low;
import annotations.mut;
import annotations.read;

public class Database {

	@low String name;
	@low @mut List<Row> rows;
	
	public Database(@low String name) {
		this.name = name;
		rows = new ArrayList<Row>();
	}
	
	@low @read public @low String getName() {
		return name;
	}

	@low @mut public void setName(@low String name) {
		this.name = name;
	}
	
	@low @mut public @low Row addRow() {
		@low @mut Row row = new Row();
		rows.add(row);
		return row;
	}
	
	@low @mut public void removeRow(@low int index) {
		rows.remove(index);
	}
		
	@low @mut public void setEntry(@low String key, @low String value, @low int rowNumber) {
		@low @mut Row row;
		if (rowNumber < 0 || rowNumber >= rows.size()) {
			row = addRow();
		} else {
			row = rows.get(rowNumber);
		}
		
		row.setEntry(key, value);
	}
	
	@low @read public @low String getEntry(@low String key, @low int rowNumber) {
		@low @read Row row;
		if (rowNumber < 0 || rowNumber >= rows.size()) {
			return null;
		} else {
			row = rows.get(rowNumber);
		}
		
		return row.getEntry(key);
	}
	
	@low @read public @low @read List<String> getAllEntries(@low String key) {
		@low int i = 0;
		@low @mut List<String> result = new ArrayList<String>();
		while (i < rows.size()) {
			@low String nextValue = rows.get(i).getEntry(key);
			result.add(nextValue);
		}
		return result;
	}
}
