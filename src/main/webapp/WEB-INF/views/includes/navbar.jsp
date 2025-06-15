<nav class="navbar navbar-expand-lg navbar-light bg-light border-bottom">
    <div class="container-fluid">
        <button class="btn btn-primary" id="menu-toggle">Toggle Menu</button>
        <div class="collapse navbar-collapse" id="navbarSupportedContent">
            <ul class="navbar-nav ms-auto mt-2 mt-lg-0">
                <li class="nav-item">
                    <form action="${pageContext.request.contextPath}/logout" method="post" class="d-flex">
                        <button type="submit" class="btn btn-outline-danger">Logout</button>
                    </form>
                </li>
            </ul>
        </div>
    </div>
</nav>

<script>
    document.getElementById("menu-toggle").addEventListener("click", function (e) {
        e.preventDefault();
        document.getElementById("sidebar-wrapper").classList.toggle("d-none");
    });
</script>