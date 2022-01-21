/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author omkar
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.table.DefaultTableModel;

public class DBHelper {

    public void addData(String sql) {
        try {
            Class.forName("java.sql.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/game", "root", "12345678");
            Statement st = con.createStatement();
            int j = st.executeUpdate(sql);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void showtableData() {
        try {
            String sql = "select * from highscore order by score desc;";
            DefaultTableModel tm = (DefaultTableModel) FGAME.jt1.getModel();
            Class.forName("java.sql.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/game", "root", "12345678");
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                Object ob[] = {rs.getString(1), rs.getInt(2), rs.getDate(3), rs.getTime(4)};
                tm.addRow(ob);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public String checkData(String sql) {
        String r = null;
        try {
            Class.forName("java.sql.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/game", "root", "12345678");
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            rs.next();
            String p = rs.getString(1);
            r = p;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return r;
    }

    public boolean checklog(String s) {
        boolean boo = false;
        try {
            String sql = "select loginid from userinfo;";
            Class.forName("java.sql.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/game", "root", "12345678");
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                String q = rs.getString(1);
                if (s.equals(q)) {
                    boo = true;
                    break;
                } else {
                    boo = false;
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return boo;
    }
}
