package com.example.lab3;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthorController.class)
class Lab3ApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void testGetAuthorInfo() throws Exception {
		mockMvc.perform(get("/author"))
				.andExpect(status().isOk())
				.andExpect(view().name("author"))
				.andExpect(model().attribute("authorName", "Юрий Алексеевич Катохин"));
	}
}
