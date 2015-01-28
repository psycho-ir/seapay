<%--
  Created by IntelliJ IDEA.
  User: ngs
  Date: 2/9/13
  Time: 4:09 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<spring:url value="/transaction/findUnstables" var="transaction_unstable_url"/>

<div>
    <div>
        <b>hi transaction search!!!!!!!!!</b>
        <form method="POST" action="${transaction_unstable_url}">
            <input type="submit" value="Find All unstable transactions"/>
        </form>



    </div>
</div>
