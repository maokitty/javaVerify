package paxi.maokitty.verify.spring.aop;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.NameMatchMethodPointcutAdvisor;
import paxi.maokitty.verify.spring.aop.domain.ComplexClass;
import paxi.maokitty.verify.spring.aop.service.ExecuteService;
import paxi.maokitty.verify.spring.aop.service.inter.ExecuteInterface;

/**
 * Created by maokitty on 19/5/15.
 * https://docs.spring.io/spring/docs/4.2.x/spring-framework-reference/html/aop-api.html#aop-prog
 */
public class ProxyFactoryVerify {
    private static final Logger LOG = LoggerFactory.getLogger(ProxyFactoryVerify.class);
    public static void main(String[] args) {

        //创建代理的时候，目标对象有接口就使用JDK代理，否则使用CGLIB
        ProxyFactory proxyFactory=new ProxyFactory(new ExecuteService());

        MethodInterceptor aroundAdvice = new MethodInterceptor() {
            public Object invoke(MethodInvocation invocation) throws Throwable {
                LOG.info("around advice before method:{}", invocation.getMethod().getName());
                Object proceed = invocation.proceed();
                LOG.info("around advice after method:{}", invocation.getMethod().getName());
                return proceed;
            }
        };


        NameMatchMethodPointcutAdvisor nameAdvisor = new NameMatchMethodPointcutAdvisor();
        //告诉代理这里只拦截 logicHandleMultipleParam 这个PointCut 的执行
        nameAdvisor.setMappedName("logicHandleMultipleParam");
        //设置环绕执行
        nameAdvisor.setAdvice(aroundAdvice);
        proxyFactory.addAdvisor(nameAdvisor);

        //代理接口创建需要使用接口来处理代理，否则会有异常: java.lang.ClassCastException: com.sun.proxy.$Proxy17 cannot be cast to paxi.maokitty.verify.spring.aop.service.ExecuteService
        ExecuteInterface proxy = (ExecuteInterface) proxyFactory.getProxy();
        ComplexClass c=new ComplexClass();
        c.setDesc("complex");
        c.setValue(1);
        proxy.logicHandleMultipleParam(1, c);
        try {
            proxy.logicError();
        }catch (Exception e){
            LOG.error("ignore error detail");
        }

    }
}
