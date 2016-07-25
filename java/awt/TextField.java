/*     */ package java.awt;
/*     */ 
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.peer.TextFieldPeer;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.util.EventListener;
/*     */ import javax.accessibility.AccessibleContext;
/*     */ import javax.accessibility.AccessibleState;
/*     */ import javax.accessibility.AccessibleStateSet;
/*     */ 
/*     */ public class TextField extends TextComponent
/*     */ {
/*     */   int columns;
/*     */   char echoChar;
/*     */   transient ActionListener actionListener;
/*     */   private static final String base = "textfield";
/* 127 */   private static int nameCounter = 0;
/*     */   private static final long serialVersionUID = -2966288784432217853L;
/* 647 */   private int textFieldSerializedDataVersion = 1;
/*     */ 
/*     */   private static native void initIDs();
/*     */ 
/*     */   public TextField()
/*     */     throws HeadlessException
/*     */   {
/* 154 */     this("", 0);
/*     */   }
/*     */ 
/*     */   public TextField(String paramString)
/*     */     throws HeadlessException
/*     */   {
/* 167 */     this(paramString, paramString != null ? paramString.length() : 0);
/*     */   }
/*     */ 
/*     */   public TextField(int paramInt)
/*     */     throws HeadlessException
/*     */   {
/* 182 */     this("", paramInt);
/*     */   }
/*     */ 
/*     */   public TextField(String paramString, int paramInt)
/*     */     throws HeadlessException
/*     */   {
/* 201 */     super(paramString);
/* 202 */     this.columns = (paramInt >= 0 ? paramInt : 0);
/*     */   }
/*     */ 
/*     */   String constructComponentName()
/*     */   {
/* 210 */     synchronized (TextField.class) {
/* 211 */       return "textfield" + nameCounter++;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void addNotify()
/*     */   {
/* 220 */     synchronized (getTreeLock()) {
/* 221 */       if (this.peer == null)
/* 222 */         this.peer = getToolkit().createTextField(this);
/* 223 */       super.addNotify();
/*     */     }
/*     */   }
/*     */ 
/*     */   public char getEchoChar()
/*     */   {
/* 246 */     return this.echoChar;
/*     */   }
/*     */ 
/*     */   public void setEchoChar(char paramChar)
/*     */   {
/* 271 */     setEchoCharacter(paramChar);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public synchronized void setEchoCharacter(char paramChar)
/*     */   {
/* 280 */     if (this.echoChar != paramChar) {
/* 281 */       this.echoChar = paramChar;
/* 282 */       TextFieldPeer localTextFieldPeer = (TextFieldPeer)this.peer;
/* 283 */       if (localTextFieldPeer != null)
/* 284 */         localTextFieldPeer.setEchoChar(paramChar);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setText(String paramString)
/*     */   {
/* 296 */     super.setText(paramString);
/*     */ 
/* 299 */     invalidateIfValid();
/*     */   }
/*     */ 
/*     */   public boolean echoCharIsSet()
/*     */   {
/* 316 */     return this.echoChar != 0;
/*     */   }
/*     */ 
/*     */   public int getColumns()
/*     */   {
/* 327 */     return this.columns;
/*     */   }
/*     */ 
/*     */   public void setColumns(int paramInt)
/*     */   {
/*     */     int i;
/* 342 */     synchronized (this) {
/* 343 */       i = this.columns;
/* 344 */       if (paramInt < 0) {
/* 345 */         throw new IllegalArgumentException("columns less than zero.");
/*     */       }
/* 347 */       if (paramInt != i) {
/* 348 */         this.columns = paramInt;
/*     */       }
/*     */     }
/*     */ 
/* 352 */     if (paramInt != i)
/* 353 */       invalidate();
/*     */   }
/*     */ 
/*     */   public Dimension getPreferredSize(int paramInt)
/*     */   {
/* 367 */     return preferredSize(paramInt);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public Dimension preferredSize(int paramInt)
/*     */   {
/* 376 */     synchronized (getTreeLock()) {
/* 377 */       TextFieldPeer localTextFieldPeer = (TextFieldPeer)this.peer;
/* 378 */       return localTextFieldPeer != null ? localTextFieldPeer.getPreferredSize(paramInt) : super.preferredSize();
/*     */     }
/*     */   }
/*     */ 
/*     */   public Dimension getPreferredSize()
/*     */   {
/* 391 */     return preferredSize();
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public Dimension preferredSize()
/*     */   {
/* 400 */     synchronized (getTreeLock()) {
/* 401 */       return this.columns > 0 ? preferredSize(this.columns) : super.preferredSize();
/*     */     }
/*     */   }
/*     */ 
/*     */   public Dimension getMinimumSize(int paramInt)
/*     */   {
/* 415 */     return minimumSize(paramInt);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public Dimension minimumSize(int paramInt)
/*     */   {
/* 424 */     synchronized (getTreeLock()) {
/* 425 */       TextFieldPeer localTextFieldPeer = (TextFieldPeer)this.peer;
/* 426 */       return localTextFieldPeer != null ? localTextFieldPeer.getMinimumSize(paramInt) : super.minimumSize();
/*     */     }
/*     */   }
/*     */ 
/*     */   public Dimension getMinimumSize()
/*     */   {
/* 439 */     return minimumSize();
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public Dimension minimumSize()
/*     */   {
/* 448 */     synchronized (getTreeLock()) {
/* 449 */       return this.columns > 0 ? minimumSize(this.columns) : super.minimumSize();
/*     */     }
/*     */   }
/*     */ 
/*     */   public synchronized void addActionListener(ActionListener paramActionListener)
/*     */   {
/* 469 */     if (paramActionListener == null) {
/* 470 */       return;
/*     */     }
/* 472 */     this.actionListener = AWTEventMulticaster.add(this.actionListener, paramActionListener);
/* 473 */     this.newEventsOnly = true;
/*     */   }
/*     */ 
/*     */   public synchronized void removeActionListener(ActionListener paramActionListener)
/*     */   {
/* 490 */     if (paramActionListener == null) {
/* 491 */       return;
/*     */     }
/* 493 */     this.actionListener = AWTEventMulticaster.remove(this.actionListener, paramActionListener);
/*     */   }
/*     */ 
/*     */   public synchronized ActionListener[] getActionListeners()
/*     */   {
/* 510 */     return (ActionListener[])getListeners(ActionListener.class);
/*     */   }
/*     */ 
/*     */   public <T extends EventListener> T[] getListeners(Class<T> paramClass)
/*     */   {
/* 547 */     ActionListener localActionListener = null;
/* 548 */     if (paramClass == ActionListener.class)
/* 549 */       localActionListener = this.actionListener;
/*     */     else {
/* 551 */       return super.getListeners(paramClass);
/*     */     }
/* 553 */     return AWTEventMulticaster.getListeners(localActionListener, paramClass);
/*     */   }
/*     */ 
/*     */   boolean eventEnabled(AWTEvent paramAWTEvent)
/*     */   {
/* 558 */     if (paramAWTEvent.id == 1001) {
/* 559 */       if (((this.eventMask & 0x80) != 0L) || (this.actionListener != null))
/*     */       {
/* 561 */         return true;
/*     */       }
/* 563 */       return false;
/*     */     }
/* 565 */     return super.eventEnabled(paramAWTEvent);
/*     */   }
/*     */ 
/*     */   protected void processEvent(AWTEvent paramAWTEvent)
/*     */   {
/* 584 */     if ((paramAWTEvent instanceof ActionEvent)) {
/* 585 */       processActionEvent((ActionEvent)paramAWTEvent);
/* 586 */       return;
/*     */     }
/* 588 */     super.processEvent(paramAWTEvent);
/*     */   }
/*     */ 
/*     */   protected void processActionEvent(ActionEvent paramActionEvent)
/*     */   {
/* 615 */     ActionListener localActionListener = this.actionListener;
/* 616 */     if (localActionListener != null)
/* 617 */       localActionListener.actionPerformed(paramActionEvent);
/*     */   }
/*     */ 
/*     */   protected String paramString()
/*     */   {
/* 631 */     String str = super.paramString();
/* 632 */     if (this.echoChar != 0) {
/* 633 */       str = str + ",echo=" + this.echoChar;
/*     */     }
/* 635 */     return str;
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*     */     throws IOException
/*     */   {
/* 667 */     paramObjectOutputStream.defaultWriteObject();
/*     */ 
/* 669 */     AWTEventMulticaster.save(paramObjectOutputStream, "actionL", this.actionListener);
/* 670 */     paramObjectOutputStream.writeObject(null);
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream)
/*     */     throws ClassNotFoundException, IOException, HeadlessException
/*     */   {
/* 690 */     paramObjectInputStream.defaultReadObject();
/*     */ 
/* 693 */     if (this.columns < 0)
/* 694 */       this.columns = 0;
/*     */     Object localObject;
/* 699 */     while (null != (localObject = paramObjectInputStream.readObject())) {
/* 700 */       String str = ((String)localObject).intern();
/*     */ 
/* 702 */       if ("actionL" == str) {
/* 703 */         addActionListener((ActionListener)paramObjectInputStream.readObject());
/*     */       }
/*     */       else
/* 706 */         paramObjectInputStream.readObject();
/*     */     }
/*     */   }
/*     */ 
/*     */   public AccessibleContext getAccessibleContext()
/*     */   {
/* 728 */     if (this.accessibleContext == null) {
/* 729 */       this.accessibleContext = new AccessibleAWTTextField();
/*     */     }
/* 731 */     return this.accessibleContext;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 141 */     Toolkit.loadLibraries();
/* 142 */     if (!GraphicsEnvironment.isHeadless())
/* 143 */       initIDs();
/*     */   }
/*     */ 
/*     */   protected class AccessibleAWTTextField extends TextComponent.AccessibleAWTTextComponent
/*     */   {
/*     */     private static final long serialVersionUID = 6219164359235943158L;
/*     */ 
/*     */     protected AccessibleAWTTextField()
/*     */     {
/* 740 */       super();
/*     */     }
/*     */ 
/*     */     public AccessibleStateSet getAccessibleStateSet()
/*     */     {
/* 755 */       AccessibleStateSet localAccessibleStateSet = super.getAccessibleStateSet();
/* 756 */       localAccessibleStateSet.add(AccessibleState.SINGLE_LINE);
/* 757 */       return localAccessibleStateSet;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.TextField
 * JD-Core Version:    0.6.2
 */