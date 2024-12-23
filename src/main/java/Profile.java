
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(urlPatterns = {"/Profile"})
public class Profile extends HttpServlet {

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
        String email = (String) request.getSession().getAttribute("user");

        if (email == null) {
            response.getWriter().write("No user is signed in.");
            return;
        }

        // Initialize a list to store the book data
        List<Map<String, String>> books = new ArrayList<>();

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            // Load JDBC driver (optional for newer versions)
            Class.forName(JDBC_DRIVER);
            // Open a connection to the database
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            String sql = "select uid from my_web_app.users where email = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, email);

            // Execute the query
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {

                int userId = rs.getInt("uid");
                String sql2 = """
                            SELECT 
                                b.id AS book_id, 
                                b.title AS book_title, 
                                b.author AS book_author, 
                                b.isbn AS book_isbn, 
                                b.availability AS book_availability, 
                                r.dueDate AS due_date 
                            FROM 
                                my_web_app.reservations r 
                            JOIN 
                                my_web_app.users u ON r.userId = u.uid 
                            JOIN 
                                my_web_app.books b ON r.bookId = b.id 
                            WHERE 
                                u.uid = ?;
                        """;
                PreparedStatement stmt2 = connection.prepareStatement(sql2);
                stmt2.setString(1, String.valueOf(userId));

                ResultSet rs2 = stmt2.executeQuery();

                while (rs2.next()) {

                    // Parse the dueDate string into a LocalDate object
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    LocalDate dueDate = LocalDate.parse(rs2.getString("due_date"), formatter);

                    // Get the current date
                    LocalDate today = LocalDate.now();

                    // Calculate the days left
                    long daysLeft = ChronoUnit.DAYS.between(today, dueDate);
                    int daysLeftAsInt = Math.toIntExact(daysLeft);
                    
                    Map<String, String> book = new HashMap<>();
                    book.put("id", String.valueOf(rs2.getInt("book_id")));
                    book.put("title", rs2.getString("book_title"));
                    book.put("author", rs2.getString("book_author"));
                    book.put("isbn", rs2.getString("book_isbn"));
                    book.put("availability", rs2.getString("book_availability"));
                    book.put("daysLeft", String.valueOf(daysLeftAsInt));
                    books.add(book);
                }

                // Pass the books list to the request object
                request.setAttribute("books", books);

                // Forward the request to the JSP file
                RequestDispatcher dispatcher = request.getRequestDispatcher("./profile.jsp");
                dispatcher.forward(request, response);

            } else {
                response.getWriter().write("Failed to load user profile.");
            }

        } catch (IOException | ClassNotFoundException | SQLException e) {
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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

}
