/*    */ package sun.security.krb5.internal.rcache;
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ import java.util.Hashtable;
/*    */ import sun.security.krb5.internal.Krb5;
/*    */ 
/*    */ public class CacheTable extends Hashtable<String, ReplayCache>
/*    */ {
/*    */   private static final long serialVersionUID = -4695501354546664910L;
/* 46 */   private boolean DEBUG = Krb5.DEBUG;
/*    */ 
/*    */   public synchronized void put(String paramString, AuthTime paramAuthTime, long paramLong)
/*    */   {
/* 56 */     ReplayCache localReplayCache = (ReplayCache)super.get(paramString);
/* 57 */     if (localReplayCache == null) {
/* 58 */       if (this.DEBUG) {
/* 59 */         System.out.println("replay cache for " + paramString + " is null.");
/*    */       }
/* 61 */       localReplayCache = new ReplayCache(paramString, this);
/* 62 */       localReplayCache.put(paramAuthTime, paramLong);
/* 63 */       super.put(paramString, localReplayCache);
/*    */     }
/*    */     else {
/* 66 */       localReplayCache.put(paramAuthTime, paramLong);
/*    */ 
/* 68 */       super.put(paramString, localReplayCache);
/* 69 */       if (this.DEBUG)
/* 70 */         System.out.println("replay cache found.");
/*    */     }
/*    */   }
/*    */ 
/*    */   public Object get(AuthTime paramAuthTime, String paramString)
/*    */   {
/* 84 */     ReplayCache localReplayCache = (ReplayCache)super.get(paramString);
/* 85 */     if ((localReplayCache != null) && (localReplayCache.contains(paramAuthTime))) {
/* 86 */       return paramAuthTime;
/*    */     }
/* 88 */     return null;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.krb5.internal.rcache.CacheTable
 * JD-Core Version:    0.6.2
 */