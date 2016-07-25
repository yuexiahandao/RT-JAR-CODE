/*     */ package com.sun.corba.se.impl.encoding;
/*     */ 
/*     */ import com.sun.corba.se.impl.logging.ORBUtilSystemException;
/*     */ import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
/*     */ import org.omg.CORBA.CompletionStatus;
/*     */ 
/*     */ public class CDROutputStream_1_2 extends CDROutputStream_1_1
/*     */ {
/*  60 */   protected boolean primitiveAcrossFragmentedChunk = false;
/*     */ 
/*  80 */   protected boolean specialChunk = false;
/*     */   private boolean headerPadding;
/*     */ 
/*     */   protected void handleSpecialChunkBegin(int paramInt)
/*     */   {
/*  91 */     if ((this.inBlock) && (paramInt + this.bbwi.position() > this.bbwi.buflen))
/*     */     {
/*  96 */       int i = this.bbwi.position();
/*  97 */       this.bbwi.position(this.blockSizeIndex - 4);
/*     */ 
/* 100 */       writeLongWithoutAlign(i - this.blockSizeIndex + paramInt);
/* 101 */       this.bbwi.position(i);
/*     */ 
/* 105 */       this.specialChunk = true;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void handleSpecialChunkEnd()
/*     */   {
/* 112 */     if ((this.inBlock) && (this.specialChunk))
/*     */     {
/* 117 */       this.inBlock = false;
/* 118 */       this.blockSizeIndex = -1;
/* 119 */       this.blockSizePosition = -1;
/*     */ 
/* 123 */       start_block();
/*     */ 
/* 128 */       this.specialChunk = false;
/*     */     }
/*     */   }
/*     */ 
/*     */   private void checkPrimitiveAcrossFragmentedChunk()
/*     */   {
/* 135 */     if (this.primitiveAcrossFragmentedChunk) {
/* 136 */       this.primitiveAcrossFragmentedChunk = false;
/*     */ 
/* 138 */       this.inBlock = false;
/*     */ 
/* 143 */       this.blockSizeIndex = -1;
/* 144 */       this.blockSizePosition = -1;
/*     */ 
/* 147 */       start_block();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void write_octet(byte paramByte)
/*     */   {
/* 153 */     super.write_octet(paramByte);
/* 154 */     checkPrimitiveAcrossFragmentedChunk();
/*     */   }
/*     */ 
/*     */   public void write_short(short paramShort) {
/* 158 */     super.write_short(paramShort);
/* 159 */     checkPrimitiveAcrossFragmentedChunk();
/*     */   }
/*     */ 
/*     */   public void write_long(int paramInt) {
/* 163 */     super.write_long(paramInt);
/* 164 */     checkPrimitiveAcrossFragmentedChunk();
/*     */   }
/*     */ 
/*     */   public void write_longlong(long paramLong) {
/* 168 */     super.write_longlong(paramLong);
/* 169 */     checkPrimitiveAcrossFragmentedChunk();
/*     */   }
/*     */ 
/*     */   void setHeaderPadding(boolean paramBoolean)
/*     */   {
/* 174 */     this.headerPadding = paramBoolean;
/*     */   }
/*     */ 
/*     */   protected void alignAndReserve(int paramInt1, int paramInt2)
/*     */   {
/* 185 */     if (this.headerPadding == true) {
/* 186 */       this.headerPadding = false;
/* 187 */       alignOnBoundary(8);
/*     */     }
/*     */ 
/* 197 */     this.bbwi.position(this.bbwi.position() + computeAlignment(paramInt1));
/*     */ 
/* 199 */     if (this.bbwi.position() + paramInt2 > this.bbwi.buflen)
/* 200 */       grow(paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   protected void grow(int paramInt1, int paramInt2)
/*     */   {
/* 206 */     int i = this.bbwi.position();
/*     */ 
/* 217 */     int j = (this.inBlock) && (!this.specialChunk) ? 1 : 0;
/* 218 */     if (j != 0) {
/* 219 */       int k = this.bbwi.position();
/*     */ 
/* 221 */       this.bbwi.position(this.blockSizeIndex - 4);
/*     */ 
/* 223 */       writeLongWithoutAlign(k - this.blockSizeIndex + paramInt2);
/*     */ 
/* 225 */       this.bbwi.position(k);
/*     */     }
/*     */ 
/* 228 */     this.bbwi.needed = paramInt2;
/* 229 */     this.bufferManagerWrite.overflow(this.bbwi);
/*     */ 
/* 236 */     if (this.bbwi.fragmented)
/*     */     {
/* 239 */       this.bbwi.fragmented = false;
/*     */ 
/* 245 */       this.fragmentOffset += i - this.bbwi.position();
/*     */ 
/* 249 */       if (j != 0)
/* 250 */         this.primitiveAcrossFragmentedChunk = true;
/*     */     }
/*     */   }
/*     */ 
/*     */   public GIOPVersion getGIOPVersion()
/*     */   {
/* 256 */     return GIOPVersion.V1_2;
/*     */   }
/*     */ 
/*     */   public void write_wchar(char paramChar)
/*     */   {
/* 269 */     CodeSetConversion.CTBConverter localCTBConverter = getWCharConverter();
/*     */ 
/* 271 */     localCTBConverter.convert(paramChar);
/*     */ 
/* 273 */     handleSpecialChunkBegin(1 + localCTBConverter.getNumBytes());
/*     */ 
/* 275 */     write_octet((byte)localCTBConverter.getNumBytes());
/*     */ 
/* 277 */     byte[] arrayOfByte = localCTBConverter.getBytes();
/*     */ 
/* 281 */     internalWriteOctetArray(arrayOfByte, 0, localCTBConverter.getNumBytes());
/*     */ 
/* 283 */     handleSpecialChunkEnd();
/*     */   }
/*     */ 
/*     */   public void write_wchar_array(char[] paramArrayOfChar, int paramInt1, int paramInt2)
/*     */   {
/* 288 */     if (paramArrayOfChar == null) {
/* 289 */       throw this.wrapper.nullParam(CompletionStatus.COMPLETED_MAYBE);
/*     */     }
/*     */ 
/* 292 */     CodeSetConversion.CTBConverter localCTBConverter = getWCharConverter();
/*     */ 
/* 298 */     int i = 0;
/*     */ 
/* 302 */     int j = (int)Math.ceil(localCTBConverter.getMaxBytesPerChar() * paramInt2);
/* 303 */     byte[] arrayOfByte = new byte[j + paramInt2];
/*     */ 
/* 305 */     for (int k = 0; k < paramInt2; k++)
/*     */     {
/* 307 */       localCTBConverter.convert(paramArrayOfChar[(paramInt1 + k)]);
/*     */ 
/* 310 */       arrayOfByte[(i++)] = ((byte)localCTBConverter.getNumBytes());
/*     */ 
/* 313 */       System.arraycopy(localCTBConverter.getBytes(), 0, arrayOfByte, i, localCTBConverter.getNumBytes());
/*     */ 
/* 317 */       i += localCTBConverter.getNumBytes();
/*     */     }
/*     */ 
/* 323 */     handleSpecialChunkBegin(i);
/*     */ 
/* 327 */     internalWriteOctetArray(arrayOfByte, 0, i);
/*     */ 
/* 329 */     handleSpecialChunkEnd();
/*     */   }
/*     */ 
/*     */   public void write_wstring(String paramString) {
/* 333 */     if (paramString == null) {
/* 334 */       throw this.wrapper.nullParam(CompletionStatus.COMPLETED_MAYBE);
/*     */     }
/*     */ 
/* 341 */     if (paramString.length() == 0) {
/* 342 */       write_long(0);
/* 343 */       return;
/*     */     }
/*     */ 
/* 346 */     CodeSetConversion.CTBConverter localCTBConverter = getWCharConverter();
/*     */ 
/* 348 */     localCTBConverter.convert(paramString);
/*     */ 
/* 350 */     handleSpecialChunkBegin(computeAlignment(4) + 4 + localCTBConverter.getNumBytes());
/*     */ 
/* 352 */     write_long(localCTBConverter.getNumBytes());
/*     */ 
/* 355 */     internalWriteOctetArray(localCTBConverter.getBytes(), 0, localCTBConverter.getNumBytes());
/*     */ 
/* 357 */     handleSpecialChunkEnd();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.encoding.CDROutputStream_1_2
 * JD-Core Version:    0.6.2
 */