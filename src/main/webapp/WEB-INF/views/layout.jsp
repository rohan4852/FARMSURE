<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <!DOCTYPE html>
    <html lang="en">

    <head>
        <meta charset="UTF-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <title>
            <c:out value="${pageTitle != null ? pageTitle : 'Farma'}" />
        </title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" />
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet" />
    </head>

    <body>
        <div class="d-flex" id="wrapper">
            <%@ include file="/WEB-INF/views/includes/sidebar.jsp" %>

                <div id="page-content-wrapper" class="flex-grow-1">
                    <%@ include file="/WEB-INF/views/includes/navbar.jsp" %>

                        <div class="container-fluid mt-4">
                            <jsp:include page="${contentPage}" />
                        </div>

                        <%@ include file="/WEB-INF/views/includes/footer.jsp" %>
                </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>

    </html>