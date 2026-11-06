package ar.edu.ubp.das.mascotasrest.aspects;

import ar.edu.ubp.das.mascotasrest.exceptions.NegocioException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(0)
public class ExceptionHandlingAspect {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandlingAspect.class);

    @Around("execution(* ar.edu.ubp.das.mascotasrest.resources..*(..))")
    public Object handleExceptions(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            return joinPoint.proceed();
        }
        catch (NegocioException e) {
            // Error esperado (regla de negocio / permisos): alcanza con un warning
            logger.warn("Operación rechazada en {}.{}: {} ({})",
                    joinPoint.getSignature().getDeclaringType().getSimpleName(),
                    joinPoint.getSignature().getName(),
                    e.getMessage(),
                    e.getStatus());
            throw e;
        }
        catch (Exception e) {
            logger.error("Error en el método: {}.{}. Detalle: {}",
                    joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(),
                    e.getMessage(),
                    e);
            throw e;
        }
    }
}
