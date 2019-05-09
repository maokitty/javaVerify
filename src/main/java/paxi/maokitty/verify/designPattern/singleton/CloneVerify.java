package paxi.maokitty.verify.designPattern.singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import paxi.maokitty.verify.designPattern.singleton.service.cloneable.DoubleCheckCloneable;
import paxi.maokitty.verify.designPattern.singleton.service.cloneable.StaticInnerCloneable;
import paxi.maokitty.verify.designPattern.singleton.service.tryfix.DoubleCheckFix;
import paxi.maokitty.verify.util.PrintUtil;

/**
 * Created by maokitty on 19/5/2.
 */
public class CloneVerify {
    private static final Logger LOG = LoggerFactory.getLogger(CloneVerify.class);
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
            LOG.info("doubleCheckVerify first obj is  DoubleCheckCloneable.getInstance() obj ? {}", (first.hashCode() == DoubleCheckCloneable.getInstance().hashCode()));
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
        LOG.info("doubleCheckFixCloneVerify first obj is  DoubleCheckFix.getInstance obj ? {}", (first.hashCode() == DoubleCheckFix.getInstance().hashCode()));
    }


    /**
     * 无法阻止克隆创建不同的实例
     */
    public void innerStaticVerify(){
        try {
            StaticInnerCloneable first = (StaticInnerCloneable) StaticInnerCloneable.getInstance().clone();
            LOG.info("doubleCheckVerify first obj is  StaticInnerCloneable.getInstance() obj ? {}", (first.hashCode() == StaticInnerCloneable.getInstance().hashCode()));
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    public void notCloneableVerify(){
        LOG.info("not support cloneable class will be error");
    }

    public void enumVerify(){
        LOG.info("enum not support clone");
    }
}
