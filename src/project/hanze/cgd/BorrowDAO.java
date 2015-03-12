/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.hanze.cgd;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author G.A.Y
 */
public class BorrowDAO extends DAOAbstract {

    public static BorrowDAO getInstance() {
        return BorrowDAOHolder.INSTANCE;
    }

    private static class BorrowDAOHolder {

        private static final BorrowDAO INSTANCE = new BorrowDAO();
    }

    public void create() {
        try {
            this.openConnection();
            String sql = "CREATE TABLE IF NOT EXISTS BORROW ("
                    + "Id INTEGER PRIMARY KEY   AUTOINCREMENT,"
                    + "id_student int NOT NULL,"
                    + "id_book int NOT NULL,"
                    + "date VARCHAR(15) NOT NULL)";
            this.execute(sql);
            this.closeConnection();
        } catch (SQLException ex) {
            Logger.getLogger(BorrowDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void insert(int idStudent, int idBook) {
        try {
            this.openConnection();
            String sql = "INSERT INTO BORROW (id_student,id_book,date) VALUES (?,?,?)";
            Calendar c = Calendar.getInstance();
            SimpleDateFormat formater = new SimpleDateFormat("yyyy/MM/dd");
            String data = formater.format(c.getTime());
            
            System.out.println(data);
            System.out.println("idStudent ->" + idStudent);
            System.out.println("idBook ->" + idBook);
            
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, idStudent);
                preparedStatement.setInt(2, idBook);
                preparedStatement.setString(3, data);
                preparedStatement.executeUpdate();
            }
            
            this.closeConnection();
        } catch (SQLException ex) {
            Logger.getLogger(BorrowDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
