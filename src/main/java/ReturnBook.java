
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet(urlPatterns = {"/ReturnBook"})
public class ReturnBook extends HttpServlet {

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

        Connection connection = null;
        PreparedStatement deleteReservationStmt = null;
        PreparedStatement updateBookStmt = null;

        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            response.getWriter().write(e.getMessage());
        }
        try {
            // Database connection
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            // 1. Delete the reservation record
            String deleteReservationSql = "DELETE FROM my_web_app.reservations WHERE bookId = ?";
            deleteReservationStmt = connection.prepareStatement(deleteReservationSql);
            deleteReservationStmt.setString(1, bookId);
            int rowsDeleted = deleteReservationStmt.executeUpdate();

            if (rowsDeleted > 0) {
                // 2. Update the availability of the book
                String updateBookSql = "UPDATE my_web_app.books SET availability = 'available' WHERE id = ?";
                updateBookStmt = connection.prepareStatement(updateBookSql);
                updateBookStmt.setString(1, bookId);
                int rowsUpdated = updateBookStmt.executeUpdate();

                if (rowsUpdated > 0) {
                    response.sendRedirect("./Profile");
                } else {
                    response.getWriter().write("Failed to update book availability.");
                }
            } else {
                response.getWriter().write("No reservation found for this user and book.");
            }

        } catch (SQLException e) {
            response.getWriter().write("Error: " + e.getMessage());
        } finally {
            try {
                if (deleteReservationStmt != null) {
                    deleteReservationStmt.close();
                }
                if (updateBookStmt != null) {
                    updateBookStmt.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                response.getWriter().write("Error: " + e.getMessage());
            }
        }
    }
}
