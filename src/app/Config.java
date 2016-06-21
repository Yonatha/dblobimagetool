package app;

import db.Database;
import util.KeyValuePair;

/**
 *
 * @author Yonatha Almeida
 */
public class Config {

    private KeyValuePair field_origin_db_driver;

    // Database
    private Database dbOrigin;
    private Database dbTarget;

    // Checkbox
    private boolean check_box_use_origin;
    private boolean check_box_save_disk;

    // Image
    private String field_save_disk_patch;
    private int field_image_height;
    private double field_image_quality;
    private int field_image_width;
    private String field_image_extension;

    public KeyValuePair getField_origin_db_driver() {
        return field_origin_db_driver;
    }

    public void setField_origin_db_driver(KeyValuePair field_origin_db_driver) {
        
        System.out.println(field_origin_db_driver.getKey());
        
        this.field_origin_db_driver = field_origin_db_driver;
    }

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

    public boolean isCheck_box_use_origin() {
        return check_box_use_origin;
    }

    public void setCheck_box_use_origin(boolean check_box_use_origin) {
        this.check_box_use_origin = check_box_use_origin;
    }

    public boolean isCheck_box_save_disk() {
        return check_box_save_disk;
    }

    public void setCheck_box_save_disk(boolean check_box_save_disk) {
        this.check_box_save_disk = check_box_save_disk;
    }

    public String getField_save_disk_patch() {
        return field_save_disk_patch;
    }

    public void setField_save_disk_patch(String field_save_disk_patch) {
        this.field_save_disk_patch = field_save_disk_patch;
    }

    public int getField_image_height() {
        return field_image_height;
    }

    public void setField_image_height(int field_image_height) {
        this.field_image_height = field_image_height;
    }

    public double getField_image_quality() {
        return field_image_quality;
    }

    public void setField_image_quality(double field_image_quality) {
        this.field_image_quality = field_image_quality;
    }

    public int getField_image_width() {
        return field_image_width;
    }

    public void setField_image_width(int field_image_width) {
        this.field_image_width = field_image_width;
    }

    public String getField_image_extension() {
        return field_image_extension;
    }

    public void setField_image_extension(String field_image_extension) {
        this.field_image_extension = field_image_extension;
    }

}
