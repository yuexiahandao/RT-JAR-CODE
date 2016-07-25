/*    */ package sun.net.www.http;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.security.AccessController;
/*    */ import java.security.PrivilegedAction;
/*    */ import java.util.LinkedList;
/*    */ import sun.net.NetProperties;
/*    */ 
/*    */ class KeepAliveStreamCleaner extends LinkedList<KeepAliveCleanerEntry>
/*    */   implements Runnable
/*    */ {
/* 51 */   protected static int MAX_DATA_REMAINING = 512;
/*    */ 
/* 77 */   protected static int MAX_CAPACITY = j;
/*    */   protected static final int TIMEOUT = 5000;
/*    */   private static final int MAX_RETRIES = 5;
/*    */ 
/*    */   public boolean offer(KeepAliveCleanerEntry paramKeepAliveCleanerEntry)
/*    */   {
/* 84 */     if (size() >= MAX_CAPACITY) {
/* 85 */       return false;
/*    */     }
/* 87 */     return super.offer(paramKeepAliveCleanerEntry);
/*    */   }
/*    */ 
/*    */   public void run()
/*    */   {
/* 93 */     KeepAliveCleanerEntry localKeepAliveCleanerEntry = null;
/*    */     do
/*    */       try
/*    */       {
/* 97 */         synchronized (this) {
/* 98 */           long l1 = System.currentTimeMillis();
/* 99 */           long l2 = 5000L;
/* 100 */           while ((localKeepAliveCleanerEntry = (KeepAliveCleanerEntry)poll()) == null) {
/* 101 */             wait(l2);
/*    */ 
/* 103 */             long l4 = System.currentTimeMillis();
/* 104 */             long l6 = l4 - l1;
/* 105 */             if (l6 > l2)
/*    */             {
/* 107 */               localKeepAliveCleanerEntry = (KeepAliveCleanerEntry)poll();
/* 108 */               break;
/*    */             }
/* 110 */             l1 = l4;
/* 111 */             l2 -= l6;
/*    */           }
/*    */         }
/*    */ 
/* 115 */         if (localKeepAliveCleanerEntry == null) {
/*    */           break;
/*    */         }
/* 118 */         ??? = localKeepAliveCleanerEntry.getKeepAliveStream();
/*    */ 
/* 120 */         if (??? != null)
/* 121 */           synchronized (???) {
/* 122 */             HttpClient localHttpClient = localKeepAliveCleanerEntry.getHttpClient();
/*    */             try {
/* 124 */               if ((localHttpClient != null) && (!localHttpClient.isInKeepAliveCache())) {
/* 125 */                 int i = localHttpClient.getReadTimeout();
/* 126 */                 localHttpClient.setReadTimeout(5000);
/* 127 */                 long l3 = ((KeepAliveStream)???).remainingToRead();
/* 128 */                 if (l3 > 0L) {
/* 129 */                   long l5 = 0L;
/* 130 */                   int j = 0;
/* 131 */                   while ((l5 < l3) && (j < 5)) {
/* 132 */                     l3 -= l5;
/* 133 */                     l5 = ((KeepAliveStream)???).skip(l3);
/* 134 */                     if (l5 == 0L)
/* 135 */                       j++;
/*    */                   }
/* 137 */                   l3 -= l5;
/*    */                 }
/* 139 */                 if (l3 == 0L) {
/* 140 */                   localHttpClient.setReadTimeout(i);
/* 141 */                   localHttpClient.finished();
/*    */                 } else {
/* 143 */                   localHttpClient.closeServer();
/*    */                 }
/*    */               }
/*    */             } catch (IOException localIOException) { localHttpClient.closeServer();
/*    */             } finally {
/* 148 */               ((KeepAliveStream)???).setClosed();
/*    */             }
/*    */           }
/*    */       } catch (InterruptedException localInterruptedException) {
/*    */       }
/* 153 */     while (localKeepAliveCleanerEntry != null);
/*    */   }
/*    */ 
/*    */   static
/*    */   {
/* 64 */     int i = ((Integer)AccessController.doPrivileged(new PrivilegedAction()
/*    */     {
/*    */       public Integer run() {
/* 67 */         return NetProperties.getInteger("http.KeepAlive.remainingData", KeepAliveStreamCleaner.MAX_DATA_REMAINING);
/*    */       }
/*    */     })).intValue() * 1024;
/*    */ 
/* 69 */     MAX_DATA_REMAINING = i;
/*    */ 
/* 72 */     int j = ((Integer)AccessController.doPrivileged(new PrivilegedAction()
/*    */     {
/*    */       public Integer run() {
/* 75 */         return NetProperties.getInteger("http.KeepAlive.queuedConnections", KeepAliveStreamCleaner.MAX_CAPACITY);
/*    */       }
/*    */     })).intValue();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.www.http.KeepAliveStreamCleaner
 * JD-Core Version:    0.6.2
 */