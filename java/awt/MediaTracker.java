/*     */ package java.awt;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public class MediaTracker
/*     */   implements Serializable
/*     */ {
/*     */   Component target;
/*     */   MediaEntry head;
/*     */   private static final long serialVersionUID = -483174189758638095L;
/*     */   public static final int LOADING = 1;
/*     */   public static final int ABORTED = 2;
/*     */   public static final int ERRORED = 4;
/*     */   public static final int COMPLETE = 8;
/*     */   static final int DONE = 14;
/*     */ 
/*     */   public MediaTracker(Component paramComponent)
/*     */   {
/* 200 */     this.target = paramComponent;
/*     */   }
/*     */ 
/*     */   public void addImage(Image paramImage, int paramInt)
/*     */   {
/* 211 */     addImage(paramImage, paramInt, -1, -1);
/*     */   }
/*     */ 
/*     */   public synchronized void addImage(Image paramImage, int paramInt1, int paramInt2, int paramInt3)
/*     */   {
/* 225 */     this.head = MediaEntry.insert(this.head, new ImageMediaEntry(this, paramImage, paramInt1, paramInt2, paramInt3));
/*     */   }
/*     */ 
/*     */   public boolean checkAll()
/*     */   {
/* 281 */     return checkAll(false, true);
/*     */   }
/*     */ 
/*     */   public boolean checkAll(boolean paramBoolean)
/*     */   {
/* 307 */     return checkAll(paramBoolean, true);
/*     */   }
/*     */ 
/*     */   private synchronized boolean checkAll(boolean paramBoolean1, boolean paramBoolean2) {
/* 311 */     MediaEntry localMediaEntry = this.head;
/* 312 */     boolean bool = true;
/* 313 */     while (localMediaEntry != null) {
/* 314 */       if ((localMediaEntry.getStatus(paramBoolean1, paramBoolean2) & 0xE) == 0) {
/* 315 */         bool = false;
/*     */       }
/* 317 */       localMediaEntry = localMediaEntry.next;
/*     */     }
/* 319 */     return bool;
/*     */   }
/*     */ 
/*     */   public synchronized boolean isErrorAny()
/*     */   {
/* 331 */     MediaEntry localMediaEntry = this.head;
/* 332 */     while (localMediaEntry != null) {
/* 333 */       if ((localMediaEntry.getStatus(false, true) & 0x4) != 0) {
/* 334 */         return true;
/*     */       }
/* 336 */       localMediaEntry = localMediaEntry.next;
/*     */     }
/* 338 */     return false;
/*     */   }
/*     */ 
/*     */   public synchronized Object[] getErrorsAny()
/*     */   {
/* 351 */     MediaEntry localMediaEntry = this.head;
/* 352 */     int i = 0;
/* 353 */     while (localMediaEntry != null) {
/* 354 */       if ((localMediaEntry.getStatus(false, true) & 0x4) != 0) {
/* 355 */         i++;
/*     */       }
/* 357 */       localMediaEntry = localMediaEntry.next;
/*     */     }
/* 359 */     if (i == 0) {
/* 360 */       return null;
/*     */     }
/* 362 */     Object[] arrayOfObject = new Object[i];
/* 363 */     localMediaEntry = this.head;
/* 364 */     i = 0;
/* 365 */     while (localMediaEntry != null) {
/* 366 */       if ((localMediaEntry.getStatus(false, false) & 0x4) != 0) {
/* 367 */         arrayOfObject[(i++)] = localMediaEntry.getMedia();
/*     */       }
/* 369 */       localMediaEntry = localMediaEntry.next;
/*     */     }
/* 371 */     return arrayOfObject;
/*     */   }
/*     */ 
/*     */   public void waitForAll()
/*     */     throws InterruptedException
/*     */   {
/* 391 */     waitForAll(0L);
/*     */   }
/*     */ 
/*     */   public synchronized boolean waitForAll(long paramLong)
/*     */     throws InterruptedException
/*     */   {
/* 418 */     long l1 = System.currentTimeMillis() + paramLong;
/* 419 */     boolean bool = true;
/*     */     while (true) {
/* 421 */       int i = statusAll(bool, bool);
/* 422 */       if ((i & 0x1) == 0) {
/* 423 */         return i == 8;
/*     */       }
/* 425 */       bool = false;
/*     */       long l2;
/* 427 */       if (paramLong == 0L) {
/* 428 */         l2 = 0L;
/*     */       } else {
/* 430 */         l2 = l1 - System.currentTimeMillis();
/* 431 */         if (l2 <= 0L) {
/* 432 */           return false;
/*     */         }
/*     */       }
/* 435 */       wait(l2);
/*     */     }
/*     */   }
/*     */ 
/*     */   public int statusAll(boolean paramBoolean)
/*     */   {
/* 463 */     return statusAll(paramBoolean, true);
/*     */   }
/*     */ 
/*     */   private synchronized int statusAll(boolean paramBoolean1, boolean paramBoolean2) {
/* 467 */     MediaEntry localMediaEntry = this.head;
/* 468 */     int i = 0;
/* 469 */     while (localMediaEntry != null) {
/* 470 */       i |= localMediaEntry.getStatus(paramBoolean1, paramBoolean2);
/* 471 */       localMediaEntry = localMediaEntry.next;
/*     */     }
/* 473 */     return i;
/*     */   }
/*     */ 
/*     */   public boolean checkID(int paramInt)
/*     */   {
/* 497 */     return checkID(paramInt, false, true);
/*     */   }
/*     */ 
/*     */   public boolean checkID(int paramInt, boolean paramBoolean)
/*     */   {
/* 524 */     return checkID(paramInt, paramBoolean, true);
/*     */   }
/*     */ 
/*     */   private synchronized boolean checkID(int paramInt, boolean paramBoolean1, boolean paramBoolean2)
/*     */   {
/* 529 */     MediaEntry localMediaEntry = this.head;
/* 530 */     boolean bool = true;
/* 531 */     while (localMediaEntry != null) {
/* 532 */       if ((localMediaEntry.getID() == paramInt) && ((localMediaEntry.getStatus(paramBoolean1, paramBoolean2) & 0xE) == 0))
/*     */       {
/* 535 */         bool = false;
/*     */       }
/* 537 */       localMediaEntry = localMediaEntry.next;
/*     */     }
/* 539 */     return bool;
/*     */   }
/*     */ 
/*     */   public synchronized boolean isErrorID(int paramInt)
/*     */   {
/* 553 */     MediaEntry localMediaEntry = this.head;
/* 554 */     while (localMediaEntry != null) {
/* 555 */       if ((localMediaEntry.getID() == paramInt) && ((localMediaEntry.getStatus(false, true) & 0x4) != 0))
/*     */       {
/* 558 */         return true;
/*     */       }
/* 560 */       localMediaEntry = localMediaEntry.next;
/*     */     }
/* 562 */     return false;
/*     */   }
/*     */ 
/*     */   public synchronized Object[] getErrorsID(int paramInt)
/*     */   {
/* 578 */     MediaEntry localMediaEntry = this.head;
/* 579 */     int i = 0;
/* 580 */     while (localMediaEntry != null) {
/* 581 */       if ((localMediaEntry.getID() == paramInt) && ((localMediaEntry.getStatus(false, true) & 0x4) != 0))
/*     */       {
/* 584 */         i++;
/*     */       }
/* 586 */       localMediaEntry = localMediaEntry.next;
/*     */     }
/* 588 */     if (i == 0) {
/* 589 */       return null;
/*     */     }
/* 591 */     Object[] arrayOfObject = new Object[i];
/* 592 */     localMediaEntry = this.head;
/* 593 */     i = 0;
/* 594 */     while (localMediaEntry != null) {
/* 595 */       if ((localMediaEntry.getID() == paramInt) && ((localMediaEntry.getStatus(false, false) & 0x4) != 0))
/*     */       {
/* 598 */         arrayOfObject[(i++)] = localMediaEntry.getMedia();
/*     */       }
/* 600 */       localMediaEntry = localMediaEntry.next;
/*     */     }
/* 602 */     return arrayOfObject;
/*     */   }
/*     */ 
/*     */   public void waitForID(int paramInt)
/*     */     throws InterruptedException
/*     */   {
/* 622 */     waitForID(paramInt, 0L);
/*     */   }
/*     */ 
/*     */   public synchronized boolean waitForID(int paramInt, long paramLong)
/*     */     throws InterruptedException
/*     */   {
/* 650 */     long l1 = System.currentTimeMillis() + paramLong;
/* 651 */     boolean bool = true;
/*     */     while (true) {
/* 653 */       int i = statusID(paramInt, bool, bool);
/* 654 */       if ((i & 0x1) == 0) {
/* 655 */         return i == 8;
/*     */       }
/* 657 */       bool = false;
/*     */       long l2;
/* 659 */       if (paramLong == 0L) {
/* 660 */         l2 = 0L;
/*     */       } else {
/* 662 */         l2 = l1 - System.currentTimeMillis();
/* 663 */         if (l2 <= 0L) {
/* 664 */           return false;
/*     */         }
/*     */       }
/* 667 */       wait(l2);
/*     */     }
/*     */   }
/*     */ 
/*     */   public int statusID(int paramInt, boolean paramBoolean)
/*     */   {
/* 697 */     return statusID(paramInt, paramBoolean, true);
/*     */   }
/*     */ 
/*     */   private synchronized int statusID(int paramInt, boolean paramBoolean1, boolean paramBoolean2) {
/* 701 */     MediaEntry localMediaEntry = this.head;
/* 702 */     int i = 0;
/* 703 */     while (localMediaEntry != null) {
/* 704 */       if (localMediaEntry.getID() == paramInt) {
/* 705 */         i |= localMediaEntry.getStatus(paramBoolean1, paramBoolean2);
/*     */       }
/* 707 */       localMediaEntry = localMediaEntry.next;
/*     */     }
/* 709 */     return i;
/*     */   }
/*     */ 
/*     */   public synchronized void removeImage(Image paramImage)
/*     */   {
/* 722 */     Object localObject1 = this.head;
/* 723 */     Object localObject2 = null;
/* 724 */     while (localObject1 != null) {
/* 725 */       MediaEntry localMediaEntry = ((MediaEntry)localObject1).next;
/* 726 */       if (((MediaEntry)localObject1).getMedia() == paramImage) {
/* 727 */         if (localObject2 == null)
/* 728 */           this.head = localMediaEntry;
/*     */         else {
/* 730 */           localObject2.next = localMediaEntry;
/*     */         }
/* 732 */         ((MediaEntry)localObject1).cancel();
/*     */       } else {
/* 734 */         localObject2 = localObject1;
/*     */       }
/* 736 */       localObject1 = localMediaEntry;
/*     */     }
/* 738 */     notifyAll();
/*     */   }
/*     */ 
/*     */   public synchronized void removeImage(Image paramImage, int paramInt)
/*     */   {
/* 753 */     Object localObject1 = this.head;
/* 754 */     Object localObject2 = null;
/* 755 */     while (localObject1 != null) {
/* 756 */       MediaEntry localMediaEntry = ((MediaEntry)localObject1).next;
/* 757 */       if ((((MediaEntry)localObject1).getID() == paramInt) && (((MediaEntry)localObject1).getMedia() == paramImage)) {
/* 758 */         if (localObject2 == null)
/* 759 */           this.head = localMediaEntry;
/*     */         else {
/* 761 */           localObject2.next = localMediaEntry;
/*     */         }
/* 763 */         ((MediaEntry)localObject1).cancel();
/*     */       } else {
/* 765 */         localObject2 = localObject1;
/*     */       }
/* 767 */       localObject1 = localMediaEntry;
/*     */     }
/* 769 */     notifyAll();
/*     */   }
/*     */ 
/*     */   public synchronized void removeImage(Image paramImage, int paramInt1, int paramInt2, int paramInt3)
/*     */   {
/* 786 */     Object localObject1 = this.head;
/* 787 */     Object localObject2 = null;
/* 788 */     while (localObject1 != null) {
/* 789 */       MediaEntry localMediaEntry = ((MediaEntry)localObject1).next;
/* 790 */       if ((((MediaEntry)localObject1).getID() == paramInt1) && ((localObject1 instanceof ImageMediaEntry)) && (((ImageMediaEntry)localObject1).matches(paramImage, paramInt2, paramInt3)))
/*     */       {
/* 793 */         if (localObject2 == null)
/* 794 */           this.head = localMediaEntry;
/*     */         else {
/* 796 */           localObject2.next = localMediaEntry;
/*     */         }
/* 798 */         ((MediaEntry)localObject1).cancel();
/*     */       } else {
/* 800 */         localObject2 = localObject1;
/*     */       }
/* 802 */       localObject1 = localMediaEntry;
/*     */     }
/* 804 */     notifyAll();
/*     */   }
/*     */ 
/*     */   synchronized void setDone() {
/* 808 */     notifyAll();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.MediaTracker
 * JD-Core Version:    0.6.2
 */