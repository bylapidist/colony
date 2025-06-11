package net.lapidist.colony.serialization;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a class for Kryo registration.
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface KryoType {
}
