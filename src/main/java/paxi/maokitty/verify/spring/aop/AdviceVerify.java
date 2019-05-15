package paxi.maokitty.verify.spring.aop;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import paxi.maokitty.verify.spring.aop.domain.ComplexClass;
import paxi.maokitty.verify.spring.aop.service.AnnotationService;
import paxi.maokitty.verify.spring.aop.service.ExecuteService;

/**
 * Created by maokitty on 19/5/14.
 */
public class AdviceVerify {
    public static void main(String[] args) {
        ApplicationContext ac = new ClassPathXmlApplicationContext("aop-application.xml");
        ExecuteService exe = ac.getBean("executeService", ExecuteService.class);
        ComplexClass c=new ComplexClass();
        c.setValue(1);
        c.setDesc("complex");
        exe.logicHandleMultipleParam(1,c);
        AnnotationService annotationService = ac.getBean("annotationService", AnnotationService.class);
        annotationService.logicHandle(1,c);
    }
}
