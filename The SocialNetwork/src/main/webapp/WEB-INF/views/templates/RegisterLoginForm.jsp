<!-- registerform -->
<div id="registerfade">
  <c:choose>
    <c:when test="${not empty activeUser}">
      <h3 style="text-align: center;">
        <fmt:message key="registerform.headline.update" />
      </h3>
      <sf:form action="Update.do" commandName="UserDTO">
        <div class="fl">
          <sf:label path="forename">
            <fmt:message key="registerform.forname" />
          </sf:label>
        </div>
        <div class="fr">
          <sf:input class="input_text" path="forename" />
        </div>
        <div class="clr"></div>
        <div class="fl">
          <sf:label path="surname">
            <fmt:message key="registerform.surname" />
          </sf:label>
        </div>
        <div class="fr">
          <sf:input class="input_text" path="surname" />
        </div>
        <div class="clr"></div>
        <div class="fl">
          <sf:label path="mailadress">
            <fmt:message key="registerform.mail" />
          </sf:label>
        </div>
        <div class="fr">
          <sf:input required="required" type="email" class="input_text" path="mailadress" />
        </div>
        <div class="clr"></div>
        <div class="fl">
          <sf:label path="password">
            <fmt:message key="registerform.password" />
          </sf:label>
        </div>
        <div class="fr">
          <sf:password required="required" class="input_text" path="password" />
        </div>
        <div class="clr"></div>
        <div class="fl">
          <sf:label path="street">
            <fmt:message key="registerform.street" />
          </sf:label>
        </div>
        <div class="fr">
          <sf:input class="input_text" path="street" />
        </div>
        <div class="clr"></div>
        <div class="fl">
          <sf:label path="housenumber">
            <fmt:message key="registerform.number" />
          </sf:label>
        </div>
        <div class="fr">
          <sf:input class="input_text" path="housenumber" />
        </div>
        <div class="clr"></div>
        <div class="fl">
          <sf:label path="postcode">
            <fmt:message key="registerform.postcode" />
          </sf:label>
        </div>
        <div class="fr">
          <sf:input class="input_text" path="postcode" />
        </div>
        <div class="clr"></div>
        <div class="fl">
          <sf:label path="city">
            <fmt:message key="registerform.city" />
          </sf:label>
        </div>
        <div class="fr">
          <sf:input class="input_text" path="city" />
        </div>
        <div class="clr"></div>
        <div class="fl">
          <div style="margin-left: 30px; margin-top: 20px;" class="rm fr button_grey registerclose">
            <fmt:message key="website.cancel" />
          </div>
        </div>
        <div class="fr">
          <sf:button style="margin-right: 30px; margin-top: 20px;" class="rm fr button_blue">
            <fmt:message key="website.submit" />
          </sf:button>
        </div>
      </sf:form>
    </c:when>
    <c:otherwise>
      <h3 style="text-align: center;">
        <fmt:message key="registerform.headline" />
      </h3>
      <sf:form action="Register.do" commandName="UserDTO">
        <div class="fl">
          <sf:label path="forename">
            <fmt:message key="registerform.forname" />
          </sf:label>
        </div>
        <div class="fr">
          <sf:input class="input_text" path="forename" />
        </div>
        <div class="clr"></div>
        <div class="fl">
          <sf:label path="surname">
            <fmt:message key="registerform.surname" />
          </sf:label>
        </div>
        <div class="fr">
          <sf:input class="input_text" path="surname" />
        </div>
        <div class="clr"></div>
        <div class="fl">
          <sf:label path="mailadress">
            <fmt:message key="registerform.mail" />
          </sf:label>
        </div>
        <div class="fr">
          <sf:input type="email" required="required" class="input_text" path="mailadress" />
        </div>
        <div class="clr"></div>
        <div class="fl">
          <sf:label path="password">
            <fmt:message key="registerform.password" />
          </sf:label>
        </div>
        <div class="fr">
          <sf:password required="required" class="input_text" path="password" />
        </div>
        <div class="clr"></div>
        <div class="fl">
          <sf:label path="street">
            <fmt:message key="registerform.street" />
          </sf:label>
        </div>
        <div class="fr">
          <sf:input class="input_text" path="street" />
        </div>
        <div class="clr"></div>
        <div class="fl">
          <sf:label path="housenumber">
            <fmt:message key="registerform.number" />
          </sf:label>
        </div>
        <div class="fr">
          <sf:input class="input_text" path="housenumber" />
        </div>
        <div class="clr"></div>
        <div class="fl">
          <sf:label path="postcode">
            <fmt:message key="registerform.postcode" />
          </sf:label>
        </div>
        <div class="fr">
          <sf:input class="input_text" path="postcode" />
        </div>
        <div class="clr"></div>
        <div class="fl">
          <sf:label path="city">
            <fmt:message key="registerform.city" />
          </sf:label>
        </div>
        <div class="fr">
          <sf:input class="input_text" path="city" />
        </div>
        <div class="clr"></div>
        <div class="fl">
          <div style="margin-left: 30px; margin-top: 20px;" class="rm fr button_grey registerclose">
            <fmt:message key="website.cancel" />
          </div>
        </div>
        <div class="fr">
          <sf:button style="margin-right: 30px; margin-top: 20px;" class="rm fr button_blue">
            <fmt:message key="website.submit" />
          </sf:button>
        </div>
      </sf:form>
    </c:otherwise>
  </c:choose>



</div>
<div id="loginfade">
  <h3 style="text-align: center;">
    <fmt:message key="loginform.headline" />
  </h3>
  <sf:form action="Login.do" commandName="LoginDTO">
    <div class="fl">
      <sf:label path="mailadress">
        <fmt:message key="registerform.mail" />
      </sf:label>
    </div>
    <div class="fr">
      <sf:input type="email" required="required" class="input_text" path="mailadress" />
    </div>
    <div class="clr"></div>
    <div class="fl">
      <sf:label path="password">
        <fmt:message key="registerform.password" />
      </sf:label>
    </div>
    <div class="fr">
      <sf:password required="required" class="input_text" path="password" />
    </div>
    <div class="clr"></div>
    <div class="fl">
      <div style="margin-left: 30px; margin-top: 20px;" class="rm fr button_grey loginclose">
        <fmt:message key="website.cancel" />
      </div>
    </div>
    <div class="fr">
      <sf:button style="margin-right: 30px; margin-top: 20px;" class="rm fr button_blue">
        <fmt:message key="website.submit" />
      </sf:button>
    </div>
  </sf:form>
</div>
<!-- /registerform -->