<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" %>
<html lang="ar" dir="rtl">

    <head>

        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        <meta name="description" content="">
        <meta name="author" content="">

        <title>مكتبة المدرسة</title>

        <!-- Bootstrap core CSS -->
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/css/bootstrap.min.css" integrity="sha384-TX8t27EcRE3e/ihU7zmQxVncDAy5uIKz4rEkgIXeMed4M0jlfIDPvg6uqKI2xXr2" crossorigin="anonymous">

        <!-- <link href="vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet"> -->

        <!-- Custom fonts for this template -->
        <!-- <link href="{{ url_for('static', filename='css/all.min.css') }}" rel="stylesheet" type="text/css"> -->
        <!-- <link href='https://fonts.googleapis.com/css?family=Lora:400,700,400italic,700italic' rel='stylesheet' type='text/css'>
        <link href='https://fonts.googleapis.com/css?family=Open+Sans:300italic,400italic,600italic,700italic,800italic,400,300,600,700,800' rel='stylesheet' type='text/css'> -->
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link href="https://fonts.googleapis.com/css2?family=El+Messiri:wght@400..700&family=Gloria+Hallelujah&family=Inter:wght@400;500;700&display=swap" rel="stylesheet" type='text/css'>

        <!-- Custom styles for this template -->
        <link href="static/css/clean-blog.min.css" rel="stylesheet">

    </head>

    <body>

        <!-- Navigation -->
        <nav class="navbar navbar-expand-md navbar-light fixed-top" id="mainNav">
            <div class="container">
                <a class="navbar-brand" href="${pageContext.request.contextPath}/index.jsp">مكتبة المدرسة</a>
                <button class="navbar-toggler navbar-toggler-right" type="button" data-toggle="collapse" data-target="#navbarResponsive" aria-controls="navbarResponsive" aria-expanded="false" aria-label="Toggle navigation">
                    Menu
                    <i class="fas fa-bars"></i>
                </button>
                <div class="collapse navbar-collapse" id="navbarResponsive">
                    <ul class="navbar-nav mr-auto">
                        <li class="nav-item dropdown">
                            <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                خدمات عن بعد
                            </a>
                            <div class="dropdown-menu" aria-labelledby="navbarDropdown">
                                <a class="dropdown-item" href="${pageContext.request.contextPath}/book_catalog.jsp">فهرس المكتبة</a>
                                <a class="dropdown-item" href="${pageContext.request.contextPath}/login.jsp">التسجيل في المكتبة</a>
                                <a class="dropdown-item" href="https://st.iqraa.opu.dz/">التسجيل في منصة اقرأ</a>
                                <% if (session.getAttribute("user") != null) { %>
                                <% if (session.getAttribute("user").toString().indexOf("admin@admin.com") != -1) { %>
                                <div class="dropdown-divider"></div>
                                <a class="dropdown-item" href="./addBook">اضافة كتاب</a>
                                <% }%>
                                <% }%>
                            </div>
                        </li>
                        <li class="nav-item dropdown">
                            <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                روابط مهمة
                            </a>
                            <div class="dropdown-menu" aria-labelledby="navbarDropdown">
                                <a class="dropdown-item" href="http://moodle.ens-kouba.dz/">منصة التعلم عن بعد (moodle)</a>
                                <a class="dropdown-item" href="#">المكتبة الرقمية للمدرسة (Library)</a>
                                <a class="dropdown-item" href="#">المستودع المؤسسي للمدرسة (Dspace)</a>
                                <div class="dropdown-divider"></div>
                                <a class="dropdown-item" href="${pageContext.request.contextPath}/regle.jsp">القانون الداخلي للمكتبة</a>
                                <a class="dropdown-item" href="${pageContext.request.contextPath}/about.jsp">عن المكتبة</a>
                                <a class="dropdown-item" href="${pageContext.request.contextPath}/contact.jsp">اتصل بنا</a>
                            </div>
                        </li>
                        <% if (session.getAttribute ( 
                                "user") != null) { %>
                        <li class="nav-item">
                            <a class="nav-link" href="./logout">تسجيل الخروج</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="./Profile">
                                <%=session.getAttribute("user")%>
                            </a>
                        </li>
                        <% }

                            
                            
                        else { %>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/login.jsp">تسجيل الدخول</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/signup.jsp">انشاء حساب</a>
                        </li>
                        <% }%>
                    </ul>
                </div>
            </div>
        </nav>
