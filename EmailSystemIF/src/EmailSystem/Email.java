package EmailSystem;

import annotations.high;
import annotations.low;
import annotations.mut;
import annotations.read;

public class Email {
	protected @low int id;

	protected @low String subject;

	protected @high String body;

	protected @low @mut Client from;

	protected @low String to;

	static @low int emailCounter = 1;

	public Email(@low int id) {
		this.id = id;
	}

	static @low @mut Email createEmail(@low @mut Client from, @low String to, @low String subject, @high String body) {
		@low @mut Email msg = new Email(emailCounter++);
		msg.setEmailFrom(from); 				
		msg.setEmailTo(to); 					
		msg.setEmailSubject(subject); 			
		msg.setEmailBody(body); 				
		return msg; 							
	}

	@low @mut public /*@pure@*/ @high boolean isReadable() {
		if (!isEncrypted())
			return true;
		else
			return false;
	}

	private static void printMail__wrappee__Base(@low @mut Email msg) {
		Util.prompt("ID:  " + msg.getId());
		Util.prompt("FROM: " + msg.getEmailFrom());
		Util.prompt("TO: " + msg.getEmailTo());
		Util.prompt("SUBJECT: " + msg.getEmailSubject());
		//Util.prompt("IS_READABLE " + msg.isReadable()); //high
		//Util.prompt("BODY: " + msg.getEmailBody());		//high
	}

	private static void printMail__wrappee__Encrypt(@low @mut Email msg) {
		printMail__wrappee__Base(msg);
		//Util.prompt("ENCRYPTED " + msg.isEncrypted()); //high

	}

	private static void printMail__wrappee__Sign(@low @mut Email msg) {
		printMail__wrappee__Encrypt(msg);
		//Util.prompt("SIGNED " + msg.isSigned()); //high
		//Util.prompt("SIGNATURE " + msg.getEmailSignKey()); //high
	}

	static void printMail(@low @mut Email msg) {
		printMail__wrappee__Sign(msg);
		//Util.prompt("SIGNATURE VERIFIED " + msg.isSignatureVerified()); //high
	}

	@low @mut public @low @mut Email cloneEmail(@low @mut Email msg) {
//		try {
			return (Email) this.clone();
//		} catch (CloneNotSupportedException e) {
//			throw new Error("Clone not supported");
//		}
	}

	@low @mut public /*@pure@*/ @low @mut Client getEmailFrom() {
		return from;
	}

	@low @read public @low int getId() {
		return id;
	}

	@low @read public @low String getEmailSubject() {
		return subject;
	}

	@low @read public @low String getEmailTo() {
		return to;
	}

	@low @mut public void setEmailBody(@high String value) {
		body = value;
	}

	@low @mut public void setEmailFrom(@low @mut Client value) {
		this.from = value;
	}

	@low @mut public void setEmailSubject(@low String value) {
		this.subject = value;
	}

	@low @mut public void setEmailTo(@low String value) {
		to = value;
	}

	@low @read public @high String getEmailBody() {
		return body;
	}

	protected @high boolean isEncrypted;

	protected @high int encryptionKey;

	@low @read /*@pure@*/ @high boolean isEncrypted() {
		return isEncrypted;
	}

	@low @mut public void setEmailIsEncrypted(@high boolean value) {
		isEncrypted = value; //should encrypt or decrypt the body of the message
	}

	@low @mut public void setEmailEncryptionKey(@high int value) {
		this.encryptionKey = value;
	}

	@low @read public /*@pure@*/ @high int getEmailEncryptionKey() {
		return encryptionKey;
	}

	protected @high boolean signed;

	protected @high int signkey;

	@low @mut public void setEmailIsSigned(@high boolean value) {
		signed = value;
	}

	@low @mut public void setEmailSignKey(@high int value) {
		signkey = value;
	}

	@low @read public @high boolean isSigned() {
		return signed;
	}

	@low @read public /*@pure@*/ @high int getEmailSignKey() {
		return signkey;
	}

	@low @read protected @high boolean isSignatureVerified;

	@low @read  public /*@pure@*/ @high boolean isSignatureVerified() {
		return isSignatureVerified;
	}

	@low @mut public void setIsSignatureVerified(@high boolean value) {
		this.isSignatureVerified = value;
	}
}
