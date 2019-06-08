package paxi.maokitty.verify.singleton.service.serializeable;

import java.io.Serializable;

/**
 * Created by maokitty on 19/5/2.
 */
public class DoubleCheckSerializeable implements Serializable{
    private static final long serialVersionUID = 3947641595261225368L;

    private static volatile DoubleCheckSerializeable INSTANCE;

    private DoubleCheckSerializeable(){}

    public static  DoubleCheckSerializeable getInstance(){
        if (INSTANCE==null){
            synchronized(DoubleCheckSerializeable.class){
                if (INSTANCE == null){
                    INSTANCE = new DoubleCheckSerializeable();
                }
            }
        }
        return INSTANCE;
    }
}
