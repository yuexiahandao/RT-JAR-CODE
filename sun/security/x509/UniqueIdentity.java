/*     */ package sun.security.x509;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import sun.security.util.BitArray;
/*     */ import sun.security.util.DerInputStream;
/*     */ import sun.security.util.DerOutputStream;
/*     */ import sun.security.util.DerValue;
/*     */ 
/*     */ public class UniqueIdentity
/*     */ {
/*     */   private BitArray id;
/*     */ 
/*     */   public UniqueIdentity(BitArray paramBitArray)
/*     */   {
/*  49 */     this.id = paramBitArray;
/*     */   }
/*     */ 
/*     */   public UniqueIdentity(byte[] paramArrayOfByte)
/*     */   {
/*  58 */     this.id = new BitArray(paramArrayOfByte.length * 8, paramArrayOfByte);
/*     */   }
/*     */ 
/*     */   public UniqueIdentity(DerInputStream paramDerInputStream)
/*     */     throws IOException
/*     */   {
/*  68 */     DerValue localDerValue = paramDerInputStream.getDerValue();
/*  69 */     this.id = localDerValue.getUnalignedBitString(true);
/*     */   }
/*     */ 
/*     */   public UniqueIdentity(DerValue paramDerValue)
/*     */     throws IOException
/*     */   {
/*  80 */     this.id = paramDerValue.getUnalignedBitString(true);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/*  87 */     return "UniqueIdentity:" + this.id.toString() + "\n";
/*     */   }
/*     */ 
/*     */   public void encode(DerOutputStream paramDerOutputStream, byte paramByte)
/*     */     throws IOException
/*     */   {
/*  98 */     byte[] arrayOfByte = this.id.toByteArray();
/*  99 */     int i = arrayOfByte.length * 8 - this.id.length();
/*     */ 
/* 101 */     paramDerOutputStream.write(paramByte);
/* 102 */     paramDerOutputStream.putLength(arrayOfByte.length + 1);
/*     */ 
/* 104 */     paramDerOutputStream.write(i);
/* 105 */     paramDerOutputStream.write(arrayOfByte);
/*     */   }
/*     */ 
/*     */   public boolean[] getId()
/*     */   {
/* 112 */     if (this.id == null) return null;
/*     */ 
/* 114 */     return this.id.toBooleanArray();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.x509.UniqueIdentity
 * JD-Core Version:    0.6.2
 */