package paxi.maokitty.verify.designPattern.singleton;

import paxi.maokitty.verify.service.singleton.StaticInnerSingleTonService;
import paxi.maokitty.verify.service.singleton.SingleTonEnum;
import paxi.maokitty.verify.util.DirectorUtil;
import paxi.maokitty.verify.util.PrintUtil;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by maokitty on 19/4/28.
 * 使用双亲加载模型的都正常实现了单例，但是破坏了都无法实现类型转换
 */
public class ClassLoaderVerify {
    public static void main(String[] args) {
        ClassLoaderVerify verify = new ClassLoaderVerify();
        verify.loadClassOverrideTest();
        verify.findClassOverrideTest();
        verify.enumFindClassOverrideTest();
        verify.enumLoadClassOverrideTest();
    }

    /**
     * 破坏了双亲加载模型,使用了不同的类加载器
     */
    public void enumLoadClassOverrideTest(){
        ClassLoader firstLoader = getNewLoadClassPaxiClassLoader();
        try {
            Class<?> firstLC = firstLoader.loadClass(SingleTonEnum.class.getName());
            Object first = executeGetInstance(firstLC);
            PrintUtil.out("enumLoadClassOverrideTest first class loader is SingleTonEnum.INSTANCE class loader ? %b ", (first.getClass().getClassLoader() == SingleTonEnum.INSTANCE.getClass().getClassLoader()));
            PrintUtil.out("enumLoadClassOverrideTest first obj is  SingleTonEnum.INSTANCE obj ? %b " , (first.hashCode() == SingleTonEnum.INSTANCE.hashCode()));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 使用双亲加载模型
     */

    public void enumFindClassOverrideTest(){
        ClassLoader firstLoader = getNewPaxiFindClassClassLoader();
        try {
            Class<?> firstLC = firstLoader.loadClass(SingleTonEnum.class.getName());
            SingleTonEnum first = (SingleTonEnum) executeGetInstance(firstLC);
            PrintUtil.out("enumFindClassOverrideTest first class loader is SingleTonEnum.INSTANCE class loader ? %b " ,(first.getClass().getClassLoader() == SingleTonEnum.INSTANCE.getClass().getClassLoader()));
            PrintUtil.out("enumFindClassOverrideTest first obj is  SingleTonEnum.INSTANCE obj ? %b " ,(first.getInstance().hashCode() == SingleTonEnum.INSTANCE.getInstance().hashCode()));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }



    /**
     * 直接覆盖loadClass,破坏了双亲加载模型,无法实现类型转换
     */
    public void loadClassOverrideTest() {
        ClassLoader firstLoader = getNewLoadClassPaxiClassLoader();
        try {
            Class<?> firstLC = firstLoader.loadClass(StaticInnerSingleTonService.class.getName());
            Object first = executeGetInstance(firstLC);
            PrintUtil.out("loadClassOverrideTest first class loader is StaticInnerSingleTonService.getInstance() class loader ? %b " , (first.getClass().getClassLoader() == StaticInnerSingleTonService.getInstance().getClass().getClassLoader()));
            PrintUtil.out("loadClassOverrideTest first obj is  StaticInnerSingleTonService.getInstance() obj ? %b " , (first.hashCode() == StaticInnerSingleTonService.getInstance().hashCode()));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    /**
     * 使用双亲加载模型
     */
    public void findClassOverrideTest(){
        ClassLoader firstLoader = getNewPaxiFindClassClassLoader();
        try {
            Class<?> firstLC = firstLoader.loadClass(StaticInnerSingleTonService.class.getName());
            StaticInnerSingleTonService first = (StaticInnerSingleTonService)executeGetInstance(firstLC);
            System.out.println("findClassOverrideTest first class loader is StaticInnerSingleTonService.getInstance() class loader ? " + (first.getClass().getClassLoader() == StaticInnerSingleTonService.getInstance().getClass().getClassLoader()));
            System.out.println("findClassOverrideTest first obj is  StaticInnerSingleTonService.getInstance() obj ? " + (first.getInstance().hashCode() == StaticInnerSingleTonService.getInstance().hashCode()));
        } catch (ClassNotFoundException e) {
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

    private ClassLoader getNewLoadClassPaxiClassLoader(){
        return new ClassLoader() {
            @Override
            public Class<?> loadClass(String name) throws ClassNotFoundException {
                if (name.startsWith("paxi")){
                    //只加在自己的类
                    try {
                        String subPath = name.replaceAll("\\.", File.separator)+".class";
                        String fName= DirectorUtil.getProjectDir()+File.separator+"target"+File.separator+"classes"+File.separator+subPath;
                        FileInputStream is=new FileInputStream(new File(fName));
                        if (is==null){
                            return super.findClass(name);
                        }
                        byte[] b=new byte[is.available()];
                        is.read(b);
                        return defineClass(name,b,0,b.length);
                    }catch (Exception e){
                        throw new ClassNotFoundException();
                    }
                }else{
                    return super.loadClass(name);
                }
            }
        };
    }

    /**
     * 双亲加载模型都不会执行这个方法
     * @return
     */
    private ClassLoader getNewPaxiFindClassClassLoader(){
        return new ClassLoader() {
            @Override
            protected Class<?> findClass(String name) throws ClassNotFoundException {
                if (name.startsWith("paxi")){
                    //只加在自己的类
                    try {
                        String subPath = name.replaceAll("\\.", File.separator)+".class";
                        String fName=DirectorUtil.getProjectDir()+File.separator+"target"+File.separator+"classes"+File.separator+subPath;
                        FileInputStream is=new FileInputStream(new File(fName));
                        if (is==null){
                            return super.findClass(name);
                        }
                        byte[] b=new byte[is.available()];
                        is.read(b);
                        return defineClass(name,b,0,b.length);
                    }catch (Exception e){
                        throw new ClassNotFoundException();
                    }
                }else{
                    return super.findClass(name);
                }
            }
        };
    }
}
