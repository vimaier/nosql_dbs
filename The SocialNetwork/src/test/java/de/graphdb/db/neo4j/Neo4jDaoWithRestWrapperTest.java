package de.graphdb.db.neo4j;

import static org.junit.Assert.*;

import java.util.Collection;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.graphdb.dto.LoginDTO;
import de.graphdb.dto.UserDTO;

public class Neo4jDaoWithRestWrapperTest {

	@SuppressWarnings("unused")
	private static Logger log = LoggerFactory.getLogger(Neo4jDaoWithRestWrapperTest.class);
	private static Neo4jDaoWithWrappedRest neo4jDao = null;

	@BeforeClass
	public static void setUp() throws Exception {
		neo4jDao = new Neo4jDaoWithWrappedRest();
		createDummyData();
	}


	@AfterClass
	public static void tearDown() throws Exception {
		deleteDummyData();
	}

	
	@Test
	public void testInsertUser() {
		UserDTO user = getUserToInsert();
		neo4jDao.insertUser(user);
		UserDTO userInDb = neo4jDao.getUserById(user.getId());

		assertTrue("Created user and fetched user are not equal", null != userInDb && userInDb.areAttributesEqual(user));
	}
	
	@Test
	public void testDeleteUser() {
		UserDTO userDTO = getUserToDelete();
		neo4jDao.deleteUser(userDTO);
		
		Collection<UserDTO> foundUsers = neo4jDao.findUsers(userDTO.getMailadress());
		
		assertTrue("User found but should be deleted", 0 == foundUsers.size());
	}
	
	@Test
	public void testUpdateUser() {
		UserDTO userDTO = getUserToUpdate();
		userDTO.setForename("new_name");
		userDTO.setSurname("new_surname");
		userDTO.setStreet("new_street");
		userDTO.setHousenumber("new_housenumber");
		userDTO.setPostcode("new_postcode");
		userDTO.setCity("new_city");
		userDTO.setPassword("new_password");
		neo4jDao.updateUser(userDTO);
		
		Collection<UserDTO> foundUsers = neo4jDao.findUsers(userDTO.getMailadress());
		if(0 == foundUsers.size())
			fail("Updated user not found");
		UserDTO updatedUser = foundUsers.iterator().next();
		assertTrue("Updated user not equal with local copy", userDTO.areAttributesEqual(updatedUser));
	}
	
	@Test
	public void testLoginUser() {
		UserDTO userDTO = getUserForLogin();
		LoginDTO login = new LoginDTO(userDTO.getMailadress(), userDTO.getPassword());
		UserDTO loginUser = neo4jDao.loginUser(login);
		
		assertTrue("Could not login", userDTO.areAttributesEqual(loginUser));
	}
	
	@Ignore @Test
	public void testFindFriends() {
		fail("Not yet implemented");
	}
	
	@Ignore @Test
	public void testFindUsers() {
		fail("Not yet implemented");
	}
	
	@Ignore @Test
	public void testMakeFriends() {
		fail("Not yet implemented");
	}
	
	@Ignore @Test
	public void testUnfriend() {
		fail("Not yet implemented");
	}
	
	@Ignore @Test
	public void testFindFriendsOfFriends() {
		fail("Not yet implemented");
	}
	
	@Ignore @Test
	public void testFindRelative() {
		fail("Not yet implemented");
	}

	private static void createDummyData() {
		neo4jDao.insertUser(getUserToDelete());
		neo4jDao.insertUser(getUserToUpdate());
		neo4jDao.insertUser(getUserForLogin());
		
	}	
	private static void deleteDummyData() {
		neo4jDao.deleteUser(getUserToInsert());
		neo4jDao.deleteUser(getUserToUpdate());
	}
	
	
	private static UserDTO getUserToDelete() {
		UserDTO userDTO = new UserDTO();
		userDTO.setForename("del_forename");
		userDTO.setSurname("del_surname");
		userDTO.setMailadress("del_mailaddress");
		userDTO.setStreet("del_street");
		userDTO.setHousenumber("del_housenumber");
		userDTO.setPostcode("del_postcode");
		userDTO.setCity("del_city");
		userDTO.setPassword("del_password");
		return userDTO;
	}
	
	private static UserDTO getUserToInsert() {
		UserDTO userDTO = new UserDTO();
		userDTO.setForename("a_forename");
		userDTO.setSurname("a_surname");
		userDTO.setMailadress("a_mailaddress");
		userDTO.setStreet("a_street");
		userDTO.setHousenumber("a_housenumber");
		userDTO.setPostcode("a_postcode");
		userDTO.setCity("a_city");
		userDTO.setPassword("a_password");
		return userDTO;		
	}
	
	private static UserDTO getUserToUpdate() {
		UserDTO userDTO = new UserDTO();
		userDTO.setForename("up_forename");
		userDTO.setSurname("up_surname");
		userDTO.setMailadress("up_mailaddress");
		userDTO.setStreet("up_street");
		userDTO.setHousenumber("up_housenumber");
		userDTO.setPostcode("up_postcode");
		userDTO.setCity("up_city");
		userDTO.setPassword("up_password");
		return userDTO;		
	}
	
	private static UserDTO getUserForLogin() {
		UserDTO userDTO = new UserDTO();
		userDTO.setForename("login_forename");
		userDTO.setSurname("login_surname");
		userDTO.setMailadress("login_mailaddress");
		userDTO.setStreet("login_street");
		userDTO.setHousenumber("login_housenumber");
		userDTO.setPostcode("login_postcode");
		userDTO.setCity("login_city");
		userDTO.setPassword("login_password");
		return userDTO;		
	}
	
}
