<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <jsp:directive.include file="/WEB-INF/views/layout.jsp" />
    <jsp:useBean id="pageTitle" class="java.lang.String" scope="request" />
    <% request.setAttribute("pageTitle", "Merchant Prices" );
        request.setAttribute("contentPage", "/farmer/merchant_prices_content.jsp" ); %>