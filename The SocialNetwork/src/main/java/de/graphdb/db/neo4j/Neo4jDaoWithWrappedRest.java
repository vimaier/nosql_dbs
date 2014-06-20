package de.graphdb.db.neo4j;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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

	private static final String SERVER_ROOT_URI = "http://10.0.107.174:7474/db/data/";

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
		if (!LAZY_INIT) {
			init();
		}
	}

	private RestGraphDatabase getRestGraphDatabase() {
		if (null == restGraphDatabase)
			synchronized (this) {
				if (null == restGraphDatabase)
					init();
			}
		return restGraphDatabase;
	}

	private RestAPI getRestAPI() {
		if (null == restAPI)
			synchronized (this) {
				if (null == restAPI)
					init();
			}
		return restAPI;
	}

	private QueryEngine<?> getQueryEngine() {
		if (null == queryEngine)
			synchronized (this) {
				if (null == queryEngine)
					init();
			}
		return queryEngine;
	}
	
	public void executeCypherQuery(final String cypherQuery) {
		getQueryEngine().query(cypherQuery, null);
	}

	@Override
	public boolean insertUser(UserDTO user) {
		if (null == user.getMailadress()) {
			log.error("Tried to insert a user but mailadress is null User: "
					+ user.toString());
			return false;
		}

		try {
			Iterator<Node> resIterator = getQueryEngine()
					.query(getInsertCypherQuery(user),
							getCypherQueryMapUser(user)).to(Node.class)
					.iterator();
			Node createdNode = resIterator.next();
			user.setId(Long.toString(createdNode.getId()));
		} catch (Exception exc) {
			log.error(String.format("Could not insert user:'%s' Exception: %s",
					user.toString(), exc.toString()));
			return false;
		}

		return true;
	}

	@Override
	public UserDTO updateUser(UserDTO user) {
		if (null == user.getMailadress()) {
			log.error("Tried to update a user but mailadress is null. User: "
					+ user.toString());
			return null;
		}

		try {
			getQueryEngine().query(getUpdateCypherQuery(user),
					getCypherQueryMapUser(user));
		} catch (Exception exc) {
			log.error(String.format("Could not update user:'%s' Exception: %s",
					user.toString(), exc.toString()));
			return null;
		}
		return user;
	}

	@Override
	public boolean deleteUser(UserDTO user) {
		if (null == user.getMailadress()) {
			log.error("Tried to delete a user but mailadress is null. User: "
					+ user.toString());
			return false;
		}

		try {
			getQueryEngine().query(getDeleteCypherQuery(true),
					MapUtil.map("ref_mailadress", user.getMailadress()));
		} catch (Exception exc) {
			try {
				getQueryEngine().query(getDeleteCypherQuery(false),
						MapUtil.map("ref_mailadress", user.getMailadress()));
			} catch (Exception ex) {
				log.error(String.format(
						"Could not delete user:'%s' Exception: %s",
						user.toString(), exc.toString()));
				return false;
			}
		}
		return true;
	}

	@Override
	public Collection<UserDTO> findFriends(UserDTO user) {
		if (null == user.getMailadress()) {
			log.error("Tried to find friends of a user but mailadress is null. User: "
					+ user.toString());
			return null;
		}

		Iterator<Node> resultIter = null;
		try {
			resultIter = getQueryEngine()
					.query(getFindFriendsCypherQuery(user),
							MapUtil.map("ref_mailadress", user.getMailadress()))
					.to(Node.class).iterator();
		} catch (Exception ex) {
			log.error(String.format(
					"Could not find friends of a user:'%s' Exception: %s",
					user.toString(), ex.toString()));
			return null;
		}

		if (resultIter == null)
			return null;

		Collection<UserDTO> friends = new ArrayList<UserDTO>();
		while (resultIter.hasNext()) {
			Node node = resultIter.next();
			friends.add(createUserFromNode(node));
		}

		return friends;
	}

	/**
	 * @param "suchbegriff" sucht in: mailadress surname forename
	 * @Override
	 */
	public Collection<UserDTO> findUsers(String suchbegriff) {
		if (suchbegriff.length() < 1)
			return null;

		String searchterm = "(?i)" + suchbegriff + ".*";

		Iterator<Node> resultIter = null;
		try {
			resultIter = getQueryEngine()
					.query(getFindUsersCypherQuery(),
							MapUtil.map("ref_mailadress", searchterm,
									"ref_forename", searchterm, "ref_surname",
									searchterm)).to(Node.class).iterator();
		} catch (Exception ex) {
			log.error(String
					.format("Could not find Users to the given searchterm:'%s' Exception: %s",
							suchbegriff, ex.toString()));
			return null;
		}

		if (resultIter == null)
			return null;

		Collection<UserDTO> foundUsers = new ArrayList<UserDTO>();
		while (resultIter.hasNext()) {
			Node node = resultIter.next();
			foundUsers.add(createUserFromNode(node));
		}
		return foundUsers;
	}

	@Override
	public boolean makeFriends(UserDTO user, UserDTO friend) {
		if (null == user.getMailadress() || null == friend.getMailadress()) {
			log.error("Tried to make friends of two users but mailadress is null. User1: "
					+ user.toString() + ", User2: " + friend.toString());
			return false;
		}

		try {
			getQueryEngine().query(
					getMakeFriendsCypherQuery(),
					MapUtil.map("r_mail_1", user.getMailadress(), "r_mail_2",
							friend.getMailadress(), "r_since", Calendar
									.getInstance().getTime().toString()));
		} catch (Exception exc) {
			log.error(
					String.format("Could not make friends between user(%s) and friend(%s)"),
					user.toString(), friend.toString());

			return false;
		}
		return true; // No check if user and friend actually exist...
	}

	@Override
	public boolean unfriend(UserDTO user, UserDTO friend) {
		if (null == user.getMailadress() || null == friend.getMailadress()) {
			log.error("Tried to unfriend two users but mailadress is null. User1: "
					+ user.toString() + ", User2: " + friend.toString());
			return false;
		}

		try {
			getQueryEngine().query(
					getUnfriendCypherQuery(),
					MapUtil.map("r_mail_1", user.getMailadress(), "r_mail_2",
							friend.getMailadress()));
		} catch (Exception exc) {
			log.error(
					String.format("Could not delete relation between user(%s) and friend(%s)"),
					user.toString(), friend.toString());

			return false;
		}
		return true; // No check if user and friend actually exist...
	}

	@Override
	public Collection<UserDTO> findFriendsOfFriends(UserDTO user) {
		if (null == user.getMailadress()) {
			log.error("Tried to find friend of friends but mailadress is null User: "
					+ user.toString());
			return null;
		}

		Iterator<Node> resultIter = null;

		try {
			resultIter = getQueryEngine()
					.query(getFindFriendsOfFriendsCypherQuery(),
							MapUtil.map("ref_mailadress", user.getMailadress()))
					.to(Node.class).iterator();
		} catch (Exception ex) {
			log.error(String
					.format("Could not find friends of friends to the given User:'%s' Exception: %s",
							user.toString(), ex.toString()));
			return null;
		}

		if (resultIter == null)
			return null;

		Collection<UserDTO> friendsOfFriends = new ArrayList<UserDTO>();
		while (resultIter.hasNext()) {
			Node node = resultIter.next();
			friendsOfFriends.add(createUserFromNode(node));
		}
		return friendsOfFriends;
	}


	private UserDTO createUserFromNode(Node node) {
		UserDTO userDTO = new UserDTO(
				(String) node.getProperty("forename", ""),
				(String) node.getProperty("surname", ""),
				(String) node.getProperty("mailadress", ""),
				(String) node.getProperty("street", ""),
				(String) node.getProperty("housenumber", ""),
				(String) node.getProperty("postcode", ""),
				(String) node.getProperty("city", ""),
				(String) node.getProperty("password", ""), null // We did not
																// load the
																// picture here
																// (vimaier)
		);
		userDTO.setId(Long.toString(node.getId()));

		return userDTO;
	}

	@Override
	public UserDTO loginUser(LoginDTO login) {
		if (null == login.getMailadress()) {
			log.error("Tried to login user but mailadress is null User: "
					+ login.toString());
			return null;
		}

		Iterator<Node> resultIter = null;

		try {
			resultIter = getQueryEngine()
					.query(getLoginUserCypherQuery(),
							MapUtil.map("ref_mailadress",
									login.getMailadress(), "ref_password",
									login.getPassword())).to(Node.class)
					.iterator();
		} catch (Exception ex) {
			log.error(String.format("Could not login User:'%s' Exception: %s",
					login.toString(), ex.toString()));
			return null;
		}

		if (resultIter == null)
			return null;

		Collection<UserDTO> foundUsers = new ArrayList<UserDTO>();
		while (resultIter.hasNext()) {
			Node node = resultIter.next();
			foundUsers.add(createUserFromNode(node));
		}

		return foundUsers.iterator().next();
	}

	private final String GET_NODE_BY_ID_QUERY = "START n=node({r_id}) RETURN n";

	@Override
	public UserDTO getUserById(String id) {
		try {
			QueryResult<?> queryResult = getQueryEngine().query(
					GET_NODE_BY_ID_QUERY,
					MapUtil.map("r_id", Long.parseLong(id)));
			ConvertedResult<Node> convertedResult = queryResult.to(Node.class);
			Iterator<Node> iterator = convertedResult.iterator();
			if (iterator.hasNext())
				return createUserFromNode(iterator.next());
		} catch (Exception exc) {
			log.error(String.format(
					"Exception in getting user by id(%s); Exception: %s", id,
					exc.toString()));
			return null;
		}
		return null;
	}

	// other methods
	private Map<String, Object> getCypherQueryMapUser(UserDTO user) {
		Map<String, Object> result = new HashMap<String, Object>();

		result.put("ref_mailadress", user.getMailadress()); // mailadress !=
															// null !!!!
		if (user.getPassword() != null && !user.getPassword().equals(""))
			result.put("ref_password", user.getPassword());
		if (user.getForename() != null && !user.getForename().equals(""))
			result.put("ref_forename", user.getForename());
		if (user.getSurname() != null && !user.getSurname().equals(""))
			result.put("ref_surename", user.getSurname());
		if (user.getStreet() != null && !user.getStreet().equals(""))
			result.put("ref_street", user.getStreet());
		if (user.getHousenumber() != null && !user.getHousenumber().equals(""))
			result.put("ref_housenumber", user.getHousenumber());
		if (user.getPostcode() != null && !user.getPostcode().equals(""))
			result.put("ref_postcode", user.getPostcode());
		if (user.getCity() != null && !user.getCity().equals(""))
			result.put("ref_city", user.getCity());

		return result;
	}

	/**
	 * CREATE (u:USER { mailadress:{ref_mailadress}, password:{ref_password},
	 * forename:{ref_forename}, surname:{ref_surename}, street:{ref_street},
	 * housenumber:{ref_housenumber}, postcode:{ref_postcode}, city:{ref_city}
	 * }) return u
	 * 
	 * @param user
	 * @return
	 */
	private String getInsertCypherQuery(UserDTO user) {
		StringBuilder sb = new StringBuilder();

		sb.append("CREATE (u:" + UserDTO.LABEL + "{");
		sb.append("mailadress:{ref_mailadress},"); // mailadress != null !!!!
		if (user.getPassword() != null && !user.getPassword().equals(""))
			sb.append("password:{ref_password},");
		if (user.getForename() != null && !user.getForename().equals(""))
			sb.append("forename:{ref_forename},");
		if (user.getSurname() != null && !user.getSurname().equals(""))
			sb.append("surname:{ref_surename},");
		if (user.getStreet() != null && !user.getStreet().equals(""))
			sb.append("street:{ref_street},");
		if (user.getHousenumber() != null && !user.getHousenumber().equals(""))
			sb.append("housenumber:{ref_housenumber},");
		if (user.getPostcode() != null && !user.getPostcode().equals(""))
			sb.append("postcode:{ref_postcode},");
		if (user.getCity() != null && !user.getCity().equals(""))
			sb.append("city:{ref_city},");
		sb.deleteCharAt(sb.length() - 1);
		sb.append("}) return u");

		return sb.toString();
	}

	/**
	 * MATCH (u:USER) WHERE u.mailadress={ref_mailadress} SET
	 * u.password={ref_password}, u.forename={ref_forename},
	 * u.surname={ref_surename}, u.street={ref_street},
	 * u.housenumber={ref_housenumber}, u.postcode={ref_postcode},
	 * u.city={ref_city} return u
	 * 
	 * @param user
	 * @return
	 */
	private String getUpdateCypherQuery(UserDTO user) {
		StringBuilder sb = new StringBuilder();

		sb.append("MATCH (u:" + UserDTO.LABEL
				+ ") WHERE u.mailadress={ref_mailadress} SET ");
		if (user.getPassword() != null && !user.getPassword().equals(""))
			sb.append("u.password={ref_password},");
		if (user.getForename() != null && !user.getForename().equals(""))
			sb.append("u.forename={ref_forename},");
		if (user.getSurname() != null && !user.getSurname().equals(""))
			sb.append("u.surname={ref_surename},");
		if (user.getStreet() != null && !user.getStreet().equals(""))
			sb.append("u.street={ref_street},");
		if (user.getHousenumber() != null && !user.getHousenumber().equals(""))
			sb.append("u.housenumber={ref_housenumber},");
		if (user.getPostcode() != null && !user.getPostcode().equals(""))
			sb.append("u.postcode={ref_postcode},");
		if (user.getCity() != null && !user.getCity().equals(""))
			sb.append("u.city={ref_city},");
		sb.deleteCharAt(sb.length() - 1);
		sb.append(" return u");

		return sb.toString();
	}

	/**
	 * if(withEdges) MATCH (n:USER) WHERE n.mailadress={ref_mailadress} OPTIONAL
	 * MATCH (n)-[r]-() DELETE n,r else MATCH (n:USER) WHERE
	 * n.mailadress={ref_mailadress} DELETE n
	 * 
	 * @param withEdges
	 * @return
	 */
	private String getDeleteCypherQuery(boolean withEdges) {
		StringBuilder sb = new StringBuilder();

		if (withEdges)
			sb.append("MATCH (n:"
					+ UserDTO.LABEL
					+ ") WHERE n.mailadress={ref_mailadress} OPTIONAL MATCH (n)-[r]-() DELETE n,r");
		else
			sb.append("MATCH (n:" + UserDTO.LABEL
					+ ") WHERE n.mailadress={ref_mailadress} DELETE n");

		return sb.toString();
	}

	/**
	 * match (u:USER)-[:FRIENDS]-(f:USER) where u.mailadress = {ref_mailadress}
	 * return f
	 * 
	 * @param user
	 * @return
	 */
	private String getFindFriendsCypherQuery(UserDTO user) {
		StringBuilder sb = new StringBuilder();

		sb.append("match (u:" + UserDTO.LABEL + ")-[:" + RELATIONSHIPS.FRIENDS
				+ "]-(f:" + UserDTO.LABEL + ")");
		sb.append("where u.mailadress = {ref_mailadress} return f");

		return sb.toString();
	}

	/**
	 * match (u:USER) where u.mailadress =~ {ref_mailadress} or u.forename =~
	 * {ref_forename} or u.surname =~ {ref_surname} return u
	 * 
	 * ref_x = (?i)"searchterm".*
	 * 
	 * @return
	 */
	private String getFindUsersCypherQuery() {
		StringBuilder sb = new StringBuilder();

		sb.append("match (u:" + UserDTO.LABEL + ")");
		sb.append("where u.mailadress =~ {ref_mailadress} or u.forename =~ {ref_forename} or u.surname =~ {ref_surname} ");
		sb.append("return u");

		return sb.toString();
	}

	/**
	 * match (u:USER),(f:USER) where u.mailadress = {r_mail_1} and f.mailadress
	 * = {r_mail_2} create (u)-[r:FRIENDS {since: {r_since}}]->(f)
	 * 
	 * @return
	 */
	private String getMakeFriendsCypherQuery() {
		StringBuilder sb = new StringBuilder();

		sb.append("match (u:" + UserDTO.LABEL + "),(f:" + UserDTO.LABEL + ") ");
		sb.append("where u.mailadress = {r_mail_1} and f.mailadress = {r_mail_2} ");
		sb.append("create (u)-[r:" + RELATIONSHIPS.FRIENDS
				+ " {since: {r_since}}]->(f)");

		return sb.toString();
	}

	/**
	 * match (u:USER)-[r:FRIENDS]->(f:USER) where u.mailadress = {r_mail_1} and
	 * f.mailadress = {r_mail_2} delete r
	 * 
	 * @return
	 */
	private String getUnfriendCypherQuery() {
		StringBuilder sb = new StringBuilder();

		sb.append("match (u:" + UserDTO.LABEL + ")-[r:" + RELATIONSHIPS.FRIENDS
				+ "]->(f:" + UserDTO.LABEL + ") ");
		sb.append("where u.mailadress = {r_mail_1} and f.mailadress = {r_mail_2} delete r");

		return sb.toString();
	}

	/**
	 * match (u:USER)-[:FRIENDS*2]-(f:USER) where u.mailadress =
	 * {ref_mailadress} and not (u:USER)-[:FRIENDS*0..1]-(f:USER) return f
	 * 
	 * @return
	 */
	private String getFindFriendsOfFriendsCypherQuery() {
		StringBuilder sb = new StringBuilder();

		sb.append("match (u:" + UserDTO.LABEL + ")-[:" + RELATIONSHIPS.FRIENDS
				+ "*2]-(f:" + UserDTO.LABEL + ") ");
		sb.append("where u.mailadress = {ref_mailadress} and ");
		sb.append("not (u:" + UserDTO.LABEL + ")-[:" + RELATIONSHIPS.FRIENDS
				+ "*0..1]-(f:" + UserDTO.LABEL + ") ");
		sb.append("return f");

		return sb.toString();
	}

	/**
	 * match (u:USER) where u.mailadress = {ref_mailadress} and
	 * u.password={ref_password} return u
	 * 
	 * @return
	 */
	private String getLoginUserCypherQuery() {
		StringBuilder sb = new StringBuilder();

		sb.append("match (u:" + UserDTO.LABEL + ") ");
		sb.append("where u.mailadress = {ref_mailadress} and u.password={ref_password} return u");

		return sb.toString();
	}

}
