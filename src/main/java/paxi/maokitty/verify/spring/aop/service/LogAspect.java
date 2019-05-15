package paxi.maokitty.verify.spring.aop.service;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import paxi.maokitty.verify.spring.aop.domain.ComplexClass;

/**
 * Created by maokitty on 19/5/14.
 */
@Service
@Aspect
public class LogAspect {
    private static final Logger LOG = LoggerFactory.getLogger(LogAspect.class);

    //定义所有ExecuteService的方法执行的时候都需要加上这个切面
    @Pointcut("execution(* paxi.maokitty.verify.spring.aop.service.ExecuteService.*(..))")
    public void allClassPointCut(){
        //空壳，什么都不会干
        LOG.info("this log will not be show");
    }


    @Before("allClassPointCut()")
    public void beforeAspectExecuteService(JoinPoint joinPoint){
        LOG.info("beforeAspectExecuteService execute method:{}",new Object[]{joinPoint.getStaticPart().toShortString()});
    }

    @Around("allClassPointCut()")
    public Object aroundAspectExecuteService(ProceedingJoinPoint pjp) throws Throwable {
        LOG.info("aroundAspectExecuteService before execute method:{}",new Object[]{pjp.getStaticPart().toShortString()});
        try {
            Object proceed = pjp.proceed();
            LOG.info("aroundAspectExecuteService after execute method:{}", new Object[]{pjp.getStaticPart().toShortString()});
            return proceed;
        } catch (Throwable throwable) {
            LOG.error("aroundAspectExecuteService",throwable);
            throw throwable;
        }
    }
    //默认的value就是 PointCut
    @After("allClassPointCut()")
    public void afterAspectExecuteService(JoinPoint joinPoint){
        LOG.info("afterAspectExecuteService after method:{}", new Object[]{joinPoint.getStaticPart().toShortString()});
        showParam(joinPoint.getArgs());
    }

    @AfterThrowing(pointcut = "allClassPointCut()",throwing = "ex")
    public void afterThrowing(JoinPoint joinPoint,Throwable ex){
        LOG.info("afterThrowing  method:{}", new Object[]{joinPoint.getStaticPart().toShortString(), ex});
    }

    @AfterReturning("allClassPointCut()")
    public void afterReturining(JoinPoint joinPoint){
        LOG.info("afterReturining  method:{}",new Object[]{joinPoint.getStaticPart().toShortString()});
    }

    //方法上定义注解
    @Pointcut("@annotation(paxi.maokitty.verify.spring.aop.annotation.SelfAnnotation))")
    public void selfAnnotionPointCut(){}

    @Around("selfAnnotionPointCut()")
    public Object annotationAround(ProceedingJoinPoint jp){
        String shortFuncDesc = jp.getStaticPart().toShortString();
        LOG.info("annotationAround before method:{}", shortFuncDesc);
        Object proceed = null;
        try {
            MethodSignature signature = (MethodSignature) jp.getSignature();
            LOG.info("annotationAround show method annotation:{}",signature.getMethod().getDeclaredAnnotations()[0].toString());
            showParam(jp.getArgs());
            proceed = jp.proceed();
            LOG.info("annotationAround after method:{}",shortFuncDesc);
        } catch (Throwable throwable) {
            LOG.error("annotationAround",throwable);
        }
        return proceed;
    }

    private void showParam(Object[] args){
        if (args == null){
            LOG.info("showParam args is null");
            return;
        }
        for (Object arg:args){
            if (arg instanceof Integer){
                LOG.info("showParam integer:{}",arg);
            }else if(arg instanceof ComplexClass){
                LOG.info("showParam complexClass:{}",arg);
            }
        }
    }





}
