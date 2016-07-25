/*    */ package com.sun.corba.se.impl.ior;
/*    */ 
/*    */ import com.sun.corba.se.impl.logging.IORSystemException;
/*    */ import com.sun.corba.se.spi.ior.ObjectAdapterId;
/*    */ import com.sun.corba.se.spi.orb.ORB;
/*    */ import com.sun.corba.se.spi.orb.ORBVersionFactory;
/*    */ 
/*    */ public abstract class OldObjectKeyTemplateBase extends ObjectKeyTemplateBase
/*    */ {
/*    */   public OldObjectKeyTemplateBase(ORB paramORB, int paramInt1, int paramInt2, int paramInt3, String paramString, ObjectAdapterId paramObjectAdapterId)
/*    */   {
/* 46 */     super(paramORB, paramInt1, paramInt2, paramInt3, paramString, paramObjectAdapterId);
/*    */ 
/* 49 */     if (paramInt1 == -1347695874)
/* 50 */       setORBVersion(ORBVersionFactory.getOLD());
/* 51 */     else if (paramInt1 == -1347695873)
/* 52 */       setORBVersion(ORBVersionFactory.getNEW());
/*    */     else
/* 54 */       throw this.wrapper.badMagic(new Integer(paramInt1));
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.ior.OldObjectKeyTemplateBase
 * JD-Core Version:    0.6.2
 */