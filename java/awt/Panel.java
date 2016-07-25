/*     */ package java.awt;
/*     */ 
/*     */ import javax.accessibility.Accessible;
/*     */ import javax.accessibility.AccessibleContext;
/*     */ import javax.accessibility.AccessibleRole;
/*     */ 
/*     */ public class Panel extends Container
/*     */   implements Accessible
/*     */ {
/*     */   private static final String base = "panel";
/*  43 */   private static int nameCounter = 0;
/*     */   private static final long serialVersionUID = -2728009084054400034L;
/*     */ 
/*     */   public Panel()
/*     */   {
/*  56 */     this(new FlowLayout());
/*     */   }
/*     */ 
/*     */   public Panel(LayoutManager paramLayoutManager)
/*     */   {
/*  65 */     setLayout(paramLayoutManager);
/*     */   }
/*     */ 
/*     */   String constructComponentName()
/*     */   {
/*  73 */     synchronized (Panel.class) {
/*  74 */       return "panel" + nameCounter++;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void addNotify()
/*     */   {
/*  84 */     synchronized (getTreeLock()) {
/*  85 */       if (this.peer == null)
/*  86 */         this.peer = getToolkit().createPanel(this);
/*  87 */       super.addNotify();
/*     */     }
/*     */   }
/*     */ 
/*     */   public AccessibleContext getAccessibleContext()
/*     */   {
/* 106 */     if (this.accessibleContext == null) {
/* 107 */       this.accessibleContext = new AccessibleAWTPanel();
/*     */     }
/* 109 */     return this.accessibleContext;
/*     */   }
/*     */ 
/*     */   protected class AccessibleAWTPanel extends Container.AccessibleAWTContainer
/*     */   {
/*     */     private static final long serialVersionUID = -6409552226660031050L;
/*     */ 
/*     */     protected AccessibleAWTPanel()
/*     */     {
/* 118 */       super();
/*     */     }
/*     */ 
/*     */     public AccessibleRole getAccessibleRole()
/*     */     {
/* 129 */       return AccessibleRole.PANEL;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.Panel
 * JD-Core Version:    0.6.2
 */