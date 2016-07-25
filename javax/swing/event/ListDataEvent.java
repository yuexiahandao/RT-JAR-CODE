/*     */ package javax.swing.event;
/*     */ 
/*     */ import java.util.EventObject;
/*     */ 
/*     */ public class ListDataEvent extends EventObject
/*     */ {
/*     */   public static final int CONTENTS_CHANGED = 0;
/*     */   public static final int INTERVAL_ADDED = 1;
/*     */   public static final int INTERVAL_REMOVED = 2;
/*     */   private int type;
/*     */   private int index0;
/*     */   private int index1;
/*     */ 
/*     */   public int getType()
/*     */   {
/*  68 */     return this.type;
/*     */   }
/*     */ 
/*     */   public int getIndex0()
/*     */   {
/*  77 */     return this.index0;
/*     */   }
/*     */ 
/*     */   public int getIndex1()
/*     */   {
/*  84 */     return this.index1;
/*     */   }
/*     */ 
/*     */   public ListDataEvent(Object paramObject, int paramInt1, int paramInt2, int paramInt3)
/*     */   {
/*  98 */     super(paramObject);
/*  99 */     this.type = paramInt1;
/* 100 */     this.index0 = Math.min(paramInt2, paramInt3);
/* 101 */     this.index1 = Math.max(paramInt2, paramInt3);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 115 */     return getClass().getName() + "[type=" + this.type + ",index0=" + this.index0 + ",index1=" + this.index1 + "]";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.event.ListDataEvent
 * JD-Core Version:    0.6.2
 */