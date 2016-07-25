/*    */ package org.omg.IOP;
/*    */ 
/*    */ import org.omg.CORBA.portable.IDLEntity;
/*    */ 
/*    */ public final class TaggedComponent
/*    */   implements IDLEntity
/*    */ {
/* 15 */   public int tag = 0;
/*    */ 
/* 18 */   public byte[] component_data = null;
/*    */ 
/*    */   public TaggedComponent()
/*    */   {
/*    */   }
/*    */ 
/*    */   public TaggedComponent(int paramInt, byte[] paramArrayOfByte)
/*    */   {
/* 26 */     this.tag = paramInt;
/* 27 */     this.component_data = paramArrayOfByte;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.IOP.TaggedComponent
 * JD-Core Version:    0.6.2
 */