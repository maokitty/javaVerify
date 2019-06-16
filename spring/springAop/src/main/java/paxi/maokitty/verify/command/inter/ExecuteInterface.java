package paxi.maokitty.verify.command.inter;


import paxi.maokitty.verify.domain.ComplexClass;

/**
 * Created by maokitty on 19/5/15.
 */
public interface ExecuteInterface  {
    void logicHandleMultipleParam(int p, ComplexClass c);
    void logicError();
}
