/*     */ package java.util.zip;
/*     */ 
/*     */ import java.io.FilterOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ 
/*     */ public class DeflaterOutputStream extends FilterOutputStream
/*     */ {
/*     */   protected Deflater def;
/*     */   protected byte[] buf;
/*  57 */   private boolean closed = false;
/*     */   private final boolean syncFlush;
/* 145 */   boolean usesDefaultDeflater = false;
/*     */ 
/*     */   public DeflaterOutputStream(OutputStream paramOutputStream, Deflater paramDeflater, int paramInt, boolean paramBoolean)
/*     */   {
/*  82 */     super(paramOutputStream);
/*  83 */     if ((paramOutputStream == null) || (paramDeflater == null))
/*  84 */       throw new NullPointerException();
/*  85 */     if (paramInt <= 0) {
/*  86 */       throw new IllegalArgumentException("buffer size <= 0");
/*     */     }
/*  88 */     this.def = paramDeflater;
/*  89 */     this.buf = new byte[paramInt];
/*  90 */     this.syncFlush = paramBoolean;
/*     */   }
/*     */ 
/*     */   public DeflaterOutputStream(OutputStream paramOutputStream, Deflater paramDeflater, int paramInt)
/*     */   {
/* 107 */     this(paramOutputStream, paramDeflater, paramInt, false);
/*     */   }
/*     */ 
/*     */   public DeflaterOutputStream(OutputStream paramOutputStream, Deflater paramDeflater, boolean paramBoolean)
/*     */   {
/* 127 */     this(paramOutputStream, paramDeflater, 512, paramBoolean);
/*     */   }
/*     */ 
/*     */   public DeflaterOutputStream(OutputStream paramOutputStream, Deflater paramDeflater)
/*     */   {
/* 142 */     this(paramOutputStream, paramDeflater, 512, false);
/*     */   }
/*     */ 
/*     */   public DeflaterOutputStream(OutputStream paramOutputStream, boolean paramBoolean)
/*     */   {
/* 162 */     this(paramOutputStream, new Deflater(), 512, paramBoolean);
/* 163 */     this.usesDefaultDeflater = true;
/*     */   }
/*     */ 
/*     */   public DeflaterOutputStream(OutputStream paramOutputStream)
/*     */   {
/* 175 */     this(paramOutputStream, false);
/* 176 */     this.usesDefaultDeflater = true;
/*     */   }
/*     */ 
/*     */   public void write(int paramInt)
/*     */     throws IOException
/*     */   {
/* 186 */     byte[] arrayOfByte = new byte[1];
/* 187 */     arrayOfByte[0] = ((byte)(paramInt & 0xFF));
/* 188 */     write(arrayOfByte, 0, 1);
/*     */   }
/*     */ 
/*     */   public void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws IOException
/*     */   {
/* 200 */     if (this.def.finished()) {
/* 201 */       throw new IOException("write beyond end of stream");
/*     */     }
/* 203 */     if ((paramInt1 | paramInt2 | paramInt1 + paramInt2 | paramArrayOfByte.length - (paramInt1 + paramInt2)) < 0)
/* 204 */       throw new IndexOutOfBoundsException();
/* 205 */     if (paramInt2 == 0) {
/* 206 */       return;
/*     */     }
/* 208 */     if (!this.def.finished()) {
/* 209 */       this.def.setInput(paramArrayOfByte, paramInt1, paramInt2);
/* 210 */       while (!this.def.needsInput())
/* 211 */         deflate();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void finish()
/*     */     throws IOException
/*     */   {
/* 223 */     if (!this.def.finished()) {
/* 224 */       this.def.finish();
/* 225 */       while (!this.def.finished())
/* 226 */         deflate();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void close()
/*     */     throws IOException
/*     */   {
/* 237 */     if (!this.closed) {
/* 238 */       finish();
/* 239 */       if (this.usesDefaultDeflater)
/* 240 */         this.def.end();
/* 241 */       this.out.close();
/* 242 */       this.closed = true;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void deflate()
/*     */     throws IOException
/*     */   {
/* 251 */     int i = this.def.deflate(this.buf, 0, this.buf.length);
/* 252 */     if (i > 0)
/* 253 */       this.out.write(this.buf, 0, i);
/*     */   }
/*     */ 
/*     */   public void flush()
/*     */     throws IOException
/*     */   {
/* 273 */     if ((this.syncFlush) && (!this.def.finished())) {
/* 274 */       int i = 0;
/* 275 */       while ((i = this.def.deflate(this.buf, 0, this.buf.length, 2)) > 0)
/*     */       {
/* 277 */         this.out.write(this.buf, 0, i);
/* 278 */         if (i < this.buf.length)
/* 279 */           break;
/*     */       }
/*     */     }
/* 282 */     this.out.flush();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.zip.DeflaterOutputStream
 * JD-Core Version:    0.6.2
 */