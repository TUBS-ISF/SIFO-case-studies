package bankaccount;
import annotations.low;

public class Application {
	/*@ invariant account != null; @*/

	@low Account account = new Account();

	/*@ 
	 ensures (account.balance >= 0 ==> account.interest >= \old(account.interest)) 
	  && (account.balance <= 0 ==> account.interest <= \old(account.interest));
	assignable account.interest; @*/
	@low public void nextDay() {
		account.balance.withdraw = 0;
		account.interest += account.calculateInterest();
	}

	/*@ 
	 ensures account.balance == \old(account.balance) + \old(account.interest) 
	  && account.interest == 0;
	assignable account.interest, account.balance; @*/
	@low public void nextYear() {
		account.balance.balance += account.interest;
		account.interest = 0;
	}


}
