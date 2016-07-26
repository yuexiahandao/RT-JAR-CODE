/*     */
package java.lang;
/*     */ 
/*     */

import java.io.IOException;
/*     */ import java.io.InvalidObjectException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectStreamException;
/*     */ import java.io.Serializable;
/*     */ import java.util.Map;

/*     */
/*     */ public abstract class Enum<E extends Enum<E>>
/*     */ implements Comparable<E>, Serializable
/*     */ {
    /*     */   private final String name;
    /*     */   private final int ordinal;

    /*     */
/*     */
    public final String name()
/*     */ {
/*  77 */
        return this.name;
/*     */
    }

    /*     */
/*     */
    public final int ordinal()
/*     */ {
/* 103 */
        return this.ordinal;
/*     */
    }

    /*     */
/*     */
    protected Enum(String paramString, int paramInt)
/*     */ {
/* 118 */
        this.name = paramString;
/* 119 */
        this.ordinal = paramInt;
/*     */
    }

    /*     */
/*     */
    public String toString()
/*     */ {
/* 131 */
        return this.name;
/*     */
    }

    /*     */
/*     */
    public final boolean equals(Object paramObject)
/*     */ {
/* 143 */
        return this == paramObject;
/*     */
    }

    /*     */
/*     */
    public final int hashCode()
/*     */ {
/* 152 */
        return super.hashCode();
/*     */
    }

    /*     */
/*     */
    protected final Object clone()
/*     */     throws CloneNotSupportedException
/*     */ {
/* 163 */
        throw new CloneNotSupportedException();
/*     */
    }

    /*     */
/*     */
    public final int compareTo(E paramE)
/*     */ {
/* 176 */
        E ? = paramE;
/* 177 */
        Enum localEnum = this;
/* 178 */
        if ((localEnum.getClass() != ?.getClass())&&(localEnum.getDeclaringClass() != ?.getDeclaringClass()))
/*     */
        {
/* 180 */
            throw new ClassCastException();
/* 181 */
        }
        return localEnum.ordinal - ?.ordinal;
/*     */
    }

    /*     */
/*     */
    public final Class<E> getDeclaringClass()
/*     */ {
/* 197 */
        Class localClass1 = getClass();
/* 198 */
        Class localClass2 = localClass1.getSuperclass();
/* 199 */
        return localClass2 == Enum.class ? localClass1 : localClass2;
/*     */
    }

    /*     */
/*     */
    public static <T extends Enum<T>> T valueOf(Class<T> paramClass, String paramString)
/*     */ {
/* 231 */
        Enum localEnum = (Enum) paramClass.enumConstantDirectory().get(paramString);
/* 232 */
        if (localEnum != null)
/* 233 */ return localEnum;
/* 234 */
        if (paramString == null)
/* 235 */ throw new NullPointerException("Name is null");
/* 236 */
        throw new IllegalArgumentException("No enum constant " + paramClass.getCanonicalName() + "." + paramString);
/*     */
    }

    /*     */
/*     */
    protected final void finalize()
/*     */ {
/*     */
    }

    /*     */
/*     */
    private void readObject(ObjectInputStream paramObjectInputStream)
/*     */     throws IOException, ClassNotFoundException
/*     */ {
/* 250 */
        throw new InvalidObjectException("can't deserialize enum");
/*     */
    }

    /*     */
/*     */
    private void readObjectNoData() throws ObjectStreamException {
/* 254 */
        throw new InvalidObjectException("can't deserialize enum");
/*     */
    }
/*     */
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.Enum
 * JD-Core Version:    0.6.2
 */