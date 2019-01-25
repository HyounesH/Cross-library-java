/**
 * 
 */
package com.crossover.techtrial.controller;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.crossover.techtrial.model.Member;
import com.crossover.techtrial.repositories.MemberRepository;

/**
 * @author kshah
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class MemberControllerTest {
  
  MockMvc mockMvc;
  
  @Mock
  private MemberController memberController;
  
  @Autowired
  private TestRestTemplate template;
  
  @Autowired
  MemberRepository memberRepository;
  
  @Before
  public void setup() throws Exception {
    mockMvc = MockMvcBuilders.standaloneSetup(memberController).build();
  }
  
  @Test
  public void testMemberRegsitrationsuccessful() throws Exception {
    HttpEntity<Object> member = getHttpEntity(
        "{\"name\": \"test\", \"email\": \"test10000000000001@gmail.com\"," 
            + " \"membershipStatus\": \"ACTIVE\",\"membershipStartDate\":\"2018-08-08T12:12:12\" }");
    
    ResponseEntity<Member> response = template.postForEntity(
        "/api/member", member, Member.class);
    
    Assert.assertEquals("test", response.getBody().getName());
    Assert.assertEquals(200,response.getStatusCode().value());
    
    //cleanup the user
    memberRepository.deleteById(response.getBody().getId());
  }
  
  @Test
  public void testMemberDuplicatedEmailOnRegistration() throws Exception {
      HttpEntity<Object> member1 = getHttpEntity(
              "{\"name\": \"Younes hamdane\", \"email\": \"younes.hamdane11@gmail.com\","
                      + " \"membershipStatus\": \"ACTIVE\",\"membershipStartDate\":\"2019-01-25T17:12:00\" }");

      HttpEntity<Object> member2 = getHttpEntity(
              "{\"name\": \"hamdane \", \"email\": \"younes.hamdane11@gmail.com\","
                      + " \"membershipStatus\": \"ACTIVE\",\"membershipStartDate\":\"2019-01-25T17:12:00\" }");

      ResponseEntity<Member> response1= template.postForEntity(
              "/api/member", member1, Member.class);

      Assert.assertEquals("Younes hamdane", response1.getBody().getName());
      Assert.assertEquals(200, response1.getStatusCode().value());

      ResponseEntity<Member> response2 = template.postForEntity(
              "/api/member", member2, Member.class);

      Assert.assertEquals(400, response2.getStatusCode().value());

      //cleanup the user
      memberRepository.deleteById(response1.getBody().getId());
  }

  @Test
  public void testWrongNameStartOnRegistration() throws Exception {
      HttpEntity<Object> member = getHttpEntity(
              "{\"name\": \"1test\", \"email\": \"test@gmail.com\","
                      + " \"membershipStatus\": \"ACTIVE\",\"membershipStartDate\":\"2019-02-13T22:00:01\" }");

      ResponseEntity<Member> response = template.postForEntity(
              "/api/member", member, Member.class);

      Assert.assertEquals(400, response.getStatusCode().value());
  }

  @Test
  public void testTooShortNameOnRegistration() throws Exception {
      HttpEntity<Object> member = getHttpEntity(
              "{\"name\": \"t\", \"email\": \"abcd@gmail.com\","
                      + " \"membershipStatus\": \"ACTIVE\",\"membershipStartDate\":\"2019-02-13T22:00:01\" }");

      ResponseEntity<Member> response = template.postForEntity(
              "/api/member", member, Member.class);

      Assert.assertEquals(400, response.getStatusCode().value());
  }

  @Test
  public void testTooLongNameOnRegistration() throws Exception {
      HttpEntity<Object> member = getHttpEntity(
              "{\"name\": \"hello its me i was wondering abcdefghigklmnopqrstuvwxyz\", \"email\": \"younes123@gmail.com\","
                      + " \"membershipStatus\": \"ACTIVE\",\"membershipStartDate\":\"2019-02-13T22:00:01\" }");

      ResponseEntity<Member> response = template.postForEntity(
              "/api/member", member, Member.class);

      Assert.assertEquals(400, response.getStatusCode().value());
  }
  
 

  @Test
  public void getMemberByIdSuccessfully() throws Exception {

      HttpEntity<Object> member = getHttpEntity(
              "{\"name\": \"code it\", \"email\": \"helloworld@gmail.com\","
                      + " \"membershipStatus\": \"ACTIVE\",\"membershipStartDate\":\"2019-02-13T22:00:01\" }");

      ResponseEntity<Member> response = template.postForEntity(
              "/api/member", member, Member.class);

      Assert.assertEquals("code it", response.getBody().getName());
      Assert.assertEquals(200, response.getStatusCode().value());

      Member m = template.getForObject("/api/member/"+response.getBody().getId(), Member.class);

      Assert.assertEquals("code it", m.getName());

      //deleting the user
      memberRepository.deleteById(response.getBody().getId());

  }
  
  @Test
	public void testDeletingMember() {
		HttpEntity<Object> member1 = getHttpEntity("{\"name\": \"hello world\", \"email\": \"helloworld@gmail.com\","
				+ " \"membershipStatus\": \"ACTIVE\",\"membershipStartDate\":\"2019-02-13T22:00:01\" }");
		HttpEntity<Object> member2 = getHttpEntity("{\"name\": \"hello crossover\", \"email\": \"crossover@gmail.com\","
				+ " \"membershipStatus\": \"ACTIVE\",\"membershipStartDate\":\"2019-02-13T22:00:01\" }");
		
		ResponseEntity<Member> response1 = template.postForEntity("/api/member", member1, Member.class);
		
		ResponseEntity<Member> response2 = template.postForEntity("/api/member", member2, Member.class);
		
		Assert.assertEquals("hello world", response1.getBody().getName());
		Assert.assertEquals(200, response1.getStatusCode().value());
		
		Assert.assertEquals("hello crossover", response2.getBody().getName());
		Assert.assertEquals(200, response2.getStatusCode().value());
		
		//get list members before deleting a member
		List<Member> listMembersBefore=memberRepository.findAll();
		
		memberRepository.deleteById(response1.getBody().getId());
		 
		//get List members after deleting membre 1
		
		List<Member> listMembersAfter=memberRepository.findAll();
		
		Assert.assertEquals(listMembersAfter.size(), listMembersBefore.size()-1);
		
		memberRepository.deleteById(response2.getBody().getId());
		

	}
 
  @Test
  public void getAllMembersSuccessfully() throws Exception {

      HttpEntity<Object> member = getHttpEntity(
              "{\"name\": \"girls code\", \"email\": \"girlsCode@gmail.com\","
                      + " \"membershipStatus\": \"ACTIVE\",\"membershipStartDate\":\"2019-02-13T22:00:01\" }");

      ResponseEntity<Member> response = template.postForEntity(
              "/api/member", member, Member.class);

      Assert.assertEquals("girls code", response.getBody().getName());
      Assert.assertEquals(200, response.getStatusCode().value());

      ResponseEntity<Member[]> responseEntity = template.getForEntity("/api/member", Member[].class);

      Assert.assertTrue((responseEntity.getBody().length > 0));

      //deleting the user
      memberRepository.deleteById(response.getBody().getId());

  }

  private HttpEntity<Object> getHttpEntity(Object body) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    return new HttpEntity<Object>(body, headers);
  }

}
