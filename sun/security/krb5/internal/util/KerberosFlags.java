/*     */ package sun.security.krb5.internal.util;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Arrays;
/*     */ import sun.security.util.BitArray;
/*     */ import sun.security.util.DerOutputStream;
/*     */ 
/*     */ public class KerberosFlags
/*     */ {
/*     */   BitArray bits;
/*     */   protected static final int BITS_PER_UNIT = 8;
/*     */ 
/*     */   public KerberosFlags(int paramInt)
/*     */     throws IllegalArgumentException
/*     */   {
/*  64 */     this.bits = new BitArray(paramInt);
/*     */   }
/*     */ 
/*     */   public KerberosFlags(int paramInt, byte[] paramArrayOfByte) throws IllegalArgumentException {
/*  68 */     this.bits = new BitArray(paramInt, paramArrayOfByte);
/*  69 */     if (paramInt != 32)
/*  70 */       this.bits = new BitArray(Arrays.copyOf(this.bits.toBooleanArray(), 32));
/*     */   }
/*     */ 
/*     */   public KerberosFlags(boolean[] paramArrayOfBoolean)
/*     */   {
/*  75 */     this.bits = new BitArray(paramArrayOfBoolean.length == 32 ? paramArrayOfBoolean : Arrays.copyOf(paramArrayOfBoolean, 32));
/*     */   }
/*     */ 
/*     */   public void set(int paramInt, boolean paramBoolean)
/*     */   {
/*  81 */     this.bits.set(paramInt, paramBoolean);
/*     */   }
/*     */ 
/*     */   public boolean get(int paramInt) {
/*  85 */     return this.bits.get(paramInt);
/*     */   }
/*     */ 
/*     */   public boolean[] toBooleanArray() {
/*  89 */     return this.bits.toBooleanArray();
/*     */   }
/*     */ 
/*     */   public byte[] asn1Encode()
/*     */     throws IOException
/*     */   {
/*  99 */     DerOutputStream localDerOutputStream = new DerOutputStream();
/* 100 */     localDerOutputStream.putUnalignedBitString(this.bits);
/* 101 */     return localDerOutputStream.toByteArray();
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 105 */     return this.bits.toString();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.krb5.internal.util.KerberosFlags
 * JD-Core Version:    0.6.2
 */