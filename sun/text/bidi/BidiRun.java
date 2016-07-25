/*     */ package sun.text.bidi;
/*     */ 
/*     */ public class BidiRun
/*     */ {
/*     */   int start;
/*     */   int limit;
/*     */   int insertRemove;
/*     */   byte level;
/*     */ 
/*     */   BidiRun()
/*     */   {
/*  82 */     this(0, 0, (byte)0);
/*     */   }
/*     */ 
/*     */   BidiRun(int paramInt1, int paramInt2, byte paramByte)
/*     */   {
/*  90 */     this.start = paramInt1;
/*  91 */     this.limit = paramInt2;
/*  92 */     this.level = paramByte;
/*     */   }
/*     */ 
/*     */   void copyFrom(BidiRun paramBidiRun)
/*     */   {
/* 100 */     this.start = paramBidiRun.start;
/* 101 */     this.limit = paramBidiRun.limit;
/* 102 */     this.level = paramBidiRun.level;
/* 103 */     this.insertRemove = paramBidiRun.insertRemove;
/*     */   }
/*     */ 
/*     */   public byte getEmbeddingLevel()
/*     */   {
/* 111 */     return this.level;
/*     */   }
/*     */ 
/*     */   boolean isEvenRun()
/*     */   {
/* 121 */     return (this.level & 0x1) == 0;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.text.bidi.BidiRun
 * JD-Core Version:    0.6.2
 */