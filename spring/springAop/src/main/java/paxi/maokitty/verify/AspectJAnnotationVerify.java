package paxi.maokitty.verify;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import paxi.maokitty.verify.domain.ComplexClass;
import paxi.maokitty.verify.service.AnnotationService;
import paxi.maokitty.verify.command.inter.ExecuteInterface;

/**
 * Created by maokitty on 19/5/14.
 * https://docs.spring.io/spring/docs/4.2.x/spring-framework-reference/html/aop.html#aop-introduction
 */
public class AspectJAnnotationVerify {
    public static void main(String[] args) {
        ApplicationContext ac = new ClassPathXmlApplicationContext("aop-application.xml");
        ExecuteInterface exe = ac.getBean("executeService", ExecuteInterface.class);
        ComplexClass c=new ComplexClass();
        c.setValue(1);
        c.setDesc("complex");
        exe.logicHandleMultipleParam(1,c);
        AnnotationService annotationService = ac.getBean("annotationService", AnnotationService.class);
        annotationService.logicHandle(1,c);
    }
}
