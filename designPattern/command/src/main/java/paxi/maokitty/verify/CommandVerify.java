package paxi.maokitty.verify;

import paxi.maokitty.verify.command.CatCommand;
import paxi.maokitty.verify.service.CatService;
import paxi.maokitty.verify.command.FindCommand;
import paxi.maokitty.verify.service.FindService;

/**
 * Created by maokitty on 19/6/16.
 * 简单的命令模式，仅需要持有接受者
 */
public class CommandVerify {
    public static void main(String[] args) {
        FindService findService=new FindService();
        CatService catService = new CatService();
        String target = String.valueOf(1);
        //可以记录执行了那些命令，如果出现了问题，能够按照执行的顺序做恢复
        CatCommand catCommand = new CatCommand(catService,target);
        FindCommand findCommand = new FindCommand(findService,target);
        if (findCommand.execute()){
            catCommand.execute();
        }

    }
}
