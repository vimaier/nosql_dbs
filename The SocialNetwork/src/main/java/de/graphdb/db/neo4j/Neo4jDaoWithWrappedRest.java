package de.graphdb.db.neo4j;

import java.util.Collection;

import org.neo4j.rest.graphdb.RestAPI;
import org.neo4j.rest.graphdb.RestGraphDatabase;
import org.neo4j.rest.graphdb.query.QueryEngine;
import org.neo4j.rest.graphdb.query.RestCypherQueryEngine;

import de.graphdb.db.GraphDBInterface;
import de.graphdb.dto.UserDTO;

public class Neo4jDaoWithWrappedRest implements GraphDBInterface {

	private static final String SERVER_ROOT_URI = "http://localhost:7474/db/data/";
	
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
		// TODO Auto-generated method stub
		return false;
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
	public Collection<UserDTO> findUsers(String suchbegriff) {
		// TODO Auto-generated method stub
		return null;
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

}
