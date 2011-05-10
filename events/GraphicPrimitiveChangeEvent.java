package events;

import java.beans.PropertyChangeEvent;

public class GraphicPrimitiveChangeEvent extends PropertyChangeEvent {

      public GraphicPrimitiveChangeEvent(Object source,
                                         String propertyName,
                                         Object oldValue,
                                         Object newValue){
        super(source, propertyName, oldValue, newValue);
      }

}
