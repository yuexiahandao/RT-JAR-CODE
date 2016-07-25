/*     */ package javax.naming;
/*     */ 
/*     */ public class BinaryRefAddr extends RefAddr
/*     */ {
/*  70 */   private byte[] buf = null;
/*     */   private static final long serialVersionUID = -3415254970957330361L;
/*     */ 
/*     */   public BinaryRefAddr(String paramString, byte[] paramArrayOfByte)
/*     */   {
/*  81 */     this(paramString, paramArrayOfByte, 0, paramArrayOfByte.length);
/*     */   }
/*     */ 
/*     */   public BinaryRefAddr(String paramString, byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */   {
/*  97 */     super(paramString);
/*  98 */     this.buf = new byte[paramInt2];
/*  99 */     System.arraycopy(paramArrayOfByte, paramInt1, this.buf, 0, paramInt2);
/*     */   }
/*     */ 
/*     */   public Object getContent()
/*     */   {
/* 112 */     return this.buf;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 124 */     if ((paramObject != null) && ((paramObject instanceof BinaryRefAddr))) {
/* 125 */       BinaryRefAddr localBinaryRefAddr = (BinaryRefAddr)paramObject;
/* 126 */       if (this.addrType.compareTo(localBinaryRefAddr.addrType) == 0) {
/* 127 */         if ((this.buf == null) && (localBinaryRefAddr.buf == null))
/* 128 */           return true;
/* 129 */         if ((this.buf == null) || (localBinaryRefAddr.buf == null) || (this.buf.length != localBinaryRefAddr.buf.length))
/*     */         {
/* 131 */           return false;
/* 132 */         }for (int i = 0; i < this.buf.length; i++)
/* 133 */           if (this.buf[i] != localBinaryRefAddr.buf[i])
/* 134 */             return false;
/* 135 */         return true;
/*     */       }
/*     */     }
/* 138 */     return false;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 151 */     int i = this.addrType.hashCode();
/* 152 */     for (int j = 0; j < this.buf.length; j++) {
/* 153 */       i += this.buf[j];
/*     */     }
/* 155 */     return i;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 168 */     StringBuffer localStringBuffer = new StringBuffer("Address Type: " + this.addrType + "\n");
/*     */ 
/* 170 */     localStringBuffer.append("AddressContents: ");
/* 171 */     for (int i = 0; (i < this.buf.length) && (i < 32); i++) {
/* 172 */       localStringBuffer.append(Integer.toHexString(this.buf[i]) + " ");
/*     */     }
/* 174 */     if (this.buf.length >= 32)
/* 175 */       localStringBuffer.append(" ...\n");
/* 176 */     return localStringBuffer.toString();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.naming.BinaryRefAddr
 * JD-Core Version:    0.6.2
 */