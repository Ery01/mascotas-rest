package ar.edu.ubp.das.mascotasrest.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indica qué roles pueden ejecutar un recurso REST.
 * Se puede poner sobre la clase (aplica a todos sus métodos) o sobre un método.
 * La verificación la hace el aspecto RolesAspect (POA).
 *
 * Ejemplo: @RolesPermitidos(Roles.REFUGIO)
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RolesPermitidos {

    String[] value();

}
