<body>
	<div class="main">
		<div class="main_resize">
			<div class="header">
				<div class="logo">
					<h1>
						<a href="#"><fmt:message key="website.title2" /></a>
					</h1>
				</div>
				<%@ include file="RegisterLoginForm.jsp"%>
				<div id="overlay"></div>
				<!--searchform -->
				<div class="search">
					<sf:form action="Search.do" commandName="SearchForm">
						<span> <sf:input path="search" id="s" /><input
							name="searchsubmit" type="image"
							src="resources/images/search.gif" value="Go" id="searchsubmit"
							class="btn" />
						</span>
					</sf:form>
					<!--/searchform -->
					<div class="clr"></div>
				</div>
				<!-- buttons_login_register -->
				<c:choose>
					<c:when test="${not empty activeUser}">
						<div class="login">
							<input type="button" id="registeropen" class="rm fl button_grey"
								value="<fmt:message key="menu.update"/>" />
							<sf:form action="Logout.do" commandName="UserDTO">
								<input type="submit" class="rm fr button_blue"
									value="<fmt:message key="menu.logout"/>" />
							</sf:form>
						</div>
					</c:when>
					<c:otherwise>
						<div class="login">
							<input type="button" id="registeropen" class="rm fl button_grey"
								value="<fmt:message key="menu.login"/>" /> <input type="button"
								id="loginopen" class="rm fr button_blue"
								value="<fmt:message key="menu.create"/>" />
						</div>
					</c:otherwise>
				</c:choose>
				<!-- /buttons_login_register -->
				<div class="clr"></div>
				<div class="menu_nav">
					<ul>
						<li class="active"><a href="Home.do">Home</a></li>
						<c:choose>
							<c:when test="${not empty activeUser}">
								<li><a href="UserIndex.do">Eigene Daten</a></li>
							</c:when>
							<c:otherwise>
								<li><a href="#">Support</a></li>
							</c:otherwise>
						</c:choose>
					</ul>
					<div class="clr"></div>
				</div>
				<div class="hbg">
					<img src="resources/images/header_images.jpg" width="923"
						height="291" alt="" />
				</div>
			</div>