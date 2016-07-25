/*     */ package sun.awt.windows;
/*     */ 
/*     */ import java.awt.Adjustable;
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Insets;
/*     */ import java.awt.Point;
/*     */ import java.awt.ScrollPane;
/*     */ import java.awt.ScrollPaneAdjustable;
/*     */ import java.awt.peer.ScrollPanePeer;
/*     */ import sun.awt.PeerEvent;
/*     */ import sun.util.logging.PlatformLogger;
/*     */ 
/*     */ class WScrollPanePeer extends WPanelPeer
/*     */   implements ScrollPanePeer
/*     */ {
/*  36 */   private static final PlatformLogger log = PlatformLogger.getLogger("sun.awt.windows.WScrollPanePeer");
/*     */   int scrollbarWidth;
/*     */   int scrollbarHeight;
/*     */   int prevx;
/*     */   int prevy;
/*     */ 
/*     */   static native void initIDs();
/*     */ 
/*     */   native void create(WComponentPeer paramWComponentPeer);
/*     */ 
/*     */   native int getOffset(int paramInt);
/*     */ 
/*     */   WScrollPanePeer(Component paramComponent)
/*     */   {
/*  52 */     super(paramComponent);
/*  53 */     this.scrollbarWidth = _getVScrollbarWidth();
/*  54 */     this.scrollbarHeight = _getHScrollbarHeight();
/*     */   }
/*     */ 
/*     */   void initialize() {
/*  58 */     super.initialize();
/*  59 */     setInsets();
/*  60 */     Insets localInsets = getInsets();
/*  61 */     setScrollPosition(-localInsets.left, -localInsets.top);
/*     */   }
/*     */ 
/*     */   public void setUnitIncrement(Adjustable paramAdjustable, int paramInt)
/*     */   {
/*     */   }
/*     */ 
/*     */   public Insets insets() {
/*  69 */     return getInsets();
/*     */   }
/*     */   private native void setInsets();
/*     */ 
/*     */   public synchronized native void setScrollPosition(int paramInt1, int paramInt2);
/*     */ 
/*     */   public int getHScrollbarHeight() {
/*  76 */     return this.scrollbarHeight;
/*     */   }
/*     */   private native int _getHScrollbarHeight();
/*     */ 
/*     */   public int getVScrollbarWidth() {
/*  81 */     return this.scrollbarWidth;
/*     */   }
/*     */   private native int _getVScrollbarWidth();
/*     */ 
/*     */   public Point getScrollOffset() {
/*  86 */     int i = getOffset(0);
/*  87 */     int j = getOffset(1);
/*  88 */     return new Point(i, j);
/*     */   }
/*     */ 
/*     */   public void childResized(int paramInt1, int paramInt2)
/*     */   {
/*  98 */     ScrollPane localScrollPane = (ScrollPane)this.target;
/*  99 */     Dimension localDimension = localScrollPane.getSize();
/* 100 */     setSpans(localDimension.width, localDimension.height, paramInt1, paramInt2);
/* 101 */     setInsets();
/*     */   }
/*     */ 
/*     */   synchronized native void setSpans(int paramInt1, int paramInt2, int paramInt3, int paramInt4);
/*     */ 
/*     */   public void setValue(Adjustable paramAdjustable, int paramInt)
/*     */   {
/* 114 */     Component localComponent = getScrollChild();
/* 115 */     if (localComponent == null) {
/* 116 */       return;
/*     */     }
/*     */ 
/* 119 */     Point localPoint = localComponent.getLocation();
/* 120 */     switch (paramAdjustable.getOrientation()) {
/*     */     case 1:
/* 122 */       setScrollPosition(-localPoint.x, paramInt);
/* 123 */       break;
/*     */     case 0:
/* 125 */       setScrollPosition(paramInt, -localPoint.y);
/*     */     }
/*     */   }
/*     */ 
/*     */   private Component getScrollChild()
/*     */   {
/* 131 */     ScrollPane localScrollPane = (ScrollPane)this.target;
/* 132 */     Component localComponent = null;
/*     */     try {
/* 134 */       localComponent = localScrollPane.getComponent(0);
/*     */     }
/*     */     catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException) {
/*     */     }
/* 138 */     return localComponent;
/*     */   }
/*     */ 
/*     */   private void postScrollEvent(int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean)
/*     */   {
/* 147 */     Adjustor localAdjustor = new Adjustor(paramInt1, paramInt2, paramInt3, paramBoolean);
/* 148 */     WToolkit.executeOnEventHandlerThread(new ScrollEvent(this.target, localAdjustor));
/*     */   }
/*     */ 
/*     */   native void setTypedValue(ScrollPaneAdjustable paramScrollPaneAdjustable, int paramInt1, int paramInt2);
/*     */ 
/*     */   static
/*     */   {
/*  44 */     initIDs();
/*     */   }
/*     */ 
/*     */   class Adjustor
/*     */     implements Runnable
/*     */   {
/*     */     int orient;
/*     */     int type;
/*     */     int pos;
/*     */     boolean isAdjusting;
/*     */ 
/*     */     Adjustor(int paramInt1, int paramInt2, int paramBoolean, boolean arg5)
/*     */     {
/* 184 */       this.orient = paramInt1;
/* 185 */       this.type = paramInt2;
/* 186 */       this.pos = paramBoolean;
/*     */       boolean bool;
/* 187 */       this.isAdjusting = bool;
/*     */     }
/*     */ 
/*     */     public void run() {
/* 191 */       if (WScrollPanePeer.this.getScrollChild() == null) {
/* 192 */         return;
/*     */       }
/* 194 */       ScrollPane localScrollPane = (ScrollPane)WScrollPanePeer.this.target;
/* 195 */       ScrollPaneAdjustable localScrollPaneAdjustable = null;
/*     */ 
/* 202 */       if (this.orient == 1)
/* 203 */         localScrollPaneAdjustable = (ScrollPaneAdjustable)localScrollPane.getVAdjustable();
/* 204 */       else if (this.orient == 0) {
/* 205 */         localScrollPaneAdjustable = (ScrollPaneAdjustable)localScrollPane.getHAdjustable();
/*     */       }
/* 207 */       else if (WScrollPanePeer.log.isLoggable(500)) {
/* 208 */         WScrollPanePeer.log.fine("Assertion failed: unknown orient");
/*     */       }
/*     */ 
/* 212 */       if (localScrollPaneAdjustable == null) {
/* 213 */         return;
/*     */       }
/*     */ 
/* 216 */       int i = localScrollPaneAdjustable.getValue();
/* 217 */       switch (this.type) {
/*     */       case 2:
/* 219 */         i -= localScrollPaneAdjustable.getUnitIncrement();
/* 220 */         break;
/*     */       case 1:
/* 222 */         i += localScrollPaneAdjustable.getUnitIncrement();
/* 223 */         break;
/*     */       case 3:
/* 225 */         i -= localScrollPaneAdjustable.getBlockIncrement();
/* 226 */         break;
/*     */       case 4:
/* 228 */         i += localScrollPaneAdjustable.getBlockIncrement();
/* 229 */         break;
/*     */       case 5:
/* 231 */         i = this.pos;
/* 232 */         break;
/*     */       default:
/* 234 */         if (WScrollPanePeer.log.isLoggable(500)) {
/* 235 */           WScrollPanePeer.log.fine("Assertion failed: unknown type");
/*     */         }
/* 237 */         return;
/*     */       }
/*     */ 
/* 241 */       i = Math.max(localScrollPaneAdjustable.getMinimum(), i);
/* 242 */       i = Math.min(localScrollPaneAdjustable.getMaximum(), i);
/*     */ 
/* 245 */       localScrollPaneAdjustable.setValueIsAdjusting(this.isAdjusting);
/*     */ 
/* 251 */       WScrollPanePeer.this.setTypedValue(localScrollPaneAdjustable, i, this.type);
/*     */ 
/* 255 */       Object localObject = WScrollPanePeer.this.getScrollChild();
/*     */ 
/* 257 */       while ((localObject != null) && (!(((Component)localObject).getPeer() instanceof WComponentPeer)))
/*     */       {
/* 259 */         localObject = ((Component)localObject).getParent();
/*     */       }
/* 261 */       if ((WScrollPanePeer.log.isLoggable(500)) && 
/* 262 */         (localObject == null)) {
/* 263 */         WScrollPanePeer.log.fine("Assertion (hwAncestor != null) failed, couldn't find heavyweight ancestor of scroll pane child");
/*     */       }
/*     */ 
/* 267 */       WComponentPeer localWComponentPeer = (WComponentPeer)((Component)localObject).getPeer();
/* 268 */       localWComponentPeer.paintDamagedAreaImmediately();
/*     */     }
/*     */   }
/*     */ 
/*     */   class ScrollEvent extends PeerEvent
/*     */   {
/*     */     ScrollEvent(Object paramRunnable, Runnable arg3)
/*     */     {
/* 158 */       super(localRunnable, 0L);
/*     */     }
/*     */ 
/*     */     public PeerEvent coalesceEvents(PeerEvent paramPeerEvent) {
/* 162 */       if (WScrollPanePeer.log.isLoggable(300)) {
/* 163 */         WScrollPanePeer.log.finest("ScrollEvent coalesced: " + paramPeerEvent);
/*     */       }
/* 165 */       if ((paramPeerEvent instanceof ScrollEvent)) {
/* 166 */         return paramPeerEvent;
/*     */       }
/* 168 */       return null;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.windows.WScrollPanePeer
 * JD-Core Version:    0.6.2
 */