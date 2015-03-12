 <<<<<<< Updated upstream
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.hanze.cgd;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import project.hanze.cdp.Student;



public class StudentDAO extends DAOAbstract implements DAO<Student> {

    public static StudentDAO getInstance() {
        return StudentDAOHolder.INSTANCE;
    }

    private static class StudentDAOHolder {

        private static final StudentDAO INSTANCE = new StudentDAO();
    }

    @Override
    public void create() {
        try {
            this.openConnection();
            String sql = "CREATE TABLE IF NOT EXISTS STUDENT ("
                    + "Id INTEGER PRIMARY KEY   AUTOINCREMENT,"
                    + "name VARCHAR(50) NOT NULL,"
                    + "studentId VARCHAR(4) NOT NULL UNIQUE,"
                    + "telephoneNumber VARCHAR(20) NOT NULL,"
                    + "money DOUBLE NOT NULL,"
                    + "loginAttempts int NOT NULL,"
                    + "booksBorrowed int NOT NULL"
                    + ")";
            this.execute(sql);
            this.closeConnection();
        } catch (SQLException ex) {
            Logger.getLogger(StudentDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void insert(Student obj) {
        try {
            this.openConnection();

            String sql = "INSERT INTO STUDENT (name,studentId,telephoneNumber,"
                    + "money,loginAttempts,booksBorrowed) VALUES (?,?,?,?,?,?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, obj.getName());
                preparedStatement.setString(2, obj.getStudentId());
                preparedStatement.setString(3, obj.getTelephoneNumber());
                preparedStatement.setDouble(4, obj.getMoney());
                preparedStatement.setInt(5, 0);
                preparedStatement.setInt(6, 0);
                preparedStatement.executeUpdate();
            }
            this.closeConnection();
        } catch (SQLException ex) {
            Logger.getLogger(StudentDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void delete(Student obj) {
        try {
            this.openConnection();
            String sql = "DELETE FROM STUDENT WHERE studentId = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, obj.getStudentId());
                preparedStatement.executeUpdate();
            }
            this.closeConnection();
        } catch (SQLException ex) {
            Logger.getLogger(StudentDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int getLoginAttemptsByName(String name) {
        try {
            this.openConnection();
            String sql = "SELECT loginAttempts FROM STUDENT WHERE name = ?";
            ResultSet resultSet;
            int result;
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, name);
                resultSet = preparedStatement.executeQuery();
                result = resultSet.getInt(1);
            }
            this.closeConnection();
            return result;
        } catch (SQLException ex) {
            Logger.getLogger(StudentDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public String getNameByStudentId(String studentId) {
        try {
            this.openConnection();
            String sql = "SELECT name FROM student where studentId = ?";

            ResultSet resultSet;
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, studentId);
                resultSet = preparedStatement.executeQuery();
            }

            String name = "";
            if (resultSet.next()) {
                name = resultSet.getString(1);
            }

            this.closeConnection();
            return name;
        } catch (SQLException ex) {
            Logger.getLogger(StudentDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void setMoneyByStudentId(String studentId, Double amount) {
        try {
            this.openConnection();
            String sql = "UPDATE student SET money = ? where studentId = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setDouble(1, amount);
                preparedStatement.setString(2, studentId);
                preparedStatement.executeUpdate();
            }

            this.closeConnection();
        } catch (SQLException ex) {
            Logger.getLogger(StudentDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public LoginStatus studentLogin(String name, String studentId) {
        try {
            if (checkLoginAttempts(name)) {
                if (checkCredentials(name, studentId)) {
                    setAttemptsZero(studentId);
                    return LoginStatus.OK;
                } else {
                    incLoginAttempts(name);
                    return LoginStatus.BAD;
                }
            }
            return LoginStatus.BLOCKED;
        } catch (SQLException ex) {
            Logger.getLogger(StudentDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private boolean checkLoginAttempts(String name) {
        try {
            this.openConnection();
            String sql = "SELECT * FROM STUDENT WHERE name = ? AND loginAttempts < 3";
            ResultSet resultSet;
            boolean result;
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, name);
                resultSet = preparedStatement.executeQuery();
                result = resultSet.next();
            }
            this.closeConnection();
            return result;
        } catch (SQLException ex) {
            Logger.getLogger(StudentDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    private boolean checkCredentials(String name, String studentId) {
        try {
            this.openConnection();
            String sql = "SELECT * FROM STUDENT WHERE name = ? AND studentId = ?";
            boolean result;
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, studentId);
                ResultSet resultSet = preparedStatement.executeQuery();
                result = resultSet.next();
            }
            this.closeConnection();
            return result;
        } catch (SQLException ex) {
            Logger.getLogger(StudentDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
                
        return false;
    }

    private void incLoginAttempts(String name) throws SQLException {
        this.openConnection();
        String sql = "UPDATE STUDENT SET loginAttempts = loginAttempts + 1 WHERE name = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, name);
        preparedStatement.executeUpdate();
        this.closeConnection();
    }

    public void setAttemptsZero(String studentId) throws SQLException {
        String sql = "UPDATE student SET loginAttempts = 0 where studentId = '" + studentId + "'";
        this.openConnection();
        this.executeUpdate(sql);
        this.closeConnection();
    }

    public boolean queryBorrowed(Student student) throws SQLException {
        this.openConnection();
        String sql = "SELECT * FROM STUDENT WHERE name = ? AND booksBorrowed < 3";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, student.getName());
        ResultSet resultSet = preparedStatement.executeQuery();
        boolean r = resultSet.next();
        this.closeConnection();
        this.closeStatement();
        return r;
    }

    public int getId(Student student) throws SQLException {
        this.openConnection();
        String sql = "SELECT * FROM STUDENT WHERE name= ? and studentId = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, student.getName());
        preparedStatement.setString(2, student.getStudentId());
        ResultSet resultSet = preparedStatement.executeQuery();
        int id = resultSet.getInt("Id");
        this.closeConnection();
        this.closeStatement();
        return id;
    }

    @Override
    public List<Student> read(Class<Student> classe) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
=======
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.hanze.cgd;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;






import project.hanze.cdp.Student;

public class StudentDAO extends DAOAbstract implements DAO<Student> {

    public static StudentDAO getInstance() {
        return StudentDAOHolder.INSTANCE;
    }

    private static class StudentDAOHolder {

        private static final StudentDAO INSTANCE = new StudentDAO();
    }

    @Override
    public void create() throws ClassNotFoundException, SQLException {
        this.openConnection();
        String sql = "CREATE TABLE IF NOT EXISTS STUDENT ("
                + "Id INTEGER PRIMARY KEY   AUTOINCREMENT,"
                + "name VARCHAR(50) NOT NULL,"
                + "studentId VARCHAR(4) NOT NULL UNIQUE,"
                + "telephoneNumber VARCHAR(20) NOT NULL,"
                + "money DOUBLE NOT NULL,"
                + "loginAttempts int NOT NULL,"
                + "booksBorrowed int NOT NULL"
                + ")";

        this.execute(sql);
        this.closeConnection();
    }

    public void insert(Student obj) throws SQLException, ClassNotFoundException {
        this.openConnection();
        String sql = "INSERT INTO STUDENT (name,studentId,telephoneNumber,money,loginAttempts,booksBorrowed) VALUES (?,?,?,?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, obj.getName());
        preparedStatement.setString(2, obj.getStudentId());
        preparedStatement.setString(3, obj.getTelephoneNumber());
        preparedStatement.setDouble(4, obj.getMoney());
        preparedStatement.setInt(5, 0);
        preparedStatement.setInt(6, 0);
        preparedStatement.executeUpdate();
        this.closeConnection();
    }

    public void delete(Student obj) throws SQLException, ClassNotFoundException {
        this.openConnection();
        String sql = "DELETE FROM STUDENT WHERE studentId = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, obj.getStudentId());
        preparedStatement.executeUpdate();
        this.closeConnection();
    }

    public int getLoginAttemptsByName(String name) throws SQLException {
        this.openConnection();
        String sql = "SELECT loginAttempts FROM STUDENT WHERE name = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, name);
        ResultSet resultSet = preparedStatement.executeQuery();
        this.closeConnection();
        return resultSet.getInt(1);
    }

    public String getNameByStudentId(String studentId) throws SQLException {
        String name = "";
        String sql = "SELECT name FROM student where studentId = '" + studentId + "'";
        this.openConnection();

        ResultSet r = this.executeQuery(sql);
        while (r.next()) {
            name = r.getString(1);
        }
        this.closeConnection();

        return name;
    }

    public void setMoneyByStudentId(String studentId, Double amount) throws SQLException {
        String sql = "UPDATE student SET money = " + amount.toString() + " where studentId = '" + studentId + "'";
        this.openConnection();
        this.executeUpdate(sql);
        this.closeConnection();
    }

    public LoginStatus studentLogin(String name, String studentId) throws SQLException {
        if (checkLoginAttempts(name)) {
            if (checkCredentials(name, studentId)) {
                setAttemptsZero(studentId);
                return LoginStatus.OK;
            } else {
                incLoginAttempts(name);
                return LoginStatus.BAD;
            }
        }
        return LoginStatus.BLOCKED;
    }

    private boolean checkLoginAttempts(String name) throws SQLException {
        this.openConnection();
        String sql = "SELECT * FROM STUDENT WHERE name = ? AND loginAttempts < 3";
        ResultSet resultSet;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, name);
            resultSet = preparedStatement.executeQuery();
        }
        this.closeConnection();
        return resultSet.next();
    }

    private boolean checkCredentials(String name, String studentId) throws SQLException {
        this.openConnection();
        String sql = "SELECT * FROM STUDENT WHERE name = ? AND studentId = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, name);
        preparedStatement.setString(2, studentId);
        ResultSet resultSet = preparedStatement.executeQuery();
        this.closeConnection();
        return resultSet.next();
    }

    private void incLoginAttempts(String name) throws SQLException {
        this.openConnection();
        String sql = "UPDATE STUDENT SET loginAttempts = loginAttempts + 1 WHERE name = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, name);
        preparedStatement.executeUpdate();
        this.closeConnection();
    }

    public void setAttemptsZero(String studentId) throws SQLException {
        String sql = "UPDATE student SET loginAttempts = 0 where studentId = '" + studentId + "'";
        this.openConnection();
        this.executeUpdate(sql);
        this.closeConnection();
    }

    public boolean queryBorrowed(Student student) throws SQLException {
        this.openConnection();
        String sql = "SELECT * FROM STUDENT WHERE name = ? AND booksBorrowed < 3";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, student.getName());
        ResultSet resultSet = preparedStatement.executeQuery();
        boolean r = resultSet.next();
        this.closeConnection();
        return r;
    }

    public int getId(Student student) throws SQLException {
        this.openConnection();
        String sql = "SELECT * FROM STUDENT WHERE name= ? and studentId = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, student.getName());
        preparedStatement.setString(2, student.getStudentId());
        ResultSet resultSet = preparedStatement.executeQuery();
        int id = resultSet.getInt("Id");
        this.closeConnection();
        return id;
    }

    @Override
    public List<Student> read(Class<Student> classe) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
>>>>>>> Stashed changes
