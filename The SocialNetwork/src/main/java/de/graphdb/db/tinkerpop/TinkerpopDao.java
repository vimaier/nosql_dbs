package de.graphdb.db.tinkerpop;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.tinkerpop.rexster.client.RexProException;
import com.tinkerpop.rexster.client.RexsterClient;
import com.tinkerpop.rexster.client.RexsterClientFactory;

import de.graphdb.db.GraphDBInterface;
import de.graphdb.dto.UserDTO;

public class TinkerpopDao implements GraphDBInterface {

	// Rexster-object for client connection
	private RexsterClient client;

	private boolean clientStatus = false;  
	
	// Defines strings for connection
	private final String serverIP = "10.0.3.44"; // ip of the server
	private final String serverDB = "tinkergraph"; // name of the
															// database

	// Connect to Rexster-client
	private void connect() {
		try {
			if(clientStatus == false){
				clientStatus = true;
				client = RexsterClientFactory.open(serverIP, serverDB);				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Disconnect connection to Rexster
	private void disconnect() {
		try {
			client.close();
			clientStatus = false;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private Object getVIdByEmail(UserDTO user){
		List<Map<String, Object>> results;
		Object id = null;
		
		try {
			connect();
			
			if(user.getMailadress() != null){
				results = client.execute("g.V.filter{it.mailadress=='" + user.getMailadress() + "'");
				
				if(results.size() == 1)
				{
					id = results.get(0).get("_Id");
				}
			}
		} catch (RexProException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return id;
	}

	@Override
	public boolean insertUser(UserDTO user) {
		boolean ret = false;
		
		try {
			// String gremlinQuery = user.getInsertGremlinQuery();

			connect();

			List<Map<String, Object>> results = client.execute("g.addVertex(["
					+ "forname: " + user.getForname() + ","
					+ "surname: " + user.getSurname() + ","
					+ "mailadress: " + user.getMailadress()	+ ","
					+ "street: " + user.getStreet() + ","
					+ "housenumber: " + user.getHousenumber() + ","
					+ "postcode: " + user.getPostcode() + ","
					+ "city: " + user.getCity() + ","
					+ "password: " + user.getPassword() + ","
					+ "])");

			if (results.size() > 0) {
				Map resMap = results.get(0);
				boolean hasId = resMap.containsKey("_id");

				if (true == hasId) {
					ret = true;
				}
			}

			disconnect();
		} catch (Exception e) {
			disconnect();
		}
		
		return ret;
	}

	@Override
	public UserDTO updateUser(UserDTO user) {		
		UserDTO userRet = user; 
		Object id = getVIdByEmail(user);
		
		try {
			if(id != null)
			{				
				if(user.getForname() != null) 
					client.execute("g.v(" + id + ").forname='" + user.getForname() + "'");
				if(user.getSurname() != null)
					client.execute("g.v(" + id + ").surname='" + user.getSurname() + "'");
				if(user.getStreet() != null)
					client.execute("g.v(" + id + ").street='" + user.getStreet() + "'");
				if(user.getHousenumber() != null)
					client.execute("g.v(" + id + ").housenumber='" + user.getHousenumber() + "'");
				if(user.getPostcode() != null)
					client.execute("g.v(" + id + ").postcode='" + user.getPostcode() + "'");
				if(user.getCity() != null)
					client.execute("g.v(" + id + ").city='" + user.getCity() + "'");
				if(user.getPassword() != null)
					client.execute("g.v(" + id + ").password='" + user.getPassword() + "'");					
			} else {
				userRet = null;
			}	
		} catch (RexProException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			disconnect();
		}
		
		return userRet;
	}

	@Override
	public boolean deleteUser(UserDTO user) {
		boolean ret = false;
		
		Object id = getVIdByEmail(user);
		
		try {
			List<Map<String, Object>> results = client.execute("g.v(" + id + ").remove()");
			
			if(results.size() == 1)
			{
				ret = true;
			}
		} catch (RexProException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ret;
	}

	@Override
	public Collection<UserDTO> findFriends(UserDTO user) {
		Collection<UserDTO> coll = new LinkedList<UserDTO>();
		
		Object id = getVIdByEmail(user);
		
		try {
			List<Map<String, Object>> results = client.execute("g.v(" + id + ").outE('friends').inV.map");
			
			Iterator<Map<String, Object>> it = results.iterator();
			
			while(it.hasNext())
			{
				Map<String, Object> currMap = it.next();
				
				UserDTO newUser = new UserDTO();
				
				newUser.setForname(currMap.get("forname").toString());
				newUser.setSurname(currMap.get("surname").toString());
				newUser.setMailadress(currMap.get("mailadress").toString());
				newUser.setStreet(currMap.get("street").toString());
				newUser.setHousenumber(currMap.get("housenumber").toString());
				newUser.setPostcode(currMap.get("postcode").toString());
				newUser.setCity(currMap.get("city").toString());
				newUser.setPassword(currMap.get("password").toString());
				newUser.setPicture_up((CommonsMultipartFile) currMap.get("picture_up"));
				
				coll.add(newUser);
			}
			
		} catch (RexProException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return coll;
	}

	@Override
	public Collection<UserDTO> findUsers(String suchbegriff) {
		Collection<UserDTO> coll = new LinkedList<UserDTO>();
		
		try {
			connect();
			
			List<Map<String, Object>> results = client.execute("g.V.id");
			
			Iterator<Map<String, Object>> it = results.iterator();
			
			List<Map<String, Object>> values;
			
			while(it.hasNext())
			{
				Object id = it.next();
				
				values = client.execute("g.v(" + id + ").values()");
				
				if(values.contains(suchbegriff))
				{
					List<Map<String, Object>> userData = client.execute("g.v(" + id + ").map");
					
					Iterator<Map<String, Object>> itUserData = userData.iterator();
					
					while(itUserData.hasNext())
					{
						Map<String, Object> userDataMap = itUserData.next();
						
						UserDTO newUser = new UserDTO();
						
						newUser.setForname(userDataMap.get("forname").toString());
						newUser.setSurname(userDataMap.get("surname").toString());
						newUser.setMailadress(userDataMap.get("mailadress").toString());
						newUser.setStreet(userDataMap.get("street").toString());
						newUser.setHousenumber(userDataMap.get("housenumber").toString());
						newUser.setPostcode(userDataMap.get("postcode").toString());
						newUser.setCity(userDataMap.get("city").toString());
						newUser.setPassword(userDataMap.get("password").toString());
						newUser.setPicture_up((CommonsMultipartFile) userDataMap.get("picture_up"));
						
						coll.add(newUser);						
					}
				}
			}
		} catch (RexProException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		disconnect();
		return coll;
	}

	@Override
	public boolean makeFriends(UserDTO user, UserDTO friend) {		
		boolean ret = false;
		
		Object userId = getVIdByEmail(user);
		Object friendId = getVIdByEmail(friend);
		
		try {
			if(userId != null && friendId != null)
			{
				List<Map<String, Object>> results = client.execute(
						"v1=g.v(" + userId + ");"
						+ "v2=g.v(" + friendId + ");"
						+ "g.addEdge(v1,v2,'friends')");
				
				if(results.size() == 1)
				{
					ret = true;
				}
			}
		} catch (RexProException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		disconnect();
		return ret;
	}

	@Override
	public boolean unfriend(UserDTO user, UserDTO friend) {
		boolean ret = false;
		
		Object userId = getVIdByEmail(user);
		Object friendId = getVIdByEmail(friend);
		
		try {
			if(userId != null && friendId != null)
			{
				List<Map<String, Object>> results = client.execute("g.v(" + userId + ").outE('friends').as('x').inV.has('email', '" + friend.getMailadress() + "').back('x').remove()");
				
				if(results.size() == 1)
				{
					ret = true;
				}
			}
		} catch (RexProException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		disconnect();
		return ret;
	}

	/* List<Map<String, Object>> results = client.execute("g.v(1).outE('knows').inV.outE('created').inV"); */
	@Override
	public Collection<UserDTO> findFriendsOfFriends(Collection<UserDTO> friends) {
		
		Collection<UserDTO> friendsCollection = new LinkedList<UserDTO>();
		
		Iterator<UserDTO> it = friends.iterator();
		
		while(it.hasNext())
		{
			UserDTO user = it.next();
			
			Collection<UserDTO> findFriendsCollection = findFriends(user);
			
			Iterator<UserDTO> it2 = findFriendsCollection.iterator();
			
			while(it2.hasNext())
			{
				friendsCollection.add(it2.next());
			}
		}
		
		return friendsCollection;
	}

	@Override
	public Collection<UserDTO> findRelative(UserDTO user) {
		
		return null;
	}
}
