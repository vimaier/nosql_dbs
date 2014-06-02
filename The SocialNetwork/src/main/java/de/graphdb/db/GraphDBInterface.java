package de.graphdb.db;

import java.util.Collection;

import de.graphdb.dto.LoginDTO;
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
   * Findet Nutzer anhand eines Suchbegriffes
   * 
   * @param user
   * @return Collection<UserDTO>
   */
  public Collection<UserDTO> findUsers(String suchbegriff);

  /**
   * Beziehung zwischen user und friend Aufbauen
   * 
   * @param user
   * @param friend
   * @return
   */
  public boolean makeFriends(UserDTO user, UserDTO friend);

  /**
   * Beziehung zwischen user und friend beenden
   * 
   * @param user
   * @param friend
   * @return
   */
  public boolean unfriend(UserDTO user, UserDTO friend);

  /**
   * Gibt Freunde von Freunden zurueck - Eventuell auf 10 oder 20 limitieren.
   * 
   * @param friends
   *          Collection<UserDTO>
   * @return Collection<UserDTO>
   */
  public Collection<UserDTO> findFriendsOfFriends(UserDTO user);

  /**
   * Gibt Verwandte zurueck - Eventuell auf 10 oder 20 limitieren.
   * 
   * @param user
   *          UserDTO
   * @return Collection<UserDTO>
   */
  public Collection<UserDTO> findRelative(UserDTO user);
  
  /**
   * Rueckgabe des kompletten UserDTO welches ueber die uebergebene Email(unique) gefunden wird.
   * Abgleich von Email und Passwort uebernimmt der Controller
   * 
   * @param login LoginDTO
   * @return UserDTO
   */
  public UserDTO loginUser(LoginDTO login);

}
