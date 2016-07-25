/*     */ package java.awt;
/*     */ 
/*     */ import java.awt.event.ItemEvent;
/*     */ import java.awt.event.ItemListener;
/*     */ import java.awt.peer.CheckboxPeer;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.util.EventListener;
/*     */ import javax.accessibility.Accessible;
/*     */ import javax.accessibility.AccessibleAction;
/*     */ import javax.accessibility.AccessibleContext;
/*     */ import javax.accessibility.AccessibleRole;
/*     */ import javax.accessibility.AccessibleState;
/*     */ import javax.accessibility.AccessibleStateSet;
/*     */ import javax.accessibility.AccessibleValue;
/*     */ 
/*     */ public class Checkbox extends Component
/*     */   implements ItemSelectable, Accessible
/*     */ {
/*     */   String label;
/*     */   boolean state;
/*     */   CheckboxGroup group;
/*     */   transient ItemListener itemListener;
/*     */   private static final String base = "checkbox";
/* 116 */   private static int nameCounter = 0;
/*     */   private static final long serialVersionUID = 7270714317450821763L;
/* 610 */   private int checkboxSerializedDataVersion = 1;
/*     */ 
/*     */   void setStateInternal(boolean paramBoolean)
/*     */   {
/* 128 */     this.state = paramBoolean;
/* 129 */     CheckboxPeer localCheckboxPeer = (CheckboxPeer)this.peer;
/* 130 */     if (localCheckboxPeer != null)
/* 131 */       localCheckboxPeer.setState(paramBoolean);
/*     */   }
/*     */ 
/*     */   public Checkbox()
/*     */     throws HeadlessException
/*     */   {
/* 144 */     this("", false, null);
/*     */   }
/*     */ 
/*     */   public Checkbox(String paramString)
/*     */     throws HeadlessException
/*     */   {
/* 160 */     this(paramString, false, null);
/*     */   }
/*     */ 
/*     */   public Checkbox(String paramString, boolean paramBoolean)
/*     */     throws HeadlessException
/*     */   {
/* 177 */     this(paramString, paramBoolean, null);
/*     */   }
/*     */ 
/*     */   public Checkbox(String paramString, boolean paramBoolean, CheckboxGroup paramCheckboxGroup)
/*     */     throws HeadlessException
/*     */   {
/* 197 */     GraphicsEnvironment.checkHeadless();
/* 198 */     this.label = paramString;
/* 199 */     this.state = paramBoolean;
/* 200 */     this.group = paramCheckboxGroup;
/* 201 */     if ((paramBoolean) && (paramCheckboxGroup != null))
/* 202 */       paramCheckboxGroup.setSelectedCheckbox(this);
/*     */   }
/*     */ 
/*     */   public Checkbox(String paramString, CheckboxGroup paramCheckboxGroup, boolean paramBoolean)
/*     */     throws HeadlessException
/*     */   {
/* 223 */     this(paramString, paramBoolean, paramCheckboxGroup);
/*     */   }
/*     */ 
/*     */   String constructComponentName()
/*     */   {
/* 233 */     synchronized (Checkbox.class) {
/* 234 */       return "checkbox" + nameCounter++;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void addNotify()
/*     */   {
/* 246 */     synchronized (getTreeLock()) {
/* 247 */       if (this.peer == null)
/* 248 */         this.peer = getToolkit().createCheckbox(this);
/* 249 */       super.addNotify();
/*     */     }
/*     */   }
/*     */ 
/*     */   public String getLabel()
/*     */   {
/* 261 */     return this.label;
/*     */   }
/*     */ 
/*     */   public void setLabel(String paramString)
/*     */   {
/* 272 */     int i = 0;
/*     */ 
/* 274 */     synchronized (this) {
/* 275 */       if ((paramString != this.label) && ((this.label == null) || (!this.label.equals(paramString))))
/*     */       {
/* 277 */         this.label = paramString;
/* 278 */         CheckboxPeer localCheckboxPeer = (CheckboxPeer)this.peer;
/* 279 */         if (localCheckboxPeer != null) {
/* 280 */           localCheckboxPeer.setLabel(paramString);
/*     */         }
/* 282 */         i = 1;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 287 */     if (i != 0)
/* 288 */       invalidateIfValid();
/*     */   }
/*     */ 
/*     */   public boolean getState()
/*     */   {
/* 301 */     return this.state;
/*     */   }
/*     */ 
/*     */   public void setState(boolean paramBoolean)
/*     */   {
/* 320 */     CheckboxGroup localCheckboxGroup = this.group;
/* 321 */     if (localCheckboxGroup != null) {
/* 322 */       if (paramBoolean)
/* 323 */         localCheckboxGroup.setSelectedCheckbox(this);
/* 324 */       else if (localCheckboxGroup.getSelectedCheckbox() == this) {
/* 325 */         paramBoolean = true;
/*     */       }
/*     */     }
/* 328 */     setStateInternal(paramBoolean);
/*     */   }
/*     */ 
/*     */   public Object[] getSelectedObjects()
/*     */   {
/* 337 */     if (this.state) {
/* 338 */       Object[] arrayOfObject = new Object[1];
/* 339 */       arrayOfObject[0] = this.label;
/* 340 */       return arrayOfObject;
/*     */     }
/* 342 */     return null;
/*     */   }
/*     */ 
/*     */   public CheckboxGroup getCheckboxGroup()
/*     */   {
/* 352 */     return this.group;
/*     */   }
/*     */ 
/*     */   public void setCheckboxGroup(CheckboxGroup paramCheckboxGroup)
/*     */   {
/* 378 */     if (this.group == paramCheckboxGroup)
/*     */       return;
/*     */     CheckboxGroup localCheckboxGroup;
/*     */     boolean bool;
/* 382 */     synchronized (this) {
/* 383 */       localCheckboxGroup = this.group;
/* 384 */       bool = getState();
/*     */ 
/* 386 */       this.group = paramCheckboxGroup;
/* 387 */       CheckboxPeer localCheckboxPeer = (CheckboxPeer)this.peer;
/* 388 */       if (localCheckboxPeer != null) {
/* 389 */         localCheckboxPeer.setCheckboxGroup(paramCheckboxGroup);
/*     */       }
/* 391 */       if ((this.group != null) && (getState())) {
/* 392 */         if (this.group.getSelectedCheckbox() != null)
/* 393 */           setState(false);
/*     */         else {
/* 395 */           this.group.setSelectedCheckbox(this);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 408 */     if ((localCheckboxGroup != null) && (bool))
/* 409 */       localCheckboxGroup.setSelectedCheckbox(null);
/*     */   }
/*     */ 
/*     */   public synchronized void addItemListener(ItemListener paramItemListener)
/*     */   {
/* 430 */     if (paramItemListener == null) {
/* 431 */       return;
/*     */     }
/* 433 */     this.itemListener = AWTEventMulticaster.add(this.itemListener, paramItemListener);
/* 434 */     this.newEventsOnly = true;
/*     */   }
/*     */ 
/*     */   public synchronized void removeItemListener(ItemListener paramItemListener)
/*     */   {
/* 452 */     if (paramItemListener == null) {
/* 453 */       return;
/*     */     }
/* 455 */     this.itemListener = AWTEventMulticaster.remove(this.itemListener, paramItemListener);
/*     */   }
/*     */ 
/*     */   public synchronized ItemListener[] getItemListeners()
/*     */   {
/* 473 */     return (ItemListener[])getListeners(ItemListener.class);
/*     */   }
/*     */ 
/*     */   public <T extends EventListener> T[] getListeners(Class<T> paramClass)
/*     */   {
/* 510 */     ItemListener localItemListener = null;
/* 511 */     if (paramClass == ItemListener.class)
/* 512 */       localItemListener = this.itemListener;
/*     */     else {
/* 514 */       return super.getListeners(paramClass);
/*     */     }
/* 516 */     return AWTEventMulticaster.getListeners(localItemListener, paramClass);
/*     */   }
/*     */ 
/*     */   boolean eventEnabled(AWTEvent paramAWTEvent)
/*     */   {
/* 521 */     if (paramAWTEvent.id == 701) {
/* 522 */       if (((this.eventMask & 0x200) != 0L) || (this.itemListener != null))
/*     */       {
/* 524 */         return true;
/*     */       }
/* 526 */       return false;
/*     */     }
/* 528 */     return super.eventEnabled(paramAWTEvent);
/*     */   }
/*     */ 
/*     */   protected void processEvent(AWTEvent paramAWTEvent)
/*     */   {
/* 546 */     if ((paramAWTEvent instanceof ItemEvent)) {
/* 547 */       processItemEvent((ItemEvent)paramAWTEvent);
/* 548 */       return;
/*     */     }
/* 550 */     super.processEvent(paramAWTEvent);
/*     */   }
/*     */ 
/*     */   protected void processItemEvent(ItemEvent paramItemEvent)
/*     */   {
/* 578 */     ItemListener localItemListener = this.itemListener;
/* 579 */     if (localItemListener != null)
/* 580 */       localItemListener.itemStateChanged(paramItemEvent);
/*     */   }
/*     */ 
/*     */   protected String paramString()
/*     */   {
/* 594 */     String str1 = super.paramString();
/* 595 */     String str2 = this.label;
/* 596 */     if (str2 != null) {
/* 597 */       str1 = str1 + ",label=" + str2;
/*     */     }
/* 599 */     return str1 + ",state=" + this.state;
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*     */     throws IOException
/*     */   {
/* 634 */     paramObjectOutputStream.defaultWriteObject();
/*     */ 
/* 636 */     AWTEventMulticaster.save(paramObjectOutputStream, "itemL", this.itemListener);
/* 637 */     paramObjectOutputStream.writeObject(null);
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream)
/*     */     throws ClassNotFoundException, IOException, HeadlessException
/*     */   {
/* 659 */     GraphicsEnvironment.checkHeadless();
/* 660 */     paramObjectInputStream.defaultReadObject();
/*     */     Object localObject;
/* 663 */     while (null != (localObject = paramObjectInputStream.readObject())) {
/* 664 */       String str = ((String)localObject).intern();
/*     */ 
/* 666 */       if ("itemL" == str) {
/* 667 */         addItemListener((ItemListener)paramObjectInputStream.readObject());
/*     */       }
/*     */       else
/* 670 */         paramObjectInputStream.readObject();
/*     */     }
/*     */   }
/*     */ 
/*     */   private static native void initIDs();
/*     */ 
/*     */   public AccessibleContext getAccessibleContext()
/*     */   {
/* 696 */     if (this.accessibleContext == null) {
/* 697 */       this.accessibleContext = new AccessibleAWTCheckbox();
/*     */     }
/* 699 */     return this.accessibleContext;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  80 */     Toolkit.loadLibraries();
/*  81 */     if (!GraphicsEnvironment.isHeadless())
/*  82 */       initIDs();
/*     */   }
/*     */ 
/*     */   protected class AccessibleAWTCheckbox extends Component.AccessibleAWTComponent
/*     */     implements ItemListener, AccessibleAction, AccessibleValue
/*     */   {
/*     */     private static final long serialVersionUID = 7881579233144754107L;
/*     */ 
/*     */     public AccessibleAWTCheckbox()
/*     */     {
/* 717 */       super();
/* 718 */       Checkbox.this.addItemListener(this);
/*     */     }
/*     */ 
/*     */     public void itemStateChanged(ItemEvent paramItemEvent)
/*     */     {
/* 726 */       Checkbox localCheckbox = (Checkbox)paramItemEvent.getSource();
/* 727 */       if (Checkbox.this.accessibleContext != null)
/* 728 */         if (localCheckbox.getState()) {
/* 729 */           Checkbox.this.accessibleContext.firePropertyChange("AccessibleState", null, AccessibleState.CHECKED);
/*     */         }
/*     */         else
/*     */         {
/* 733 */           Checkbox.this.accessibleContext.firePropertyChange("AccessibleState", AccessibleState.CHECKED, null);
/*     */         }
/*     */     }
/*     */ 
/*     */     public AccessibleAction getAccessibleAction()
/*     */     {
/* 749 */       return this;
/*     */     }
/*     */ 
/*     */     public AccessibleValue getAccessibleValue()
/*     */     {
/* 761 */       return this;
/*     */     }
/*     */ 
/*     */     public int getAccessibleActionCount()
/*     */     {
/* 772 */       return 0;
/*     */     }
/*     */ 
/*     */     public String getAccessibleActionDescription(int paramInt)
/*     */     {
/* 781 */       return null;
/*     */     }
/*     */ 
/*     */     public boolean doAccessibleAction(int paramInt)
/*     */     {
/* 791 */       return false;
/*     */     }
/*     */ 
/*     */     public Number getCurrentAccessibleValue()
/*     */     {
/* 802 */       return null;
/*     */     }
/*     */ 
/*     */     public boolean setCurrentAccessibleValue(Number paramNumber)
/*     */     {
/* 812 */       return false;
/*     */     }
/*     */ 
/*     */     public Number getMinimumAccessibleValue()
/*     */     {
/* 823 */       return null;
/*     */     }
/*     */ 
/*     */     public Number getMaximumAccessibleValue()
/*     */     {
/* 834 */       return null;
/*     */     }
/*     */ 
/*     */     public AccessibleRole getAccessibleRole()
/*     */     {
/* 845 */       return AccessibleRole.CHECK_BOX;
/*     */     }
/*     */ 
/*     */     public AccessibleStateSet getAccessibleStateSet()
/*     */     {
/* 856 */       AccessibleStateSet localAccessibleStateSet = super.getAccessibleStateSet();
/* 857 */       if (Checkbox.this.getState()) {
/* 858 */         localAccessibleStateSet.add(AccessibleState.CHECKED);
/*     */       }
/* 860 */       return localAccessibleStateSet;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.Checkbox
 * JD-Core Version:    0.6.2
 */