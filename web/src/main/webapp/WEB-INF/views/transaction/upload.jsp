<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<spring:url value="/admin/upload" var="upload_url"/>
<form:form modelAttribute="uploadItem" enctype="multipart/form-data" action="${upload_url}" method="post">
    <div style="padding-top:80px;font-size:14px; width: 100%; text-align: center;">
        تراکنش های بانک صادرات:
        <form:input path="fileData" type="file"></form:input>
        <br />
        <br />
        <input type="submit" style="height:35px;" value="بارگذاری">
    </div>
</form:form>
