/*     */ package sun.awt.windows;
/*     */ 
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ 
/*     */ class WDropTargetContextPeerFileStream extends FileInputStream
/*     */ {
/*     */   private long stgmedium;
/*     */ 
/*     */   WDropTargetContextPeerFileStream(String paramString, long paramLong)
/*     */     throws FileNotFoundException
/*     */   {
/* 136 */     super(paramString);
/*     */ 
/* 138 */     this.stgmedium = paramLong;
/*     */   }
/*     */ 
/*     */   public void close()
/*     */     throws IOException
/*     */   {
/* 146 */     if (this.stgmedium != 0L) {
/* 147 */       super.close();
/* 148 */       freeStgMedium(this.stgmedium);
/* 149 */       this.stgmedium = 0L;
/*     */     }
/*     */   }
/*     */ 
/*     */   private native void freeStgMedium(long paramLong);
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.windows.WDropTargetContextPeerFileStream
 * JD-Core Version:    0.6.2
 */