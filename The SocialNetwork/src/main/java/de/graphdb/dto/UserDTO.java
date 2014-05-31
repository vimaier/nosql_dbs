package de.graphdb.dto;

import java.util.Calendar;

import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

@Repository
public class UserDTO {

	private static final String LABEL = "USER";
	private static final String RELATIONSHIP_FRIENDS = "FRIENDS";

	private String forname;
	private String surname;
	private String mailadress;
	private String street;
	private String housenumber;
	private String postcode;
	private String city;
	private String password;
	private CommonsMultipartFile picture_up;

	public String getForname() {
		return forname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setForname(String forname) {
		this.forname = forname;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getMailadress() {
		return mailadress;
	}

	public void setMailadress(String mailadress) {
		this.mailadress = mailadress;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getHousenumber() {
		return housenumber;
	}

	public void setHousenumber(String housenumber) {
		this.housenumber = housenumber;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public CommonsMultipartFile getPicture_up() {
		return picture_up;
	}

	public void setPicture_up(CommonsMultipartFile picture_up) {
		this.picture_up = picture_up;
	}

	public UserDTO() {

	}

	public UserDTO(String forname, String surname, String mailadress,
			String street, String housenumber, String postcode, String city,
			String password, CommonsMultipartFile picture_up) {
		super();
		this.forname = forname;
		this.surname = surname;
		this.mailadress = mailadress;
		this.street = street;
		this.housenumber = housenumber;
		this.postcode = postcode;
		this.city = city;
		this.password = password;
		this.picture_up = picture_up;
	}

	@Override
	public String toString() {
		return "UserDTO [forname=" + forname + ", surname=" + surname
				+ ", mailadress=" + mailadress + ", street=" + street
				+ ", housenumber=" + housenumber + ", postcode=" + postcode
				+ ", city=" + city + ", picture_up=" + picture_up + "]";
	}

	public String getInsertCypherQuery() {
		if(this.mailadress == null)
			return null;
		
		StringBuilder sb = new StringBuilder();

		sb.append("{ \"query\" : \"create (u:" + LABEL + "{");
		if(this.forname != null)
			sb.append("forname: '" + this.forname + "',");
		if(this.surname != null)
			sb.append("surname: '" + this.surname + "',");
		if(this.mailadress != null)
			sb.append("mailadress: '" + this.mailadress + "',");
		if(this.street != null)
			sb.append("street: '" + this.street + "',");
		if(this.housenumber != null)
			sb.append("housenumber: '" + this.housenumber + "',");
		if(this.postcode != null)
			sb.append("postcode: '" + this.postcode + "',");
		if(this.city != null)
			sb.append("city: '" + this.city + "',");
		if(this.password != null)
			sb.append("password: '" + this.password + "',");
		sb.deleteCharAt(sb.length()-1);
		sb.append(" }) return u\" }");

		return sb.toString();
	}

	public String getUpdateCypherQuery() {
		if(this.mailadress == null)
			return null;
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("{ \"query\" : \"match (u:" + LABEL + ") ");
		sb.append("where u.mailadress = '" + this.mailadress + "' ");
		if(this.forname != null)
			sb.append("set u.forname = '" + this.forname + "',");
		if(this.surname != null)
			sb.append("u.surname = '" + this.surname + "',");
		if(this.street != null)
			sb.append("u.street = '" + this.street + "',");
		if(this.housenumber != null)
			sb.append("u.housenumber = '" + this.housenumber + "',");
		if(this.postcode != null)
			sb.append("u.postcode = '" + this.postcode + "',");
		if(this.city != null)
			sb.append("u.city = '" + this.city + "',");
		if(this.password != null)
			sb.append("u.password = '" + this.password + "',");
		sb.deleteCharAt(sb.length()-1);
		sb.append(" return u\" }");
		
		return sb.toString();
	}
	
	public String getDeleteCypherQuery(){
		if(this.mailadress == null)
			return null;
		
		StringBuilder sb = new StringBuilder();
		
		// TODO: relationen noch löschen!!
		//sb.append("{ \"query\" : \"match (u:" + LABEL + ")-[r]-() ");
		sb.append("{ \"query\" : \"match (u:" + LABEL + ") ");
		sb.append("where u.mailadress = '"+this.mailadress+"' ");
		//sb.append("delete u, r\" }");
		sb.append("delete u\" }");
		
		return sb.toString();
	}
	
	public String getMakeFriendsCypherQuery(UserDTO friend){
		if(this.mailadress == null || friend.mailadress == null)
			return null;
		
		StringBuilder sb = new StringBuilder();
		
		String since = Calendar.getInstance().getTime().toString();
		
		sb.append("{ \"query\" : \"match (u:" + LABEL + "),(f:"+LABEL+") ");
		sb.append("where u.mailadress = '"+this.mailadress+"' and f.mailadress = '"+friend.mailadress+"' ");
		sb.append("create (u)-[r:"+RELATIONSHIP_FRIENDS+" {since: '"+since+"'}]->(f)\" }");
		
		return sb.toString();
	}

	public String getFriendsCypherQuery(){
		if(this.mailadress == null)
			return null;
		
		StringBuilder sb = new StringBuilder();

		sb.append("{ \"query\" : \"match (u:" + LABEL + ")-[:"+RELATIONSHIP_FRIENDS+"]->(f:" + LABEL + ") ");
		sb.append("where u.mailadress = '"+this.mailadress+"' ");
		sb.append("return f\" }");
		
		return sb.toString();
	}
}
