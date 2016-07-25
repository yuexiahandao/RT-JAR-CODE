/*    */ package com.sun.corba.se.impl.transport;
/*    */ 
/*    */ import com.sun.corba.se.spi.transport.ReadTimeouts;
/*    */ 
/*    */ public class ReadTCPTimeoutsImpl
/*    */   implements ReadTimeouts
/*    */ {
/*    */   private int initial_time_to_wait;
/*    */   private int max_time_to_wait;
/*    */   private int max_giop_header_time_to_wait;
/*    */   private double backoff_factor;
/*    */ 
/*    */   public ReadTCPTimeoutsImpl(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*    */   {
/* 45 */     this.initial_time_to_wait = paramInt1;
/* 46 */     this.max_time_to_wait = paramInt2;
/* 47 */     this.max_giop_header_time_to_wait = paramInt3;
/* 48 */     this.backoff_factor = (1.0D + paramInt4 / 100.0D);
/*    */   }
/*    */   public int get_initial_time_to_wait() {
/* 51 */     return this.initial_time_to_wait; } 
/* 52 */   public int get_max_time_to_wait() { return this.max_time_to_wait; } 
/* 53 */   public double get_backoff_factor() { return this.backoff_factor; } 
/*    */   public int get_max_giop_header_time_to_wait() {
/* 55 */     return this.max_giop_header_time_to_wait;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.transport.ReadTCPTimeoutsImpl
 * JD-Core Version:    0.6.2
 */