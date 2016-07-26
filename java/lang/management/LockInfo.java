/*     */
package java.lang.management;
/*     */ 
/*     */

import java.beans.ConstructorProperties;

/*     */
/*     */ public class LockInfo
/*     */ {
    /*     */   private String className;
    /*     */   private int identityHashCode;

    /*     */
/*     */
    @ConstructorProperties({"className", "identityHashCode"})
/*     */ public LockInfo(String paramString, int paramInt)
/*     */ {
/*  71 */
        if (paramString == null) {
/*  72 */
            throw new NullPointerException("Parameter className cannot be null");
/*     */
        }
/*  74 */
        this.className = paramString;
/*  75 */
        this.identityHashCode = paramInt;
/*     */
    }

    /*     */
/*     */   LockInfo(Object paramObject)
/*     */ {
/*  82 */
        this.className = paramObject.getClass().getName();
/*  83 */
        this.identityHashCode = System.identityHashCode(paramObject);
/*     */
    }

    /*     */
/*     */
    public String getClassName()
/*     */ {
/*  92 */
        return this.className;
/*     */
    }

    /*     */
/*     */
    public int getIdentityHashCode()
/*     */ {
/* 102 */
        return this.identityHashCode;
/*     */
    }

    /*     */
/*     */
    public String toString()
/*     */ {
/* 120 */
        return this.className + '@' + Integer.toHexString(this.identityHashCode);
/*     */
    }
/*     */
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.management.LockInfo
 * JD-Core Version:    0.6.2
 */