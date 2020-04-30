package bankaccount;
import annotations.low;
import annotations.mut;

public class Application {
	/*@ invariant account != null; @*/

	@low @mut Account account = new Account();

	/*@ 
	 ensures (account.balance.balance >= 0 ==> account.interest >= \old(account.interest)) 
	  && (account.balance.balance <= 0 ==> account.interest <= \old(account.interest));
	assignable account.interest; @*/
	@low @mut public void nextDay() {
		account.balance.withdraw = 0;
		account.interest += account.calculateInterest();
	}

	/*@ 
	 ensures account.balance.balance == \old(account.balance.balance) + \old(account.interest) 
	  && account.interest == 0;
	assignable account.interest, account.balance; @*/
	@low @mut public void nextYear() {
		account.balance.balance += account.interest;
		account.interest = 0;
	}


}
