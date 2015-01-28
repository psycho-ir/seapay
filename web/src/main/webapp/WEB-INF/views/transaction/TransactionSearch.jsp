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
<spring:url value="/transaction/search" var="transaction_search_url"/>

<div>
    <div>
        <b>hi transaction search!!!!!!!!!</b>
        <form:form method="POST" commandName="transactionSearchViewModel" action="${transaction_search_url}">
            <form:input path="transactionId" id="transactionId"/>


            <br/>
            <input type="submit"/> <br/>
            <div>
            <span>${transactionViewModel.transactionId}</span>   <br/>
            <span>${transactionViewModel.amount}</span>   <br/>
            <span>${transactionViewModel.amountDepositLetter}</span>   <br/>
            <span>${transactionViewModel.description}</span>     <br/>
            <span>${transactionViewModel.status}</span>      <br/>
            </div>

        </form:form>
    </div>
</div>
