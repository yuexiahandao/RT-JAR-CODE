/*     */ package javax.swing.text;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ class SegmentCache
/*     */ {
/*  46 */   private static SegmentCache sharedCache = new SegmentCache();
/*     */   private List<Segment> segments;
/*     */ 
/*     */   public static SegmentCache getSharedInstance()
/*     */   {
/*  58 */     return sharedCache;
/*     */   }
/*     */ 
/*     */   public static Segment getSharedSegment()
/*     */   {
/*  66 */     return getSharedInstance().getSegment();
/*     */   }
/*     */ 
/*     */   public static void releaseSharedSegment(Segment paramSegment)
/*     */   {
/*  74 */     getSharedInstance().releaseSegment(paramSegment);
/*     */   }
/*     */ 
/*     */   public SegmentCache()
/*     */   {
/*  83 */     this.segments = new ArrayList(11);
/*     */   }
/*     */ 
/*     */   public Segment getSegment()
/*     */   {
/*  91 */     synchronized (this) {
/*  92 */       int i = this.segments.size();
/*     */ 
/*  94 */       if (i > 0) {
/*  95 */         return (Segment)this.segments.remove(i - 1);
/*     */       }
/*     */     }
/*  98 */     return new CachedSegment(null);
/*     */   }
/*     */ 
/*     */   public void releaseSegment(Segment paramSegment)
/*     */   {
/* 111 */     if ((paramSegment instanceof CachedSegment))
/* 112 */       synchronized (this) {
/* 113 */         paramSegment.array = null;
/* 114 */         paramSegment.count = 0;
/* 115 */         this.segments.add(paramSegment);
/*     */       }
/*     */   }
/*     */ 
/*     */   private static class CachedSegment extends Segment
/*     */   {
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.text.SegmentCache
 * JD-Core Version:    0.6.2
 */