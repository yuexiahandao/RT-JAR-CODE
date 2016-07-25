/*     */ package javax.swing;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.util.Arrays;
/*     */ import javax.accessibility.AccessibleContext;
/*     */ import javax.accessibility.AccessibleRole;
/*     */ import javax.accessibility.AccessibleText;
/*     */ import javax.accessibility.AccessibleTextSequence;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.text.BadLocationException;
/*     */ import javax.swing.text.Document;
/*     */ import javax.swing.text.Segment;
/*     */ 
/*     */ public class JPasswordField extends JTextField
/*     */ {
/*     */   private static final String uiClassID = "PasswordFieldUI";
/*     */   private char echoChar;
/* 331 */   private boolean echoCharSet = false;
/*     */ 
/*     */   public JPasswordField()
/*     */   {
/*  85 */     this(null, null, 0);
/*     */   }
/*     */ 
/*     */   public JPasswordField(String paramString)
/*     */   {
/*  96 */     this(null, paramString, 0);
/*     */   }
/*     */ 
/*     */   public JPasswordField(int paramInt)
/*     */   {
/* 107 */     this(null, null, paramInt);
/*     */   }
/*     */ 
/*     */   public JPasswordField(String paramString, int paramInt)
/*     */   {
/* 119 */     this(null, paramString, paramInt);
/*     */   }
/*     */ 
/*     */   public JPasswordField(Document paramDocument, String paramString, int paramInt)
/*     */   {
/* 138 */     super(paramDocument, paramString, paramInt);
/*     */ 
/* 142 */     enableInputMethods(false);
/*     */   }
/*     */ 
/*     */   public String getUIClassID()
/*     */   {
/* 153 */     return "PasswordFieldUI";
/*     */   }
/*     */ 
/*     */   public void updateUI()
/*     */   {
/* 162 */     if (!this.echoCharSet) {
/* 163 */       this.echoChar = '*';
/*     */     }
/* 165 */     super.updateUI();
/*     */   }
/*     */ 
/*     */   public char getEchoChar()
/*     */   {
/* 178 */     return this.echoChar;
/*     */   }
/*     */ 
/*     */   public void setEchoChar(char paramChar)
/*     */   {
/* 197 */     this.echoChar = paramChar;
/* 198 */     this.echoCharSet = true;
/* 199 */     repaint();
/* 200 */     revalidate();
/*     */   }
/*     */ 
/*     */   public boolean echoCharIsSet()
/*     */   {
/* 213 */     return this.echoChar != 0;
/*     */   }
/*     */ 
/*     */   public void cut()
/*     */   {
/* 227 */     if (getClientProperty("JPasswordField.cutCopyAllowed") != Boolean.TRUE)
/* 228 */       UIManager.getLookAndFeel().provideErrorFeedback(this);
/*     */     else
/* 230 */       super.cut();
/*     */   }
/*     */ 
/*     */   public void copy()
/*     */   {
/* 243 */     if (getClientProperty("JPasswordField.cutCopyAllowed") != Boolean.TRUE)
/* 244 */       UIManager.getLookAndFeel().provideErrorFeedback(this);
/*     */     else
/* 246 */       super.copy();
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public String getText()
/*     */   {
/* 263 */     return super.getText();
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public String getText(int paramInt1, int paramInt2)
/*     */     throws BadLocationException
/*     */   {
/* 281 */     return super.getText(paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public char[] getPassword()
/*     */   {
/* 294 */     Document localDocument = getDocument();
/* 295 */     Segment localSegment = new Segment();
/*     */     try {
/* 297 */       localDocument.getText(0, localDocument.getLength(), localSegment);
/*     */     } catch (BadLocationException localBadLocationException) {
/* 299 */       return null;
/*     */     }
/* 301 */     char[] arrayOfChar = new char[localSegment.count];
/* 302 */     System.arraycopy(localSegment.array, localSegment.offset, arrayOfChar, 0, localSegment.count);
/* 303 */     return arrayOfChar;
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*     */     throws IOException
/*     */   {
/* 311 */     paramObjectOutputStream.defaultWriteObject();
/* 312 */     if (getUIClassID().equals("PasswordFieldUI")) {
/* 313 */       byte b = JComponent.getWriteObjCounter(this);
/* 314 */       b = (byte)(b - 1); JComponent.setWriteObjCounter(this, b);
/* 315 */       if ((b == 0) && (this.ui != null))
/* 316 */         this.ui.installUI(this);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected String paramString()
/*     */   {
/* 344 */     return super.paramString() + ",echoChar=" + this.echoChar;
/*     */   }
/*     */ 
/*     */   boolean customSetUIProperty(String paramString, Object paramObject)
/*     */   {
/* 357 */     if (paramString == "echoChar") {
/* 358 */       if (!this.echoCharSet) {
/* 359 */         setEchoChar(((Character)paramObject).charValue());
/* 360 */         this.echoCharSet = false;
/*     */       }
/* 362 */       return true;
/*     */     }
/* 364 */     return false;
/*     */   }
/*     */ 
/*     */   public AccessibleContext getAccessibleContext()
/*     */   {
/* 385 */     if (this.accessibleContext == null) {
/* 386 */       this.accessibleContext = new AccessibleJPasswordField();
/*     */     }
/* 388 */     return this.accessibleContext;
/*     */   }
/*     */ 
/*     */   protected class AccessibleJPasswordField extends JTextField.AccessibleJTextField
/*     */   {
/*     */     protected AccessibleJPasswordField()
/*     */     {
/* 406 */       super();
/*     */     }
/*     */ 
/*     */     public AccessibleRole getAccessibleRole()
/*     */     {
/* 416 */       return AccessibleRole.PASSWORD_TEXT;
/*     */     }
/*     */ 
/*     */     public AccessibleText getAccessibleText()
/*     */     {
/* 433 */       return this;
/*     */     }
/*     */ 
/*     */     private String getEchoString(String paramString)
/*     */     {
/* 442 */       if (paramString == null) {
/* 443 */         return null;
/*     */       }
/* 445 */       char[] arrayOfChar = new char[paramString.length()];
/* 446 */       Arrays.fill(arrayOfChar, JPasswordField.this.getEchoChar());
/* 447 */       return new String(arrayOfChar);
/*     */     }
/*     */ 
/*     */     public String getAtIndex(int paramInt1, int paramInt2)
/*     */     {
/* 467 */       String str = null;
/* 468 */       if (paramInt1 == 1) {
/* 469 */         str = super.getAtIndex(paramInt1, paramInt2);
/*     */       }
/*     */       else
/*     */       {
/* 473 */         char[] arrayOfChar = JPasswordField.this.getPassword();
/* 474 */         if ((arrayOfChar == null) || (paramInt2 < 0) || (paramInt2 >= arrayOfChar.length))
/*     */         {
/* 476 */           return null;
/*     */         }
/* 478 */         str = new String(arrayOfChar);
/*     */       }
/* 480 */       return getEchoString(str);
/*     */     }
/*     */ 
/*     */     public String getAfterIndex(int paramInt1, int paramInt2)
/*     */     {
/* 500 */       if (paramInt1 == 1) {
/* 501 */         String str = super.getAfterIndex(paramInt1, paramInt2);
/* 502 */         return getEchoString(str);
/*     */       }
/*     */ 
/* 506 */       return null;
/*     */     }
/*     */ 
/*     */     public String getBeforeIndex(int paramInt1, int paramInt2)
/*     */     {
/* 527 */       if (paramInt1 == 1) {
/* 528 */         String str = super.getBeforeIndex(paramInt1, paramInt2);
/* 529 */         return getEchoString(str);
/*     */       }
/*     */ 
/* 533 */       return null;
/*     */     }
/*     */ 
/*     */     public String getTextRange(int paramInt1, int paramInt2)
/*     */     {
/* 548 */       String str = super.getTextRange(paramInt1, paramInt2);
/* 549 */       return getEchoString(str);
/*     */     }
/*     */ 
/*     */     public AccessibleTextSequence getTextSequenceAt(int paramInt1, int paramInt2)
/*     */     {
/* 574 */       if (paramInt1 == 1) {
/* 575 */         localObject = super.getTextSequenceAt(paramInt1, paramInt2);
/* 576 */         if (localObject == null) {
/* 577 */           return null;
/*     */         }
/* 579 */         return new AccessibleTextSequence(((AccessibleTextSequence)localObject).startIndex, ((AccessibleTextSequence)localObject).endIndex, getEchoString(((AccessibleTextSequence)localObject).text));
/*     */       }
/*     */ 
/* 584 */       Object localObject = JPasswordField.this.getPassword();
/* 585 */       if ((localObject == null) || (paramInt2 < 0) || (paramInt2 >= localObject.length))
/*     */       {
/* 587 */         return null;
/*     */       }
/* 589 */       String str = new String((char[])localObject);
/* 590 */       return new AccessibleTextSequence(0, localObject.length - 1, getEchoString(str));
/*     */     }
/*     */ 
/*     */     public AccessibleTextSequence getTextSequenceAfter(int paramInt1, int paramInt2)
/*     */     {
/* 616 */       if (paramInt1 == 1) {
/* 617 */         AccessibleTextSequence localAccessibleTextSequence = super.getTextSequenceAfter(paramInt1, paramInt2);
/* 618 */         if (localAccessibleTextSequence == null) {
/* 619 */           return null;
/*     */         }
/* 621 */         return new AccessibleTextSequence(localAccessibleTextSequence.startIndex, localAccessibleTextSequence.endIndex, getEchoString(localAccessibleTextSequence.text));
/*     */       }
/*     */ 
/* 626 */       return null;
/*     */     }
/*     */ 
/*     */     public AccessibleTextSequence getTextSequenceBefore(int paramInt1, int paramInt2)
/*     */     {
/* 651 */       if (paramInt1 == 1) {
/* 652 */         AccessibleTextSequence localAccessibleTextSequence = super.getTextSequenceBefore(paramInt1, paramInt2);
/* 653 */         if (localAccessibleTextSequence == null) {
/* 654 */           return null;
/*     */         }
/* 656 */         return new AccessibleTextSequence(localAccessibleTextSequence.startIndex, localAccessibleTextSequence.endIndex, getEchoString(localAccessibleTextSequence.text));
/*     */       }
/*     */ 
/* 661 */       return null;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.JPasswordField
 * JD-Core Version:    0.6.2
 */