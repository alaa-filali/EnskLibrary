
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

@WebServlet(urlPatterns = {"/addBook"})
public class addBook extends HttpServlet {

    // Database connection parameters
    private static final String DB_URL = "jdbc:mysql://localhost:3306/mysql";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "admin";

    // JDBC driver name, for MySQL
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        response.sendRedirect("add_book.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        String authorName = request.getParameter("author_name");
        String bookName = request.getParameter("book_name");
        String isbn = request.getParameter("isbn");
        String availability = request.getParameter("availability");
        
        // check if user is already in the db
        if (authorName == null || bookName.trim().isEmpty()) {
            response.getWriter().write("author name and book name are required.");
            return;
        }
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            // Load JDBC driver (optional for newer versions)
            Class.forName(JDBC_DRIVER);
            // Open a connection to the database
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            String sql = "INSERT INTO my_web_app.books (title, author, isbn, availability) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, bookName);
            stmt.setString(2, authorName);
            stmt.setString(3, isbn);
            stmt.setString(4, availability);

            // Execute the query
            int rowsAffected = stmt.executeUpdate();
            
            // Check if user in db 
            if (rowsAffected > 0) {
                request.setAttribute("books", null);
                // Forward the request to the JSP page
                RequestDispatcher dispatcher = request.getRequestDispatcher("/book_catalog.jsp");
                dispatcher.forward(request, response);
            } else {
                response.getWriter().write("Failed to add book.");
            }

        } catch (ServletException | IOException | ClassNotFoundException | SQLException e) {
            response.getWriter().write(e.getMessage());
        } finally {
            // Clean up resources
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException se) {
                response.getWriter().write(se.getMessage());
            }
        }
    }

}
