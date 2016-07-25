/*      */ package javax.swing.text;
/*      */ 
/*      */ import java.awt.font.TextAttribute;
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectInputValidation;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.OutputStreamWriter;
/*      */ import java.io.PrintStream;
/*      */ import java.io.PrintWriter;
/*      */ import java.io.Serializable;
/*      */ import java.io.UnsupportedEncodingException;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.text.Bidi;
/*      */ import java.util.Dictionary;
/*      */ import java.util.Enumeration;
/*      */ import java.util.EventListener;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Vector;
/*      */ import javax.swing.UIManager;
/*      */ import javax.swing.event.DocumentEvent;
/*      */ import javax.swing.event.DocumentEvent.ElementChange;
/*      */ import javax.swing.event.DocumentEvent.EventType;
/*      */ import javax.swing.event.DocumentListener;
/*      */ import javax.swing.event.EventListenerList;
/*      */ import javax.swing.event.UndoableEditEvent;
/*      */ import javax.swing.event.UndoableEditListener;
/*      */ import javax.swing.tree.TreeNode;
/*      */ import javax.swing.undo.AbstractUndoableEdit;
/*      */ import javax.swing.undo.CannotRedoException;
/*      */ import javax.swing.undo.CannotUndoException;
/*      */ import javax.swing.undo.CompoundEdit;
/*      */ import javax.swing.undo.UndoableEdit;
/*      */ import sun.font.BidiUtils;
/*      */ import sun.swing.SwingUtilities2;
/*      */ 
/*      */ public abstract class AbstractDocument
/*      */   implements Document, Serializable
/*      */ {
/*      */   private transient int numReaders;
/*      */   private transient Thread currWriter;
/*      */   private transient int numWriters;
/*      */   private transient boolean notifyingListeners;
/*      */   private static Boolean defaultI18NProperty;
/* 1484 */   private Dictionary<Object, Object> documentProperties = null;
/*      */ 
/* 1489 */   protected EventListenerList listenerList = new EventListenerList();
/*      */   private Content data;
/*      */   private AttributeContext context;
/*      */   private transient BranchElement bidiRoot;
/*      */   private DocumentFilter documentFilter;
/*      */   private transient DocumentFilter.FilterBypass filterBypass;
/*      */   private static final String BAD_LOCK_STATE = "document lock failure";
/*      */   protected static final String BAD_LOCATION = "document location failure";
/*      */   public static final String ParagraphElementName = "paragraph";
/*      */   public static final String ContentElementName = "content";
/*      */   public static final String SectionElementName = "section";
/*      */   public static final String BidiElementName = "bidi level";
/*      */   public static final String ElementNameAttribute = "$ename";
/*      */   static final String I18NProperty = "i18n";
/* 1571 */   static final Object MultiByteProperty = "multiByte";
/*      */   static final String AsyncLoadPriority = "load priority";
/*      */ 
/*      */   protected AbstractDocument(Content paramContent)
/*      */   {
/*  109 */     this(paramContent, StyleContext.getDefaultStyleContext());
/*      */   }
/*      */ 
/*      */   protected AbstractDocument(Content paramContent, AttributeContext paramAttributeContext)
/*      */   {
/*  120 */     this.data = paramContent;
/*  121 */     this.context = paramAttributeContext;
/*  122 */     this.bidiRoot = new BidiRootElement();
/*      */     Object localObject1;
/*  124 */     if (defaultI18NProperty == null)
/*      */     {
/*  126 */       localObject1 = (String)AccessController.doPrivileged(new PrivilegedAction()
/*      */       {
/*      */         public String run() {
/*  129 */           return System.getProperty("i18n");
/*      */         }
/*      */       });
/*  133 */       if (localObject1 != null)
/*  134 */         defaultI18NProperty = Boolean.valueOf((String)localObject1);
/*      */       else {
/*  136 */         defaultI18NProperty = Boolean.FALSE;
/*      */       }
/*      */     }
/*  139 */     putProperty("i18n", defaultI18NProperty);
/*      */ 
/*  146 */     writeLock();
/*      */     try {
/*  148 */       localObject1 = new Element[1];
/*  149 */       localObject1[0] = new BidiElement(this.bidiRoot, 0, 1, 0);
/*  150 */       this.bidiRoot.replace(0, 0, (Element[])localObject1);
/*      */     } finally {
/*  152 */       writeUnlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   public Dictionary<Object, Object> getDocumentProperties()
/*      */   {
/*  165 */     if (this.documentProperties == null) {
/*  166 */       this.documentProperties = new Hashtable(2);
/*      */     }
/*  168 */     return this.documentProperties;
/*      */   }
/*      */ 
/*      */   public void setDocumentProperties(Dictionary<Object, Object> paramDictionary)
/*      */   {
/*  178 */     this.documentProperties = paramDictionary;
/*      */   }
/*      */ 
/*      */   protected void fireInsertUpdate(DocumentEvent paramDocumentEvent)
/*      */   {
/*  191 */     this.notifyingListeners = true;
/*      */     try
/*      */     {
/*  194 */       Object[] arrayOfObject = this.listenerList.getListenerList();
/*      */ 
/*  197 */       for (int i = arrayOfObject.length - 2; i >= 0; i -= 2)
/*  198 */         if (arrayOfObject[i] == DocumentListener.class)
/*      */         {
/*  202 */           ((DocumentListener)arrayOfObject[(i + 1)]).insertUpdate(paramDocumentEvent);
/*      */         }
/*      */     }
/*      */     finally {
/*  206 */       this.notifyingListeners = false;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void fireChangedUpdate(DocumentEvent paramDocumentEvent)
/*      */   {
/*  220 */     this.notifyingListeners = true;
/*      */     try
/*      */     {
/*  223 */       Object[] arrayOfObject = this.listenerList.getListenerList();
/*      */ 
/*  226 */       for (int i = arrayOfObject.length - 2; i >= 0; i -= 2)
/*  227 */         if (arrayOfObject[i] == DocumentListener.class)
/*      */         {
/*  231 */           ((DocumentListener)arrayOfObject[(i + 1)]).changedUpdate(paramDocumentEvent);
/*      */         }
/*      */     }
/*      */     finally {
/*  235 */       this.notifyingListeners = false;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void fireRemoveUpdate(DocumentEvent paramDocumentEvent)
/*      */   {
/*  249 */     this.notifyingListeners = true;
/*      */     try
/*      */     {
/*  252 */       Object[] arrayOfObject = this.listenerList.getListenerList();
/*      */ 
/*  255 */       for (int i = arrayOfObject.length - 2; i >= 0; i -= 2)
/*  256 */         if (arrayOfObject[i] == DocumentListener.class)
/*      */         {
/*  260 */           ((DocumentListener)arrayOfObject[(i + 1)]).removeUpdate(paramDocumentEvent);
/*      */         }
/*      */     }
/*      */     finally {
/*  264 */       this.notifyingListeners = false;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void fireUndoableEditUpdate(UndoableEditEvent paramUndoableEditEvent)
/*      */   {
/*  279 */     Object[] arrayOfObject = this.listenerList.getListenerList();
/*      */ 
/*  282 */     for (int i = arrayOfObject.length - 2; i >= 0; i -= 2)
/*  283 */       if (arrayOfObject[i] == UndoableEditListener.class)
/*      */       {
/*  287 */         ((UndoableEditListener)arrayOfObject[(i + 1)]).undoableEditHappened(paramUndoableEditEvent);
/*      */       }
/*      */   }
/*      */ 
/*      */   public <T extends EventListener> T[] getListeners(Class<T> paramClass)
/*      */   {
/*  328 */     return this.listenerList.getListeners(paramClass);
/*      */   }
/*      */ 
/*      */   public int getAsynchronousLoadPriority()
/*      */   {
/*  339 */     Integer localInteger = (Integer)getProperty("load priority");
/*      */ 
/*  341 */     if (localInteger != null) {
/*  342 */       return localInteger.intValue();
/*      */     }
/*  344 */     return -1;
/*      */   }
/*      */ 
/*      */   public void setAsynchronousLoadPriority(int paramInt)
/*      */   {
/*  354 */     Object localObject = paramInt >= 0 ? Integer.valueOf(paramInt) : null;
/*  355 */     putProperty("load priority", localObject);
/*      */   }
/*      */ 
/*      */   public void setDocumentFilter(DocumentFilter paramDocumentFilter)
/*      */   {
/*  369 */     this.documentFilter = paramDocumentFilter;
/*      */   }
/*      */ 
/*      */   public DocumentFilter getDocumentFilter()
/*      */   {
/*  382 */     return this.documentFilter;
/*      */   }
/*      */ 
/*      */   public void render(Runnable paramRunnable)
/*      */   {
/*  418 */     readLock();
/*      */     try {
/*  420 */       paramRunnable.run();
/*      */     } finally {
/*  422 */       readUnlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   public int getLength()
/*      */   {
/*  434 */     return this.data.length() - 1;
/*      */   }
/*      */ 
/*      */   public void addDocumentListener(DocumentListener paramDocumentListener)
/*      */   {
/*  444 */     this.listenerList.add(DocumentListener.class, paramDocumentListener);
/*      */   }
/*      */ 
/*      */   public void removeDocumentListener(DocumentListener paramDocumentListener)
/*      */   {
/*  454 */     this.listenerList.remove(DocumentListener.class, paramDocumentListener);
/*      */   }
/*      */ 
/*      */   public DocumentListener[] getDocumentListeners()
/*      */   {
/*  470 */     return (DocumentListener[])this.listenerList.getListeners(DocumentListener.class);
/*      */   }
/*      */ 
/*      */   public void addUndoableEditListener(UndoableEditListener paramUndoableEditListener)
/*      */   {
/*  483 */     this.listenerList.add(UndoableEditListener.class, paramUndoableEditListener);
/*      */   }
/*      */ 
/*      */   public void removeUndoableEditListener(UndoableEditListener paramUndoableEditListener)
/*      */   {
/*  493 */     this.listenerList.remove(UndoableEditListener.class, paramUndoableEditListener);
/*      */   }
/*      */ 
/*      */   public UndoableEditListener[] getUndoableEditListeners()
/*      */   {
/*  510 */     return (UndoableEditListener[])this.listenerList.getListeners(UndoableEditListener.class);
/*      */   }
/*      */ 
/*      */   public final Object getProperty(Object paramObject)
/*      */   {
/*  525 */     return getDocumentProperties().get(paramObject);
/*      */   }
/*      */ 
/*      */   public final void putProperty(Object paramObject1, Object paramObject2)
/*      */   {
/*  543 */     if (paramObject2 != null)
/*  544 */       getDocumentProperties().put(paramObject1, paramObject2);
/*      */     else {
/*  546 */       getDocumentProperties().remove(paramObject1);
/*      */     }
/*  548 */     if ((paramObject1 == TextAttribute.RUN_DIRECTION) && (Boolean.TRUE.equals(getProperty("i18n"))))
/*      */     {
/*  553 */       writeLock();
/*      */       try {
/*  555 */         DefaultDocumentEvent localDefaultDocumentEvent = new DefaultDocumentEvent(0, getLength(), DocumentEvent.EventType.INSERT);
/*      */ 
/*  558 */         updateBidi(localDefaultDocumentEvent);
/*      */       } finally {
/*  560 */         writeUnlock();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void remove(int paramInt1, int paramInt2)
/*      */     throws BadLocationException
/*      */   {
/*  583 */     DocumentFilter localDocumentFilter = getDocumentFilter();
/*      */ 
/*  585 */     writeLock();
/*      */     try {
/*  587 */       if (localDocumentFilter != null) {
/*  588 */         localDocumentFilter.remove(getFilterBypass(), paramInt1, paramInt2);
/*      */       }
/*      */       else
/*  591 */         handleRemove(paramInt1, paramInt2);
/*      */     }
/*      */     finally {
/*  594 */       writeUnlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   void handleRemove(int paramInt1, int paramInt2)
/*      */     throws BadLocationException
/*      */   {
/*  603 */     if (paramInt2 > 0) {
/*  604 */       if ((paramInt1 < 0) || (paramInt1 + paramInt2 > getLength())) {
/*  605 */         throw new BadLocationException("Invalid remove", getLength() + 1);
/*      */       }
/*      */ 
/*  608 */       DefaultDocumentEvent localDefaultDocumentEvent = new DefaultDocumentEvent(paramInt1, paramInt2, DocumentEvent.EventType.REMOVE);
/*      */ 
/*  613 */       boolean bool = Utilities.isComposedTextElement(this, paramInt1);
/*      */ 
/*  615 */       removeUpdate(localDefaultDocumentEvent);
/*  616 */       UndoableEdit localUndoableEdit = this.data.remove(paramInt1, paramInt2);
/*  617 */       if (localUndoableEdit != null) {
/*  618 */         localDefaultDocumentEvent.addEdit(localUndoableEdit);
/*      */       }
/*  620 */       postRemoveUpdate(localDefaultDocumentEvent);
/*      */ 
/*  622 */       localDefaultDocumentEvent.end();
/*  623 */       fireRemoveUpdate(localDefaultDocumentEvent);
/*      */ 
/*  626 */       if ((localUndoableEdit != null) && (!bool))
/*  627 */         fireUndoableEditUpdate(new UndoableEditEvent(this, localDefaultDocumentEvent));
/*      */     }
/*      */   }
/*      */ 
/*      */   public void replace(int paramInt1, int paramInt2, String paramString, AttributeSet paramAttributeSet)
/*      */     throws BadLocationException
/*      */   {
/*  654 */     if ((paramInt2 == 0) && ((paramString == null) || (paramString.length() == 0))) {
/*  655 */       return;
/*      */     }
/*  657 */     DocumentFilter localDocumentFilter = getDocumentFilter();
/*      */ 
/*  659 */     writeLock();
/*      */     try {
/*  661 */       if (localDocumentFilter != null) {
/*  662 */         localDocumentFilter.replace(getFilterBypass(), paramInt1, paramInt2, paramString, paramAttributeSet);
/*      */       }
/*      */       else
/*      */       {
/*  666 */         if (paramInt2 > 0) {
/*  667 */           remove(paramInt1, paramInt2);
/*      */         }
/*  669 */         if ((paramString != null) && (paramString.length() > 0))
/*  670 */           insertString(paramInt1, paramString, paramAttributeSet);
/*      */       }
/*      */     }
/*      */     finally {
/*  674 */       writeUnlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insertString(int paramInt, String paramString, AttributeSet paramAttributeSet)
/*      */     throws BadLocationException
/*      */   {
/*  697 */     if ((paramString == null) || (paramString.length() == 0)) {
/*  698 */       return;
/*      */     }
/*  700 */     DocumentFilter localDocumentFilter = getDocumentFilter();
/*      */ 
/*  702 */     writeLock();
/*      */     try {
/*  704 */       if (localDocumentFilter != null) {
/*  705 */         localDocumentFilter.insertString(getFilterBypass(), paramInt, paramString, paramAttributeSet);
/*      */       }
/*      */       else
/*  708 */         handleInsertString(paramInt, paramString, paramAttributeSet);
/*      */     }
/*      */     finally {
/*  711 */       writeUnlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   void handleInsertString(int paramInt, String paramString, AttributeSet paramAttributeSet)
/*      */     throws BadLocationException
/*      */   {
/*  721 */     if ((paramString == null) || (paramString.length() == 0)) {
/*  722 */       return;
/*      */     }
/*  724 */     UndoableEdit localUndoableEdit = this.data.insertString(paramInt, paramString);
/*  725 */     DefaultDocumentEvent localDefaultDocumentEvent = new DefaultDocumentEvent(paramInt, paramString.length(), DocumentEvent.EventType.INSERT);
/*      */ 
/*  727 */     if (localUndoableEdit != null) {
/*  728 */       localDefaultDocumentEvent.addEdit(localUndoableEdit);
/*      */     }
/*      */ 
/*  732 */     if (getProperty("i18n").equals(Boolean.FALSE))
/*      */     {
/*  735 */       Object localObject = getProperty(TextAttribute.RUN_DIRECTION);
/*  736 */       if ((localObject != null) && (localObject.equals(TextAttribute.RUN_DIRECTION_RTL))) {
/*  737 */         putProperty("i18n", Boolean.TRUE);
/*      */       } else {
/*  739 */         char[] arrayOfChar = paramString.toCharArray();
/*  740 */         if (SwingUtilities2.isComplexLayout(arrayOfChar, 0, arrayOfChar.length)) {
/*  741 */           putProperty("i18n", Boolean.TRUE);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  746 */     insertUpdate(localDefaultDocumentEvent, paramAttributeSet);
/*      */ 
/*  748 */     localDefaultDocumentEvent.end();
/*  749 */     fireInsertUpdate(localDefaultDocumentEvent);
/*      */ 
/*  752 */     if ((localUndoableEdit != null) && ((paramAttributeSet == null) || (!paramAttributeSet.isDefined(StyleConstants.ComposedTextAttribute))))
/*      */     {
/*  754 */       fireUndoableEditUpdate(new UndoableEditEvent(this, localDefaultDocumentEvent));
/*      */     }
/*      */   }
/*      */ 
/*      */   public String getText(int paramInt1, int paramInt2)
/*      */     throws BadLocationException
/*      */   {
/*  769 */     if (paramInt2 < 0) {
/*  770 */       throw new BadLocationException("Length must be positive", paramInt2);
/*      */     }
/*  772 */     String str = this.data.getString(paramInt1, paramInt2);
/*  773 */     return str;
/*      */   }
/*      */ 
/*      */   public void getText(int paramInt1, int paramInt2, Segment paramSegment)
/*      */     throws BadLocationException
/*      */   {
/*  809 */     if (paramInt2 < 0) {
/*  810 */       throw new BadLocationException("Length must be positive", paramInt2);
/*      */     }
/*  812 */     this.data.getChars(paramInt1, paramInt2, paramSegment);
/*      */   }
/*      */ 
/*      */   public synchronized Position createPosition(int paramInt)
/*      */     throws BadLocationException
/*      */   {
/*  831 */     return this.data.createPosition(paramInt);
/*      */   }
/*      */ 
/*      */   public final Position getStartPosition()
/*      */   {
/*      */     Position localPosition;
/*      */     try
/*      */     {
/*  844 */       localPosition = createPosition(0);
/*      */     } catch (BadLocationException localBadLocationException) {
/*  846 */       localPosition = null;
/*      */     }
/*  848 */     return localPosition;
/*      */   }
/*      */ 
/*      */   public final Position getEndPosition()
/*      */   {
/*      */     Position localPosition;
/*      */     try
/*      */     {
/*  861 */       localPosition = createPosition(this.data.length());
/*      */     } catch (BadLocationException localBadLocationException) {
/*  863 */       localPosition = null;
/*      */     }
/*  865 */     return localPosition;
/*      */   }
/*      */ 
/*      */   public Element[] getRootElements()
/*      */   {
/*  876 */     Element[] arrayOfElement = new Element[2];
/*  877 */     arrayOfElement[0] = getDefaultRootElement();
/*  878 */     arrayOfElement[1] = getBidiRootElement();
/*  879 */     return arrayOfElement;
/*      */   }
/*      */ 
/*      */   public abstract Element getDefaultRootElement();
/*      */ 
/*      */   private DocumentFilter.FilterBypass getFilterBypass()
/*      */   {
/*  899 */     if (this.filterBypass == null) {
/*  900 */       this.filterBypass = new DefaultFilterBypass(null);
/*      */     }
/*  902 */     return this.filterBypass;
/*      */   }
/*      */ 
/*      */   public Element getBidiRootElement()
/*      */   {
/*  911 */     return this.bidiRoot;
/*      */   }
/*      */ 
/*      */   boolean isLeftToRight(int paramInt1, int paramInt2)
/*      */   {
/*  919 */     if (!getProperty("i18n").equals(Boolean.TRUE)) {
/*  920 */       return true;
/*      */     }
/*  922 */     Element localElement1 = getBidiRootElement();
/*  923 */     int i = localElement1.getElementIndex(paramInt1);
/*  924 */     Element localElement2 = localElement1.getElement(i);
/*  925 */     if (localElement2.getEndOffset() >= paramInt2) {
/*  926 */       AttributeSet localAttributeSet = localElement2.getAttributes();
/*  927 */       return StyleConstants.getBidiLevel(localAttributeSet) % 2 == 0;
/*      */     }
/*  929 */     return true;
/*      */   }
/*      */ 
/*      */   public abstract Element getParagraphElement(int paramInt);
/*      */ 
/*      */   protected final AttributeContext getAttributeContext()
/*      */   {
/*  951 */     return this.context;
/*      */   }
/*      */ 
/*      */   protected void insertUpdate(DefaultDocumentEvent paramDefaultDocumentEvent, AttributeSet paramAttributeSet)
/*      */   {
/*  964 */     if (getProperty("i18n").equals(Boolean.TRUE)) {
/*  965 */       updateBidi(paramDefaultDocumentEvent);
/*      */     }
/*      */ 
/*  968 */     if ((paramDefaultDocumentEvent.type == DocumentEvent.EventType.INSERT) && (paramDefaultDocumentEvent.getLength() > 0) && (!Boolean.TRUE.equals(getProperty(MultiByteProperty))))
/*      */     {
/*  971 */       Segment localSegment = SegmentCache.getSharedSegment();
/*      */       try {
/*  973 */         getText(paramDefaultDocumentEvent.getOffset(), paramDefaultDocumentEvent.getLength(), localSegment);
/*  974 */         localSegment.first();
/*      */         do
/*  976 */           if (localSegment.current() > 'ÿ') {
/*  977 */             putProperty(MultiByteProperty, Boolean.TRUE);
/*  978 */             break;
/*      */           }
/*  980 */         while (localSegment.next() != 65535);
/*      */       }
/*      */       catch (BadLocationException localBadLocationException) {
/*      */       }
/*  984 */       SegmentCache.releaseSharedSegment(localSegment);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void removeUpdate(DefaultDocumentEvent paramDefaultDocumentEvent)
/*      */   {
/*      */   }
/*      */ 
/*      */   protected void postRemoveUpdate(DefaultDocumentEvent paramDefaultDocumentEvent)
/*      */   {
/* 1010 */     if (getProperty("i18n").equals(Boolean.TRUE))
/* 1011 */       updateBidi(paramDefaultDocumentEvent);
/*      */   }
/*      */ 
/*      */   void updateBidi(DefaultDocumentEvent paramDefaultDocumentEvent)
/*      */   {
/*      */     Element localElement1;
/*      */     Element localElement2;
/* 1029 */     if ((paramDefaultDocumentEvent.type == DocumentEvent.EventType.INSERT) || (paramDefaultDocumentEvent.type == DocumentEvent.EventType.CHANGE))
/*      */     {
/* 1032 */       int i = paramDefaultDocumentEvent.getOffset();
/* 1033 */       int j = i + paramDefaultDocumentEvent.getLength();
/* 1034 */       localElement1 = getParagraphElement(i).getStartOffset();
/* 1035 */       localElement2 = getParagraphElement(j).getEndOffset();
/* 1036 */     } else if (paramDefaultDocumentEvent.type == DocumentEvent.EventType.REMOVE) {
/* 1037 */       localObject1 = getParagraphElement(paramDefaultDocumentEvent.getOffset());
/* 1038 */       localElement1 = ((Element)localObject1).getStartOffset();
/* 1039 */       localElement2 = ((Element)localObject1).getEndOffset();
/*      */     } else {
/* 1041 */       throw new Error("Internal error: unknown event type.");
/*      */     }
/*      */ 
/* 1049 */     Object localObject1 = calculateBidiLevels(localElement1, localElement2);
/*      */ 
/* 1052 */     Vector localVector = new Vector();
/*      */ 
/* 1060 */     int k = localElement1;
/* 1061 */     int m = 0;
/* 1062 */     if (k > 0) {
/* 1063 */       localElement3 = this.bidiRoot.getElementIndex(localElement1 - 1);
/* 1064 */       m = localElement3;
/* 1065 */       localElement4 = this.bidiRoot.getElement(localElement3);
/* 1066 */       int i1 = StyleConstants.getBidiLevel(localElement4.getAttributes());
/*      */ 
/* 1068 */       if (i1 == localObject1[0])
/* 1069 */         k = localElement4.getStartOffset();
/* 1070 */       else if (localElement4.getEndOffset() > localElement1) {
/* 1071 */         localVector.addElement(new BidiElement(this.bidiRoot, localElement4.getStartOffset(), localElement1, i1));
/*      */       }
/*      */       else
/*      */       {
/* 1075 */         m++;
/*      */       }
/*      */     }
/*      */ 
/* 1079 */     Element localElement3 = 0;
/* 1080 */     while ((localElement3 < localObject1.length) && (localObject1[localElement3] == localObject1[0])) {
/* 1081 */       localElement3++;
/*      */     }
/*      */ 
/* 1090 */     Element localElement4 = localElement2;
/* 1091 */     BidiElement localBidiElement = null;
/* 1092 */     int i2 = this.bidiRoot.getElementCount() - 1;
/*      */     Element localElement6;
/*      */     int n;
/* 1093 */     if (localElement4 <= getLength()) {
/* 1094 */       localElement5 = this.bidiRoot.getElementIndex(localElement2);
/* 1095 */       i2 = localElement5;
/* 1096 */       localElement6 = this.bidiRoot.getElement(localElement5);
/* 1097 */       int i4 = StyleConstants.getBidiLevel(localElement6.getAttributes());
/* 1098 */       if (i4 == localObject1[(localObject1.length - 1)])
/* 1099 */         n = localElement6.getEndOffset();
/* 1100 */       else if (localElement6.getStartOffset() < localElement2) {
/* 1101 */         localBidiElement = new BidiElement(this.bidiRoot, localElement2, localElement6.getEndOffset(), i4);
/*      */       }
/*      */       else
/*      */       {
/* 1105 */         i2--;
/*      */       }
/*      */     }
/*      */ 
/* 1109 */     Element localElement5 = localObject1.length;
/*      */ 
/* 1111 */     while ((localElement5 > localElement3) && (localObject1[(localElement5 - 1)] == localObject1[(localObject1.length - 1)])) {
/* 1112 */       localElement5--;
/*      */     }
/*      */ 
/* 1119 */     if ((localElement3 == localElement5) && (localObject1[0] == localObject1[(localObject1.length - 1)])) {
/* 1120 */       localVector.addElement(new BidiElement(this.bidiRoot, k, n, localObject1[0]));
/*      */     }
/*      */     else
/*      */     {
/* 1124 */       localVector.addElement(new BidiElement(this.bidiRoot, k, localElement3 + localElement1, localObject1[0]));
/*      */ 
/* 1128 */       for (localElement6 = localElement3; localElement6 < localElement5; )
/*      */       {
/* 1131 */         for (localObject2 = localElement6; (localObject2 < localObject1.length) && (localObject1[localObject2] == localObject1[localElement6]); localObject2++);
/* 1132 */         localVector.addElement(new BidiElement(this.bidiRoot, localElement1 + localElement6, localElement1 + localObject2, localObject1[localElement6]));
/*      */ 
/* 1135 */         localElement6 = localObject2;
/*      */       }
/*      */ 
/* 1138 */       localVector.addElement(new BidiElement(this.bidiRoot, localElement5 + localElement1, n, localObject1[(localObject1.length - 1)]));
/*      */     }
/*      */ 
/* 1144 */     if (localBidiElement != null) {
/* 1145 */       localVector.addElement(localBidiElement);
/*      */     }
/*      */ 
/* 1150 */     int i3 = 0;
/* 1151 */     if (this.bidiRoot.getElementCount() > 0) {
/* 1152 */       i3 = i2 - m + 1;
/*      */     }
/* 1154 */     Object localObject2 = new Element[i3];
/* 1155 */     for (int i5 = 0; i5 < i3; i5++) {
/* 1156 */       localObject2[i5] = this.bidiRoot.getElement(m + i5);
/*      */     }
/*      */ 
/* 1159 */     Element[] arrayOfElement = new Element[localVector.size()];
/* 1160 */     localVector.copyInto(arrayOfElement);
/*      */ 
/* 1163 */     ElementEdit localElementEdit = new ElementEdit(this.bidiRoot, m, (Element[])localObject2, arrayOfElement);
/*      */ 
/* 1165 */     paramDefaultDocumentEvent.addEdit(localElementEdit);
/*      */ 
/* 1168 */     this.bidiRoot.replace(m, localObject2.length, arrayOfElement);
/*      */   }
/*      */ 
/*      */   private byte[] calculateBidiLevels(int paramInt1, int paramInt2)
/*      */   {
/* 1177 */     byte[] arrayOfByte = new byte[paramInt2 - paramInt1];
/* 1178 */     int i = 0;
/* 1179 */     Boolean localBoolean1 = null;
/* 1180 */     Object localObject = getProperty(TextAttribute.RUN_DIRECTION);
/* 1181 */     if ((localObject instanceof Boolean)) {
/* 1182 */       localBoolean1 = (Boolean)localObject;
/*      */     }
/*      */ 
/* 1187 */     for (int j = paramInt1; j < paramInt2; ) {
/* 1188 */       Element localElement = getParagraphElement(j);
/* 1189 */       int k = localElement.getStartOffset();
/* 1190 */       int m = localElement.getEndOffset();
/*      */ 
/* 1195 */       Boolean localBoolean2 = localBoolean1;
/* 1196 */       localObject = localElement.getAttributes().getAttribute(TextAttribute.RUN_DIRECTION);
/* 1197 */       if ((localObject instanceof Boolean)) {
/* 1198 */         localBoolean2 = (Boolean)localObject;
/*      */       }
/*      */ 
/* 1205 */       Segment localSegment = SegmentCache.getSharedSegment();
/*      */       try {
/* 1207 */         getText(k, m - k, localSegment);
/*      */       } catch (BadLocationException localBadLocationException) {
/* 1209 */         throw new Error("Internal error: " + localBadLocationException.toString());
/*      */       }
/*      */ 
/* 1213 */       int n = -2;
/* 1214 */       if (localBoolean2 != null) {
/* 1215 */         if (TextAttribute.RUN_DIRECTION_LTR.equals(localBoolean2))
/* 1216 */           n = 0;
/*      */         else {
/* 1218 */           n = 1;
/*      */         }
/*      */       }
/* 1221 */       Bidi localBidi = new Bidi(localSegment.array, localSegment.offset, null, 0, localSegment.count, n);
/*      */ 
/* 1223 */       BidiUtils.getLevels(localBidi, arrayOfByte, i);
/* 1224 */       i += localBidi.getLength();
/*      */ 
/* 1226 */       j = localElement.getEndOffset();
/* 1227 */       SegmentCache.releaseSharedSegment(localSegment);
/*      */     }
/*      */ 
/* 1231 */     if (i != arrayOfByte.length) {
/* 1232 */       throw new Error("levelsEnd assertion failed.");
/*      */     }
/* 1234 */     return arrayOfByte;
/*      */   }
/*      */ 
/*      */   public void dump(PrintStream paramPrintStream)
/*      */   {
/* 1243 */     Element localElement = getDefaultRootElement();
/* 1244 */     if ((localElement instanceof AbstractElement)) {
/* 1245 */       ((AbstractElement)localElement).dump(paramPrintStream, 0);
/*      */     }
/* 1247 */     this.bidiRoot.dump(paramPrintStream, 0);
/*      */   }
/*      */ 
/*      */   protected final Content getContent()
/*      */   {
/* 1256 */     return this.data;
/*      */   }
/*      */ 
/*      */   protected Element createLeafElement(Element paramElement, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
/*      */   {
/* 1275 */     return new LeafElement(paramElement, paramAttributeSet, paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   protected Element createBranchElement(Element paramElement, AttributeSet paramAttributeSet)
/*      */   {
/* 1286 */     return new BranchElement(paramElement, paramAttributeSet);
/*      */   }
/*      */ 
/*      */   protected final synchronized Thread getCurrentWriter()
/*      */   {
/* 1302 */     return this.currWriter;
/*      */   }
/*      */ 
/*      */   protected final synchronized void writeLock()
/*      */   {
/*      */     try
/*      */     {
/* 1332 */       while ((this.numReaders > 0) || (this.currWriter != null)) {
/* 1333 */         if (Thread.currentThread() == this.currWriter) {
/* 1334 */           if (this.notifyingListeners)
/*      */           {
/* 1338 */             throw new IllegalStateException("Attempt to mutate in notification");
/*      */           }
/*      */ 
/* 1341 */           this.numWriters += 1;
/* 1342 */           return;
/*      */         }
/* 1344 */         wait();
/*      */       }
/* 1346 */       this.currWriter = Thread.currentThread();
/* 1347 */       this.numWriters = 1;
/*      */     } catch (InterruptedException localInterruptedException) {
/* 1349 */       throw new Error("Interrupted attempt to aquire write lock");
/*      */     }
/*      */   }
/*      */ 
/*      */   protected final synchronized void writeUnlock()
/*      */   {
/* 1361 */     if (--this.numWriters <= 0) {
/* 1362 */       this.numWriters = 0;
/* 1363 */       this.currWriter = null;
/* 1364 */       notifyAll();
/*      */     }
/*      */   }
/*      */ 
/*      */   public final synchronized void readLock()
/*      */   {
/*      */     try
/*      */     {
/* 1381 */       while (this.currWriter != null) {
/* 1382 */         if (this.currWriter == Thread.currentThread())
/*      */         {
/* 1385 */           return;
/*      */         }
/* 1387 */         wait();
/*      */       }
/* 1389 */       this.numReaders += 1;
/*      */     } catch (InterruptedException localInterruptedException) {
/* 1391 */       throw new Error("Interrupted attempt to aquire read lock");
/*      */     }
/*      */   }
/*      */ 
/*      */   public final synchronized void readUnlock()
/*      */   {
/* 1414 */     if (this.currWriter == Thread.currentThread())
/*      */     {
/* 1417 */       return;
/*      */     }
/* 1419 */     if (this.numReaders <= 0) {
/* 1420 */       throw new StateInvariantError("document lock failure");
/*      */     }
/* 1422 */     this.numReaders -= 1;
/* 1423 */     notify();
/*      */   }
/*      */ 
/*      */   private void readObject(ObjectInputStream paramObjectInputStream)
/*      */     throws ClassNotFoundException, IOException
/*      */   {
/* 1431 */     paramObjectInputStream.defaultReadObject();
/* 1432 */     this.listenerList = new EventListenerList();
/*      */ 
/* 1437 */     this.bidiRoot = new BidiRootElement();
/*      */     try {
/* 1439 */       writeLock();
/* 1440 */       Element[] arrayOfElement = new Element[1];
/* 1441 */       arrayOfElement[0] = new BidiElement(this.bidiRoot, 0, 1, 0);
/* 1442 */       this.bidiRoot.replace(0, 0, arrayOfElement);
/*      */     } finally {
/* 1444 */       writeUnlock();
/*      */     }
/*      */ 
/* 1450 */     paramObjectInputStream.registerValidation(new ObjectInputValidation() {
/*      */       public void validateObject() {
/*      */         try {
/* 1453 */           AbstractDocument.this.writeLock();
/* 1454 */           AbstractDocument.DefaultDocumentEvent localDefaultDocumentEvent = new AbstractDocument.DefaultDocumentEvent(AbstractDocument.this, 0, AbstractDocument.this.getLength(), DocumentEvent.EventType.INSERT);
/*      */ 
/* 1457 */           AbstractDocument.this.updateBidi(localDefaultDocumentEvent);
/*      */         }
/*      */         finally {
/* 1460 */           AbstractDocument.this.writeUnlock();
/*      */         }
/*      */       }
/*      */     }
/*      */     , 0);
/*      */   }
/*      */ 
/*      */   public abstract class AbstractElement
/*      */     implements Element, MutableAttributeSet, Serializable, TreeNode
/*      */   {
/*      */     private Element parent;
/*      */     private transient AttributeSet attributes;
/*      */ 
/*      */     public AbstractElement(Element paramAttributeSet, AttributeSet arg3)
/*      */     {
/* 1774 */       this.parent = paramAttributeSet;
/* 1775 */       this.attributes = AbstractDocument.this.getAttributeContext().getEmptySet();
/*      */       AttributeSet localAttributeSet;
/* 1776 */       if (localAttributeSet != null)
/* 1777 */         addAttributes(localAttributeSet);
/*      */     }
/*      */ 
/*      */     private final void indent(PrintWriter paramPrintWriter, int paramInt)
/*      */     {
/* 1782 */       for (int i = 0; i < paramInt; i++)
/* 1783 */         paramPrintWriter.print("  ");
/*      */     }
/*      */ 
/*      */     public void dump(PrintStream paramPrintStream, int paramInt)
/*      */     {
/*      */       PrintWriter localPrintWriter;
/*      */       try
/*      */       {
/* 1796 */         localPrintWriter = new PrintWriter(new OutputStreamWriter(paramPrintStream, "JavaEsc"), true);
/*      */       }
/*      */       catch (UnsupportedEncodingException localUnsupportedEncodingException) {
/* 1799 */         localPrintWriter = new PrintWriter(paramPrintStream, true);
/*      */       }
/* 1801 */       indent(localPrintWriter, paramInt);
/* 1802 */       if (getName() == null)
/* 1803 */         localPrintWriter.print("<??");
/*      */       else
/* 1805 */         localPrintWriter.print("<" + getName());
/*      */       Object localObject1;
/*      */       Object localObject2;
/* 1807 */       if (getAttributeCount() > 0) {
/* 1808 */         localPrintWriter.println("");
/*      */ 
/* 1810 */         localObject1 = this.attributes.getAttributeNames();
/* 1811 */         while (((Enumeration)localObject1).hasMoreElements()) {
/* 1812 */           localObject2 = ((Enumeration)localObject1).nextElement();
/* 1813 */           indent(localPrintWriter, paramInt + 1);
/* 1814 */           localPrintWriter.println(localObject2 + "=" + getAttribute(localObject2));
/*      */         }
/* 1816 */         indent(localPrintWriter, paramInt);
/*      */       }
/* 1818 */       localPrintWriter.println(">");
/*      */ 
/* 1820 */       if (isLeaf()) {
/* 1821 */         indent(localPrintWriter, paramInt + 1);
/* 1822 */         localPrintWriter.print("[" + getStartOffset() + "," + getEndOffset() + "]");
/* 1823 */         localObject1 = AbstractDocument.this.getContent();
/*      */         try {
/* 1825 */           localObject2 = ((AbstractDocument.Content)localObject1).getString(getStartOffset(), getEndOffset() - getStartOffset());
/*      */ 
/* 1827 */           if (((String)localObject2).length() > 40) {
/* 1828 */             localObject2 = ((String)localObject2).substring(0, 40) + "...";
/*      */           }
/* 1830 */           localPrintWriter.println("[" + (String)localObject2 + "]");
/*      */         } catch (BadLocationException localBadLocationException) {
/*      */         }
/*      */       }
/*      */       else {
/* 1835 */         int i = getElementCount();
/* 1836 */         for (int j = 0; j < i; j++) {
/* 1837 */           AbstractElement localAbstractElement = (AbstractElement)getElement(j);
/* 1838 */           localAbstractElement.dump(paramPrintStream, paramInt + 1);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     public int getAttributeCount()
/*      */     {
/* 1853 */       return this.attributes.getAttributeCount();
/*      */     }
/*      */ 
/*      */     public boolean isDefined(Object paramObject)
/*      */     {
/* 1864 */       return this.attributes.isDefined(paramObject);
/*      */     }
/*      */ 
/*      */     public boolean isEqual(AttributeSet paramAttributeSet)
/*      */     {
/* 1875 */       return this.attributes.isEqual(paramAttributeSet);
/*      */     }
/*      */ 
/*      */     public AttributeSet copyAttributes()
/*      */     {
/* 1885 */       return this.attributes.copyAttributes();
/*      */     }
/*      */ 
/*      */     public Object getAttribute(Object paramObject)
/*      */     {
/* 1896 */       Object localObject1 = this.attributes.getAttribute(paramObject);
/* 1897 */       if (localObject1 == null)
/*      */       {
/* 1901 */         Object localObject2 = this.parent != null ? this.parent.getAttributes() : null;
/* 1902 */         if (localObject2 != null) {
/* 1903 */           localObject1 = localObject2.getAttribute(paramObject);
/*      */         }
/*      */       }
/* 1906 */       return localObject1;
/*      */     }
/*      */ 
/*      */     public Enumeration<?> getAttributeNames()
/*      */     {
/* 1916 */       return this.attributes.getAttributeNames();
/*      */     }
/*      */ 
/*      */     public boolean containsAttribute(Object paramObject1, Object paramObject2)
/*      */     {
/* 1928 */       return this.attributes.containsAttribute(paramObject1, paramObject2);
/*      */     }
/*      */ 
/*      */     public boolean containsAttributes(AttributeSet paramAttributeSet)
/*      */     {
/* 1940 */       return this.attributes.containsAttributes(paramAttributeSet);
/*      */     }
/*      */ 
/*      */     public AttributeSet getResolveParent()
/*      */     {
/* 1952 */       AttributeSet localAttributeSet = this.attributes.getResolveParent();
/* 1953 */       if ((localAttributeSet == null) && (this.parent != null)) {
/* 1954 */         localAttributeSet = this.parent.getAttributes();
/*      */       }
/* 1956 */       return localAttributeSet;
/*      */     }
/*      */ 
/*      */     public void addAttribute(Object paramObject1, Object paramObject2)
/*      */     {
/* 1971 */       checkForIllegalCast();
/* 1972 */       AbstractDocument.AttributeContext localAttributeContext = AbstractDocument.this.getAttributeContext();
/* 1973 */       this.attributes = localAttributeContext.addAttribute(this.attributes, paramObject1, paramObject2);
/*      */     }
/*      */ 
/*      */     public void addAttributes(AttributeSet paramAttributeSet)
/*      */     {
/* 1983 */       checkForIllegalCast();
/* 1984 */       AbstractDocument.AttributeContext localAttributeContext = AbstractDocument.this.getAttributeContext();
/* 1985 */       this.attributes = localAttributeContext.addAttributes(this.attributes, paramAttributeSet);
/*      */     }
/*      */ 
/*      */     public void removeAttribute(Object paramObject)
/*      */     {
/* 1995 */       checkForIllegalCast();
/* 1996 */       AbstractDocument.AttributeContext localAttributeContext = AbstractDocument.this.getAttributeContext();
/* 1997 */       this.attributes = localAttributeContext.removeAttribute(this.attributes, paramObject);
/*      */     }
/*      */ 
/*      */     public void removeAttributes(Enumeration<?> paramEnumeration)
/*      */     {
/* 2007 */       checkForIllegalCast();
/* 2008 */       AbstractDocument.AttributeContext localAttributeContext = AbstractDocument.this.getAttributeContext();
/* 2009 */       this.attributes = localAttributeContext.removeAttributes(this.attributes, paramEnumeration);
/*      */     }
/*      */ 
/*      */     public void removeAttributes(AttributeSet paramAttributeSet)
/*      */     {
/* 2019 */       checkForIllegalCast();
/* 2020 */       AbstractDocument.AttributeContext localAttributeContext = AbstractDocument.this.getAttributeContext();
/* 2021 */       if (paramAttributeSet == this)
/* 2022 */         this.attributes = localAttributeContext.getEmptySet();
/*      */       else
/* 2024 */         this.attributes = localAttributeContext.removeAttributes(this.attributes, paramAttributeSet);
/*      */     }
/*      */ 
/*      */     public void setResolveParent(AttributeSet paramAttributeSet)
/*      */     {
/* 2035 */       checkForIllegalCast();
/* 2036 */       AbstractDocument.AttributeContext localAttributeContext = AbstractDocument.this.getAttributeContext();
/* 2037 */       if (paramAttributeSet != null) {
/* 2038 */         this.attributes = localAttributeContext.addAttribute(this.attributes, StyleConstants.ResolveAttribute, paramAttributeSet);
/*      */       }
/*      */       else
/*      */       {
/* 2042 */         this.attributes = localAttributeContext.removeAttribute(this.attributes, StyleConstants.ResolveAttribute);
/*      */       }
/*      */     }
/*      */ 
/*      */     private final void checkForIllegalCast()
/*      */     {
/* 2048 */       Thread localThread = AbstractDocument.this.getCurrentWriter();
/* 2049 */       if ((localThread == null) || (localThread != Thread.currentThread()))
/* 2050 */         throw new StateInvariantError("Illegal cast to MutableAttributeSet");
/*      */     }
/*      */ 
/*      */     public Document getDocument()
/*      */     {
/* 2062 */       return AbstractDocument.this;
/*      */     }
/*      */ 
/*      */     public Element getParentElement()
/*      */     {
/* 2071 */       return this.parent;
/*      */     }
/*      */ 
/*      */     public AttributeSet getAttributes()
/*      */     {
/* 2080 */       return this;
/*      */     }
/*      */ 
/*      */     public String getName()
/*      */     {
/* 2089 */       if (this.attributes.isDefined("$ename")) {
/* 2090 */         return (String)this.attributes.getAttribute("$ename");
/*      */       }
/* 2092 */       return null;
/*      */     }
/*      */ 
/*      */     public abstract int getStartOffset();
/*      */ 
/*      */     public abstract int getEndOffset();
/*      */ 
/*      */     public abstract Element getElement(int paramInt);
/*      */ 
/*      */     public abstract int getElementCount();
/*      */ 
/*      */     public abstract int getElementIndex(int paramInt);
/*      */ 
/*      */     public abstract boolean isLeaf();
/*      */ 
/*      */     public TreeNode getChildAt(int paramInt)
/*      */     {
/* 2146 */       return (TreeNode)getElement(paramInt);
/*      */     }
/*      */ 
/*      */     public int getChildCount()
/*      */     {
/* 2156 */       return getElementCount();
/*      */     }
/*      */ 
/*      */     public TreeNode getParent()
/*      */     {
/* 2164 */       return (TreeNode)getParentElement();
/*      */     }
/*      */ 
/*      */     public int getIndex(TreeNode paramTreeNode)
/*      */     {
/* 2176 */       for (int i = getChildCount() - 1; i >= 0; i--)
/* 2177 */         if (getChildAt(i) == paramTreeNode)
/* 2178 */           return i;
/* 2179 */       return -1;
/*      */     }
/*      */ 
/*      */     public abstract boolean getAllowsChildren();
/*      */ 
/*      */     public abstract Enumeration children();
/*      */ 
/*      */     private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*      */       throws IOException
/*      */     {
/* 2200 */       paramObjectOutputStream.defaultWriteObject();
/* 2201 */       StyleContext.writeAttributeSet(paramObjectOutputStream, this.attributes);
/*      */     }
/*      */ 
/*      */     private void readObject(ObjectInputStream paramObjectInputStream)
/*      */       throws ClassNotFoundException, IOException
/*      */     {
/* 2207 */       paramObjectInputStream.defaultReadObject();
/* 2208 */       SimpleAttributeSet localSimpleAttributeSet = new SimpleAttributeSet();
/* 2209 */       StyleContext.readAttributeSet(paramObjectInputStream, localSimpleAttributeSet);
/* 2210 */       AbstractDocument.AttributeContext localAttributeContext = AbstractDocument.this.getAttributeContext();
/* 2211 */       this.attributes = localAttributeContext.addAttributes(SimpleAttributeSet.EMPTY, localSimpleAttributeSet);
/*      */     }
/*      */   }
/*      */ 
/*      */   public static abstract interface AttributeContext
/*      */   {
/*      */     public abstract AttributeSet addAttribute(AttributeSet paramAttributeSet, Object paramObject1, Object paramObject2);
/*      */ 
/*      */     public abstract AttributeSet addAttributes(AttributeSet paramAttributeSet1, AttributeSet paramAttributeSet2);
/*      */ 
/*      */     public abstract AttributeSet removeAttribute(AttributeSet paramAttributeSet, Object paramObject);
/*      */ 
/*      */     public abstract AttributeSet removeAttributes(AttributeSet paramAttributeSet, Enumeration<?> paramEnumeration);
/*      */ 
/*      */     public abstract AttributeSet removeAttributes(AttributeSet paramAttributeSet1, AttributeSet paramAttributeSet2);
/*      */ 
/*      */     public abstract AttributeSet getEmptySet();
/*      */ 
/*      */     public abstract void reclaim(AttributeSet paramAttributeSet);
/*      */   }
/*      */ 
/*      */   class BidiElement extends AbstractDocument.LeafElement
/*      */   {
/*      */     BidiElement(Element paramInt1, int paramInt2, int paramInt3, int arg5)
/*      */     {
/* 2674 */       super(paramInt1, new SimpleAttributeSet(), paramInt2, paramInt3);
/*      */       int i;
/* 2675 */       addAttribute(StyleConstants.BidiLevel, Integer.valueOf(i));
/*      */     }
/*      */ 
/*      */     public String getName()
/*      */     {
/* 2685 */       return "bidi level";
/*      */     }
/*      */ 
/*      */     int getLevel() {
/* 2689 */       Integer localInteger = (Integer)getAttribute(StyleConstants.BidiLevel);
/* 2690 */       if (localInteger != null) {
/* 2691 */         return localInteger.intValue();
/*      */       }
/* 2693 */       return 0;
/*      */     }
/*      */ 
/*      */     boolean isLeftToRight() {
/* 2697 */       return getLevel() % 2 == 0;
/*      */     }
/*      */   }
/*      */ 
/*      */   class BidiRootElement extends AbstractDocument.BranchElement
/*      */   {
/*      */     BidiRootElement()
/*      */     {
/* 2653 */       super(null, null);
/*      */     }
/*      */ 
/*      */     public String getName()
/*      */     {
/* 2661 */       return "bidi root";
/*      */     }
/*      */   }
/*      */ 
/*      */   public class BranchElement extends AbstractDocument.AbstractElement
/*      */   {
/*      */     private AbstractDocument.AbstractElement[] children;
/*      */     private int nchildren;
/*      */     private int lastIndex;
/*      */ 
/*      */     public BranchElement(Element paramAttributeSet, AttributeSet arg3)
/*      */     {
/* 2244 */       super(paramAttributeSet, localAttributeSet);
/* 2245 */       this.children = new AbstractDocument.AbstractElement[1];
/* 2246 */       this.nchildren = 0;
/* 2247 */       this.lastIndex = -1;
/*      */     }
/*      */ 
/*      */     public Element positionToElement(int paramInt)
/*      */     {
/* 2258 */       int i = getElementIndex(paramInt);
/* 2259 */       AbstractDocument.AbstractElement localAbstractElement = this.children[i];
/* 2260 */       int j = localAbstractElement.getStartOffset();
/* 2261 */       int k = localAbstractElement.getEndOffset();
/* 2262 */       if ((paramInt >= j) && (paramInt < k)) {
/* 2263 */         return localAbstractElement;
/*      */       }
/* 2265 */       return null;
/*      */     }
/*      */ 
/*      */     public void replace(int paramInt1, int paramInt2, Element[] paramArrayOfElement)
/*      */     {
/* 2276 */       int i = paramArrayOfElement.length - paramInt2;
/* 2277 */       int j = paramInt1 + paramInt2;
/* 2278 */       int k = this.nchildren - j;
/* 2279 */       int m = j + i;
/* 2280 */       if (this.nchildren + i >= this.children.length)
/*      */       {
/* 2282 */         int n = Math.max(2 * this.children.length, this.nchildren + i);
/* 2283 */         AbstractDocument.AbstractElement[] arrayOfAbstractElement = new AbstractDocument.AbstractElement[n];
/* 2284 */         System.arraycopy(this.children, 0, arrayOfAbstractElement, 0, paramInt1);
/* 2285 */         System.arraycopy(paramArrayOfElement, 0, arrayOfAbstractElement, paramInt1, paramArrayOfElement.length);
/* 2286 */         System.arraycopy(this.children, j, arrayOfAbstractElement, m, k);
/* 2287 */         this.children = arrayOfAbstractElement;
/*      */       }
/*      */       else {
/* 2290 */         System.arraycopy(this.children, j, this.children, m, k);
/* 2291 */         System.arraycopy(paramArrayOfElement, 0, this.children, paramInt1, paramArrayOfElement.length);
/*      */       }
/* 2293 */       this.nchildren += i;
/*      */     }
/*      */ 
/*      */     public String toString()
/*      */     {
/* 2302 */       return "BranchElement(" + getName() + ") " + getStartOffset() + "," + getEndOffset() + "\n";
/*      */     }
/*      */ 
/*      */     public String getName()
/*      */     {
/* 2314 */       String str = super.getName();
/* 2315 */       if (str == null) {
/* 2316 */         str = "paragraph";
/*      */       }
/* 2318 */       return str;
/*      */     }
/*      */ 
/*      */     public int getStartOffset()
/*      */     {
/* 2327 */       return this.children[0].getStartOffset();
/*      */     }
/*      */ 
/*      */     public int getEndOffset()
/*      */     {
/* 2337 */       AbstractDocument.AbstractElement localAbstractElement = this.nchildren > 0 ? this.children[(this.nchildren - 1)] : this.children[0];
/*      */ 
/* 2339 */       return localAbstractElement.getEndOffset();
/*      */     }
/*      */ 
/*      */     public Element getElement(int paramInt)
/*      */     {
/* 2349 */       if (paramInt < this.nchildren) {
/* 2350 */         return this.children[paramInt];
/*      */       }
/* 2352 */       return null;
/*      */     }
/*      */ 
/*      */     public int getElementCount()
/*      */     {
/* 2361 */       return this.nchildren;
/*      */     }
/*      */ 
/*      */     public int getElementIndex(int paramInt)
/*      */     {
/* 2372 */       int j = 0;
/* 2373 */       int k = this.nchildren - 1;
/* 2374 */       int m = 0;
/* 2375 */       int n = getStartOffset();
/*      */ 
/* 2378 */       if (this.nchildren == 0) {
/* 2379 */         return 0;
/*      */       }
/* 2381 */       if (paramInt >= getEndOffset())
/* 2382 */         return this.nchildren - 1;
/*      */       AbstractDocument.AbstractElement localAbstractElement;
/*      */       int i1;
/* 2386 */       if ((this.lastIndex >= j) && (this.lastIndex <= k)) {
/* 2387 */         localAbstractElement = this.children[this.lastIndex];
/* 2388 */         n = localAbstractElement.getStartOffset();
/* 2389 */         i1 = localAbstractElement.getEndOffset();
/* 2390 */         if ((paramInt >= n) && (paramInt < i1)) {
/* 2391 */           return this.lastIndex;
/*      */         }
/*      */ 
/* 2396 */         if (paramInt < n)
/* 2397 */           k = this.lastIndex;
/*      */         else
/* 2399 */           j = this.lastIndex;
/*      */       }
/*      */       int i;
/* 2403 */       while (j <= k) {
/* 2404 */         m = j + (k - j) / 2;
/* 2405 */         localAbstractElement = this.children[m];
/* 2406 */         n = localAbstractElement.getStartOffset();
/* 2407 */         i1 = localAbstractElement.getEndOffset();
/* 2408 */         if ((paramInt >= n) && (paramInt < i1))
/*      */         {
/* 2410 */           i = m;
/* 2411 */           this.lastIndex = i;
/* 2412 */           return i;
/* 2413 */         }if (paramInt < n)
/* 2414 */           k = m - 1;
/*      */         else {
/* 2416 */           j = m + 1;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 2421 */       if (paramInt < n)
/* 2422 */         i = m;
/*      */       else {
/* 2424 */         i = m + 1;
/*      */       }
/* 2426 */       this.lastIndex = i;
/* 2427 */       return i;
/*      */     }
/*      */ 
/*      */     public boolean isLeaf()
/*      */     {
/* 2436 */       return false;
/*      */     }
/*      */ 
/*      */     public boolean getAllowsChildren()
/*      */     {
/* 2447 */       return true;
/*      */     }
/*      */ 
/*      */     public Enumeration children()
/*      */     {
/* 2457 */       if (this.nchildren == 0) {
/* 2458 */         return null;
/*      */       }
/* 2460 */       Vector localVector = new Vector(this.nchildren);
/*      */ 
/* 2462 */       for (int i = 0; i < this.nchildren; i++)
/* 2463 */         localVector.addElement(this.children[i]);
/* 2464 */       return localVector.elements();
/*      */     }
/*      */   }
/*      */ 
/*      */   public static abstract interface Content
/*      */   {
/*      */     public abstract Position createPosition(int paramInt)
/*      */       throws BadLocationException;
/*      */ 
/*      */     public abstract int length();
/*      */ 
/*      */     public abstract UndoableEdit insertString(int paramInt, String paramString)
/*      */       throws BadLocationException;
/*      */ 
/*      */     public abstract UndoableEdit remove(int paramInt1, int paramInt2)
/*      */       throws BadLocationException;
/*      */ 
/*      */     public abstract String getString(int paramInt1, int paramInt2)
/*      */       throws BadLocationException;
/*      */ 
/*      */     public abstract void getChars(int paramInt1, int paramInt2, Segment paramSegment)
/*      */       throws BadLocationException;
/*      */   }
/*      */ 
/*      */   public class DefaultDocumentEvent extends CompoundEdit
/*      */     implements DocumentEvent
/*      */   {
/*      */     private int offset;
/*      */     private int length;
/*      */     private Hashtable<Element, DocumentEvent.ElementChange> changeLookup;
/*      */     private DocumentEvent.EventType type;
/*      */ 
/*      */     public DefaultDocumentEvent(int paramInt1, int paramEventType, DocumentEvent.EventType arg4)
/*      */     {
/* 2720 */       this.offset = paramInt1;
/* 2721 */       this.length = paramEventType;
/*      */       Object localObject;
/* 2722 */       this.type = localObject;
/*      */     }
/*      */ 
/*      */     public String toString()
/*      */     {
/* 2731 */       return this.edits.toString();
/*      */     }
/*      */ 
/*      */     public boolean addEdit(UndoableEdit paramUndoableEdit)
/*      */     {
/* 2748 */       if ((this.changeLookup == null) && (this.edits.size() > 10)) {
/* 2749 */         this.changeLookup = new Hashtable();
/* 2750 */         int i = this.edits.size();
/* 2751 */         for (int j = 0; j < i; j++) {
/* 2752 */           Object localObject = this.edits.elementAt(j);
/* 2753 */           if ((localObject instanceof DocumentEvent.ElementChange)) {
/* 2754 */             DocumentEvent.ElementChange localElementChange2 = (DocumentEvent.ElementChange)localObject;
/* 2755 */             this.changeLookup.put(localElementChange2.getElement(), localElementChange2);
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 2762 */       if ((this.changeLookup != null) && ((paramUndoableEdit instanceof DocumentEvent.ElementChange))) {
/* 2763 */         DocumentEvent.ElementChange localElementChange1 = (DocumentEvent.ElementChange)paramUndoableEdit;
/* 2764 */         this.changeLookup.put(localElementChange1.getElement(), localElementChange1);
/*      */       }
/* 2766 */       return super.addEdit(paramUndoableEdit);
/*      */     }
/*      */ 
/*      */     public void redo()
/*      */       throws CannotRedoException
/*      */     {
/* 2775 */       AbstractDocument.this.writeLock();
/*      */       try
/*      */       {
/* 2778 */         super.redo();
/*      */ 
/* 2780 */         AbstractDocument.UndoRedoDocumentEvent localUndoRedoDocumentEvent = new AbstractDocument.UndoRedoDocumentEvent(AbstractDocument.this, this, false);
/* 2781 */         if (this.type == DocumentEvent.EventType.INSERT)
/* 2782 */           AbstractDocument.this.fireInsertUpdate(localUndoRedoDocumentEvent);
/* 2783 */         else if (this.type == DocumentEvent.EventType.REMOVE)
/* 2784 */           AbstractDocument.this.fireRemoveUpdate(localUndoRedoDocumentEvent);
/*      */         else
/* 2786 */           AbstractDocument.this.fireChangedUpdate(localUndoRedoDocumentEvent);
/*      */       }
/*      */       finally {
/* 2789 */         AbstractDocument.this.writeUnlock();
/*      */       }
/*      */     }
/*      */ 
/*      */     public void undo()
/*      */       throws CannotUndoException
/*      */     {
/* 2799 */       AbstractDocument.this.writeLock();
/*      */       try
/*      */       {
/* 2802 */         super.undo();
/*      */ 
/* 2804 */         AbstractDocument.UndoRedoDocumentEvent localUndoRedoDocumentEvent = new AbstractDocument.UndoRedoDocumentEvent(AbstractDocument.this, this, true);
/* 2805 */         if (this.type == DocumentEvent.EventType.REMOVE)
/* 2806 */           AbstractDocument.this.fireInsertUpdate(localUndoRedoDocumentEvent);
/* 2807 */         else if (this.type == DocumentEvent.EventType.INSERT)
/* 2808 */           AbstractDocument.this.fireRemoveUpdate(localUndoRedoDocumentEvent);
/*      */         else
/* 2810 */           AbstractDocument.this.fireChangedUpdate(localUndoRedoDocumentEvent);
/*      */       }
/*      */       finally {
/* 2813 */         AbstractDocument.this.writeUnlock();
/*      */       }
/*      */     }
/*      */ 
/*      */     public boolean isSignificant()
/*      */     {
/* 2825 */       return true;
/*      */     }
/*      */ 
/*      */     public String getPresentationName()
/*      */     {
/* 2836 */       DocumentEvent.EventType localEventType = getType();
/* 2837 */       if (localEventType == DocumentEvent.EventType.INSERT)
/* 2838 */         return UIManager.getString("AbstractDocument.additionText");
/* 2839 */       if (localEventType == DocumentEvent.EventType.REMOVE)
/* 2840 */         return UIManager.getString("AbstractDocument.deletionText");
/* 2841 */       return UIManager.getString("AbstractDocument.styleChangeText");
/*      */     }
/*      */ 
/*      */     public String getUndoPresentationName()
/*      */     {
/* 2852 */       return UIManager.getString("AbstractDocument.undoText") + " " + getPresentationName();
/*      */     }
/*      */ 
/*      */     public String getRedoPresentationName()
/*      */     {
/* 2864 */       return UIManager.getString("AbstractDocument.redoText") + " " + getPresentationName();
/*      */     }
/*      */ 
/*      */     public DocumentEvent.EventType getType()
/*      */     {
/* 2877 */       return this.type;
/*      */     }
/*      */ 
/*      */     public int getOffset()
/*      */     {
/* 2887 */       return this.offset;
/*      */     }
/*      */ 
/*      */     public int getLength()
/*      */     {
/* 2897 */       return this.length;
/*      */     }
/*      */ 
/*      */     public Document getDocument()
/*      */     {
/* 2907 */       return AbstractDocument.this;
/*      */     }
/*      */ 
/*      */     public DocumentEvent.ElementChange getChange(Element paramElement)
/*      */     {
/* 2917 */       if (this.changeLookup != null) {
/* 2918 */         return (DocumentEvent.ElementChange)this.changeLookup.get(paramElement);
/*      */       }
/* 2920 */       int i = this.edits.size();
/* 2921 */       for (int j = 0; j < i; j++) {
/* 2922 */         Object localObject = this.edits.elementAt(j);
/* 2923 */         if ((localObject instanceof DocumentEvent.ElementChange)) {
/* 2924 */           DocumentEvent.ElementChange localElementChange = (DocumentEvent.ElementChange)localObject;
/* 2925 */           if (paramElement.equals(localElementChange.getElement())) {
/* 2926 */             return localElementChange;
/*      */           }
/*      */         }
/*      */       }
/* 2930 */       return null;
/*      */     }
/*      */   }
/*      */ 
/*      */   private class DefaultFilterBypass extends DocumentFilter.FilterBypass
/*      */   {
/*      */     private DefaultFilterBypass()
/*      */     {
/*      */     }
/*      */ 
/*      */     public Document getDocument()
/*      */     {
/* 3098 */       return AbstractDocument.this;
/*      */     }
/*      */ 
/*      */     public void remove(int paramInt1, int paramInt2) throws BadLocationException
/*      */     {
/* 3103 */       AbstractDocument.this.handleRemove(paramInt1, paramInt2);
/*      */     }
/*      */ 
/*      */     public void insertString(int paramInt, String paramString, AttributeSet paramAttributeSet)
/*      */       throws BadLocationException
/*      */     {
/* 3109 */       AbstractDocument.this.handleInsertString(paramInt, paramString, paramAttributeSet);
/*      */     }
/*      */ 
/*      */     public void replace(int paramInt1, int paramInt2, String paramString, AttributeSet paramAttributeSet) throws BadLocationException
/*      */     {
/* 3114 */       AbstractDocument.this.handleRemove(paramInt1, paramInt2);
/* 3115 */       AbstractDocument.this.handleInsertString(paramInt1, paramString, paramAttributeSet);
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class ElementEdit extends AbstractUndoableEdit
/*      */     implements DocumentEvent.ElementChange
/*      */   {
/*      */     private Element e;
/*      */     private int index;
/*      */     private Element[] removed;
/*      */     private Element[] added;
/*      */ 
/*      */     public ElementEdit(Element paramElement, int paramInt, Element[] paramArrayOfElement1, Element[] paramArrayOfElement2)
/*      */     {
/* 3014 */       this.e = paramElement;
/* 3015 */       this.index = paramInt;
/* 3016 */       this.removed = paramArrayOfElement1;
/* 3017 */       this.added = paramArrayOfElement2;
/*      */     }
/*      */ 
/*      */     public Element getElement()
/*      */     {
/* 3026 */       return this.e;
/*      */     }
/*      */ 
/*      */     public int getIndex()
/*      */     {
/* 3035 */       return this.index;
/*      */     }
/*      */ 
/*      */     public Element[] getChildrenRemoved()
/*      */     {
/* 3044 */       return this.removed;
/*      */     }
/*      */ 
/*      */     public Element[] getChildrenAdded()
/*      */     {
/* 3053 */       return this.added;
/*      */     }
/*      */ 
/*      */     public void redo()
/*      */       throws CannotRedoException
/*      */     {
/* 3062 */       super.redo();
/*      */ 
/* 3065 */       Element[] arrayOfElement = this.removed;
/* 3066 */       this.removed = this.added;
/* 3067 */       this.added = arrayOfElement;
/*      */ 
/* 3070 */       ((AbstractDocument.BranchElement)this.e).replace(this.index, this.removed.length, this.added);
/*      */     }
/*      */ 
/*      */     public void undo()
/*      */       throws CannotUndoException
/*      */     {
/* 3079 */       super.undo();
/*      */ 
/* 3081 */       ((AbstractDocument.BranchElement)this.e).replace(this.index, this.added.length, this.removed);
/*      */ 
/* 3084 */       Element[] arrayOfElement = this.removed;
/* 3085 */       this.removed = this.added;
/* 3086 */       this.added = arrayOfElement;
/*      */     }
/*      */   }
/*      */ 
/*      */   public class LeafElement extends AbstractDocument.AbstractElement
/*      */   {
/*      */     private transient Position p0;
/*      */     private transient Position p1;
/*      */ 
/*      */     public LeafElement(Element paramAttributeSet, AttributeSet paramInt1, int paramInt2, int arg5)
/*      */     {
/* 2502 */       super(paramAttributeSet, paramInt1);
/*      */       try {
/* 2504 */         this.p0 = AbstractDocument.this.createPosition(paramInt2);
/*      */         int i;
/* 2505 */         this.p1 = AbstractDocument.this.createPosition(i);
/*      */       } catch (BadLocationException localBadLocationException) {
/* 2507 */         this.p0 = null;
/* 2508 */         this.p1 = null;
/* 2509 */         throw new StateInvariantError("Can't create Position references");
/*      */       }
/*      */     }
/*      */ 
/*      */     public String toString()
/*      */     {
/* 2519 */       return "LeafElement(" + getName() + ") " + this.p0 + "," + this.p1 + "\n";
/*      */     }
/*      */ 
/*      */     public int getStartOffset()
/*      */     {
/* 2530 */       return this.p0.getOffset();
/*      */     }
/*      */ 
/*      */     public int getEndOffset()
/*      */     {
/* 2539 */       return this.p1.getOffset();
/*      */     }
/*      */ 
/*      */     public String getName()
/*      */     {
/* 2548 */       String str = super.getName();
/* 2549 */       if (str == null) {
/* 2550 */         str = "content";
/*      */       }
/* 2552 */       return str;
/*      */     }
/*      */ 
/*      */     public int getElementIndex(int paramInt)
/*      */     {
/* 2562 */       return -1;
/*      */     }
/*      */ 
/*      */     public Element getElement(int paramInt)
/*      */     {
/* 2572 */       return null;
/*      */     }
/*      */ 
/*      */     public int getElementCount()
/*      */     {
/* 2581 */       return 0;
/*      */     }
/*      */ 
/*      */     public boolean isLeaf()
/*      */     {
/* 2590 */       return true;
/*      */     }
/*      */ 
/*      */     public boolean getAllowsChildren()
/*      */     {
/* 2600 */       return false;
/*      */     }
/*      */ 
/*      */     public Enumeration children()
/*      */     {
/* 2610 */       return null;
/*      */     }
/*      */ 
/*      */     private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*      */       throws IOException
/*      */     {
/* 2616 */       paramObjectOutputStream.defaultWriteObject();
/* 2617 */       paramObjectOutputStream.writeInt(this.p0.getOffset());
/* 2618 */       paramObjectOutputStream.writeInt(this.p1.getOffset());
/*      */     }
/*      */ 
/*      */     private void readObject(ObjectInputStream paramObjectInputStream)
/*      */       throws ClassNotFoundException, IOException
/*      */     {
/* 2624 */       paramObjectInputStream.defaultReadObject();
/*      */ 
/* 2627 */       int i = paramObjectInputStream.readInt();
/* 2628 */       int j = paramObjectInputStream.readInt();
/*      */       try {
/* 2630 */         this.p0 = AbstractDocument.this.createPosition(i);
/* 2631 */         this.p1 = AbstractDocument.this.createPosition(j);
/*      */       } catch (BadLocationException localBadLocationException) {
/* 2633 */         this.p0 = null;
/* 2634 */         this.p1 = null;
/* 2635 */         throw new IOException("Can't restore Position references");
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   class UndoRedoDocumentEvent
/*      */     implements DocumentEvent
/*      */   {
/* 2949 */     private AbstractDocument.DefaultDocumentEvent src = null;
/*      */     private boolean isUndo;
/* 2951 */     private DocumentEvent.EventType type = null;
/*      */ 
/*      */     public UndoRedoDocumentEvent(AbstractDocument.DefaultDocumentEvent paramBoolean, boolean arg3) {
/* 2954 */       this.src = paramBoolean;
/*      */       boolean bool;
/* 2955 */       this.isUndo = bool;
/* 2956 */       if (bool) {
/* 2957 */         if (paramBoolean.getType().equals(DocumentEvent.EventType.INSERT))
/* 2958 */           this.type = DocumentEvent.EventType.REMOVE;
/* 2959 */         else if (paramBoolean.getType().equals(DocumentEvent.EventType.REMOVE))
/* 2960 */           this.type = DocumentEvent.EventType.INSERT;
/*      */         else
/* 2962 */           this.type = paramBoolean.getType();
/*      */       }
/*      */       else
/* 2965 */         this.type = paramBoolean.getType();
/*      */     }
/*      */ 
/*      */     public AbstractDocument.DefaultDocumentEvent getSource()
/*      */     {
/* 2970 */       return this.src;
/*      */     }
/*      */ 
/*      */     public int getOffset()
/*      */     {
/* 2976 */       return this.src.getOffset();
/*      */     }
/*      */ 
/*      */     public int getLength() {
/* 2980 */       return this.src.getLength();
/*      */     }
/*      */ 
/*      */     public Document getDocument() {
/* 2984 */       return this.src.getDocument();
/*      */     }
/*      */ 
/*      */     public DocumentEvent.EventType getType() {
/* 2988 */       return this.type;
/*      */     }
/*      */ 
/*      */     public DocumentEvent.ElementChange getChange(Element paramElement) {
/* 2992 */       return this.src.getChange(paramElement);
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.text.AbstractDocument
 * JD-Core Version:    0.6.2
 */