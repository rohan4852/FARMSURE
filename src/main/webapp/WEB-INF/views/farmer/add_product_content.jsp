<!-- Original content of farmer/add_product.jsp should be placed here.
Since the current add_product.jsp only contains layout inclusion and pageTitle setting,
please provide the original content or confirm if this is the full content. -->
<div class="container mt-4">
    <h2>Add Product</h2>
    <form action="${pageContext.request.contextPath}/farmer/add_product" method="post" enctype="multipart/form-data">
        <div class="mb-3">
            <label for="productName" class="form-label">Product Name</label>
            <input type="text" class="form-control" id="productName" name="productName" required />
        </div>
        <div class="mb-3">
            <label for="productDescription" class="form-label">Description</label>
            <textarea class="form-control" id="productDescription" name="productDescription" rows="3"
                required></textarea>
        </div>
        <div class="mb-3">
            <label for="productPrice" class="form-label">Price</label>
            <input type="number" class="form-control" id="productPrice" name="productPrice" required />
        </div>
        <div class="mb-3">
            <label for="productImage" class="form-label">Image</label>
            <input type="file" class="form-control" id="productImage" name="productImage" />
        </div>
        <button type="submit" class="btn btn-primary">Add Product</button>
    </form>
</div>