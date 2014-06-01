package de.graphdb.db.orientdb;

import static org.junit.Assert.fail;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.graphdb.db.orientdb.OrientDbConnection;
import de.graphdb.dto.UserDTO;

public class OrientDbConnectionTest
{
	private Logger log = LoggerFactory.getLogger(OrientDbConnectionTest.class);
	
	@Test
	public void openDatabaseTest()
	{
		log.debug("Start openDatabaseTest");
		
		try
		{
			OrientDbConnection c = OrientDbConnection.getInstance();
			
			c.open();
			
			c.close();
		}
		catch(Exception e)
		{
			log.error("",e);
			fail("Unexpected exception");
		}		
	}
	
	@Test
	public void insertUserTest()
	{
		log.debug("Start openDatabaseTest");
		
		try
		{
			OrientDbConnection c = OrientDbConnection.getInstance();
			
			OrientDbDao dao = new OrientDbDao();
			
			UserDTO u = new UserDTO();
			
			u.setForename("test_forename");
			u.setSurname("test_surname");
			u.setCity("test_city");
			u.setHousenumber("test_housenumber");
			u.setPostcode("test_postcode");
			u.setStreet("test_street");
			u.setMailadress("test_email");
			u.setPassword("test_password");
			
			dao.insertUser(u);
			
			if(u.getId() == null || u.getId().isEmpty())
				fail("Couldn't store user");
			
			UserDTO toCompare = dao.getUserById(u.getId());
			
			System.out.println(toCompare);
			
			dao.deleteUser(u);	
			
			toCompare = dao.getUserById(u.getId());
			
			System.out.println(toCompare);
			
			c.close();
		}
		catch(Exception e)
		{
			log.error("",e);
			fail("Unexpected exception");
		}		
	}
}
