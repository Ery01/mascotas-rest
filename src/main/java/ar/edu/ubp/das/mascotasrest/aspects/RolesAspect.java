package ar.edu.ubp.das.mascotasrest.aspects;

import ar.edu.ubp.das.mascotasrest.BE.UsuarioSesionBE;
import ar.edu.ubp.das.mascotasrest.config.JwtFilter;
import ar.edu.ubp.das.mascotasrest.exceptions.NegocioException;
import ar.edu.ubp.das.mascotasrest.security.RolesPermitidos;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;

@Aspect
@Component
@Order(1)
public class RolesAspect {

    @Before("execution(* ar.edu.ubp.das.mascotasrest.resources..*(..))")
    public void verificarRol(JoinPoint joinPoint) {
        MethodSignature firma = (MethodSignature) joinPoint.getSignature();
        RolesPermitidos permitidos = firma.getMethod().getAnnotation(RolesPermitidos.class);
        if (permitidos == null) {
            permitidos = joinPoint.getTarget().getClass().getAnnotation(RolesPermitidos.class);
        }
        if (permitidos == null) {
            return;
        }

        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        UsuarioSesionBE usuario = (UsuarioSesionBE) request.getAttribute(JwtFilter.ATRIBUTO_USUARIO);

        if (usuario == null) {
            throw new NegocioException(HttpStatus.UNAUTHORIZED, "Debe iniciar sesión para acceder a este recurso.");
        }
        if (!Arrays.asList(permitidos.value()).contains(usuario.getRol())) {
            throw new NegocioException(HttpStatus.FORBIDDEN, "Su perfil no tiene permiso para realizar esta operación.");
        }
    }
}
