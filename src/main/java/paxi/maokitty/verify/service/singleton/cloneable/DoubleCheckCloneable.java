package paxi.maokitty.verify.service.singleton.cloneable;

/**
 * Created by maokitty on 19/5/2.
 */
public class DoubleCheckCloneable implements Cloneable {
   private static volatile  DoubleCheckCloneable INSTANCE= null;
   private DoubleCheckCloneable(){}
   public static  DoubleCheckCloneable getInstance(){
       if (INSTANCE == null){
           synchronized (DoubleCheckCloneable.class){
               if (INSTANCE == null)
               {
                   INSTANCE = new DoubleCheckCloneable();
               }
           }
       }
       return INSTANCE;
   }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
