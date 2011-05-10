package graphics.strokes.impl;

import graphics.strokes.*;

public class IntermitentStroke implements Stroke {
    private int drawedPoints = 0;

  public IntermitentStroke(){
    super();
  }

    public boolean drawNextPoint() {
        boolean res = true;

        if (drawedPoints % 2 != 0) {
            res = false;
        }

        drawedPoints++;

        return res;
    }

}
