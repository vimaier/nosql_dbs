package de.graphdb.form;

import java.util.Collection;

import de.graphdb.dto.UserDTO;

public class UserIndexForm {

  private UserDTO user;
  private Collection<UserDTO> friends;
  private Collection<UserDTO> friendsoffriends;

  public UserIndexForm() {
  }

  public UserIndexForm(UserDTO user, Collection<UserDTO> friends,
      Collection<UserDTO> friendsoffriends) {
    this.user = user;
    this.friends = friends;
    this.friendsoffriends = friendsoffriends;
  }

  public UserDTO getUser() {
    return user;
  }

  public void setUser(UserDTO user) {
    this.user = user;
  }

  public Collection<UserDTO> getFriends() {
    return friends;
  }

  public void setFriends(Collection<UserDTO> friends) {
    this.friends = friends;
  }

  public Collection<UserDTO> getFriendsoffriends() {
    return friendsoffriends;
  }

  public void setFriendsoffriends(Collection<UserDTO> friendsoffriends) {
    this.friendsoffriends = friendsoffriends;
  }

}
