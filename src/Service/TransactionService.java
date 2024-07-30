package Service;

import Database.DatabaseConnection;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TransactionService {
    public boolean checkoutBook(int userId, int bookId) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            connection.setAutoCommit(false);

            String updateBookQuery = "UPDATE books SET available = false WHERE id = ? AND available = true";
            PreparedStatement updateBookStatement = connection.prepareStatement(updateBookQuery);
            updateBookStatement.setInt(1, bookId);
            int rowsUpdated = updateBookStatement.executeUpdate();

            if (rowsUpdated > 0) {
                String insertTransactionQuery = "INSERT INTO transactions (user_id, book_id, checkout_date) VALUES (?, ?, ?)";
                PreparedStatement insertTransactionStatement = connection.prepareStatement(insertTransactionQuery);
                insertTransactionStatement.setInt(1, userId);
                insertTransactionStatement.setInt(2, bookId);
                insertTransactionStatement.setDate(3, new Date(System.currentTimeMillis()));
                int rowsInserted = insertTransactionStatement.executeUpdate();

                if (rowsInserted > 0) {
                    connection.commit();
                    return true;
                }
            }

            connection.rollback();
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean returnBook(int userId, int bookId) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            connection.setAutoCommit(false);

            String updateTransactionQuery = "UPDATE transactions SET return_date = ? WHERE user_id = ? AND book_id = ? AND return_date IS NULL";
            PreparedStatement updateTransactionStatement = connection.prepareStatement(updateTransactionQuery);
            updateTransactionStatement.setDate(1, new Date(System.currentTimeMillis()));
            updateTransactionStatement.setInt(2, userId);
            updateTransactionStatement.setInt(3, bookId);
            int rowsUpdated = updateTransactionStatement.executeUpdate();

            if (rowsUpdated > 0) {
                String updateBookQuery = "UPDATE books SET available = true WHERE id = ?";
                PreparedStatement updateBookStatement = connection.prepareStatement(updateBookQuery);
                updateBookStatement.setInt(1, bookId);
                int bookRowsUpdated = updateBookStatement.executeUpdate();

                if (bookRowsUpdated > 0) {
                    connection.commit();
                    return true;
                }
            }

            connection.rollback();
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
