package com.sun.beans.finder;

final class Signature {
    private final Class<?> type;
    private final String name;
    private final Class<?>[] args;
    private volatile int code;

    Signature(Class<?> paramClass, Class<?>[] paramArrayOfClass) {
        this(paramClass, null, paramArrayOfClass);
    }

    Signature(Class<?> paramClass, String paramString, Class<?>[] paramArrayOfClass) {
        this.type = paramClass;
        this.name = paramString;
        this.args = paramArrayOfClass;
    }

    Class<?> getType() {
        return this.type;
    }

    String getName() {
        return this.name;
    }

    Class<?>[] getArgs() {
        return this.args;
    }

    public boolean equals(Object paramObject) {
        if (this == paramObject) {
            return true;
        }
        if ((paramObject instanceof Signature)) {
            Signature localSignature = (Signature) paramObject;
            return (isEqual(localSignature.type, this.type)) && (isEqual(localSignature.name, this.name)) && (isEqual(localSignature.args, this.args));
        }

        return false;
    }

    private static boolean isEqual(Object paramObject1, Object paramObject2) {
        return paramObject1 == null ? false : paramObject2 == null ? true : paramObject1.equals(paramObject2);
    }

    private static boolean isEqual(Class<?>[] paramArrayOfClass1, Class<?>[] paramArrayOfClass2) {
        if ((paramArrayOfClass1 == null) || (paramArrayOfClass2 == null)) {
            return paramArrayOfClass1 == paramArrayOfClass2;
        }
        if (paramArrayOfClass1.length != paramArrayOfClass2.length) {
            return false;
        }
        for (int i = 0; i < paramArrayOfClass1.length; i++) {
            if (!isEqual(paramArrayOfClass1[i], paramArrayOfClass2[i])) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        if (this.code == 0) {
            int i = 17;
            i = addHashCode(i, this.type);
            i = addHashCode(i, this.name);

            if (this.args != null) {
                for (Class localClass : this.args) {
                    i = addHashCode(i, localClass);
                }
            }
            this.code = i;
        }
        return this.code;
    }

    private static int addHashCode(int paramInt, Object paramObject) {
        paramInt *= 37;
        return paramObject != null ? paramInt + paramObject.hashCode() : paramInt;
    }
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.beans.finder.Signature
 * JD-Core Version:    0.6.2
 */