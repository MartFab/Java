package controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PersistenceController {

    public PersistenceController() {
    }

    public void persistAsObjects(File file, ImageController imageController) {
        try {
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(imageController);
            oos.flush();
            oos.close();
            fos.close();
        } catch (IOException ex) {
            Logger.getLogger(PersistenceController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void persistAsXML() {
    }

    public ImageController load() {
        return null;
    }

    public ImageController loadFromObjects(File file) {
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        ImageController imageController = null;

        try {
            fis = new FileInputStream(file);
            ois = new ObjectInputStream(fis);
            imageController = (ImageController) ois.readObject();
            ois.close();
            fis.close();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(PersistenceController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PersistenceController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fis.close();
            } catch (IOException ex) {
                Logger.getLogger(PersistenceController.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                ois.close();
            } catch (IOException ex) {
                Logger.getLogger(PersistenceController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return imageController;
    }

    private ImageController loadFromXML() {
        return null;
    }
}

