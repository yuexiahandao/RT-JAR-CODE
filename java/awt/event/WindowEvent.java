/*     */ package java.awt.event;
/*     */ 
/*     */ import java.awt.Window;
/*     */ import sun.awt.AppContext;
/*     */ import sun.awt.SunToolkit;
/*     */ 
/*     */ public class WindowEvent extends ComponentEvent
/*     */ {
/*     */   public static final int WINDOW_FIRST = 200;
/*     */   public static final int WINDOW_OPENED = 200;
/*     */   public static final int WINDOW_CLOSING = 201;
/*     */   public static final int WINDOW_CLOSED = 202;
/*     */   public static final int WINDOW_ICONIFIED = 203;
/*     */   public static final int WINDOW_DEICONIFIED = 204;
/*     */   public static final int WINDOW_ACTIVATED = 205;
/*     */   public static final int WINDOW_DEACTIVATED = 206;
/*     */   public static final int WINDOW_GAINED_FOCUS = 207;
/*     */   public static final int WINDOW_LOST_FOCUS = 208;
/*     */   public static final int WINDOW_STATE_CHANGED = 209;
/*     */   public static final int WINDOW_LAST = 209;
/*     */   transient Window opposite;
/*     */   int oldState;
/*     */   int newState;
/*     */   private static final long serialVersionUID = -1567959133147912127L;
/*     */ 
/*     */   public WindowEvent(Window paramWindow1, int paramInt1, Window paramWindow2, int paramInt2, int paramInt3)
/*     */   {
/* 203 */     super(paramWindow1, paramInt1);
/* 204 */     this.opposite = paramWindow2;
/* 205 */     this.oldState = paramInt2;
/* 206 */     this.newState = paramInt3;
/*     */   }
/*     */ 
/*     */   public WindowEvent(Window paramWindow1, int paramInt, Window paramWindow2)
/*     */   {
/* 248 */     this(paramWindow1, paramInt, paramWindow2, 0, 0);
/*     */   }
/*     */ 
/*     */   public WindowEvent(Window paramWindow, int paramInt1, int paramInt2, int paramInt3)
/*     */   {
/* 281 */     this(paramWindow, paramInt1, null, paramInt2, paramInt3);
/*     */   }
/*     */ 
/*     */   public WindowEvent(Window paramWindow, int paramInt)
/*     */   {
/* 299 */     this(paramWindow, paramInt, null, 0, 0);
/*     */   }
/*     */ 
/*     */   public Window getWindow()
/*     */   {
/* 308 */     return (this.source instanceof Window) ? (Window)this.source : null;
/*     */   }
/*     */ 
/*     */   public Window getOppositeWindow()
/*     */   {
/* 325 */     if (this.opposite == null) {
/* 326 */       return null;
/*     */     }
/*     */ 
/* 329 */     return SunToolkit.targetToAppContext(this.opposite) == AppContext.getAppContext() ? this.opposite : null;
/*     */   }
/*     */ 
/*     */   public int getOldState()
/*     */   {
/* 355 */     return this.oldState;
/*     */   }
/*     */ 
/*     */   public int getNewState()
/*     */   {
/* 378 */     return this.newState;
/*     */   }
/*     */ 
/*     */   public String paramString()
/*     */   {
/* 389 */     switch (this.id) {
/*     */     case 200:
/* 391 */       str = "WINDOW_OPENED";
/* 392 */       break;
/*     */     case 201:
/* 394 */       str = "WINDOW_CLOSING";
/* 395 */       break;
/*     */     case 202:
/* 397 */       str = "WINDOW_CLOSED";
/* 398 */       break;
/*     */     case 203:
/* 400 */       str = "WINDOW_ICONIFIED";
/* 401 */       break;
/*     */     case 204:
/* 403 */       str = "WINDOW_DEICONIFIED";
/* 404 */       break;
/*     */     case 205:
/* 406 */       str = "WINDOW_ACTIVATED";
/* 407 */       break;
/*     */     case 206:
/* 409 */       str = "WINDOW_DEACTIVATED";
/* 410 */       break;
/*     */     case 207:
/* 412 */       str = "WINDOW_GAINED_FOCUS";
/* 413 */       break;
/*     */     case 208:
/* 415 */       str = "WINDOW_LOST_FOCUS";
/* 416 */       break;
/*     */     case 209:
/* 418 */       str = "WINDOW_STATE_CHANGED";
/* 419 */       break;
/*     */     default:
/* 421 */       str = "unknown type";
/*     */     }
/* 423 */     String str = str + ",opposite=" + getOppositeWindow() + ",oldState=" + this.oldState + ",newState=" + this.newState;
/*     */ 
/* 426 */     return str;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.event.WindowEvent
 * JD-Core Version:    0.6.2
 */