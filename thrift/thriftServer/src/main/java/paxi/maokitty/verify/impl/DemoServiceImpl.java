package paxi.maokitty.verify.impl;

import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import paxi.maokitty.verify.exception.myException;
import paxi.maokitty.verify.service.DemoService;

/**
 * Created by maokitty on 2019/8/8.
 */
public class DemoServiceImpl implements DemoService.Iface{
    private static final Logger LOG = LoggerFactory.getLogger(DemoServiceImpl.class);
    @Override
    public String say(String msg) throws myException, TException {
        LOG.info("say:{}",msg);
        return "git it";
    }
}
