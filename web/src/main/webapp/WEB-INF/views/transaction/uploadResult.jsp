<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<spring:url value="/transaction/upload" var="upload_url"/>
<div style="padding-top: 80px; font-size:14px; width: 100%; text-align: center;">
    تعداد تراکنشهای بارگذاری شده:
    ${succeed}
    عدد
    <br/>
    تراکنشهای تکراری:
    <br/>
    <c:forEach items="${duplicates}" var="item">
        ${item}
        <br/>
    </c:forEach>
</div>

