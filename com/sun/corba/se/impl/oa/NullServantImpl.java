/*    */ package com.sun.corba.se.impl.oa;
/*    */ 
/*    */ import com.sun.corba.se.spi.oa.NullServant;
/*    */ import org.omg.CORBA.SystemException;
/*    */ 
/*    */ public class NullServantImpl
/*    */   implements NullServant
/*    */ {
/*    */   private SystemException sysex;
/*    */ 
/*    */   public NullServantImpl(SystemException paramSystemException)
/*    */   {
/* 38 */     this.sysex = paramSystemException;
/*    */   }
/*    */ 
/*    */   public SystemException getException()
/*    */   {
/* 43 */     return this.sysex;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.oa.NullServantImpl
 * JD-Core Version:    0.6.2
 */