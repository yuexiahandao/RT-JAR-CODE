/*     */ package java.awt;
/*     */ 
/*     */ import java.awt.image.BufferStrategy;
/*     */ import java.awt.peer.CanvasPeer;
/*     */ import javax.accessibility.Accessible;
/*     */ import javax.accessibility.AccessibleContext;
/*     */ import javax.accessibility.AccessibleRole;
/*     */ 
/*     */ public class Canvas extends Component
/*     */   implements Accessible
/*     */ {
/*     */   private static final String base = "canvas";
/*  47 */   private static int nameCounter = 0;
/*     */   private static final long serialVersionUID = -2284879212465893870L;
/*     */ 
/*     */   public Canvas()
/*     */   {
/*     */   }
/*     */ 
/*     */   public Canvas(GraphicsConfiguration paramGraphicsConfiguration)
/*     */   {
/*  68 */     this();
/*  69 */     setGraphicsConfiguration(paramGraphicsConfiguration);
/*     */   }
/*     */ 
/*     */   void setGraphicsConfiguration(GraphicsConfiguration paramGraphicsConfiguration)
/*     */   {
/*  74 */     synchronized (getTreeLock()) {
/*  75 */       CanvasPeer localCanvasPeer = (CanvasPeer)getPeer();
/*  76 */       if (localCanvasPeer != null) {
/*  77 */         paramGraphicsConfiguration = localCanvasPeer.getAppropriateGraphicsConfiguration(paramGraphicsConfiguration);
/*     */       }
/*  79 */       super.setGraphicsConfiguration(paramGraphicsConfiguration);
/*     */     }
/*     */   }
/*     */ 
/*     */   String constructComponentName()
/*     */   {
/*  88 */     synchronized (Canvas.class) {
/*  89 */       return "canvas" + nameCounter++;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void addNotify()
/*     */   {
/* 100 */     synchronized (getTreeLock()) {
/* 101 */       if (this.peer == null)
/* 102 */         this.peer = getToolkit().createCanvas(this);
/* 103 */       super.addNotify();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void paint(Graphics paramGraphics)
/*     */   {
/* 122 */     paramGraphics.clearRect(0, 0, this.width, this.height);
/*     */   }
/*     */ 
/*     */   public void update(Graphics paramGraphics)
/*     */   {
/* 141 */     paramGraphics.clearRect(0, 0, this.width, this.height);
/* 142 */     paint(paramGraphics);
/*     */   }
/*     */ 
/*     */   boolean postsOldMouseEvents() {
/* 146 */     return true;
/*     */   }
/*     */ 
/*     */   public void createBufferStrategy(int paramInt)
/*     */   {
/* 169 */     super.createBufferStrategy(paramInt);
/*     */   }
/*     */ 
/*     */   public void createBufferStrategy(int paramInt, BufferCapabilities paramBufferCapabilities)
/*     */     throws AWTException
/*     */   {
/* 194 */     super.createBufferStrategy(paramInt, paramBufferCapabilities);
/*     */   }
/*     */ 
/*     */   public BufferStrategy getBufferStrategy()
/*     */   {
/* 207 */     return super.getBufferStrategy();
/*     */   }
/*     */ 
/*     */   public AccessibleContext getAccessibleContext()
/*     */   {
/* 226 */     if (this.accessibleContext == null) {
/* 227 */       this.accessibleContext = new AccessibleAWTCanvas();
/*     */     }
/* 229 */     return this.accessibleContext;
/*     */   }
/*     */ 
/*     */   protected class AccessibleAWTCanvas extends Component.AccessibleAWTComponent
/*     */   {
/*     */     private static final long serialVersionUID = -6325592262103146699L;
/*     */ 
/*     */     protected AccessibleAWTCanvas()
/*     */     {
/* 238 */       super();
/*     */     }
/*     */ 
/*     */     public AccessibleRole getAccessibleRole()
/*     */     {
/* 250 */       return AccessibleRole.CANVAS;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.Canvas
 * JD-Core Version:    0.6.2
 */