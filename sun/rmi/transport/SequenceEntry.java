/*     */ package sun.rmi.transport;
/*     */ 
/*     */ class SequenceEntry
/*     */ {
/*     */   long sequenceNum;
/*     */   boolean keep;
/*     */ 
/*     */   SequenceEntry(long paramLong)
/*     */   {
/* 460 */     this.sequenceNum = paramLong;
/* 461 */     this.keep = false;
/*     */   }
/*     */ 
/*     */   void retain(long paramLong) {
/* 465 */     this.sequenceNum = paramLong;
/* 466 */     this.keep = true;
/*     */   }
/*     */ 
/*     */   void update(long paramLong) {
/* 470 */     this.sequenceNum = paramLong;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.rmi.transport.SequenceEntry
 * JD-Core Version:    0.6.2
 */