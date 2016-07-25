/*     */ package sun.awt.windows;
/*     */ 
/*     */ import java.awt.AWTEvent;
/*     */ import java.awt.Font;
/*     */ import java.awt.MenuItem;
/*     */ import java.awt.MenuShortcut;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.peer.MenuItemPeer;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.MissingResourceException;
/*     */ import java.util.ResourceBundle;
/*     */ import sun.util.logging.PlatformLogger;
/*     */ 
/*     */ class WMenuItemPeer extends WObjectPeer
/*     */   implements MenuItemPeer
/*     */ {
/*  37 */   private static final PlatformLogger log = PlatformLogger.getLogger("sun.awt.WMenuItemPeer");
/*     */   String shortcutLabel;
/*     */   protected WMenuPeer parent;
/*     */   private final boolean isCheckbox;
/* 161 */   private static Font defaultMenuFont = (Font)AccessController.doPrivileged(new PrivilegedAction()
/*     */   {
/*     */     public Object run() {
/*     */       try {
/* 165 */         ResourceBundle localResourceBundle = ResourceBundle.getBundle("sun.awt.windows.awtLocalization");
/* 166 */         return Font.decode(localResourceBundle.getString("menuFont"));
/*     */       } catch (MissingResourceException localMissingResourceException) {
/* 168 */         if (WMenuItemPeer.log.isLoggable(500))
/* 169 */           WMenuItemPeer.log.fine("WMenuItemPeer: " + localMissingResourceException.getMessage() + ". Using default MenuItem font.", localMissingResourceException);
/*     */       }
/* 171 */       return new Font("SanSerif", 0, 11);
/*     */     }
/*     */   });
/*     */ 
/*     */   private synchronized native void _dispose();
/*     */ 
/*     */   protected void disposeImpl()
/*     */   {
/*  52 */     WToolkit.targetDisposedPeer(this.target, this);
/*  53 */     _dispose();
/*     */   }
/*     */ 
/*     */   public void setEnabled(boolean paramBoolean) {
/*  57 */     enable(paramBoolean);
/*     */   }
/*     */ 
/*     */   public void enable()
/*     */   {
/*  64 */     enable(true);
/*     */   }
/*     */ 
/*     */   public void disable()
/*     */   {
/*  71 */     enable(false);
/*     */   }
/*     */ 
/*     */   public void readShortcutLabel()
/*     */   {
/*  76 */     WMenuPeer localWMenuPeer = this.parent;
/*  77 */     while ((localWMenuPeer != null) && (!(localWMenuPeer instanceof WMenuBarPeer))) {
/*  78 */       localWMenuPeer = localWMenuPeer.parent;
/*     */     }
/*  80 */     if ((localWMenuPeer instanceof WMenuBarPeer)) {
/*  81 */       MenuShortcut localMenuShortcut = ((MenuItem)this.target).getShortcut();
/*  82 */       this.shortcutLabel = (localMenuShortcut != null ? localMenuShortcut.toString() : null);
/*     */     } else {
/*  84 */       this.shortcutLabel = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setLabel(String paramString)
/*     */   {
/*  90 */     readShortcutLabel();
/*  91 */     _setLabel(paramString);
/*     */   }
/*     */ 
/*     */   public native void _setLabel(String paramString);
/*     */ 
/*     */   protected WMenuItemPeer()
/*     */   {
/* 100 */     this.isCheckbox = false;
/*     */   }
/*     */   WMenuItemPeer(MenuItem paramMenuItem) {
/* 103 */     this(paramMenuItem, false);
/*     */   }
/*     */ 
/*     */   WMenuItemPeer(MenuItem paramMenuItem, boolean paramBoolean) {
/* 107 */     this.target = paramMenuItem;
/* 108 */     this.parent = ((WMenuPeer)WToolkit.targetToPeer(paramMenuItem.getParent()));
/* 109 */     this.isCheckbox = paramBoolean;
/* 110 */     create(this.parent);
/*     */ 
/* 112 */     checkMenuCreation();
/*     */ 
/* 114 */     readShortcutLabel();
/*     */   }
/*     */ 
/*     */   protected void checkMenuCreation()
/*     */   {
/* 120 */     if (this.pData == 0L)
/*     */     {
/* 122 */       if (this.createError != null)
/*     */       {
/* 124 */         throw this.createError;
/*     */       }
/*     */ 
/* 128 */       throw new InternalError("couldn't create menu peer");
/*     */     }
/*     */   }
/*     */ 
/*     */   void postEvent(AWTEvent paramAWTEvent)
/*     */   {
/* 138 */     WToolkit.postEvent(WToolkit.targetToAppContext(this.target), paramAWTEvent);
/*     */   }
/*     */ 
/*     */   native void create(WMenuPeer paramWMenuPeer);
/*     */ 
/*     */   native void enable(boolean paramBoolean);
/*     */ 
/*     */   void handleAction(final long paramLong, int paramInt)
/*     */   {
/* 148 */     WToolkit.executeOnEventHandlerThread(this.target, new Runnable() {
/*     */       public void run() {
/* 150 */         WMenuItemPeer.this.postEvent(new ActionEvent(WMenuItemPeer.this.target, 1001, ((MenuItem)WMenuItemPeer.this.target).getActionCommand(), paramLong, this.val$modifiers));
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   static Font getDefaultFont()
/*     */   {
/* 178 */     return defaultMenuFont;
/*     */   }
/*     */ 
/*     */   private static native void initIDs();
/*     */ 
/*     */   public void setFont(Font paramFont)
/*     */   {
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  40 */     initIDs();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.windows.WMenuItemPeer
 * JD-Core Version:    0.6.2
 */