<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
        <!DOCTYPE html>
        <html>

        <head>
            <title>Transactions</title>
            <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" />
        </head>

        <body>
            <h1>Transactions</h1>
            <table>
                <thead>
                    <tr>
                        <th>Farmer</th>
                        <th>Merchant</th>
                        <th>Product</th>
                        <th>Price</th>
                        <th>Quantity</th>
                        <th>Transaction Date</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="transaction" items="${transactions}">
                        <tr>
                            <td>${transaction.farmer.username}</td>
                            <td>${transaction.merchant.username}</td>
                            <td>${transaction.product.name}</td>
                            <td>${transaction.price}</td>
                            <td>${transaction.quantity}</td>
                            <td>
                                <fmt:formatDate value="${transaction.transactionDate}" pattern="yyyy-MM-dd HH:mm" />
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </body>

        </html>