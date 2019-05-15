package paxi.maokitty.verify.spring.aop.service.inter;

import paxi.maokitty.verify.spring.aop.domain.ComplexClass;

/**
 * Created by maokitty on 19/5/15.
 */
public interface ExecuteInterface  {
    void logicHandleMultipleParam(int p,ComplexClass c);
    void logicError();
}
