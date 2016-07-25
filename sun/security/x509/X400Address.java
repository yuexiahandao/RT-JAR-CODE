/*     */ package sun.security.x509;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import sun.security.util.DerOutputStream;
/*     */ import sun.security.util.DerValue;
/*     */ 
/*     */ public class X400Address
/*     */   implements GeneralNameInterface
/*     */ {
/* 338 */   byte[] nameValue = null;
/*     */ 
/*     */   public X400Address(byte[] paramArrayOfByte)
/*     */   {
/* 346 */     this.nameValue = paramArrayOfByte;
/*     */   }
/*     */ 
/*     */   public X400Address(DerValue paramDerValue)
/*     */     throws IOException
/*     */   {
/* 356 */     this.nameValue = paramDerValue.toByteArray();
/*     */   }
/*     */ 
/*     */   public int getType()
/*     */   {
/* 363 */     return 3;
/*     */   }
/*     */ 
/*     */   public void encode(DerOutputStream paramDerOutputStream)
/*     */     throws IOException
/*     */   {
/* 373 */     DerValue localDerValue = new DerValue(this.nameValue);
/* 374 */     paramDerOutputStream.putDerValue(localDerValue);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 381 */     return "X400Address: <DER-encoded value>";
/*     */   }
/*     */ 
/*     */   public int constrains(GeneralNameInterface paramGeneralNameInterface)
/*     */     throws UnsupportedOperationException
/*     */   {
/*     */     int i;
/* 401 */     if (paramGeneralNameInterface == null)
/* 402 */       i = -1;
/* 403 */     else if (paramGeneralNameInterface.getType() != 3) {
/* 404 */       i = -1;
/*     */     }
/*     */     else
/* 407 */       throw new UnsupportedOperationException("Narrowing, widening, and match are not supported for X400Address.");
/* 408 */     return i;
/*     */   }
/*     */ 
/*     */   public int subtreeDepth()
/*     */     throws UnsupportedOperationException
/*     */   {
/* 420 */     throw new UnsupportedOperationException("subtreeDepth not supported for X400Address");
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.x509.X400Address
 * JD-Core Version:    0.6.2
 */