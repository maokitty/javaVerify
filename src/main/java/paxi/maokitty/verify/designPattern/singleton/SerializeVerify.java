package paxi.maokitty.verify.designPattern.singleton;

import paxi.maokitty.verify.service.singleton.SingleTonEnum;
import paxi.maokitty.verify.service.singleton.serializeable.DoubleCheckSerializeable;
import paxi.maokitty.verify.service.singleton.serializeable.StaticInnerSingleTonSerializable;
import paxi.maokitty.verify.service.singleton.tryfix.DoubleCheckFix;
import paxi.maokitty.verify.util.DirectorUtil;
import paxi.maokitty.verify.util.PrintUtil;

import java.io.*;

/**
 * Created by maokitty on 19/5/2.
 */
public class SerializeVerify {
    public static void main(String[] args) {
        SerializeVerify serial = new SerializeVerify();
        serial.doubleCheckJavaSerializeVerify();
        serial.doubleCheckFixSerializeVerify();
        serial.enumSerializeVerify();
        serial.innerStaticSerializeVerify();
        serial.notSerializeVerify();
    }

    /**
     * 无法阻止序列化创建不同的实例
     */
    public void doubleCheckJavaSerializeVerify(){
        javaSerialize(DoubleCheckSerializeable.getInstance());
        DoubleCheckSerializeable first = (DoubleCheckSerializeable)javaDeserialize();
        PrintUtil.out("doubleCheckJavaSerializeVerify first obj is  DoubleCheckSerializeable.getInstance obj ? %b", (first.hashCode() == DoubleCheckSerializeable.getInstance().hashCode()));
    }
    public void doubleCheckFixSerializeVerify(){
        javaSerialize(DoubleCheckFix.getInstance());
        DoubleCheckFix first = (DoubleCheckFix)javaDeserialize();
        PrintUtil.out("doubleCheckFixSerializeVerify first obj is  DoubleCheckFix.getInstance obj ? %b", (first.hashCode() == DoubleCheckFix.getInstance().hashCode()));
    }

    public void notSerializeVerify(){
        PrintUtil.out("not support serialize will be error");
    }

    public void enumSerializeVerify(){
        javaSerialize(SingleTonEnum.getInstance());
        SingleTonEnum first = (SingleTonEnum) javaDeserialize();
        PrintUtil.out("enumSerializeVerify first obj is  SingleTonEnum.getInstance obj ? %b", (first.hashCode() == SingleTonEnum.getInstance().hashCode()));
    }

    /**
     * 无法阻止序列化创建不同的实例
     */
    public void innerStaticSerializeVerify(){
        javaSerialize(StaticInnerSingleTonSerializable.getInstance());
        StaticInnerSingleTonSerializable first = (StaticInnerSingleTonSerializable) javaDeserialize();
        PrintUtil.out("innerStaticSerializeVerify first obj is  StaticInnerSingleTonSerializable.getInstance obj ? %b", (first.hashCode() == StaticInnerSingleTonSerializable.getInstance().hashCode()));
    }


    private void javaSerialize(Object dc){
        FileOutputStream out=null;
        ObjectOutputStream stream=null;
        try {
            out = new FileOutputStream(DirectorUtil.dcSerializeAddress());
            stream = new ObjectOutputStream(out);
            stream.writeObject(dc);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
          try {
              if (stream!=null){
                  stream.close();
              }
              if (out!=null){
                  out.close();
              }
          } catch (IOException e) {
              e.printStackTrace();

          }

        }
    }

    private Object javaDeserialize(){
        FileInputStream in =null;
        ObjectInputStream stream=null;
        try {
            in=new FileInputStream(DirectorUtil.dcSerializeAddress());
            stream=new ObjectInputStream(in);
            return  stream.readObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }finally {
            try {
              if (stream!=null){
                  stream.close();
              }
                if (in!=null){
                    in.close();
                }
            }catch (IOException e) {
                e.printStackTrace();

            }
        }
        return null;
    }
}
