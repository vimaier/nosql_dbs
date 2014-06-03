package de.graphdb.controller;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.InvalidPropertyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.HttpSessionRequiredException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.request.WebRequest;

import de.graphdb.db.orientdb.OrientDbDao;
import de.graphdb.dto.LoginDTO;
import de.graphdb.dto.UserDTO;
import de.graphdb.form.UserIndexForm;

/**
 * Handles requests for the application home page.
 */
@Controller
@SessionAttributes({ "UserDTO", "LoginDTO", "UserIndexForm" })
public class HomeController {

  @Autowired
  private WebRequest request;
  @Autowired
  private HttpSession session;
  private static final Logger logger = LoggerFactory
      .getLogger(HomeController.class);
  private ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
      "spring.xml");

  // private TinkerpopDao db = new TinkerpopDao();
  private OrientDbDao db = new OrientDbDao();

  // private Neo4jDao db = new Neo4jDao();

  @ModelAttribute("LoginDTO")
  public LoginDTO createLoginDTO() {
    return (LoginDTO) context.getBean("LoginDTO");
  }

  @ModelAttribute("UserDTO")
  public UserDTO createUserDTO() {
    return (UserDTO) context.getBean("UserDTO");
  }

  @ModelAttribute("UserIndexForm")
  public UserIndexForm createUserindexForm() {
    return (UserIndexForm) context.getBean("UserindexForm");
  }

  @RequestMapping(value = "Home.do")
  public String home(ModelMap model) {
    UserDTO currUser = (UserDTO) session.getAttribute("activeUser");
    if (currUser != null) {
      model.put("UserDTO", currUser);
    }
    return "index";
  }

  @RequestMapping(value = { "/", "Index.do" })
  public String homeLoggedIn(ModelMap model) {
    model.put("UserDTO", createUserDTO());
    model.put("LoginDTO", createLoginDTO());
    return "index";
  }

  @RequestMapping(value = "Login.do", method = RequestMethod.POST)
  public String Login(ModelMap model, @ModelAttribute("LoginDTO") LoginDTO login) {
    logger.info("***** Login Mail=" + login.getMailadress());
    UserDTO user = db.loginUser(login);
    session.removeAttribute("activeUser");
    if (user != null && login.getPassword() != null
        && !login.getPassword().isEmpty()
        && login.getPassword().equals(user.getPassword())) {
      user.setPassword("");
      session.setAttribute("activeUser", user);
      model.put("UserDTO", user);
    }
    return "redirect:UserIndex.do";
  }

  @RequestMapping(value = "Register.do", method = RequestMethod.POST)
  public String Register(ModelMap model, @ModelAttribute("UserDTO") UserDTO user) {
    logger.info("***** Register USER=" + user);
    Boolean check = false;
    check = db.insertUser(user);
    if (!check) {
      logger.info("Fehler beim Registrieren des Nutzers: " + user);
    } else {
      model.put("UserDTO", createUserDTO());
    }
    return "index";
  }

  @RequestMapping(value = "Update.do", method = RequestMethod.POST)
  public String Update(ModelMap model, @ModelAttribute("UserDTO") UserDTO user) {
    logger.info("***** Update USER=" + user);
    user = db.updateUser(user);
    if (user == null || user.getId() == null || user.getId().isEmpty()) {
      logger.info("Fehler beim Update des Nutzers: " + user);
      user = (UserDTO) session.getAttribute("activeUser");
    } else {
      model.put("UserDTO", user);
      user.setPassword("");
      session.setAttribute("activeUser", user);
    }
    return "redirect:UserIndex.do";
  }

  @RequestMapping(value = "Logout.do", method = RequestMethod.POST)
  public String Logout(ModelMap model, @ModelAttribute("UserDTO") UserDTO user) {
    logger.info("***** Logout USER=" + user);
    session.removeAttribute("activeUser");
    model.put("UserDTO", createUserDTO());
    return "index";
  }

  @RequestMapping(value = "UserIndex.do")
  public String UserIndex(ModelMap model,
      @RequestParam(value = "id", required = false) String id) {
    logger.info("***** UserIndex");
    UserIndexForm uform = createUserindexForm();
    if (id == null || id.isEmpty()) {
      UserDTO currUser = (UserDTO) session.getAttribute("activeUser");
      if (currUser != null && currUser.getId() != null
          && !currUser.getId().isEmpty()) {
        id = currUser.getId();
        uform.setUser(currUser);
      } else {
        return "redirect:Index.do";
      }
    } else {
      uform.setUser(db.getUserById(id));
    }
    uform.setFriends(db.findFriends(user));
    return "userindex";
  }

  /**
   * Catch InvalidPropertyException if Session timeout
   * 
   * @param exception
   * @return redirect:Login.do
   */
  @ExceptionHandler(InvalidPropertyException.class)
  public String handleInvalidPropertyException(
      InvalidPropertyException exception) {
    logger.info("****** handleInvalidPropertyException - Catching: "
        + exception.getClass().getSimpleName());
    logger.info("Exception: " + exception);
    return "redirect:Index.do";
  }

  /**
   * Catch HttpSessionRequiredException if Session timeout
   * 
   * @param exception
   * @return redirect:Login.do
   */
  @ExceptionHandler(HttpSessionRequiredException.class)
  public String handleHttpSessionRequiredException(
      HttpSessionRequiredException exception) {
    logger.info("****** handleHttpSessionRequiredException - Catching: "
        + exception.getClass().getSimpleName());
    logger.info("Exception: " + exception);
    return "redirect:Index.do";
  }

}
