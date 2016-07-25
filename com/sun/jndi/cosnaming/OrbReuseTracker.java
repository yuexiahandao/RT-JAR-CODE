/*    */ package com.sun.jndi.cosnaming;
/*    */ 
/*    */ import org.omg.CORBA.ORB;
/*    */ 
/*    */ class OrbReuseTracker
/*    */ {
/*    */   int referenceCnt;
/*    */   ORB orb;
/*    */   private static final boolean debug = false;
/*    */ 
/*    */   OrbReuseTracker(ORB paramORB)
/*    */   {
/* 44 */     this.orb = paramORB;
/* 45 */     this.referenceCnt += 1;
/*    */   }
/*    */ 
/*    */   synchronized void incRefCount()
/*    */   {
/* 52 */     this.referenceCnt += 1;
/*    */   }
/*    */ 
/*    */   synchronized void decRefCount()
/*    */   {
/* 59 */     this.referenceCnt -= 1;
/*    */ 
/* 63 */     if (this.referenceCnt == 0)
/*    */     {
/* 67 */       this.orb.destroy();
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jndi.cosnaming.OrbReuseTracker
 * JD-Core Version:    0.6.2
 */