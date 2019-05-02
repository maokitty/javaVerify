package paxi.maokitty.verify.designPattern.singleton;


import paxi.maokitty.verify.service.singleton.DoubleCheckSingleTonService;
import paxi.maokitty.verify.service.singleton.SingleTonEnum;
import paxi.maokitty.verify.service.singleton.StaticInnerSingleTonService;
import paxi.maokitty.verify.service.singleton.tryfix.DoubleCheckFix;
import paxi.maokitty.verify.util.PrintUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by maokitty on 19/5/2.
 */
public class ReflectionVerify {
    public static void main(String[] args) {
        ReflectionVerify reflect=new ReflectionVerify();
        reflect.staticInnerClassVerify();
        reflect.enumClassVerify();
        reflect.dcClassVerify();
        reflect.doubleCheckReflectFailFixVerify();
    }

    /**
     * 无法阻止反射创建不同的实例
     */
    public void staticInnerClassVerify(){
        Class<StaticInnerSingleTonService> klazz = StaticInnerSingleTonService.class;
        StaticInnerSingleTonService first = (StaticInnerSingleTonService) executeGetInstance(klazz);
        PrintUtil.out("staticInnerClassVerify first obj is  StaticInnerSingleTonService.getInstance() obj ? %b", (first.hashCode() == StaticInnerSingleTonService.getInstance().hashCode()));
    }

    /**
     * 反射创建一样
     */
    public void enumClassVerify(){
        Class<SingleTonEnum> klazz = SingleTonEnum.class;
        SingleTonEnum first = (SingleTonEnum) executeGetInstance(klazz);
        PrintUtil.out("enumClassVerify first obj is  SingleTonEnum.getInstance() obj ? %b",(first.hashCode()==SingleTonEnum.getInstance().hashCode()));
    }

    /**
     * 无法阻止反射创建不同的实例
     */
     public void dcClassVerify(){
        Class<DoubleCheckSingleTonService> klazz = DoubleCheckSingleTonService.class;
         DoubleCheckSingleTonService first = (DoubleCheckSingleTonService) executeGetInstance(klazz);
         PrintUtil.out("dcClassVerify first obj is  DoubleCheckSingleTonService.getInstance() obj ? %b",(first.hashCode()==DoubleCheckSingleTonService.getInstance().hashCode()));
     }

    public void doubleCheckReflectFailFixVerify(){
        Class<DoubleCheckFix> klazz = DoubleCheckFix.class;
        Constructor dc = null;
        try {
            dc = klazz.getDeclaredConstructor(new Class[0]);
            dc.setAccessible(true);
            DoubleCheckFix first = (DoubleCheckFix) dc.newInstance();
            Field create = klazz.getDeclaredField("create");
            create.setAccessible(true);
            create.setBoolean("create", false);
            PrintUtil.out("doubleCheckReflectFailFixVerify first obj is  DoubleCheckFix.getInstance() obj ? %b",(first.hashCode()==DoubleCheckFix.getInstance().hashCode()));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

    }

    private Object executeGetInstance(Class klazz){
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
