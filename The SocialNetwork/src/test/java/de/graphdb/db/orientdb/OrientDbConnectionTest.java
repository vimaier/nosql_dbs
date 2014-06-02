package de.graphdb.db.orientdb;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

import de.graphdb.dto.UserDTO;

public class OrientDbConnectionTest
{
	private Logger log = LoggerFactory.getLogger(OrientDbConnectionTest.class);
	
	private UserDTO getTestUser(int index)
	{
		UserDTO u = new UserDTO();
		
		String indexString = "";
		
		if(index > 0)
			indexString = "" + index;
		
		u.setForename("test" + indexString + " _forename");
		u.setSurname("test" + indexString + " _surname");
		u.setCity("test" + indexString + " _city");
		u.setHousenumber("test" + indexString + " _housenumber");
		u.setPostcode("test" + indexString + " _postcode");
		u.setStreet("test" + indexString + " _street");
		u.setMailadress("test" + indexString + " _email");
		u.setPassword("test" + indexString + " _password");
		
		return u;
	}
	
	private UserDTO getTestUser()
	{
		return getTestUser(-1);
	}
	
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
		log.debug("Start insertUserTest");
		
		try
		{
			OrientDbDao dao = new OrientDbDao();
			
			UserDTO u = getTestUser();
			
			/*Tried to store null, should return false**/
			assertFalse(dao.insertUser(null));
			
			assertTrue(dao.insertUser(u));
			
			if(u.getId() == null || u.getId().isEmpty())
				fail("Couldn't store user");
			
			/*Tried to insert the user object again, should return false**/
			assertFalse(dao.insertUser(u));
			
			UserDTO toCompare = dao.getUserById(u.getId());
			
			assertTrue(u.getForename().equals(toCompare.getForename()));
			assertTrue(u.getSurname().equals(toCompare.getSurname()));
			assertTrue(u.getCity().equals(toCompare.getCity()));
			assertTrue(u.getHousenumber().equals(toCompare.getHousenumber()));
			assertTrue(u.getPostcode().equals(toCompare.getPostcode()));
			assertTrue(u.getStreet().equals(toCompare.getStreet()));
			assertTrue(u.getMailadress().equals(toCompare.getMailadress()));
			assertTrue(u.getPassword().equals(toCompare.getPassword()));
			
			assertTrue(dao.deleteUser(u));		
			
			toCompare = dao.getUserById(u.getId());
			
			assertTrue(toCompare == null);
			
			OrientDbConnection.getInstance().close();
		}
		catch(Exception e)
		{
			log.error("",e);
			fail("Unexpected exception");
		}		
	}
	
	@Test
	public void updateUserTest()
	{
		log.debug("Start updateUserTest");
		
		try
		{
			OrientDbDao dao = new OrientDbDao();
			
			UserDTO u = getTestUser();
			
			/*Tried to update null, should return null**/
			assertFalse(dao.updateUser(null) != null);
			
			/*Tried to update a not persisted user, should return null**/
			assertFalse(dao.updateUser(u) != null);
			
			assertTrue(dao.insertUser(u));
			
			if(u.getId() == null || u.getId().isEmpty())
				fail("Couldn't store user");
				
			
			UserDTO fromDB = dao.getUserById(u.getId());
			assertTrue(fromDB != null);
			
			fromDB.setForename("updated_forename");
			fromDB.setSurname("updated_surname");
			fromDB.setCity("updated_city");
			fromDB.setHousenumber("updated_housenumber");
			fromDB.setPostcode("updated_postcode");
			fromDB.setStreet("updated_street");
			fromDB.setMailadress("updated_email");
			fromDB.setPassword("updated_password");
			
			assertTrue(dao.updateUser(fromDB) != null);
			
			UserDTO toCompare = dao.getUserById(u.getId());
			
			assertTrue(toCompare != null);
			
			assertTrue(fromDB.getForename().equals(toCompare.getForename()));
			assertTrue(fromDB.getSurname().equals(toCompare.getSurname()));
			assertTrue(fromDB.getCity().equals(toCompare.getCity()));
			assertTrue(fromDB.getHousenumber().equals(toCompare.getHousenumber()));
			assertTrue(fromDB.getPostcode().equals(toCompare.getPostcode()));
			assertTrue(fromDB.getStreet().equals(toCompare.getStreet()));
			assertTrue(fromDB.getMailadress().equals(toCompare.getMailadress()));
			assertTrue(fromDB.getPassword().equals(toCompare.getPassword()));
			
			assertTrue(dao.deleteUser(u));	
			
			toCompare = dao.getUserById(u.getId());
			
			assertTrue(toCompare == null);
			
			OrientDbConnection.getInstance().close();
		}
		catch(Exception e)
		{
			log.error("",e);
			fail("Unexpected exception");
		}		
	}
	
	@Test
	public void deleteUserTest()
	{
		log.debug("Start deleteUserTest");
		
		try
		{
			OrientDbDao dao = new OrientDbDao();
			
			UserDTO u = getTestUser();
			
			/*Tried to delete null, should return false**/
			assertFalse(dao.deleteUser(null));
			
			/*Tried to delete a not persisted user, should return false**/
			assertFalse(dao.deleteUser(u));
			
			assertTrue(dao.insertUser(u));
			
			if(u.getId() == null || u.getId().isEmpty())
				fail("Couldn't store user");
				
			
			assertTrue(dao.deleteUser(u));	
			
			UserDTO toCompare = dao.getUserById(u.getId());
			
			assertTrue(toCompare == null);
			
			OrientDbConnection.getInstance().close();
		}
		catch(Exception e)
		{
			log.error("",e);
			fail("Unexpected exception");
		}		
	}
	
	@Test
	public void makeFriendsTest()
	{
		log.debug("Start makeFriendsTest");
		
		try
		{
			OrientDbDao dao = new OrientDbDao();
			
			UserDTO u1 = getTestUser(1);
			UserDTO u2 = getTestUser(2);
			
			/*Tried to create a relationship between null-Objects and not persisted entities, should return false*/
			assertFalse(dao.makeFriends(null,null));
			assertFalse(dao.makeFriends(u1,null));
			assertFalse(dao.makeFriends(null,u2));
			assertFalse(dao.makeFriends(u1,u2));			
			
			assertTrue(dao.insertUser(u1));
			assertTrue(dao.insertUser(u2));
			
			/*Tried to create a relationship between null-Objects , should return false*/
			assertFalse(dao.makeFriends(u1,null));
			assertFalse(dao.makeFriends(null,u2));		
			
			assertTrue(dao.makeFriends(u1, u2));	
						
			OrientGraph g = OrientDbConnection.getInstance().getGraphFactory().getTx();

			try
			{
				Vertex v1 = g.getVertex(u1.getId());
				assertTrue(v1 != null);
				
				int count = 0;
				
				for(Edge e : v1.getEdges(Direction.OUT,UserDTO.RELATIONSHIPS.FRIENDS.toString()))
				{
					count++;
					
					Vertex in_v = e.getVertex(Direction.IN);
					
					assertTrue(in_v != null);					
					assertTrue(in_v.getId().toString().equals(u2.getId()));
				}
				
				assertTrue(count == 1);
				
				Vertex v2 = g.getVertex(u2.getId());
				assertTrue(v2 != null);
				
				count = 0;
				
				for(Edge e : v2.getEdges(Direction.IN,UserDTO.RELATIONSHIPS.FRIENDS.toString()))
				{
					count++;
					
					Vertex out_v = e.getVertex(Direction.OUT);
					
					assertTrue(out_v != null);
					assertTrue(out_v.getId().toString().equals(u1.getId()));
				}
				
				assertTrue(count == 1);
			}
			catch(Exception e)
			{
				log.error("",e);
				g.rollback();
				fail("Unexpected exception");
			}
			finally
			{
				g.shutdown();
			}			
			
			assertTrue(dao.deleteUser(u1));	
		    assertTrue(dao.deleteUser(u2));
			
			OrientDbConnection.getInstance().close();
		}
		catch(Exception e)
		{
			log.error("",e);
			fail("Unexpected exception");
		}		
	}
	
	@Test
	public void unfriendTest()
	{
		log.debug("Start makeFriendsTest");
		
		try
		{
			OrientDbDao dao = new OrientDbDao();
			
			UserDTO u1 = getTestUser(1);
			UserDTO u2 = getTestUser(2);
			
			assertTrue(dao.insertUser(u1));
			assertTrue(dao.insertUser(u2));
			
			assertTrue(dao.makeFriends(u1, u2));	
			
			assertTrue(dao.unfriend(u1, u2));
						
			OrientGraph g = OrientDbConnection.getInstance().getGraphFactory().getTx();

			try
			{
				Vertex v1 = g.getVertex(u1.getId());
				assertTrue(v1 != null);
				
				assertFalse(v1.getEdges(Direction.OUT,UserDTO.RELATIONSHIPS.FRIENDS.toString()).iterator().hasNext());
				
				Vertex v2 = g.getVertex(u2.getId());
				assertTrue(v2 != null);
				
				assertFalse(v2.getEdges(Direction.IN,UserDTO.RELATIONSHIPS.FRIENDS.toString()).iterator().hasNext());
			}
			catch(Exception e)
			{
				log.error("",e);
				g.rollback();
				fail("Unexpected exception");
			}
			finally
			{
				g.shutdown();
			}			
			
			assertTrue(dao.deleteUser(u1));	
		    assertTrue(dao.deleteUser(u2));
			
			OrientDbConnection.getInstance().close();
		}
		catch(Exception e)
		{
			log.error("",e);
			fail("Unexpected exception");
		}		
	}
}
