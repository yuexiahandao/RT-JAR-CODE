/*     */ package sun.awt;
/*     */ 
/*     */ import java.awt.Canvas;
/*     */ import java.awt.Component;
/*     */ import java.awt.Panel;
/*     */ import java.awt.Scrollbar;
/*     */ import java.awt.Window;
/*     */ import java.awt.peer.ComponentPeer;
/*     */ import java.awt.peer.KeyboardFocusManagerPeer;
/*     */ import sun.util.logging.PlatformLogger;
/*     */ 
/*     */ public abstract class KeyboardFocusManagerPeerImpl
/*     */   implements KeyboardFocusManagerPeer
/*     */ {
/*  46 */   private static final PlatformLogger focusLog = PlatformLogger.getLogger("sun.awt.focus.KeyboardFocusManagerPeerImpl");
/*     */ 
/*  48 */   private static AWTAccessor.KeyboardFocusManagerAccessor kfmAccessor = AWTAccessor.getKeyboardFocusManagerAccessor();
/*     */   public static final int SNFH_FAILURE = 0;
/*     */   public static final int SNFH_SUCCESS_HANDLED = 1;
/*     */   public static final int SNFH_SUCCESS_PROCEED = 2;
/*     */ 
/*     */   public void clearGlobalFocusOwner(Window paramWindow)
/*     */   {
/*  58 */     if (paramWindow != null) {
/*  59 */       Component localComponent = paramWindow.getFocusOwner();
/*  60 */       if (focusLog.isLoggable(500))
/*  61 */         focusLog.fine("Clearing global focus owner " + localComponent);
/*  62 */       if (localComponent != null) {
/*  63 */         CausedFocusEvent localCausedFocusEvent = new CausedFocusEvent(localComponent, 1005, false, null, CausedFocusEvent.Cause.CLEAR_GLOBAL_FOCUS_OWNER);
/*     */ 
/*  65 */         SunToolkit.postPriorityEvent(localCausedFocusEvent);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public static boolean shouldFocusOnClick(Component paramComponent)
/*     */   {
/*  78 */     int i = 0;
/*     */ 
/*  85 */     if (((paramComponent instanceof Canvas)) || ((paramComponent instanceof Scrollbar)))
/*     */     {
/*  88 */       i = 1;
/*     */     }
/*  91 */     else if ((paramComponent instanceof Panel)) {
/*  92 */       i = ((Panel)paramComponent).getComponentCount() == 0 ? 1 : 0;
/*     */     }
/*     */     else
/*     */     {
/*  97 */       Object localObject = paramComponent != null ? paramComponent.getPeer() : null;
/*  98 */       i = localObject != null ? localObject.isFocusable() : 0;
/*     */     }
/* 100 */     return (i != 0) && (AWTAccessor.getComponentAccessor().canBeFocusOwner(paramComponent));
/*     */   }
/*     */ 
/*     */   public static boolean deliverFocus(Component paramComponent1, Component paramComponent2, boolean paramBoolean1, boolean paramBoolean2, long paramLong, CausedFocusEvent.Cause paramCause, Component paramComponent3)
/*     */   {
/* 115 */     if (paramComponent1 == null) {
/* 116 */       paramComponent1 = paramComponent2;
/*     */     }
/*     */ 
/* 119 */     Component localComponent = paramComponent3;
/* 120 */     if ((localComponent != null) && (localComponent.getPeer() == null)) {
/* 121 */       localComponent = null;
/*     */     }
/* 123 */     if (localComponent != null) {
/* 124 */       localCausedFocusEvent = new CausedFocusEvent(localComponent, 1005, false, paramComponent1, paramCause);
/*     */ 
/* 127 */       if (focusLog.isLoggable(400))
/* 128 */         focusLog.finer("Posting focus event: " + localCausedFocusEvent);
/* 129 */       SunToolkit.postPriorityEvent(localCausedFocusEvent);
/*     */     }
/*     */ 
/* 132 */     CausedFocusEvent localCausedFocusEvent = new CausedFocusEvent(paramComponent1, 1004, false, localComponent, paramCause);
/*     */ 
/* 135 */     if (focusLog.isLoggable(400))
/* 136 */       focusLog.finer("Posting focus event: " + localCausedFocusEvent);
/* 137 */     SunToolkit.postPriorityEvent(localCausedFocusEvent);
/* 138 */     return true;
/*     */   }
/*     */ 
/*     */   public static boolean requestFocusFor(Component paramComponent, CausedFocusEvent.Cause paramCause)
/*     */   {
/* 143 */     return AWTAccessor.getComponentAccessor().requestFocus(paramComponent, paramCause);
/*     */   }
/*     */ 
/*     */   public static int shouldNativelyFocusHeavyweight(Component paramComponent1, Component paramComponent2, boolean paramBoolean1, boolean paramBoolean2, long paramLong, CausedFocusEvent.Cause paramCause)
/*     */   {
/* 154 */     return kfmAccessor.shouldNativelyFocusHeavyweight(paramComponent1, paramComponent2, paramBoolean1, paramBoolean2, paramLong, paramCause);
/*     */   }
/*     */ 
/*     */   public static void removeLastFocusRequest(Component paramComponent)
/*     */   {
/* 159 */     kfmAccessor.removeLastFocusRequest(paramComponent);
/*     */   }
/*     */ 
/*     */   public static boolean processSynchronousLightweightTransfer(Component paramComponent1, Component paramComponent2, boolean paramBoolean1, boolean paramBoolean2, long paramLong)
/*     */   {
/* 169 */     return kfmAccessor.processSynchronousLightweightTransfer(paramComponent1, paramComponent2, paramBoolean1, paramBoolean2, paramLong);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.KeyboardFocusManagerPeerImpl
 * JD-Core Version:    0.6.2
 */