<%@ include file="templates/Head.jsp"%>
<%@ include file="templates/Header.jsp"%>
<div class="content">
	<div class="content_bg">
		<c:choose>
			<c:when test="${not empty SearchForm.result}">
				<div style="text-align: center;margin: auto;width: 780px;">
				<h2><fmt:message key="search.result" /></h2>
					<c:forEach items="${SearchForm.result}" var="res">
						<div class="friendpic"
							onclick="location.href='UserIndex.do?id=${res.id}';">
							<img alt="" src="resources/images/profile.jpg">
							<div>
								<c:out value="${res.forename}" />
								&nbsp;
								<c:out value="${res.surname}" />
							</div>
						</div>
					</c:forEach>
					<div class="clr"></div>
				</div>
			</c:when>
			<c:otherwise>
			<div id="userindexright"><fmt:message key="no.friends" /></div>
			</c:otherwise>
		</c:choose>
		<div class="clr"></div>
	</div>
</div>
<%-- <%@ include file="templates/Bluebar.jsp"%> --%>
<%@ include file="templates/Footer.jsp"%>