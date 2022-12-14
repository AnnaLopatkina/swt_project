package missmint.orders.controllers;

import missmint.inventory.products.OrderItem;
import missmint.orders.order.MissMintOrder;
import missmint.orders.order.OrderState;
import missmint.orders.service.MissMintService;
import org.junit.jupiter.api.Test;
import org.salespointframework.catalog.Catalog;
import org.salespointframework.order.OrderManager;
import org.salespointframework.time.BusinessTime;
import org.salespointframework.useraccount.Password;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Locale;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class OrderOverviewControllerTests {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private OrderManager<MissMintOrder> orderManager;
	@Autowired
	private UserAccountManager userAccountManager;
	@Autowired
	private BusinessTime time;
	@Autowired
	private Catalog<MissMintService> serviceCatalog;
	@Autowired
	private Catalog<OrderItem> itemCatalog;

	@Test
	void unauthenticated() throws Exception {
		mvc.perform(get("/orders"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrlPattern("**/login"));

		mvc.perform(get("/orders/detail/123"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrlPattern("**/login"));
	}

	@Test
	@WithMockUser()
	void noOrders() throws Exception {
		mvc.perform(get("/orders"))
			.andExpect(status().isOk());
	}

	@Test
	@WithMockUser()
	void orders() throws Exception {
		MissMintOrder order = createOrder();

		mvc.perform(get("/orders"))
			.andExpect(status().isOk())
			.andExpect(content().string(containsString(String.valueOf(order.getId()))))
			.andExpect(content().string(containsString(order.getCustomer())))
			.andExpect(content().string(containsString(String.format("/orders/detail/%s", order.getId()))))
			.andExpect(content().string(not(containsString(String.format("/orders/pickup/%s", order.getId())))))
			.andExpect(content().string(containsString("encrypted message")));
	}


	@Test
	@WithMockUser()
	void ordersCanBePickedUp() throws Exception {
		MissMintOrder order = createOrder();

		LocalDate now = time.getTime().toLocalDate();
		order.setInbound(now.minusWeeks(1));
		order.setOrderState(OrderState.FINISHED);

		orderManager.save(order);

		mvc.perform(get("/orders/").locale(Locale.ROOT))
			.andExpect(status().isOk())
			.andExpect(content().string(containsString(String.valueOf(order.getId()))))
			.andExpect(content().string(containsString(String.format("/orders/pickup/%s", order.getId()))));
	}

	@Test
	@WithMockUser()
	void orderDetailNoOrder() throws Exception {
		mvc.perform(get("/orders/detail/123"))
			.andExpect(status().is5xxServerError());
	}

	@Test
	@WithMockUser()
	void orderDetail() throws Exception {
		MissMintOrder order = createOrder();

		mvc.perform(get(String.format("/orders/detail/%s", order.getId())).locale(Locale.ROOT))
			.andExpect(status().isOk())
			.andExpect(content().string(containsString(String.valueOf(order.getId()))))
			.andExpect(content().string(containsString(order.getCustomer())))
			.andExpect(content().string(containsString(order.getInbound().toString())))
			.andExpect(content().string(containsString(order.getExpectedFinished().toString())))
			.andExpect(content().string(containsString(">Expected Finishing Date<")))
			.andExpect(content().string(not(containsString(">Finishing Date<"))))
			.andExpect(content().string(containsString("Waiting")))
			.andExpect(content().string(containsString("Sewing (Buttons)")))
			.andExpect(content().string(containsString("encrypted message")));
	}

	private MissMintOrder createOrder() {
		UserAccount userAccount = userAccountManager.create("alice", Password.UnencryptedPassword.of("password"));

		OrderItem orderItem = itemCatalog.save(new OrderItem("encrypted message"));

		Optional<MissMintService> optionalService = serviceCatalog.findByName("sewing-buttons").get().findAny();
		assertThat(optionalService).isNotEmpty();
		MissMintService service = optionalService.get();

		LocalDate now = time.getTime().toLocalDate();
		MissMintOrder order = new MissMintOrder(userAccount, "Bob", now, service, orderItem);
		order.setExpectedFinished(now.plusDays(1));
		orderManager.save(order);
		return order;
	}
}