/*      */ package javax.swing.text;
/*      */ 
/*      */ import java.awt.Component;
/*      */ import java.awt.Graphics;
/*      */ import java.awt.HeadlessException;
/*      */ import java.awt.Point;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.Toolkit;
/*      */ import java.awt.datatransfer.Clipboard;
/*      */ import java.awt.datatransfer.ClipboardOwner;
/*      */ import java.awt.datatransfer.StringSelection;
/*      */ import java.awt.datatransfer.Transferable;
/*      */ import java.awt.event.ActionEvent;
/*      */ import java.awt.event.ActionListener;
/*      */ import java.awt.event.FocusEvent;
/*      */ import java.awt.event.FocusListener;
/*      */ import java.awt.event.MouseEvent;
/*      */ import java.awt.event.MouseListener;
/*      */ import java.awt.event.MouseMotionListener;
/*      */ import java.beans.PropertyChangeEvent;
/*      */ import java.beans.PropertyChangeListener;
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.util.EventListener;
/*      */ import javax.swing.Action;
/*      */ import javax.swing.ActionMap;
/*      */ import javax.swing.JComponent;
/*      */ import javax.swing.JPasswordField;
/*      */ import javax.swing.LookAndFeel;
/*      */ import javax.swing.SwingUtilities;
/*      */ import javax.swing.Timer;
/*      */ import javax.swing.TransferHandler;
/*      */ import javax.swing.UIManager;
/*      */ import javax.swing.event.ChangeEvent;
/*      */ import javax.swing.event.ChangeListener;
/*      */ import javax.swing.event.DocumentEvent;
/*      */ import javax.swing.event.DocumentListener;
/*      */ import javax.swing.event.EventListenerList;
/*      */ import javax.swing.plaf.TextUI;
/*      */ import sun.swing.SwingUtilities2;
/*      */ 
/*      */ public class DefaultCaret extends Rectangle
/*      */   implements Caret, FocusListener, MouseListener, MouseMotionListener
/*      */ {
/*      */   public static final int UPDATE_WHEN_ON_EDT = 0;
/*      */   public static final int NEVER_UPDATE = 1;
/*      */   public static final int ALWAYS_UPDATE = 2;
/* 1548 */   protected EventListenerList listenerList = new EventListenerList();
/*      */ 
/* 1556 */   protected transient ChangeEvent changeEvent = null;
/*      */   JTextComponent component;
/* 1562 */   int updatePolicy = 0;
/*      */   boolean visible;
/*      */   boolean active;
/*      */   int dot;
/*      */   int mark;
/*      */   Object selectionTag;
/*      */   boolean selectionVisible;
/*      */   Timer flasher;
/*      */   Point magicCaretPosition;
/*      */   transient Position.Bias dotBias;
/*      */   transient Position.Bias markBias;
/*      */   boolean dotLTR;
/*      */   boolean markLTR;
/* 1575 */   transient Handler handler = new Handler();
/* 1576 */   private transient int[] flagXPoints = new int[3];
/* 1577 */   private transient int[] flagYPoints = new int[3];
/*      */   private transient NavigationFilter.FilterBypass filterBypass;
/* 1579 */   private static transient Action selectWord = null;
/* 1580 */   private static transient Action selectLine = null;
/*      */   private boolean ownsSelection;
/*      */   private boolean forceCaretPositionChange;
/*      */   private transient boolean shouldHandleRelease;
/* 1607 */   private transient MouseEvent selectedWordEvent = null;
/*      */ 
/* 1612 */   private int caretWidth = -1;
/* 1613 */   private float aspectRatio = -1.0F;
/*      */ 
/*      */   public void setUpdatePolicy(int paramInt)
/*      */   {
/*  203 */     this.updatePolicy = paramInt;
/*      */   }
/*      */ 
/*      */   public int getUpdatePolicy()
/*      */   {
/*  220 */     return this.updatePolicy;
/*      */   }
/*      */ 
/*      */   protected final JTextComponent getComponent()
/*      */   {
/*  230 */     return this.component;
/*      */   }
/*      */ 
/*      */   protected final synchronized void repaint()
/*      */   {
/*  244 */     if (this.component != null)
/*  245 */       this.component.repaint(this.x, this.y, this.width, this.height);
/*      */   }
/*      */ 
/*      */   protected synchronized void damage(Rectangle paramRectangle)
/*      */   {
/*  260 */     if (paramRectangle != null) {
/*  261 */       int i = getCaretWidth(paramRectangle.height);
/*  262 */       this.x = (paramRectangle.x - 4 - (i >> 1));
/*  263 */       this.y = paramRectangle.y;
/*  264 */       this.width = (9 + i);
/*  265 */       this.height = paramRectangle.height;
/*  266 */       repaint();
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void adjustVisibility(Rectangle paramRectangle)
/*      */   {
/*  281 */     if (this.component == null) {
/*  282 */       return;
/*      */     }
/*  284 */     if (SwingUtilities.isEventDispatchThread())
/*  285 */       this.component.scrollRectToVisible(paramRectangle);
/*      */     else
/*  287 */       SwingUtilities.invokeLater(new SafeScroller(paramRectangle));
/*      */   }
/*      */ 
/*      */   protected Highlighter.HighlightPainter getSelectionPainter()
/*      */   {
/*  297 */     return DefaultHighlighter.DefaultPainter;
/*      */   }
/*      */ 
/*      */   protected void positionCaret(MouseEvent paramMouseEvent)
/*      */   {
/*  307 */     Point localPoint = new Point(paramMouseEvent.getX(), paramMouseEvent.getY());
/*  308 */     Position.Bias[] arrayOfBias = new Position.Bias[1];
/*  309 */     int i = this.component.getUI().viewToModel(this.component, localPoint, arrayOfBias);
/*  310 */     if (arrayOfBias[0] == null)
/*  311 */       arrayOfBias[0] = Position.Bias.Forward;
/*  312 */     if (i >= 0)
/*  313 */       setDot(i, arrayOfBias[0]);
/*      */   }
/*      */ 
/*      */   protected void moveCaret(MouseEvent paramMouseEvent)
/*      */   {
/*  326 */     Point localPoint = new Point(paramMouseEvent.getX(), paramMouseEvent.getY());
/*  327 */     Position.Bias[] arrayOfBias = new Position.Bias[1];
/*  328 */     int i = this.component.getUI().viewToModel(this.component, localPoint, arrayOfBias);
/*  329 */     if (arrayOfBias[0] == null)
/*  330 */       arrayOfBias[0] = Position.Bias.Forward;
/*  331 */     if (i >= 0)
/*  332 */       moveDot(i, arrayOfBias[0]);
/*      */   }
/*      */ 
/*      */   public void focusGained(FocusEvent paramFocusEvent)
/*      */   {
/*  347 */     if (this.component.isEnabled()) {
/*  348 */       if (this.component.isEditable()) {
/*  349 */         setVisible(true);
/*      */       }
/*  351 */       setSelectionVisible(true);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void focusLost(FocusEvent paramFocusEvent)
/*      */   {
/*  364 */     setVisible(false);
/*  365 */     setSelectionVisible((this.ownsSelection) || (paramFocusEvent.isTemporary()));
/*      */   }
/*      */ 
/*      */   private void selectWord(MouseEvent paramMouseEvent)
/*      */   {
/*  373 */     if ((this.selectedWordEvent != null) && (this.selectedWordEvent.getX() == paramMouseEvent.getX()) && (this.selectedWordEvent.getY() == paramMouseEvent.getY()))
/*      */     {
/*  377 */       return;
/*      */     }
/*  379 */     Action localAction = null;
/*  380 */     ActionMap localActionMap = getComponent().getActionMap();
/*  381 */     if (localActionMap != null) {
/*  382 */       localAction = localActionMap.get("select-word");
/*      */     }
/*  384 */     if (localAction == null) {
/*  385 */       if (selectWord == null) {
/*  386 */         selectWord = new DefaultEditorKit.SelectWordAction();
/*      */       }
/*  388 */       localAction = selectWord;
/*      */     }
/*  390 */     localAction.actionPerformed(new ActionEvent(getComponent(), 1001, null, paramMouseEvent.getWhen(), paramMouseEvent.getModifiers()));
/*      */ 
/*  392 */     this.selectedWordEvent = paramMouseEvent;
/*      */   }
/*      */ 
/*      */   public void mouseClicked(MouseEvent paramMouseEvent)
/*      */   {
/*  406 */     if (getComponent() == null) {
/*  407 */       return;
/*      */     }
/*      */ 
/*  410 */     int i = SwingUtilities2.getAdjustedClickCount(getComponent(), paramMouseEvent);
/*      */ 
/*  412 */     if (!paramMouseEvent.isConsumed())
/*      */     {
/*      */       Object localObject1;
/*      */       Object localObject2;
/*  413 */       if (SwingUtilities.isLeftMouseButton(paramMouseEvent))
/*      */       {
/*  415 */         if (i == 1) {
/*  416 */           this.selectedWordEvent = null;
/*  417 */         } else if ((i == 2) && (SwingUtilities2.canEventAccessSystemClipboard(paramMouseEvent)))
/*      */         {
/*  419 */           selectWord(paramMouseEvent);
/*  420 */           this.selectedWordEvent = null;
/*  421 */         } else if ((i == 3) && (SwingUtilities2.canEventAccessSystemClipboard(paramMouseEvent)))
/*      */         {
/*  423 */           localObject1 = null;
/*  424 */           localObject2 = getComponent().getActionMap();
/*  425 */           if (localObject2 != null) {
/*  426 */             localObject1 = ((ActionMap)localObject2).get("select-line");
/*      */           }
/*  428 */           if (localObject1 == null) {
/*  429 */             if (selectLine == null) {
/*  430 */               selectLine = new DefaultEditorKit.SelectLineAction();
/*      */             }
/*  432 */             localObject1 = selectLine;
/*      */           }
/*  434 */           ((Action)localObject1).actionPerformed(new ActionEvent(getComponent(), 1001, null, paramMouseEvent.getWhen(), paramMouseEvent.getModifiers()));
/*      */         }
/*      */       }
/*  437 */       else if (SwingUtilities.isMiddleMouseButton(paramMouseEvent))
/*      */       {
/*  439 */         if ((i == 1) && (this.component.isEditable()) && (this.component.isEnabled()) && (SwingUtilities2.canEventAccessSystemClipboard(paramMouseEvent)))
/*      */         {
/*  442 */           localObject1 = (JTextComponent)paramMouseEvent.getSource();
/*  443 */           if (localObject1 != null)
/*      */             try {
/*  445 */               localObject2 = ((JTextComponent)localObject1).getToolkit();
/*  446 */               Clipboard localClipboard = ((Toolkit)localObject2).getSystemSelection();
/*  447 */               if (localClipboard != null)
/*      */               {
/*  449 */                 adjustCaret(paramMouseEvent);
/*  450 */                 TransferHandler localTransferHandler = ((JTextComponent)localObject1).getTransferHandler();
/*  451 */                 if (localTransferHandler != null) {
/*  452 */                   Transferable localTransferable = null;
/*      */                   try
/*      */                   {
/*  455 */                     localTransferable = localClipboard.getContents(null);
/*      */                   }
/*      */                   catch (IllegalStateException localIllegalStateException) {
/*  458 */                     UIManager.getLookAndFeel().provideErrorFeedback((Component)localObject1);
/*      */                   }
/*      */ 
/*  461 */                   if (localTransferable != null) {
/*  462 */                     localTransferHandler.importData((JComponent)localObject1, localTransferable);
/*      */                   }
/*      */                 }
/*  465 */                 adjustFocus(true);
/*      */               }
/*      */             }
/*      */             catch (HeadlessException localHeadlessException)
/*      */             {
/*      */             }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void mousePressed(MouseEvent paramMouseEvent)
/*      */   {
/*  489 */     int i = SwingUtilities2.getAdjustedClickCount(getComponent(), paramMouseEvent);
/*      */ 
/*  491 */     if (SwingUtilities.isLeftMouseButton(paramMouseEvent))
/*  492 */       if (paramMouseEvent.isConsumed()) {
/*  493 */         this.shouldHandleRelease = true;
/*      */       } else {
/*  495 */         this.shouldHandleRelease = false;
/*  496 */         adjustCaretAndFocus(paramMouseEvent);
/*  497 */         if ((i == 2) && (SwingUtilities2.canEventAccessSystemClipboard(paramMouseEvent)))
/*      */         {
/*  499 */           selectWord(paramMouseEvent);
/*      */         }
/*      */       }
/*      */   }
/*      */ 
/*      */   void adjustCaretAndFocus(MouseEvent paramMouseEvent)
/*      */   {
/*  506 */     adjustCaret(paramMouseEvent);
/*  507 */     adjustFocus(false);
/*      */   }
/*      */ 
/*      */   private void adjustCaret(MouseEvent paramMouseEvent)
/*      */   {
/*  514 */     if (((paramMouseEvent.getModifiers() & 0x1) != 0) && (getDot() != -1))
/*      */     {
/*  516 */       moveCaret(paramMouseEvent);
/*  517 */     } else if (!paramMouseEvent.isPopupTrigger())
/*  518 */       positionCaret(paramMouseEvent);
/*      */   }
/*      */ 
/*      */   private void adjustFocus(boolean paramBoolean)
/*      */   {
/*  528 */     if ((this.component != null) && (this.component.isEnabled()) && (this.component.isRequestFocusEnabled()))
/*      */     {
/*  530 */       if (paramBoolean) {
/*  531 */         this.component.requestFocusInWindow();
/*      */       }
/*      */       else
/*  534 */         this.component.requestFocus();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void mouseReleased(MouseEvent paramMouseEvent)
/*      */   {
/*  546 */     if ((!paramMouseEvent.isConsumed()) && (this.shouldHandleRelease) && (SwingUtilities.isLeftMouseButton(paramMouseEvent)))
/*      */     {
/*  550 */       adjustCaretAndFocus(paramMouseEvent);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void mouseEntered(MouseEvent paramMouseEvent)
/*      */   {
/*      */   }
/*      */ 
/*      */   public void mouseExited(MouseEvent paramMouseEvent)
/*      */   {
/*      */   }
/*      */ 
/*      */   public void mouseDragged(MouseEvent paramMouseEvent)
/*      */   {
/*  585 */     if ((!paramMouseEvent.isConsumed()) && (SwingUtilities.isLeftMouseButton(paramMouseEvent)))
/*  586 */       moveCaret(paramMouseEvent);
/*      */   }
/*      */ 
/*      */   public void mouseMoved(MouseEvent paramMouseEvent)
/*      */   {
/*      */   }
/*      */ 
/*      */   public void paint(Graphics paramGraphics)
/*      */   {
/*  618 */     if (isVisible())
/*      */       try {
/*  620 */         TextUI localTextUI = this.component.getUI();
/*  621 */         Rectangle localRectangle1 = localTextUI.modelToView(this.component, this.dot, this.dotBias);
/*      */ 
/*  623 */         if ((localRectangle1 == null) || ((localRectangle1.width == 0) && (localRectangle1.height == 0))) {
/*  624 */           return;
/*      */         }
/*  626 */         if ((this.width > 0) && (this.height > 0) && (!_contains(localRectangle1.x, localRectangle1.y, localRectangle1.width, localRectangle1.height)))
/*      */         {
/*  630 */           Rectangle localRectangle2 = paramGraphics.getClipBounds();
/*      */ 
/*  632 */           if ((localRectangle2 != null) && (!localRectangle2.contains(this)))
/*      */           {
/*  635 */             repaint();
/*      */           }
/*      */ 
/*  640 */           damage(localRectangle1);
/*      */         }
/*  642 */         paramGraphics.setColor(this.component.getCaretColor());
/*  643 */         int i = getCaretWidth(localRectangle1.height);
/*  644 */         localRectangle1.x -= (i >> 1);
/*  645 */         paramGraphics.fillRect(localRectangle1.x, localRectangle1.y, i, localRectangle1.height);
/*      */ 
/*  652 */         Document localDocument = this.component.getDocument();
/*  653 */         if ((localDocument instanceof AbstractDocument)) {
/*  654 */           Element localElement = ((AbstractDocument)localDocument).getBidiRootElement();
/*  655 */           if ((localElement != null) && (localElement.getElementCount() > 1))
/*      */           {
/*  657 */             this.flagXPoints[0] = (localRectangle1.x + (this.dotLTR ? i : 0));
/*  658 */             this.flagYPoints[0] = localRectangle1.y;
/*  659 */             this.flagXPoints[1] = this.flagXPoints[0];
/*  660 */             this.flagYPoints[1] = (this.flagYPoints[0] + 4);
/*  661 */             this.flagXPoints[2] = (this.flagXPoints[0] + (this.dotLTR ? 4 : -4));
/*  662 */             this.flagYPoints[2] = this.flagYPoints[0];
/*  663 */             paramGraphics.fillPolygon(this.flagXPoints, this.flagYPoints, 3);
/*      */           }
/*      */         }
/*      */       }
/*      */       catch (BadLocationException localBadLocationException)
/*      */       {
/*      */       }
/*      */   }
/*      */ 
/*      */   public void install(JTextComponent paramJTextComponent)
/*      */   {
/*  685 */     this.component = paramJTextComponent;
/*  686 */     Document localDocument = paramJTextComponent.getDocument();
/*  687 */     this.dot = (this.mark = 0);
/*  688 */     this.dotLTR = (this.markLTR = 1);
/*  689 */     this.dotBias = (this.markBias = Position.Bias.Forward);
/*  690 */     if (localDocument != null) {
/*  691 */       localDocument.addDocumentListener(this.handler);
/*      */     }
/*  693 */     paramJTextComponent.addPropertyChangeListener(this.handler);
/*  694 */     paramJTextComponent.addFocusListener(this);
/*  695 */     paramJTextComponent.addMouseListener(this);
/*  696 */     paramJTextComponent.addMouseMotionListener(this);
/*      */ 
/*  700 */     if (this.component.hasFocus()) {
/*  701 */       focusGained(null);
/*      */     }
/*      */ 
/*  704 */     Number localNumber = (Number)paramJTextComponent.getClientProperty("caretAspectRatio");
/*  705 */     if (localNumber != null)
/*  706 */       this.aspectRatio = localNumber.floatValue();
/*      */     else {
/*  708 */       this.aspectRatio = -1.0F;
/*      */     }
/*      */ 
/*  711 */     Integer localInteger = (Integer)paramJTextComponent.getClientProperty("caretWidth");
/*  712 */     if (localInteger != null)
/*  713 */       this.caretWidth = localInteger.intValue();
/*      */     else
/*  715 */       this.caretWidth = -1;
/*      */   }
/*      */ 
/*      */   public void deinstall(JTextComponent paramJTextComponent)
/*      */   {
/*  728 */     paramJTextComponent.removeMouseListener(this);
/*  729 */     paramJTextComponent.removeMouseMotionListener(this);
/*  730 */     paramJTextComponent.removeFocusListener(this);
/*  731 */     paramJTextComponent.removePropertyChangeListener(this.handler);
/*  732 */     Document localDocument = paramJTextComponent.getDocument();
/*  733 */     if (localDocument != null) {
/*  734 */       localDocument.removeDocumentListener(this.handler);
/*      */     }
/*  736 */     synchronized (this) {
/*  737 */       this.component = null;
/*      */     }
/*  739 */     if (this.flasher != null)
/*  740 */       this.flasher.stop();
/*      */   }
/*      */ 
/*      */   public void addChangeListener(ChangeListener paramChangeListener)
/*      */   {
/*  754 */     this.listenerList.add(ChangeListener.class, paramChangeListener);
/*      */   }
/*      */ 
/*      */   public void removeChangeListener(ChangeListener paramChangeListener)
/*      */   {
/*  764 */     this.listenerList.remove(ChangeListener.class, paramChangeListener);
/*      */   }
/*      */ 
/*      */   public ChangeListener[] getChangeListeners()
/*      */   {
/*  781 */     return (ChangeListener[])this.listenerList.getListeners(ChangeListener.class);
/*      */   }
/*      */ 
/*      */   protected void fireStateChanged()
/*      */   {
/*  794 */     Object[] arrayOfObject = this.listenerList.getListenerList();
/*      */ 
/*  797 */     for (int i = arrayOfObject.length - 2; i >= 0; i -= 2)
/*  798 */       if (arrayOfObject[i] == ChangeListener.class)
/*      */       {
/*  800 */         if (this.changeEvent == null)
/*  801 */           this.changeEvent = new ChangeEvent(this);
/*  802 */         ((ChangeListener)arrayOfObject[(i + 1)]).stateChanged(this.changeEvent);
/*      */       }
/*      */   }
/*      */ 
/*      */   public <T extends EventListener> T[] getListeners(Class<T> paramClass)
/*      */   {
/*  844 */     return this.listenerList.getListeners(paramClass);
/*      */   }
/*      */ 
/*      */   public void setSelectionVisible(boolean paramBoolean)
/*      */   {
/*  853 */     if (paramBoolean != this.selectionVisible) {
/*  854 */       this.selectionVisible = paramBoolean;
/*      */       Highlighter localHighlighter;
/*  855 */       if (this.selectionVisible)
/*      */       {
/*  857 */         localHighlighter = this.component.getHighlighter();
/*  858 */         if ((this.dot != this.mark) && (localHighlighter != null) && (this.selectionTag == null)) {
/*  859 */           int i = Math.min(this.dot, this.mark);
/*  860 */           int j = Math.max(this.dot, this.mark);
/*  861 */           Highlighter.HighlightPainter localHighlightPainter = getSelectionPainter();
/*      */           try {
/*  863 */             this.selectionTag = localHighlighter.addHighlight(i, j, localHighlightPainter);
/*      */           } catch (BadLocationException localBadLocationException) {
/*  865 */             this.selectionTag = null;
/*      */           }
/*      */         }
/*      */ 
/*      */       }
/*  870 */       else if (this.selectionTag != null) {
/*  871 */         localHighlighter = this.component.getHighlighter();
/*  872 */         localHighlighter.removeHighlight(this.selectionTag);
/*  873 */         this.selectionTag = null;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean isSelectionVisible()
/*      */   {
/*  885 */     return this.selectionVisible;
/*      */   }
/*      */ 
/*      */   public boolean isActive()
/*      */   {
/*  903 */     return this.active;
/*      */   }
/*      */ 
/*      */   public boolean isVisible()
/*      */   {
/*  924 */     return this.visible;
/*      */   }
/*      */ 
/*      */   public void setVisible(boolean paramBoolean)
/*      */   {
/*  965 */     if (this.component != null) {
/*  966 */       this.active = paramBoolean;
/*  967 */       TextUI localTextUI = this.component.getUI();
/*  968 */       if (this.visible != paramBoolean) {
/*  969 */         this.visible = paramBoolean;
/*      */         try
/*      */         {
/*  972 */           Rectangle localRectangle = localTextUI.modelToView(this.component, this.dot, this.dotBias);
/*  973 */           damage(localRectangle);
/*      */         }
/*      */         catch (BadLocationException localBadLocationException) {
/*      */         }
/*      */       }
/*      */     }
/*  979 */     if (this.flasher != null)
/*  980 */       if (this.visible)
/*  981 */         this.flasher.start();
/*      */       else
/*  983 */         this.flasher.stop();
/*      */   }
/*      */ 
/*      */   public void setBlinkRate(int paramInt)
/*      */   {
/*  995 */     if (paramInt != 0) {
/*  996 */       if (this.flasher == null) {
/*  997 */         this.flasher = new Timer(paramInt, this.handler);
/*      */       }
/*  999 */       this.flasher.setDelay(paramInt);
/*      */     }
/* 1001 */     else if (this.flasher != null) {
/* 1002 */       this.flasher.stop();
/* 1003 */       this.flasher.removeActionListener(this.handler);
/* 1004 */       this.flasher = null;
/*      */     }
/*      */   }
/*      */ 
/*      */   public int getBlinkRate()
/*      */   {
/* 1017 */     return this.flasher == null ? 0 : this.flasher.getDelay();
/*      */   }
/*      */ 
/*      */   public int getDot()
/*      */   {
/* 1027 */     return this.dot;
/*      */   }
/*      */ 
/*      */   public int getMark()
/*      */   {
/* 1038 */     return this.mark;
/*      */   }
/*      */ 
/*      */   public void setDot(int paramInt)
/*      */   {
/* 1051 */     setDot(paramInt, Position.Bias.Forward);
/*      */   }
/*      */ 
/*      */   public void moveDot(int paramInt)
/*      */   {
/* 1063 */     moveDot(paramInt, Position.Bias.Forward);
/*      */   }
/*      */ 
/*      */   public void moveDot(int paramInt, Position.Bias paramBias)
/*      */   {
/* 1079 */     if (paramBias == null) {
/* 1080 */       throw new IllegalArgumentException("null bias");
/*      */     }
/*      */ 
/* 1083 */     if (!this.component.isEnabled())
/*      */     {
/* 1085 */       setDot(paramInt, paramBias);
/* 1086 */       return;
/*      */     }
/* 1088 */     if (paramInt != this.dot) {
/* 1089 */       NavigationFilter localNavigationFilter = this.component.getNavigationFilter();
/*      */ 
/* 1091 */       if (localNavigationFilter != null) {
/* 1092 */         localNavigationFilter.moveDot(getFilterBypass(), paramInt, paramBias);
/*      */       }
/*      */       else
/* 1095 */         handleMoveDot(paramInt, paramBias);
/*      */     }
/*      */   }
/*      */ 
/*      */   void handleMoveDot(int paramInt, Position.Bias paramBias)
/*      */   {
/* 1101 */     changeCaretPosition(paramInt, paramBias);
/*      */ 
/* 1103 */     if (this.selectionVisible) {
/* 1104 */       Highlighter localHighlighter = this.component.getHighlighter();
/* 1105 */       if (localHighlighter != null) {
/* 1106 */         int i = Math.min(paramInt, this.mark);
/* 1107 */         int j = Math.max(paramInt, this.mark);
/*      */ 
/* 1110 */         if (i == j) {
/* 1111 */           if (this.selectionTag != null) {
/* 1112 */             localHighlighter.removeHighlight(this.selectionTag);
/* 1113 */             this.selectionTag = null;
/*      */           }
/*      */         }
/*      */         else
/*      */           try {
/* 1118 */             if (this.selectionTag != null) {
/* 1119 */               localHighlighter.changeHighlight(this.selectionTag, i, j);
/*      */             } else {
/* 1121 */               Highlighter.HighlightPainter localHighlightPainter = getSelectionPainter();
/* 1122 */               this.selectionTag = localHighlighter.addHighlight(i, j, localHighlightPainter);
/*      */             }
/*      */           } catch (BadLocationException localBadLocationException) {
/* 1125 */             throw new StateInvariantError("Bad caret position");
/*      */           }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setDot(int paramInt, Position.Bias paramBias)
/*      */   {
/* 1144 */     if (paramBias == null) {
/* 1145 */       throw new IllegalArgumentException("null bias");
/*      */     }
/*      */ 
/* 1148 */     NavigationFilter localNavigationFilter = this.component.getNavigationFilter();
/*      */ 
/* 1150 */     if (localNavigationFilter != null) {
/* 1151 */       localNavigationFilter.setDot(getFilterBypass(), paramInt, paramBias);
/*      */     }
/*      */     else
/* 1154 */       handleSetDot(paramInt, paramBias);
/*      */   }
/*      */ 
/*      */   void handleSetDot(int paramInt, Position.Bias paramBias)
/*      */   {
/* 1160 */     Document localDocument = this.component.getDocument();
/* 1161 */     if (localDocument != null) {
/* 1162 */       paramInt = Math.min(paramInt, localDocument.getLength());
/*      */     }
/* 1164 */     paramInt = Math.max(paramInt, 0);
/*      */ 
/* 1167 */     if (paramInt == 0) {
/* 1168 */       paramBias = Position.Bias.Forward;
/*      */     }
/* 1170 */     this.mark = paramInt;
/* 1171 */     if ((this.dot != paramInt) || (this.dotBias != paramBias) || (this.selectionTag != null) || (this.forceCaretPositionChange))
/*      */     {
/* 1173 */       changeCaretPosition(paramInt, paramBias);
/*      */     }
/* 1175 */     this.markBias = this.dotBias;
/* 1176 */     this.markLTR = this.dotLTR;
/* 1177 */     Highlighter localHighlighter = this.component.getHighlighter();
/* 1178 */     if ((localHighlighter != null) && (this.selectionTag != null)) {
/* 1179 */       localHighlighter.removeHighlight(this.selectionTag);
/* 1180 */       this.selectionTag = null;
/*      */     }
/*      */   }
/*      */ 
/*      */   public Position.Bias getDotBias()
/*      */   {
/* 1191 */     return this.dotBias;
/*      */   }
/*      */ 
/*      */   public Position.Bias getMarkBias()
/*      */   {
/* 1201 */     return this.markBias;
/*      */   }
/*      */ 
/*      */   boolean isDotLeftToRight() {
/* 1205 */     return this.dotLTR;
/*      */   }
/*      */ 
/*      */   boolean isMarkLeftToRight() {
/* 1209 */     return this.markLTR;
/*      */   }
/*      */ 
/*      */   boolean isPositionLTR(int paramInt, Position.Bias paramBias) {
/* 1213 */     Document localDocument = this.component.getDocument();
/* 1214 */     if ((localDocument instanceof AbstractDocument)) {
/* 1215 */       if (paramBias == Position.Bias.Backward) { paramInt--; if (paramInt < 0)
/* 1216 */           paramInt = 0; }
/* 1217 */       return ((AbstractDocument)localDocument).isLeftToRight(paramInt, paramInt);
/*      */     }
/* 1219 */     return true;
/*      */   }
/*      */ 
/*      */   Position.Bias guessBiasForOffset(int paramInt, Position.Bias paramBias, boolean paramBoolean)
/*      */   {
/* 1233 */     if (paramBoolean != isPositionLTR(paramInt, paramBias)) {
/* 1234 */       paramBias = Position.Bias.Backward;
/*      */     }
/* 1236 */     else if ((paramBias != Position.Bias.Backward) && (paramBoolean != isPositionLTR(paramInt, Position.Bias.Backward)))
/*      */     {
/* 1238 */       paramBias = Position.Bias.Backward;
/*      */     }
/* 1240 */     if ((paramBias == Position.Bias.Backward) && (paramInt > 0))
/*      */       try {
/* 1242 */         Segment localSegment = new Segment();
/* 1243 */         this.component.getDocument().getText(paramInt - 1, 1, localSegment);
/* 1244 */         if ((localSegment.count > 0) && (localSegment.array[localSegment.offset] == '\n'))
/* 1245 */           paramBias = Position.Bias.Forward;
/*      */       }
/*      */       catch (BadLocationException localBadLocationException)
/*      */       {
/*      */       }
/* 1250 */     return paramBias;
/*      */   }
/*      */ 
/*      */   void changeCaretPosition(int paramInt, Position.Bias paramBias)
/*      */   {
/* 1264 */     repaint();
/*      */ 
/* 1268 */     if ((this.flasher != null) && (this.flasher.isRunning())) {
/* 1269 */       this.visible = true;
/* 1270 */       this.flasher.restart();
/*      */     }
/*      */ 
/* 1274 */     this.dot = paramInt;
/* 1275 */     this.dotBias = paramBias;
/* 1276 */     this.dotLTR = isPositionLTR(paramInt, paramBias);
/* 1277 */     fireStateChanged();
/*      */ 
/* 1279 */     updateSystemSelection();
/*      */ 
/* 1281 */     setMagicCaretPosition(null);
/*      */ 
/* 1288 */     Runnable local1 = new Runnable() {
/*      */       public void run() {
/* 1290 */         DefaultCaret.this.repaintNewCaret();
/*      */       }
/*      */     };
/* 1293 */     SwingUtilities.invokeLater(local1);
/*      */   }
/*      */ 
/*      */   void repaintNewCaret()
/*      */   {
/* 1303 */     if (this.component != null) {
/* 1304 */       TextUI localTextUI = this.component.getUI();
/* 1305 */       Document localDocument = this.component.getDocument();
/* 1306 */       if ((localTextUI != null) && (localDocument != null))
/*      */       {
/*      */         Rectangle localRectangle;
/*      */         try
/*      */         {
/* 1311 */           localRectangle = localTextUI.modelToView(this.component, this.dot, this.dotBias);
/*      */         } catch (BadLocationException localBadLocationException) {
/* 1313 */           localRectangle = null;
/*      */         }
/* 1315 */         if (localRectangle != null) {
/* 1316 */           adjustVisibility(localRectangle);
/*      */ 
/* 1318 */           if (getMagicCaretPosition() == null) {
/* 1319 */             setMagicCaretPosition(new Point(localRectangle.x, localRectangle.y));
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/* 1324 */         damage(localRectangle);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void updateSystemSelection() {
/* 1330 */     if (!SwingUtilities2.canCurrentEventAccessSystemClipboard()) {
/* 1331 */       return;
/*      */     }
/* 1333 */     if ((this.dot != this.mark) && (this.component != null) && (this.component.hasFocus())) {
/* 1334 */       Clipboard localClipboard = getSystemSelection();
/* 1335 */       if (localClipboard != null)
/*      */       {
/*      */         String str;
/* 1337 */         if (((this.component instanceof JPasswordField)) && (this.component.getClientProperty("JPasswordField.cutCopyAllowed") != Boolean.TRUE))
/*      */         {
/* 1341 */           StringBuilder localStringBuilder = null;
/* 1342 */           char c = ((JPasswordField)this.component).getEchoChar();
/* 1343 */           int i = Math.min(getDot(), getMark());
/* 1344 */           int j = Math.max(getDot(), getMark());
/* 1345 */           for (int k = i; k < j; k++) {
/* 1346 */             if (localStringBuilder == null) {
/* 1347 */               localStringBuilder = new StringBuilder();
/*      */             }
/* 1349 */             localStringBuilder.append(c);
/*      */           }
/* 1351 */           str = localStringBuilder != null ? localStringBuilder.toString() : null;
/*      */         } else {
/* 1353 */           str = this.component.getSelectedText();
/*      */         }
/*      */         try {
/* 1356 */           localClipboard.setContents(new StringSelection(str), getClipboardOwner());
/*      */ 
/* 1359 */           this.ownsSelection = true;
/*      */         }
/*      */         catch (IllegalStateException localIllegalStateException)
/*      */         {
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private Clipboard getSystemSelection()
/*      */   {
/*      */     try {
/* 1371 */       return this.component.getToolkit().getSystemSelection();
/*      */     }
/*      */     catch (HeadlessException localHeadlessException) {
/*      */     }
/*      */     catch (SecurityException localSecurityException) {
/*      */     }
/* 1377 */     return null;
/*      */   }
/*      */ 
/*      */   private ClipboardOwner getClipboardOwner() {
/* 1381 */     return this.handler;
/*      */   }
/*      */ 
/*      */   private void ensureValidPosition()
/*      */   {
/* 1391 */     int i = this.component.getDocument().getLength();
/* 1392 */     if ((this.dot > i) || (this.mark > i))
/*      */     {
/* 1396 */       handleSetDot(i, Position.Bias.Forward);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setMagicCaretPosition(Point paramPoint)
/*      */   {
/* 1410 */     this.magicCaretPosition = paramPoint;
/*      */   }
/*      */ 
/*      */   public Point getMagicCaretPosition()
/*      */   {
/* 1420 */     return this.magicCaretPosition;
/*      */   }
/*      */ 
/*      */   public boolean equals(Object paramObject)
/*      */   {
/* 1434 */     return this == paramObject;
/*      */   }
/*      */ 
/*      */   public String toString() {
/* 1438 */     String str = "Dot=(" + this.dot + ", " + this.dotBias + ")";
/* 1439 */     str = str + " Mark=(" + this.mark + ", " + this.markBias + ")";
/* 1440 */     return str;
/*      */   }
/*      */ 
/*      */   private NavigationFilter.FilterBypass getFilterBypass() {
/* 1444 */     if (this.filterBypass == null) {
/* 1445 */       this.filterBypass = new DefaultFilterBypass(null);
/*      */     }
/* 1447 */     return this.filterBypass;
/*      */   }
/*      */ 
/*      */   private boolean _contains(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/* 1453 */     int i = this.width;
/* 1454 */     int j = this.height;
/* 1455 */     if ((i | j | paramInt3 | paramInt4) < 0)
/*      */     {
/* 1457 */       return false;
/*      */     }
/*      */ 
/* 1460 */     int k = this.x;
/* 1461 */     int m = this.y;
/* 1462 */     if ((paramInt1 < k) || (paramInt2 < m)) {
/* 1463 */       return false;
/*      */     }
/* 1465 */     if (paramInt3 > 0) {
/* 1466 */       i += k;
/* 1467 */       paramInt3 += paramInt1;
/* 1468 */       if (paramInt3 <= paramInt1)
/*      */       {
/* 1473 */         if ((i >= k) || (paramInt3 > i)) return false;
/*      */ 
/*      */       }
/* 1478 */       else if ((i >= k) && (paramInt3 > i)) return false;
/*      */ 
/*      */     }
/* 1481 */     else if (k + i < paramInt1) {
/* 1482 */       return false;
/*      */     }
/* 1484 */     if (paramInt4 > 0) {
/* 1485 */       j += m;
/* 1486 */       paramInt4 += paramInt2;
/* 1487 */       if (paramInt4 <= paramInt2) {
/* 1488 */         if ((j >= m) || (paramInt4 > j)) return false;
/*      */       }
/* 1490 */       else if ((j >= m) && (paramInt4 > j)) return false;
/*      */ 
/*      */     }
/* 1493 */     else if (m + j < paramInt2) {
/* 1494 */       return false;
/*      */     }
/* 1496 */     return true;
/*      */   }
/*      */ 
/*      */   int getCaretWidth(int paramInt) {
/* 1500 */     if (this.aspectRatio > -1.0F) {
/* 1501 */       return (int)(this.aspectRatio * paramInt) + 1;
/*      */     }
/*      */ 
/* 1504 */     if (this.caretWidth > -1) {
/* 1505 */       return this.caretWidth;
/*      */     }
/* 1507 */     Object localObject = UIManager.get("Caret.width");
/* 1508 */     if ((localObject instanceof Integer)) {
/* 1509 */       return ((Integer)localObject).intValue();
/*      */     }
/* 1511 */     return 1;
/*      */   }
/*      */ 
/*      */   private void readObject(ObjectInputStream paramObjectInputStream)
/*      */     throws ClassNotFoundException, IOException
/*      */   {
/* 1521 */     paramObjectInputStream.defaultReadObject();
/* 1522 */     this.handler = new Handler();
/* 1523 */     if (!paramObjectInputStream.readBoolean()) {
/* 1524 */       this.dotBias = Position.Bias.Forward;
/*      */     }
/*      */     else {
/* 1527 */       this.dotBias = Position.Bias.Backward;
/*      */     }
/* 1529 */     if (!paramObjectInputStream.readBoolean()) {
/* 1530 */       this.markBias = Position.Bias.Forward;
/*      */     }
/*      */     else
/* 1533 */       this.markBias = Position.Bias.Backward;
/*      */   }
/*      */ 
/*      */   private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException
/*      */   {
/* 1538 */     paramObjectOutputStream.defaultWriteObject();
/* 1539 */     paramObjectOutputStream.writeBoolean(this.dotBias == Position.Bias.Backward);
/* 1540 */     paramObjectOutputStream.writeBoolean(this.markBias == Position.Bias.Backward);
/*      */   }
/*      */ 
/*      */   private class DefaultFilterBypass extends NavigationFilter.FilterBypass
/*      */   {
/*      */     private DefaultFilterBypass()
/*      */     {
/*      */     }
/*      */ 
/*      */     public Caret getCaret()
/*      */     {
/* 1912 */       return DefaultCaret.this;
/*      */     }
/*      */ 
/*      */     public void setDot(int paramInt, Position.Bias paramBias) {
/* 1916 */       DefaultCaret.this.handleSetDot(paramInt, paramBias);
/*      */     }
/*      */ 
/*      */     public void moveDot(int paramInt, Position.Bias paramBias) {
/* 1920 */       DefaultCaret.this.handleMoveDot(paramInt, paramBias);
/*      */     }
/*      */   }
/*      */ 
/*      */   class Handler
/*      */     implements PropertyChangeListener, DocumentListener, ActionListener, ClipboardOwner
/*      */   {
/*      */     Handler()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void actionPerformed(ActionEvent paramActionEvent)
/*      */     {
/* 1643 */       if ((DefaultCaret.this.width == 0) || (DefaultCaret.this.height == 0))
/*      */       {
/* 1646 */         if (DefaultCaret.this.component != null) {
/* 1647 */           TextUI localTextUI = DefaultCaret.this.component.getUI();
/*      */           try {
/* 1649 */             Rectangle localRectangle = localTextUI.modelToView(DefaultCaret.this.component, DefaultCaret.this.dot, DefaultCaret.this.dotBias);
/*      */ 
/* 1651 */             if ((localRectangle != null) && (localRectangle.width != 0) && (localRectangle.height != 0))
/* 1652 */               DefaultCaret.this.damage(localRectangle);
/*      */           }
/*      */           catch (BadLocationException localBadLocationException) {
/*      */           }
/*      */         }
/*      */       }
/* 1658 */       DefaultCaret.this.visible = (!DefaultCaret.this.visible);
/* 1659 */       DefaultCaret.this.repaint();
/*      */     }
/*      */ 
/*      */     public void insertUpdate(DocumentEvent paramDocumentEvent)
/*      */     {
/* 1672 */       if ((DefaultCaret.this.getUpdatePolicy() == 1) || ((DefaultCaret.this.getUpdatePolicy() == 0) && (!SwingUtilities.isEventDispatchThread())))
/*      */       {
/* 1676 */         if (((paramDocumentEvent.getOffset() <= DefaultCaret.this.dot) || (paramDocumentEvent.getOffset() <= DefaultCaret.this.mark)) && (DefaultCaret.this.selectionTag != null)) {
/*      */           try
/*      */           {
/* 1679 */             DefaultCaret.this.component.getHighlighter().changeHighlight(DefaultCaret.this.selectionTag, Math.min(DefaultCaret.this.dot, DefaultCaret.this.mark), Math.max(DefaultCaret.this.dot, DefaultCaret.this.mark));
/*      */           }
/*      */           catch (BadLocationException localBadLocationException1) {
/* 1682 */             localBadLocationException1.printStackTrace();
/*      */           }
/*      */         }
/* 1685 */         return;
/*      */       }
/* 1687 */       int i = paramDocumentEvent.getOffset();
/* 1688 */       int j = paramDocumentEvent.getLength();
/* 1689 */       int k = DefaultCaret.this.dot;
/* 1690 */       int m = 0;
/*      */ 
/* 1692 */       if ((paramDocumentEvent instanceof AbstractDocument.UndoRedoDocumentEvent)) {
/* 1693 */         DefaultCaret.this.setDot(i + j);
/* 1694 */         return;
/*      */       }
/* 1696 */       if (k >= i) {
/* 1697 */         k += j;
/* 1698 */         m = (short)(m | 0x1);
/*      */       }
/* 1700 */       int n = DefaultCaret.this.mark;
/* 1701 */       if (n >= i) {
/* 1702 */         n += j;
/* 1703 */         m = (short)(m | 0x2);
/*      */       }
/*      */ 
/* 1706 */       if (m != 0) {
/* 1707 */         Position.Bias localBias = DefaultCaret.this.dotBias;
/* 1708 */         if (DefaultCaret.this.dot == i) { Document localDocument = DefaultCaret.this.component.getDocument();
/*      */           int i1;
/*      */           try {
/* 1712 */             Segment localSegment = new Segment();
/* 1713 */             localDocument.getText(k - 1, 1, localSegment);
/* 1714 */             i1 = (localSegment.count > 0) && (localSegment.array[localSegment.offset] == '\n') ? 1 : 0;
/*      */           }
/*      */           catch (BadLocationException localBadLocationException2) {
/* 1717 */             i1 = 0;
/*      */           }
/* 1719 */           if (i1 != 0)
/* 1720 */             localBias = Position.Bias.Forward;
/*      */           else {
/* 1722 */             localBias = Position.Bias.Backward;
/*      */           }
/*      */         }
/* 1725 */         if (n == k) {
/* 1726 */           DefaultCaret.this.setDot(k, localBias);
/* 1727 */           DefaultCaret.this.ensureValidPosition();
/*      */         }
/*      */         else {
/* 1730 */           DefaultCaret.this.setDot(n, DefaultCaret.this.markBias);
/* 1731 */           if (DefaultCaret.this.getDot() == n)
/*      */           {
/* 1735 */             DefaultCaret.this.moveDot(k, localBias);
/*      */           }
/* 1737 */           DefaultCaret.this.ensureValidPosition();
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     public void removeUpdate(DocumentEvent paramDocumentEvent)
/*      */     {
/* 1750 */       if ((DefaultCaret.this.getUpdatePolicy() == 1) || ((DefaultCaret.this.getUpdatePolicy() == 0) && (!SwingUtilities.isEventDispatchThread())))
/*      */       {
/* 1754 */         i = DefaultCaret.this.component.getDocument().getLength();
/* 1755 */         DefaultCaret.this.dot = Math.min(DefaultCaret.this.dot, i);
/* 1756 */         DefaultCaret.this.mark = Math.min(DefaultCaret.this.mark, i);
/* 1757 */         if (((paramDocumentEvent.getOffset() < DefaultCaret.this.dot) || (paramDocumentEvent.getOffset() < DefaultCaret.this.mark)) && (DefaultCaret.this.selectionTag != null)) {
/*      */           try
/*      */           {
/* 1760 */             DefaultCaret.this.component.getHighlighter().changeHighlight(DefaultCaret.this.selectionTag, Math.min(DefaultCaret.this.dot, DefaultCaret.this.mark), Math.max(DefaultCaret.this.dot, DefaultCaret.this.mark));
/*      */           }
/*      */           catch (BadLocationException localBadLocationException) {
/* 1763 */             localBadLocationException.printStackTrace();
/*      */           }
/*      */         }
/* 1766 */         return;
/*      */       }
/* 1768 */       int i = paramDocumentEvent.getOffset();
/* 1769 */       int j = i + paramDocumentEvent.getLength();
/* 1770 */       int k = DefaultCaret.this.dot;
/* 1771 */       int m = 0;
/* 1772 */       int n = DefaultCaret.this.mark;
/* 1773 */       int i1 = 0;
/*      */ 
/* 1775 */       if ((paramDocumentEvent instanceof AbstractDocument.UndoRedoDocumentEvent)) {
/* 1776 */         DefaultCaret.this.setDot(i);
/* 1777 */         return;
/*      */       }
/* 1779 */       if (k >= j) {
/* 1780 */         k -= j - i;
/* 1781 */         if (k == j)
/* 1782 */           m = 1;
/*      */       }
/* 1784 */       else if (k >= i) {
/* 1785 */         k = i;
/* 1786 */         m = 1;
/*      */       }
/* 1788 */       if (n >= j) {
/* 1789 */         n -= j - i;
/* 1790 */         if (n == j)
/* 1791 */           i1 = 1;
/*      */       }
/* 1793 */       else if (n >= i) {
/* 1794 */         n = i;
/* 1795 */         i1 = 1;
/*      */       }
/* 1797 */       if (n == k) {
/* 1798 */         DefaultCaret.this.forceCaretPositionChange = true;
/*      */         try {
/* 1800 */           DefaultCaret.this.setDot(k, DefaultCaret.this.guessBiasForOffset(k, DefaultCaret.this.dotBias, DefaultCaret.this.dotLTR));
/*      */         }
/*      */         finally {
/* 1803 */           DefaultCaret.this.forceCaretPositionChange = false;
/*      */         }
/* 1805 */         DefaultCaret.this.ensureValidPosition();
/*      */       } else {
/* 1807 */         Position.Bias localBias1 = DefaultCaret.this.dotBias;
/* 1808 */         Position.Bias localBias2 = DefaultCaret.this.markBias;
/* 1809 */         if (m != 0) {
/* 1810 */           localBias1 = DefaultCaret.this.guessBiasForOffset(k, localBias1, DefaultCaret.this.dotLTR);
/*      */         }
/* 1812 */         if (i1 != 0) {
/* 1813 */           localBias2 = DefaultCaret.this.guessBiasForOffset(DefaultCaret.this.mark, localBias2, DefaultCaret.this.markLTR);
/*      */         }
/* 1815 */         DefaultCaret.this.setDot(n, localBias2);
/* 1816 */         if (DefaultCaret.this.getDot() == n)
/*      */         {
/* 1819 */           DefaultCaret.this.moveDot(k, localBias1);
/*      */         }
/* 1821 */         DefaultCaret.this.ensureValidPosition();
/*      */       }
/*      */     }
/*      */ 
/*      */     public void changedUpdate(DocumentEvent paramDocumentEvent)
/*      */     {
/* 1832 */       if ((DefaultCaret.this.getUpdatePolicy() == 1) || ((DefaultCaret.this.getUpdatePolicy() == 0) && (!SwingUtilities.isEventDispatchThread())))
/*      */       {
/* 1835 */         return;
/*      */       }
/* 1837 */       if ((paramDocumentEvent instanceof AbstractDocument.UndoRedoDocumentEvent))
/* 1838 */         DefaultCaret.this.setDot(paramDocumentEvent.getOffset() + paramDocumentEvent.getLength());
/*      */     }
/*      */ 
/*      */     public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent)
/*      */     {
/* 1849 */       Object localObject1 = paramPropertyChangeEvent.getOldValue();
/* 1850 */       Object localObject2 = paramPropertyChangeEvent.getNewValue();
/* 1851 */       if (((localObject1 instanceof Document)) || ((localObject2 instanceof Document))) {
/* 1852 */         DefaultCaret.this.setDot(0);
/* 1853 */         if (localObject1 != null) {
/* 1854 */           ((Document)localObject1).removeDocumentListener(this);
/*      */         }
/* 1856 */         if (localObject2 != null)
/* 1857 */           ((Document)localObject2).addDocumentListener(this);
/*      */       }
/*      */       else
/*      */       {
/*      */         Object localObject3;
/* 1859 */         if ("enabled".equals(paramPropertyChangeEvent.getPropertyName())) {
/* 1860 */           localObject3 = (Boolean)paramPropertyChangeEvent.getNewValue();
/* 1861 */           if (DefaultCaret.this.component.isFocusOwner())
/* 1862 */             if (localObject3 == Boolean.TRUE) {
/* 1863 */               if (DefaultCaret.this.component.isEditable()) {
/* 1864 */                 DefaultCaret.this.setVisible(true);
/*      */               }
/* 1866 */               DefaultCaret.this.setSelectionVisible(true);
/*      */             } else {
/* 1868 */               DefaultCaret.this.setVisible(false);
/* 1869 */               DefaultCaret.this.setSelectionVisible(false);
/*      */             }
/*      */         }
/* 1872 */         else if ("caretWidth".equals(paramPropertyChangeEvent.getPropertyName())) {
/* 1873 */           localObject3 = (Integer)paramPropertyChangeEvent.getNewValue();
/* 1874 */           if (localObject3 != null)
/* 1875 */             DefaultCaret.this.caretWidth = ((Integer)localObject3).intValue();
/*      */           else {
/* 1877 */             DefaultCaret.this.caretWidth = -1;
/*      */           }
/* 1879 */           DefaultCaret.this.repaint();
/* 1880 */         } else if ("caretAspectRatio".equals(paramPropertyChangeEvent.getPropertyName())) {
/* 1881 */           localObject3 = (Number)paramPropertyChangeEvent.getNewValue();
/* 1882 */           if (localObject3 != null)
/* 1883 */             DefaultCaret.this.aspectRatio = ((Number)localObject3).floatValue();
/*      */           else {
/* 1885 */             DefaultCaret.this.aspectRatio = -1.0F;
/*      */           }
/* 1887 */           DefaultCaret.this.repaint();
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     public void lostOwnership(Clipboard paramClipboard, Transferable paramTransferable)
/*      */     {
/* 1900 */       if (DefaultCaret.this.ownsSelection) {
/* 1901 */         DefaultCaret.this.ownsSelection = false;
/* 1902 */         if ((DefaultCaret.this.component != null) && (!DefaultCaret.this.component.hasFocus()))
/* 1903 */           DefaultCaret.this.setSelectionVisible(false);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   class SafeScroller
/*      */     implements Runnable
/*      */   {
/*      */     Rectangle r;
/*      */ 
/*      */     SafeScroller(Rectangle arg2)
/*      */     {
/*      */       Object localObject;
/* 1618 */       this.r = localObject;
/*      */     }
/*      */ 
/*      */     public void run() {
/* 1622 */       if (DefaultCaret.this.component != null)
/* 1623 */         DefaultCaret.this.component.scrollRectToVisible(this.r);
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.text.DefaultCaret
 * JD-Core Version:    0.6.2
 */