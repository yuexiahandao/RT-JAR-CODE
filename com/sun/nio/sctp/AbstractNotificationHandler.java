/*     */ package com.sun.nio.sctp;
/*     */ 
/*     */ public class AbstractNotificationHandler<T>
/*     */   implements NotificationHandler<T>
/*     */ {
/*     */   public HandlerResult handleNotification(Notification paramNotification, T paramT)
/*     */   {
/*  66 */     return HandlerResult.CONTINUE;
/*     */   }
/*     */ 
/*     */   public HandlerResult handleNotification(AssociationChangeNotification paramAssociationChangeNotification, T paramT)
/*     */   {
/*  84 */     return HandlerResult.CONTINUE;
/*     */   }
/*     */ 
/*     */   public HandlerResult handleNotification(PeerAddressChangeNotification paramPeerAddressChangeNotification, T paramT)
/*     */   {
/* 102 */     return HandlerResult.CONTINUE;
/*     */   }
/*     */ 
/*     */   public HandlerResult handleNotification(SendFailedNotification paramSendFailedNotification, T paramT)
/*     */   {
/* 120 */     return HandlerResult.CONTINUE;
/*     */   }
/*     */ 
/*     */   public HandlerResult handleNotification(ShutdownNotification paramShutdownNotification, T paramT)
/*     */   {
/* 138 */     return HandlerResult.CONTINUE;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.nio.sctp.AbstractNotificationHandler
 * JD-Core Version:    0.6.2
 */