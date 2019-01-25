package com.crossover.techtrial.controller;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.crossover.techtrial.model.Book;
import com.crossover.techtrial.repositories.BookRepository;
import com.crossover.techtrial.service.BookService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookControllerTest {

	MockMvc mockMvc;
	@Mock
	private BookController bookController;

	@Autowired
	private TestRestTemplate template;

	@Autowired
	BookRepository bookRepository;
	
	@Autowired
	private BookService bookService;

	@Before
	public void setup() throws Exception {
		mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
	}

	@Test
	public void testBookRegistrationSuccessful() throws Exception {
		HttpEntity<Object> book = getHttpEntity("{\"title\": \"Book X\" }");

		ResponseEntity<Book> response = template.postForEntity("/api/book", book, Book.class);

		Assert.assertEquals("Book X", response.getBody().getTitle());
		Assert.assertEquals(200, response.getStatusCode().value());

		// deleting the book
		bookRepository.deleteById(response.getBody().getId());
	}

	@Test
	public void getBookByIdSuccessfully() throws Exception {

		HttpEntity<Object> book = getHttpEntity("{\"title\": \"Book X\" }");

		ResponseEntity<Book> response = template.postForEntity("/api/book", book, Book.class);

		Assert.assertEquals("Book X", response.getBody().getTitle());
		Assert.assertEquals(200, response.getStatusCode().value());

		Book m = template.getForObject("/api/book/" + response.getBody().getId(), Book.class);

		Assert.assertEquals("Book X", m.getTitle());

		// deleting the book
		bookRepository.deleteById(response.getBody().getId());

	}
   
	@Test
	public void DeletingBookSuccessfully() {
		HttpEntity<Object> book1 = getHttpEntity("{\"title\": \"Book X\" }");
		HttpEntity<Object> book2 = getHttpEntity("{\"title\": \"Book Y\" }");
		
		ResponseEntity<Book> response1 = template.postForEntity("/api/book", book1, Book.class);
		ResponseEntity<Book> response2 = template.postForEntity("/api/book", book2, Book.class);
		
		Assert.assertEquals("Book X", response1.getBody().getTitle());
		Assert.assertEquals(200, response1.getStatusCode().value());
		
		Assert.assertEquals("Book Y", response2.getBody().getTitle());
		Assert.assertEquals(200, response2.getStatusCode().value());
		
		//list books before deleteing an item
		List<Book> listBooksBefore=bookService.getAll();
		
		//deleting book 1
		bookRepository.deleteById(response1.getBody().getId());	
		
		List<Book> listBooksAfter=bookService.getAll();
		
		Assert.assertEquals(listBooksAfter.size(), listBooksBefore.size()-1);
		
		bookRepository.deleteById(response2.getBody().getId());
		
	}
	@Test
	public void getAllBooksSuccessfully() throws Exception {

		HttpEntity<Object> book = getHttpEntity("{\"title\": \"Book X\" }");

		ResponseEntity<Book> response = template.postForEntity("/api/book", book, Book.class);

		Assert.assertEquals("Book X", response.getBody().getTitle());
		Assert.assertEquals(200, response.getStatusCode().value());

		ResponseEntity<Book[]> responseEntity = template.getForEntity("/api/book", Book[].class);

		Assert.assertTrue((responseEntity.getBody().length > 0));

		// deleting the book
		bookRepository.deleteById(response.getBody().getId());

	}

	private HttpEntity<Object> getHttpEntity(Object body) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		return new HttpEntity<Object>(body, headers);
	}

}
