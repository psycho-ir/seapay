<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<spring:message code="redirect.title" var="title"/>
<spring:url value="/resources/images/" var="resource_url" />
<spring:url value="/resources/images/return.png" var="return_img"/>
<script type="text/javascript">
    $(document).ready(function(){
        $("#form").delay(4000).show(function(){
            $('#form').submit();
        });

    });
</script>
  <style type="text/css">
      h1{
          font-size:11px !important;
          font-family: tahoma,verdana;
      }
      form {
          padding-right: 50px;
      }
  </style>
<div class="rich-panel ">
    <div class="rich-panel-header ">
        <span class="richTitleText"> ${title}</span>
    </div>
    <div class="rich-panel-body ">
        <div align="center">
<c:if test="${not empty messages}">
    <c:forEach items="${messages}" var="message">
        <div>${message.text}</div>
    </c:forEach>
</c:if>
<c:if test="${empty messages}">
<form id="form" method="${method}" action="${url}" >
        <c:forEach items="${parameters}" var="parameter">
                <input type="hidden" name="${parameter.key}" value="${parameter.value}" />
        </c:forEach>
    <h1>${title}</h1>
    </form>

</c:if>
        <div align="center">
            <img src="${resource_url}redirect.gif">
        </div>
            </div>
</div>
</div>