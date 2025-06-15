<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <div class="container mt-5">
        <div class="row justify-content-center">
            <div class="col-md-6">
                <div class="card">
                    <div class="card-header">
                        <h3 class="text-center mb-0">Register</h3>
                    </div>
                    <div class="card-body">
                        <c:if test="${not empty error}">
                            <div class="alert alert-danger">${error}</div>
                        </c:if>
                        <form action="${pageContext.request.contextPath}/register" method="post"
                            class="needs-validation" novalidate>
                            <div class="mb-3">
                                <label for="username" class="form-label">Username</label>
                                <input type="text" class="form-control" id="username" name="username" required
                                    autofocus />
                            </div>
                            <div class="mb-3">
                                <label for="password" class="form-label">Password</label>
                                <input type="password" class="form-control" id="password" name="password" required />
                            </div>
                            <div class="mb-3">
                                <label for="role" class="form-label">Role</label>
                                <select class="form-select" id="role" name="role" required>
                                    <option value="">Select a role</option>
                                    <option value="FARMER">Farmer</option>
                                    <option value="MERCHANT">Merchant</option>
                                    <option value="GOVERNMENT">Government</option>
                                </select>
                            </div>
                            <button type="submit" class="btn btn-primary w-100">Register</button>
                        </form>
                        <div class="mt-3 text-center">
                            <a href="${pageContext.request.contextPath}/login" class="text-decoration-none">Already have
                                an account? Login here</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>