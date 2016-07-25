/*     */ package java.awt;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectInputStream.GetField;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.ObjectOutputStream.PutField;
/*     */ import java.io.ObjectStreamField;
/*     */ import java.io.Serializable;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class CardLayout
/*     */   implements LayoutManager2, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -4328196481005934313L;
/*  69 */   Vector vector = new Vector();
/*     */ 
/*  87 */   int currentCard = 0;
/*     */   int hgap;
/*     */   int vgap;
/* 118 */   private static final ObjectStreamField[] serialPersistentFields = { new ObjectStreamField("tab", Hashtable.class), new ObjectStreamField("hgap", Integer.TYPE), new ObjectStreamField("vgap", Integer.TYPE), new ObjectStreamField("vector", Vector.class), new ObjectStreamField("currentCard", Integer.TYPE) };
/*     */ 
/*     */   public CardLayout()
/*     */   {
/* 130 */     this(0, 0);
/*     */   }
/*     */ 
/*     */   public CardLayout(int paramInt1, int paramInt2)
/*     */   {
/* 142 */     this.hgap = paramInt1;
/* 143 */     this.vgap = paramInt2;
/*     */   }
/*     */ 
/*     */   public int getHgap()
/*     */   {
/* 154 */     return this.hgap;
/*     */   }
/*     */ 
/*     */   public void setHgap(int paramInt)
/*     */   {
/* 165 */     this.hgap = paramInt;
/*     */   }
/*     */ 
/*     */   public int getVgap()
/*     */   {
/* 175 */     return this.vgap;
/*     */   }
/*     */ 
/*     */   public void setVgap(int paramInt)
/*     */   {
/* 186 */     this.vgap = paramInt;
/*     */   }
/*     */ 
/*     */   public void addLayoutComponent(Component paramComponent, Object paramObject)
/*     */   {
/* 203 */     synchronized (paramComponent.getTreeLock()) {
/* 204 */       if (paramObject == null) {
/* 205 */         paramObject = "";
/*     */       }
/* 207 */       if ((paramObject instanceof String))
/* 208 */         addLayoutComponent((String)paramObject, paramComponent);
/*     */       else
/* 210 */         throw new IllegalArgumentException("cannot add to layout: constraint must be a string");
/*     */     }
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public void addLayoutComponent(String paramString, Component paramComponent)
/*     */   {
/* 221 */     synchronized (paramComponent.getTreeLock()) {
/* 222 */       if (!this.vector.isEmpty()) {
/* 223 */         paramComponent.setVisible(false);
/*     */       }
/* 225 */       for (int i = 0; i < this.vector.size(); i++) {
/* 226 */         if (((Card)this.vector.get(i)).name.equals(paramString)) {
/* 227 */           ((Card)this.vector.get(i)).comp = paramComponent;
/* 228 */           return;
/*     */         }
/*     */       }
/* 231 */       this.vector.add(new Card(paramString, paramComponent));
/*     */     }
/*     */   }
/*     */ 
/*     */   public void removeLayoutComponent(Component paramComponent)
/*     */   {
/* 243 */     synchronized (paramComponent.getTreeLock()) {
/* 244 */       for (int i = 0; i < this.vector.size(); i++)
/* 245 */         if (((Card)this.vector.get(i)).comp == paramComponent)
/*     */         {
/* 247 */           if ((paramComponent.isVisible()) && (paramComponent.getParent() != null)) {
/* 248 */             next(paramComponent.getParent());
/*     */           }
/*     */ 
/* 251 */           this.vector.remove(i);
/*     */ 
/* 254 */           if (this.currentCard <= i) break;
/* 255 */           this.currentCard -= 1; break;
/*     */         }
/*     */     }
/*     */   }
/*     */ 
/*     */   public Dimension preferredLayoutSize(Container paramContainer)
/*     */   {
/* 273 */     synchronized (paramContainer.getTreeLock()) {
/* 274 */       Insets localInsets = paramContainer.getInsets();
/* 275 */       int i = paramContainer.getComponentCount();
/* 276 */       int j = 0;
/* 277 */       int k = 0;
/*     */ 
/* 279 */       for (int m = 0; m < i; m++) {
/* 280 */         Component localComponent = paramContainer.getComponent(m);
/* 281 */         Dimension localDimension = localComponent.getPreferredSize();
/* 282 */         if (localDimension.width > j) {
/* 283 */           j = localDimension.width;
/*     */         }
/* 285 */         if (localDimension.height > k) {
/* 286 */           k = localDimension.height;
/*     */         }
/*     */       }
/* 289 */       return new Dimension(localInsets.left + localInsets.right + j + this.hgap * 2, localInsets.top + localInsets.bottom + k + this.vgap * 2);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Dimension minimumLayoutSize(Container paramContainer)
/*     */   {
/* 303 */     synchronized (paramContainer.getTreeLock()) {
/* 304 */       Insets localInsets = paramContainer.getInsets();
/* 305 */       int i = paramContainer.getComponentCount();
/* 306 */       int j = 0;
/* 307 */       int k = 0;
/*     */ 
/* 309 */       for (int m = 0; m < i; m++) {
/* 310 */         Component localComponent = paramContainer.getComponent(m);
/* 311 */         Dimension localDimension = localComponent.getMinimumSize();
/* 312 */         if (localDimension.width > j) {
/* 313 */           j = localDimension.width;
/*     */         }
/* 315 */         if (localDimension.height > k) {
/* 316 */           k = localDimension.height;
/*     */         }
/*     */       }
/* 319 */       return new Dimension(localInsets.left + localInsets.right + j + this.hgap * 2, localInsets.top + localInsets.bottom + k + this.vgap * 2);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Dimension maximumLayoutSize(Container paramContainer)
/*     */   {
/* 333 */     return new Dimension(2147483647, 2147483647);
/*     */   }
/*     */ 
/*     */   public float getLayoutAlignmentX(Container paramContainer)
/*     */   {
/* 344 */     return 0.5F;
/*     */   }
/*     */ 
/*     */   public float getLayoutAlignmentY(Container paramContainer)
/*     */   {
/* 355 */     return 0.5F;
/*     */   }
/*     */ 
/*     */   public void invalidateLayout(Container paramContainer)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void layoutContainer(Container paramContainer)
/*     */   {
/* 376 */     synchronized (paramContainer.getTreeLock()) {
/* 377 */       Insets localInsets = paramContainer.getInsets();
/* 378 */       int i = paramContainer.getComponentCount();
/* 379 */       Component localComponent = null;
/* 380 */       int j = 0;
/*     */ 
/* 382 */       for (int k = 0; k < i; k++) {
/* 383 */         localComponent = paramContainer.getComponent(k);
/* 384 */         localComponent.setBounds(this.hgap + localInsets.left, this.vgap + localInsets.top, paramContainer.width - (this.hgap * 2 + localInsets.left + localInsets.right), paramContainer.height - (this.vgap * 2 + localInsets.top + localInsets.bottom));
/*     */ 
/* 387 */         if (localComponent.isVisible()) {
/* 388 */           j = 1;
/*     */         }
/*     */       }
/*     */ 
/* 392 */       if ((j == 0) && (i > 0))
/* 393 */         paramContainer.getComponent(0).setVisible(true);
/*     */     }
/*     */   }
/*     */ 
/*     */   void checkLayout(Container paramContainer)
/*     */   {
/* 403 */     if (paramContainer.getLayout() != this)
/* 404 */       throw new IllegalArgumentException("wrong parent for CardLayout");
/*     */   }
/*     */ 
/*     */   public void first(Container paramContainer)
/*     */   {
/* 414 */     synchronized (paramContainer.getTreeLock()) {
/* 415 */       checkLayout(paramContainer);
/* 416 */       int i = paramContainer.getComponentCount();
/* 417 */       for (int j = 0; j < i; j++) {
/* 418 */         Component localComponent = paramContainer.getComponent(j);
/* 419 */         if (localComponent.isVisible()) {
/* 420 */           localComponent.setVisible(false);
/* 421 */           break;
/*     */         }
/*     */       }
/* 424 */       if (i > 0) {
/* 425 */         this.currentCard = 0;
/* 426 */         paramContainer.getComponent(0).setVisible(true);
/* 427 */         paramContainer.validate();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void next(Container paramContainer)
/*     */   {
/* 440 */     synchronized (paramContainer.getTreeLock()) {
/* 441 */       checkLayout(paramContainer);
/* 442 */       int i = paramContainer.getComponentCount();
/* 443 */       for (int j = 0; j < i; j++) {
/* 444 */         Component localComponent = paramContainer.getComponent(j);
/* 445 */         if (localComponent.isVisible()) {
/* 446 */           localComponent.setVisible(false);
/* 447 */           this.currentCard = ((j + 1) % i);
/* 448 */           localComponent = paramContainer.getComponent(this.currentCard);
/* 449 */           localComponent.setVisible(true);
/* 450 */           paramContainer.validate();
/* 451 */           return;
/*     */         }
/*     */       }
/* 454 */       showDefaultComponent(paramContainer);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void previous(Container paramContainer)
/*     */   {
/* 466 */     synchronized (paramContainer.getTreeLock()) {
/* 467 */       checkLayout(paramContainer);
/* 468 */       int i = paramContainer.getComponentCount();
/* 469 */       for (int j = 0; j < i; j++) {
/* 470 */         Component localComponent = paramContainer.getComponent(j);
/* 471 */         if (localComponent.isVisible()) {
/* 472 */           localComponent.setVisible(false);
/* 473 */           this.currentCard = (j > 0 ? j - 1 : i - 1);
/* 474 */           localComponent = paramContainer.getComponent(this.currentCard);
/* 475 */           localComponent.setVisible(true);
/* 476 */           paramContainer.validate();
/* 477 */           return;
/*     */         }
/*     */       }
/* 480 */       showDefaultComponent(paramContainer);
/*     */     }
/*     */   }
/*     */ 
/*     */   void showDefaultComponent(Container paramContainer) {
/* 485 */     if (paramContainer.getComponentCount() > 0) {
/* 486 */       this.currentCard = 0;
/* 487 */       paramContainer.getComponent(0).setVisible(true);
/* 488 */       paramContainer.validate();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void last(Container paramContainer)
/*     */   {
/* 498 */     synchronized (paramContainer.getTreeLock()) {
/* 499 */       checkLayout(paramContainer);
/* 500 */       int i = paramContainer.getComponentCount();
/* 501 */       for (int j = 0; j < i; j++) {
/* 502 */         Component localComponent = paramContainer.getComponent(j);
/* 503 */         if (localComponent.isVisible()) {
/* 504 */           localComponent.setVisible(false);
/* 505 */           break;
/*     */         }
/*     */       }
/* 508 */       if (i > 0) {
/* 509 */         this.currentCard = (i - 1);
/* 510 */         paramContainer.getComponent(this.currentCard).setVisible(true);
/* 511 */         paramContainer.validate();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void show(Container paramContainer, String paramString)
/*     */   {
/* 525 */     synchronized (paramContainer.getTreeLock()) {
/* 526 */       checkLayout(paramContainer);
/* 527 */       Component localComponent = null;
/* 528 */       int i = this.vector.size();
/*     */       Object localObject1;
/* 529 */       for (int j = 0; j < i; j++) {
/* 530 */         localObject1 = (Card)this.vector.get(j);
/* 531 */         if (((Card)localObject1).name.equals(paramString)) {
/* 532 */           localComponent = ((Card)localObject1).comp;
/* 533 */           this.currentCard = j;
/* 534 */           break;
/*     */         }
/*     */       }
/* 537 */       if ((localComponent != null) && (!localComponent.isVisible())) {
/* 538 */         i = paramContainer.getComponentCount();
/* 539 */         for (j = 0; j < i; j++) {
/* 540 */           localObject1 = paramContainer.getComponent(j);
/* 541 */           if (((Component)localObject1).isVisible()) {
/* 542 */             ((Component)localObject1).setVisible(false);
/* 543 */             break;
/*     */           }
/*     */         }
/* 546 */         localComponent.setVisible(true);
/* 547 */         paramContainer.validate();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 557 */     return getClass().getName() + "[hgap=" + this.hgap + ",vgap=" + this.vgap + "]";
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream)
/*     */     throws ClassNotFoundException, IOException
/*     */   {
/* 566 */     ObjectInputStream.GetField localGetField = paramObjectInputStream.readFields();
/*     */ 
/* 568 */     this.hgap = localGetField.get("hgap", 0);
/* 569 */     this.vgap = localGetField.get("vgap", 0);
/*     */     Hashtable localHashtable;
/*     */     Enumeration localEnumeration;
/* 571 */     if (localGetField.defaulted("vector"))
/*     */     {
/* 573 */       localHashtable = (Hashtable)localGetField.get("tab", null);
/* 574 */       this.vector = new Vector();
/* 575 */       if ((localHashtable != null) && (!localHashtable.isEmpty()))
/* 576 */         for (localEnumeration = localHashtable.keys(); localEnumeration.hasMoreElements(); ) {
/* 577 */           String str = (String)localEnumeration.nextElement();
/* 578 */           Component localComponent = (Component)localHashtable.get(str);
/* 579 */           this.vector.add(new Card(str, localComponent));
/* 580 */           if (localComponent.isVisible())
/* 581 */             this.currentCard = (this.vector.size() - 1);
/*     */         }
/*     */     }
/*     */     else
/*     */     {
/* 586 */       this.vector = ((Vector)localGetField.get("vector", null));
/* 587 */       this.currentCard = localGetField.get("currentCard", 0);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*     */     throws IOException
/*     */   {
/* 597 */     Hashtable localHashtable = new Hashtable();
/* 598 */     int i = this.vector.size();
/* 599 */     for (int j = 0; j < i; j++) {
/* 600 */       Card localCard = (Card)this.vector.get(j);
/* 601 */       localHashtable.put(localCard.name, localCard.comp);
/*     */     }
/*     */ 
/* 604 */     ObjectOutputStream.PutField localPutField = paramObjectOutputStream.putFields();
/* 605 */     localPutField.put("hgap", this.hgap);
/* 606 */     localPutField.put("vgap", this.vgap);
/* 607 */     localPutField.put("vector", this.vector);
/* 608 */     localPutField.put("currentCard", this.currentCard);
/* 609 */     localPutField.put("tab", localHashtable);
/* 610 */     paramObjectOutputStream.writeFields();
/*     */   }
/*     */ 
/*     */   class Card
/*     */     implements Serializable
/*     */   {
/*     */     static final long serialVersionUID = 6640330810709497518L;
/*     */     public String name;
/*     */     public Component comp;
/*     */ 
/*     */     public Card(String paramComponent, Component arg3)
/*     */     {
/*  79 */       this.name = paramComponent;
/*     */       Object localObject;
/*  80 */       this.comp = localObject;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.CardLayout
 * JD-Core Version:    0.6.2
 */