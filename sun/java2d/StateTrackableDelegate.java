/*     */ package sun.java2d;
/*     */ 
/*     */ public final class StateTrackableDelegate
/*     */   implements StateTrackable
/*     */ {
/*  47 */   public static final StateTrackableDelegate UNTRACKABLE_DELEGATE = new StateTrackableDelegate(StateTrackable.State.UNTRACKABLE);
/*     */ 
/*  55 */   public static final StateTrackableDelegate IMMUTABLE_DELEGATE = new StateTrackableDelegate(StateTrackable.State.IMMUTABLE);
/*     */   private StateTrackable.State theState;
/*     */   StateTracker theTracker;
/*     */   private int numDynamicAgents;
/*     */ 
/*     */   public static StateTrackableDelegate createInstance(StateTrackable.State paramState)
/*     */   {
/*  69 */     switch (2.$SwitchMap$sun$java2d$StateTrackable$State[paramState.ordinal()]) {
/*     */     case 1:
/*  71 */       return UNTRACKABLE_DELEGATE;
/*     */     case 2:
/*  73 */       return new StateTrackableDelegate(StateTrackable.State.STABLE);
/*     */     case 3:
/*  75 */       return new StateTrackableDelegate(StateTrackable.State.DYNAMIC);
/*     */     case 4:
/*  77 */       return IMMUTABLE_DELEGATE;
/*     */     }
/*  79 */     throw new InternalError("unknown state");
/*     */   }
/*     */ 
/*     */   private StateTrackableDelegate(StateTrackable.State paramState)
/*     */   {
/*  92 */     this.theState = paramState;
/*     */   }
/*     */ 
/*     */   public StateTrackable.State getState()
/*     */   {
/* 100 */     return this.theState;
/*     */   }
/*     */ 
/*     */   public synchronized StateTracker getStateTracker()
/*     */   {
/* 108 */     Object localObject = this.theTracker;
/* 109 */     if (localObject == null) {
/* 110 */       switch (2.$SwitchMap$sun$java2d$StateTrackable$State[this.theState.ordinal()]) {
/*     */       case 4:
/* 112 */         localObject = StateTracker.ALWAYS_CURRENT;
/* 113 */         break;
/*     */       case 2:
/* 115 */         localObject = new StateTracker() {
/*     */           public boolean isCurrent() {
/* 117 */             return StateTrackableDelegate.this.theTracker == this;
/*     */           }
/*     */         };
/* 120 */         break;
/*     */       case 1:
/*     */       case 3:
/* 126 */         localObject = StateTracker.NEVER_CURRENT;
/*     */       }
/*     */ 
/* 129 */       this.theTracker = ((StateTracker)localObject);
/*     */     }
/* 131 */     return localObject;
/*     */   }
/*     */ 
/*     */   public synchronized void setImmutable()
/*     */   {
/* 144 */     if ((this.theState == StateTrackable.State.UNTRACKABLE) || (this.theState == StateTrackable.State.DYNAMIC)) {
/* 145 */       throw new IllegalStateException("UNTRACKABLE or DYNAMIC objects cannot become IMMUTABLE");
/*     */     }
/*     */ 
/* 148 */     this.theState = StateTrackable.State.IMMUTABLE;
/* 149 */     this.theTracker = null;
/*     */   }
/*     */ 
/*     */   public synchronized void setUntrackable()
/*     */   {
/* 164 */     if (this.theState == StateTrackable.State.IMMUTABLE) {
/* 165 */       throw new IllegalStateException("IMMUTABLE objects cannot become UNTRACKABLE");
/*     */     }
/*     */ 
/* 168 */     this.theState = StateTrackable.State.UNTRACKABLE;
/* 169 */     this.theTracker = null;
/*     */   }
/*     */ 
/*     */   public synchronized void addDynamicAgent()
/*     */   {
/* 195 */     if (this.theState == StateTrackable.State.IMMUTABLE) {
/* 196 */       throw new IllegalStateException("Cannot change state from IMMUTABLE");
/*     */     }
/*     */ 
/* 199 */     this.numDynamicAgents += 1;
/* 200 */     if (this.theState == StateTrackable.State.STABLE) {
/* 201 */       this.theState = StateTrackable.State.DYNAMIC;
/* 202 */       this.theTracker = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected synchronized void removeDynamicAgent()
/*     */   {
/* 230 */     if ((--this.numDynamicAgents == 0) && (this.theState == StateTrackable.State.DYNAMIC)) {
/* 231 */       this.theState = StateTrackable.State.STABLE;
/* 232 */       this.theTracker = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void markDirty()
/*     */   {
/* 254 */     this.theTracker = null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.StateTrackableDelegate
 * JD-Core Version:    0.6.2
 */