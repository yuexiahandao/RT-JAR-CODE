/*      */ package javax.swing.text;
/*      */ 
/*      */ import java.awt.AWTEvent;
/*      */ import java.awt.Color;
/*      */ import java.awt.ComponentOrientation;
/*      */ import java.awt.Container;
/*      */ import java.awt.Dimension;
/*      */ import java.awt.EventQueue;
/*      */ import java.awt.Graphics;
/*      */ import java.awt.GraphicsEnvironment;
/*      */ import java.awt.HeadlessException;
/*      */ import java.awt.IllegalComponentStateException;
/*      */ import java.awt.Insets;
/*      */ import java.awt.Point;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.Shape;
/*      */ import java.awt.datatransfer.Clipboard;
/*      */ import java.awt.datatransfer.DataFlavor;
/*      */ import java.awt.datatransfer.StringSelection;
/*      */ import java.awt.datatransfer.Transferable;
/*      */ import java.awt.datatransfer.UnsupportedFlavorException;
/*      */ import java.awt.event.ActionEvent;
/*      */ import java.awt.event.ComponentAdapter;
/*      */ import java.awt.event.ComponentEvent;
/*      */ import java.awt.event.FocusEvent;
/*      */ import java.awt.event.FocusListener;
/*      */ import java.awt.event.InputEvent;
/*      */ import java.awt.event.InputMethodEvent;
/*      */ import java.awt.event.InputMethodListener;
/*      */ import java.awt.event.KeyEvent;
/*      */ import java.awt.event.MouseEvent;
/*      */ import java.awt.event.MouseListener;
/*      */ import java.awt.font.TextAttribute;
/*      */ import java.awt.font.TextHitInfo;
/*      */ import java.awt.im.InputContext;
/*      */ import java.awt.im.InputMethodRequests;
/*      */ import java.awt.print.Printable;
/*      */ import java.awt.print.PrinterAbortException;
/*      */ import java.awt.print.PrinterException;
/*      */ import java.awt.print.PrinterJob;
/*      */ import java.beans.Transient;
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.PrintStream;
/*      */ import java.io.Reader;
/*      */ import java.io.Serializable;
/*      */ import java.io.Writer;
/*      */ import java.lang.reflect.Method;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.text.AttributedCharacterIterator;
/*      */ import java.text.AttributedCharacterIterator.Attribute;
/*      */ import java.text.AttributedString;
/*      */ import java.text.BreakIterator;
/*      */ import java.text.MessageFormat;
/*      */ import java.util.Collections;
/*      */ import java.util.Enumeration;
/*      */ import java.util.HashMap;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Map;
/*      */ import java.util.Vector;
/*      */ import java.util.concurrent.Callable;
/*      */ import java.util.concurrent.ExecutionException;
/*      */ import java.util.concurrent.Future;
/*      */ import java.util.concurrent.FutureTask;
/*      */ import javax.accessibility.Accessible;
/*      */ import javax.accessibility.AccessibleAction;
/*      */ import javax.accessibility.AccessibleContext;
/*      */ import javax.accessibility.AccessibleEditableText;
/*      */ import javax.accessibility.AccessibleExtendedText;
/*      */ import javax.accessibility.AccessibleRole;
/*      */ import javax.accessibility.AccessibleState;
/*      */ import javax.accessibility.AccessibleStateSet;
/*      */ import javax.accessibility.AccessibleText;
/*      */ import javax.accessibility.AccessibleTextSequence;
/*      */ import javax.print.PrintService;
/*      */ import javax.print.attribute.HashPrintRequestAttributeSet;
/*      */ import javax.print.attribute.PrintRequestAttributeSet;
/*      */ import javax.swing.Action;
/*      */ import javax.swing.ActionMap;
/*      */ import javax.swing.DropMode;
/*      */ import javax.swing.InputMap;
/*      */ import javax.swing.JComponent;
/*      */ import javax.swing.JComponent.AccessibleJComponent;
/*      */ import javax.swing.JViewport;
/*      */ import javax.swing.KeyStroke;
/*      */ import javax.swing.LookAndFeel;
/*      */ import javax.swing.Scrollable;
/*      */ import javax.swing.SwingUtilities;
/*      */ import javax.swing.TransferHandler;
/*      */ import javax.swing.TransferHandler.DropLocation;
/*      */ import javax.swing.UIManager;
/*      */ import javax.swing.event.CaretEvent;
/*      */ import javax.swing.event.CaretListener;
/*      */ import javax.swing.event.ChangeEvent;
/*      */ import javax.swing.event.ChangeListener;
/*      */ import javax.swing.event.DocumentEvent;
/*      */ import javax.swing.event.DocumentListener;
/*      */ import javax.swing.event.EventListenerList;
/*      */ import javax.swing.plaf.TextUI;
/*      */ import javax.swing.plaf.UIResource;
/*      */ import sun.awt.AppContext;
/*      */ import sun.swing.PrintingStatus;
/*      */ import sun.swing.SwingAccessor;
/*      */ import sun.swing.SwingAccessor.JTextComponentAccessor;
/*      */ import sun.swing.SwingUtilities2;
/*      */ import sun.swing.text.TextComponentPrintable;
/*      */ 
/*      */ public abstract class JTextComponent extends JComponent
/*      */   implements Scrollable, Accessible
/*      */ {
/*      */   public static final String FOCUS_ACCELERATOR_KEY = "focusAcceleratorKey";
/*      */   private Document model;
/*      */   private transient Caret caret;
/*      */   private NavigationFilter navigationFilter;
/*      */   private transient Highlighter highlighter;
/*      */   private transient Keymap keymap;
/*      */   private transient MutableCaretEvent caretEvent;
/*      */   private Color caretColor;
/*      */   private Color selectionColor;
/*      */   private Color selectedTextColor;
/*      */   private Color disabledTextColor;
/*      */   private boolean editable;
/*      */   private Insets margin;
/*      */   private char focusAccelerator;
/*      */   private boolean dragEnabled;
/* 3854 */   private DropMode dropMode = DropMode.USE_SELECTION;
/*      */   private transient DropLocation dropLocation;
/*      */   private static DefaultTransferHandler defaultTransferHandler;
/*      */   private static Map<String, Boolean> overrideMap;
/* 4056 */   private static final Object KEYMAP_TABLE = new StringBuilder("JTextComponent_KeymapTable");
/*      */   private transient InputMethodRequests inputMethodRequestsHandler;
/*      */   private SimpleAttributeSet composedTextAttribute;
/*      */   private String composedTextContent;
/*      */   private Position composedTextStart;
/*      */   private Position composedTextEnd;
/*      */   private Position latestCommittedTextStart;
/*      */   private Position latestCommittedTextEnd;
/*      */   private ComposedTextCaret composedTextCaret;
/*      */   private transient Caret originalCaret;
/*      */   private boolean checkedInputOverride;
/*      */   private boolean needToSendKeyTypedEvent;
/* 4390 */   private static final Object FOCUSED_COMPONENT = new StringBuilder("JTextComponent_FocusedComponent");
/*      */   public static final String DEFAULT_KEYMAP = "default";
/*      */ 
/*      */   public JTextComponent()
/*      */   {
/*  315 */     enableEvents(2056L);
/*  316 */     this.caretEvent = new MutableCaretEvent(this);
/*  317 */     addMouseListener(this.caretEvent);
/*  318 */     addFocusListener(this.caretEvent);
/*  319 */     setEditable(true);
/*  320 */     setDragEnabled(false);
/*  321 */     setLayout(null);
/*  322 */     updateUI();
/*      */   }
/*      */ 
/*      */   public TextUI getUI()
/*      */   {
/*  330 */     return (TextUI)this.ui;
/*      */   }
/*      */ 
/*      */   public void setUI(TextUI paramTextUI)
/*      */   {
/*  338 */     super.setUI(paramTextUI);
/*      */   }
/*      */ 
/*      */   public void updateUI()
/*      */   {
/*  348 */     setUI((TextUI)UIManager.getUI(this));
/*  349 */     invalidate();
/*      */   }
/*      */ 
/*      */   public void addCaretListener(CaretListener paramCaretListener)
/*      */   {
/*  360 */     this.listenerList.add(CaretListener.class, paramCaretListener);
/*      */   }
/*      */ 
/*      */   public void removeCaretListener(CaretListener paramCaretListener)
/*      */   {
/*  370 */     this.listenerList.remove(CaretListener.class, paramCaretListener);
/*      */   }
/*      */ 
/*      */   public CaretListener[] getCaretListeners()
/*      */   {
/*  387 */     return (CaretListener[])this.listenerList.getListeners(CaretListener.class);
/*      */   }
/*      */ 
/*      */   protected void fireCaretUpdate(CaretEvent paramCaretEvent)
/*      */   {
/*  402 */     Object[] arrayOfObject = this.listenerList.getListenerList();
/*      */ 
/*  405 */     for (int i = arrayOfObject.length - 2; i >= 0; i -= 2)
/*  406 */       if (arrayOfObject[i] == CaretListener.class)
/*  407 */         ((CaretListener)arrayOfObject[(i + 1)]).caretUpdate(paramCaretEvent);
/*      */   }
/*      */ 
/*      */   public void setDocument(Document paramDocument)
/*      */   {
/*  426 */     Document localDocument = this.model;
/*      */     try
/*      */     {
/*  433 */       if ((localDocument instanceof AbstractDocument)) {
/*  434 */         ((AbstractDocument)localDocument).readLock();
/*      */       }
/*  436 */       if (this.accessibleContext != null) {
/*  437 */         this.model.removeDocumentListener((AccessibleJTextComponent)this.accessibleContext);
/*      */       }
/*      */ 
/*  440 */       if (this.inputMethodRequestsHandler != null) {
/*  441 */         this.model.removeDocumentListener((DocumentListener)this.inputMethodRequestsHandler);
/*      */       }
/*  443 */       this.model = paramDocument;
/*      */ 
/*  447 */       Boolean localBoolean = getComponentOrientation().isLeftToRight() ? TextAttribute.RUN_DIRECTION_LTR : TextAttribute.RUN_DIRECTION_RTL;
/*      */ 
/*  450 */       if (localBoolean != paramDocument.getProperty(TextAttribute.RUN_DIRECTION)) {
/*  451 */         paramDocument.putProperty(TextAttribute.RUN_DIRECTION, localBoolean);
/*      */       }
/*  453 */       firePropertyChange("document", localDocument, paramDocument);
/*      */     } finally {
/*  455 */       if ((localDocument instanceof AbstractDocument)) {
/*  456 */         ((AbstractDocument)localDocument).readUnlock();
/*      */       }
/*      */     }
/*      */ 
/*  460 */     revalidate();
/*  461 */     repaint();
/*  462 */     if (this.accessibleContext != null) {
/*  463 */       this.model.addDocumentListener((AccessibleJTextComponent)this.accessibleContext);
/*      */     }
/*      */ 
/*  466 */     if (this.inputMethodRequestsHandler != null)
/*  467 */       this.model.addDocumentListener((DocumentListener)this.inputMethodRequestsHandler);
/*      */   }
/*      */ 
/*      */   public Document getDocument()
/*      */   {
/*  481 */     return this.model;
/*      */   }
/*      */ 
/*      */   public void setComponentOrientation(ComponentOrientation paramComponentOrientation)
/*      */   {
/*  488 */     Document localDocument = getDocument();
/*  489 */     if (localDocument != null) {
/*  490 */       Boolean localBoolean = paramComponentOrientation.isLeftToRight() ? TextAttribute.RUN_DIRECTION_LTR : TextAttribute.RUN_DIRECTION_RTL;
/*      */ 
/*  493 */       localDocument.putProperty(TextAttribute.RUN_DIRECTION, localBoolean);
/*      */     }
/*  495 */     super.setComponentOrientation(paramComponentOrientation);
/*      */   }
/*      */ 
/*      */   public Action[] getActions()
/*      */   {
/*  508 */     return getUI().getEditorKit(this).getActions();
/*      */   }
/*      */ 
/*      */   public void setMargin(Insets paramInsets)
/*      */   {
/*  527 */     Insets localInsets = this.margin;
/*  528 */     this.margin = paramInsets;
/*  529 */     firePropertyChange("margin", localInsets, paramInsets);
/*  530 */     invalidate();
/*      */   }
/*      */ 
/*      */   public Insets getMargin()
/*      */   {
/*  540 */     return this.margin;
/*      */   }
/*      */ 
/*      */   public void setNavigationFilter(NavigationFilter paramNavigationFilter)
/*      */   {
/*  551 */     this.navigationFilter = paramNavigationFilter;
/*      */   }
/*      */ 
/*      */   public NavigationFilter getNavigationFilter()
/*      */   {
/*  564 */     return this.navigationFilter;
/*      */   }
/*      */ 
/*      */   @Transient
/*      */   public Caret getCaret()
/*      */   {
/*  575 */     return this.caret;
/*      */   }
/*      */ 
/*      */   public void setCaret(Caret paramCaret)
/*      */   {
/*  592 */     if (this.caret != null) {
/*  593 */       this.caret.removeChangeListener(this.caretEvent);
/*  594 */       this.caret.deinstall(this);
/*      */     }
/*  596 */     Caret localCaret = this.caret;
/*  597 */     this.caret = paramCaret;
/*  598 */     if (this.caret != null) {
/*  599 */       this.caret.install(this);
/*  600 */       this.caret.addChangeListener(this.caretEvent);
/*      */     }
/*  602 */     firePropertyChange("caret", localCaret, this.caret);
/*      */   }
/*      */ 
/*      */   public Highlighter getHighlighter()
/*      */   {
/*  611 */     return this.highlighter;
/*      */   }
/*      */ 
/*      */   public void setHighlighter(Highlighter paramHighlighter)
/*      */   {
/*  630 */     if (this.highlighter != null) {
/*  631 */       this.highlighter.deinstall(this);
/*      */     }
/*  633 */     Highlighter localHighlighter = this.highlighter;
/*  634 */     this.highlighter = paramHighlighter;
/*  635 */     if (this.highlighter != null) {
/*  636 */       this.highlighter.install(this);
/*      */     }
/*  638 */     firePropertyChange("highlighter", localHighlighter, paramHighlighter);
/*      */   }
/*      */ 
/*      */   public void setKeymap(Keymap paramKeymap)
/*      */   {
/*  655 */     Keymap localKeymap = this.keymap;
/*  656 */     this.keymap = paramKeymap;
/*  657 */     firePropertyChange("keymap", localKeymap, this.keymap);
/*  658 */     updateInputMap(localKeymap, paramKeymap);
/*      */   }
/*      */ 
/*      */   public void setDragEnabled(boolean paramBoolean)
/*      */   {
/*  695 */     if ((paramBoolean) && (GraphicsEnvironment.isHeadless())) {
/*  696 */       throw new HeadlessException();
/*      */     }
/*  698 */     this.dragEnabled = paramBoolean;
/*      */   }
/*      */ 
/*      */   public boolean getDragEnabled()
/*      */   {
/*  709 */     return this.dragEnabled;
/*      */   }
/*      */ 
/*      */   public final void setDropMode(DropMode paramDropMode)
/*      */   {
/*  739 */     if (paramDropMode != null) {
/*  740 */       switch (5.$SwitchMap$javax$swing$DropMode[paramDropMode.ordinal()]) {
/*      */       case 1:
/*      */       case 2:
/*  743 */         this.dropMode = paramDropMode;
/*  744 */         return;
/*      */       }
/*      */     }
/*      */ 
/*  748 */     throw new IllegalArgumentException(paramDropMode + ": Unsupported drop mode for text");
/*      */   }
/*      */ 
/*      */   public final DropMode getDropMode()
/*      */   {
/*  759 */     return this.dropMode;
/*      */   }
/*      */ 
/*      */   DropLocation dropLocationForPoint(Point paramPoint)
/*      */   {
/*  794 */     Position.Bias[] arrayOfBias = new Position.Bias[1];
/*  795 */     int i = getUI().viewToModel(this, paramPoint, arrayOfBias);
/*      */ 
/*  799 */     if (arrayOfBias[0] == null) {
/*  800 */       arrayOfBias[0] = Position.Bias.Forward;
/*      */     }
/*      */ 
/*  803 */     return new DropLocation(paramPoint, i, arrayOfBias[0], null);
/*      */   }
/*      */ 
/*      */   Object setDropLocation(TransferHandler.DropLocation paramDropLocation, Object paramObject, boolean paramBoolean)
/*      */   {
/*  849 */     Object localObject = null;
/*  850 */     DropLocation localDropLocation1 = (DropLocation)paramDropLocation;
/*      */     boolean bool;
/*  852 */     if (this.dropMode == DropMode.USE_SELECTION) {
/*  853 */       if (localDropLocation1 == null) {
/*  854 */         if (paramObject != null)
/*      */         {
/*  868 */           Object[] arrayOfObject = (Object[])paramObject;
/*      */ 
/*  870 */           if (!paramBoolean) {
/*  871 */             if ((this.caret instanceof DefaultCaret)) {
/*  872 */               ((DefaultCaret)this.caret).setDot(((Integer)arrayOfObject[0]).intValue(), (Position.Bias)arrayOfObject[3]);
/*      */ 
/*  874 */               ((DefaultCaret)this.caret).moveDot(((Integer)arrayOfObject[1]).intValue(), (Position.Bias)arrayOfObject[4]);
/*      */             }
/*      */             else {
/*  877 */               this.caret.setDot(((Integer)arrayOfObject[0]).intValue());
/*  878 */               this.caret.moveDot(((Integer)arrayOfObject[1]).intValue());
/*      */             }
/*      */           }
/*      */ 
/*  882 */           this.caret.setVisible(((Boolean)arrayOfObject[2]).booleanValue());
/*      */         }
/*      */       } else {
/*  885 */         if (this.dropLocation == null)
/*      */         {
/*  888 */           if ((this.caret instanceof DefaultCaret)) {
/*  889 */             DefaultCaret localDefaultCaret = (DefaultCaret)this.caret;
/*  890 */             bool = localDefaultCaret.isActive();
/*  891 */             localObject = new Object[] { Integer.valueOf(localDefaultCaret.getMark()), Integer.valueOf(localDefaultCaret.getDot()), Boolean.valueOf(bool), localDefaultCaret.getMarkBias(), localDefaultCaret.getDotBias() };
/*      */           }
/*      */           else
/*      */           {
/*  897 */             bool = this.caret.isVisible();
/*  898 */             localObject = new Object[] { Integer.valueOf(this.caret.getMark()), Integer.valueOf(this.caret.getDot()), Boolean.valueOf(bool) };
/*      */           }
/*      */ 
/*  903 */           this.caret.setVisible(true);
/*      */         } else {
/*  905 */           localObject = paramObject;
/*      */         }
/*      */ 
/*  908 */         if ((this.caret instanceof DefaultCaret))
/*  909 */           ((DefaultCaret)this.caret).setDot(localDropLocation1.getIndex(), localDropLocation1.getBias());
/*      */         else {
/*  911 */           this.caret.setDot(localDropLocation1.getIndex());
/*      */         }
/*      */       }
/*      */     }
/*  915 */     else if (localDropLocation1 == null) {
/*  916 */       if (paramObject != null) {
/*  917 */         this.caret.setVisible(((Boolean)paramObject).booleanValue());
/*      */       }
/*      */     }
/*  920 */     else if (this.dropLocation == null) {
/*  921 */       bool = (this.caret instanceof DefaultCaret) ? ((DefaultCaret)this.caret).isActive() : this.caret.isVisible();
/*      */ 
/*  924 */       localObject = Boolean.valueOf(bool);
/*  925 */       this.caret.setVisible(false);
/*      */     } else {
/*  927 */       localObject = paramObject;
/*      */     }
/*      */ 
/*  932 */     DropLocation localDropLocation2 = this.dropLocation;
/*  933 */     this.dropLocation = localDropLocation1;
/*  934 */     firePropertyChange("dropLocation", localDropLocation2, this.dropLocation);
/*      */ 
/*  936 */     return localObject;
/*      */   }
/*      */ 
/*      */   public final DropLocation getDropLocation()
/*      */   {
/*  958 */     return this.dropLocation;
/*      */   }
/*      */ 
/*      */   void updateInputMap(Keymap paramKeymap1, Keymap paramKeymap2)
/*      */   {
/*  970 */     InputMap localInputMap1 = getInputMap(0);
/*  971 */     InputMap localInputMap2 = localInputMap1;
/*  972 */     while ((localInputMap1 != null) && (!(localInputMap1 instanceof KeymapWrapper))) {
/*  973 */       localInputMap2 = localInputMap1;
/*  974 */       localInputMap1 = localInputMap1.getParent();
/*      */     }
/*  976 */     if (localInputMap1 != null)
/*      */     {
/*  979 */       if (paramKeymap2 == null) {
/*  980 */         if (localInputMap2 != localInputMap1) {
/*  981 */           localInputMap2.setParent(localInputMap1.getParent());
/*      */         }
/*      */         else
/*  984 */           localInputMap2.setParent(null);
/*      */       }
/*      */       else
/*      */       {
/*  988 */         localObject1 = new KeymapWrapper(paramKeymap2);
/*  989 */         localInputMap2.setParent((InputMap)localObject1);
/*  990 */         if (localInputMap2 != localInputMap1) {
/*  991 */           ((InputMap)localObject1).setParent(localInputMap1.getParent());
/*      */         }
/*      */       }
/*      */     }
/*  995 */     else if (paramKeymap2 != null) {
/*  996 */       localInputMap1 = getInputMap(0);
/*  997 */       if (localInputMap1 != null)
/*      */       {
/* 1000 */         localObject1 = new KeymapWrapper(paramKeymap2);
/* 1001 */         ((InputMap)localObject1).setParent(localInputMap1.getParent());
/* 1002 */         localInputMap1.setParent((InputMap)localObject1);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1007 */     Object localObject1 = getActionMap();
/* 1008 */     Object localObject2 = localObject1;
/* 1009 */     while ((localObject1 != null) && (!(localObject1 instanceof KeymapActionMap))) {
/* 1010 */       localObject2 = localObject1;
/* 1011 */       localObject1 = ((ActionMap)localObject1).getParent();
/*      */     }
/*      */     KeymapActionMap localKeymapActionMap;
/* 1013 */     if (localObject1 != null)
/*      */     {
/* 1016 */       if (paramKeymap2 == null) {
/* 1017 */         if (localObject2 != localObject1) {
/* 1018 */           localObject2.setParent(((ActionMap)localObject1).getParent());
/*      */         }
/*      */         else
/* 1021 */           localObject2.setParent(null);
/*      */       }
/*      */       else
/*      */       {
/* 1025 */         localKeymapActionMap = new KeymapActionMap(paramKeymap2);
/* 1026 */         localObject2.setParent(localKeymapActionMap);
/* 1027 */         if (localObject2 != localObject1) {
/* 1028 */           localKeymapActionMap.setParent(((ActionMap)localObject1).getParent());
/*      */         }
/*      */       }
/*      */     }
/* 1032 */     else if (paramKeymap2 != null) {
/* 1033 */       localObject1 = getActionMap();
/* 1034 */       if (localObject1 != null)
/*      */       {
/* 1037 */         localKeymapActionMap = new KeymapActionMap(paramKeymap2);
/* 1038 */         localKeymapActionMap.setParent(((ActionMap)localObject1).getParent());
/* 1039 */         ((ActionMap)localObject1).setParent(localKeymapActionMap);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public Keymap getKeymap()
/*      */   {
/* 1051 */     return this.keymap;
/*      */   }
/*      */ 
/*      */   public static Keymap addKeymap(String paramString, Keymap paramKeymap)
/*      */   {
/* 1070 */     DefaultKeymap localDefaultKeymap = new DefaultKeymap(paramString, paramKeymap);
/* 1071 */     if (paramString != null)
/*      */     {
/* 1073 */       getKeymapTable().put(paramString, localDefaultKeymap);
/*      */     }
/* 1075 */     return localDefaultKeymap;
/*      */   }
/*      */ 
/*      */   public static Keymap removeKeymap(String paramString)
/*      */   {
/* 1086 */     return (Keymap)getKeymapTable().remove(paramString);
/*      */   }
/*      */ 
/*      */   public static Keymap getKeymap(String paramString)
/*      */   {
/* 1097 */     return (Keymap)getKeymapTable().get(paramString);
/*      */   }
/*      */ 
/*      */   private static HashMap<String, Keymap> getKeymapTable() {
/* 1101 */     synchronized (KEYMAP_TABLE) {
/* 1102 */       AppContext localAppContext = AppContext.getAppContext();
/* 1103 */       HashMap localHashMap = (HashMap)localAppContext.get(KEYMAP_TABLE);
/*      */ 
/* 1105 */       if (localHashMap == null) {
/* 1106 */         localHashMap = new HashMap(17);
/* 1107 */         localAppContext.put(KEYMAP_TABLE, localHashMap);
/*      */ 
/* 1109 */         Keymap localKeymap = addKeymap("default", null);
/* 1110 */         localKeymap.setDefaultAction(new DefaultEditorKit.DefaultKeyTypedAction());
/*      */       }
/*      */ 
/* 1113 */       return localHashMap;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static void loadKeymap(Keymap paramKeymap, KeyBinding[] paramArrayOfKeyBinding, Action[] paramArrayOfAction)
/*      */   {
/* 1189 */     Hashtable localHashtable = new Hashtable();
/*      */     Object localObject2;
/*      */     Object localObject3;
/* 1190 */     for (localObject2 : paramArrayOfAction) {
/* 1191 */       localObject3 = (String)localObject2.getValue("Name");
/* 1192 */       localHashtable.put(localObject3 != null ? localObject3 : "", localObject2);
/*      */     }
/* 1194 */     for (localObject2 : paramArrayOfKeyBinding) {
/* 1195 */       localObject3 = (Action)localHashtable.get(localObject2.actionName);
/* 1196 */       if (localObject3 != null)
/* 1197 */         paramKeymap.addActionForKeyStroke(localObject2.key, (Action)localObject3);
/*      */     }
/*      */   }
/*      */ 
/*      */   private static Boolean isProcessInputMethodEventOverridden(Class<?> paramClass)
/*      */   {
/* 1210 */     if (paramClass == JTextComponent.class) {
/* 1211 */       return Boolean.FALSE;
/*      */     }
/* 1213 */     Boolean localBoolean1 = (Boolean)overrideMap.get(paramClass.getName());
/*      */ 
/* 1215 */     if (localBoolean1 != null) {
/* 1216 */       return localBoolean1;
/*      */     }
/* 1218 */     Boolean localBoolean2 = isProcessInputMethodEventOverridden(paramClass.getSuperclass());
/*      */ 
/* 1221 */     if (localBoolean2.booleanValue())
/*      */     {
/* 1224 */       overrideMap.put(paramClass.getName(), localBoolean2);
/* 1225 */       return localBoolean2;
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/* 1230 */       Class[] arrayOfClass = new Class[1];
/* 1231 */       arrayOfClass[0] = InputMethodEvent.class;
/*      */ 
/* 1233 */       Method localMethod = paramClass.getDeclaredMethod("processInputMethodEvent", arrayOfClass);
/*      */ 
/* 1235 */       localBoolean1 = Boolean.TRUE;
/*      */     } catch (NoSuchMethodException localNoSuchMethodException) {
/* 1237 */       localBoolean1 = Boolean.FALSE;
/*      */     }
/* 1239 */     overrideMap.put(paramClass.getName(), localBoolean1);
/* 1240 */     return localBoolean1;
/*      */   }
/*      */ 
/*      */   public Color getCaretColor()
/*      */   {
/* 1250 */     return this.caretColor;
/*      */   }
/*      */ 
/*      */   public void setCaretColor(Color paramColor)
/*      */   {
/* 1267 */     Color localColor = this.caretColor;
/* 1268 */     this.caretColor = paramColor;
/* 1269 */     firePropertyChange("caretColor", localColor, this.caretColor);
/*      */   }
/*      */ 
/*      */   public Color getSelectionColor()
/*      */   {
/* 1279 */     return this.selectionColor;
/*      */   }
/*      */ 
/*      */   public void setSelectionColor(Color paramColor)
/*      */   {
/* 1296 */     Color localColor = this.selectionColor;
/* 1297 */     this.selectionColor = paramColor;
/* 1298 */     firePropertyChange("selectionColor", localColor, this.selectionColor);
/*      */   }
/*      */ 
/*      */   public Color getSelectedTextColor()
/*      */   {
/* 1308 */     return this.selectedTextColor;
/*      */   }
/*      */ 
/*      */   public void setSelectedTextColor(Color paramColor)
/*      */   {
/* 1325 */     Color localColor = this.selectedTextColor;
/* 1326 */     this.selectedTextColor = paramColor;
/* 1327 */     firePropertyChange("selectedTextColor", localColor, this.selectedTextColor);
/*      */   }
/*      */ 
/*      */   public Color getDisabledTextColor()
/*      */   {
/* 1337 */     return this.disabledTextColor;
/*      */   }
/*      */ 
/*      */   public void setDisabledTextColor(Color paramColor)
/*      */   {
/* 1353 */     Color localColor = this.disabledTextColor;
/* 1354 */     this.disabledTextColor = paramColor;
/* 1355 */     firePropertyChange("disabledTextColor", localColor, this.disabledTextColor);
/*      */   }
/*      */ 
/*      */   public void replaceSelection(String paramString)
/*      */   {
/* 1372 */     Document localDocument = getDocument();
/* 1373 */     if (localDocument != null)
/*      */       try {
/* 1375 */         boolean bool = saveComposedText(this.caret.getDot());
/* 1376 */         int i = Math.min(this.caret.getDot(), this.caret.getMark());
/* 1377 */         int j = Math.max(this.caret.getDot(), this.caret.getMark());
/* 1378 */         if ((localDocument instanceof AbstractDocument)) {
/* 1379 */           ((AbstractDocument)localDocument).replace(i, j - i, paramString, null);
/*      */         }
/*      */         else {
/* 1382 */           if (i != j) {
/* 1383 */             localDocument.remove(i, j - i);
/*      */           }
/* 1385 */           if ((paramString != null) && (paramString.length() > 0)) {
/* 1386 */             localDocument.insertString(i, paramString, null);
/*      */           }
/*      */         }
/* 1389 */         if (bool)
/* 1390 */           restoreComposedText();
/*      */       }
/*      */       catch (BadLocationException localBadLocationException) {
/* 1393 */         UIManager.getLookAndFeel().provideErrorFeedback(this);
/*      */       }
/*      */   }
/*      */ 
/*      */   public String getText(int paramInt1, int paramInt2)
/*      */     throws BadLocationException
/*      */   {
/* 1408 */     return getDocument().getText(paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   public Rectangle modelToView(int paramInt)
/*      */     throws BadLocationException
/*      */   {
/* 1428 */     return getUI().modelToView(this, paramInt);
/*      */   }
/*      */ 
/*      */   public int viewToModel(Point paramPoint)
/*      */   {
/* 1446 */     return getUI().viewToModel(this, paramPoint);
/*      */   }
/*      */ 
/*      */   public void cut()
/*      */   {
/* 1459 */     if ((isEditable()) && (isEnabled()))
/* 1460 */       invokeAction("cut", TransferHandler.getCutAction());
/*      */   }
/*      */ 
/*      */   public void copy()
/*      */   {
/* 1474 */     invokeAction("copy", TransferHandler.getCopyAction());
/*      */   }
/*      */ 
/*      */   public void paste()
/*      */   {
/* 1490 */     if ((isEditable()) && (isEnabled()))
/* 1491 */       invokeAction("paste", TransferHandler.getPasteAction());
/*      */   }
/*      */ 
/*      */   private void invokeAction(String paramString, Action paramAction)
/*      */   {
/* 1503 */     ActionMap localActionMap = getActionMap();
/* 1504 */     Action localAction = null;
/*      */ 
/* 1506 */     if (localActionMap != null) {
/* 1507 */       localAction = localActionMap.get(paramString);
/*      */     }
/* 1509 */     if (localAction == null) {
/* 1510 */       installDefaultTransferHandlerIfNecessary();
/* 1511 */       localAction = paramAction;
/*      */     }
/* 1513 */     localAction.actionPerformed(new ActionEvent(this, 1001, (String)localAction.getValue("Name"), EventQueue.getMostRecentEventTime(), getCurrentEventModifiers()));
/*      */   }
/*      */ 
/*      */   private void installDefaultTransferHandlerIfNecessary()
/*      */   {
/* 1525 */     if (getTransferHandler() == null) {
/* 1526 */       if (defaultTransferHandler == null) {
/* 1527 */         defaultTransferHandler = new DefaultTransferHandler();
/*      */       }
/* 1529 */       setTransferHandler(defaultTransferHandler);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void moveCaretPosition(int paramInt)
/*      */   {
/* 1548 */     Document localDocument = getDocument();
/* 1549 */     if (localDocument != null) {
/* 1550 */       if ((paramInt > localDocument.getLength()) || (paramInt < 0)) {
/* 1551 */         throw new IllegalArgumentException("bad position: " + paramInt);
/*      */       }
/* 1553 */       this.caret.moveDot(paramInt);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setFocusAccelerator(char paramChar)
/*      */   {
/* 1579 */     paramChar = Character.toUpperCase(paramChar);
/* 1580 */     char c = this.focusAccelerator;
/* 1581 */     this.focusAccelerator = paramChar;
/*      */ 
/* 1585 */     firePropertyChange("focusAcceleratorKey", c, this.focusAccelerator);
/* 1586 */     firePropertyChange("focusAccelerator", c, this.focusAccelerator);
/*      */   }
/*      */ 
/*      */   public char getFocusAccelerator()
/*      */   {
/* 1597 */     return this.focusAccelerator;
/*      */   }
/*      */ 
/*      */   public void read(Reader paramReader, Object paramObject)
/*      */     throws IOException
/*      */   {
/* 1620 */     EditorKit localEditorKit = getUI().getEditorKit(this);
/* 1621 */     Document localDocument = localEditorKit.createDefaultDocument();
/* 1622 */     if (paramObject != null)
/* 1623 */       localDocument.putProperty("stream", paramObject);
/*      */     try
/*      */     {
/* 1626 */       localEditorKit.read(paramReader, localDocument, 0);
/* 1627 */       setDocument(localDocument);
/*      */     } catch (BadLocationException localBadLocationException) {
/* 1629 */       throw new IOException(localBadLocationException.getMessage());
/*      */     }
/*      */   }
/*      */ 
/*      */   public void write(Writer paramWriter)
/*      */     throws IOException
/*      */   {
/* 1642 */     Document localDocument = getDocument();
/*      */     try {
/* 1644 */       getUI().getEditorKit(this).write(paramWriter, localDocument, 0, localDocument.getLength());
/*      */     } catch (BadLocationException localBadLocationException) {
/* 1646 */       throw new IOException(localBadLocationException.getMessage());
/*      */     }
/*      */   }
/*      */ 
/*      */   public void removeNotify() {
/* 1651 */     super.removeNotify();
/* 1652 */     if (getFocusedComponent() == this)
/* 1653 */       AppContext.getAppContext().remove(FOCUSED_COMPONENT);
/*      */   }
/*      */ 
/*      */   public void setCaretPosition(int paramInt)
/*      */   {
/* 1675 */     Document localDocument = getDocument();
/* 1676 */     if (localDocument != null) {
/* 1677 */       if ((paramInt > localDocument.getLength()) || (paramInt < 0)) {
/* 1678 */         throw new IllegalArgumentException("bad position: " + paramInt);
/*      */       }
/* 1680 */       this.caret.setDot(paramInt);
/*      */     }
/*      */   }
/*      */ 
/*      */   @Transient
/*      */   public int getCaretPosition()
/*      */   {
/* 1693 */     return this.caret.getDot();
/*      */   }
/*      */ 
/*      */   public void setText(String paramString)
/*      */   {
/*      */     try
/*      */     {
/* 1716 */       Document localDocument = getDocument();
/* 1717 */       if ((localDocument instanceof AbstractDocument)) {
/* 1718 */         ((AbstractDocument)localDocument).replace(0, localDocument.getLength(), paramString, null);
/*      */       }
/*      */       else {
/* 1721 */         localDocument.remove(0, localDocument.getLength());
/* 1722 */         localDocument.insertString(0, paramString, null);
/*      */       }
/*      */     } catch (BadLocationException localBadLocationException) {
/* 1725 */       UIManager.getLookAndFeel().provideErrorFeedback(this);
/*      */     }
/*      */   }
/*      */ 
/*      */   public String getText()
/*      */   {
/* 1743 */     Document localDocument = getDocument();
/*      */     String str;
/*      */     try
/*      */     {
/* 1746 */       str = localDocument.getText(0, localDocument.getLength());
/*      */     } catch (BadLocationException localBadLocationException) {
/* 1748 */       str = null;
/*      */     }
/* 1750 */     return str;
/*      */   }
/*      */ 
/*      */   public String getSelectedText()
/*      */   {
/* 1764 */     String str = null;
/* 1765 */     int i = Math.min(this.caret.getDot(), this.caret.getMark());
/* 1766 */     int j = Math.max(this.caret.getDot(), this.caret.getMark());
/* 1767 */     if (i != j) {
/*      */       try {
/* 1769 */         Document localDocument = getDocument();
/* 1770 */         str = localDocument.getText(i, j - i);
/*      */       } catch (BadLocationException localBadLocationException) {
/* 1772 */         throw new IllegalArgumentException(localBadLocationException.getMessage());
/*      */       }
/*      */     }
/* 1775 */     return str;
/*      */   }
/*      */ 
/*      */   public boolean isEditable()
/*      */   {
/* 1786 */     return this.editable;
/*      */   }
/*      */ 
/*      */   public void setEditable(boolean paramBoolean)
/*      */   {
/* 1802 */     if (paramBoolean != this.editable) {
/* 1803 */       boolean bool = this.editable;
/* 1804 */       this.editable = paramBoolean;
/* 1805 */       enableInputMethods(this.editable);
/* 1806 */       firePropertyChange("editable", Boolean.valueOf(bool), Boolean.valueOf(this.editable));
/* 1807 */       repaint();
/*      */     }
/*      */   }
/*      */ 
/*      */   @Transient
/*      */   public int getSelectionStart()
/*      */   {
/* 1819 */     int i = Math.min(this.caret.getDot(), this.caret.getMark());
/* 1820 */     return i;
/*      */   }
/*      */ 
/*      */   public void setSelectionStart(int paramInt)
/*      */   {
/* 1841 */     select(paramInt, getSelectionEnd());
/*      */   }
/*      */ 
/*      */   @Transient
/*      */   public int getSelectionEnd()
/*      */   {
/* 1852 */     int i = Math.max(this.caret.getDot(), this.caret.getMark());
/* 1853 */     return i;
/*      */   }
/*      */ 
/*      */   public void setSelectionEnd(int paramInt)
/*      */   {
/* 1874 */     select(getSelectionStart(), paramInt);
/*      */   }
/*      */ 
/*      */   public void select(int paramInt1, int paramInt2)
/*      */   {
/* 1907 */     int i = getDocument().getLength();
/*      */ 
/* 1909 */     if (paramInt1 < 0) {
/* 1910 */       paramInt1 = 0;
/*      */     }
/* 1912 */     if (paramInt1 > i) {
/* 1913 */       paramInt1 = i;
/*      */     }
/* 1915 */     if (paramInt2 > i) {
/* 1916 */       paramInt2 = i;
/*      */     }
/* 1918 */     if (paramInt2 < paramInt1) {
/* 1919 */       paramInt2 = paramInt1;
/*      */     }
/*      */ 
/* 1922 */     setCaretPosition(paramInt1);
/* 1923 */     moveCaretPosition(paramInt2);
/*      */   }
/*      */ 
/*      */   public void selectAll()
/*      */   {
/* 1931 */     Document localDocument = getDocument();
/* 1932 */     if (localDocument != null) {
/* 1933 */       setCaretPosition(0);
/* 1934 */       moveCaretPosition(localDocument.getLength());
/*      */     }
/*      */   }
/*      */ 
/*      */   public String getToolTipText(MouseEvent paramMouseEvent)
/*      */   {
/* 1963 */     String str = super.getToolTipText(paramMouseEvent);
/*      */ 
/* 1965 */     if (str == null) {
/* 1966 */       TextUI localTextUI = getUI();
/* 1967 */       if (localTextUI != null) {
/* 1968 */         str = localTextUI.getToolTipText(this, new Point(paramMouseEvent.getX(), paramMouseEvent.getY()));
/*      */       }
/*      */     }
/*      */ 
/* 1972 */     return str;
/*      */   }
/*      */ 
/*      */   public Dimension getPreferredScrollableViewportSize()
/*      */   {
/* 1986 */     return getPreferredSize();
/*      */   }
/*      */ 
/*      */   public int getScrollableUnitIncrement(Rectangle paramRectangle, int paramInt1, int paramInt2)
/*      */   {
/* 2011 */     switch (paramInt1) {
/*      */     case 1:
/* 2013 */       return paramRectangle.height / 10;
/*      */     case 0:
/* 2015 */       return paramRectangle.width / 10;
/*      */     }
/* 2017 */     throw new IllegalArgumentException("Invalid orientation: " + paramInt1);
/*      */   }
/*      */ 
/*      */   public int getScrollableBlockIncrement(Rectangle paramRectangle, int paramInt1, int paramInt2)
/*      */   {
/* 2041 */     switch (paramInt1) {
/*      */     case 1:
/* 2043 */       return paramRectangle.height;
/*      */     case 0:
/* 2045 */       return paramRectangle.width;
/*      */     }
/* 2047 */     throw new IllegalArgumentException("Invalid orientation: " + paramInt1);
/*      */   }
/*      */ 
/*      */   public boolean getScrollableTracksViewportWidth()
/*      */   {
/* 2069 */     Container localContainer = SwingUtilities.getUnwrappedParent(this);
/* 2070 */     if ((localContainer instanceof JViewport)) {
/* 2071 */       return localContainer.getWidth() > getPreferredSize().width;
/*      */     }
/* 2073 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean getScrollableTracksViewportHeight()
/*      */   {
/* 2090 */     Container localContainer = SwingUtilities.getUnwrappedParent(this);
/* 2091 */     if ((localContainer instanceof JViewport)) {
/* 2092 */       return localContainer.getHeight() > getPreferredSize().height;
/*      */     }
/* 2094 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean print()
/*      */     throws PrinterException
/*      */   {
/* 2125 */     return print(null, null, true, null, null, true);
/*      */   }
/*      */ 
/*      */   public boolean print(MessageFormat paramMessageFormat1, MessageFormat paramMessageFormat2)
/*      */     throws PrinterException
/*      */   {
/* 2155 */     return print(paramMessageFormat1, paramMessageFormat2, true, null, null, true);
/*      */   }
/*      */ 
/*      */   public boolean print(MessageFormat paramMessageFormat1, MessageFormat paramMessageFormat2, boolean paramBoolean1, PrintService paramPrintService, PrintRequestAttributeSet paramPrintRequestAttributeSet, boolean paramBoolean2)
/*      */     throws PrinterException
/*      */   {
/* 2276 */     final PrinterJob localPrinterJob = PrinterJob.getPrinterJob();
/*      */ 
/* 2279 */     boolean bool1 = GraphicsEnvironment.isHeadless();
/* 2280 */     final boolean bool2 = SwingUtilities.isEventDispatchThread();
/*      */ 
/* 2282 */     Printable localPrintable2 = getPrintable(paramMessageFormat1, paramMessageFormat2);
/*      */     final PrintingStatus localPrintingStatus;
/*      */     Printable localPrintable1;
/* 2283 */     if ((paramBoolean2) && (!bool1)) {
/* 2284 */       localPrintingStatus = PrintingStatus.createPrintingStatus(this, localPrinterJob);
/*      */ 
/* 2286 */       localPrintable1 = localPrintingStatus.createNotificationPrintable(localPrintable2);
/*      */     }
/*      */     else {
/* 2289 */       localPrintingStatus = null;
/* 2290 */       localPrintable1 = localPrintable2;
/*      */     }
/*      */ 
/* 2293 */     if (paramPrintService != null) {
/* 2294 */       localPrinterJob.setPrintService(paramPrintService);
/*      */     }
/*      */ 
/* 2297 */     localPrinterJob.setPrintable(localPrintable1);
/*      */ 
/* 2299 */     final PrintRequestAttributeSet localPrintRequestAttributeSet = paramPrintRequestAttributeSet == null ? new HashPrintRequestAttributeSet() : paramPrintRequestAttributeSet;
/*      */ 
/* 2303 */     if ((paramBoolean1) && (!bool1) && (!localPrinterJob.printDialog(localPrintRequestAttributeSet))) {
/* 2304 */       return false;
/*      */     }
/*      */ 
/* 2316 */     Callable local2 = new Callable()
/*      */     {
/*      */       public Object call() throws Exception {
/*      */         try {
/* 2320 */           localPrinterJob.print(localPrintRequestAttributeSet);
/*      */         } finally {
/* 2322 */           if (localPrintingStatus != null) {
/* 2323 */             localPrintingStatus.dispose();
/*      */           }
/*      */         }
/* 2326 */         return null;
/*      */       }
/*      */     };
/* 2330 */     final FutureTask localFutureTask = new FutureTask(local2);
/*      */ 
/* 2333 */     Runnable local3 = new Runnable()
/*      */     {
/*      */       public void run()
/*      */       {
/* 2337 */         boolean bool = false;
/*      */         Throwable localThrowable;
/* 2338 */         if (bool2) {
/* 2339 */           if (JTextComponent.this.isEnabled()) {
/* 2340 */             bool = true;
/* 2341 */             JTextComponent.this.setEnabled(false);
/*      */           }
/*      */         }
/*      */         else try {
/* 2345 */             bool = ((Boolean)SwingUtilities2.submit(new Callable()
/*      */             {
/*      */               public Boolean call() throws Exception {
/* 2348 */                 boolean bool = JTextComponent.this.isEnabled();
/* 2349 */                 if (bool) {
/* 2350 */                   JTextComponent.this.setEnabled(false);
/*      */                 }
/* 2352 */                 return Boolean.valueOf(bool);
/*      */               }
/*      */             }).get()).booleanValue();
/*      */           }
/*      */           catch (InterruptedException localInterruptedException1)
/*      */           {
/* 2356 */             throw new RuntimeException(localInterruptedException1);
/*      */           } catch (ExecutionException localExecutionException1) {
/* 2358 */             localThrowable = localExecutionException1.getCause();
/* 2359 */             if ((localThrowable instanceof Error)) {
/* 2360 */               throw ((Error)localThrowable);
/*      */             }
/* 2362 */             if ((localThrowable instanceof RuntimeException)) {
/* 2363 */               throw ((RuntimeException)localThrowable);
/*      */             }
/* 2365 */             throw new AssertionError(localThrowable);
/*      */           }
/*      */ 
/*      */ 
/* 2369 */         JTextComponent.this.getDocument().render(localFutureTask);
/*      */ 
/* 2372 */         if (bool)
/* 2373 */           if (bool2)
/* 2374 */             JTextComponent.this.setEnabled(true);
/*      */           else
/*      */             try {
/* 2377 */               SwingUtilities2.submit(new Runnable()
/*      */               {
/*      */                 public void run() {
/* 2380 */                   JTextComponent.this.setEnabled(true);
/*      */                 }
/*      */               }
/*      */               , null).get();
/*      */             }
/*      */             catch (InterruptedException localInterruptedException2)
/*      */             {
/* 2384 */               throw new RuntimeException(localInterruptedException2);
/*      */             } catch (ExecutionException localExecutionException2) {
/* 2386 */               localThrowable = localExecutionException2.getCause();
/* 2387 */               if ((localThrowable instanceof Error)) {
/* 2388 */                 throw ((Error)localThrowable);
/*      */               }
/* 2390 */               if ((localThrowable instanceof RuntimeException)) {
/* 2391 */                 throw ((RuntimeException)localThrowable);
/*      */               }
/* 2393 */               throw new AssertionError(localThrowable);
/*      */             }
/*      */       }
/*      */     };
/* 2400 */     if ((!paramBoolean2) || (bool1)) {
/* 2401 */       local3.run();
/*      */     }
/* 2403 */     else if (bool2) {
/* 2404 */       new Thread(local3).start();
/* 2405 */       localPrintingStatus.showModal(true);
/*      */     } else {
/* 2407 */       localPrintingStatus.showModal(false);
/* 2408 */       local3.run();
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/* 2415 */       localFutureTask.get();
/*      */     } catch (InterruptedException localInterruptedException) {
/* 2417 */       throw new RuntimeException(localInterruptedException);
/*      */     } catch (ExecutionException localExecutionException) {
/* 2419 */       Throwable localThrowable = localExecutionException.getCause();
/* 2420 */       if ((localThrowable instanceof PrinterAbortException)) {
/* 2421 */         if ((localPrintingStatus != null) && (localPrintingStatus.isAborted()))
/*      */         {
/* 2423 */           return false;
/*      */         }
/* 2425 */         throw ((PrinterAbortException)localThrowable);
/*      */       }
/* 2427 */       if ((localThrowable instanceof PrinterException))
/* 2428 */         throw ((PrinterException)localThrowable);
/* 2429 */       if ((localThrowable instanceof RuntimeException))
/* 2430 */         throw ((RuntimeException)localThrowable);
/* 2431 */       if ((localThrowable instanceof Error)) {
/* 2432 */         throw ((Error)localThrowable);
/*      */       }
/* 2434 */       throw new AssertionError(localThrowable);
/*      */     }
/*      */ 
/* 2437 */     return true;
/*      */   }
/*      */ 
/*      */   public Printable getPrintable(MessageFormat paramMessageFormat1, MessageFormat paramMessageFormat2)
/*      */   {
/* 2501 */     return TextComponentPrintable.getPrintable(this, paramMessageFormat1, paramMessageFormat2);
/*      */   }
/*      */ 
/*      */   public AccessibleContext getAccessibleContext()
/*      */   {
/* 2524 */     if (this.accessibleContext == null) {
/* 2525 */       this.accessibleContext = new AccessibleJTextComponent();
/*      */     }
/* 2527 */     return this.accessibleContext;
/*      */   }
/*      */ 
/*      */   private void readObject(ObjectInputStream paramObjectInputStream)
/*      */     throws IOException, ClassNotFoundException
/*      */   {
/* 3795 */     paramObjectInputStream.defaultReadObject();
/* 3796 */     this.caretEvent = new MutableCaretEvent(this);
/* 3797 */     addMouseListener(this.caretEvent);
/* 3798 */     addFocusListener(this.caretEvent);
/*      */   }
/*      */ 
/*      */   protected String paramString()
/*      */   {
/* 3937 */     String str1 = this.editable ? "true" : "false";
/*      */ 
/* 3939 */     String str2 = this.caretColor != null ? this.caretColor.toString() : "";
/*      */ 
/* 3941 */     String str3 = this.selectionColor != null ? this.selectionColor.toString() : "";
/*      */ 
/* 3943 */     String str4 = this.selectedTextColor != null ? this.selectedTextColor.toString() : "";
/*      */ 
/* 3945 */     String str5 = this.disabledTextColor != null ? this.disabledTextColor.toString() : "";
/*      */ 
/* 3947 */     String str6 = this.margin != null ? this.margin.toString() : "";
/*      */ 
/* 3950 */     return super.paramString() + ",caretColor=" + str2 + ",disabledTextColor=" + str5 + ",editable=" + str1 + ",margin=" + str6 + ",selectedTextColor=" + str4 + ",selectionColor=" + str3;
/*      */   }
/*      */ 
/*      */   static final JTextComponent getFocusedComponent()
/*      */   {
/* 4041 */     return (JTextComponent)AppContext.getAppContext().get(FOCUSED_COMPONENT);
/*      */   }
/*      */ 
/*      */   private int getCurrentEventModifiers()
/*      */   {
/* 4046 */     int i = 0;
/* 4047 */     AWTEvent localAWTEvent = EventQueue.getCurrentEvent();
/* 4048 */     if ((localAWTEvent instanceof InputEvent))
/* 4049 */       i = ((InputEvent)localAWTEvent).getModifiers();
/* 4050 */     else if ((localAWTEvent instanceof ActionEvent)) {
/* 4051 */       i = ((ActionEvent)localAWTEvent).getModifiers();
/*      */     }
/* 4053 */     return i;
/*      */   }
/*      */ 
/*      */   protected void processInputMethodEvent(InputMethodEvent paramInputMethodEvent)
/*      */   {
/* 4498 */     super.processInputMethodEvent(paramInputMethodEvent);
/*      */ 
/* 4500 */     if (!paramInputMethodEvent.isConsumed()) {
/* 4501 */       if (!isEditable()) {
/* 4502 */         return;
/*      */       }
/* 4504 */       switch (paramInputMethodEvent.getID()) {
/*      */       case 1100:
/* 4506 */         replaceInputMethodText(paramInputMethodEvent);
/*      */       case 1101:
/* 4511 */         setInputMethodCaretPosition(paramInputMethodEvent);
/*      */       }
/*      */ 
/* 4516 */       paramInputMethodEvent.consume();
/*      */     }
/*      */   }
/*      */ 
/*      */   public InputMethodRequests getInputMethodRequests()
/*      */   {
/* 4524 */     if (this.inputMethodRequestsHandler == null) {
/* 4525 */       this.inputMethodRequestsHandler = new InputMethodRequestsHandler();
/* 4526 */       Document localDocument = getDocument();
/* 4527 */       if (localDocument != null) {
/* 4528 */         localDocument.addDocumentListener((DocumentListener)this.inputMethodRequestsHandler);
/*      */       }
/*      */     }
/*      */ 
/* 4532 */     return this.inputMethodRequestsHandler;
/*      */   }
/*      */ 
/*      */   public void addInputMethodListener(InputMethodListener paramInputMethodListener)
/*      */   {
/* 4539 */     super.addInputMethodListener(paramInputMethodListener);
/* 4540 */     if (paramInputMethodListener != null) {
/* 4541 */       this.needToSendKeyTypedEvent = false;
/* 4542 */       this.checkedInputOverride = true;
/*      */     }
/*      */   }
/*      */ 
/*      */   private void replaceInputMethodText(InputMethodEvent paramInputMethodEvent)
/*      */   {
/* 4710 */     int i = paramInputMethodEvent.getCommittedCharacterCount();
/* 4711 */     AttributedCharacterIterator localAttributedCharacterIterator = paramInputMethodEvent.getText();
/*      */ 
/* 4715 */     Document localDocument = getDocument();
/* 4716 */     if (composedTextExists()) {
/*      */       try {
/* 4718 */         localDocument.remove(this.composedTextStart.getOffset(), this.composedTextEnd.getOffset() - this.composedTextStart.getOffset());
/*      */       }
/*      */       catch (BadLocationException localBadLocationException1) {
/*      */       }
/* 4722 */       this.composedTextStart = (this.composedTextEnd = null);
/* 4723 */       this.composedTextAttribute = null;
/* 4724 */       this.composedTextContent = null;
/*      */     }
/*      */ 
/* 4727 */     if (localAttributedCharacterIterator != null) {
/* 4728 */       localAttributedCharacterIterator.first();
/* 4729 */       int k = 0;
/* 4730 */       int m = 0;
/*      */ 
/* 4733 */       if (i > 0)
/*      */       {
/* 4735 */         k = this.caret.getDot();
/*      */ 
/* 4739 */         if (shouldSynthensizeKeyEvents()) {
/* 4740 */           for (char c1 = localAttributedCharacterIterator.current(); i > 0; 
/* 4741 */             i--) {
/* 4742 */             KeyEvent localKeyEvent = new KeyEvent(this, 400, EventQueue.getMostRecentEventTime(), 0, 0, c1);
/*      */ 
/* 4745 */             processKeyEvent(localKeyEvent);
/*      */ 
/* 4741 */             c1 = localAttributedCharacterIterator.next();
/*      */           }
/*      */ 
/*      */         }
/*      */         else
/*      */         {
/* 4748 */           StringBuilder localStringBuilder = new StringBuilder();
/* 4749 */           for (char c2 = localAttributedCharacterIterator.current(); i > 0; 
/* 4750 */             i--) {
/* 4751 */             localStringBuilder.append(c2);
/*      */ 
/* 4750 */             c2 = localAttributedCharacterIterator.next();
/*      */           }
/*      */ 
/* 4755 */           mapCommittedTextToAction(localStringBuilder.toString());
/*      */         }
/*      */ 
/* 4759 */         m = this.caret.getDot();
/*      */       }
/*      */ 
/* 4763 */       int j = localAttributedCharacterIterator.getIndex();
/* 4764 */       if (j < localAttributedCharacterIterator.getEndIndex()) {
/* 4765 */         createComposedTextAttribute(j, localAttributedCharacterIterator);
/*      */         try {
/* 4767 */           replaceSelection(null);
/* 4768 */           localDocument.insertString(this.caret.getDot(), this.composedTextContent, this.composedTextAttribute);
/*      */ 
/* 4770 */           this.composedTextStart = localDocument.createPosition(this.caret.getDot() - this.composedTextContent.length());
/*      */ 
/* 4772 */           this.composedTextEnd = localDocument.createPosition(this.caret.getDot());
/*      */         } catch (BadLocationException localBadLocationException2) {
/* 4774 */           this.composedTextStart = (this.composedTextEnd = null);
/* 4775 */           this.composedTextAttribute = null;
/* 4776 */           this.composedTextContent = null;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 4781 */       if (k != m) {
/*      */         try {
/* 4783 */           this.latestCommittedTextStart = localDocument.createPosition(k);
/*      */ 
/* 4785 */           this.latestCommittedTextEnd = localDocument.createPosition(m);
/*      */         }
/*      */         catch (BadLocationException localBadLocationException3) {
/* 4788 */           this.latestCommittedTextStart = (this.latestCommittedTextEnd = null);
/*      */         }
/*      */       }
/*      */       else
/* 4792 */         this.latestCommittedTextStart = (this.latestCommittedTextEnd = null);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void createComposedTextAttribute(int paramInt, AttributedCharacterIterator paramAttributedCharacterIterator)
/*      */   {
/* 4800 */     Document localDocument = getDocument();
/* 4801 */     StringBuilder localStringBuilder = new StringBuilder();
/*      */     int j;
/* 4804 */     for (int i = paramAttributedCharacterIterator.setIndex(paramInt); 
/* 4805 */       i != 65535; j = paramAttributedCharacterIterator.next()) {
/* 4806 */       localStringBuilder.append(i);
/*      */     }
/*      */ 
/* 4809 */     this.composedTextContent = localStringBuilder.toString();
/* 4810 */     this.composedTextAttribute = new SimpleAttributeSet();
/* 4811 */     this.composedTextAttribute.addAttribute(StyleConstants.ComposedTextAttribute, new AttributedString(paramAttributedCharacterIterator, paramInt, paramAttributedCharacterIterator.getEndIndex()));
/*      */   }
/*      */ 
/*      */   protected boolean saveComposedText(int paramInt)
/*      */   {
/* 4828 */     if (composedTextExists()) {
/* 4829 */       int i = this.composedTextStart.getOffset();
/* 4830 */       int j = this.composedTextEnd.getOffset() - this.composedTextStart.getOffset();
/*      */ 
/* 4832 */       if ((paramInt >= i) && (paramInt <= i + j))
/*      */         try {
/* 4834 */           getDocument().remove(i, j);
/* 4835 */           return true;
/*      */         } catch (BadLocationException localBadLocationException) {
/*      */         }
/*      */     }
/* 4839 */     return false;
/*      */   }
/*      */ 
/*      */   protected void restoreComposedText()
/*      */   {
/* 4852 */     Document localDocument = getDocument();
/*      */     try {
/* 4854 */       localDocument.insertString(this.caret.getDot(), this.composedTextContent, this.composedTextAttribute);
/*      */ 
/* 4857 */       this.composedTextStart = localDocument.createPosition(this.caret.getDot() - this.composedTextContent.length());
/*      */ 
/* 4859 */       this.composedTextEnd = localDocument.createPosition(this.caret.getDot());
/*      */     }
/*      */     catch (BadLocationException localBadLocationException)
/*      */     {
/*      */     }
/*      */   }
/*      */ 
/*      */   private void mapCommittedTextToAction(String paramString)
/*      */   {
/* 4869 */     Keymap localKeymap = getKeymap();
/* 4870 */     if (localKeymap != null) {
/* 4871 */       Action localAction = null;
/*      */       Object localObject;
/* 4872 */       if (paramString.length() == 1) {
/* 4873 */         localObject = KeyStroke.getKeyStroke(paramString.charAt(0));
/* 4874 */         localAction = localKeymap.getAction((KeyStroke)localObject);
/*      */       }
/*      */ 
/* 4877 */       if (localAction == null) {
/* 4878 */         localAction = localKeymap.getDefaultAction();
/*      */       }
/*      */ 
/* 4881 */       if (localAction != null) {
/* 4882 */         localObject = new ActionEvent(this, 1001, paramString, EventQueue.getMostRecentEventTime(), getCurrentEventModifiers());
/*      */ 
/* 4887 */         localAction.actionPerformed((ActionEvent)localObject);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void setInputMethodCaretPosition(InputMethodEvent paramInputMethodEvent)
/*      */   {
/*      */     int i;
/* 4899 */     if (composedTextExists()) {
/* 4900 */       i = this.composedTextStart.getOffset();
/* 4901 */       if (!(this.caret instanceof ComposedTextCaret)) {
/* 4902 */         if (this.composedTextCaret == null) {
/* 4903 */           this.composedTextCaret = new ComposedTextCaret();
/*      */         }
/* 4905 */         this.originalCaret = this.caret;
/*      */ 
/* 4907 */         exchangeCaret(this.originalCaret, this.composedTextCaret);
/*      */       }
/*      */ 
/* 4910 */       TextHitInfo localTextHitInfo = paramInputMethodEvent.getCaret();
/* 4911 */       if (localTextHitInfo != null) {
/* 4912 */         int j = localTextHitInfo.getInsertionIndex();
/* 4913 */         i += j;
/* 4914 */         if (j == 0)
/*      */         {
/*      */           try
/*      */           {
/* 4918 */             Rectangle localRectangle1 = modelToView(i);
/* 4919 */             Rectangle localRectangle2 = modelToView(this.composedTextEnd.getOffset());
/* 4920 */             Rectangle localRectangle3 = getBounds();
/* 4921 */             localRectangle1.x += Math.min(localRectangle2.x - localRectangle1.x, localRectangle3.width);
/* 4922 */             scrollRectToVisible(localRectangle1); } catch (BadLocationException localBadLocationException) {
/*      */           }
/*      */         }
/*      */       }
/* 4926 */       this.caret.setDot(i);
/* 4927 */     } else if ((this.caret instanceof ComposedTextCaret)) {
/* 4928 */       i = this.caret.getDot();
/*      */ 
/* 4930 */       exchangeCaret(this.caret, this.originalCaret);
/* 4931 */       this.caret.setDot(i);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void exchangeCaret(Caret paramCaret1, Caret paramCaret2) {
/* 4936 */     int i = paramCaret1.getBlinkRate();
/* 4937 */     setCaret(paramCaret2);
/* 4938 */     this.caret.setBlinkRate(i);
/* 4939 */     this.caret.setVisible(hasFocus());
/*      */   }
/*      */ 
/*      */   private boolean shouldSynthensizeKeyEvents()
/*      */   {
/* 4946 */     if (!this.checkedInputOverride) {
/* 4947 */       this.checkedInputOverride = true;
/* 4948 */       this.needToSendKeyTypedEvent = (!isProcessInputMethodEventOverridden());
/*      */     }
/*      */ 
/* 4951 */     return this.needToSendKeyTypedEvent;
/*      */   }
/*      */ 
/*      */   private boolean isProcessInputMethodEventOverridden()
/*      */   {
/* 4960 */     if (overrideMap == null) {
/* 4961 */       overrideMap = Collections.synchronizedMap(new HashMap());
/*      */     }
/* 4963 */     Boolean localBoolean1 = (Boolean)overrideMap.get(getClass().getName());
/*      */ 
/* 4965 */     if (localBoolean1 != null) {
/* 4966 */       return localBoolean1.booleanValue();
/*      */     }
/* 4968 */     Boolean localBoolean2 = (Boolean)AccessController.doPrivileged(new PrivilegedAction()
/*      */     {
/*      */       public Boolean run() {
/* 4971 */         return JTextComponent.isProcessInputMethodEventOverridden(JTextComponent.this.getClass());
/*      */       }
/*      */     });
/* 4976 */     return localBoolean2.booleanValue();
/*      */   }
/*      */ 
/*      */   boolean composedTextExists()
/*      */   {
/* 4983 */     return this.composedTextStart != null;
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*  763 */     SwingAccessor.setJTextComponentAccessor(new SwingAccessor.JTextComponentAccessor()
/*      */     {
/*      */       public TransferHandler.DropLocation dropLocationForPoint(JTextComponent paramAnonymousJTextComponent, Point paramAnonymousPoint)
/*      */       {
/*  768 */         return paramAnonymousJTextComponent.dropLocationForPoint(paramAnonymousPoint);
/*      */       }
/*      */ 
/*      */       public Object setDropLocation(JTextComponent paramAnonymousJTextComponent, TransferHandler.DropLocation paramAnonymousDropLocation, Object paramAnonymousObject, boolean paramAnonymousBoolean)
/*      */       {
/*  774 */         return paramAnonymousJTextComponent.setDropLocation(paramAnonymousDropLocation, paramAnonymousObject, paramAnonymousBoolean);
/*      */       }
/*      */     });
/*      */   }
/*      */ 
/*      */   public class AccessibleJTextComponent extends JComponent.AccessibleJComponent
/*      */     implements AccessibleText, CaretListener, DocumentListener, AccessibleAction, AccessibleEditableText, AccessibleExtendedText
/*      */   {
/*      */     int caretPos;
/*      */     Point oldLocationOnScreen;
/*      */ 
/*      */     public AccessibleJTextComponent()
/*      */     {
/* 2556 */       super();
/* 2557 */       Document localDocument = JTextComponent.this.getDocument();
/* 2558 */       if (localDocument != null) {
/* 2559 */         localDocument.addDocumentListener(this);
/*      */       }
/* 2561 */       JTextComponent.this.addCaretListener(this);
/* 2562 */       this.caretPos = getCaretPosition();
/*      */       try
/*      */       {
/* 2565 */         this.oldLocationOnScreen = getLocationOnScreen();
/*      */       }
/*      */       catch (IllegalComponentStateException localIllegalComponentStateException)
/*      */       {
/*      */       }
/*      */ 
/* 2573 */       JTextComponent.this.addComponentListener(new ComponentAdapter()
/*      */       {
/*      */         public void componentMoved(ComponentEvent paramAnonymousComponentEvent) {
/*      */           try {
/* 2577 */             Point localPoint = JTextComponent.AccessibleJTextComponent.this.getLocationOnScreen();
/* 2578 */             JTextComponent.AccessibleJTextComponent.this.firePropertyChange("AccessibleVisibleData", JTextComponent.AccessibleJTextComponent.this.oldLocationOnScreen, localPoint);
/*      */ 
/* 2582 */             JTextComponent.AccessibleJTextComponent.this.oldLocationOnScreen = localPoint;
/*      */           }
/*      */           catch (IllegalComponentStateException localIllegalComponentStateException)
/*      */           {
/*      */           }
/*      */         }
/*      */       });
/*      */     }
/*      */ 
/*      */     public void caretUpdate(CaretEvent paramCaretEvent)
/*      */     {
/* 2599 */       int i = paramCaretEvent.getDot();
/* 2600 */       int j = paramCaretEvent.getMark();
/* 2601 */       if (this.caretPos != i)
/*      */       {
/* 2603 */         firePropertyChange("AccessibleCaret", new Integer(this.caretPos), new Integer(i));
/*      */ 
/* 2605 */         this.caretPos = i;
/*      */         try
/*      */         {
/* 2608 */           this.oldLocationOnScreen = getLocationOnScreen();
/*      */         } catch (IllegalComponentStateException localIllegalComponentStateException) {
/*      */         }
/*      */       }
/* 2612 */       if (j != i)
/*      */       {
/* 2614 */         firePropertyChange("AccessibleSelection", null, getSelectedText());
/*      */       }
/*      */     }
/*      */ 
/*      */     public void insertUpdate(DocumentEvent paramDocumentEvent)
/*      */     {
/* 2629 */       final Integer localInteger = new Integer(paramDocumentEvent.getOffset());
/* 2630 */       if (SwingUtilities.isEventDispatchThread()) {
/* 2631 */         firePropertyChange("AccessibleText", null, localInteger);
/*      */       } else {
/* 2633 */         Runnable local2 = new Runnable() {
/*      */           public void run() {
/* 2635 */             JTextComponent.AccessibleJTextComponent.this.firePropertyChange("AccessibleText", null, localInteger);
/*      */           }
/*      */         };
/* 2639 */         SwingUtilities.invokeLater(local2);
/*      */       }
/*      */     }
/*      */ 
/*      */     public void removeUpdate(DocumentEvent paramDocumentEvent)
/*      */     {
/* 2651 */       final Integer localInteger = new Integer(paramDocumentEvent.getOffset());
/* 2652 */       if (SwingUtilities.isEventDispatchThread()) {
/* 2653 */         firePropertyChange("AccessibleText", null, localInteger);
/*      */       } else {
/* 2655 */         Runnable local3 = new Runnable() {
/*      */           public void run() {
/* 2657 */             JTextComponent.AccessibleJTextComponent.this.firePropertyChange("AccessibleText", null, localInteger);
/*      */           }
/*      */         };
/* 2661 */         SwingUtilities.invokeLater(local3);
/*      */       }
/*      */     }
/*      */ 
/*      */     public void changedUpdate(DocumentEvent paramDocumentEvent)
/*      */     {
/* 2673 */       final Integer localInteger = new Integer(paramDocumentEvent.getOffset());
/* 2674 */       if (SwingUtilities.isEventDispatchThread()) {
/* 2675 */         firePropertyChange("AccessibleText", null, localInteger);
/*      */       } else {
/* 2677 */         Runnable local4 = new Runnable() {
/*      */           public void run() {
/* 2679 */             JTextComponent.AccessibleJTextComponent.this.firePropertyChange("AccessibleText", null, localInteger);
/*      */           }
/*      */         };
/* 2683 */         SwingUtilities.invokeLater(local4);
/*      */       }
/*      */     }
/*      */ 
/*      */     public AccessibleStateSet getAccessibleStateSet()
/*      */     {
/* 2701 */       AccessibleStateSet localAccessibleStateSet = super.getAccessibleStateSet();
/* 2702 */       if (JTextComponent.this.isEditable()) {
/* 2703 */         localAccessibleStateSet.add(AccessibleState.EDITABLE);
/*      */       }
/* 2705 */       return localAccessibleStateSet;
/*      */     }
/*      */ 
/*      */     public AccessibleRole getAccessibleRole()
/*      */     {
/* 2717 */       return AccessibleRole.TEXT;
/*      */     }
/*      */ 
/*      */     public AccessibleText getAccessibleText()
/*      */     {
/* 2729 */       return this;
/*      */     }
/*      */ 
/*      */     public int getIndexAtPoint(Point paramPoint)
/*      */     {
/* 2749 */       if (paramPoint == null) {
/* 2750 */         return -1;
/*      */       }
/* 2752 */       return JTextComponent.this.viewToModel(paramPoint);
/*      */     }
/*      */ 
/*      */     Rectangle getRootEditorRect()
/*      */     {
/* 2763 */       Rectangle localRectangle = JTextComponent.this.getBounds();
/* 2764 */       if ((localRectangle.width > 0) && (localRectangle.height > 0)) {
/* 2765 */         localRectangle.x = (localRectangle.y = 0);
/* 2766 */         Insets localInsets = JTextComponent.this.getInsets();
/* 2767 */         localRectangle.x += localInsets.left;
/* 2768 */         localRectangle.y += localInsets.top;
/* 2769 */         localRectangle.width -= localInsets.left + localInsets.right;
/* 2770 */         localRectangle.height -= localInsets.top + localInsets.bottom;
/* 2771 */         return localRectangle;
/*      */       }
/* 2773 */       return null;
/*      */     }
/*      */ 
/*      */     public Rectangle getCharacterBounds(int paramInt)
/*      */     {
/* 2805 */       if ((paramInt < 0) || (paramInt > JTextComponent.this.model.getLength() - 1)) {
/* 2806 */         return null;
/*      */       }
/* 2808 */       TextUI localTextUI = JTextComponent.this.getUI();
/* 2809 */       if (localTextUI == null) {
/* 2810 */         return null;
/*      */       }
/* 2812 */       Rectangle localRectangle1 = null;
/* 2813 */       Rectangle localRectangle2 = getRootEditorRect();
/* 2814 */       if (localRectangle2 == null) {
/* 2815 */         return null;
/*      */       }
/* 2817 */       if ((JTextComponent.this.model instanceof AbstractDocument))
/* 2818 */         ((AbstractDocument)JTextComponent.this.model).readLock();
/*      */       try
/*      */       {
/* 2821 */         View localView = localTextUI.getRootView(JTextComponent.this);
/* 2822 */         if (localView != null) {
/* 2823 */           localView.setSize(localRectangle2.width, localRectangle2.height);
/*      */ 
/* 2825 */           Shape localShape = localView.modelToView(paramInt, Position.Bias.Forward, paramInt + 1, Position.Bias.Backward, localRectangle2);
/*      */ 
/* 2829 */           localRectangle1 = (localShape instanceof Rectangle) ? (Rectangle)localShape : localShape.getBounds();
/*      */         }
/*      */       }
/*      */       catch (BadLocationException localBadLocationException) {
/*      */       }
/*      */       finally {
/* 2835 */         if ((JTextComponent.this.model instanceof AbstractDocument)) {
/* 2836 */           ((AbstractDocument)JTextComponent.this.model).readUnlock();
/*      */         }
/*      */       }
/* 2839 */       return localRectangle1;
/*      */     }
/*      */ 
/*      */     public int getCharCount()
/*      */     {
/* 2848 */       return JTextComponent.this.model.getLength();
/*      */     }
/*      */ 
/*      */     public int getCaretPosition()
/*      */     {
/* 2861 */       return JTextComponent.this.getCaretPosition();
/*      */     }
/*      */ 
/*      */     public AttributeSet getCharacterAttribute(int paramInt)
/*      */     {
/* 2871 */       Element localElement = null;
/* 2872 */       if ((JTextComponent.this.model instanceof AbstractDocument))
/* 2873 */         ((AbstractDocument)JTextComponent.this.model).readLock();
/*      */       try
/*      */       {
/* 2876 */         for (localElement = JTextComponent.this.model.getDefaultRootElement(); !localElement.isLeaf(); ) {
/* 2877 */           int i = localElement.getElementIndex(paramInt);
/* 2878 */           localElement = localElement.getElement(i);
/*      */         }
/*      */       } finally {
/* 2881 */         if ((JTextComponent.this.model instanceof AbstractDocument)) {
/* 2882 */           ((AbstractDocument)JTextComponent.this.model).readUnlock();
/*      */         }
/*      */       }
/* 2885 */       return localElement.getAttributes();
/*      */     }
/*      */ 
/*      */     public int getSelectionStart()
/*      */     {
/* 2899 */       return JTextComponent.this.getSelectionStart();
/*      */     }
/*      */ 
/*      */     public int getSelectionEnd()
/*      */     {
/* 2912 */       return JTextComponent.this.getSelectionEnd();
/*      */     }
/*      */ 
/*      */     public String getSelectedText()
/*      */     {
/* 2921 */       return JTextComponent.this.getSelectedText();
/*      */     }
/*      */ 
/*      */     public String getAtIndex(int paramInt1, int paramInt2)
/*      */     {
/* 2947 */       return getAtIndex(paramInt1, paramInt2, 0);
/*      */     }
/*      */ 
/*      */     public String getAfterIndex(int paramInt1, int paramInt2)
/*      */     {
/* 2960 */       return getAtIndex(paramInt1, paramInt2, 1);
/*      */     }
/*      */ 
/*      */     public String getBeforeIndex(int paramInt1, int paramInt2)
/*      */     {
/* 2973 */       return getAtIndex(paramInt1, paramInt2, -1);
/*      */     }
/*      */ 
/*      */     private String getAtIndex(int paramInt1, int paramInt2, int paramInt3)
/*      */     {
/* 2983 */       if ((JTextComponent.this.model instanceof AbstractDocument))
/* 2984 */         ((AbstractDocument)JTextComponent.this.model).readLock();
/*      */       try
/*      */       {
/*      */         Object localObject1;
/* 2987 */         if ((paramInt2 < 0) || (paramInt2 >= JTextComponent.this.model.getLength())) {
/* 2988 */           return null;
/*      */         }
/* 2990 */         switch (paramInt1) {
/*      */         case 1:
/* 2992 */           if ((paramInt2 + paramInt3 < JTextComponent.this.model.getLength()) && (paramInt2 + paramInt3 >= 0))
/*      */           {
/* 2994 */             return JTextComponent.this.model.getText(paramInt2 + paramInt3, 1);
/*      */           }
/*      */ 
/*      */           break;
/*      */         case 2:
/*      */         case 3:
/* 3001 */           localObject1 = getSegmentAt(paramInt1, paramInt2);
/* 3002 */           if (localObject1 != null) {
/* 3003 */             if (paramInt3 != 0)
/*      */             {
/*      */               int i;
/* 3007 */               if (paramInt3 < 0) {
/* 3008 */                 i = ((IndexedSegment)localObject1).modelOffset - 1;
/*      */               }
/*      */               else {
/* 3011 */                 i = ((IndexedSegment)localObject1).modelOffset + paramInt3 * ((IndexedSegment)localObject1).count;
/*      */               }
/* 3013 */               if ((i >= 0) && (i <= JTextComponent.this.model.getLength())) {
/* 3014 */                 localObject1 = getSegmentAt(paramInt1, i);
/*      */               }
/*      */               else {
/* 3017 */                 localObject1 = null;
/*      */               }
/*      */             }
/* 3020 */             if (localObject1 != null) {
/* 3021 */               return new String(((IndexedSegment)localObject1).array, ((IndexedSegment)localObject1).offset, ((IndexedSegment)localObject1).count);
/*      */             }
/*      */           }
/*      */           break;
/*      */         }
/*      */ 
/*      */       }
/*      */       catch (BadLocationException localBadLocationException)
/*      */       {
/*      */       }
/*      */       finally
/*      */       {
/* 3033 */         if ((JTextComponent.this.model instanceof AbstractDocument)) {
/* 3034 */           ((AbstractDocument)JTextComponent.this.model).readUnlock();
/*      */         }
/*      */       }
/* 3037 */       return null;
/*      */     }
/*      */ 
/*      */     private Element getParagraphElement(int paramInt)
/*      */     {
/* 3045 */       if ((JTextComponent.this.model instanceof PlainDocument)) {
/* 3046 */         localObject = (PlainDocument)JTextComponent.this.model;
/* 3047 */         return ((PlainDocument)localObject).getParagraphElement(paramInt);
/* 3048 */       }if ((JTextComponent.this.model instanceof StyledDocument)) {
/* 3049 */         localObject = (StyledDocument)JTextComponent.this.model;
/* 3050 */         return ((StyledDocument)localObject).getParagraphElement(paramInt);
/*      */       }
/*      */ 
/* 3053 */       for (Object localObject = JTextComponent.this.model.getDefaultRootElement(); !((Element)localObject).isLeaf(); ) {
/* 3054 */         int i = ((Element)localObject).getElementIndex(paramInt);
/* 3055 */         localObject = ((Element)localObject).getElement(i);
/*      */       }
/* 3057 */       if (localObject == null) {
/* 3058 */         return null;
/*      */       }
/* 3060 */       return ((Element)localObject).getParentElement();
/*      */     }
/*      */ 
/*      */     private IndexedSegment getParagraphElementText(int paramInt)
/*      */       throws BadLocationException
/*      */     {
/* 3071 */       Element localElement = getParagraphElement(paramInt);
/*      */ 
/* 3074 */       if (localElement != null) {
/* 3075 */         IndexedSegment localIndexedSegment = new IndexedSegment(null);
/*      */         try {
/* 3077 */           int i = localElement.getEndOffset() - localElement.getStartOffset();
/* 3078 */           JTextComponent.this.model.getText(localElement.getStartOffset(), i, localIndexedSegment);
/*      */         } catch (BadLocationException localBadLocationException) {
/* 3080 */           return null;
/*      */         }
/* 3082 */         localIndexedSegment.modelOffset = localElement.getStartOffset();
/* 3083 */         return localIndexedSegment;
/*      */       }
/* 3085 */       return null;
/*      */     }
/*      */ 
/*      */     private IndexedSegment getSegmentAt(int paramInt1, int paramInt2)
/*      */       throws BadLocationException
/*      */     {
/* 3099 */       IndexedSegment localIndexedSegment = getParagraphElementText(paramInt2);
/* 3100 */       if (localIndexedSegment == null)
/* 3101 */         return null;
/*      */       BreakIterator localBreakIterator;
/* 3104 */       switch (paramInt1) {
/*      */       case 2:
/* 3106 */         localBreakIterator = BreakIterator.getWordInstance(getLocale());
/* 3107 */         break;
/*      */       case 3:
/* 3109 */         localBreakIterator = BreakIterator.getSentenceInstance(getLocale());
/* 3110 */         break;
/*      */       default:
/* 3112 */         return null;
/*      */       }
/* 3114 */       localIndexedSegment.first();
/* 3115 */       localBreakIterator.setText(localIndexedSegment);
/* 3116 */       int i = localBreakIterator.following(paramInt2 - localIndexedSegment.modelOffset + localIndexedSegment.offset);
/* 3117 */       if (i == -1) {
/* 3118 */         return null;
/*      */       }
/* 3120 */       if (i > localIndexedSegment.offset + localIndexedSegment.count) {
/* 3121 */         return null;
/*      */       }
/* 3123 */       int j = localBreakIterator.previous();
/* 3124 */       if ((j == -1) || (j >= localIndexedSegment.offset + localIndexedSegment.count))
/*      */       {
/* 3126 */         return null;
/*      */       }
/* 3128 */       localIndexedSegment.modelOffset = (localIndexedSegment.modelOffset + j - localIndexedSegment.offset);
/* 3129 */       localIndexedSegment.offset = j;
/* 3130 */       localIndexedSegment.count = (i - j);
/* 3131 */       return localIndexedSegment;
/*      */     }
/*      */ 
/*      */     public AccessibleEditableText getAccessibleEditableText()
/*      */     {
/* 3144 */       return this;
/*      */     }
/*      */ 
/*      */     public void setTextContents(String paramString)
/*      */     {
/* 3154 */       JTextComponent.this.setText(paramString);
/*      */     }
/*      */ 
/*      */     public void insertTextAtIndex(int paramInt, String paramString)
/*      */     {
/* 3166 */       Document localDocument = JTextComponent.this.getDocument();
/* 3167 */       if (localDocument != null)
/*      */         try {
/* 3169 */           if ((paramString != null) && (paramString.length() > 0)) {
/* 3170 */             boolean bool = JTextComponent.this.saveComposedText(paramInt);
/* 3171 */             localDocument.insertString(paramInt, paramString, null);
/* 3172 */             if (bool)
/* 3173 */               JTextComponent.this.restoreComposedText();
/*      */           }
/*      */         }
/*      */         catch (BadLocationException localBadLocationException) {
/* 3177 */           UIManager.getLookAndFeel().provideErrorFeedback(JTextComponent.this);
/*      */         }
/*      */     }
/*      */ 
/*      */     public String getTextRange(int paramInt1, int paramInt2)
/*      */     {
/* 3191 */       String str = null;
/* 3192 */       int i = Math.min(paramInt1, paramInt2);
/* 3193 */       int j = Math.max(paramInt1, paramInt2);
/* 3194 */       if (i != j) {
/*      */         try {
/* 3196 */           Document localDocument = JTextComponent.this.getDocument();
/* 3197 */           str = localDocument.getText(i, j - i);
/*      */         } catch (BadLocationException localBadLocationException) {
/* 3199 */           throw new IllegalArgumentException(localBadLocationException.getMessage());
/*      */         }
/*      */       }
/* 3202 */       return str;
/*      */     }
/*      */ 
/*      */     public void delete(int paramInt1, int paramInt2)
/*      */     {
/* 3213 */       if ((JTextComponent.this.isEditable()) && (isEnabled()))
/*      */         try {
/* 3215 */           int i = Math.min(paramInt1, paramInt2);
/* 3216 */           int j = Math.max(paramInt1, paramInt2);
/* 3217 */           if (i != j) {
/* 3218 */             Document localDocument = JTextComponent.this.getDocument();
/* 3219 */             localDocument.remove(i, j - i);
/*      */           }
/*      */         }
/*      */         catch (BadLocationException localBadLocationException) {
/*      */         }
/* 3224 */       else UIManager.getLookAndFeel().provideErrorFeedback(JTextComponent.this);
/*      */     }
/*      */ 
/*      */     public void cut(int paramInt1, int paramInt2)
/*      */     {
/* 3236 */       selectText(paramInt1, paramInt2);
/* 3237 */       JTextComponent.this.cut();
/*      */     }
/*      */ 
/*      */     public void paste(int paramInt)
/*      */     {
/* 3248 */       JTextComponent.this.setCaretPosition(paramInt);
/* 3249 */       JTextComponent.this.paste();
/*      */     }
/*      */ 
/*      */     public void replaceText(int paramInt1, int paramInt2, String paramString)
/*      */     {
/* 3262 */       selectText(paramInt1, paramInt2);
/* 3263 */       JTextComponent.this.replaceSelection(paramString);
/*      */     }
/*      */ 
/*      */     public void selectText(int paramInt1, int paramInt2)
/*      */     {
/* 3274 */       JTextComponent.this.select(paramInt1, paramInt2);
/*      */     }
/*      */ 
/*      */     public void setAttributes(int paramInt1, int paramInt2, AttributeSet paramAttributeSet)
/*      */     {
/* 3290 */       Document localDocument = JTextComponent.this.getDocument();
/* 3291 */       if ((localDocument != null) && ((localDocument instanceof StyledDocument))) {
/* 3292 */         StyledDocument localStyledDocument = (StyledDocument)localDocument;
/* 3293 */         int i = paramInt1;
/* 3294 */         int j = paramInt2 - paramInt1;
/* 3295 */         localStyledDocument.setCharacterAttributes(i, j, paramAttributeSet, true);
/*      */       }
/*      */     }
/*      */ 
/*      */     private AccessibleTextSequence getSequenceAtIndex(int paramInt1, int paramInt2, int paramInt3)
/*      */     {
/* 3333 */       if ((paramInt2 < 0) || (paramInt2 >= JTextComponent.this.model.getLength())) {
/* 3334 */         return null;
/*      */       }
/* 3336 */       if ((paramInt3 < -1) || (paramInt3 > 1))
/* 3337 */         return null;
/*      */       int i;
/*      */       int k;
/* 3340 */       switch (paramInt1) {
/*      */       case 1:
/* 3342 */         if ((JTextComponent.this.model instanceof AbstractDocument)) {
/* 3343 */           ((AbstractDocument)JTextComponent.this.model).readLock();
/*      */         }
/* 3345 */         AccessibleTextSequence localAccessibleTextSequence1 = null;
/*      */         try {
/* 3347 */           if ((paramInt2 + paramInt3 < JTextComponent.this.model.getLength()) && (paramInt2 + paramInt3 >= 0))
/*      */           {
/* 3349 */             localAccessibleTextSequence1 = new AccessibleTextSequence(paramInt2 + paramInt3, paramInt2 + paramInt3 + 1, JTextComponent.this.model.getText(paramInt2 + paramInt3, 1));
/*      */           }
/*      */ 
/*      */         }
/*      */         catch (BadLocationException localBadLocationException1)
/*      */         {
/*      */         }
/*      */         finally
/*      */         {
/* 3359 */           if ((JTextComponent.this.model instanceof AbstractDocument)) {
/* 3360 */             ((AbstractDocument)JTextComponent.this.model).readUnlock();
/*      */           }
/*      */         }
/* 3363 */         return localAccessibleTextSequence1;
/*      */       case 2:
/*      */       case 3:
/* 3367 */         if ((JTextComponent.this.model instanceof AbstractDocument)) {
/* 3368 */           ((AbstractDocument)JTextComponent.this.model).readLock();
/*      */         }
/* 3370 */         AccessibleTextSequence localAccessibleTextSequence2 = null;
/*      */         try {
/* 3372 */           IndexedSegment localIndexedSegment = getSegmentAt(paramInt1, paramInt2);
/* 3373 */           if (localIndexedSegment != null) {
/* 3374 */             if (paramInt3 != 0)
/*      */             {
/* 3377 */               if (paramInt3 < 0) {
/* 3378 */                 i = localIndexedSegment.modelOffset - 1;
/*      */               }
/*      */               else {
/* 3381 */                 i = localIndexedSegment.modelOffset + localIndexedSegment.count;
/*      */               }
/* 3383 */               if ((i >= 0) && (i <= JTextComponent.this.model.getLength())) {
/* 3384 */                 localIndexedSegment = getSegmentAt(paramInt1, i);
/*      */               }
/*      */               else {
/* 3387 */                 localIndexedSegment = null;
/*      */               }
/*      */             }
/* 3390 */             if ((localIndexedSegment != null) && (localIndexedSegment.offset + localIndexedSegment.count <= JTextComponent.this.model.getLength()))
/*      */             {
/* 3392 */               localAccessibleTextSequence2 = new AccessibleTextSequence(localIndexedSegment.offset, localIndexedSegment.offset + localIndexedSegment.count, new String(localIndexedSegment.array, localIndexedSegment.offset, localIndexedSegment.count));
/*      */             }
/*      */           }
/*      */ 
/*      */         }
/*      */         catch (BadLocationException localBadLocationException2)
/*      */         {
/*      */         }
/*      */         finally
/*      */         {
/* 3402 */           if ((JTextComponent.this.model instanceof AbstractDocument)) {
/* 3403 */             ((AbstractDocument)JTextComponent.this.model).readUnlock();
/*      */           }
/*      */         }
/* 3406 */         return localAccessibleTextSequence2;
/*      */       case 4:
/* 3409 */         AccessibleTextSequence localAccessibleTextSequence3 = null;
/* 3410 */         if ((JTextComponent.this.model instanceof AbstractDocument))
/* 3411 */           ((AbstractDocument)JTextComponent.this.model).readLock();
/*      */         try
/*      */         {
/* 3414 */           i = Utilities.getRowStart(JTextComponent.this, paramInt2);
/*      */ 
/* 3416 */           k = Utilities.getRowEnd(JTextComponent.this, paramInt2);
/*      */ 
/* 3418 */           if ((i >= 0) && (k >= i)) {
/* 3419 */             if (paramInt3 == 0) {
/* 3420 */               localAccessibleTextSequence3 = new AccessibleTextSequence(i, k, JTextComponent.this.model.getText(i, k - i + 1));
/*      */             }
/* 3424 */             else if ((paramInt3 == -1) && (i > 0)) {
/* 3425 */               k = Utilities.getRowEnd(JTextComponent.this, i - 1);
/*      */ 
/* 3428 */               i = Utilities.getRowStart(JTextComponent.this, i - 1);
/*      */ 
/* 3431 */               if ((i >= 0) && (k >= i)) {
/* 3432 */                 localAccessibleTextSequence3 = new AccessibleTextSequence(i, k, JTextComponent.this.model.getText(i, k - i + 1));
/*      */               }
/*      */ 
/*      */             }
/* 3438 */             else if ((paramInt3 == 1) && (k < JTextComponent.this.model.getLength()))
/*      */             {
/* 3440 */               i = Utilities.getRowStart(JTextComponent.this, k + 1);
/*      */ 
/* 3443 */               k = Utilities.getRowEnd(JTextComponent.this, k + 1);
/*      */ 
/* 3446 */               if ((i >= 0) && (k >= i)) {
/* 3447 */                 localAccessibleTextSequence3 = new AccessibleTextSequence(i, k, JTextComponent.this.model.getText(i, k - i + 1));
/*      */               }
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/*      */         }
/*      */         catch (BadLocationException localBadLocationException3)
/*      */         {
/*      */         }
/*      */         finally
/*      */         {
/* 3459 */           if ((JTextComponent.this.model instanceof AbstractDocument)) {
/* 3460 */             ((AbstractDocument)JTextComponent.this.model).readUnlock();
/*      */           }
/*      */         }
/* 3463 */         return localAccessibleTextSequence3;
/*      */       case 5:
/* 3471 */         String str = null;
/* 3472 */         if ((JTextComponent.this.model instanceof AbstractDocument))
/* 3473 */           ((AbstractDocument)JTextComponent.this.model).readLock();
/*      */         int j;
/*      */         try
/*      */         {
/* 3477 */           j = k = -2147483648;
/*      */ 
/* 3479 */           int m = paramInt2;
/* 3480 */           switch (paramInt3)
/*      */           {
/*      */           case -1:
/* 3485 */             k = getRunEdge(paramInt2, paramInt3);
/*      */ 
/* 3488 */             m = k - 1;
/* 3489 */             break;
/*      */           case 1:
/* 3494 */             j = getRunEdge(paramInt2, paramInt3);
/*      */ 
/* 3497 */             m = j;
/* 3498 */             break;
/*      */           case 0:
/* 3502 */             break;
/*      */           default:
/* 3505 */             throw new AssertionError(paramInt3);
/*      */           }
/*      */ 
/* 3510 */           j = j != -2147483648 ? j : getRunEdge(m, -1);
/*      */ 
/* 3513 */           k = k != -2147483648 ? k : getRunEdge(m, 1);
/*      */ 
/* 3517 */           str = JTextComponent.this.model.getText(j, k - j);
/*      */         }
/*      */         catch (BadLocationException localBadLocationException4)
/*      */         {
/* 3523 */           return null;
/*      */         } finally {
/* 3525 */           if ((JTextComponent.this.model instanceof AbstractDocument)) {
/* 3526 */             ((AbstractDocument)JTextComponent.this.model).readUnlock();
/*      */           }
/*      */         }
/* 3529 */         return new AccessibleTextSequence(j, k, str);
/*      */       }
/*      */ 
/* 3536 */       return null;
/*      */     }
/*      */ 
/*      */     private int getRunEdge(int paramInt1, int paramInt2)
/*      */       throws BadLocationException
/*      */     {
/* 3550 */       if ((paramInt1 < 0) || (paramInt1 >= JTextComponent.this.model.getLength())) {
/* 3551 */         throw new BadLocationException("Location out of bounds", paramInt1);
/*      */       }
/*      */ 
/* 3556 */       int i = -1;
/* 3557 */       Element localElement1 = JTextComponent.this.model.getDefaultRootElement();
/* 3558 */       while (!localElement1.isLeaf()) {
/* 3559 */         i = localElement1.getElementIndex(paramInt1);
/* 3560 */         localElement1 = localElement1.getElement(i);
/*      */       }
/* 3562 */       if (i == -1) {
/* 3563 */         throw new AssertionError(paramInt1);
/*      */       }
/*      */ 
/* 3566 */       AttributeSet localAttributeSet = localElement1.getAttributes();
/* 3567 */       Element localElement2 = localElement1.getParentElement();
/*      */       Element localElement3;
/* 3573 */       switch (paramInt2) {
/*      */       case -1:
/*      */       case 1:
/* 3576 */         int j = i;
/* 3577 */         int k = localElement2.getElementCount();
/*      */ 
/* 3579 */         while ((j + paramInt2 > 0) && (j + paramInt2 < k) && (localElement2.getElement(j + paramInt2).getAttributes().isEqual(localAttributeSet)))
/*      */         {
/* 3582 */           j += paramInt2;
/*      */         }
/* 3584 */         localElement3 = localElement2.getElement(j);
/* 3585 */         break;
/*      */       default:
/* 3587 */         throw new AssertionError(paramInt2);
/*      */       }
/* 3589 */       switch (paramInt2) {
/*      */       case -1:
/* 3591 */         return localElement3.getStartOffset();
/*      */       case 1:
/* 3593 */         return localElement3.getEndOffset();
/*      */       }
/*      */ 
/* 3597 */       return -2147483648;
/*      */     }
/*      */ 
/*      */     public AccessibleTextSequence getTextSequenceAt(int paramInt1, int paramInt2)
/*      */     {
/* 3624 */       return getSequenceAtIndex(paramInt1, paramInt2, 0);
/*      */     }
/*      */ 
/*      */     public AccessibleTextSequence getTextSequenceAfter(int paramInt1, int paramInt2)
/*      */     {
/* 3648 */       return getSequenceAtIndex(paramInt1, paramInt2, 1);
/*      */     }
/*      */ 
/*      */     public AccessibleTextSequence getTextSequenceBefore(int paramInt1, int paramInt2)
/*      */     {
/* 3672 */       return getSequenceAtIndex(paramInt1, paramInt2, -1);
/*      */     }
/*      */ 
/*      */     public Rectangle getTextBounds(int paramInt1, int paramInt2)
/*      */     {
/* 3687 */       if ((paramInt1 < 0) || (paramInt1 > JTextComponent.this.model.getLength() - 1) || (paramInt2 < 0) || (paramInt2 > JTextComponent.this.model.getLength() - 1) || (paramInt1 > paramInt2))
/*      */       {
/* 3690 */         return null;
/*      */       }
/* 3692 */       TextUI localTextUI = JTextComponent.this.getUI();
/* 3693 */       if (localTextUI == null) {
/* 3694 */         return null;
/*      */       }
/* 3696 */       Rectangle localRectangle1 = null;
/* 3697 */       Rectangle localRectangle2 = getRootEditorRect();
/* 3698 */       if (localRectangle2 == null) {
/* 3699 */         return null;
/*      */       }
/* 3701 */       if ((JTextComponent.this.model instanceof AbstractDocument))
/* 3702 */         ((AbstractDocument)JTextComponent.this.model).readLock();
/*      */       try
/*      */       {
/* 3705 */         View localView = localTextUI.getRootView(JTextComponent.this);
/* 3706 */         if (localView != null) {
/* 3707 */           Shape localShape = localView.modelToView(paramInt1, Position.Bias.Forward, paramInt2, Position.Bias.Backward, localRectangle2);
/*      */ 
/* 3711 */           localRectangle1 = (localShape instanceof Rectangle) ? (Rectangle)localShape : localShape.getBounds();
/*      */         }
/*      */       }
/*      */       catch (BadLocationException localBadLocationException) {
/*      */       }
/*      */       finally {
/* 3717 */         if ((JTextComponent.this.model instanceof AbstractDocument)) {
/* 3718 */           ((AbstractDocument)JTextComponent.this.model).readUnlock();
/*      */         }
/*      */       }
/* 3721 */       return localRectangle1;
/*      */     }
/*      */ 
/*      */     public AccessibleAction getAccessibleAction()
/*      */     {
/* 3730 */       return this;
/*      */     }
/*      */ 
/*      */     public int getAccessibleActionCount()
/*      */     {
/* 3742 */       Action[] arrayOfAction = JTextComponent.this.getActions();
/* 3743 */       return arrayOfAction.length;
/*      */     }
/*      */ 
/*      */     public String getAccessibleActionDescription(int paramInt)
/*      */     {
/* 3755 */       Action[] arrayOfAction = JTextComponent.this.getActions();
/* 3756 */       if ((paramInt < 0) || (paramInt >= arrayOfAction.length)) {
/* 3757 */         return null;
/*      */       }
/* 3759 */       return (String)arrayOfAction[paramInt].getValue("Name");
/*      */     }
/*      */ 
/*      */     public boolean doAccessibleAction(int paramInt)
/*      */     {
/* 3771 */       Action[] arrayOfAction = JTextComponent.this.getActions();
/* 3772 */       if ((paramInt < 0) || (paramInt >= arrayOfAction.length)) {
/* 3773 */         return false;
/*      */       }
/* 3775 */       ActionEvent localActionEvent = new ActionEvent(JTextComponent.this, 1001, null, EventQueue.getMostRecentEventTime(), JTextComponent.this.getCurrentEventModifiers());
/*      */ 
/* 3780 */       arrayOfAction[paramInt].actionPerformed(localActionEvent);
/* 3781 */       return true;
/*      */     }
/*      */ 
/*      */     private class IndexedSegment extends Segment
/*      */     {
/*      */       public int modelOffset;
/*      */ 
/*      */       private IndexedSegment()
/*      */       {
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   class ComposedTextCaret extends DefaultCaret
/*      */     implements Serializable
/*      */   {
/*      */     Color bg;
/*      */ 
/*      */     ComposedTextCaret()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void install(JTextComponent paramJTextComponent)
/*      */     {
/* 4996 */       super.install(paramJTextComponent);
/*      */ 
/* 4998 */       Document localDocument = paramJTextComponent.getDocument();
/* 4999 */       if ((localDocument instanceof StyledDocument)) {
/* 5000 */         StyledDocument localStyledDocument = (StyledDocument)localDocument;
/* 5001 */         Element localElement = localStyledDocument.getCharacterElement(paramJTextComponent.composedTextStart.getOffset());
/* 5002 */         AttributeSet localAttributeSet = localElement.getAttributes();
/* 5003 */         this.bg = localStyledDocument.getBackground(localAttributeSet);
/*      */       }
/*      */ 
/* 5006 */       if (this.bg == null)
/* 5007 */         this.bg = paramJTextComponent.getBackground();
/*      */     }
/*      */ 
/*      */     public void paint(Graphics paramGraphics)
/*      */     {
/* 5015 */       if (isVisible())
/*      */         try {
/* 5017 */           Rectangle localRectangle = this.component.modelToView(getDot());
/* 5018 */           paramGraphics.setXORMode(this.bg);
/* 5019 */           paramGraphics.drawLine(localRectangle.x, localRectangle.y, localRectangle.x, localRectangle.y + localRectangle.height - 1);
/* 5020 */           paramGraphics.setPaintMode();
/*      */         }
/*      */         catch (BadLocationException localBadLocationException)
/*      */         {
/*      */         }
/*      */     }
/*      */ 
/*      */     protected void positionCaret(MouseEvent paramMouseEvent)
/*      */     {
/* 5033 */       JTextComponent localJTextComponent = this.component;
/* 5034 */       Point localPoint = new Point(paramMouseEvent.getX(), paramMouseEvent.getY());
/* 5035 */       int i = localJTextComponent.viewToModel(localPoint);
/* 5036 */       int j = localJTextComponent.composedTextStart.getOffset();
/* 5037 */       if ((i < j) || (i > JTextComponent.this.composedTextEnd.getOffset()))
/*      */       {
/*      */         try
/*      */         {
/* 5041 */           Position localPosition = localJTextComponent.getDocument().createPosition(i);
/* 5042 */           localJTextComponent.getInputContext().endComposition();
/*      */ 
/* 5046 */           EventQueue.invokeLater(new JTextComponent.DoSetCaretPosition(JTextComponent.this, localJTextComponent, localPosition));
/*      */         } catch (BadLocationException localBadLocationException) {
/* 5048 */           System.err.println(localBadLocationException);
/*      */         }
/*      */       }
/*      */       else
/* 5052 */         super.positionCaret(paramMouseEvent);
/*      */     }
/*      */   }
/*      */ 
/*      */   static class DefaultKeymap
/*      */     implements Keymap
/*      */   {
/*      */     String nm;
/*      */     Keymap parent;
/*      */     Hashtable<KeyStroke, Action> bindings;
/*      */     Action defaultAction;
/*      */ 
/*      */     DefaultKeymap(String paramString, Keymap paramKeymap)
/*      */     {
/* 4082 */       this.nm = paramString;
/* 4083 */       this.parent = paramKeymap;
/* 4084 */       this.bindings = new Hashtable();
/*      */     }
/*      */ 
/*      */     public Action getDefaultAction()
/*      */     {
/* 4096 */       if (this.defaultAction != null) {
/* 4097 */         return this.defaultAction;
/*      */       }
/* 4099 */       return this.parent != null ? this.parent.getDefaultAction() : null;
/*      */     }
/*      */ 
/*      */     public void setDefaultAction(Action paramAction)
/*      */     {
/* 4106 */       this.defaultAction = paramAction;
/*      */     }
/*      */ 
/*      */     public String getName() {
/* 4110 */       return this.nm;
/*      */     }
/*      */ 
/*      */     public Action getAction(KeyStroke paramKeyStroke) {
/* 4114 */       Action localAction = (Action)this.bindings.get(paramKeyStroke);
/* 4115 */       if ((localAction == null) && (this.parent != null)) {
/* 4116 */         localAction = this.parent.getAction(paramKeyStroke);
/*      */       }
/* 4118 */       return localAction;
/*      */     }
/*      */ 
/*      */     public KeyStroke[] getBoundKeyStrokes() {
/* 4122 */       KeyStroke[] arrayOfKeyStroke = new KeyStroke[this.bindings.size()];
/* 4123 */       int i = 0;
/* 4124 */       for (Enumeration localEnumeration = this.bindings.keys(); localEnumeration.hasMoreElements(); ) {
/* 4125 */         arrayOfKeyStroke[(i++)] = ((KeyStroke)localEnumeration.nextElement());
/*      */       }
/* 4127 */       return arrayOfKeyStroke;
/*      */     }
/*      */ 
/*      */     public Action[] getBoundActions() {
/* 4131 */       Action[] arrayOfAction = new Action[this.bindings.size()];
/* 4132 */       int i = 0;
/* 4133 */       for (Enumeration localEnumeration = this.bindings.elements(); localEnumeration.hasMoreElements(); ) {
/* 4134 */         arrayOfAction[(i++)] = ((Action)localEnumeration.nextElement());
/*      */       }
/* 4136 */       return arrayOfAction;
/*      */     }
/*      */ 
/*      */     public KeyStroke[] getKeyStrokesForAction(Action paramAction) {
/* 4140 */       if (paramAction == null) {
/* 4141 */         return null;
/*      */       }
/* 4143 */       Object localObject1 = null;
/*      */ 
/* 4145 */       Vector localVector = null;
/* 4146 */       for (Object localObject2 = this.bindings.keys(); ((Enumeration)localObject2).hasMoreElements(); ) {
/* 4147 */         KeyStroke localKeyStroke = (KeyStroke)((Enumeration)localObject2).nextElement();
/* 4148 */         if (this.bindings.get(localKeyStroke) == paramAction) {
/* 4149 */           if (localVector == null) {
/* 4150 */             localVector = new Vector();
/*      */           }
/* 4152 */           localVector.addElement(localKeyStroke);
/*      */         }
/*      */       }
/*      */ 
/* 4156 */       if (this.parent != null) {
/* 4157 */         localObject2 = this.parent.getKeyStrokesForAction(paramAction);
/* 4158 */         if (localObject2 != null)
/*      */         {
/* 4161 */           int i = 0;
/* 4162 */           for (int j = localObject2.length - 1; j >= 0; 
/* 4163 */             j--) {
/* 4164 */             if (isLocallyDefined(localObject2[j])) {
/* 4165 */               localObject2[j] = null;
/* 4166 */               i++;
/*      */             }
/*      */           }
/* 4169 */           if ((i > 0) && (i < localObject2.length)) {
/* 4170 */             if (localVector == null) {
/* 4171 */               localVector = new Vector();
/*      */             }
/* 4173 */             for (j = localObject2.length - 1; j >= 0; 
/* 4174 */               j--) {
/* 4175 */               if (localObject2[j] != null) {
/* 4176 */                 localVector.addElement(localObject2[j]);
/*      */               }
/*      */             }
/*      */           }
/* 4180 */           else if (i == 0) {
/* 4181 */             if (localVector == null) {
/* 4182 */               localObject1 = localObject2;
/*      */             }
/*      */             else {
/* 4185 */               localObject1 = new KeyStroke[localVector.size() + localObject2.length];
/*      */ 
/* 4187 */               localVector.copyInto((Object[])localObject1);
/* 4188 */               System.arraycopy(localObject2, 0, localObject1, localVector.size(), localObject2.length);
/*      */ 
/* 4190 */               localVector = null;
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/* 4195 */       if (localVector != null) {
/* 4196 */         localObject1 = new KeyStroke[localVector.size()];
/* 4197 */         localVector.copyInto((Object[])localObject1);
/*      */       }
/* 4199 */       return localObject1;
/*      */     }
/*      */ 
/*      */     public boolean isLocallyDefined(KeyStroke paramKeyStroke) {
/* 4203 */       return this.bindings.containsKey(paramKeyStroke);
/*      */     }
/*      */ 
/*      */     public void addActionForKeyStroke(KeyStroke paramKeyStroke, Action paramAction) {
/* 4207 */       this.bindings.put(paramKeyStroke, paramAction);
/*      */     }
/*      */ 
/*      */     public void removeKeyStrokeBinding(KeyStroke paramKeyStroke) {
/* 4211 */       this.bindings.remove(paramKeyStroke);
/*      */     }
/*      */ 
/*      */     public void removeBindings() {
/* 4215 */       this.bindings.clear();
/*      */     }
/*      */ 
/*      */     public Keymap getResolveParent() {
/* 4219 */       return this.parent;
/*      */     }
/*      */ 
/*      */     public void setResolveParent(Keymap paramKeymap) {
/* 4223 */       this.parent = paramKeymap;
/*      */     }
/*      */ 
/*      */     public String toString()
/*      */     {
/* 4231 */       return "Keymap[" + this.nm + "]" + this.bindings;
/*      */     }
/*      */   }
/*      */ 
/*      */   static class DefaultTransferHandler extends TransferHandler
/*      */     implements UIResource
/*      */   {
/*      */     public void exportToClipboard(JComponent paramJComponent, Clipboard paramClipboard, int paramInt)
/*      */       throws IllegalStateException
/*      */     {
/* 3970 */       if ((paramJComponent instanceof JTextComponent)) {
/* 3971 */         JTextComponent localJTextComponent = (JTextComponent)paramJComponent;
/* 3972 */         int i = localJTextComponent.getSelectionStart();
/* 3973 */         int j = localJTextComponent.getSelectionEnd();
/* 3974 */         if (i != j)
/*      */           try {
/* 3976 */             Document localDocument = localJTextComponent.getDocument();
/* 3977 */             String str = localDocument.getText(i, j - i);
/* 3978 */             StringSelection localStringSelection = new StringSelection(str);
/*      */ 
/* 3983 */             paramClipboard.setContents(localStringSelection, null);
/*      */ 
/* 3985 */             if (paramInt == 2)
/* 3986 */               localDocument.remove(i, j - i);
/*      */           } catch (BadLocationException localBadLocationException) {
/*      */           }
/*      */       }
/*      */     }
/*      */ 
/*      */     public boolean importData(JComponent paramJComponent, Transferable paramTransferable) {
/* 3993 */       if ((paramJComponent instanceof JTextComponent)) {
/* 3994 */         DataFlavor localDataFlavor = getFlavor(paramTransferable.getTransferDataFlavors());
/*      */ 
/* 3996 */         if (localDataFlavor != null) {
/* 3997 */           InputContext localInputContext = paramJComponent.getInputContext();
/* 3998 */           if (localInputContext != null)
/* 3999 */             localInputContext.endComposition();
/*      */           try
/*      */           {
/* 4002 */             String str = (String)paramTransferable.getTransferData(localDataFlavor);
/*      */ 
/* 4004 */             ((JTextComponent)paramJComponent).replaceSelection(str);
/* 4005 */             return true;
/*      */           } catch (UnsupportedFlavorException localUnsupportedFlavorException) {
/*      */           } catch (IOException localIOException) {
/*      */           }
/*      */         }
/*      */       }
/* 4011 */       return false;
/*      */     }
/*      */ 
/*      */     public boolean canImport(JComponent paramJComponent, DataFlavor[] paramArrayOfDataFlavor) {
/* 4015 */       JTextComponent localJTextComponent = (JTextComponent)paramJComponent;
/* 4016 */       if ((!localJTextComponent.isEditable()) || (!localJTextComponent.isEnabled())) {
/* 4017 */         return false;
/*      */       }
/* 4019 */       return getFlavor(paramArrayOfDataFlavor) != null;
/*      */     }
/*      */     public int getSourceActions(JComponent paramJComponent) {
/* 4022 */       return 0;
/*      */     }
/*      */     private DataFlavor getFlavor(DataFlavor[] paramArrayOfDataFlavor) {
/* 4025 */       if (paramArrayOfDataFlavor != null) {
/* 4026 */         for (DataFlavor localDataFlavor : paramArrayOfDataFlavor) {
/* 4027 */           if (localDataFlavor.equals(DataFlavor.stringFlavor)) {
/* 4028 */             return localDataFlavor;
/*      */           }
/*      */         }
/*      */       }
/* 4032 */       return null;
/*      */     }
/*      */   }
/*      */ 
/*      */   private class DoSetCaretPosition
/*      */     implements Runnable
/*      */   {
/*      */     JTextComponent host;
/*      */     Position newPos;
/*      */ 
/*      */     DoSetCaretPosition(JTextComponent paramPosition, Position arg3)
/*      */     {
/* 5065 */       this.host = paramPosition;
/*      */       Object localObject;
/* 5066 */       this.newPos = localObject;
/*      */     }
/*      */ 
/*      */     public void run() {
/* 5070 */       this.host.setCaretPosition(this.newPos.getOffset());
/*      */     }
/*      */   }
/*      */ 
/*      */   public static final class DropLocation extends TransferHandler.DropLocation
/*      */   {
/*      */     private final int index;
/*      */     private final Position.Bias bias;
/*      */ 
/*      */     private DropLocation(Point paramPoint, int paramInt, Position.Bias paramBias)
/*      */     {
/* 3872 */       super();
/* 3873 */       this.index = paramInt;
/* 3874 */       this.bias = paramBias;
/*      */     }
/*      */ 
/*      */     public int getIndex()
/*      */     {
/* 3885 */       return this.index;
/*      */     }
/*      */ 
/*      */     public Position.Bias getBias()
/*      */     {
/* 3894 */       return this.bias;
/*      */     }
/*      */ 
/*      */     public String toString()
/*      */     {
/* 3906 */       return getClass().getName() + "[dropPoint=" + getDropPoint() + "," + "index=" + this.index + "," + "bias=" + this.bias + "]";
/*      */     }
/*      */   }
/*      */ 
/*      */   class InputMethodRequestsHandler
/*      */     implements InputMethodRequests, DocumentListener
/*      */   {
/*      */     InputMethodRequestsHandler()
/*      */     {
/*      */     }
/*      */ 
/*      */     public AttributedCharacterIterator cancelLatestCommittedText(AttributedCharacterIterator.Attribute[] paramArrayOfAttribute)
/*      */     {
/* 4556 */       Document localDocument = JTextComponent.this.getDocument();
/* 4557 */       if ((localDocument != null) && (JTextComponent.this.latestCommittedTextStart != null) && (!JTextComponent.this.latestCommittedTextStart.equals(JTextComponent.this.latestCommittedTextEnd)))
/*      */         try
/*      */         {
/* 4560 */           int i = JTextComponent.this.latestCommittedTextStart.getOffset();
/* 4561 */           int j = JTextComponent.this.latestCommittedTextEnd.getOffset();
/* 4562 */           String str = localDocument.getText(i, j - i);
/*      */ 
/* 4564 */           localDocument.remove(i, j - i);
/* 4565 */           return new AttributedString(str).getIterator();
/*      */         } catch (BadLocationException localBadLocationException) {
/*      */         }
/* 4568 */       return null;
/*      */     }
/*      */ 
/*      */     public AttributedCharacterIterator getCommittedText(int paramInt1, int paramInt2, AttributedCharacterIterator.Attribute[] paramArrayOfAttribute)
/*      */     {
/* 4573 */       int i = 0;
/* 4574 */       int j = 0;
/* 4575 */       if (JTextComponent.this.composedTextExists()) {
/* 4576 */         i = JTextComponent.this.composedTextStart.getOffset();
/* 4577 */         j = JTextComponent.this.composedTextEnd.getOffset();
/*      */       }
/*      */       String str;
/*      */       try
/*      */       {
/* 4582 */         if (paramInt1 < i) {
/* 4583 */           if (paramInt2 <= i) {
/* 4584 */             str = JTextComponent.this.getText(paramInt1, paramInt2 - paramInt1);
/*      */           } else {
/* 4586 */             int k = i - paramInt1;
/* 4587 */             str = JTextComponent.this.getText(paramInt1, k) + JTextComponent.this.getText(j, paramInt2 - paramInt1 - k);
/*      */           }
/*      */         }
/*      */         else
/* 4591 */           str = JTextComponent.this.getText(paramInt1 + (j - i), paramInt2 - paramInt1);
/*      */       }
/*      */       catch (BadLocationException localBadLocationException)
/*      */       {
/* 4595 */         throw new IllegalArgumentException("Invalid range");
/*      */       }
/* 4597 */       return new AttributedString(str).getIterator();
/*      */     }
/*      */ 
/*      */     public int getCommittedTextLength() {
/* 4601 */       Document localDocument = JTextComponent.this.getDocument();
/* 4602 */       int i = 0;
/* 4603 */       if (localDocument != null) {
/* 4604 */         i = localDocument.getLength();
/* 4605 */         if (JTextComponent.this.composedTextContent != null) {
/* 4606 */           if ((JTextComponent.this.composedTextEnd == null) || (JTextComponent.this.composedTextStart == null))
/*      */           {
/* 4615 */             i -= JTextComponent.this.composedTextContent.length();
/*      */           }
/* 4617 */           else i -= JTextComponent.this.composedTextEnd.getOffset() - JTextComponent.this.composedTextStart.getOffset();
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 4622 */       return i;
/*      */     }
/*      */ 
/*      */     public int getInsertPositionOffset() {
/* 4626 */       int i = 0;
/* 4627 */       int j = 0;
/* 4628 */       if (JTextComponent.this.composedTextExists()) {
/* 4629 */         i = JTextComponent.this.composedTextStart.getOffset();
/* 4630 */         j = JTextComponent.this.composedTextEnd.getOffset();
/*      */       }
/* 4632 */       int k = JTextComponent.this.getCaretPosition();
/*      */ 
/* 4634 */       if (k < i)
/* 4635 */         return k;
/* 4636 */       if (k < j) {
/* 4637 */         return i;
/*      */       }
/* 4639 */       return k - (j - i);
/*      */     }
/*      */ 
/*      */     public TextHitInfo getLocationOffset(int paramInt1, int paramInt2)
/*      */     {
/* 4644 */       if (JTextComponent.this.composedTextAttribute == null) {
/* 4645 */         return null;
/*      */       }
/* 4647 */       Point localPoint = JTextComponent.this.getLocationOnScreen();
/* 4648 */       localPoint.x = (paramInt1 - localPoint.x);
/* 4649 */       localPoint.y = (paramInt2 - localPoint.y);
/* 4650 */       int i = JTextComponent.this.viewToModel(localPoint);
/* 4651 */       if ((i >= JTextComponent.this.composedTextStart.getOffset()) && (i <= JTextComponent.this.composedTextEnd.getOffset()))
/*      */       {
/* 4653 */         return TextHitInfo.leading(i - JTextComponent.this.composedTextStart.getOffset());
/*      */       }
/* 4655 */       return null;
/*      */     }
/*      */ 
/*      */     public Rectangle getTextLocation(TextHitInfo paramTextHitInfo)
/*      */     {
/*      */       Rectangle localRectangle;
/*      */       try
/*      */       {
/* 4664 */         localRectangle = JTextComponent.this.modelToView(JTextComponent.this.getCaretPosition());
/* 4665 */         if (localRectangle != null) {
/* 4666 */           Point localPoint = JTextComponent.this.getLocationOnScreen();
/* 4667 */           localRectangle.translate(localPoint.x, localPoint.y);
/*      */         }
/*      */       } catch (BadLocationException localBadLocationException) {
/* 4670 */         localRectangle = null;
/*      */       }
/*      */ 
/* 4673 */       if (localRectangle == null) {
/* 4674 */         localRectangle = new Rectangle();
/*      */       }
/* 4676 */       return localRectangle;
/*      */     }
/*      */ 
/*      */     public AttributedCharacterIterator getSelectedText(AttributedCharacterIterator.Attribute[] paramArrayOfAttribute)
/*      */     {
/* 4681 */       String str = JTextComponent.this.getSelectedText();
/* 4682 */       if (str != null) {
/* 4683 */         return new AttributedString(str).getIterator();
/*      */       }
/* 4685 */       return null;
/*      */     }
/*      */ 
/*      */     public void changedUpdate(DocumentEvent paramDocumentEvent)
/*      */     {
/* 4692 */       JTextComponent.this.latestCommittedTextStart = JTextComponent.access$602(JTextComponent.this, null);
/*      */     }
/*      */ 
/*      */     public void insertUpdate(DocumentEvent paramDocumentEvent) {
/* 4696 */       JTextComponent.this.latestCommittedTextStart = JTextComponent.access$602(JTextComponent.this, null);
/*      */     }
/*      */ 
/*      */     public void removeUpdate(DocumentEvent paramDocumentEvent) {
/* 4700 */       JTextComponent.this.latestCommittedTextStart = JTextComponent.access$602(JTextComponent.this, null);
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class KeyBinding
/*      */   {
/*      */     public KeyStroke key;
/*      */     public String actionName;
/*      */ 
/*      */     public KeyBinding(KeyStroke paramKeyStroke, String paramString)
/*      */     {
/* 1148 */       this.key = paramKeyStroke;
/* 1149 */       this.actionName = paramString;
/*      */     }
/*      */   }
/*      */ 
/*      */   static class KeymapActionMap extends ActionMap
/*      */   {
/*      */     private Keymap keymap;
/*      */ 
/*      */     KeymapActionMap(Keymap paramKeymap)
/*      */     {
/* 4318 */       this.keymap = paramKeymap;
/*      */     }
/*      */ 
/*      */     public Object[] keys() {
/* 4322 */       Object[] arrayOfObject1 = super.keys();
/* 4323 */       Action[] arrayOfAction = this.keymap.getBoundActions();
/* 4324 */       int i = arrayOfObject1 == null ? 0 : arrayOfObject1.length;
/* 4325 */       int j = arrayOfAction == null ? 0 : arrayOfAction.length;
/* 4326 */       int k = this.keymap.getDefaultAction() != null ? 1 : 0;
/* 4327 */       if (k != 0) {
/* 4328 */         j++;
/*      */       }
/* 4330 */       if (i == 0) {
/* 4331 */         if (k != 0) {
/* 4332 */           arrayOfObject2 = new Object[j];
/* 4333 */           if (j > 1) {
/* 4334 */             System.arraycopy(arrayOfAction, 0, arrayOfObject2, 0, j - 1);
/*      */           }
/*      */ 
/* 4337 */           arrayOfObject2[(j - 1)] = JTextComponent.KeymapWrapper.DefaultActionKey;
/* 4338 */           return arrayOfObject2;
/*      */         }
/* 4340 */         return arrayOfAction;
/*      */       }
/* 4342 */       if (j == 0) {
/* 4343 */         return arrayOfObject1;
/*      */       }
/* 4345 */       Object[] arrayOfObject2 = new Object[i + j];
/*      */ 
/* 4347 */       System.arraycopy(arrayOfObject1, 0, arrayOfObject2, 0, i);
/* 4348 */       if (k != 0) {
/* 4349 */         if (j > 1) {
/* 4350 */           System.arraycopy(arrayOfAction, 0, arrayOfObject2, i, j - 1);
/*      */         }
/*      */ 
/* 4353 */         arrayOfObject2[(i + j - 1)] = JTextComponent.KeymapWrapper.DefaultActionKey;
/*      */       }
/*      */       else
/*      */       {
/* 4357 */         System.arraycopy(arrayOfAction, 0, arrayOfObject2, i, j);
/*      */       }
/* 4359 */       return arrayOfObject2;
/*      */     }
/*      */ 
/*      */     public int size()
/*      */     {
/* 4364 */       Action[] arrayOfAction = this.keymap.getBoundActions();
/* 4365 */       int i = arrayOfAction == null ? 0 : arrayOfAction.length;
/* 4366 */       if (this.keymap.getDefaultAction() != null) {
/* 4367 */         i++;
/*      */       }
/* 4369 */       return super.size() + i;
/*      */     }
/*      */ 
/*      */     public Action get(Object paramObject) {
/* 4373 */       Action localAction = super.get(paramObject);
/* 4374 */       if (localAction == null)
/*      */       {
/* 4376 */         if (paramObject == JTextComponent.KeymapWrapper.DefaultActionKey) {
/* 4377 */           localAction = this.keymap.getDefaultAction();
/*      */         }
/* 4379 */         else if ((paramObject instanceof Action))
/*      */         {
/* 4383 */           localAction = (Action)paramObject;
/*      */         }
/*      */       }
/* 4386 */       return localAction;
/*      */     }
/*      */   }
/*      */ 
/*      */   static class KeymapWrapper extends InputMap
/*      */   {
/* 4257 */     static final Object DefaultActionKey = new Object();
/*      */     private Keymap keymap;
/*      */ 
/*      */     KeymapWrapper(Keymap paramKeymap)
/*      */     {
/* 4262 */       this.keymap = paramKeymap;
/*      */     }
/*      */ 
/*      */     public KeyStroke[] keys() {
/* 4266 */       KeyStroke[] arrayOfKeyStroke1 = super.keys();
/* 4267 */       KeyStroke[] arrayOfKeyStroke2 = this.keymap.getBoundKeyStrokes();
/* 4268 */       int i = arrayOfKeyStroke1 == null ? 0 : arrayOfKeyStroke1.length;
/* 4269 */       int j = arrayOfKeyStroke2 == null ? 0 : arrayOfKeyStroke2.length;
/* 4270 */       if (i == 0) {
/* 4271 */         return arrayOfKeyStroke2;
/*      */       }
/* 4273 */       if (j == 0) {
/* 4274 */         return arrayOfKeyStroke1;
/*      */       }
/* 4276 */       KeyStroke[] arrayOfKeyStroke3 = new KeyStroke[i + j];
/*      */ 
/* 4278 */       System.arraycopy(arrayOfKeyStroke1, 0, arrayOfKeyStroke3, 0, i);
/* 4279 */       System.arraycopy(arrayOfKeyStroke2, 0, arrayOfKeyStroke3, i, j);
/* 4280 */       return arrayOfKeyStroke3;
/*      */     }
/*      */ 
/*      */     public int size()
/*      */     {
/* 4285 */       KeyStroke[] arrayOfKeyStroke = this.keymap.getBoundKeyStrokes();
/* 4286 */       int i = arrayOfKeyStroke == null ? 0 : arrayOfKeyStroke.length;
/*      */ 
/* 4288 */       return super.size() + i;
/*      */     }
/*      */ 
/*      */     public Object get(KeyStroke paramKeyStroke) {
/* 4292 */       Object localObject = this.keymap.getAction(paramKeyStroke);
/* 4293 */       if (localObject == null) {
/* 4294 */         localObject = super.get(paramKeyStroke);
/* 4295 */         if ((localObject == null) && (paramKeyStroke.getKeyChar() != 65535) && (this.keymap.getDefaultAction() != null))
/*      */         {
/* 4300 */           localObject = DefaultActionKey;
/*      */         }
/*      */       }
/* 4303 */       return localObject;
/*      */     }
/*      */   }
/*      */ 
/*      */   static class MutableCaretEvent extends CaretEvent
/*      */     implements ChangeListener, FocusListener, MouseListener
/*      */   {
/*      */     private boolean dragActive;
/*      */     private int dot;
/*      */     private int mark;
/*      */ 
/*      */     MutableCaretEvent(JTextComponent paramJTextComponent)
/*      */     {
/* 4408 */       super();
/*      */     }
/*      */ 
/*      */     final void fire() {
/* 4412 */       JTextComponent localJTextComponent = (JTextComponent)getSource();
/* 4413 */       if (localJTextComponent != null) {
/* 4414 */         Caret localCaret = localJTextComponent.getCaret();
/* 4415 */         this.dot = localCaret.getDot();
/* 4416 */         this.mark = localCaret.getMark();
/* 4417 */         localJTextComponent.fireCaretUpdate(this);
/*      */       }
/*      */     }
/*      */ 
/*      */     public final String toString() {
/* 4422 */       return "dot=" + this.dot + "," + "mark=" + this.mark;
/*      */     }
/*      */ 
/*      */     public final int getDot()
/*      */     {
/* 4428 */       return this.dot;
/*      */     }
/*      */ 
/*      */     public final int getMark() {
/* 4432 */       return this.mark;
/*      */     }
/*      */ 
/*      */     public final void stateChanged(ChangeEvent paramChangeEvent)
/*      */     {
/* 4438 */       if (!this.dragActive)
/* 4439 */         fire();
/*      */     }
/*      */ 
/*      */     public void focusGained(FocusEvent paramFocusEvent)
/*      */     {
/* 4445 */       AppContext.getAppContext().put(JTextComponent.FOCUSED_COMPONENT, paramFocusEvent.getSource());
/*      */     }
/*      */ 
/*      */     public void focusLost(FocusEvent paramFocusEvent)
/*      */     {
/*      */     }
/*      */ 
/*      */     public final void mousePressed(MouseEvent paramMouseEvent)
/*      */     {
/* 4462 */       this.dragActive = true;
/*      */     }
/*      */ 
/*      */     public final void mouseReleased(MouseEvent paramMouseEvent)
/*      */     {
/* 4472 */       this.dragActive = false;
/* 4473 */       fire();
/*      */     }
/*      */ 
/*      */     public final void mouseClicked(MouseEvent paramMouseEvent)
/*      */     {
/*      */     }
/*      */ 
/*      */     public final void mouseEntered(MouseEvent paramMouseEvent)
/*      */     {
/*      */     }
/*      */ 
/*      */     public final void mouseExited(MouseEvent paramMouseEvent)
/*      */     {
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.text.JTextComponent
 * JD-Core Version:    0.6.2
 */