package project.gasnow.common.aop;

import org.aspectj.lang.annotation.Pointcut;

public class PointcutBundle {

    @Pointcut("execution(* project.gasnow..*Controller*.*(..))")
    public void controllerPointCut(){}

    @Pointcut("execution(* project.gasnow..*ServiceImpl*.*(..))")
    public void serviceImplPointCut(){}
}
