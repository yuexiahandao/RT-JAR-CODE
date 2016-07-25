/*    */ package org.omg.IOP;
/*    */ 
/*    */ import org.omg.CORBA.portable.IDLEntity;
/*    */ 
/*    */ public final class Encoding
/*    */   implements IDLEntity
/*    */ {
/* 17 */   public short format = 0;
/*    */ 
/* 22 */   public byte major_version = 0;
/*    */ 
/* 27 */   public byte minor_version = 0;
/*    */ 
/*    */   public Encoding()
/*    */   {
/*    */   }
/*    */ 
/*    */   public Encoding(short paramShort, byte paramByte1, byte paramByte2)
/*    */   {
/* 35 */     this.format = paramShort;
/* 36 */     this.major_version = paramByte1;
/* 37 */     this.minor_version = paramByte2;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.IOP.Encoding
 * JD-Core Version:    0.6.2
 */