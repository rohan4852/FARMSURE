<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
        <!DOCTYPE html>
        <html>

        <head>
            <title>Farmer Products</title>
            <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" />
        </head>

        <body>
            <h1>Your Products</h1>
            <a href="${pageContext.request.contextPath}/farmer/products/new">Add New Product</a>
            <table>
                <thead>
                    <tr>
                        <th>Name</th>
                        <th>Quantity</th>
                        <th>Quality</th>
                        <th>Created At</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="product" items="${products}">
                        <tr>
                            <td>${product.name}</td>
                            <td>${product.quantity}</td>
                            <td>${product.quality}</td>
                            <td>
                                <fmt:formatDate value="${product.createdAt}" pattern="yyyy-MM-dd HH:mm" />
                            </td>
                            <td>
                                <a href="${pageContext.request.contextPath}/farmer/products/${product.id}/prices">View
                                    Merchant Prices</a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </body>

        </html>