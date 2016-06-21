package util;

import app.Config;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import net.coobird.thumbnailator.Thumbnails;
import org.primefaces.model.DefaultStreamedContent;

/**
 * @author Yonatha Almeida <yonathalmeida@gmail.com>
 */
public class Imagem extends Thread {

    private Config config;
    private static final int BUFFER_SIZE = 4096;
    Connection conn = null;
    Statement stmt = null;

    @Override
    public void run() {
        synchronized (this) {
            this.extractAndResize();
        }
    }

    public Imagem(Config config) throws ClassNotFoundException, SQLException {
        this.setAppConfig(config);
    }

    public Config getAppConfig() {
        return config;
    }

    public void setAppConfig(Config config) {
        this.config = config;
    }

    /**
     * Extrai as imagens que estão armazenadas em uma coluna do tipo blob
     */
    public void extractAndResize() {
        try {

            if (config.getDbOrigin().connect()) {

                ResultSet rs = config.getDbOrigin().execute();
                while (rs.next()) {
                    String id = rs.getString("ID");
                    DefaultStreamedContent blob = new DefaultStreamedContent(new ByteArrayInputStream(rs.getBytes(config.getDbOrigin().getTABLE_BLOB_COLUM())));

                    InputStream inputStream = blob.getStream();
                    OutputStream outputStream = new FileOutputStream(config.getField_save_disk_patch() + id + "." + config.getField_image_extension());

                    int bytesRead = -1;
                    byte[] buffer = new byte[BUFFER_SIZE];
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }

                    Imagem i = new Imagem(this.getAppConfig());
                    i.redimencionar(id);

                    Thumbnails.of(config.getField_save_disk_patch() + id + "." + config.getField_image_extension())
                            .size(config.getField_image_width(), config.getField_image_height())
                            .outputQuality(config.getField_image_quality())
                            .toOutputStream(outputStream);

                    InputStream x = new FileInputStream(new File(config.getField_save_disk_patch() + id + "_otimizado." + config.getField_image_extension()));
                    this.saveInTarget(x);

                    inputStream.close();
                    outputStream.close();
                }
                config.getDbOrigin().disconnect();
            }

        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se2) {
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    /**
     * Redimenciona imagens
     */
    public void redimencionar(String id) throws IOException {

        Thumbnails.of(new File(config.getField_save_disk_patch() + id + "." + config.getField_image_extension()))
                .size(config.getField_image_width(), config.getField_image_height())
                .outputQuality(config.getField_image_quality())
                .toFile(new File(config.getField_save_disk_patch() + id + "_otimizado." + config.getField_image_extension()));
    }

    public void salvarEmDisco(String id) throws IOException {

        Thumbnails.of(new File(config.getField_save_disk_patch() + id + "." + config.getField_image_extension()))
                .size(config.getField_image_width(), config.getField_image_height())
                .outputQuality(config.getField_image_quality())
                .toFile(new File(config.getField_save_disk_patch() + id + "_otimizado." + config.getField_image_extension()));

    }

    /**
     * Insere imagens no banco
     */
    public void insert() throws FileNotFoundException, SQLException, ClassNotFoundException, IOException {

        config.getDbOrigin().setConn(DriverManager.getConnection("jdbc:mysql://" + config.getDbOrigin().getHOST() + "/" + config.getDbOrigin().getNAME() + "?user=" + config.getDbOrigin().getUSER() + "&password=" + config.getDbOrigin().getPASS()));

        Class.forName(config.getDbTarget().getDRIVER());
        try {
            for (int i = 1; i < 29; i++) {
                PreparedStatement ps;
                ps = config.getDbOrigin().getConn().prepareStatement("INSERT INTO fotos (imagem) " + "values(?)");
                InputStream inputStream = new FileInputStream(new File("C:\\imagens\\" + i + ".jpg"));
                ps.setBlob(1, inputStream);
                ps.executeUpdate();
            }
            this.config.getDbOrigin().getConn().close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Salva o binário otimizado na fonte de dados destinarária
     */
    public void saveInTarget(InputStream inputStream) throws SQLException, ClassNotFoundException, IOException {

        config.getDbTarget().setConn(DriverManager.getConnection("jdbc:mysql://" + config.getDbTarget().getHOST() + "/" + config.getDbTarget().getNAME() + "?user=" + config.getDbTarget().getUSER() + "&password=" + config.getDbTarget().getPASS()));

        Class.forName(config.getDbTarget().getDRIVER());
        PreparedStatement ps;
        ps = config.getDbTarget().getConn().prepareStatement("INSERT INTO " + config.getDbTarget().getTABLE_NAME() + " (" + config.getDbTarget().getTABLE_BLOB_COLUM() + ") " + "values(?)");
        ps.setBlob(1, inputStream);
        ps.executeUpdate();
        this.config.getDbTarget().getConn().close();
    }

}
