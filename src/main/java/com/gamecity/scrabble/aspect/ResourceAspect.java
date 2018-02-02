package com.gamecity.scrabble.aspect;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ResourceAspect
{
    private static final Logger logger = LoggerFactory.getLogger(ResourceAspect.class);

    @Pointcut("execution(* com.gamecity.scrabble.resource..*(..))")
    public void resource()
    {
        // nothing to do
    }

    @Around("resource()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable
    {
        long start = System.currentTimeMillis();
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        long elapsedTime = System.currentTimeMillis() - start;
        logger.info("Method {}.{}({}) -> execution time : {} ms", className, methodName, Arrays.toString(joinPoint.getArgs()), elapsedTime);
        return joinPoint.proceed();
    }

    @AfterThrowing(pointcut = "resource()", throwing = "exception")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable exception)
    {
        logger.error("An exception has been thrown in " + joinPoint.getSignature().getName() + " ()");
        logger.error("Cause : {}, {}", exception.getMessage(), exception);
    }
}
