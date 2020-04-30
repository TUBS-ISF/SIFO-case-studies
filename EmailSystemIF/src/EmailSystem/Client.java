package EmailSystem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import annotations.high;
import annotations.imm;
import annotations.low;
import annotations.mut;
import annotations.read;

public class Client {

	protected @low int id;

	protected @low String name;

	@low @read public @low int getId() {
		return id;
	}

	private Client(@low int id, @low String name, @high int privateKey, @low boolean autoResponse) {
		this.id = id;
		this.name = name;

		setPrivateKey(privateKey);
		setAutoResponse(autoResponse);
	}

	public static @low Client createClient(@low String name, @high int privateKey, @low boolean autoResponse) {
		@low @mut Client client = new Client(clientCounter++, name, privateKey, autoResponse);
		clients[client.getId()] = client; // clients[] low
		return client;
	}

	static void deliver(@low @mut Client client, @low @mut Email msg) {
		Util.prompt("mail delivered\n");
	}


	/*@ 
	 ensures msg.isEncrypted() ==> encryptedMails.contains(msg);
	 assignable \nothing; @*/
	private static void incoming__wrappee__AutoResponder(@low @mut Client client, @low @mut Email msg) {
		deliver(client, msg);
		if (client.isAutoResponse()) {
			autoRespond(client, msg);
		}
	}

	/*@ 
	 ensures msg.isEncrypted() ==> encryptedMails.contains(msg);
	 assignable \nothing; @*/
	private static void incoming__wrappee__Forward(@low @mut Client client, @low @mut Email msg) {
		incoming__wrappee__AutoResponder(client, msg);
		@low @mut Client receiver = client.getForwardReceiver();
		if (receiver != null) {
			msg.setEmailTo(receiver.getName());
			forward(client, msg);
			incoming(receiver, msg);
		}
	}

	/*@ 
	 ensures msg.isEncrypted() ==> encryptedMails.contains(msg);
	 assignable \nothing; @*/
	private static void incoming__wrappee__Verify(@low @mut Client client, @low @mut Email msg) {
		verify(client, msg);
		incoming__wrappee__Forward(client, msg);
	}

	/*@ 
	 ensures msg.isEncrypted() ==> encryptedMails.contains(msg);
	 assignable \nothing; @*/
	static void incoming(@low @mut Client client, @low @mut Email msg) {

		@high int privkey = client.getPrivateKey(); //receiver key
		@high boolean intermediateBool = msg.isEncrypted();
		@high int intermediateInt = msg.getEmailEncryptionKey();
		if (privkey != 0) {
			if (msg.isEncrypted() && isKeyPairValid(msg.getEmailEncryptionKey(), privkey)) { //high context, only assignments to high
				intermediateBool = false;
				intermediateInt = 0;
			}
		}
		msg.setEmailIsEncrypted(intermediateBool);
		msg.setEmailEncryptionKey(intermediateInt);
		incoming__wrappee__Verify(client, msg);
	}

	/*@ 
	 requires !msg.isSignatureVerified(); 
	 assignable \nothing; @*/
	static void mail(@low @mut Client client, @low @mut Email msg) {
		Util.prompt("mail sent");
	}

	private static void outgoing__wrappee__Base(@low @mut Client client, @low @mut Email msg) {
		msg.setEmailFrom(client);
		mail(client, msg);
	}

	private static void outgoing__wrappee__Encrypt(@low @mut Client client, @low @mut Email msg) {

		@low @mut Client receiver = getClientByAdress(msg.getEmailTo());
		@low int pubkey = client.getKeyringPublicKeyByClient(receiver);
		if (pubkey != 0) { // low
			msg.setEmailEncryptionKey(pubkey); //pubkey promoted to high
			msg.setEmailIsEncrypted(true);
			Util.prompt("Encrypted Mail " + msg.getId());
		}

		outgoing__wrappee__Base(client, msg);
	}

	private static void outgoing__wrappee__Addressbook(@low @mut Client client, @low @mut Email msg) {
		@low @mut List<String> aliasReceivers = client.getAddressBookReceiversForAlias(msg.getEmailTo());
		if (!aliasReceivers.isEmpty()) {

			for (int i = 1; i < aliasReceivers.size(); i++) {
				@low String receiverAddress = aliasReceivers.get(i);
				msg.setEmailTo(receiverAddress);
				outgoing(client, msg);
				incoming(Client.getClientByAdress(receiverAddress), msg);
			}
			msg.setEmailTo(aliasReceivers.get(0));
			outgoing__wrappee__Encrypt(client, msg);
		} else {

			outgoing__wrappee__Encrypt(client, msg);
		}
	}

	static void outgoing(@low @mut Client client, @low @mut Email msg) {
		sign(client, msg);
		outgoing__wrappee__Addressbook(client, msg);
	}

	public static @low int sendEmail(@low @mut Client sender, @low String receiverAddress, @low String subject, @high String body) {
		@low @mut Email email = Email.createEmail(sender, receiverAddress, subject, body);
		Util.prompt("sending Mail " + email.getId());
		outgoing(sender, email);
		@low @mut Client receiver = Client.getClientByAdress(email.getEmailTo());
		if (receiver != null) {
			incoming(receiver, email);
		} else {
//			throw new IllegalArgumentException("Receiver " + receiverAddress + " Unknown");
		}
		return 0;
	}

	@low @read public @low String getName() {
		return name;
	}

	static @low int clientCounter = 1;

	static @low @mut Client[] clients = new Client[4];

	static @low @mut Client getClientById(@low int id) {
		return clients[id];
	}

	static @low @mut Client getClientByAdress(@low String address) {
		for (int i = 0; i < clients.length; i++) {
			if (clients[i] != null && clients[i].getName().equals(address)) {
				return clients[i];
			}
		}
		return null;
//		throw new IllegalArgumentException("Receiver " + address + " Unknown");
	}

	@low @mut public static void resetClients() {
		clientCounter = 1;
		for (int i = 0; i < clients.length; i++) {
			clients[i] = null;
		}
	}

	@Override
	@low @read public @low String toString() {
		return name;
	}

	protected @low @mut ArrayList<KeyringEntry> keyring = new ArrayList<KeyringEntry>();

	protected @high int privateKey;

	@low @mut public void setPrivateKey(@high int privateKey) { 
		this.privateKey = privateKey;
	}

	@low @read public /*@pure@*/ @high int getPrivateKey() { 
		return privateKey;
	}

	@low @mut public void addKeyringEntry(@low @mut Client client, @low int publicKey) {
		this.keyring.add(new KeyringEntry(client, publicKey));
	}

	@low @read public /*@pure@*/ @low int getKeyringPublicKeyByClient(@low @read Client client) {
		for (@low @mut KeyringEntry e : keyring) {
			if (e.getKeyOwner().equals(client)) {
				return e.getPublicKey();
			}
		}
		return 0;
	}

	public /*@pure@*/ static @high boolean isKeyPairValid(@high int publicKey, @high int privateKey) {
		//Util.prompt("keypair valid " + publicKey + " " + privateKey);
		if (publicKey == 0 || privateKey == 0)
			return false;
		return privateKey == publicKey; //should be a secure validation. This is just an example.
	}

	static class KeyringEntry {
		private @low @mut Client keyOwner;

		private @low int publicKey;

		public KeyringEntry(@low @mut Client keyOwner, @low int publicKey) {
			super();
			this.keyOwner = keyOwner;
			this.publicKey = publicKey;
		}

		@low @read public @low @read Client getKeyOwner() {
			return keyOwner;
		}

		@low @read public @low int getPublicKey() {
			return publicKey;
		}

	}

	///*@model@*/ Set<Email> encryptedMails = new HashSet<Email>(2);

	///*@model@*/ Set<Email> unEncryptedMails = new HashSet<Email>(2);

	protected @low boolean autoResponse;

	@low @mut private void setAutoResponse(@low boolean autoResponse) {
		this.autoResponse = autoResponse;
	}

	@low @read public @low boolean isAutoResponse() {
		return autoResponse;
	}

	/*@ 
	 requires !msg.isReadable();
	 assignable \nothing; @*/
	static void autoRespond(@low @mut Client client, @low @mut Email msg) {
		Util.prompt("sending autoresponse\n");
		@low @mut Client sender = msg.getEmailFrom();
		msg.setEmailTo(sender.getName());
		outgoing(client, msg);
		incoming(sender, msg);
	}

	protected @low @mut ArrayList<AddressBookEntry> addressbook = new ArrayList<AddressBookEntry>();

	@low @mut public @low @mut List<String> getAddressBookReceiversForAlias(@low String alias) {
		for (@low @mut AddressBookEntry e : addressbook) {
			if (e.getAlias().equals(alias)) {
				return e.getReceivers();
			}
		}
		return Collections.emptyList();
	}

	@low @mut public void addAddressbookEntry(@low String alias, @low String receiver) {
		for (@low @mut AddressBookEntry e : addressbook) { 
			if (e.getAlias().equals(alias)) {
				e.addReceiver(receiver);
				return;
			}
		}
		@low @mut AddressBookEntry newEntry = new AddressBookEntry(alias);
		newEntry.addReceiver(receiver);
		addressbook.add(newEntry);
	}

	static void sign(@low @mut Client client, @low @mut Email msg) { 
		@high int privkey = client.getPrivateKey();
		@high boolean intermediateBool = msg.isSigned();
		@high int intermediateInt = msg.getEmailSignKey();
		if (privkey == 0) {
			
		} else {
			intermediateBool = true;
			intermediateInt = privkey;
		}
		msg.setEmailIsSigned(intermediateBool);
		msg.setEmailSignKey(intermediateInt);
	}

	///*@model@*/ Set<Email> signedMails = new HashSet<Email>(2);

	/*@ 
	 requires !msg.isReadable(); 
	 assignable \nothing; @*/
	static void verify(@low @mut Client client, @low @mut Email msg) { //receiver of message
		@low int pubkey = client.getKeyringPublicKeyByClient(msg.getEmailFrom());
		@high boolean intermediateBool = msg.isSignatureVerified();
		if (pubkey != 0 && isKeyPairValid(msg.getEmailSignKey(), pubkey)) { //high guard, only high assignments
			intermediateBool = true;
		}
		msg.setIsSignatureVerified(intermediateBool);
	}

	protected @low @mut Client forwardReceiver;

	@low @mut public void setForwardReceiver(@low @mut Client forwardReceiver) {
		this.forwardReceiver = forwardReceiver;
	}

	@low @read public @low @read Client getForwardReceiver() {
		return forwardReceiver;
	}

	static void forward(@low @mut Client client, @low @mut Email msg) {
		Util.prompt("Forwarding message.\n");
		Email.printMail(msg);
		outgoing(client, msg);
	}
}
