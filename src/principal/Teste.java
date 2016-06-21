/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package principal;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author Yonatha
 */
public class Teste {

    private String DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private String DB_HOST = "10.10.40.55";
    private String DB_USER = "relatorios";
    private String DB_PASS = "relatorios";
    private String DB_NAME = "CorporeRM_UnipeSuporte";

    Connection getConnection() throws Exception {
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://" + DB_HOST + ";user=" + DB_USER + ";password=" + DB_PASS + ";database=" + DB_NAME);
        return conn;
    }

    byte[] getBLOB() throws Exception {
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        String query = "SELECT ID, IMAGEM FROM GIMAGEM WHERE ID = 394";
        Blob blob = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(query);
            rs = pstmt.executeQuery();
            rs.next();
            blob = rs.getBlob("IMAGEM");
        } catch (Exception e) {
        } finally {
            rs.close();
            pstmt.close();
            conn.close();
        }
        if (blob != null) {
            return blob.getBytes((long) 1, (int) blob.length());
        } else {
            return null;
        }
    }
}
