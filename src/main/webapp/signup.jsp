<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="header.jsp" %>

<!-- Page Header -->
<header class="masthead" style="background-image: url('${pageContext.request.contextPath}/static/img/signup-bg.jpg')">
    <div class="overlay"></div>
    <div class="container">
        <div class="row">
            <div class="col-lg-8 col-md-10 mx-auto">
                <div class="page-heading">
                    <h1>انشاء حساب</h1>
                    <span class="subheading">انشئ حسابك الان لتتمكن من حجز كتبك</span>
                </div>
            </div>
        </div>
    </div>
</header>

<div class="container">
    <div class="row">
        <div class="col-lg-8 col-md-10 mx-auto">
            <div style="direction: ltr;">
                <form action="./Signup" method="post">
                    <label for="email">Email:</label>
                    <input type="text" id="email" name="email" required />

                    <label for="password">Password:</label>
                    <input type="password" id="password" name="password" required />

                    <button type="submit" class="btn btn-primary">Submit</button>
                </form>
            </div>
        </div>
    </div>
</div>

<%@ include file="footer.jsp" %>