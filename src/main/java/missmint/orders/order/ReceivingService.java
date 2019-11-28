package missmint.orders.order;

import missmint.inventory.products.OrderItem;
import missmint.orders.service.MissMintService;
import missmint.orders.service.ServiceManager;
import missmint.time.EntryRepository;
import missmint.time.TimeTableEntry;
import missmint.time.TimeTableService;
import org.salespointframework.accountancy.Accountancy;
import org.salespointframework.accountancy.AccountancyEntry;
import org.salespointframework.catalog.Catalog;
import org.salespointframework.order.OrderManager;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class ReceivingService {
	private final OrderService orderService;
	private final OrderManager<MissMintOrder> orderManager;
	private final TimeTableService timeTableService;
	private final Catalog<OrderItem> itemCatalog;
	private EntryRepository entryRepository;
	private Accountancy accountancy;
	private final ServiceManager serviceManager;

	public ReceivingService(
		OrderService orderService,
		OrderManager<MissMintOrder> orderManager,
		TimeTableService timeTableService,
		Catalog<OrderItem> itemCatalog,
		EntryRepository entryRepository,
		Accountancy accountancy,
		ServiceManager serviceManager
	) {
		this.orderService = orderService;
		this.orderManager = orderManager;
		this.timeTableService = timeTableService;
		this.itemCatalog = itemCatalog;
		this.entryRepository = entryRepository;
		this.accountancy = accountancy;
		this.serviceManager = serviceManager;
	}

	public void receiveOrder(MissMintOrder order) {
		MissMintService service = serviceManager.getService(order);
		Assert.isTrue(orderService.isOrderAcceptable(service), "service must be acceptable");
		itemCatalog.save(order.getItem());

		order.getOrderLines().forEach(orderLine ->
			accountancy.add(new AccountancyEntry(orderLine.getPrice(), orderLine.getProductName()))
		);

		TimeTableEntry entry = timeTableService.createEntry(order);
		order.setExpectedFinished(entry.getDate());
		orderManager.save(order);
		entryRepository.save(entry);

		// TODO use material, finance
	}
}
