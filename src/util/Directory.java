/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author Yonatha
 */
public class Directory {

    private String dirToOpen = null;

    public void open(String dir) throws IOException {
        this.setDirToOpen(dir);
        Desktop desktop = Desktop.getDesktop();
        try {
            desktop.open(new File(this.getDirToOpen()));
        } catch (IllegalArgumentException iae) {
            System.out.println("File Not Found");
        }
    }

    public String getDirToOpen() {
        return dirToOpen;
    }

    public void setDirToOpen(String dirToOpen) {
        this.dirToOpen = dirToOpen;
    }
}
