
import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.security.crypto.bcrypt.BCrypt;

@WebServlet(urlPatterns = {"/login"})
public class login extends HttpServlet {

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
        response.sendRedirect("./login.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // check if user is already in the db
        if (email == null || email.trim().isEmpty()) {
            response.getWriter().write("email is required.");
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

            String sql = "SELECT password FROM my_web_app.users WHERE email = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, email);

            // Execute the query
            ResultSet rs = stmt.executeQuery();
            
            // Check if user in db 
            if (rs.next()) {
                String hashedPasswordFromDatabase = rs.getString("password"); // Retrieve the password
                boolean isPasswordMatch = BCrypt.checkpw(password, hashedPasswordFromDatabase);
                if (isPasswordMatch) {
                    // create a session
                HttpSession session = request.getSession();
                session.setAttribute("user", email);
                // redirect to home page
                // Pass the data to the request object
                request.setAttribute("books", null);
                // Forward the request to the JSP page
                RequestDispatcher dispatcher = request.getRequestDispatcher("/book_catalog.jsp");
                dispatcher.forward(request, response);
                } else {
                     response.getWriter().write("Password don't match.");
                }
            } else {
                response.getWriter().write("Failed to login user.");
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
