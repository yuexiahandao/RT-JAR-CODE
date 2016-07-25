/*     */ package javax.swing.text;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.text.ParseException;
/*     */ import javax.swing.JFormattedTextField;
/*     */ import javax.swing.JFormattedTextField.AbstractFormatter;
/*     */ import javax.swing.plaf.TextUI;
/*     */ import sun.reflect.misc.ReflectUtil;
/*     */ import sun.swing.SwingUtilities2;
/*     */ 
/*     */ public class DefaultFormatter extends JFormattedTextField.AbstractFormatter
/*     */   implements Cloneable, Serializable
/*     */ {
/*     */   private boolean allowsInvalid;
/*     */   private boolean overwriteMode;
/*     */   private boolean commitOnEdit;
/*     */   private Class<?> valueClass;
/*     */   private NavigationFilter navigationFilter;
/*     */   private DocumentFilter documentFilter;
/*     */   transient ReplaceHolder replaceHolder;
/*     */ 
/*     */   public DefaultFormatter()
/*     */   {
/*  90 */     this.overwriteMode = true;
/*  91 */     this.allowsInvalid = true;
/*     */   }
/*     */ 
/*     */   public void install(JFormattedTextField paramJFormattedTextField)
/*     */   {
/* 125 */     super.install(paramJFormattedTextField);
/* 126 */     positionCursorAtInitialLocation();
/*     */   }
/*     */ 
/*     */   public void setCommitsOnValidEdit(boolean paramBoolean)
/*     */   {
/* 144 */     this.commitOnEdit = paramBoolean;
/*     */   }
/*     */ 
/*     */   public boolean getCommitsOnValidEdit()
/*     */   {
/* 154 */     return this.commitOnEdit;
/*     */   }
/*     */ 
/*     */   public void setOverwriteMode(boolean paramBoolean)
/*     */   {
/* 165 */     this.overwriteMode = paramBoolean;
/*     */   }
/*     */ 
/*     */   public boolean getOverwriteMode()
/*     */   {
/* 174 */     return this.overwriteMode;
/*     */   }
/*     */ 
/*     */   public void setAllowsInvalid(boolean paramBoolean)
/*     */   {
/* 188 */     this.allowsInvalid = paramBoolean;
/*     */   }
/*     */ 
/*     */   public boolean getAllowsInvalid()
/*     */   {
/* 198 */     return this.allowsInvalid;
/*     */   }
/*     */ 
/*     */   public void setValueClass(Class<?> paramClass)
/*     */   {
/* 210 */     this.valueClass = paramClass;
/*     */   }
/*     */ 
/*     */   public Class<?> getValueClass()
/*     */   {
/* 219 */     return this.valueClass;
/*     */   }
/*     */ 
/*     */   public Object stringToValue(String paramString)
/*     */     throws ParseException
/*     */   {
/* 237 */     Class localClass = getValueClass();
/* 238 */     JFormattedTextField localJFormattedTextField = getFormattedTextField();
/*     */     Object localObject;
/* 240 */     if ((localClass == null) && (localJFormattedTextField != null)) {
/* 241 */       localObject = localJFormattedTextField.getValue();
/*     */ 
/* 243 */       if (localObject != null) {
/* 244 */         localClass = localObject.getClass();
/*     */       }
/*     */     }
/* 247 */     if (localClass != null)
/*     */     {
/*     */       try
/*     */       {
/* 251 */         ReflectUtil.checkPackageAccess(localClass);
/* 252 */         SwingUtilities2.checkAccess(localClass.getModifiers());
/* 253 */         localObject = localClass.getConstructor(new Class[] { String.class });
/*     */       }
/*     */       catch (NoSuchMethodException localNoSuchMethodException) {
/* 256 */         localObject = null;
/*     */       }
/*     */ 
/* 259 */       if (localObject != null) {
/*     */         try {
/* 261 */           SwingUtilities2.checkAccess(((Constructor)localObject).getModifiers());
/* 262 */           return ((Constructor)localObject).newInstance(new Object[] { paramString });
/*     */         } catch (Throwable localThrowable) {
/* 264 */           throw new ParseException("Error creating instance", 0);
/*     */         }
/*     */       }
/*     */     }
/* 268 */     return paramString;
/*     */   }
/*     */ 
/*     */   public String valueToString(Object paramObject)
/*     */     throws ParseException
/*     */   {
/* 280 */     if (paramObject == null) {
/* 281 */       return "";
/*     */     }
/* 283 */     return paramObject.toString();
/*     */   }
/*     */ 
/*     */   protected DocumentFilter getDocumentFilter()
/*     */   {
/* 293 */     if (this.documentFilter == null) {
/* 294 */       this.documentFilter = new DefaultDocumentFilter(null);
/*     */     }
/* 296 */     return this.documentFilter;
/*     */   }
/*     */ 
/*     */   protected NavigationFilter getNavigationFilter()
/*     */   {
/* 306 */     if (this.navigationFilter == null) {
/* 307 */       this.navigationFilter = new DefaultNavigationFilter(null);
/*     */     }
/* 309 */     return this.navigationFilter;
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */     throws CloneNotSupportedException
/*     */   {
/* 318 */     DefaultFormatter localDefaultFormatter = (DefaultFormatter)super.clone();
/*     */ 
/* 320 */     localDefaultFormatter.navigationFilter = null;
/* 321 */     localDefaultFormatter.documentFilter = null;
/* 322 */     localDefaultFormatter.replaceHolder = null;
/* 323 */     return localDefaultFormatter;
/*     */   }
/*     */ 
/*     */   void positionCursorAtInitialLocation()
/*     */   {
/* 331 */     JFormattedTextField localJFormattedTextField = getFormattedTextField();
/* 332 */     if (localJFormattedTextField != null)
/* 333 */       localJFormattedTextField.setCaretPosition(getInitialVisualPosition());
/*     */   }
/*     */ 
/*     */   int getInitialVisualPosition()
/*     */   {
/* 342 */     return getNextNavigatableChar(0, 1);
/*     */   }
/*     */ 
/*     */   boolean isNavigatable(int paramInt)
/*     */   {
/* 352 */     return true;
/*     */   }
/*     */ 
/*     */   boolean isLegalInsertText(String paramString)
/*     */   {
/* 361 */     return true;
/*     */   }
/*     */ 
/*     */   private int getNextNavigatableChar(int paramInt1, int paramInt2)
/*     */   {
/* 369 */     int i = getFormattedTextField().getDocument().getLength();
/*     */ 
/* 371 */     while ((paramInt1 >= 0) && (paramInt1 < i)) {
/* 372 */       if (isNavigatable(paramInt1)) {
/* 373 */         return paramInt1;
/*     */       }
/* 375 */       paramInt1 += paramInt2;
/*     */     }
/* 377 */     return paramInt1;
/*     */   }
/*     */ 
/*     */   String getReplaceString(int paramInt1, int paramInt2, String paramString)
/*     */   {
/* 388 */     String str1 = getFormattedTextField().getText();
/*     */ 
/* 391 */     String str2 = str1.substring(0, paramInt1);
/* 392 */     if (paramString != null) {
/* 393 */       str2 = str2 + paramString;
/*     */     }
/* 395 */     if (paramInt1 + paramInt2 < str1.length()) {
/* 396 */       str2 = str2 + str1.substring(paramInt1 + paramInt2);
/*     */     }
/* 398 */     return str2;
/*     */   }
/*     */ 
/*     */   boolean isValidEdit(ReplaceHolder paramReplaceHolder)
/*     */   {
/* 407 */     if (!getAllowsInvalid()) {
/* 408 */       String str = getReplaceString(paramReplaceHolder.offset, paramReplaceHolder.length, paramReplaceHolder.text);
/*     */       try
/*     */       {
/* 411 */         paramReplaceHolder.value = stringToValue(str);
/*     */ 
/* 413 */         return true;
/*     */       } catch (ParseException localParseException) {
/* 415 */         return false;
/*     */       }
/*     */     }
/* 418 */     return true;
/*     */   }
/*     */ 
/*     */   void commitEdit()
/*     */     throws ParseException
/*     */   {
/* 425 */     JFormattedTextField localJFormattedTextField = getFormattedTextField();
/*     */ 
/* 427 */     if (localJFormattedTextField != null)
/* 428 */       localJFormattedTextField.commitEdit();
/*     */   }
/*     */ 
/*     */   void updateValue()
/*     */   {
/* 438 */     updateValue(null);
/*     */   }
/*     */ 
/*     */   void updateValue(Object paramObject)
/*     */   {
/*     */     try
/*     */     {
/* 448 */       if (paramObject == null) {
/* 449 */         String str = getFormattedTextField().getText();
/*     */ 
/* 451 */         paramObject = stringToValue(str);
/*     */       }
/*     */ 
/* 454 */       if (getCommitsOnValidEdit()) {
/* 455 */         commitEdit();
/*     */       }
/* 457 */       setEditValid(true);
/*     */     } catch (ParseException localParseException) {
/* 459 */       setEditValid(false);
/*     */     }
/*     */   }
/*     */ 
/*     */   int getNextCursorPosition(int paramInt1, int paramInt2)
/*     */   {
/* 470 */     int i = getNextNavigatableChar(paramInt1, paramInt2);
/* 471 */     int j = getFormattedTextField().getDocument().getLength();
/*     */ 
/* 473 */     if (!getAllowsInvalid()) {
/* 474 */       if ((paramInt2 == -1) && (paramInt1 == i))
/*     */       {
/* 477 */         i = getNextNavigatableChar(i, 1);
/* 478 */         if (i >= j) {
/* 479 */           i = paramInt1;
/*     */         }
/*     */       }
/* 482 */       else if ((paramInt2 == 1) && (i >= j))
/*     */       {
/* 484 */         i = getNextNavigatableChar(j - 1, -1);
/* 485 */         if (i < j) {
/* 486 */           i++;
/*     */         }
/*     */       }
/*     */     }
/* 490 */     return i;
/*     */   }
/*     */ 
/*     */   void repositionCursor(int paramInt1, int paramInt2)
/*     */   {
/* 497 */     getFormattedTextField().getCaret().setDot(getNextCursorPosition(paramInt1, paramInt2));
/*     */   }
/*     */ 
/*     */   int getNextVisualPositionFrom(JTextComponent paramJTextComponent, int paramInt1, Position.Bias paramBias, int paramInt2, Position.Bias[] paramArrayOfBias)
/*     */     throws BadLocationException
/*     */   {
/* 509 */     int i = paramJTextComponent.getUI().getNextVisualPositionFrom(paramJTextComponent, paramInt1, paramBias, paramInt2, paramArrayOfBias);
/*     */ 
/* 512 */     if (i == -1) {
/* 513 */       return -1;
/*     */     }
/* 515 */     if ((!getAllowsInvalid()) && ((paramInt2 == 3) || (paramInt2 == 7)))
/*     */     {
/* 517 */       int j = -1;
/*     */ 
/* 519 */       while ((!isNavigatable(i)) && (i != j)) {
/* 520 */         j = i;
/* 521 */         i = paramJTextComponent.getUI().getNextVisualPositionFrom(paramJTextComponent, i, paramBias, paramInt2, paramArrayOfBias);
/*     */       }
/*     */ 
/* 524 */       int k = getFormattedTextField().getDocument().getLength();
/* 525 */       if ((j == i) || (i == k)) {
/* 526 */         if (i == 0) {
/* 527 */           paramArrayOfBias[0] = Position.Bias.Forward;
/* 528 */           i = getInitialVisualPosition();
/*     */         }
/* 530 */         if ((i >= k) && (k > 0))
/*     */         {
/* 532 */           paramArrayOfBias[0] = Position.Bias.Forward;
/* 533 */           i = getNextNavigatableChar(k - 1, -1) + 1;
/*     */         }
/*     */       }
/*     */     }
/* 537 */     return i;
/*     */   }
/*     */ 
/*     */   boolean canReplace(ReplaceHolder paramReplaceHolder)
/*     */   {
/* 545 */     return isValidEdit(paramReplaceHolder);
/*     */   }
/*     */ 
/*     */   void replace(DocumentFilter.FilterBypass paramFilterBypass, int paramInt1, int paramInt2, String paramString, AttributeSet paramAttributeSet)
/*     */     throws BadLocationException
/*     */   {
/* 554 */     ReplaceHolder localReplaceHolder = getReplaceHolder(paramFilterBypass, paramInt1, paramInt2, paramString, paramAttributeSet);
/*     */ 
/* 556 */     replace(localReplaceHolder);
/*     */   }
/*     */ 
/*     */   boolean replace(ReplaceHolder paramReplaceHolder)
/*     */     throws BadLocationException
/*     */   {
/* 570 */     int i = 1;
/* 571 */     int j = 1;
/*     */ 
/* 573 */     if ((paramReplaceHolder.length > 0) && ((paramReplaceHolder.text == null) || (paramReplaceHolder.text.length() == 0)) && ((getFormattedTextField().getSelectionStart() != paramReplaceHolder.offset) || (paramReplaceHolder.length > 1)))
/*     */     {
/* 576 */       j = -1;
/*     */     }
/*     */ 
/* 579 */     if ((getOverwriteMode()) && (paramReplaceHolder.text != null) && (getFormattedTextField().getSelectedText() == null))
/*     */     {
/* 582 */       paramReplaceHolder.length = Math.min(Math.max(paramReplaceHolder.length, paramReplaceHolder.text.length()), paramReplaceHolder.fb.getDocument().getLength() - paramReplaceHolder.offset);
/*     */     }
/*     */ 
/* 585 */     if (((paramReplaceHolder.text != null) && (!isLegalInsertText(paramReplaceHolder.text))) || (!canReplace(paramReplaceHolder)) || ((paramReplaceHolder.length == 0) && ((paramReplaceHolder.text == null) || (paramReplaceHolder.text.length() == 0))))
/*     */     {
/* 588 */       i = 0;
/*     */     }
/* 590 */     if (i != 0) {
/* 591 */       int k = paramReplaceHolder.cursorPosition;
/*     */ 
/* 593 */       paramReplaceHolder.fb.replace(paramReplaceHolder.offset, paramReplaceHolder.length, paramReplaceHolder.text, paramReplaceHolder.attrs);
/* 594 */       if (k == -1) {
/* 595 */         k = paramReplaceHolder.offset;
/* 596 */         if ((j == 1) && (paramReplaceHolder.text != null)) {
/* 597 */           k = paramReplaceHolder.offset + paramReplaceHolder.text.length();
/*     */         }
/*     */       }
/* 600 */       updateValue(paramReplaceHolder.value);
/* 601 */       repositionCursor(k, j);
/* 602 */       return true;
/*     */     }
/*     */ 
/* 605 */     invalidEdit();
/*     */ 
/* 607 */     return false;
/*     */   }
/*     */ 
/*     */   void setDot(NavigationFilter.FilterBypass paramFilterBypass, int paramInt, Position.Bias paramBias)
/*     */   {
/* 615 */     paramFilterBypass.setDot(paramInt, paramBias);
/*     */   }
/*     */ 
/*     */   void moveDot(NavigationFilter.FilterBypass paramFilterBypass, int paramInt, Position.Bias paramBias)
/*     */   {
/* 624 */     paramFilterBypass.moveDot(paramInt, paramBias);
/*     */   }
/*     */ 
/*     */   ReplaceHolder getReplaceHolder(DocumentFilter.FilterBypass paramFilterBypass, int paramInt1, int paramInt2, String paramString, AttributeSet paramAttributeSet)
/*     */   {
/* 635 */     if (this.replaceHolder == null) {
/* 636 */       this.replaceHolder = new ReplaceHolder();
/*     */     }
/* 638 */     this.replaceHolder.reset(paramFilterBypass, paramInt1, paramInt2, paramString, paramAttributeSet);
/* 639 */     return this.replaceHolder;
/*     */   }
/*     */ 
/*     */   private class DefaultDocumentFilter extends DocumentFilter
/*     */     implements Serializable
/*     */   {
/*     */     private DefaultDocumentFilter()
/*     */     {
/*     */     }
/*     */ 
/*     */     public void remove(DocumentFilter.FilterBypass paramFilterBypass, int paramInt1, int paramInt2)
/*     */       throws BadLocationException
/*     */     {
/* 731 */       JFormattedTextField localJFormattedTextField = DefaultFormatter.this.getFormattedTextField();
/* 732 */       if (localJFormattedTextField.composedTextExists())
/*     */       {
/* 734 */         paramFilterBypass.remove(paramInt1, paramInt2);
/*     */       }
/* 736 */       else DefaultFormatter.this.replace(paramFilterBypass, paramInt1, paramInt2, null, null);
/*     */     }
/*     */ 
/*     */     public void insertString(DocumentFilter.FilterBypass paramFilterBypass, int paramInt, String paramString, AttributeSet paramAttributeSet)
/*     */       throws BadLocationException
/*     */     {
/* 743 */       JFormattedTextField localJFormattedTextField = DefaultFormatter.this.getFormattedTextField();
/* 744 */       if ((localJFormattedTextField.composedTextExists()) || (Utilities.isComposedTextAttributeDefined(paramAttributeSet)))
/*     */       {
/* 747 */         paramFilterBypass.insertString(paramInt, paramString, paramAttributeSet);
/*     */       }
/* 749 */       else DefaultFormatter.this.replace(paramFilterBypass, paramInt, 0, paramString, paramAttributeSet);
/*     */     }
/*     */ 
/*     */     public void replace(DocumentFilter.FilterBypass paramFilterBypass, int paramInt1, int paramInt2, String paramString, AttributeSet paramAttributeSet)
/*     */       throws BadLocationException
/*     */     {
/* 756 */       JFormattedTextField localJFormattedTextField = DefaultFormatter.this.getFormattedTextField();
/* 757 */       if ((localJFormattedTextField.composedTextExists()) || (Utilities.isComposedTextAttributeDefined(paramAttributeSet)))
/*     */       {
/* 760 */         paramFilterBypass.replace(paramInt1, paramInt2, paramString, paramAttributeSet);
/*     */       }
/* 762 */       else DefaultFormatter.this.replace(paramFilterBypass, paramInt1, paramInt2, paramString, paramAttributeSet);
/*     */     }
/*     */   }
/*     */ 
/*     */   private class DefaultNavigationFilter extends NavigationFilter
/*     */     implements Serializable
/*     */   {
/*     */     private DefaultNavigationFilter()
/*     */     {
/*     */     }
/*     */ 
/*     */     public void setDot(NavigationFilter.FilterBypass paramFilterBypass, int paramInt, Position.Bias paramBias)
/*     */     {
/* 687 */       JFormattedTextField localJFormattedTextField = DefaultFormatter.this.getFormattedTextField();
/* 688 */       if (localJFormattedTextField.composedTextExists())
/*     */       {
/* 690 */         paramFilterBypass.setDot(paramInt, paramBias);
/*     */       }
/* 692 */       else DefaultFormatter.this.setDot(paramFilterBypass, paramInt, paramBias);
/*     */     }
/*     */ 
/*     */     public void moveDot(NavigationFilter.FilterBypass paramFilterBypass, int paramInt, Position.Bias paramBias)
/*     */     {
/* 697 */       JFormattedTextField localJFormattedTextField = DefaultFormatter.this.getFormattedTextField();
/* 698 */       if (localJFormattedTextField.composedTextExists())
/*     */       {
/* 700 */         paramFilterBypass.moveDot(paramInt, paramBias);
/*     */       }
/* 702 */       else DefaultFormatter.this.moveDot(paramFilterBypass, paramInt, paramBias);
/*     */     }
/*     */ 
/*     */     public int getNextVisualPositionFrom(JTextComponent paramJTextComponent, int paramInt1, Position.Bias paramBias, int paramInt2, Position.Bias[] paramArrayOfBias)
/*     */       throws BadLocationException
/*     */     {
/* 711 */       if (paramJTextComponent.composedTextExists())
/*     */       {
/* 713 */         return paramJTextComponent.getUI().getNextVisualPositionFrom(paramJTextComponent, paramInt1, paramBias, paramInt2, paramArrayOfBias);
/*     */       }
/*     */ 
/* 716 */       return DefaultFormatter.this.getNextVisualPositionFrom(paramJTextComponent, paramInt1, paramBias, paramInt2, paramArrayOfBias);
/*     */     }
/*     */   }
/*     */ 
/*     */   static class ReplaceHolder
/*     */   {
/*     */     DocumentFilter.FilterBypass fb;
/*     */     int offset;
/*     */     int length;
/*     */     String text;
/*     */     AttributeSet attrs;
/*     */     Object value;
/*     */     int cursorPosition;
/*     */ 
/*     */     void reset(DocumentFilter.FilterBypass paramFilterBypass, int paramInt1, int paramInt2, String paramString, AttributeSet paramAttributeSet)
/*     */     {
/* 669 */       this.fb = paramFilterBypass;
/* 670 */       this.offset = paramInt1;
/* 671 */       this.length = paramInt2;
/* 672 */       this.text = paramString;
/* 673 */       this.attrs = paramAttributeSet;
/* 674 */       this.value = null;
/* 675 */       this.cursorPosition = -1;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.text.DefaultFormatter
 * JD-Core Version:    0.6.2
 */