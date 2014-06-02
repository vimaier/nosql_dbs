package de.graphdb.db.neo4j;

import java.util.Collection;

import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import de.graphdb.db.GraphDBInterface;
import de.graphdb.dto.LoginDTO;
import de.graphdb.dto.UserDTO;
import de.graphdb.service.ParseJSON;

public class Neo4jDao implements GraphDBInterface {
	
	private static final String SERVER_ROOT_URI = "http://10.0.3.43:7474/db/data/";

	@Override
	public boolean insertUser(UserDTO user) {
		String cypherjsonquery = user.getInsertCypherQuery();

		System.out.println("Query: "+cypherjsonquery);
		
		ClientResponse respo=null;
		try {
			final String nodeEntryPointURI = SERVER_ROOT_URI + "cypher";
			WebResource res = Client.create().resource(nodeEntryPointURI);
			respo = res.accept(MediaType.APPLICATION_JSON)
						                 .type(MediaType.APPLICATION_JSON)
						                 .entity(cypherjsonquery)
						                 .post(ClientResponse.class);
			String erg = respo.getEntity(String.class).toString(); //.getBytes("UTF8");
			System.out.println(erg);
			
			respo.close();
			return true;
		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public UserDTO updateUser(UserDTO user) {
		String cypherjsonquery = user.getUpdateCypherQuery();
		
		System.out.println("Query: "+cypherjsonquery);
		
		ClientResponse respo=null;
		try {
			final String nodeEntryPointURI = SERVER_ROOT_URI + "cypher";
			WebResource res = Client.create().resource(nodeEntryPointURI);
			respo = res.accept(MediaType.APPLICATION_JSON)
						                 .type(MediaType.APPLICATION_JSON)
						                 .entity(cypherjsonquery)
						                 .post(ClientResponse.class);
			String erg = respo.getEntity(String.class).toString(); //.getBytes("UTF8");
			System.out.println(erg);
			
			respo.close();
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		
		return user;
	}

	@Override
	public boolean deleteUser(UserDTO user) {
		String cypherjsonquery = user.getDeleteCypherQuery();

		System.out.println("Query: "+cypherjsonquery);
		
		ClientResponse respo=null;
		try {
			final String nodeEntryPointURI = SERVER_ROOT_URI + "cypher";
			WebResource res = Client.create().resource(nodeEntryPointURI);
			respo = res.accept(MediaType.APPLICATION_JSON)
						                 .type(MediaType.APPLICATION_JSON)
						                 .entity(cypherjsonquery)
						                 .post(ClientResponse.class);
			String erg = respo.getEntity(String.class).toString(); //.getBytes("UTF8");
			System.out.println(erg);
			
			respo.close();
			return true;
		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public Collection<UserDTO> findFriends(UserDTO user) {
		Collection<UserDTO> friends = null;
		
		String cypherjsonquery = user.getFriendsCypherQuery();

		System.out.println("Query: "+cypherjsonquery);
		
		ClientResponse respo=null;
		try {
			final String nodeEntryPointURI = SERVER_ROOT_URI + "cypher";
			WebResource res = Client.create().resource(nodeEntryPointURI);
			respo = res.accept(MediaType.APPLICATION_JSON)
						                 .type(MediaType.APPLICATION_JSON)
						                 .entity(cypherjsonquery)
						                 .post(ClientResponse.class);
			String erg = respo.getEntity(String.class).toString(); //.getBytes("UTF8");
			System.err.println("sfdsdf");
			System.out.println(erg);
			
			respo.close();
			
			friends = ParseJSON.parseString(erg);	
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return friends;
	}

	@Override
	public Collection<UserDTO> findUsers(String suchbegriff) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean makeFriends(UserDTO user, UserDTO friend) {
		String cypherjsonquery = user.getMakeFriendsCypherQuery(friend);

		System.out.println("Query: "+cypherjsonquery);
		
		ClientResponse respo=null;
		try {
			final String nodeEntryPointURI = SERVER_ROOT_URI + "cypher";
			WebResource res = Client.create().resource(nodeEntryPointURI);
			respo = res.accept(MediaType.APPLICATION_JSON)
						                 .type(MediaType.APPLICATION_JSON)
						                 .entity(cypherjsonquery)
						                 .post(ClientResponse.class);
			String erg = respo.getEntity(String.class).toString(); //.getBytes("UTF8");
			System.out.println(erg);
			
			respo.close();
			return true;
		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
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

  @Override
  public UserDTO loginUser(LoginDTO login) {
    // TODO Auto-generated method stub
    return null;
  }

}
