/*      */ package javax.swing.text;
/*      */ 
/*      */ import java.awt.event.ActionEvent;
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.text.AttributedCharacterIterator;
/*      */ import java.text.AttributedCharacterIterator.Attribute;
/*      */ import java.text.Format;
/*      */ import java.text.Format.Field;
/*      */ import java.text.ParseException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.BitSet;
/*      */ import java.util.Map;
/*      */ import javax.swing.AbstractAction;
/*      */ import javax.swing.Action;
/*      */ import javax.swing.JFormattedTextField;
/*      */ 
/*      */ public class InternationalFormatter extends DefaultFormatter
/*      */ {
/*   99 */   private static final Format.Field[] EMPTY_FIELD_ARRAY = new Format.Field[0];
/*      */   private Format format;
/*      */   private Comparable max;
/*      */   private Comparable min;
/*      */   private transient BitSet literalMask;
/*      */   private transient AttributedCharacterIterator iterator;
/*      */   private transient boolean validMask;
/*      */   private transient String string;
/*      */   private transient boolean ignoreDocumentMutate;
/*      */ 
/*      */   public InternationalFormatter()
/*      */   {
/*  169 */     setOverwriteMode(false);
/*      */   }
/*      */ 
/*      */   public InternationalFormatter(Format paramFormat)
/*      */   {
/*  179 */     this();
/*  180 */     setFormat(paramFormat);
/*      */   }
/*      */ 
/*      */   public void setFormat(Format paramFormat)
/*      */   {
/*  191 */     this.format = paramFormat;
/*      */   }
/*      */ 
/*      */   public Format getFormat()
/*      */   {
/*  201 */     return this.format;
/*      */   }
/*      */ 
/*      */   public void setMinimum(Comparable paramComparable)
/*      */   {
/*  214 */     if ((getValueClass() == null) && (paramComparable != null)) {
/*  215 */       setValueClass(paramComparable.getClass());
/*      */     }
/*  217 */     this.min = paramComparable;
/*      */   }
/*      */ 
/*      */   public Comparable getMinimum()
/*      */   {
/*  226 */     return this.min;
/*      */   }
/*      */ 
/*      */   public void setMaximum(Comparable paramComparable)
/*      */   {
/*  239 */     if ((getValueClass() == null) && (paramComparable != null)) {
/*  240 */       setValueClass(paramComparable.getClass());
/*      */     }
/*  242 */     this.max = paramComparable;
/*      */   }
/*      */ 
/*      */   public Comparable getMaximum()
/*      */   {
/*  251 */     return this.max;
/*      */   }
/*      */ 
/*      */   public void install(JFormattedTextField paramJFormattedTextField)
/*      */   {
/*  285 */     super.install(paramJFormattedTextField);
/*  286 */     updateMaskIfNecessary();
/*      */ 
/*  288 */     positionCursorAtInitialLocation();
/*      */   }
/*      */ 
/*      */   public String valueToString(Object paramObject)
/*      */     throws ParseException
/*      */   {
/*  300 */     if (paramObject == null) {
/*  301 */       return "";
/*      */     }
/*  303 */     Format localFormat = getFormat();
/*      */ 
/*  305 */     if (localFormat == null) {
/*  306 */       return paramObject.toString();
/*      */     }
/*  308 */     return localFormat.format(paramObject);
/*      */   }
/*      */ 
/*      */   public Object stringToValue(String paramString)
/*      */     throws ParseException
/*      */   {
/*  320 */     Object localObject = stringToValue(paramString, getFormat());
/*      */ 
/*  324 */     if ((localObject != null) && (getValueClass() != null) && (!getValueClass().isInstance(localObject)))
/*      */     {
/*  326 */       localObject = super.stringToValue(localObject.toString());
/*      */     }
/*      */     try {
/*  329 */       if (!isValidValue(localObject, true))
/*  330 */         throw new ParseException("Value not within min/max range", 0);
/*      */     }
/*      */     catch (ClassCastException localClassCastException) {
/*  333 */       throw new ParseException("Class cast exception comparing values: " + localClassCastException, 0);
/*      */     }
/*      */ 
/*  336 */     return localObject;
/*      */   }
/*      */ 
/*      */   public Format.Field[] getFields(int paramInt)
/*      */   {
/*  350 */     if (getAllowsInvalid())
/*      */     {
/*  352 */       updateMask();
/*      */     }
/*      */ 
/*  355 */     Map localMap = getAttributes(paramInt);
/*      */ 
/*  357 */     if ((localMap != null) && (localMap.size() > 0)) {
/*  358 */       ArrayList localArrayList = new ArrayList();
/*      */ 
/*  360 */       localArrayList.addAll(localMap.keySet());
/*  361 */       return (Format.Field[])localArrayList.toArray(EMPTY_FIELD_ARRAY);
/*      */     }
/*  363 */     return EMPTY_FIELD_ARRAY;
/*      */   }
/*      */ 
/*      */   public Object clone()
/*      */     throws CloneNotSupportedException
/*      */   {
/*  372 */     InternationalFormatter localInternationalFormatter = (InternationalFormatter)super.clone();
/*      */ 
/*  375 */     localInternationalFormatter.literalMask = null;
/*  376 */     localInternationalFormatter.iterator = null;
/*  377 */     localInternationalFormatter.validMask = false;
/*  378 */     localInternationalFormatter.string = null;
/*  379 */     return localInternationalFormatter;
/*      */   }
/*      */ 
/*      */   protected Action[] getActions()
/*      */   {
/*  387 */     if (getSupportsIncrement()) {
/*  388 */       return new Action[] { new IncrementAction("increment", 1), new IncrementAction("decrement", -1) };
/*      */     }
/*      */ 
/*  391 */     return null;
/*      */   }
/*      */ 
/*      */   Object stringToValue(String paramString, Format paramFormat)
/*      */     throws ParseException
/*      */   {
/*  399 */     if (paramFormat == null) {
/*  400 */       return paramString;
/*      */     }
/*  402 */     return paramFormat.parseObject(paramString);
/*      */   }
/*      */ 
/*      */   boolean isValidValue(Object paramObject, boolean paramBoolean)
/*      */   {
/*  413 */     Comparable localComparable1 = getMinimum();
/*      */     try
/*      */     {
/*  416 */       if ((localComparable1 != null) && (localComparable1.compareTo(paramObject) > 0))
/*  417 */         return false;
/*      */     }
/*      */     catch (ClassCastException localClassCastException1) {
/*  420 */       if (paramBoolean) {
/*  421 */         throw localClassCastException1;
/*      */       }
/*  423 */       return false;
/*      */     }
/*      */ 
/*  426 */     Comparable localComparable2 = getMaximum();
/*      */     try {
/*  428 */       if ((localComparable2 != null) && (localComparable2.compareTo(paramObject) < 0))
/*  429 */         return false;
/*      */     }
/*      */     catch (ClassCastException localClassCastException2) {
/*  432 */       if (paramBoolean) {
/*  433 */         throw localClassCastException2;
/*      */       }
/*  435 */       return false;
/*      */     }
/*  437 */     return true;
/*      */   }
/*      */ 
/*      */   Map<AttributedCharacterIterator.Attribute, Object> getAttributes(int paramInt)
/*      */   {
/*  444 */     if (isValidMask()) {
/*  445 */       AttributedCharacterIterator localAttributedCharacterIterator = getIterator();
/*      */ 
/*  447 */       if ((paramInt >= 0) && (paramInt <= localAttributedCharacterIterator.getEndIndex())) {
/*  448 */         localAttributedCharacterIterator.setIndex(paramInt);
/*  449 */         return localAttributedCharacterIterator.getAttributes();
/*      */       }
/*      */     }
/*  452 */     return null;
/*      */   }
/*      */ 
/*      */   int getAttributeStart(AttributedCharacterIterator.Attribute paramAttribute)
/*      */   {
/*  462 */     if (isValidMask()) {
/*  463 */       AttributedCharacterIterator localAttributedCharacterIterator = getIterator();
/*      */ 
/*  465 */       localAttributedCharacterIterator.first();
/*  466 */       while (localAttributedCharacterIterator.current() != 65535) {
/*  467 */         if (localAttributedCharacterIterator.getAttribute(paramAttribute) != null) {
/*  468 */           return localAttributedCharacterIterator.getIndex();
/*      */         }
/*  470 */         localAttributedCharacterIterator.next();
/*      */       }
/*      */     }
/*  473 */     return -1;
/*      */   }
/*      */ 
/*      */   AttributedCharacterIterator getIterator()
/*      */   {
/*  481 */     return this.iterator;
/*      */   }
/*      */ 
/*      */   void updateMaskIfNecessary()
/*      */   {
/*  488 */     if ((!getAllowsInvalid()) && (getFormat() != null))
/*  489 */       if (!isValidMask()) {
/*  490 */         updateMask();
/*      */       }
/*      */       else {
/*  493 */         String str = getFormattedTextField().getText();
/*      */ 
/*  495 */         if (!str.equals(this.string))
/*  496 */           updateMask();
/*      */       }
/*      */   }
/*      */ 
/*      */   void updateMask()
/*      */   {
/*  510 */     if (getFormat() != null) {
/*  511 */       Document localDocument = getFormattedTextField().getDocument();
/*      */ 
/*  513 */       this.validMask = false;
/*  514 */       if (localDocument != null) {
/*      */         try {
/*  516 */           this.string = localDocument.getText(0, localDocument.getLength());
/*      */         } catch (BadLocationException localBadLocationException) {
/*  518 */           this.string = null;
/*      */         }
/*  520 */         if (this.string != null)
/*      */           try {
/*  522 */             Object localObject = stringToValue(this.string);
/*  523 */             AttributedCharacterIterator localAttributedCharacterIterator = getFormat().formatToCharacterIterator(localObject);
/*      */ 
/*  526 */             updateMask(localAttributedCharacterIterator);
/*      */           }
/*      */           catch (ParseException localParseException)
/*      */           {
/*      */           }
/*      */           catch (IllegalArgumentException localIllegalArgumentException) {
/*      */           }
/*      */           catch (NullPointerException localNullPointerException) {
/*      */           }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   int getLiteralCountTo(int paramInt) {
/*  540 */     int i = 0;
/*      */ 
/*  542 */     for (int j = 0; j < paramInt; j++) {
/*  543 */       if (isLiteral(j)) {
/*  544 */         i++;
/*      */       }
/*      */     }
/*  547 */     return i;
/*      */   }
/*      */ 
/*      */   boolean isLiteral(int paramInt)
/*      */   {
/*  555 */     if ((isValidMask()) && (paramInt < this.string.length())) {
/*  556 */       return this.literalMask.get(paramInt);
/*      */     }
/*  558 */     return false;
/*      */   }
/*      */ 
/*      */   char getLiteral(int paramInt)
/*      */   {
/*  565 */     if ((isValidMask()) && (this.string != null) && (paramInt < this.string.length())) {
/*  566 */       return this.string.charAt(paramInt);
/*      */     }
/*  568 */     return '\000';
/*      */   }
/*      */ 
/*      */   boolean isNavigatable(int paramInt)
/*      */   {
/*  577 */     return !isLiteral(paramInt);
/*      */   }
/*      */ 
/*      */   void updateValue(Object paramObject)
/*      */   {
/*  584 */     super.updateValue(paramObject);
/*  585 */     updateMaskIfNecessary();
/*      */   }
/*      */ 
/*      */   void replace(DocumentFilter.FilterBypass paramFilterBypass, int paramInt1, int paramInt2, String paramString, AttributeSet paramAttributeSet)
/*      */     throws BadLocationException
/*      */   {
/*  595 */     if (this.ignoreDocumentMutate) {
/*  596 */       paramFilterBypass.replace(paramInt1, paramInt2, paramString, paramAttributeSet);
/*  597 */       return;
/*      */     }
/*  599 */     super.replace(paramFilterBypass, paramInt1, paramInt2, paramString, paramAttributeSet);
/*      */   }
/*      */ 
/*      */   private int getNextNonliteralIndex(int paramInt1, int paramInt2)
/*      */   {
/*  609 */     int i = getFormattedTextField().getDocument().getLength();
/*      */ 
/*  611 */     while ((paramInt1 >= 0) && (paramInt1 < i)) {
/*  612 */       if (isLiteral(paramInt1)) {
/*  613 */         paramInt1 += paramInt2;
/*      */       }
/*      */       else {
/*  616 */         return paramInt1;
/*      */       }
/*      */     }
/*  619 */     return paramInt2 == -1 ? 0 : i;
/*      */   }
/*      */ 
/*      */   boolean canReplace(DefaultFormatter.ReplaceHolder paramReplaceHolder)
/*      */   {
/*  632 */     if (!getAllowsInvalid()) {
/*  633 */       String str = paramReplaceHolder.text;
/*  634 */       int i = str != null ? str.length() : 0;
/*  635 */       JFormattedTextField localJFormattedTextField = getFormattedTextField();
/*      */ 
/*  637 */       if ((i == 0) && (paramReplaceHolder.length == 1) && (localJFormattedTextField.getSelectionStart() != paramReplaceHolder.offset))
/*      */       {
/*  639 */         paramReplaceHolder.offset = getNextNonliteralIndex(paramReplaceHolder.offset, -1);
/*  640 */       } else if (getOverwriteMode()) {
/*  641 */         int j = paramReplaceHolder.offset;
/*  642 */         int k = j;
/*  643 */         int m = 0;
/*      */ 
/*  645 */         for (int n = 0; n < paramReplaceHolder.length; n++) {
/*  646 */           while (isLiteral(j)) j++;
/*  647 */           if (j >= this.string.length()) {
/*  648 */             j = k;
/*  649 */             m = 1;
/*  650 */             break;
/*      */           }
/*  652 */           j++; k = j;
/*      */         }
/*  654 */         if ((m != 0) || (localJFormattedTextField.getSelectedText() == null)) {
/*  655 */           paramReplaceHolder.length = (j - paramReplaceHolder.offset);
/*      */         }
/*      */       }
/*  658 */       else if (i > 0)
/*      */       {
/*  660 */         paramReplaceHolder.offset = getNextNonliteralIndex(paramReplaceHolder.offset, 1);
/*      */       }
/*      */       else
/*      */       {
/*  664 */         paramReplaceHolder.offset = getNextNonliteralIndex(paramReplaceHolder.offset, -1);
/*      */       }
/*  666 */       ((ExtendedReplaceHolder)paramReplaceHolder).endOffset = paramReplaceHolder.offset;
/*  667 */       ((ExtendedReplaceHolder)paramReplaceHolder).endTextLength = (paramReplaceHolder.text != null ? paramReplaceHolder.text.length() : 0);
/*      */     }
/*      */     else
/*      */     {
/*  671 */       ((ExtendedReplaceHolder)paramReplaceHolder).endOffset = paramReplaceHolder.offset;
/*  672 */       ((ExtendedReplaceHolder)paramReplaceHolder).endTextLength = (paramReplaceHolder.text != null ? paramReplaceHolder.text.length() : 0);
/*      */     }
/*      */ 
/*  675 */     boolean bool = super.canReplace(paramReplaceHolder);
/*  676 */     if ((bool) && (!getAllowsInvalid())) {
/*  677 */       ((ExtendedReplaceHolder)paramReplaceHolder).resetFromValue(this);
/*      */     }
/*  679 */     return bool;
/*      */   }
/*      */ 
/*      */   boolean replace(DefaultFormatter.ReplaceHolder paramReplaceHolder)
/*      */     throws BadLocationException
/*      */   {
/*  689 */     int i = -1;
/*  690 */     int j = 1;
/*  691 */     int k = -1;
/*      */ 
/*  693 */     if ((paramReplaceHolder.length > 0) && ((paramReplaceHolder.text == null) || (paramReplaceHolder.text.length() == 0)) && ((getFormattedTextField().getSelectionStart() != paramReplaceHolder.offset) || (paramReplaceHolder.length > 1)))
/*      */     {
/*  696 */       j = -1;
/*      */     }
/*  698 */     if (!getAllowsInvalid()) {
/*  699 */       if (((paramReplaceHolder.text == null) || (paramReplaceHolder.text.length() == 0)) && (paramReplaceHolder.length > 0))
/*      */       {
/*  701 */         i = getFormattedTextField().getSelectionStart();
/*      */       }
/*      */       else {
/*  704 */         i = paramReplaceHolder.offset;
/*      */       }
/*  706 */       k = getLiteralCountTo(i);
/*      */     }
/*  708 */     if (super.replace(paramReplaceHolder)) {
/*  709 */       if (i != -1) {
/*  710 */         int m = ((ExtendedReplaceHolder)paramReplaceHolder).endOffset;
/*      */ 
/*  712 */         m += ((ExtendedReplaceHolder)paramReplaceHolder).endTextLength;
/*  713 */         repositionCursor(k, m, j);
/*      */       }
/*      */       else {
/*  716 */         i = ((ExtendedReplaceHolder)paramReplaceHolder).endOffset;
/*  717 */         if (j == 1) {
/*  718 */           i += ((ExtendedReplaceHolder)paramReplaceHolder).endTextLength;
/*      */         }
/*  720 */         repositionCursor(i, j);
/*      */       }
/*  722 */       return true;
/*      */     }
/*  724 */     return false;
/*      */   }
/*      */ 
/*      */   private void repositionCursor(int paramInt1, int paramInt2, int paramInt3)
/*      */   {
/*  736 */     int i = getLiteralCountTo(paramInt2);
/*      */ 
/*  738 */     if (i != paramInt2) {
/*  739 */       paramInt2 -= paramInt1;
/*  740 */       for (int j = 0; j < paramInt2; j++) {
/*  741 */         if (isLiteral(j)) {
/*  742 */           paramInt2++;
/*      */         }
/*      */       }
/*      */     }
/*  746 */     repositionCursor(paramInt2, 1);
/*      */   }
/*      */ 
/*      */   char getBufferedChar(int paramInt)
/*      */   {
/*  754 */     if ((isValidMask()) && 
/*  755 */       (this.string != null) && (paramInt < this.string.length())) {
/*  756 */       return this.string.charAt(paramInt);
/*      */     }
/*      */ 
/*  759 */     return '\000';
/*      */   }
/*      */ 
/*      */   boolean isValidMask()
/*      */   {
/*  766 */     return this.validMask;
/*      */   }
/*      */ 
/*      */   boolean isLiteral(Map paramMap)
/*      */   {
/*  773 */     return (paramMap == null) || (paramMap.size() == 0);
/*      */   }
/*      */ 
/*      */   private void updateMask(AttributedCharacterIterator paramAttributedCharacterIterator)
/*      */   {
/*  782 */     if (paramAttributedCharacterIterator != null) {
/*  783 */       this.validMask = true;
/*  784 */       this.iterator = paramAttributedCharacterIterator;
/*      */ 
/*  787 */       if (this.literalMask == null) {
/*  788 */         this.literalMask = new BitSet();
/*      */       }
/*      */       else {
/*  791 */         for (int i = this.literalMask.length() - 1; i >= 0; 
/*  792 */           i--) {
/*  793 */           this.literalMask.clear(i);
/*      */         }
/*      */       }
/*      */ 
/*  797 */       paramAttributedCharacterIterator.first();
/*  798 */       while (paramAttributedCharacterIterator.current() != 65535) {
/*  799 */         Map localMap = paramAttributedCharacterIterator.getAttributes();
/*  800 */         boolean bool = isLiteral(localMap);
/*  801 */         int j = paramAttributedCharacterIterator.getIndex();
/*  802 */         int k = paramAttributedCharacterIterator.getRunLimit();
/*      */ 
/*  804 */         while (j < k) {
/*  805 */           if (bool) {
/*  806 */             this.literalMask.set(j);
/*      */           }
/*      */           else {
/*  809 */             this.literalMask.clear(j);
/*      */           }
/*  811 */           j++;
/*      */         }
/*  813 */         paramAttributedCharacterIterator.setIndex(j);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   boolean canIncrement(Object paramObject, int paramInt)
/*      */   {
/*  824 */     return paramObject != null;
/*      */   }
/*      */ 
/*      */   void selectField(Object paramObject, int paramInt)
/*      */   {
/*  831 */     AttributedCharacterIterator localAttributedCharacterIterator = getIterator();
/*      */ 
/*  833 */     if ((localAttributedCharacterIterator != null) && ((paramObject instanceof AttributedCharacterIterator.Attribute)))
/*      */     {
/*  835 */       AttributedCharacterIterator.Attribute localAttribute = (AttributedCharacterIterator.Attribute)paramObject;
/*      */ 
/*  838 */       localAttributedCharacterIterator.first();
/*  839 */       while (localAttributedCharacterIterator.current() != 65535) {
/*  840 */         while ((localAttributedCharacterIterator.getAttribute(localAttribute) == null) && (localAttributedCharacterIterator.next() != 65535));
/*  842 */         if (localAttributedCharacterIterator.current() != 65535) {
/*  843 */           int i = localAttributedCharacterIterator.getRunLimit(localAttribute);
/*      */ 
/*  845 */           paramInt--; if (paramInt <= 0) {
/*  846 */             getFormattedTextField().select(localAttributedCharacterIterator.getIndex(), i);
/*      */ 
/*  848 */             break;
/*      */           }
/*  850 */           localAttributedCharacterIterator.setIndex(i);
/*  851 */           localAttributedCharacterIterator.next();
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   Object getAdjustField(int paramInt, Map paramMap)
/*      */   {
/*  861 */     return null;
/*      */   }
/*      */ 
/*      */   private int getFieldTypeCountTo(Object paramObject, int paramInt)
/*      */   {
/*  870 */     AttributedCharacterIterator localAttributedCharacterIterator = getIterator();
/*  871 */     int i = 0;
/*      */ 
/*  873 */     if ((localAttributedCharacterIterator != null) && ((paramObject instanceof AttributedCharacterIterator.Attribute)))
/*      */     {
/*  875 */       AttributedCharacterIterator.Attribute localAttribute = (AttributedCharacterIterator.Attribute)paramObject;
/*      */ 
/*  878 */       localAttributedCharacterIterator.first();
/*  879 */       while (localAttributedCharacterIterator.getIndex() < paramInt) {
/*  880 */         while ((localAttributedCharacterIterator.getAttribute(localAttribute) == null) && (localAttributedCharacterIterator.next() != 65535));
/*  882 */         if (localAttributedCharacterIterator.current() == 65535) break;
/*  883 */         localAttributedCharacterIterator.setIndex(localAttributedCharacterIterator.getRunLimit(localAttribute));
/*  884 */         localAttributedCharacterIterator.next();
/*  885 */         i++;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  892 */     return i;
/*      */   }
/*      */ 
/*      */   Object adjustValue(Object paramObject1, Map paramMap, Object paramObject2, int paramInt)
/*      */     throws BadLocationException, ParseException
/*      */   {
/*  905 */     return null;
/*      */   }
/*      */ 
/*      */   boolean getSupportsIncrement()
/*      */   {
/*  916 */     return false;
/*      */   }
/*      */ 
/*      */   void resetValue(Object paramObject)
/*      */     throws BadLocationException, ParseException
/*      */   {
/*  924 */     Document localDocument = getFormattedTextField().getDocument();
/*  925 */     String str = valueToString(paramObject);
/*      */     try
/*      */     {
/*  928 */       this.ignoreDocumentMutate = true;
/*  929 */       localDocument.remove(0, localDocument.getLength());
/*  930 */       localDocument.insertString(0, str, null);
/*      */     } finally {
/*  932 */       this.ignoreDocumentMutate = false;
/*      */     }
/*  934 */     updateValue(paramObject);
/*      */   }
/*      */ 
/*      */   private void readObject(ObjectInputStream paramObjectInputStream)
/*      */     throws IOException, ClassNotFoundException
/*      */   {
/*  943 */     paramObjectInputStream.defaultReadObject();
/*  944 */     updateMaskIfNecessary();
/*      */   }
/*      */ 
/*      */   DefaultFormatter.ReplaceHolder getReplaceHolder(DocumentFilter.FilterBypass paramFilterBypass, int paramInt1, int paramInt2, String paramString, AttributeSet paramAttributeSet)
/*      */   {
/*  954 */     if (this.replaceHolder == null) {
/*  955 */       this.replaceHolder = new ExtendedReplaceHolder();
/*      */     }
/*  957 */     return super.getReplaceHolder(paramFilterBypass, paramInt1, paramInt2, paramString, paramAttributeSet);
/*      */   }
/*      */ 
/*      */   static class ExtendedReplaceHolder extends DefaultFormatter.ReplaceHolder
/*      */   {
/*      */     int endOffset;
/*      */     int endTextLength;
/*      */ 
/*      */     void resetFromValue(InternationalFormatter paramInternationalFormatter)
/*      */     {
/*  981 */       this.offset = 0;
/*      */       try {
/*  983 */         this.text = paramInternationalFormatter.valueToString(this.value);
/*      */       }
/*      */       catch (ParseException localParseException)
/*      */       {
/*  987 */         this.text = "";
/*      */       }
/*  989 */       this.length = this.fb.getDocument().getLength();
/*      */     }
/*      */   }
/*      */ 
/*      */   private class IncrementAction extends AbstractAction
/*      */   {
/*      */     private int direction;
/*      */ 
/*      */     IncrementAction(String paramInt, int arg3)
/*      */     {
/* 1003 */       super();
/*      */       int i;
/* 1004 */       this.direction = i;
/*      */     }
/*      */ 
/*      */     public void actionPerformed(ActionEvent paramActionEvent)
/*      */     {
/* 1009 */       if (InternationalFormatter.this.getFormattedTextField().isEditable()) {
/* 1010 */         if (InternationalFormatter.this.getAllowsInvalid())
/*      */         {
/* 1012 */           InternationalFormatter.this.updateMask();
/*      */         }
/*      */ 
/* 1015 */         int i = 0;
/*      */ 
/* 1017 */         if (InternationalFormatter.this.isValidMask()) {
/* 1018 */           int j = InternationalFormatter.this.getFormattedTextField().getSelectionStart();
/*      */ 
/* 1020 */           if (j != -1) {
/* 1021 */             AttributedCharacterIterator localAttributedCharacterIterator = InternationalFormatter.this.getIterator();
/*      */ 
/* 1023 */             localAttributedCharacterIterator.setIndex(j);
/*      */ 
/* 1025 */             Map localMap = localAttributedCharacterIterator.getAttributes();
/* 1026 */             Object localObject1 = InternationalFormatter.this.getAdjustField(j, localMap);
/*      */ 
/* 1028 */             if (InternationalFormatter.this.canIncrement(localObject1, j))
/*      */               try {
/* 1030 */                 Object localObject2 = InternationalFormatter.this.stringToValue(InternationalFormatter.this.getFormattedTextField().getText());
/*      */ 
/* 1032 */                 int k = InternationalFormatter.this.getFieldTypeCountTo(localObject1, j);
/*      */ 
/* 1035 */                 localObject2 = InternationalFormatter.this.adjustValue(localObject2, localMap, localObject1, this.direction);
/*      */ 
/* 1037 */                 if ((localObject2 != null) && (InternationalFormatter.this.isValidValue(localObject2, false))) {
/* 1038 */                   InternationalFormatter.this.resetValue(localObject2);
/* 1039 */                   InternationalFormatter.this.updateMask();
/*      */ 
/* 1041 */                   if (InternationalFormatter.this.isValidMask()) {
/* 1042 */                     InternationalFormatter.this.selectField(localObject1, k);
/*      */                   }
/* 1044 */                   i = 1;
/*      */                 }
/*      */               } catch (ParseException localParseException) {
/*      */               }
/*      */               catch (BadLocationException localBadLocationException) {
/*      */               }
/*      */           }
/*      */         }
/* 1052 */         if (i == 0)
/* 1053 */           InternationalFormatter.this.invalidEdit();
/*      */       }
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.text.InternationalFormatter
 * JD-Core Version:    0.6.2
 */