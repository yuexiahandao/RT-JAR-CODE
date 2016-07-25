/*     */ package javax.swing;
/*     */ 
/*     */ final class LegacyLayoutFocusTraversalPolicy extends LayoutFocusTraversalPolicy
/*     */ {
/*     */   LegacyLayoutFocusTraversalPolicy(DefaultFocusManager paramDefaultFocusManager)
/*     */   {
/* 155 */     super(new CompareTabOrderComparator(paramDefaultFocusManager));
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.LegacyLayoutFocusTraversalPolicy
 * JD-Core Version:    0.6.2
 */