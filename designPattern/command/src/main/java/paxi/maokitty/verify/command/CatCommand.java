package paxi.maokitty.verify.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import paxi.maokitty.verify.command.inter.Command;
import paxi.maokitty.verify.service.CatService;

/**
 * Created by maokitty on 19/6/16.
 */
public class CatCommand implements Command {
    private static final Logger LOG = LoggerFactory.getLogger(CatCommand.class);

    private CatService catService;
    private String target;

    public CatCommand(CatService catService,String target) {
        this.catService = catService;
        this.target=target;
    }
    @Override
    public boolean execute() {
        LOG.info("execute cat command");
        catService.catTargetFile(target);
        return true;
    }
}
