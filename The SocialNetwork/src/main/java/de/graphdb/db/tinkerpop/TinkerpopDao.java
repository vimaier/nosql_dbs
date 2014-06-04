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
import de.graphdb.dto.LoginDTO;
import de.graphdb.dto.UserDTO;

public class TinkerpopDao implements GraphDBInterface {

	// Rexster-object for client connection
	private RexsterClient client;

	private boolean clientStatus = false;

	// Defines strings for connection
	private final String serverIP = "10.0.107.174"; // ip of the server
	private final String serverDB = "neo4jsample"; // name of the
													// database

	// Connect to Rexster-client
	public void connect() {
		try {
			if (clientStatus == false) {
				clientStatus = true;
				client = RexsterClientFactory.open(serverIP, serverDB);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Disconnect connection to Rexster
	public void disconnect() {
		try {
			client.close();
			clientStatus = false;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public UserDTO mapToUserDTO(Map<String, Object> currMap)
	{
		UserDTO newUser = new UserDTO();

		if (currMap.get("forname") != null)
			newUser.setForename(currMap.get("forname").toString());
		if (currMap.get("surname") != null)
			newUser.setSurname(currMap.get("surname").toString());
		if (currMap.get("mailadress") != null)
		{
			newUser.setMailadress(currMap.get("mailadress").toString());
			newUser.setId(getVIdByEmail(newUser).toString());
		}
		if (currMap.get("street") != null)
			newUser.setStreet(currMap.get("street").toString());
		if (currMap.get("housenumber") != null)
			newUser.setHousenumber(currMap.get("housenumber").toString());
		if (currMap.get("postcode") != null)
			newUser.setPostcode(currMap.get("postcode").toString());
		if (currMap.get("city") != null)
			newUser.setCity(currMap.get("city").toString());
		if (currMap.get("password") != null)
			newUser.setPassword(currMap.get("password").toString());
		if (currMap.get("picture_up") != null)
			newUser.setPicture_up((CommonsMultipartFile) currMap
					.get("picture_up"));

		return newUser;
	}
	
	/**
	 * Get the Id of a User by email-adress
	 * @param user
	 * @return 
	 */	
	private Object getVIdByEmail(UserDTO user) {
		List<Map<String, Object>> results;
		Object id = null;

		try {
			connect();

			if (user.getMailadress() != null) 
			{
				results = client.execute("g.V.has('mailadress', '" + user.getMailadress() + "').id");
				
				id = results.get(0);
			}
		} catch (RexProException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return id;
	}
	
	@Override
	public UserDTO getUserById(String id) {
		return null;
	}

	@Override
	public boolean insertUser(UserDTO user) {
		boolean ret = false;
		System.out.println("----------------------------------------------------------------------- BIN DRIN");
		try {
			connect();

			if (user.getMailadress() != null) {
				StringBuilder sb = new StringBuilder();

				// beginn
				sb.append("g.addVertex([");
				if (user.getForename() != null)
					sb.append("forname: '" + user.getForename() + "',");
				if (user.getSurname() != null)
					sb.append("surname: '" + user.getSurname() + "',");
				if (user.getMailadress() != null)
					sb.append("mailadress: '" + user.getMailadress() + "',");
				if (user.getStreet() != null)
					sb.append("street: '" + user.getStreet() + "',");
				if (user.getHousenumber() != null)
					sb.append("housenumber: '" + user.getHousenumber() + "',");
				if (user.getPostcode() != null)
					sb.append("postcode: '" + user.getPostcode() + "',");
				if (user.getCity() != null)
					sb.append("city: '" + user.getCity() + "',");
				if (user.getPassword() != null)
					sb.append("password: '" + user.getPassword() + "',");
				// entferne letztes komma
				sb.deleteCharAt(sb.length() - 1);
				// ende
				sb.append("])");

				List<Map<String, Object>> results = client.execute(sb
						.toString());

				if (results.size() > 0) {
					Map resMap = results.get(0);
					boolean hasId = resMap.containsKey("_id");

					if (true == hasId) {
						ret = true;
					}
				}
				
				System.out.println("----------------------------------------------------------------------- BUILD STRING");
			}

			disconnect();

		} catch (Exception e) {
			System.out.println(e.toString());
			disconnect();
		}
		System.out.println("----------------------------------------------------------------------- ENDE INSERT");
		return ret;
	}

	@Override
	public UserDTO updateUser(UserDTO user) {
		UserDTO userRet = user;
		Object id = getVIdByEmail(user);

		// Setze Id im UserId
		userRet.setId(id.toString());
		
		try {
			if (id != null) {
				if (user.getForename() != null)
					client.execute("g.v(" + id + ").forname='" + user.getForename() + "'");
				if (user.getSurname() != null)
					client.execute("g.v(" + id + ").surname='" + user.getSurname() + "'");
				if (user.getStreet() != null)
					client.execute("g.v(" + id + ").street='" + user.getStreet() + "'");
				if (user.getHousenumber() != null)
					client.execute("g.v(" + id + ").housenumber='" + user.getHousenumber() + "'");
				if (user.getPostcode() != null)
					client.execute("g.v(" + id + ").postcode='"	+ user.getPostcode() + "'");
				if (user.getCity() != null)
					client.execute("g.v(" + id + ").city='" + user.getCity() + "'");
				if (user.getPassword() != null)
					client.execute("g.v(" + id + ").password='"	+ user.getPassword() + "'");
			} else {
				userRet = null;
			}
		} catch (RexProException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			disconnect();
		}

		return userRet;
	}

	@Override
	public boolean deleteUser(UserDTO user) {
		boolean ret = false;

		Object id = getVIdByEmail(user);

		try {
			List<Map<String, Object>> results = client.execute("g.v(" + id
					+ ").remove()");

			if (results.size() == 1) {
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
			List<Map<String, Object>> results = client.execute("g.v(" + id
					+ ").outE('friends').inV.map");

			Iterator<Map<String, Object>> it = results.iterator();

			while (it.hasNext()) {
				Map<String, Object> currMap = it.next();

				
//				UserDTO newUser = new UserDTO();
//
//				if (currMap.get("forname") != null)
//					newUser.setForename(currMap.get("forname").toString());
//				if (currMap.get("surname") != null)
//					newUser.setSurname(currMap.get("surname").toString());
//				if (currMap.get("mailadress") != null)
//				{
//					newUser.setMailadress(currMap.get("mailadress").toString());
//					newUser.setId(getVIdByEmail(newUser).toString());
//				}
//				if (currMap.get("street") != null)
//					newUser.setStreet(currMap.get("street").toString());
//				if (currMap.get("housenumber") != null)
//					newUser.setHousenumber(currMap.get("housenumber").toString());
//				if (currMap.get("postcode") != null)
//					newUser.setPostcode(currMap.get("postcode").toString());
//				if (currMap.get("city") != null)
//					newUser.setCity(currMap.get("city").toString());
//				if (currMap.get("password") != null)
//					newUser.setPassword(currMap.get("password").toString());
//				if (currMap.get("picture_up") != null)
//					newUser.setPicture_up((CommonsMultipartFile) currMap
//							.get("picture_up"));
				
				UserDTO newUser = mapToUserDTO(currMap);
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

			while (it.hasNext()) {
				Object id = it.next();

				values = client.execute("g.v(" + id + ").values()");

				if (values.contains(suchbegriff)) {
					List<Map<String, Object>> userData = client.execute("g.v(" + id + ").map");

					Iterator<Map<String, Object>> itUserData = userData
							.iterator();

					while (itUserData.hasNext()) {
						Map<String, Object> userDataMap = itUserData.next();

						UserDTO findUser = mapToUserDTO(userDataMap);
						coll.add(findUser);
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
			if (userId != null && friendId != null) {
				List<Map<String, Object>> results = client.execute("v1=g.v("
						+ userId + ");" + "v2=g.v(" + friendId + ");"
						+ "g.addEdge(v1,v2,'friends')");

				if (results.size() == 1) {
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
			connect();
			
			if (userId != null && friendId != null) {
				List<Map<String, Object>> results = client.execute(
						"g.v(" + userId	+ ")."
								+ "outE('friends').as('x')."
								+ "inV.has('mailadress', '" + friend.getMailadress() + "')."
								+ "back('x').remove()");
				
				if (results.size() == 1) {
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
	public Collection<UserDTO> findFriendsOfFriends(UserDTO user) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * List<Map<String, Object>> results =
	 * client.execute("g.v(1).outE('knows').inV.outE('created').inV");
	 */
	// @Override
	// public Collection<UserDTO> findFriendsOfFriends(Collection<UserDTO>
	// friends) {
	//
	// Collection<UserDTO> friendsCollection = new LinkedList<UserDTO>();
	//
	// Iterator<UserDTO> it = friends.iterator();
	//
	// while(it.hasNext())
	// {
	// UserDTO user = it.next();
	//
	// Collection<UserDTO> findFriendsCollection = findFriends(user);
	//
	// Iterator<UserDTO> it2 = findFriendsCollection.iterator();
	//
	// while(it2.hasNext())
	// {
	// friendsCollection.add(it2.next());
	// }
	// }
	//
	// return friendsCollection;
	// }

	@Override
	public Collection<UserDTO> findRelative(UserDTO user) {

		return null;
	}

	@Override
	public UserDTO loginUser(LoginDTO login) {
		System.out.println("#####################################################################");
		System.out.println(login.toString());
		
		if (login.getMailadress() != null && login.getPassword() != null) {
			
			try {
				connect();
				
				List<Map<String, Object>> results = client.execute("g.V.has('mailadress', '" + login.getMailadress() + "').has('password', '"	+ login.getPassword() + "').map");

				System.out.println("g.V.has('mailadress', '" + login.getMailadress() + "').has('password', '"	+ login.getPassword() + "').map");
				
				Iterator<Map<String, Object>> itUserData = results.iterator();

				while (itUserData.hasNext()) {
					Map<String, Object> userDataMap = itUserData.next();

					UserDTO newUser = new UserDTO();

					newUser.setForename(userDataMap.get("forname").toString());
					newUser.setSurname(userDataMap.get("surname").toString());
					newUser.setMailadress(userDataMap.get("mailadress")
							.toString());
					newUser.setStreet(userDataMap.get("street").toString());
					newUser.setHousenumber(userDataMap.get("housenumber")
							.toString());
					newUser.setPostcode(userDataMap.get("postcode").toString());
					newUser.setCity(userDataMap.get("city").toString());
					newUser.setPassword(userDataMap.get("password").toString());
					newUser.setPicture_up((CommonsMultipartFile) userDataMap
							.get("picture_up"));
										
					return newUser;
				}
			} catch (RexProException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				disconnect();
			}
		}
		
		return null;
	}
}
