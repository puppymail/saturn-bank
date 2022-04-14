package com.epam.saturn.operator.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.util.Arrays;

@Component
@Aspect
public class LoggingAspect {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Pointcut("within(@org.springframework.stereotype.Service *)" +
            " || within(@org.springframework.stereotype.Controller *)")
    public void springBeanPointcut() {}

    @Pointcut("within(com.epam.saturn.operator..*)" +
            " || within(com.epam.saturn.operator.service..*)" +
            " || within(com.epam.saturn.operator.controller..*)")
    public void applicationPackagePointcut() {}

    @Pointcut("execution(* org.springframework.data.repository.Repository+.*(..))")
    public void repositoryPointcut() {}

    @Around("applicationPackagePointcut() && springBeanPointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        return log(joinPoint);
    }

    @Around("repositoryPointcut()")
    public Object logRepository(ProceedingJoinPoint joinPoint) throws Throwable {
        return log(joinPoint);
    }

    @AfterThrowing(pointcut = "applicationPackagePointcut() && springBeanPointcut()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        logger.error("Exception in {}.{}() with cause: {}, message: {}", joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(), e.getCause(), e.getMessage());
    }

    private Object log(ProceedingJoinPoint joinPoint) throws Throwable {
        String[] parameterNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();
        Object[] args = joinPoint.getArgs();
        String[] parameters = new String[args.length];
        for (int i = 0; i < parameters.length; i++) {
            parameters[i] = parameterNames[i] + "=" + args[i];
        }
        logger.debug("Enter: {}.{}() with argument[s]: {}", joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(), Arrays.toString(parameters));
        try {
            Object result = joinPoint.proceed();
            logger.debug("Exit: {}.{}() with result: {}", joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(), result);
            return result;
        } catch (IllegalArgumentException e) {
            logger.error("Illegal argument: {} in {}.{}()", Arrays.toString(joinPoint.getArgs()),
                    joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
            throw e;
        }
    }

}
