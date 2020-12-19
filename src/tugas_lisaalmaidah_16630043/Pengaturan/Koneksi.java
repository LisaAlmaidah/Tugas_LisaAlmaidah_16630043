/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tugas_lisaalmaidah_16630043.Pengaturan;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Administrator
 */
public class Koneksi {
    private final String url="jdbc:mysql://localhost/tugas_lisaalmaidah_16630043";
    private final String user="root";
    private final String pass="";
    
    Connection conn;
    
    public Connection getConnection(){
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url,user,pass);
        } catch (SQLException ex) {
            Logger.getLogger(Koneksi.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Terjadi Kesalahan Pada Koneksi. Details: \n" +ex.toString());
        }
        return conn;
    }
}
