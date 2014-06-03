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
				<c:out value="${UserDTO.forename}" />
			</div>
			<div class="clr"></div>
			<div class="fl width150">
				<fmt:message key="registerform.surname" />
				:
			</div>
			<div class="fr width300">
				<c:out value="${UserDTO.surname}" />
			</div>
			<div class="clr"></div>
			<div class="fl width150">
				<fmt:message key="registerform.mail" />
				:
			</div>
			<div class="fr width300">
				<c:out value="${UserDTO.mailadress}" />
			</div>
			<div class="clr"></div>
			<div class="fl width150">
				<fmt:message key="registerform.street" />
				:
			</div>
			<div class="fr width300">
				<c:out value="${UserDTO.street}" />
			</div>
			<div class="clr"></div>
			<div class="fl width150">
				<fmt:message key="registerform.number" />
				:
			</div>
			<div class="fr width300">
				<c:out value="${UserDTO.housenumber}" />
			</div>
			<div class="clr"></div>
			<div class="fl width150">
				<fmt:message key="registerform.postcode" />
				:
			</div>
			<div class="fr width300">
				<c:out value="${UserDTO.postcode}" />
			</div>
			<div class="clr"></div>
			<div class="fl width150">
				<fmt:message key="registerform.city" />
				:
			</div>
			<div class="fr width300">
				<c:out value="${UserDTO.city}" />
			</div>
			<div class="clr"></div>
		</div>
		<div id="userindexright">
			<div class="friendpic"></div>
			<div class="friendpic"></div>
			<div class="friendpic"></div>
			<div class="clr"></div>
			<div class="friendpic"></div>
			<div class="friendpic"></div>
			<div class="friendpic"></div>
		</div>
		<div class="clr"></div>
	</div>
</div>
<%-- <%@ include file="templates/Bluebar.jsp"%> --%>
<%@ include file="templates/Footer.jsp"%>