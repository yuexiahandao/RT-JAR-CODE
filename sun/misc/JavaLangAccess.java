package sun.misc;

import java.security.AccessControlContext;
import sun.nio.ch.Interruptible;
import sun.reflect.ConstantPool;
import sun.reflect.annotation.AnnotationType;

public abstract interface JavaLangAccess
{
  public abstract ConstantPool getConstantPool(Class paramClass);

  public abstract boolean casAnnotationType(Class<?> paramClass, AnnotationType paramAnnotationType1, AnnotationType paramAnnotationType2);

  public abstract AnnotationType getAnnotationType(Class paramClass);

  public abstract byte[] getRawClassAnnotations(Class<?> paramClass);

  public abstract <E extends Enum<E>> E[] getEnumConstantsShared(Class<E> paramClass);

  public abstract void blockedOn(Thread paramThread, Interruptible paramInterruptible);

  public abstract void registerShutdownHook(int paramInt, boolean paramBoolean, Runnable paramRunnable);

  public abstract int getStackTraceDepth(Throwable paramThrowable);

  public abstract StackTraceElement getStackTraceElement(Throwable paramThrowable, int paramInt);

  public abstract int getStringHash32(String paramString);

  public abstract Thread newThreadWithAcc(Runnable paramRunnable, AccessControlContext paramAccessControlContext);

  public abstract void invokeFinalize(Object paramObject)
    throws Throwable;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.misc.JavaLangAccess
 * JD-Core Version:    0.6.2
 */