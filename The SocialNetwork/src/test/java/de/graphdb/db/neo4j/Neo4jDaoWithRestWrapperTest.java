package de.graphdb.db.neo4j;

import static org.junit.Assert.*;

import java.util.Collection;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import de.graphdb.dto.UserDTO;

public class Neo4jDaoWithRestWrapperTest {
	
	private Neo4jDaoWithWrappedRest neo4jDao;

	@Before
	public void setUp() throws Exception {
		neo4jDao = new Neo4jDaoWithWrappedRest();
		createDummyData();
	}


	@After
	public void tearDown() throws Exception {
		deleteDummyData();
	}

	
	@Test
	public void testInsertUser() {
		UserDTO userDTO = new UserDTO();
		userDTO.setForename("a_forename");
		userDTO.setSurname("a_surname");
		userDTO.setMailadress("a_mailadress");
		userDTO.setStreet("a_street");
		userDTO.setHousenumber("a_housenumber");
		userDTO.setPostcode("a_postcode");
		userDTO.setCity("a_city");
		userDTO.setPassword("a_password");
		
		neo4jDao.insertUser(userDTO);
		
		Collection<UserDTO> foundUsers = neo4jDao.findUsers("a_mailadress");
		if(1 != foundUsers.size()) {
			fail("Found more than one user for 'a_mailadress'");
		}
		UserDTO userInDb = foundUsers.iterator().next();
		if( ! userInDb.areAttributesEqual(userDTO)){
			fail("Created user and fetched user are not equal");
		}
	}

	@Ignore @Test
	public void testDeleteUser() {
		fail("Not yet implemented");
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

	private void createDummyData() {
		// TODO Auto-generated method stub
		
	}	
	private void deleteDummyData() {
		// TODO Auto-generated method stub
		
	}
	
}
