/*     */ package sun.awt;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.Cursor;
/*     */ import java.awt.IllegalComponentStateException;
/*     */ import java.awt.Point;
/*     */ import java.awt.Toolkit;
/*     */ import java.awt.Window;
/*     */ import java.awt.event.InputEvent;
/*     */ import java.awt.event.InvocationEvent;
/*     */ 
/*     */ public abstract class GlobalCursorManager
/*     */ {
/*  74 */   private final NativeUpdater nativeUpdater = new NativeUpdater();
/*     */   private long lastUpdateMillis;
/*  85 */   private final Object lastUpdateLock = new Object();
/*     */ 
/*     */   public void updateCursorImmediately()
/*     */   {
/*  92 */     synchronized (this.nativeUpdater) {
/*  93 */       this.nativeUpdater.pending = false;
/*     */     }
/*  95 */     _updateCursor(false);
/*     */   }
/*     */ 
/*     */   public void updateCursorImmediately(InputEvent paramInputEvent)
/*     */   {
/*     */     int i;
/* 106 */     synchronized (this.lastUpdateLock) {
/* 107 */       i = paramInputEvent.getWhen() >= this.lastUpdateMillis ? 1 : 0;
/*     */     }
/* 109 */     if (i != 0)
/* 110 */       _updateCursor(true);
/*     */   }
/*     */ 
/*     */   public void updateCursorLater(Component paramComponent)
/*     */   {
/* 119 */     this.nativeUpdater.postIfNotPending(paramComponent, new InvocationEvent(Toolkit.getDefaultToolkit(), this.nativeUpdater));
/*     */   }
/*     */ 
/*     */   protected abstract void setCursor(Component paramComponent, Cursor paramCursor, boolean paramBoolean);
/*     */ 
/*     */   protected abstract void getCursorPos(Point paramPoint);
/*     */ 
/*     */   protected abstract Component findComponentAt(Container paramContainer, int paramInt1, int paramInt2);
/*     */ 
/*     */   protected abstract Point getLocationOnScreen(Component paramComponent);
/*     */ 
/*     */   protected abstract Component findHeavyweightUnderCursor(boolean paramBoolean);
/*     */ 
/*     */   private void _updateCursor(boolean paramBoolean)
/*     */   {
/* 171 */     synchronized (this.lastUpdateLock) {
/* 172 */       this.lastUpdateMillis = System.currentTimeMillis();
/*     */     }
/*     */ 
/* 175 */     ??? = null; Point localPoint = null;
/*     */     try
/*     */     {
/* 179 */       Object localObject2 = findHeavyweightUnderCursor(paramBoolean);
/* 180 */       if (localObject2 == null) {
/* 181 */         updateCursorOutOfJava();
/* 182 */         return;
/*     */       }
/*     */ 
/* 185 */       if ((localObject2 instanceof Window))
/* 186 */         localPoint = AWTAccessor.getComponentAccessor().getLocation((Component)localObject2);
/* 187 */       else if ((localObject2 instanceof Container)) {
/* 188 */         localPoint = getLocationOnScreen((Component)localObject2);
/*     */       }
/* 190 */       if (localPoint != null) {
/* 191 */         ??? = new Point();
/* 192 */         getCursorPos((Point)???);
/* 193 */         Component localComponent = findComponentAt((Container)localObject2, ((Point)???).x - localPoint.x, ((Point)???).y - localPoint.y);
/*     */ 
/* 200 */         if (localComponent != null) {
/* 201 */           localObject2 = localComponent;
/*     */         }
/*     */       }
/*     */ 
/* 205 */       setCursor((Component)localObject2, AWTAccessor.getComponentAccessor().getCursor((Component)localObject2), paramBoolean);
/*     */     }
/*     */     catch (IllegalComponentStateException localIllegalComponentStateException)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void updateCursorOutOfJava()
/*     */   {
/*     */   }
/*     */ 
/*     */   class NativeUpdater
/*     */     implements Runnable
/*     */   {
/*  41 */     boolean pending = false;
/*     */ 
/*     */     NativeUpdater() {  } 
/*  44 */     public void run() { int i = 0;
/*  45 */       synchronized (this) {
/*  46 */         if (this.pending) {
/*  47 */           this.pending = false;
/*  48 */           i = 1;
/*     */         }
/*     */       }
/*  51 */       if (i != 0)
/*  52 */         GlobalCursorManager.this._updateCursor(false);
/*     */     }
/*     */ 
/*     */     public void postIfNotPending(Component paramComponent, InvocationEvent paramInvocationEvent)
/*     */     {
/*  57 */       int i = 0;
/*  58 */       synchronized (this) {
/*  59 */         if (!this.pending) {
/*  60 */           this.pending = (i = 1);
/*     */         }
/*     */       }
/*  63 */       if (i != 0)
/*  64 */         SunToolkit.postEvent(SunToolkit.targetToAppContext(paramComponent), paramInvocationEvent);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.GlobalCursorManager
 * JD-Core Version:    0.6.2
 */