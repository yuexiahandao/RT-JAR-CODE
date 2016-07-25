/*    */ package com.sun.jmx.snmp;
/*    */ 
/*    */ public class SnmpOpaque extends SnmpString
/*    */ {
/*    */   private static final long serialVersionUID = 380952213936036664L;
/*    */   static final String name = "Opaque";
/*    */ 
/*    */   public SnmpOpaque(byte[] paramArrayOfByte)
/*    */   {
/* 49 */     super(paramArrayOfByte);
/*    */   }
/*    */ 
/*    */   public SnmpOpaque(Byte[] paramArrayOfByte)
/*    */   {
/* 57 */     super(paramArrayOfByte);
/*    */   }
/*    */ 
/*    */   public SnmpOpaque(String paramString)
/*    */   {
/* 65 */     super(paramString);
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 76 */     StringBuffer localStringBuffer = new StringBuffer();
/* 77 */     for (int i = 0; i < this.value.length; i++) {
/* 78 */       int j = this.value[i];
/* 79 */       int k = j >= 0 ? j : j + 256;
/* 80 */       localStringBuffer.append(Character.forDigit(k / 16, 16));
/* 81 */       localStringBuffer.append(Character.forDigit(k % 16, 16));
/*    */     }
/* 83 */     return localStringBuffer.toString();
/*    */   }
/*    */ 
/*    */   public final String getTypeName()
/*    */   {
/* 91 */     return "Opaque";
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.SnmpOpaque
 * JD-Core Version:    0.6.2
 */