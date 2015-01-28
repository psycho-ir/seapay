<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ page contentType="text/html; charset=utf-8" %>
<spring:url value="/resources/javascript/jquery-1.7.1.js" var="jquery_url"/>
<spring:url value="/resources/css/style.css" var="style"/>
<spring:message code="application_name" var="title"/>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>${title}</title>
    <link rel="stylesheet" type="text/css" media="screen" href="${style}"/>
    <script type="text/javascript" src="${jquery_url}"></script>
</head>
<body>
<style type="text/css">
    a {
        text-decoration: none;
    }
</style>
<table align="center" bgcolor="#ffffff" border="0" cellpadding="0" cellspacing="0">
    <tr valign="top">
        <td nowrap="nowrap" align="center">
    <tiles:insertAttribute name="header" ignore="true"/>
        </td>
    </tr>
    <tr>
        <td>
            <table style="width:100%;">
                <tr>
                    <td width="75%" style="vertical-align: top;">
    <tiles:insertAttribute name="body" ignore="true"/>
                    </td>
                    <td width="25%" style="vertical-align: top; padding-left:5px;">
                        <tiles:insertAttribute name="menu" ignore="true"/>
                    </td>

                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <td>
    <tiles:insertAttribute name="footer" ignore="true"/>
        </td>
    </tr>
    <tr valign="middle">
        <td style="padding-left:5px;font-size:11px;font-family:tahoma,verdana;text-align:center;">
             <span >${support}
                    <a href="http://www.samenea.com/">${Company}
                        ${samenea}
                    </a> </span>

        </td>
    </tr>
</table>
</body>
</html>