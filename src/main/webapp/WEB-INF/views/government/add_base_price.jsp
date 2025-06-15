<!DOCTYPE html>
<html>

<head>
    <title>Add Base Price</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" />
</head>

<body>
    <h1>Add New Base Price</h1>
    <form action="${pageContext.request.contextPath}/government/base-prices" method="post">
        <label for="productName">Product Name:</label>
        <input type="text" id="productName" name="productName" required />
        <br />
        <label for="price">Price:</label>
        <input type="number" id="price" name="price" step="0.01" required />
        <br />
        <label for="effectiveDate">Effective Date:</label>
        <input type="date" id="effectiveDate" name="effectiveDate" required />
        <br />
        <button type="submit">Add Base Price</button>
    </form>
    <a href="${pageContext.request.contextPath}/government/base-prices">Back to Base Prices</a>
</body>

</html>