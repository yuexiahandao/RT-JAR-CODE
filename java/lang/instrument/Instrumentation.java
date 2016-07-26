package java.lang.instrument;

import java.util.jar.JarFile;

public abstract interface Instrumentation {
    public abstract void addTransformer(ClassFileTransformer paramClassFileTransformer, boolean paramBoolean);

    public abstract void addTransformer(ClassFileTransformer paramClassFileTransformer);

    public abstract boolean removeTransformer(ClassFileTransformer paramClassFileTransformer);

    public abstract boolean isRetransformClassesSupported();

    public abstract void retransformClasses(Class<?>[] paramArrayOfClass)
            throws UnmodifiableClassException;

    public abstract boolean isRedefineClassesSupported();

    public abstract void redefineClasses(ClassDefinition[] paramArrayOfClassDefinition)
            throws ClassNotFoundException, UnmodifiableClassException;

    public abstract boolean isModifiableClass(Class<?> paramClass);

    public abstract Class[] getAllLoadedClasses();

    public abstract Class[] getInitiatedClasses(ClassLoader paramClassLoader);

    public abstract long getObjectSize(Object paramObject);

    public abstract void appendToBootstrapClassLoaderSearch(JarFile paramJarFile);

    public abstract void appendToSystemClassLoaderSearch(JarFile paramJarFile);

    public abstract boolean isNativeMethodPrefixSupported();

    public abstract void setNativeMethodPrefix(ClassFileTransformer paramClassFileTransformer, String paramString);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.instrument.Instrumentation
 * JD-Core Version:    0.6.2
 */