<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
        <!DOCTYPE html>
        <html>

        <head>
            <title>Base Prices</title>
            <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" />
        </head>

        <body>
            <h1>Base Prices</h1>
            <a href="${pageContext.request.contextPath}/government/base-prices/new">Add New Base Price</a>
            <table>
                <thead>
                    <tr>
                        <th>Product Name</th>
                        <th>Price</th>
                        <th>Effective Date</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="basePrice" items="${basePrices}">
                        <tr>
                            <td>${basePrice.productName}</td>
                            <td>${basePrice.price}</td>
                            <td>
                                <fmt:formatDate value="${basePrice.effectiveDate}" pattern="yyyy-MM-dd" />
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </body>

        </html>