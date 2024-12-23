<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="header.jsp" %>

<!-- Page Header -->
<header class="masthead"
    style="background-image: url('${pageContext.request.contextPath}/static/img/login-bg.jpg')">
    <div class="overlay"></div>
    <div class="container">
        <div class="row">
            <div class="col-lg-8 col-md-10 mx-auto">
                <div class="page-heading">
                    <h1>تسجيل الدخول</h1>
                    <span class="subheading">مرحبا بك مجددا</span>
                </div>
            </div>
        </div>
    </div>
</header>

<div class="container">
    <div class="row">

        <div class="col-lg-8 col-md-10 mx-auto content">
<!--            {% with messages = get_flashed_messages() %}
                {% if messages %}
                    {% for message in messages %}
                        <p>{{ message }}</p>
                    {% endfor %}
                {% endif %}
            {% endwith %}-->
            <div style="direction: ltr;">
            <form action="./login" method="post">
                    <label for="email">Email:</label>
                    <input type="text" id="email" name="email" required />

                    <label for="password">Password:</label>
                    <input type="password" id="password" name="password" required />

                    <button type="submit" class="btn btn-primary">Submit</button>
                </form></div>
        </div>
    </div>
</div>


<%@ include file="footer.jsp" %>
