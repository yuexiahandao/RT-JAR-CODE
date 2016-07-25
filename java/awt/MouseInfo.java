/*     */ package java.awt;
/*     */ 
/*     */ import java.awt.peer.MouseInfoPeer;
/*     */ import sun.security.util.SecurityConstants.AWT;
/*     */ 
/*     */ public class MouseInfo
/*     */ {
/*     */   public static PointerInfo getPointerInfo()
/*     */     throws HeadlessException
/*     */   {
/*  73 */     if (GraphicsEnvironment.isHeadless()) {
/*  74 */       throw new HeadlessException();
/*     */     }
/*     */ 
/*  77 */     SecurityManager localSecurityManager = System.getSecurityManager();
/*  78 */     if (localSecurityManager != null) {
/*  79 */       localSecurityManager.checkPermission(SecurityConstants.AWT.WATCH_MOUSE_PERMISSION);
/*     */     }
/*     */ 
/*  82 */     Point localPoint = new Point(0, 0);
/*  83 */     int i = Toolkit.getDefaultToolkit().getMouseInfoPeer().fillPointWithCoords(localPoint);
/*  84 */     GraphicsDevice[] arrayOfGraphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
/*     */ 
/*  86 */     PointerInfo localPointerInfo = null;
/*  87 */     if (areScreenDevicesIndependent(arrayOfGraphicsDevice))
/*  88 */       localPointerInfo = new PointerInfo(arrayOfGraphicsDevice[i], localPoint);
/*     */     else {
/*  90 */       for (int j = 0; j < arrayOfGraphicsDevice.length; j++) {
/*  91 */         GraphicsConfiguration localGraphicsConfiguration = arrayOfGraphicsDevice[j].getDefaultConfiguration();
/*  92 */         Rectangle localRectangle = localGraphicsConfiguration.getBounds();
/*  93 */         if (localRectangle.contains(localPoint)) {
/*  94 */           localPointerInfo = new PointerInfo(arrayOfGraphicsDevice[j], localPoint);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/*  99 */     return localPointerInfo;
/*     */   }
/*     */ 
/*     */   private static boolean areScreenDevicesIndependent(GraphicsDevice[] paramArrayOfGraphicsDevice) {
/* 103 */     for (int i = 0; i < paramArrayOfGraphicsDevice.length; i++) {
/* 104 */       Rectangle localRectangle = paramArrayOfGraphicsDevice[i].getDefaultConfiguration().getBounds();
/* 105 */       if ((localRectangle.x != 0) || (localRectangle.y != 0)) {
/* 106 */         return false;
/*     */       }
/*     */     }
/* 109 */     return true;
/*     */   }
/*     */ 
/*     */   public static int getNumberOfButtons()
/*     */     throws HeadlessException
/*     */   {
/* 121 */     if (GraphicsEnvironment.isHeadless()) {
/* 122 */       throw new HeadlessException();
/*     */     }
/* 124 */     Object localObject = Toolkit.getDefaultToolkit().getDesktopProperty("awt.mouse.numButtons");
/*     */ 
/* 126 */     if ((localObject instanceof Integer)) {
/* 127 */       return ((Integer)localObject).intValue();
/*     */     }
/*     */ 
/* 131 */     if (!$assertionsDisabled) throw new AssertionError("awt.mouse.numButtons is not an integer property");
/* 132 */     return 0;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.MouseInfo
 * JD-Core Version:    0.6.2
 */