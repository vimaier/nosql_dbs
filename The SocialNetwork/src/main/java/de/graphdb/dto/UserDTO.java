package de.graphdb.dto;

import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

@Repository
public class UserDTO {

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
        + ", housenumber=" + housenumber + ", postcode=" + postcode + ", city="
        + city + ", picture_up=" + picture_up + "]";
  }

  public String getInsertCypherQuery(){
	  String label = "User";
	  
	  StringBuilder sb = new StringBuilder();
	  
	  sb.append("{ \"query\" : \"create (u:"+label+"{");
	  sb.append("forname: '"+this.forname+"',");
	  sb.append("surname: '"+this.surname+"',");
	  sb.append("mailadress: '"+this.mailadress+"',");
	  sb.append("street: '"+this.street+"',");
	  sb.append("housenumber: '"+ this.housenumber+"',");
	  sb.append("postcode: '"+this.postcode+"',");
	  sb.append("city: '"+this.city+"',");
	  sb.append("password: '"+this.password+"'");
	  
	  sb.append(" }) return u\" }");
	  
	  return sb.toString();
  }
  
}
