/*     */ package java.awt;
/*     */ 
/*     */ import java.awt.peer.ComponentPeer;
/*     */ import java.awt.peer.LightweightPeer;
/*     */ import sun.awt.SunGraphicsCallback;
/*     */ 
/*     */ abstract class GraphicsCallback extends SunGraphicsCallback
/*     */ {
/*     */   static final class PaintAllCallback extends GraphicsCallback
/*     */   {
/*  57 */     private static PaintAllCallback instance = new PaintAllCallback();
/*     */ 
/*     */     public void run(Component paramComponent, Graphics paramGraphics)
/*     */     {
/*  61 */       paramComponent.paintAll(paramGraphics);
/*     */     }
/*     */     static PaintAllCallback getInstance() {
/*  64 */       return instance;
/*     */     }
/*     */   }
/*     */ 
/*     */   static final class PaintCallback extends GraphicsCallback
/*     */   {
/*  35 */     private static PaintCallback instance = new PaintCallback();
/*     */ 
/*     */     public void run(Component paramComponent, Graphics paramGraphics)
/*     */     {
/*  39 */       paramComponent.paint(paramGraphics);
/*     */     }
/*     */     static PaintCallback getInstance() {
/*  42 */       return instance;
/*     */     }
/*     */   }
/*     */ 
/*     */   static final class PaintHeavyweightComponentsCallback extends GraphicsCallback
/*     */   {
/* 113 */     private static PaintHeavyweightComponentsCallback instance = new PaintHeavyweightComponentsCallback();
/*     */ 
/*     */     public void run(Component paramComponent, Graphics paramGraphics)
/*     */     {
/* 118 */       if ((paramComponent.peer instanceof LightweightPeer))
/* 119 */         paramComponent.paintHeavyweightComponents(paramGraphics);
/*     */       else
/* 121 */         paramComponent.paintAll(paramGraphics);
/*     */     }
/*     */ 
/*     */     static PaintHeavyweightComponentsCallback getInstance() {
/* 125 */       return instance;
/*     */     }
/*     */   }
/*     */ 
/*     */   static final class PeerPaintCallback extends GraphicsCallback
/*     */   {
/*  79 */     private static PeerPaintCallback instance = new PeerPaintCallback();
/*     */ 
/*     */     public void run(Component paramComponent, Graphics paramGraphics)
/*     */     {
/*  83 */       paramComponent.validate();
/*  84 */       if ((paramComponent.peer instanceof LightweightPeer))
/*  85 */         paramComponent.lightweightPaint(paramGraphics);
/*     */       else
/*  87 */         paramComponent.peer.paint(paramGraphics);
/*     */     }
/*     */ 
/*     */     static PeerPaintCallback getInstance() {
/*  91 */       return instance;
/*     */     }
/*     */   }
/*     */ 
/*  95 */   static final class PeerPrintCallback extends GraphicsCallback { private static PeerPrintCallback instance = new PeerPrintCallback();
/*     */ 
/*     */     public void run(Component paramComponent, Graphics paramGraphics)
/*     */     {
/*  99 */       paramComponent.validate();
/* 100 */       if ((paramComponent.peer instanceof LightweightPeer))
/* 101 */         paramComponent.lightweightPrint(paramGraphics);
/*     */       else
/* 103 */         paramComponent.peer.print(paramGraphics);
/*     */     }
/*     */ 
/*     */     static PeerPrintCallback getInstance() {
/* 107 */       return instance;
/*     */     }
/*     */   }
/*     */ 
/*     */   static final class PrintAllCallback extends GraphicsCallback
/*     */   {
/*  68 */     private static PrintAllCallback instance = new PrintAllCallback();
/*     */ 
/*     */     public void run(Component paramComponent, Graphics paramGraphics)
/*     */     {
/*  72 */       paramComponent.printAll(paramGraphics);
/*     */     }
/*     */     static PrintAllCallback getInstance() {
/*  75 */       return instance;
/*     */     }
/*     */   }
/*     */ 
/*     */   static final class PrintCallback extends GraphicsCallback
/*     */   {
/*  46 */     private static PrintCallback instance = new PrintCallback();
/*     */ 
/*     */     public void run(Component paramComponent, Graphics paramGraphics)
/*     */     {
/*  50 */       paramComponent.print(paramGraphics);
/*     */     }
/*     */     static PrintCallback getInstance() {
/*  53 */       return instance;
/*     */     }
/*     */   }
/*     */ 
/*     */   static final class PrintHeavyweightComponentsCallback extends GraphicsCallback
/*     */   {
/* 131 */     private static PrintHeavyweightComponentsCallback instance = new PrintHeavyweightComponentsCallback();
/*     */ 
/*     */     public void run(Component paramComponent, Graphics paramGraphics)
/*     */     {
/* 136 */       if ((paramComponent.peer instanceof LightweightPeer))
/* 137 */         paramComponent.printHeavyweightComponents(paramGraphics);
/*     */       else
/* 139 */         paramComponent.printAll(paramGraphics);
/*     */     }
/*     */ 
/*     */     static PrintHeavyweightComponentsCallback getInstance() {
/* 143 */       return instance;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.GraphicsCallback
 * JD-Core Version:    0.6.2
 */