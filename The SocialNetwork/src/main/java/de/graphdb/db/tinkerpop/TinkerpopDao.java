package de.graphdb.db.tinkerpop;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.tinkerpop.rexster.client.RexsterClient;
import com.tinkerpop.rexster.client.RexsterClientFactory;

import de.graphdb.db.GraphDBInterface;
import de.graphdb.dto.UserDTO;

public class TinkerpopDao implements GraphDBInterface{
	
	// Rexster-object for client connection
	static RexsterClient client;
	
	// Defines strings for connection
	private static final String serverIP = "10.0.3.44"; // ip of the server
	private static final String serverDB = "tinkergraph"; // name of the database
	
	// Connect to Rexster-client
	public static void connect(){
		try {
			client = RexsterClientFactory.open(serverIP, serverDB);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// Disconnect connection to Rexster
	public static void disconnect(){
		try {
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean insertUser(UserDTO user) {
		try {
			String gremlinQuery = user.getInsertGremlinQuery();
			
			connect();
			
			System.out.println("Query:" + gremlinQuery);
			
			List<Map<String, Object>> results = client.execute(gremlinQuery);
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
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
