<!DOCTYPE html>
<html>

<head>
    <title>Offer Price</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" />
</head>

<body>
    <h1>Offer Price for ${product.name}</h1>
    <form action="${pageContext.request.contextPath}/merchant/products/${product.id}/offer" method="post">
        <label for="price">Price:</label>
        <input type="number" id="price" name="price" step="0.01" required />
        <br />
        <button type="submit">Submit Offer</button>
    </form>
    <a href="${pageContext.request.contextPath}/merchant/products">Back to Products</a>
</body>

</html>