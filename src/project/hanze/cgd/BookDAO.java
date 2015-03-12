/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.hanze.cgd;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import project.hanze.cdp.Book;

/**
 *
 * @author juniorm10
 */
public class BookDAO extends DAOAbstract implements DAO<Book> {

    public static BookDAO getInstance() {
        return BookDAOHolder.INSTANCE;
    }

    private static class BookDAOHolder {

        private static final BookDAO INSTANCE = new BookDAO();
    }

    @Override
    public void create() {
        try {
            this.openConnection();
            String sql = "CREATE TABLE IF NOT EXISTS BOOK ("
                    + "Id INTEGER PRIMARY KEY   AUTOINCREMENT,"
                    + "title VARCHAR(50) NOT NULL,"
                    + "author VARCHAR(50) NOT NULL,"
                    + "borrowed BOOLEAN NOT NULL)";
            this.execute(sql);
            this.closeConnection();
        } catch (SQLException ex) {
            Logger.getLogger(BookDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void insert(Book obj) {
        try {
            this.openConnection();
            String sql = "INSERT INTO BOOK (title,author,borrowed) VALUES (?,?,?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, obj.getTitle());
                preparedStatement.setString(2, obj.getAuthor());
                preparedStatement.setBoolean(3, false);
                preparedStatement.executeUpdate();
            }
            this.closeConnection();
        } catch (SQLException ex) {
            Logger.getLogger(BookDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public ArrayList<Book> read(Class<Book> classe) {
        return null;
    }

    public ArrayList<String> readAuthor() {
        ArrayList<String> booksAuthor = new ArrayList<>();
        try {
            this.openConnection();
            String sql = "SELECT DISTINCT author FROM BOOK order by author";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                ResultSet r = preparedStatement.executeQuery();
                while (r.next()) {
                    booksAuthor.add(r.getString("author"));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(BookDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return booksAuthor;
    }

    public ArrayList<String> readBooksByAuthor(String author) {
        ArrayList<String> booksTitle = new ArrayList<>();
        try {
            this.openConnection();
            String sql = "SELECT DISTINCT title FROM BOOK  where author = ? order by title";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, author);
                ResultSet r = preparedStatement.executeQuery();
                while (r.next()) {
                    booksTitle.add(r.getString("title"));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(BookDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return booksTitle;
    }

    public int countBooksByAuthorAndTitle(String author, String title) {
        try {
            this.openConnection();

            String sql = "SELECT count(title)  AS total FROM BOOK where author= ? and title= ?";
            int total;
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, author);
                preparedStatement.setString(2, title);
                ResultSet r = preparedStatement.executeQuery();
                total = 0;
                while (r.next()) {
                    total = Integer.valueOf(r.getString("total"));
                }
            }
            this.closeConnection();
            return total;
        } catch (SQLException ex) {
            Logger.getLogger(BookDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public int verifyAvailable(String title, String author) {
        try {
            this.openConnection();
            String sql = "SELECT  *  FROM BOOK WHERE author= ? and title= ? and borrowed= ?";
            int id;
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, author);
                preparedStatement.setString(2, title);
                preparedStatement.setInt(3, 0);
                ResultSet r = preparedStatement.executeQuery();
                id = 0;
                if (r.next()) {
                    id = r.getInt("id");
                }
            }
            this.closeConnection();
            return id;
        } catch (SQLException ex) {
            Logger.getLogger(BookDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public void delete(Book obj) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
