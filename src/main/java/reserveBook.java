
import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@WebServlet(urlPatterns = {"/reserveBook"})
public class reserveBook extends HttpServlet {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/my_web_app";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "admin";
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");

        String bookId = request.getParameter("book_id");
        if (bookId == null || bookId.isEmpty()) {
            response.getWriter().write("Invalid book ID.");
            return;
        }

        Connection connection = null;
        PreparedStatement bookStmt = null;
        PreparedStatement userStmt = null;
        PreparedStatement reserveStmt = null;
        PreparedStatement updateBookStmt = null;
        ResultSet bookRs = null;
        ResultSet userRs = null;

        try {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            // Fetch book details
            String bookQuery = "SELECT * FROM my_web_app.books WHERE id = ?";
            bookStmt = connection.prepareStatement(bookQuery);
            bookStmt.setString(1, bookId);
            bookRs = bookStmt.executeQuery();

            if (bookRs.next()) {

                // Check if user is logged in
                String userEmail = (String) request.getSession().getAttribute("user");
                if (userEmail == null) {
                    response.getWriter().write("No user is signed in.");
                    return;
                }

                // Fetch user ID
                String userQuery = "SELECT uid FROM my_web_app.users WHERE email = ?";
                userStmt = connection.prepareStatement(userQuery);
                userStmt.setString(1, userEmail);
                userRs = userStmt.executeQuery();

                if (userRs.next()) {
                    int userId = userRs.getInt("uid");

                    // Calculate due date
                    LocalDateTime dueDate = LocalDateTime.now().plus(15, ChronoUnit.DAYS);

                    // Reserve the book
                    String reserveQuery = "INSERT INTO my_web_app.reservations (userId, bookId, dueDate) VALUES (?, ?, ?)";
                    reserveStmt = connection.prepareStatement(reserveQuery);
                    reserveStmt.setInt(1, userId);
                    reserveStmt.setString(2, bookId);
                    reserveStmt.setString(3, dueDate.toString());
                    int rowsAffected = reserveStmt.executeUpdate();

                    if (rowsAffected > 0) {
                        // Update book availability
                        String updateBookQuery = "UPDATE my_web_app.books SET availability = 'checked_out' WHERE id = ?";
                        updateBookStmt = connection.prepareStatement(updateBookQuery);
                        updateBookStmt.setString(1, bookId);
                        int updateRows = updateBookStmt.executeUpdate();

                        if (updateRows > 0) {
                            request.setAttribute("message", "Book reserved successfully and marked as checked out!");
                            RequestDispatcher dispatcher = request.getRequestDispatcher("/book_catalog.jsp");
                            dispatcher.forward(request, response);
                        } else {
                            response.getWriter().write("Book reserved, but failed to update availability.");
                        }
                    } else {
                        response.getWriter().write("Failed to reserve the book.");
                    }
                } else {
                    response.getWriter().write("User not found.");
                }
            } else {
                response.getWriter().write("Book not found.");
            }
        } catch (ClassNotFoundException | SQLException e) {
            response.getWriter().write("Error: " + e.getMessage());
        } finally {
            try {
                if (bookRs != null) bookRs.close();
                if (userRs != null) userRs.close();
                if (bookStmt != null) bookStmt.close();
                if (userStmt != null) userStmt.close();
                if (reserveStmt != null) reserveStmt.close();
                if (updateBookStmt != null) updateBookStmt.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                response.getWriter().write("Cleanup Error: " + e.getMessage());
            }
        }
    }
}

//@WebServlet(urlPatterns = {"/reserveBook"})
//public class reserveBook extends HttpServlet {
//
//    private static final String DB_URL = "jdbc:mysql://localhost:3306/my_web_app";
//    private static final String DB_USER = "root";
//    private static final String DB_PASSWORD = "admin";
//    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
//
//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        response.setContentType("text/html");
//
//        String bookId = request.getParameter("book_id");
//        if (bookId == null || bookId.isEmpty()) {
//            response.getWriter().write("Invalid book ID.");
//            return;
//        }
//
//        Connection connection = null;
//        PreparedStatement bookStmt = null;
//        PreparedStatement userStmt = null;
//        PreparedStatement reserveStmt = null;
//        ResultSet bookRs = null;
//        ResultSet userRs = null;
//
//        try {
//            Class.forName(JDBC_DRIVER);
//            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
//
//            // Fetch book details
//            String bookQuery = "SELECT * FROM my_web_app.books WHERE id = ?";
//            bookStmt = connection.prepareStatement(bookQuery);
//            bookStmt.setString(1, bookId);
//            bookRs = bookStmt.executeQuery();
//
//            if (bookRs.next()) {
//
//                // Check if user is logged in
//                String userEmail = (String) request.getSession().getAttribute("user");
//                if (userEmail == null) {
//                    response.getWriter().write("No user is signed in.");
//                    return;
//                }
//
//                // Fetch user ID
//                String userQuery = "SELECT * FROM my_web_app.users WHERE email = ?";
//                userStmt = connection.prepareStatement(userQuery);
//                userStmt.setString(1, userEmail);
//                userRs = userStmt.executeQuery();
//
//                if (userRs.next()) {
//                    int userId = userRs.getInt("uid");
//
//                    // Calculate due date
//                    LocalDateTime dueDate = LocalDateTime.now().plus(15, ChronoUnit.DAYS);
//
//                    // Reserve the book
//                    String reserveQuery = "INSERT INTO my_web_app.reservations (userId, bookId, dueDate) VALUES (?, ?, ?)";
//                    reserveStmt = connection.prepareStatement(reserveQuery);
//                    reserveStmt.setInt(1, userId);
//                    reserveStmt.setString(2, bookId);
//                    reserveStmt.setString(3, dueDate.toString());
//                    int rowsAffected = reserveStmt.executeUpdate();
//
//                    if (rowsAffected > 0) {
//                        request.setAttribute("message", "Book reserved successfully!");
//                        RequestDispatcher dispatcher = request.getRequestDispatcher("/book_catalog.jsp");
//                        dispatcher.forward(request, response);
//                    } else {
//                        response.getWriter().write("Failed to reserve the book.");
//                    }
//                } else {
//                    response.getWriter().write("User not found.");
//                }
//            } else {
//                response.getWriter().write("Book not found.");
//            }
//        } catch (ClassNotFoundException | SQLException e) {
//            response.getWriter().write("Error: " + e.getMessage());
//        } finally {
//            try {
//                if (bookRs != null) {
//                    bookRs.close();
//                }
//                if (userRs != null) {
//                    userRs.close();
//                }
//                if (bookStmt != null) {
//                    bookStmt.close();
//                }
//                if (userStmt != null) {
//                    userStmt.close();
//                }
//                if (reserveStmt != null) {
//                    reserveStmt.close();
//                }
//                if (connection != null) {
//                    connection.close();
//                }
//            } catch (SQLException e) {
//                response.getWriter().write("Cleanup Error: " + e.getMessage());
//            }
//        }
//    }
//}

//@WebServlet(urlPatterns = {"/reserveBook"})
//public class reserveBook extends HttpServlet {
//
//    // Database connection parameters
//    private static final String DB_URL = "jdbc:mysql://localhost:3306/mysql";
//    private static final String DB_USER = "root";
//    private static final String DB_PASSWORD = "admin";
//
//    // JDBC driver name, for MySQL
//    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
//
//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        response.setContentType("text/html");
//
//        String bookId = request.getParameter("book_id");
//
//        Connection connection = null;
//        PreparedStatement statement = null;
//        ResultSet resultSet = null;
//
//        Map<String, String> book = new HashMap<>();
//
//        try {
//            // Load JDBC driver (optional for newer versions)
//            Class.forName(JDBC_DRIVER);
//            // Open a connection to the database
//            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
//
//            String sql = "select * from my_web_app.books where id = ?";
//            PreparedStatement stmt = connection.prepareStatement(sql);
//            stmt.setString(1, bookId);
//
//            // Execute the query
//            ResultSet rs = stmt.executeQuery(); // Check if user in db 
//
//            if (rs.next()) {
//                book.put("id", String.valueOf(rs.getInt("id")));
//                book.put("title", rs.getString("title"));
//                book.put("author", rs.getString("author"));
//                book.put("isbn", rs.getString("isbn"));
//                book.put("availability", rs.getString("availability"));
//
//                // Current date and time
//                LocalDateTime now = LocalDateTime.now();
//
//                // Add 15 days to the current date
//                LocalDateTime dueDate = now.plus(15, ChronoUnit.DAYS);
//
//                if (request.getSession().getAttribute("user") != null) {
//                    String sql2 = "select id from my_web_app.users where email = ?";
//                    PreparedStatement stmt2 = connection.prepareStatement(sql2);
//                    stmt.setString(1, request.getSession().getAttribute("user").toString());
//                    int userId = stmt2.executeQuery().getInt("id");
//
//                    String sql3 = "insert into my_web_app.reservations (userId, bookId, dueDate) VALUES (?, ?, ?)";
//                    PreparedStatement stmt3 = connection.prepareStatement(sql3);
//                    stmt.setString(1, String.valueOf(userId));
//                    stmt.setString(2, book.get("id"));
//                    stmt.setString(3, dueDate.toString());
//                    int rowsAffected = stmt3.executeUpdate();
//                    if (rowsAffected > 0) {
//                        request.setAttribute("books", null);
//
//                        // Forward the request to the JSP page
//                        RequestDispatcher dispatcher = request.getRequestDispatcher("/book_catalog.jsp");
//                        dispatcher.forward(request, response);
//                    }
//                } else {
//                    response.getWriter().write("no user signed in");
//                }
//            } else {
//                response.getWriter().write("Failed to reserve book.");
//            }
//
//        } catch (ServletException | IOException | ClassNotFoundException | SQLException e) {
//            response.getWriter().write(e.getMessage());
//        } finally {
//            // Clean up resources
//            try {
//                if (resultSet != null) {
//                    resultSet.close();
//                }
//                if (statement != null) {
//                    statement.close();
//                }
//                if (connection != null) {
//                    connection.close();
//                }
//            } catch (SQLException se) {
//                response.getWriter().write(se.getMessage());
//            }
//        }
//    }
//
//}
