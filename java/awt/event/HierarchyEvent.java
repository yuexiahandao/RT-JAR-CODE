/*     */ package java.awt.event;
/*     */ 
/*     */ import java.awt.AWTEvent;
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ 
/*     */ public class HierarchyEvent extends AWTEvent
/*     */ {
/*     */   private static final long serialVersionUID = -5337576970038043990L;
/*     */   public static final int HIERARCHY_FIRST = 1400;
/*     */   public static final int HIERARCHY_CHANGED = 1400;
/*     */   public static final int ANCESTOR_MOVED = 1401;
/*     */   public static final int ANCESTOR_RESIZED = 1402;
/*     */   public static final int HIERARCHY_LAST = 1402;
/*     */   public static final int PARENT_CHANGED = 1;
/*     */   public static final int DISPLAYABILITY_CHANGED = 2;
/*     */   public static final int SHOWING_CHANGED = 4;
/*     */   Component changed;
/*     */   Container changedParent;
/*     */   long changeFlags;
/*     */ 
/*     */   public HierarchyEvent(Component paramComponent1, int paramInt, Component paramComponent2, Container paramContainer)
/*     */   {
/* 195 */     super(paramComponent1, paramInt);
/* 196 */     this.changed = paramComponent2;
/* 197 */     this.changedParent = paramContainer;
/*     */   }
/*     */ 
/*     */   public HierarchyEvent(Component paramComponent1, int paramInt, Component paramComponent2, Container paramContainer, long paramLong)
/*     */   {
/* 232 */     super(paramComponent1, paramInt);
/* 233 */     this.changed = paramComponent2;
/* 234 */     this.changedParent = paramContainer;
/* 235 */     this.changeFlags = paramLong;
/*     */   }
/*     */ 
/*     */   public Component getComponent()
/*     */   {
/* 246 */     return (this.source instanceof Component) ? (Component)this.source : null;
/*     */   }
/*     */ 
/*     */   public Component getChanged()
/*     */   {
/* 256 */     return this.changed;
/*     */   }
/*     */ 
/*     */   public Container getChangedParent()
/*     */   {
/* 273 */     return this.changedParent;
/*     */   }
/*     */ 
/*     */   public long getChangeFlags()
/*     */   {
/* 285 */     return this.changeFlags;
/*     */   }
/*     */ 
/*     */   public String paramString()
/*     */   {
/*     */     String str;
/* 296 */     switch (this.id) {
/*     */     case 1401:
/* 298 */       str = "ANCESTOR_MOVED (" + this.changed + "," + this.changedParent + ")";
/* 299 */       break;
/*     */     case 1402:
/* 301 */       str = "ANCESTOR_RESIZED (" + this.changed + "," + this.changedParent + ")";
/* 302 */       break;
/*     */     case 1400:
/* 304 */       str = "HIERARCHY_CHANGED (";
/* 305 */       int i = 1;
/* 306 */       if ((this.changeFlags & 1L) != 0L) {
/* 307 */         i = 0;
/* 308 */         str = str + "PARENT_CHANGED";
/*     */       }
/* 310 */       if ((this.changeFlags & 0x2) != 0L) {
/* 311 */         if (i != 0)
/* 312 */           i = 0;
/*     */         else {
/* 314 */           str = str + ",";
/*     */         }
/* 316 */         str = str + "DISPLAYABILITY_CHANGED";
/*     */       }
/* 318 */       if ((this.changeFlags & 0x4) != 0L) {
/* 319 */         if (i != 0)
/* 320 */           i = 0;
/*     */         else {
/* 322 */           str = str + ",";
/*     */         }
/* 324 */         str = str + "SHOWING_CHANGED";
/*     */       }
/* 326 */       if (i == 0) {
/* 327 */         str = str + ",";
/*     */       }
/* 329 */       str = str + this.changed + "," + this.changedParent + ")";
/* 330 */       break;
/*     */     default:
/* 333 */       str = "unknown type";
/*     */     }
/* 335 */     return str;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.event.HierarchyEvent
 * JD-Core Version:    0.6.2
 */