package paxi.maokitty.verify.singleton.service.tryfix;

import java.io.Serializable;

/**
 * Created by maokitty on 19/5/2.
 */
public  class DoubleCheckFix implements Cloneable,Serializable{

    private static final long serialVersionUID = 2376698019364690740L;
    private static volatile DoubleCheckFix INSTANCE=null;
    private static boolean create=false;
    private DoubleCheckFix(){
        //here will be fail
        synchronized (DoubleCheckFix.class){
            if (!create){
                create=!create;
            }else{
                throw new IllegalStateException("不可创建");
            }
        }
    }

    public static DoubleCheckFix getInstance(){
        if (INSTANCE == null){
            synchronized (DoubleCheckFix.class){
                if (INSTANCE == null){
                    INSTANCE = new DoubleCheckFix();
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        //success
        return getInstance();
    }

    private Object readResolve()  {
        //success
        return getInstance();
    }
}
