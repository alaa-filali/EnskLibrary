
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

@WebServlet(urlPatterns = {"/Signup"})
public class Signup extends HttpServlet {

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
        response.sendRedirect("./signup.jsp");
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

            // SQL query to check if the username exists
            String sql = "SELECT * FROM my_web_app.users WHERE email = ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, email);

            // Execute the query
            resultSet = statement.executeQuery();
            // Check if the username exists
            if (!resultSet.next()) {
                // Hash the password using BCrypt
                String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

                // Insert the user into the database
                String sql2 = "INSERT INTO my_web_app.users (email, password) VALUES (?, ?)";
                statement = connection.prepareStatement(sql2);
                statement.setString(1, email);
                statement.setString(2, hashedPassword);

                int rowsInserted = statement.executeUpdate();
                if (rowsInserted > 0) {
                    response.getWriter().write("User registered successfully.");
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
                    response.getWriter().write("Failed to register user.");
                }
            } else {
                // Username exists
                response.getWriter().write("Username already exists.");

//                response.sendRedirect("login.jsp");
            }
        } catch (Exception e) {
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
                se.printStackTrace();
                response.getWriter().write(se.getMessage());
            }
        }
    }

}
