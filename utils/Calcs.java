package utils;

import graphics.HandlePoint;
import graphics.primitives.impl.Line;
import java.util.ArrayList;
import java.util.List;

public final class Calcs {
  
  public Calcs(){
    super();
  }

  public static double scalarMult(HandlePoint hpA, HandlePoint hpB) {
      return hpA.getX() * hpB.getX() + hpA.getY() * hpB.getY();
  }

  public static double module(HandlePoint hp) {
      return Math.sqrt( hp.getX() * hp.getX() + hp.getY() * hp.getY() );
  }

  public static double getAngle(HandlePoint hpA, HandlePoint hpB) {
      double scalarMult = scalarMult(hpA, hpB);
      double modA = module(hpA);
      double modB = module(hpB);

      return Math.acos( scalarMult / (modA * modB) );
      
  }

   public static double getDistance(HandlePoint hp1, HandlePoint hp2) {
        return Math.sqrt(
                Math.pow(hp1.getX() - hp2.getX(),2) +
                Math.pow(hp1.getY() - hp2.getY(),2));
    }

   public static List<HandlePoint> getFilletPoints(Line lt, Line ls, double r) {

       List<HandlePoint> points = new ArrayList<HandlePoint>();

       double mt = lt.getSlope();
       double ms = ls.getSlope();
       double bt = lt.getOffset();
       double bs = ls.getOffset();

       double x;
       double y;

       x = (r * Math.sqrt(1 + ms * ms) - r * Math.sqrt(1 + mt * mt) - bt + bs) / (mt - ms);
       y = ((r * Math.sqrt(1 + mt * mt) + mt * x + bt) - ms * x - bs) / Math.sqrt(1 + ms * ms);

       points.add(new HandlePoint(null, x, y));

       return points;
   }
}
