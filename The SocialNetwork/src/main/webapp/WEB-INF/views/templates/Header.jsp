<body>
<div class="main">
  <div class="main_resize">
    <div class="header">
      <div class="logo">
        <h1><a href="#"><fmt:message key="website.title2"/></a></h1>
      </div>
        <!-- registerform -->
      	<div id="loginfade">
   				<p>
        		<a href="#" id="loginclose">close</a>
   				</p>
    			<sf:form action="Register.do" commandName="UserDTO">
    			  <sf:label path="forname">forname</sf:label><sf:input path="forname" />
    				<sf:label path="surname">surname</sf:label><sf:input path="surname" />
    				<sf:label path="mailadress">mailadress</sf:label><sf:input path="mailadress" />
    				<sf:label path="street">street</sf:label><sf:input path="street" />
    				<sf:label path="housenumber">housenumber</sf:label><sf:input path="housenumber" />
    				<sf:label path="postcode">postcode</sf:label><sf:input path="postcode" />
    				<sf:label path="city">city</sf:label><sf:input path="city" />
    				<sf:button>submit</sf:button>
    			</sf:form>
				</div>
				<!-- /registerform -->
			<div id="overlay"></div>
			<!--searchform -->                
      <div class="search">      
        <form method="get" id="search" action="#">        
          <span>
          <input type="text" value="Search..." name="s" id="s" />
          <input name="searchsubmit" type="image" src="resources/images/search.gif" value="Go" id="searchsubmit" class="btn"  />
          </span>
        </form>
        <!--/searchform -->
        <div class="clr"></div>
      </div>
      <!-- buttons_login_register -->
      <div class="login">
        <form method="get" id="search12" action="#"> 
          <input type="button" id="loginopen" class="rm fl button_grey" value="<fmt:message key="menu.login"/>" />
      	  <input type="button" class="rm fr button_blue" value="<fmt:message key="menu.create"/>" />
      	</form>
      </div> 
      <!-- /buttons_login_register --> 
      <div class="clr"></div>
      <div class="menu_nav">
        <ul>
          <li class="active"><a href="index.html">Home</a></li>
          <li><a href="support.html">Support</a></li>
          <li><a href="about.html">About Us</a></li>
          <li><a href="blog.html">Blog</a></li>
          <li><a href="contact.html">Contact Us</a></li>
        </ul>
        <div class="clr"></div>
      </div>
      <div class="hbg"><img src="resources/images/header_images.jpg" width="923" height="291" alt="" /></div>
    </div>