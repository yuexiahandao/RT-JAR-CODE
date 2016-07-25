/*     */ package java.awt;
/*     */ 
/*     */ import java.awt.peer.SystemTrayPeer;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.beans.PropertyChangeSupport;
/*     */ import java.util.Vector;
/*     */ import sun.awt.AWTAccessor;
/*     */ import sun.awt.AWTAccessor.SystemTrayAccessor;
/*     */ import sun.awt.AppContext;
/*     */ import sun.awt.HeadlessToolkit;
/*     */ import sun.awt.SunToolkit;
/*     */ import sun.security.util.SecurityConstants.AWT;
/*     */ 
/*     */ public class SystemTray
/*     */ {
/*     */   private static SystemTray systemTray;
/* 125 */   private int currentIconID = 0;
/*     */   private transient SystemTrayPeer peer;
/* 129 */   private static final TrayIcon[] EMPTY_TRAY_ARRAY = new TrayIcon[0];
/*     */ 
/*     */   private SystemTray()
/*     */   {
/* 148 */     addNotify();
/*     */   }
/*     */ 
/*     */   public static SystemTray getSystemTray()
/*     */   {
/* 178 */     checkSystemTrayAllowed();
/* 179 */     if (GraphicsEnvironment.isHeadless()) {
/* 180 */       throw new HeadlessException();
/*     */     }
/*     */ 
/* 183 */     initializeSystemTrayIfNeeded();
/*     */ 
/* 185 */     if (!isSupported()) {
/* 186 */       throw new UnsupportedOperationException("The system tray is not supported on the current platform.");
/*     */     }
/*     */ 
/* 190 */     return systemTray;
/*     */   }
/*     */ 
/*     */   public static boolean isSupported()
/*     */   {
/* 219 */     Toolkit localToolkit = Toolkit.getDefaultToolkit();
/* 220 */     if ((localToolkit instanceof SunToolkit))
/*     */     {
/* 222 */       initializeSystemTrayIfNeeded();
/* 223 */       return ((SunToolkit)localToolkit).isTraySupported();
/* 224 */     }if ((localToolkit instanceof HeadlessToolkit))
/*     */     {
/* 227 */       return ((HeadlessToolkit)localToolkit).isTraySupported();
/*     */     }
/* 229 */     return false;
/*     */   }
/*     */ 
/*     */   public void add(TrayIcon paramTrayIcon)
/*     */     throws AWTException
/*     */   {
/* 255 */     if (paramTrayIcon == null) {
/* 256 */       throw new NullPointerException("adding null TrayIcon");
/*     */     }
/* 258 */     TrayIcon[] arrayOfTrayIcon1 = null; TrayIcon[] arrayOfTrayIcon2 = null;
/* 259 */     Vector localVector = null;
/* 260 */     synchronized (this) {
/* 261 */       arrayOfTrayIcon1 = systemTray.getTrayIcons();
/* 262 */       localVector = (Vector)AppContext.getAppContext().get(TrayIcon.class);
/* 263 */       if (localVector == null) {
/* 264 */         localVector = new Vector(3);
/* 265 */         AppContext.getAppContext().put(TrayIcon.class, localVector);
/*     */       }
/* 267 */       else if (localVector.contains(paramTrayIcon)) {
/* 268 */         throw new IllegalArgumentException("adding TrayIcon that is already added");
/*     */       }
/* 270 */       localVector.add(paramTrayIcon);
/* 271 */       arrayOfTrayIcon2 = systemTray.getTrayIcons();
/*     */ 
/* 273 */       paramTrayIcon.setID(++this.currentIconID);
/*     */     }
/*     */     try {
/* 276 */       paramTrayIcon.addNotify();
/*     */     } catch (AWTException localAWTException) {
/* 278 */       localVector.remove(paramTrayIcon);
/* 279 */       throw localAWTException;
/*     */     }
/* 281 */     firePropertyChange("trayIcons", arrayOfTrayIcon1, arrayOfTrayIcon2);
/*     */   }
/*     */ 
/*     */   public void remove(TrayIcon paramTrayIcon)
/*     */   {
/* 301 */     if (paramTrayIcon == null) {
/* 302 */       return;
/*     */     }
/* 304 */     TrayIcon[] arrayOfTrayIcon1 = null; TrayIcon[] arrayOfTrayIcon2 = null;
/* 305 */     synchronized (this) {
/* 306 */       arrayOfTrayIcon1 = systemTray.getTrayIcons();
/* 307 */       Vector localVector = (Vector)AppContext.getAppContext().get(TrayIcon.class);
/*     */ 
/* 309 */       if ((localVector == null) || (!localVector.remove(paramTrayIcon))) {
/* 310 */         return;
/*     */       }
/* 312 */       paramTrayIcon.removeNotify();
/* 313 */       arrayOfTrayIcon2 = systemTray.getTrayIcons();
/*     */     }
/* 315 */     firePropertyChange("trayIcons", arrayOfTrayIcon1, arrayOfTrayIcon2);
/*     */   }
/*     */ 
/*     */   public TrayIcon[] getTrayIcons()
/*     */   {
/* 338 */     Vector localVector = (Vector)AppContext.getAppContext().get(TrayIcon.class);
/* 339 */     if (localVector != null) {
/* 340 */       return (TrayIcon[])localVector.toArray(new TrayIcon[localVector.size()]);
/*     */     }
/* 342 */     return EMPTY_TRAY_ARRAY;
/*     */   }
/*     */ 
/*     */   public Dimension getTrayIconSize()
/*     */   {
/* 358 */     return this.peer.getTrayIconSize();
/*     */   }
/*     */ 
/*     */   public synchronized void addPropertyChangeListener(String paramString, PropertyChangeListener paramPropertyChangeListener)
/*     */   {
/* 402 */     if (paramPropertyChangeListener == null) {
/* 403 */       return;
/*     */     }
/* 405 */     getCurrentChangeSupport().addPropertyChangeListener(paramString, paramPropertyChangeListener);
/*     */   }
/*     */ 
/*     */   public synchronized void removePropertyChangeListener(String paramString, PropertyChangeListener paramPropertyChangeListener)
/*     */   {
/* 426 */     if (paramPropertyChangeListener == null) {
/* 427 */       return;
/*     */     }
/* 429 */     getCurrentChangeSupport().removePropertyChangeListener(paramString, paramPropertyChangeListener);
/*     */   }
/*     */ 
/*     */   public synchronized PropertyChangeListener[] getPropertyChangeListeners(String paramString)
/*     */   {
/* 448 */     return getCurrentChangeSupport().getPropertyChangeListeners(paramString);
/*     */   }
/*     */ 
/*     */   private void firePropertyChange(String paramString, Object paramObject1, Object paramObject2)
/*     */   {
/* 469 */     if ((paramObject1 != null) && (paramObject2 != null) && (paramObject1.equals(paramObject2))) {
/* 470 */       return;
/*     */     }
/* 472 */     getCurrentChangeSupport().firePropertyChange(paramString, paramObject1, paramObject2);
/*     */   }
/*     */ 
/*     */   private synchronized PropertyChangeSupport getCurrentChangeSupport()
/*     */   {
/* 482 */     PropertyChangeSupport localPropertyChangeSupport = (PropertyChangeSupport)AppContext.getAppContext().get(SystemTray.class);
/*     */ 
/* 485 */     if (localPropertyChangeSupport == null) {
/* 486 */       localPropertyChangeSupport = new PropertyChangeSupport(this);
/* 487 */       AppContext.getAppContext().put(SystemTray.class, localPropertyChangeSupport);
/*     */     }
/* 489 */     return localPropertyChangeSupport;
/*     */   }
/*     */ 
/*     */   synchronized void addNotify() {
/* 493 */     if (this.peer == null) {
/* 494 */       Toolkit localToolkit = Toolkit.getDefaultToolkit();
/* 495 */       if ((localToolkit instanceof SunToolkit))
/* 496 */         this.peer = ((SunToolkit)Toolkit.getDefaultToolkit()).createSystemTray(this);
/* 497 */       else if ((localToolkit instanceof HeadlessToolkit))
/* 498 */         this.peer = ((HeadlessToolkit)Toolkit.getDefaultToolkit()).createSystemTray(this);
/*     */     }
/*     */   }
/*     */ 
/*     */   static void checkSystemTrayAllowed()
/*     */   {
/* 504 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 505 */     if (localSecurityManager != null)
/* 506 */       localSecurityManager.checkPermission(SecurityConstants.AWT.ACCESS_SYSTEM_TRAY_PERMISSION);
/*     */   }
/*     */ 
/*     */   private static void initializeSystemTrayIfNeeded()
/*     */   {
/* 511 */     synchronized (SystemTray.class) {
/* 512 */       if (systemTray == null)
/* 513 */         systemTray = new SystemTray();
/*     */     }
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 132 */     AWTAccessor.setSystemTrayAccessor(new AWTAccessor.SystemTrayAccessor()
/*     */     {
/*     */       public void firePropertyChange(SystemTray paramAnonymousSystemTray, String paramAnonymousString, Object paramAnonymousObject1, Object paramAnonymousObject2)
/*     */       {
/* 138 */         paramAnonymousSystemTray.firePropertyChange(paramAnonymousString, paramAnonymousObject1, paramAnonymousObject2);
/*     */       }
/*     */     });
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.SystemTray
 * JD-Core Version:    0.6.2
 */