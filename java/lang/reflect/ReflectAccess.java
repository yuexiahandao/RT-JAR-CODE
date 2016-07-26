/*     */
package java.lang.reflect;
/*     */ 
/*     */

import sun.reflect.ConstructorAccessor;
/*     */ import sun.reflect.LangReflectAccess;
/*     */ import sun.reflect.MethodAccessor;

/*     */
/*     */ class ReflectAccess
/*     */ implements LangReflectAccess
/*     */ {
    /*     */
    public Field newField(Class<?> paramClass1, String paramString1, Class<?> paramClass2, int paramInt1, int paramInt2, String paramString2, byte[] paramArrayOfByte)
/*     */ {
/*  44 */
        return new Field(paramClass1, paramString1, paramClass2, paramInt1, paramInt2, paramString2, paramArrayOfByte);
/*     */
    }

    /*     */
/*     */
    public Method newMethod(Class<?> paramClass1, String paramString1, Class<?>[] paramArrayOfClass1, Class<?> paramClass2, Class<?>[] paramArrayOfClass2, int paramInt1, int paramInt2, String paramString2, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3)
/*     */ {
/*  65 */
        return new Method(paramClass1, paramString1, paramArrayOfClass1, paramClass2, paramArrayOfClass2, paramInt1, paramInt2, paramString2, paramArrayOfByte1, paramArrayOfByte2, paramArrayOfByte3);
/*     */
    }

    /*     */
/*     */
    public <T> Constructor<T> newConstructor(Class<T> paramClass, Class<?>[] paramArrayOfClass1, Class<?>[] paramArrayOfClass2, int paramInt1, int paramInt2, String paramString, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
/*     */ {
/*  87 */
        return new Constructor(paramClass, paramArrayOfClass1, paramArrayOfClass2, paramInt1, paramInt2, paramString, paramArrayOfByte1, paramArrayOfByte2);
/*     */
    }

    /*     */
/*     */
    public MethodAccessor getMethodAccessor(Method paramMethod)
/*     */ {
/*  98 */
        return paramMethod.getMethodAccessor();
/*     */
    }

    /*     */
/*     */
    public void setMethodAccessor(Method paramMethod, MethodAccessor paramMethodAccessor) {
/* 102 */
        paramMethod.setMethodAccessor(paramMethodAccessor);
/*     */
    }

    /*     */
/*     */
    public ConstructorAccessor getConstructorAccessor(Constructor<?> paramConstructor) {
/* 106 */
        return paramConstructor.getConstructorAccessor();
/*     */
    }

    /*     */
/*     */
    public void setConstructorAccessor(Constructor<?> paramConstructor, ConstructorAccessor paramConstructorAccessor)
/*     */ {
/* 112 */
        paramConstructor.setConstructorAccessor(paramConstructorAccessor);
/*     */
    }

    /*     */
/*     */
    public int getConstructorSlot(Constructor<?> paramConstructor) {
/* 116 */
        return paramConstructor.getSlot();
/*     */
    }

    /*     */
/*     */
    public String getConstructorSignature(Constructor<?> paramConstructor) {
/* 120 */
        return paramConstructor.getSignature();
/*     */
    }

    /*     */
/*     */
    public byte[] getConstructorAnnotations(Constructor<?> paramConstructor) {
/* 124 */
        return paramConstructor.getRawAnnotations();
/*     */
    }

    /*     */
/*     */
    public byte[] getConstructorParameterAnnotations(Constructor<?> paramConstructor) {
/* 128 */
        return paramConstructor.getRawParameterAnnotations();
/*     */
    }

    /*     */
/*     */
    public Method copyMethod(Method paramMethod)
/*     */ {
/* 136 */
        return paramMethod.copy();
/*     */
    }

    /*     */
/*     */
    public Field copyField(Field paramField) {
/* 140 */
        return paramField.copy();
/*     */
    }

    /*     */
/*     */
    public <T> Constructor<T> copyConstructor(Constructor<T> paramConstructor) {
/* 144 */
        return paramConstructor.copy();
/*     */
    }
/*     */
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.reflect.ReflectAccess
 * JD-Core Version:    0.6.2
 */