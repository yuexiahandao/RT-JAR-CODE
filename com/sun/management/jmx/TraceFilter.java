/*    */ package com.sun.management.jmx;
/*    */ 
/*    */ import javax.management.Notification;
/*    */ import javax.management.NotificationFilter;
/*    */ 
/*    */ @Deprecated
/*    */ public class TraceFilter
/*    */   implements NotificationFilter
/*    */ {
/*    */   protected int levels;
/*    */   protected int types;
/*    */ 
/*    */   public TraceFilter(int paramInt1, int paramInt2)
/*    */     throws IllegalArgumentException
/*    */   {
/* 39 */     this.levels = paramInt1;
/* 40 */     this.types = paramInt2;
/*    */   }
/*    */ 
/*    */   public boolean isNotificationEnabled(Notification paramNotification)
/*    */   {
/* 53 */     return false;
/*    */   }
/*    */ 
/*    */   public int getLevels()
/*    */   {
/* 62 */     return this.levels;
/*    */   }
/*    */ 
/*    */   public int getTypes()
/*    */   {
/* 71 */     return this.types;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.management.jmx.TraceFilter
 * JD-Core Version:    0.6.2
 */