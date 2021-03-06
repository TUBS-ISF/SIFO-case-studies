package EmailSystem;

import annotations.low;
import annotations.mut;

public class MainComp {

	public static void main(String[] args) {

		@low @mut Client client1 = Client.createClient("Malte", 5, false); // ok
		client1.setPrivateKey(5); //ok
		@low @mut Client client2 = Client.createClient("Sebastian", 7, true); // ok
		@low @mut Client client3 = Client.createClient("Tim", 3, false); // ok
		
		client1.addAddressbookEntry("client2", "Sebastian"); // ok
		client1.addKeyringEntry(client2, 7);
		client1.addKeyringEntry(client3, 3);
		client2.addKeyringEntry(client1, 5);
		client2.addKeyringEntry(client3, 3);
		client3.addKeyringEntry(client2, 7);
		
		System.out.println("Addressbook entry: " + client1.getAddressBookReceiversForAlias("client2")); // ok

		client2.setForwardReceiver(client3); //ok

		Client.sendEmail(client1, "Sebastian", "Email1", "Hallo, das ist die erste Mail");  // ok
		
		System.out.println("Forwarded by client2: " + client2.getForwardReceiver()); // ok
		
		System.out.println(client1.getAddressBookReceiversForAlias("client2")); // ok

		Client.resetClients(); // ok

	}

}
