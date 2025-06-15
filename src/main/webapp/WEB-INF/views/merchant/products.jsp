<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <!DOCTYPE html>
    <html>

    <head>
        <title>Merchant Products</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" />
    </head>

    <body>
        <h1>Available Products</h1>
        <table>
            <thead>
                <tr>
                    <th>Name</th>
                    <th>Quantity</th>
                    <th>Quality</th>
                    <th>Farmer</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="product" items="${products}">
                    <tr>
                        <td>${product.name}</td>
                        <td>${product.quantity}</td>
                        <td>${product.quality}</td>
                        <td>${product.farmer.username}</td>
                        <td>
                            <a href="${pageContext.request.contextPath}/merchant/products/${product.id}/offer">Offer
                                Price</a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </body>

    </html>