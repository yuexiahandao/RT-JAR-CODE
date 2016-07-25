/*     */ package java.awt;
/*     */ 
/*     */ import java.awt.event.ItemEvent;
/*     */ import java.awt.event.ItemListener;
/*     */ import java.awt.peer.ChoicePeer;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.util.EventListener;
/*     */ import java.util.Vector;
/*     */ import javax.accessibility.Accessible;
/*     */ import javax.accessibility.AccessibleAction;
/*     */ import javax.accessibility.AccessibleContext;
/*     */ import javax.accessibility.AccessibleRole;
/*     */ 
/*     */ public class Choice extends Component
/*     */   implements ItemSelectable, Accessible
/*     */ {
/*     */   Vector pItems;
/*  93 */   int selectedIndex = -1;
/*     */   transient ItemListener itemListener;
/*     */   private static final String base = "choice";
/*  98 */   private static int nameCounter = 0;
/*     */   private static final long serialVersionUID = -4075310674757313071L;
/* 646 */   private int choiceSerializedDataVersion = 1;
/*     */ 
/*     */   public Choice()
/*     */     throws HeadlessException
/*     */   {
/* 118 */     GraphicsEnvironment.checkHeadless();
/* 119 */     this.pItems = new Vector();
/*     */   }
/*     */ 
/*     */   String constructComponentName()
/*     */   {
/* 127 */     synchronized (Choice.class) {
/* 128 */       return "choice" + nameCounter++;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void addNotify()
/*     */   {
/* 140 */     synchronized (getTreeLock()) {
/* 141 */       if (this.peer == null)
/* 142 */         this.peer = getToolkit().createChoice(this);
/* 143 */       super.addNotify();
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getItemCount()
/*     */   {
/* 154 */     return countItems();
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public int countItems()
/*     */   {
/* 163 */     return this.pItems.size();
/*     */   }
/*     */ 
/*     */   public String getItem(int paramInt)
/*     */   {
/* 173 */     return getItemImpl(paramInt);
/*     */   }
/*     */ 
/*     */   final String getItemImpl(int paramInt)
/*     */   {
/* 181 */     return (String)this.pItems.elementAt(paramInt);
/*     */   }
/*     */ 
/*     */   public void add(String paramString)
/*     */   {
/* 192 */     addItem(paramString);
/*     */   }
/*     */ 
/*     */   public void addItem(String paramString)
/*     */   {
/* 205 */     synchronized (this) {
/* 206 */       insertNoInvalidate(paramString, this.pItems.size());
/*     */     }
/*     */ 
/* 210 */     invalidateIfValid();
/*     */   }
/*     */ 
/*     */   private void insertNoInvalidate(String paramString, int paramInt)
/*     */   {
/* 224 */     if (paramString == null) {
/* 225 */       throw new NullPointerException("cannot add null item to Choice");
/*     */     }
/*     */ 
/* 228 */     this.pItems.insertElementAt(paramString, paramInt);
/* 229 */     ChoicePeer localChoicePeer = (ChoicePeer)this.peer;
/* 230 */     if (localChoicePeer != null) {
/* 231 */       localChoicePeer.add(paramString, paramInt);
/*     */     }
/*     */ 
/* 234 */     if ((this.selectedIndex < 0) || (this.selectedIndex >= paramInt))
/* 235 */       select(0);
/*     */   }
/*     */ 
/*     */   public void insert(String paramString, int paramInt)
/*     */   {
/* 259 */     synchronized (this) {
/* 260 */       if (paramInt < 0) {
/* 261 */         throw new IllegalArgumentException("index less than zero.");
/*     */       }
/*     */ 
/* 264 */       paramInt = Math.min(paramInt, this.pItems.size());
/*     */ 
/* 266 */       insertNoInvalidate(paramString, paramInt);
/*     */     }
/*     */ 
/* 270 */     invalidateIfValid();
/*     */   }
/*     */ 
/*     */   public void remove(String paramString)
/*     */   {
/* 287 */     synchronized (this) {
/* 288 */       int i = this.pItems.indexOf(paramString);
/* 289 */       if (i < 0) {
/* 290 */         throw new IllegalArgumentException("item " + paramString + " not found in choice");
/*     */       }
/*     */ 
/* 293 */       removeNoInvalidate(i);
/*     */     }
/*     */ 
/* 298 */     invalidateIfValid();
/*     */   }
/*     */ 
/*     */   public void remove(int paramInt)
/*     */   {
/* 315 */     synchronized (this) {
/* 316 */       removeNoInvalidate(paramInt);
/*     */     }
/*     */ 
/* 320 */     invalidateIfValid();
/*     */   }
/*     */ 
/*     */   private void removeNoInvalidate(int paramInt)
/*     */   {
/* 331 */     this.pItems.removeElementAt(paramInt);
/* 332 */     ChoicePeer localChoicePeer = (ChoicePeer)this.peer;
/* 333 */     if (localChoicePeer != null) {
/* 334 */       localChoicePeer.remove(paramInt);
/*     */     }
/*     */ 
/* 337 */     if (this.pItems.size() == 0)
/* 338 */       this.selectedIndex = -1;
/* 339 */     else if (this.selectedIndex == paramInt)
/* 340 */       select(0);
/* 341 */     else if (this.selectedIndex > paramInt)
/* 342 */       select(this.selectedIndex - 1);
/*     */   }
/*     */ 
/*     */   public void removeAll()
/*     */   {
/* 353 */     synchronized (this) {
/* 354 */       if (this.peer != null) {
/* 355 */         ((ChoicePeer)this.peer).removeAll();
/*     */       }
/* 357 */       this.pItems.removeAllElements();
/* 358 */       this.selectedIndex = -1;
/*     */     }
/*     */ 
/* 362 */     invalidateIfValid();
/*     */   }
/*     */ 
/*     */   public synchronized String getSelectedItem()
/*     */   {
/* 372 */     return this.selectedIndex >= 0 ? getItem(this.selectedIndex) : null;
/*     */   }
/*     */ 
/*     */   public synchronized Object[] getSelectedObjects()
/*     */   {
/* 381 */     if (this.selectedIndex >= 0) {
/* 382 */       Object[] arrayOfObject = new Object[1];
/* 383 */       arrayOfObject[0] = getItem(this.selectedIndex);
/* 384 */       return arrayOfObject;
/*     */     }
/* 386 */     return null;
/*     */   }
/*     */ 
/*     */   public int getSelectedIndex()
/*     */   {
/* 398 */     return this.selectedIndex;
/*     */   }
/*     */ 
/*     */   public synchronized void select(int paramInt)
/*     */   {
/* 419 */     if ((paramInt >= this.pItems.size()) || (paramInt < 0)) {
/* 420 */       throw new IllegalArgumentException("illegal Choice item position: " + paramInt);
/*     */     }
/* 422 */     if (this.pItems.size() > 0) {
/* 423 */       this.selectedIndex = paramInt;
/* 424 */       ChoicePeer localChoicePeer = (ChoicePeer)this.peer;
/* 425 */       if (localChoicePeer != null)
/* 426 */         localChoicePeer.select(paramInt);
/*     */     }
/*     */   }
/*     */ 
/*     */   public synchronized void select(String paramString)
/*     */   {
/* 448 */     int i = this.pItems.indexOf(paramString);
/* 449 */     if (i >= 0)
/* 450 */       select(i);
/*     */   }
/*     */ 
/*     */   public synchronized void addItemListener(ItemListener paramItemListener)
/*     */   {
/* 471 */     if (paramItemListener == null) {
/* 472 */       return;
/*     */     }
/* 474 */     this.itemListener = AWTEventMulticaster.add(this.itemListener, paramItemListener);
/* 475 */     this.newEventsOnly = true;
/*     */   }
/*     */ 
/*     */   public synchronized void removeItemListener(ItemListener paramItemListener)
/*     */   {
/* 493 */     if (paramItemListener == null) {
/* 494 */       return;
/*     */     }
/* 496 */     this.itemListener = AWTEventMulticaster.remove(this.itemListener, paramItemListener);
/*     */   }
/*     */ 
/*     */   public synchronized ItemListener[] getItemListeners()
/*     */   {
/* 514 */     return (ItemListener[])getListeners(ItemListener.class);
/*     */   }
/*     */ 
/*     */   public <T extends EventListener> T[] getListeners(Class<T> paramClass)
/*     */   {
/* 551 */     ItemListener localItemListener = null;
/* 552 */     if (paramClass == ItemListener.class)
/* 553 */       localItemListener = this.itemListener;
/*     */     else {
/* 555 */       return super.getListeners(paramClass);
/*     */     }
/* 557 */     return AWTEventMulticaster.getListeners(localItemListener, paramClass);
/*     */   }
/*     */ 
/*     */   boolean eventEnabled(AWTEvent paramAWTEvent)
/*     */   {
/* 562 */     if (paramAWTEvent.id == 701) {
/* 563 */       if (((this.eventMask & 0x200) != 0L) || (this.itemListener != null))
/*     */       {
/* 565 */         return true;
/*     */       }
/* 567 */       return false;
/*     */     }
/* 569 */     return super.eventEnabled(paramAWTEvent);
/*     */   }
/*     */ 
/*     */   protected void processEvent(AWTEvent paramAWTEvent)
/*     */   {
/* 587 */     if ((paramAWTEvent instanceof ItemEvent)) {
/* 588 */       processItemEvent((ItemEvent)paramAWTEvent);
/* 589 */       return;
/*     */     }
/* 591 */     super.processEvent(paramAWTEvent);
/*     */   }
/*     */ 
/*     */   protected void processItemEvent(ItemEvent paramItemEvent)
/*     */   {
/* 619 */     ItemListener localItemListener = this.itemListener;
/* 620 */     if (localItemListener != null)
/* 621 */       localItemListener.itemStateChanged(paramItemEvent);
/*     */   }
/*     */ 
/*     */   protected String paramString()
/*     */   {
/* 635 */     return super.paramString() + ",current=" + getSelectedItem();
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*     */     throws IOException
/*     */   {
/* 670 */     paramObjectOutputStream.defaultWriteObject();
/*     */ 
/* 672 */     AWTEventMulticaster.save(paramObjectOutputStream, "itemL", this.itemListener);
/* 673 */     paramObjectOutputStream.writeObject(null);
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream)
/*     */     throws ClassNotFoundException, IOException, HeadlessException
/*     */   {
/* 695 */     GraphicsEnvironment.checkHeadless();
/* 696 */     paramObjectInputStream.defaultReadObject();
/*     */     Object localObject;
/* 699 */     while (null != (localObject = paramObjectInputStream.readObject())) {
/* 700 */       String str = ((String)localObject).intern();
/*     */ 
/* 702 */       if ("itemL" == str) {
/* 703 */         addItemListener((ItemListener)paramObjectInputStream.readObject());
/*     */       }
/*     */       else
/* 706 */         paramObjectInputStream.readObject();
/*     */     }
/*     */   }
/*     */ 
/*     */   public AccessibleContext getAccessibleContext()
/*     */   {
/* 728 */     if (this.accessibleContext == null) {
/* 729 */       this.accessibleContext = new AccessibleAWTChoice();
/*     */     }
/* 731 */     return this.accessibleContext;
/*     */   }
/*     */ 
/*     */   protected class AccessibleAWTChoice extends Component.AccessibleAWTComponent
/*     */     implements AccessibleAction
/*     */   {
/*     */     private static final long serialVersionUID = 7175603582428509322L;
/*     */ 
/*     */     public AccessibleAWTChoice()
/*     */     {
/* 749 */       super();
/*     */     }
/*     */ 
/*     */     public AccessibleAction getAccessibleAction()
/*     */     {
/* 762 */       return this;
/*     */     }
/*     */ 
/*     */     public AccessibleRole getAccessibleRole()
/*     */     {
/* 773 */       return AccessibleRole.COMBO_BOX;
/*     */     }
/*     */ 
/*     */     public int getAccessibleActionCount()
/*     */     {
/* 784 */       return 0;
/*     */     }
/*     */ 
/*     */     public String getAccessibleActionDescription(int paramInt)
/*     */     {
/* 795 */       return null;
/*     */     }
/*     */ 
/*     */     public boolean doAccessibleAction(int paramInt)
/*     */     {
/* 806 */       return false;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.Choice
 * JD-Core Version:    0.6.2
 */