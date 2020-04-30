package EmailSystem;

import java.util.ArrayList;

import annotations.low;
import annotations.mut;
import annotations.read;

public class AddressBookEntry {
	@low String alias;

	@low @mut ArrayList<String> receivers;

	public AddressBookEntry(@low String alias) {
		super();
		this.alias = alias;
		this.receivers = new ArrayList<String>();
	}

	@low @mut public void addReceiver(@low String address) { 
		receivers.add(address); 
	}

	@low @read public @low String getAlias() {
		return alias;
	}

	@low @mut public @low @mut ArrayList<String> getReceivers() {
		return receivers;
	}

}
