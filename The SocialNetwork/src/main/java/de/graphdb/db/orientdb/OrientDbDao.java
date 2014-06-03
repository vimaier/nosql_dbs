package de.graphdb.db.orientdb;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

import de.graphdb.db.GraphDBInterface;
import de.graphdb.dto.LoginDTO;
import de.graphdb.dto.UserDTO;
import de.graphdb.dto.UserDTO.RELATIONSHIPS;

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
		v.setProperty("mailadress", u.getMailadress());
		v.setProperty("password", u.getPassword());	
	}

	private void convertVertexToUser(Vertex v,UserDTO u)
	{
		u.setId(v.getId().toString().substring(1));
		u.setForename((String) v.getProperty("forename"));
		u.setSurname((String) v.getProperty("surname"));
		u.setCity((String) v.getProperty("city"));
		u.setHousenumber((String) v.getProperty("housenumber"));
		u.setPostcode((String) v.getProperty("postcode"));
		u.setStreet((String) v.getProperty("street"));
		u.setMailadress((String) v.getProperty("mailadress"));
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
					Vertex v = g.addVertex("V");

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

		if(user.getId() == null || user.getId().isEmpty())
		{
			log.debug("Invalid userId " + user.getId() + " -> abort update");
			return null;
		}

		OrientGraph g = getGraph();

		if(g != null)
		{
			try
			{
				Vertex toUpdate = g.getVertex(user.getId());

				if(toUpdate == null)
				{
					log.error("Tried to update a not existing user for id: " + user.getId());
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
				log.error("Couldn't update user",e);
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

		if(user.getId() == null || user.getId().isEmpty())
		{
			log.debug("Invalid userId " + user.getId() + " -> abort delete");
			return false;
		}

		OrientGraph g = getGraph();

		if(g != null)
		{
			try
			{
				Vertex toDelete = g.getVertex(user.getId());

				if(toDelete == null)
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
		if(user == null)
		{
			log.debug("UserObject is null -> abort return null");
			return null;
		}

		if(user.getId() == null || user.getId().isEmpty())
		{
			log.debug("Invalid userId " + user.getId() + " -> return null");
			return null;
		}

		OrientGraph g = getGraph();

		if(g != null)
		{
			try
			{
				Vertex collectFriends = g.getVertex(user.getId());

				if(collectFriends == null)
				{
					log.error("Tried to find friends for a not persisted user -> return null");
					g.commit();
					return null;
				}
				else
				{
					Collection<UserDTO> toReturn = new ArrayList<UserDTO>();

					/*get all realtionships with the laben 'FRIENDS' and add the nodes to the result list*/
					for(Vertex toAdd : collectFriends.getVertices(Direction.BOTH,UserDTO.RELATIONSHIPS.FRIENDS.toString()))
					{
						if(toAdd != collectFriends)
						{
							UserDTO userToAdd = new UserDTO();
							convertVertexToUser(toAdd, userToAdd);

							if(toReturn.contains(userToAdd) == false)
								toReturn.add(userToAdd);
						}
					}

					return toReturn;
				}
			}
			catch(Exception e)
			{
				log.error("Couldn't find friends for userid: " + user.getId(),e);
				g.rollback();
			}
			finally
			{
				g.shutdown();
			}			
		}	

		return null;
	}

	/**
	 * Find a user by his fore/-lastname
	 * 
	 * Valid keyword are:
	 * 
	 * - Forename  
	 * - Lastname  
	 * - Forename Lastname
	 * - Lastname Forename
	 * 
	 * or a substring of them
	 * 
	 * @param user
	 * @return Collection<UserDTO>
	 */
	public Collection<UserDTO> findUsers(String suchbegriff)
	{
		if(suchbegriff == null)
		{
			log.debug("Recieved empty keyword -> abort search and return null");
		}

		OrientGraph g = getGraph();

		try
		{
			Map<String,Object> fieldValues = new HashMap<String,Object>();
			fieldValues.put("forename", "%" + suchbegriff.toLowerCase() + "%");
			fieldValues.put("surname", "%" + suchbegriff.toLowerCase() + "%");

			String[] splittedKeyword = suchbegriff.toLowerCase().split(" ");

			String toAppend  = "";		

			/*splitt the name for key word like 'Forename Surname'*/
			if(splittedKeyword.length == 2)
			{
				toAppend = " OR forename.append(surname).toLowerCase() LIKE :fl OR surname.append(forename).toLowerCase() LIKE :lf";

				fieldValues.put("fl", "%" + splittedKeyword[0] + splittedKeyword[1] + "%");
				fieldValues.put("lf", "%" + splittedKeyword[0] + splittedKeyword[1] + "%");
			}

			String query = "select from V WHERE forename.toLowerCase() LIKE :forename OR surname.toLowerCase() LIKE :surname" + toAppend ;

			OSQLSynchQuery<Vertex> getAllQuery = new OSQLSynchQuery<Vertex>(query);
			Iterable<Vertex> result = g.command(getAllQuery).execute(fieldValues);

			Collection<UserDTO> toReturn  = new ArrayList<UserDTO>();

			for(Vertex v : result)
			{
				UserDTO toAdd = new UserDTO();
				convertVertexToUser(v, toAdd);
				toReturn.add(toAdd);
			}

			g.commit();

			log.debug("Found " + toReturn.size() + " users for keyword: " + suchbegriff);

			return toReturn;
		}
		catch(Exception e)
		{
			log.error("",e);
			g.rollback();
		}
		finally
		{
			g.shutdown();
		}

		return null;
	}

	@Override
	public boolean makeFriends(UserDTO user, UserDTO friend)
	{
		return createRelationShip(user, friend, UserDTO.RELATIONSHIPS.FRIENDS);
	}

	private boolean createRelationShip(UserDTO user, UserDTO user2,RELATIONSHIPS relationship)
	{
		if(user == null || user2 == null)
		{
			log.debug("Recieved null as param -> abort create realtionship");
			return false;
		}

		if(user.getId() == null || user.getId().isEmpty())
		{
			log.debug("Invalid userId " + user.getId() + " -> abort create realtionship");
			return false;
		}

		if(user.getId() == null || user2.getId().isEmpty())
		{
			log.debug("Invalid user2Id " + user2.getId() + " -> abort create realtionship");
			return false;
		}

		if(relationship == null)
		{
			log.debug("Invalid relationship: " + relationship);
			return false;
		}

		OrientGraph g = getGraph();

		if(g != null)
		{
			try
			{
				Vertex userV = g.getVertex(user.getId());
				Vertex userF = g.getVertex(user2.getId());

				if(userV == null || userF == null)
				{
					log.debug("Couldn't find both users to create a relationship");
					return false;
				}

				/*create a new edge between the given objects and set the current systemtime as 
				 * date of creation*/
				Edge e = g.addEdge(null,userV,userF,relationship.toString());
				e.setProperty("dateOfCreation", new Date(System.currentTimeMillis()).toString());

				g.commit();

				log.debug("Successfully stored new realtionship");
				return true;
			}
			catch(Exception e)
			{
				log.error("Couldn't create relationship '" + relationship + "' between " + user.getId() + " and " + user2.getId(),e);
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
		return deleteRelationship(user, friend, UserDTO.RELATIONSHIPS.FRIENDS);
	}

	private boolean deleteRelationship(UserDTO user, UserDTO user2, RELATIONSHIPS relationship)
	{
		if(user == null || user2 == null)
		{
			log.debug("Recieved null as param");
			return false;
		}

		if(relationship == null)
		{
			log.debug("Recieved invalid realtionship: " + relationship);
			return false;
		}

		OrientGraph g = getGraph();

		if(g != null)
		{
			try
			{
				Vertex userV = g.getVertex(user.getId());
				Vertex userF = g.getVertex(user2.getId());

				if(userV == null || userF == null)
				{
					log.debug("Couldn't find both users to delete a relationship");
					return false;
				}

				/*select relationships of the user and delete them by user*/
				/*A sql query could resolve the problem more elegant*/
				for(Edge e : userV.getEdges(Direction.BOTH, relationship.toString()))
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
				log.error("Couldn't delete relationship '" + relationship + "' between " + user.getId() + " and " + user2.getId(),e);
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
	public Collection<UserDTO> findFriendsOfFriends(UserDTO user)
	{
		/*This is not really the requested implementation. 
		 * It should be replaced with a 'traverse' query.
		 * 
		 * 
		 * A proper way would be to replace the parameter 'friends' with a single user object
		 * and to use the orientdb traverse api to retrieve all desired users
		 * 
		 * */

		if(user == null)
		{
			log.debug("Recieved null as param -> abort and return null");
			return null;
		}

		OrientGraph g = getGraph();

		try
		{
			/*convert nodes to userObjects*/
			Collection<UserDTO> toReturn = new ArrayList<UserDTO>();
			for (ODocument id : new OSQLSynchQuery<ODocument>("select from (traverse out_FRIENDS,in from " + user.getId() + " while $depth <= 4) where $depth = 4 AND @class = 'V'"))
			{
				Vertex vToAdd = g.getVertex(id.getIdentity().toString());
				UserDTO toAdd = new UserDTO();
				convertVertexToUser(vToAdd, toAdd);
				toReturn.add(toAdd);
			}

			log.debug("Return a result list with size: " + toReturn.size());

			g.commit();
			return toReturn;
		}
		catch(Exception e)
		{
			log.error("Couldn't traverse friend of friend if the user: " + user.getId(),e);
			g.rollback();
		}
		finally
		{
			g.shutdown();
		}		

		return null;	
	}

	@Override
	public Collection<UserDTO> findRelative(UserDTO user)
	{
		// Actually, there is no such method to create a realtionship or properie for relatives
		return null;
	}

	@Override
	public UserDTO loginUser(LoginDTO login) 
	{
		if(login == null)
		{
			log.debug("Recieved recieved empty login-object");
			return null;
		}

		if(login.getPassword() == null || login.getPassword().isEmpty())
		{
			log.debug("LoginObject contains an invalid password: " + login.getPassword());
			return null;
		}

		if(login.getMailadress() == null || login.getMailadress().isEmpty())
		{
			log.debug("LoginObject contains an invalid mailadress " + login.getMailadress());
			return null;
		}

		OrientGraph g = getGraph();

		try
		{
			/*create basic query to look after mail and password*/
			String query = "SELECT FROM V WHERE password LIKE :password AND mailadress.toLowerCase() LIKE :mail" ;

			/*Set fields*/
			Map<String,Object> fieldValues = new HashMap<String,Object>();
			fieldValues.put("password", login.getPassword());
			fieldValues.put("mail", login.getMailadress().toLowerCase());

			//execute query
			OSQLSynchQuery<Vertex> getAllQuery = new OSQLSynchQuery<Vertex>(query);
			Iterable<Vertex> vs = g.command(getAllQuery).execute(fieldValues);

			/*convert nodes to userObjects*/
			List<Vertex> result = new ArrayList<Vertex>();
			for(Vertex v : vs)
				result.add(v);

			/*start security control. The result of this query should be a single result*/
			if(result.size() == 0)
			{
				log.debug("Couldn't find a user for mail: " + login.getMailadress() + " and password: "+ login.getPassword());
			}

			if(result.size() > 1)
			{
				log.debug("Found multiple entries for users with mail: " + login.getMailadress() + " and password: "+ login.getPassword());
			}

			if(result.size() == 1)
			{
				UserDTO toReturn = new UserDTO();
				convertVertexToUser(result.get(0),toReturn);
				g.commit();
				return toReturn;
			}		

			g.commit();
		}
		catch(Exception e)
		{
			log.error("Couldn't login with credentials: " + login.getMailadress() + " : " + login.getPassword(),e);
			g.rollback();
		}
		finally
		{
			g.shutdown();
		}		

		return null;		
	}
}
