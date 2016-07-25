/*      */ package javax.swing.plaf.basic;
/*      */ 
/*      */ import java.awt.Color;
/*      */ import java.awt.Component;
/*      */ import java.awt.Container;
/*      */ import java.awt.Cursor;
/*      */ import java.awt.Dimension;
/*      */ import java.awt.Font;
/*      */ import java.awt.Graphics;
/*      */ import java.awt.Insets;
/*      */ import java.awt.LayoutManager;
/*      */ import java.awt.LayoutManager2;
/*      */ import java.awt.Point;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.Shape;
/*      */ import java.awt.datatransfer.DataFlavor;
/*      */ import java.awt.datatransfer.Transferable;
/*      */ import java.awt.datatransfer.UnsupportedFlavorException;
/*      */ import java.awt.event.ActionEvent;
/*      */ import java.awt.event.MouseEvent;
/*      */ import java.awt.im.InputContext;
/*      */ import java.beans.PropertyChangeEvent;
/*      */ import java.beans.PropertyChangeListener;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.Reader;
/*      */ import java.io.StringBufferInputStream;
/*      */ import java.io.StringReader;
/*      */ import java.io.StringWriter;
/*      */ import java.util.Enumeration;
/*      */ import java.util.HashSet;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Set;
/*      */ import javax.swing.AbstractAction;
/*      */ import javax.swing.Action;
/*      */ import javax.swing.ActionMap;
/*      */ import javax.swing.DropMode;
/*      */ import javax.swing.InputMap;
/*      */ import javax.swing.JComponent;
/*      */ import javax.swing.JEditorPane;
/*      */ import javax.swing.JPasswordField;
/*      */ import javax.swing.JTextArea;
/*      */ import javax.swing.KeyStroke;
/*      */ import javax.swing.LookAndFeel;
/*      */ import javax.swing.SwingUtilities;
/*      */ import javax.swing.TransferHandler;
/*      */ import javax.swing.TransferHandler.TransferSupport;
/*      */ import javax.swing.UIDefaults;
/*      */ import javax.swing.UIManager;
/*      */ import javax.swing.border.Border;
/*      */ import javax.swing.event.DocumentEvent;
/*      */ import javax.swing.event.DocumentListener;
/*      */ import javax.swing.event.MouseInputAdapter;
/*      */ import javax.swing.plaf.ActionMapUIResource;
/*      */ import javax.swing.plaf.ComponentInputMapUIResource;
/*      */ import javax.swing.plaf.InputMapUIResource;
/*      */ import javax.swing.plaf.TextUI;
/*      */ import javax.swing.plaf.UIResource;
/*      */ import javax.swing.plaf.synth.SynthUI;
/*      */ import javax.swing.text.AbstractDocument;
/*      */ import javax.swing.text.AttributeSet;
/*      */ import javax.swing.text.BadLocationException;
/*      */ import javax.swing.text.Caret;
/*      */ import javax.swing.text.DefaultCaret;
/*      */ import javax.swing.text.DefaultEditorKit;
/*      */ import javax.swing.text.DefaultEditorKit.InsertBreakAction;
/*      */ import javax.swing.text.DefaultHighlighter;
/*      */ import javax.swing.text.Document;
/*      */ import javax.swing.text.EditorKit;
/*      */ import javax.swing.text.Element;
/*      */ import javax.swing.text.Highlighter;
/*      */ import javax.swing.text.JTextComponent;
/*      */ import javax.swing.text.JTextComponent.DropLocation;
/*      */ import javax.swing.text.JTextComponent.KeyBinding;
/*      */ import javax.swing.text.Keymap;
/*      */ import javax.swing.text.Position;
/*      */ import javax.swing.text.Position.Bias;
/*      */ import javax.swing.text.TextAction;
/*      */ import javax.swing.text.View;
/*      */ import javax.swing.text.ViewFactory;
/*      */ import sun.awt.AppContext;
/*      */ import sun.swing.DefaultLookup;
/*      */ 
/*      */ public abstract class BasicTextUI extends TextUI
/*      */   implements ViewFactory
/*      */ {
/* 1296 */   private static BasicCursor textCursor = new BasicCursor(2);
/*      */ 
/* 1299 */   private static final EditorKit defaultKit = new DefaultEditorKit();
/*      */   transient JTextComponent editor;
/*      */   transient boolean painted;
/* 1302 */   transient RootView rootView = new RootView();
/* 1303 */   transient UpdateHandler updateHandler = new UpdateHandler();
/* 1304 */   private static final TransferHandler defaultTransferHandler = new TextTransferHandler();
/* 1305 */   private final DragListener dragListener = getDragListener();
/* 1306 */   private static final Position.Bias[] discardBias = new Position.Bias[1];
/*      */   private DefaultCaret dropCaret;
/*      */ 
/*      */   public BasicTextUI()
/*      */   {
/*  109 */     this.painted = false;
/*      */   }
/*      */ 
/*      */   protected Caret createCaret()
/*      */   {
/*  121 */     return new BasicCaret();
/*      */   }
/*      */ 
/*      */   protected Highlighter createHighlighter()
/*      */   {
/*  133 */     return new BasicHighlighter();
/*      */   }
/*      */ 
/*      */   protected String getKeymapName()
/*      */   {
/*  145 */     String str = getClass().getName();
/*  146 */     int i = str.lastIndexOf('.');
/*  147 */     if (i >= 0) {
/*  148 */       str = str.substring(i + 1, str.length());
/*      */     }
/*  150 */     return str;
/*      */   }
/*      */ 
/*      */   protected Keymap createKeymap()
/*      */   {
/*  171 */     String str1 = getKeymapName();
/*  172 */     Keymap localKeymap1 = JTextComponent.getKeymap(str1);
/*  173 */     if (localKeymap1 == null) {
/*  174 */       Keymap localKeymap2 = JTextComponent.getKeymap("default");
/*  175 */       localKeymap1 = JTextComponent.addKeymap(str1, localKeymap2);
/*  176 */       String str2 = getPropertyPrefix();
/*  177 */       Object localObject = DefaultLookup.get(this.editor, this, str2 + ".keyBindings");
/*      */ 
/*  179 */       if ((localObject != null) && ((localObject instanceof JTextComponent.KeyBinding[]))) {
/*  180 */         JTextComponent.KeyBinding[] arrayOfKeyBinding = (JTextComponent.KeyBinding[])localObject;
/*  181 */         JTextComponent.loadKeymap(localKeymap1, arrayOfKeyBinding, getComponent().getActions());
/*      */       }
/*      */     }
/*  184 */     return localKeymap1;
/*      */   }
/*      */ 
/*      */   protected void propertyChange(PropertyChangeEvent paramPropertyChangeEvent)
/*      */   {
/*  202 */     if ((paramPropertyChangeEvent.getPropertyName().equals("editable")) || (paramPropertyChangeEvent.getPropertyName().equals("enabled")))
/*      */     {
/*  205 */       updateBackground((JTextComponent)paramPropertyChangeEvent.getSource());
/*      */     }
/*      */   }
/*      */ 
/*      */   private void updateBackground(JTextComponent paramJTextComponent)
/*      */   {
/*  225 */     if (((this instanceof SynthUI)) || ((paramJTextComponent instanceof JTextArea))) {
/*  226 */       return;
/*      */     }
/*  228 */     Color localColor1 = paramJTextComponent.getBackground();
/*  229 */     if ((localColor1 instanceof UIResource)) {
/*  230 */       String str = getPropertyPrefix();
/*      */ 
/*  232 */       Color localColor2 = DefaultLookup.getColor(paramJTextComponent, this, str + ".disabledBackground", null);
/*      */ 
/*  234 */       Color localColor3 = DefaultLookup.getColor(paramJTextComponent, this, str + ".inactiveBackground", null);
/*      */ 
/*  236 */       Color localColor4 = DefaultLookup.getColor(paramJTextComponent, this, str + ".background", null);
/*      */ 
/*  258 */       if ((((paramJTextComponent instanceof JTextArea)) || ((paramJTextComponent instanceof JEditorPane))) && (localColor1 != localColor2) && (localColor1 != localColor3) && (localColor1 != localColor4))
/*      */       {
/*  263 */         return;
/*      */       }
/*      */ 
/*  266 */       Color localColor5 = null;
/*  267 */       if (!paramJTextComponent.isEnabled()) {
/*  268 */         localColor5 = localColor2;
/*      */       }
/*  270 */       if ((localColor5 == null) && (!paramJTextComponent.isEditable())) {
/*  271 */         localColor5 = localColor3;
/*      */       }
/*  273 */       if (localColor5 == null) {
/*  274 */         localColor5 = localColor4;
/*      */       }
/*  276 */       if ((localColor5 != null) && (localColor5 != localColor1))
/*  277 */         paramJTextComponent.setBackground(localColor5);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected abstract String getPropertyPrefix();
/*      */ 
/*      */   protected void installDefaults()
/*      */   {
/*  304 */     String str = getPropertyPrefix();
/*  305 */     Font localFont = this.editor.getFont();
/*  306 */     if ((localFont == null) || ((localFont instanceof UIResource))) {
/*  307 */       this.editor.setFont(UIManager.getFont(str + ".font"));
/*      */     }
/*      */ 
/*  310 */     Color localColor1 = this.editor.getBackground();
/*  311 */     if ((localColor1 == null) || ((localColor1 instanceof UIResource))) {
/*  312 */       this.editor.setBackground(UIManager.getColor(str + ".background"));
/*      */     }
/*      */ 
/*  315 */     Color localColor2 = this.editor.getForeground();
/*  316 */     if ((localColor2 == null) || ((localColor2 instanceof UIResource))) {
/*  317 */       this.editor.setForeground(UIManager.getColor(str + ".foreground"));
/*      */     }
/*      */ 
/*  320 */     Color localColor3 = this.editor.getCaretColor();
/*  321 */     if ((localColor3 == null) || ((localColor3 instanceof UIResource))) {
/*  322 */       this.editor.setCaretColor(UIManager.getColor(str + ".caretForeground"));
/*      */     }
/*      */ 
/*  325 */     Color localColor4 = this.editor.getSelectionColor();
/*  326 */     if ((localColor4 == null) || ((localColor4 instanceof UIResource))) {
/*  327 */       this.editor.setSelectionColor(UIManager.getColor(str + ".selectionBackground"));
/*      */     }
/*      */ 
/*  330 */     Color localColor5 = this.editor.getSelectedTextColor();
/*  331 */     if ((localColor5 == null) || ((localColor5 instanceof UIResource))) {
/*  332 */       this.editor.setSelectedTextColor(UIManager.getColor(str + ".selectionForeground"));
/*      */     }
/*      */ 
/*  335 */     Color localColor6 = this.editor.getDisabledTextColor();
/*  336 */     if ((localColor6 == null) || ((localColor6 instanceof UIResource))) {
/*  337 */       this.editor.setDisabledTextColor(UIManager.getColor(str + ".inactiveForeground"));
/*      */     }
/*      */ 
/*  340 */     Border localBorder = this.editor.getBorder();
/*  341 */     if ((localBorder == null) || ((localBorder instanceof UIResource))) {
/*  342 */       this.editor.setBorder(UIManager.getBorder(str + ".border"));
/*      */     }
/*      */ 
/*  345 */     Insets localInsets = this.editor.getMargin();
/*  346 */     if ((localInsets == null) || ((localInsets instanceof UIResource))) {
/*  347 */       this.editor.setMargin(UIManager.getInsets(str + ".margin"));
/*      */     }
/*      */ 
/*  350 */     updateCursor();
/*      */   }
/*      */ 
/*      */   private void installDefaults2() {
/*  354 */     this.editor.addMouseListener(this.dragListener);
/*  355 */     this.editor.addMouseMotionListener(this.dragListener);
/*      */ 
/*  357 */     String str = getPropertyPrefix();
/*      */ 
/*  359 */     Caret localCaret = this.editor.getCaret();
/*  360 */     if ((localCaret == null) || ((localCaret instanceof UIResource))) {
/*  361 */       localCaret = createCaret();
/*  362 */       this.editor.setCaret(localCaret);
/*      */ 
/*  364 */       int i = DefaultLookup.getInt(getComponent(), this, str + ".caretBlinkRate", 500);
/*  365 */       localCaret.setBlinkRate(i);
/*      */     }
/*      */ 
/*  368 */     Highlighter localHighlighter = this.editor.getHighlighter();
/*  369 */     if ((localHighlighter == null) || ((localHighlighter instanceof UIResource))) {
/*  370 */       this.editor.setHighlighter(createHighlighter());
/*      */     }
/*      */ 
/*  373 */     TransferHandler localTransferHandler = this.editor.getTransferHandler();
/*  374 */     if ((localTransferHandler == null) || ((localTransferHandler instanceof UIResource)))
/*  375 */       this.editor.setTransferHandler(getTransferHandler());
/*      */   }
/*      */ 
/*      */   protected void uninstallDefaults()
/*      */   {
/*  389 */     this.editor.removeMouseListener(this.dragListener);
/*  390 */     this.editor.removeMouseMotionListener(this.dragListener);
/*      */ 
/*  392 */     if ((this.editor.getCaretColor() instanceof UIResource)) {
/*  393 */       this.editor.setCaretColor(null);
/*      */     }
/*      */ 
/*  396 */     if ((this.editor.getSelectionColor() instanceof UIResource)) {
/*  397 */       this.editor.setSelectionColor(null);
/*      */     }
/*      */ 
/*  400 */     if ((this.editor.getDisabledTextColor() instanceof UIResource)) {
/*  401 */       this.editor.setDisabledTextColor(null);
/*      */     }
/*      */ 
/*  404 */     if ((this.editor.getSelectedTextColor() instanceof UIResource)) {
/*  405 */       this.editor.setSelectedTextColor(null);
/*      */     }
/*      */ 
/*  408 */     if ((this.editor.getBorder() instanceof UIResource)) {
/*  409 */       this.editor.setBorder(null);
/*      */     }
/*      */ 
/*  412 */     if ((this.editor.getMargin() instanceof UIResource)) {
/*  413 */       this.editor.setMargin(null);
/*      */     }
/*      */ 
/*  416 */     if ((this.editor.getCaret() instanceof UIResource)) {
/*  417 */       this.editor.setCaret(null);
/*      */     }
/*      */ 
/*  420 */     if ((this.editor.getHighlighter() instanceof UIResource)) {
/*  421 */       this.editor.setHighlighter(null);
/*      */     }
/*      */ 
/*  424 */     if ((this.editor.getTransferHandler() instanceof UIResource)) {
/*  425 */       this.editor.setTransferHandler(null);
/*      */     }
/*      */ 
/*  428 */     if ((this.editor.getCursor() instanceof UIResource))
/*  429 */       this.editor.setCursor(null);
/*      */   }
/*      */ 
/*      */   protected void installListeners()
/*      */   {
/*      */   }
/*      */ 
/*      */   protected void uninstallListeners()
/*      */   {
/*      */   }
/*      */ 
/*      */   protected void installKeyboardActions()
/*      */   {
/*  448 */     this.editor.setKeymap(createKeymap());
/*      */ 
/*  450 */     InputMap localInputMap = getInputMap();
/*  451 */     if (localInputMap != null) {
/*  452 */       SwingUtilities.replaceUIInputMap(this.editor, 0, localInputMap);
/*      */     }
/*      */ 
/*  456 */     ActionMap localActionMap = getActionMap();
/*  457 */     if (localActionMap != null) {
/*  458 */       SwingUtilities.replaceUIActionMap(this.editor, localActionMap);
/*      */     }
/*      */ 
/*  461 */     updateFocusAcceleratorBinding(false);
/*      */   }
/*      */ 
/*      */   InputMap getInputMap()
/*      */   {
/*  468 */     InputMapUIResource localInputMapUIResource = new InputMapUIResource();
/*      */ 
/*  470 */     InputMap localInputMap = (InputMap)DefaultLookup.get(this.editor, this, getPropertyPrefix() + ".focusInputMap");
/*      */ 
/*  473 */     if (localInputMap != null) {
/*  474 */       localInputMapUIResource.setParent(localInputMap);
/*      */     }
/*  476 */     return localInputMapUIResource;
/*      */   }
/*      */ 
/*      */   void updateFocusAcceleratorBinding(boolean paramBoolean)
/*      */   {
/*  484 */     int i = this.editor.getFocusAccelerator();
/*      */ 
/*  486 */     if ((paramBoolean) || (i != 0)) {
/*  487 */       Object localObject = SwingUtilities.getUIInputMap(this.editor, 2);
/*      */ 
/*  490 */       if ((localObject == null) && (i != 0)) {
/*  491 */         localObject = new ComponentInputMapUIResource(this.editor);
/*  492 */         SwingUtilities.replaceUIInputMap(this.editor, 2, (InputMap)localObject);
/*      */ 
/*  494 */         ActionMap localActionMap = getActionMap();
/*  495 */         SwingUtilities.replaceUIActionMap(this.editor, localActionMap);
/*      */       }
/*  497 */       if (localObject != null) {
/*  498 */         ((InputMap)localObject).clear();
/*  499 */         if (i != 0)
/*  500 */           ((InputMap)localObject).put(KeyStroke.getKeyStroke(i, BasicLookAndFeel.getFocusAcceleratorKeyMask()), "requestFocus");
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   void updateFocusTraversalKeys()
/*      */   {
/*  521 */     EditorKit localEditorKit = getEditorKit(this.editor);
/*  522 */     if ((localEditorKit != null) && ((localEditorKit instanceof DefaultEditorKit)))
/*      */     {
/*  524 */       Set localSet1 = this.editor.getFocusTraversalKeys(0);
/*      */ 
/*  527 */       Set localSet2 = this.editor.getFocusTraversalKeys(1);
/*      */ 
/*  530 */       HashSet localHashSet1 = new HashSet(localSet1);
/*      */ 
/*  532 */       HashSet localHashSet2 = new HashSet(localSet2);
/*      */ 
/*  534 */       if (this.editor.isEditable()) {
/*  535 */         localHashSet1.remove(KeyStroke.getKeyStroke(9, 0));
/*      */ 
/*  537 */         localHashSet2.remove(KeyStroke.getKeyStroke(9, 1));
/*      */       }
/*      */       else
/*      */       {
/*  541 */         localHashSet1.add(KeyStroke.getKeyStroke(9, 0));
/*      */ 
/*  543 */         localHashSet2.add(KeyStroke.getKeyStroke(9, 1));
/*      */       }
/*      */ 
/*  547 */       LookAndFeel.installProperty(this.editor, "focusTraversalKeysForward", localHashSet1);
/*      */ 
/*  550 */       LookAndFeel.installProperty(this.editor, "focusTraversalKeysBackward", localHashSet2);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void updateCursor()
/*      */   {
/*  561 */     if ((!this.editor.isCursorSet()) || ((this.editor.getCursor() instanceof UIResource)))
/*      */     {
/*  563 */       Cursor localCursor = this.editor.isEditable() ? textCursor : null;
/*  564 */       this.editor.setCursor(localCursor);
/*      */     }
/*      */   }
/*      */ 
/*      */   TransferHandler getTransferHandler()
/*      */   {
/*  573 */     return defaultTransferHandler;
/*      */   }
/*      */ 
/*      */   ActionMap getActionMap()
/*      */   {
/*  580 */     String str = getPropertyPrefix() + ".actionMap";
/*  581 */     ActionMap localActionMap = (ActionMap)UIManager.get(str);
/*      */ 
/*  583 */     if (localActionMap == null) {
/*  584 */       localActionMap = createActionMap();
/*  585 */       if (localActionMap != null) {
/*  586 */         UIManager.getLookAndFeelDefaults().put(str, localActionMap);
/*      */       }
/*      */     }
/*  589 */     ActionMapUIResource localActionMapUIResource = new ActionMapUIResource();
/*  590 */     localActionMapUIResource.put("requestFocus", new FocusAction());
/*      */ 
/*  600 */     if (((getEditorKit(this.editor) instanceof DefaultEditorKit)) && 
/*  601 */       (localActionMap != null)) {
/*  602 */       Action localAction = localActionMap.get("insert-break");
/*  603 */       if ((localAction != null) && ((localAction instanceof DefaultEditorKit.InsertBreakAction)))
/*      */       {
/*  605 */         TextActionWrapper localTextActionWrapper = new TextActionWrapper((TextAction)localAction);
/*  606 */         localActionMapUIResource.put(localTextActionWrapper.getValue("Name"), localTextActionWrapper);
/*      */       }
/*      */     }
/*      */ 
/*  610 */     if (localActionMap != null) {
/*  611 */       localActionMapUIResource.setParent(localActionMap);
/*      */     }
/*  613 */     return localActionMapUIResource;
/*      */   }
/*      */ 
/*      */   ActionMap createActionMap()
/*      */   {
/*  621 */     ActionMapUIResource localActionMapUIResource = new ActionMapUIResource();
/*  622 */     Action[] arrayOfAction = this.editor.getActions();
/*      */ 
/*  624 */     int i = arrayOfAction.length;
/*  625 */     for (int j = 0; j < i; j++) {
/*  626 */       Action localAction = arrayOfAction[j];
/*  627 */       localActionMapUIResource.put(localAction.getValue("Name"), localAction);
/*      */     }
/*      */ 
/*  630 */     localActionMapUIResource.put(TransferHandler.getCutAction().getValue("Name"), TransferHandler.getCutAction());
/*      */ 
/*  632 */     localActionMapUIResource.put(TransferHandler.getCopyAction().getValue("Name"), TransferHandler.getCopyAction());
/*      */ 
/*  634 */     localActionMapUIResource.put(TransferHandler.getPasteAction().getValue("Name"), TransferHandler.getPasteAction());
/*      */ 
/*  636 */     return localActionMapUIResource;
/*      */   }
/*      */ 
/*      */   protected void uninstallKeyboardActions() {
/*  640 */     this.editor.setKeymap(null);
/*  641 */     SwingUtilities.replaceUIInputMap(this.editor, 2, null);
/*      */ 
/*  643 */     SwingUtilities.replaceUIActionMap(this.editor, null);
/*      */   }
/*      */ 
/*      */   protected void paintBackground(Graphics paramGraphics)
/*      */   {
/*  655 */     paramGraphics.setColor(this.editor.getBackground());
/*  656 */     paramGraphics.fillRect(0, 0, this.editor.getWidth(), this.editor.getHeight());
/*      */   }
/*      */ 
/*      */   protected final JTextComponent getComponent()
/*      */   {
/*  667 */     return this.editor;
/*      */   }
/*      */ 
/*      */   protected void modelChanged()
/*      */   {
/*  679 */     ViewFactory localViewFactory = this.rootView.getViewFactory();
/*  680 */     Document localDocument = this.editor.getDocument();
/*  681 */     Element localElement = localDocument.getDefaultRootElement();
/*  682 */     setView(localViewFactory.create(localElement));
/*      */   }
/*      */ 
/*      */   protected final void setView(View paramView)
/*      */   {
/*  693 */     this.rootView.setView(paramView);
/*  694 */     this.painted = false;
/*  695 */     this.editor.revalidate();
/*  696 */     this.editor.repaint();
/*      */   }
/*      */ 
/*      */   protected void paintSafely(Graphics paramGraphics)
/*      */   {
/*  720 */     this.painted = true;
/*  721 */     Highlighter localHighlighter = this.editor.getHighlighter();
/*  722 */     Caret localCaret = this.editor.getCaret();
/*      */ 
/*  725 */     if (this.editor.isOpaque()) {
/*  726 */       paintBackground(paramGraphics);
/*      */     }
/*      */ 
/*  730 */     if (localHighlighter != null) {
/*  731 */       localHighlighter.paint(paramGraphics);
/*      */     }
/*      */ 
/*  735 */     Rectangle localRectangle = getVisibleEditorRect();
/*  736 */     if (localRectangle != null) {
/*  737 */       this.rootView.paint(paramGraphics, localRectangle);
/*      */     }
/*      */ 
/*  741 */     if (localCaret != null) {
/*  742 */       localCaret.paint(paramGraphics);
/*      */     }
/*      */ 
/*  745 */     if (this.dropCaret != null)
/*  746 */       this.dropCaret.paint(paramGraphics);
/*      */   }
/*      */ 
/*      */   public void installUI(JComponent paramJComponent)
/*      */   {
/*  777 */     if ((paramJComponent instanceof JTextComponent)) {
/*  778 */       this.editor = ((JTextComponent)paramJComponent);
/*      */ 
/*  783 */       LookAndFeel.installProperty(this.editor, "opaque", Boolean.TRUE);
/*  784 */       LookAndFeel.installProperty(this.editor, "autoscrolls", Boolean.TRUE);
/*      */ 
/*  787 */       installDefaults();
/*  788 */       installDefaults2();
/*      */ 
/*  791 */       this.editor.addPropertyChangeListener(this.updateHandler);
/*  792 */       Document localDocument = this.editor.getDocument();
/*  793 */       if (localDocument == null)
/*      */       {
/*  797 */         this.editor.setDocument(getEditorKit(this.editor).createDefaultDocument());
/*      */       } else {
/*  799 */         localDocument.addDocumentListener(this.updateHandler);
/*  800 */         modelChanged();
/*      */       }
/*      */ 
/*  804 */       installListeners();
/*  805 */       installKeyboardActions();
/*      */ 
/*  807 */       LayoutManager localLayoutManager = this.editor.getLayout();
/*  808 */       if ((localLayoutManager == null) || ((localLayoutManager instanceof UIResource)))
/*      */       {
/*  811 */         this.editor.setLayout(this.updateHandler);
/*      */       }
/*      */ 
/*  814 */       updateBackground(this.editor);
/*      */     } else {
/*  816 */       throw new Error("TextUI needs JTextComponent");
/*      */     }
/*      */   }
/*      */ 
/*      */   public void uninstallUI(JComponent paramJComponent)
/*      */   {
/*  829 */     this.editor.removePropertyChangeListener(this.updateHandler);
/*  830 */     this.editor.getDocument().removeDocumentListener(this.updateHandler);
/*      */ 
/*  833 */     this.painted = false;
/*  834 */     uninstallDefaults();
/*  835 */     this.rootView.setView(null);
/*  836 */     paramJComponent.removeAll();
/*  837 */     LayoutManager localLayoutManager = paramJComponent.getLayout();
/*  838 */     if ((localLayoutManager instanceof UIResource)) {
/*  839 */       paramJComponent.setLayout(null);
/*      */     }
/*      */ 
/*  843 */     uninstallKeyboardActions();
/*  844 */     uninstallListeners();
/*      */ 
/*  846 */     this.editor = null;
/*      */   }
/*      */ 
/*      */   public void update(Graphics paramGraphics, JComponent paramJComponent)
/*      */   {
/*  860 */     paint(paramGraphics, paramJComponent);
/*      */   }
/*      */ 
/*      */   public final void paint(Graphics paramGraphics, JComponent paramJComponent)
/*      */   {
/*  875 */     if ((this.rootView.getViewCount() > 0) && (this.rootView.getView(0) != null)) {
/*  876 */       Document localDocument = this.editor.getDocument();
/*  877 */       if ((localDocument instanceof AbstractDocument))
/*  878 */         ((AbstractDocument)localDocument).readLock();
/*      */       try
/*      */       {
/*  881 */         paintSafely(paramGraphics);
/*      */       } finally {
/*  883 */         if ((localDocument instanceof AbstractDocument))
/*  884 */           ((AbstractDocument)localDocument).readUnlock();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public Dimension getPreferredSize(JComponent paramJComponent)
/*      */   {
/*  903 */     Document localDocument = this.editor.getDocument();
/*  904 */     Insets localInsets = paramJComponent.getInsets();
/*  905 */     Dimension localDimension = paramJComponent.getSize();
/*      */ 
/*  907 */     if ((localDocument instanceof AbstractDocument))
/*  908 */       ((AbstractDocument)localDocument).readLock();
/*      */     try
/*      */     {
/*  911 */       if ((localDimension.width > localInsets.left + localInsets.right) && (localDimension.height > localInsets.top + localInsets.bottom)) {
/*  912 */         this.rootView.setSize(localDimension.width - localInsets.left - localInsets.right, localDimension.height - localInsets.top - localInsets.bottom);
/*      */       }
/*  914 */       else if ((localDimension.width == 0) && (localDimension.height == 0))
/*      */       {
/*  917 */         this.rootView.setSize(2.147484E+009F, 2.147484E+009F);
/*      */       }
/*  919 */       localDimension.width = ((int)Math.min(()this.rootView.getPreferredSpan(0) + localInsets.left + localInsets.right, 2147483647L));
/*      */ 
/*  921 */       localDimension.height = ((int)Math.min(()this.rootView.getPreferredSpan(1) + localInsets.top + localInsets.bottom, 2147483647L));
/*      */     }
/*      */     finally {
/*  924 */       if ((localDocument instanceof AbstractDocument)) {
/*  925 */         ((AbstractDocument)localDocument).readUnlock();
/*      */       }
/*      */     }
/*  928 */     return localDimension;
/*      */   }
/*      */ 
/*      */   public Dimension getMinimumSize(JComponent paramJComponent)
/*      */   {
/*  938 */     Document localDocument = this.editor.getDocument();
/*  939 */     Insets localInsets = paramJComponent.getInsets();
/*  940 */     Dimension localDimension = new Dimension();
/*  941 */     if ((localDocument instanceof AbstractDocument))
/*  942 */       ((AbstractDocument)localDocument).readLock();
/*      */     try
/*      */     {
/*  945 */       localDimension.width = ((int)this.rootView.getMinimumSpan(0) + localInsets.left + localInsets.right);
/*  946 */       localDimension.height = ((int)this.rootView.getMinimumSpan(1) + localInsets.top + localInsets.bottom);
/*      */     } finally {
/*  948 */       if ((localDocument instanceof AbstractDocument)) {
/*  949 */         ((AbstractDocument)localDocument).readUnlock();
/*      */       }
/*      */     }
/*  952 */     return localDimension;
/*      */   }
/*      */ 
/*      */   public Dimension getMaximumSize(JComponent paramJComponent)
/*      */   {
/*  962 */     Document localDocument = this.editor.getDocument();
/*  963 */     Insets localInsets = paramJComponent.getInsets();
/*  964 */     Dimension localDimension = new Dimension();
/*  965 */     if ((localDocument instanceof AbstractDocument))
/*  966 */       ((AbstractDocument)localDocument).readLock();
/*      */     try
/*      */     {
/*  969 */       localDimension.width = ((int)Math.min(()this.rootView.getMaximumSpan(0) + localInsets.left + localInsets.right, 2147483647L));
/*      */ 
/*  971 */       localDimension.height = ((int)Math.min(()this.rootView.getMaximumSpan(1) + localInsets.top + localInsets.bottom, 2147483647L));
/*      */     }
/*      */     finally {
/*  974 */       if ((localDocument instanceof AbstractDocument)) {
/*  975 */         ((AbstractDocument)localDocument).readUnlock();
/*      */       }
/*      */     }
/*  978 */     return localDimension;
/*      */   }
/*      */ 
/*      */   protected Rectangle getVisibleEditorRect()
/*      */   {
/*  995 */     Rectangle localRectangle = this.editor.getBounds();
/*  996 */     if ((localRectangle.width > 0) && (localRectangle.height > 0)) {
/*  997 */       localRectangle.x = (localRectangle.y = 0);
/*  998 */       Insets localInsets = this.editor.getInsets();
/*  999 */       localRectangle.x += localInsets.left;
/* 1000 */       localRectangle.y += localInsets.top;
/* 1001 */       localRectangle.width -= localInsets.left + localInsets.right;
/* 1002 */       localRectangle.height -= localInsets.top + localInsets.bottom;
/* 1003 */       return localRectangle;
/*      */     }
/* 1005 */     return null;
/*      */   }
/*      */ 
/*      */   public Rectangle modelToView(JTextComponent paramJTextComponent, int paramInt)
/*      */     throws BadLocationException
/*      */   {
/* 1022 */     return modelToView(paramJTextComponent, paramInt, Position.Bias.Forward);
/*      */   }
/*      */ 
/*      */   public Rectangle modelToView(JTextComponent paramJTextComponent, int paramInt, Position.Bias paramBias)
/*      */     throws BadLocationException
/*      */   {
/* 1039 */     Document localDocument = this.editor.getDocument();
/* 1040 */     if ((localDocument instanceof AbstractDocument))
/* 1041 */       ((AbstractDocument)localDocument).readLock();
/*      */     try
/*      */     {
/* 1044 */       Rectangle localRectangle1 = getVisibleEditorRect();
/* 1045 */       if (localRectangle1 != null) {
/* 1046 */         this.rootView.setSize(localRectangle1.width, localRectangle1.height);
/* 1047 */         Shape localShape = this.rootView.modelToView(paramInt, localRectangle1, paramBias);
/* 1048 */         if (localShape != null)
/* 1049 */           return localShape.getBounds();
/*      */       }
/*      */     }
/*      */     finally {
/* 1053 */       if ((localDocument instanceof AbstractDocument)) {
/* 1054 */         ((AbstractDocument)localDocument).readUnlock();
/*      */       }
/*      */     }
/* 1057 */     return null;
/*      */   }
/*      */ 
/*      */   public int viewToModel(JTextComponent paramJTextComponent, Point paramPoint)
/*      */   {
/* 1074 */     return viewToModel(paramJTextComponent, paramPoint, discardBias);
/*      */   }
/*      */ 
/*      */   public int viewToModel(JTextComponent paramJTextComponent, Point paramPoint, Position.Bias[] paramArrayOfBias)
/*      */   {
/* 1092 */     int i = -1;
/* 1093 */     Document localDocument = this.editor.getDocument();
/* 1094 */     if ((localDocument instanceof AbstractDocument))
/* 1095 */       ((AbstractDocument)localDocument).readLock();
/*      */     try
/*      */     {
/* 1098 */       Rectangle localRectangle = getVisibleEditorRect();
/* 1099 */       if (localRectangle != null) {
/* 1100 */         this.rootView.setSize(localRectangle.width, localRectangle.height);
/* 1101 */         i = this.rootView.viewToModel(paramPoint.x, paramPoint.y, localRectangle, paramArrayOfBias);
/*      */       }
/*      */     } finally {
/* 1104 */       if ((localDocument instanceof AbstractDocument)) {
/* 1105 */         ((AbstractDocument)localDocument).readUnlock();
/*      */       }
/*      */     }
/* 1108 */     return i;
/*      */   }
/*      */ 
/*      */   public int getNextVisualPositionFrom(JTextComponent paramJTextComponent, int paramInt1, Position.Bias paramBias, int paramInt2, Position.Bias[] paramArrayOfBias)
/*      */     throws BadLocationException
/*      */   {
/* 1117 */     Document localDocument = this.editor.getDocument();
/* 1118 */     if ((localDocument instanceof AbstractDocument))
/* 1119 */       ((AbstractDocument)localDocument).readLock();
/*      */     try
/*      */     {
/* 1122 */       if (this.painted) {
/* 1123 */         Rectangle localRectangle = getVisibleEditorRect();
/* 1124 */         if (localRectangle != null) {
/* 1125 */           this.rootView.setSize(localRectangle.width, localRectangle.height);
/*      */         }
/* 1127 */         return this.rootView.getNextVisualPositionFrom(paramInt1, paramBias, localRectangle, paramInt2, paramArrayOfBias);
/*      */       }
/*      */     }
/*      */     finally {
/* 1131 */       if ((localDocument instanceof AbstractDocument)) {
/* 1132 */         ((AbstractDocument)localDocument).readUnlock();
/*      */       }
/*      */     }
/* 1135 */     return -1;
/*      */   }
/*      */ 
/*      */   public void damageRange(JTextComponent paramJTextComponent, int paramInt1, int paramInt2)
/*      */   {
/* 1149 */     damageRange(paramJTextComponent, paramInt1, paramInt2, Position.Bias.Forward, Position.Bias.Backward);
/*      */   }
/*      */ 
/*      */   public void damageRange(JTextComponent paramJTextComponent, int paramInt1, int paramInt2, Position.Bias paramBias1, Position.Bias paramBias2)
/*      */   {
/* 1161 */     if (this.painted) {
/* 1162 */       Rectangle localRectangle1 = getVisibleEditorRect();
/* 1163 */       if (localRectangle1 != null) {
/* 1164 */         Document localDocument = paramJTextComponent.getDocument();
/* 1165 */         if ((localDocument instanceof AbstractDocument))
/* 1166 */           ((AbstractDocument)localDocument).readLock();
/*      */         try
/*      */         {
/* 1169 */           this.rootView.setSize(localRectangle1.width, localRectangle1.height);
/* 1170 */           Shape localShape = this.rootView.modelToView(paramInt1, paramBias1, paramInt2, paramBias2, localRectangle1);
/*      */ 
/* 1172 */           Rectangle localRectangle2 = (localShape instanceof Rectangle) ? (Rectangle)localShape : localShape.getBounds();
/*      */ 
/* 1174 */           this.editor.repaint(localRectangle2.x, localRectangle2.y, localRectangle2.width, localRectangle2.height);
/*      */         } catch (BadLocationException localBadLocationException) {
/*      */         } finally {
/* 1177 */           if ((localDocument instanceof AbstractDocument))
/* 1178 */             ((AbstractDocument)localDocument).readUnlock();
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public EditorKit getEditorKit(JTextComponent paramJTextComponent)
/*      */   {
/* 1193 */     return defaultKit;
/*      */   }
/*      */ 
/*      */   public View getRootView(JTextComponent paramJTextComponent)
/*      */   {
/* 1215 */     return this.rootView;
/*      */   }
/*      */ 
/*      */   public String getToolTipText(JTextComponent paramJTextComponent, Point paramPoint)
/*      */   {
/* 1228 */     if (!this.painted) {
/* 1229 */       return null;
/*      */     }
/* 1231 */     Document localDocument = this.editor.getDocument();
/* 1232 */     String str = null;
/* 1233 */     Rectangle localRectangle = getVisibleEditorRect();
/*      */ 
/* 1235 */     if (localRectangle != null) {
/* 1236 */       if ((localDocument instanceof AbstractDocument))
/* 1237 */         ((AbstractDocument)localDocument).readLock();
/*      */       try
/*      */       {
/* 1240 */         str = this.rootView.getToolTipText(paramPoint.x, paramPoint.y, localRectangle);
/*      */       } finally {
/* 1242 */         if ((localDocument instanceof AbstractDocument)) {
/* 1243 */           ((AbstractDocument)localDocument).readUnlock();
/*      */         }
/*      */       }
/*      */     }
/* 1247 */     return str;
/*      */   }
/*      */ 
/*      */   public View create(Element paramElement)
/*      */   {
/* 1263 */     return null;
/*      */   }
/*      */ 
/*      */   public View create(Element paramElement, int paramInt1, int paramInt2)
/*      */   {
/* 1279 */     return null;
/*      */   }
/*      */ 
/*      */   private static DragListener getDragListener()
/*      */   {
/* 2102 */     synchronized (DragListener.class) {
/* 2103 */       DragListener localDragListener = (DragListener)AppContext.getAppContext().get(DragListener.class);
/*      */ 
/* 2107 */       if (localDragListener == null) {
/* 2108 */         localDragListener = new DragListener();
/* 2109 */         AppContext.getAppContext().put(DragListener.class, localDragListener);
/*      */       }
/*      */ 
/* 2112 */       return localDragListener;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class BasicCaret extends DefaultCaret
/*      */     implements UIResource
/*      */   {
/*      */   }
/*      */ 
/*      */   static class BasicCursor extends Cursor
/*      */     implements UIResource
/*      */   {
/*      */     BasicCursor(int paramInt)
/*      */     {
/* 1288 */       super();
/*      */     }
/*      */ 
/*      */     BasicCursor(String paramString) {
/* 1292 */       super();
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class BasicHighlighter extends DefaultHighlighter
/*      */     implements UIResource
/*      */   {
/*      */   }
/*      */ 
/*      */   static class DragListener extends MouseInputAdapter
/*      */     implements DragRecognitionSupport.BeforeDrag
/*      */   {
/*      */     private boolean dragStarted;
/*      */ 
/*      */     public void dragStarting(MouseEvent paramMouseEvent)
/*      */     {
/* 2126 */       this.dragStarted = true;
/*      */     }
/*      */ 
/*      */     public void mousePressed(MouseEvent paramMouseEvent) {
/* 2130 */       JTextComponent localJTextComponent = (JTextComponent)paramMouseEvent.getSource();
/* 2131 */       if (localJTextComponent.getDragEnabled()) {
/* 2132 */         this.dragStarted = false;
/* 2133 */         if ((isDragPossible(paramMouseEvent)) && (DragRecognitionSupport.mousePressed(paramMouseEvent)))
/* 2134 */           paramMouseEvent.consume();
/*      */       }
/*      */     }
/*      */ 
/*      */     public void mouseReleased(MouseEvent paramMouseEvent)
/*      */     {
/* 2140 */       JTextComponent localJTextComponent = (JTextComponent)paramMouseEvent.getSource();
/* 2141 */       if (localJTextComponent.getDragEnabled()) {
/* 2142 */         if (this.dragStarted) {
/* 2143 */           paramMouseEvent.consume();
/*      */         }
/*      */ 
/* 2146 */         DragRecognitionSupport.mouseReleased(paramMouseEvent);
/*      */       }
/*      */     }
/*      */ 
/*      */     public void mouseDragged(MouseEvent paramMouseEvent) {
/* 2151 */       JTextComponent localJTextComponent = (JTextComponent)paramMouseEvent.getSource();
/* 2152 */       if ((localJTextComponent.getDragEnabled()) && (
/* 2153 */         (this.dragStarted) || (DragRecognitionSupport.mouseDragged(paramMouseEvent, this))))
/* 2154 */         paramMouseEvent.consume();
/*      */     }
/*      */ 
/*      */     protected boolean isDragPossible(MouseEvent paramMouseEvent)
/*      */     {
/* 2167 */       JTextComponent localJTextComponent = (JTextComponent)paramMouseEvent.getSource();
/* 2168 */       if (localJTextComponent.isEnabled()) {
/* 2169 */         Caret localCaret = localJTextComponent.getCaret();
/* 2170 */         int i = localCaret.getDot();
/* 2171 */         int j = localCaret.getMark();
/* 2172 */         if (i != j) {
/* 2173 */           Point localPoint = new Point(paramMouseEvent.getX(), paramMouseEvent.getY());
/* 2174 */           int k = localJTextComponent.viewToModel(localPoint);
/*      */ 
/* 2176 */           int m = Math.min(i, j);
/* 2177 */           int n = Math.max(i, j);
/* 2178 */           if ((k >= m) && (k < n)) {
/* 2179 */             return true;
/*      */           }
/*      */         }
/*      */       }
/* 2183 */       return false;
/*      */     }
/*      */   }
/*      */ 
/*      */   class FocusAction extends AbstractAction
/*      */   {
/*      */     FocusAction()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void actionPerformed(ActionEvent paramActionEvent)
/*      */     {
/* 2093 */       BasicTextUI.this.editor.requestFocus();
/*      */     }
/*      */ 
/*      */     public boolean isEnabled() {
/* 2097 */       return BasicTextUI.this.editor.isEditable();
/*      */     }
/*      */   }
/*      */ 
/*      */   class RootView extends View
/*      */   {
/*      */     private View view;
/*      */ 
/*      */     RootView()
/*      */     {
/* 1316 */       super();
/*      */     }
/*      */ 
/*      */     void setView(View paramView) {
/* 1320 */       View localView = this.view;
/* 1321 */       this.view = null;
/* 1322 */       if (localView != null)
/*      */       {
/* 1325 */         localView.setParent(null);
/*      */       }
/* 1327 */       if (paramView != null) {
/* 1328 */         paramView.setParent(this);
/*      */       }
/* 1330 */       this.view = paramView;
/*      */     }
/*      */ 
/*      */     public AttributeSet getAttributes()
/*      */     {
/* 1339 */       return null;
/*      */     }
/*      */ 
/*      */     public float getPreferredSpan(int paramInt)
/*      */     {
/* 1352 */       if (this.view != null) {
/* 1353 */         return this.view.getPreferredSpan(paramInt);
/*      */       }
/* 1355 */       return 10.0F;
/*      */     }
/*      */ 
/*      */     public float getMinimumSpan(int paramInt)
/*      */     {
/* 1368 */       if (this.view != null) {
/* 1369 */         return this.view.getMinimumSpan(paramInt);
/*      */       }
/* 1371 */       return 10.0F;
/*      */     }
/*      */ 
/*      */     public float getMaximumSpan(int paramInt)
/*      */     {
/* 1384 */       return 2.147484E+009F;
/*      */     }
/*      */ 
/*      */     public void preferenceChanged(View paramView, boolean paramBoolean1, boolean paramBoolean2)
/*      */     {
/* 1406 */       BasicTextUI.this.editor.revalidate();
/*      */     }
/*      */ 
/*      */     public float getAlignment(int paramInt)
/*      */     {
/* 1417 */       if (this.view != null) {
/* 1418 */         return this.view.getAlignment(paramInt);
/*      */       }
/* 1420 */       return 0.0F;
/*      */     }
/*      */ 
/*      */     public void paint(Graphics paramGraphics, Shape paramShape)
/*      */     {
/* 1430 */       if (this.view != null) {
/* 1431 */         Rectangle localRectangle = (paramShape instanceof Rectangle) ? (Rectangle)paramShape : paramShape.getBounds();
/*      */ 
/* 1433 */         setSize(localRectangle.width, localRectangle.height);
/* 1434 */         this.view.paint(paramGraphics, paramShape);
/*      */       }
/*      */     }
/*      */ 
/*      */     public void setParent(View paramView)
/*      */     {
/* 1444 */       throw new Error("Can't set parent on root view");
/*      */     }
/*      */ 
/*      */     public int getViewCount()
/*      */     {
/* 1456 */       return 1;
/*      */     }
/*      */ 
/*      */     public View getView(int paramInt)
/*      */     {
/* 1466 */       return this.view;
/*      */     }
/*      */ 
/*      */     public int getViewIndex(int paramInt, Position.Bias paramBias)
/*      */     {
/* 1480 */       return 0;
/*      */     }
/*      */ 
/*      */     public Shape getChildAllocation(int paramInt, Shape paramShape)
/*      */     {
/* 1496 */       return paramShape;
/*      */     }
/*      */ 
/*      */     public Shape modelToView(int paramInt, Shape paramShape, Position.Bias paramBias)
/*      */       throws BadLocationException
/*      */     {
/* 1508 */       if (this.view != null) {
/* 1509 */         return this.view.modelToView(paramInt, paramShape, paramBias);
/*      */       }
/* 1511 */       return null;
/*      */     }
/*      */ 
/*      */     public Shape modelToView(int paramInt1, Position.Bias paramBias1, int paramInt2, Position.Bias paramBias2, Shape paramShape)
/*      */       throws BadLocationException
/*      */     {
/* 1534 */       if (this.view != null) {
/* 1535 */         return this.view.modelToView(paramInt1, paramBias1, paramInt2, paramBias2, paramShape);
/*      */       }
/* 1537 */       return null;
/*      */     }
/*      */ 
/*      */     public int viewToModel(float paramFloat1, float paramFloat2, Shape paramShape, Position.Bias[] paramArrayOfBias)
/*      */     {
/* 1551 */       if (this.view != null) {
/* 1552 */         int i = this.view.viewToModel(paramFloat1, paramFloat2, paramShape, paramArrayOfBias);
/* 1553 */         return i;
/*      */       }
/* 1555 */       return -1;
/*      */     }
/*      */ 
/*      */     public int getNextVisualPositionFrom(int paramInt1, Position.Bias paramBias, Shape paramShape, int paramInt2, Position.Bias[] paramArrayOfBias)
/*      */       throws BadLocationException
/*      */     {
/* 1579 */       if (this.view != null) {
/* 1580 */         int i = this.view.getNextVisualPositionFrom(paramInt1, paramBias, paramShape, paramInt2, paramArrayOfBias);
/*      */ 
/* 1582 */         if (i != -1) {
/* 1583 */           paramInt1 = i;
/*      */         }
/*      */         else {
/* 1586 */           paramArrayOfBias[0] = paramBias;
/*      */         }
/*      */       }
/* 1589 */       return paramInt1;
/*      */     }
/*      */ 
/*      */     public void insertUpdate(DocumentEvent paramDocumentEvent, Shape paramShape, ViewFactory paramViewFactory)
/*      */     {
/* 1601 */       if (this.view != null)
/* 1602 */         this.view.insertUpdate(paramDocumentEvent, paramShape, paramViewFactory);
/*      */     }
/*      */ 
/*      */     public void removeUpdate(DocumentEvent paramDocumentEvent, Shape paramShape, ViewFactory paramViewFactory)
/*      */     {
/* 1615 */       if (this.view != null)
/* 1616 */         this.view.removeUpdate(paramDocumentEvent, paramShape, paramViewFactory);
/*      */     }
/*      */ 
/*      */     public void changedUpdate(DocumentEvent paramDocumentEvent, Shape paramShape, ViewFactory paramViewFactory)
/*      */     {
/* 1629 */       if (this.view != null)
/* 1630 */         this.view.changedUpdate(paramDocumentEvent, paramShape, paramViewFactory);
/*      */     }
/*      */ 
/*      */     public Document getDocument()
/*      */     {
/* 1640 */       return BasicTextUI.this.editor.getDocument();
/*      */     }
/*      */ 
/*      */     public int getStartOffset()
/*      */     {
/* 1649 */       if (this.view != null) {
/* 1650 */         return this.view.getStartOffset();
/*      */       }
/* 1652 */       return getElement().getStartOffset();
/*      */     }
/*      */ 
/*      */     public int getEndOffset()
/*      */     {
/* 1661 */       if (this.view != null) {
/* 1662 */         return this.view.getEndOffset();
/*      */       }
/* 1664 */       return getElement().getEndOffset();
/*      */     }
/*      */ 
/*      */     public Element getElement()
/*      */     {
/* 1673 */       if (this.view != null) {
/* 1674 */         return this.view.getElement();
/*      */       }
/* 1676 */       return BasicTextUI.this.editor.getDocument().getDefaultRootElement();
/*      */     }
/*      */ 
/*      */     public View breakView(int paramInt, float paramFloat, Shape paramShape)
/*      */     {
/* 1689 */       throw new Error("Can't break root view");
/*      */     }
/*      */ 
/*      */     public int getResizeWeight(int paramInt)
/*      */     {
/* 1700 */       if (this.view != null) {
/* 1701 */         return this.view.getResizeWeight(paramInt);
/*      */       }
/* 1703 */       return 0;
/*      */     }
/*      */ 
/*      */     public void setSize(float paramFloat1, float paramFloat2)
/*      */     {
/* 1713 */       if (this.view != null)
/* 1714 */         this.view.setSize(paramFloat1, paramFloat2);
/*      */     }
/*      */ 
/*      */     public Container getContainer()
/*      */     {
/* 1727 */       return BasicTextUI.this.editor;
/*      */     }
/*      */ 
/*      */     public ViewFactory getViewFactory()
/*      */     {
/* 1744 */       EditorKit localEditorKit = BasicTextUI.this.getEditorKit(BasicTextUI.this.editor);
/* 1745 */       ViewFactory localViewFactory = localEditorKit.getViewFactory();
/* 1746 */       if (localViewFactory != null) {
/* 1747 */         return localViewFactory;
/*      */       }
/* 1749 */       return BasicTextUI.this;
/*      */     }
/*      */   }
/*      */ 
/*      */   class TextActionWrapper extends TextAction
/*      */   {
/* 2083 */     TextAction action = null;
/*      */ 
/*      */     public TextActionWrapper(TextAction arg2)
/*      */     {
/* 2069 */       super();
/* 2070 */       this.action = localObject;
/*      */     }
/*      */ 
/*      */     public void actionPerformed(ActionEvent paramActionEvent)
/*      */     {
/* 2078 */       this.action.actionPerformed(paramActionEvent);
/*      */     }
/*      */     public boolean isEnabled() {
/* 2081 */       return (BasicTextUI.this.editor == null) || (BasicTextUI.this.editor.isEditable()) ? this.action.isEnabled() : false;
/*      */     }
/*      */   }
/*      */ 
/*      */   static class TextTransferHandler extends TransferHandler
/*      */     implements UIResource
/*      */   {
/*      */     private JTextComponent exportComp;
/*      */     private boolean shouldRemove;
/*      */     private int p0;
/*      */     private int p1;
/*      */     private boolean modeBetween;
/*      */     private boolean isDrop;
/*      */     private int dropAction;
/*      */     private Position.Bias dropBias;
/*      */ 
/*      */     TextTransferHandler()
/*      */     {
/* 2198 */       this.modeBetween = false;
/*      */ 
/* 2203 */       this.isDrop = false;
/*      */ 
/* 2208 */       this.dropAction = 2;
/*      */     }
/*      */ 
/*      */     protected DataFlavor getImportFlavor(DataFlavor[] paramArrayOfDataFlavor, JTextComponent paramJTextComponent)
/*      */     {
/* 2228 */       DataFlavor localDataFlavor1 = null;
/* 2229 */       DataFlavor localDataFlavor2 = null;
/* 2230 */       DataFlavor localDataFlavor3 = null;
/*      */       String str;
/* 2232 */       if ((paramJTextComponent instanceof JEditorPane)) {
/* 2233 */         for (i = 0; i < paramArrayOfDataFlavor.length; i++) {
/* 2234 */           str = paramArrayOfDataFlavor[i].getMimeType();
/* 2235 */           if (str.startsWith(((JEditorPane)paramJTextComponent).getEditorKit().getContentType()))
/* 2236 */             return paramArrayOfDataFlavor[i];
/* 2237 */           if ((localDataFlavor1 == null) && (str.startsWith("text/plain")))
/* 2238 */             localDataFlavor1 = paramArrayOfDataFlavor[i];
/* 2239 */           else if ((localDataFlavor2 == null) && (str.startsWith("application/x-java-jvm-local-objectref")) && (paramArrayOfDataFlavor[i].getRepresentationClass() == String.class))
/*      */           {
/* 2241 */             localDataFlavor2 = paramArrayOfDataFlavor[i];
/* 2242 */           } else if ((localDataFlavor3 == null) && (paramArrayOfDataFlavor[i].equals(DataFlavor.stringFlavor))) {
/* 2243 */             localDataFlavor3 = paramArrayOfDataFlavor[i];
/*      */           }
/*      */         }
/* 2246 */         if (localDataFlavor1 != null)
/* 2247 */           return localDataFlavor1;
/* 2248 */         if (localDataFlavor2 != null)
/* 2249 */           return localDataFlavor2;
/* 2250 */         if (localDataFlavor3 != null) {
/* 2251 */           return localDataFlavor3;
/*      */         }
/* 2253 */         return null;
/*      */       }
/*      */ 
/* 2257 */       for (int i = 0; i < paramArrayOfDataFlavor.length; i++) {
/* 2258 */         str = paramArrayOfDataFlavor[i].getMimeType();
/* 2259 */         if (str.startsWith("text/plain"))
/* 2260 */           return paramArrayOfDataFlavor[i];
/* 2261 */         if ((localDataFlavor2 == null) && (str.startsWith("application/x-java-jvm-local-objectref")) && (paramArrayOfDataFlavor[i].getRepresentationClass() == String.class))
/*      */         {
/* 2263 */           localDataFlavor2 = paramArrayOfDataFlavor[i];
/* 2264 */         } else if ((localDataFlavor3 == null) && (paramArrayOfDataFlavor[i].equals(DataFlavor.stringFlavor))) {
/* 2265 */           localDataFlavor3 = paramArrayOfDataFlavor[i];
/*      */         }
/*      */       }
/* 2268 */       if (localDataFlavor2 != null)
/* 2269 */         return localDataFlavor2;
/* 2270 */       if (localDataFlavor3 != null) {
/* 2271 */         return localDataFlavor3;
/*      */       }
/* 2273 */       return null;
/*      */     }
/*      */ 
/*      */     protected void handleReaderImport(Reader paramReader, JTextComponent paramJTextComponent, boolean paramBoolean)
/*      */       throws BadLocationException, IOException
/*      */     {
/*      */       int j;
/*      */       int k;
/*      */       Object localObject;
/* 2281 */       if (paramBoolean) {
/* 2282 */         int i = paramJTextComponent.getSelectionStart();
/* 2283 */         j = paramJTextComponent.getSelectionEnd();
/* 2284 */         k = j - i;
/* 2285 */         EditorKit localEditorKit = paramJTextComponent.getUI().getEditorKit(paramJTextComponent);
/* 2286 */         localObject = paramJTextComponent.getDocument();
/* 2287 */         if (k > 0) {
/* 2288 */           ((Document)localObject).remove(i, k);
/*      */         }
/* 2290 */         localEditorKit.read(paramReader, (Document)localObject, i);
/*      */       } else {
/* 2292 */         char[] arrayOfChar = new char[1024];
/*      */ 
/* 2294 */         k = 0;
/*      */ 
/* 2296 */         localObject = null;
/*      */ 
/* 2300 */         while ((j = paramReader.read(arrayOfChar, 0, arrayOfChar.length)) != -1) {
/* 2301 */           if (localObject == null) {
/* 2302 */             localObject = new StringBuffer(j);
/*      */           }
/* 2304 */           int m = 0;
/* 2305 */           for (int n = 0; n < j; n++) {
/* 2306 */             switch (arrayOfChar[n]) {
/*      */             case '\r':
/* 2308 */               if (k != 0) {
/* 2309 */                 if (n == 0)
/* 2310 */                   ((StringBuffer)localObject).append('\n');
/*      */                 else
/* 2312 */                   arrayOfChar[(n - 1)] = '\n';
/*      */               }
/*      */               else {
/* 2315 */                 k = 1;
/*      */               }
/* 2317 */               break;
/*      */             case '\n':
/* 2319 */               if (k != 0) {
/* 2320 */                 if (n > m + 1) {
/* 2321 */                   ((StringBuffer)localObject).append(arrayOfChar, m, n - m - 1);
/*      */                 }
/*      */ 
/* 2325 */                 k = 0;
/* 2326 */                 m = n; } break;
/*      */             default:
/* 2330 */               if (k != 0) {
/* 2331 */                 if (n == 0)
/* 2332 */                   ((StringBuffer)localObject).append('\n');
/*      */                 else {
/* 2334 */                   arrayOfChar[(n - 1)] = '\n';
/*      */                 }
/* 2336 */                 k = 0;
/*      */               }
/*      */               break;
/*      */             }
/*      */           }
/* 2341 */           if (m < j) {
/* 2342 */             if (k != 0) {
/* 2343 */               if (m < j - 1)
/* 2344 */                 ((StringBuffer)localObject).append(arrayOfChar, m, j - m - 1);
/*      */             }
/*      */             else {
/* 2347 */               ((StringBuffer)localObject).append(arrayOfChar, m, j - m);
/*      */             }
/*      */           }
/*      */         }
/* 2351 */         if (k != 0) {
/* 2352 */           ((StringBuffer)localObject).append('\n');
/*      */         }
/* 2354 */         paramJTextComponent.replaceSelection(localObject != null ? ((StringBuffer)localObject).toString() : "");
/*      */       }
/*      */     }
/*      */ 
/*      */     public int getSourceActions(JComponent paramJComponent)
/*      */     {
/* 2373 */       if (((paramJComponent instanceof JPasswordField)) && (paramJComponent.getClientProperty("JPasswordField.cutCopyAllowed") != Boolean.TRUE))
/*      */       {
/* 2376 */         return 0;
/*      */       }
/*      */ 
/* 2379 */       return ((JTextComponent)paramJComponent).isEditable() ? 3 : 1;
/*      */     }
/*      */ 
/*      */     protected Transferable createTransferable(JComponent paramJComponent)
/*      */     {
/* 2392 */       this.exportComp = ((JTextComponent)paramJComponent);
/* 2393 */       this.shouldRemove = true;
/* 2394 */       this.p0 = this.exportComp.getSelectionStart();
/* 2395 */       this.p1 = this.exportComp.getSelectionEnd();
/* 2396 */       return this.p0 != this.p1 ? new TextTransferable(this.exportComp, this.p0, this.p1) : null;
/*      */     }
/*      */ 
/*      */     protected void exportDone(JComponent paramJComponent, Transferable paramTransferable, int paramInt)
/*      */     {
/* 2411 */       if ((this.shouldRemove) && (paramInt == 2)) {
/* 2412 */         TextTransferable localTextTransferable = (TextTransferable)paramTransferable;
/* 2413 */         localTextTransferable.removeText();
/*      */       }
/*      */ 
/* 2416 */       this.exportComp = null;
/*      */     }
/*      */ 
/*      */     public boolean importData(TransferHandler.TransferSupport paramTransferSupport) {
/* 2420 */       this.isDrop = paramTransferSupport.isDrop();
/*      */ 
/* 2422 */       if (this.isDrop) {
/* 2423 */         this.modeBetween = (((JTextComponent)paramTransferSupport.getComponent()).getDropMode() == DropMode.INSERT);
/*      */ 
/* 2426 */         this.dropBias = ((JTextComponent.DropLocation)paramTransferSupport.getDropLocation()).getBias();
/*      */ 
/* 2428 */         this.dropAction = paramTransferSupport.getDropAction();
/*      */       }
/*      */       try
/*      */       {
/* 2432 */         return super.importData(paramTransferSupport);
/*      */       } finally {
/* 2434 */         this.isDrop = false;
/* 2435 */         this.modeBetween = false;
/* 2436 */         this.dropBias = null;
/* 2437 */         this.dropAction = 2;
/*      */       }
/*      */     }
/*      */ 
/*      */     public boolean importData(JComponent paramJComponent, Transferable paramTransferable)
/*      */     {
/* 2453 */       JTextComponent localJTextComponent = (JTextComponent)paramJComponent;
/*      */ 
/* 2455 */       int i = this.modeBetween ? localJTextComponent.getDropLocation().getIndex() : localJTextComponent.getCaretPosition();
/*      */ 
/* 2462 */       if ((this.dropAction == 2) && (localJTextComponent == this.exportComp) && (i >= this.p0) && (i <= this.p1)) {
/* 2463 */         this.shouldRemove = false;
/* 2464 */         return true;
/*      */       }
/*      */ 
/* 2467 */       boolean bool1 = false;
/* 2468 */       DataFlavor localDataFlavor = getImportFlavor(paramTransferable.getTransferDataFlavors(), localJTextComponent);
/* 2469 */       if (localDataFlavor != null)
/*      */         try {
/* 2471 */           boolean bool2 = false;
/* 2472 */           if ((paramJComponent instanceof JEditorPane)) {
/* 2473 */             localObject = (JEditorPane)paramJComponent;
/* 2474 */             if ((!((JEditorPane)localObject).getContentType().startsWith("text/plain")) && (localDataFlavor.getMimeType().startsWith(((JEditorPane)localObject).getContentType())))
/*      */             {
/* 2476 */               bool2 = true;
/*      */             }
/*      */           }
/* 2479 */           Object localObject = localJTextComponent.getInputContext();
/* 2480 */           if (localObject != null) {
/* 2481 */             ((InputContext)localObject).endComposition();
/*      */           }
/* 2483 */           Reader localReader = localDataFlavor.getReaderForText(paramTransferable);
/*      */           Caret localCaret;
/* 2485 */           if (this.modeBetween) {
/* 2486 */             localCaret = localJTextComponent.getCaret();
/* 2487 */             if ((localCaret instanceof DefaultCaret))
/* 2488 */               ((DefaultCaret)localCaret).setDot(i, this.dropBias);
/*      */             else {
/* 2490 */               localJTextComponent.setCaretPosition(i);
/*      */             }
/*      */           }
/*      */ 
/* 2494 */           handleReaderImport(localReader, localJTextComponent, bool2);
/*      */ 
/* 2496 */           if (this.isDrop) {
/* 2497 */             localJTextComponent.requestFocus();
/* 2498 */             localCaret = localJTextComponent.getCaret();
/* 2499 */             if ((localCaret instanceof DefaultCaret)) {
/* 2500 */               int j = localCaret.getDot();
/* 2501 */               Position.Bias localBias = ((DefaultCaret)localCaret).getDotBias();
/*      */ 
/* 2503 */               ((DefaultCaret)localCaret).setDot(i, this.dropBias);
/* 2504 */               ((DefaultCaret)localCaret).moveDot(j, localBias);
/*      */             } else {
/* 2506 */               localJTextComponent.select(i, localJTextComponent.getCaretPosition());
/*      */             }
/*      */           }
/*      */ 
/* 2510 */           bool1 = true;
/*      */         } catch (UnsupportedFlavorException localUnsupportedFlavorException) {
/*      */         } catch (BadLocationException localBadLocationException) {
/*      */         }
/*      */         catch (IOException localIOException) {
/*      */         }
/* 2516 */       return bool1;
/*      */     }
/*      */ 
/*      */     public boolean canImport(JComponent paramJComponent, DataFlavor[] paramArrayOfDataFlavor)
/*      */     {
/* 2530 */       JTextComponent localJTextComponent = (JTextComponent)paramJComponent;
/* 2531 */       if ((!localJTextComponent.isEditable()) || (!localJTextComponent.isEnabled())) {
/* 2532 */         return false;
/*      */       }
/* 2534 */       return getImportFlavor(paramArrayOfDataFlavor, localJTextComponent) != null;
/*      */     }
/*      */ 
/*      */     static class TextTransferable extends BasicTransferable {
/*      */       Position p0;
/*      */       Position p1;
/*      */       String mimeType;
/*      */       String richText;
/*      */       JTextComponent c;
/*      */ 
/*      */       TextTransferable(JTextComponent paramJTextComponent, int paramInt1, int paramInt2) {
/* 2549 */         super(null);
/*      */ 
/* 2551 */         this.c = paramJTextComponent;
/*      */ 
/* 2553 */         Document localDocument = paramJTextComponent.getDocument();
/*      */         try
/*      */         {
/* 2556 */           this.p0 = localDocument.createPosition(paramInt1);
/* 2557 */           this.p1 = localDocument.createPosition(paramInt2);
/*      */ 
/* 2559 */           this.plainData = paramJTextComponent.getSelectedText();
/*      */ 
/* 2561 */           if ((paramJTextComponent instanceof JEditorPane)) {
/* 2562 */             JEditorPane localJEditorPane = (JEditorPane)paramJTextComponent;
/*      */ 
/* 2564 */             this.mimeType = localJEditorPane.getContentType();
/*      */ 
/* 2566 */             if (this.mimeType.startsWith("text/plain")) {
/* 2567 */               return;
/*      */             }
/*      */ 
/* 2570 */             StringWriter localStringWriter = new StringWriter(this.p1.getOffset() - this.p0.getOffset());
/* 2571 */             localJEditorPane.getEditorKit().write(localStringWriter, localDocument, this.p0.getOffset(), this.p1.getOffset() - this.p0.getOffset());
/*      */ 
/* 2573 */             if (this.mimeType.startsWith("text/html"))
/* 2574 */               this.htmlData = localStringWriter.toString();
/*      */             else
/* 2576 */               this.richText = localStringWriter.toString();
/*      */           }
/*      */         } catch (BadLocationException localBadLocationException) {
/*      */         }
/*      */         catch (IOException localIOException) {
/*      */         }
/*      */       }
/*      */ 
/*      */       void removeText() {
/* 2585 */         if ((this.p0 != null) && (this.p1 != null) && (this.p0.getOffset() != this.p1.getOffset()))
/*      */           try {
/* 2587 */             Document localDocument = this.c.getDocument();
/* 2588 */             localDocument.remove(this.p0.getOffset(), this.p1.getOffset() - this.p0.getOffset());
/*      */           }
/*      */           catch (BadLocationException localBadLocationException)
/*      */           {
/*      */           }
/*      */       }
/*      */ 
/*      */       protected DataFlavor[] getRicherFlavors()
/*      */       {
/* 2601 */         if (this.richText == null) {
/* 2602 */           return null;
/*      */         }
/*      */         try
/*      */         {
/* 2606 */           DataFlavor[] arrayOfDataFlavor = new DataFlavor[3];
/* 2607 */           arrayOfDataFlavor[0] = new DataFlavor(this.mimeType + ";class=java.lang.String");
/* 2608 */           arrayOfDataFlavor[1] = new DataFlavor(this.mimeType + ";class=java.io.Reader");
/* 2609 */           arrayOfDataFlavor[2] = new DataFlavor(this.mimeType + ";class=java.io.InputStream;charset=unicode");
/* 2610 */           return arrayOfDataFlavor;
/*      */         }
/*      */         catch (ClassNotFoundException localClassNotFoundException)
/*      */         {
/*      */         }
/* 2615 */         return null;
/*      */       }
/*      */ 
/*      */       protected Object getRicherData(DataFlavor paramDataFlavor)
/*      */         throws UnsupportedFlavorException
/*      */       {
/* 2622 */         if (this.richText == null) {
/* 2623 */           return null;
/*      */         }
/*      */ 
/* 2626 */         if (String.class.equals(paramDataFlavor.getRepresentationClass()))
/* 2627 */           return this.richText;
/* 2628 */         if (Reader.class.equals(paramDataFlavor.getRepresentationClass()))
/* 2629 */           return new StringReader(this.richText);
/* 2630 */         if (InputStream.class.equals(paramDataFlavor.getRepresentationClass())) {
/* 2631 */           return new StringBufferInputStream(this.richText);
/*      */         }
/* 2633 */         throw new UnsupportedFlavorException(paramDataFlavor);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   class UpdateHandler
/*      */     implements PropertyChangeListener, DocumentListener, LayoutManager2, UIResource
/*      */   {
/*      */     private Hashtable<Component, Object> constraints;
/* 2061 */     private boolean i18nView = false;
/*      */ 
/*      */     UpdateHandler()
/*      */     {
/*      */     }
/*      */ 
/*      */     public final void propertyChange(PropertyChangeEvent paramPropertyChangeEvent)
/*      */     {
/* 1773 */       Object localObject1 = paramPropertyChangeEvent.getOldValue();
/* 1774 */       Object localObject2 = paramPropertyChangeEvent.getNewValue();
/* 1775 */       String str = paramPropertyChangeEvent.getPropertyName();
/* 1776 */       if (((localObject1 instanceof Document)) || ((localObject2 instanceof Document))) {
/* 1777 */         if (localObject1 != null) {
/* 1778 */           ((Document)localObject1).removeDocumentListener(this);
/* 1779 */           this.i18nView = false;
/*      */         }
/* 1781 */         if (localObject2 != null) {
/* 1782 */           ((Document)localObject2).addDocumentListener(this);
/* 1783 */           if ("document" == str) {
/* 1784 */             BasicTextUI.this.setView(null);
/* 1785 */             BasicTextUI.this.propertyChange(paramPropertyChangeEvent);
/* 1786 */             BasicTextUI.this.modelChanged();
/* 1787 */             return;
/*      */           }
/*      */         }
/* 1790 */         BasicTextUI.this.modelChanged();
/*      */       }
/* 1792 */       if ("focusAccelerator" == str) {
/* 1793 */         BasicTextUI.this.updateFocusAcceleratorBinding(true);
/* 1794 */       } else if ("componentOrientation" == str)
/*      */       {
/* 1797 */         BasicTextUI.this.modelChanged();
/* 1798 */       } else if ("font" == str) {
/* 1799 */         BasicTextUI.this.modelChanged();
/* 1800 */       } else if ("dropLocation" == str) {
/* 1801 */         dropIndexChanged();
/* 1802 */       } else if ("editable" == str) {
/* 1803 */         BasicTextUI.this.updateCursor();
/* 1804 */         BasicTextUI.this.modelChanged();
/*      */       }
/* 1806 */       BasicTextUI.this.propertyChange(paramPropertyChangeEvent);
/*      */     }
/*      */ 
/*      */     private void dropIndexChanged() {
/* 1810 */       if (BasicTextUI.this.editor.getDropMode() == DropMode.USE_SELECTION) {
/* 1811 */         return;
/*      */       }
/*      */ 
/* 1814 */       JTextComponent.DropLocation localDropLocation = BasicTextUI.this.editor.getDropLocation();
/*      */ 
/* 1816 */       if (localDropLocation == null) {
/* 1817 */         if (BasicTextUI.this.dropCaret != null) {
/* 1818 */           BasicTextUI.this.dropCaret.deinstall(BasicTextUI.this.editor);
/* 1819 */           BasicTextUI.this.editor.repaint(BasicTextUI.this.dropCaret);
/* 1820 */           BasicTextUI.this.dropCaret = null;
/*      */         }
/*      */       } else {
/* 1823 */         if (BasicTextUI.this.dropCaret == null) {
/* 1824 */           BasicTextUI.this.dropCaret = new BasicTextUI.BasicCaret();
/* 1825 */           BasicTextUI.this.dropCaret.install(BasicTextUI.this.editor);
/* 1826 */           BasicTextUI.this.dropCaret.setVisible(true);
/*      */         }
/*      */ 
/* 1829 */         BasicTextUI.this.dropCaret.setDot(localDropLocation.getIndex(), localDropLocation.getBias());
/*      */       }
/*      */     }
/*      */ 
/*      */     public final void insertUpdate(DocumentEvent paramDocumentEvent)
/*      */     {
/* 1847 */       Document localDocument = paramDocumentEvent.getDocument();
/* 1848 */       Object localObject = localDocument.getProperty("i18n");
/* 1849 */       if ((localObject instanceof Boolean)) {
/* 1850 */         localBoolean = (Boolean)localObject;
/* 1851 */         if (localBoolean.booleanValue() != this.i18nView)
/*      */         {
/* 1853 */           this.i18nView = localBoolean.booleanValue();
/* 1854 */           BasicTextUI.this.modelChanged();
/* 1855 */           return;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1860 */       Boolean localBoolean = BasicTextUI.this.painted ? BasicTextUI.this.getVisibleEditorRect() : null;
/* 1861 */       BasicTextUI.this.rootView.insertUpdate(paramDocumentEvent, localBoolean, BasicTextUI.this.rootView.getViewFactory());
/*      */     }
/*      */ 
/*      */     public final void removeUpdate(DocumentEvent paramDocumentEvent)
/*      */     {
/* 1875 */       Shape localShape = BasicTextUI.this.painted ? BasicTextUI.this.getVisibleEditorRect() : null;
/* 1876 */       BasicTextUI.this.rootView.removeUpdate(paramDocumentEvent, localShape, BasicTextUI.this.rootView.getViewFactory());
/*      */     }
/*      */ 
/*      */     public final void changedUpdate(DocumentEvent paramDocumentEvent)
/*      */     {
/* 1890 */       Shape localShape = BasicTextUI.this.painted ? BasicTextUI.this.getVisibleEditorRect() : null;
/* 1891 */       BasicTextUI.this.rootView.changedUpdate(paramDocumentEvent, localShape, BasicTextUI.this.rootView.getViewFactory());
/*      */     }
/*      */ 
/*      */     public void addLayoutComponent(String paramString, Component paramComponent)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void removeLayoutComponent(Component paramComponent)
/*      */     {
/* 1911 */       if (this.constraints != null)
/*      */       {
/* 1913 */         this.constraints.remove(paramComponent);
/*      */       }
/*      */     }
/*      */ 
/*      */     public Dimension preferredLayoutSize(Container paramContainer)
/*      */     {
/* 1926 */       return null;
/*      */     }
/*      */ 
/*      */     public Dimension minimumLayoutSize(Container paramContainer)
/*      */     {
/* 1937 */       return null;
/*      */     }
/*      */ 
/*      */     public void layoutContainer(Container paramContainer)
/*      */     {
/* 1954 */       if ((this.constraints != null) && (!this.constraints.isEmpty())) {
/* 1955 */         Rectangle localRectangle1 = BasicTextUI.this.getVisibleEditorRect();
/* 1956 */         if (localRectangle1 != null) {
/* 1957 */           Document localDocument = BasicTextUI.this.editor.getDocument();
/* 1958 */           if ((localDocument instanceof AbstractDocument))
/* 1959 */             ((AbstractDocument)localDocument).readLock();
/*      */           try
/*      */           {
/* 1962 */             BasicTextUI.this.rootView.setSize(localRectangle1.width, localRectangle1.height);
/* 1963 */             Enumeration localEnumeration = this.constraints.keys();
/* 1964 */             while (localEnumeration.hasMoreElements()) {
/* 1965 */               Component localComponent = (Component)localEnumeration.nextElement();
/* 1966 */               View localView = (View)this.constraints.get(localComponent);
/* 1967 */               Shape localShape = calculateViewPosition(localRectangle1, localView);
/* 1968 */               if (localShape != null) {
/* 1969 */                 Rectangle localRectangle2 = (localShape instanceof Rectangle) ? (Rectangle)localShape : localShape.getBounds();
/*      */ 
/* 1971 */                 localComponent.setBounds(localRectangle2);
/*      */               }
/*      */             }
/*      */           } finally {
/* 1975 */             if ((localDocument instanceof AbstractDocument))
/* 1976 */               ((AbstractDocument)localDocument).readUnlock();
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     Shape calculateViewPosition(Shape paramShape, View paramView)
/*      */     {
/* 1987 */       int i = paramView.getStartOffset();
/* 1988 */       View localView = null;
/* 1989 */       for (Object localObject = BasicTextUI.this.rootView; (localObject != null) && (localObject != paramView); localObject = localView) {
/* 1990 */         int j = ((View)localObject).getViewIndex(i, Position.Bias.Forward);
/* 1991 */         paramShape = ((View)localObject).getChildAllocation(j, paramShape);
/* 1992 */         localView = ((View)localObject).getView(j);
/*      */       }
/* 1994 */       return localView != null ? paramShape : null;
/*      */     }
/*      */ 
/*      */     public void addLayoutComponent(Component paramComponent, Object paramObject)
/*      */     {
/* 2006 */       if ((paramObject instanceof View)) {
/* 2007 */         if (this.constraints == null) {
/* 2008 */           this.constraints = new Hashtable(7);
/*      */         }
/* 2010 */         this.constraints.put(paramComponent, paramObject);
/*      */       }
/*      */     }
/*      */ 
/*      */     public Dimension maximumLayoutSize(Container paramContainer)
/*      */     {
/* 2022 */       return null;
/*      */     }
/*      */ 
/*      */     public float getLayoutAlignmentX(Container paramContainer)
/*      */     {
/* 2033 */       return 0.5F;
/*      */     }
/*      */ 
/*      */     public float getLayoutAlignmentY(Container paramContainer)
/*      */     {
/* 2044 */       return 0.5F;
/*      */     }
/*      */ 
/*      */     public void invalidateLayout(Container paramContainer)
/*      */     {
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.basic.BasicTextUI
 * JD-Core Version:    0.6.2
 */