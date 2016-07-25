/*     */ package java.awt;
/*     */ 
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.AdjustmentEvent;
/*     */ import java.awt.event.InputEvent;
/*     */ import java.awt.event.ItemEvent;
/*     */ import java.awt.event.KeyEvent;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.awt.peer.ComponentPeer;
/*     */ import java.awt.peer.LightweightPeer;
/*     */ import java.lang.reflect.Field;
/*     */ import java.security.AccessControlContext;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.EventObject;
/*     */ import sun.awt.AWTAccessor;
/*     */ import sun.awt.AWTAccessor.AWTEventAccessor;
/*     */ import sun.util.logging.PlatformLogger;
/*     */ 
/*     */ public abstract class AWTEvent extends EventObject
/*     */ {
/*  81 */   private static final PlatformLogger log = PlatformLogger.getLogger("java.awt.AWTEvent");
/*     */   private byte[] bdata;
/*     */   protected int id;
/* 101 */   protected boolean consumed = false;
/*     */ 
/* 106 */   private volatile transient AccessControlContext acc = AccessController.getContext();
/*     */ 
/* 119 */   transient boolean focusManagerIsDispatching = false;
/*     */   transient boolean isPosted;
/*     */   private transient boolean isSystemGenerated;
/*     */   public static final long COMPONENT_EVENT_MASK = 1L;
/*     */   public static final long CONTAINER_EVENT_MASK = 2L;
/*     */   public static final long FOCUS_EVENT_MASK = 4L;
/*     */   public static final long KEY_EVENT_MASK = 8L;
/*     */   public static final long MOUSE_EVENT_MASK = 16L;
/*     */   public static final long MOUSE_MOTION_EVENT_MASK = 32L;
/*     */   public static final long WINDOW_EVENT_MASK = 64L;
/*     */   public static final long ACTION_EVENT_MASK = 128L;
/*     */   public static final long ADJUSTMENT_EVENT_MASK = 256L;
/*     */   public static final long ITEM_EVENT_MASK = 512L;
/*     */   public static final long TEXT_EVENT_MASK = 1024L;
/*     */   public static final long INPUT_METHOD_EVENT_MASK = 2048L;
/*     */   static final long INPUT_METHODS_ENABLED_MASK = 4096L;
/*     */   public static final long PAINT_EVENT_MASK = 8192L;
/*     */   public static final long INVOCATION_EVENT_MASK = 16384L;
/*     */   public static final long HIERARCHY_EVENT_MASK = 32768L;
/*     */   public static final long HIERARCHY_BOUNDS_EVENT_MASK = 65536L;
/*     */   public static final long MOUSE_WHEEL_EVENT_MASK = 131072L;
/*     */   public static final long WINDOW_STATE_EVENT_MASK = 262144L;
/*     */   public static final long WINDOW_FOCUS_EVENT_MASK = 524288L;
/*     */   public static final int RESERVED_ID_MAX = 1999;
/* 245 */   private static Field inputEvent_CanAccessSystemClipboard_Field = null;
/*     */   private static final long serialVersionUID = -1825314779160409405L;
/*     */ 
/*     */   final AccessControlContext getAccessControlContext()
/*     */   {
/* 113 */     if (this.acc == null) {
/* 114 */       throw new SecurityException("AWTEvent is missing AccessControlContext");
/*     */     }
/* 116 */     return this.acc;
/*     */   }
/*     */ 
/*     */   private static synchronized Field get_InputEvent_CanAccessSystemClipboard()
/*     */   {
/* 288 */     if (inputEvent_CanAccessSystemClipboard_Field == null) {
/* 289 */       inputEvent_CanAccessSystemClipboard_Field = (Field)AccessController.doPrivileged(new PrivilegedAction()
/*     */       {
/*     */         public Object run()
/*     */         {
/* 293 */           Field localField = null;
/*     */           try {
/* 295 */             localField = InputEvent.class.getDeclaredField("canAccessSystemClipboard");
/*     */ 
/* 297 */             localField.setAccessible(true);
/* 298 */             return localField;
/*     */           } catch (SecurityException localSecurityException) {
/* 300 */             if (AWTEvent.log.isLoggable(500))
/* 301 */               AWTEvent.log.fine("AWTEvent.get_InputEvent_CanAccessSystemClipboard() got SecurityException ", localSecurityException);
/*     */           }
/*     */           catch (NoSuchFieldException localNoSuchFieldException) {
/* 304 */             if (AWTEvent.log.isLoggable(500)) {
/* 305 */               AWTEvent.log.fine("AWTEvent.get_InputEvent_CanAccessSystemClipboard() got NoSuchFieldException ", localNoSuchFieldException);
/*     */             }
/*     */           }
/* 308 */           return null;
/*     */         }
/*     */       });
/*     */     }
/*     */ 
/* 313 */     return inputEvent_CanAccessSystemClipboard_Field;
/*     */   }
/*     */ 
/*     */   private static native void initIDs();
/*     */ 
/*     */   public AWTEvent(Event paramEvent)
/*     */   {
/* 327 */     this(paramEvent.target, paramEvent.id);
/*     */   }
/*     */ 
/*     */   public AWTEvent(Object paramObject, int paramInt)
/*     */   {
/* 337 */     super(paramObject);
/* 338 */     this.id = paramInt;
/* 339 */     switch (paramInt) {
/*     */     case 601:
/*     */     case 701:
/*     */     case 900:
/*     */     case 1001:
/* 344 */       this.consumed = true;
/* 345 */       break;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setSource(Object paramObject)
/*     */   {
/* 363 */     if (this.source == paramObject) {
/* 364 */       return;
/*     */     }
/*     */ 
/* 367 */     Object localObject1 = null;
/* 368 */     if ((paramObject instanceof Component)) {
/* 369 */       localObject1 = (Component)paramObject;
/* 370 */       while ((localObject1 != null) && (((Component)localObject1).peer != null) && ((((Component)localObject1).peer instanceof LightweightPeer)))
/*     */       {
/* 372 */         localObject1 = ((Component)localObject1).parent;
/*     */       }
/*     */     }
/*     */ 
/* 376 */     synchronized (this) {
/* 377 */       this.source = paramObject;
/* 378 */       if (localObject1 != null) {
/* 379 */         ComponentPeer localComponentPeer = ((Component)localObject1).peer;
/* 380 */         if (localComponentPeer != null)
/* 381 */           nativeSetSource(localComponentPeer);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private native void nativeSetSource(ComponentPeer paramComponentPeer);
/*     */ 
/*     */   public int getID()
/*     */   {
/* 393 */     return this.id;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 400 */     String str = null;
/* 401 */     if ((this.source instanceof Component))
/* 402 */       str = ((Component)this.source).getName();
/* 403 */     else if ((this.source instanceof MenuComponent)) {
/* 404 */       str = ((MenuComponent)this.source).getName();
/*     */     }
/* 406 */     return getClass().getName() + "[" + paramString() + "] on " + (str != null ? str : this.source);
/*     */   }
/*     */ 
/*     */   public String paramString()
/*     */   {
/* 420 */     return "";
/*     */   }
/*     */ 
/*     */   protected void consume()
/*     */   {
/* 428 */     switch (this.id) {
/*     */     case 401:
/*     */     case 402:
/*     */     case 501:
/*     */     case 502:
/*     */     case 503:
/*     */     case 504:
/*     */     case 505:
/*     */     case 506:
/*     */     case 507:
/*     */     case 1100:
/*     */     case 1101:
/* 440 */       this.consumed = true;
/* 441 */       break;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected boolean isConsumed()
/*     */   {
/* 451 */     return this.consumed;
/*     */   }
/*     */ 
/*     */   Event convertToOld()
/*     */   {
/* 464 */     Object localObject1 = getSource();
/* 465 */     int i = this.id;
/*     */     Object localObject2;
/* 467 */     switch (this.id) {
/*     */     case 401:
/*     */     case 402:
/* 470 */       KeyEvent localKeyEvent = (KeyEvent)this;
/* 471 */       if (localKeyEvent.isActionKey()) {
/* 472 */         i = this.id == 401 ? 403 : 404;
/*     */       }
/*     */ 
/* 475 */       int j = localKeyEvent.getKeyCode();
/* 476 */       if ((j == 16) || (j == 17) || (j == 18))
/*     */       {
/* 479 */         return null;
/*     */       }
/*     */ 
/* 482 */       return new Event(localObject1, localKeyEvent.getWhen(), i, 0, 0, Event.getOldEventKey(localKeyEvent), localKeyEvent.getModifiers() & 0xFFFFFFEF);
/*     */     case 501:
/*     */     case 502:
/*     */     case 503:
/*     */     case 504:
/*     */     case 505:
/*     */     case 506:
/* 492 */       MouseEvent localMouseEvent = (MouseEvent)this;
/*     */ 
/* 494 */       Event localEvent = new Event(localObject1, localMouseEvent.getWhen(), i, localMouseEvent.getX(), localMouseEvent.getY(), 0, localMouseEvent.getModifiers() & 0xFFFFFFEF);
/*     */ 
/* 497 */       localEvent.clickCount = localMouseEvent.getClickCount();
/* 498 */       return localEvent;
/*     */     case 1004:
/* 501 */       return new Event(localObject1, 1004, null);
/*     */     case 1005:
/* 504 */       return new Event(localObject1, 1005, null);
/*     */     case 201:
/*     */     case 203:
/*     */     case 204:
/* 509 */       return new Event(localObject1, i, null);
/*     */     case 100:
/* 512 */       if (((localObject1 instanceof Frame)) || ((localObject1 instanceof Dialog))) {
/* 513 */         localObject2 = ((Component)localObject1).getLocation();
/* 514 */         return new Event(localObject1, 0L, 205, ((Point)localObject2).x, ((Point)localObject2).y, 0, 0);
/*     */       }
/*     */ 
/*     */       break;
/*     */     case 1001:
/* 519 */       localObject2 = (ActionEvent)this;
/*     */       String str;
/* 521 */       if ((localObject1 instanceof Button))
/* 522 */         str = ((Button)localObject1).getLabel();
/* 523 */       else if ((localObject1 instanceof MenuItem))
/* 524 */         str = ((MenuItem)localObject1).getLabel();
/*     */       else {
/* 526 */         str = ((ActionEvent)localObject2).getActionCommand();
/*     */       }
/* 528 */       return new Event(localObject1, 0L, i, 0, 0, 0, ((ActionEvent)localObject2).getModifiers(), str);
/*     */     case 701:
/* 531 */       ItemEvent localItemEvent = (ItemEvent)this;
/*     */       Object localObject3;
/* 533 */       if ((localObject1 instanceof List)) {
/* 534 */         i = localItemEvent.getStateChange() == 1 ? 701 : 702;
/*     */ 
/* 536 */         localObject3 = localItemEvent.getItem();
/*     */       } else {
/* 538 */         i = 1001;
/* 539 */         if ((localObject1 instanceof Choice)) {
/* 540 */           localObject3 = localItemEvent.getItem();
/*     */         }
/*     */         else {
/* 543 */           localObject3 = Boolean.valueOf(localItemEvent.getStateChange() == 1);
/*     */         }
/*     */       }
/* 546 */       return new Event(localObject1, i, localObject3);
/*     */     case 601:
/* 549 */       AdjustmentEvent localAdjustmentEvent = (AdjustmentEvent)this;
/* 550 */       switch (localAdjustmentEvent.getAdjustmentType()) {
/*     */       case 1:
/* 552 */         i = 602;
/* 553 */         break;
/*     */       case 2:
/* 555 */         i = 601;
/* 556 */         break;
/*     */       case 4:
/* 558 */         i = 604;
/* 559 */         break;
/*     */       case 3:
/* 561 */         i = 603;
/* 562 */         break;
/*     */       case 5:
/* 564 */         if (localAdjustmentEvent.getValueIsAdjusting()) {
/* 565 */           i = 605;
/*     */         }
/*     */         else {
/* 568 */           i = 607;
/*     */         }
/* 570 */         break;
/*     */       default:
/* 572 */         return null;
/*     */       }
/* 574 */       return new Event(localObject1, i, Integer.valueOf(localAdjustmentEvent.getValue()));
/*     */     }
/*     */ 
/* 578 */     return null;
/*     */   }
/*     */ 
/*     */   void copyPrivateDataInto(AWTEvent paramAWTEvent)
/*     */   {
/* 588 */     paramAWTEvent.bdata = this.bdata;
/*     */ 
/* 590 */     if (((this instanceof InputEvent)) && ((paramAWTEvent instanceof InputEvent))) {
/* 591 */       Field localField = get_InputEvent_CanAccessSystemClipboard();
/* 592 */       if (localField != null) {
/*     */         try {
/* 594 */           boolean bool = localField.getBoolean(this);
/* 595 */           localField.setBoolean(paramAWTEvent, bool);
/*     */         } catch (IllegalAccessException localIllegalAccessException) {
/* 597 */           if (log.isLoggable(500)) {
/* 598 */             log.fine("AWTEvent.copyPrivateDataInto() got IllegalAccessException ", localIllegalAccessException);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 603 */     paramAWTEvent.isSystemGenerated = this.isSystemGenerated;
/*     */   }
/*     */ 
/*     */   void dispatched() {
/* 607 */     if ((this instanceof InputEvent)) {
/* 608 */       Field localField = get_InputEvent_CanAccessSystemClipboard();
/* 609 */       if (localField != null)
/*     */         try {
/* 611 */           localField.setBoolean(this, false);
/*     */         } catch (IllegalAccessException localIllegalAccessException) {
/* 613 */           if (log.isLoggable(500))
/* 614 */             log.fine("AWTEvent.dispatched() got IllegalAccessException ", localIllegalAccessException);
/*     */         }
/*     */     }
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 254 */     Toolkit.loadLibraries();
/* 255 */     if (!GraphicsEnvironment.isHeadless()) {
/* 256 */       initIDs();
/*     */     }
/* 258 */     AWTAccessor.setAWTEventAccessor(new AWTAccessor.AWTEventAccessor()
/*     */     {
/*     */       public void setPosted(AWTEvent paramAnonymousAWTEvent) {
/* 261 */         paramAnonymousAWTEvent.isPosted = true;
/*     */       }
/*     */ 
/*     */       public void setSystemGenerated(AWTEvent paramAnonymousAWTEvent) {
/* 265 */         paramAnonymousAWTEvent.isSystemGenerated = true;
/*     */       }
/*     */ 
/*     */       public boolean isSystemGenerated(AWTEvent paramAnonymousAWTEvent) {
/* 269 */         return paramAnonymousAWTEvent.isSystemGenerated;
/*     */       }
/*     */ 
/*     */       public AccessControlContext getAccessControlContext(AWTEvent paramAnonymousAWTEvent) {
/* 273 */         return paramAnonymousAWTEvent.getAccessControlContext();
/*     */       }
/*     */ 
/*     */       public byte[] getBData(AWTEvent paramAnonymousAWTEvent) {
/* 277 */         return paramAnonymousAWTEvent.bdata;
/*     */       }
/*     */ 
/*     */       public void setBData(AWTEvent paramAnonymousAWTEvent, byte[] paramAnonymousArrayOfByte) {
/* 281 */         paramAnonymousAWTEvent.bdata = paramAnonymousArrayOfByte;
/*     */       }
/*     */     });
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.AWTEvent
 * JD-Core Version:    0.6.2
 */