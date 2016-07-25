/*     */ package com.sun.java.swing.plaf.windows;
/*     */ 
/*     */ import java.awt.AlphaComposite;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.security.AccessController;
/*     */ import java.util.ArrayList;
/*     */ import java.util.EnumMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.WeakHashMap;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JTabbedPane;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.Timer;
/*     */ import javax.swing.UIManager;
/*     */ import sun.awt.AppContext;
/*     */ import sun.security.action.GetBooleanAction;
/*     */ import sun.swing.UIClientPropertyKey;
/*     */ 
/*     */ class AnimationController
/*     */   implements ActionListener, PropertyChangeListener
/*     */ {
/*  72 */   private static final boolean VISTA_ANIMATION_DISABLED = ((Boolean)AccessController.doPrivileged(new GetBooleanAction("swing.disablevistaanimation"))).booleanValue();
/*     */ 
/*  76 */   private static final Object ANIMATION_CONTROLLER_KEY = new StringBuilder("ANIMATION_CONTROLLER_KEY");
/*     */ 
/*  79 */   private final Map<JComponent, Map<TMSchema.Part, AnimationState>> animationStateMap = new WeakHashMap();
/*     */ 
/*  84 */   private final Timer timer = new Timer(33, this);
/*     */ 
/*     */   private static synchronized AnimationController getAnimationController()
/*     */   {
/*  88 */     AppContext localAppContext = AppContext.getAppContext();
/*  89 */     Object localObject = localAppContext.get(ANIMATION_CONTROLLER_KEY);
/*  90 */     if (localObject == null) {
/*  91 */       localObject = new AnimationController();
/*  92 */       localAppContext.put(ANIMATION_CONTROLLER_KEY, localObject);
/*     */     }
/*  94 */     return (AnimationController)localObject;
/*     */   }
/*     */ 
/*     */   private AnimationController() {
/*  98 */     this.timer.setRepeats(true);
/*  99 */     this.timer.setCoalesce(true);
/*     */ 
/* 101 */     UIManager.addPropertyChangeListener(this);
/*     */   }
/*     */ 
/*     */   private static void triggerAnimation(JComponent paramJComponent, TMSchema.Part paramPart, TMSchema.State paramState)
/*     */   {
/* 106 */     if (((paramJComponent instanceof JTabbedPane)) || (paramPart == TMSchema.Part.TP_BUTTON))
/*     */     {
/* 113 */       return;
/*     */     }
/* 115 */     AnimationController localAnimationController = getAnimationController();
/*     */ 
/* 117 */     TMSchema.State localState = localAnimationController.getState(paramJComponent, paramPart);
/* 118 */     if (localState != paramState) {
/* 119 */       localAnimationController.putState(paramJComponent, paramPart, paramState);
/* 120 */       if (paramState == TMSchema.State.DEFAULTED)
/*     */       {
/* 123 */         localState = TMSchema.State.HOT;
/*     */       }
/* 125 */       if (localState != null)
/*     */       {
/*     */         long l;
/* 127 */         if (paramState == TMSchema.State.DEFAULTED)
/*     */         {
/* 131 */           l = 1000L;
/*     */         } else {
/* 133 */           XPStyle localXPStyle = XPStyle.getXP();
/* 134 */           l = localXPStyle != null ? localXPStyle.getThemeTransitionDuration(paramJComponent, paramPart, normalizeState(localState), normalizeState(paramState), TMSchema.Prop.TRANSITIONDURATIONS) : 1000L;
/*     */         }
/*     */ 
/* 142 */         localAnimationController.startAnimation(paramJComponent, paramPart, localState, paramState, l);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private static TMSchema.State normalizeState(TMSchema.State paramState)
/*     */   {
/*     */     TMSchema.State localState;
/* 152 */     switch (1.$SwitchMap$com$sun$java$swing$plaf$windows$TMSchema$State[paramState.ordinal()])
/*     */     {
/*     */     case 1:
/*     */     case 2:
/*     */     case 3:
/* 158 */       localState = TMSchema.State.UPPRESSED;
/* 159 */       break;
/*     */     case 4:
/*     */     case 5:
/*     */     case 6:
/* 166 */       localState = TMSchema.State.UPDISABLED;
/* 167 */       break;
/*     */     case 7:
/*     */     case 8:
/*     */     case 9:
/* 174 */       localState = TMSchema.State.UPHOT;
/* 175 */       break;
/*     */     case 10:
/*     */     case 11:
/*     */     case 12:
/* 182 */       localState = TMSchema.State.UPNORMAL;
/* 183 */       break;
/*     */     default:
/* 186 */       localState = paramState;
/*     */     }
/*     */ 
/* 189 */     return localState;
/*     */   }
/*     */ 
/*     */   private synchronized TMSchema.State getState(JComponent paramJComponent, TMSchema.Part paramPart) {
/* 193 */     TMSchema.State localState = null;
/* 194 */     Object localObject = paramJComponent.getClientProperty(PartUIClientPropertyKey.getKey(paramPart));
/*     */ 
/* 196 */     if ((localObject instanceof TMSchema.State)) {
/* 197 */       localState = (TMSchema.State)localObject;
/*     */     }
/* 199 */     return localState;
/*     */   }
/*     */ 
/*     */   private synchronized void putState(JComponent paramJComponent, TMSchema.Part paramPart, TMSchema.State paramState)
/*     */   {
/* 204 */     paramJComponent.putClientProperty(PartUIClientPropertyKey.getKey(paramPart), paramState);
/*     */   }
/*     */ 
/*     */   private synchronized void startAnimation(JComponent paramJComponent, TMSchema.Part paramPart, TMSchema.State paramState1, TMSchema.State paramState2, long paramLong)
/*     */   {
/* 213 */     boolean bool = false;
/* 214 */     if (paramState2 == TMSchema.State.DEFAULTED) {
/* 215 */       bool = true;
/*     */     }
/* 217 */     Object localObject = (Map)this.animationStateMap.get(paramJComponent);
/* 218 */     if (paramLong <= 0L) {
/* 219 */       if (localObject != null) {
/* 220 */         ((Map)localObject).remove(paramPart);
/* 221 */         if (((Map)localObject).size() == 0) {
/* 222 */           this.animationStateMap.remove(paramJComponent);
/*     */         }
/*     */       }
/* 225 */       return;
/*     */     }
/* 227 */     if (localObject == null) {
/* 228 */       localObject = new EnumMap(TMSchema.Part.class);
/* 229 */       this.animationStateMap.put(paramJComponent, localObject);
/*     */     }
/* 231 */     ((Map)localObject).put(paramPart, new AnimationState(paramState1, paramLong, bool));
/*     */ 
/* 233 */     if (!this.timer.isRunning())
/* 234 */       this.timer.start();
/*     */   }
/*     */ 
/*     */   static void paintSkin(JComponent paramJComponent, XPStyle.Skin paramSkin, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, TMSchema.State paramState)
/*     */   {
/* 240 */     if (VISTA_ANIMATION_DISABLED) {
/* 241 */       paramSkin.paintSkinRaw(paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramState);
/* 242 */       return;
/*     */     }
/* 244 */     triggerAnimation(paramJComponent, paramSkin.part, paramState);
/* 245 */     AnimationController localAnimationController = getAnimationController();
/* 246 */     synchronized (localAnimationController) {
/* 247 */       AnimationState localAnimationState = null;
/* 248 */       Map localMap = (Map)localAnimationController.animationStateMap.get(paramJComponent);
/*     */ 
/* 250 */       if (localMap != null) {
/* 251 */         localAnimationState = (AnimationState)localMap.get(paramSkin.part);
/*     */       }
/* 253 */       if (localAnimationState != null)
/* 254 */         localAnimationState.paintSkin(paramSkin, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramState);
/*     */       else
/* 256 */         paramSkin.paintSkinRaw(paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramState);
/*     */     }
/*     */   }
/*     */ 
/*     */   public synchronized void propertyChange(PropertyChangeEvent paramPropertyChangeEvent)
/*     */   {
/* 262 */     if (("lookAndFeel" == paramPropertyChangeEvent.getPropertyName()) && (!(paramPropertyChangeEvent.getNewValue() instanceof WindowsLookAndFeel)))
/*     */     {
/* 264 */       dispose();
/*     */     }
/*     */   }
/*     */ 
/*     */   public synchronized void actionPerformed(ActionEvent paramActionEvent) {
/* 269 */     ArrayList localArrayList1 = null;
/* 270 */     ArrayList localArrayList2 = null;
/* 271 */     for (Iterator localIterator1 = this.animationStateMap.keySet().iterator(); localIterator1.hasNext(); ) { localJComponent = (JComponent)localIterator1.next();
/* 272 */       localJComponent.repaint();
/* 273 */       if (localArrayList2 != null) {
/* 274 */         localArrayList2.clear();
/*     */       }
/* 276 */       localMap = (Map)this.animationStateMap.get(localJComponent);
/* 277 */       if ((!localJComponent.isShowing()) || (localMap == null) || (localMap.size() == 0))
/*     */       {
/* 280 */         if (localArrayList1 == null) {
/* 281 */           localArrayList1 = new ArrayList();
/*     */         }
/* 283 */         localArrayList1.add(localJComponent);
/*     */       }
/*     */       else {
/* 286 */         for (localIterator2 = localMap.keySet().iterator(); localIterator2.hasNext(); ) { localPart = (TMSchema.Part)localIterator2.next();
/* 287 */           if (((AnimationState)localMap.get(localPart)).isDone()) {
/* 288 */             if (localArrayList2 == null) {
/* 289 */               localArrayList2 = new ArrayList();
/*     */             }
/* 291 */             localArrayList2.add(localPart);
/*     */           }
/*     */         }
/* 294 */         if (localArrayList2 != null)
/* 295 */           if (localArrayList2.size() == localMap.size())
/*     */           {
/* 297 */             if (localArrayList1 == null) {
/* 298 */               localArrayList1 = new ArrayList();
/*     */             }
/* 300 */             localArrayList1.add(localJComponent);
/*     */           } else {
/* 302 */             for (localIterator2 = localArrayList2.iterator(); localIterator2.hasNext(); ) { localPart = (TMSchema.Part)localIterator2.next();
/* 303 */               localMap.remove(localPart);
/*     */             }
/*     */           }
/*     */       }
/*     */     }
/*     */     JComponent localJComponent;
/*     */     Map localMap;
/*     */     Iterator localIterator2;
/*     */     TMSchema.Part localPart;
/* 308 */     if (localArrayList1 != null) {
/* 309 */       for (localIterator1 = localArrayList1.iterator(); localIterator1.hasNext(); ) { localJComponent = (JComponent)localIterator1.next();
/* 310 */         this.animationStateMap.remove(localJComponent);
/*     */       }
/*     */     }
/* 313 */     if (this.animationStateMap.size() == 0)
/* 314 */       this.timer.stop();
/*     */   }
/*     */ 
/*     */   private synchronized void dispose()
/*     */   {
/* 319 */     this.timer.stop();
/* 320 */     UIManager.removePropertyChangeListener(this);
/* 321 */     synchronized (AnimationController.class) {
/* 322 */       AppContext.getAppContext().put(ANIMATION_CONTROLLER_KEY, null);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class AnimationState
/*     */   {
/*     */     private final TMSchema.State startState;
/*     */     private final long duration;
/*     */     private long startTime;
/* 339 */     private boolean isForward = true;
/*     */     private boolean isForwardAndReverse;
/*     */     private float progress;
/*     */ 
/*     */     AnimationState(TMSchema.State paramState, long paramLong, boolean paramBoolean)
/*     */     {
/* 351 */       assert ((paramState != null) && (paramLong > 0L));
/* 352 */       assert (SwingUtilities.isEventDispatchThread());
/*     */ 
/* 354 */       this.startState = paramState;
/* 355 */       this.duration = (paramLong * 1000000L);
/* 356 */       this.startTime = System.nanoTime();
/* 357 */       this.isForwardAndReverse = paramBoolean;
/* 358 */       this.progress = 0.0F;
/*     */     }
/*     */     private void updateProgress() {
/* 361 */       assert (SwingUtilities.isEventDispatchThread());
/*     */ 
/* 363 */       if (isDone()) {
/* 364 */         return;
/*     */       }
/* 366 */       long l = System.nanoTime();
/*     */ 
/* 368 */       this.progress = ((float)(l - this.startTime) / (float)this.duration);
/*     */ 
/* 370 */       this.progress = Math.max(this.progress, 0.0F);
/* 371 */       if (this.progress >= 1.0F) {
/* 372 */         this.progress = 1.0F;
/* 373 */         if (this.isForwardAndReverse) {
/* 374 */           this.startTime = l;
/* 375 */           this.progress = 0.0F;
/* 376 */           this.isForward = (!this.isForward);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/*     */     void paintSkin(XPStyle.Skin paramSkin, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, TMSchema.State paramState) {
/* 382 */       assert (SwingUtilities.isEventDispatchThread());
/*     */ 
/* 384 */       updateProgress();
/* 385 */       if (!isDone()) {
/* 386 */         Graphics2D localGraphics2D = (Graphics2D)paramGraphics.create();
/* 387 */         paramSkin.paintSkinRaw(localGraphics2D, paramInt1, paramInt2, paramInt3, paramInt4, this.startState);
/*     */         float f;
/* 389 */         if (this.isForward)
/* 390 */           f = this.progress;
/*     */         else {
/* 392 */           f = 1.0F - this.progress;
/*     */         }
/* 394 */         localGraphics2D.setComposite(AlphaComposite.SrcOver.derive(f));
/* 395 */         paramSkin.paintSkinRaw(localGraphics2D, paramInt1, paramInt2, paramInt3, paramInt4, paramState);
/* 396 */         localGraphics2D.dispose();
/*     */       } else {
/* 398 */         paramSkin.paintSkinRaw(paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramState);
/*     */       }
/*     */     }
/*     */ 
/* 402 */     boolean isDone() { assert (SwingUtilities.isEventDispatchThread());
/*     */ 
/* 404 */       return this.progress >= 1.0F; }
/*     */   }
/*     */ 
/*     */   private static class PartUIClientPropertyKey implements UIClientPropertyKey
/*     */   {
/* 411 */     private static final Map<TMSchema.Part, PartUIClientPropertyKey> map = new EnumMap(TMSchema.Part.class);
/*     */     private final TMSchema.Part part;
/*     */ 
/*     */     static synchronized PartUIClientPropertyKey getKey(TMSchema.Part paramPart)
/*     */     {
/* 415 */       PartUIClientPropertyKey localPartUIClientPropertyKey = (PartUIClientPropertyKey)map.get(paramPart);
/* 416 */       if (localPartUIClientPropertyKey == null) {
/* 417 */         localPartUIClientPropertyKey = new PartUIClientPropertyKey(paramPart);
/* 418 */         map.put(paramPart, localPartUIClientPropertyKey);
/*     */       }
/* 420 */       return localPartUIClientPropertyKey;
/*     */     }
/*     */ 
/*     */     private PartUIClientPropertyKey(TMSchema.Part paramPart)
/*     */     {
/* 425 */       this.part = paramPart;
/*     */     }
/*     */     public String toString() {
/* 428 */       return this.part.toString();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.java.swing.plaf.windows.AnimationController
 * JD-Core Version:    0.6.2
 */