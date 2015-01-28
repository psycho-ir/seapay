<%--
  Created by IntelliJ IDEA.
  User: payam
  Date: 2/19/13
  Time: 6:10 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<spring:url value="http://www.samen.ir" var="samen_url"/>
<spring:message code="confirm.return" var="return"/>
<spring:url value="/resources/images/PageNotFound.png" var="PageNotFound_image"/>
<spring:url value="/resources/images/return.png" var="return_img"/>

<script>
    $(document).ready(function () {
        $('#return').click(function () {
            window.location="${samen_url}";
        });
    });
</script>
<div class="rich-panel ">
    <div class="rich-panel-header ">
        <span class="richTitleText"> ${return}</span>
    </div>
    <div align="center">
        <img src="${PageNotFound_image}">
    </div>
    <div align="center">
        <button type="button"  id="return" class="negative">
            <img alt="" src="${return_img}">
            ${return}</button>
    </div>
</div>