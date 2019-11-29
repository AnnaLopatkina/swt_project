package missmint.finance;

import org.javamoney.moneta.Money;
import org.salespointframework.accountancy.Accountancy;
import org.salespointframework.accountancy.AccountancyEntry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.money.MonetaryAmount;

@Service
public class FinanceService {
	private final Accountancy accountancy;
	private final FinanceService financeService;
	@Value("${general.currency}")
	private String currency;

	public FinanceService(Accountancy accountancy, FinanceService financeService) {
		this.accountancy = accountancy;
		this.financeService = financeService;
	}

	public void createFinanceItemForm(AddFinanceForm form) {
		Assert.notNull(form, "AddFinanceForm cannot be null.");

		financeService.add(form.getDescription(), Money.of(form.getPrice(), currency));
	}

	public Money getSum() {
		Money sum = Money.of(0, currency);
		for (AccountancyEntry entry : accountancy.findAll()) {
			sum = sum.add(entry.getValue());
		}
		return sum;
	}

	public void add(String description, MonetaryAmount value) {
		financeService.add(description, value);
	}

}
