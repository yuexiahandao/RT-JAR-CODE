/*     */ package javax.swing;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.FocusTraversalPolicy;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import sun.security.action.GetPropertyAction;
/*     */ import sun.util.logging.PlatformLogger;
/*     */ 
/*     */ public class SortingFocusTraversalPolicy extends InternalFrameFocusTraversalPolicy
/*     */ {
/*     */   private Comparator<? super Component> comparator;
/*  70 */   private boolean implicitDownCycleTraversal = true;
/*     */ 
/*  72 */   private PlatformLogger log = PlatformLogger.getLogger("javax.swing.SortingFocusTraversalPolicy");
/*     */   private transient Container cachedRoot;
/*     */   private transient List<Component> cachedCycle;
/*  92 */   private static final SwingContainerOrderFocusTraversalPolicy fitnessTestPolicy = new SwingContainerOrderFocusTraversalPolicy();
/*     */ 
/*  94 */   private final int FORWARD_TRAVERSAL = 0;
/*  95 */   private final int BACKWARD_TRAVERSAL = 1;
/*     */ 
/* 106 */   private static final boolean legacySortingFTPEnabled = "true".equals(AccessController.doPrivileged(new GetPropertyAction("swing.legacySortingFTPEnabled", "true")));
/*     */ 
/* 108 */   private static final Method legacyMergeSortMethod = legacySortingFTPEnabled ? (Method)AccessController.doPrivileged(new PrivilegedAction()
/*     */   {
/*     */     public Method run() {
/*     */       try {
/* 112 */         Class localClass = Class.forName("java.util.Arrays");
/* 113 */         Method localMethod = localClass.getDeclaredMethod("legacyMergeSort", new Class[] { [Ljava.lang.Object.class, Comparator.class });
/* 114 */         localMethod.setAccessible(true);
/* 115 */         return localMethod;
/*     */       } catch (ClassNotFoundException|NoSuchMethodException localClassNotFoundException) {
/*     */       }
/* 118 */       return null;
/*     */     }
/*     */   }) : null;
/*     */ 
/*     */   protected SortingFocusTraversalPolicy()
/*     */   {
/*     */   }
/*     */ 
/*     */   public SortingFocusTraversalPolicy(Comparator<? super Component> paramComparator)
/*     */   {
/* 138 */     this.comparator = paramComparator;
/*     */   }
/*     */ 
/*     */   private List<Component> getFocusTraversalCycle(Container paramContainer) {
/* 142 */     ArrayList localArrayList = new ArrayList();
/* 143 */     enumerateAndSortCycle(paramContainer, localArrayList);
/* 144 */     return localArrayList;
/*     */   }
/*     */   private int getComponentIndex(List<Component> paramList, Component paramComponent) {
/*     */     int i;
/*     */     try {
/* 149 */       i = Collections.binarySearch(paramList, paramComponent, this.comparator);
/*     */     } catch (ClassCastException localClassCastException) {
/* 151 */       if (this.log.isLoggable(500)) {
/* 152 */         this.log.fine("### During the binary search for " + paramComponent + " the exception occured: ", localClassCastException);
/*     */       }
/* 154 */       return -1;
/*     */     }
/* 156 */     if (i < 0)
/*     */     {
/* 161 */       i = paramList.indexOf(paramComponent);
/*     */     }
/* 163 */     return i;
/*     */   }
/*     */ 
/*     */   private void enumerateAndSortCycle(Container paramContainer, List<Component> paramList) {
/* 167 */     if (paramContainer.isShowing()) {
/* 168 */       enumerateCycle(paramContainer, paramList);
/* 169 */       if ((!legacySortingFTPEnabled) || (!legacySort(paramList, this.comparator)))
/*     */       {
/* 172 */         Collections.sort(paramList, this.comparator);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private boolean legacySort(List<Component> paramList, Comparator<? super Component> paramComparator) {
/* 178 */     if (legacyMergeSortMethod == null) {
/* 179 */       return false;
/*     */     }
/* 181 */     Object[] arrayOfObject1 = paramList.toArray();
/*     */     try {
/* 183 */       legacyMergeSortMethod.invoke(null, new Object[] { arrayOfObject1, paramComparator });
/*     */     } catch (IllegalAccessException|InvocationTargetException localIllegalAccessException) {
/* 185 */       return false;
/*     */     }
/* 187 */     ListIterator localListIterator = paramList.listIterator();
/* 188 */     for (Object localObject : arrayOfObject1) {
/* 189 */       localListIterator.next();
/* 190 */       localListIterator.set((Component)localObject);
/*     */     }
/* 192 */     return true;
/*     */   }
/*     */ 
/*     */   private void enumerateCycle(Container paramContainer, List<Component> paramList) {
/* 196 */     if ((!paramContainer.isVisible()) || (!paramContainer.isDisplayable())) {
/* 197 */       return;
/*     */     }
/*     */ 
/* 200 */     paramList.add(paramContainer);
/*     */ 
/* 202 */     Component[] arrayOfComponent1 = paramContainer.getComponents();
/* 203 */     for (Component localComponent : arrayOfComponent1) {
/* 204 */       if ((localComponent instanceof Container)) {
/* 205 */         Container localContainer = (Container)localComponent;
/*     */ 
/* 207 */         if ((!localContainer.isFocusCycleRoot()) && (!localContainer.isFocusTraversalPolicyProvider()) && ((!(localContainer instanceof JComponent)) || (!((JComponent)localContainer).isManagingFocus())))
/*     */         {
/* 211 */           enumerateCycle(localContainer, paramList);
/* 212 */           continue;
/*     */         }
/*     */       }
/* 215 */       paramList.add(localComponent);
/*     */     }
/*     */   }
/*     */ 
/*     */   Container getTopmostProvider(Container paramContainer, Component paramComponent) {
/* 220 */     Container localContainer1 = paramComponent.getParent();
/* 221 */     Container localContainer2 = null;
/* 222 */     while ((localContainer1 != paramContainer) && (localContainer1 != null)) {
/* 223 */       if (localContainer1.isFocusTraversalPolicyProvider()) {
/* 224 */         localContainer2 = localContainer1;
/*     */       }
/* 226 */       localContainer1 = localContainer1.getParent();
/*     */     }
/* 228 */     if (localContainer1 == null) {
/* 229 */       return null;
/*     */     }
/* 231 */     return localContainer2;
/*     */   }
/*     */ 
/*     */   private Component getComponentDownCycle(Component paramComponent, int paramInt)
/*     */   {
/* 242 */     Component localComponent = null;
/*     */ 
/* 244 */     if ((paramComponent instanceof Container)) {
/* 245 */       Container localContainer = (Container)paramComponent;
/*     */ 
/* 247 */       if (localContainer.isFocusCycleRoot()) {
/* 248 */         if (getImplicitDownCycleTraversal()) {
/* 249 */           localComponent = localContainer.getFocusTraversalPolicy().getDefaultComponent(localContainer);
/*     */ 
/* 251 */           if ((localComponent != null) && (this.log.isLoggable(500)))
/* 252 */             this.log.fine("### Transfered focus down-cycle to " + localComponent + " in the focus cycle root " + localContainer);
/*     */         }
/*     */         else
/*     */         {
/* 256 */           return null;
/*     */         }
/* 258 */       } else if (localContainer.isFocusTraversalPolicyProvider()) {
/* 259 */         localComponent = paramInt == 0 ? localContainer.getFocusTraversalPolicy().getDefaultComponent(localContainer) : localContainer.getFocusTraversalPolicy().getLastComponent(localContainer);
/*     */ 
/* 263 */         if ((localComponent != null) && (this.log.isLoggable(500))) {
/* 264 */           this.log.fine("### Transfered focus to " + localComponent + " in the FTP provider " + localContainer);
/*     */         }
/*     */       }
/*     */     }
/* 268 */     return localComponent;
/*     */   }
/*     */ 
/*     */   public Component getComponentAfter(Container paramContainer, Component paramComponent)
/*     */   {
/* 294 */     if (this.log.isLoggable(500)) {
/* 295 */       this.log.fine("### Searching in " + paramContainer + " for component after " + paramComponent);
/*     */     }
/*     */ 
/* 298 */     if ((paramContainer == null) || (paramComponent == null)) {
/* 299 */       throw new IllegalArgumentException("aContainer and aComponent cannot be null");
/*     */     }
/* 301 */     if ((!paramContainer.isFocusTraversalPolicyProvider()) && (!paramContainer.isFocusCycleRoot())) {
/* 302 */       throw new IllegalArgumentException("aContainer should be focus cycle root or focus traversal policy provider");
/*     */     }
/* 304 */     if ((paramContainer.isFocusCycleRoot()) && (!paramComponent.isFocusCycleRoot(paramContainer))) {
/* 305 */       throw new IllegalArgumentException("aContainer is not a focus cycle root of aComponent");
/*     */     }
/*     */ 
/* 310 */     Component localComponent1 = getComponentDownCycle(paramComponent, 0);
/* 311 */     if (localComponent1 != null) {
/* 312 */       return localComponent1;
/*     */     }
/*     */ 
/* 316 */     Container localContainer = getTopmostProvider(paramContainer, paramComponent);
/* 317 */     if (localContainer != null) {
/* 318 */       if (this.log.isLoggable(500)) {
/* 319 */         this.log.fine("### Asking FTP " + localContainer + " for component after " + paramComponent);
/*     */       }
/*     */ 
/* 323 */       localObject = localContainer.getFocusTraversalPolicy();
/* 324 */       Component localComponent2 = ((FocusTraversalPolicy)localObject).getComponentAfter(localContainer, paramComponent);
/*     */ 
/* 328 */       if (localComponent2 != null) {
/* 329 */         if (this.log.isLoggable(500)) this.log.fine("### FTP returned " + localComponent2);
/* 330 */         return localComponent2;
/*     */       }
/* 332 */       paramComponent = localContainer;
/*     */     }
/*     */ 
/* 335 */     Object localObject = getFocusTraversalCycle(paramContainer);
/*     */ 
/* 337 */     if (this.log.isLoggable(500)) this.log.fine("### Cycle is " + localObject + ", component is " + paramComponent);
/*     */ 
/* 339 */     int i = getComponentIndex((List)localObject, paramComponent);
/*     */ 
/* 341 */     if (i < 0) {
/* 342 */       if (this.log.isLoggable(500)) {
/* 343 */         this.log.fine("### Didn't find component " + paramComponent + " in a cycle " + paramContainer);
/*     */       }
/* 345 */       return getFirstComponent(paramContainer);
/*     */     }
/*     */ 
/* 348 */     for (i++; i < ((List)localObject).size(); i++) {
/* 349 */       localComponent1 = (Component)((List)localObject).get(i);
/* 350 */       if (accept(localComponent1))
/* 351 */         return localComponent1;
/* 352 */       if ((localComponent1 = getComponentDownCycle(localComponent1, 0)) != null) {
/* 353 */         return localComponent1;
/*     */       }
/*     */     }
/*     */ 
/* 357 */     if (paramContainer.isFocusCycleRoot()) {
/* 358 */       this.cachedRoot = paramContainer;
/* 359 */       this.cachedCycle = ((List)localObject);
/*     */ 
/* 361 */       localComponent1 = getFirstComponent(paramContainer);
/*     */ 
/* 363 */       this.cachedRoot = null;
/* 364 */       this.cachedCycle = null;
/*     */ 
/* 366 */       return localComponent1;
/*     */     }
/* 368 */     return null;
/*     */   }
/*     */ 
/*     */   public Component getComponentBefore(Container paramContainer, Component paramComponent)
/*     */   {
/* 394 */     if ((paramContainer == null) || (paramComponent == null)) {
/* 395 */       throw new IllegalArgumentException("aContainer and aComponent cannot be null");
/*     */     }
/* 397 */     if ((!paramContainer.isFocusTraversalPolicyProvider()) && (!paramContainer.isFocusCycleRoot())) {
/* 398 */       throw new IllegalArgumentException("aContainer should be focus cycle root or focus traversal policy provider");
/*     */     }
/* 400 */     if ((paramContainer.isFocusCycleRoot()) && (!paramComponent.isFocusCycleRoot(paramContainer))) {
/* 401 */       throw new IllegalArgumentException("aContainer is not a focus cycle root of aComponent");
/*     */     }
/*     */ 
/* 405 */     Container localContainer = getTopmostProvider(paramContainer, paramComponent);
/* 406 */     if (localContainer != null) {
/* 407 */       if (this.log.isLoggable(500)) {
/* 408 */         this.log.fine("### Asking FTP " + localContainer + " for component after " + paramComponent);
/*     */       }
/*     */ 
/* 412 */       localObject = localContainer.getFocusTraversalPolicy();
/* 413 */       Component localComponent1 = ((FocusTraversalPolicy)localObject).getComponentBefore(localContainer, paramComponent);
/*     */ 
/* 417 */       if (localComponent1 != null) {
/* 418 */         if (this.log.isLoggable(500)) this.log.fine("### FTP returned " + localComponent1);
/* 419 */         return localComponent1;
/*     */       }
/* 421 */       paramComponent = localContainer;
/*     */ 
/* 424 */       if (accept(paramComponent)) {
/* 425 */         return paramComponent;
/*     */       }
/*     */     }
/*     */ 
/* 429 */     Object localObject = getFocusTraversalCycle(paramContainer);
/*     */ 
/* 431 */     if (this.log.isLoggable(500)) this.log.fine("### Cycle is " + localObject + ", component is " + paramComponent);
/*     */ 
/* 433 */     int i = getComponentIndex((List)localObject, paramComponent);
/*     */ 
/* 435 */     if (i < 0) {
/* 436 */       if (this.log.isLoggable(500)) {
/* 437 */         this.log.fine("### Didn't find component " + paramComponent + " in a cycle " + paramContainer);
/*     */       }
/* 439 */       return getLastComponent(paramContainer);
/*     */     }
/*     */     Component localComponent2;
/* 445 */     for (i--; i >= 0; i--) {
/* 446 */       localComponent2 = (Component)((List)localObject).get(i);
/*     */       Component localComponent3;
/* 447 */       if ((localComponent2 != paramContainer) && ((localComponent3 = getComponentDownCycle(localComponent2, 1)) != null))
/* 448 */         return localComponent3;
/* 449 */       if (accept(localComponent2)) {
/* 450 */         return localComponent2;
/*     */       }
/*     */     }
/*     */ 
/* 454 */     if (paramContainer.isFocusCycleRoot()) {
/* 455 */       this.cachedRoot = paramContainer;
/* 456 */       this.cachedCycle = ((List)localObject);
/*     */ 
/* 458 */       localComponent2 = getLastComponent(paramContainer);
/*     */ 
/* 460 */       this.cachedRoot = null;
/* 461 */       this.cachedCycle = null;
/*     */ 
/* 463 */       return localComponent2;
/*     */     }
/* 465 */     return null;
/*     */   }
/*     */ 
/*     */   public Component getFirstComponent(Container paramContainer)
/*     */   {
/* 482 */     if (this.log.isLoggable(500)) this.log.fine("### Getting first component in " + paramContainer);
/* 483 */     if (paramContainer == null)
/* 484 */       throw new IllegalArgumentException("aContainer cannot be null");
/*     */     List localList;
/* 487 */     if (this.cachedRoot == paramContainer)
/* 488 */       localList = this.cachedCycle;
/*     */     else {
/* 490 */       localList = getFocusTraversalCycle(paramContainer);
/*     */     }
/*     */ 
/* 493 */     if (localList.size() == 0) {
/* 494 */       if (this.log.isLoggable(500)) this.log.fine("### Cycle is empty");
/* 495 */       return null;
/*     */     }
/* 497 */     if (this.log.isLoggable(500)) this.log.fine("### Cycle is " + localList);
/*     */ 
/* 499 */     for (Component localComponent : localList) {
/* 500 */       if (accept(localComponent))
/* 501 */         return localComponent;
/* 502 */       if ((localComponent != paramContainer) && ((localComponent = getComponentDownCycle(localComponent, 0)) != null))
/*     */       {
/* 505 */         return localComponent;
/*     */       }
/*     */     }
/* 508 */     return null;
/*     */   }
/*     */ 
/*     */   public Component getLastComponent(Container paramContainer)
/*     */   {
/* 524 */     if (this.log.isLoggable(500)) this.log.fine("### Getting last component in " + paramContainer);
/*     */ 
/* 526 */     if (paramContainer == null)
/* 527 */       throw new IllegalArgumentException("aContainer cannot be null");
/*     */     List localList;
/* 530 */     if (this.cachedRoot == paramContainer)
/* 531 */       localList = this.cachedCycle;
/*     */     else {
/* 533 */       localList = getFocusTraversalCycle(paramContainer);
/*     */     }
/*     */ 
/* 536 */     if (localList.size() == 0) {
/* 537 */       if (this.log.isLoggable(500)) this.log.fine("### Cycle is empty");
/* 538 */       return null;
/*     */     }
/* 540 */     if (this.log.isLoggable(500)) this.log.fine("### Cycle is " + localList);
/*     */ 
/* 542 */     for (int i = localList.size() - 1; i >= 0; i--) {
/* 543 */       Component localComponent = (Component)localList.get(i);
/* 544 */       if (accept(localComponent))
/* 545 */         return localComponent;
/* 546 */       if (((localComponent instanceof Container)) && (localComponent != paramContainer)) {
/* 547 */         Container localContainer = (Container)localComponent;
/* 548 */         if (localContainer.isFocusTraversalPolicyProvider()) {
/* 549 */           return localContainer.getFocusTraversalPolicy().getLastComponent(localContainer);
/*     */         }
/*     */       }
/*     */     }
/* 553 */     return null;
/*     */   }
/*     */ 
/*     */   public Component getDefaultComponent(Container paramContainer)
/*     */   {
/* 570 */     return getFirstComponent(paramContainer);
/*     */   }
/*     */ 
/*     */   public void setImplicitDownCycleTraversal(boolean paramBoolean)
/*     */   {
/* 588 */     this.implicitDownCycleTraversal = paramBoolean;
/*     */   }
/*     */ 
/*     */   public boolean getImplicitDownCycleTraversal()
/*     */   {
/* 605 */     return this.implicitDownCycleTraversal;
/*     */   }
/*     */ 
/*     */   protected void setComparator(Comparator<? super Component> paramComparator)
/*     */   {
/* 615 */     this.comparator = paramComparator;
/*     */   }
/*     */ 
/*     */   protected Comparator<? super Component> getComparator()
/*     */   {
/* 625 */     return this.comparator;
/*     */   }
/*     */ 
/*     */   protected boolean accept(Component paramComponent)
/*     */   {
/* 639 */     return fitnessTestPolicy.accept(paramComponent);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.SortingFocusTraversalPolicy
 * JD-Core Version:    0.6.2
 */