<%--
  Created by IntelliJ IDEA.
  User: soroosh
  Date: 1/8/13
  Time: 3:42 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<spring:message code="error.generic" var="title"/>
<spring:url value="/resources/images/" var="resource_url" />
<spring:url value="http://www.samen.ir" var="samen_url"/>
<spring:message code="confirm.return" var="return"/>
<spring:message code="technical.error.message" var="contactus_message"/>
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
        <span class="richTitleText"> ${title}</span>
    </div>
    <div class="rich-panel-body ">
        <div class="rich-panel-body ">
            <div id="content" style="font-family: tahoma,verdana;font-size:11px;direction:rtl;padding-top:20px;">${contactus_message}&nbsp
            </div>


        </div>
    <div align="center">
        <button type="button"  id="return" class="negative">
            <img alt="" src="${return_img}">
            ${return}</button>
    </div>
</div>
</div>