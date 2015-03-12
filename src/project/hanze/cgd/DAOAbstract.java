/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.hanze.cgd;

import java.sql.*;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class DAOAbstract {

    protected Connection connection;
    protected Statement statement;

    protected void openConnection() throws SQLException {

        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DAOAbstract.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.connection = DriverManager.getConnection("jdbc:sqlite:arrombado.db");
        if (this.connection == null) {
            System.out.println("Problema ao abrir conexao");
        }
    }

    protected void execute(String query) throws SQLException {
        this.statement = connection.createStatement();
        statement.execute(query);
        statement.close();
    }

   
    protected void closeConnection() throws SQLException {
        connection.close();
    }

}