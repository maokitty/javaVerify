package paxi.maokitty.verify.designPattern.singleton;

import paxi.maokitty.verify.service.singleton.cloneable.DoubleCheckCloneable;
import paxi.maokitty.verify.service.singleton.cloneable.StaticInnerCloneable;
import paxi.maokitty.verify.service.singleton.tryfix.DoubleCheckFix;
import paxi.maokitty.verify.util.PrintUtil;

/**
 * Created by maokitty on 19/5/2.
 */
public class CloneVerify {
    public static void main(String[] args) {
        CloneVerify verify = new CloneVerify();
        verify.doubleCheckVerify();
        verify.innerStaticVerify();
        verify.enumVerify();
        verify.notCloneableVerify();
        verify.doubleCheckFixCloneVerify();
    }

    /**
     * 无法阻止克隆创建不同的实例
     */
    public void doubleCheckVerify(){
        try {
            DoubleCheckCloneable first= (DoubleCheckCloneable) DoubleCheckCloneable.getInstance().clone();
            PrintUtil.out("doubleCheckVerify first obj is  DoubleCheckCloneable.getInstance() obj ? %b",(first.hashCode()== DoubleCheckCloneable.getInstance().hashCode()));
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    public void doubleCheckFixCloneVerify(){
        DoubleCheckFix first = null;
        try {
            first = (DoubleCheckFix)DoubleCheckFix.getInstance().clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        PrintUtil.out("doubleCheckFixCloneVerify first obj is  DoubleCheckFix.getInstance obj ? %b", (first.hashCode() == DoubleCheckFix.getInstance().hashCode()));
    }


    /**
     * 无法阻止克隆创建不同的实例
     */
    public void innerStaticVerify(){
        try {
            StaticInnerCloneable first = (StaticInnerCloneable) StaticInnerCloneable.getInstance().clone();
            PrintUtil.out("doubleCheckVerify first obj is  StaticInnerCloneable.getInstance() obj ? %b",(first.hashCode()== StaticInnerCloneable.getInstance().hashCode()));
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    public void notCloneableVerify(){
        PrintUtil.out("not support cloneable class will be error");
    }

    public void enumVerify(){
        PrintUtil.out("enum not support clone");
    }
}
