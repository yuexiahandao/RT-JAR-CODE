/*     */ package sun.misc;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.PrintStream;
/*     */ 
/*     */ public class HexDumpEncoder extends CharacterEncoder
/*     */ {
/*     */   private int offset;
/*     */   private int thisLineLength;
/*     */   private int currentByte;
/*  51 */   private byte[] thisLine = new byte[16];
/*     */ 
/*     */   static void hexDigit(PrintStream paramPrintStream, byte paramByte)
/*     */   {
/*  56 */     int i = (char)(paramByte >> 4 & 0xF);
/*  57 */     if (i > 9)
/*  58 */       i = (char)(i - 10 + 65);
/*     */     else
/*  60 */       i = (char)(i + 48);
/*  61 */     paramPrintStream.write(i);
/*  62 */     i = (char)(paramByte & 0xF);
/*  63 */     if (i > 9)
/*  64 */       i = (char)(i - 10 + 65);
/*     */     else
/*  66 */       i = (char)(i + 48);
/*  67 */     paramPrintStream.write(i);
/*     */   }
/*     */ 
/*     */   protected int bytesPerAtom() {
/*  71 */     return 1;
/*     */   }
/*     */ 
/*     */   protected int bytesPerLine() {
/*  75 */     return 16;
/*     */   }
/*     */ 
/*     */   protected void encodeBufferPrefix(OutputStream paramOutputStream) throws IOException {
/*  79 */     this.offset = 0;
/*  80 */     super.encodeBufferPrefix(paramOutputStream);
/*     */   }
/*     */ 
/*     */   protected void encodeLinePrefix(OutputStream paramOutputStream, int paramInt) throws IOException {
/*  84 */     hexDigit(this.pStream, (byte)(this.offset >>> 8 & 0xFF));
/*  85 */     hexDigit(this.pStream, (byte)(this.offset & 0xFF));
/*  86 */     this.pStream.print(": ");
/*  87 */     this.currentByte = 0;
/*  88 */     this.thisLineLength = paramInt;
/*     */   }
/*     */ 
/*     */   protected void encodeAtom(OutputStream paramOutputStream, byte[] paramArrayOfByte, int paramInt1, int paramInt2) throws IOException {
/*  92 */     this.thisLine[this.currentByte] = paramArrayOfByte[paramInt1];
/*  93 */     hexDigit(this.pStream, paramArrayOfByte[paramInt1]);
/*  94 */     this.pStream.print(" ");
/*  95 */     this.currentByte += 1;
/*  96 */     if (this.currentByte == 8)
/*  97 */       this.pStream.print("  ");
/*     */   }
/*     */ 
/*     */   protected void encodeLineSuffix(OutputStream paramOutputStream) throws IOException {
/* 101 */     if (this.thisLineLength < 16) {
/* 102 */       for (i = this.thisLineLength; i < 16; i++) {
/* 103 */         this.pStream.print("   ");
/* 104 */         if (i == 7)
/* 105 */           this.pStream.print("  ");
/*     */       }
/*     */     }
/* 108 */     this.pStream.print(" ");
/* 109 */     for (int i = 0; i < this.thisLineLength; i++) {
/* 110 */       if ((this.thisLine[i] < 32) || (this.thisLine[i] > 122))
/* 111 */         this.pStream.print(".");
/*     */       else {
/* 113 */         this.pStream.write(this.thisLine[i]);
/*     */       }
/*     */     }
/* 116 */     this.pStream.println();
/* 117 */     this.offset += this.thisLineLength;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.misc.HexDumpEncoder
 * JD-Core Version:    0.6.2
 */