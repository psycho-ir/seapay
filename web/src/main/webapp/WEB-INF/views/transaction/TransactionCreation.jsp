<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<spring:url value="/transaction/create" var="createUrl"/>

<div style="padding-top:20px;padding-right: 50px; ">
    <form:form modelAttribute="transaction" action="${createUrl}" method="post">
        <form:errors path="*" element="div">
        </form:errors>
        <form:input path="amount" type="text" id="amount" name="amount"/>
        <br/>
        <form:input path="description" type="text" id="description" name="description"/>
        <br/>
        <form:button/>
    </form:form>
</div>