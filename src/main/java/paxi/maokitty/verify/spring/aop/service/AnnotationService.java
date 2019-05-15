package paxi.maokitty.verify.spring.aop.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import paxi.maokitty.verify.spring.aop.annotation.SelfAnnotation;
import paxi.maokitty.verify.spring.aop.domain.ComplexClass;

/**
 * Created by maokitty on 19/5/14.
 */
@Service
public class AnnotationService {
    private static final Logger LOG = LoggerFactory.getLogger(AnnotationService.class);

    @SelfAnnotation
    public void logicHandle(int p,ComplexClass c){
        LOG.info("logicHandle p:{} ComplexClass:{}",p,c);
    }
}
