/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 * Add comment
 */

package javacad;

import gui.MainWindow;
import java.util.logging.Level;
import controllers.LogLevelController;


/**
 *  @author dszama
 */
public class Main {
    public static void main (String[] args) {

        if (args.length > 0) {
            if (args[0].startsWith("--debug")) {
                LogLevelController.getLogLevelManager().setLevel(Level.ALL);
            }
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                    MainWindow mainWindow = new MainWindow();
                    mainWindow.setVisible(true);
            }
        });
    }

}
