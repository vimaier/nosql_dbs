package de.graphdb.db.neo4j;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.neo4j.graphdb.Node;
import org.neo4j.helpers.collection.MapUtil;
import org.neo4j.rest.graphdb.RestAPI;
import org.neo4j.rest.graphdb.RestGraphDatabase;
import org.neo4j.rest.graphdb.query.QueryEngine;
import org.neo4j.rest.graphdb.query.RestCypherQueryEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.graphdb.db.GraphDBInterface;
import de.graphdb.dto.LoginDTO;
import de.graphdb.dto.UserDTO;

/*
 * Some good examples...
 * https://github.com/neo4j/java-rest-binding/blob/1590210bf1097411d208fa977f83a60a5677c277/src/test/java/org/neo4j/rest/graphdb/RestCypherQueryEngineTest.java
 * 
 */
public class Neo4jDaoWithWrappedRest implements GraphDBInterface {

	private static final String SERVER_ROOT_URI = "http://localhost:7474/db/data/";
	
	
	private Logger log = LoggerFactory.getLogger(Neo4jDaoWithWrappedRest.class);
	
	private final boolean LAZY_INIT = true;
	private RestGraphDatabase restGraphDatabase;
	private RestAPI restAPI;
	private QueryEngine queryEngine;
	
	private void init() {
		RestGraphDatabase rgdb = new RestGraphDatabase(SERVER_ROOT_URI);
		restAPI = (rgdb).getRestAPI();
		queryEngine = new RestCypherQueryEngine(restAPI); 
	}
	
	public Neo4jDaoWithWrappedRest() {
		if( ! LAZY_INIT) {
			init();
		}
	}
	
	
    
	private RestGraphDatabase getRestGraphDatabase() {
		if(null == restGraphDatabase)
			synchronized (this) {
				if(null == restGraphDatabase)
					init();
			}
		return restGraphDatabase;
	}

	private RestAPI getRestAPI() {
		if(null == restAPI)
			synchronized (this) {
				if(null == restAPI)
					init();
			}
		return restAPI;
	}

	private QueryEngine getQueryEngine() {
		if(null == queryEngine)
			synchronized (this) {
				if(null == queryEngine)
					init();
			}
		return queryEngine;
	}

	@Override
	public boolean insertUser(UserDTO user) {
		try{
			
			String statement = getInsertCypherQuery(user);
			getQueryEngine().query(statement, null);
		}catch(Exception exc) {
			log.error(
					String.format("Could not insert user:'%s' Exception: %s", 
							user.toString(), exc.toString()) 
					 );
			return false;
		}
		//TODO: Maybe we should insert the ID for the inserted user? (vimaier)
		return true;
	}

	@Override
	public UserDTO updateUser(UserDTO user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean deleteUser(UserDTO user) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Collection<UserDTO> findFriends(UserDTO user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	/**
	 * @param suchbegriff Should be mailadress since it is a unique ID
	 */
	public Collection<UserDTO> findUsers(String suchbegriff) {
		final String mailaddress = suchbegriff;
		final String queryString = "match (n) where n.mailaddress = {ref_mailaddress} return n";
        final Iterator resultIter = queryEngine.query(queryString, MapUtil.map("ref_mailaddress", mailaddress)).to(Node.class).iterator();
        
        Collection<UserDTO> foundUsers = new ArrayList<UserDTO>();
        while(resultIter.hasNext()) {
        	Node node = (Node) resultIter.next();
        	foundUsers.add(createUserFromNode(node));
        }
        return foundUsers;
	}

	@Override
	public boolean makeFriends(UserDTO user, UserDTO friend) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean unfriend(UserDTO user, UserDTO friend) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Collection<UserDTO> findFriendsOfFriends(Collection<UserDTO> friends) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<UserDTO> findRelative(UserDTO user) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	public String getInsertCypherQuery(UserDTO u) {
		if (u.getMailadress() == null)
			return null;

		StringBuilder sb = new StringBuilder();
		
		//TODO: We should switch from hardcoded strings to variables (vimaier)
		sb.append("create (u:" + UserDTO.LABEL + "{");
		if (u.getForname() != null)
			sb.append("forename: '" + u.getForname() + "',");
		if (u.getSurname() != null)
			sb.append("surname: '" + u.getSurname() + "',");
		if (u.getMailadress() != null)
			sb.append("mailaddress: '" + u.getMailadress() + "',");
		if (u.getStreet() != null)
			sb.append("street: '" + u.getStreet() + "',");
		if (u.getHousenumber() != null)
			sb.append("housenumber: '" + u.getHousenumber() + "',");
		if (u.getPostcode() != null)
			sb.append("postcode: '" + u.getPostcode() + "',");
		if (u.getCity() != null)
			sb.append("city: '" + u.getCity() + "',");
		if (u.getPassword() != null)
			sb.append("password: '" + u.getPassword() + "',");
		sb.deleteCharAt(sb.length() - 1);
		sb.append(" }) return u");

		return sb.toString();
	}
	

	private UserDTO createUserFromNode(Node node) {
		return new UserDTO( (String) node.getProperty("forename", ""), 
				(String) node.getProperty("surname", ""), 
				(String) node.getProperty("mailaddress", ""), 
				(String) node.getProperty("street", ""), 
				(String) node.getProperty("housenumber", ""), 
				(String) node.getProperty("postcode", ""), 
				(String) node.getProperty("city", ""), 
				(String) node.getProperty("password", ""), 
				null // We did not load the picture here (vimaier)
				);
	}

  @Override
  public UserDTO loginUser(LoginDTO login) {
    // TODO Auto-generated method stub
    return null;
  }
}


