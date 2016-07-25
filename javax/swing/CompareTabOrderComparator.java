/*     */ package javax.swing;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.util.Comparator;
/*     */ 
/*     */ final class CompareTabOrderComparator
/*     */   implements Comparator<Component>
/*     */ {
/*     */   private final DefaultFocusManager defaultFocusManager;
/*     */ 
/*     */   CompareTabOrderComparator(DefaultFocusManager paramDefaultFocusManager)
/*     */   {
/* 163 */     this.defaultFocusManager = paramDefaultFocusManager;
/*     */   }
/*     */ 
/*     */   public int compare(Component paramComponent1, Component paramComponent2) {
/* 167 */     if (paramComponent1 == paramComponent2) {
/* 168 */       return 0;
/*     */     }
/* 170 */     return this.defaultFocusManager.compareTabOrder(paramComponent1, paramComponent2) ? -1 : 1;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.CompareTabOrderComparator
 * JD-Core Version:    0.6.2
 */