package java.lang.reflect;

import java.lang.annotation.Annotation;

public abstract interface AnnotatedElement {
    public abstract boolean isAnnotationPresent(Class<? extends Annotation> paramClass);

    public abstract <T extends Annotation> T getAnnotation(Class<T> paramClass);

    public abstract Annotation[] getAnnotations();

    public abstract Annotation[] getDeclaredAnnotations();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.reflect.AnnotatedElement
 * JD-Core Version:    0.6.2
 */