package paxi.maokitty.verify.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import paxi.maokitty.verify.service.FindService;
import paxi.maokitty.verify.command.inter.Command;


/**
 * Created by maokitty on 19/6/16.
 */
public class FindCommand implements Command {
    private static Logger LOG = LoggerFactory.getLogger(FindCommand.class);

    private String target;
    private FindService findService;
    public FindCommand(FindService findService,String target) {
        this.findService=findService;
        this.target=target;
    }

    @Override
    public boolean execute() {
        LOG.info("execute find command ");
       return findService.findTargetFilePath(target);
    }
}
