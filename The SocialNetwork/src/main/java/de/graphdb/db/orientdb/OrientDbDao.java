package de.graphdb.db.orientdb;

import java.sql.Date;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
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
		v.setProperty("forename", u.getForename());
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
		u.setForename((String) v.getProperty("forename"));
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
				g.rollback();
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
				g.rollback();
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
				g.rollback();
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
				g.rollback();
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
		if(user == null || friend == null)
		{
			log.debug("Recieved null as param");
			return false;
		}
		
		OrientGraph g = getGraph();
		
		if(g != null)
		{
			try
			{
				Vertex userV = g.getVertex(user.getId());
				Vertex userF = g.getVertex(friend.getId());
				
				if(userV == null || userF == null)
				{
					log.debug("Couldn't find both users to create a relationship");
					return false;
				}
				
				Edge e = g.addEdge(null,userV,userF,UserDTO.RELATIONSHIP_FRIENDS);
				e.setProperty("friendsSince", new Date(System.currentTimeMillis()).toString());
				
				g.commit();
				
				log.debug("Successfully stored new realtionship");
				return true;
			}
			catch(Exception e)
			{
				log.error("Couldn't create relationship 'FRIEND' between " + user.getId() + " and " + friend.getId(),e);
				g.rollback();
			}
			finally
			{
				g.shutdown();
			}			
		}	
		
		return false;
	}

	@Override
	public boolean unfriend(UserDTO user, UserDTO friend)
	{
		if(user == null || friend == null)
		{
			log.debug("Recieved null as param");
			return false;
		}
		
		OrientGraph g = getGraph();
		
		if(g != null)
		{
			try
			{
				Vertex userV = g.getVertex(user.getId());
				Vertex userF = g.getVertex(friend.getId());
				
				if(userV == null || userF == null)
				{
					log.debug("Couldn't find both users to delete a relationship");
					return false;
				}
				
				for(Edge e : userV.getEdges(Direction.BOTH, UserDTO.RELATIONSHIP_FRIENDS))
				{
					if(e.getVertex(Direction.IN).getId().equals(userF.getId()) || 
							e.getVertex(Direction.OUT).getId().equals(userF.getId()))
					{
						g.removeEdge(e);
					}
				}
				
				g.commit();
				
				log.debug("Successfully deleted realtionship");
				return true;
			}
			catch(Exception e)
			{
				log.error("Couldn't delete relationship 'FRIEND' between " + user.getId() + " and " + friend.getId(),e);
				g.rollback();
			}
			finally
			{
				g.shutdown();
			}			
		}	
		
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