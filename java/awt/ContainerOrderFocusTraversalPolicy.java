/*     */ package java.awt;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import sun.util.logging.PlatformLogger;
/*     */ 
/*     */ public class ContainerOrderFocusTraversalPolicy extends FocusTraversalPolicy
/*     */   implements Serializable
/*     */ {
/*  63 */   private static final PlatformLogger log = PlatformLogger.getLogger("java.awt.ContainerOrderFocusTraversalPolicy");
/*     */ 
/*  65 */   private final int FORWARD_TRAVERSAL = 0;
/*  66 */   private final int BACKWARD_TRAVERSAL = 1;
/*     */   private static final long serialVersionUID = 486933713763926351L;
/*  73 */   private boolean implicitDownCycleTraversal = true;
/*     */   private transient Container cachedRoot;
/*     */   private transient List cachedCycle;
/*     */ 
/*     */   private List<Component> getFocusTraversalCycle(Container paramContainer)
/*     */   {
/* 106 */     ArrayList localArrayList = new ArrayList();
/* 107 */     enumerateCycle(paramContainer, localArrayList);
/* 108 */     return localArrayList;
/*     */   }
/*     */   private int getComponentIndex(List<Component> paramList, Component paramComponent) {
/* 111 */     return paramList.indexOf(paramComponent);
/*     */   }
/*     */ 
/*     */   private void enumerateCycle(Container paramContainer, List paramList) {
/* 115 */     if ((!paramContainer.isVisible()) || (!paramContainer.isDisplayable())) {
/* 116 */       return;
/*     */     }
/*     */ 
/* 119 */     paramList.add(paramContainer);
/*     */ 
/* 121 */     Component[] arrayOfComponent = paramContainer.getComponents();
/* 122 */     for (int i = 0; i < arrayOfComponent.length; i++) {
/* 123 */       Component localComponent = arrayOfComponent[i];
/* 124 */       if ((localComponent instanceof Container)) {
/* 125 */         Container localContainer = (Container)localComponent;
/*     */ 
/* 127 */         if ((!localContainer.isFocusCycleRoot()) && (!localContainer.isFocusTraversalPolicyProvider())) {
/* 128 */           enumerateCycle(localContainer, paramList);
/* 129 */           continue;
/*     */         }
/*     */       }
/* 132 */       paramList.add(localComponent);
/*     */     }
/*     */   }
/*     */ 
/*     */   private Container getTopmostProvider(Container paramContainer, Component paramComponent) {
/* 137 */     Container localContainer1 = paramComponent.getParent();
/* 138 */     Container localContainer2 = null;
/* 139 */     while ((localContainer1 != paramContainer) && (localContainer1 != null)) {
/* 140 */       if (localContainer1.isFocusTraversalPolicyProvider()) {
/* 141 */         localContainer2 = localContainer1;
/*     */       }
/* 143 */       localContainer1 = localContainer1.getParent();
/*     */     }
/* 145 */     if (localContainer1 == null) {
/* 146 */       return null;
/*     */     }
/* 148 */     return localContainer2;
/*     */   }
/*     */ 
/*     */   private Component getComponentDownCycle(Component paramComponent, int paramInt)
/*     */   {
/* 159 */     Component localComponent = null;
/*     */ 
/* 161 */     if ((paramComponent instanceof Container)) {
/* 162 */       Container localContainer = (Container)paramComponent;
/*     */ 
/* 164 */       if (localContainer.isFocusCycleRoot()) {
/* 165 */         if (getImplicitDownCycleTraversal()) {
/* 166 */           localComponent = localContainer.getFocusTraversalPolicy().getDefaultComponent(localContainer);
/*     */ 
/* 168 */           if ((localComponent != null) && (log.isLoggable(500)))
/* 169 */             log.fine("### Transfered focus down-cycle to " + localComponent + " in the focus cycle root " + localContainer);
/*     */         }
/*     */         else
/*     */         {
/* 173 */           return null;
/*     */         }
/* 175 */       } else if (localContainer.isFocusTraversalPolicyProvider()) {
/* 176 */         localComponent = paramInt == 0 ? localContainer.getFocusTraversalPolicy().getDefaultComponent(localContainer) : localContainer.getFocusTraversalPolicy().getLastComponent(localContainer);
/*     */ 
/* 180 */         if ((localComponent != null) && (log.isLoggable(500))) {
/* 181 */           log.fine("### Transfered focus to " + localComponent + " in the FTP provider " + localContainer);
/*     */         }
/*     */       }
/*     */     }
/* 185 */     return localComponent;
/*     */   }
/*     */ 
/*     */   public Component getComponentAfter(Container paramContainer, Component paramComponent)
/*     */   {
/* 211 */     if (log.isLoggable(500)) log.fine("### Searching in " + paramContainer + " for component after " + paramComponent);
/*     */ 
/* 213 */     if ((paramContainer == null) || (paramComponent == null)) {
/* 214 */       throw new IllegalArgumentException("aContainer and aComponent cannot be null");
/*     */     }
/* 216 */     if ((!paramContainer.isFocusTraversalPolicyProvider()) && (!paramContainer.isFocusCycleRoot())) {
/* 217 */       throw new IllegalArgumentException("aContainer should be focus cycle root or focus traversal policy provider");
/*     */     }
/* 219 */     if ((paramContainer.isFocusCycleRoot()) && (!paramComponent.isFocusCycleRoot(paramContainer))) {
/* 220 */       throw new IllegalArgumentException("aContainer is not a focus cycle root of aComponent");
/*     */     }
/*     */ 
/* 223 */     synchronized (paramContainer.getTreeLock())
/*     */     {
/* 225 */       if ((!paramContainer.isVisible()) || (!paramContainer.isDisplayable())) {
/* 226 */         return null;
/*     */       }
/*     */ 
/* 231 */       Component localComponent1 = getComponentDownCycle(paramComponent, 0);
/* 232 */       if (localComponent1 != null) {
/* 233 */         return localComponent1;
/*     */       }
/*     */ 
/* 237 */       Container localContainer = getTopmostProvider(paramContainer, paramComponent);
/* 238 */       if (localContainer != null) {
/* 239 */         if (log.isLoggable(500)) {
/* 240 */           log.fine("### Asking FTP " + localContainer + " for component after " + paramComponent);
/*     */         }
/*     */ 
/* 244 */         localObject1 = localContainer.getFocusTraversalPolicy();
/* 245 */         Component localComponent2 = ((FocusTraversalPolicy)localObject1).getComponentAfter(localContainer, paramComponent);
/*     */ 
/* 249 */         if (localComponent2 != null) {
/* 250 */           if (log.isLoggable(500)) log.fine("### FTP returned " + localComponent2);
/* 251 */           return localComponent2;
/*     */         }
/* 253 */         paramComponent = localContainer;
/*     */       }
/*     */ 
/* 256 */       Object localObject1 = getFocusTraversalCycle(paramContainer);
/*     */ 
/* 258 */       if (log.isLoggable(500)) log.fine("### Cycle is " + localObject1 + ", component is " + paramComponent);
/*     */ 
/* 260 */       int i = getComponentIndex((List)localObject1, paramComponent);
/*     */ 
/* 262 */       if (i < 0) {
/* 263 */         if (log.isLoggable(500)) {
/* 264 */           log.fine("### Didn't find component " + paramComponent + " in a cycle " + paramContainer);
/*     */         }
/* 266 */         return getFirstComponent(paramContainer);
/*     */       }
/*     */ 
/* 269 */       for (i++; i < ((List)localObject1).size(); i++) {
/* 270 */         localComponent1 = (Component)((List)localObject1).get(i);
/* 271 */         if (accept(localComponent1))
/* 272 */           return localComponent1;
/* 273 */         if ((localComponent1 = getComponentDownCycle(localComponent1, 0)) != null) {
/* 274 */           return localComponent1;
/*     */         }
/*     */       }
/*     */ 
/* 278 */       if (paramContainer.isFocusCycleRoot()) {
/* 279 */         this.cachedRoot = paramContainer;
/* 280 */         this.cachedCycle = ((List)localObject1);
/*     */ 
/* 282 */         localComponent1 = getFirstComponent(paramContainer);
/*     */ 
/* 284 */         this.cachedRoot = null;
/* 285 */         this.cachedCycle = null;
/*     */ 
/* 287 */         return localComponent1;
/*     */       }
/*     */     }
/* 290 */     return null;
/*     */   }
/*     */ 
/*     */   public Component getComponentBefore(Container paramContainer, Component paramComponent)
/*     */   {
/* 309 */     if ((paramContainer == null) || (paramComponent == null)) {
/* 310 */       throw new IllegalArgumentException("aContainer and aComponent cannot be null");
/*     */     }
/* 312 */     if ((!paramContainer.isFocusTraversalPolicyProvider()) && (!paramContainer.isFocusCycleRoot())) {
/* 313 */       throw new IllegalArgumentException("aContainer should be focus cycle root or focus traversal policy provider");
/*     */     }
/* 315 */     if ((paramContainer.isFocusCycleRoot()) && (!paramComponent.isFocusCycleRoot(paramContainer))) {
/* 316 */       throw new IllegalArgumentException("aContainer is not a focus cycle root of aComponent");
/*     */     }
/*     */ 
/* 319 */     synchronized (paramContainer.getTreeLock())
/*     */     {
/* 321 */       if ((!paramContainer.isVisible()) || (!paramContainer.isDisplayable())) {
/* 322 */         return null;
/*     */       }
/*     */ 
/* 326 */       Container localContainer = getTopmostProvider(paramContainer, paramComponent);
/* 327 */       if (localContainer != null) {
/* 328 */         if (log.isLoggable(500)) {
/* 329 */           log.fine("### Asking FTP " + localContainer + " for component after " + paramComponent);
/*     */         }
/*     */ 
/* 333 */         localObject1 = localContainer.getFocusTraversalPolicy();
/* 334 */         Component localComponent1 = ((FocusTraversalPolicy)localObject1).getComponentBefore(localContainer, paramComponent);
/*     */ 
/* 338 */         if (localComponent1 != null) {
/* 339 */           if (log.isLoggable(500)) log.fine("### FTP returned " + localComponent1);
/* 340 */           return localComponent1;
/*     */         }
/* 342 */         paramComponent = localContainer;
/*     */ 
/* 345 */         if (accept(paramComponent)) {
/* 346 */           return paramComponent;
/*     */         }
/*     */       }
/*     */ 
/* 350 */       Object localObject1 = getFocusTraversalCycle(paramContainer);
/*     */ 
/* 352 */       if (log.isLoggable(500)) log.fine("### Cycle is " + localObject1 + ", component is " + paramComponent);
/*     */ 
/* 354 */       int i = getComponentIndex((List)localObject1, paramComponent);
/*     */ 
/* 356 */       if (i < 0) {
/* 357 */         if (log.isLoggable(500)) {
/* 358 */           log.fine("### Didn't find component " + paramComponent + " in a cycle " + paramContainer);
/*     */         }
/* 360 */         return getLastComponent(paramContainer);
/*     */       }
/*     */ 
/* 363 */       Component localComponent2 = null;
/* 364 */       Component localComponent3 = null;
/*     */ 
/* 366 */       for (i--; i >= 0; i--) {
/* 367 */         localComponent2 = (Component)((List)localObject1).get(i);
/* 368 */         if ((localComponent2 != paramContainer) && ((localComponent3 = getComponentDownCycle(localComponent2, 1)) != null))
/* 369 */           return localComponent3;
/* 370 */         if (accept(localComponent2)) {
/* 371 */           return localComponent2;
/*     */         }
/*     */       }
/*     */ 
/* 375 */       if (paramContainer.isFocusCycleRoot()) {
/* 376 */         this.cachedRoot = paramContainer;
/* 377 */         this.cachedCycle = ((List)localObject1);
/*     */ 
/* 379 */         localComponent2 = getLastComponent(paramContainer);
/*     */ 
/* 381 */         this.cachedRoot = null;
/* 382 */         this.cachedCycle = null;
/*     */ 
/* 384 */         return localComponent2;
/*     */       }
/*     */     }
/* 387 */     return null;
/*     */   }
/*     */ 
/*     */   public Component getFirstComponent(Container paramContainer)
/*     */   {
/* 404 */     if (log.isLoggable(500)) log.fine("### Getting first component in " + paramContainer);
/* 405 */     if (paramContainer == null) {
/* 406 */       throw new IllegalArgumentException("aContainer cannot be null");
/*     */     }
/*     */ 
/* 410 */     synchronized (paramContainer.getTreeLock())
/*     */     {
/* 412 */       if ((!paramContainer.isVisible()) || (!paramContainer.isDisplayable()))
/* 413 */         return null;
/*     */       List localList;
/* 416 */       if (this.cachedRoot == paramContainer)
/* 417 */         localList = this.cachedCycle;
/*     */       else {
/* 419 */         localList = getFocusTraversalCycle(paramContainer);
/*     */       }
/*     */ 
/* 422 */       if (localList.size() == 0) {
/* 423 */         if (log.isLoggable(500)) log.fine("### Cycle is empty");
/* 424 */         return null;
/*     */       }
/* 426 */       if (log.isLoggable(500)) log.fine("### Cycle is " + localList);
/*     */ 
/* 428 */       for (Component localComponent : localList) {
/* 429 */         if (accept(localComponent))
/* 430 */           return localComponent;
/* 431 */         if ((localComponent != paramContainer) && ((localComponent = getComponentDownCycle(localComponent, 0)) != null))
/*     */         {
/* 434 */           return localComponent;
/*     */         }
/*     */       }
/*     */     }
/* 438 */     return null;
/*     */   }
/*     */ 
/*     */   public Component getLastComponent(Container paramContainer)
/*     */   {
/* 454 */     if (log.isLoggable(500)) log.fine("### Getting last component in " + paramContainer);
/*     */ 
/* 456 */     if (paramContainer == null) {
/* 457 */       throw new IllegalArgumentException("aContainer cannot be null");
/*     */     }
/*     */ 
/* 460 */     synchronized (paramContainer.getTreeLock())
/*     */     {
/* 462 */       if ((!paramContainer.isVisible()) || (!paramContainer.isDisplayable()))
/* 463 */         return null;
/*     */       List localList;
/* 466 */       if (this.cachedRoot == paramContainer)
/* 467 */         localList = this.cachedCycle;
/*     */       else {
/* 469 */         localList = getFocusTraversalCycle(paramContainer);
/*     */       }
/*     */ 
/* 472 */       if (localList.size() == 0) {
/* 473 */         if (log.isLoggable(500)) log.fine("### Cycle is empty");
/* 474 */         return null;
/*     */       }
/* 476 */       if (log.isLoggable(500)) log.fine("### Cycle is " + localList);
/*     */ 
/* 478 */       for (int i = localList.size() - 1; i >= 0; i--) {
/* 479 */         Component localComponent = (Component)localList.get(i);
/* 480 */         if (accept(localComponent))
/* 481 */           return localComponent;
/* 482 */         if (((localComponent instanceof Container)) && (localComponent != paramContainer)) {
/* 483 */           Container localContainer = (Container)localComponent;
/* 484 */           if (localContainer.isFocusTraversalPolicyProvider()) {
/* 485 */             return localContainer.getFocusTraversalPolicy().getLastComponent(localContainer);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 490 */     return null;
/*     */   }
/*     */ 
/*     */   public Component getDefaultComponent(Container paramContainer)
/*     */   {
/* 507 */     return getFirstComponent(paramContainer);
/*     */   }
/*     */ 
/*     */   public void setImplicitDownCycleTraversal(boolean paramBoolean)
/*     */   {
/* 526 */     this.implicitDownCycleTraversal = paramBoolean;
/*     */   }
/*     */ 
/*     */   public boolean getImplicitDownCycleTraversal()
/*     */   {
/* 543 */     return this.implicitDownCycleTraversal;
/*     */   }
/*     */ 
/*     */   protected boolean accept(Component paramComponent)
/*     */   {
/* 557 */     if (!paramComponent.canBeFocusOwner()) {
/* 558 */       return false;
/*     */     }
/*     */ 
/* 564 */     if (!(paramComponent instanceof Window)) {
/* 565 */       for (Container localContainer = paramComponent.getParent(); 
/* 566 */         localContainer != null; 
/* 567 */         localContainer = localContainer.getParent())
/*     */       {
/* 569 */         if ((!localContainer.isEnabled()) && (!localContainer.isLightweight())) {
/* 570 */           return false;
/*     */         }
/* 572 */         if ((localContainer instanceof Window))
/*     */         {
/*     */           break;
/*     */         }
/*     */       }
/*     */     }
/* 578 */     return true;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.ContainerOrderFocusTraversalPolicy
 * JD-Core Version:    0.6.2
 */