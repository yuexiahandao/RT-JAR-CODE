/*     */ package javax.smartcardio;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.Arrays;
/*     */ 
/*     */ public final class ATR
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 6695383790847736493L;
/*     */   private byte[] atr;
/*     */   private transient int startHistorical;
/*     */   private transient int nHistorical;
/*     */ 
/*     */   public ATR(byte[] paramArrayOfByte)
/*     */   {
/*  60 */     this.atr = ((byte[])paramArrayOfByte.clone());
/*  61 */     parse();
/*     */   }
/*     */ 
/*     */   private void parse() {
/*  65 */     if (this.atr.length < 2) {
/*  66 */       return;
/*     */     }
/*  68 */     if ((this.atr[0] != 59) && (this.atr[0] != 63)) {
/*  69 */       return;
/*     */     }
/*  71 */     int i = (this.atr[1] & 0xF0) >> 4;
/*  72 */     int j = this.atr[1] & 0xF;
/*  73 */     int k = 2;
/*  74 */     while ((i != 0) && (k < this.atr.length)) {
/*  75 */       if ((i & 0x1) != 0) {
/*  76 */         k++;
/*     */       }
/*  78 */       if ((i & 0x2) != 0) {
/*  79 */         k++;
/*     */       }
/*  81 */       if ((i & 0x4) != 0) {
/*  82 */         k++;
/*     */       }
/*  84 */       if ((i & 0x8) != 0) {
/*  85 */         if (k >= this.atr.length) {
/*  86 */           return;
/*     */         }
/*  88 */         i = (this.atr[(k++)] & 0xF0) >> 4;
/*     */       } else {
/*  90 */         i = 0;
/*     */       }
/*     */     }
/*  93 */     int m = k + j;
/*  94 */     if ((m == this.atr.length) || (m == this.atr.length - 1)) {
/*  95 */       this.startHistorical = k;
/*  96 */       this.nHistorical = j;
/*     */     }
/*     */   }
/*     */ 
/*     */   public byte[] getBytes()
/*     */   {
/* 106 */     return (byte[])this.atr.clone();
/*     */   }
/*     */ 
/*     */   public byte[] getHistoricalBytes()
/*     */   {
/* 117 */     byte[] arrayOfByte = new byte[this.nHistorical];
/* 118 */     System.arraycopy(this.atr, this.startHistorical, arrayOfByte, 0, this.nHistorical);
/* 119 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 128 */     return "ATR: " + this.atr.length + " bytes";
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 140 */     if (this == paramObject) {
/* 141 */       return true;
/*     */     }
/* 143 */     if (!(paramObject instanceof ATR)) {
/* 144 */       return false;
/*     */     }
/* 146 */     ATR localATR = (ATR)paramObject;
/* 147 */     return Arrays.equals(this.atr, localATR.atr);
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 156 */     return Arrays.hashCode(this.atr);
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream) throws IOException, ClassNotFoundException
/*     */   {
/* 161 */     this.atr = ((byte[])paramObjectInputStream.readUnshared());
/* 162 */     parse();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.smartcardio.ATR
 * JD-Core Version:    0.6.2
 */