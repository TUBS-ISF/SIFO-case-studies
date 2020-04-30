package paycard;

import annotations.high;
import annotations.low;
import annotations.mut;
import annotations.read;

public class PayCard {
    
    /*@
      @ public instance invariant balance >= 0;
      @ public instance invariant limit > 0;
      @ public instance invariant unsuccessfulOperations >= 0;
      @*/
    
    /*@ spec_public @*/ @high int limit=1000; 
    /*@ spec_public @*/ @high int unsuccessfulOperations; 
    /*@ spec_public @*/ @low int id;
    /*@ spec_public @*/ @high int balance=0; 
    /*@ spec_public @*/ @high @mut protected LogFile log; 
    
    public PayCard(@high int limit, @low int id) {
		balance = 0;
		unsuccessfulOperations=0;
		this.limit = limit;
		this.id = id;
		log = new LogFile();
    }
    
    public PayCard() {
    	balance=0;
		unsuccessfulOperations=0;
    }
    
    /*@
      @ ensures \result.limit==100;
      @*/
    public static @low @mut PayCard createJuniorCard() {
    	return new PayCard(100, 0);
    }
    
    /*@
      @ public normal_behavior
      @ requires amount>0;
      @ {|
      @     requires amount + balance < limit && isValid() == true;
      @     ensures \result == true;
      @     ensures balance == amount + \old(balance);
      @     assignable balance;
      @
      @     also
      @
      @     requires amount + balance >= limit || isValid() == false;
      @     ensures \result == false;
      @     ensures unsuccessfulOperations == \old(unsuccessfulOperations) + 1; 
      @     assignable unsuccessfulOperations;
      @ |}
      @ 	
      @ also
      @
      @ public exceptional_behavior
      @ requires amount <= 0;
      @*/
    @low @mut public @high boolean charge(@low int amount) throws IllegalArgumentException {
		if (amount <= 0) {
			return false;
//		    throw new IllegalArgumentException();
		}
		@high int intermediate = this.balance;
	    @high boolean result = this.balance+amount<this.limit;
	    if (result) { //high check
	        intermediate=this.balance+amount;
	    }
	    this.balance = intermediate;
	    if (result) {
	        return true;
	    } else {
	        return false;
	    }
    }
    
    /*@
      @ public normal_behavior
      @ requires amount>0;
      @ assignable \everything;
      @ ensures balance >= \old(balance);
      @*/
    @low @mut public void chargeAndRecord(@low int amount) {
		@high @mut LogFile logFile = log;
    	if (charge(amount)) {//high guard
//		    try {
			logFile.addRecord(balance);//logFile high
//		    } catch (CardException e){
//			throw new IllegalArgumentException();
//		    }
		}
    }
    
    /*@
      @ public normal_behavior
      @ requires true;
      @ ensures \result == (unsuccessfulOperations<=3); 
      @ assignable \nothing;
      @*/
    @low @read public /*@pure@*/ @high boolean isValid() {
		if (unsuccessfulOperations<=3) {
		    return true;
		} else {
		    return false;
		}
    }
    
    @low @read public @high String infoCardMsg() {//getter of high field
    	return (" Current balance on card is " + balance);
    }
    
    @low @read public @low int getID() {//getter of low field
    	return id;
    }
}
