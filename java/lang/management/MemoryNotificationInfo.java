/*     */
package java.lang.management;
/*     */ 
/*     */

import javax.management.openmbean.CompositeData;
/*     */ import sun.management.MemoryNotifInfoCompositeData;

/*     */
/*     */ public class MemoryNotificationInfo
/*     */ {
    /*     */   private final String poolName;
    /*     */   private final MemoryUsage usage;
    /*     */   private final long count;
    /*     */   public static final String MEMORY_THRESHOLD_EXCEEDED = "java.management.memory.threshold.exceeded";
    /*     */   public static final String MEMORY_COLLECTION_THRESHOLD_EXCEEDED = "java.management.memory.collection.threshold.exceeded";

    /*     */
/*     */
    public MemoryNotificationInfo(String paramString, MemoryUsage paramMemoryUsage, long paramLong)
/*     */ {
/* 151 */
        if (paramString == null) {
/* 152 */
            throw new NullPointerException("Null poolName");
/*     */
        }
/* 154 */
        if (paramMemoryUsage == null) {
/* 155 */
            throw new NullPointerException("Null usage");
/*     */
        }
/*     */ 
/* 158 */
        this.poolName = paramString;
/* 159 */
        this.usage = paramMemoryUsage;
/* 160 */
        this.count = paramLong;
/*     */
    }

    /*     */
/*     */   MemoryNotificationInfo(CompositeData paramCompositeData) {
/* 164 */
        MemoryNotifInfoCompositeData.validateCompositeData(paramCompositeData);
/*     */ 
/* 166 */
        this.poolName = MemoryNotifInfoCompositeData.getPoolName(paramCompositeData);
/* 167 */
        this.usage = MemoryNotifInfoCompositeData.getUsage(paramCompositeData);
/* 168 */
        this.count = MemoryNotifInfoCompositeData.getCount(paramCompositeData);
/*     */
    }

    /*     */
/*     */
    public String getPoolName()
/*     */ {
/* 178 */
        return this.poolName;
/*     */
    }

    /*     */
/*     */
    public MemoryUsage getUsage()
/*     */ {
/* 189 */
        return this.usage;
/*     */
    }

    /*     */
/*     */
    public long getCount()
/*     */ {
/* 206 */
        return this.count;
/*     */
    }

    /*     */
/*     */
    public static MemoryNotificationInfo from(CompositeData paramCompositeData)
/*     */ {
/* 246 */
        if (paramCompositeData == null) {
/* 247 */
            return null;
/*     */
        }
/*     */ 
/* 250 */
        if ((paramCompositeData instanceof MemoryNotifInfoCompositeData)) {
/* 251 */
            return ((MemoryNotifInfoCompositeData) paramCompositeData).getMemoryNotifInfo();
/*     */
        }
/* 253 */
        return new MemoryNotificationInfo(paramCompositeData);
/*     */
    }
/*     */
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.management.MemoryNotificationInfo
 * JD-Core Version:    0.6.2
 */