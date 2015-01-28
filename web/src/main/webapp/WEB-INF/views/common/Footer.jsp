<%--
  Created by IntelliJ IDEA.
  User: payam
  Date: 1/22/13
  Time: 11:49 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<spring:message code="copyright" var="copyright"/>
<spring:message code="support" var="support"/>
<spring:message code="company" var="Company"/>
<spring:message code="samenea" var="samenea"/>
<spring:message code="contactus" var="contactus"/>
<spring:message code="support1" var="support1"/>
<spring:message code="terms" var="terms"/>
<spring:message code="privatepolicy" var="privatepolicy"/>
<spring:url value="/resources/images/samenea_logo.png" var="samenea_logo"/>
<spring:url value="/resources/images/footer.png" var="footer"/>
<table class="footerTable" align="center" border="0" cellpadding="2" cellspacing="2" width="100%">
    <tr valign="middle">
        <td align="center">
            <span class="footerText" style="font-family: tahoma,verdana;font-size:11px; ">
             <a href="http://www.samen.ir/">
                 ${copyright}
             </a>
            </span>
        </td>
    </tr>
</table>
