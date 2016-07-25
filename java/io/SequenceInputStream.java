/*     */ package java.io;
/*     */ 
/*     */ import java.util.Enumeration;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class SequenceInputStream extends InputStream
/*     */ {
/*     */   Enumeration e;
/*     */   InputStream in;
/*     */ 
/*     */   public SequenceInputStream(Enumeration<? extends InputStream> paramEnumeration)
/*     */   {
/*  67 */     this.e = paramEnumeration;
/*     */     try {
/*  69 */       nextStream();
/*     */     }
/*     */     catch (IOException localIOException) {
/*  72 */       throw new Error("panic");
/*     */     }
/*     */   }
/*     */ 
/*     */   public SequenceInputStream(InputStream paramInputStream1, InputStream paramInputStream2)
/*     */   {
/*  88 */     Vector localVector = new Vector(2);
/*     */ 
/*  90 */     localVector.addElement(paramInputStream1);
/*  91 */     localVector.addElement(paramInputStream2);
/*  92 */     this.e = localVector.elements();
/*     */     try {
/*  94 */       nextStream();
/*     */     }
/*     */     catch (IOException localIOException) {
/*  97 */       throw new Error("panic");
/*     */     }
/*     */   }
/*     */ 
/*     */   final void nextStream()
/*     */     throws IOException
/*     */   {
/* 105 */     if (this.in != null) {
/* 106 */       this.in.close();
/*     */     }
/*     */ 
/* 109 */     if (this.e.hasMoreElements()) {
/* 110 */       this.in = ((InputStream)this.e.nextElement());
/* 111 */       if (this.in == null)
/* 112 */         throw new NullPointerException();
/*     */     } else {
/* 114 */       this.in = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   public int available()
/*     */     throws IOException
/*     */   {
/* 138 */     if (this.in == null) {
/* 139 */       return 0;
/*     */     }
/* 141 */     return this.in.available();
/*     */   }
/*     */ 
/*     */   public int read()
/*     */     throws IOException
/*     */   {
/* 163 */     if (this.in == null) {
/* 164 */       return -1;
/*     */     }
/* 166 */     int i = this.in.read();
/* 167 */     if (i == -1) {
/* 168 */       nextStream();
/* 169 */       return read();
/*     */     }
/* 171 */     return i;
/*     */   }
/*     */ 
/*     */   public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws IOException
/*     */   {
/* 198 */     if (this.in == null)
/* 199 */       return -1;
/* 200 */     if (paramArrayOfByte == null)
/* 201 */       throw new NullPointerException();
/* 202 */     if ((paramInt1 < 0) || (paramInt2 < 0) || (paramInt2 > paramArrayOfByte.length - paramInt1))
/* 203 */       throw new IndexOutOfBoundsException();
/* 204 */     if (paramInt2 == 0) {
/* 205 */       return 0;
/*     */     }
/*     */ 
/* 208 */     int i = this.in.read(paramArrayOfByte, paramInt1, paramInt2);
/* 209 */     if (i <= 0) {
/* 210 */       nextStream();
/* 211 */       return read(paramArrayOfByte, paramInt1, paramInt2);
/*     */     }
/* 213 */     return i;
/*     */   }
/*     */ 
/*     */   public void close()
/*     */     throws IOException
/*     */   {
/*     */     do
/* 232 */       nextStream();
/* 233 */     while (this.in != null);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.io.SequenceInputStream
 * JD-Core Version:    0.6.2
 */