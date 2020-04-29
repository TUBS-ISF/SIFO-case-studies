package EmailSystem;

import java.util.ArrayList;

import annotations.low;

public class AddressBookEntry {
	@low String alias;

	@low ArrayList<String> receivers;

	public AddressBookEntry(@low String alias) {
		super();
		this.alias = alias;
		this.receivers = new ArrayList<String>();
	}

	public void addReceiver(@low String address) { 
		receivers.add(address); 
	}

	public @low String getAlias() {
		return alias;
	}

	public @low ArrayList<String> getReceivers() {
		return receivers;
	}

}
