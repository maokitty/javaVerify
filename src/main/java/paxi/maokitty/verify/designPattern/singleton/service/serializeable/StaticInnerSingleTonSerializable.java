package paxi.maokitty.verify.designPattern.singleton.service.serializeable;

import java.io.Serializable;

public class StaticInnerSingleTonSerializable implements Serializable{

    private static final long serialVersionUID = -7978521466846350767L;

    private StaticInnerSingleTonSerializable(){};
    private static class SingletonHolder{
        public  static  final StaticInnerSingleTonSerializable INSTANCE=new StaticInnerSingleTonSerializable();
    }

    public static  StaticInnerSingleTonSerializable getInstance(){
        return SingletonHolder.INSTANCE;
    }

}

