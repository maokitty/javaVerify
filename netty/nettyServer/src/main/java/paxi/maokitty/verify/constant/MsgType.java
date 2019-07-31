package paxi.maokitty.verify.constant;

/**
 * Created by maokitty on 19/7/9.
 */
public enum MsgType {
    PING(1,"ping"),
    PONG(2,"pong"),
    REGISTER(3,"register"),
    ECHO(4,"echo"),
    UNKNOWN(-1,"unknown")
    ;
    private int val;
    private String desc;

    MsgType(int val, String desc) {
        this.val = val;
        this.desc = desc;
    }

    public int getVal() {
        return val;
    }

    public String getDesc() {
        return desc;
    }
    public static MsgType get(String message){
        switch (message){
            case "ping": return PING;
            case "pong":return PONG;
            case "register": return REGISTER;
            case "echo":return ECHO;
            default:return UNKNOWN;
        }
    }

}
