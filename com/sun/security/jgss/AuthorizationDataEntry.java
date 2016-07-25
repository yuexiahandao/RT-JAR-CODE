/*    */ package com.sun.security.jgss;
/*    */ 
/*    */ import sun.misc.HexDumpEncoder;
/*    */ 
/*    */ public final class AuthorizationDataEntry
/*    */ {
/*    */   private final int type;
/*    */   private final byte[] data;
/*    */ 
/*    */   public AuthorizationDataEntry(int paramInt, byte[] paramArrayOfByte)
/*    */   {
/* 43 */     this.type = paramInt;
/* 44 */     this.data = ((byte[])paramArrayOfByte.clone());
/*    */   }
/*    */ 
/*    */   public int getType()
/*    */   {
/* 52 */     return this.type;
/*    */   }
/*    */ 
/*    */   public byte[] getData()
/*    */   {
/* 60 */     return (byte[])this.data.clone();
/*    */   }
/*    */ 
/*    */   public String toString() {
/* 64 */     return "AuthorizationDataEntry: type=" + this.type + ", data=" + this.data.length + " bytes:\n" + new HexDumpEncoder().encodeBuffer(this.data);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.security.jgss.AuthorizationDataEntry
 * JD-Core Version:    0.6.2
 */