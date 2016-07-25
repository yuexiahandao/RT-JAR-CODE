/*     */ package sun.java2d;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class Spans
/*     */ {
/*     */   private static final int kMaxAddsSinceSort = 256;
/*  52 */   private List mSpans = new Vector(256);
/*     */ 
/*  60 */   private int mAddsSinceSort = 0;
/*     */ 
/*     */   public void add(float paramFloat1, float paramFloat2)
/*     */   {
/*  73 */     if (this.mSpans != null) {
/*  74 */       this.mSpans.add(new Span(paramFloat1, paramFloat2));
/*     */ 
/*  76 */       if (++this.mAddsSinceSort >= 256)
/*  77 */         sortAndCollapse();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void addInfinite()
/*     */   {
/*  92 */     this.mSpans = null;
/*     */   }
/*     */ 
/*     */   public boolean intersects(float paramFloat1, float paramFloat2)
/*     */   {
/*     */     boolean bool;
/* 104 */     if (this.mSpans != null)
/*     */     {
/* 110 */       if (this.mAddsSinceSort > 0) {
/* 111 */         sortAndCollapse();
/*     */       }
/*     */ 
/* 119 */       int i = Collections.binarySearch(this.mSpans, new Span(paramFloat1, paramFloat2), SpanIntersection.instance);
/*     */ 
/* 123 */       bool = i >= 0;
/*     */     }
/*     */     else
/*     */     {
/* 129 */       bool = true;
/*     */     }
/*     */ 
/* 132 */     return bool;
/*     */   }
/*     */ 
/*     */   private void sortAndCollapse()
/*     */   {
/* 144 */     Collections.sort(this.mSpans);
/* 145 */     this.mAddsSinceSort = 0;
/*     */ 
/* 147 */     Iterator localIterator = this.mSpans.iterator();
/*     */ 
/* 153 */     Object localObject = null;
/* 154 */     if (localIterator.hasNext()) {
/* 155 */       localObject = (Span)localIterator.next();
/*     */     }
/*     */ 
/* 161 */     while (localIterator.hasNext())
/*     */     {
/* 163 */       Span localSpan = (Span)localIterator.next();
/*     */ 
/* 185 */       if (((Span)localObject).subsume(localSpan)) {
/* 186 */         localIterator.remove();
/*     */       }
/*     */       else
/*     */       {
/* 194 */         localObject = localSpan;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   static class Span
/*     */     implements Comparable
/*     */   {
/*     */     private float mStart;
/*     */     private float mEnd;
/*     */ 
/*     */     Span(float paramFloat1, float paramFloat2)
/*     */     {
/* 238 */       this.mStart = paramFloat1;
/* 239 */       this.mEnd = paramFloat2;
/*     */     }
/*     */ 
/*     */     final float getStart()
/*     */     {
/* 248 */       return this.mStart;
/*     */     }
/*     */ 
/*     */     final float getEnd()
/*     */     {
/* 257 */       return this.mEnd;
/*     */     }
/*     */ 
/*     */     final void setStart(float paramFloat)
/*     */     {
/* 265 */       this.mStart = paramFloat;
/*     */     }
/*     */ 
/*     */     final void setEnd(float paramFloat)
/*     */     {
/* 273 */       this.mEnd = paramFloat;
/*     */     }
/*     */ 
/*     */     boolean subsume(Span paramSpan)
/*     */     {
/* 290 */       boolean bool = contains(paramSpan.mStart);
/*     */ 
/* 297 */       if ((bool) && (paramSpan.mEnd > this.mEnd)) {
/* 298 */         this.mEnd = paramSpan.mEnd;
/*     */       }
/*     */ 
/* 301 */       return bool;
/*     */     }
/*     */ 
/*     */     boolean contains(float paramFloat)
/*     */     {
/* 310 */       return (this.mStart <= paramFloat) && (paramFloat < this.mEnd);
/*     */     }
/*     */ 
/*     */     public int compareTo(Object paramObject)
/*     */     {
/* 319 */       Span localSpan = (Span)paramObject;
/* 320 */       float f = localSpan.getStart();
/*     */       int i;
/* 323 */       if (this.mStart < f)
/* 324 */         i = -1;
/* 325 */       else if (this.mStart > f)
/* 326 */         i = 1;
/*     */       else {
/* 328 */         i = 0;
/*     */       }
/*     */ 
/* 331 */       return i;
/*     */     }
/*     */ 
/*     */     public String toString() {
/* 335 */       return "Span: " + this.mStart + " to " + this.mEnd;
/*     */     }
/*     */   }
/*     */ 
/*     */   static class SpanIntersection
/*     */     implements Comparator
/*     */   {
/* 354 */     static final SpanIntersection instance = new SpanIntersection();
/*     */ 
/*     */     public int compare(Object paramObject1, Object paramObject2)
/*     */     {
/* 366 */       Spans.Span localSpan1 = (Spans.Span)paramObject1;
/* 367 */       Spans.Span localSpan2 = (Spans.Span)paramObject2;
/*     */       int i;
/* 373 */       if (localSpan1.getEnd() <= localSpan2.getStart()) {
/* 374 */         i = -1;
/*     */       }
/* 380 */       else if (localSpan1.getStart() >= localSpan2.getEnd()) {
/* 381 */         i = 1;
/*     */       }
/*     */       else
/*     */       {
/* 386 */         i = 0;
/*     */       }
/*     */ 
/* 389 */       return i;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.Spans
 * JD-Core Version:    0.6.2
 */