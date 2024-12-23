<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="header.jsp" %>

<style>
    html {
        scroll-behavior: smooth;
    }
</style>

<!-- Page Header -->
<header class="masthead" style="background-image: url('${pageContext.request.contextPath}/static/img/ens.jpg')">
    <div class="overlay"></div>
    <div class="container">
        <div class="row">
            <div class="col-lg-8 col-md-10 mx-auto">
                <div class="page-heading">
                    <h1>مكتبة المدرسة</h1>
                    <span class="subheading">المدرسة العليا للاساتذة بالقبة الشيخ البشير الابراهيمي</span>
                </div>
            </div>
        </div>
    </div>
</header>

<div class="container" id="books_table">
    <div class="row">

        <div class="col-lg-8 col-md-10 mx-auto content">
            <h1>Simple Search</h1>
            <br>
            <div style="direction: ltr;">
                <form action="./book_catalog" method="post">
                    <label for="author_name">Author Name: </label>
                    <input type="text" id="author_name" name="author_name"/>

                    <label for="book_name">Book Name: </label>
                    <input type="text" id="book_name" name="book_name"/>

                    <button type="submit" class="btn btn-primary">Submit</button>
                </form>
                <!-- Check if there's an error -->
                <%
                    String error = (String) request.getAttribute("error");
                    if (error != null) {
                %>
                <p style="color: red;"><%= error%></p>
                <%
                } else {
                %>
                <script>
                    // Scroll to the specified section when the page loads
                    document.addEventListener("DOMContentLoaded", function () {
                        var element = document.getElementById('books_table');
                        if (element) {
                            element.scrollIntoView();
                        }
                    });
                </script>
                <br><br>
                <% if (request.getAttribute("books") != null) { %>
                <!-- Display the list of books -->
                <table border="1">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Title</th>
                            <th>Author</th>
                            <th>ISBN</th>
                            <th>Availability</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            List<Map<String, String>> books = (List<Map<String, String>>) request.getAttribute("books");
                            if (books != null && !books.isEmpty()) {
                                for (Map<String, String> book : books) {
                        %>
                        <tr>
                            <td><%= book.get("id")%></td>
                            <td>
                                <a href="./bookDetails?book_id=<%= book.get("id")%>">
                                    <%= book.get("title")%>
                                </a>
                            </td>
                            <td><%= book.get("author")%></td>
                            <td><%= book.get("isbn")%></td>
                            <td><%= book.get("availability")%></td>
                        </tr>
                        <%
                            }
                        } else {
                        %>
                        <tr>
                            <td colspan="5">No books found.</td>
                        </tr>
                        <% } %>
                    </tbody>
                </table>
                <% }%>
                <% }%>
                
            </div>
        </div>
    </div>
</div>
<br><br>

<%@ include file="footer.jsp" %>