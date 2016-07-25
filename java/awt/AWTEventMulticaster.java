/*      */ package java.awt;
/*      */ 
/*      */ import java.awt.event.ActionEvent;
/*      */ import java.awt.event.ActionListener;
/*      */ import java.awt.event.AdjustmentEvent;
/*      */ import java.awt.event.AdjustmentListener;
/*      */ import java.awt.event.ComponentEvent;
/*      */ import java.awt.event.ComponentListener;
/*      */ import java.awt.event.ContainerEvent;
/*      */ import java.awt.event.ContainerListener;
/*      */ import java.awt.event.FocusEvent;
/*      */ import java.awt.event.FocusListener;
/*      */ import java.awt.event.HierarchyBoundsListener;
/*      */ import java.awt.event.HierarchyEvent;
/*      */ import java.awt.event.HierarchyListener;
/*      */ import java.awt.event.InputMethodEvent;
/*      */ import java.awt.event.InputMethodListener;
/*      */ import java.awt.event.ItemEvent;
/*      */ import java.awt.event.ItemListener;
/*      */ import java.awt.event.KeyEvent;
/*      */ import java.awt.event.KeyListener;
/*      */ import java.awt.event.MouseEvent;
/*      */ import java.awt.event.MouseListener;
/*      */ import java.awt.event.MouseMotionListener;
/*      */ import java.awt.event.MouseWheelEvent;
/*      */ import java.awt.event.MouseWheelListener;
/*      */ import java.awt.event.TextEvent;
/*      */ import java.awt.event.TextListener;
/*      */ import java.awt.event.WindowEvent;
/*      */ import java.awt.event.WindowFocusListener;
/*      */ import java.awt.event.WindowListener;
/*      */ import java.awt.event.WindowStateListener;
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.Serializable;
/*      */ import java.lang.reflect.Array;
/*      */ import java.util.EventListener;
/*      */ 
/*      */ public class AWTEventMulticaster
/*      */   implements ComponentListener, ContainerListener, FocusListener, KeyListener, MouseListener, MouseMotionListener, WindowListener, WindowFocusListener, WindowStateListener, ActionListener, ItemListener, AdjustmentListener, TextListener, InputMethodListener, HierarchyListener, HierarchyBoundsListener, MouseWheelListener
/*      */ {
/*      */   protected final EventListener a;
/*      */   protected final EventListener b;
/*      */ 
/*      */   protected AWTEventMulticaster(EventListener paramEventListener1, EventListener paramEventListener2)
/*      */   {
/*  125 */     this.a = paramEventListener1; this.b = paramEventListener2;
/*      */   }
/*      */ 
/*      */   protected EventListener remove(EventListener paramEventListener)
/*      */   {
/*  143 */     if (paramEventListener == this.a) return this.b;
/*  144 */     if (paramEventListener == this.b) return this.a;
/*  145 */     EventListener localEventListener1 = removeInternal(this.a, paramEventListener);
/*  146 */     EventListener localEventListener2 = removeInternal(this.b, paramEventListener);
/*  147 */     if ((localEventListener1 == this.a) && (localEventListener2 == this.b)) {
/*  148 */       return this;
/*      */     }
/*  150 */     return addInternal(localEventListener1, localEventListener2);
/*      */   }
/*      */ 
/*      */   public void componentResized(ComponentEvent paramComponentEvent)
/*      */   {
/*  159 */     ((MouseWheelListener)this.a).componentResized(paramComponentEvent);
/*  160 */     ((MouseWheelListener)this.b).componentResized(paramComponentEvent);
/*      */   }
/*      */ 
/*      */   public void componentMoved(ComponentEvent paramComponentEvent)
/*      */   {
/*  169 */     ((MouseWheelListener)this.a).componentMoved(paramComponentEvent);
/*  170 */     ((MouseWheelListener)this.b).componentMoved(paramComponentEvent);
/*      */   }
/*      */ 
/*      */   public void componentShown(ComponentEvent paramComponentEvent)
/*      */   {
/*  179 */     ((MouseWheelListener)this.a).componentShown(paramComponentEvent);
/*  180 */     ((MouseWheelListener)this.b).componentShown(paramComponentEvent);
/*      */   }
/*      */ 
/*      */   public void componentHidden(ComponentEvent paramComponentEvent)
/*      */   {
/*  189 */     ((MouseWheelListener)this.a).componentHidden(paramComponentEvent);
/*  190 */     ((MouseWheelListener)this.b).componentHidden(paramComponentEvent);
/*      */   }
/*      */ 
/*      */   public void componentAdded(ContainerEvent paramContainerEvent)
/*      */   {
/*  199 */     ((ContainerListener)this.a).componentAdded(paramContainerEvent);
/*  200 */     ((ContainerListener)this.b).componentAdded(paramContainerEvent);
/*      */   }
/*      */ 
/*      */   public void componentRemoved(ContainerEvent paramContainerEvent)
/*      */   {
/*  209 */     ((ContainerListener)this.a).componentRemoved(paramContainerEvent);
/*  210 */     ((ContainerListener)this.b).componentRemoved(paramContainerEvent);
/*      */   }
/*      */ 
/*      */   public void focusGained(FocusEvent paramFocusEvent)
/*      */   {
/*  219 */     ((FocusListener)this.a).focusGained(paramFocusEvent);
/*  220 */     ((FocusListener)this.b).focusGained(paramFocusEvent);
/*      */   }
/*      */ 
/*      */   public void focusLost(FocusEvent paramFocusEvent)
/*      */   {
/*  229 */     ((FocusListener)this.a).focusLost(paramFocusEvent);
/*  230 */     ((FocusListener)this.b).focusLost(paramFocusEvent);
/*      */   }
/*      */ 
/*      */   public void keyTyped(KeyEvent paramKeyEvent)
/*      */   {
/*  239 */     ((KeyListener)this.a).keyTyped(paramKeyEvent);
/*  240 */     ((KeyListener)this.b).keyTyped(paramKeyEvent);
/*      */   }
/*      */ 
/*      */   public void keyPressed(KeyEvent paramKeyEvent)
/*      */   {
/*  249 */     ((KeyListener)this.a).keyPressed(paramKeyEvent);
/*  250 */     ((KeyListener)this.b).keyPressed(paramKeyEvent);
/*      */   }
/*      */ 
/*      */   public void keyReleased(KeyEvent paramKeyEvent)
/*      */   {
/*  259 */     ((KeyListener)this.a).keyReleased(paramKeyEvent);
/*  260 */     ((KeyListener)this.b).keyReleased(paramKeyEvent);
/*      */   }
/*      */ 
/*      */   public void mouseClicked(MouseEvent paramMouseEvent)
/*      */   {
/*  269 */     ((MouseListener)this.a).mouseClicked(paramMouseEvent);
/*  270 */     ((MouseListener)this.b).mouseClicked(paramMouseEvent);
/*      */   }
/*      */ 
/*      */   public void mousePressed(MouseEvent paramMouseEvent)
/*      */   {
/*  279 */     ((MouseListener)this.a).mousePressed(paramMouseEvent);
/*  280 */     ((MouseListener)this.b).mousePressed(paramMouseEvent);
/*      */   }
/*      */ 
/*      */   public void mouseReleased(MouseEvent paramMouseEvent)
/*      */   {
/*  289 */     ((MouseListener)this.a).mouseReleased(paramMouseEvent);
/*  290 */     ((MouseListener)this.b).mouseReleased(paramMouseEvent);
/*      */   }
/*      */ 
/*      */   public void mouseEntered(MouseEvent paramMouseEvent)
/*      */   {
/*  299 */     ((MouseListener)this.a).mouseEntered(paramMouseEvent);
/*  300 */     ((MouseListener)this.b).mouseEntered(paramMouseEvent);
/*      */   }
/*      */ 
/*      */   public void mouseExited(MouseEvent paramMouseEvent)
/*      */   {
/*  309 */     ((MouseListener)this.a).mouseExited(paramMouseEvent);
/*  310 */     ((MouseListener)this.b).mouseExited(paramMouseEvent);
/*      */   }
/*      */ 
/*      */   public void mouseDragged(MouseEvent paramMouseEvent)
/*      */   {
/*  319 */     ((MouseMotionListener)this.a).mouseDragged(paramMouseEvent);
/*  320 */     ((MouseMotionListener)this.b).mouseDragged(paramMouseEvent);
/*      */   }
/*      */ 
/*      */   public void mouseMoved(MouseEvent paramMouseEvent)
/*      */   {
/*  329 */     ((MouseMotionListener)this.a).mouseMoved(paramMouseEvent);
/*  330 */     ((MouseMotionListener)this.b).mouseMoved(paramMouseEvent);
/*      */   }
/*      */ 
/*      */   public void windowOpened(WindowEvent paramWindowEvent)
/*      */   {
/*  339 */     ((WindowListener)this.a).windowOpened(paramWindowEvent);
/*  340 */     ((WindowListener)this.b).windowOpened(paramWindowEvent);
/*      */   }
/*      */ 
/*      */   public void windowClosing(WindowEvent paramWindowEvent)
/*      */   {
/*  349 */     ((WindowListener)this.a).windowClosing(paramWindowEvent);
/*  350 */     ((WindowListener)this.b).windowClosing(paramWindowEvent);
/*      */   }
/*      */ 
/*      */   public void windowClosed(WindowEvent paramWindowEvent)
/*      */   {
/*  359 */     ((WindowListener)this.a).windowClosed(paramWindowEvent);
/*  360 */     ((WindowListener)this.b).windowClosed(paramWindowEvent);
/*      */   }
/*      */ 
/*      */   public void windowIconified(WindowEvent paramWindowEvent)
/*      */   {
/*  369 */     ((WindowListener)this.a).windowIconified(paramWindowEvent);
/*  370 */     ((WindowListener)this.b).windowIconified(paramWindowEvent);
/*      */   }
/*      */ 
/*      */   public void windowDeiconified(WindowEvent paramWindowEvent)
/*      */   {
/*  379 */     ((WindowListener)this.a).windowDeiconified(paramWindowEvent);
/*  380 */     ((WindowListener)this.b).windowDeiconified(paramWindowEvent);
/*      */   }
/*      */ 
/*      */   public void windowActivated(WindowEvent paramWindowEvent)
/*      */   {
/*  389 */     ((WindowListener)this.a).windowActivated(paramWindowEvent);
/*  390 */     ((WindowListener)this.b).windowActivated(paramWindowEvent);
/*      */   }
/*      */ 
/*      */   public void windowDeactivated(WindowEvent paramWindowEvent)
/*      */   {
/*  399 */     ((WindowListener)this.a).windowDeactivated(paramWindowEvent);
/*  400 */     ((WindowListener)this.b).windowDeactivated(paramWindowEvent);
/*      */   }
/*      */ 
/*      */   public void windowStateChanged(WindowEvent paramWindowEvent)
/*      */   {
/*  410 */     ((WindowStateListener)this.a).windowStateChanged(paramWindowEvent);
/*  411 */     ((WindowStateListener)this.b).windowStateChanged(paramWindowEvent);
/*      */   }
/*      */ 
/*      */   public void windowGainedFocus(WindowEvent paramWindowEvent)
/*      */   {
/*  422 */     ((WindowFocusListener)this.a).windowGainedFocus(paramWindowEvent);
/*  423 */     ((WindowFocusListener)this.b).windowGainedFocus(paramWindowEvent);
/*      */   }
/*      */ 
/*      */   public void windowLostFocus(WindowEvent paramWindowEvent)
/*      */   {
/*  433 */     ((WindowFocusListener)this.a).windowLostFocus(paramWindowEvent);
/*  434 */     ((WindowFocusListener)this.b).windowLostFocus(paramWindowEvent);
/*      */   }
/*      */ 
/*      */   public void actionPerformed(ActionEvent paramActionEvent)
/*      */   {
/*  443 */     ((ActionListener)this.a).actionPerformed(paramActionEvent);
/*  444 */     ((ActionListener)this.b).actionPerformed(paramActionEvent);
/*      */   }
/*      */ 
/*      */   public void itemStateChanged(ItemEvent paramItemEvent)
/*      */   {
/*  453 */     ((ItemListener)this.a).itemStateChanged(paramItemEvent);
/*  454 */     ((ItemListener)this.b).itemStateChanged(paramItemEvent);
/*      */   }
/*      */ 
/*      */   public void adjustmentValueChanged(AdjustmentEvent paramAdjustmentEvent)
/*      */   {
/*  463 */     ((AdjustmentListener)this.a).adjustmentValueChanged(paramAdjustmentEvent);
/*  464 */     ((AdjustmentListener)this.b).adjustmentValueChanged(paramAdjustmentEvent);
/*      */   }
/*      */   public void textValueChanged(TextEvent paramTextEvent) {
/*  467 */     ((TextListener)this.a).textValueChanged(paramTextEvent);
/*  468 */     ((TextListener)this.b).textValueChanged(paramTextEvent);
/*      */   }
/*      */ 
/*      */   public void inputMethodTextChanged(InputMethodEvent paramInputMethodEvent)
/*      */   {
/*  477 */     ((InputMethodListener)this.a).inputMethodTextChanged(paramInputMethodEvent);
/*  478 */     ((InputMethodListener)this.b).inputMethodTextChanged(paramInputMethodEvent);
/*      */   }
/*      */ 
/*      */   public void caretPositionChanged(InputMethodEvent paramInputMethodEvent)
/*      */   {
/*  487 */     ((InputMethodListener)this.a).caretPositionChanged(paramInputMethodEvent);
/*  488 */     ((InputMethodListener)this.b).caretPositionChanged(paramInputMethodEvent);
/*      */   }
/*      */ 
/*      */   public void hierarchyChanged(HierarchyEvent paramHierarchyEvent)
/*      */   {
/*  498 */     ((HierarchyListener)this.a).hierarchyChanged(paramHierarchyEvent);
/*  499 */     ((HierarchyListener)this.b).hierarchyChanged(paramHierarchyEvent);
/*      */   }
/*      */ 
/*      */   public void ancestorMoved(HierarchyEvent paramHierarchyEvent)
/*      */   {
/*  509 */     ((HierarchyBoundsListener)this.a).ancestorMoved(paramHierarchyEvent);
/*  510 */     ((HierarchyBoundsListener)this.b).ancestorMoved(paramHierarchyEvent);
/*      */   }
/*      */ 
/*      */   public void ancestorResized(HierarchyEvent paramHierarchyEvent)
/*      */   {
/*  520 */     ((HierarchyBoundsListener)this.a).ancestorResized(paramHierarchyEvent);
/*  521 */     ((HierarchyBoundsListener)this.b).ancestorResized(paramHierarchyEvent);
/*      */   }
/*      */ 
/*      */   public void mouseWheelMoved(MouseWheelEvent paramMouseWheelEvent)
/*      */   {
/*  531 */     ((MouseWheelListener)this.a).mouseWheelMoved(paramMouseWheelEvent);
/*  532 */     ((MouseWheelListener)this.b).mouseWheelMoved(paramMouseWheelEvent);
/*      */   }
/*      */ 
/*      */   public static ComponentListener add(ComponentListener paramComponentListener1, ComponentListener paramComponentListener2)
/*      */   {
/*  542 */     return (MouseWheelListener)addInternal(paramComponentListener1, paramComponentListener2);
/*      */   }
/*      */ 
/*      */   public static ContainerListener add(ContainerListener paramContainerListener1, ContainerListener paramContainerListener2)
/*      */   {
/*  552 */     return (ContainerListener)addInternal(paramContainerListener1, paramContainerListener2);
/*      */   }
/*      */ 
/*      */   public static FocusListener add(FocusListener paramFocusListener1, FocusListener paramFocusListener2)
/*      */   {
/*  562 */     return (FocusListener)addInternal(paramFocusListener1, paramFocusListener2);
/*      */   }
/*      */ 
/*      */   public static KeyListener add(KeyListener paramKeyListener1, KeyListener paramKeyListener2)
/*      */   {
/*  572 */     return (KeyListener)addInternal(paramKeyListener1, paramKeyListener2);
/*      */   }
/*      */ 
/*      */   public static MouseListener add(MouseListener paramMouseListener1, MouseListener paramMouseListener2)
/*      */   {
/*  582 */     return (MouseListener)addInternal(paramMouseListener1, paramMouseListener2);
/*      */   }
/*      */ 
/*      */   public static MouseMotionListener add(MouseMotionListener paramMouseMotionListener1, MouseMotionListener paramMouseMotionListener2)
/*      */   {
/*  592 */     return (MouseMotionListener)addInternal(paramMouseMotionListener1, paramMouseMotionListener2);
/*      */   }
/*      */ 
/*      */   public static WindowListener add(WindowListener paramWindowListener1, WindowListener paramWindowListener2)
/*      */   {
/*  602 */     return (WindowListener)addInternal(paramWindowListener1, paramWindowListener2);
/*      */   }
/*      */ 
/*      */   public static WindowStateListener add(WindowStateListener paramWindowStateListener1, WindowStateListener paramWindowStateListener2)
/*      */   {
/*  614 */     return (WindowStateListener)addInternal(paramWindowStateListener1, paramWindowStateListener2);
/*      */   }
/*      */ 
/*      */   public static WindowFocusListener add(WindowFocusListener paramWindowFocusListener1, WindowFocusListener paramWindowFocusListener2)
/*      */   {
/*  626 */     return (WindowFocusListener)addInternal(paramWindowFocusListener1, paramWindowFocusListener2);
/*      */   }
/*      */ 
/*      */   public static ActionListener add(ActionListener paramActionListener1, ActionListener paramActionListener2)
/*      */   {
/*  636 */     return (ActionListener)addInternal(paramActionListener1, paramActionListener2);
/*      */   }
/*      */ 
/*      */   public static ItemListener add(ItemListener paramItemListener1, ItemListener paramItemListener2)
/*      */   {
/*  646 */     return (ItemListener)addInternal(paramItemListener1, paramItemListener2);
/*      */   }
/*      */ 
/*      */   public static AdjustmentListener add(AdjustmentListener paramAdjustmentListener1, AdjustmentListener paramAdjustmentListener2)
/*      */   {
/*  656 */     return (AdjustmentListener)addInternal(paramAdjustmentListener1, paramAdjustmentListener2);
/*      */   }
/*      */   public static TextListener add(TextListener paramTextListener1, TextListener paramTextListener2) {
/*  659 */     return (TextListener)addInternal(paramTextListener1, paramTextListener2);
/*      */   }
/*      */ 
/*      */   public static InputMethodListener add(InputMethodListener paramInputMethodListener1, InputMethodListener paramInputMethodListener2)
/*      */   {
/*  669 */     return (InputMethodListener)addInternal(paramInputMethodListener1, paramInputMethodListener2);
/*      */   }
/*      */ 
/*      */   public static HierarchyListener add(HierarchyListener paramHierarchyListener1, HierarchyListener paramHierarchyListener2)
/*      */   {
/*  680 */     return (HierarchyListener)addInternal(paramHierarchyListener1, paramHierarchyListener2);
/*      */   }
/*      */ 
/*      */   public static HierarchyBoundsListener add(HierarchyBoundsListener paramHierarchyBoundsListener1, HierarchyBoundsListener paramHierarchyBoundsListener2)
/*      */   {
/*  691 */     return (HierarchyBoundsListener)addInternal(paramHierarchyBoundsListener1, paramHierarchyBoundsListener2);
/*      */   }
/*      */ 
/*      */   public static MouseWheelListener add(MouseWheelListener paramMouseWheelListener1, MouseWheelListener paramMouseWheelListener2)
/*      */   {
/*  703 */     return (MouseWheelListener)addInternal(paramMouseWheelListener1, paramMouseWheelListener2);
/*      */   }
/*      */ 
/*      */   public static ComponentListener remove(ComponentListener paramComponentListener1, ComponentListener paramComponentListener2)
/*      */   {
/*  713 */     return (MouseWheelListener)removeInternal(paramComponentListener1, paramComponentListener2);
/*      */   }
/*      */ 
/*      */   public static ContainerListener remove(ContainerListener paramContainerListener1, ContainerListener paramContainerListener2)
/*      */   {
/*  723 */     return (ContainerListener)removeInternal(paramContainerListener1, paramContainerListener2);
/*      */   }
/*      */ 
/*      */   public static FocusListener remove(FocusListener paramFocusListener1, FocusListener paramFocusListener2)
/*      */   {
/*  733 */     return (FocusListener)removeInternal(paramFocusListener1, paramFocusListener2);
/*      */   }
/*      */ 
/*      */   public static KeyListener remove(KeyListener paramKeyListener1, KeyListener paramKeyListener2)
/*      */   {
/*  743 */     return (KeyListener)removeInternal(paramKeyListener1, paramKeyListener2);
/*      */   }
/*      */ 
/*      */   public static MouseListener remove(MouseListener paramMouseListener1, MouseListener paramMouseListener2)
/*      */   {
/*  753 */     return (MouseListener)removeInternal(paramMouseListener1, paramMouseListener2);
/*      */   }
/*      */ 
/*      */   public static MouseMotionListener remove(MouseMotionListener paramMouseMotionListener1, MouseMotionListener paramMouseMotionListener2)
/*      */   {
/*  763 */     return (MouseMotionListener)removeInternal(paramMouseMotionListener1, paramMouseMotionListener2);
/*      */   }
/*      */ 
/*      */   public static WindowListener remove(WindowListener paramWindowListener1, WindowListener paramWindowListener2)
/*      */   {
/*  773 */     return (WindowListener)removeInternal(paramWindowListener1, paramWindowListener2);
/*      */   }
/*      */ 
/*      */   public static WindowStateListener remove(WindowStateListener paramWindowStateListener1, WindowStateListener paramWindowStateListener2)
/*      */   {
/*  785 */     return (WindowStateListener)removeInternal(paramWindowStateListener1, paramWindowStateListener2);
/*      */   }
/*      */ 
/*      */   public static WindowFocusListener remove(WindowFocusListener paramWindowFocusListener1, WindowFocusListener paramWindowFocusListener2)
/*      */   {
/*  797 */     return (WindowFocusListener)removeInternal(paramWindowFocusListener1, paramWindowFocusListener2);
/*      */   }
/*      */ 
/*      */   public static ActionListener remove(ActionListener paramActionListener1, ActionListener paramActionListener2)
/*      */   {
/*  807 */     return (ActionListener)removeInternal(paramActionListener1, paramActionListener2);
/*      */   }
/*      */ 
/*      */   public static ItemListener remove(ItemListener paramItemListener1, ItemListener paramItemListener2)
/*      */   {
/*  817 */     return (ItemListener)removeInternal(paramItemListener1, paramItemListener2);
/*      */   }
/*      */ 
/*      */   public static AdjustmentListener remove(AdjustmentListener paramAdjustmentListener1, AdjustmentListener paramAdjustmentListener2)
/*      */   {
/*  827 */     return (AdjustmentListener)removeInternal(paramAdjustmentListener1, paramAdjustmentListener2);
/*      */   }
/*      */   public static TextListener remove(TextListener paramTextListener1, TextListener paramTextListener2) {
/*  830 */     return (TextListener)removeInternal(paramTextListener1, paramTextListener2);
/*      */   }
/*      */ 
/*      */   public static InputMethodListener remove(InputMethodListener paramInputMethodListener1, InputMethodListener paramInputMethodListener2)
/*      */   {
/*  840 */     return (InputMethodListener)removeInternal(paramInputMethodListener1, paramInputMethodListener2);
/*      */   }
/*      */ 
/*      */   public static HierarchyListener remove(HierarchyListener paramHierarchyListener1, HierarchyListener paramHierarchyListener2)
/*      */   {
/*  851 */     return (HierarchyListener)removeInternal(paramHierarchyListener1, paramHierarchyListener2);
/*      */   }
/*      */ 
/*      */   public static HierarchyBoundsListener remove(HierarchyBoundsListener paramHierarchyBoundsListener1, HierarchyBoundsListener paramHierarchyBoundsListener2)
/*      */   {
/*  863 */     return (HierarchyBoundsListener)removeInternal(paramHierarchyBoundsListener1, paramHierarchyBoundsListener2);
/*      */   }
/*      */ 
/*      */   public static MouseWheelListener remove(MouseWheelListener paramMouseWheelListener1, MouseWheelListener paramMouseWheelListener2)
/*      */   {
/*  875 */     return (MouseWheelListener)removeInternal(paramMouseWheelListener1, paramMouseWheelListener2);
/*      */   }
/*      */ 
/*      */   protected static EventListener addInternal(EventListener paramEventListener1, EventListener paramEventListener2)
/*      */   {
/*  889 */     if (paramEventListener1 == null) return paramEventListener2;
/*  890 */     if (paramEventListener2 == null) return paramEventListener1;
/*  891 */     return new AWTEventMulticaster(paramEventListener1, paramEventListener2);
/*      */   }
/*      */ 
/*      */   protected static EventListener removeInternal(EventListener paramEventListener1, EventListener paramEventListener2)
/*      */   {
/*  906 */     if ((paramEventListener1 == paramEventListener2) || (paramEventListener1 == null))
/*  907 */       return null;
/*  908 */     if ((paramEventListener1 instanceof AWTEventMulticaster)) {
/*  909 */       return ((AWTEventMulticaster)paramEventListener1).remove(paramEventListener2);
/*      */     }
/*  911 */     return paramEventListener1;
/*      */   }
/*      */ 
/*      */   protected void saveInternal(ObjectOutputStream paramObjectOutputStream, String paramString)
/*      */     throws IOException
/*      */   {
/*  920 */     if ((this.a instanceof AWTEventMulticaster)) {
/*  921 */       ((AWTEventMulticaster)this.a).saveInternal(paramObjectOutputStream, paramString);
/*      */     }
/*  923 */     else if ((this.a instanceof Serializable)) {
/*  924 */       paramObjectOutputStream.writeObject(paramString);
/*  925 */       paramObjectOutputStream.writeObject(this.a);
/*      */     }
/*      */ 
/*  928 */     if ((this.b instanceof AWTEventMulticaster)) {
/*  929 */       ((AWTEventMulticaster)this.b).saveInternal(paramObjectOutputStream, paramString);
/*      */     }
/*  931 */     else if ((this.b instanceof Serializable)) {
/*  932 */       paramObjectOutputStream.writeObject(paramString);
/*  933 */       paramObjectOutputStream.writeObject(this.b);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected static void save(ObjectOutputStream paramObjectOutputStream, String paramString, EventListener paramEventListener) throws IOException {
/*  938 */     if (paramEventListener == null) {
/*  939 */       return;
/*      */     }
/*  941 */     if ((paramEventListener instanceof AWTEventMulticaster)) {
/*  942 */       ((AWTEventMulticaster)paramEventListener).saveInternal(paramObjectOutputStream, paramString);
/*      */     }
/*  944 */     else if ((paramEventListener instanceof Serializable)) {
/*  945 */       paramObjectOutputStream.writeObject(paramString);
/*  946 */       paramObjectOutputStream.writeObject(paramEventListener);
/*      */     }
/*      */   }
/*      */ 
/*      */   private static int getListenerCount(EventListener paramEventListener, Class paramClass)
/*      */   {
/*  957 */     if ((paramEventListener instanceof AWTEventMulticaster)) {
/*  958 */       AWTEventMulticaster localAWTEventMulticaster = (AWTEventMulticaster)paramEventListener;
/*  959 */       return getListenerCount(localAWTEventMulticaster.a, paramClass) + getListenerCount(localAWTEventMulticaster.b, paramClass);
/*      */     }
/*      */ 
/*  964 */     return paramClass.isInstance(paramEventListener) ? 1 : 0;
/*      */   }
/*      */ 
/*      */   private static int populateListenerArray(EventListener[] paramArrayOfEventListener, EventListener paramEventListener, int paramInt)
/*      */   {
/*  976 */     if ((paramEventListener instanceof AWTEventMulticaster)) {
/*  977 */       AWTEventMulticaster localAWTEventMulticaster = (AWTEventMulticaster)paramEventListener;
/*  978 */       int i = populateListenerArray(paramArrayOfEventListener, localAWTEventMulticaster.a, paramInt);
/*  979 */       return populateListenerArray(paramArrayOfEventListener, localAWTEventMulticaster.b, i);
/*      */     }
/*  981 */     if (paramArrayOfEventListener.getClass().getComponentType().isInstance(paramEventListener)) {
/*  982 */       paramArrayOfEventListener[paramInt] = paramEventListener;
/*  983 */       return paramInt + 1;
/*      */     }
/*      */ 
/*  987 */     return paramInt;
/*      */   }
/*      */ 
/*      */   public static <T extends EventListener> T[] getListeners(EventListener paramEventListener, Class<T> paramClass)
/*      */   {
/* 1023 */     if (paramClass == null) {
/* 1024 */       throw new NullPointerException("Listener type should not be null");
/*      */     }
/*      */ 
/* 1027 */     int i = getListenerCount(paramEventListener, paramClass);
/* 1028 */     EventListener[] arrayOfEventListener = (EventListener[])Array.newInstance(paramClass, i);
/* 1029 */     populateListenerArray(arrayOfEventListener, paramEventListener, 0);
/* 1030 */     return arrayOfEventListener;
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.AWTEventMulticaster
 * JD-Core Version:    0.6.2
 */