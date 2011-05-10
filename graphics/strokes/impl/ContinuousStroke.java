package graphics.strokes.impl;

import graphics.strokes.*;

public class ContinuousStroke implements Stroke {

  public ContinuousStroke(){
    super();
  }

    public boolean drawNextPoint() {
        return true;
    }

}
