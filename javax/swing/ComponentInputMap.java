/*     */ package javax.swing;
/*     */ 
/*     */ public class ComponentInputMap extends InputMap
/*     */ {
/*     */   private JComponent component;
/*     */ 
/*     */   public ComponentInputMap(JComponent paramJComponent)
/*     */   {
/*  50 */     this.component = paramJComponent;
/*  51 */     if (paramJComponent == null)
/*  52 */       throw new IllegalArgumentException("ComponentInputMaps must be associated with a non-null JComponent");
/*     */   }
/*     */ 
/*     */   public void setParent(InputMap paramInputMap)
/*     */   {
/*  68 */     if (getParent() == paramInputMap) {
/*  69 */       return;
/*     */     }
/*  71 */     if ((paramInputMap != null) && ((!(paramInputMap instanceof ComponentInputMap)) || (((ComponentInputMap)paramInputMap).getComponent() != getComponent())))
/*     */     {
/*  73 */       throw new IllegalArgumentException("ComponentInputMaps must have a parent ComponentInputMap associated with the same component");
/*     */     }
/*  75 */     super.setParent(paramInputMap);
/*  76 */     getComponent().componentInputMapChanged(this);
/*     */   }
/*     */ 
/*     */   public JComponent getComponent()
/*     */   {
/*  83 */     return this.component;
/*     */   }
/*     */ 
/*     */   public void put(KeyStroke paramKeyStroke, Object paramObject)
/*     */   {
/*  92 */     super.put(paramKeyStroke, paramObject);
/*  93 */     if (getComponent() != null)
/*  94 */       getComponent().componentInputMapChanged(this);
/*     */   }
/*     */ 
/*     */   public void remove(KeyStroke paramKeyStroke)
/*     */   {
/* 102 */     super.remove(paramKeyStroke);
/* 103 */     if (getComponent() != null)
/* 104 */       getComponent().componentInputMapChanged(this);
/*     */   }
/*     */ 
/*     */   public void clear()
/*     */   {
/* 112 */     int i = size();
/* 113 */     super.clear();
/* 114 */     if ((i > 0) && (getComponent() != null))
/* 115 */       getComponent().componentInputMapChanged(this);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.ComponentInputMap
 * JD-Core Version:    0.6.2
 */