package events;

import java.util.EventListener;

public interface GraphicPrimitiveChangeListener extends EventListener {

    public void handleGraphicPrimitiveChange(GraphicPrimitiveChangeEvent evt);

}
