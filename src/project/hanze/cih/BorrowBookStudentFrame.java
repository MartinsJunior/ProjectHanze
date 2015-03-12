/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.hanze.cih;

import java.awt.event.ItemEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import project.hanze.cdp.Student;
import project.hanze.cgd.BookDAO;
import project.hanze.cgd.BorrowDAO;
import project.hanze.cgd.StudentDAO;

/**
 *
 * @author juniorm10
 */
public class BorrowBookStudentFrame extends javax.swing.JFrame {

    private Student student = new Student();

    public BorrowBookStudentFrame(String name, String studentId) {
        initComponents();
        jError.setText("");
        this.student.setName(name);
        this.student.setStudentId(studentId);
        ArrayList<String> readAuthor = null;
        readAuthor = BookDAO.getInstance().readAuthor();
        for (String string : readAuthor) {
            jComboBoxAuthor.addItem(string);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jComboBoxAuthor = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jComboBoxTitle = new javax.swing.JComboBox();
        jButton1 = new javax.swing.JButton();
        jError = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jComboBoxAuthor.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBoxAuthorItemStateChanged(evt);
            }
        });

        jLabel2.setText("Author:");

        jLabel1.setText("Title:");

        jComboBoxTitle.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBoxTitleItemStateChanged(evt);
            }
        });

        jButton1.setText("Borrow");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jError.setForeground(new java.awt.Color(255, 51, 51));
        jError.setText("You book borrow limit has been reached !");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 288, Short.MAX_VALUE)
                        .addComponent(jButton1))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel1))
                                .addGap(17, 17, 17)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jComboBoxAuthor, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jComboBoxTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jError, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBoxAuthor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBoxTitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jError, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBoxAuthorItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBoxAuthorItemStateChanged
        jComboBoxTitle.removeAllItems();
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            ArrayList<String> readBooksByAuthor = null;
            readBooksByAuthor = BookDAO.getInstance().readBooksByAuthor((String) jComboBoxAuthor.getSelectedItem());
            for (String string : readBooksByAuthor) {
                jComboBoxTitle.addItem(string);
            }

        }
    }//GEN-LAST:event_jComboBoxAuthorItemStateChanged

    private void jComboBoxTitleItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBoxTitleItemStateChanged

    }//GEN-LAST:event_jComboBoxTitleItemStateChanged

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            if (StudentDAO.getInstance().queryBorrowed(student)) {
                String title = jComboBoxTitle.getSelectedItem().toString();
                String author = jComboBoxAuthor.getSelectedItem().toString();
                int idBook =BookDAO.getInstance().verifyAvailable(title,author);
                int idStudent = StudentDAO.getInstance().getId(student);
                BorrowDAO.getInstance().insert(idStudent, idBook);
            } else {
                System.out.println("já tem 3 livros");
            }
        } catch (SQLException ex) {
            Logger.getLogger(BorrowBookStudentFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JComboBox jComboBoxAuthor;
    private javax.swing.JComboBox jComboBoxTitle;
    private javax.swing.JLabel jError;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    // End of variables declaration//GEN-END:variables
}
