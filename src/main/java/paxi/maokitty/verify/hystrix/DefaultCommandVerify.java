package paxi.maokitty.verify.hystrix;

import paxi.maokitty.verify.hystrix.command.DefaultSettingCommand;
import paxi.maokitty.verify.hystrix.service.RemoteLogicService;

/**
 * Created by maokitty on 19/5/21.
 */
public class DefaultCommandVerify {
    public static void main(String[] args) {
        RemoteLogicService logicService = new RemoteLogicService();
        String world="default command";
        DefaultSettingCommand defaultCommand = new DefaultSettingCommand(logicService,world);
        defaultCommand.execute();
    }
}
