/*     */ package java.awt;
/*     */ 
/*     */ abstract class MediaEntry
/*     */ {
/*     */   MediaTracker tracker;
/*     */   int ID;
/*     */   MediaEntry next;
/*     */   int status;
/*     */   boolean cancelled;
/*     */   static final int LOADING = 1;
/*     */   static final int ABORTED = 2;
/*     */   static final int ERRORED = 4;
/*     */   static final int COMPLETE = 8;
/*     */   static final int LOADSTARTED = 13;
/*     */   static final int DONE = 14;
/*     */ 
/*     */   MediaEntry(MediaTracker paramMediaTracker, int paramInt)
/*     */   {
/* 821 */     this.tracker = paramMediaTracker;
/* 822 */     this.ID = paramInt;
/*     */   }
/*     */ 
/*     */   abstract Object getMedia();
/*     */ 
/*     */   static MediaEntry insert(MediaEntry paramMediaEntry1, MediaEntry paramMediaEntry2) {
/* 828 */     MediaEntry localMediaEntry1 = paramMediaEntry1;
/* 829 */     MediaEntry localMediaEntry2 = null;
/* 830 */     while ((localMediaEntry1 != null) && 
/* 831 */       (localMediaEntry1.ID <= paramMediaEntry2.ID))
/*     */     {
/* 834 */       localMediaEntry2 = localMediaEntry1;
/* 835 */       localMediaEntry1 = localMediaEntry1.next;
/*     */     }
/* 837 */     paramMediaEntry2.next = localMediaEntry1;
/* 838 */     if (localMediaEntry2 == null)
/* 839 */       paramMediaEntry1 = paramMediaEntry2;
/*     */     else {
/* 841 */       localMediaEntry2.next = paramMediaEntry2;
/*     */     }
/* 843 */     return paramMediaEntry1;
/*     */   }
/*     */ 
/*     */   int getID() {
/* 847 */     return this.ID;
/*     */   }
/*     */ 
/*     */   abstract void startLoad();
/*     */ 
/*     */   void cancel() {
/* 853 */     this.cancelled = true;
/*     */   }
/*     */ 
/*     */   synchronized int getStatus(boolean paramBoolean1, boolean paramBoolean2)
/*     */   {
/* 865 */     if ((paramBoolean1) && ((this.status & 0xD) == 0)) {
/* 866 */       this.status = (this.status & 0xFFFFFFFD | 0x1);
/* 867 */       startLoad();
/*     */     }
/* 869 */     return this.status;
/*     */   }
/*     */ 
/*     */   void setStatus(int paramInt) {
/* 873 */     synchronized (this) {
/* 874 */       this.status = paramInt;
/*     */     }
/* 876 */     this.tracker.setDone();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.MediaEntry
 * JD-Core Version:    0.6.2
 */