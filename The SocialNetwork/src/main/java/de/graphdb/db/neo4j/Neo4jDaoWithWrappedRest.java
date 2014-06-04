package de.graphdb.db.neo4j;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;

import org.neo4j.graphdb.Node;
import org.neo4j.helpers.collection.MapUtil;
import org.neo4j.rest.graphdb.RestAPI;
import org.neo4j.rest.graphdb.RestGraphDatabase;
import org.neo4j.rest.graphdb.query.QueryEngine;
import org.neo4j.rest.graphdb.query.RestCypherQueryEngine;
import org.neo4j.rest.graphdb.util.ConvertedResult;
import org.neo4j.rest.graphdb.util.QueryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.graphdb.db.GraphDBInterface;
import de.graphdb.dto.LoginDTO;
import de.graphdb.dto.UserDTO;
import de.graphdb.dto.UserDTO.RELATIONSHIPS;

/*
 * Some good examples...
 * https://github.com/neo4j/java-rest-binding/blob/1590210bf1097411d208fa977f83a60a5677c277/src/test/java/org/neo4j/rest/graphdb/RestCypherQueryEngineTest.java
 * 
 */
public class Neo4jDaoWithWrappedRest implements GraphDBInterface {

	private static final String SERVER_ROOT_URI = "http://10.0.107.147:7474/db/data/";
	
	
	private Logger log = LoggerFactory.getLogger(Neo4jDaoWithWrappedRest.class);
	
	private final boolean LAZY_INIT = true;
	private RestGraphDatabase restGraphDatabase;
	private RestAPI restAPI;
	private QueryEngine<?> queryEngine;
	
	private void init() {
		restGraphDatabase = new RestGraphDatabase(SERVER_ROOT_URI);
		restAPI = getRestGraphDatabase().getRestAPI();
		queryEngine = new RestCypherQueryEngine(getRestAPI()); 
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

	private QueryEngine<?> getQueryEngine() {
		if(null == queryEngine)
			synchronized (this) {
				if(null == queryEngine)
					init();
			}
		return queryEngine;
	}
	

	private final String INSERT_USER_QUERY = "CREATE (u:" + UserDTO.LABEL +
			" {forename:{ref_forename}, surname:{ref_surename}, street:{ref_street}, housenumber:{ref_housenumber}, " +
			"mailaddress:{ref_mailaddress}, postcode:{ref_postcode}, city:{ref_city}, password:{ref_password}}) RETURN u";
	@Override
	public boolean insertUser(UserDTO user) {
		if (null == user.getMailadress()){
			log.error("Tried to insert a user but mailaddress is null User: "+user.toString());
			return false;
		}
		try{
			Iterator<Node> resIterator = getQueryEngine().query(INSERT_USER_QUERY, 
					MapUtil.map("ref_forename", user.getForename(), "ref_surename", user.getSurname(),
							"ref_street", user.getStreet(), "ref_housenumber", user.getHousenumber(),
							"ref_postcode", user.getPostcode(), "ref_city", user.getCity(),
							"ref_password", user.getPassword(), "ref_mailaddress", user.getMailadress()
					)).to(Node.class).iterator();
			Node createdNode = resIterator.next();
			user.setId(Long.toString(createdNode.getId()));
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
		try{
			String statement = "MATCH (u:"+UserDTO.LABEL+") WHERE u.mailaddress={ref_mailaddress} SET " +
					"u.forename={ref_forename}, u.surname={ref_surename}, u.street={ref_street}, u.housenumber={ref_housenumber}, " +
					"u.postcode={ref_postcode}, u.city={ref_city}, u.password={ref_password} return u";
			getQueryEngine().query(statement, 
					MapUtil.map("ref_forename", user.getForename(), "ref_surename", user.getSurname(),
							"ref_street", user.getStreet(), "ref_housenumber", user.getHousenumber(),
							"ref_postcode", user.getPostcode(), "ref_city", user.getCity(),
							"ref_password", user.getPassword(), "ref_mailaddress", user.getMailadress()
					));
		}catch(Exception exc) {
			log.error(
					String.format("Could not update user:'%s' Exception: %s", 
							user.toString(), exc.toString()) 
					 );
			return null;
		}
		return null;
	}

	@Override
	public boolean deleteUser(UserDTO user) {
		try{
			String statement = "MATCH (n:"+UserDTO.LABEL+") WHERE n.mailaddress={ref_mailaddress} OPTIONAL MATCH (n)-[r]-() DELETE n,r";
			getQueryEngine().query(statement, MapUtil.map("ref_mailaddress", user.getMailadress()));
		}catch(Exception exc) {
			log.error(
					String.format("Could not delete user:'%s' Exception: %s", 
							user.toString(), exc.toString()) 
					 );
			return false;
		}
		return true;
	}

	@Override
	public Collection<UserDTO> findFriends(UserDTO user) {
		final String queryString = "match (u:"+UserDTO.LABEL+")-[:" + RELATIONSHIPS.FRIENDS + "]->(f:"+UserDTO.LABEL+")" +
				"where u.mailaddress = {ref_mailaddress} return f";
        final Iterator<Node> resultIter = getQueryEngine().query(queryString, MapUtil.map("ref_mailaddress", user.getMailadress())).to(Node.class).iterator();
        
        Collection<UserDTO> friends = new ArrayList<UserDTO>();
        while(resultIter.hasNext()) {
        	Node node = resultIter.next();
        	friends.add(createUserFromNode(node));
        }
        return friends;
	}

	@Override
	/**
	 * @param suchbegriff Should be mailadress since it is a unique ID
	 */
	public Collection<UserDTO> findUsers(String suchbegriff) {
		final String mailaddress = suchbegriff;
		//FIXME: this is not correct... should be a search over all nodes (vimaier)
		final String queryString = "match (u:"+UserDTO.LABEL+") where u.mailaddress = {ref_mailaddress} return u";
        final Iterator<Node> resultIter = getQueryEngine().query(queryString, MapUtil.map("ref_mailaddress", mailaddress)).to(Node.class).iterator();
        
        Collection<UserDTO> foundUsers = new ArrayList<UserDTO>();
        while(resultIter.hasNext()) {
        	Node node = resultIter.next();
        	foundUsers.add(createUserFromNode(node));
        }
        return foundUsers;
	}

	@Override
	public boolean makeFriends(UserDTO user, UserDTO friend) {
		final String queryString = "match (u:" + UserDTO.LABEL + "),(f:" + UserDTO.LABEL + ") where u.mailadress = {r_mail_1}" +
	            " and f.mailadress = {r_mail_2} create (u)-[r:" + RELATIONSHIPS.FRIENDS + " {since: {r_since}}]->(f)";
		String since = Calendar.getInstance().getTime().toString();
		try{
			getQueryEngine().query(queryString, 
					MapUtil.map("r_mail_1", user.getMailadress(), "r_mail_2", friend.getMailadress(), "r_since", since)
					);
		}catch(Exception exc) {
			log.error(String.format("Could not make friends between user(%s) and friend(%s)"), user.toString(), friend.toString()); 

			return false;
		}
		return true; // No check if user and friend actually exist...        
	}

	@Override
	public boolean unfriend(UserDTO user, UserDTO friend) {
		final String queryString = "match (u:" + UserDTO.LABEL + ")-[r:" + RELATIONSHIPS.FRIENDS + "]->(f:" + UserDTO.LABEL + ") " +
				"where u.mailadress = {r_mail_1} and f.mailadress = {r_mail_2} delete r";
		try{
			getQueryEngine().query(queryString, 
					MapUtil.map("r_mail_1", user.getMailadress(), "r_mail_2", friend.getMailadress() )
					);
		}catch(Exception exc) {
			log.error(String.format("Could not delete relation between user(%s) and friend(%s)"), user.toString(), friend.toString()); 

			return false;
		}
		return true; // No check if user and friend actually exist...  
	}

	@Override
	public Collection<UserDTO> findFriendsOfFriends(UserDTO user) {
		final String queryString = "match (u:"+UserDTO.LABEL+")-[:" + RELATIONSHIPS.FRIENDS + "]->(f:"+UserDTO.LABEL+")" +
				"-[:" + RELATIONSHIPS.FRIENDS + "]->(fof) " +
				"where u.mailaddress = {ref_mailaddress} return distinct fof";
        final Iterator<Node> resultIter = getQueryEngine().query(queryString, MapUtil.map("ref_mailaddress", user.getMailadress())).to(Node.class).iterator();
        
        Collection<UserDTO> friendsOfFriends = new ArrayList<UserDTO>();
        while(resultIter.hasNext()) {
        	Node node = resultIter.next();
        	friendsOfFriends.add(createUserFromNode(node));
        }
        return friendsOfFriends;
	}

	@Override
	public Collection<UserDTO> findRelative(UserDTO user) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	private UserDTO createUserFromNode(Node node) {
		UserDTO userDTO = new UserDTO( (String) node.getProperty("forename", ""), 
				(String) node.getProperty("surname", ""), 
				(String) node.getProperty("mailaddress", ""), 
				(String) node.getProperty("street", ""), 
				(String) node.getProperty("housenumber", ""), 
				(String) node.getProperty("postcode", ""), 
				(String) node.getProperty("city", ""), 
				(String) node.getProperty("password", ""), 
				null // We did not load the picture here (vimaier)
				);
		userDTO.setId( Long.toString(node.getId()) );
		
		return userDTO;
	}

  @Override
  public UserDTO loginUser(LoginDTO login) {
	  final String queryString = "match (u:"+UserDTO.LABEL+") where u.mailaddress = {ref_mailaddress} and u.password={ref_password} return u";
      final Iterator<Node> resultIter = getQueryEngine().query(queryString, 
    		  MapUtil.map("ref_mailaddress", login.getMailadress(), "ref_password", login.getPassword())
    		  ).to(Node.class).iterator();
      
      Collection<UserDTO> foundUsers = new ArrayList<UserDTO>();
      while(resultIter.hasNext()) {
      	Node node = resultIter.next();
      	foundUsers.add(createUserFromNode(node));
      }
      if(0 == foundUsers.size())
    	  return null;
      if(1 < foundUsers.size()) {
    	log.error(String.format("Database has several nodes for combination email(%s) and password(%s)", 
    			login.getMailadress(), login.getPassword()) ); 
    	return null;
      }
     return foundUsers.iterator().next();
  }

	private final String GET_NODE_BY_ID_QUERY = "START n=node({r_id}) RETURN n";
	@Override
	public UserDTO getUserById(String id) {
		try{
			QueryResult<?> queryResult = getQueryEngine().query(GET_NODE_BY_ID_QUERY, MapUtil.map("r_id", Long.parseLong(id)) );
			ConvertedResult<Node> convertedResult = queryResult.to(Node.class);
			Iterator<Node> iterator = convertedResult.iterator();
			if(iterator.hasNext())
				return createUserFromNode(iterator.next());
		}catch(Exception exc) {
			log.error(String.format("Exception in getting user by id(%s); Exception: %s", id, exc.toString())); 
			return null;
		}
		return null;
	}
}


