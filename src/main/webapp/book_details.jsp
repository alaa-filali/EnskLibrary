<%@page import="java.util.Map"%>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="header.jsp" %>

<%
    Map<String, String> book = (Map<String, String>) request.getAttribute("book");
%>

<!-- Page Header -->
<header class="masthead" style="background-image: url('./static/img/book.jpg')">
    <div class="overlay"></div>
    <div class="container">
        <div class="row">
            <div class="col-lg-8 col-md-10 mx-auto">
                <div class="page-heading">
                    <h1>معلومات اكثر حول الكتاب</h1>
                    <div style="direction: ltr;">
                        <span class="subheading">
                            <%= book.get("title") %>
                            -
                            <%= book.get("author") %>
                        </span>
                    </div>
                </div>
            </div>
        </div>
    </div>
</header>

<div class="container">
    <div class="row">

        <div class="col-lg-3 col-md-10 mx-auto content">
            <div style="direction: ltr;">  <!-- style="direction: ltr;" -->

                <div>
                    <div>
                        <h5>Title: <%= book.get("title") %></h5>
                        <p>
                        <ul>
                            <li>
                                Author: <%= book.get("author") %>
                            </li>
                            <li>
                                ISBN: <%= book.get("isbn") %>
                            </li>
                            <li>
                                Availability: <%= book.get("availability") %>
                                
                            </li>
                        </ul>
                        </p>
                        <% if (book.get("availability").toString().indexOf("checked_out") != -1) { %>
                        <a href="#" class="btn btn-primary disabled" disabled="disabled">Already Reserved</a>
                        <% } else { %>
                        <a href="./reserveBook?book_id=<%= book.get("id")%>" class="btn btn-primary">Reserve</a>
                        <% } %>
                    </div>

                </div>
            </div>
        </div>
    </div>


    <%@ include file="footer.jsp" %>