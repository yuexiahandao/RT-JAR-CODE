/*     */
package java.lang.management;
/*     */ 
/*     */

import javax.management.openmbean.CompositeData;
/*     */ import sun.management.MemoryUsageCompositeData;

/*     */
/*     */ public class MemoryUsage
/*     */ {
    /*     */   private final long init;
    /*     */   private final long used;
    /*     */   private final long committed;
    /*     */   private final long max;

    /*     */
/*     */
    public MemoryUsage(long paramLong1, long paramLong2, long paramLong3, long paramLong4)
/*     */ {
/* 141 */
        if (paramLong1 < -1L) {
/* 142 */
            throw new IllegalArgumentException("init parameter = " + paramLong1 + " is negative but not -1.");
/*     */
        }
/*     */ 
/* 145 */
        if (paramLong4 < -1L) {
/* 146 */
            throw new IllegalArgumentException("max parameter = " + paramLong4 + " is negative but not -1.");
/*     */
        }
/*     */ 
/* 149 */
        if (paramLong2 < 0L) {
/* 150 */
            throw new IllegalArgumentException("used parameter = " + paramLong2 + " is negative.");
/*     */
        }
/*     */ 
/* 153 */
        if (paramLong3 < 0L) {
/* 154 */
            throw new IllegalArgumentException("committed parameter = " + paramLong3 + " is negative.");
/*     */
        }
/*     */ 
/* 157 */
        if (paramLong2 > paramLong3) {
/* 158 */
            throw new IllegalArgumentException("used = " + paramLong2 + " should be <= committed = " + paramLong3);
/*     */
        }
/*     */ 
/* 161 */
        if ((paramLong4 >= 0L) && (paramLong3 > paramLong4)) {
/* 162 */
            throw new IllegalArgumentException("committed = " + paramLong3 + " should be < max = " + paramLong4);
/*     */
        }
/*     */ 
/* 166 */
        this.init = paramLong1;
/* 167 */
        this.used = paramLong2;
/* 168 */
        this.committed = paramLong3;
/* 169 */
        this.max = paramLong4;
/*     */
    }

    /*     */
/*     */
    private MemoryUsage(CompositeData paramCompositeData)
/*     */ {
/* 178 */
        MemoryUsageCompositeData.validateCompositeData(paramCompositeData);
/*     */ 
/* 180 */
        this.init = MemoryUsageCompositeData.getInit(paramCompositeData);
/* 181 */
        this.used = MemoryUsageCompositeData.getUsed(paramCompositeData);
/* 182 */
        this.committed = MemoryUsageCompositeData.getCommitted(paramCompositeData);
/* 183 */
        this.max = MemoryUsageCompositeData.getMax(paramCompositeData);
/*     */
    }

    /*     */
/*     */
    public long getInit()
/*     */ {
/* 195 */
        return this.init;
/*     */
    }

    /*     */
/*     */
    public long getUsed()
/*     */ {
/* 205 */
        return this.used;
/*     */
    }

    /*     */
/*     */
    public long getCommitted()
/*     */ {
/* 217 */
        return this.committed;
/*     */
    }

    /*     */
/*     */
    public long getMax()
/*     */ {
/* 235 */
        return this.max;
/*     */
    }

    /*     */
/*     */
    public String toString()
/*     */ {
/* 242 */
        StringBuffer localStringBuffer = new StringBuffer();
/* 243 */
        localStringBuffer.append("init = " + this.init + "(" + (this.init >> 10) + "K) ");
/* 244 */
        localStringBuffer.append("used = " + this.used + "(" + (this.used >> 10) + "K) ");
/* 245 */
        localStringBuffer.append("committed = " + this.committed + "(" + (this.committed >> 10) + "K) ");
/*     */ 
/* 247 */
        localStringBuffer.append("max = " + this.max + "(" + (this.max >> 10) + "K)");
/* 248 */
        return localStringBuffer.toString();
/*     */
    }

    /*     */
/*     */
    public static MemoryUsage from(CompositeData paramCompositeData)
/*     */ {
/* 292 */
        if (paramCompositeData == null) {
/* 293 */
            return null;
/*     */
        }
/*     */ 
/* 296 */
        if ((paramCompositeData instanceof MemoryUsageCompositeData)) {
/* 297 */
            return ((MemoryUsageCompositeData) paramCompositeData).getMemoryUsage();
/*     */
        }
/* 299 */
        return new MemoryUsage(paramCompositeData);
/*     */
    }
/*     */
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.management.MemoryUsage
 * JD-Core Version:    0.6.2
 */