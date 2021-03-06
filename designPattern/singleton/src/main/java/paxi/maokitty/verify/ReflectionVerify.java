package paxi.maokitty.verify;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import paxi.maokitty.verify.service.DoubleCheckSingleTonService;
import paxi.maokitty.verify.service.SingleTonEnum;
import paxi.maokitty.verify.service.StaticInnerSingleTonService;
import paxi.maokitty.verify.service.tryfix.DoubleCheckFix;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by maokitty on 19/5/2.
 */
public class ReflectionVerify {
    private static final Logger LOG = LoggerFactory.getLogger(ReflectionVerify.class);
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
        LOG.info("staticInnerClassVerify first obj is  StaticInnerSingleTonService.getInstance() obj ? {}", (first.hashCode() == StaticInnerSingleTonService.getInstance().hashCode()));
    }

    /**
     * 反射创建一样
     */
    public void enumClassVerify(){
        Class<SingleTonEnum> klazz = SingleTonEnum.class;
        SingleTonEnum first = (SingleTonEnum) executeGetInstance(klazz);
        LOG.info("enumClassVerify first obj is  SingleTonEnum.getInstance() obj ? {}", (first.hashCode() == SingleTonEnum.getInstance().hashCode()));
    }

    /**
     * 无法阻止反射创建不同的实例
     */
     public void dcClassVerify(){
        Class<DoubleCheckSingleTonService> klazz = DoubleCheckSingleTonService.class;
         DoubleCheckSingleTonService first = (DoubleCheckSingleTonService) executeGetInstance(klazz);
         LOG.info("dcClassVerify first obj is  DoubleCheckSingleTonService.getInstance() obj ? {}", (first.hashCode() == DoubleCheckSingleTonService.getInstance().hashCode()));
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
            LOG.info("doubleCheckReflectFailFixVerify first obj is  DoubleCheckFix.getInstance() obj ? {}",(first.hashCode()== DoubleCheckFix.getInstance().hashCode()));
        } catch (NoSuchMethodException e) {
            LOG.error("doubleCheckReflectFailFixVerify",e);
        } catch (InvocationTargetException e) {
            LOG.error("doubleCheckReflectFailFixVerify",e);
        } catch (InstantiationException e) {
            LOG.error("doubleCheckReflectFailFixVerify",e);
        } catch (IllegalAccessException e) {
            LOG.error("doubleCheckReflectFailFixVerify",e);
        } catch (NoSuchFieldException e) {
            LOG.error("doubleCheckReflectFailFixVerify",e);
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
            LOG.error("executeGetInstance",e);
        } catch (IllegalAccessException e) {
            LOG.error("executeGetInstance",e);
        } catch (NoSuchMethodException e) {
            LOG.error("executeGetInstance",e);
        } catch (InvocationTargetException e) {
            LOG.error("executeGetInstance",e);
        }
        return null;
    }
}
