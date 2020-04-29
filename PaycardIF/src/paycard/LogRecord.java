package paycard;

import annotations.high;
import annotations.low;
import annotations.read;

public class LogRecord {
    
    /*@ public instance invariant
      @     !empty ==> (balance >= 0 && transactionId >= 0);
      @ public static invariant transactionCounter >= 0;
      @*/
    
    private /*@ spec_public @*/ @low static int transactionCounter = 0;
    
    private /*@ spec_public @*/ @high int balance = -1;
    private /*@ spec_public @*/ @high int transactionId = -1;
    private /*@ spec_public @*/ @high boolean empty = true;
    
    public /*@pure@*/ LogRecord() {}
    
    
    /*@ public normal_behavior
      @   requires balance >= 0;
      @   assignable empty, this.balance, transactionId, transactionCounter;
      @   ensures this.balance == balance && 
      @           transactionId == \old(transactionCounter);
      @*/
    @low public void setRecord(@high int balance)  { //throws CardException
		if(balance < 0){
	//	    throw new CardException();
		} else {
			this.empty = false;
			this.balance = balance;
			this.transactionId = transactionCounter++;
		}
    }
    
    /*@ public normal_behavior
      @   ensures \result == balance;
      @*/
    @low @read public /*@pure@*/ @high int getBalance() {//high getter
	return balance;
    }
    
    /*@ public normal_behavior
      @   ensures \result == transactionId;
      @*/
    @low @read public /*@pure@*/ @high int getTransactionId() {//if called with high receiver, high return value
	return transactionId;
    }
}
