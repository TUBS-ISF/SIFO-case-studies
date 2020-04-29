package paycard;

import annotations.high;
import annotations.low;
import annotations.read;

public class LogFile {
    
    /*@ public invariant
      @    logArray.length == logFileSize && 
      @    currentRecord < logFileSize && 
      @    currentRecord >= 0 && \nonnullelements(logArray);
      @*/
    private /*@ spec_public @*/ @high static int logFileSize = 3;
    private /*@ spec_public @*/ @high int currentRecord;
    private /*@ spec_public @*/ @high LogRecord[] logArray = new LogRecord[logFileSize];
    
    public /*@pure@*/ LogFile() {
	@high int i=0;
	while(i<logArray.length){//high guard
	    logArray[i++] = new LogRecord();
	}
	currentRecord = 0;
    }
    
    
    /*@ public normal_behavior
      @    requires balance >= 0;
      @    ensures \old(currentRecord) + 1 != logFileSize ? 
      @        currentRecord == \old(currentRecord) + 1 : currentRecord == 0;
      @    ensures logArray[currentRecord].balance == balance;
      @*/
    @low public void addRecord(@high int balance) {//high method high balance //throws CardException
	currentRecord++;
	@high int tmpRecord = currentRecord;
	if (currentRecord == logFileSize) { //high guard
		tmpRecord = 0;
	}
	currentRecord = tmpRecord;
	logArray[currentRecord].setRecord(balance);
    }
    
    
    /*@ public normal_behavior
      @    ensures (\forall int i; 0 <= i && i<logArray.length;
      @             logArray[i].balance <= \result.balance);
      @    diverges true;
      @ */
    @low @read public /*@pure@*/ @high LogRecord getMaximumRecord(){//return high
	@high LogRecord max = logArray[0];
	@high int i=1;
	/*@ loop_invariant
	  @   0<=i && i <= logArray.length 
	  @   && max!=null &&
	  @   (\forall int j; 0 <= j && j<i; 
	  @     max.balance >= logArray[j].balance);
	  @ assignable max, i;
	  @*/
	while(i<logArray.length){//high guard
	    @high LogRecord lr = logArray[i++];
	    if (lr.getBalance() > max.getBalance()){ //high guard
		max = lr;//high variables
	    }
	}
	return max;
    }
}
