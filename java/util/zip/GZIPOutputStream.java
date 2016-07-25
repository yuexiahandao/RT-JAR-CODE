/*     */ package java.util.zip;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ 
/*     */ public class GZIPOutputStream extends DeflaterOutputStream
/*     */ {
/*  42 */   protected CRC32 crc = new CRC32();
/*     */   private static final int GZIP_MAGIC = 35615;
/*     */   private static final int TRAILER_SIZE = 8;
/*     */ 
/*     */   public GZIPOutputStream(OutputStream paramOutputStream, int paramInt)
/*     */     throws IOException
/*     */   {
/*  68 */     this(paramOutputStream, paramInt, false);
/*     */   }
/*     */ 
/*     */   public GZIPOutputStream(OutputStream paramOutputStream, int paramInt, boolean paramBoolean)
/*     */     throws IOException
/*     */   {
/*  91 */     super(paramOutputStream, new Deflater(-1, true), paramInt, paramBoolean);
/*     */ 
/*  94 */     this.usesDefaultDeflater = true;
/*  95 */     writeHeader();
/*  96 */     this.crc.reset();
/*     */   }
/*     */ 
/*     */   public GZIPOutputStream(OutputStream paramOutputStream)
/*     */     throws IOException
/*     */   {
/* 110 */     this(paramOutputStream, 512, false);
/*     */   }
/*     */ 
/*     */   public GZIPOutputStream(OutputStream paramOutputStream, boolean paramBoolean)
/*     */     throws IOException
/*     */   {
/* 132 */     this(paramOutputStream, 512, paramBoolean);
/*     */   }
/*     */ 
/*     */   public synchronized void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws IOException
/*     */   {
/* 146 */     super.write(paramArrayOfByte, paramInt1, paramInt2);
/* 147 */     this.crc.update(paramArrayOfByte, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public void finish()
/*     */     throws IOException
/*     */   {
/* 157 */     if (!this.def.finished()) {
/* 158 */       this.def.finish();
/* 159 */       while (!this.def.finished()) {
/* 160 */         int i = this.def.deflate(this.buf, 0, this.buf.length);
/* 161 */         if ((this.def.finished()) && (i <= this.buf.length - 8))
/*     */         {
/* 163 */           writeTrailer(this.buf, i);
/* 164 */           i += 8;
/* 165 */           this.out.write(this.buf, 0, i);
/* 166 */           return;
/*     */         }
/* 168 */         if (i > 0) {
/* 169 */           this.out.write(this.buf, 0, i);
/*     */         }
/*     */       }
/*     */ 
/* 173 */       byte[] arrayOfByte = new byte[8];
/* 174 */       writeTrailer(arrayOfByte, 0);
/* 175 */       this.out.write(arrayOfByte);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void writeHeader()
/*     */     throws IOException
/*     */   {
/* 183 */     this.out.write(new byte[] { 31, -117, 8, 0, 0, 0, 0, 0, 0, 0 });
/*     */   }
/*     */ 
/*     */   private void writeTrailer(byte[] paramArrayOfByte, int paramInt)
/*     */     throws IOException
/*     */   {
/* 202 */     writeInt((int)this.crc.getValue(), paramArrayOfByte, paramInt);
/* 203 */     writeInt(this.def.getTotalIn(), paramArrayOfByte, paramInt + 4);
/*     */   }
/*     */ 
/*     */   private void writeInt(int paramInt1, byte[] paramArrayOfByte, int paramInt2)
/*     */     throws IOException
/*     */   {
/* 211 */     writeShort(paramInt1 & 0xFFFF, paramArrayOfByte, paramInt2);
/* 212 */     writeShort(paramInt1 >> 16 & 0xFFFF, paramArrayOfByte, paramInt2 + 2);
/*     */   }
/*     */ 
/*     */   private void writeShort(int paramInt1, byte[] paramArrayOfByte, int paramInt2)
/*     */     throws IOException
/*     */   {
/* 220 */     paramArrayOfByte[paramInt2] = ((byte)(paramInt1 & 0xFF));
/* 221 */     paramArrayOfByte[(paramInt2 + 1)] = ((byte)(paramInt1 >> 8 & 0xFF));
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.zip.GZIPOutputStream
 * JD-Core Version:    0.6.2
 */