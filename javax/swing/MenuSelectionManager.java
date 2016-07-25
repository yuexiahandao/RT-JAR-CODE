/*     */ package javax.swing;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Point;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.event.KeyEvent;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.io.PrintStream;
/*     */ import java.util.Vector;
/*     */ import javax.swing.event.ChangeEvent;
/*     */ import javax.swing.event.ChangeListener;
/*     */ import javax.swing.event.EventListenerList;
/*     */ import sun.awt.AppContext;
/*     */ import sun.swing.SwingUtilities2;
/*     */ 
/*     */ public class MenuSelectionManager
/*     */ {
/*  41 */   private Vector<MenuElement> selection = new Vector();
/*     */   private static final boolean TRACE = false;
/*     */   private static final boolean VERBOSE = false;
/*     */   private static final boolean DEBUG = false;
/*  48 */   private static final StringBuilder MENU_SELECTION_MANAGER_KEY = new StringBuilder("javax.swing.MenuSelectionManager");
/*     */ 
/*  81 */   protected transient ChangeEvent changeEvent = null;
/*  82 */   protected EventListenerList listenerList = new EventListenerList();
/*     */ 
/*     */   public static MenuSelectionManager defaultManager()
/*     */   {
/*  57 */     synchronized (MENU_SELECTION_MANAGER_KEY) {
/*  58 */       AppContext localAppContext = AppContext.getAppContext();
/*  59 */       MenuSelectionManager localMenuSelectionManager = (MenuSelectionManager)localAppContext.get(MENU_SELECTION_MANAGER_KEY);
/*     */ 
/*  61 */       if (localMenuSelectionManager == null) {
/*  62 */         localMenuSelectionManager = new MenuSelectionManager();
/*  63 */         localAppContext.put(MENU_SELECTION_MANAGER_KEY, localMenuSelectionManager);
/*     */ 
/*  66 */         Object localObject1 = localAppContext.get(SwingUtilities2.MENU_SELECTION_MANAGER_LISTENER_KEY);
/*  67 */         if ((localObject1 != null) && ((localObject1 instanceof ChangeListener))) {
/*  68 */           localMenuSelectionManager.addChangeListener((ChangeListener)localObject1);
/*     */         }
/*     */       }
/*     */ 
/*  72 */       return localMenuSelectionManager;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setSelectedPath(MenuElement[] paramArrayOfMenuElement)
/*     */   {
/*  97 */     int k = this.selection.size();
/*  98 */     int m = 0;
/*     */ 
/* 100 */     if (paramArrayOfMenuElement == null) {
/* 101 */       paramArrayOfMenuElement = new MenuElement[0];
/*     */     }
/*     */ 
/* 109 */     int i = 0; for (int j = paramArrayOfMenuElement.length; (i < j) && 
/* 110 */       (i < k) && (this.selection.elementAt(i) == paramArrayOfMenuElement[i]); i++)
/*     */     {
/* 111 */       m++;
/*     */     }
/*     */ 
/* 116 */     for (i = k - 1; i >= m; i--) {
/* 117 */       MenuElement localMenuElement = (MenuElement)this.selection.elementAt(i);
/* 118 */       this.selection.removeElementAt(i);
/* 119 */       localMenuElement.menuSelectionChanged(false);
/*     */     }
/*     */ 
/* 122 */     i = m; for (j = paramArrayOfMenuElement.length; i < j; i++) {
/* 123 */       if (paramArrayOfMenuElement[i] != null) {
/* 124 */         this.selection.addElement(paramArrayOfMenuElement[i]);
/* 125 */         paramArrayOfMenuElement[i].menuSelectionChanged(true);
/*     */       }
/*     */     }
/*     */ 
/* 129 */     fireStateChanged();
/*     */   }
/*     */ 
/*     */   public MenuElement[] getSelectedPath()
/*     */   {
/* 138 */     MenuElement[] arrayOfMenuElement = new MenuElement[this.selection.size()];
/*     */ 
/* 140 */     int i = 0; for (int j = this.selection.size(); i < j; i++)
/* 141 */       arrayOfMenuElement[i] = ((MenuElement)this.selection.elementAt(i));
/* 142 */     return arrayOfMenuElement;
/*     */   }
/*     */ 
/*     */   public void clearSelectedPath()
/*     */   {
/* 150 */     if (this.selection.size() > 0)
/* 151 */       setSelectedPath(null);
/*     */   }
/*     */ 
/*     */   public void addChangeListener(ChangeListener paramChangeListener)
/*     */   {
/* 161 */     this.listenerList.add(ChangeListener.class, paramChangeListener);
/*     */   }
/*     */ 
/*     */   public void removeChangeListener(ChangeListener paramChangeListener)
/*     */   {
/* 170 */     this.listenerList.remove(ChangeListener.class, paramChangeListener);
/*     */   }
/*     */ 
/*     */   public ChangeListener[] getChangeListeners()
/*     */   {
/* 182 */     return (ChangeListener[])this.listenerList.getListeners(ChangeListener.class);
/*     */   }
/*     */ 
/*     */   protected void fireStateChanged()
/*     */   {
/* 194 */     Object[] arrayOfObject = this.listenerList.getListenerList();
/*     */ 
/* 197 */     for (int i = arrayOfObject.length - 2; i >= 0; i -= 2)
/* 198 */       if (arrayOfObject[i] == ChangeListener.class)
/*     */       {
/* 200 */         if (this.changeEvent == null)
/* 201 */           this.changeEvent = new ChangeEvent(this);
/* 202 */         ((ChangeListener)arrayOfObject[(i + 1)]).stateChanged(this.changeEvent);
/*     */       }
/*     */   }
/*     */ 
/*     */   public void processMouseEvent(MouseEvent paramMouseEvent)
/*     */   {
/* 225 */     Point localPoint = paramMouseEvent.getPoint();
/*     */ 
/* 227 */     Component localComponent2 = paramMouseEvent.getComponent();
/*     */ 
/* 229 */     if ((localComponent2 != null) && (!localComponent2.isShowing()))
/*     */     {
/* 232 */       return;
/*     */     }
/*     */ 
/* 235 */     int i4 = paramMouseEvent.getID();
/* 236 */     int i5 = paramMouseEvent.getModifiers();
/*     */ 
/* 238 */     if (((i4 == 504) || (i4 == 505)) && ((i5 & 0x1C) != 0))
/*     */     {
/* 242 */       return;
/*     */     }
/*     */ 
/* 245 */     if (localComponent2 != null) {
/* 246 */       SwingUtilities.convertPointToScreen(localPoint, localComponent2);
/*     */     }
/*     */ 
/* 249 */     int i = localPoint.x;
/* 250 */     int j = localPoint.y;
/*     */ 
/* 252 */     Vector localVector = (Vector)this.selection.clone();
/* 253 */     int i3 = localVector.size();
/* 254 */     int i6 = 0;
/* 255 */     for (int k = i3 - 1; (k >= 0) && (i6 == 0); k--) {
/* 256 */       MenuElement localMenuElement = (MenuElement)localVector.elementAt(k);
/* 257 */       MenuElement[] arrayOfMenuElement1 = localMenuElement.getSubElements();
/*     */ 
/* 259 */       MenuElement[] arrayOfMenuElement2 = null;
/* 260 */       int m = 0; for (int n = arrayOfMenuElement1.length; (m < n) && (i6 == 0); m++)
/* 261 */         if (arrayOfMenuElement1[m] != null)
/*     */         {
/* 263 */           Component localComponent1 = arrayOfMenuElement1[m].getComponent();
/* 264 */           if (localComponent1.isShowing())
/*     */           {
/*     */             int i1;
/*     */             int i2;
/* 266 */             if ((localComponent1 instanceof JComponent)) {
/* 267 */               i1 = localComponent1.getWidth();
/* 268 */               i2 = localComponent1.getHeight();
/*     */             } else {
/* 270 */               Rectangle localRectangle = localComponent1.getBounds();
/* 271 */               i1 = localRectangle.width;
/* 272 */               i2 = localRectangle.height;
/*     */             }
/* 274 */             localPoint.x = i;
/* 275 */             localPoint.y = j;
/* 276 */             SwingUtilities.convertPointFromScreen(localPoint, localComponent1);
/*     */ 
/* 281 */             if ((localPoint.x >= 0) && (localPoint.x < i1) && (localPoint.y >= 0) && (localPoint.y < i2))
/*     */             {
/* 284 */               if (arrayOfMenuElement2 == null) {
/* 285 */                 arrayOfMenuElement2 = new MenuElement[k + 2];
/* 286 */                 for (int i7 = 0; i7 <= k; i7++)
/* 287 */                   arrayOfMenuElement2[i7] = ((MenuElement)localVector.elementAt(i7));
/*     */               }
/* 289 */               arrayOfMenuElement2[(k + 1)] = arrayOfMenuElement1[m];
/* 290 */               MenuElement[] arrayOfMenuElement3 = getSelectedPath();
/*     */ 
/* 293 */               if ((arrayOfMenuElement3[(arrayOfMenuElement3.length - 1)] != arrayOfMenuElement2[(k + 1)]) && ((arrayOfMenuElement3.length < 2) || (arrayOfMenuElement3[(arrayOfMenuElement3.length - 2)] != arrayOfMenuElement2[(k + 1)])))
/*     */               {
/* 298 */                 localObject = arrayOfMenuElement3[(arrayOfMenuElement3.length - 1)].getComponent();
/*     */ 
/* 300 */                 MouseEvent localMouseEvent1 = new MouseEvent((Component)localObject, 505, paramMouseEvent.getWhen(), paramMouseEvent.getModifiers(), localPoint.x, localPoint.y, paramMouseEvent.getXOnScreen(), paramMouseEvent.getYOnScreen(), paramMouseEvent.getClickCount(), paramMouseEvent.isPopupTrigger(), 0);
/*     */ 
/* 308 */                 arrayOfMenuElement3[(arrayOfMenuElement3.length - 1)].processMouseEvent(localMouseEvent1, arrayOfMenuElement2, this);
/*     */ 
/* 311 */                 MouseEvent localMouseEvent2 = new MouseEvent(localComponent1, 504, paramMouseEvent.getWhen(), paramMouseEvent.getModifiers(), localPoint.x, localPoint.y, paramMouseEvent.getXOnScreen(), paramMouseEvent.getYOnScreen(), paramMouseEvent.getClickCount(), paramMouseEvent.isPopupTrigger(), 0);
/*     */ 
/* 320 */                 arrayOfMenuElement1[m].processMouseEvent(localMouseEvent2, arrayOfMenuElement2, this);
/*     */               }
/* 322 */               Object localObject = new MouseEvent(localComponent1, paramMouseEvent.getID(), paramMouseEvent.getWhen(), paramMouseEvent.getModifiers(), localPoint.x, localPoint.y, paramMouseEvent.getXOnScreen(), paramMouseEvent.getYOnScreen(), paramMouseEvent.getClickCount(), paramMouseEvent.isPopupTrigger(), 0);
/*     */ 
/* 329 */               arrayOfMenuElement1[m].processMouseEvent((MouseEvent)localObject, arrayOfMenuElement2, this);
/* 330 */               i6 = 1;
/* 331 */               paramMouseEvent.consume();
/*     */             }
/*     */           }
/*     */         }
/*     */     }
/*     */   }
/*     */ 
/* 338 */   private void printMenuElementArray(MenuElement[] paramArrayOfMenuElement) { printMenuElementArray(paramArrayOfMenuElement, false); }
/*     */ 
/*     */   private void printMenuElementArray(MenuElement[] paramArrayOfMenuElement, boolean paramBoolean)
/*     */   {
/* 342 */     System.out.println("Path is(");
/*     */ 
/* 344 */     int i = 0; for (int j = paramArrayOfMenuElement.length; i < j; i++) {
/* 345 */       for (int k = 0; k <= i; k++)
/* 346 */         System.out.print("  ");
/* 347 */       MenuElement localMenuElement = paramArrayOfMenuElement[i];
/* 348 */       if ((localMenuElement instanceof JMenuItem))
/* 349 */         System.out.println(((JMenuItem)localMenuElement).getText() + ", ");
/* 350 */       else if ((localMenuElement instanceof JMenuBar))
/* 351 */         System.out.println("JMenuBar, ");
/* 352 */       else if ((localMenuElement instanceof JPopupMenu))
/* 353 */         System.out.println("JPopupMenu, ");
/* 354 */       else if (localMenuElement == null)
/* 355 */         System.out.println("NULL , ");
/*     */       else {
/* 357 */         System.out.println("" + localMenuElement + ", ");
/*     */       }
/*     */     }
/* 360 */     System.out.println(")");
/*     */ 
/* 362 */     if (paramBoolean == true)
/* 363 */       Thread.dumpStack();
/*     */   }
/*     */ 
/*     */   public Component componentForPoint(Component paramComponent, Point paramPoint)
/*     */   {
/* 380 */     Point localPoint = paramPoint;
/*     */ 
/* 390 */     SwingUtilities.convertPointToScreen(localPoint, paramComponent);
/*     */ 
/* 392 */     int i = localPoint.x;
/* 393 */     int j = localPoint.y;
/*     */ 
/* 395 */     Vector localVector = (Vector)this.selection.clone();
/* 396 */     int i3 = localVector.size();
/* 397 */     for (int k = i3 - 1; k >= 0; k--) {
/* 398 */       MenuElement localMenuElement = (MenuElement)localVector.elementAt(k);
/* 399 */       MenuElement[] arrayOfMenuElement = localMenuElement.getSubElements();
/*     */ 
/* 401 */       int m = 0; for (int n = arrayOfMenuElement.length; m < n; m++)
/* 402 */         if (arrayOfMenuElement[m] != null)
/*     */         {
/* 404 */           Component localComponent = arrayOfMenuElement[m].getComponent();
/* 405 */           if (localComponent.isShowing())
/*     */           {
/*     */             int i1;
/*     */             int i2;
/* 407 */             if ((localComponent instanceof JComponent)) {
/* 408 */               i1 = localComponent.getWidth();
/* 409 */               i2 = localComponent.getHeight();
/*     */             } else {
/* 411 */               Rectangle localRectangle = localComponent.getBounds();
/* 412 */               i1 = localRectangle.width;
/* 413 */               i2 = localRectangle.height;
/*     */             }
/* 415 */             localPoint.x = i;
/* 416 */             localPoint.y = j;
/* 417 */             SwingUtilities.convertPointFromScreen(localPoint, localComponent);
/*     */ 
/* 422 */             if ((localPoint.x >= 0) && (localPoint.x < i1) && (localPoint.y >= 0) && (localPoint.y < i2))
/* 423 */               return localComponent;
/*     */           }
/*     */         }
/*     */     }
/* 427 */     return null;
/*     */   }
/*     */ 
/*     */   public void processKeyEvent(KeyEvent paramKeyEvent)
/*     */   {
/* 437 */     MenuElement[] arrayOfMenuElement1 = new MenuElement[0];
/* 438 */     arrayOfMenuElement1 = (MenuElement[])this.selection.toArray(arrayOfMenuElement1);
/* 439 */     int i = arrayOfMenuElement1.length;
/*     */ 
/* 442 */     if (i < 1) {
/* 443 */       return;
/*     */     }
/*     */ 
/* 446 */     for (int j = i - 1; j >= 0; j--) {
/* 447 */       MenuElement localMenuElement = arrayOfMenuElement1[j];
/* 448 */       MenuElement[] arrayOfMenuElement3 = localMenuElement.getSubElements();
/* 449 */       arrayOfMenuElement2 = null;
/*     */ 
/* 451 */       for (int k = 0; k < arrayOfMenuElement3.length; k++) {
/* 452 */         if ((arrayOfMenuElement3[k] != null) && (arrayOfMenuElement3[k].getComponent().isShowing()) && (arrayOfMenuElement3[k].getComponent().isEnabled()))
/*     */         {
/* 457 */           if (arrayOfMenuElement2 == null) {
/* 458 */             arrayOfMenuElement2 = new MenuElement[j + 2];
/* 459 */             System.arraycopy(arrayOfMenuElement1, 0, arrayOfMenuElement2, 0, j + 1);
/*     */           }
/* 461 */           arrayOfMenuElement2[(j + 1)] = arrayOfMenuElement3[k];
/* 462 */           arrayOfMenuElement3[k].processKeyEvent(paramKeyEvent, arrayOfMenuElement2, this);
/* 463 */           if (paramKeyEvent.isConsumed()) {
/* 464 */             return;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 470 */     MenuElement[] arrayOfMenuElement2 = new MenuElement[1];
/* 471 */     arrayOfMenuElement2[0] = arrayOfMenuElement1[0];
/* 472 */     arrayOfMenuElement2[0].processKeyEvent(paramKeyEvent, arrayOfMenuElement2, this);
/* 473 */     if (paramKeyEvent.isConsumed());
/*     */   }
/*     */ 
/*     */   public boolean isComponentPartOfCurrentMenu(Component paramComponent)
/*     */   {
/* 482 */     if (this.selection.size() > 0) {
/* 483 */       MenuElement localMenuElement = (MenuElement)this.selection.elementAt(0);
/* 484 */       return isComponentPartOfCurrentMenu(localMenuElement, paramComponent);
/*     */     }
/* 486 */     return false;
/*     */   }
/*     */ 
/*     */   private boolean isComponentPartOfCurrentMenu(MenuElement paramMenuElement, Component paramComponent)
/*     */   {
/* 493 */     if (paramMenuElement == null) {
/* 494 */       return false;
/*     */     }
/* 496 */     if (paramMenuElement.getComponent() == paramComponent) {
/* 497 */       return true;
/*     */     }
/* 499 */     MenuElement[] arrayOfMenuElement = paramMenuElement.getSubElements();
/* 500 */     int i = 0; for (int j = arrayOfMenuElement.length; i < j; i++) {
/* 501 */       if (isComponentPartOfCurrentMenu(arrayOfMenuElement[i], paramComponent)) {
/* 502 */         return true;
/*     */       }
/*     */     }
/* 505 */     return false;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.MenuSelectionManager
 * JD-Core Version:    0.6.2
 */