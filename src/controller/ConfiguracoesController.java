package controller;

import app.Config;
import db.Database;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import util.Directory;
import util.Imagem;
import util.KeyValuePair;

/**
 * @author Yonatha Almeida <yonathalmeida@gmail.com>
 */
public class ConfiguracoesController implements Initializable {

    @FXML
    private Config config;

    @FXML
    private ComboBox field_origin_db_driver;

    @FXML
    private TextField field_origin_db_host;

    @FXML
    private TextField field_origin_host_port;

    @FXML
    private TextField field_origin_db_name;

    @FXML
    private TextField field_origin_db_table;

    @FXML
    private TextField field_origin_db_user;

    @FXML
    private TextField field_origin_db_table_blob_field;

    @FXML
    private TextField field_origin_db_password;

    // Target
    @FXML
    private ComboBox field_target_db_driver;

    @FXML
    private TextField field_target_db_host;

    @FXML
    private TextField field_target_host_port;

    @FXML
    private TextField field_target_db_name;

    @FXML
    private TextField field_target_db_table;

    @FXML
    private TextField field_target_db_user;

    @FXML
    private TextField field_target_db_table_blob_fild;

    @FXML
    private TextField field_target_db_password;

    @FXML
    private TextField field_save_disk_patch;

    @FXML
    private TextField field_image_height;

    @FXML
    private TextField field_image_quality;

    @FXML
    private TextField field_image_width;

    @FXML
    private TextField field_image_extension;

    @FXML
    private CheckBox check_box_use_origin;

    @FXML
    private CheckBox check_box_save_disk;

    @FXML
    private Database dbOrigin;

    @FXML
    private Database dbTarget;

    public Database getDbOrigin() {
        return dbOrigin;
    }

    public void setDbOrigin(Database dbOrigin) {
        this.dbOrigin = dbOrigin;
    }

    public Database getDbTarget() {
        return dbTarget;
    }

    public void setDbTarget(Database dbTarget) {
        this.dbTarget = dbTarget;
    }

    KeyValuePair sqlServerDriver = new KeyValuePair("com.microsoft.sqlserver.jdbc.SQLServerDriver", "SQL Server 2008");
    KeyValuePair mySqlDriver = new KeyValuePair("com.mysql.jdbc.Driver", "MySQL 5.5");
    ObservableList<KeyValuePair> drivers = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        drivers.add(sqlServerDriver);
        drivers.add(mySqlDriver);

        this.populateOriginDriverCombo();;
        this.populateTargetDriverCombo();
    }

    @FXML
    public Config loadConfig() {
        Config config = new Config();
        config.setDbOrigin(getOriginConfigValues());
        config.setDbTarget(getTargetConfigValues());
        return config;
    }

    /**
     * Inicia o processo de extração de leitura das tabelas e extração e
     * otimização dos binários das imagens
     *
     * @return void
     */
    @FXML
    public void actionStart(ActionEvent event) throws SQLException, ClassNotFoundException, IOException {

        Config config = new Config();
        config.setDbOrigin(getOriginConfigValues());
        config.setDbTarget(getTargetConfigValues());
        config.setCheck_box_save_disk(this.check_box_use_origin.isSelected());
        config.setCheck_box_use_origin(this.check_box_save_disk.isSelected());

        config.setField_image_extension(this.field_image_extension.getText());
        config.setField_image_width(Integer.parseInt(this.field_image_width.getText()));
        config.setField_image_height(Integer.parseInt(this.field_image_height.getText()));
        config.setField_save_disk_patch(field_save_disk_patch.getText());
        config.setField_image_quality(Double.parseDouble(this.field_image_quality.getText()));

        config.setDbOrigin(this.getOriginConfigValues());
        config.setDbTarget(this.getTargetConfigValues());

        System.out.println("Extraindo imagens");
        Imagem i = new Imagem(config);
        i.start();
        synchronized (i) {
            try {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("");
                alert.setHeaderText("Atenção");
                alert.setContentText("O processo de otimização foi iniciado. Aguarde um instante...");
                alert.showAndWait();

                i.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Processo");
            alert.setHeaderText("Sucesso");
            alert.setContentText("Processo concluído com sucesso.");
            alert.showAndWait();
        }

        // Abre o diretório onde foram extraidas as imagens
        Directory save_disk_patch = new Directory();
        save_disk_patch.open(config.getField_save_disk_patch());
    }

    /**
     * Realiza um teste na conexão do banco de dados de origem
     *
     * @return boolean
     */
    @FXML
    public boolean actionConnectionOriginTest() throws ClassNotFoundException, SQLException {

        try {
            this.getOriginConfigValues();
            if (this.dbOrigin.connect()) {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Teste de conexão");
                alert.setHeaderText("Sucesso");
                alert.setContentText("Conexão estabelecida com sucesso.");
                alert.showAndWait();
                return true;
            } else {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Teste de conexão");
                alert.setHeaderText("Erro");
                alert.setContentText("Não foi possível estabelecer a conexão com a base de dados de origem.");
                alert.showAndWait();
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Realiza um teste na conexão do banco de dados de origem
     *
     * @return boolean
     */
    @FXML
    public boolean actionConnectionTargetTest() throws ClassNotFoundException, SQLException {
        try {
            this.getTargetConfigValues();
            if (this.dbTarget.connect()) {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Teste de conexão");
                alert.setHeaderText("Sucesso");
                alert.setContentText("Conexão estabelecida com sucesso.");
                alert.showAndWait();
                return true;
            } else {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Teste de conexão");
                alert.setHeaderText("Erro");
                alert.setContentText("Não foi possível estabelecer a conexão com a base de dados de origem.");
                alert.showAndWait();
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Obtem os valores da base de conexão de origem
     *
     * @return Config
     */
    public Database getOriginConfigValues() {
        Database db = new Database();
        KeyValuePair originDriver = (KeyValuePair) field_origin_db_driver.getValue();
        db.setDRIVER(originDriver.getKey());
        db.setHOST(this.field_origin_db_host.getText());
        db.setNAME(this.field_origin_db_name.getText());
        db.setUSER(this.field_origin_db_user.getText());
        db.setPORT(this.field_origin_host_port.getText());
        db.setPASS(this.field_origin_db_password.getText());
        db.setTABLE_NAME(this.field_origin_db_table.getText());
        db.setTABLE_BLOB_COLUM(this.field_origin_db_table_blob_field.getText());
        this.setDbOrigin(db);
        return db;
    }

    /**
     * Obtem os valores da base de conexão de destino
     *
     * @return Config
     */
    public Database getTargetConfigValues() {
        Database db = new Database();
        KeyValuePair targetDriver = (KeyValuePair) field_target_db_driver.getValue();
        db.setDRIVER(targetDriver.getKey());
        db.setHOST(this.field_target_db_host.getText());
        db.setNAME(this.field_target_db_name.getText());
        db.setUSER(this.field_target_db_user.getText());
        db.setPORT(this.field_target_host_port.getText());
        db.setPASS(this.field_target_db_password.getText());
        db.setTABLE_NAME(this.field_target_db_table.getText());
        db.setTABLE_BLOB_COLUM(this.field_target_db_table_blob_fild.getText());
        this.setDbTarget(db);
        return db;
    }

    /**
     * Exibe a janela de informações sobre o software
     *
     * @return void
     */
    @FXML
    public void actionSobre() throws ClassNotFoundException, SQLException {

        try {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Sobre");
            alert.setHeaderText("DBlobImage Tool");
            alert.setContentText("Desenvolvido por Yonatha Almeida - yonathalmeida@gmail.com");
            alert.showAndWait();
        } catch (Exception e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    /**
     * Fecha a aplicação
     *
     * @return void
     */
    @FXML
    public void actionSair() throws ClassNotFoundException, SQLException {
        Platform.exit();
    }

    /**
     * Fecha a aplicação
     *
     * @return void
     */
    @FXML
    public void actionInserir() throws ClassNotFoundException, SQLException, FileNotFoundException, IOException {

        Imagem i = new Imagem(loadConfig());
        i.insert();

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Insert");
        alert.setHeaderText("Sucesso");
        alert.setContentText("Os dados foram inseridos com sucesso");
        alert.showAndWait();

    }

    /**
     * Popula o combobox contido no FXML com as opções de dirver disponíveis
     * para a conexão de origem
     *
     * @return void
     */
    void populateOriginDriverCombo() {
        try {
            field_origin_db_driver.getItems().add(sqlServerDriver); // adiciona o driver SQL Server
            field_origin_db_driver.getItems().add(mySqlDriver); // Adiciona o driver MySQL
            field_origin_db_driver.getSelectionModel().selectLast();// descomentar para exibir Selecione
            field_origin_db_driver.setOnAction((event) -> { // Ação ao selecione

                KeyValuePair originDriver = (KeyValuePair) field_origin_db_driver.getValue();
                getOriginConfigValues();
                if (config == null) {
                    loadConfig();
                } else {
                    config.getDbOrigin().setDRIVER(originDriver.getKey());
                }
                System.out.println("O driver de origem foi configurado para: " + originDriver.getKey());

            });
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Popula o combobox contido no FXML com as opções de dirver disponíveis
     * para a conexão destinatária
     *
     * @return void
     */
    void populateTargetDriverCombo() {
        try {
            field_target_db_driver.getItems().add(sqlServerDriver);
            field_target_db_driver.getItems().add(mySqlDriver);
            field_target_db_driver.getSelectionModel().selectLast();// descomentar para exibir Selecione
            field_target_db_driver.setOnAction((event) -> {

                KeyValuePair targetDriver = (KeyValuePair) field_target_db_driver.getValue();
                getTargetConfigValues();

                if (config == null) {
                    loadConfig();
                    config.getDbTarget().setDRIVER(targetDriver.getKey());
                } else {
                    config.getDbTarget().setDRIVER(targetDriver.getKey());
                }
                System.out.println("O driver destinatário foi configurado para: " + targetDriver.getKey());
            });
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
