package controllers;

import java.util.logging.Level;

public class LogLevelController {

    private static LogLevelController logLevelManager;
    private Level level = Level.OFF;

    public static LogLevelController getLogLevelManager() {
        if (logLevelManager == null) {
            logLevelManager = new LogLevelController();
        }
        return logLevelManager;
    }

  private LogLevelController(){
    super();
  }

    /**
     * @return the level
     */
    public Level getLevel() {
        return level;
    }

    /**
     * @param level the level to set
     */
    public void setLevel(Level level) {
        this.level = level;
    }

}
