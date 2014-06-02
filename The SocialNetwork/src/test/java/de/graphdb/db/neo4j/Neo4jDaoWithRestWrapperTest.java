package de.graphdb.db.neo4j;

import static org.junit.Assert.*;

import java.util.Collection;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.graphdb.dto.UserDTO;

public class Neo4jDaoWithRestWrapperTest {

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
		Collection<UserDTO> foundUsers = neo4jDao.findUsers(user.getMailadress());
		if(1 < foundUsers.size()) {
			fail( String.format("Found more than one user for '%s'", user.getMailadress()) );
		}
		if(0 == foundUsers.size()) {
			fail("Could not find created user");
		}
		UserDTO userInDb = foundUsers.iterator().next();
		assertTrue("Created user and fetched user are not equal", userInDb.areAttributesEqual(user));
	}
	
	@Test
	public void testDeleteUser() {
		UserDTO userDTO = getUserToDelete();
		neo4jDao.deleteUser(userDTO);
		
		Collection<UserDTO> foundUsers = neo4jDao.findUsers(userDTO.getMailadress());
		
		assertTrue("User found but should be deleted", 0 == foundUsers.size());
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
		
	}	
	private static void deleteDummyData() {
		neo4jDao.deleteUser(getUserToInsert());
		
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
	
}
