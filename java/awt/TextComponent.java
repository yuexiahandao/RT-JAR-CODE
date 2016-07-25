/*      */ package java.awt;
/*      */ 
/*      */ import java.awt.event.TextEvent;
/*      */ import java.awt.event.TextListener;
/*      */ import java.awt.im.InputMethodRequests;
/*      */ import java.awt.peer.TextComponentPeer;
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.text.BreakIterator;
/*      */ import java.util.EventListener;
/*      */ import javax.accessibility.Accessible;
/*      */ import javax.accessibility.AccessibleContext;
/*      */ import javax.accessibility.AccessibleRole;
/*      */ import javax.accessibility.AccessibleState;
/*      */ import javax.accessibility.AccessibleStateSet;
/*      */ import javax.accessibility.AccessibleText;
/*      */ import javax.swing.text.AttributeSet;
/*      */ import sun.awt.InputMethodSupport;
/*      */ 
/*      */ public class TextComponent extends Component
/*      */   implements Accessible
/*      */ {
/*      */   String text;
/*   81 */   boolean editable = true;
/*      */   int selectionStart;
/*      */   int selectionEnd;
/*  108 */   boolean backgroundSetByClientCode = false;
/*      */   protected transient TextListener textListener;
/*      */   private static final long serialVersionUID = -2214773872412987419L;
/*  746 */   private int textComponentSerializedDataVersion = 1;
/*      */ 
/* 1224 */   private boolean checkForEnableIM = true;
/*      */ 
/*      */   TextComponent(String paramString)
/*      */     throws HeadlessException
/*      */   {
/*  131 */     GraphicsEnvironment.checkHeadless();
/*  132 */     this.text = (paramString != null ? paramString : "");
/*  133 */     setCursor(Cursor.getPredefinedCursor(2));
/*      */   }
/*      */ 
/*      */   private void enableInputMethodsIfNecessary() {
/*  137 */     if (this.checkForEnableIM) {
/*  138 */       this.checkForEnableIM = false;
/*      */       try {
/*  140 */         Toolkit localToolkit = Toolkit.getDefaultToolkit();
/*  141 */         boolean bool = false;
/*  142 */         if ((localToolkit instanceof InputMethodSupport)) {
/*  143 */           bool = ((InputMethodSupport)localToolkit).enableInputMethodsForTextComponent();
/*      */         }
/*      */ 
/*  146 */         enableInputMethods(bool);
/*      */       }
/*      */       catch (Exception localException)
/*      */       {
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void enableInputMethods(boolean paramBoolean)
/*      */   {
/*  166 */     this.checkForEnableIM = false;
/*  167 */     super.enableInputMethods(paramBoolean);
/*      */   }
/*      */ 
/*      */   boolean areInputMethodsEnabled()
/*      */   {
/*  173 */     if (this.checkForEnableIM) {
/*  174 */       enableInputMethodsIfNecessary();
/*      */     }
/*      */ 
/*  179 */     return (this.eventMask & 0x1000) != 0L;
/*      */   }
/*      */ 
/*      */   public InputMethodRequests getInputMethodRequests() {
/*  183 */     TextComponentPeer localTextComponentPeer = (TextComponentPeer)this.peer;
/*  184 */     if (localTextComponentPeer != null) return localTextComponentPeer.getInputMethodRequests();
/*  185 */     return null;
/*      */   }
/*      */ 
/*      */   public void addNotify()
/*      */   {
/*  198 */     super.addNotify();
/*  199 */     enableInputMethodsIfNecessary();
/*      */   }
/*      */ 
/*      */   public void removeNotify()
/*      */   {
/*  209 */     synchronized (getTreeLock()) {
/*  210 */       TextComponentPeer localTextComponentPeer = (TextComponentPeer)this.peer;
/*  211 */       if (localTextComponentPeer != null) {
/*  212 */         this.text = localTextComponentPeer.getText();
/*  213 */         this.selectionStart = localTextComponentPeer.getSelectionStart();
/*  214 */         this.selectionEnd = localTextComponentPeer.getSelectionEnd();
/*      */       }
/*  216 */       super.removeNotify();
/*      */     }
/*      */   }
/*      */ 
/*      */   public synchronized void setText(String paramString)
/*      */   {
/*  229 */     int i = ((this.text == null) || (this.text.isEmpty())) && ((paramString == null) || (paramString.isEmpty())) ? 1 : 0;
/*      */ 
/*  231 */     this.text = (paramString != null ? paramString : "");
/*  232 */     TextComponentPeer localTextComponentPeer = (TextComponentPeer)this.peer;
/*      */ 
/*  236 */     if ((localTextComponentPeer != null) && (i == 0))
/*  237 */       localTextComponentPeer.setText(this.text);
/*      */   }
/*      */ 
/*      */   public synchronized String getText()
/*      */   {
/*  249 */     TextComponentPeer localTextComponentPeer = (TextComponentPeer)this.peer;
/*  250 */     if (localTextComponentPeer != null) {
/*  251 */       this.text = localTextComponentPeer.getText();
/*      */     }
/*  253 */     return this.text;
/*      */   }
/*      */ 
/*      */   public synchronized String getSelectedText()
/*      */   {
/*  263 */     return getText().substring(getSelectionStart(), getSelectionEnd());
/*      */   }
/*      */ 
/*      */   public boolean isEditable()
/*      */   {
/*  274 */     return this.editable;
/*      */   }
/*      */ 
/*      */   public synchronized void setEditable(boolean paramBoolean)
/*      */   {
/*  294 */     if (this.editable == paramBoolean) {
/*  295 */       return;
/*      */     }
/*      */ 
/*  298 */     this.editable = paramBoolean;
/*  299 */     TextComponentPeer localTextComponentPeer = (TextComponentPeer)this.peer;
/*  300 */     if (localTextComponentPeer != null)
/*  301 */       localTextComponentPeer.setEditable(paramBoolean);
/*      */   }
/*      */ 
/*      */   public Color getBackground()
/*      */   {
/*  319 */     if ((!this.editable) && (!this.backgroundSetByClientCode)) {
/*  320 */       return SystemColor.control;
/*      */     }
/*      */ 
/*  323 */     return super.getBackground();
/*      */   }
/*      */ 
/*      */   public void setBackground(Color paramColor)
/*      */   {
/*  336 */     this.backgroundSetByClientCode = true;
/*  337 */     super.setBackground(paramColor);
/*      */   }
/*      */ 
/*      */   public synchronized int getSelectionStart()
/*      */   {
/*  348 */     TextComponentPeer localTextComponentPeer = (TextComponentPeer)this.peer;
/*  349 */     if (localTextComponentPeer != null) {
/*  350 */       this.selectionStart = localTextComponentPeer.getSelectionStart();
/*      */     }
/*  352 */     return this.selectionStart;
/*      */   }
/*      */ 
/*      */   public synchronized void setSelectionStart(int paramInt)
/*      */   {
/*  374 */     select(paramInt, getSelectionEnd());
/*      */   }
/*      */ 
/*      */   public synchronized int getSelectionEnd()
/*      */   {
/*  385 */     TextComponentPeer localTextComponentPeer = (TextComponentPeer)this.peer;
/*  386 */     if (localTextComponentPeer != null) {
/*  387 */       this.selectionEnd = localTextComponentPeer.getSelectionEnd();
/*      */     }
/*  389 */     return this.selectionEnd;
/*      */   }
/*      */ 
/*      */   public synchronized void setSelectionEnd(int paramInt)
/*      */   {
/*  410 */     select(getSelectionStart(), paramInt);
/*      */   }
/*      */ 
/*      */   public synchronized void select(int paramInt1, int paramInt2)
/*      */   {
/*  446 */     String str = getText();
/*  447 */     if (paramInt1 < 0) {
/*  448 */       paramInt1 = 0;
/*      */     }
/*  450 */     if (paramInt1 > str.length()) {
/*  451 */       paramInt1 = str.length();
/*      */     }
/*  453 */     if (paramInt2 > str.length()) {
/*  454 */       paramInt2 = str.length();
/*      */     }
/*  456 */     if (paramInt2 < paramInt1) {
/*  457 */       paramInt2 = paramInt1;
/*      */     }
/*      */ 
/*  460 */     this.selectionStart = paramInt1;
/*  461 */     this.selectionEnd = paramInt2;
/*      */ 
/*  463 */     TextComponentPeer localTextComponentPeer = (TextComponentPeer)this.peer;
/*  464 */     if (localTextComponentPeer != null)
/*  465 */       localTextComponentPeer.select(paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   public synchronized void selectAll()
/*      */   {
/*  474 */     this.selectionStart = 0;
/*  475 */     this.selectionEnd = getText().length();
/*      */ 
/*  477 */     TextComponentPeer localTextComponentPeer = (TextComponentPeer)this.peer;
/*  478 */     if (localTextComponentPeer != null)
/*  479 */       localTextComponentPeer.select(this.selectionStart, this.selectionEnd);
/*      */   }
/*      */ 
/*      */   public synchronized void setCaretPosition(int paramInt)
/*      */   {
/*  500 */     if (paramInt < 0) {
/*  501 */       throw new IllegalArgumentException("position less than zero.");
/*      */     }
/*      */ 
/*  504 */     int i = getText().length();
/*  505 */     if (paramInt > i) {
/*  506 */       paramInt = i;
/*      */     }
/*      */ 
/*  509 */     TextComponentPeer localTextComponentPeer = (TextComponentPeer)this.peer;
/*  510 */     if (localTextComponentPeer != null)
/*  511 */       localTextComponentPeer.setCaretPosition(paramInt);
/*      */     else
/*  513 */       select(paramInt, paramInt);
/*      */   }
/*      */ 
/*      */   public synchronized int getCaretPosition()
/*      */   {
/*  529 */     TextComponentPeer localTextComponentPeer = (TextComponentPeer)this.peer;
/*  530 */     int i = 0;
/*      */ 
/*  532 */     if (localTextComponentPeer != null)
/*  533 */       i = localTextComponentPeer.getCaretPosition();
/*      */     else {
/*  535 */       i = this.selectionStart;
/*      */     }
/*  537 */     int j = getText().length();
/*  538 */     if (i > j) {
/*  539 */       i = j;
/*      */     }
/*  541 */     return i;
/*      */   }
/*      */ 
/*      */   public synchronized void addTextListener(TextListener paramTextListener)
/*      */   {
/*  558 */     if (paramTextListener == null) {
/*  559 */       return;
/*      */     }
/*  561 */     this.textListener = AWTEventMulticaster.add(this.textListener, paramTextListener);
/*  562 */     this.newEventsOnly = true;
/*      */   }
/*      */ 
/*      */   public synchronized void removeTextListener(TextListener paramTextListener)
/*      */   {
/*  580 */     if (paramTextListener == null) {
/*  581 */       return;
/*      */     }
/*  583 */     this.textListener = AWTEventMulticaster.remove(this.textListener, paramTextListener);
/*      */   }
/*      */ 
/*      */   public synchronized TextListener[] getTextListeners()
/*      */   {
/*  600 */     return (TextListener[])getListeners(TextListener.class);
/*      */   }
/*      */ 
/*      */   public <T extends EventListener> T[] getListeners(Class<T> paramClass)
/*      */   {
/*  637 */     TextListener localTextListener = null;
/*  638 */     if (paramClass == TextListener.class)
/*  639 */       localTextListener = this.textListener;
/*      */     else {
/*  641 */       return super.getListeners(paramClass);
/*      */     }
/*  643 */     return AWTEventMulticaster.getListeners(localTextListener, paramClass);
/*      */   }
/*      */ 
/*      */   boolean eventEnabled(AWTEvent paramAWTEvent)
/*      */   {
/*  648 */     if (paramAWTEvent.id == 900) {
/*  649 */       if (((this.eventMask & 0x400) != 0L) || (this.textListener != null))
/*      */       {
/*  651 */         return true;
/*      */       }
/*  653 */       return false;
/*      */     }
/*  655 */     return super.eventEnabled(paramAWTEvent);
/*      */   }
/*      */ 
/*      */   protected void processEvent(AWTEvent paramAWTEvent)
/*      */   {
/*  669 */     if ((paramAWTEvent instanceof TextEvent)) {
/*  670 */       processTextEvent((TextEvent)paramAWTEvent);
/*  671 */       return;
/*      */     }
/*  673 */     super.processEvent(paramAWTEvent);
/*      */   }
/*      */ 
/*      */   protected void processTextEvent(TextEvent paramTextEvent)
/*      */   {
/*  696 */     TextListener localTextListener = this.textListener;
/*  697 */     if (localTextListener != null) {
/*  698 */       int i = paramTextEvent.getID();
/*  699 */       switch (i) {
/*      */       case 900:
/*  701 */         localTextListener.textValueChanged(paramTextEvent);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected String paramString()
/*      */   {
/*  718 */     String str = super.paramString() + ",text=" + getText();
/*  719 */     if (this.editable) {
/*  720 */       str = str + ",editable";
/*      */     }
/*  722 */     return str + ",selection=" + getSelectionStart() + "-" + getSelectionEnd();
/*      */   }
/*      */ 
/*      */   private boolean canAccessClipboard()
/*      */   {
/*  729 */     SecurityManager localSecurityManager = System.getSecurityManager();
/*  730 */     if (localSecurityManager == null) return true; try
/*      */     {
/*  732 */       localSecurityManager.checkSystemClipboardAccess();
/*  733 */       return true; } catch (SecurityException localSecurityException) {
/*      */     }
/*  735 */     return false;
/*      */   }
/*      */ 
/*      */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*      */     throws IOException
/*      */   {
/*  769 */     TextComponentPeer localTextComponentPeer = (TextComponentPeer)this.peer;
/*  770 */     if (localTextComponentPeer != null) {
/*  771 */       this.text = localTextComponentPeer.getText();
/*  772 */       this.selectionStart = localTextComponentPeer.getSelectionStart();
/*  773 */       this.selectionEnd = localTextComponentPeer.getSelectionEnd();
/*      */     }
/*      */ 
/*  776 */     paramObjectOutputStream.defaultWriteObject();
/*      */ 
/*  778 */     AWTEventMulticaster.save(paramObjectOutputStream, "textL", this.textListener);
/*  779 */     paramObjectOutputStream.writeObject(null);
/*      */   }
/*      */ 
/*      */   private void readObject(ObjectInputStream paramObjectInputStream)
/*      */     throws ClassNotFoundException, IOException, HeadlessException
/*      */   {
/*  798 */     GraphicsEnvironment.checkHeadless();
/*  799 */     paramObjectInputStream.defaultReadObject();
/*      */ 
/*  803 */     this.text = (this.text != null ? this.text : "");
/*  804 */     select(this.selectionStart, this.selectionEnd);
/*      */     Object localObject;
/*  807 */     while (null != (localObject = paramObjectInputStream.readObject())) {
/*  808 */       String str = ((String)localObject).intern();
/*      */ 
/*  810 */       if ("textL" == str) {
/*  811 */         addTextListener((TextListener)paramObjectInputStream.readObject());
/*      */       }
/*      */       else {
/*  814 */         paramObjectInputStream.readObject();
/*      */       }
/*      */     }
/*  817 */     enableInputMethodsIfNecessary();
/*      */   }
/*      */ 
/*      */   int getIndexAtPoint(Point paramPoint)
/*      */   {
/*  830 */     return -1;
/*      */   }
/*      */ 
/*      */   Rectangle getCharacterBounds(int paramInt)
/*      */   {
/*  845 */     return null;
/*      */   }
/*      */ 
/*      */   public AccessibleContext getAccessibleContext()
/*      */   {
/*  867 */     if (this.accessibleContext == null) {
/*  868 */       this.accessibleContext = new AccessibleAWTTextComponent();
/*      */     }
/*  870 */     return this.accessibleContext;
/*      */   }
/*      */ 
/*      */   protected class AccessibleAWTTextComponent extends Component.AccessibleAWTComponent
/*      */     implements AccessibleText, TextListener
/*      */   {
/*      */     private static final long serialVersionUID = 3631432373506317811L;
/*      */     private static final boolean NEXT = true;
/*      */     private static final boolean PREVIOUS = false;
/*      */ 
/*      */     public AccessibleAWTTextComponent()
/*      */     {
/*  892 */       super();
/*  893 */       TextComponent.this.addTextListener(this);
/*      */     }
/*      */ 
/*      */     public void textValueChanged(TextEvent paramTextEvent)
/*      */     {
/*  900 */       Integer localInteger = Integer.valueOf(TextComponent.this.getCaretPosition());
/*  901 */       firePropertyChange("AccessibleText", null, localInteger);
/*      */     }
/*      */ 
/*      */     public AccessibleStateSet getAccessibleStateSet()
/*      */     {
/*  918 */       AccessibleStateSet localAccessibleStateSet = super.getAccessibleStateSet();
/*  919 */       if (TextComponent.this.isEditable()) {
/*  920 */         localAccessibleStateSet.add(AccessibleState.EDITABLE);
/*      */       }
/*  922 */       return localAccessibleStateSet;
/*      */     }
/*      */ 
/*      */     public AccessibleRole getAccessibleRole()
/*      */     {
/*  934 */       return AccessibleRole.TEXT;
/*      */     }
/*      */ 
/*      */     public AccessibleText getAccessibleText()
/*      */     {
/*  946 */       return this;
/*      */     }
/*      */ 
/*      */     public int getIndexAtPoint(Point paramPoint)
/*      */     {
/*  966 */       return TextComponent.this.getIndexAtPoint(paramPoint);
/*      */     }
/*      */ 
/*      */     public Rectangle getCharacterBounds(int paramInt)
/*      */     {
/*  979 */       return TextComponent.this.getCharacterBounds(paramInt);
/*      */     }
/*      */ 
/*      */     public int getCharCount()
/*      */     {
/*  988 */       return TextComponent.this.getText().length();
/*      */     }
/*      */ 
/*      */     public int getCaretPosition()
/*      */     {
/* 1001 */       return TextComponent.this.getCaretPosition();
/*      */     }
/*      */ 
/*      */     public AttributeSet getCharacterAttribute(int paramInt)
/*      */     {
/* 1011 */       return null;
/*      */     }
/*      */ 
/*      */     public int getSelectionStart()
/*      */     {
/* 1024 */       return TextComponent.this.getSelectionStart();
/*      */     }
/*      */ 
/*      */     public int getSelectionEnd()
/*      */     {
/* 1037 */       return TextComponent.this.getSelectionEnd();
/*      */     }
/*      */ 
/*      */     public String getSelectedText()
/*      */     {
/* 1046 */       String str = TextComponent.this.getSelectedText();
/*      */ 
/* 1048 */       if ((str == null) || (str.equals(""))) {
/* 1049 */         return null;
/*      */       }
/* 1051 */       return str;
/*      */     }
/*      */ 
/*      */     public String getAtIndex(int paramInt1, int paramInt2)
/*      */     {
/* 1064 */       if ((paramInt2 < 0) || (paramInt2 >= TextComponent.this.getText().length()))
/* 1065 */         return null;
/*      */       String str;
/*      */       BreakIterator localBreakIterator;
/*      */       int i;
/* 1067 */       switch (paramInt1) {
/*      */       case 1:
/* 1069 */         return TextComponent.this.getText().substring(paramInt2, paramInt2 + 1);
/*      */       case 2:
/* 1071 */         str = TextComponent.this.getText();
/* 1072 */         localBreakIterator = BreakIterator.getWordInstance();
/* 1073 */         localBreakIterator.setText(str);
/* 1074 */         i = localBreakIterator.following(paramInt2);
/* 1075 */         return str.substring(localBreakIterator.previous(), i);
/*      */       case 3:
/* 1078 */         str = TextComponent.this.getText();
/* 1079 */         localBreakIterator = BreakIterator.getSentenceInstance();
/* 1080 */         localBreakIterator.setText(str);
/* 1081 */         i = localBreakIterator.following(paramInt2);
/* 1082 */         return str.substring(localBreakIterator.previous(), i);
/*      */       }
/*      */ 
/* 1085 */       return null;
/*      */     }
/*      */ 
/*      */     private int findWordLimit(int paramInt, BreakIterator paramBreakIterator, boolean paramBoolean, String paramString)
/*      */     {
/* 1103 */       int i = paramBoolean == true ? paramBreakIterator.following(paramInt) : paramBreakIterator.preceding(paramInt);
/*      */ 
/* 1105 */       int j = paramBoolean == true ? paramBreakIterator.next() : paramBreakIterator.previous();
/*      */ 
/* 1107 */       while (j != -1) {
/* 1108 */         for (int k = Math.min(i, j); k < Math.max(i, j); k++) {
/* 1109 */           if (Character.isLetter(paramString.charAt(k))) {
/* 1110 */             return i;
/*      */           }
/*      */         }
/* 1113 */         i = j;
/* 1114 */         j = paramBoolean == true ? paramBreakIterator.next() : paramBreakIterator.previous();
/*      */       }
/*      */ 
/* 1117 */       return -1;
/*      */     }
/*      */ 
/*      */     public String getAfterIndex(int paramInt1, int paramInt2)
/*      */     {
/* 1130 */       if ((paramInt2 < 0) || (paramInt2 >= TextComponent.this.getText().length()))
/* 1131 */         return null;
/*      */       String str;
/*      */       BreakIterator localBreakIterator;
/*      */       int i;
/*      */       int j;
/* 1133 */       switch (paramInt1) {
/*      */       case 1:
/* 1135 */         if (paramInt2 + 1 >= TextComponent.this.getText().length()) {
/* 1136 */           return null;
/*      */         }
/* 1138 */         return TextComponent.this.getText().substring(paramInt2 + 1, paramInt2 + 2);
/*      */       case 2:
/* 1140 */         str = TextComponent.this.getText();
/* 1141 */         localBreakIterator = BreakIterator.getWordInstance();
/* 1142 */         localBreakIterator.setText(str);
/* 1143 */         i = findWordLimit(paramInt2, localBreakIterator, true, str);
/* 1144 */         if ((i == -1) || (i >= str.length())) {
/* 1145 */           return null;
/*      */         }
/* 1147 */         j = localBreakIterator.following(i);
/* 1148 */         if ((j == -1) || (j >= str.length())) {
/* 1149 */           return null;
/*      */         }
/* 1151 */         return str.substring(i, j);
/*      */       case 3:
/* 1154 */         str = TextComponent.this.getText();
/* 1155 */         localBreakIterator = BreakIterator.getSentenceInstance();
/* 1156 */         localBreakIterator.setText(str);
/* 1157 */         i = localBreakIterator.following(paramInt2);
/* 1158 */         if ((i == -1) || (i >= str.length())) {
/* 1159 */           return null;
/*      */         }
/* 1161 */         j = localBreakIterator.following(i);
/* 1162 */         if ((j == -1) || (j >= str.length())) {
/* 1163 */           return null;
/*      */         }
/* 1165 */         return str.substring(i, j);
/*      */       }
/*      */ 
/* 1168 */       return null;
/*      */     }
/*      */ 
/*      */     public String getBeforeIndex(int paramInt1, int paramInt2)
/*      */     {
/* 1183 */       if ((paramInt2 < 0) || (paramInt2 > TextComponent.this.getText().length() - 1))
/* 1184 */         return null;
/*      */       String str;
/*      */       BreakIterator localBreakIterator;
/*      */       int i;
/*      */       int j;
/* 1186 */       switch (paramInt1) {
/*      */       case 1:
/* 1188 */         if (paramInt2 == 0) {
/* 1189 */           return null;
/*      */         }
/* 1191 */         return TextComponent.this.getText().substring(paramInt2 - 1, paramInt2);
/*      */       case 2:
/* 1193 */         str = TextComponent.this.getText();
/* 1194 */         localBreakIterator = BreakIterator.getWordInstance();
/* 1195 */         localBreakIterator.setText(str);
/* 1196 */         i = findWordLimit(paramInt2, localBreakIterator, false, str);
/* 1197 */         if (i == -1) {
/* 1198 */           return null;
/*      */         }
/* 1200 */         j = localBreakIterator.preceding(i);
/* 1201 */         if (j == -1) {
/* 1202 */           return null;
/*      */         }
/* 1204 */         return str.substring(j, i);
/*      */       case 3:
/* 1207 */         str = TextComponent.this.getText();
/* 1208 */         localBreakIterator = BreakIterator.getSentenceInstance();
/* 1209 */         localBreakIterator.setText(str);
/* 1210 */         i = localBreakIterator.following(paramInt2);
/* 1211 */         i = localBreakIterator.previous();
/* 1212 */         j = localBreakIterator.previous();
/* 1213 */         if (j == -1) {
/* 1214 */           return null;
/*      */         }
/* 1216 */         return str.substring(j, i);
/*      */       }
/*      */ 
/* 1219 */       return null;
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.TextComponent
 * JD-Core Version:    0.6.2
 */