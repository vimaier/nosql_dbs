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
		</div>
		<div id="userindexright">
		<c:forEach items="${UserIndexForm.friends}" var="friend">
				<div class="friendpic" onclick="location.href='UserIndex.do?id=${friend.id}';">
					<img alt="" src="resources/images/profile.jpg">
					<div><c:out value="${friend.forename}" />&nbsp;<c:out value="${friend.surname}" /></div>
				</div>
		</c:forEach>
			<div class="clr"></div>
		</div>
		<div id="userindexleft">
		Freunde von Freunden
		</div>
		<div class="clr"></div>
	</div>
</div>
<%-- <%@ include file="templates/Bluebar.jsp"%> --%>
<%@ include file="templates/Footer.jsp"%>