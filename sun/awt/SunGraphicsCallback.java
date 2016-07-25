/*     */ package sun.awt;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Shape;
/*     */ import sun.util.logging.PlatformLogger;
/*     */ 
/*     */ public abstract class SunGraphicsCallback
/*     */ {
/*     */   public static final int HEAVYWEIGHTS = 1;
/*     */   public static final int LIGHTWEIGHTS = 2;
/*     */   public static final int TWO_PASSES = 4;
/*  37 */   private static final PlatformLogger log = PlatformLogger.getLogger("sun.awt.SunGraphicsCallback");
/*     */ 
/*     */   public abstract void run(Component paramComponent, Graphics paramGraphics);
/*     */ 
/*     */   protected void constrainGraphics(Graphics paramGraphics, Rectangle paramRectangle) {
/*  42 */     if ((paramGraphics instanceof ConstrainableGraphics))
/*  43 */       ((ConstrainableGraphics)paramGraphics).constrain(paramRectangle.x, paramRectangle.y, paramRectangle.width, paramRectangle.height);
/*     */     else {
/*  45 */       paramGraphics.translate(paramRectangle.x, paramRectangle.y);
/*     */     }
/*  47 */     paramGraphics.clipRect(0, 0, paramRectangle.width, paramRectangle.height);
/*     */   }
/*     */ 
/*     */   public final void runOneComponent(Component paramComponent, Rectangle paramRectangle, Graphics paramGraphics, Shape paramShape, int paramInt)
/*     */   {
/*  53 */     if ((paramComponent == null) || (paramComponent.getPeer() == null) || (!paramComponent.isVisible())) {
/*  54 */       return;
/*     */     }
/*  56 */     boolean bool = paramComponent.isLightweight();
/*  57 */     if (((bool) && ((paramInt & 0x2) == 0)) || ((!bool) && ((paramInt & 0x1) == 0)))
/*     */     {
/*  59 */       return;
/*     */     }
/*     */ 
/*  62 */     if (paramRectangle == null) {
/*  63 */       paramRectangle = paramComponent.getBounds();
/*     */     }
/*     */ 
/*  66 */     if ((paramShape == null) || (paramShape.intersects(paramRectangle))) {
/*  67 */       Graphics localGraphics = paramGraphics.create();
/*     */       try {
/*  69 */         constrainGraphics(localGraphics, paramRectangle);
/*  70 */         localGraphics.setFont(paramComponent.getFont());
/*  71 */         localGraphics.setColor(paramComponent.getForeground());
/*  72 */         if ((localGraphics instanceof Graphics2D))
/*  73 */           ((Graphics2D)localGraphics).setBackground(paramComponent.getBackground());
/*  74 */         else if ((localGraphics instanceof Graphics2Delegate)) {
/*  75 */           ((Graphics2Delegate)localGraphics).setBackground(paramComponent.getBackground());
/*     */         }
/*     */ 
/*  78 */         run(paramComponent, localGraphics);
/*     */       } finally {
/*  80 */         localGraphics.dispose();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void runComponents(Component[] paramArrayOfComponent, Graphics paramGraphics, int paramInt)
/*     */   {
/*  87 */     int i = paramArrayOfComponent.length;
/*  88 */     Shape localShape = paramGraphics.getClip();
/*     */ 
/*  90 */     if ((log.isLoggable(400)) && (localShape != null)) {
/*  91 */       Rectangle localRectangle = localShape.getBounds();
/*  92 */       log.finer("x = " + localRectangle.x + ", y = " + localRectangle.y + ", width = " + localRectangle.width + ", height = " + localRectangle.height);
/*     */     }
/*     */     int j;
/* 106 */     if ((paramInt & 0x4) != 0) {
/* 107 */       for (j = i - 1; j >= 0; j--) {
/* 108 */         runOneComponent(paramArrayOfComponent[j], null, paramGraphics, localShape, 2);
/*     */       }
/* 110 */       for (j = i - 1; j >= 0; j--)
/* 111 */         runOneComponent(paramArrayOfComponent[j], null, paramGraphics, localShape, 1);
/*     */     }
/*     */     else {
/* 114 */       for (j = i - 1; j >= 0; j--)
/* 115 */         runOneComponent(paramArrayOfComponent[j], null, paramGraphics, localShape, paramInt);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static final class PaintHeavyweightComponentsCallback extends SunGraphicsCallback
/*     */   {
/* 123 */     private static PaintHeavyweightComponentsCallback instance = new PaintHeavyweightComponentsCallback();
/*     */ 
/*     */     public void run(Component paramComponent, Graphics paramGraphics)
/*     */     {
/* 128 */       if (!paramComponent.isLightweight())
/* 129 */         paramComponent.paintAll(paramGraphics);
/* 130 */       else if ((paramComponent instanceof Container))
/* 131 */         runComponents(((Container)paramComponent).getComponents(), paramGraphics, 3);
/*     */     }
/*     */ 
/*     */     public static PaintHeavyweightComponentsCallback getInstance()
/*     */     {
/* 136 */       return instance;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static final class PrintHeavyweightComponentsCallback extends SunGraphicsCallback
/*     */   {
/* 142 */     private static PrintHeavyweightComponentsCallback instance = new PrintHeavyweightComponentsCallback();
/*     */ 
/*     */     public void run(Component paramComponent, Graphics paramGraphics)
/*     */     {
/* 147 */       if (!paramComponent.isLightweight())
/* 148 */         paramComponent.printAll(paramGraphics);
/* 149 */       else if ((paramComponent instanceof Container))
/* 150 */         runComponents(((Container)paramComponent).getComponents(), paramGraphics, 3);
/*     */     }
/*     */ 
/*     */     public static PrintHeavyweightComponentsCallback getInstance()
/*     */     {
/* 155 */       return instance;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.SunGraphicsCallback
 * JD-Core Version:    0.6.2
 */