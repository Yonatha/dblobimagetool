package db;

import app.Config;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Realiza conexão com a base de dados SQL Server
 *
 * @author Yonatha Almeida <yonathalmeida@gmail.com>
 */
public class Database {

    // Orign database params
    private String DRIVER;
    private String HOST;
    private String USER;
    private String PASS;
    private String NAME;
    private String TABLE_NAME;
    private String TABLE_BLOB_COLUM;
    private String PORT;

    private Connection conn = null;
    private Statement stmt = null;
    private Config appConfig;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    public Statement getStmt() {
        return stmt;
    }

    public void setStmt(Statement stmt) {
        this.stmt = stmt;
    }

    public Config getConfig() {
        return appConfig;
    }

    public void setConfig(Config appConfig) {
        this.appConfig = appConfig;
    }

    public String getDRIVER() {
        return DRIVER;
    }

    public void setDRIVER(String DRIVER) {
        this.DRIVER = DRIVER;
    }

    public String getHOST() {
        return HOST;
    }

    public void setHOST(String HOST) {
        this.HOST = HOST;
    }

    public String getUSER() {
        return USER;
    }

    public void setUSER(String USER) {
        this.USER = USER;
    }

    public String getPASS() {
        return PASS;
    }

    public void setPASS(String PASS) {
        this.PASS = PASS;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getTABLE_NAME() {
        return TABLE_NAME;
    }

    public void setTABLE_NAME(String TABLE_NAME) {
        this.TABLE_NAME = TABLE_NAME;
    }

    public String getTABLE_BLOB_COLUM() {
        return TABLE_BLOB_COLUM;
    }

    public void setTABLE_BLOB_COLUM(String TABLE_BLOB_COLUM) {
        this.TABLE_BLOB_COLUM = TABLE_BLOB_COLUM;
    }

    public String getPORT() {
        return PORT;
    }

    public void setPORT(String PORT) {
        this.PORT = PORT;
    }

    /**
     * Aplica as configurações e estabelece a conexão com o banco de dados de
     * origem
     *
     * @param Config appConfig
     * @return boolean
     */
    public boolean connect() throws ClassNotFoundException, SQLException {

        try {
            Class.forName(getDRIVER());
            switch (getDRIVER()) {
                case "com.microsoft.sqlserver.jdbc.SQLServerDriver":
                    this.setConn(DriverManager.getConnection("jdbc:sqlserver://" + getHOST() + ";user=" + getUSER() + ";password=" + getPASS() + ";database=" + getNAME()));
                    break;
                case "com.mysql.jdbc.Driver":
                    this.setConn(DriverManager.getConnection("jdbc:mysql://" + getHOST() + "/" + getNAME() + "?user=" + getUSER() + "&password=" + getPASS()));
                    break;
                default:
                    throw new IllegalArgumentException("Invalid day of the week: " + appConfig.getField_origin_db_driver().getValue());
            }

            this.setStmt(conn.createStatement());
            return true;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    /**
     * Executa a consulta SQL
     *
     * @return ResultSet
     */
    public ResultSet execute() throws ClassNotFoundException, SQLException {

        try {
            String sql;
            if (getDRIVER().equals("com.microsoft.sqlserver.jdbc.SQLServerDriver")) {
                sql = "SELECT id, " + this.getTABLE_BLOB_COLUM() + " FROM " + this.getTABLE_NAME();
            } else if (getDRIVER().equals("com.mysql.jdbc.Driver")) {
                sql = "SELECT id, " + this.getTABLE_BLOB_COLUM() + " FROM " + this.getTABLE_NAME();
            } else {
                sql = null;
            }
//            System.out.println("SQL: " + sql);
            ResultSet rs = getStmt().executeQuery(sql);
            return rs;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    /**
     * Desconecta do servidor de banco de dados
     */
    public void disconnect() throws SQLException {
        this.getConn().close();
        this.getStmt().close();
        System.out.println("Operação realizada com sucesso.");
        System.out.println("Conexão encerranda.");
    }
}
