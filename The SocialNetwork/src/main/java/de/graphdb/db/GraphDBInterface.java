package de.graphdb.db;

import java.util.Collection;

import de.graphdb.dto.UserDTO;

public interface GraphDBInterface {

  /**
   * Einen neuen Nutzer in der Datenbank anlegen. Rueckgabewert: true bei
   * Erfolg; false bei Misserfolg
   * 
   * @param user
   *          UserDTO
   * @return boolean
   */
  public boolean insertUser(UserDTO user);

  /**
   * Einen vorhandenen Datensatz updaten und diesen dann bei Erfolg zurueck
   * geben
   * 
   * @param user
   *          UserDTO
   * @return UserDTO
   */
  public UserDTO updateUser(UserDTO user);

  /**
   * Nutzer loeschen; ture bei Erfolg; false bei Misserfolg
   * 
   * @param user
   *          UserDTO
   * @return UserDTO
   */
  public boolean deleteUser(UserDTO user);

  /**
   * Gibt eine Lister aller Freunde des Nutzers zurueck
   * 
   * @param user
   *          UserDTO
   * @return Collection<UserDTO>
   */
  public Collection<UserDTO> findFriends(UserDTO user);

  /**
   * Gibt Freunde von Freunden zurück - Eventuell auf 10 oder 20 limitieren.
   * 
   * @param friends
   *          Collection<UserDTO>
   * @return Collection<UserDTO>
   */
  public Collection<UserDTO> findFriendsOfFriends(Collection<UserDTO> friends);

}
