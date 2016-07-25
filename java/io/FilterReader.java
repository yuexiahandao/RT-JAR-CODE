/*     */ package java.io;
/*     */ 
/*     */ public abstract class FilterReader extends Reader
/*     */ {
/*     */   protected Reader in;
/*     */ 
/*     */   protected FilterReader(Reader paramReader)
/*     */   {
/*  55 */     super(paramReader);
/*  56 */     this.in = paramReader;
/*     */   }
/*     */ 
/*     */   public int read()
/*     */     throws IOException
/*     */   {
/*  65 */     return this.in.read();
/*     */   }
/*     */ 
/*     */   public int read(char[] paramArrayOfChar, int paramInt1, int paramInt2)
/*     */     throws IOException
/*     */   {
/*  74 */     return this.in.read(paramArrayOfChar, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public long skip(long paramLong)
/*     */     throws IOException
/*     */   {
/*  83 */     return this.in.skip(paramLong);
/*     */   }
/*     */ 
/*     */   public boolean ready()
/*     */     throws IOException
/*     */   {
/*  92 */     return this.in.ready();
/*     */   }
/*     */ 
/*     */   public boolean markSupported()
/*     */   {
/*  99 */     return this.in.markSupported();
/*     */   }
/*     */ 
/*     */   public void mark(int paramInt)
/*     */     throws IOException
/*     */   {
/* 108 */     this.in.mark(paramInt);
/*     */   }
/*     */ 
/*     */   public void reset()
/*     */     throws IOException
/*     */   {
/* 117 */     this.in.reset();
/*     */   }
/*     */ 
/*     */   public void close() throws IOException {
/* 121 */     this.in.close();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.io.FilterReader
 * JD-Core Version:    0.6.2
 */