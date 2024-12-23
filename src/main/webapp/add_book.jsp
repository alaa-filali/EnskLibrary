<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="header.jsp" %>

<!-- Page Header -->
<header class="masthead"
        style="background-image: url('./static/img/add-bg.jpg')">
    <div class="overlay"></div>
    <div class="container">
        <div class="row">
            <div class="col-lg-8 col-md-10 mx-auto">
                <div class="page-heading">
                    <h1>اضافة كتاب</h1>
                    <span class="subheading">قم باضافة كتاب والمعلومات الخاصة به</span>
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
                <form action="./addBook" method="post">
                    <label for="author_name">Author Name: </label>
                    <input type="text" id="author_name" name="author_name"/>

                    <label for="book_name">Book Name: </label>
                    <input type="text" id="book_name" name="book_name"/>

                    <label for="isbn">ISBN: </label>
                    <input type="text" id="isbn" name="isbn"/>
                    <label for="availability">Availability:</label>
                    <select id="availability" name="availability">
                        <option value="available">Available</option>
                        <option value="checked_out">Checked Out</option>
                    </select>

                    <button type="submit" class="btn btn-primary">Submit</button>
                </form></div>
        </div>
    </div>
</div>


<%@ include file="footer.jsp" %>