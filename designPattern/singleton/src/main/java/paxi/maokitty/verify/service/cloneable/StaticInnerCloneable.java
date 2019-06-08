package paxi.maokitty.verify.service.cloneable;

/**
 * Created by maokitty on 19/5/2.
 */
public class StaticInnerCloneable implements Cloneable {
    private StaticInnerCloneable(){};
    private static class SingletonHolder{
        public  static  final StaticInnerCloneable INSTANCE=new StaticInnerCloneable();
    }

    public static final StaticInnerCloneable getInstance(){
        return SingletonHolder.INSTANCE;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
