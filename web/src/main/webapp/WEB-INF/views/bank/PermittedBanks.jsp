<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<spring:url value="/resources/images/" var="resource_url"/>
<spring:message code="confirm.equals" var="equals"/>
<spring:message code="confirm.amount" var="amount"/>
<spring:message code="confirm.name" var="name"/>
<spring:message code="confirm.regard" var="regard"/>
<spring:message code="confirm.settle" var="settle"/>
<spring:message code="confirm.cancel" var="cancel"/>
<spring:message code="confirm.rial" var="rial"/>
<spring:message code="trasactionId" var="trasactionId"/>
<spring:message code="bankpay" var="bankpay"/>
<spring:message code="confirm.return" var="return"/>
<spring:url value="/transaction/cancel" var="cancel_url"/>
<spring:url value="/resources/images/return.png" var="return_img"/>


<style>
    * {
        font-family: tahoma, verdana !important;
    }

    .title {
        color: red;
    }

    .banks {
        float: left;
        padding-right: 20px;
        padding-top: 10px;
    }
</style>
<script>
    $(document).ready(function () {
        $('#return').click(function () {
            window.location = '${cancel_url}/${transactionId}';
        });
    });
</script>
<div class="rich-panel ">
    <div class="rich-panel-header ">
        <span class="richTitleText"> ${bankpay}</span>
    </div>
    <div class="rich-panel-body ">
        <p style="font-family:tahoma,verdana; font-size:12px; width:620px;direction:rtl; ">
            ${amount} <span class="title">${transactionViewModel.amount}&nbsp${rial}</span>
            ${equals}<span class="title"> ${transactionViewModel.amountDepositLetter}&nbsp;${rial}</span>
            &nbsp ${trasactionId}<span class="title"> &nbsp${transactionViewModel.transactionId}</span>
            &nbsp; ${settle}

        </p>
    </div>
    <c:if test="${not empty messages}">
        <c:forEach items="${messages}" var="message">
            <div>${message.text}</div>
        </c:forEach>
    </c:if>
    <div align="center" style="font-size:11px; ">
        <span>${bankpay}</span>
    </div>
    <div align="center" style="font-size:11px; ">
        <c:if test="${empty messages}">
            <c:if test="${not empty banks}">
                <table>
                    <tr>
                        <c:forEach items="${banks}" var="bank">
                            <td>
                                <div>
                                    <center>


                                        <table>
                                            <tr>
                                                <td>
                                                    <a href='<spring:url value="/banks/gateway/${bank}/${transactionId}"></spring:url>'>
                                                        <img width="50px" height="50px" class="img_bank"
                                                             src='${resource_url}<spring:message code="${bank}.imgAddress"  ></spring:message>'/>
                                                    </a>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td colspan="2">
                                                    <div style="width:70px ">
                                                        <a href='<spring:url value="/banks/gateway/${bank}/${transactionId}"></spring:url>'>
                                                            <spring:message code="${bank}.name"></spring:message>
                                                        </a>

                                                    </div>
                                                </td>
                                            </tr>
                                        </table>

                                    </center>
                                </div>
                            </td>


                        </c:forEach>
                    </tr>
                </table>
            </c:if>
        </c:if>
    </div>
    <div style="padding-top:50px;" align="center">

        <button type="button" id="return" class="negative">
            <img alt="" src="${return_img}">
            ${return}</button>


    </div>
</div>



