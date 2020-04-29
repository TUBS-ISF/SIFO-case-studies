package bankaccount;
import annotations.high;
import annotations.low;

public class Balance {

	@low int balance;
	@low int withdraw;
	
	Balance(@low int balance, @low int withdraw) {
		this.balance = balance;
		this.withdraw = withdraw;
	}
	
	/*@ 
	 ensures (!\result ==> balance == \old(balance)) 
	  && (\result ==> balance == \old(balance) + x); 
	assignable balance; @*/
	@high @high boolean updateBalance (@low int x, @high int OVERDRAFT_LIMIT) {
		@high int newBalance = balance + x;
		@high boolean result = true;
		if (newBalance < OVERDRAFT_LIMIT) {
			newBalance = balance;
			result = false;
		}
		balance = newBalance;
		return result;
	}
	
	/*@ 
	 ensures (!\result ==> balance == \old(balance)) 
	  && (\result ==> balance == \old(balance) - x);
	assignable balance; @*/
	@high @high boolean undoUpdateBalance (@low int x, @high int OVERDRAFT_LIMIT) {
		@high int newBalance = balance - x;
		@high boolean result = true;
		if (newBalance < OVERDRAFT_LIMIT) {
			newBalance = balance;
			result = false;
		}
		balance = newBalance;
		return result;
	}
	
	/*@ 
	 ensures (!\result ==> withdraw == \old(withdraw)) 
	  && (\result ==> withdraw <= \old(withdraw));
	assignable withdraw; @*/
	@high public @high boolean update(@low int x, @high int DAILY_LIMIT, @high int OVERDRAFT_LIMIT) {
		@high int newWithdraw = withdraw;
		@high int oldWithdraw = withdraw;
		@high boolean result = true;
		if (x < 0)  {
			newWithdraw += x;
			if (newWithdraw < DAILY_LIMIT) {
				newWithdraw = oldWithdraw;
				result = false;
			}
		}
		if (newWithdraw >= DAILY_LIMIT && !updateBalance(x, OVERDRAFT_LIMIT)) {
			newWithdraw = oldWithdraw;
			result =  false;
		}
		withdraw = newWithdraw;
		return result;
	}

	/*@ 
	 ensures (!\result ==> withdraw == \old(withdraw)) 
	  && (\result ==> withdraw >= \old(withdraw));
	assignable withdraw; @*/
	@high public @high boolean undoUpdate(@low int x, @high int DAILY_LIMIT, @high int OVERDRAFT_LIMIT) {
		@high int newWithdraw = withdraw;
		@high int oldWithdraw = withdraw;
		@high boolean result = true;
		if (x < 0)  {
			newWithdraw -= x;
			if (newWithdraw < DAILY_LIMIT) {
				newWithdraw = oldWithdraw;
				result = false;
			}
		}
		if (newWithdraw >= DAILY_LIMIT && !undoUpdateBalance(x, OVERDRAFT_LIMIT)) {
			newWithdraw = oldWithdraw;
			result =  false;
		}
		withdraw = newWithdraw;
		return result;
	}
}
