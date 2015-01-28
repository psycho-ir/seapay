<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>

  <div style="padding-top: 20px;padding-right: 50px;">
<a href="<spring:url value="/bank/${transactionId}/interpret?Status=OK" />">OK</a>
<br/>
<a href="<spring:url value="/bank/${transactionId}/interpret?Status=Failed" />">FAILED</a>
  </div>