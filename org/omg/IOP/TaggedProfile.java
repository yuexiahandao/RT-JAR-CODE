/*    */ package org.omg.IOP;
/*    */ 
/*    */ import org.omg.CORBA.portable.IDLEntity;
/*    */ 
/*    */ public final class TaggedProfile
/*    */   implements IDLEntity
/*    */ {
/* 15 */   public int tag = 0;
/*    */ 
/* 18 */   public byte[] profile_data = null;
/*    */ 
/*    */   public TaggedProfile()
/*    */   {
/*    */   }
/*    */ 
/*    */   public TaggedProfile(int paramInt, byte[] paramArrayOfByte)
/*    */   {
/* 26 */     this.tag = paramInt;
/* 27 */     this.profile_data = paramArrayOfByte;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.IOP.TaggedProfile
 * JD-Core Version:    0.6.2
 */