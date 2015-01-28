<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<spring:message code="redirect.returntitle" var="title"/>
<spring:url value="/resources/images/" var="resource_url" />
<script type="text/javascript">
    $(document).ready(function(){
        $('#form').submit();
    });
</script>
<style type="text/css">
    h1{
        font-size:11px !important;
        font-family: tahoma,verdana;
    }
    form {
        padding-right: 20px;
    }
</style>
<div class="rich-panel ">
    <div class="rich-panel-header ">
        <span class="richTitleText"> ${return}</span>
    </div>
<div align="center">
<c:if test="${not empty messages}">
    <c:forEach items="${messages}" var="message">
        <div>${message.text}</div>
    </c:forEach>
</c:if>
<c:if test="${empty messages}">
    <form id="form" method="POST" action="${order.callbackUrl}" >
            <input type="hidden" name="result" value="${result}" />
            <input type="hidden" name="transactionId" value="${order.transactionId}" />
            <input type="hidden" name="BANK_REFERENCE_ID" value="${BANK_REFERENCE_ID}" />
            <h1>${title}</h1>
    </form>
       <div align="center">
    <img src="${resource_url}redirect.gif">
       </div>
</c:if>
</div>
    </div>