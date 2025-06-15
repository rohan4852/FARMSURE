<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <div class="container mt-5">
        <div class="row justify-content-center">
            <div class="col-md-6">
                <div class="card">
                    <div class="card-header">
                        <h3 class="text-center mb-0">Login</h3>
                    </div>
                    <div class="card-body">
                        <c:if test="${not empty param.error}">
                            <div class="alert alert-danger">
                                Invalid username or password. Please make sure you entered the correct credentials.
                            </div>
                        </c:if>
                        <c:if test="${not empty param.logout}">
                            <div class="alert alert-success">
                                You have been successfully logged out.
                            </div>
                        </c:if>
                        <c:if test="${not empty param.registered}">
                            <div class="alert alert-success">
                                Registration successful! You can now login with your credentials.
                            </div>
                        </c:if>
                        <c:if test="${not empty success}">
                            <div class="alert alert-success">
                                ${success}
                            </div>
                        </c:if>
                        <form action="${pageContext.request.contextPath}/login" method="post" class="needs-validation"
                            novalidate>
                            <div class="mb-3">
                                <label for="username" class="form-label">Username</label>
                                <input type="text" class="form-control" id="username" name="username" required
                                    autofocus />
                            </div>
                            <div class="mb-3">
                                <label for="password" class="form-label">Password</label>
                                <input type="password" class="form-control" id="password" name="password" required />
                            </div>
                            <div class="mb-3 form-check">
                                <input type="checkbox" class="form-check-input" id="remember-me" name="remember-me" />
                                <label class="form-check-label" for="remember-me">Remember me</label>
                            </div>
                            <button type="submit" class="btn btn-primary w-100">Login</button>
                        </form>
                        <div class="mt-3 text-center">
                            <a href="${pageContext.request.contextPath}/register" class="text-decoration-none">Don't
                                have an account? Register here</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>