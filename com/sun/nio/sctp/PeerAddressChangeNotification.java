/*    */ package com.sun.nio.sctp;
/*    */ 
/*    */ import java.net.SocketAddress;
/*    */ 
/*    */ public abstract class PeerAddressChangeNotification
/*    */   implements Notification
/*    */ {
/*    */   public abstract SocketAddress address();
/*    */ 
/*    */   public abstract Association association();
/*    */ 
/*    */   public abstract AddressChangeEvent event();
/*    */ 
/*    */   public static enum AddressChangeEvent
/*    */   {
/* 53 */     ADDR_AVAILABLE, 
/*    */ 
/* 59 */     ADDR_UNREACHABLE, 
/*    */ 
/* 64 */     ADDR_REMOVED, 
/*    */ 
/* 69 */     ADDR_ADDED, 
/*    */ 
/* 74 */     ADDR_MADE_PRIMARY, 
/*    */ 
/* 79 */     ADDR_CONFIRMED;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.nio.sctp.PeerAddressChangeNotification
 * JD-Core Version:    0.6.2
 */