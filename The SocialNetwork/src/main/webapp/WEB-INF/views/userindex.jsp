<%@ include file="templates/Head.jsp"%>
<%@ include file="templates/Header.jsp"%>
<div class="content">
	<div class="content_bg">
		<div id="userindexleft">
			<div class="fl width150">
				<fmt:message key="registerform.forname" />
				:
			</div>
			<div class="fr width300">
				<c:out value="${UserIndexForm.user.forename}" />
			</div>
			<div class="clr"></div>
			<div class="fl width150">
				<fmt:message key="registerform.surname" />
				:
			</div>
			<div class="fr width300">
				<c:out value="${UserIndexForm.user.surname}" />
			</div>
			<div class="clr"></div>
			<div class="fl width150">
				<fmt:message key="registerform.mail" />
				:
			</div>
			<div class="fr width300">
				<c:out value="${UserIndexForm.user.mailadress}" />
			</div>
			<div class="clr"></div>
			<div class="fl width150">
				<fmt:message key="registerform.street" />
				:
			</div>
			<div class="fr width300">
				<c:out value="${UserIndexForm.user.street}" />
			</div>
			<div class="clr"></div>
			<div class="fl width150">
				<fmt:message key="registerform.number" />
				:
			</div>
			<div class="fr width300">
				<c:out value="${UserIndexForm.user.housenumber}" />
			</div>
			<div class="clr"></div>
			<div class="fl width150">
				<fmt:message key="registerform.postcode" />
				:
			</div>
			<div class="fr width300">
				<c:out value="${UserIndexForm.user.postcode}" />
			</div>
			<div class="clr"></div>
			<div class="fl width150">
				<fmt:message key="registerform.city" />
				:
			</div>
			<div class="fr width300">
				<c:out value="${UserIndexForm.user.city}" />
			</div>
			<div class="clr"></div>
			<c:choose>
				<c:when
					test="${not empty activeUser && activeUser.id!=UserIndexForm.user.id && !isFriend}">
					<sf:form action="MakeFriends.do" commandName="UserIndexForm">
						<input type="submit" class="button_blue"
							value="<fmt:message key="menu.make.friend"/>"
							style="margin: 10px auto 0px auto;" />
					</sf:form>
				</c:when>
				<c:when test="${isFriend}">
					<sf:form action="Unfriend.do" commandName="UserIndexForm">
						<input type="submit" class="button_blue"
							value="<fmt:message key="menu.unfriend"/>"
							style="margin: 10px auto 0px auto;" />
					</sf:form>
				</c:when>
				<c:when test="${activeUser.id==UserIndexForm.user.id}">
					<sf:form action="Delete.do" commandName="UserIndexForm">
						<input type="submit" class="button_grey"
							value="<fmt:message key="menu.delete"/>"
							style="margin: 10px auto 0px auto;" />
					</sf:form>
				</c:when>
			</c:choose>
		</div>
		<c:choose>
			<c:when test="${not empty UserIndexForm.friends}">
				<div id="userindexright">
					<h3>
						<fmt:message key="user.friends" />
					</h3>
					<c:forEach items="${UserIndexForm.friends}" var="friend">
						<div class="friendpic"
							onclick="location.href='UserIndex.do?id=${friend.id}';">
							<img alt="" src="resources/images/profile.jpg">
							<div>
								<c:out value="${friend.forename}" />
								&nbsp;
								<c:out value="${friend.surname}" />
							</div>
						</div>
					</c:forEach>
					<div class="clr"></div>
				</div>
				<div id="userindexleft">
					<h4>
						<fmt:message key="friends.of.friends" />
					</h4>
					<c:forEach items="${UserIndexForm.friendsoffriends}" var="fof">
					<div class="friendpicmini"
							onclick="location.href='UserIndex.do?id=${fof.id}';">
							<img alt="" src="resources/images/profile.jpg">
							<div>
								<c:out value="${fof.forename}" />
								&nbsp;
								<c:out value="${fof.surname}" />
							</div>
						</div>
					</c:forEach>
				</div>
			</c:when>
			<c:otherwise>
				<div id="userindexright">
					<fmt:message key="no.friends" />
				</div>
			</c:otherwise>
		</c:choose>
		<div class="clr"></div>
	</div>
</div>
<%-- <%@ include file="templates/Bluebar.jsp"%> --%>
<%@ include file="templates/Footer.jsp"%>