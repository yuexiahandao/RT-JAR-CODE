/*     */ package sun.awt.windows;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.Event;
/*     */ import java.awt.Point;
/*     */ import java.awt.PopupMenu;
/*     */ import java.awt.peer.PopupMenuPeer;
/*     */ import sun.awt.AWTAccessor;
/*     */ import sun.awt.AWTAccessor.MenuComponentAccessor;
/*     */ import sun.awt.AWTAccessor.PopupMenuAccessor;
/*     */ 
/*     */ public class WPopupMenuPeer extends WMenuPeer
/*     */   implements PopupMenuPeer
/*     */ {
/*     */   public WPopupMenuPeer(PopupMenu paramPopupMenu)
/*     */   {
/*  38 */     this.target = paramPopupMenu;
/*  39 */     Object localObject = null;
/*     */ 
/*  44 */     boolean bool = AWTAccessor.getPopupMenuAccessor().isTrayIconPopup(paramPopupMenu);
/*  45 */     if (bool)
/*  46 */       localObject = AWTAccessor.getMenuComponentAccessor().getParent(paramPopupMenu);
/*     */     else {
/*  48 */       localObject = paramPopupMenu.getParent();
/*     */     }
/*     */ 
/*  51 */     if ((localObject instanceof Component)) {
/*  52 */       WComponentPeer localWComponentPeer = (WComponentPeer)WToolkit.targetToPeer(localObject);
/*  53 */       if (localWComponentPeer == null)
/*     */       {
/*  58 */         localObject = WToolkit.getNativeContainer((Component)localObject);
/*  59 */         localWComponentPeer = (WComponentPeer)WToolkit.targetToPeer(localObject);
/*     */       }
/*  61 */       createMenu(localWComponentPeer);
/*     */ 
/*  63 */       checkMenuCreation();
/*     */     } else {
/*  65 */       throw new IllegalArgumentException("illegal popup menu container class");
/*     */     }
/*     */   }
/*     */ 
/*     */   native void createMenu(WComponentPeer paramWComponentPeer);
/*     */ 
/*     */   public void show(Event paramEvent)
/*     */   {
/*  73 */     Component localComponent = (Component)paramEvent.target;
/*  74 */     WComponentPeer localWComponentPeer = (WComponentPeer)WToolkit.targetToPeer(localComponent);
/*  75 */     if (localWComponentPeer == null)
/*     */     {
/*  80 */       Container localContainer = WToolkit.getNativeContainer(localComponent);
/*  81 */       paramEvent.target = localContainer;
/*     */ 
/*  84 */       for (Object localObject = localComponent; localObject != localContainer; localObject = ((Component)localObject).getParent()) {
/*  85 */         Point localPoint = ((Component)localObject).getLocation();
/*  86 */         paramEvent.x += localPoint.x;
/*  87 */         paramEvent.y += localPoint.y;
/*     */       }
/*     */     }
/*  90 */     _show(paramEvent);
/*     */   }
/*     */ 
/*     */   void show(Component paramComponent, Point paramPoint)
/*     */   {
/*  98 */     WComponentPeer localWComponentPeer = (WComponentPeer)WToolkit.targetToPeer(paramComponent);
/*  99 */     Event localEvent = new Event(paramComponent, 0L, 501, paramPoint.x, paramPoint.y, 0, 0);
/* 100 */     if (localWComponentPeer == null) {
/* 101 */       Container localContainer = WToolkit.getNativeContainer(paramComponent);
/* 102 */       localEvent.target = localContainer;
/*     */     }
/* 104 */     localEvent.x = paramPoint.x;
/* 105 */     localEvent.y = paramPoint.y;
/* 106 */     _show(localEvent);
/*     */   }
/*     */ 
/*     */   public native void _show(Event paramEvent);
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.windows.WPopupMenuPeer
 * JD-Core Version:    0.6.2
 */