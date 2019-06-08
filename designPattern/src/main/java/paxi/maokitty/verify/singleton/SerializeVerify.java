package paxi.maokitty.verify.singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import paxi.maokitty.verify.singleton.service.SingleTonEnum;
import paxi.maokitty.verify.singleton.service.serializeable.DoubleCheckSerializeable;
import paxi.maokitty.verify.singleton.service.serializeable.StaticInnerSingleTonSerializable;
import paxi.maokitty.verify.singleton.service.tryfix.DoubleCheckFix;
import paxi.maokitty.verify.util.DirectorUtil;

import java.io.*;

/**
 * Created by maokitty on 19/5/2.
 */
public class SerializeVerify {
    private static final Logger LOG = LoggerFactory.getLogger(SerializeVerify.class);
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
        LOG.info("doubleCheckJavaSerializeVerify first obj is  DoubleCheckSerializeable.getInstance obj ? {}", (first.hashCode() == DoubleCheckSerializeable.getInstance().hashCode()));
    }
    public void doubleCheckFixSerializeVerify(){
        javaSerialize(DoubleCheckFix.getInstance());
        DoubleCheckFix first = (DoubleCheckFix)javaDeserialize();
        LOG.info("doubleCheckFixSerializeVerify first obj is  DoubleCheckFix.getInstance obj ? {} ", (first.hashCode() == DoubleCheckFix.getInstance().hashCode()));
    }

    public void notSerializeVerify(){
        LOG.info("not support serialize will be error");
    }

    public void enumSerializeVerify(){
        javaSerialize(SingleTonEnum.getInstance());
        SingleTonEnum first = (SingleTonEnum) javaDeserialize();
        LOG.info("enumSerializeVerify first obj is  SingleTonEnum.getInstance obj ? {}", (first.hashCode() == SingleTonEnum.getInstance().hashCode()));
    }

    /**
     * 无法阻止序列化创建不同的实例
     */
    public void innerStaticSerializeVerify(){
        javaSerialize(StaticInnerSingleTonSerializable.getInstance());
        StaticInnerSingleTonSerializable first = (StaticInnerSingleTonSerializable) javaDeserialize();
        LOG.info("innerStaticSerializeVerify first obj is  StaticInnerSingleTonSerializable.getInstance obj ? {}", (first.hashCode() == StaticInnerSingleTonSerializable.getInstance().hashCode()));
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
