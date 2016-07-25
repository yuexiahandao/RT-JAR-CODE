/*      */ package java.awt;
/*      */ 
/*      */ import java.awt.event.ActionEvent;
/*      */ import java.awt.event.FocusListener;
/*      */ import java.awt.peer.MenuComponentPeer;
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.Serializable;
/*      */ import java.security.AccessControlContext;
/*      */ import java.security.AccessController;
/*      */ import java.util.Locale;
/*      */ import javax.accessibility.Accessible;
/*      */ import javax.accessibility.AccessibleComponent;
/*      */ import javax.accessibility.AccessibleContext;
/*      */ import javax.accessibility.AccessibleRole;
/*      */ import javax.accessibility.AccessibleSelection;
/*      */ import javax.accessibility.AccessibleStateSet;
/*      */ import sun.awt.AWTAccessor;
/*      */ import sun.awt.AWTAccessor.MenuComponentAccessor;
/*      */ import sun.awt.AppContext;
/*      */ 
/*      */ public abstract class MenuComponent
/*      */   implements Serializable
/*      */ {
/*      */   transient MenuComponentPeer peer;
/*      */   transient MenuContainer parent;
/*      */   transient AppContext appContext;
/*      */   Font font;
/*      */   private String name;
/*   95 */   private boolean nameExplicitlySet = false;
/*      */ 
/*  102 */   boolean newEventsOnly = false;
/*      */ 
/*  107 */   private volatile transient AccessControlContext acc = AccessController.getContext();
/*      */   static final String actionListenerK = "actionL";
/*      */   static final String itemListenerK = "itemL";
/*      */   private static final long serialVersionUID = -4536902356223894379L;
/*  449 */   AccessibleContext accessibleContext = null;
/*      */ 
/*      */   final AccessControlContext getAccessControlContext()
/*      */   {
/*  114 */     if (this.acc == null) {
/*  115 */       throw new SecurityException("MenuComponent is missing AccessControlContext");
/*      */     }
/*      */ 
/*  118 */     return this.acc;
/*      */   }
/*      */ 
/*      */   public MenuComponent()
/*      */     throws HeadlessException
/*      */   {
/*  159 */     GraphicsEnvironment.checkHeadless();
/*  160 */     this.appContext = AppContext.getAppContext();
/*      */   }
/*      */ 
/*      */   String constructComponentName()
/*      */   {
/*  169 */     return null;
/*      */   }
/*      */ 
/*      */   public String getName()
/*      */   {
/*  181 */     if ((this.name == null) && (!this.nameExplicitlySet)) {
/*  182 */       synchronized (this) {
/*  183 */         if ((this.name == null) && (!this.nameExplicitlySet))
/*  184 */           this.name = constructComponentName();
/*      */       }
/*      */     }
/*  187 */     return this.name;
/*      */   }
/*      */ 
/*      */   public void setName(String paramString)
/*      */   {
/*  197 */     synchronized (this) {
/*  198 */       this.name = paramString;
/*  199 */       this.nameExplicitlySet = true;
/*      */     }
/*      */   }
/*      */ 
/*      */   public MenuContainer getParent()
/*      */   {
/*  210 */     return getParent_NoClientCode();
/*      */   }
/*      */ 
/*      */   final MenuContainer getParent_NoClientCode()
/*      */   {
/*  217 */     return this.parent;
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public MenuComponentPeer getPeer()
/*      */   {
/*  226 */     return this.peer;
/*      */   }
/*      */ 
/*      */   public Font getFont()
/*      */   {
/*  236 */     Font localFont = this.font;
/*  237 */     if (localFont != null) {
/*  238 */       return localFont;
/*      */     }
/*  240 */     MenuContainer localMenuContainer = this.parent;
/*  241 */     if (localMenuContainer != null) {
/*  242 */       return localMenuContainer.getFont();
/*      */     }
/*  244 */     return null;
/*      */   }
/*      */ 
/*      */   final Font getFont_NoClientCode()
/*      */   {
/*  252 */     Font localFont = this.font;
/*  253 */     if (localFont != null) {
/*  254 */       return localFont;
/*      */     }
/*      */ 
/*  261 */     MenuContainer localMenuContainer = this.parent;
/*  262 */     if (localMenuContainer != null) {
/*  263 */       if ((localMenuContainer instanceof Component))
/*  264 */         localFont = ((Component)localMenuContainer).getFont_NoClientCode();
/*  265 */       else if ((localMenuContainer instanceof MenuComponent)) {
/*  266 */         localFont = ((MenuComponent)localMenuContainer).getFont_NoClientCode();
/*      */       }
/*      */     }
/*  269 */     return localFont;
/*      */   }
/*      */ 
/*      */   public void setFont(Font paramFont)
/*      */   {
/*  291 */     this.font = paramFont;
/*      */ 
/*  293 */     MenuComponentPeer localMenuComponentPeer = this.peer;
/*  294 */     if (localMenuComponentPeer != null)
/*  295 */       localMenuComponentPeer.setFont(paramFont);
/*      */   }
/*      */ 
/*      */   public void removeNotify()
/*      */   {
/*  305 */     synchronized (getTreeLock()) {
/*  306 */       MenuComponentPeer localMenuComponentPeer = this.peer;
/*  307 */       if (localMenuComponentPeer != null) {
/*  308 */         Toolkit.getEventQueue().removeSourceEvents(this, true);
/*  309 */         this.peer = null;
/*  310 */         localMenuComponentPeer.dispose();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public boolean postEvent(Event paramEvent)
/*      */   {
/*  327 */     MenuContainer localMenuContainer = this.parent;
/*  328 */     if (localMenuContainer != null) {
/*  329 */       localMenuContainer.postEvent(paramEvent);
/*      */     }
/*  331 */     return false;
/*      */   }
/*      */ 
/*      */   public final void dispatchEvent(AWTEvent paramAWTEvent)
/*      */   {
/*  339 */     dispatchEventImpl(paramAWTEvent);
/*      */   }
/*      */ 
/*      */   void dispatchEventImpl(AWTEvent paramAWTEvent) {
/*  343 */     EventQueue.setCurrentEventAndMostRecentTime(paramAWTEvent);
/*      */ 
/*  345 */     Toolkit.getDefaultToolkit().notifyAWTEventListeners(paramAWTEvent);
/*      */ 
/*  347 */     if ((this.newEventsOnly) || ((this.parent != null) && ((this.parent instanceof MenuComponent)) && (((MenuComponent)this.parent).newEventsOnly)))
/*      */     {
/*  350 */       if (eventEnabled(paramAWTEvent)) {
/*  351 */         processEvent(paramAWTEvent);
/*  352 */       } else if (((paramAWTEvent instanceof ActionEvent)) && (this.parent != null)) {
/*  353 */         paramAWTEvent.setSource(this.parent);
/*  354 */         ((MenuComponent)this.parent).dispatchEvent(paramAWTEvent);
/*      */       }
/*      */     }
/*      */     else {
/*  358 */       Event localEvent = paramAWTEvent.convertToOld();
/*  359 */       if (localEvent != null)
/*  360 */         postEvent(localEvent);
/*      */     }
/*      */   }
/*      */ 
/*      */   boolean eventEnabled(AWTEvent paramAWTEvent)
/*      */   {
/*  367 */     return false;
/*      */   }
/*      */ 
/*      */   protected void processEvent(AWTEvent paramAWTEvent)
/*      */   {
/*      */   }
/*      */ 
/*      */   protected String paramString()
/*      */   {
/*  391 */     String str = getName();
/*  392 */     return str != null ? str : "";
/*      */   }
/*      */ 
/*      */   public String toString()
/*      */   {
/*  400 */     return getClass().getName() + "[" + paramString() + "]";
/*      */   }
/*      */ 
/*      */   protected final Object getTreeLock()
/*      */   {
/*  410 */     return Component.LOCK;
/*      */   }
/*      */ 
/*      */   private void readObject(ObjectInputStream paramObjectInputStream)
/*      */     throws ClassNotFoundException, IOException, HeadlessException
/*      */   {
/*  426 */     GraphicsEnvironment.checkHeadless();
/*      */ 
/*  428 */     this.acc = AccessController.getContext();
/*      */ 
/*  430 */     paramObjectInputStream.defaultReadObject();
/*      */ 
/*  432 */     this.appContext = AppContext.getAppContext();
/*      */   }
/*      */ 
/*      */   private static native void initIDs();
/*      */ 
/*      */   public AccessibleContext getAccessibleContext()
/*      */   {
/*  465 */     return this.accessibleContext;
/*      */   }
/*      */ 
/*      */   int getAccessibleIndexInParent()
/*      */   {
/* 1031 */     MenuContainer localMenuContainer = this.parent;
/* 1032 */     if (!(localMenuContainer instanceof MenuComponent))
/*      */     {
/* 1034 */       return -1;
/*      */     }
/* 1036 */     MenuComponent localMenuComponent = (MenuComponent)localMenuContainer;
/* 1037 */     return localMenuComponent.getAccessibleChildIndex(this);
/*      */   }
/*      */ 
/*      */   int getAccessibleChildIndex(MenuComponent paramMenuComponent)
/*      */   {
/* 1048 */     return -1;
/*      */   }
/*      */ 
/*      */   AccessibleStateSet getAccessibleStateSet()
/*      */   {
/* 1059 */     AccessibleStateSet localAccessibleStateSet = new AccessibleStateSet();
/* 1060 */     return localAccessibleStateSet;
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*   54 */     Toolkit.loadLibraries();
/*   55 */     if (!GraphicsEnvironment.isHeadless()) {
/*   56 */       initIDs();
/*      */     }
/*      */ 
/*  133 */     AWTAccessor.setMenuComponentAccessor(new AWTAccessor.MenuComponentAccessor()
/*      */     {
/*      */       public AppContext getAppContext(MenuComponent paramAnonymousMenuComponent) {
/*  136 */         return paramAnonymousMenuComponent.appContext;
/*      */       }
/*      */ 
/*      */       public void setAppContext(MenuComponent paramAnonymousMenuComponent, AppContext paramAnonymousAppContext) {
/*  140 */         paramAnonymousMenuComponent.appContext = paramAnonymousAppContext;
/*      */       }
/*      */       public MenuContainer getParent(MenuComponent paramAnonymousMenuComponent) {
/*  143 */         return paramAnonymousMenuComponent.parent;
/*      */       }
/*      */       public Font getFont_NoClientCode(MenuComponent paramAnonymousMenuComponent) {
/*  146 */         return paramAnonymousMenuComponent.getFont_NoClientCode();
/*      */       }
/*      */     });
/*      */   }
/*      */ 
/*      */   protected abstract class AccessibleAWTMenuComponent extends AccessibleContext
/*      */     implements Serializable, AccessibleComponent, AccessibleSelection
/*      */   {
/*      */     private static final long serialVersionUID = -4269533416223798698L;
/*      */ 
/*      */     protected AccessibleAWTMenuComponent()
/*      */     {
/*      */     }
/*      */ 
/*      */     public AccessibleSelection getAccessibleSelection()
/*      */     {
/*  506 */       return this;
/*      */     }
/*      */ 
/*      */     public String getAccessibleName()
/*      */     {
/*  523 */       return this.accessibleName;
/*      */     }
/*      */ 
/*      */     public String getAccessibleDescription()
/*      */     {
/*  542 */       return this.accessibleDescription;
/*      */     }
/*      */ 
/*      */     public AccessibleRole getAccessibleRole()
/*      */     {
/*  553 */       return AccessibleRole.AWT_COMPONENT;
/*      */     }
/*      */ 
/*      */     public AccessibleStateSet getAccessibleStateSet()
/*      */     {
/*  564 */       return MenuComponent.this.getAccessibleStateSet();
/*      */     }
/*      */ 
/*      */     public Accessible getAccessibleParent()
/*      */     {
/*  577 */       if (this.accessibleParent != null) {
/*  578 */         return this.accessibleParent;
/*      */       }
/*  580 */       MenuContainer localMenuContainer = MenuComponent.this.getParent();
/*  581 */       if ((localMenuContainer instanceof Accessible)) {
/*  582 */         return (Accessible)localMenuContainer;
/*      */       }
/*      */ 
/*  585 */       return null;
/*      */     }
/*      */ 
/*      */     public int getAccessibleIndexInParent()
/*      */     {
/*  596 */       return MenuComponent.this.getAccessibleIndexInParent();
/*      */     }
/*      */ 
/*      */     public int getAccessibleChildrenCount()
/*      */     {
/*  607 */       return 0;
/*      */     }
/*      */ 
/*      */     public Accessible getAccessibleChild(int paramInt)
/*      */     {
/*  617 */       return null;
/*      */     }
/*      */ 
/*      */     public Locale getLocale()
/*      */     {
/*  626 */       MenuContainer localMenuContainer = MenuComponent.this.getParent();
/*  627 */       if ((localMenuContainer instanceof Component)) {
/*  628 */         return ((Component)localMenuContainer).getLocale();
/*      */       }
/*  630 */       return Locale.getDefault();
/*      */     }
/*      */ 
/*      */     public AccessibleComponent getAccessibleComponent()
/*      */     {
/*  640 */       return this;
/*      */     }
/*      */ 
/*      */     public Color getBackground()
/*      */     {
/*  653 */       return null;
/*      */     }
/*      */ 
/*      */     public void setBackground(Color paramColor)
/*      */     {
/*      */     }
/*      */ 
/*      */     public Color getForeground()
/*      */     {
/*  674 */       return null;
/*      */     }
/*      */ 
/*      */     public void setForeground(Color paramColor)
/*      */     {
/*      */     }
/*      */ 
/*      */     public Cursor getCursor()
/*      */     {
/*  693 */       return null;
/*      */     }
/*      */ 
/*      */     public void setCursor(Cursor paramCursor)
/*      */     {
/*      */     }
/*      */ 
/*      */     public Font getFont()
/*      */     {
/*  715 */       return MenuComponent.this.getFont();
/*      */     }
/*      */ 
/*      */     public void setFont(Font paramFont)
/*      */     {
/*  724 */       MenuComponent.this.setFont(paramFont);
/*      */     }
/*      */ 
/*      */     public FontMetrics getFontMetrics(Font paramFont)
/*      */     {
/*  736 */       return null;
/*      */     }
/*      */ 
/*      */     public boolean isEnabled()
/*      */     {
/*  745 */       return true;
/*      */     }
/*      */ 
/*      */     public void setEnabled(boolean paramBoolean)
/*      */     {
/*      */     }
/*      */ 
/*      */     public boolean isVisible()
/*      */     {
/*  767 */       return true;
/*      */     }
/*      */ 
/*      */     public void setVisible(boolean paramBoolean)
/*      */     {
/*      */     }
/*      */ 
/*      */     public boolean isShowing()
/*      */     {
/*  789 */       return true;
/*      */     }
/*      */ 
/*      */     public boolean contains(Point paramPoint)
/*      */     {
/*  802 */       return false;
/*      */     }
/*      */ 
/*      */     public Point getLocationOnScreen()
/*      */     {
/*  812 */       return null;
/*      */     }
/*      */ 
/*      */     public Point getLocation()
/*      */     {
/*  826 */       return null;
/*      */     }
/*      */ 
/*      */     public void setLocation(Point paramPoint)
/*      */     {
/*      */     }
/*      */ 
/*      */     public Rectangle getBounds()
/*      */     {
/*  846 */       return null;
/*      */     }
/*      */ 
/*      */     public void setBounds(Rectangle paramRectangle)
/*      */     {
/*      */     }
/*      */ 
/*      */     public Dimension getSize()
/*      */     {
/*  873 */       return null;
/*      */     }
/*      */ 
/*      */     public void setSize(Dimension paramDimension)
/*      */     {
/*      */     }
/*      */ 
/*      */     public Accessible getAccessibleAt(Point paramPoint)
/*      */     {
/*  899 */       return null;
/*      */     }
/*      */ 
/*      */     public boolean isFocusTraversable()
/*      */     {
/*  908 */       return true;
/*      */     }
/*      */ 
/*      */     public void requestFocus()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void addFocusListener(FocusListener paramFocusListener)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void removeFocusListener(FocusListener paramFocusListener)
/*      */     {
/*      */     }
/*      */ 
/*      */     public int getAccessibleSelectionCount()
/*      */     {
/*  948 */       return 0;
/*      */     }
/*      */ 
/*      */     public Accessible getAccessibleSelection(int paramInt)
/*      */     {
/*  964 */       return null;
/*      */     }
/*      */ 
/*      */     public boolean isAccessibleChildSelected(int paramInt)
/*      */     {
/*  977 */       return false;
/*      */     }
/*      */ 
/*      */     public void addAccessibleSelection(int paramInt)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void removeAccessibleSelection(int paramInt)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void clearAccessibleSelection()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void selectAllAccessibleSelection()
/*      */     {
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.MenuComponent
 * JD-Core Version:    0.6.2
 */