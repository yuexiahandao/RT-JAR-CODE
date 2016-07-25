/*     */ package java.awt;
/*     */ 
/*     */ import java.awt.event.InputEvent;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.awt.image.DataBufferInt;
/*     */ import java.awt.image.DirectColorModel;
/*     */ import java.awt.image.Raster;
/*     */ import java.awt.image.WritableRaster;
/*     */ import java.awt.peer.RobotPeer;
/*     */ import java.io.PrintStream;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import sun.awt.ComponentFactory;
/*     */ import sun.awt.SunToolkit;
/*     */ import sun.awt.image.SunWritableRaster;
/*     */ import sun.java2d.Disposer;
/*     */ import sun.java2d.DisposerRecord;
/*     */ import sun.security.util.SecurityConstants.AWT;
/*     */ 
/*     */ public class Robot
/*     */ {
/*     */   private static final int MAX_DELAY = 60000;
/*     */   private RobotPeer peer;
/*  71 */   private boolean isAutoWaitForIdle = false;
/*  72 */   private int autoDelay = 0;
/*  73 */   private static int LEGAL_BUTTON_MASK = 0;
/*     */   private Point gdLoc;
/*  78 */   private DirectColorModel screenCapCM = null;
/*     */ 
/* 181 */   private transient Object anchor = new Object();
/*     */   private transient RobotDisposer disposer;
/*     */ 
/*     */   public Robot()
/*     */     throws AWTException
/*     */   {
/*  93 */     if (GraphicsEnvironment.isHeadless()) {
/*  94 */       throw new AWTException("headless environment");
/*     */     }
/*  96 */     init(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice());
/*     */   }
/*     */ 
/*     */   public Robot(GraphicsDevice paramGraphicsDevice)
/*     */     throws AWTException
/*     */   {
/* 129 */     checkIsScreenDevice(paramGraphicsDevice);
/* 130 */     init(paramGraphicsDevice);
/*     */   }
/*     */ 
/*     */   private void init(GraphicsDevice paramGraphicsDevice) throws AWTException {
/* 134 */     checkRobotAllowed();
/* 135 */     this.gdLoc = paramGraphicsDevice.getDefaultConfiguration().getBounds().getLocation();
/* 136 */     Toolkit localToolkit = Toolkit.getDefaultToolkit();
/* 137 */     if ((localToolkit instanceof ComponentFactory)) {
/* 138 */       this.peer = ((ComponentFactory)localToolkit).createRobot(this, paramGraphicsDevice);
/* 139 */       this.disposer = new RobotDisposer(this.peer);
/* 140 */       Disposer.addRecord(this.anchor, this.disposer);
/*     */     }
/* 142 */     initLegalButtonMask();
/*     */   }
/*     */ 
/*     */   private static synchronized void initLegalButtonMask() {
/* 146 */     if (LEGAL_BUTTON_MASK != 0) return;
/*     */ 
/* 148 */     int i = 0;
/* 149 */     if ((Toolkit.getDefaultToolkit().areExtraMouseButtonsEnabled()) && 
/* 150 */       ((Toolkit.getDefaultToolkit() instanceof SunToolkit))) {
/* 151 */       int j = ((SunToolkit)Toolkit.getDefaultToolkit()).getNumberOfButtons();
/* 152 */       for (int k = 0; k < j; k++) {
/* 153 */         i |= InputEvent.getMaskForButton(k + 1);
/*     */       }
/*     */     }
/*     */ 
/* 157 */     i |= 7196;
/*     */ 
/* 163 */     LEGAL_BUTTON_MASK = i;
/*     */   }
/*     */ 
/*     */   private void checkRobotAllowed()
/*     */   {
/* 168 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 169 */     if (localSecurityManager != null)
/* 170 */       localSecurityManager.checkPermission(SecurityConstants.AWT.CREATE_ROBOT_PERMISSION);
/*     */   }
/*     */ 
/*     */   private void checkIsScreenDevice(GraphicsDevice paramGraphicsDevice)
/*     */   {
/* 176 */     if ((paramGraphicsDevice == null) || (paramGraphicsDevice.getType() != 0))
/* 177 */       throw new IllegalArgumentException("not a valid screen device");
/*     */   }
/*     */ 
/*     */   public synchronized void mouseMove(int paramInt1, int paramInt2)
/*     */   {
/* 203 */     this.peer.mouseMove(this.gdLoc.x + paramInt1, this.gdLoc.y + paramInt2);
/* 204 */     afterEvent();
/*     */   }
/*     */ 
/*     */   public synchronized void mousePress(int paramInt)
/*     */   {
/* 260 */     checkButtonsArgument(paramInt);
/* 261 */     this.peer.mousePress(paramInt);
/* 262 */     afterEvent();
/*     */   }
/*     */ 
/*     */   public synchronized void mouseRelease(int paramInt)
/*     */   {
/* 317 */     checkButtonsArgument(paramInt);
/* 318 */     this.peer.mouseRelease(paramInt);
/* 319 */     afterEvent();
/*     */   }
/*     */ 
/*     */   private void checkButtonsArgument(int paramInt) {
/* 323 */     if ((paramInt | LEGAL_BUTTON_MASK) != LEGAL_BUTTON_MASK)
/* 324 */       throw new IllegalArgumentException("Invalid combination of button flags");
/*     */   }
/*     */ 
/*     */   public synchronized void mouseWheel(int paramInt)
/*     */   {
/* 338 */     this.peer.mouseWheel(paramInt);
/* 339 */     afterEvent();
/*     */   }
/*     */ 
/*     */   public synchronized void keyPress(int paramInt)
/*     */   {
/* 357 */     checkKeycodeArgument(paramInt);
/* 358 */     this.peer.keyPress(paramInt);
/* 359 */     afterEvent();
/*     */   }
/*     */ 
/*     */   public synchronized void keyRelease(int paramInt)
/*     */   {
/* 376 */     checkKeycodeArgument(paramInt);
/* 377 */     this.peer.keyRelease(paramInt);
/* 378 */     afterEvent();
/*     */   }
/*     */ 
/*     */   private void checkKeycodeArgument(int paramInt)
/*     */   {
/* 386 */     if (paramInt == 0)
/* 387 */       throw new IllegalArgumentException("Invalid key code");
/*     */   }
/*     */ 
/*     */   public synchronized Color getPixelColor(int paramInt1, int paramInt2)
/*     */   {
/* 398 */     Color localColor = new Color(this.peer.getRGBPixel(this.gdLoc.x + paramInt1, this.gdLoc.y + paramInt2));
/* 399 */     return localColor;
/*     */   }
/*     */ 
/*     */   public synchronized BufferedImage createScreenCapture(Rectangle paramRectangle)
/*     */   {
/* 413 */     checkScreenCaptureAllowed();
/*     */ 
/* 416 */     Rectangle localRectangle = new Rectangle(paramRectangle);
/* 417 */     localRectangle.translate(this.gdLoc.x, this.gdLoc.y);
/* 418 */     checkValidRect(localRectangle);
/*     */ 
/* 424 */     if (this.screenCapCM == null)
/*     */     {
/* 431 */       this.screenCapCM = new DirectColorModel(24, 16711680, 65280, 255);
/*     */     }
/*     */ 
/* 439 */     Toolkit.getDefaultToolkit().sync();
/*     */ 
/* 442 */     int[] arrayOfInt2 = new int[3];
/*     */ 
/* 444 */     int[] arrayOfInt1 = this.peer.getRGBPixels(localRectangle);
/* 445 */     DataBufferInt localDataBufferInt = new DataBufferInt(arrayOfInt1, arrayOfInt1.length);
/*     */ 
/* 447 */     arrayOfInt2[0] = this.screenCapCM.getRedMask();
/* 448 */     arrayOfInt2[1] = this.screenCapCM.getGreenMask();
/* 449 */     arrayOfInt2[2] = this.screenCapCM.getBlueMask();
/*     */ 
/* 451 */     WritableRaster localWritableRaster = Raster.createPackedRaster(localDataBufferInt, localRectangle.width, localRectangle.height, localRectangle.width, arrayOfInt2, null);
/* 452 */     SunWritableRaster.makeTrackable(localDataBufferInt);
/*     */ 
/* 454 */     BufferedImage localBufferedImage = new BufferedImage(this.screenCapCM, localWritableRaster, false, null);
/*     */ 
/* 456 */     return localBufferedImage;
/*     */   }
/*     */ 
/*     */   private static void checkValidRect(Rectangle paramRectangle) {
/* 460 */     if ((paramRectangle.width <= 0) || (paramRectangle.height <= 0))
/* 461 */       throw new IllegalArgumentException("Rectangle width and height must be > 0");
/*     */   }
/*     */ 
/*     */   private static void checkScreenCaptureAllowed()
/*     */   {
/* 466 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 467 */     if (localSecurityManager != null)
/* 468 */       localSecurityManager.checkPermission(SecurityConstants.AWT.READ_DISPLAY_PIXELS_PERMISSION);
/*     */   }
/*     */ 
/*     */   private void afterEvent()
/*     */   {
/* 477 */     autoWaitForIdle();
/* 478 */     autoDelay();
/*     */   }
/*     */ 
/*     */   public synchronized boolean isAutoWaitForIdle()
/*     */   {
/* 487 */     return this.isAutoWaitForIdle;
/*     */   }
/*     */ 
/*     */   public synchronized void setAutoWaitForIdle(boolean paramBoolean)
/*     */   {
/* 496 */     this.isAutoWaitForIdle = paramBoolean;
/*     */   }
/*     */ 
/*     */   private void autoWaitForIdle()
/*     */   {
/* 503 */     if (this.isAutoWaitForIdle)
/* 504 */       waitForIdle();
/*     */   }
/*     */ 
/*     */   public synchronized int getAutoDelay()
/*     */   {
/* 512 */     return this.autoDelay;
/*     */   }
/*     */ 
/*     */   public synchronized void setAutoDelay(int paramInt)
/*     */   {
/* 520 */     checkDelayArgument(paramInt);
/* 521 */     this.autoDelay = paramInt;
/*     */   }
/*     */ 
/*     */   private void autoDelay()
/*     */   {
/* 528 */     delay(this.autoDelay);
/*     */   }
/*     */ 
/*     */   public synchronized void delay(int paramInt)
/*     */   {
/* 540 */     checkDelayArgument(paramInt);
/*     */     try {
/* 542 */       Thread.sleep(paramInt);
/*     */     } catch (InterruptedException localInterruptedException) {
/* 544 */       localInterruptedException.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   private void checkDelayArgument(int paramInt) {
/* 549 */     if ((paramInt < 0) || (paramInt > 60000))
/* 550 */       throw new IllegalArgumentException("Delay must be to 0 to 60,000ms");
/*     */   }
/*     */ 
/*     */   public synchronized void waitForIdle()
/*     */   {
/* 559 */     checkNotDispatchThread();
/*     */     try
/*     */     {
/* 563 */       SunToolkit.flushPendingEvents();
/* 564 */       EventQueue.invokeAndWait(new Runnable() {
/*     */         public void run() {
/*     */         }
/*     */       });
/*     */     }
/*     */     catch (InterruptedException localInterruptedException) {
/* 570 */       System.err.println("Robot.waitForIdle, non-fatal exception caught:");
/* 571 */       localInterruptedException.printStackTrace();
/*     */     } catch (InvocationTargetException localInvocationTargetException) {
/* 573 */       System.err.println("Robot.waitForIdle, non-fatal exception caught:");
/* 574 */       localInvocationTargetException.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   private void checkNotDispatchThread() {
/* 579 */     if (EventQueue.isDispatchThread())
/* 580 */       throw new IllegalThreadStateException("Cannot call method from the event dispatcher thread");
/*     */   }
/*     */ 
/*     */   public synchronized String toString()
/*     */   {
/* 590 */     String str = "autoDelay = " + getAutoDelay() + ", " + "autoWaitForIdle = " + isAutoWaitForIdle();
/* 591 */     return getClass().getName() + "[ " + str + " ]";
/*     */   }
/*     */ 
/*     */   static class RobotDisposer
/*     */     implements DisposerRecord
/*     */   {
/*     */     private final RobotPeer peer;
/*     */ 
/*     */     public RobotDisposer(RobotPeer paramRobotPeer)
/*     */     {
/* 186 */       this.peer = paramRobotPeer;
/*     */     }
/*     */     public void dispose() {
/* 189 */       if (this.peer != null)
/* 190 */         this.peer.dispose();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.Robot
 * JD-Core Version:    0.6.2
 */