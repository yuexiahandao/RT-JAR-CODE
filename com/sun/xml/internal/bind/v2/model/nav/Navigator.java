package com.sun.xml.internal.bind.v2.model.nav;

import com.sun.xml.internal.bind.v2.runtime.Location;
import java.util.Collection;

public abstract interface Navigator<T, C, F, M>
{
  public abstract C getSuperClass(C paramC);

  public abstract T getBaseClass(T paramT, C paramC);

  public abstract String getClassName(C paramC);

  public abstract String getTypeName(T paramT);

  public abstract String getClassShortName(C paramC);

  public abstract Collection<? extends F> getDeclaredFields(C paramC);

  public abstract F getDeclaredField(C paramC, String paramString);

  public abstract Collection<? extends M> getDeclaredMethods(C paramC);

  public abstract C getDeclaringClassForField(F paramF);

  public abstract C getDeclaringClassForMethod(M paramM);

  public abstract T getFieldType(F paramF);

  public abstract String getFieldName(F paramF);

  public abstract String getMethodName(M paramM);

  public abstract T getReturnType(M paramM);

  public abstract T[] getMethodParameters(M paramM);

  public abstract boolean isStaticMethod(M paramM);

  public abstract boolean isSubClassOf(T paramT1, T paramT2);

  public abstract T ref(Class paramClass);

  public abstract T use(C paramC);

  public abstract C asDecl(T paramT);

  public abstract C asDecl(Class paramClass);

  public abstract boolean isArray(T paramT);

  public abstract boolean isArrayButNotByteArray(T paramT);

  public abstract T getComponentType(T paramT);

  public abstract T getTypeArgument(T paramT, int paramInt);

  public abstract boolean isParameterizedType(T paramT);

  public abstract boolean isPrimitive(T paramT);

  public abstract T getPrimitive(Class paramClass);

  public abstract Location getClassLocation(C paramC);

  public abstract Location getFieldLocation(F paramF);

  public abstract Location getMethodLocation(M paramM);

  public abstract boolean hasDefaultConstructor(C paramC);

  public abstract boolean isStaticField(F paramF);

  public abstract boolean isPublicMethod(M paramM);

  public abstract boolean isFinalMethod(M paramM);

  public abstract boolean isPublicField(F paramF);

  public abstract boolean isEnum(C paramC);

  public abstract <P> T erasure(T paramT);

  public abstract boolean isAbstract(C paramC);

  public abstract boolean isFinal(C paramC);

  public abstract F[] getEnumConstants(C paramC);

  public abstract T getVoidType();

  public abstract String getPackageName(C paramC);

  public abstract C loadObjectFactory(C paramC, String paramString);

  public abstract boolean isBridgeMethod(M paramM);

  public abstract boolean isOverriding(M paramM, C paramC);

  public abstract boolean isInterface(C paramC);

  public abstract boolean isTransient(F paramF);

  public abstract boolean isInnerClass(C paramC);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.model.nav.Navigator
 * JD-Core Version:    0.6.2
 */