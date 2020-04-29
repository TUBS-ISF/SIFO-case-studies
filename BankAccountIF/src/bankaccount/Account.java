package bankaccount;
import annotations.high;
import annotations.low;

public class Account {
	final @high int OVERDRAFT_LIMIT  = -5000;
	
	/*@ invariant withdraw >= DAILY_LIMIT; @*/

	final @high static int DAILY_LIMIT = -1000;

	final @low static int INTEREST_RATE = 2;

	@high int interest = 0;

	/*@ invariant balance >= OVERDRAFT_LIMIT; @*/

	@high Balance balance = new Balance(0, 0);

	/*@ 
	 ensures balance == 0;
	assignable balance; @*/
	Account() {
	}

	/*@ 
	 ensures (balance >= 0 ==> \result >= 0) 
	   && (balance <= 0 ==> \result <= 0); @*/
	 @low public /*@pure@*/  @high int calculateInterest() {
		return balance.balance * INTEREST_RATE / 36500;
	}

	/*@ 
	 requires daysLeft >= 0;
	ensures calculateInterest() >= 0 ==> \result >= interest; @*/
	 @low public /*@pure@*/  @high int estimatedInterest(@low int daysLeft) {
		return interest + daysLeft * calculateInterest();
	}

	/*@ 
	 requires amount >= 0;
	ensures balance >= amount <==> \result;
	assignable \nothing; @*/
	@high public @high boolean credit(@low int amount) {
		return balance.balance >= amount;
	}

	private @low boolean lock = false;

	/*@ 
	 ensures this.lock;
	assignable lock; @*/
	@low public void lock() {
		lock = true;
	}

	/*@ 
	 ensures !this.lock;
	assignable lock; @*/
	@low public void unLock() {
		lock = false;
	}

	/*@ 
	 ensures \result == this. lock;
	assignable \nothing; @*/
	@low public @low boolean isLocked() {
		return lock;
	}


}
