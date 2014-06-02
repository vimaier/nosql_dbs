package de.graphdb.controller;

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
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.request.WebRequest;

import de.graphdb.db.neo4j.Neo4jDao;
import de.graphdb.db.orientdb.OrientDbDao;
import de.graphdb.db.tinkerpop.TinkerpopDao;
import de.graphdb.dto.LoginDTO;
import de.graphdb.dto.UserDTO;

/**
 * Handles requests for the application home page.
 */
@Controller
@SessionAttributes({ "UserDTO", "LoginDTO" })
public class HomeController {

  @Autowired
  private WebRequest request;
  private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
  private ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
  
  //private TinkerpopDao db = new TinkerpopDao();
  //private OrientDbDao db = new OrientDbDao();
  private Neo4jDao db = new Neo4jDao();

  @ModelAttribute("LoginDTO")
  public LoginDTO createLoginDTO() {
    return (LoginDTO) context.getBean("LoginDTO");
  }

  @ModelAttribute("UserDTO")
  public UserDTO createUserDTO() {
    return (UserDTO) context.getBean("UserDTO");
  }

  @RequestMapping(value = "/", method = RequestMethod.GET)
  public String home(ModelMap model) {

    model.put("UserDTO", createUserDTO());
    model.put("LoginDTO", createLoginDTO());
    return "index";
  }

  @RequestMapping(value = "Login.do", method = RequestMethod.POST)
  public String Login(ModelMap model) {

    return "index";
  }

  @RequestMapping(value = "Register.do", method = RequestMethod.POST)
  public String Register(ModelMap model, @ModelAttribute("UserDTO") UserDTO user) {

    logger.info("***** Register USER=" + user);
    db.insertUser(user);

    return "index";
  }

  /**
   * Catch InvalidPropertyException if Session timeout
   * 
   * @param exception
   * @return redirect:Login.do
   */
  @ExceptionHandler(InvalidPropertyException.class)
  public String handleInvalidPropertyException(InvalidPropertyException exception) {
    logger.info("****** handleInvalidPropertyException - Catching: " + exception.getClass().getSimpleName());
    logger.info("Exception: " + exception);
    return "redirect:Login.do";
  }

  /**
   * Catch HttpSessionRequiredException if Session timeout
   * 
   * @param exception
   * @return redirect:Login.do
   */
  @ExceptionHandler(HttpSessionRequiredException.class)
  public String handleHttpSessionRequiredException(HttpSessionRequiredException exception) {
    logger.info("****** handleHttpSessionRequiredException - Catching: " + exception.getClass().getSimpleName());
    logger.info("Exception: " + exception);
    return "redirect:Login.do";
  }

}
