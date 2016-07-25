/*    */ package org.jcp.xml.dsig.internal;
/*    */ 
/*    */ import java.io.ByteArrayOutputStream;
/*    */ import javax.crypto.Mac;
/*    */ 
/*    */ public class MacOutputStream extends ByteArrayOutputStream
/*    */ {
/*    */   private final Mac mac;
/*    */ 
/*    */   public MacOutputStream(Mac paramMac)
/*    */   {
/* 38 */     this.mac = paramMac;
/*    */   }
/*    */ 
/*    */   public void write(byte[] paramArrayOfByte)
/*    */   {
/* 43 */     super.write(paramArrayOfByte, 0, paramArrayOfByte.length);
/* 44 */     this.mac.update(paramArrayOfByte);
/*    */   }
/*    */ 
/*    */   public void write(int paramInt)
/*    */   {
/* 49 */     super.write(paramInt);
/* 50 */     this.mac.update((byte)paramInt);
/*    */   }
/*    */ 
/*    */   public void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*    */   {
/* 55 */     super.write(paramArrayOfByte, paramInt1, paramInt2);
/* 56 */     this.mac.update(paramArrayOfByte, paramInt1, paramInt2);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.jcp.xml.dsig.internal.MacOutputStream
 * JD-Core Version:    0.6.2
 */