/*     */ package java.awt;
/*     */ 
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.peer.ButtonPeer;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.util.EventListener;
/*     */ import javax.accessibility.Accessible;
/*     */ import javax.accessibility.AccessibleAction;
/*     */ import javax.accessibility.AccessibleContext;
/*     */ import javax.accessibility.AccessibleRole;
/*     */ import javax.accessibility.AccessibleValue;
/*     */ 
/*     */ public class Button extends Component
/*     */   implements Accessible
/*     */ {
/*     */   String label;
/*     */   String actionCommand;
/*     */   transient ActionListener actionListener;
/*     */   private static final String base = "button";
/* 109 */   private static int nameCounter = 0;
/*     */   private static final long serialVersionUID = -8774683716313001058L;
/* 434 */   private int buttonSerializedDataVersion = 1;
/*     */ 
/*     */   private static native void initIDs();
/*     */ 
/*     */   public Button()
/*     */     throws HeadlessException
/*     */   {
/* 139 */     this("");
/*     */   }
/*     */ 
/*     */   public Button(String paramString)
/*     */     throws HeadlessException
/*     */   {
/* 152 */     GraphicsEnvironment.checkHeadless();
/* 153 */     this.label = paramString;
/*     */   }
/*     */ 
/*     */   String constructComponentName()
/*     */   {
/* 161 */     synchronized (Button.class) {
/* 162 */       return "button" + nameCounter++;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void addNotify()
/*     */   {
/* 175 */     synchronized (getTreeLock()) {
/* 176 */       if (this.peer == null)
/* 177 */         this.peer = getToolkit().createButton(this);
/* 178 */       super.addNotify();
/*     */     }
/*     */   }
/*     */ 
/*     */   public String getLabel()
/*     */   {
/* 190 */     return this.label;
/*     */   }
/*     */ 
/*     */   public void setLabel(String paramString)
/*     */   {
/* 201 */     int i = 0;
/*     */ 
/* 203 */     synchronized (this) {
/* 204 */       if ((paramString != this.label) && ((this.label == null) || (!this.label.equals(paramString))))
/*     */       {
/* 206 */         this.label = paramString;
/* 207 */         ButtonPeer localButtonPeer = (ButtonPeer)this.peer;
/* 208 */         if (localButtonPeer != null) {
/* 209 */           localButtonPeer.setLabel(paramString);
/*     */         }
/* 211 */         i = 1;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 216 */     if (i != 0)
/* 217 */       invalidateIfValid();
/*     */   }
/*     */ 
/*     */   public void setActionCommand(String paramString)
/*     */   {
/* 234 */     this.actionCommand = paramString;
/*     */   }
/*     */ 
/*     */   public String getActionCommand()
/*     */   {
/* 243 */     return this.actionCommand == null ? this.label : this.actionCommand;
/*     */   }
/*     */ 
/*     */   public synchronized void addActionListener(ActionListener paramActionListener)
/*     */   {
/* 261 */     if (paramActionListener == null) {
/* 262 */       return;
/*     */     }
/* 264 */     this.actionListener = AWTEventMulticaster.add(this.actionListener, paramActionListener);
/* 265 */     this.newEventsOnly = true;
/*     */   }
/*     */ 
/*     */   public synchronized void removeActionListener(ActionListener paramActionListener)
/*     */   {
/* 283 */     if (paramActionListener == null) {
/* 284 */       return;
/*     */     }
/* 286 */     this.actionListener = AWTEventMulticaster.remove(this.actionListener, paramActionListener);
/*     */   }
/*     */ 
/*     */   public synchronized ActionListener[] getActionListeners()
/*     */   {
/* 303 */     return (ActionListener[])getListeners(ActionListener.class);
/*     */   }
/*     */ 
/*     */   public <T extends EventListener> T[] getListeners(Class<T> paramClass)
/*     */   {
/* 340 */     ActionListener localActionListener = null;
/* 341 */     if (paramClass == ActionListener.class)
/* 342 */       localActionListener = this.actionListener;
/*     */     else {
/* 344 */       return super.getListeners(paramClass);
/*     */     }
/* 346 */     return AWTEventMulticaster.getListeners(localActionListener, paramClass);
/*     */   }
/*     */ 
/*     */   boolean eventEnabled(AWTEvent paramAWTEvent)
/*     */   {
/* 351 */     if (paramAWTEvent.id == 1001) {
/* 352 */       if (((this.eventMask & 0x80) != 0L) || (this.actionListener != null))
/*     */       {
/* 354 */         return true;
/*     */       }
/* 356 */       return false;
/*     */     }
/* 358 */     return super.eventEnabled(paramAWTEvent);
/*     */   }
/*     */ 
/*     */   protected void processEvent(AWTEvent paramAWTEvent)
/*     */   {
/* 376 */     if ((paramAWTEvent instanceof ActionEvent)) {
/* 377 */       processActionEvent((ActionEvent)paramAWTEvent);
/* 378 */       return;
/*     */     }
/* 380 */     super.processEvent(paramAWTEvent);
/*     */   }
/*     */ 
/*     */   protected void processActionEvent(ActionEvent paramActionEvent)
/*     */   {
/* 407 */     ActionListener localActionListener = this.actionListener;
/* 408 */     if (localActionListener != null)
/* 409 */       localActionListener.actionPerformed(paramActionEvent);
/*     */   }
/*     */ 
/*     */   protected String paramString()
/*     */   {
/* 423 */     return super.paramString() + ",label=" + this.label;
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*     */     throws IOException
/*     */   {
/* 458 */     paramObjectOutputStream.defaultWriteObject();
/*     */ 
/* 460 */     AWTEventMulticaster.save(paramObjectOutputStream, "actionL", this.actionListener);
/* 461 */     paramObjectOutputStream.writeObject(null);
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream)
/*     */     throws ClassNotFoundException, IOException, HeadlessException
/*     */   {
/* 483 */     GraphicsEnvironment.checkHeadless();
/* 484 */     paramObjectInputStream.defaultReadObject();
/*     */     Object localObject;
/* 487 */     while (null != (localObject = paramObjectInputStream.readObject())) {
/* 488 */       String str = ((String)localObject).intern();
/*     */ 
/* 490 */       if ("actionL" == str) {
/* 491 */         addActionListener((ActionListener)paramObjectInputStream.readObject());
/*     */       }
/*     */       else
/* 494 */         paramObjectInputStream.readObject();
/*     */     }
/*     */   }
/*     */ 
/*     */   public AccessibleContext getAccessibleContext()
/*     */   {
/* 519 */     if (this.accessibleContext == null) {
/* 520 */       this.accessibleContext = new AccessibleAWTButton();
/*     */     }
/* 522 */     return this.accessibleContext;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 119 */     Toolkit.loadLibraries();
/* 120 */     if (!GraphicsEnvironment.isHeadless())
/* 121 */       initIDs();
/*     */   }
/*     */ 
/*     */   protected class AccessibleAWTButton extends Component.AccessibleAWTComponent
/*     */     implements AccessibleAction, AccessibleValue
/*     */   {
/*     */     private static final long serialVersionUID = -5932203980244017102L;
/*     */ 
/*     */     protected AccessibleAWTButton()
/*     */     {
/* 531 */       super();
/*     */     }
/*     */ 
/*     */     public String getAccessibleName()
/*     */     {
/* 546 */       if (this.accessibleName != null) {
/* 547 */         return this.accessibleName;
/*     */       }
/* 549 */       if (Button.this.getLabel() == null) {
/* 550 */         return super.getAccessibleName();
/*     */       }
/* 552 */       return Button.this.getLabel();
/*     */     }
/*     */ 
/*     */     public AccessibleAction getAccessibleAction()
/*     */     {
/* 566 */       return this;
/*     */     }
/*     */ 
/*     */     public AccessibleValue getAccessibleValue()
/*     */     {
/* 578 */       return this;
/*     */     }
/*     */ 
/*     */     public int getAccessibleActionCount()
/*     */     {
/* 589 */       return 1;
/*     */     }
/*     */ 
/*     */     public String getAccessibleActionDescription(int paramInt)
/*     */     {
/* 598 */       if (paramInt == 0)
/*     */       {
/* 600 */         return "click";
/*     */       }
/* 602 */       return null;
/*     */     }
/*     */ 
/*     */     public boolean doAccessibleAction(int paramInt)
/*     */     {
/* 613 */       if (paramInt == 0)
/*     */       {
/* 615 */         Toolkit.getEventQueue().postEvent(new ActionEvent(Button.this, 1001, Button.this.getActionCommand()));
/*     */ 
/* 619 */         return true;
/*     */       }
/* 621 */       return false;
/*     */     }
/*     */ 
/*     */     public Number getCurrentAccessibleValue()
/*     */     {
/* 633 */       return Integer.valueOf(0);
/*     */     }
/*     */ 
/*     */     public boolean setCurrentAccessibleValue(Number paramNumber)
/*     */     {
/* 642 */       return false;
/*     */     }
/*     */ 
/*     */     public Number getMinimumAccessibleValue()
/*     */     {
/* 651 */       return Integer.valueOf(0);
/*     */     }
/*     */ 
/*     */     public Number getMaximumAccessibleValue()
/*     */     {
/* 660 */       return Integer.valueOf(0);
/*     */     }
/*     */ 
/*     */     public AccessibleRole getAccessibleRole()
/*     */     {
/* 671 */       return AccessibleRole.PUSH_BUTTON;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.Button
 * JD-Core Version:    0.6.2
 */