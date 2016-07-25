/*     */ package javax.management.remote;
/*     */ 
/*     */ import javax.management.Notification;
/*     */ 
/*     */ public class JMXConnectionNotification extends Notification
/*     */ {
/*     */   private static final long serialVersionUID = -2331308725952627538L;
/*     */   public static final String OPENED = "jmx.remote.connection.opened";
/*     */   public static final String CLOSED = "jmx.remote.connection.closed";
/*     */   public static final String FAILED = "jmx.remote.connection.failed";
/*     */   public static final String NOTIFS_LOST = "jmx.remote.connection.notifs.lost";
/*     */   private final String connectionId;
/*     */ 
/*     */   public JMXConnectionNotification(String paramString1, Object paramObject1, String paramString2, long paramLong, String paramString3, Object paramObject2)
/*     */   {
/* 163 */     super((String)nonNull(paramString1), nonNull(paramObject1), Math.max(0L, paramLong), System.currentTimeMillis(), paramString3);
/*     */ 
/* 168 */     if ((paramString1 == null) || (paramObject1 == null) || (paramString2 == null))
/* 169 */       throw new NullPointerException("Illegal null argument");
/* 170 */     if (paramLong < 0L)
/* 171 */       throw new IllegalArgumentException("Negative sequence number");
/* 172 */     this.connectionId = paramString2;
/* 173 */     setUserData(paramObject2);
/*     */   }
/*     */ 
/*     */   private static Object nonNull(Object paramObject) {
/* 177 */     if (paramObject == null) {
/* 178 */       return "";
/*     */     }
/* 180 */     return paramObject;
/*     */   }
/*     */ 
/*     */   public String getConnectionId()
/*     */   {
/* 189 */     return this.connectionId;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.remote.JMXConnectionNotification
 * JD-Core Version:    0.6.2
 */