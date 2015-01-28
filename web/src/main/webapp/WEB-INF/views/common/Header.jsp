<%--
  Created by IntelliJ IDEA.
  User: payam
  Date: 1/22/13
  Time: 11:46 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<spring:url value="/resources/images/header.png" var="logo"/>

<table align="center" border="0" cellpadding="0" cellspacing="0">
    <tr valign="top">
        <td align="center" nowrap="nowrap">
            <a href="http://www.samen.ir/">
                <img name="arm" src="${logo}" border="0" width="995px" style="padding-bottom: 10px;">
            </a>
        </td>
    </tr>
    <tr valign="middle">
        <td align="center" nowrap="nowrap">
            <table align="center" class="headerTable" bgcolor="#828b8b" border="0" cellpadding="0" cellspacing="0" width="100%">
                <tr valign="middle">
                    <td align="left" width="30%">
                        <span class="topInfoTitle"></span>
                        <span class="topInfo"></span>
                    </td>

                </tr>
            </table>
        </td>
    </tr>
</table>