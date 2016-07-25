/*     */ package javax.swing;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.FocusTraversalPolicy;
/*     */ 
/*     */ public class DefaultFocusManager extends FocusManager
/*     */ {
/*  51 */   final FocusTraversalPolicy gluePolicy = new LegacyGlueFocusTraversalPolicy(this);
/*     */ 
/*  53 */   private final FocusTraversalPolicy layoutPolicy = new LegacyLayoutFocusTraversalPolicy(this);
/*     */ 
/*  55 */   private final LayoutComparator comparator = new LayoutComparator();
/*     */ 
/*     */   public DefaultFocusManager()
/*     */   {
/*  59 */     setDefaultFocusTraversalPolicy(this.gluePolicy);
/*     */   }
/*     */ 
/*     */   public Component getComponentAfter(Container paramContainer, Component paramComponent)
/*     */   {
/*  65 */     Container localContainer = paramContainer.isFocusCycleRoot() ? paramContainer : paramContainer.getFocusCycleRootAncestor();
/*     */ 
/*  71 */     if (localContainer != null) {
/*  72 */       FocusTraversalPolicy localFocusTraversalPolicy = localContainer.getFocusTraversalPolicy();
/*  73 */       if (localFocusTraversalPolicy != this.gluePolicy) {
/*  74 */         return localFocusTraversalPolicy.getComponentAfter(localContainer, paramComponent);
/*     */       }
/*     */ 
/*  77 */       this.comparator.setComponentOrientation(localContainer.getComponentOrientation());
/*  78 */       return this.layoutPolicy.getComponentAfter(localContainer, paramComponent);
/*     */     }
/*     */ 
/*  81 */     return null;
/*     */   }
/*     */ 
/*     */   public Component getComponentBefore(Container paramContainer, Component paramComponent)
/*     */   {
/*  87 */     Container localContainer = paramContainer.isFocusCycleRoot() ? paramContainer : paramContainer.getFocusCycleRootAncestor();
/*     */ 
/*  93 */     if (localContainer != null) {
/*  94 */       FocusTraversalPolicy localFocusTraversalPolicy = localContainer.getFocusTraversalPolicy();
/*  95 */       if (localFocusTraversalPolicy != this.gluePolicy) {
/*  96 */         return localFocusTraversalPolicy.getComponentBefore(localContainer, paramComponent);
/*     */       }
/*     */ 
/*  99 */       this.comparator.setComponentOrientation(localContainer.getComponentOrientation());
/* 100 */       return this.layoutPolicy.getComponentBefore(localContainer, paramComponent);
/*     */     }
/*     */ 
/* 103 */     return null;
/*     */   }
/*     */ 
/*     */   public Component getFirstComponent(Container paramContainer) {
/* 107 */     Container localContainer = paramContainer.isFocusCycleRoot() ? paramContainer : paramContainer.getFocusCycleRootAncestor();
/*     */ 
/* 113 */     if (localContainer != null) {
/* 114 */       FocusTraversalPolicy localFocusTraversalPolicy = localContainer.getFocusTraversalPolicy();
/* 115 */       if (localFocusTraversalPolicy != this.gluePolicy) {
/* 116 */         return localFocusTraversalPolicy.getFirstComponent(localContainer);
/*     */       }
/*     */ 
/* 119 */       this.comparator.setComponentOrientation(localContainer.getComponentOrientation());
/* 120 */       return this.layoutPolicy.getFirstComponent(localContainer);
/*     */     }
/*     */ 
/* 123 */     return null;
/*     */   }
/*     */ 
/*     */   public Component getLastComponent(Container paramContainer) {
/* 127 */     Container localContainer = paramContainer.isFocusCycleRoot() ? paramContainer : paramContainer.getFocusCycleRootAncestor();
/*     */ 
/* 133 */     if (localContainer != null) {
/* 134 */       FocusTraversalPolicy localFocusTraversalPolicy = localContainer.getFocusTraversalPolicy();
/* 135 */       if (localFocusTraversalPolicy != this.gluePolicy) {
/* 136 */         return localFocusTraversalPolicy.getLastComponent(localContainer);
/*     */       }
/*     */ 
/* 139 */       this.comparator.setComponentOrientation(localContainer.getComponentOrientation());
/* 140 */       return this.layoutPolicy.getLastComponent(localContainer);
/*     */     }
/*     */ 
/* 143 */     return null;
/*     */   }
/*     */ 
/*     */   public boolean compareTabOrder(Component paramComponent1, Component paramComponent2) {
/* 147 */     return this.comparator.compare(paramComponent1, paramComponent2) < 0;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.DefaultFocusManager
 * JD-Core Version:    0.6.2
 */