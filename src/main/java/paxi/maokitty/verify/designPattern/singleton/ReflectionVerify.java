package paxi.maokitty.verify.designPattern.singleton;


import com.sun.tools.javac.util.Assert;
import paxi.maokitty.verify.service.singleton.SingleTonEnum;
import paxi.maokitty.verify.service.singleton.StaticInnerSingleTonService;
import paxi.maokitty.verify.util.PrintUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by maokitty on 19/5/2.
 */
public class ReflectionVerify {
    public static void main(String[] args) {
        ReflectionVerify reflect=new ReflectionVerify();
        reflect.staticInnerClassVerify();
        reflect.enumClassVerify();
    }

    public void staticInnerClassVerify(){
        Class<StaticInnerSingleTonService> klazz = StaticInnerSingleTonService.class;
        StaticInnerSingleTonService first = (StaticInnerSingleTonService) getObj(klazz);
        StaticInnerSingleTonService second = (StaticInnerSingleTonService) getObj(klazz);
        PrintUtil.out("staticInnerClassVerify first obj is  sencond obj ? %b",(first.getInstance().hashCode()==second.getInstance().hashCode()));
    }

    public void enumClassVerify(){
        Class<SingleTonEnum> klazz = SingleTonEnum.class;
        SingleTonEnum first = (SingleTonEnum) getObj(klazz);
        SingleTonEnum second = (SingleTonEnum) getObj(klazz);
        PrintUtil.out("enumClassVerify first obj is  sencond obj ? %b",(first.getInstance().hashCode()==second.getInstance().hashCode()));
    }

    private Object getObj(Class klazz){
        try {
            Object obj=null;
            if (klazz.isEnum()){
                obj = klazz.getEnumConstants()[0];
            }else{
                Constructor dc = klazz.getDeclaredConstructor(new Class[0]);
                dc.setAccessible(true);
                obj = dc.newInstance();
            }
            return obj;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
