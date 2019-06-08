package paxi.maokitty.verify.util;

/**
 * Created by maokitty on 19/5/2.
 */
public class DirectorUtil {
    public static String getProjectDir(){
        return System.getProperty("user.dir")+"/designPattern/singleton";
    }

    public static String dcSerializeAddress(){
        return getProjectDir()+"/src/main/resources/serialize/singleton.ser";
    }
}
