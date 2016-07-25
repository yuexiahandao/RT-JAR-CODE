/*     */ package java.io;
/*     */ 
/*     */ public abstract class FilterWriter extends Writer
/*     */ {
/*     */   protected Writer out;
/*     */ 
/*     */   protected FilterWriter(Writer paramWriter)
/*     */   {
/*  55 */     super(paramWriter);
/*  56 */     this.out = paramWriter;
/*     */   }
/*     */ 
/*     */   public void write(int paramInt)
/*     */     throws IOException
/*     */   {
/*  65 */     this.out.write(paramInt);
/*     */   }
/*     */ 
/*     */   public void write(char[] paramArrayOfChar, int paramInt1, int paramInt2)
/*     */     throws IOException
/*     */   {
/*  78 */     this.out.write(paramArrayOfChar, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public void write(String paramString, int paramInt1, int paramInt2)
/*     */     throws IOException
/*     */   {
/*  91 */     this.out.write(paramString, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public void flush()
/*     */     throws IOException
/*     */   {
/* 100 */     this.out.flush();
/*     */   }
/*     */ 
/*     */   public void close() throws IOException {
/* 104 */     this.out.close();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.io.FilterWriter
 * JD-Core Version:    0.6.2
 */