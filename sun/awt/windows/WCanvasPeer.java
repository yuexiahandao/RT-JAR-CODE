/*     */ package sun.awt.windows;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.GraphicsConfiguration;
/*     */ import java.awt.peer.CanvasPeer;
/*     */ import sun.awt.Graphics2Delegate;
/*     */ import sun.awt.PaintEventDispatcher;
/*     */ import sun.awt.SunToolkit;
/*     */ 
/*     */ class WCanvasPeer extends WComponentPeer
/*     */   implements CanvasPeer
/*     */ {
/*     */   private boolean eraseBackground;
/*     */ 
/*     */   WCanvasPeer(Component paramComponent)
/*     */   {
/*  41 */     super(paramComponent);
/*     */   }
/*     */ 
/*     */   native void create(WComponentPeer paramWComponentPeer);
/*     */ 
/*     */   void initialize() {
/*  47 */     this.eraseBackground = (!SunToolkit.getSunAwtNoerasebackground());
/*  48 */     boolean bool = SunToolkit.getSunAwtErasebackgroundonresize();
/*     */ 
/*  51 */     if (!PaintEventDispatcher.getPaintEventDispatcher().shouldDoNativeBackgroundErase((Component)this.target))
/*     */     {
/*  53 */       this.eraseBackground = false;
/*     */     }
/*  55 */     setNativeBackgroundErase(this.eraseBackground, bool);
/*  56 */     super.initialize();
/*  57 */     Color localColor = ((Component)this.target).getBackground();
/*  58 */     if (localColor != null)
/*  59 */       setBackground(localColor);
/*     */   }
/*     */ 
/*     */   public void paint(Graphics paramGraphics)
/*     */   {
/*  64 */     Dimension localDimension = ((Component)this.target).getSize();
/*  65 */     if (((paramGraphics instanceof Graphics2D)) || ((paramGraphics instanceof Graphics2Delegate)))
/*     */     {
/*  68 */       paramGraphics.clearRect(0, 0, localDimension.width, localDimension.height);
/*     */     }
/*     */     else {
/*  71 */       paramGraphics.setColor(((Component)this.target).getBackground());
/*  72 */       paramGraphics.fillRect(0, 0, localDimension.width, localDimension.height);
/*  73 */       paramGraphics.setColor(((Component)this.target).getForeground());
/*     */     }
/*  75 */     super.paint(paramGraphics);
/*     */   }
/*     */ 
/*     */   public boolean shouldClearRectBeforePaint() {
/*  79 */     return this.eraseBackground;
/*     */   }
/*     */ 
/*     */   void disableBackgroundErase()
/*     */   {
/*  87 */     this.eraseBackground = false;
/*  88 */     setNativeBackgroundErase(false, false);
/*     */   }
/*     */ 
/*     */   private native void setNativeBackgroundErase(boolean paramBoolean1, boolean paramBoolean2);
/*     */ 
/*     */   public GraphicsConfiguration getAppropriateGraphicsConfiguration(GraphicsConfiguration paramGraphicsConfiguration)
/*     */   {
/* 105 */     return paramGraphicsConfiguration;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.windows.WCanvasPeer
 * JD-Core Version:    0.6.2
 */