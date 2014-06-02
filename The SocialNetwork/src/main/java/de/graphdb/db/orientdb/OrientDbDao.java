package de.graphdb.db.orientdb;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

import de.graphdb.db.GraphDBInterface;
import de.graphdb.dto.UserDTO;

public class OrientDbDao implements GraphDBInterface
{
	private final static Logger log = LoggerFactory.getLogger(OrientDbDao.class);
	
	private final OrientDbConnection connection;
	
	public OrientDbDao()
	{
		this.connection = OrientDbConnection.getInstance();
	}
	
	private OrientGraph getGraph()
	{
		try
		{
			return this.connection.getGraphFactory().getTx();
		}
		catch(Exception e)
		{
			log.error("Couldn't create graphTx",e);
			return null;
		}		
	}
	
	private void convertUserToVertex(Vertex v,UserDTO u)
	{
		v.setProperty("forename", u.getForname());
		v.setProperty("surname", u.getSurname());
		v.setProperty("city", u.getCity());
		v.setProperty("housenumber", u.getHousenumber());
		v.setProperty("postcode", u.getPostcode());
		v.setProperty("street", u.getStreet());
		v.setProperty("email", u.getMailadress());
		v.setProperty("password", u.getPassword());		
	}
	
	private void convertVertexToUser(Vertex v,UserDTO u)
	{
		u.setForname((String) v.getProperty("forename"));
		u.setSurname((String) v.getProperty("surname"));
		u.setCity((String) v.getProperty("city"));
		u.setHousenumber((String) v.getProperty("housenumber"));
		u.setPostcode((String) v.getProperty("postcode"));
		u.setStreet((String) v.getProperty("street"));
		u.setMailadress((String) v.getProperty("email"));
		u.setPassword((String) v.getProperty("password"));	
	}
	
	@Override
	public boolean insertUser(UserDTO user)
	{
		if(user == null)
		{
			log.debug("UserObject is null -> abort insert");
			return false;
		}
		
		OrientGraph g = getGraph();
		
		if(g != null)
		{
			try
			{
				if(user.getId() != null && !user.getId().isEmpty() && g.getVertex(user.getId()) != null)
				{
					log.error("Tried to insert an alreay existing user");
					g.commit();
					return false;
				}
				else
				{
					Vertex v = g.addVertex(null);
					
					convertUserToVertex(v, user);
					
					g.commit();
					
					user.setId(v.getId().toString());
					
					log.debug("stored new user with the id: " + user.getId());
					return true;
				}
			}
			catch(Exception e)
			{
				log.error("Couldn't insert new user",e);
			}
			finally
			{
				g.shutdown();
			}			
		}	
		
		return false;
	}

	@Override
	public UserDTO updateUser(UserDTO user)
	{
		if(user == null)
		{
			log.debug("UserObject is null -> abort update");
			return null;
		}
		
		OrientGraph g = getGraph();
		
		if(g != null)
		{
			try
			{
				Vertex toUpdate = g.getVertex(user.getId());
				
				if(user.getId() == null || user.getId().isEmpty() || toUpdate == null)
				{
					log.error("Tried to update a not existing user");
					g.commit();
					return null;
				}
				else
				{
					convertUserToVertex(toUpdate, user);
					
					g.commit();
					
					log.debug("update user with the id: " + user.getId());
					return user;
				}
			}
			catch(Exception e)
			{
				log.error("Couldn't update user");
			}
			finally
			{
				g.shutdown();
			}			
		}	
		
		return null;
	}
	
	public UserDTO getUserById(String id)
	{
		if(id == null || id.isEmpty())
		{
			log.debug("recieved empty id");
			return null;
		}
		
		OrientGraph g = getGraph();
		
		if(g != null)
		{
			try
			{
				Vertex v = g.getVertex(id);
				g.commit();
				if(v == null)
				{
					log.debug("Found no user with the id: " + id);
					return null;
				}
				else
				{
					UserDTO toReturn = new UserDTO();
					toReturn.setId(id);
					convertVertexToUser(v, toReturn);
					return toReturn;
				}				
			}
			catch(Exception e)
			{
				log.error("Couldn't search for user with id: " + id);
			}
			finally
			{
				g.shutdown();
			}			
		}	
		
		return null;
	}

	@Override
	public boolean deleteUser(UserDTO user)
	{
		if(user == null)
		{
			log.debug("UserObject is null -> abort delete");
			return false;
		}
		
		OrientGraph g = getGraph();
		
		if(g != null)
		{
			try
			{
				Vertex toDelete = g.getVertex(user.getId());
				
				if(user.getId() == null || user.getId().isEmpty() || toDelete == null)
				{
					log.error("Tried to delete an not existing user");
					g.commit();
					return false;
				}
				else
				{
					g.removeVertex(g.getVertex(toDelete));
					g.commit();
					
					user.setId(null);
					return true;
				}
			}
			catch(Exception e)
			{
				log.error("Couldn't delete new user",e);
			}
			finally
			{
				g.shutdown();
			}			
		}	
		
		return false;
	}

	@Override
	public Collection<UserDTO> findFriends(UserDTO user)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<UserDTO> findUsers(String suchbegriff)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean makeFriends(UserDTO user, UserDTO friend)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean unfriend(UserDTO user, UserDTO friend)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Collection<UserDTO> findFriendsOfFriends(Collection<UserDTO> friends)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<UserDTO> findRelative(UserDTO user)
	{
		// TODO Auto-generated method stub
		return null;
	}

}
