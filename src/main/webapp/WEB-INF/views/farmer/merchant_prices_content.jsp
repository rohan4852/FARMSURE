<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
        <h1>Merchant Prices for ${product.name}</h1>
        <table>
            <thead>
                <tr>
                    <th>Merchant</th>
                    <th>Price</th>
                    <th>Offered At</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="offer" items="${priceOffers}">
                    <tr>
                        <td>${offer.merchant.username}</td>
                        <td>${offer.price}</td>
                        <td>
                            <fmt:formatDate value="${offer.createdAt}" pattern="yyyy-MM-dd HH:mm" />
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        <a href="${pageContext.request.contextPath}/farmer/products">Back to Products</a>