package bankaccount;
import annotations.high;
import annotations.low;
import annotations.mut;

public class Transaction {
	/*@ 
	 requires destination != null && source != null;
	  requires source != destination;
	  ensures \result ==> (\old(destination.balance) + amount == destination.balance);
	  ensures \result ==> (\old(source.balance) - amount == source.balance);
	  ensures !\result ==> (\old(destination.balance) == destination.balance);
	  ensures !\result ==> (\old(source.balance) == source.balance);
	  assignable \nothing; @*/
	@low @mut public @high boolean transfer(@low @mut Account source, @low @mut Account destination, @low int amount) {
		@high boolean result = true;
		if (!lock(source, destination)) {
			result = false;
		} else {
			@high @mut Balance sourceBalance = source.balance;
			@high @mut Balance destinationBalance = destination.balance;
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
	private static synchronized @low boolean lock(@low @mut Account source, @low @mut Account destination) {
		if (source.isLocked()) return false;
		if (destination.isLocked()) return false;
		source.lock();
		destination.lock();
		return true;
	}


}
