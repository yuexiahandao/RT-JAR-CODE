/*     */ package com.sun.corba.se.impl.encoding;
/*     */ 
/*     */ import com.sun.corba.se.impl.logging.ORBUtilSystemException;
/*     */ import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
/*     */ import org.omg.CORBA.CompletionStatus;
/*     */ 
/*     */ public class CDROutputStream_1_1 extends CDROutputStream_1_0
/*     */ {
/*  46 */   protected int fragmentOffset = 0;
/*     */ 
/*     */   protected void alignAndReserve(int paramInt1, int paramInt2)
/*     */   {
/*  58 */     int i = computeAlignment(paramInt1);
/*     */ 
/*  60 */     if (this.bbwi.position() + paramInt2 + i > this.bbwi.buflen) {
/*  61 */       grow(paramInt1, paramInt2);
/*     */ 
/*  70 */       i = computeAlignment(paramInt1);
/*     */     }
/*     */ 
/*  73 */     this.bbwi.position(this.bbwi.position() + i);
/*     */   }
/*     */ 
/*     */   protected void grow(int paramInt1, int paramInt2)
/*     */   {
/*  78 */     int i = this.bbwi.position();
/*     */ 
/*  80 */     super.grow(paramInt1, paramInt2);
/*     */ 
/*  85 */     if (this.bbwi.fragmented)
/*     */     {
/*  88 */       this.bbwi.fragmented = false;
/*     */ 
/*  94 */       this.fragmentOffset += i - this.bbwi.position();
/*     */     }
/*     */   }
/*     */ 
/*     */   public int get_offset() {
/*  99 */     return this.bbwi.position() + this.fragmentOffset;
/*     */   }
/*     */ 
/*     */   public GIOPVersion getGIOPVersion() {
/* 103 */     return GIOPVersion.V1_1;
/*     */   }
/*     */ 
/*     */   public void write_wchar(char paramChar)
/*     */   {
/* 112 */     CodeSetConversion.CTBConverter localCTBConverter = getWCharConverter();
/*     */ 
/* 114 */     localCTBConverter.convert(paramChar);
/*     */ 
/* 116 */     if (localCTBConverter.getNumBytes() != 2) {
/* 117 */       throw this.wrapper.badGiop11Ctb(CompletionStatus.COMPLETED_MAYBE);
/*     */     }
/* 119 */     alignAndReserve(localCTBConverter.getAlignment(), localCTBConverter.getNumBytes());
/*     */ 
/* 122 */     this.parent.write_octet_array(localCTBConverter.getBytes(), 0, localCTBConverter.getNumBytes());
/*     */   }
/*     */ 
/*     */   public void write_wstring(String paramString)
/*     */   {
/* 129 */     if (paramString == null) {
/* 130 */       throw this.wrapper.nullParam(CompletionStatus.COMPLETED_MAYBE);
/*     */     }
/*     */ 
/* 136 */     int i = paramString.length() + 1;
/*     */ 
/* 138 */     write_long(i);
/*     */ 
/* 140 */     CodeSetConversion.CTBConverter localCTBConverter = getWCharConverter();
/*     */ 
/* 142 */     localCTBConverter.convert(paramString);
/*     */ 
/* 144 */     internalWriteOctetArray(localCTBConverter.getBytes(), 0, localCTBConverter.getNumBytes());
/*     */ 
/* 147 */     write_short((short)0);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.encoding.CDROutputStream_1_1
 * JD-Core Version:    0.6.2
 */