package ar.edu.ubp.das.mascotasrest.aspects;

import ar.edu.ubp.das.mascotasrest.BE.UsuarioSesionBE;
import ar.edu.ubp.das.mascotasrest.config.JwtFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.Arrays;

@Aspect
@Component
@Order(2)
public class AuditAspect {
    private static final Logger logger = LoggerFactory.getLogger(AuditAspect.class);

    @Pointcut("execution(* ar.edu.ubp.das.mascotasrest.resources..*(..))")
    public void resourceMethods() {}

    @Before("resourceMethods()")
    public void logBefore(JoinPoint joinPoint) {
        logger.info("Usuario: {} | Entrando en {}.{} con argumentos: {}",
                usuarioActual(),
                joinPoint.getSignature().getDeclaringType().getSimpleName(),
                joinPoint.getSignature().getName(),
                argumentosSeguros(joinPoint.getArgs()));
    }

    @AfterReturning(pointcut = "resourceMethods()", returning = "result")
    public void logAfter(JoinPoint joinPoint, Object result) {
        Object estado = (result instanceof ResponseEntity<?> r) ? r.getStatusCode() : "-";
        logger.info("Usuario: {} | Método {}.{} ejecutado con éxito. Estado HTTP: {}",
                usuarioActual(),
                joinPoint.getSignature().getDeclaringType().getSimpleName(),
                joinPoint.getSignature().getName(),
                estado);
    }

    private String usuarioActual() {
        RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
        Object usuario = attrs == null ? null
                : attrs.getAttribute(JwtFilter.ATRIBUTO_USUARIO, RequestAttributes.SCOPE_REQUEST);
        return usuario instanceof UsuarioSesionBE u ? u.getCuil() + " (" + u.getRol() + ")" : "anónimo";
    }

    // El login no se registra con sus argumentos para no dejar la contraseña en el log
    private String argumentosSeguros(Object[] args) {
        return Arrays.stream(args)
                .filter(a -> !(a instanceof UsuarioSesionBE))
                .map(a -> a != null && a.getClass().getSimpleName().equals("LoginDataBE") ? "LoginDataBE{***}" : String.valueOf(a))
                .toList()
                .toString();
    }
}
