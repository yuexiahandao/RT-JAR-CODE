/*    */ package com.sun.corba.se.impl.protocol.giopmsgheaders;
/*    */ 
/*    */ import org.omg.CORBA.portable.IDLEntity;
/*    */ import org.omg.IOP.IOR;
/*    */ 
/*    */ public final class IORAddressingInfo
/*    */   implements IDLEntity
/*    */ {
/* 38 */   public int selected_profile_index = 0;
/* 39 */   public IOR ior = null;
/*    */ 
/*    */   public IORAddressingInfo()
/*    */   {
/*    */   }
/*    */ 
/*    */   public IORAddressingInfo(int paramInt, IOR paramIOR)
/*    */   {
/* 47 */     this.selected_profile_index = paramInt;
/* 48 */     this.ior = paramIOR;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.protocol.giopmsgheaders.IORAddressingInfo
 * JD-Core Version:    0.6.2
 */