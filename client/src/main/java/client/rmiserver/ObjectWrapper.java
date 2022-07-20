package client.rmiserver;

import java.io.Serializable;

public class ObjectWrapper implements Serializable {
    Object object;

    public ObjectWrapper(Object object) {
        this.object = object;
    }

    public Object getObject() {
        return object;
    }
}
