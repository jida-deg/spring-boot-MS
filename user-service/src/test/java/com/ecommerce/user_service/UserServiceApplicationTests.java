package com.ecommerce.user_service;

import com.ecommerce.user_service.dto.auth.RegisterRequest;
import com.ecommerce.user_service.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
		"app.jwt.secret=MDEyMzQ1Njc4OWFiY2RlZjAxMjM0NTY3ODlhYmNkZWY=",
		"spring.datasource.url=jdbc:h2:mem:user-service-test;DB_CLOSE_DELAY=-1",
		"spring.datasource.username=sa",
		"spring.datasource.password=",
		"spring.jpa.database-platform=org.hibernate.dialect.H2Dialect"
})
@AutoConfigureMockMvc
class UserServiceApplicationTests {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private MockMvc mockMvc;

	@Test
	void contextLoads() {
	}

	@Test
	void passwordEncoderStoresOnlyAOneWayHash() {
		String rawPassword = "a-long-test-password";
		String encodedPassword = passwordEncoder.encode(rawPassword);

		assertNotEquals(rawPassword, encodedPassword);
		assertTrue(passwordEncoder.matches(rawPassword, encodedPassword));
	}

	@Test
	void passwordsAreAcceptedOnInputButNeverSerialized() throws Exception {
		String rawPassword = "a-long-test-password";
		String requestJson = """
				{"fullName":"Test User","email":"test@example.com","password":"a-long-test-password"}
				""";

		RegisterRequest request = objectMapper.readValue(requestJson, RegisterRequest.class);
		User user = new User("Test User", "test@example.com", passwordEncoder.encode(rawPassword));

		assertEquals(rawPassword, request.getPassword());
		assertFalse(objectMapper.writeValueAsString(request).contains("password"));
		assertFalse(objectMapper.writeValueAsString(user).contains(user.getPassword()));
	}

	@Test
	void singularRegistrationRouteIsPublic() throws Exception {
		mockMvc.perform(post("/api/user/register")
				.contentType("application/json")
				.content("{}"))
				.andExpect(status().isBadRequest());
	}

}
