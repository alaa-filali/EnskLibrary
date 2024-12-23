<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="header.jsp" %>

<!-- Page Header -->
<header class="masthead" style="background-image: url('./static/img/profile-bg.jpg')">
    <div class="overlay"></div>
    <div class="container">
        <div class="row">
            <div class="col-lg-8 col-md-10 mx-auto">
                <div class="page-heading">
                    <h1>الملف الشخصي</h1>
                    <span class="subheading"><%= session.getAttribute("user")%></span>
                </div>
            </div>
        </div>
    </div>
</header>

<h2 class="text-center">الكتب المحجوزة</h2>
<br><br>
<%
    List<Map<String, String>> books = (List<Map<String, String>>) request.getAttribute("books");
    if (books == null || books.isEmpty()) { %>
<p class="text-center">
    لم تقم بحجز اي كتب
</p>
<% } else { %>
<!-- Reserved Books Section -->
<div class="container" style="direction: ltr;">
    <div class="row">
        <%
            if (books != null && !books.isEmpty()) {
                for (Map<String, String> book : books) {
        %>
        <div>
            <div class="card" style="width: 18rem;">
                <div class="card-body">
                    <h5 class="card-title">Title: <%= book.get("title")%></h5>
                    <p class="card-text">Author: <%= book.get("author")%></p>
                    <p class="card-text">ISBN: <%= book.get("isbn")%></p>
                    <% if (Integer.parseInt(book.get("daysLeft")) > 0) {%>
                    <p class="card-text">Return Book in: <%= book.get("daysLeft")%> days</p>
                    <% } else { %>
                    <p class="card-text">Return the book right now 😠</p>
                    <% }%>
                    <a href="./ReturnBook?book_id=<%= book.get("id")%>" class="btn btn-primary">Return</a>
                </div>
            </div>
        </div>
        <%
                }
            }
        %>
    </div>
</div>
<% }%>
<br><br>
<%@ include file="footer.jsp" %>