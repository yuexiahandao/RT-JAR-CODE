/*     */
package java.lang;
/*     */ 
/*     */

import java.io.Serializable;

/*     */
/*     */ public final class Boolean
/*     */ implements Serializable, Comparable<Boolean>
/*     */ {
    /*  50 */   public static final Boolean TRUE = new Boolean(true);
    /*     */
/*  56 */   public static final Boolean FALSE = new Boolean(false);
    /*     */
/*  63 */   public static final Class<Boolean> TYPE = Class.getPrimitiveClass("boolean");
    /*     */   private final boolean value;
    /*     */   private static final long serialVersionUID = -3665804199014368530L;

    /*     */
/*     */
    public Boolean(boolean paramBoolean)
/*     */ {
/*  87 */
        this.value = paramBoolean;
/*     */
    }

    /*     */
/*     */
    public Boolean(String paramString)
/*     */ {
/* 104 */
        this(toBoolean(paramString));
/*     */
    }

    /*     */
/*     */
    public static boolean parseBoolean(String paramString)
/*     */ {
/* 121 */
        return toBoolean(paramString);
/*     */
    }

    /*     */
/*     */
    public boolean booleanValue()
/*     */ {
/* 131 */
        return this.value;
/*     */
    }

    /*     */
/*     */
    public static Boolean valueOf(boolean paramBoolean)
/*     */ {
/* 149 */
        return paramBoolean ? TRUE : FALSE;
/*     */
    }

    /*     */
/*     */
    public static Boolean valueOf(String paramString)
/*     */ {
/* 162 */
        return toBoolean(paramString) ? TRUE : FALSE;
/*     */
    }

    /*     */
/*     */
    public static String toString(boolean paramBoolean)
/*     */ {
/* 176 */
        return paramBoolean ? "true" : "false";
/*     */
    }

    /*     */
/*     */
    public String toString()
/*     */ {
/* 188 */
        return this.value ? "true" : "false";
/*     */
    }

    /*     */
/*     */
    public int hashCode()
/*     */ {
/* 199 */
        return this.value ? 1231 : 1237;
/*     */
    }

    /*     */
/*     */
    public boolean equals(Object paramObject)
/*     */ {
/* 212 */
        if ((paramObject instanceof Boolean)) {
/* 213 */
            return this.value == ((Boolean) paramObject).booleanValue();
/*     */
        }
/* 215 */
        return false;
/*     */
    }

    /*     */
/*     */
    public static boolean getBoolean(String paramString)
/*     */ {
/* 236 */
        boolean bool = false;
/*     */
        try {
/* 238 */
            bool = toBoolean(System.getProperty(paramString));
/*     */
        } catch (IllegalArgumentException localIllegalArgumentException) {
/*     */
        } catch (NullPointerException localNullPointerException) {
/*     */
        }
/* 242 */
        return bool;
/*     */
    }

    /*     */
/*     */
    public int compareTo(Boolean paramBoolean)
/*     */ {
/* 258 */
        return compare(this.value, paramBoolean.value);
/*     */
    }

    /*     */
/*     */
    public static int compare(boolean paramBoolean1, boolean paramBoolean2)
/*     */ {
/* 276 */
        return paramBoolean1 ? 1 : paramBoolean1 == paramBoolean2 ? 0 : -1;
/*     */
    }

    /*     */
/*     */
    private static boolean toBoolean(String paramString) {
/* 280 */
        return (paramString != null) && (paramString.equalsIgnoreCase("true"));
/*     */
    }
/*     */
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.Boolean
 * JD-Core Version:    0.6.2
 */