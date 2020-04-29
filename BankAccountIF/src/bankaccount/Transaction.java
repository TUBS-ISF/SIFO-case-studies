package bankaccount;
import annotations.high;
import annotations.low;

public class Transaction {
	/*@ 
	 requires destination != null && source != null;
	  requires source != destination;
	  ensures \result ==> (\old(destination.balance) + amount == destination.balance);
	  ensures \result ==> (\old(source.balance) - amount == source.balance);
	  ensures !\result ==> (\old(destination.balance) == destination.balance);
	  ensures !\result ==> (\old(source.balance) == source.balance);
	  assignable \nothing; @*/
	@low public @high boolean transfer(@low Account source, @low Account destination, @low int amount) {
		@high boolean result = true;
		if (!lock(source, destination)) {
			result = false;
		} else {
			@high Balance sourceBalance = source.balance;
			@high Balance destinationBalance = destination.balance;
			if (amount <= 0) {
				result = false;
			} else if (!sourceBalance.update(amount * -1, Account.DAILY_LIMIT, source.OVERDRAFT_LIMIT)) {
				result = false;
			} else if (!destinationBalance.update(amount, Account.DAILY_LIMIT, destination.OVERDRAFT_LIMIT)) {
				result = false;
				sourceBalance.undoUpdate(amount * -1, Account.DAILY_LIMIT, source.OVERDRAFT_LIMIT);
			} else {
				result = true;
			}
			source.unLock();
			destination.unLock();
		}
		return result;
	}

	private static @low boolean declassify(@high boolean result) {
		return result;
	}

	/*@ 
	 requires destination != null && source != null;
	  requires source != destination;
	  ensures \result ==> source.isLocked() && destination.isLocked();
	  assignable \nothing; @*/
	@low private @low static synchronized boolean lock(@low Account source, @low Account destination) {
		if (source.isLocked()) return false;
		if (destination.isLocked()) return false;
		source.lock();
		destination.lock();
		return true;
	}


}
