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
//	private final String serverDB = "tinkergraph"; // name of the database
	private final String serverDB = "neo4jsample"; // name of the database

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

	public UserDTO mapToUserDTO(Map<String, Object> currMap) {
		UserDTO newUser = new UserDTO();

		if (currMap.get("forname") != null)
			newUser.setForename(currMap.get("forname").toString());
		if (currMap.get("surname") != null)
			newUser.setSurname(currMap.get("surname").toString());
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
		if (currMap.get("mailadress") != null)
		{
			newUser.setMailadress(currMap.get("mailadress").toString());
			newUser.setId(getVIdByEmail(newUser).toString());
		}
		
		
		return newUser;
	}

	private Object getVIdByEmail(UserDTO user) {
		Object id = null;

		try {
			connect();

			if (user.getMailadress() != null) {
				List<Map<String, Object>> results = client.execute("g.V.has('mailadress', '" + user.getMailadress() + "').id");
				id = results.get(0);
			} else {
				System.out.println("****************************************************************************************************");
				
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
		UserDTO user = new UserDTO();
		
		try {
			connect();
			
			// Gremlin-Query in der Konsole ausgeben
			System.out.println("***** getUserById - Gremlin: g.v(" + id + ").map");

			List<Map<String, Object>> results = client.execute("g.v(" + id + ").map");
			
			Iterator<Map<String, Object>> itUserData = results.iterator();

			while (itUserData.hasNext()) {
				Map<String, Object> userDataMap = itUserData.next();

				UserDTO newUser = new UserDTO();

				// Setze Eigenschaften in das UserDTO-Objekt
				newUser.setForename(userDataMap.get("forname").toString());
				newUser.setSurname(userDataMap.get("surname").toString());
				newUser.setMailadress(userDataMap.get("mailadress").toString());
				newUser.setStreet(userDataMap.get("street").toString());
				newUser.setHousenumber(userDataMap.get("housenumber").toString());
				newUser.setPostcode(userDataMap.get("postcode").toString());
				newUser.setCity(userDataMap.get("city").toString());
				newUser.setPassword(userDataMap.get("password").toString());
				newUser.setPicture_up((CommonsMultipartFile) userDataMap.get("picture_up"));
				
				// Wichtig, dass die ID erst hier gesetzt wird, weil vorher in User keine eMail gesetzt ist 
				newUser.setId(getVIdByEmail(newUser).toString());
				
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
		
		return user;		
	}

	@Override
	public boolean insertUser(UserDTO user) {
		boolean ret = false;
		
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
			}

			disconnect();

		} catch (Exception e) {
			System.out.println(e.toString());
			disconnect();
		}

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
					client.execute("g.v(" + id + ").forname='"
							+ user.getForename() + "'");
				if (user.getSurname() != null)
					client.execute("g.v(" + id + ").surname='"
							+ user.getSurname() + "'");
				if (user.getStreet() != null)
					client.execute("g.v(" + id + ").street='"
							+ user.getStreet() + "'");
				if (user.getHousenumber() != null)
					client.execute("g.v(" + id + ").housenumber='"
							+ user.getHousenumber() + "'");
				if (user.getPostcode() != null)
					client.execute("g.v(" + id + ").postcode='"
							+ user.getPostcode() + "'");
				if (user.getCity() != null)
					client.execute("g.v(" + id + ").city='" + user.getCity()
							+ "'");
				if (user.getPassword() != null)
					client.execute("g.v(" + id + ").password='"
							+ user.getPassword() + "'");
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

			// Gremlin-Query in der Konsole ausgeben
			System.out.println("g.v(" + id + ").remove()");
			
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
		// Id des Users finden und in ein neues UserObjekt speichern
		UserDTO id = getUserById(user.getId());

		Collection<UserDTO> coll = new LinkedList<UserDTO>();
		
		try {
			connect();
			
			// Gremlin-Query in der Konsole ausgeben
			System.out.println("####################################################################################");
			System.out.println("***** findFriends - Gremlin: g.v(" + user.getId() + ").outE('friends').inV.map");
			
			List<Map<String, Object>> results = client.execute("g.v(" + id.getId() + ").outE('friends').inV.map");
			
//			List<Map<String, Object>> results = client.execute("g.v(" + user.getId() + ").outE('friends').inV.map");

			
			Iterator<Map<String, Object>> it = results.iterator();

			while (it.hasNext()) {
				Map<String, Object> currMap = it.next();
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
			
			// Finde zuerst alle Vertices und speichere sie in results
			List<Map<String, Object>> results = client.execute("g.V.id");
			Iterator<Map<String, Object>> it = results.iterator();
			
			// Gremlin-Query in der Konsole ausgeben
			System.out.println("***** Gremlin: g.V.id");
			
			// Lege Liste an für die while-Schleife
			List<Map<String, Object>> values;

			// Prüfe jeden Vertex, ob der Suchbegriff enthalten ist 
			while (it.hasNext()) {
				Object id = it.next();
				
				// Frage alle Werte eines Vertex ab und speichere sie in values
				values = client.execute("g.v(" + id + ").values()");

				// Gremlin-Query in der Konsole ausgeben
				System.out.println("***** Gremlin: g.v(" + id + ").values()");
				
				// Values auf Suchbegriff durchsuchen
				if (values.contains(suchbegriff)) {
					List<Map<String, Object>> userData = client.execute("g.v(" + id + ").map");

					Iterator<Map<String, Object>> itUserData = userData.iterator();

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
				List<Map<String, Object>> results = client.execute("v1=g.v(" + userId + ");" + "v2=g.v(" + friendId + ");"
																+ "g.addEdge(v1,v2,'friends');"
																+ "g.addEdge(v2,v1,'friends')");

				// Gremlin-Query in der Konsole ausgeben
				System.out.println("***** makeFriends - Gremlin: v1=g.v(" + userId + ");" + "v2=g.v(" + friendId + ");" + "g.addEdge(v1,v2,'friends')");
				
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
//				List<Map<String, Object>> results = client.execute("g.v(" + userId + ")." + "outE('friends').as('x')." + "inV.has('mailadress', '" + friend.getMailadress()	+ "')." + "back('x').remove()"
//																	);
				
				List<Map<String, Object>> results = client.execute("g.v(" + userId + ").bothE('friends').as('x').bothV.has('mailadress', '" + friend.getMailadress() + "').back('x').remove()");
				
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
		if (login.getMailadress() != null && login.getPassword() != null) {
			try {
				connect();
				
				List<Map<String, Object>> results = client
						.execute("g.V.has('mailadress', '"
								+ login.getMailadress()
								+ "').has('password', '" + login.getPassword()
								+ "').map");

				// Gremlin-Query in der Konsole ausgeben
				System.out.println("***** Gremlin: g.V.has('mailadress', '" + login.getMailadress() + "').has('password', '" + login.getPassword() + "').map");
				
				Iterator<Map<String, Object>> itUserData = results.iterator();

				while (itUserData.hasNext()) {
					Map<String, Object> userDataMap = itUserData.next();

					UserDTO newUser = new UserDTO();

					// Setze Eigenschaften in das UserDTO-Objekt
					newUser.setForename(userDataMap.get("forname").toString());
					newUser.setSurname(userDataMap.get("surname").toString());
					newUser.setMailadress(userDataMap.get("mailadress").toString());
					newUser.setStreet(userDataMap.get("street").toString());
					newUser.setHousenumber(userDataMap.get("housenumber").toString());
					newUser.setPostcode(userDataMap.get("postcode").toString());
					newUser.setCity(userDataMap.get("city").toString());
					newUser.setPassword(userDataMap.get("password").toString());
					newUser.setPicture_up((CommonsMultipartFile) userDataMap.get("picture_up"));
					
					// Wichtig, dass die ID erst hier gesetzt wird, weil vorher in User keine eMail gesetzt ist 
					newUser.setId(getVIdByEmail(newUser).toString());
					
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
