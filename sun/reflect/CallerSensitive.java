package sun.reflect;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME) // 该Annotation在运行期间都有效
@Target({java.lang.annotation.ElementType.METHOD}) // 作用于方法
public @interface CallerSensitive {
}