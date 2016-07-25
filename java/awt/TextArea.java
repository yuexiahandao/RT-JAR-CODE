/*     */ package java.awt;
/*     */ 
/*     */ import java.awt.peer.TextAreaPeer;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import javax.accessibility.AccessibleContext;
/*     */ import javax.accessibility.AccessibleState;
/*     */ import javax.accessibility.AccessibleStateSet;
/*     */ 
/*     */ public class TextArea extends TextComponent
/*     */ {
/*     */   int rows;
/*     */   int columns;
/*     */   private static final String base = "text";
/*  83 */   private static int nameCounter = 0;
/*     */   public static final int SCROLLBARS_BOTH = 0;
/*     */   public static final int SCROLLBARS_VERTICAL_ONLY = 1;
/*     */   public static final int SCROLLBARS_HORIZONTAL_ONLY = 2;
/*     */   public static final int SCROLLBARS_NONE = 3;
/*     */   private int scrollbarVisibility;
/* 144 */   private static Set forwardTraversalKeys = KeyboardFocusManager.initFocusTraversalKeysSet("ctrl TAB", new HashSet());
/*     */ 
/* 147 */   private static Set backwardTraversalKeys = KeyboardFocusManager.initFocusTraversalKeysSet("ctrl shift TAB", new HashSet());
/*     */   private static final long serialVersionUID = 3692302836626095722L;
/* 625 */   private int textAreaSerializedDataVersion = 2;
/*     */ 
/*     */   private static native void initIDs();
/*     */ 
/*     */   public TextArea()
/*     */     throws HeadlessException
/*     */   {
/* 162 */     this("", 0, 0, 0);
/*     */   }
/*     */ 
/*     */   public TextArea(String paramString)
/*     */     throws HeadlessException
/*     */   {
/* 178 */     this(paramString, 0, 0, 0);
/*     */   }
/*     */ 
/*     */   public TextArea(int paramInt1, int paramInt2)
/*     */     throws HeadlessException
/*     */   {
/* 196 */     this("", paramInt1, paramInt2, 0);
/*     */   }
/*     */ 
/*     */   public TextArea(String paramString, int paramInt1, int paramInt2)
/*     */     throws HeadlessException
/*     */   {
/* 218 */     this(paramString, paramInt1, paramInt2, 0);
/*     */   }
/*     */ 
/*     */   public TextArea(String paramString, int paramInt1, int paramInt2, int paramInt3)
/*     */     throws HeadlessException
/*     */   {
/* 258 */     super(paramString);
/*     */ 
/* 260 */     this.rows = (paramInt1 >= 0 ? paramInt1 : 0);
/* 261 */     this.columns = (paramInt2 >= 0 ? paramInt2 : 0);
/*     */ 
/* 263 */     if ((paramInt3 >= 0) && (paramInt3 <= 3))
/* 264 */       this.scrollbarVisibility = paramInt3;
/*     */     else {
/* 266 */       this.scrollbarVisibility = 0;
/*     */     }
/*     */ 
/* 269 */     setFocusTraversalKeys(0, forwardTraversalKeys);
/*     */ 
/* 271 */     setFocusTraversalKeys(1, backwardTraversalKeys);
/*     */   }
/*     */ 
/*     */   String constructComponentName()
/*     */   {
/* 280 */     synchronized (TextArea.class) {
/* 281 */       return "text" + nameCounter++;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void addNotify()
/*     */   {
/* 291 */     synchronized (getTreeLock()) {
/* 292 */       if (this.peer == null)
/* 293 */         this.peer = getToolkit().createTextArea(this);
/* 294 */       super.addNotify();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void insert(String paramString, int paramInt)
/*     */   {
/* 313 */     insertText(paramString, paramInt);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public synchronized void insertText(String paramString, int paramInt)
/*     */   {
/* 322 */     TextAreaPeer localTextAreaPeer = (TextAreaPeer)this.peer;
/* 323 */     if (localTextAreaPeer != null)
/* 324 */       localTextAreaPeer.insert(paramString, paramInt);
/*     */     else
/* 326 */       this.text = (this.text.substring(0, paramInt) + paramString + this.text.substring(paramInt));
/*     */   }
/*     */ 
/*     */   public void append(String paramString)
/*     */   {
/* 341 */     appendText(paramString);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public synchronized void appendText(String paramString)
/*     */   {
/* 350 */     if (this.peer != null)
/* 351 */       insertText(paramString, getText().length());
/*     */     else
/* 353 */       this.text += paramString;
/*     */   }
/*     */ 
/*     */   public void replaceRange(String paramString, int paramInt1, int paramInt2)
/*     */   {
/* 377 */     replaceText(paramString, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public synchronized void replaceText(String paramString, int paramInt1, int paramInt2)
/*     */   {
/* 386 */     TextAreaPeer localTextAreaPeer = (TextAreaPeer)this.peer;
/* 387 */     if (localTextAreaPeer != null)
/* 388 */       localTextAreaPeer.replaceRange(paramString, paramInt1, paramInt2);
/*     */     else
/* 390 */       this.text = (this.text.substring(0, paramInt1) + paramString + this.text.substring(paramInt2));
/*     */   }
/*     */ 
/*     */   public int getRows()
/*     */   {
/* 402 */     return this.rows;
/*     */   }
/*     */ 
/*     */   public void setRows(int paramInt)
/*     */   {
/* 416 */     int i = this.rows;
/* 417 */     if (paramInt < 0) {
/* 418 */       throw new IllegalArgumentException("rows less than zero.");
/*     */     }
/* 420 */     if (paramInt != i) {
/* 421 */       this.rows = paramInt;
/* 422 */       invalidate();
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getColumns()
/*     */   {
/* 433 */     return this.columns;
/*     */   }
/*     */ 
/*     */   public void setColumns(int paramInt)
/*     */   {
/* 447 */     int i = this.columns;
/* 448 */     if (paramInt < 0) {
/* 449 */       throw new IllegalArgumentException("columns less than zero.");
/*     */     }
/* 451 */     if (paramInt != i) {
/* 452 */       this.columns = paramInt;
/* 453 */       invalidate();
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getScrollbarVisibility()
/*     */   {
/* 475 */     return this.scrollbarVisibility;
/*     */   }
/*     */ 
/*     */   public Dimension getPreferredSize(int paramInt1, int paramInt2)
/*     */   {
/* 491 */     return preferredSize(paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public Dimension preferredSize(int paramInt1, int paramInt2)
/*     */   {
/* 500 */     synchronized (getTreeLock()) {
/* 501 */       TextAreaPeer localTextAreaPeer = (TextAreaPeer)this.peer;
/* 502 */       return localTextAreaPeer != null ? localTextAreaPeer.getPreferredSize(paramInt1, paramInt2) : super.preferredSize();
/*     */     }
/*     */   }
/*     */ 
/*     */   public Dimension getPreferredSize()
/*     */   {
/* 515 */     return preferredSize();
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public Dimension preferredSize()
/*     */   {
/* 524 */     synchronized (getTreeLock()) {
/* 525 */       return (this.rows > 0) && (this.columns > 0) ? preferredSize(this.rows, this.columns) : super.preferredSize();
/*     */     }
/*     */   }
/*     */ 
/*     */   public Dimension getMinimumSize(int paramInt1, int paramInt2)
/*     */   {
/* 543 */     return minimumSize(paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public Dimension minimumSize(int paramInt1, int paramInt2)
/*     */   {
/* 552 */     synchronized (getTreeLock()) {
/* 553 */       TextAreaPeer localTextAreaPeer = (TextAreaPeer)this.peer;
/* 554 */       return localTextAreaPeer != null ? localTextAreaPeer.getMinimumSize(paramInt1, paramInt2) : super.minimumSize();
/*     */     }
/*     */   }
/*     */ 
/*     */   public Dimension getMinimumSize()
/*     */   {
/* 567 */     return minimumSize();
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public Dimension minimumSize()
/*     */   {
/* 576 */     synchronized (getTreeLock()) {
/* 577 */       return (this.rows > 0) && (this.columns > 0) ? minimumSize(this.rows, this.columns) : super.minimumSize();
/*     */     }
/*     */   }
/*     */ 
/*     */   protected String paramString()
/*     */   {
/*     */     String str;
/* 594 */     switch (this.scrollbarVisibility) {
/*     */     case 0:
/* 596 */       str = "both";
/* 597 */       break;
/*     */     case 1:
/* 599 */       str = "vertical-only";
/* 600 */       break;
/*     */     case 2:
/* 602 */       str = "horizontal-only";
/* 603 */       break;
/*     */     case 3:
/* 605 */       str = "none";
/* 606 */       break;
/*     */     default:
/* 608 */       str = "invalid display policy";
/*     */     }
/*     */ 
/* 611 */     return super.paramString() + ",rows=" + this.rows + ",columns=" + this.columns + ",scrollbarVisibility=" + str;
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream)
/*     */     throws ClassNotFoundException, IOException, HeadlessException
/*     */   {
/* 638 */     paramObjectInputStream.defaultReadObject();
/*     */ 
/* 642 */     if (this.columns < 0) {
/* 643 */       this.columns = 0;
/*     */     }
/* 645 */     if (this.rows < 0) {
/* 646 */       this.rows = 0;
/*     */     }
/*     */ 
/* 649 */     if ((this.scrollbarVisibility < 0) || (this.scrollbarVisibility > 3))
/*     */     {
/* 651 */       this.scrollbarVisibility = 0;
/*     */     }
/*     */ 
/* 654 */     if (this.textAreaSerializedDataVersion < 2) {
/* 655 */       setFocusTraversalKeys(0, forwardTraversalKeys);
/*     */ 
/* 657 */       setFocusTraversalKeys(1, backwardTraversalKeys);
/*     */     }
/*     */   }
/*     */ 
/*     */   public AccessibleContext getAccessibleContext()
/*     */   {
/* 680 */     if (this.accessibleContext == null) {
/* 681 */       this.accessibleContext = new AccessibleAWTTextArea();
/*     */     }
/* 683 */     return this.accessibleContext;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 140 */     Toolkit.loadLibraries();
/* 141 */     if (!GraphicsEnvironment.isHeadless())
/* 142 */       initIDs();
/*     */   }
/*     */ 
/*     */   protected class AccessibleAWTTextArea extends TextComponent.AccessibleAWTTextComponent
/*     */   {
/*     */     private static final long serialVersionUID = 3472827823632144419L;
/*     */ 
/*     */     protected AccessibleAWTTextArea()
/*     */     {
/* 692 */       super();
/*     */     }
/*     */ 
/*     */     public AccessibleStateSet getAccessibleStateSet()
/*     */     {
/* 707 */       AccessibleStateSet localAccessibleStateSet = super.getAccessibleStateSet();
/* 708 */       localAccessibleStateSet.add(AccessibleState.MULTI_LINE);
/* 709 */       return localAccessibleStateSet;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.TextArea
 * JD-Core Version:    0.6.2
 */