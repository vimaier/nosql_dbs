package de.graphdb.dto;

import org.springframework.stereotype.Repository;

@Repository
public class LoginDTO {

  private String mailadress;
  private String password;

  public LoginDTO() {

  }

  public LoginDTO(String mailadress, String password) {
    super();
    this.mailadress = mailadress;
    this.password = password;
  }

  public String getMailadress() {
    return mailadress;
  }

  public void setMailadress(String mailadress) {
    this.mailadress = mailadress;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  @Override
  public String toString() {
    return "LoginDTO [mailadress=" + mailadress + ", password=" + password
        + "]";
  }

}
