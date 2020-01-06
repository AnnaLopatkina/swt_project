package missmint.users.service;

import missmint.users.forms.RegistrationForm;
import missmint.users.model.Staff;
import missmint.users.repositories.StaffRepository;
import org.h2.engine.User;
import org.junit.jupiter.api.Test;
import org.salespointframework.useraccount.Password;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@SpringBootTest
@Transactional
public class StaffManagementTest {

	@Autowired
	private StaffManagement staffManagement;

	@Autowired
	private UserAccountManager userAccountManager;

	@Autowired
	private StaffRepository staffRepository;

	@Test
	void createNewStaff() {
		var userName = "user_123";
		var form = new RegistrationForm("Le", "me", userName, "123", BigDecimal.valueOf(1));
		staffManagement.createStaff(form);
		var user = userAccountManager.findByUsername(userName);
		assertTrue(user.isPresent());
		assertTrue(staffRepository.findByUserAccount(user.orElseThrow()).isPresent());
	}

	@Test
	void nullRegistrationForm() {
		assertThatThrownBy(() -> staffManagement.createStaff(null)).isNotNull();
	}

	@Test
	void deleteExistingStaff() {
		var userName = "user_123";
		var form = new RegistrationForm("Le", "me", userName, "123", BigDecimal.valueOf(1));
		staffManagement.createStaff(form);
		var user = userAccountManager.findByUsername(userName).orElseThrow();
		var staff = staffRepository.findByUserAccount(user).orElseThrow();
		staffManagement.deleteStaff(user.getUsername());

		assertTrue(userAccountManager.findByUsername(userName).isEmpty());
		assertTrue(staffRepository.findById(staff.getId()).isEmpty());
	}

	@Test
	void deleteStaffByEmptyName() {
		assertThatThrownBy(() -> staffManagement.deleteStaff(null)).isNotNull();
	}

	@Test
	void changePassword() {
		String userName = "user_123";
		RegistrationForm form = new RegistrationForm("Le", "me", userName, "123", BigDecimal.valueOf(1));
		staffManagement.createStaff(form);
		UserAccount user = userAccountManager.findByUsername(userName).orElseThrow();
		Password.EncryptedPassword password = user.getPassword();
		Staff staff = staffRepository.findByUserAccount(user).orElseThrow();

		staffManagement.changePassword(staff, "321");
		assertThat(user.getPassword()).isNotEqualTo(password);
	}
}
