
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
import java.util.HashMap;
import java.util.Map;

@WebServlet(urlPatterns = {"/bookDetails"})
public class bookDetails extends HttpServlet {

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

        String bookId = request.getParameter("book_id");

        // Query the database
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            response.getWriter().write(e.getMessage());
        }
        // Create a map for each book and add it to the list
        Map<String, String> book = new HashMap<>();
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT * FROM my_web_app.books WHERE id LIKE ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, bookId);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        book.put("id", String.valueOf(rs.getInt("id")));
                        book.put("title", rs.getString("title"));
                        book.put("author", rs.getString("author"));
                        book.put("isbn", rs.getString("isbn"));
                        book.put("availability", rs.getString("availability"));
                    }
                }
            }

            // Pass the books list to the request object
            request.setAttribute("book", book);

            // Forward the request to the JSP file
            RequestDispatcher dispatcher = request.getRequestDispatcher("book_details.jsp");
            dispatcher.forward(request, response);

        } catch (SQLException e) {
            request.setAttribute("error", "An error occurred while fetching books: " + e.getMessage());
        }
    }

}
