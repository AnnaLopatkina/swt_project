package missmint.finance;

import org.salespointframework.accountancy.Accountancy;
import org.salespointframework.accountancy.AccountancyEntry;
import org.springframework.stereotype.Service;

@Service
public class FinanceService {
	//private final FinanceRepository financeRepository;
	private final Accountancy accountancy;

	public FinanceService(Accountancy accountancy) {
		//this.financeRepository = financeRepository;
		this.accountancy = accountancy;
	}

	public Iterable <AccountancyEntry> showAllFinance(){
		return accountancy.findAll();
	}

	public void add(AccountancyEntry accountancyEntry){
		accountancy.add(accountancyEntry);
		//financeRepository.save(accountancyEntry);
	}

}
