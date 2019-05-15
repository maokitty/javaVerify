package paxi.maokitty.verify.spring.aop.domain;

/**
 * Created by maokitty on 19/5/14.
 */
public class ComplexClass {
    private int value;
    private String desc;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "ComplexClass{" +
                "value=" + value +
                ", desc='" + desc + '\'' +
                '}';
    }
}
