/*      */ package javax.swing.text.html;
/*      */ 
/*      */ import java.awt.Color;
/*      */ import java.awt.Component;
/*      */ import java.awt.Container;
/*      */ import java.awt.Cursor;
/*      */ import java.awt.Dimension;
/*      */ import java.awt.Font;
/*      */ import java.awt.FontMetrics;
/*      */ import java.awt.IllegalComponentStateException;
/*      */ import java.awt.Image;
/*      */ import java.awt.Insets;
/*      */ import java.awt.Point;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.Shape;
/*      */ import java.awt.event.FocusListener;
/*      */ import java.beans.PropertyChangeEvent;
/*      */ import java.beans.PropertyChangeListener;
/*      */ import java.text.BreakIterator;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Locale;
/*      */ import java.util.Vector;
/*      */ import javax.accessibility.Accessible;
/*      */ import javax.accessibility.AccessibleComponent;
/*      */ import javax.accessibility.AccessibleContext;
/*      */ import javax.accessibility.AccessibleIcon;
/*      */ import javax.accessibility.AccessibleRole;
/*      */ import javax.accessibility.AccessibleState;
/*      */ import javax.accessibility.AccessibleStateSet;
/*      */ import javax.accessibility.AccessibleTable;
/*      */ import javax.accessibility.AccessibleText;
/*      */ import javax.swing.JEditorPane;
/*      */ import javax.swing.event.DocumentEvent;
/*      */ import javax.swing.event.DocumentEvent.EventType;
/*      */ import javax.swing.event.DocumentListener;
/*      */ import javax.swing.plaf.TextUI;
/*      */ import javax.swing.text.AbstractDocument;
/*      */ import javax.swing.text.AttributeSet;
/*      */ import javax.swing.text.BadLocationException;
/*      */ import javax.swing.text.Document;
/*      */ import javax.swing.text.Element;
/*      */ import javax.swing.text.JTextComponent;
/*      */ import javax.swing.text.PlainDocument;
/*      */ import javax.swing.text.Position.Bias;
/*      */ import javax.swing.text.Segment;
/*      */ import javax.swing.text.StyleConstants;
/*      */ import javax.swing.text.StyledDocument;
/*      */ import javax.swing.text.View;
/*      */ 
/*      */ class AccessibleHTML
/*      */   implements Accessible
/*      */ {
/*      */   private JEditorPane editor;
/*      */   private Document model;
/*      */   private DocumentListener docListener;
/*      */   private PropertyChangeListener propChangeListener;
/*      */   private ElementInfo rootElementInfo;
/*      */   private RootHTMLAccessibleContext rootHTMLAccessibleContext;
/*      */ 
/*      */   public AccessibleHTML(JEditorPane paramJEditorPane)
/*      */   {
/*   72 */     this.editor = paramJEditorPane;
/*   73 */     this.propChangeListener = new PropertyChangeHandler(null);
/*   74 */     setDocument(this.editor.getDocument());
/*      */ 
/*   76 */     this.docListener = new DocumentHandler(null);
/*      */   }
/*      */ 
/*      */   private void setDocument(Document paramDocument)
/*      */   {
/*   83 */     if (this.model != null) {
/*   84 */       this.model.removeDocumentListener(this.docListener);
/*      */     }
/*   86 */     if (this.editor != null) {
/*   87 */       this.editor.removePropertyChangeListener(this.propChangeListener);
/*      */     }
/*   89 */     this.model = paramDocument;
/*   90 */     if (this.model != null) {
/*   91 */       if (this.rootElementInfo != null) {
/*   92 */         this.rootElementInfo.invalidate(false);
/*      */       }
/*   94 */       buildInfo();
/*   95 */       this.model.addDocumentListener(this.docListener);
/*      */     }
/*      */     else {
/*   98 */       this.rootElementInfo = null;
/*      */     }
/*  100 */     if (this.editor != null)
/*  101 */       this.editor.addPropertyChangeListener(this.propChangeListener);
/*      */   }
/*      */ 
/*      */   private Document getDocument()
/*      */   {
/*  109 */     return this.model;
/*      */   }
/*      */ 
/*      */   private JEditorPane getTextComponent()
/*      */   {
/*  116 */     return this.editor;
/*      */   }
/*      */ 
/*      */   private ElementInfo getRootInfo()
/*      */   {
/*  123 */     return this.rootElementInfo;
/*      */   }
/*      */ 
/*      */   private View getRootView()
/*      */   {
/*  131 */     return getTextComponent().getUI().getRootView(getTextComponent());
/*      */   }
/*      */ 
/*      */   private Rectangle getRootEditorRect()
/*      */   {
/*  138 */     Rectangle localRectangle = getTextComponent().getBounds();
/*  139 */     if ((localRectangle.width > 0) && (localRectangle.height > 0)) {
/*  140 */       localRectangle.x = (localRectangle.y = 0);
/*  141 */       Insets localInsets = this.editor.getInsets();
/*  142 */       localRectangle.x += localInsets.left;
/*  143 */       localRectangle.y += localInsets.top;
/*  144 */       localRectangle.width -= localInsets.left + localInsets.right;
/*  145 */       localRectangle.height -= localInsets.top + localInsets.bottom;
/*  146 */       return localRectangle;
/*      */     }
/*  148 */     return null;
/*      */   }
/*      */ 
/*      */   private Object lock()
/*      */   {
/*  157 */     Document localDocument = getDocument();
/*      */ 
/*  159 */     if ((localDocument instanceof AbstractDocument)) {
/*  160 */       ((AbstractDocument)localDocument).readLock();
/*  161 */       return localDocument;
/*      */     }
/*  163 */     return null;
/*      */   }
/*      */ 
/*      */   private void unlock(Object paramObject)
/*      */   {
/*  170 */     if (paramObject != null)
/*  171 */       ((AbstractDocument)paramObject).readUnlock();
/*      */   }
/*      */ 
/*      */   private void buildInfo()
/*      */   {
/*  179 */     Object localObject1 = lock();
/*      */     try
/*      */     {
/*  182 */       Document localDocument = getDocument();
/*  183 */       Element localElement = localDocument.getDefaultRootElement();
/*      */ 
/*  185 */       this.rootElementInfo = new ElementInfo(localElement);
/*  186 */       this.rootElementInfo.validate();
/*      */     } finally {
/*  188 */       unlock(localObject1);
/*      */     }
/*      */   }
/*      */ 
/*      */   ElementInfo createElementInfo(Element paramElement, ElementInfo paramElementInfo)
/*      */   {
/*  196 */     AttributeSet localAttributeSet = paramElement.getAttributes();
/*      */ 
/*  198 */     if (localAttributeSet != null) {
/*  199 */       Object localObject = localAttributeSet.getAttribute(StyleConstants.NameAttribute);
/*      */ 
/*  201 */       if (localObject == HTML.Tag.IMG) {
/*  202 */         return new IconElementInfo(paramElement, paramElementInfo);
/*      */       }
/*  204 */       if ((localObject == HTML.Tag.CONTENT) || (localObject == HTML.Tag.CAPTION)) {
/*  205 */         return new TextElementInfo(paramElement, paramElementInfo);
/*      */       }
/*  207 */       if (localObject == HTML.Tag.TABLE) {
/*  208 */         return new TableElementInfo(paramElement, paramElementInfo);
/*      */       }
/*      */     }
/*  211 */     return null;
/*      */   }
/*      */ 
/*      */   public AccessibleContext getAccessibleContext()
/*      */   {
/*  218 */     if (this.rootHTMLAccessibleContext == null) {
/*  219 */       this.rootHTMLAccessibleContext = new RootHTMLAccessibleContext(this.rootElementInfo);
/*      */     }
/*      */ 
/*  222 */     return this.rootHTMLAccessibleContext;
/*      */   }
/*      */ 
/*      */   private class DocumentHandler
/*      */     implements DocumentListener
/*      */   {
/*      */     private DocumentHandler()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void insertUpdate(DocumentEvent paramDocumentEvent)
/*      */     {
/* 3037 */       AccessibleHTML.ElementInfo.access$1800(AccessibleHTML.this.getRootInfo(), paramDocumentEvent);
/*      */     }
/*      */     public void removeUpdate(DocumentEvent paramDocumentEvent) {
/* 3040 */       AccessibleHTML.ElementInfo.access$1800(AccessibleHTML.this.getRootInfo(), paramDocumentEvent);
/*      */     }
/*      */     public void changedUpdate(DocumentEvent paramDocumentEvent) {
/* 3043 */       AccessibleHTML.ElementInfo.access$1800(AccessibleHTML.this.getRootInfo(), paramDocumentEvent);
/*      */     }
/*      */   }
/*      */ 
/*      */   private class ElementInfo
/*      */   {
/*      */     private ArrayList<ElementInfo> children;
/*      */     private Element element;
/*      */     private ElementInfo parent;
/*      */     private boolean isValid;
/*      */     private boolean canBeValid;
/*      */ 
/*      */     ElementInfo(Element arg2)
/*      */     {
/* 2669 */       this(localElement, null);
/*      */     }
/*      */ 
/*      */     ElementInfo(Element paramElementInfo, ElementInfo arg3)
/*      */     {
/* 2677 */       this.element = paramElementInfo;
/*      */       Object localObject;
/* 2678 */       this.parent = localObject;
/* 2679 */       this.isValid = false;
/* 2680 */       this.canBeValid = true;
/*      */     }
/*      */ 
/*      */     protected void validate()
/*      */     {
/* 2689 */       this.isValid = true;
/* 2690 */       loadChildren(getElement());
/*      */     }
/*      */ 
/*      */     protected void loadChildren(Element paramElement)
/*      */     {
/* 2697 */       if (!paramElement.isLeaf()) {
/* 2698 */         int i = 0; int j = paramElement.getElementCount();
/* 2699 */         for (; i < j; i++) {
/* 2700 */           Element localElement = paramElement.getElement(i);
/* 2701 */           ElementInfo localElementInfo = AccessibleHTML.this.createElementInfo(localElement, this);
/*      */ 
/* 2703 */           if (localElementInfo != null) {
/* 2704 */             addChild(localElementInfo);
/*      */           }
/*      */           else
/* 2707 */             loadChildren(localElement);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     public int getIndexInParent()
/*      */     {
/* 2718 */       if ((this.parent == null) || (!this.parent.isValid())) {
/* 2719 */         return -1;
/*      */       }
/* 2721 */       return this.parent.indexOf(this);
/*      */     }
/*      */ 
/*      */     public Element getElement()
/*      */     {
/* 2728 */       return this.element;
/*      */     }
/*      */ 
/*      */     public ElementInfo getParent()
/*      */     {
/* 2735 */       return this.parent;
/*      */     }
/*      */ 
/*      */     public int indexOf(ElementInfo paramElementInfo)
/*      */     {
/* 2743 */       ArrayList localArrayList = this.children;
/*      */ 
/* 2745 */       if (localArrayList != null) {
/* 2746 */         return localArrayList.indexOf(paramElementInfo);
/*      */       }
/* 2748 */       return -1;
/*      */     }
/*      */ 
/*      */     public ElementInfo getChild(int paramInt)
/*      */     {
/* 2756 */       if (validateIfNecessary()) {
/* 2757 */         ArrayList localArrayList = this.children;
/*      */ 
/* 2759 */         if ((localArrayList != null) && (paramInt >= 0) && (paramInt < localArrayList.size()))
/*      */         {
/* 2761 */           return (ElementInfo)localArrayList.get(paramInt);
/*      */         }
/*      */       }
/* 2764 */       return null;
/*      */     }
/*      */ 
/*      */     public int getChildCount()
/*      */     {
/* 2771 */       validateIfNecessary();
/* 2772 */       return this.children == null ? 0 : this.children.size();
/*      */     }
/*      */ 
/*      */     protected void addChild(ElementInfo paramElementInfo)
/*      */     {
/* 2779 */       if (this.children == null) {
/* 2780 */         this.children = new ArrayList();
/*      */       }
/* 2782 */       this.children.add(paramElementInfo);
/*      */     }
/*      */ 
/*      */     protected View getView()
/*      */     {
/* 2790 */       if (!validateIfNecessary()) {
/* 2791 */         return null;
/*      */       }
/* 2793 */       Object localObject1 = AccessibleHTML.this.lock();
/*      */       try {
/* 2795 */         View localView1 = AccessibleHTML.this.getRootView();
/* 2796 */         Element localElement = getElement();
/* 2797 */         int i = localElement.getStartOffset();
/*      */         View localView2;
/* 2799 */         if (localView1 != null) {
/* 2800 */           return getView(localView1, localElement, i);
/*      */         }
/* 2802 */         return null;
/*      */       } finally {
/* 2804 */         AccessibleHTML.this.unlock(localObject1);
/*      */       }
/*      */     }
/*      */ 
/*      */     public Rectangle getBounds()
/*      */     {
/* 2813 */       if (!validateIfNecessary()) {
/* 2814 */         return null;
/*      */       }
/* 2816 */       Object localObject1 = AccessibleHTML.this.lock();
/*      */       try {
/* 2818 */         Rectangle localRectangle1 = AccessibleHTML.this.getRootEditorRect();
/* 2819 */         View localView = AccessibleHTML.this.getRootView();
/* 2820 */         Element localElement = getElement();
/*      */ 
/* 2822 */         if ((localRectangle1 != null) && (localView != null))
/*      */           try {
/* 2824 */             return localView.modelToView(localElement.getStartOffset(), Position.Bias.Forward, localElement.getEndOffset(), Position.Bias.Backward, localRectangle1).getBounds();
/*      */           }
/*      */           catch (BadLocationException localBadLocationException)
/*      */           {
/*      */           }
/*      */       }
/*      */       finally
/*      */       {
/* 2832 */         AccessibleHTML.this.unlock(localObject1);
/*      */       }
/* 2834 */       return null;
/*      */     }
/*      */ 
/*      */     protected boolean isValid()
/*      */     {
/* 2841 */       return this.isValid;
/*      */     }
/*      */ 
/*      */     protected AttributeSet getAttributes()
/*      */     {
/* 2849 */       if (validateIfNecessary()) {
/* 2850 */         return getElement().getAttributes();
/*      */       }
/* 2852 */       return null;
/*      */     }
/*      */ 
/*      */     protected AttributeSet getViewAttributes()
/*      */     {
/* 2861 */       if (validateIfNecessary()) {
/* 2862 */         View localView = getView();
/*      */ 
/* 2864 */         if (localView != null) {
/* 2865 */           return localView.getElement().getAttributes();
/*      */         }
/* 2867 */         return getElement().getAttributes();
/*      */       }
/* 2869 */       return null;
/*      */     }
/*      */ 
/*      */     protected int getIntAttr(AttributeSet paramAttributeSet, Object paramObject, int paramInt)
/*      */     {
/* 2877 */       if ((paramAttributeSet != null) && (paramAttributeSet.isDefined(paramObject)))
/*      */       {
/* 2879 */         String str = (String)paramAttributeSet.getAttribute(paramObject);
/*      */         int i;
/* 2880 */         if (str == null)
/* 2881 */           i = paramInt;
/*      */         else {
/*      */           try
/*      */           {
/* 2885 */             i = Math.max(0, Integer.parseInt(str));
/*      */           } catch (NumberFormatException localNumberFormatException) {
/* 2887 */             i = paramInt;
/*      */           }
/*      */         }
/* 2890 */         return i;
/*      */       }
/* 2892 */       return paramInt;
/*      */     }
/*      */ 
/*      */     protected boolean validateIfNecessary()
/*      */     {
/* 2903 */       if ((!isValid()) && (this.canBeValid)) {
/* 2904 */         this.children = null;
/* 2905 */         Object localObject1 = AccessibleHTML.this.lock();
/*      */         try
/*      */         {
/* 2908 */           validate();
/*      */         } finally {
/* 2910 */           AccessibleHTML.this.unlock(localObject1);
/*      */         }
/*      */       }
/* 2913 */       return isValid();
/*      */     }
/*      */ 
/*      */     protected void invalidate(boolean paramBoolean)
/*      */     {
/* 2921 */       if (!isValid()) {
/* 2922 */         if ((this.canBeValid) && (!paramBoolean)) {
/* 2923 */           this.canBeValid = false;
/*      */         }
/* 2925 */         return;
/*      */       }
/* 2927 */       this.isValid = false;
/* 2928 */       this.canBeValid = paramBoolean;
/* 2929 */       if (this.children != null) {
/* 2930 */         for (ElementInfo localElementInfo : this.children) {
/* 2931 */           localElementInfo.invalidate(false);
/*      */         }
/* 2933 */         this.children = null;
/*      */       }
/*      */     }
/*      */ 
/*      */     private View getView(View paramView, Element paramElement, int paramInt) {
/* 2938 */       if (paramView.getElement() == paramElement) {
/* 2939 */         return paramView;
/*      */       }
/* 2941 */       int i = paramView.getViewIndex(paramInt, Position.Bias.Forward);
/*      */ 
/* 2943 */       if ((i != -1) && (i < paramView.getViewCount())) {
/* 2944 */         return getView(paramView.getView(i), paramElement, paramInt);
/*      */       }
/* 2946 */       return null;
/*      */     }
/*      */ 
/*      */     private int getClosestInfoIndex(int paramInt) {
/* 2950 */       for (int i = 0; i < getChildCount(); i++) {
/* 2951 */         ElementInfo localElementInfo = getChild(i);
/*      */ 
/* 2953 */         if ((paramInt < localElementInfo.getElement().getEndOffset()) || (paramInt == localElementInfo.getElement().getStartOffset()))
/*      */         {
/* 2955 */           return i;
/*      */         }
/*      */       }
/* 2958 */       return -1;
/*      */     }
/*      */ 
/*      */     private void update(DocumentEvent paramDocumentEvent) {
/* 2962 */       if (!isValid()) {
/* 2963 */         return;
/* 2965 */       }ElementInfo localElementInfo = getParent();
/* 2966 */       Element localElement = getElement();
/*      */       Object localObject1;
/*      */       do { localObject1 = paramDocumentEvent.getChange(localElement);
/* 2970 */         if (localObject1 != null) {
/* 2971 */           if (localElement == getElement())
/*      */           {
/* 2973 */             invalidate(true);
/*      */           }
/* 2975 */           else if (localElementInfo != null) {
/* 2976 */             localElementInfo.invalidate(localElementInfo == AccessibleHTML.this.getRootInfo());
/*      */           }
/* 2978 */           return;
/*      */         }
/* 2980 */         localElement = localElement.getParentElement(); }
/* 2981 */       while ((localElementInfo != null) && (localElement != null) && (localElement != localElementInfo.getElement()));
/*      */ 
/* 2984 */       if (getChildCount() > 0) {
/* 2985 */         localObject1 = getElement();
/* 2986 */         int i = paramDocumentEvent.getOffset();
/* 2987 */         int j = getClosestInfoIndex(i);
/* 2988 */         if ((j == -1) && (paramDocumentEvent.getType() == DocumentEvent.EventType.REMOVE) && (i >= ((Element)localObject1).getEndOffset()))
/*      */         {
/* 2995 */           j = getChildCount() - 1;
/*      */         }
/* 2997 */         Object localObject2 = j >= 0 ? getChild(j) : null;
/* 2998 */         if ((localObject2 != null) && (localObject2.getElement().getStartOffset() == i) && (i > 0))
/*      */         {
/* 3002 */           j = Math.max(j - 1, 0);
/*      */         }
/*      */         int k;
/* 3005 */         if (paramDocumentEvent.getType() != DocumentEvent.EventType.REMOVE) {
/* 3006 */           k = getClosestInfoIndex(i + paramDocumentEvent.getLength());
/* 3007 */           if (k < 0)
/* 3008 */             k = getChildCount() - 1;
/*      */         }
/*      */         else
/*      */         {
/* 3012 */           k = j;
/*      */ 
/* 3014 */           while ((k + 1 < getChildCount()) && (getChild(k + 1).getElement().getEndOffset() == getChild(k + 1).getElement().getStartOffset()))
/*      */           {
/* 3017 */             k++;
/*      */           }
/*      */         }
/* 3020 */         j = Math.max(j, 0);
/*      */ 
/* 3023 */         for (int m = j; (m <= k) && (isValid()); m++)
/* 3024 */           getChild(m).update(paramDocumentEvent);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected abstract class HTMLAccessibleContext extends AccessibleContext
/*      */     implements Accessible, AccessibleComponent
/*      */   {
/*      */     protected AccessibleHTML.ElementInfo elementInfo;
/*      */ 
/*      */     public HTMLAccessibleContext(AccessibleHTML.ElementInfo arg2)
/*      */     {
/*      */       Object localObject;
/*  303 */       this.elementInfo = localObject;
/*      */     }
/*      */ 
/*      */     public AccessibleContext getAccessibleContext()
/*      */     {
/*  308 */       return this;
/*      */     }
/*      */ 
/*      */     public AccessibleStateSet getAccessibleStateSet()
/*      */     {
/*  319 */       AccessibleStateSet localAccessibleStateSet = new AccessibleStateSet();
/*  320 */       JEditorPane localJEditorPane = AccessibleHTML.this.getTextComponent();
/*      */ 
/*  322 */       if (localJEditorPane.isEnabled()) {
/*  323 */         localAccessibleStateSet.add(AccessibleState.ENABLED);
/*      */       }
/*  325 */       if (((localJEditorPane instanceof JTextComponent)) && (((JTextComponent)localJEditorPane).isEditable()))
/*      */       {
/*  328 */         localAccessibleStateSet.add(AccessibleState.EDITABLE);
/*  329 */         localAccessibleStateSet.add(AccessibleState.FOCUSABLE);
/*      */       }
/*  331 */       if (localJEditorPane.isVisible()) {
/*  332 */         localAccessibleStateSet.add(AccessibleState.VISIBLE);
/*      */       }
/*  334 */       if (localJEditorPane.isShowing()) {
/*  335 */         localAccessibleStateSet.add(AccessibleState.SHOWING);
/*      */       }
/*  337 */       return localAccessibleStateSet;
/*      */     }
/*      */ 
/*      */     public int getAccessibleIndexInParent()
/*      */     {
/*  351 */       return this.elementInfo.getIndexInParent();
/*      */     }
/*      */ 
/*      */     public int getAccessibleChildrenCount()
/*      */     {
/*  360 */       return this.elementInfo.getChildCount();
/*      */     }
/*      */ 
/*      */     public Accessible getAccessibleChild(int paramInt)
/*      */     {
/*  374 */       AccessibleHTML.ElementInfo localElementInfo = this.elementInfo.getChild(paramInt);
/*  375 */       if ((localElementInfo != null) && ((localElementInfo instanceof AccessibleComponent))) {
/*  376 */         return (AccessibleComponent)localElementInfo;
/*      */       }
/*  378 */       return null;
/*      */     }
/*      */ 
/*      */     public Locale getLocale()
/*      */       throws IllegalComponentStateException
/*      */     {
/*  395 */       return AccessibleHTML.this.editor.getLocale();
/*      */     }
/*      */ 
/*      */     public AccessibleComponent getAccessibleComponent()
/*      */     {
/*  401 */       return this;
/*      */     }
/*      */ 
/*      */     public Color getBackground()
/*      */     {
/*  412 */       return AccessibleHTML.this.getTextComponent().getBackground();
/*      */     }
/*      */ 
/*      */     public void setBackground(Color paramColor)
/*      */     {
/*  422 */       AccessibleHTML.this.getTextComponent().setBackground(paramColor);
/*      */     }
/*      */ 
/*      */     public Color getForeground()
/*      */     {
/*  433 */       return AccessibleHTML.this.getTextComponent().getForeground();
/*      */     }
/*      */ 
/*      */     public void setForeground(Color paramColor)
/*      */     {
/*  443 */       AccessibleHTML.this.getTextComponent().setForeground(paramColor);
/*      */     }
/*      */ 
/*      */     public Cursor getCursor()
/*      */     {
/*  453 */       return AccessibleHTML.this.getTextComponent().getCursor();
/*      */     }
/*      */ 
/*      */     public void setCursor(Cursor paramCursor)
/*      */     {
/*  463 */       AccessibleHTML.this.getTextComponent().setCursor(paramCursor);
/*      */     }
/*      */ 
/*      */     public Font getFont()
/*      */     {
/*  473 */       return AccessibleHTML.this.getTextComponent().getFont();
/*      */     }
/*      */ 
/*      */     public void setFont(Font paramFont)
/*      */     {
/*  483 */       AccessibleHTML.this.getTextComponent().setFont(paramFont);
/*      */     }
/*      */ 
/*      */     public FontMetrics getFontMetrics(Font paramFont)
/*      */     {
/*  494 */       return AccessibleHTML.this.getTextComponent().getFontMetrics(paramFont);
/*      */     }
/*      */ 
/*      */     public boolean isEnabled()
/*      */     {
/*  509 */       return AccessibleHTML.this.getTextComponent().isEnabled();
/*      */     }
/*      */ 
/*      */     public void setEnabled(boolean paramBoolean)
/*      */     {
/*  519 */       AccessibleHTML.this.getTextComponent().setEnabled(paramBoolean);
/*      */     }
/*      */ 
/*      */     public boolean isVisible()
/*      */     {
/*  538 */       return AccessibleHTML.this.getTextComponent().isVisible();
/*      */     }
/*      */ 
/*      */     public void setVisible(boolean paramBoolean)
/*      */     {
/*  548 */       AccessibleHTML.this.getTextComponent().setVisible(paramBoolean);
/*      */     }
/*      */ 
/*      */     public boolean isShowing()
/*      */     {
/*  561 */       return AccessibleHTML.this.getTextComponent().isShowing();
/*      */     }
/*      */ 
/*      */     public boolean contains(Point paramPoint)
/*      */     {
/*  574 */       Rectangle localRectangle = getBounds();
/*  575 */       if (localRectangle != null) {
/*  576 */         return localRectangle.contains(paramPoint.x, paramPoint.y);
/*      */       }
/*  578 */       return false;
/*      */     }
/*      */ 
/*      */     public Point getLocationOnScreen()
/*      */     {
/*  591 */       Point localPoint = AccessibleHTML.this.getTextComponent().getLocationOnScreen();
/*  592 */       Rectangle localRectangle = getBounds();
/*  593 */       if (localRectangle != null) {
/*  594 */         return new Point(localPoint.x + localRectangle.x, localPoint.y + localRectangle.y);
/*      */       }
/*      */ 
/*  597 */       return null;
/*      */     }
/*      */ 
/*      */     public Point getLocation()
/*      */     {
/*  613 */       Rectangle localRectangle = getBounds();
/*  614 */       if (localRectangle != null) {
/*  615 */         return new Point(localRectangle.x, localRectangle.y);
/*      */       }
/*  617 */       return null;
/*      */     }
/*      */ 
/*      */     public void setLocation(Point paramPoint)
/*      */     {
/*      */     }
/*      */ 
/*      */     public Rectangle getBounds()
/*      */     {
/*  639 */       return this.elementInfo.getBounds();
/*      */     }
/*      */ 
/*      */     public void setBounds(Rectangle paramRectangle)
/*      */     {
/*      */     }
/*      */ 
/*      */     public Dimension getSize()
/*      */     {
/*  664 */       Rectangle localRectangle = getBounds();
/*  665 */       if (localRectangle != null) {
/*  666 */         return new Dimension(localRectangle.width, localRectangle.height);
/*      */       }
/*  668 */       return null;
/*      */     }
/*      */ 
/*      */     public void setSize(Dimension paramDimension)
/*      */     {
/*  679 */       JEditorPane localJEditorPane = AccessibleHTML.this.getTextComponent();
/*  680 */       localJEditorPane.setSize(paramDimension);
/*      */     }
/*      */ 
/*      */     public Accessible getAccessibleAt(Point paramPoint)
/*      */     {
/*  692 */       AccessibleHTML.ElementInfo localElementInfo = getElementInfoAt(AccessibleHTML.this.rootElementInfo, paramPoint);
/*  693 */       if ((localElementInfo instanceof AccessibleComponent)) {
/*  694 */         return (AccessibleComponent)localElementInfo;
/*      */       }
/*  696 */       return null;
/*      */     }
/*      */ 
/*      */     private AccessibleHTML.ElementInfo getElementInfoAt(AccessibleHTML.ElementInfo paramElementInfo, Point paramPoint)
/*      */     {
/*  701 */       if (paramElementInfo.getBounds() == null) {
/*  702 */         return null;
/*      */       }
/*  704 */       if ((paramElementInfo.getChildCount() == 0) && (paramElementInfo.getBounds().contains(paramPoint)))
/*      */       {
/*  706 */         return paramElementInfo;
/*      */       }
/*      */       Object localObject;
/*  709 */       if ((paramElementInfo instanceof AccessibleHTML.TableElementInfo))
/*      */       {
/*  712 */         AccessibleHTML.ElementInfo localElementInfo1 = ((AccessibleHTML.TableElementInfo)paramElementInfo).getCaptionInfo();
/*      */ 
/*  714 */         if (localElementInfo1 != null) {
/*  715 */           localObject = localElementInfo1.getBounds();
/*  716 */           if ((localObject != null) && (((Rectangle)localObject).contains(paramPoint))) {
/*  717 */             return localElementInfo1;
/*      */           }
/*      */         }
/*      */       }
/*  721 */       for (int i = 0; i < paramElementInfo.getChildCount(); i++)
/*      */       {
/*  723 */         localObject = paramElementInfo.getChild(i);
/*  724 */         AccessibleHTML.ElementInfo localElementInfo2 = getElementInfoAt((AccessibleHTML.ElementInfo)localObject, paramPoint);
/*  725 */         if (localElementInfo2 != null) {
/*  726 */           return localElementInfo2;
/*      */         }
/*      */       }
/*      */ 
/*  730 */       return null;
/*      */     }
/*      */ 
/*      */     public boolean isFocusTraversable()
/*      */     {
/*  745 */       JEditorPane localJEditorPane = AccessibleHTML.this.getTextComponent();
/*  746 */       if (((localJEditorPane instanceof JTextComponent)) && 
/*  747 */         (((JTextComponent)localJEditorPane).isEditable())) {
/*  748 */         return true;
/*      */       }
/*      */ 
/*  751 */       return false;
/*      */     }
/*      */ 
/*      */     public void requestFocus()
/*      */     {
/*  762 */       if (!isFocusTraversable()) {
/*  763 */         return;
/*      */       }
/*      */ 
/*  766 */       JEditorPane localJEditorPane = AccessibleHTML.this.getTextComponent();
/*  767 */       if ((localJEditorPane instanceof JTextComponent))
/*      */       {
/*  769 */         localJEditorPane.requestFocusInWindow();
/*      */         try
/*      */         {
/*  772 */           if (this.elementInfo.validateIfNecessary())
/*      */           {
/*  774 */             Element localElement = this.elementInfo.getElement();
/*  775 */             ((JTextComponent)localJEditorPane).setCaretPosition(localElement.getStartOffset());
/*      */ 
/*  778 */             AccessibleContext localAccessibleContext = AccessibleHTML.this.editor.getAccessibleContext();
/*  779 */             PropertyChangeEvent localPropertyChangeEvent = new PropertyChangeEvent(this, "AccessibleState", null, AccessibleState.FOCUSED);
/*      */ 
/*  782 */             localAccessibleContext.firePropertyChange("AccessibleState", null, localPropertyChangeEvent);
/*      */           }
/*      */         }
/*      */         catch (IllegalArgumentException localIllegalArgumentException)
/*      */         {
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     public void addFocusListener(FocusListener paramFocusListener)
/*      */     {
/*  800 */       AccessibleHTML.this.getTextComponent().addFocusListener(paramFocusListener);
/*      */     }
/*      */ 
/*      */     public void removeFocusListener(FocusListener paramFocusListener)
/*      */     {
/*  811 */       AccessibleHTML.this.getTextComponent().removeFocusListener(paramFocusListener);
/*      */     }
/*      */   }
/*      */ 
/*      */   private class IconElementInfo extends AccessibleHTML.ElementInfo
/*      */     implements Accessible
/*      */   {
/* 1232 */     private int width = -1;
/* 1233 */     private int height = -1;
/*      */     private AccessibleContext accessibleContext;
/*      */ 
/*      */     IconElementInfo(Element paramElementInfo, AccessibleHTML.ElementInfo arg3)
/*      */     {
/* 1236 */       super(paramElementInfo, localElementInfo);
/*      */     }
/*      */ 
/*      */     protected void invalidate(boolean paramBoolean) {
/* 1240 */       super.invalidate(paramBoolean);
/* 1241 */       this.width = (this.height = -1);
/*      */     }
/*      */ 
/*      */     private int getImageSize(Object paramObject) {
/* 1245 */       if (validateIfNecessary()) {
/* 1246 */         int i = getIntAttr(getAttributes(), paramObject, -1);
/*      */ 
/* 1248 */         if (i == -1) {
/* 1249 */           View localView = getView();
/*      */ 
/* 1251 */           i = 0;
/* 1252 */           if ((localView instanceof ImageView)) {
/* 1253 */             Image localImage = ((ImageView)localView).getImage();
/* 1254 */             if (localImage != null) {
/* 1255 */               if (paramObject == HTML.Attribute.WIDTH) {
/* 1256 */                 i = localImage.getWidth(null);
/*      */               }
/*      */               else {
/* 1259 */                 i = localImage.getHeight(null);
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/* 1264 */         return i;
/*      */       }
/* 1266 */       return 0;
/*      */     }
/*      */ 
/*      */     public AccessibleContext getAccessibleContext()
/*      */     {
/* 1273 */       if (this.accessibleContext == null) {
/* 1274 */         this.accessibleContext = new IconAccessibleContext(this);
/*      */       }
/* 1276 */       return this.accessibleContext;
/*      */     }
/*      */ 
/*      */     protected class IconAccessibleContext extends AccessibleHTML.HTMLAccessibleContext
/*      */       implements AccessibleIcon
/*      */     {
/*      */       public IconAccessibleContext(AccessibleHTML.ElementInfo arg2)
/*      */       {
/* 1286 */         super(localElementInfo);
/*      */       }
/*      */ 
/*      */       public String getAccessibleName()
/*      */       {
/* 1305 */         return getAccessibleIconDescription();
/*      */       }
/*      */ 
/*      */       public String getAccessibleDescription()
/*      */       {
/* 1319 */         return AccessibleHTML.this.editor.getContentType();
/*      */       }
/*      */ 
/*      */       public AccessibleRole getAccessibleRole()
/*      */       {
/* 1341 */         return AccessibleRole.ICON;
/*      */       }
/*      */ 
/*      */       public AccessibleIcon[] getAccessibleIcon() {
/* 1345 */         AccessibleIcon[] arrayOfAccessibleIcon = new AccessibleIcon[1];
/* 1346 */         arrayOfAccessibleIcon[0] = this;
/* 1347 */         return arrayOfAccessibleIcon;
/*      */       }
/*      */ 
/*      */       public String getAccessibleIconDescription()
/*      */       {
/* 1359 */         return ((ImageView)AccessibleHTML.IconElementInfo.this.getView()).getAltText();
/*      */       }
/*      */ 
/*      */       public void setAccessibleIconDescription(String paramString)
/*      */       {
/*      */       }
/*      */ 
/*      */       public int getAccessibleIconWidth()
/*      */       {
/* 1379 */         if (AccessibleHTML.IconElementInfo.this.width == -1) {
/* 1380 */           AccessibleHTML.IconElementInfo.this.width = AccessibleHTML.IconElementInfo.this.getImageSize(HTML.Attribute.WIDTH);
/*      */         }
/* 1382 */         return AccessibleHTML.IconElementInfo.this.width;
/*      */       }
/*      */ 
/*      */       public int getAccessibleIconHeight()
/*      */       {
/* 1391 */         if (AccessibleHTML.IconElementInfo.this.height == -1) {
/* 1392 */           AccessibleHTML.IconElementInfo.this.height = AccessibleHTML.IconElementInfo.this.getImageSize(HTML.Attribute.HEIGHT);
/*      */         }
/* 1394 */         return AccessibleHTML.IconElementInfo.this.height;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private class PropertyChangeHandler
/*      */     implements PropertyChangeListener
/*      */   {
/*      */     private PropertyChangeHandler()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent)
/*      */     {
/* 3052 */       if (paramPropertyChangeEvent.getPropertyName().equals("document"))
/*      */       {
/* 3054 */         AccessibleHTML.this.setDocument(AccessibleHTML.this.editor.getDocument());
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private class RootHTMLAccessibleContext extends AccessibleHTML.HTMLAccessibleContext
/*      */   {
/*      */     public RootHTMLAccessibleContext(AccessibleHTML.ElementInfo arg2)
/*      */     {
/*  231 */       super(localElementInfo);
/*      */     }
/*      */ 
/*      */     public String getAccessibleName()
/*      */     {
/*  250 */       if (AccessibleHTML.this.model != null) {
/*  251 */         return (String)AccessibleHTML.this.model.getProperty("title");
/*      */       }
/*  253 */       return null;
/*      */     }
/*      */ 
/*      */     public String getAccessibleDescription()
/*      */     {
/*  268 */       return AccessibleHTML.this.editor.getContentType();
/*      */     }
/*      */ 
/*      */     public AccessibleRole getAccessibleRole()
/*      */     {
/*  290 */       return AccessibleRole.TEXT;
/*      */     }
/*      */   }
/*      */ 
/*      */   private class TableElementInfo extends AccessibleHTML.ElementInfo
/*      */     implements Accessible
/*      */   {
/*      */     protected AccessibleHTML.ElementInfo caption;
/*      */     private TableCellElementInfo[][] grid;
/*      */     private AccessibleContext accessibleContext;
/*      */ 
/*      */     TableElementInfo(Element paramElementInfo, AccessibleHTML.ElementInfo arg3)
/*      */     {
/* 1424 */       super(paramElementInfo, localElementInfo);
/*      */     }
/*      */ 
/*      */     public AccessibleHTML.ElementInfo getCaptionInfo() {
/* 1428 */       return this.caption;
/*      */     }
/*      */ 
/*      */     protected void validate()
/*      */     {
/* 1435 */       super.validate();
/* 1436 */       updateGrid();
/*      */     }
/*      */ 
/*      */     protected void loadChildren(Element paramElement)
/*      */     {
/* 1444 */       for (int i = 0; i < paramElement.getElementCount(); i++) {
/* 1445 */         Element localElement = paramElement.getElement(i);
/* 1446 */         AttributeSet localAttributeSet = localElement.getAttributes();
/*      */ 
/* 1448 */         if (localAttributeSet.getAttribute(StyleConstants.NameAttribute) == HTML.Tag.TR)
/*      */         {
/* 1450 */           addChild(new TableRowElementInfo(localElement, this, i));
/*      */         }
/* 1452 */         else if (localAttributeSet.getAttribute(StyleConstants.NameAttribute) == HTML.Tag.CAPTION)
/*      */         {
/* 1456 */           this.caption = AccessibleHTML.this.createElementInfo(localElement, this);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     private void updateGrid()
/*      */     {
/* 1466 */       int i = 0;
/* 1467 */       int j = 0;
/*      */ 
/* 1469 */       for (int m = 0; m < getChildCount(); m++) {
/* 1470 */         TableRowElementInfo localTableRowElementInfo = getRow(m);
/* 1471 */         int n = 0;
/* 1472 */         for (int i1 = 0; i1 < i; i1++) {
/* 1473 */           n = Math.max(n, getRow(m - i1 - 1).getColumnCount(i1 + 2));
/*      */         }
/*      */ 
/* 1476 */         i = Math.max(localTableRowElementInfo.getRowCount(), i);
/* 1477 */         i--;
/* 1478 */         j = Math.max(j, localTableRowElementInfo.getColumnCount() + n);
/*      */       }
/* 1480 */       int k = getChildCount() + i;
/*      */ 
/* 1483 */       this.grid = new TableCellElementInfo[k][];
/* 1484 */       for (m = 0; m < k; m++) {
/* 1485 */         this.grid[m] = new TableCellElementInfo[j];
/*      */       }
/*      */ 
/* 1488 */       for (m = 0; m < k; m++)
/* 1489 */         getRow(m).updateGrid(m);
/*      */     }
/*      */ 
/*      */     public TableRowElementInfo getRow(int paramInt)
/*      */     {
/* 1497 */       return (TableRowElementInfo)getChild(paramInt);
/*      */     }
/*      */ 
/*      */     public TableCellElementInfo getCell(int paramInt1, int paramInt2)
/*      */     {
/* 1504 */       if ((validateIfNecessary()) && (paramInt1 < this.grid.length) && (paramInt2 < this.grid[0].length))
/*      */       {
/* 1506 */         return this.grid[paramInt1][paramInt2];
/*      */       }
/* 1508 */       return null;
/*      */     }
/*      */ 
/*      */     public int getRowExtentAt(int paramInt1, int paramInt2)
/*      */     {
/* 1515 */       TableCellElementInfo localTableCellElementInfo = getCell(paramInt1, paramInt2);
/*      */ 
/* 1517 */       if (localTableCellElementInfo != null) {
/* 1518 */         int i = localTableCellElementInfo.getRowCount();
/* 1519 */         int j = 1;
/*      */ 
/* 1521 */         while ((paramInt1 - j >= 0) && (this.grid[(paramInt1 - j)][paramInt2] == localTableCellElementInfo)) {
/* 1522 */           j++;
/*      */         }
/* 1524 */         return i - j + 1;
/*      */       }
/* 1526 */       return 0;
/*      */     }
/*      */ 
/*      */     public int getColumnExtentAt(int paramInt1, int paramInt2)
/*      */     {
/* 1533 */       TableCellElementInfo localTableCellElementInfo = getCell(paramInt1, paramInt2);
/*      */ 
/* 1535 */       if (localTableCellElementInfo != null) {
/* 1536 */         int i = localTableCellElementInfo.getColumnCount();
/* 1537 */         int j = 1;
/*      */ 
/* 1539 */         while ((paramInt2 - j >= 0) && (this.grid[paramInt1][(paramInt2 - j)] == localTableCellElementInfo)) {
/* 1540 */           j++;
/*      */         }
/* 1542 */         return i - j + 1;
/*      */       }
/* 1544 */       return 0;
/*      */     }
/*      */ 
/*      */     public int getRowCount()
/*      */     {
/* 1551 */       if (validateIfNecessary()) {
/* 1552 */         return this.grid.length;
/*      */       }
/* 1554 */       return 0;
/*      */     }
/*      */ 
/*      */     public int getColumnCount()
/*      */     {
/* 1561 */       if ((validateIfNecessary()) && (this.grid.length > 0)) {
/* 1562 */         return this.grid[0].length;
/*      */       }
/* 1564 */       return 0;
/*      */     }
/*      */ 
/*      */     public AccessibleContext getAccessibleContext()
/*      */     {
/* 1571 */       if (this.accessibleContext == null) {
/* 1572 */         this.accessibleContext = new TableAccessibleContext(this);
/*      */       }
/* 1574 */       return this.accessibleContext;
/*      */     }
/*      */ 
/*      */     public class TableAccessibleContext extends AccessibleHTML.HTMLAccessibleContext
/*      */       implements AccessibleTable
/*      */     {
/*      */       private AccessibleHeadersTable rowHeadersTable;
/*      */ 
/*      */       public TableAccessibleContext(AccessibleHTML.ElementInfo arg2)
/*      */       {
/* 1586 */         super(localElementInfo);
/*      */       }
/*      */ 
/*      */       public String getAccessibleName()
/*      */       {
/* 1606 */         return getAccessibleRole().toString();
/*      */       }
/*      */ 
/*      */       public String getAccessibleDescription()
/*      */       {
/* 1620 */         return AccessibleHTML.this.editor.getContentType();
/*      */       }
/*      */ 
/*      */       public AccessibleRole getAccessibleRole()
/*      */       {
/* 1642 */         return AccessibleRole.TABLE;
/*      */       }
/*      */ 
/*      */       public int getAccessibleIndexInParent()
/*      */       {
/* 1656 */         return this.elementInfo.getIndexInParent();
/*      */       }
/*      */ 
/*      */       public int getAccessibleChildrenCount()
/*      */       {
/* 1665 */         return ((AccessibleHTML.TableElementInfo)this.elementInfo).getRowCount() * ((AccessibleHTML.TableElementInfo)this.elementInfo).getColumnCount();
/*      */       }
/*      */ 
/*      */       public Accessible getAccessibleChild(int paramInt)
/*      */       {
/* 1680 */         int i = ((AccessibleHTML.TableElementInfo)this.elementInfo).getRowCount();
/* 1681 */         int j = ((AccessibleHTML.TableElementInfo)this.elementInfo).getColumnCount();
/* 1682 */         int k = paramInt / i;
/* 1683 */         int m = paramInt % j;
/* 1684 */         if ((k < 0) || (k >= i) || (m < 0) || (m >= j)) {
/* 1685 */           return null;
/*      */         }
/* 1687 */         return getAccessibleAt(k, m);
/*      */       }
/*      */ 
/*      */       public AccessibleTable getAccessibleTable()
/*      */       {
/* 1692 */         return this;
/*      */       }
/*      */ 
/*      */       public Accessible getAccessibleCaption()
/*      */       {
/* 1701 */         AccessibleHTML.ElementInfo localElementInfo = AccessibleHTML.TableElementInfo.this.getCaptionInfo();
/* 1702 */         if ((localElementInfo instanceof Accessible)) {
/* 1703 */           return (Accessible)AccessibleHTML.TableElementInfo.this.caption;
/*      */         }
/* 1705 */         return null;
/*      */       }
/*      */ 
/*      */       public void setAccessibleCaption(Accessible paramAccessible)
/*      */       {
/*      */       }
/*      */ 
/*      */       public Accessible getAccessibleSummary()
/*      */       {
/* 1723 */         return null;
/*      */       }
/*      */ 
/*      */       public void setAccessibleSummary(Accessible paramAccessible)
/*      */       {
/*      */       }
/*      */ 
/*      */       public int getAccessibleRowCount()
/*      */       {
/* 1740 */         return ((AccessibleHTML.TableElementInfo)this.elementInfo).getRowCount();
/*      */       }
/*      */ 
/*      */       public int getAccessibleColumnCount()
/*      */       {
/* 1749 */         return ((AccessibleHTML.TableElementInfo)this.elementInfo).getColumnCount();
/*      */       }
/*      */ 
/*      */       public Accessible getAccessibleAt(int paramInt1, int paramInt2)
/*      */       {
/* 1761 */         AccessibleHTML.TableElementInfo.TableCellElementInfo localTableCellElementInfo = AccessibleHTML.TableElementInfo.this.getCell(paramInt1, paramInt2);
/* 1762 */         if (localTableCellElementInfo != null) {
/* 1763 */           return localTableCellElementInfo.getAccessible();
/*      */         }
/* 1765 */         return null;
/*      */       }
/*      */ 
/*      */       public int getAccessibleRowExtentAt(int paramInt1, int paramInt2)
/*      */       {
/* 1777 */         return ((AccessibleHTML.TableElementInfo)this.elementInfo).getRowExtentAt(paramInt1, paramInt2);
/*      */       }
/*      */ 
/*      */       public int getAccessibleColumnExtentAt(int paramInt1, int paramInt2)
/*      */       {
/* 1788 */         return ((AccessibleHTML.TableElementInfo)this.elementInfo).getColumnExtentAt(paramInt1, paramInt2);
/*      */       }
/*      */ 
/*      */       public AccessibleTable getAccessibleRowHeader()
/*      */       {
/* 1798 */         return this.rowHeadersTable;
/*      */       }
/*      */ 
/*      */       public void setAccessibleRowHeader(AccessibleTable paramAccessibleTable)
/*      */       {
/*      */       }
/*      */ 
/*      */       public AccessibleTable getAccessibleColumnHeader()
/*      */       {
/* 1817 */         return null;
/*      */       }
/*      */ 
/*      */       public void setAccessibleColumnHeader(AccessibleTable paramAccessibleTable)
/*      */       {
/*      */       }
/*      */ 
/*      */       public Accessible getAccessibleRowDescription(int paramInt)
/*      */       {
/* 1836 */         return null;
/*      */       }
/*      */ 
/*      */       public void setAccessibleRowDescription(int paramInt, Accessible paramAccessible)
/*      */       {
/*      */       }
/*      */ 
/*      */       public Accessible getAccessibleColumnDescription(int paramInt)
/*      */       {
/* 1855 */         return null;
/*      */       }
/*      */ 
/*      */       public void setAccessibleColumnDescription(int paramInt, Accessible paramAccessible)
/*      */       {
/*      */       }
/*      */ 
/*      */       public boolean isAccessibleSelected(int paramInt1, int paramInt2)
/*      */       {
/* 1878 */         if (AccessibleHTML.TableElementInfo.this.validateIfNecessary()) {
/* 1879 */           if ((paramInt1 < 0) || (paramInt1 >= getAccessibleRowCount()) || (paramInt2 < 0) || (paramInt2 >= getAccessibleColumnCount()))
/*      */           {
/* 1881 */             return false;
/*      */           }
/* 1883 */           AccessibleHTML.TableElementInfo.TableCellElementInfo localTableCellElementInfo = AccessibleHTML.TableElementInfo.this.getCell(paramInt1, paramInt2);
/* 1884 */           if (localTableCellElementInfo != null) {
/* 1885 */             Element localElement = localTableCellElementInfo.getElement();
/* 1886 */             int i = localElement.getStartOffset();
/* 1887 */             int j = localElement.getEndOffset();
/* 1888 */             return (i >= AccessibleHTML.this.editor.getSelectionStart()) && (j <= AccessibleHTML.this.editor.getSelectionEnd());
/*      */           }
/*      */         }
/*      */ 
/* 1892 */         return false;
/*      */       }
/*      */ 
/*      */       public boolean isAccessibleRowSelected(int paramInt)
/*      */       {
/* 1904 */         if (AccessibleHTML.TableElementInfo.this.validateIfNecessary()) {
/* 1905 */           if ((paramInt < 0) || (paramInt >= getAccessibleRowCount())) {
/* 1906 */             return false;
/*      */           }
/* 1908 */           int i = getAccessibleColumnCount();
/*      */ 
/* 1910 */           AccessibleHTML.TableElementInfo.TableCellElementInfo localTableCellElementInfo1 = AccessibleHTML.TableElementInfo.this.getCell(paramInt, 0);
/* 1911 */           if (localTableCellElementInfo1 == null) {
/* 1912 */             return false;
/*      */           }
/* 1914 */           int j = localTableCellElementInfo1.getElement().getStartOffset();
/*      */ 
/* 1916 */           AccessibleHTML.TableElementInfo.TableCellElementInfo localTableCellElementInfo2 = AccessibleHTML.TableElementInfo.this.getCell(paramInt, i - 1);
/* 1917 */           if (localTableCellElementInfo2 == null) {
/* 1918 */             return false;
/*      */           }
/* 1920 */           int k = localTableCellElementInfo2.getElement().getEndOffset();
/*      */ 
/* 1922 */           return (j >= AccessibleHTML.this.editor.getSelectionStart()) && (k <= AccessibleHTML.this.editor.getSelectionEnd());
/*      */         }
/*      */ 
/* 1925 */         return false;
/*      */       }
/*      */ 
/*      */       public boolean isAccessibleColumnSelected(int paramInt)
/*      */       {
/* 1937 */         if (AccessibleHTML.TableElementInfo.this.validateIfNecessary()) {
/* 1938 */           if ((paramInt < 0) || (paramInt >= getAccessibleColumnCount())) {
/* 1939 */             return false;
/*      */           }
/* 1941 */           int i = getAccessibleRowCount();
/*      */ 
/* 1943 */           AccessibleHTML.TableElementInfo.TableCellElementInfo localTableCellElementInfo1 = AccessibleHTML.TableElementInfo.this.getCell(0, paramInt);
/* 1944 */           if (localTableCellElementInfo1 == null) {
/* 1945 */             return false;
/*      */           }
/* 1947 */           int j = localTableCellElementInfo1.getElement().getStartOffset();
/*      */ 
/* 1949 */           AccessibleHTML.TableElementInfo.TableCellElementInfo localTableCellElementInfo2 = AccessibleHTML.TableElementInfo.this.getCell(i - 1, paramInt);
/* 1950 */           if (localTableCellElementInfo2 == null) {
/* 1951 */             return false;
/*      */           }
/* 1953 */           int k = localTableCellElementInfo2.getElement().getEndOffset();
/* 1954 */           return (j >= AccessibleHTML.this.editor.getSelectionStart()) && (k <= AccessibleHTML.this.editor.getSelectionEnd());
/*      */         }
/*      */ 
/* 1957 */         return false;
/*      */       }
/*      */ 
/*      */       public int[] getSelectedAccessibleRows()
/*      */       {
/* 1967 */         if (AccessibleHTML.TableElementInfo.this.validateIfNecessary()) {
/* 1968 */           int i = getAccessibleRowCount();
/* 1969 */           Vector localVector = new Vector();
/*      */ 
/* 1971 */           for (int j = 0; j < i; j++) {
/* 1972 */             if (isAccessibleRowSelected(j)) {
/* 1973 */               localVector.addElement(Integer.valueOf(j));
/*      */             }
/*      */           }
/* 1976 */           int[] arrayOfInt = new int[localVector.size()];
/* 1977 */           for (int k = 0; k < arrayOfInt.length; k++) {
/* 1978 */             arrayOfInt[k] = ((Integer)localVector.elementAt(k)).intValue();
/*      */           }
/* 1980 */           return arrayOfInt;
/*      */         }
/* 1982 */         return new int[0];
/*      */       }
/*      */ 
/*      */       public int[] getSelectedAccessibleColumns()
/*      */       {
/* 1992 */         if (AccessibleHTML.TableElementInfo.this.validateIfNecessary()) {
/* 1993 */           int i = getAccessibleRowCount();
/* 1994 */           Vector localVector = new Vector();
/*      */ 
/* 1996 */           for (int j = 0; j < i; j++) {
/* 1997 */             if (isAccessibleColumnSelected(j)) {
/* 1998 */               localVector.addElement(Integer.valueOf(j));
/*      */             }
/*      */           }
/* 2001 */           int[] arrayOfInt = new int[localVector.size()];
/* 2002 */           for (int k = 0; k < arrayOfInt.length; k++) {
/* 2003 */             arrayOfInt[k] = ((Integer)localVector.elementAt(k)).intValue();
/*      */           }
/* 2005 */           return arrayOfInt;
/*      */         }
/* 2007 */         return new int[0];
/*      */       }
/*      */ 
/*      */       public int getAccessibleRow(int paramInt)
/*      */       {
/* 2020 */         if (AccessibleHTML.TableElementInfo.this.validateIfNecessary()) {
/* 2021 */           int i = getAccessibleColumnCount() * getAccessibleRowCount();
/*      */ 
/* 2023 */           if (paramInt >= i) {
/* 2024 */             return -1;
/*      */           }
/* 2026 */           return paramInt / getAccessibleColumnCount();
/*      */         }
/*      */ 
/* 2029 */         return -1;
/*      */       }
/*      */ 
/*      */       public int getAccessibleColumn(int paramInt)
/*      */       {
/* 2040 */         if (AccessibleHTML.TableElementInfo.this.validateIfNecessary()) {
/* 2041 */           int i = getAccessibleColumnCount() * getAccessibleRowCount();
/*      */ 
/* 2043 */           if (paramInt >= i) {
/* 2044 */             return -1;
/*      */           }
/* 2046 */           return paramInt % getAccessibleColumnCount();
/*      */         }
/*      */ 
/* 2049 */         return -1;
/*      */       }
/*      */ 
/*      */       public int getAccessibleIndex(int paramInt1, int paramInt2)
/*      */       {
/* 2061 */         if (AccessibleHTML.TableElementInfo.this.validateIfNecessary()) {
/* 2062 */           if ((paramInt1 >= getAccessibleRowCount()) || (paramInt2 >= getAccessibleColumnCount()))
/*      */           {
/* 2064 */             return -1;
/*      */           }
/* 2066 */           return paramInt1 * getAccessibleColumnCount() + paramInt2;
/*      */         }
/*      */ 
/* 2069 */         return -1;
/*      */       }
/*      */ 
/*      */       public String getAccessibleRowHeader(int paramInt)
/*      */       {
/* 2080 */         if (AccessibleHTML.TableElementInfo.this.validateIfNecessary()) {
/* 2081 */           AccessibleHTML.TableElementInfo.TableCellElementInfo localTableCellElementInfo = AccessibleHTML.TableElementInfo.this.getCell(paramInt, 0);
/* 2082 */           if (localTableCellElementInfo.isHeaderCell()) {
/* 2083 */             View localView = localTableCellElementInfo.getView();
/* 2084 */             if ((localView != null) && (AccessibleHTML.this.model != null)) {
/*      */               try {
/* 2086 */                 return AccessibleHTML.this.model.getText(localView.getStartOffset(), localView.getEndOffset() - localView.getStartOffset());
/*      */               }
/*      */               catch (BadLocationException localBadLocationException)
/*      */               {
/* 2090 */                 return null;
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/* 2095 */         return null;
/*      */       }
/*      */ 
/*      */       public String getAccessibleColumnHeader(int paramInt)
/*      */       {
/* 2106 */         if (AccessibleHTML.TableElementInfo.this.validateIfNecessary()) {
/* 2107 */           AccessibleHTML.TableElementInfo.TableCellElementInfo localTableCellElementInfo = AccessibleHTML.TableElementInfo.this.getCell(0, paramInt);
/* 2108 */           if (localTableCellElementInfo.isHeaderCell()) {
/* 2109 */             View localView = localTableCellElementInfo.getView();
/* 2110 */             if ((localView != null) && (AccessibleHTML.this.model != null)) {
/*      */               try {
/* 2112 */                 return AccessibleHTML.this.model.getText(localView.getStartOffset(), localView.getEndOffset() - localView.getStartOffset());
/*      */               }
/*      */               catch (BadLocationException localBadLocationException)
/*      */               {
/* 2116 */                 return null;
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/* 2121 */         return null;
/*      */       }
/*      */ 
/*      */       public void addRowHeader(AccessibleHTML.TableElementInfo.TableCellElementInfo paramTableCellElementInfo, int paramInt) {
/* 2125 */         if (this.rowHeadersTable == null) {
/* 2126 */           this.rowHeadersTable = new AccessibleHeadersTable();
/*      */         }
/* 2128 */         this.rowHeadersTable.addHeader(paramTableCellElementInfo, paramInt);
/*      */       }
/*      */ 
/*      */       protected class AccessibleHeadersTable
/*      */         implements AccessibleTable
/*      */       {
/* 2137 */         private Hashtable<Integer, ArrayList<AccessibleHTML.TableElementInfo.TableCellElementInfo>> headers = new Hashtable();
/*      */ 
/* 2139 */         private int rowCount = 0;
/* 2140 */         private int columnCount = 0;
/*      */ 
/*      */         protected AccessibleHeadersTable() {  } 
/* 2143 */         public void addHeader(AccessibleHTML.TableElementInfo.TableCellElementInfo paramTableCellElementInfo, int paramInt) { Integer localInteger = Integer.valueOf(paramInt);
/* 2144 */           ArrayList localArrayList = (ArrayList)this.headers.get(localInteger);
/* 2145 */           if (localArrayList == null) {
/* 2146 */             localArrayList = new ArrayList();
/* 2147 */             this.headers.put(localInteger, localArrayList);
/*      */           }
/* 2149 */           localArrayList.add(paramTableCellElementInfo);
/*      */         }
/*      */ 
/*      */         public Accessible getAccessibleCaption()
/*      */         {
/* 2158 */           return null;
/*      */         }
/*      */ 
/*      */         public void setAccessibleCaption(Accessible paramAccessible)
/*      */         {
/*      */         }
/*      */ 
/*      */         public Accessible getAccessibleSummary()
/*      */         {
/* 2175 */           return null;
/*      */         }
/*      */ 
/*      */         public void setAccessibleSummary(Accessible paramAccessible)
/*      */         {
/*      */         }
/*      */ 
/*      */         public int getAccessibleRowCount()
/*      */         {
/* 2192 */           return this.rowCount;
/*      */         }
/*      */ 
/*      */         public int getAccessibleColumnCount()
/*      */         {
/* 2201 */           return this.columnCount;
/*      */         }
/*      */ 
/*      */         private AccessibleHTML.TableElementInfo.TableCellElementInfo getElementInfoAt(int paramInt1, int paramInt2) {
/* 2205 */           ArrayList localArrayList = (ArrayList)this.headers.get(Integer.valueOf(paramInt1));
/* 2206 */           if (localArrayList != null) {
/* 2207 */             return (AccessibleHTML.TableElementInfo.TableCellElementInfo)localArrayList.get(paramInt2);
/*      */           }
/* 2209 */           return null;
/*      */         }
/*      */ 
/*      */         public Accessible getAccessibleAt(int paramInt1, int paramInt2)
/*      */         {
/* 2222 */           AccessibleHTML.TableElementInfo.TableCellElementInfo localTableCellElementInfo = getElementInfoAt(paramInt1, paramInt2);
/* 2223 */           if ((localTableCellElementInfo instanceof Accessible)) {
/* 2224 */             return (Accessible)localTableCellElementInfo;
/*      */           }
/* 2226 */           return null;
/*      */         }
/*      */ 
/*      */         public int getAccessibleRowExtentAt(int paramInt1, int paramInt2)
/*      */         {
/* 2238 */           AccessibleHTML.TableElementInfo.TableCellElementInfo localTableCellElementInfo = getElementInfoAt(paramInt1, paramInt2);
/* 2239 */           if (localTableCellElementInfo != null) {
/* 2240 */             return localTableCellElementInfo.getRowCount();
/*      */           }
/* 2242 */           return 0;
/*      */         }
/*      */ 
/*      */         public int getAccessibleColumnExtentAt(int paramInt1, int paramInt2)
/*      */         {
/* 2254 */           AccessibleHTML.TableElementInfo.TableCellElementInfo localTableCellElementInfo = getElementInfoAt(paramInt1, paramInt2);
/* 2255 */           if (localTableCellElementInfo != null) {
/* 2256 */             return localTableCellElementInfo.getRowCount();
/*      */           }
/* 2258 */           return 0;
/*      */         }
/*      */ 
/*      */         public AccessibleTable getAccessibleRowHeader()
/*      */         {
/* 2269 */           return null;
/*      */         }
/*      */ 
/*      */         public void setAccessibleRowHeader(AccessibleTable paramAccessibleTable)
/*      */         {
/*      */         }
/*      */ 
/*      */         public AccessibleTable getAccessibleColumnHeader()
/*      */         {
/* 2288 */           return null;
/*      */         }
/*      */ 
/*      */         public void setAccessibleColumnHeader(AccessibleTable paramAccessibleTable)
/*      */         {
/*      */         }
/*      */ 
/*      */         public Accessible getAccessibleRowDescription(int paramInt)
/*      */         {
/* 2307 */           return null;
/*      */         }
/*      */ 
/*      */         public void setAccessibleRowDescription(int paramInt, Accessible paramAccessible)
/*      */         {
/*      */         }
/*      */ 
/*      */         public Accessible getAccessibleColumnDescription(int paramInt)
/*      */         {
/* 2326 */           return null;
/*      */         }
/*      */ 
/*      */         public void setAccessibleColumnDescription(int paramInt, Accessible paramAccessible)
/*      */         {
/*      */         }
/*      */ 
/*      */         public boolean isAccessibleSelected(int paramInt1, int paramInt2)
/*      */         {
/* 2349 */           return false;
/*      */         }
/*      */ 
/*      */         public boolean isAccessibleRowSelected(int paramInt)
/*      */         {
/* 2361 */           return false;
/*      */         }
/*      */ 
/*      */         public boolean isAccessibleColumnSelected(int paramInt)
/*      */         {
/* 2373 */           return false;
/*      */         }
/*      */ 
/*      */         public int[] getSelectedAccessibleRows()
/*      */         {
/* 2383 */           return new int[0];
/*      */         }
/*      */ 
/*      */         public int[] getSelectedAccessibleColumns()
/*      */         {
/* 2393 */           return new int[0];
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     private class TableCellElementInfo extends AccessibleHTML.ElementInfo
/*      */     {
/*      */       private Accessible accessible;
/*      */       private boolean isHeaderCell;
/*      */ 
/*      */       TableCellElementInfo(Element paramElementInfo, AccessibleHTML.ElementInfo arg3)
/*      */       {
/* 2557 */         super(paramElementInfo, localElementInfo);
/* 2558 */         this.isHeaderCell = false;
/*      */       }
/*      */ 
/*      */       TableCellElementInfo(Element paramElementInfo, AccessibleHTML.ElementInfo paramBoolean, boolean arg4)
/*      */       {
/* 2563 */         super(paramElementInfo, paramBoolean);
/*      */         boolean bool;
/* 2564 */         this.isHeaderCell = bool;
/*      */       }
/*      */ 
/*      */       public boolean isHeaderCell()
/*      */       {
/* 2571 */         return this.isHeaderCell;
/*      */       }
/*      */ 
/*      */       public Accessible getAccessible()
/*      */       {
/* 2578 */         this.accessible = null;
/* 2579 */         getAccessible(this);
/* 2580 */         return this.accessible;
/*      */       }
/*      */ 
/*      */       private void getAccessible(AccessibleHTML.ElementInfo paramElementInfo)
/*      */       {
/* 2587 */         if ((paramElementInfo instanceof Accessible))
/* 2588 */           this.accessible = ((Accessible)paramElementInfo);
/*      */         else
/* 2590 */           for (int i = 0; i < paramElementInfo.getChildCount(); i++)
/* 2591 */             getAccessible(paramElementInfo.getChild(i));
/*      */       }
/*      */ 
/*      */       public int getRowCount()
/*      */       {
/* 2600 */         if (validateIfNecessary()) {
/* 2601 */           return Math.max(1, getIntAttr(getAttributes(), HTML.Attribute.ROWSPAN, 1));
/*      */         }
/*      */ 
/* 2604 */         return 0;
/*      */       }
/*      */ 
/*      */       public int getColumnCount()
/*      */       {
/* 2611 */         if (validateIfNecessary()) {
/* 2612 */           return Math.max(1, getIntAttr(getAttributes(), HTML.Attribute.COLSPAN, 1));
/*      */         }
/*      */ 
/* 2615 */         return 0;
/*      */       }
/*      */ 
/*      */       protected void invalidate(boolean paramBoolean)
/*      */       {
/* 2623 */         super.invalidate(paramBoolean);
/* 2624 */         getParent().invalidate(true);
/*      */       }
/*      */     }
/*      */ 
/*      */     private class TableRowElementInfo extends AccessibleHTML.ElementInfo
/*      */     {
/*      */       private AccessibleHTML.TableElementInfo parent;
/*      */       private int rowNumber;
/*      */ 
/*      */       TableRowElementInfo(Element paramTableElementInfo, AccessibleHTML.TableElementInfo paramInt, int arg4)
/*      */       {
/* 2407 */         super(paramTableElementInfo, paramInt);
/* 2408 */         this.parent = paramInt;
/*      */         int i;
/* 2409 */         this.rowNumber = i;
/*      */       }
/*      */ 
/*      */       protected void loadChildren(Element paramElement) {
/* 2413 */         for (int i = 0; i < paramElement.getElementCount(); i++) {
/* 2414 */           AttributeSet localAttributeSet = paramElement.getElement(i).getAttributes();
/*      */ 
/* 2416 */           if (localAttributeSet.getAttribute(StyleConstants.NameAttribute) == HTML.Tag.TH)
/*      */           {
/* 2418 */             AccessibleHTML.TableElementInfo.TableCellElementInfo localTableCellElementInfo = new AccessibleHTML.TableElementInfo.TableCellElementInfo(AccessibleHTML.TableElementInfo.this, paramElement.getElement(i), this, true);
/*      */ 
/* 2420 */             addChild(localTableCellElementInfo);
/*      */ 
/* 2422 */             AccessibleTable localAccessibleTable = this.parent.getAccessibleContext().getAccessibleTable();
/*      */ 
/* 2424 */             AccessibleHTML.TableElementInfo.TableAccessibleContext localTableAccessibleContext = (AccessibleHTML.TableElementInfo.TableAccessibleContext)localAccessibleTable;
/*      */ 
/* 2426 */             localTableAccessibleContext.addRowHeader(localTableCellElementInfo, this.rowNumber);
/*      */           }
/* 2428 */           else if (localAttributeSet.getAttribute(StyleConstants.NameAttribute) == HTML.Tag.TD)
/*      */           {
/* 2430 */             addChild(new AccessibleHTML.TableElementInfo.TableCellElementInfo(AccessibleHTML.TableElementInfo.this, paramElement.getElement(i), this, false));
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/*      */       public int getRowCount()
/*      */       {
/* 2440 */         int i = 1;
/* 2441 */         if (validateIfNecessary()) {
/* 2442 */           for (int j = 0; j < getChildCount(); 
/* 2443 */             j++)
/*      */           {
/* 2445 */             AccessibleHTML.TableElementInfo.TableCellElementInfo localTableCellElementInfo = (AccessibleHTML.TableElementInfo.TableCellElementInfo)getChild(j);
/*      */ 
/* 2448 */             if (localTableCellElementInfo.validateIfNecessary()) {
/* 2449 */               i = Math.max(i, localTableCellElementInfo.getRowCount());
/*      */             }
/*      */           }
/*      */         }
/* 2453 */         return i;
/*      */       }
/*      */ 
/*      */       public int getColumnCount()
/*      */       {
/* 2461 */         int i = 0;
/* 2462 */         if (validateIfNecessary()) {
/* 2463 */           for (int j = 0; j < getChildCount(); 
/* 2464 */             j++) {
/* 2465 */             AccessibleHTML.TableElementInfo.TableCellElementInfo localTableCellElementInfo = (AccessibleHTML.TableElementInfo.TableCellElementInfo)getChild(j);
/*      */ 
/* 2468 */             if (localTableCellElementInfo.validateIfNecessary()) {
/* 2469 */               i += localTableCellElementInfo.getColumnCount();
/*      */             }
/*      */           }
/*      */         }
/* 2473 */         return i;
/*      */       }
/*      */ 
/*      */       protected void invalidate(boolean paramBoolean)
/*      */       {
/* 2481 */         super.invalidate(paramBoolean);
/* 2482 */         getParent().invalidate(true);
/*      */       }
/*      */ 
/*      */       private void updateGrid(int paramInt)
/*      */       {
/* 2490 */         if (validateIfNecessary()) {
/* 2491 */           int i = 0;
/*      */ 
/* 2493 */           while (i == 0) {
/* 2494 */             for (j = 0; j < AccessibleHTML.TableElementInfo.this.grid[paramInt].length; 
/* 2495 */               j++) {
/* 2496 */               if (AccessibleHTML.TableElementInfo.this.grid[paramInt][j] == null) {
/* 2497 */                 i = 1;
/* 2498 */                 break;
/*      */               }
/*      */             }
/* 2501 */             if (i == 0) {
/* 2502 */               paramInt++;
/*      */             }
/*      */           }
/* 2505 */           int j = 0; for (int k = 0; k < getChildCount(); 
/* 2506 */             k++) {
/* 2507 */             AccessibleHTML.TableElementInfo.TableCellElementInfo localTableCellElementInfo = (AccessibleHTML.TableElementInfo.TableCellElementInfo)getChild(k);
/*      */ 
/* 2510 */             while (AccessibleHTML.TableElementInfo.this.grid[paramInt][j] != null) {
/* 2511 */               j++;
/*      */             }
/* 2513 */             for (int m = localTableCellElementInfo.getRowCount() - 1; 
/* 2514 */               m >= 0; m--) {
/* 2515 */               for (int n = localTableCellElementInfo.getColumnCount() - 1; 
/* 2516 */                 n >= 0; n--) {
/* 2517 */                 AccessibleHTML.TableElementInfo.this.grid[(paramInt + m)][(j + n)] = localTableCellElementInfo;
/*      */               }
/*      */             }
/* 2520 */             j += localTableCellElementInfo.getColumnCount();
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/*      */       private int getColumnCount(int paramInt)
/*      */       {
/* 2530 */         if (validateIfNecessary()) {
/* 2531 */           int i = 0;
/* 2532 */           for (int j = 0; j < getChildCount(); 
/* 2533 */             j++) {
/* 2534 */             AccessibleHTML.TableElementInfo.TableCellElementInfo localTableCellElementInfo = (AccessibleHTML.TableElementInfo.TableCellElementInfo)getChild(j);
/*      */ 
/* 2537 */             if (localTableCellElementInfo.getRowCount() >= paramInt) {
/* 2538 */               i += localTableCellElementInfo.getColumnCount();
/*      */             }
/*      */           }
/* 2541 */           return i;
/*      */         }
/* 2543 */         return 0;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   class TextElementInfo extends AccessibleHTML.ElementInfo
/*      */     implements Accessible
/*      */   {
/*      */     private AccessibleContext accessibleContext;
/*      */ 
/*      */     TextElementInfo(Element paramElementInfo, AccessibleHTML.ElementInfo arg3)
/*      */     {
/*  824 */       super(paramElementInfo, localElementInfo);
/*      */     }
/*      */ 
/*      */     public AccessibleContext getAccessibleContext()
/*      */     {
/*  831 */       if (this.accessibleContext == null) {
/*  832 */         this.accessibleContext = new TextAccessibleContext(this);
/*      */       }
/*  834 */       return this.accessibleContext;
/*      */     }
/*      */ 
/*      */     public class TextAccessibleContext extends AccessibleHTML.HTMLAccessibleContext
/*      */       implements AccessibleText
/*      */     {
/*      */       public TextAccessibleContext(AccessibleHTML.ElementInfo arg2)
/*      */       {
/*  844 */         super(localElementInfo);
/*      */       }
/*      */ 
/*      */       public AccessibleText getAccessibleText() {
/*  848 */         return this;
/*      */       }
/*      */ 
/*      */       public String getAccessibleName()
/*      */       {
/*  867 */         if (AccessibleHTML.this.model != null) {
/*  868 */           return (String)AccessibleHTML.this.model.getProperty("title");
/*      */         }
/*  870 */         return null;
/*      */       }
/*      */ 
/*      */       public String getAccessibleDescription()
/*      */       {
/*  885 */         return AccessibleHTML.this.editor.getContentType();
/*      */       }
/*      */ 
/*      */       public AccessibleRole getAccessibleRole()
/*      */       {
/*  907 */         return AccessibleRole.TEXT;
/*      */       }
/*      */ 
/*      */       public int getIndexAtPoint(Point paramPoint)
/*      */       {
/*  920 */         View localView = AccessibleHTML.TextElementInfo.this.getView();
/*  921 */         if (localView != null) {
/*  922 */           return localView.viewToModel(paramPoint.x, paramPoint.y, getBounds());
/*      */         }
/*  924 */         return -1;
/*      */       }
/*      */ 
/*      */       public Rectangle getCharacterBounds(int paramInt)
/*      */       {
/*      */         try
/*      */         {
/*  940 */           return AccessibleHTML.this.editor.getUI().modelToView(AccessibleHTML.this.editor, paramInt); } catch (BadLocationException localBadLocationException) {
/*      */         }
/*  942 */         return null;
/*      */       }
/*      */ 
/*      */       public int getCharCount()
/*      */       {
/*  952 */         if (AccessibleHTML.TextElementInfo.this.validateIfNecessary()) {
/*  953 */           Element localElement = this.elementInfo.getElement();
/*  954 */           return localElement.getEndOffset() - localElement.getStartOffset();
/*      */         }
/*  956 */         return 0;
/*      */       }
/*      */ 
/*      */       public int getCaretPosition()
/*      */       {
/*  967 */         View localView = AccessibleHTML.TextElementInfo.this.getView();
/*  968 */         if (localView == null) {
/*  969 */           return -1;
/*      */         }
/*  971 */         Container localContainer = localView.getContainer();
/*  972 */         if (localContainer == null) {
/*  973 */           return -1;
/*      */         }
/*  975 */         if ((localContainer instanceof JTextComponent)) {
/*  976 */           return ((JTextComponent)localContainer).getCaretPosition();
/*      */         }
/*  978 */         return -1;
/*      */       }
/*      */ 
/*      */       public String getAtIndex(int paramInt1, int paramInt2)
/*      */       {
/*  994 */         return getAtIndex(paramInt1, paramInt2, 0);
/*      */       }
/*      */ 
/*      */       public String getAfterIndex(int paramInt1, int paramInt2)
/*      */       {
/*  999 */         return getAtIndex(paramInt1, paramInt2, 1);
/*      */       }
/*      */ 
/*      */       public String getBeforeIndex(int paramInt1, int paramInt2) {
/* 1003 */         return getAtIndex(paramInt1, paramInt2, -1);
/*      */       }
/*      */ 
/*      */       private String getAtIndex(int paramInt1, int paramInt2, int paramInt3)
/*      */       {
/* 1012 */         if ((AccessibleHTML.this.model instanceof AbstractDocument))
/* 1013 */           ((AbstractDocument)AccessibleHTML.this.model).readLock();
/*      */         try
/*      */         {
/*      */           Object localObject1;
/* 1016 */           if ((paramInt2 < 0) || (paramInt2 >= AccessibleHTML.this.model.getLength())) {
/* 1017 */             return null;
/*      */           }
/* 1019 */           switch (paramInt1) {
/*      */           case 1:
/* 1021 */             if ((paramInt2 + paramInt3 < AccessibleHTML.this.model.getLength()) && (paramInt2 + paramInt3 >= 0))
/*      */             {
/* 1023 */               return AccessibleHTML.this.model.getText(paramInt2 + paramInt3, 1);
/*      */             }
/*      */ 
/*      */             break;
/*      */           case 2:
/*      */           case 3:
/* 1030 */             localObject1 = getSegmentAt(paramInt1, paramInt2);
/* 1031 */             if (localObject1 != null) {
/* 1032 */               if (paramInt3 != 0)
/*      */               {
/*      */                 int i;
/* 1036 */                 if (paramInt3 < 0) {
/* 1037 */                   i = ((IndexedSegment)localObject1).modelOffset - 1;
/*      */                 }
/*      */                 else {
/* 1040 */                   i = ((IndexedSegment)localObject1).modelOffset + paramInt3 * ((IndexedSegment)localObject1).count;
/*      */                 }
/* 1042 */                 if ((i >= 0) && (i <= AccessibleHTML.this.model.getLength())) {
/* 1043 */                   localObject1 = getSegmentAt(paramInt1, i);
/*      */                 }
/*      */                 else {
/* 1046 */                   localObject1 = null;
/*      */                 }
/*      */               }
/* 1049 */               if (localObject1 != null) {
/* 1050 */                 return new String(((IndexedSegment)localObject1).array, ((IndexedSegment)localObject1).offset, ((IndexedSegment)localObject1).count);
/*      */               }
/*      */             }
/*      */             break;
/*      */           }
/*      */         }
/*      */         catch (BadLocationException localBadLocationException)
/*      */         {
/*      */         }
/*      */         finally
/*      */         {
/* 1061 */           if ((AccessibleHTML.this.model instanceof AbstractDocument)) {
/* 1062 */             ((AbstractDocument)AccessibleHTML.this.model).readUnlock();
/*      */           }
/*      */         }
/* 1065 */         return null;
/*      */       }
/*      */ 
/*      */       private Element getParagraphElement(int paramInt)
/*      */       {
/* 1072 */         if ((AccessibleHTML.this.model instanceof PlainDocument)) {
/* 1073 */           localObject = (PlainDocument)AccessibleHTML.this.model;
/* 1074 */           return ((PlainDocument)localObject).getParagraphElement(paramInt);
/* 1075 */         }if ((AccessibleHTML.this.model instanceof StyledDocument)) {
/* 1076 */           localObject = (StyledDocument)AccessibleHTML.this.model;
/* 1077 */           return ((StyledDocument)localObject).getParagraphElement(paramInt);
/*      */         }
/*      */ 
/* 1080 */         for (Object localObject = AccessibleHTML.this.model.getDefaultRootElement(); !((Element)localObject).isLeaf(); ) {
/* 1081 */           int i = ((Element)localObject).getElementIndex(paramInt);
/* 1082 */           localObject = ((Element)localObject).getElement(i);
/*      */         }
/* 1084 */         if (localObject == null) {
/* 1085 */           return null;
/*      */         }
/* 1087 */         return ((Element)localObject).getParentElement();
/*      */       }
/*      */ 
/*      */       private IndexedSegment getParagraphElementText(int paramInt)
/*      */         throws BadLocationException
/*      */       {
/* 1098 */         Element localElement = getParagraphElement(paramInt);
/*      */ 
/* 1101 */         if (localElement != null) {
/* 1102 */           IndexedSegment localIndexedSegment = new IndexedSegment(null);
/*      */           try {
/* 1104 */             int i = localElement.getEndOffset() - localElement.getStartOffset();
/* 1105 */             AccessibleHTML.this.model.getText(localElement.getStartOffset(), i, localIndexedSegment);
/*      */           } catch (BadLocationException localBadLocationException) {
/* 1107 */             return null;
/*      */           }
/* 1109 */           localIndexedSegment.modelOffset = localElement.getStartOffset();
/* 1110 */           return localIndexedSegment;
/*      */         }
/* 1112 */         return null;
/*      */       }
/*      */ 
/*      */       private IndexedSegment getSegmentAt(int paramInt1, int paramInt2)
/*      */         throws BadLocationException
/*      */       {
/* 1127 */         IndexedSegment localIndexedSegment = getParagraphElementText(paramInt2);
/* 1128 */         if (localIndexedSegment == null)
/* 1129 */           return null;
/*      */         BreakIterator localBreakIterator;
/* 1132 */         switch (paramInt1) {
/*      */         case 2:
/* 1134 */           localBreakIterator = BreakIterator.getWordInstance(getLocale());
/* 1135 */           break;
/*      */         case 3:
/* 1137 */           localBreakIterator = BreakIterator.getSentenceInstance(getLocale());
/* 1138 */           break;
/*      */         default:
/* 1140 */           return null;
/*      */         }
/* 1142 */         localIndexedSegment.first();
/* 1143 */         localBreakIterator.setText(localIndexedSegment);
/* 1144 */         int i = localBreakIterator.following(paramInt2 - localIndexedSegment.modelOffset + localIndexedSegment.offset);
/* 1145 */         if (i == -1) {
/* 1146 */           return null;
/*      */         }
/* 1148 */         if (i > localIndexedSegment.offset + localIndexedSegment.count) {
/* 1149 */           return null;
/*      */         }
/* 1151 */         int j = localBreakIterator.previous();
/* 1152 */         if ((j == -1) || (j >= localIndexedSegment.offset + localIndexedSegment.count))
/*      */         {
/* 1154 */           return null;
/*      */         }
/* 1156 */         localIndexedSegment.modelOffset = (localIndexedSegment.modelOffset + j - localIndexedSegment.offset);
/* 1157 */         localIndexedSegment.offset = j;
/* 1158 */         localIndexedSegment.count = (i - j);
/* 1159 */         return localIndexedSegment;
/*      */       }
/*      */ 
/*      */       public AttributeSet getCharacterAttribute(int paramInt)
/*      */       {
/* 1169 */         if ((AccessibleHTML.this.model instanceof StyledDocument)) {
/* 1170 */           StyledDocument localStyledDocument = (StyledDocument)AccessibleHTML.this.model;
/* 1171 */           Element localElement = localStyledDocument.getCharacterElement(paramInt);
/* 1172 */           if (localElement != null) {
/* 1173 */             return localElement.getAttributes();
/*      */           }
/*      */         }
/* 1176 */         return null;
/*      */       }
/*      */ 
/*      */       public int getSelectionStart()
/*      */       {
/* 1187 */         return AccessibleHTML.this.editor.getSelectionStart();
/*      */       }
/*      */ 
/*      */       public int getSelectionEnd()
/*      */       {
/* 1198 */         return AccessibleHTML.this.editor.getSelectionEnd();
/*      */       }
/*      */ 
/*      */       public String getSelectedText()
/*      */       {
/* 1207 */         return AccessibleHTML.this.editor.getSelectedText();
/*      */       }
/*      */ 
/*      */       private String getText(int paramInt1, int paramInt2)
/*      */         throws BadLocationException
/*      */       {
/* 1217 */         if ((AccessibleHTML.this.model != null) && ((AccessibleHTML.this.model instanceof StyledDocument))) {
/* 1218 */           StyledDocument localStyledDocument = (StyledDocument)AccessibleHTML.this.model;
/* 1219 */           return AccessibleHTML.this.model.getText(paramInt1, paramInt2);
/*      */         }
/* 1221 */         return null;
/*      */       }
/*      */ 
/*      */       private class IndexedSegment extends Segment
/*      */       {
/*      */         public int modelOffset;
/*      */ 
/*      */         private IndexedSegment()
/*      */         {
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.text.html.AccessibleHTML
 * JD-Core Version:    0.6.2
 */