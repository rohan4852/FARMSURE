<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <jsp:directive.include file="/WEB-INF/views/layout.jsp" />
    <jsp:useBean id="pageTitle" class="java.lang.String" scope="request" />
    <% request.setAttribute("pageTitle", "Add Product" );
        request.setAttribute("contentPage", "/farmer/add_product_content.jsp" ); %>