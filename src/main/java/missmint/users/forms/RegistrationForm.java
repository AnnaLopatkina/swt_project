package missmint.users.forms;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

public class RegistrationForm {

	@NotBlank(message = "{RegistrationForm.name.NotBlank}")
	private final String firstName;

	@NotBlank(message = "{RegistrationForm.name.NotBlank}")
	private final String lastName;

	@NotBlank(message = "{RegistrationForm.name.NotBlank}")
	private final String userName;

	@NotEmpty(message = "{RegistrationForm.password.NotEmpty}")
	private final String password;

	@NotEmpty
	private final String role;

	public RegistrationForm(String firstName, String lastName, String userName, String password, String role) {

		this.firstName = firstName;
		this.lastName = lastName;
		this.userName = userName;
		this.password = password;
		this.role = role;
	}


	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getUserName() {
		return userName;
	}

	public String getPassword() {
		return password;
	}

	public String getRole() {
		return role;
	}
}