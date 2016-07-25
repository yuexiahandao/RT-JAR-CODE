/*     */ package java.awt;
/*     */ 
/*     */ import java.awt.event.AdjustmentEvent;
/*     */ import java.awt.event.AdjustmentListener;
/*     */ import java.awt.peer.ScrollPanePeer;
/*     */ import java.io.Serializable;
/*     */ import sun.awt.AWTAccessor;
/*     */ import sun.awt.AWTAccessor.ScrollPaneAdjustableAccessor;
/*     */ 
/*     */ public class ScrollPaneAdjustable
/*     */   implements Adjustable, Serializable
/*     */ {
/*     */   private ScrollPane sp;
/*     */   private int orientation;
/*     */   private int value;
/*     */   private int minimum;
/*     */   private int maximum;
/*     */   private int visibleAmount;
/*     */   private transient boolean isAdjusting;
/* 128 */   private int unitIncrement = 1;
/*     */ 
/* 139 */   private int blockIncrement = 1;
/*     */   private AdjustmentListener adjustmentListener;
/*     */   private static final String SCROLLPANE_ONLY = "Can be set by scrollpane only";
/*     */   private static final long serialVersionUID = -3359745691033257079L;
/*     */ 
/*     */   private static native void initIDs();
/*     */ 
/*     */   ScrollPaneAdjustable(ScrollPane paramScrollPane, AdjustmentListener paramAdjustmentListener, int paramInt)
/*     */   {
/* 187 */     this.sp = paramScrollPane;
/* 188 */     this.orientation = paramInt;
/* 189 */     addAdjustmentListener(paramAdjustmentListener);
/*     */   }
/*     */ 
/*     */   void setSpan(int paramInt1, int paramInt2, int paramInt3)
/*     */   {
/* 201 */     this.minimum = paramInt1;
/* 202 */     this.maximum = Math.max(paramInt2, this.minimum + 1);
/* 203 */     this.visibleAmount = Math.min(paramInt3, this.maximum - this.minimum);
/* 204 */     this.visibleAmount = Math.max(this.visibleAmount, 1);
/* 205 */     this.blockIncrement = Math.max((int)(paramInt3 * 0.9D), 1);
/* 206 */     setValue(this.value);
/*     */   }
/*     */ 
/*     */   public int getOrientation()
/*     */   {
/* 216 */     return this.orientation;
/*     */   }
/*     */ 
/*     */   public void setMinimum(int paramInt)
/*     */   {
/* 227 */     throw new AWTError("Can be set by scrollpane only");
/*     */   }
/*     */ 
/*     */   public int getMinimum()
/*     */   {
/* 233 */     return 0;
/*     */   }
/*     */ 
/*     */   public void setMaximum(int paramInt)
/*     */   {
/* 244 */     throw new AWTError("Can be set by scrollpane only");
/*     */   }
/*     */ 
/*     */   public int getMaximum() {
/* 248 */     return this.maximum;
/*     */   }
/*     */ 
/*     */   public synchronized void setUnitIncrement(int paramInt) {
/* 252 */     if (paramInt != this.unitIncrement) {
/* 253 */       this.unitIncrement = paramInt;
/* 254 */       if (this.sp.peer != null) {
/* 255 */         ScrollPanePeer localScrollPanePeer = (ScrollPanePeer)this.sp.peer;
/* 256 */         localScrollPanePeer.setUnitIncrement(this, paramInt);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getUnitIncrement() {
/* 262 */     return this.unitIncrement;
/*     */   }
/*     */ 
/*     */   public synchronized void setBlockIncrement(int paramInt) {
/* 266 */     this.blockIncrement = paramInt;
/*     */   }
/*     */ 
/*     */   public int getBlockIncrement() {
/* 270 */     return this.blockIncrement;
/*     */   }
/*     */ 
/*     */   public void setVisibleAmount(int paramInt)
/*     */   {
/* 281 */     throw new AWTError("Can be set by scrollpane only");
/*     */   }
/*     */ 
/*     */   public int getVisibleAmount() {
/* 285 */     return this.visibleAmount;
/*     */   }
/*     */ 
/*     */   public void setValueIsAdjusting(boolean paramBoolean)
/*     */   {
/* 297 */     if (this.isAdjusting != paramBoolean) {
/* 298 */       this.isAdjusting = paramBoolean;
/* 299 */       AdjustmentEvent localAdjustmentEvent = new AdjustmentEvent(this, 601, 5, this.value, paramBoolean);
/*     */ 
/* 303 */       this.adjustmentListener.adjustmentValueChanged(localAdjustmentEvent);
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean getValueIsAdjusting()
/*     */   {
/* 315 */     return this.isAdjusting;
/*     */   }
/*     */ 
/*     */   public void setValue(int paramInt)
/*     */   {
/* 328 */     setTypedValue(paramInt, 5);
/*     */   }
/*     */ 
/*     */   private void setTypedValue(int paramInt1, int paramInt2)
/*     */   {
/* 343 */     paramInt1 = Math.max(paramInt1, this.minimum);
/* 344 */     paramInt1 = Math.min(paramInt1, this.maximum - this.visibleAmount);
/*     */ 
/* 346 */     if (paramInt1 != this.value) {
/* 347 */       this.value = paramInt1;
/*     */ 
/* 351 */       AdjustmentEvent localAdjustmentEvent = new AdjustmentEvent(this, 601, paramInt2, this.value, this.isAdjusting);
/*     */ 
/* 355 */       this.adjustmentListener.adjustmentValueChanged(localAdjustmentEvent);
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getValue() {
/* 360 */     return this.value;
/*     */   }
/*     */ 
/*     */   public synchronized void addAdjustmentListener(AdjustmentListener paramAdjustmentListener)
/*     */   {
/* 378 */     if (paramAdjustmentListener == null) {
/* 379 */       return;
/*     */     }
/* 381 */     this.adjustmentListener = AWTEventMulticaster.add(this.adjustmentListener, paramAdjustmentListener);
/*     */   }
/*     */ 
/*     */   public synchronized void removeAdjustmentListener(AdjustmentListener paramAdjustmentListener)
/*     */   {
/* 400 */     if (paramAdjustmentListener == null) {
/* 401 */       return;
/*     */     }
/* 403 */     this.adjustmentListener = AWTEventMulticaster.remove(this.adjustmentListener, paramAdjustmentListener);
/*     */   }
/*     */ 
/*     */   public synchronized AdjustmentListener[] getAdjustmentListeners()
/*     */   {
/* 422 */     return (AdjustmentListener[])AWTEventMulticaster.getListeners(this.adjustmentListener, AdjustmentListener.class);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 432 */     return getClass().getName() + "[" + paramString() + "]";
/*     */   }
/*     */ 
/*     */   public String paramString()
/*     */   {
/* 445 */     return (this.orientation == 1 ? "vertical," : "horizontal,") + "[0.." + this.maximum + "]" + ",val=" + this.value + ",vis=" + this.visibleAmount + ",unit=" + this.unitIncrement + ",block=" + this.blockIncrement + ",isAdjusting=" + this.isAdjusting;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 157 */     Toolkit.loadLibraries();
/* 158 */     if (!GraphicsEnvironment.isHeadless()) {
/* 159 */       initIDs();
/*     */     }
/* 161 */     AWTAccessor.setScrollPaneAdjustableAccessor(new AWTAccessor.ScrollPaneAdjustableAccessor()
/*     */     {
/*     */       public void setTypedValue(ScrollPaneAdjustable paramAnonymousScrollPaneAdjustable, int paramAnonymousInt1, int paramAnonymousInt2)
/*     */       {
/* 165 */         paramAnonymousScrollPaneAdjustable.setTypedValue(paramAnonymousInt1, paramAnonymousInt2);
/*     */       }
/*     */     });
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.ScrollPaneAdjustable
 * JD-Core Version:    0.6.2
 */