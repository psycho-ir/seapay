<%--
  Created by IntelliJ IDEA.
  User: payam
  Date: 1/22/13
  Time: 12:22 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.samenea.com/jsp/appfeature" prefix="af" %>
<spring:message code="master.menuDeposit" var="deposit"/>
<spring:message code="master.menuallDeposits" var="allDeposit"/>
<spring:message code="master.menuSetting" var="stting"/>
<spring:message code="master.menuChangePassword" var="changePassword"/>
<spring:message code="master.menuLastLogin" var="lastLogin"/>
<spring:message code="master.menuSecurity" var="security"/>
<spring:message code="master.menuTransaction" var="transaction"/>
<spring:message code="menu.services" var="services"/>
<spring:message code="menu.depositCharge" var="depositCharge_txt"/>
<spring:message code="menu.payLoan" var="payLoan_txt"/>
<spring:message code="menu.tracking" var="tracking_txt"/>
<spring:message code="menu.help" var="help"/>
<spring:message code="payment.faq.url" var="faq_url"/>
<spring:message code="payment.faq.text" var="faq_txt"/>
<spring:message code="returnToSamen" var="samen"/>
<spring:message code="menu.helpTracing" var="helpTracing_txt"/>
<spring:message code="menu.helpChargeDeposit" var="helpChargeDeposit_txt"/>
<spring:message code="menu.helpPayLoan" var="helpPayLoan_txt"/>
<spring:message code="menu.contactUS.title" var="contactus_title"/>
<spring:message code="menu.contactUS.technical.title" var="contactus_technical_txt"/>
<spring:url value="/deposit/charge" var="depositCharge_url"/>
<spring:url value="/loan/pay" var="payLoan_url"/>
<spring:url value="/tracking" var="tracking_url"/>
<spring:url value="/help/deposit" var="helpChargeDeposit_url"/>
<spring:url value="/help/loan" var="helpPayLoan_url"/>
<spring:url value="/help/tracking" var="helpTracking_url"/>
<spring:url value="/contactus/technical" var="contactus_technical_url"/>
<spring:url value="/resources/images/header-Item.png" var="item_img"/>
<spring:url value="/resources/images/headerMenu.jpg" var="headerMenu_img"/>

<table dir="rtl" border="0" cellpadding="0" cellspacing="0" width="100%">

    <tr valign="top">
        <td align="center" nowrap="nowrap" width="239px">


            <div class="rich-stglpanel ">

                <div class="rich-stglpanel-header menuStyle">
                    <div class="rich-stglpanel-marker">

                        <div class="rich-stglpnl-marker">
                            <img src="${headerMenu_img}" />
                        </div>
                    </div>
                    ${contactus_title}&nbsp;</div>
                <div class="rich-stglpanel-body ">
                    <table dir="rtl" border="0" cellpadding="2" cellspacing="2" width="100%">

                        <tr valign="middle">
                            <td align="center" nowrap="nowrap" width="5%">
                                <img src="${item_img}"/>
                            </td>
                            <td align="right" nowrap="nowrap" width="95%">

                                <a href="https://epay.ebsamen.com/payments/contactus/technical"
                                   class="menuItem">${contactus_technical_txt}</a>
                            </td>

                        </tr>
                        <tr valign="middle">
                            <td align="center" nowrap="nowrap" width="5%">
                                <img src="${item_img}"/>
                            </td>
                            <td align="right" nowrap="nowrap" width="95%">

                                <a href="${faq_url}"
                                   class="menuItem">${faq_txt}</a>
                            </td>
                        </tr>
                        <tr valign="middle">
                            <td align="center" nowrap="nowrap" width="5%">
                                <img src="${item_img}"/>
                            </td>
                        <td align="right" nowrap="nowrap" width="95%">

                            <a href="http://www.samen.ir/"
                               class="menuItem">${samen}</a>
                        </td>
                        </tr>
                    </table>
                </div>
            </div>


        </td>

    </tr>

</table>