
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
import java.util.*;

@WebServlet(urlPatterns = {"/book_catalog"})
public class book_catalog extends HttpServlet {

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
        request.setAttribute("books", null);

        // Forward the request to the JSP page
        RequestDispatcher dispatcher = request.getRequestDispatcher("/book_catalog.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        String authorName = request.getParameter("author_name");
        String bookName = request.getParameter("book_name");

        // Initialize a list to store the book data
        List<Map<String, String>> books = new ArrayList<>();

        // Query the database
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            response.getWriter().write(e.getMessage());
        }
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT * FROM my_web_app.books WHERE title LIKE ? AND author LIKE ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, "%" + bookName + "%"); // Use LIKE to allow partial matches
                stmt.setString(2, "%" + authorName + "%");

                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        // Create a map for each book and add it to the list
                        Map<String, String> book = new HashMap<>();
                        book.put("id", String.valueOf(rs.getInt("id")));
                        book.put("title", rs.getString("title"));
                        book.put("author", rs.getString("author"));
                        book.put("isbn", rs.getString("isbn"));
                        book.put("availability", rs.getString("availability"));
                        books.add(book);
                    }
                }
            }
        } catch (SQLException e) {
            request.setAttribute("error", "An error occurred while fetching books: " + e.getMessage());
        }

        // Pass the books list to the request object
        request.setAttribute("books", books);

        // Forward the request to the JSP file
        RequestDispatcher dispatcher = request.getRequestDispatcher("/book_catalog.jsp");
        dispatcher.forward(request, response);
    }

}
