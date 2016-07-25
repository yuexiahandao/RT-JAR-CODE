/*      */ package javax.swing.text;
/*      */ 
/*      */ import java.awt.Color;
/*      */ import java.awt.Font;
/*      */ import java.awt.font.TextAttribute;
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.Serializable;
/*      */ import java.lang.ref.ReferenceQueue;
/*      */ import java.lang.ref.WeakReference;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Enumeration;
/*      */ import java.util.HashMap;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Stack;
/*      */ import java.util.Vector;
/*      */ import javax.swing.SwingUtilities;
/*      */ import javax.swing.event.ChangeEvent;
/*      */ import javax.swing.event.ChangeListener;
/*      */ import javax.swing.event.DocumentEvent;
/*      */ import javax.swing.event.DocumentEvent.EventType;
/*      */ import javax.swing.event.DocumentListener;
/*      */ import javax.swing.event.EventListenerList;
/*      */ import javax.swing.event.UndoableEditEvent;
/*      */ import javax.swing.undo.AbstractUndoableEdit;
/*      */ import javax.swing.undo.CannotRedoException;
/*      */ import javax.swing.undo.CannotUndoException;
/*      */ import javax.swing.undo.UndoableEdit;
/*      */ 
/*      */ public class DefaultStyledDocument extends AbstractDocument
/*      */   implements StyledDocument
/*      */ {
/*      */   public static final int BUFFER_SIZE_DEFAULT = 4096;
/*      */   protected ElementBuffer buffer;
/*      */   private transient Vector<Style> listeningStyles;
/*      */   private transient ChangeListener styleChangeListener;
/*      */   private transient ChangeListener styleContextChangeListener;
/*      */   private transient ChangeUpdateRunnable updateRunnable;
/*      */ 
/*      */   public DefaultStyledDocument(AbstractDocument.Content paramContent, StyleContext paramStyleContext)
/*      */   {
/*   82 */     super(paramContent, paramStyleContext);
/*   83 */     this.listeningStyles = new Vector();
/*   84 */     this.buffer = new ElementBuffer(createDefaultRoot());
/*   85 */     Style localStyle = paramStyleContext.getStyle("default");
/*   86 */     setLogicalStyle(0, localStyle);
/*      */   }
/*      */ 
/*      */   public DefaultStyledDocument(StyleContext paramStyleContext)
/*      */   {
/*   96 */     this(new GapContent(4096), paramStyleContext);
/*      */   }
/*      */ 
/*      */   public DefaultStyledDocument()
/*      */   {
/*  106 */     this(new GapContent(4096), new StyleContext());
/*      */   }
/*      */ 
/*      */   public Element getDefaultRootElement()
/*      */   {
/*  116 */     return this.buffer.getRootElement();
/*      */   }
/*      */ 
/*      */   protected void create(ElementSpec[] paramArrayOfElementSpec)
/*      */   {
/*      */     try
/*      */     {
/*  127 */       if (getLength() != 0) {
/*  128 */         remove(0, getLength());
/*      */       }
/*  130 */       writeLock();
/*      */ 
/*  133 */       AbstractDocument.Content localContent = getContent();
/*  134 */       int i = paramArrayOfElementSpec.length;
/*  135 */       StringBuilder localStringBuilder = new StringBuilder();
/*  136 */       for (int j = 0; j < i; j++) {
/*  137 */         ElementSpec localElementSpec = paramArrayOfElementSpec[j];
/*  138 */         if (localElementSpec.getLength() > 0) {
/*  139 */           localStringBuilder.append(localElementSpec.getArray(), localElementSpec.getOffset(), localElementSpec.getLength());
/*      */         }
/*      */       }
/*  142 */       UndoableEdit localUndoableEdit = localContent.insertString(0, localStringBuilder.toString());
/*      */ 
/*  145 */       int k = localStringBuilder.length();
/*  146 */       AbstractDocument.DefaultDocumentEvent localDefaultDocumentEvent = new AbstractDocument.DefaultDocumentEvent(this, 0, k, DocumentEvent.EventType.INSERT);
/*      */ 
/*  148 */       localDefaultDocumentEvent.addEdit(localUndoableEdit);
/*  149 */       this.buffer.create(k, paramArrayOfElementSpec, localDefaultDocumentEvent);
/*      */ 
/*  152 */       super.insertUpdate(localDefaultDocumentEvent, null);
/*      */ 
/*  155 */       localDefaultDocumentEvent.end();
/*  156 */       fireInsertUpdate(localDefaultDocumentEvent);
/*  157 */       fireUndoableEditUpdate(new UndoableEditEvent(this, localDefaultDocumentEvent));
/*      */     } catch (BadLocationException localBadLocationException) {
/*  159 */       throw new StateInvariantError("problem initializing");
/*      */     } finally {
/*  161 */       writeUnlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void insert(int paramInt, ElementSpec[] paramArrayOfElementSpec)
/*      */     throws BadLocationException
/*      */   {
/*  184 */     if ((paramArrayOfElementSpec == null) || (paramArrayOfElementSpec.length == 0)) {
/*  185 */       return;
/*      */     }
/*      */     try
/*      */     {
/*  189 */       writeLock();
/*      */ 
/*  192 */       AbstractDocument.Content localContent = getContent();
/*  193 */       int i = paramArrayOfElementSpec.length;
/*  194 */       StringBuilder localStringBuilder = new StringBuilder();
/*  195 */       for (int j = 0; j < i; j++) {
/*  196 */         ElementSpec localElementSpec = paramArrayOfElementSpec[j];
/*  197 */         if (localElementSpec.getLength() > 0) {
/*  198 */           localStringBuilder.append(localElementSpec.getArray(), localElementSpec.getOffset(), localElementSpec.getLength());
/*      */         }
/*      */       }
/*  201 */       if (localStringBuilder.length() == 0)
/*      */       {
/*      */         return;
/*      */       }
/*  205 */       UndoableEdit localUndoableEdit = localContent.insertString(paramInt, localStringBuilder.toString());
/*      */ 
/*  208 */       int k = localStringBuilder.length();
/*  209 */       AbstractDocument.DefaultDocumentEvent localDefaultDocumentEvent = new AbstractDocument.DefaultDocumentEvent(this, paramInt, k, DocumentEvent.EventType.INSERT);
/*      */ 
/*  211 */       localDefaultDocumentEvent.addEdit(localUndoableEdit);
/*  212 */       this.buffer.insert(paramInt, k, paramArrayOfElementSpec, localDefaultDocumentEvent);
/*      */ 
/*  215 */       super.insertUpdate(localDefaultDocumentEvent, null);
/*      */ 
/*  218 */       localDefaultDocumentEvent.end();
/*  219 */       fireInsertUpdate(localDefaultDocumentEvent);
/*  220 */       fireUndoableEditUpdate(new UndoableEditEvent(this, localDefaultDocumentEvent));
/*      */     } finally {
/*  222 */       writeUnlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void removeElement(Element paramElement)
/*      */   {
/*      */     try
/*      */     {
/*  265 */       writeLock();
/*  266 */       removeElementImpl(paramElement);
/*      */     } finally {
/*  268 */       writeUnlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   private void removeElementImpl(Element paramElement) {
/*  273 */     if (paramElement.getDocument() != this) {
/*  274 */       throw new IllegalArgumentException("element doesn't belong to document");
/*      */     }
/*  276 */     AbstractDocument.BranchElement localBranchElement1 = (AbstractDocument.BranchElement)paramElement.getParentElement();
/*  277 */     if (localBranchElement1 == null) {
/*  278 */       throw new IllegalArgumentException("can't remove the root element");
/*      */     }
/*      */ 
/*  281 */     int i = paramElement.getStartOffset();
/*  282 */     int j = i;
/*  283 */     int k = paramElement.getEndOffset();
/*  284 */     int m = k;
/*  285 */     int n = getLength() + 1;
/*  286 */     AbstractDocument.Content localContent = getContent();
/*  287 */     int i1 = 0;
/*  288 */     boolean bool = Utilities.isComposedTextElement(paramElement);
/*      */ 
/*  290 */     if (k >= n)
/*      */     {
/*  292 */       if (i <= 0) {
/*  293 */         throw new IllegalArgumentException("can't remove the whole content");
/*      */       }
/*  295 */       m = n - 1;
/*      */       try {
/*  297 */         if (localContent.getString(i - 1, 1).charAt(0) == '\n')
/*  298 */           j--;
/*      */       }
/*      */       catch (BadLocationException localBadLocationException1) {
/*  301 */         throw new IllegalStateException(localBadLocationException1);
/*      */       }
/*  303 */       i1 = 1;
/*      */     }
/*  305 */     int i2 = m - j;
/*      */ 
/*  307 */     AbstractDocument.DefaultDocumentEvent localDefaultDocumentEvent = new AbstractDocument.DefaultDocumentEvent(this, j, i2, DocumentEvent.EventType.REMOVE);
/*      */ 
/*  309 */     UndoableEdit localUndoableEdit = null;
/*      */ 
/*  311 */     while (localBranchElement1.getElementCount() == 1) {
/*  312 */       paramElement = localBranchElement1;
/*  313 */       localBranchElement1 = (AbstractDocument.BranchElement)localBranchElement1.getParentElement();
/*  314 */       if (localBranchElement1 == null) {
/*  315 */         throw new IllegalStateException("invalid element structure");
/*      */       }
/*      */     }
/*  318 */     Element[] arrayOfElement1 = { paramElement };
/*  319 */     Element[] arrayOfElement2 = new Element[0];
/*  320 */     int i3 = localBranchElement1.getElementIndex(i);
/*  321 */     localBranchElement1.replace(i3, 1, arrayOfElement2);
/*  322 */     localDefaultDocumentEvent.addEdit(new AbstractDocument.ElementEdit(localBranchElement1, i3, arrayOfElement1, arrayOfElement2));
/*  323 */     if (i2 > 0) {
/*      */       try {
/*  325 */         localUndoableEdit = localContent.remove(j, i2);
/*  326 */         if (localUndoableEdit != null)
/*  327 */           localDefaultDocumentEvent.addEdit(localUndoableEdit);
/*      */       }
/*      */       catch (BadLocationException localBadLocationException2)
/*      */       {
/*  331 */         throw new IllegalStateException(localBadLocationException2);
/*      */       }
/*  333 */       n -= i2;
/*      */     }
/*      */ 
/*  336 */     if (i1 != 0)
/*      */     {
/*  338 */       Element localElement1 = localBranchElement1.getElement(localBranchElement1.getElementCount() - 1);
/*  339 */       while ((localElement1 != null) && (!localElement1.isLeaf())) {
/*  340 */         localElement1 = localElement1.getElement(localElement1.getElementCount() - 1);
/*      */       }
/*  342 */       if (localElement1 == null) {
/*  343 */         throw new IllegalStateException("invalid element structure");
/*      */       }
/*  345 */       int i4 = localElement1.getStartOffset();
/*  346 */       AbstractDocument.BranchElement localBranchElement2 = (AbstractDocument.BranchElement)localElement1.getParentElement();
/*  347 */       int i5 = localBranchElement2.getElementIndex(i4);
/*      */ 
/*  349 */       Element localElement2 = createLeafElement(localBranchElement2, localElement1.getAttributes(), i4, n);
/*      */ 
/*  351 */       Element[] arrayOfElement3 = { localElement1 };
/*  352 */       Element[] arrayOfElement4 = { localElement2 };
/*  353 */       localBranchElement2.replace(i5, 1, arrayOfElement4);
/*  354 */       localDefaultDocumentEvent.addEdit(new AbstractDocument.ElementEdit(localBranchElement2, i5, arrayOfElement3, arrayOfElement4));
/*      */     }
/*      */ 
/*  358 */     postRemoveUpdate(localDefaultDocumentEvent);
/*  359 */     localDefaultDocumentEvent.end();
/*  360 */     fireRemoveUpdate(localDefaultDocumentEvent);
/*  361 */     if ((!bool) || (localUndoableEdit == null))
/*      */     {
/*  363 */       fireUndoableEditUpdate(new UndoableEditEvent(this, localDefaultDocumentEvent));
/*      */     }
/*      */   }
/*      */ 
/*      */   public Style addStyle(String paramString, Style paramStyle)
/*      */   {
/*  384 */     StyleContext localStyleContext = (StyleContext)getAttributeContext();
/*  385 */     return localStyleContext.addStyle(paramString, paramStyle);
/*      */   }
/*      */ 
/*      */   public void removeStyle(String paramString)
/*      */   {
/*  394 */     StyleContext localStyleContext = (StyleContext)getAttributeContext();
/*  395 */     localStyleContext.removeStyle(paramString);
/*      */   }
/*      */ 
/*      */   public Style getStyle(String paramString)
/*      */   {
/*  405 */     StyleContext localStyleContext = (StyleContext)getAttributeContext();
/*  406 */     return localStyleContext.getStyle(paramString);
/*      */   }
/*      */ 
/*      */   public Enumeration<?> getStyleNames()
/*      */   {
/*  416 */     return ((StyleContext)getAttributeContext()).getStyleNames();
/*      */   }
/*      */ 
/*      */   public void setLogicalStyle(int paramInt, Style paramStyle)
/*      */   {
/*  436 */     Element localElement = getParagraphElement(paramInt);
/*  437 */     if ((localElement != null) && ((localElement instanceof AbstractDocument.AbstractElement)))
/*      */       try {
/*  439 */         writeLock();
/*  440 */         StyleChangeUndoableEdit localStyleChangeUndoableEdit = new StyleChangeUndoableEdit((AbstractDocument.AbstractElement)localElement, paramStyle);
/*  441 */         ((AbstractDocument.AbstractElement)localElement).setResolveParent(paramStyle);
/*  442 */         int i = localElement.getStartOffset();
/*  443 */         int j = localElement.getEndOffset();
/*  444 */         AbstractDocument.DefaultDocumentEvent localDefaultDocumentEvent = new AbstractDocument.DefaultDocumentEvent(this, i, j - i, DocumentEvent.EventType.CHANGE);
/*      */ 
/*  446 */         localDefaultDocumentEvent.addEdit(localStyleChangeUndoableEdit);
/*  447 */         localDefaultDocumentEvent.end();
/*  448 */         fireChangedUpdate(localDefaultDocumentEvent);
/*  449 */         fireUndoableEditUpdate(new UndoableEditEvent(this, localDefaultDocumentEvent));
/*      */       } finally {
/*  451 */         writeUnlock();
/*      */       }
/*      */   }
/*      */ 
/*      */   public Style getLogicalStyle(int paramInt)
/*      */   {
/*  466 */     Style localStyle = null;
/*  467 */     Element localElement = getParagraphElement(paramInt);
/*  468 */     if (localElement != null) {
/*  469 */       AttributeSet localAttributeSet1 = localElement.getAttributes();
/*  470 */       AttributeSet localAttributeSet2 = localAttributeSet1.getResolveParent();
/*  471 */       if ((localAttributeSet2 instanceof Style)) {
/*  472 */         localStyle = (Style)localAttributeSet2;
/*      */       }
/*      */     }
/*  475 */     return localStyle;
/*      */   }
/*      */ 
/*      */   public void setCharacterAttributes(int paramInt1, int paramInt2, AttributeSet paramAttributeSet, boolean paramBoolean)
/*      */   {
/*  496 */     if (paramInt2 == 0)
/*  497 */       return;
/*      */     try
/*      */     {
/*  500 */       writeLock();
/*  501 */       AbstractDocument.DefaultDocumentEvent localDefaultDocumentEvent = new AbstractDocument.DefaultDocumentEvent(this, paramInt1, paramInt2, DocumentEvent.EventType.CHANGE);
/*      */ 
/*  505 */       this.buffer.change(paramInt1, paramInt2, localDefaultDocumentEvent);
/*      */ 
/*  507 */       AttributeSet localAttributeSet = paramAttributeSet.copyAttributes();
/*      */       int i;
/*  511 */       for (int j = paramInt1; j < paramInt1 + paramInt2; j = i) {
/*  512 */         Element localElement = getCharacterElement(j);
/*  513 */         i = localElement.getEndOffset();
/*  514 */         if (j == i)
/*      */         {
/*      */           break;
/*      */         }
/*  518 */         MutableAttributeSet localMutableAttributeSet = (MutableAttributeSet)localElement.getAttributes();
/*  519 */         localDefaultDocumentEvent.addEdit(new AttributeUndoableEdit(localElement, localAttributeSet, paramBoolean));
/*  520 */         if (paramBoolean) {
/*  521 */           localMutableAttributeSet.removeAttributes(localMutableAttributeSet);
/*      */         }
/*  523 */         localMutableAttributeSet.addAttributes(paramAttributeSet);
/*      */       }
/*  525 */       localDefaultDocumentEvent.end();
/*  526 */       fireChangedUpdate(localDefaultDocumentEvent);
/*  527 */       fireUndoableEditUpdate(new UndoableEditEvent(this, localDefaultDocumentEvent));
/*      */     } finally {
/*  529 */       writeUnlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setParagraphAttributes(int paramInt1, int paramInt2, AttributeSet paramAttributeSet, boolean paramBoolean)
/*      */   {
/*      */     try
/*      */     {
/*  550 */       writeLock();
/*  551 */       AbstractDocument.DefaultDocumentEvent localDefaultDocumentEvent = new AbstractDocument.DefaultDocumentEvent(this, paramInt1, paramInt2, DocumentEvent.EventType.CHANGE);
/*      */ 
/*  554 */       AttributeSet localAttributeSet = paramAttributeSet.copyAttributes();
/*      */ 
/*  557 */       Element localElement1 = getDefaultRootElement();
/*  558 */       int i = localElement1.getElementIndex(paramInt1);
/*  559 */       int j = localElement1.getElementIndex(paramInt1 + (paramInt2 > 0 ? paramInt2 - 1 : 0));
/*  560 */       boolean bool = Boolean.TRUE.equals(getProperty("i18n"));
/*  561 */       int k = 0;
/*  562 */       for (int m = i; m <= j; m++) {
/*  563 */         Element localElement2 = localElement1.getElement(m);
/*  564 */         MutableAttributeSet localMutableAttributeSet = (MutableAttributeSet)localElement2.getAttributes();
/*  565 */         localDefaultDocumentEvent.addEdit(new AttributeUndoableEdit(localElement2, localAttributeSet, paramBoolean));
/*  566 */         if (paramBoolean) {
/*  567 */           localMutableAttributeSet.removeAttributes(localMutableAttributeSet);
/*      */         }
/*  569 */         localMutableAttributeSet.addAttributes(paramAttributeSet);
/*  570 */         if ((bool) && (k == 0)) {
/*  571 */           k = localMutableAttributeSet.getAttribute(TextAttribute.RUN_DIRECTION) != null ? 1 : 0;
/*      */         }
/*      */       }
/*      */ 
/*  575 */       if (k != 0) {
/*  576 */         updateBidi(localDefaultDocumentEvent);
/*      */       }
/*      */ 
/*  579 */       localDefaultDocumentEvent.end();
/*  580 */       fireChangedUpdate(localDefaultDocumentEvent);
/*  581 */       fireUndoableEditUpdate(new UndoableEditEvent(this, localDefaultDocumentEvent));
/*      */     } finally {
/*  583 */       writeUnlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   public Element getParagraphElement(int paramInt)
/*      */   {
/*  597 */     for (Element localElement = getDefaultRootElement(); !localElement.isLeaf(); ) {
/*  598 */       int i = localElement.getElementIndex(paramInt);
/*  599 */       localElement = localElement.getElement(i);
/*      */     }
/*  601 */     if (localElement != null)
/*  602 */       return localElement.getParentElement();
/*  603 */     return localElement;
/*      */   }
/*      */ 
/*      */   public Element getCharacterElement(int paramInt)
/*      */   {
/*  614 */     for (Element localElement = getDefaultRootElement(); !localElement.isLeaf(); ) {
/*  615 */       int i = localElement.getElementIndex(paramInt);
/*  616 */       localElement = localElement.getElement(i);
/*      */     }
/*  618 */     return localElement;
/*      */   }
/*      */ 
/*      */   protected void insertUpdate(AbstractDocument.DefaultDocumentEvent paramDefaultDocumentEvent, AttributeSet paramAttributeSet)
/*      */   {
/*  633 */     int i = paramDefaultDocumentEvent.getOffset();
/*  634 */     int j = paramDefaultDocumentEvent.getLength();
/*  635 */     if (paramAttributeSet == null) {
/*  636 */       paramAttributeSet = SimpleAttributeSet.EMPTY;
/*      */     }
/*      */ 
/*  642 */     Element localElement1 = getParagraphElement(i + j);
/*  643 */     AttributeSet localAttributeSet1 = localElement1.getAttributes();
/*      */ 
/*  645 */     Element localElement2 = getParagraphElement(i);
/*  646 */     Element localElement3 = localElement2.getElement(localElement2.getElementIndex(i));
/*      */ 
/*  648 */     int k = i + j;
/*  649 */     int m = localElement3.getEndOffset() == k ? 1 : 0;
/*  650 */     AttributeSet localAttributeSet2 = localElement3.getAttributes();
/*      */     try
/*      */     {
/*  653 */       Segment localSegment = new Segment();
/*  654 */       Vector localVector = new Vector();
/*  655 */       Object localObject1 = null;
/*  656 */       int n = 0;
/*  657 */       short s = 6;
/*      */ 
/*  659 */       if (i > 0) {
/*  660 */         getText(i - 1, 1, localSegment);
/*  661 */         if (localSegment.array[localSegment.offset] == '\n')
/*      */         {
/*  663 */           n = 1;
/*  664 */           s = createSpecsForInsertAfterNewline(localElement1, localElement2, localAttributeSet1, localVector, i, k);
/*      */ 
/*  667 */           for (int i1 = localVector.size() - 1; i1 >= 0; 
/*  668 */             i1--) {
/*  669 */             ElementSpec localElementSpec1 = (ElementSpec)localVector.elementAt(i1);
/*  670 */             if (localElementSpec1.getType() == 1) {
/*  671 */               localObject1 = localElementSpec1;
/*  672 */               break;
/*      */             }
/*      */           }
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  679 */       if (n == 0) {
/*  680 */         localAttributeSet1 = localElement2.getAttributes();
/*      */       }
/*  682 */       getText(i, j, localSegment);
/*  683 */       char[] arrayOfChar = localSegment.array;
/*  684 */       int i2 = localSegment.offset + localSegment.count;
/*  685 */       int i3 = localSegment.offset;
/*      */ 
/*  687 */       for (int i4 = localSegment.offset; i4 < i2; i4++) {
/*  688 */         if (arrayOfChar[i4] == '\n') {
/*  689 */           i5 = i4 + 1;
/*  690 */           localVector.addElement(new ElementSpec(paramAttributeSet, (short)3, i5 - i3));
/*      */ 
/*  693 */           localVector.addElement(new ElementSpec(null, (short)2));
/*      */ 
/*  695 */           localObject1 = new ElementSpec(localAttributeSet1, (short)1);
/*      */ 
/*  697 */           localVector.addElement(localObject1);
/*  698 */           i3 = i5;
/*      */         }
/*      */       }
/*  701 */       if (i3 < i2) {
/*  702 */         localVector.addElement(new ElementSpec(paramAttributeSet, (short)3, i2 - i3));
/*      */       }
/*      */ 
/*  707 */       ElementSpec localElementSpec2 = (ElementSpec)localVector.firstElement();
/*      */ 
/*  709 */       int i5 = getLength();
/*      */ 
/*  712 */       if ((localElementSpec2.getType() == 3) && (localAttributeSet2.isEqual(paramAttributeSet)))
/*      */       {
/*  714 */         localElementSpec2.setDirection((short)4);
/*      */       }
/*      */ 
/*  718 */       if (localObject1 != null) {
/*  719 */         if (n != 0) {
/*  720 */           ((ElementSpec)localObject1).setDirection(s);
/*      */         }
/*  725 */         else if (localElement2.getEndOffset() != k) {
/*  726 */           ((ElementSpec)localObject1).setDirection((short)7);
/*      */         }
/*      */         else
/*      */         {
/*  732 */           localObject2 = localElement2.getParentElement();
/*  733 */           int i6 = ((Element)localObject2).getElementIndex(i);
/*  734 */           if ((i6 + 1 < ((Element)localObject2).getElementCount()) && (!((Element)localObject2).getElement(i6 + 1).isLeaf()))
/*      */           {
/*  736 */             ((ElementSpec)localObject1).setDirection((short)5);
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  747 */       if ((m != 0) && (k < i5)) {
/*  748 */         localObject2 = (ElementSpec)localVector.lastElement();
/*  749 */         if ((((ElementSpec)localObject2).getType() == 3) && (((ElementSpec)localObject2).getDirection() != 4) && (((localObject1 == null) && ((localElement1 == localElement2) || (n != 0))) || ((localObject1 != null) && (((ElementSpec)localObject1).getDirection() != 6))))
/*      */         {
/*  755 */           Element localElement4 = localElement1.getElement(localElement1.getElementIndex(k));
/*      */ 
/*  758 */           if ((localElement4.isLeaf()) && (paramAttributeSet.isEqual(localElement4.getAttributes())))
/*      */           {
/*  760 */             ((ElementSpec)localObject2).setDirection((short)5);
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*  767 */       else if ((m == 0) && (localObject1 != null) && (((ElementSpec)localObject1).getDirection() == 7))
/*      */       {
/*  770 */         localObject2 = (ElementSpec)localVector.lastElement();
/*  771 */         if ((((ElementSpec)localObject2).getType() == 3) && (((ElementSpec)localObject2).getDirection() != 4) && (paramAttributeSet.isEqual(localAttributeSet2)))
/*      */         {
/*  774 */           ((ElementSpec)localObject2).setDirection((short)5);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  780 */       if (Utilities.isComposedTextAttributeDefined(paramAttributeSet)) {
/*  781 */         localObject2 = (MutableAttributeSet)paramAttributeSet;
/*  782 */         ((MutableAttributeSet)localObject2).addAttributes(localAttributeSet2);
/*  783 */         ((MutableAttributeSet)localObject2).addAttribute("$ename", "content");
/*      */ 
/*  788 */         ((MutableAttributeSet)localObject2).addAttribute(StyleConstants.NameAttribute, "content");
/*      */ 
/*  790 */         if (((MutableAttributeSet)localObject2).isDefined("CR")) {
/*  791 */           ((MutableAttributeSet)localObject2).removeAttribute("CR");
/*      */         }
/*      */       }
/*      */ 
/*  795 */       Object localObject2 = new ElementSpec[localVector.size()];
/*  796 */       localVector.copyInto((Object[])localObject2);
/*  797 */       this.buffer.insert(i, j, (ElementSpec[])localObject2, paramDefaultDocumentEvent);
/*      */     }
/*      */     catch (BadLocationException localBadLocationException) {
/*      */     }
/*  801 */     super.insertUpdate(paramDefaultDocumentEvent, paramAttributeSet);
/*      */   }
/*      */ 
/*      */   short createSpecsForInsertAfterNewline(Element paramElement1, Element paramElement2, AttributeSet paramAttributeSet, Vector<ElementSpec> paramVector, int paramInt1, int paramInt2)
/*      */   {
/*      */     Object localObject1;
/*      */     Object localObject2;
/*  815 */     if (paramElement1.getParentElement() == paramElement2.getParentElement())
/*      */     {
/*  818 */       localObject1 = new ElementSpec(paramAttributeSet, (short)2);
/*  819 */       paramVector.addElement(localObject1);
/*  820 */       localObject1 = new ElementSpec(paramAttributeSet, (short)1);
/*  821 */       paramVector.addElement(localObject1);
/*  822 */       if (paramElement2.getEndOffset() != paramInt2) {
/*  823 */         return 7;
/*      */       }
/*  825 */       localObject2 = paramElement2.getParentElement();
/*  826 */       if (((Element)localObject2).getElementIndex(paramInt1) + 1 < ((Element)localObject2).getElementCount()) {
/*  827 */         return 5;
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/*  832 */       localObject1 = new Vector();
/*  833 */       localObject2 = new Vector();
/*  834 */       Element localElement = paramElement2;
/*  835 */       while (localElement != null) {
/*  836 */         ((Vector)localObject1).addElement(localElement);
/*  837 */         localElement = localElement.getParentElement();
/*      */       }
/*  839 */       localElement = paramElement1;
/*  840 */       int i = -1;
/*  841 */       while ((localElement != null) && ((i = ((Vector)localObject1).indexOf(localElement)) == -1)) {
/*  842 */         ((Vector)localObject2).addElement(localElement);
/*  843 */         localElement = localElement.getParentElement();
/*      */       }
/*  845 */       if (localElement != null)
/*      */       {
/*  848 */         for (int j = 0; j < i; 
/*  849 */           j++) {
/*  850 */           paramVector.addElement(new ElementSpec(null, (short)2));
/*      */         }
/*      */ 
/*  855 */         for (int k = ((Vector)localObject2).size() - 1; 
/*  856 */           k >= 0; k--) {
/*  857 */           ElementSpec localElementSpec = new ElementSpec(((Element)((Vector)localObject2).elementAt(k)).getAttributes(), (short)1);
/*      */ 
/*  859 */           if (k > 0)
/*  860 */             localElementSpec.setDirection((short)5);
/*  861 */           paramVector.addElement(localElementSpec);
/*      */         }
/*      */ 
/*  866 */         if (((Vector)localObject2).size() > 0) {
/*  867 */           return 5;
/*      */         }
/*      */ 
/*  870 */         return 7;
/*      */       }
/*      */     }
/*      */ 
/*  874 */     return 6;
/*      */   }
/*      */ 
/*      */   protected void removeUpdate(AbstractDocument.DefaultDocumentEvent paramDefaultDocumentEvent)
/*      */   {
/*  883 */     super.removeUpdate(paramDefaultDocumentEvent);
/*  884 */     this.buffer.remove(paramDefaultDocumentEvent.getOffset(), paramDefaultDocumentEvent.getLength(), paramDefaultDocumentEvent);
/*      */   }
/*      */ 
/*      */   protected AbstractDocument.AbstractElement createDefaultRoot()
/*      */   {
/*  898 */     writeLock();
/*  899 */     SectionElement localSectionElement = new SectionElement();
/*  900 */     AbstractDocument.BranchElement localBranchElement = new AbstractDocument.BranchElement(this, localSectionElement, null);
/*      */ 
/*  902 */     AbstractDocument.LeafElement localLeafElement = new AbstractDocument.LeafElement(this, localBranchElement, null, 0, 1);
/*  903 */     Element[] arrayOfElement = new Element[1];
/*  904 */     arrayOfElement[0] = localLeafElement;
/*  905 */     localBranchElement.replace(0, 0, arrayOfElement);
/*      */ 
/*  907 */     arrayOfElement[0] = localBranchElement;
/*  908 */     localSectionElement.replace(0, 0, arrayOfElement);
/*  909 */     writeUnlock();
/*  910 */     return localSectionElement;
/*      */   }
/*      */ 
/*      */   public Color getForeground(AttributeSet paramAttributeSet)
/*      */   {
/*  920 */     StyleContext localStyleContext = (StyleContext)getAttributeContext();
/*  921 */     return localStyleContext.getForeground(paramAttributeSet);
/*      */   }
/*      */ 
/*      */   public Color getBackground(AttributeSet paramAttributeSet)
/*      */   {
/*  931 */     StyleContext localStyleContext = (StyleContext)getAttributeContext();
/*  932 */     return localStyleContext.getBackground(paramAttributeSet);
/*      */   }
/*      */ 
/*      */   public Font getFont(AttributeSet paramAttributeSet)
/*      */   {
/*  942 */     StyleContext localStyleContext = (StyleContext)getAttributeContext();
/*  943 */     return localStyleContext.getFont(paramAttributeSet);
/*      */   }
/*      */ 
/*      */   protected void styleChanged(Style paramStyle)
/*      */   {
/*  954 */     if (getLength() != 0)
/*      */     {
/*  956 */       if (this.updateRunnable == null) {
/*  957 */         this.updateRunnable = new ChangeUpdateRunnable();
/*      */       }
/*      */ 
/*  962 */       synchronized (this.updateRunnable) {
/*  963 */         if (!this.updateRunnable.isPending) {
/*  964 */           SwingUtilities.invokeLater(this.updateRunnable);
/*  965 */           this.updateRunnable.isPending = true;
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void addDocumentListener(DocumentListener paramDocumentListener)
/*      */   {
/*  978 */     synchronized (this.listeningStyles) {
/*  979 */       int i = this.listenerList.getListenerCount(DocumentListener.class);
/*      */ 
/*  981 */       super.addDocumentListener(paramDocumentListener);
/*  982 */       if (i == 0) {
/*  983 */         if (this.styleContextChangeListener == null) {
/*  984 */           this.styleContextChangeListener = createStyleContextChangeListener();
/*      */         }
/*      */ 
/*  987 */         if (this.styleContextChangeListener != null) {
/*  988 */           StyleContext localStyleContext = (StyleContext)getAttributeContext();
/*  989 */           List localList = AbstractChangeHandler.getStaleListeners(this.styleContextChangeListener);
/*      */ 
/*  991 */           for (ChangeListener localChangeListener : localList) {
/*  992 */             localStyleContext.removeChangeListener(localChangeListener);
/*      */           }
/*  994 */           localStyleContext.addChangeListener(this.styleContextChangeListener);
/*      */         }
/*  996 */         updateStylesListeningTo();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void removeDocumentListener(DocumentListener paramDocumentListener)
/*      */   {
/* 1008 */     synchronized (this.listeningStyles) {
/* 1009 */       super.removeDocumentListener(paramDocumentListener);
/* 1010 */       if (this.listenerList.getListenerCount(DocumentListener.class) == 0) {
/* 1011 */         for (int i = this.listeningStyles.size() - 1; i >= 0; 
/* 1012 */           i--) {
/* 1013 */           ((Style)this.listeningStyles.elementAt(i)).removeChangeListener(this.styleChangeListener);
/*      */         }
/*      */ 
/* 1016 */         this.listeningStyles.removeAllElements();
/* 1017 */         if (this.styleContextChangeListener != null) {
/* 1018 */           StyleContext localStyleContext = (StyleContext)getAttributeContext();
/* 1019 */           localStyleContext.removeChangeListener(this.styleContextChangeListener);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   ChangeListener createStyleChangeListener()
/*      */   {
/* 1029 */     return new StyleChangeHandler(this);
/*      */   }
/*      */ 
/*      */   ChangeListener createStyleContextChangeListener()
/*      */   {
/* 1036 */     return new StyleContextChangeHandler(this);
/*      */   }
/*      */ 
/*      */   void updateStylesListeningTo()
/*      */   {
/* 1044 */     synchronized (this.listeningStyles) {
/* 1045 */       StyleContext localStyleContext = (StyleContext)getAttributeContext();
/* 1046 */       if (this.styleChangeListener == null) {
/* 1047 */         this.styleChangeListener = createStyleChangeListener();
/*      */       }
/* 1049 */       if ((this.styleChangeListener != null) && (localStyleContext != null)) {
/* 1050 */         Enumeration localEnumeration = localStyleContext.getStyleNames();
/* 1051 */         Vector localVector = (Vector)this.listeningStyles.clone();
/* 1052 */         this.listeningStyles.removeAllElements();
/* 1053 */         List localList = AbstractChangeHandler.getStaleListeners(this.styleChangeListener);
/*      */         Style localStyle;
/* 1055 */         while (localEnumeration.hasMoreElements()) {
/* 1056 */           String str = (String)localEnumeration.nextElement();
/* 1057 */           localStyle = localStyleContext.getStyle(str);
/* 1058 */           int j = localVector.indexOf(localStyle);
/* 1059 */           this.listeningStyles.addElement(localStyle);
/* 1060 */           if (j == -1) {
/* 1061 */             for (ChangeListener localChangeListener : localList) {
/* 1062 */               localStyle.removeChangeListener(localChangeListener);
/*      */             }
/* 1064 */             localStyle.addChangeListener(this.styleChangeListener);
/*      */           }
/*      */           else {
/* 1067 */             localVector.removeElementAt(j);
/*      */           }
/*      */         }
/* 1070 */         for (int i = localVector.size() - 1; i >= 0; i--) {
/* 1071 */           localStyle = (Style)localVector.elementAt(i);
/* 1072 */           localStyle.removeChangeListener(this.styleChangeListener);
/*      */         }
/* 1074 */         if (this.listeningStyles.size() == 0)
/* 1075 */           this.styleChangeListener = null;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void readObject(ObjectInputStream paramObjectInputStream)
/*      */     throws ClassNotFoundException, IOException
/*      */   {
/* 1083 */     this.listeningStyles = new Vector();
/* 1084 */     paramObjectInputStream.defaultReadObject();
/*      */ 
/* 1086 */     if ((this.styleContextChangeListener == null) && (this.listenerList.getListenerCount(DocumentListener.class) > 0))
/*      */     {
/* 1088 */       this.styleContextChangeListener = createStyleContextChangeListener();
/* 1089 */       if (this.styleContextChangeListener != null) {
/* 1090 */         StyleContext localStyleContext = (StyleContext)getAttributeContext();
/* 1091 */         localStyleContext.addChangeListener(this.styleContextChangeListener);
/*      */       }
/* 1093 */       updateStylesListeningTo();
/*      */     }
/*      */   }
/*      */ 
/*      */   static abstract class AbstractChangeHandler
/*      */     implements ChangeListener
/*      */   {
/* 2629 */     private static final Map<Class, ReferenceQueue<DefaultStyledDocument>> queueMap = new HashMap();
/*      */     private DocReference doc;
/*      */ 
/*      */     AbstractChangeHandler(DefaultStyledDocument paramDefaultStyledDocument)
/*      */     {
/* 2636 */       Class localClass = getClass();
/*      */       ReferenceQueue localReferenceQueue;
/* 2638 */       synchronized (queueMap) {
/* 2639 */         localReferenceQueue = (ReferenceQueue)queueMap.get(localClass);
/* 2640 */         if (localReferenceQueue == null) {
/* 2641 */           localReferenceQueue = new ReferenceQueue();
/* 2642 */           queueMap.put(localClass, localReferenceQueue);
/*      */         }
/*      */       }
/* 2645 */       this.doc = new DocReference(paramDefaultStyledDocument, localReferenceQueue);
/*      */     }
/*      */ 
/*      */     static List<ChangeListener> getStaleListeners(ChangeListener paramChangeListener)
/*      */     {
/* 2654 */       ArrayList localArrayList = new ArrayList();
/* 2655 */       ReferenceQueue localReferenceQueue = (ReferenceQueue)queueMap.get(paramChangeListener.getClass());
/*      */ 
/* 2657 */       if (localReferenceQueue != null)
/*      */       {
/* 2659 */         synchronized (localReferenceQueue)
/*      */         {
/*      */           DocReference localDocReference;
/* 2660 */           while ((localDocReference = (DocReference)localReferenceQueue.poll()) != null) {
/* 2661 */             localArrayList.add(localDocReference.getListener());
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/* 2666 */       return localArrayList;
/*      */     }
/*      */ 
/*      */     public void stateChanged(ChangeEvent paramChangeEvent)
/*      */     {
/* 2673 */       DefaultStyledDocument localDefaultStyledDocument = (DefaultStyledDocument)this.doc.get();
/* 2674 */       if (localDefaultStyledDocument != null)
/* 2675 */         fireStateChanged(localDefaultStyledDocument, paramChangeEvent);
/*      */     }
/*      */ 
/*      */     abstract void fireStateChanged(DefaultStyledDocument paramDefaultStyledDocument, ChangeEvent paramChangeEvent);
/*      */ 
/*      */     private class DocReference extends WeakReference<DefaultStyledDocument>
/*      */     {
/*      */       DocReference(ReferenceQueue<DefaultStyledDocument> arg2)
/*      */       {
/* 2617 */         super(localReferenceQueue);
/*      */       }
/*      */ 
/*      */       ChangeListener getListener()
/*      */       {
/* 2624 */         return DefaultStyledDocument.AbstractChangeHandler.this;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class AttributeUndoableEdit extends AbstractUndoableEdit
/*      */   {
/*      */     protected AttributeSet newAttributes;
/*      */     protected AttributeSet copy;
/*      */     protected boolean isReplacing;
/*      */     protected Element element;
/*      */ 
/*      */     public AttributeUndoableEdit(Element paramElement, AttributeSet paramAttributeSet, boolean paramBoolean)
/*      */     {
/* 2524 */       this.element = paramElement;
/* 2525 */       this.newAttributes = paramAttributeSet;
/* 2526 */       this.isReplacing = paramBoolean;
/*      */ 
/* 2529 */       this.copy = paramElement.getAttributes().copyAttributes();
/*      */     }
/*      */ 
/*      */     public void redo()
/*      */       throws CannotRedoException
/*      */     {
/* 2538 */       super.redo();
/* 2539 */       MutableAttributeSet localMutableAttributeSet = (MutableAttributeSet)this.element.getAttributes();
/*      */ 
/* 2541 */       if (this.isReplacing)
/* 2542 */         localMutableAttributeSet.removeAttributes(localMutableAttributeSet);
/* 2543 */       localMutableAttributeSet.addAttributes(this.newAttributes);
/*      */     }
/*      */ 
/*      */     public void undo()
/*      */       throws CannotUndoException
/*      */     {
/* 2552 */       super.undo();
/* 2553 */       MutableAttributeSet localMutableAttributeSet = (MutableAttributeSet)this.element.getAttributes();
/* 2554 */       localMutableAttributeSet.removeAttributes(localMutableAttributeSet);
/* 2555 */       localMutableAttributeSet.addAttributes(this.copy);
/*      */     }
/*      */   }
/*      */ 
/*      */   class ChangeUpdateRunnable
/*      */     implements Runnable
/*      */   {
/* 2725 */     boolean isPending = false;
/*      */ 
/*      */     ChangeUpdateRunnable() {  } 
/* 2728 */     public void run() { synchronized (this) {
/* 2729 */         this.isPending = false;
/*      */       }
/*      */       try
/*      */       {
/* 2733 */         DefaultStyledDocument.this.writeLock();
/* 2734 */         ??? = new AbstractDocument.DefaultDocumentEvent(DefaultStyledDocument.this, 0, DefaultStyledDocument.this.getLength(), DocumentEvent.EventType.CHANGE);
/*      */ 
/* 2737 */         ((AbstractDocument.DefaultDocumentEvent)???).end();
/* 2738 */         DefaultStyledDocument.this.fireChangedUpdate((DocumentEvent)???);
/*      */       } finally {
/* 2740 */         DefaultStyledDocument.this.writeUnlock();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public class ElementBuffer
/*      */     implements Serializable
/*      */   {
/*      */     Element root;
/*      */     transient int pos;
/*      */     transient int offset;
/*      */     transient int length;
/*      */     transient int endOffset;
/*      */     transient Vector<ElemChanges> changes;
/*      */     transient Stack<ElemChanges> path;
/*      */     transient boolean insertOp;
/*      */     transient boolean recreateLeafs;
/*      */     transient ElemChanges[] insertPath;
/*      */     transient boolean createdFracture;
/*      */     transient Element fracturedParent;
/*      */     transient Element fracturedChild;
/*      */     transient boolean offsetLastIndex;
/*      */     transient boolean offsetLastIndexOnReplace;
/*      */ 
/*      */     public ElementBuffer(Element arg2)
/*      */     {
/*      */       Object localObject;
/* 1406 */       this.root = localObject;
/* 1407 */       this.changes = new Vector();
/* 1408 */       this.path = new Stack();
/*      */     }
/*      */ 
/*      */     public Element getRootElement()
/*      */     {
/* 1417 */       return this.root;
/*      */     }
/*      */ 
/*      */     public void insert(int paramInt1, int paramInt2, DefaultStyledDocument.ElementSpec[] paramArrayOfElementSpec, AbstractDocument.DefaultDocumentEvent paramDefaultDocumentEvent)
/*      */     {
/* 1430 */       if (paramInt2 == 0)
/*      */       {
/* 1432 */         return;
/*      */       }
/* 1434 */       this.insertOp = true;
/* 1435 */       beginEdits(paramInt1, paramInt2);
/* 1436 */       insertUpdate(paramArrayOfElementSpec);
/* 1437 */       endEdits(paramDefaultDocumentEvent);
/*      */ 
/* 1439 */       this.insertOp = false;
/*      */     }
/*      */ 
/*      */     void create(int paramInt, DefaultStyledDocument.ElementSpec[] paramArrayOfElementSpec, AbstractDocument.DefaultDocumentEvent paramDefaultDocumentEvent) {
/* 1443 */       this.insertOp = true;
/* 1444 */       beginEdits(this.offset, paramInt);
/*      */ 
/* 1452 */       Object localObject1 = this.root;
/* 1453 */       int i = ((Element)localObject1).getElementIndex(0);
/* 1454 */       while (!((Element)localObject1).isLeaf()) {
/* 1455 */         localObject2 = ((Element)localObject1).getElement(i);
/* 1456 */         push((Element)localObject1, i);
/* 1457 */         localObject1 = localObject2;
/* 1458 */         i = ((Element)localObject1).getElementIndex(0);
/*      */       }
/* 1460 */       Object localObject2 = (ElemChanges)this.path.peek();
/* 1461 */       Element localElement = ((ElemChanges)localObject2).parent.getElement(((ElemChanges)localObject2).index);
/* 1462 */       ((ElemChanges)localObject2).added.addElement(DefaultStyledDocument.this.createLeafElement(((ElemChanges)localObject2).parent, localElement.getAttributes(), DefaultStyledDocument.this.getLength(), localElement.getEndOffset()));
/*      */ 
/* 1465 */       ((ElemChanges)localObject2).removed.addElement(localElement);
/* 1466 */       while (this.path.size() > 1) {
/* 1467 */         pop();
/*      */       }
/*      */ 
/* 1470 */       int j = paramArrayOfElementSpec.length;
/*      */ 
/* 1473 */       AttributeSet localAttributeSet = null;
/* 1474 */       if ((j > 0) && (paramArrayOfElementSpec[0].getType() == 1)) {
/* 1475 */         localAttributeSet = paramArrayOfElementSpec[0].getAttributes();
/*      */       }
/* 1477 */       if (localAttributeSet == null) {
/* 1478 */         localAttributeSet = SimpleAttributeSet.EMPTY;
/*      */       }
/* 1480 */       MutableAttributeSet localMutableAttributeSet = (MutableAttributeSet)this.root.getAttributes();
/*      */ 
/* 1482 */       paramDefaultDocumentEvent.addEdit(new DefaultStyledDocument.AttributeUndoableEdit(this.root, localAttributeSet, true));
/* 1483 */       localMutableAttributeSet.removeAttributes(localMutableAttributeSet);
/* 1484 */       localMutableAttributeSet.addAttributes(localAttributeSet);
/*      */ 
/* 1487 */       for (int k = 1; k < j; k++) {
/* 1488 */         insertElement(paramArrayOfElementSpec[k]);
/*      */       }
/*      */ 
/* 1492 */       while (this.path.size() != 0) {
/* 1493 */         pop();
/*      */       }
/*      */ 
/* 1496 */       endEdits(paramDefaultDocumentEvent);
/* 1497 */       this.insertOp = false;
/*      */     }
/*      */ 
/*      */     public void remove(int paramInt1, int paramInt2, AbstractDocument.DefaultDocumentEvent paramDefaultDocumentEvent)
/*      */     {
/* 1508 */       beginEdits(paramInt1, paramInt2);
/* 1509 */       removeUpdate();
/* 1510 */       endEdits(paramDefaultDocumentEvent);
/*      */     }
/*      */ 
/*      */     public void change(int paramInt1, int paramInt2, AbstractDocument.DefaultDocumentEvent paramDefaultDocumentEvent)
/*      */     {
/* 1521 */       beginEdits(paramInt1, paramInt2);
/* 1522 */       changeUpdate();
/* 1523 */       endEdits(paramDefaultDocumentEvent);
/*      */     }
/*      */ 
/*      */     protected void insertUpdate(DefaultStyledDocument.ElementSpec[] paramArrayOfElementSpec)
/*      */     {
/* 1533 */       Object localObject = this.root;
/* 1534 */       int i = ((Element)localObject).getElementIndex(this.offset);
/* 1535 */       while (!((Element)localObject).isLeaf()) {
/* 1536 */         Element localElement = ((Element)localObject).getElement(i);
/* 1537 */         push((Element)localObject, localElement.isLeaf() ? i : i + 1);
/* 1538 */         localObject = localElement;
/* 1539 */         i = ((Element)localObject).getElementIndex(this.offset);
/*      */       }
/*      */ 
/* 1543 */       this.insertPath = new ElemChanges[this.path.size()];
/* 1544 */       this.path.copyInto(this.insertPath);
/*      */ 
/* 1547 */       this.createdFracture = false;
/*      */ 
/* 1552 */       this.recreateLeafs = false;
/*      */       int j;
/* 1553 */       if (paramArrayOfElementSpec[0].getType() == 3) {
/* 1554 */         insertFirstContent(paramArrayOfElementSpec);
/* 1555 */         this.pos += paramArrayOfElementSpec[0].getLength();
/* 1556 */         j = 1;
/*      */       }
/*      */       else {
/* 1559 */         fractureDeepestLeaf(paramArrayOfElementSpec);
/* 1560 */         j = 0;
/*      */       }
/*      */ 
/* 1564 */       int k = paramArrayOfElementSpec.length;
/* 1565 */       for (; j < k; j++) {
/* 1566 */         insertElement(paramArrayOfElementSpec[j]);
/*      */       }
/*      */ 
/* 1570 */       if (!this.createdFracture) {
/* 1571 */         fracture(-1);
/*      */       }
/*      */ 
/* 1574 */       while (this.path.size() != 0) {
/* 1575 */         pop();
/*      */       }
/*      */ 
/* 1579 */       if ((this.offsetLastIndex) && (this.offsetLastIndexOnReplace))
/* 1580 */         this.insertPath[(this.insertPath.length - 1)].index += 1;
/*      */       ElemChanges localElemChanges;
/* 1585 */       for (int m = this.insertPath.length - 1; m >= 0; 
/* 1586 */         m--) {
/* 1587 */         localElemChanges = this.insertPath[m];
/* 1588 */         if (localElemChanges.parent == this.fracturedParent)
/* 1589 */           localElemChanges.added.addElement(this.fracturedChild);
/* 1590 */         if (((localElemChanges.added.size() > 0) || (localElemChanges.removed.size() > 0)) && (!this.changes.contains(localElemChanges)))
/*      */         {
/* 1593 */           this.changes.addElement(localElemChanges);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1600 */       if ((this.offset == 0) && (this.fracturedParent != null) && (paramArrayOfElementSpec[0].getType() == 2))
/*      */       {
/* 1602 */         m = 0;
/* 1603 */         while ((m < paramArrayOfElementSpec.length) && (paramArrayOfElementSpec[m].getType() == 2))
/*      */         {
/* 1605 */           m++;
/*      */         }
/* 1607 */         localElemChanges = this.insertPath[(this.insertPath.length - m - 1)];
/*      */ 
/* 1609 */         localElemChanges.removed.insertElementAt(localElemChanges.parent.getElement(--localElemChanges.index), 0);
/*      */       }
/*      */     }
/*      */ 
/*      */     protected void removeUpdate()
/*      */     {
/* 1620 */       removeElements(this.root, this.offset, this.offset + this.length);
/*      */     }
/*      */ 
/*      */     protected void changeUpdate()
/*      */     {
/* 1628 */       boolean bool = split(this.offset, this.length);
/* 1629 */       if (!bool)
/*      */       {
/* 1631 */         while (this.path.size() != 0) {
/* 1632 */           pop();
/*      */         }
/* 1634 */         split(this.offset + this.length, 0);
/*      */       }
/* 1636 */       while (this.path.size() != 0)
/* 1637 */         pop();
/*      */     }
/*      */ 
/*      */     boolean split(int paramInt1, int paramInt2)
/*      */     {
/* 1642 */       boolean bool = false;
/*      */ 
/* 1644 */       Element localElement1 = this.root;
/* 1645 */       int i = localElement1.getElementIndex(paramInt1);
/* 1646 */       while (!localElement1.isLeaf()) {
/* 1647 */         push(localElement1, i);
/* 1648 */         localElement1 = localElement1.getElement(i);
/* 1649 */         i = localElement1.getElementIndex(paramInt1);
/*      */       }
/*      */ 
/* 1652 */       ElemChanges localElemChanges = (ElemChanges)this.path.peek();
/* 1653 */       Element localElement2 = localElemChanges.parent.getElement(localElemChanges.index);
/*      */ 
/* 1657 */       if ((localElement2.getStartOffset() < paramInt1) && (paramInt1 < localElement2.getEndOffset()))
/*      */       {
/* 1660 */         int j = localElemChanges.index;
/* 1661 */         int k = j;
/* 1662 */         if ((paramInt1 + paramInt2 < localElemChanges.parent.getEndOffset()) && (paramInt2 != 0))
/*      */         {
/* 1664 */           k = localElemChanges.parent.getElementIndex(paramInt1 + paramInt2);
/* 1665 */           if (k == j)
/*      */           {
/* 1667 */             localElemChanges.removed.addElement(localElement2);
/* 1668 */             localElement1 = DefaultStyledDocument.this.createLeafElement(localElemChanges.parent, localElement2.getAttributes(), localElement2.getStartOffset(), paramInt1);
/*      */ 
/* 1670 */             localElemChanges.added.addElement(localElement1);
/* 1671 */             localElement1 = DefaultStyledDocument.this.createLeafElement(localElemChanges.parent, localElement2.getAttributes(), paramInt1, paramInt1 + paramInt2);
/*      */ 
/* 1673 */             localElemChanges.added.addElement(localElement1);
/* 1674 */             localElement1 = DefaultStyledDocument.this.createLeafElement(localElemChanges.parent, localElement2.getAttributes(), paramInt1 + paramInt2, localElement2.getEndOffset());
/*      */ 
/* 1676 */             localElemChanges.added.addElement(localElement1);
/* 1677 */             return true;
/*      */           }
/* 1679 */           localElement2 = localElemChanges.parent.getElement(k);
/* 1680 */           if (paramInt1 + paramInt2 == localElement2.getStartOffset())
/*      */           {
/* 1682 */             k = j;
/*      */           }
/*      */ 
/* 1685 */           bool = true;
/*      */         }
/*      */ 
/* 1689 */         this.pos = paramInt1;
/* 1690 */         localElement2 = localElemChanges.parent.getElement(j);
/* 1691 */         localElemChanges.removed.addElement(localElement2);
/* 1692 */         localElement1 = DefaultStyledDocument.this.createLeafElement(localElemChanges.parent, localElement2.getAttributes(), localElement2.getStartOffset(), this.pos);
/*      */ 
/* 1694 */         localElemChanges.added.addElement(localElement1);
/* 1695 */         localElement1 = DefaultStyledDocument.this.createLeafElement(localElemChanges.parent, localElement2.getAttributes(), this.pos, localElement2.getEndOffset());
/*      */ 
/* 1697 */         localElemChanges.added.addElement(localElement1);
/*      */ 
/* 1700 */         for (int m = j + 1; m < k; m++) {
/* 1701 */           localElement2 = localElemChanges.parent.getElement(m);
/* 1702 */           localElemChanges.removed.addElement(localElement2);
/* 1703 */           localElemChanges.added.addElement(localElement2);
/*      */         }
/*      */ 
/* 1706 */         if (k != j) {
/* 1707 */           localElement2 = localElemChanges.parent.getElement(k);
/* 1708 */           this.pos = (paramInt1 + paramInt2);
/* 1709 */           localElemChanges.removed.addElement(localElement2);
/* 1710 */           localElement1 = DefaultStyledDocument.this.createLeafElement(localElemChanges.parent, localElement2.getAttributes(), localElement2.getStartOffset(), this.pos);
/*      */ 
/* 1712 */           localElemChanges.added.addElement(localElement1);
/* 1713 */           localElement1 = DefaultStyledDocument.this.createLeafElement(localElemChanges.parent, localElement2.getAttributes(), this.pos, localElement2.getEndOffset());
/*      */ 
/* 1715 */           localElemChanges.added.addElement(localElement1);
/*      */         }
/*      */       }
/* 1718 */       return bool;
/*      */     }
/*      */ 
/*      */     void endEdits(AbstractDocument.DefaultDocumentEvent paramDefaultDocumentEvent)
/*      */     {
/* 1726 */       int i = this.changes.size();
/* 1727 */       for (int j = 0; j < i; j++) {
/* 1728 */         ElemChanges localElemChanges = (ElemChanges)this.changes.elementAt(j);
/* 1729 */         Element[] arrayOfElement1 = new Element[localElemChanges.removed.size()];
/* 1730 */         localElemChanges.removed.copyInto(arrayOfElement1);
/* 1731 */         Element[] arrayOfElement2 = new Element[localElemChanges.added.size()];
/* 1732 */         localElemChanges.added.copyInto(arrayOfElement2);
/* 1733 */         int k = localElemChanges.index;
/* 1734 */         ((AbstractDocument.BranchElement)localElemChanges.parent).replace(k, arrayOfElement1.length, arrayOfElement2);
/* 1735 */         AbstractDocument.ElementEdit localElementEdit = new AbstractDocument.ElementEdit(localElemChanges.parent, k, arrayOfElement1, arrayOfElement2);
/* 1736 */         paramDefaultDocumentEvent.addEdit(localElementEdit);
/*      */       }
/*      */ 
/* 1739 */       this.changes.removeAllElements();
/* 1740 */       this.path.removeAllElements();
/*      */     }
/*      */ 
/*      */     void beginEdits(int paramInt1, int paramInt2)
/*      */     {
/* 1767 */       this.offset = paramInt1;
/* 1768 */       this.length = paramInt2;
/* 1769 */       this.endOffset = (paramInt1 + paramInt2);
/* 1770 */       this.pos = paramInt1;
/* 1771 */       if (this.changes == null)
/* 1772 */         this.changes = new Vector();
/*      */       else {
/* 1774 */         this.changes.removeAllElements();
/*      */       }
/* 1776 */       if (this.path == null)
/* 1777 */         this.path = new Stack();
/*      */       else {
/* 1779 */         this.path.removeAllElements();
/*      */       }
/* 1781 */       this.fracturedParent = null;
/* 1782 */       this.fracturedChild = null;
/* 1783 */       this.offsetLastIndex = (this.offsetLastIndexOnReplace = 0);
/*      */     }
/*      */ 
/*      */     void push(Element paramElement, int paramInt, boolean paramBoolean)
/*      */     {
/* 1795 */       ElemChanges localElemChanges = new ElemChanges(paramElement, paramInt, paramBoolean);
/* 1796 */       this.path.push(localElemChanges);
/*      */     }
/*      */ 
/*      */     void push(Element paramElement, int paramInt) {
/* 1800 */       push(paramElement, paramInt, false);
/*      */     }
/*      */ 
/*      */     void pop() {
/* 1804 */       ElemChanges localElemChanges = (ElemChanges)this.path.peek();
/* 1805 */       this.path.pop();
/* 1806 */       if ((localElemChanges.added.size() > 0) || (localElemChanges.removed.size() > 0)) {
/* 1807 */         this.changes.addElement(localElemChanges);
/* 1808 */       } else if (!this.path.isEmpty()) {
/* 1809 */         Element localElement = localElemChanges.parent;
/* 1810 */         if (localElement.getElementCount() == 0)
/*      */         {
/* 1813 */           localElemChanges = (ElemChanges)this.path.peek();
/* 1814 */           localElemChanges.added.removeElement(localElement);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     void advance(int paramInt)
/*      */     {
/* 1823 */       this.pos += paramInt;
/*      */     }
/*      */ 
/*      */     void insertElement(DefaultStyledDocument.ElementSpec paramElementSpec) {
/* 1827 */       ElemChanges localElemChanges = (ElemChanges)this.path.peek();
/*      */       Element localElement2;
/* 1828 */       switch (paramElementSpec.getType()) {
/*      */       case 1:
/* 1830 */         switch (paramElementSpec.getDirection())
/*      */         {
/*      */         case 5:
/* 1834 */           Element localElement1 = localElemChanges.parent.getElement(localElemChanges.index);
/*      */ 
/* 1836 */           if (localElement1.isLeaf())
/*      */           {
/* 1839 */             if (localElemChanges.index + 1 < localElemChanges.parent.getElementCount())
/* 1840 */               localElement1 = localElemChanges.parent.getElement(localElemChanges.index + 1);
/*      */             else {
/* 1842 */               throw new StateInvariantError("Join next to leaf");
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/* 1848 */           push(localElement1, 0, true);
/* 1849 */           break;
/*      */         case 7:
/* 1851 */           if (!this.createdFracture)
/*      */           {
/* 1853 */             fracture(this.path.size() - 1);
/*      */           }
/*      */ 
/* 1857 */           if (!localElemChanges.isFracture) {
/* 1858 */             push(this.fracturedChild, 0, true);
/*      */           }
/*      */           else
/*      */           {
/* 1862 */             push(localElemChanges.parent.getElement(0), 0, true);
/* 1863 */           }break;
/*      */         default:
/* 1865 */           localElement2 = DefaultStyledDocument.this.createBranchElement(localElemChanges.parent, paramElementSpec.getAttributes());
/*      */ 
/* 1867 */           localElemChanges.added.addElement(localElement2);
/* 1868 */           push(localElement2, 0);
/*      */         }
/*      */ 
/* 1871 */         break;
/*      */       case 2:
/* 1873 */         pop();
/* 1874 */         break;
/*      */       case 3:
/* 1876 */         int i = paramElementSpec.getLength();
/* 1877 */         if (paramElementSpec.getDirection() != 5) {
/* 1878 */           localElement2 = DefaultStyledDocument.this.createLeafElement(localElemChanges.parent, paramElementSpec.getAttributes(), this.pos, this.pos + i);
/*      */ 
/* 1880 */           localElemChanges.added.addElement(localElement2);
/*      */         }
/*      */         else
/*      */         {
/*      */           Element localElement3;
/* 1888 */           if (!localElemChanges.isFracture) {
/* 1889 */             localElement2 = null;
/* 1890 */             if (this.insertPath != null) {
/* 1891 */               for (int j = this.insertPath.length - 1; 
/* 1892 */                 j >= 0; j--) {
/* 1893 */                 if (this.insertPath[j] == localElemChanges) {
/* 1894 */                   if (j == this.insertPath.length - 1) break;
/* 1895 */                   localElement2 = localElemChanges.parent.getElement(localElemChanges.index); break;
/*      */                 }
/*      */               }
/*      */             }
/*      */ 
/* 1900 */             if (localElement2 == null)
/* 1901 */               localElement2 = localElemChanges.parent.getElement(localElemChanges.index + 1);
/* 1902 */             localElement3 = DefaultStyledDocument.this.createLeafElement(localElemChanges.parent, localElement2.getAttributes(), this.pos, localElement2.getEndOffset());
/*      */ 
/* 1904 */             localElemChanges.added.addElement(localElement3);
/* 1905 */             localElemChanges.removed.addElement(localElement2);
/*      */           }
/*      */           else
/*      */           {
/* 1909 */             localElement2 = localElemChanges.parent.getElement(0);
/* 1910 */             localElement3 = DefaultStyledDocument.this.createLeafElement(localElemChanges.parent, localElement2.getAttributes(), this.pos, localElement2.getEndOffset());
/*      */ 
/* 1912 */             localElemChanges.added.addElement(localElement3);
/* 1913 */             localElemChanges.removed.addElement(localElement2);
/*      */           }
/*      */         }
/* 1916 */         this.pos += i;
/*      */       }
/*      */     }
/*      */ 
/*      */     boolean removeElements(Element paramElement, int paramInt1, int paramInt2)
/*      */     {
/* 1930 */       if (!paramElement.isLeaf())
/*      */       {
/* 1932 */         int i = paramElement.getElementIndex(paramInt1);
/* 1933 */         int j = paramElement.getElementIndex(paramInt2);
/* 1934 */         push(paramElement, i);
/* 1935 */         ElemChanges localElemChanges = (ElemChanges)this.path.peek();
/*      */         Element localElement1;
/* 1939 */         if (i == j) {
/* 1940 */           localElement1 = paramElement.getElement(i);
/* 1941 */           if ((paramInt1 <= localElement1.getStartOffset()) && (paramInt2 >= localElement1.getEndOffset()))
/*      */           {
/* 1944 */             localElemChanges.removed.addElement(localElement1);
/*      */           }
/* 1946 */           else if (removeElements(localElement1, paramInt1, paramInt2)) {
/* 1947 */             localElemChanges.removed.addElement(localElement1);
/*      */           }
/*      */ 
/*      */         }
/*      */         else
/*      */         {
/* 1953 */           localElement1 = paramElement.getElement(i);
/* 1954 */           Element localElement2 = paramElement.getElement(j);
/* 1955 */           int k = paramInt2 < paramElement.getEndOffset() ? 1 : 0;
/* 1956 */           if ((k != 0) && (canJoin(localElement1, localElement2)))
/*      */           {
/* 1958 */             for (int m = i; m <= j; m++) {
/* 1959 */               localElemChanges.removed.addElement(paramElement.getElement(m));
/*      */             }
/* 1961 */             Element localElement3 = join(paramElement, localElement1, localElement2, paramInt1, paramInt2);
/* 1962 */             localElemChanges.added.addElement(localElement3);
/*      */           }
/*      */           else {
/* 1965 */             int n = i + 1;
/* 1966 */             int i1 = j - 1;
/* 1967 */             if ((localElement1.getStartOffset() == paramInt1) || ((i == 0) && (localElement1.getStartOffset() > paramInt1) && (localElement1.getEndOffset() <= paramInt2)))
/*      */             {
/* 1972 */               localElement1 = null;
/* 1973 */               n = i;
/*      */             }
/* 1975 */             if (k == 0) {
/* 1976 */               localElement2 = null;
/* 1977 */               i1++;
/*      */             }
/* 1979 */             else if (localElement2.getStartOffset() == paramInt2)
/*      */             {
/* 1981 */               localElement2 = null;
/*      */             }
/* 1983 */             if (n <= i1) {
/* 1984 */               localElemChanges.index = n;
/*      */             }
/* 1986 */             for (int i2 = n; i2 <= i1; i2++) {
/* 1987 */               localElemChanges.removed.addElement(paramElement.getElement(i2));
/*      */             }
/* 1989 */             if ((localElement1 != null) && 
/* 1990 */               (removeElements(localElement1, paramInt1, paramInt2))) {
/* 1991 */               localElemChanges.removed.insertElementAt(localElement1, 0);
/* 1992 */               localElemChanges.index = i;
/*      */             }
/*      */ 
/* 1995 */             if ((localElement2 != null) && 
/* 1996 */               (removeElements(localElement2, paramInt1, paramInt2))) {
/* 1997 */               localElemChanges.removed.addElement(localElement2);
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/* 2004 */         pop();
/*      */ 
/* 2007 */         if (paramElement.getElementCount() == localElemChanges.removed.size() - localElemChanges.added.size())
/*      */         {
/* 2009 */           return true;
/*      */         }
/*      */       }
/* 2012 */       return false;
/*      */     }
/*      */ 
/*      */     boolean canJoin(Element paramElement1, Element paramElement2)
/*      */     {
/* 2020 */       if ((paramElement1 == null) || (paramElement2 == null)) {
/* 2021 */         return false;
/*      */       }
/*      */ 
/* 2024 */       boolean bool1 = paramElement1.isLeaf();
/* 2025 */       boolean bool2 = paramElement2.isLeaf();
/* 2026 */       if (bool1 != bool2) {
/* 2027 */         return false;
/*      */       }
/* 2029 */       if (bool1)
/*      */       {
/* 2032 */         return paramElement1.getAttributes().isEqual(paramElement2.getAttributes());
/*      */       }
/*      */ 
/* 2037 */       String str1 = paramElement1.getName();
/* 2038 */       String str2 = paramElement2.getName();
/* 2039 */       if (str1 != null) {
/* 2040 */         return str1.equals(str2);
/*      */       }
/* 2042 */       if (str2 != null) {
/* 2043 */         return str2.equals(str1);
/*      */       }
/*      */ 
/* 2046 */       return true;
/*      */     }
/*      */ 
/*      */     Element join(Element paramElement1, Element paramElement2, Element paramElement3, int paramInt1, int paramInt2)
/*      */     {
/* 2054 */       if ((paramElement2.isLeaf()) && (paramElement3.isLeaf())) {
/* 2055 */         return DefaultStyledDocument.this.createLeafElement(paramElement1, paramElement2.getAttributes(), paramElement2.getStartOffset(), paramElement3.getEndOffset());
/*      */       }
/* 2057 */       if ((!paramElement2.isLeaf()) && (!paramElement3.isLeaf()))
/*      */       {
/* 2062 */         Element localElement1 = DefaultStyledDocument.this.createBranchElement(paramElement1, paramElement2.getAttributes());
/* 2063 */         int i = paramElement2.getElementIndex(paramInt1);
/* 2064 */         int j = paramElement3.getElementIndex(paramInt2);
/* 2065 */         Element localElement2 = paramElement2.getElement(i);
/* 2066 */         if (localElement2.getStartOffset() >= paramInt1) {
/* 2067 */           localElement2 = null;
/*      */         }
/* 2069 */         Element localElement3 = paramElement3.getElement(j);
/* 2070 */         if (localElement3.getStartOffset() == paramInt2) {
/* 2071 */           localElement3 = null;
/*      */         }
/* 2073 */         Vector localVector = new Vector();
/*      */ 
/* 2076 */         for (int k = 0; k < i; k++) {
/* 2077 */           localVector.addElement(clone(localElement1, paramElement2.getElement(k)));
/*      */         }
/*      */ 
/* 2081 */         if (canJoin(localElement2, localElement3)) {
/* 2082 */           Element localElement4 = join(localElement1, localElement2, localElement3, paramInt1, paramInt2);
/* 2083 */           localVector.addElement(localElement4);
/*      */         } else {
/* 2085 */           if (localElement2 != null) {
/* 2086 */             localVector.addElement(cloneAsNecessary(localElement1, localElement2, paramInt1, paramInt2));
/*      */           }
/* 2088 */           if (localElement3 != null) {
/* 2089 */             localVector.addElement(cloneAsNecessary(localElement1, localElement3, paramInt1, paramInt2));
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/* 2094 */         int m = paramElement3.getElementCount();
/* 2095 */         for (int n = localElement3 == null ? j : j + 1; n < m; n++) {
/* 2096 */           localVector.addElement(clone(localElement1, paramElement3.getElement(n)));
/*      */         }
/*      */ 
/* 2100 */         Element[] arrayOfElement = new Element[localVector.size()];
/* 2101 */         localVector.copyInto(arrayOfElement);
/* 2102 */         ((AbstractDocument.BranchElement)localElement1).replace(0, 0, arrayOfElement);
/* 2103 */         return localElement1;
/*      */       }
/* 2105 */       throw new StateInvariantError("No support to join leaf element with non-leaf element");
/*      */     }
/*      */ 
/*      */     public Element clone(Element paramElement1, Element paramElement2)
/*      */     {
/* 2119 */       if (paramElement2.isLeaf()) {
/* 2120 */         return DefaultStyledDocument.this.createLeafElement(paramElement1, paramElement2.getAttributes(), paramElement2.getStartOffset(), paramElement2.getEndOffset());
/*      */       }
/*      */ 
/* 2124 */       Element localElement = DefaultStyledDocument.this.createBranchElement(paramElement1, paramElement2.getAttributes());
/* 2125 */       int i = paramElement2.getElementCount();
/* 2126 */       Element[] arrayOfElement = new Element[i];
/* 2127 */       for (int j = 0; j < i; j++) {
/* 2128 */         arrayOfElement[j] = clone(localElement, paramElement2.getElement(j));
/*      */       }
/* 2130 */       ((AbstractDocument.BranchElement)localElement).replace(0, 0, arrayOfElement);
/* 2131 */       return localElement;
/*      */     }
/*      */ 
/*      */     Element cloneAsNecessary(Element paramElement1, Element paramElement2, int paramInt1, int paramInt2)
/*      */     {
/* 2140 */       if (paramElement2.isLeaf()) {
/* 2141 */         return DefaultStyledDocument.this.createLeafElement(paramElement1, paramElement2.getAttributes(), paramElement2.getStartOffset(), paramElement2.getEndOffset());
/*      */       }
/*      */ 
/* 2145 */       Element localElement1 = DefaultStyledDocument.this.createBranchElement(paramElement1, paramElement2.getAttributes());
/* 2146 */       int i = paramElement2.getElementCount();
/* 2147 */       ArrayList localArrayList = new ArrayList(i);
/* 2148 */       for (int j = 0; j < i; j++) {
/* 2149 */         Element localElement2 = paramElement2.getElement(j);
/* 2150 */         if ((localElement2.getStartOffset() < paramInt1) || (localElement2.getEndOffset() > paramInt2)) {
/* 2151 */           localArrayList.add(cloneAsNecessary(localElement1, localElement2, paramInt1, paramInt2));
/*      */         }
/*      */       }
/* 2154 */       Element[] arrayOfElement = new Element[localArrayList.size()];
/* 2155 */       arrayOfElement = (Element[])localArrayList.toArray(arrayOfElement);
/* 2156 */       ((AbstractDocument.BranchElement)localElement1).replace(0, 0, arrayOfElement);
/* 2157 */       return localElement1;
/*      */     }
/*      */ 
/*      */     void fracture(int paramInt)
/*      */     {
/* 2172 */       int i = this.insertPath.length;
/* 2173 */       int j = -1;
/* 2174 */       boolean bool = this.recreateLeafs;
/* 2175 */       ElemChanges localElemChanges1 = this.insertPath[(i - 1)];
/*      */ 
/* 2178 */       int k = localElemChanges1.index + 1 < localElemChanges1.parent.getElementCount() ? 1 : 0;
/*      */ 
/* 2180 */       int m = bool ? i : -1;
/* 2181 */       int n = i - 1;
/*      */ 
/* 2183 */       this.createdFracture = true;
/*      */ 
/* 2187 */       for (int i1 = i - 2; i1 >= 0; i1--) {
/* 2188 */         ElemChanges localElemChanges2 = this.insertPath[i1];
/* 2189 */         if ((localElemChanges2.added.size() > 0) || (i1 == paramInt)) {
/* 2190 */           j = i1;
/* 2191 */           if ((!bool) && (k != 0)) {
/* 2192 */             bool = true;
/* 2193 */             if (m == -1)
/* 2194 */               m = n + 1;
/*      */           }
/*      */         }
/* 2197 */         if ((k == 0) && (localElemChanges2.index < localElemChanges2.parent.getElementCount()))
/*      */         {
/* 2199 */           k = 1;
/* 2200 */           n = i1;
/*      */         }
/*      */       }
/* 2203 */       if (bool)
/*      */       {
/* 2206 */         if (j == -1)
/* 2207 */           j = i - 1;
/* 2208 */         fractureFrom(this.insertPath, j, m);
/*      */       }
/*      */     }
/*      */ 
/*      */     void fractureFrom(ElemChanges[] paramArrayOfElemChanges, int paramInt1, int paramInt2)
/*      */     {
/* 2224 */       ElemChanges localElemChanges = paramArrayOfElemChanges[paramInt1];
/*      */ 
/* 2227 */       int i = paramArrayOfElemChanges.length;
/*      */       Element localElement1;
/* 2229 */       if (paramInt1 + 1 == i)
/* 2230 */         localElement1 = localElemChanges.parent.getElement(localElemChanges.index);
/*      */       else
/* 2232 */         localElement1 = localElemChanges.parent.getElement(localElemChanges.index - 1);
/*      */       Element localElement2;
/* 2233 */       if (localElement1.isLeaf()) {
/* 2234 */         localElement2 = DefaultStyledDocument.this.createLeafElement(localElemChanges.parent, localElement1.getAttributes(), Math.max(this.endOffset, localElement1.getStartOffset()), localElement1.getEndOffset());
/*      */       }
/*      */       else
/*      */       {
/* 2239 */         localElement2 = DefaultStyledDocument.this.createBranchElement(localElemChanges.parent, localElement1.getAttributes());
/*      */       }
/*      */ 
/* 2242 */       this.fracturedParent = localElemChanges.parent;
/* 2243 */       this.fracturedChild = localElement2;
/*      */ 
/* 2247 */       Element localElement3 = localElement2;
/*      */       while (true) {
/* 2249 */         paramInt1++; if (paramInt1 >= paramInt2) break;
/* 2250 */         int j = paramInt1 + 1 == paramInt2 ? 1 : 0;
/* 2251 */         int k = paramInt1 + 1 == i ? 1 : 0;
/*      */ 
/* 2256 */         localElemChanges = paramArrayOfElemChanges[paramInt1];
/*      */ 
/* 2260 */         if (j != 0) {
/* 2261 */           if ((this.offsetLastIndex) || (k == 0))
/* 2262 */             localElement1 = null;
/*      */           else
/* 2264 */             localElement1 = localElemChanges.parent.getElement(localElemChanges.index);
/*      */         }
/*      */         else {
/* 2267 */           localElement1 = localElemChanges.parent.getElement(localElemChanges.index - 1);
/*      */         }
/*      */ 
/* 2270 */         if (localElement1 != null) {
/* 2271 */           if (localElement1.isLeaf()) {
/* 2272 */             localElement2 = DefaultStyledDocument.this.createLeafElement(localElement3, localElement1.getAttributes(), Math.max(this.endOffset, localElement1.getStartOffset()), localElement1.getEndOffset());
/*      */           }
/*      */           else
/*      */           {
/* 2277 */             localElement2 = DefaultStyledDocument.this.createBranchElement(localElement3, localElement1.getAttributes());
/*      */           }
/*      */         }
/*      */         else
/*      */         {
/* 2282 */           localElement2 = null;
/*      */         }
/*      */ 
/* 2285 */         int m = localElemChanges.parent.getElementCount() - localElemChanges.index;
/*      */ 
/* 2289 */         int i1 = 1;
/*      */         int n;
/*      */         Element[] arrayOfElement;
/* 2291 */         if (localElement2 == null)
/*      */         {
/* 2293 */           if (k != 0) {
/* 2294 */             m--;
/* 2295 */             n = localElemChanges.index + 1;
/*      */           }
/*      */           else {
/* 2298 */             n = localElemChanges.index;
/*      */           }
/* 2300 */           i1 = 0;
/* 2301 */           arrayOfElement = new Element[m];
/*      */         }
/*      */         else {
/* 2304 */           if (j == 0)
/*      */           {
/* 2306 */             m++;
/* 2307 */             n = localElemChanges.index;
/*      */           }
/*      */           else
/*      */           {
/* 2311 */             n = localElemChanges.index + 1;
/*      */           }
/* 2313 */           arrayOfElement = new Element[m];
/* 2314 */           arrayOfElement[0] = localElement2;
/*      */         }
/*      */ 
/* 2317 */         for (int i2 = i1; i2 < m; 
/* 2318 */           i2++) {
/* 2319 */           Element localElement4 = localElemChanges.parent.getElement(n++);
/* 2320 */           arrayOfElement[i2] = recreateFracturedElement(localElement3, localElement4);
/* 2321 */           localElemChanges.removed.addElement(localElement4);
/*      */         }
/* 2323 */         ((AbstractDocument.BranchElement)localElement3).replace(0, 0, arrayOfElement);
/* 2324 */         localElement3 = localElement2;
/*      */       }
/*      */     }
/*      */ 
/*      */     Element recreateFracturedElement(Element paramElement1, Element paramElement2)
/*      */     {
/* 2335 */       if (paramElement2.isLeaf()) {
/* 2336 */         return DefaultStyledDocument.this.createLeafElement(paramElement1, paramElement2.getAttributes(), Math.max(paramElement2.getStartOffset(), this.endOffset), paramElement2.getEndOffset());
/*      */       }
/*      */ 
/* 2342 */       Element localElement = DefaultStyledDocument.this.createBranchElement(paramElement1, paramElement2.getAttributes());
/*      */ 
/* 2344 */       int i = paramElement2.getElementCount();
/* 2345 */       Element[] arrayOfElement = new Element[i];
/* 2346 */       for (int j = 0; j < i; j++) {
/* 2347 */         arrayOfElement[j] = recreateFracturedElement(localElement, paramElement2.getElement(j));
/*      */       }
/*      */ 
/* 2350 */       ((AbstractDocument.BranchElement)localElement).replace(0, 0, arrayOfElement);
/* 2351 */       return localElement;
/*      */     }
/*      */ 
/*      */     void fractureDeepestLeaf(DefaultStyledDocument.ElementSpec[] paramArrayOfElementSpec)
/*      */     {
/* 2360 */       ElemChanges localElemChanges = (ElemChanges)this.path.peek();
/* 2361 */       Element localElement1 = localElemChanges.parent.getElement(localElemChanges.index);
/*      */ 
/* 2364 */       if (this.offset != 0) {
/* 2365 */         Element localElement2 = DefaultStyledDocument.this.createLeafElement(localElemChanges.parent, localElement1.getAttributes(), localElement1.getStartOffset(), this.offset);
/*      */ 
/* 2370 */         localElemChanges.added.addElement(localElement2);
/*      */       }
/* 2372 */       localElemChanges.removed.addElement(localElement1);
/* 2373 */       if (localElement1.getEndOffset() != this.endOffset)
/* 2374 */         this.recreateLeafs = true;
/*      */       else
/* 2376 */         this.offsetLastIndex = true;
/*      */     }
/*      */ 
/*      */     void insertFirstContent(DefaultStyledDocument.ElementSpec[] paramArrayOfElementSpec)
/*      */     {
/* 2384 */       DefaultStyledDocument.ElementSpec localElementSpec = paramArrayOfElementSpec[0];
/* 2385 */       ElemChanges localElemChanges = (ElemChanges)this.path.peek();
/* 2386 */       Element localElement1 = localElemChanges.parent.getElement(localElemChanges.index);
/* 2387 */       int i = this.offset + localElementSpec.getLength();
/* 2388 */       int j = paramArrayOfElementSpec.length == 1 ? 1 : 0;
/*      */       Element localElement2;
/* 2390 */       switch (localElementSpec.getDirection()) {
/*      */       case 4:
/* 2392 */         if ((localElement1.getEndOffset() != i) && (j == 0))
/*      */         {
/* 2395 */           localElement2 = DefaultStyledDocument.this.createLeafElement(localElemChanges.parent, localElement1.getAttributes(), localElement1.getStartOffset(), i);
/*      */ 
/* 2398 */           localElemChanges.added.addElement(localElement2);
/* 2399 */           localElemChanges.removed.addElement(localElement1);
/*      */ 
/* 2401 */           if (localElement1.getEndOffset() != this.endOffset)
/* 2402 */             this.recreateLeafs = true;
/*      */           else
/* 2404 */             this.offsetLastIndex = true;
/*      */         }
/*      */         else {
/* 2407 */           this.offsetLastIndex = true;
/* 2408 */           this.offsetLastIndexOnReplace = true;
/*      */         }
/*      */ 
/* 2412 */         break;
/*      */       case 5:
/* 2414 */         if (this.offset != 0)
/*      */         {
/* 2417 */           localElement2 = DefaultStyledDocument.this.createLeafElement(localElemChanges.parent, localElement1.getAttributes(), localElement1.getStartOffset(), this.offset);
/*      */ 
/* 2420 */           localElemChanges.added.addElement(localElement2);
/*      */ 
/* 2423 */           Element localElement3 = localElemChanges.parent.getElement(localElemChanges.index + 1);
/* 2424 */           if (j != 0) {
/* 2425 */             localElement2 = DefaultStyledDocument.this.createLeafElement(localElemChanges.parent, localElement3.getAttributes(), this.offset, localElement3.getEndOffset());
/*      */           }
/*      */           else {
/* 2428 */             localElement2 = DefaultStyledDocument.this.createLeafElement(localElemChanges.parent, localElement3.getAttributes(), this.offset, i);
/*      */           }
/* 2430 */           localElemChanges.added.addElement(localElement2);
/* 2431 */           localElemChanges.removed.addElement(localElement1);
/* 2432 */           localElemChanges.removed.addElement(localElement3);
/* 2433 */         }break;
/*      */       default:
/* 2440 */         if (localElement1.getStartOffset() != this.offset) {
/* 2441 */           localElement2 = DefaultStyledDocument.this.createLeafElement(localElemChanges.parent, localElement1.getAttributes(), localElement1.getStartOffset(), this.offset);
/*      */ 
/* 2444 */           localElemChanges.added.addElement(localElement2);
/*      */         }
/* 2446 */         localElemChanges.removed.addElement(localElement1);
/*      */ 
/* 2448 */         localElement2 = DefaultStyledDocument.this.createLeafElement(localElemChanges.parent, localElementSpec.getAttributes(), this.offset, i);
/*      */ 
/* 2451 */         localElemChanges.added.addElement(localElement2);
/* 2452 */         if (localElement1.getEndOffset() != this.endOffset)
/*      */         {
/* 2454 */           this.recreateLeafs = true;
/*      */         }
/*      */         else
/* 2457 */           this.offsetLastIndex = true;
/*      */         break;
/*      */       }
/*      */     }
/*      */ 
/*      */     class ElemChanges
/*      */     {
/*      */       Element parent;
/*      */       int index;
/*      */       Vector<Element> added;
/*      */       Vector<Element> removed;
/*      */       boolean isFracture;
/*      */ 
/*      */       ElemChanges(Element paramInt, int paramBoolean, boolean arg4)
/*      */       {
/* 2496 */         this.parent = paramInt;
/* 2497 */         this.index = paramBoolean;
/*      */         boolean bool;
/* 2498 */         this.isFracture = bool;
/* 2499 */         this.added = new Vector();
/* 2500 */         this.removed = new Vector();
/*      */       }
/*      */ 
/*      */       public String toString() {
/* 2504 */         return "added: " + this.added + "\nremoved: " + this.removed + "\n";
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class ElementSpec
/*      */   {
/*      */     public static final short StartTagType = 1;
/*      */     public static final short EndTagType = 2;
/*      */     public static final short ContentType = 3;
/*      */     public static final short JoinPreviousDirection = 4;
/*      */     public static final short JoinNextDirection = 5;
/*      */     public static final short OriginateDirection = 6;
/*      */     public static final short JoinFractureDirection = 7;
/*      */     private AttributeSet attr;
/*      */     private int len;
/*      */     private short type;
/*      */     private short direction;
/*      */     private int offs;
/*      */     private char[] data;
/*      */ 
/*      */     public ElementSpec(AttributeSet paramAttributeSet, short paramShort)
/*      */     {
/* 1225 */       this(paramAttributeSet, paramShort, null, 0, 0);
/*      */     }
/*      */ 
/*      */     public ElementSpec(AttributeSet paramAttributeSet, short paramShort, int paramInt)
/*      */     {
/* 1239 */       this(paramAttributeSet, paramShort, null, 0, paramInt);
/*      */     }
/*      */ 
/*      */     public ElementSpec(AttributeSet paramAttributeSet, short paramShort, char[] paramArrayOfChar, int paramInt1, int paramInt2)
/*      */     {
/* 1255 */       this.attr = paramAttributeSet;
/* 1256 */       this.type = paramShort;
/* 1257 */       this.data = paramArrayOfChar;
/* 1258 */       this.offs = paramInt1;
/* 1259 */       this.len = paramInt2;
/* 1260 */       this.direction = 6;
/*      */     }
/*      */ 
/*      */     public void setType(short paramShort)
/*      */     {
/* 1270 */       this.type = paramShort;
/*      */     }
/*      */ 
/*      */     public short getType()
/*      */     {
/* 1280 */       return this.type;
/*      */     }
/*      */ 
/*      */     public void setDirection(short paramShort)
/*      */     {
/* 1290 */       this.direction = paramShort;
/*      */     }
/*      */ 
/*      */     public short getDirection()
/*      */     {
/* 1299 */       return this.direction;
/*      */     }
/*      */ 
/*      */     public AttributeSet getAttributes()
/*      */     {
/* 1308 */       return this.attr;
/*      */     }
/*      */ 
/*      */     public char[] getArray()
/*      */     {
/* 1317 */       return this.data;
/*      */     }
/*      */ 
/*      */     public int getOffset()
/*      */     {
/* 1327 */       return this.offs;
/*      */     }
/*      */ 
/*      */     public int getLength()
/*      */     {
/* 1336 */       return this.len;
/*      */     }
/*      */ 
/*      */     public String toString()
/*      */     {
/* 1345 */       String str1 = "??";
/* 1346 */       String str2 = "??";
/* 1347 */       switch (this.type) {
/*      */       case 1:
/* 1349 */         str1 = "StartTag";
/* 1350 */         break;
/*      */       case 3:
/* 1352 */         str1 = "Content";
/* 1353 */         break;
/*      */       case 2:
/* 1355 */         str1 = "EndTag";
/*      */       }
/*      */ 
/* 1358 */       switch (this.direction) {
/*      */       case 4:
/* 1360 */         str2 = "JoinPrevious";
/* 1361 */         break;
/*      */       case 5:
/* 1363 */         str2 = "JoinNext";
/* 1364 */         break;
/*      */       case 6:
/* 1366 */         str2 = "Originate";
/* 1367 */         break;
/*      */       case 7:
/* 1369 */         str2 = "Fracture";
/*      */       }
/*      */ 
/* 1372 */       return str1 + ":" + str2 + ":" + getLength();
/*      */     }
/*      */   }
/*      */ 
/*      */   protected class SectionElement extends AbstractDocument.BranchElement
/*      */   {
/*      */     public SectionElement()
/*      */     {
/* 1137 */       super(null, null);
/*      */     }
/*      */ 
/*      */     public String getName()
/*      */     {
/* 1146 */       return "section";
/*      */     }
/*      */   }
/*      */ 
/*      */   static class StyleChangeHandler extends DefaultStyledDocument.AbstractChangeHandler
/*      */   {
/*      */     StyleChangeHandler(DefaultStyledDocument paramDefaultStyledDocument)
/*      */     {
/* 2690 */       super();
/*      */     }
/*      */ 
/*      */     void fireStateChanged(DefaultStyledDocument paramDefaultStyledDocument, ChangeEvent paramChangeEvent) {
/* 2694 */       Object localObject = paramChangeEvent.getSource();
/* 2695 */       if ((localObject instanceof Style))
/* 2696 */         paramDefaultStyledDocument.styleChanged((Style)localObject);
/*      */       else
/* 2698 */         paramDefaultStyledDocument.styleChanged(null);
/*      */     }
/*      */   }
/*      */ 
/*      */   static class StyleChangeUndoableEdit extends AbstractUndoableEdit
/*      */   {
/*      */     protected AbstractDocument.AbstractElement element;
/*      */     protected Style newStyle;
/*      */     protected AttributeSet oldStyle;
/*      */ 
/*      */     public StyleChangeUndoableEdit(AbstractDocument.AbstractElement paramAbstractElement, Style paramStyle)
/*      */     {
/* 2575 */       this.element = paramAbstractElement;
/* 2576 */       this.newStyle = paramStyle;
/* 2577 */       this.oldStyle = paramAbstractElement.getResolveParent();
/*      */     }
/*      */ 
/*      */     public void redo()
/*      */       throws CannotRedoException
/*      */     {
/* 2586 */       super.redo();
/* 2587 */       this.element.setResolveParent(this.newStyle);
/*      */     }
/*      */ 
/*      */     public void undo()
/*      */       throws CannotUndoException
/*      */     {
/* 2596 */       super.undo();
/* 2597 */       this.element.setResolveParent(this.oldStyle);
/*      */     }
/*      */   }
/*      */ 
/*      */   static class StyleContextChangeHandler extends DefaultStyledDocument.AbstractChangeHandler
/*      */   {
/*      */     StyleContextChangeHandler(DefaultStyledDocument paramDefaultStyledDocument)
/*      */     {
/* 2711 */       super();
/*      */     }
/*      */ 
/*      */     void fireStateChanged(DefaultStyledDocument paramDefaultStyledDocument, ChangeEvent paramChangeEvent) {
/* 2715 */       paramDefaultStyledDocument.updateStylesListeningTo();
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.text.DefaultStyledDocument
 * JD-Core Version:    0.6.2
 */