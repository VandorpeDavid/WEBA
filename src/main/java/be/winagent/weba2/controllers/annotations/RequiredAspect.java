package be.winagent.weba2.controllers.annotations;

import be.winagent.weba2.controllers.exceptions.NotFoundException;
import lombok.SneakyThrows;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;

@Aspect
@Component
public class RequiredAspect {
    @SneakyThrows
    @Before("execution(* *(.., @Required (*), ..))")
    public void checkNotNull(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getMethod().getName();
        Class<?>[] parameterTypes = signature.getMethod().getParameterTypes();
        Annotation[][] methodAnnotations = joinPoint.getTarget().getClass().getMethod(methodName, parameterTypes).getParameterAnnotations();

        for (int i = 0; i < methodAnnotations.length; ++i) {
            Annotation[] parameterAnnotations = methodAnnotations[i];

            for (Annotation annotation : parameterAnnotations) {
                if (annotation instanceof Required) {
                    if (joinPoint.getArgs()[i] == null) {
                        throw new NotFoundException();
                    }

                    break;
                }
            }
        }
    }
}