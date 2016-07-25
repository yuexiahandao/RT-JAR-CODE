/*      */ package javax.swing;
/*      */ 
/*      */ import java.awt.BorderLayout;
/*      */ import java.awt.Component;
/*      */ import java.awt.Container;
/*      */ import java.awt.Dialog;
/*      */ import java.awt.Dimension;
/*      */ import java.awt.Frame;
/*      */ import java.awt.HeadlessException;
/*      */ import java.awt.KeyboardFocusManager;
/*      */ import java.awt.Point;
/*      */ import java.awt.Window;
/*      */ import java.awt.event.ComponentAdapter;
/*      */ import java.awt.event.ComponentEvent;
/*      */ import java.awt.event.WindowAdapter;
/*      */ import java.awt.event.WindowEvent;
/*      */ import java.awt.event.WindowListener;
/*      */ import java.beans.PropertyChangeEvent;
/*      */ import java.beans.PropertyChangeListener;
/*      */ import java.beans.PropertyVetoException;
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.Serializable;
/*      */ import java.lang.reflect.InvocationTargetException;
/*      */ import java.lang.reflect.Method;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.util.Vector;
/*      */ import javax.accessibility.Accessible;
/*      */ import javax.accessibility.AccessibleContext;
/*      */ import javax.accessibility.AccessibleRole;
/*      */ import javax.swing.event.InternalFrameAdapter;
/*      */ import javax.swing.event.InternalFrameEvent;
/*      */ import javax.swing.plaf.ComponentUI;
/*      */ import javax.swing.plaf.OptionPaneUI;
/*      */ 
/*      */ public class JOptionPane extends JComponent
/*      */   implements Accessible
/*      */ {
/*      */   private static final String uiClassID = "OptionPaneUI";
/*  325 */   public static final Object UNINITIALIZED_VALUE = "uninitializedValue";
/*      */   public static final int DEFAULT_OPTION = -1;
/*      */   public static final int YES_NO_OPTION = 0;
/*      */   public static final int YES_NO_CANCEL_OPTION = 1;
/*      */   public static final int OK_CANCEL_OPTION = 2;
/*      */   public static final int YES_OPTION = 0;
/*      */   public static final int NO_OPTION = 1;
/*      */   public static final int CANCEL_OPTION = 2;
/*      */   public static final int OK_OPTION = 0;
/*      */   public static final int CLOSED_OPTION = -1;
/*      */   public static final int ERROR_MESSAGE = 0;
/*      */   public static final int INFORMATION_MESSAGE = 1;
/*      */   public static final int WARNING_MESSAGE = 2;
/*      */   public static final int QUESTION_MESSAGE = 3;
/*      */   public static final int PLAIN_MESSAGE = -1;
/*      */   public static final String ICON_PROPERTY = "icon";
/*      */   public static final String MESSAGE_PROPERTY = "message";
/*      */   public static final String VALUE_PROPERTY = "value";
/*      */   public static final String OPTIONS_PROPERTY = "options";
/*      */   public static final String INITIAL_VALUE_PROPERTY = "initialValue";
/*      */   public static final String MESSAGE_TYPE_PROPERTY = "messageType";
/*      */   public static final String OPTION_TYPE_PROPERTY = "optionType";
/*      */   public static final String SELECTION_VALUES_PROPERTY = "selectionValues";
/*      */   public static final String INITIAL_SELECTION_VALUE_PROPERTY = "initialSelectionValue";
/*      */   public static final String INPUT_VALUE_PROPERTY = "inputValue";
/*      */   public static final String WANTS_INPUT_PROPERTY = "wantsInput";
/*      */   protected transient Icon icon;
/*      */   protected transient Object message;
/*      */   protected transient Object[] options;
/*      */   protected transient Object initialValue;
/*      */   protected int messageType;
/*      */   protected int optionType;
/*      */   protected transient Object value;
/*      */   protected transient Object[] selectionValues;
/*      */   protected transient Object inputValue;
/*      */   protected transient Object initialSelectionValue;
/*      */   protected boolean wantsInput;
/* 1663 */   private static final Object sharedFrameKey = JOptionPane.class;
/*      */ 
/*      */   public static String showInputDialog(Object paramObject)
/*      */     throws HeadlessException
/*      */   {
/*  441 */     return showInputDialog(null, paramObject);
/*      */   }
/*      */ 
/*      */   public static String showInputDialog(Object paramObject1, Object paramObject2)
/*      */   {
/*  456 */     return showInputDialog(null, paramObject1, paramObject2);
/*      */   }
/*      */ 
/*      */   public static String showInputDialog(Component paramComponent, Object paramObject)
/*      */     throws HeadlessException
/*      */   {
/*  475 */     return showInputDialog(paramComponent, paramObject, UIManager.getString("OptionPane.inputDialogTitle", paramComponent), 3);
/*      */   }
/*      */ 
/*      */   public static String showInputDialog(Component paramComponent, Object paramObject1, Object paramObject2)
/*      */   {
/*  495 */     return (String)showInputDialog(paramComponent, paramObject1, UIManager.getString("OptionPane.inputDialogTitle", paramComponent), 3, null, null, paramObject2);
/*      */   }
/*      */ 
/*      */   public static String showInputDialog(Component paramComponent, Object paramObject, String paramString, int paramInt)
/*      */     throws HeadlessException
/*      */   {
/*  525 */     return (String)showInputDialog(paramComponent, paramObject, paramString, paramInt, null, null, null);
/*      */   }
/*      */ 
/*      */   public static Object showInputDialog(Component paramComponent, Object paramObject1, String paramString, int paramInt, Icon paramIcon, Object[] paramArrayOfObject, Object paramObject2)
/*      */     throws HeadlessException
/*      */   {
/*  569 */     JOptionPane localJOptionPane = new JOptionPane(paramObject1, paramInt, 2, paramIcon, null, null);
/*      */ 
/*  573 */     localJOptionPane.setWantsInput(true);
/*  574 */     localJOptionPane.setSelectionValues(paramArrayOfObject);
/*  575 */     localJOptionPane.setInitialSelectionValue(paramObject2);
/*  576 */     localJOptionPane.setComponentOrientation((paramComponent == null ? getRootFrame() : paramComponent).getComponentOrientation());
/*      */ 
/*  579 */     int i = styleFromMessageType(paramInt);
/*  580 */     JDialog localJDialog = localJOptionPane.createDialog(paramComponent, paramString, i);
/*      */ 
/*  582 */     localJOptionPane.selectInitialValue();
/*  583 */     localJDialog.show();
/*  584 */     localJDialog.dispose();
/*      */ 
/*  586 */     Object localObject = localJOptionPane.getInputValue();
/*      */ 
/*  588 */     if (localObject == UNINITIALIZED_VALUE) {
/*  589 */       return null;
/*      */     }
/*  591 */     return localObject;
/*      */   }
/*      */ 
/*      */   public static void showMessageDialog(Component paramComponent, Object paramObject)
/*      */     throws HeadlessException
/*      */   {
/*  609 */     showMessageDialog(paramComponent, paramObject, UIManager.getString("OptionPane.messageDialogTitle", paramComponent), 1);
/*      */   }
/*      */ 
/*      */   public static void showMessageDialog(Component paramComponent, Object paramObject, String paramString, int paramInt)
/*      */     throws HeadlessException
/*      */   {
/*  638 */     showMessageDialog(paramComponent, paramObject, paramString, paramInt, null);
/*      */   }
/*      */ 
/*      */   public static void showMessageDialog(Component paramComponent, Object paramObject, String paramString, int paramInt, Icon paramIcon)
/*      */     throws HeadlessException
/*      */   {
/*  667 */     showOptionDialog(paramComponent, paramObject, paramString, -1, paramInt, paramIcon, null, null);
/*      */   }
/*      */ 
/*      */   public static int showConfirmDialog(Component paramComponent, Object paramObject)
/*      */     throws HeadlessException
/*      */   {
/*  690 */     return showConfirmDialog(paramComponent, paramObject, UIManager.getString("OptionPane.titleText"), 1);
/*      */   }
/*      */ 
/*      */   public static int showConfirmDialog(Component paramComponent, Object paramObject, String paramString, int paramInt)
/*      */     throws HeadlessException
/*      */   {
/*  719 */     return showConfirmDialog(paramComponent, paramObject, paramString, paramInt, 3);
/*      */   }
/*      */ 
/*      */   public static int showConfirmDialog(Component paramComponent, Object paramObject, String paramString, int paramInt1, int paramInt2)
/*      */     throws HeadlessException
/*      */   {
/*  758 */     return showConfirmDialog(paramComponent, paramObject, paramString, paramInt1, paramInt2, null);
/*      */   }
/*      */ 
/*      */   public static int showConfirmDialog(Component paramComponent, Object paramObject, String paramString, int paramInt1, int paramInt2, Icon paramIcon)
/*      */     throws HeadlessException
/*      */   {
/*  796 */     return showOptionDialog(paramComponent, paramObject, paramString, paramInt1, paramInt2, paramIcon, null, null);
/*      */   }
/*      */ 
/*      */   public static int showOptionDialog(Component paramComponent, Object paramObject1, String paramString, int paramInt1, int paramInt2, Icon paramIcon, Object[] paramArrayOfObject, Object paramObject2)
/*      */     throws HeadlessException
/*      */   {
/*  858 */     JOptionPane localJOptionPane = new JOptionPane(paramObject1, paramInt2, paramInt1, paramIcon, paramArrayOfObject, paramObject2);
/*      */ 
/*  862 */     localJOptionPane.setInitialValue(paramObject2);
/*  863 */     localJOptionPane.setComponentOrientation((paramComponent == null ? getRootFrame() : paramComponent).getComponentOrientation());
/*      */ 
/*  866 */     int i = styleFromMessageType(paramInt2);
/*  867 */     JDialog localJDialog = localJOptionPane.createDialog(paramComponent, paramString, i);
/*      */ 
/*  869 */     localJOptionPane.selectInitialValue();
/*  870 */     localJDialog.show();
/*  871 */     localJDialog.dispose();
/*      */ 
/*  873 */     Object localObject = localJOptionPane.getValue();
/*      */ 
/*  875 */     if (localObject == null)
/*  876 */       return -1;
/*  877 */     if (paramArrayOfObject == null) {
/*  878 */       if ((localObject instanceof Integer))
/*  879 */         return ((Integer)localObject).intValue();
/*  880 */       return -1;
/*      */     }
/*  882 */     int j = 0; int k = paramArrayOfObject.length;
/*  883 */     for (; j < k; j++) {
/*  884 */       if (paramArrayOfObject[j].equals(localObject))
/*  885 */         return j;
/*      */     }
/*  887 */     return -1;
/*      */   }
/*      */ 
/*      */   public JDialog createDialog(Component paramComponent, String paramString)
/*      */     throws HeadlessException
/*      */   {
/*  918 */     int i = styleFromMessageType(getMessageType());
/*  919 */     return createDialog(paramComponent, paramString, i);
/*      */   }
/*      */ 
/*      */   public JDialog createDialog(String paramString)
/*      */     throws HeadlessException
/*      */   {
/*  945 */     int i = styleFromMessageType(getMessageType());
/*  946 */     JDialog localJDialog = new JDialog((Dialog)null, paramString, true);
/*  947 */     initDialog(localJDialog, i, null);
/*  948 */     return localJDialog;
/*      */   }
/*      */ 
/*      */   private JDialog createDialog(Component paramComponent, String paramString, int paramInt)
/*      */     throws HeadlessException
/*      */   {
/*  957 */     Window localWindow = getWindowForComponent(paramComponent);
/*      */     JDialog localJDialog;
/*  958 */     if ((localWindow instanceof Frame))
/*  959 */       localJDialog = new JDialog((Frame)localWindow, paramString, true);
/*      */     else {
/*  961 */       localJDialog = new JDialog((Dialog)localWindow, paramString, true);
/*      */     }
/*  963 */     if ((localWindow instanceof SwingUtilities.SharedOwnerFrame)) {
/*  964 */       WindowListener localWindowListener = SwingUtilities.getSharedOwnerFrameShutdownListener();
/*      */ 
/*  966 */       localJDialog.addWindowListener(localWindowListener);
/*      */     }
/*  968 */     initDialog(localJDialog, paramInt, paramComponent);
/*  969 */     return localJDialog;
/*      */   }
/*      */ 
/*      */   private void initDialog(final JDialog paramJDialog, int paramInt, Component paramComponent) {
/*  973 */     paramJDialog.setComponentOrientation(getComponentOrientation());
/*  974 */     Container localContainer = paramJDialog.getContentPane();
/*      */ 
/*  976 */     localContainer.setLayout(new BorderLayout());
/*  977 */     localContainer.add(this, "Center");
/*  978 */     paramJDialog.setResizable(false);
/*  979 */     if (JDialog.isDefaultLookAndFeelDecorated()) {
/*  980 */       boolean bool = UIManager.getLookAndFeel().getSupportsWindowDecorations();
/*      */ 
/*  982 */       if (bool) {
/*  983 */         paramJDialog.setUndecorated(true);
/*  984 */         getRootPane().setWindowDecorationStyle(paramInt);
/*      */       }
/*      */     }
/*  987 */     paramJDialog.pack();
/*  988 */     paramJDialog.setLocationRelativeTo(paramComponent);
/*      */ 
/*  990 */     final PropertyChangeListener local1 = new PropertyChangeListener()
/*      */     {
/*      */       public void propertyChange(PropertyChangeEvent paramAnonymousPropertyChangeEvent)
/*      */       {
/*  995 */         if ((paramJDialog.isVisible()) && (paramAnonymousPropertyChangeEvent.getSource() == JOptionPane.this) && (paramAnonymousPropertyChangeEvent.getPropertyName().equals("value")) && (paramAnonymousPropertyChangeEvent.getNewValue() != null) && (paramAnonymousPropertyChangeEvent.getNewValue() != JOptionPane.UNINITIALIZED_VALUE))
/*      */         {
/*  999 */           paramJDialog.setVisible(false);
/*      */         }
/*      */       }
/*      */     };
/* 1004 */     WindowAdapter local2 = new WindowAdapter() {
/* 1005 */       private boolean gotFocus = false;
/*      */ 
/* 1007 */       public void windowClosing(WindowEvent paramAnonymousWindowEvent) { JOptionPane.this.setValue(null); }
/*      */ 
/*      */       public void windowClosed(WindowEvent paramAnonymousWindowEvent)
/*      */       {
/* 1011 */         JOptionPane.this.removePropertyChangeListener(local1);
/* 1012 */         paramJDialog.getContentPane().removeAll();
/*      */       }
/*      */ 
/*      */       public void windowGainedFocus(WindowEvent paramAnonymousWindowEvent)
/*      */       {
/* 1017 */         if (!this.gotFocus) {
/* 1018 */           JOptionPane.this.selectInitialValue();
/* 1019 */           this.gotFocus = true;
/*      */         }
/*      */       }
/*      */     };
/* 1023 */     paramJDialog.addWindowListener(local2);
/* 1024 */     paramJDialog.addWindowFocusListener(local2);
/* 1025 */     paramJDialog.addComponentListener(new ComponentAdapter()
/*      */     {
/*      */       public void componentShown(ComponentEvent paramAnonymousComponentEvent) {
/* 1028 */         JOptionPane.this.setValue(JOptionPane.UNINITIALIZED_VALUE);
/*      */       }
/*      */     });
/* 1032 */     addPropertyChangeListener(local1);
/*      */   }
/*      */ 
/*      */   public static void showInternalMessageDialog(Component paramComponent, Object paramObject)
/*      */   {
/* 1048 */     showInternalMessageDialog(paramComponent, paramObject, UIManager.getString("OptionPane.messageDialogTitle", paramComponent), 1);
/*      */   }
/*      */ 
/*      */   public static void showInternalMessageDialog(Component paramComponent, Object paramObject, String paramString, int paramInt)
/*      */   {
/* 1074 */     showInternalMessageDialog(paramComponent, paramObject, paramString, paramInt, null);
/*      */   }
/*      */ 
/*      */   public static void showInternalMessageDialog(Component paramComponent, Object paramObject, String paramString, int paramInt, Icon paramIcon)
/*      */   {
/* 1100 */     showInternalOptionDialog(paramComponent, paramObject, paramString, -1, paramInt, paramIcon, null, null);
/*      */   }
/*      */ 
/*      */   public static int showInternalConfirmDialog(Component paramComponent, Object paramObject)
/*      */   {
/* 1117 */     return showInternalConfirmDialog(paramComponent, paramObject, UIManager.getString("OptionPane.titleText"), 1);
/*      */   }
/*      */ 
/*      */   public static int showInternalConfirmDialog(Component paramComponent, Object paramObject, String paramString, int paramInt)
/*      */   {
/* 1145 */     return showInternalConfirmDialog(paramComponent, paramObject, paramString, paramInt, 3);
/*      */   }
/*      */ 
/*      */   public static int showInternalConfirmDialog(Component paramComponent, Object paramObject, String paramString, int paramInt1, int paramInt2)
/*      */   {
/* 1182 */     return showInternalConfirmDialog(paramComponent, paramObject, paramString, paramInt1, paramInt2, null);
/*      */   }
/*      */ 
/*      */   public static int showInternalConfirmDialog(Component paramComponent, Object paramObject, String paramString, int paramInt1, int paramInt2, Icon paramIcon)
/*      */   {
/* 1221 */     return showInternalOptionDialog(paramComponent, paramObject, paramString, paramInt1, paramInt2, paramIcon, null, null);
/*      */   }
/*      */ 
/*      */   public static int showInternalOptionDialog(Component paramComponent, Object paramObject1, String paramString, int paramInt1, int paramInt2, Icon paramIcon, Object[] paramArrayOfObject, Object paramObject2)
/*      */   {
/* 1277 */     JOptionPane localJOptionPane = new JOptionPane(paramObject1, paramInt2, paramInt1, paramIcon, paramArrayOfObject, paramObject2);
/*      */ 
/* 1279 */     localJOptionPane.putClientProperty(ClientPropertyKey.PopupFactory_FORCE_HEAVYWEIGHT_POPUP, Boolean.TRUE);
/*      */ 
/* 1281 */     Component localComponent = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
/*      */ 
/* 1284 */     localJOptionPane.setInitialValue(paramObject2);
/*      */ 
/* 1286 */     JInternalFrame localJInternalFrame = localJOptionPane.createInternalFrame(paramComponent, paramString);
/*      */ 
/* 1288 */     localJOptionPane.selectInitialValue();
/* 1289 */     localJInternalFrame.setVisible(true);
/*      */     Object localObject1;
/* 1299 */     if ((localJInternalFrame.isVisible()) && (!localJInternalFrame.isShowing())) {
/* 1300 */       localObject1 = localJInternalFrame.getParent();
/* 1301 */       while (localObject1 != null) {
/* 1302 */         if (!((Container)localObject1).isVisible()) {
/* 1303 */           ((Container)localObject1).setVisible(true);
/*      */         }
/* 1305 */         localObject1 = ((Container)localObject1).getParent();
/*      */       }
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/* 1311 */       localObject1 = (Method)AccessController.doPrivileged(new ModalPrivilegedAction(Container.class, "startLWModal"));
/*      */ 
/* 1313 */       if (localObject1 != null)
/* 1314 */         ((Method)localObject1).invoke(localJInternalFrame, (Object[])null);
/*      */     } catch (IllegalAccessException localIllegalAccessException) {
/*      */     }
/*      */     catch (IllegalArgumentException localIllegalArgumentException) {
/*      */     }
/*      */     catch (InvocationTargetException localInvocationTargetException) {
/*      */     }
/* 1321 */     if ((paramComponent instanceof JInternalFrame))
/*      */       try {
/* 1323 */         ((JInternalFrame)paramComponent).setSelected(true);
/*      */       }
/*      */       catch (PropertyVetoException localPropertyVetoException)
/*      */       {
/*      */       }
/* 1328 */     Object localObject2 = localJOptionPane.getValue();
/*      */ 
/* 1330 */     if ((localComponent != null) && (localComponent.isShowing())) {
/* 1331 */       localComponent.requestFocus();
/*      */     }
/* 1333 */     if (localObject2 == null) {
/* 1334 */       return -1;
/*      */     }
/* 1336 */     if (paramArrayOfObject == null) {
/* 1337 */       if ((localObject2 instanceof Integer)) {
/* 1338 */         return ((Integer)localObject2).intValue();
/*      */       }
/* 1340 */       return -1;
/*      */     }
/* 1342 */     int i = 0; int j = paramArrayOfObject.length;
/* 1343 */     for (; i < j; i++) {
/* 1344 */       if (paramArrayOfObject[i].equals(localObject2)) {
/* 1345 */         return i;
/*      */       }
/*      */     }
/* 1348 */     return -1;
/*      */   }
/*      */ 
/*      */   public static String showInternalInputDialog(Component paramComponent, Object paramObject)
/*      */   {
/* 1363 */     return showInternalInputDialog(paramComponent, paramObject, UIManager.getString("OptionPane.inputDialogTitle", paramComponent), 3);
/*      */   }
/*      */ 
/*      */   public static String showInternalInputDialog(Component paramComponent, Object paramObject, String paramString, int paramInt)
/*      */   {
/* 1383 */     return (String)showInternalInputDialog(paramComponent, paramObject, paramString, paramInt, null, null, null);
/*      */   }
/*      */ 
/*      */   public static Object showInternalInputDialog(Component paramComponent, Object paramObject1, String paramString, int paramInt, Icon paramIcon, Object[] paramArrayOfObject, Object paramObject2)
/*      */   {
/* 1419 */     JOptionPane localJOptionPane = new JOptionPane(paramObject1, paramInt, 2, paramIcon, null, null);
/*      */ 
/* 1421 */     localJOptionPane.putClientProperty(ClientPropertyKey.PopupFactory_FORCE_HEAVYWEIGHT_POPUP, Boolean.TRUE);
/*      */ 
/* 1423 */     Component localComponent = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
/*      */ 
/* 1426 */     localJOptionPane.setWantsInput(true);
/* 1427 */     localJOptionPane.setSelectionValues(paramArrayOfObject);
/* 1428 */     localJOptionPane.setInitialSelectionValue(paramObject2);
/*      */ 
/* 1430 */     JInternalFrame localJInternalFrame = localJOptionPane.createInternalFrame(paramComponent, paramString);
/*      */ 
/* 1433 */     localJOptionPane.selectInitialValue();
/* 1434 */     localJInternalFrame.setVisible(true);
/*      */     Object localObject1;
/* 1444 */     if ((localJInternalFrame.isVisible()) && (!localJInternalFrame.isShowing())) {
/* 1445 */       localObject1 = localJInternalFrame.getParent();
/* 1446 */       while (localObject1 != null) {
/* 1447 */         if (!((Container)localObject1).isVisible()) {
/* 1448 */           ((Container)localObject1).setVisible(true);
/*      */         }
/* 1450 */         localObject1 = ((Container)localObject1).getParent();
/*      */       }
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/* 1456 */       localObject1 = (Method)AccessController.doPrivileged(new ModalPrivilegedAction(Container.class, "startLWModal"));
/*      */ 
/* 1458 */       if (localObject1 != null)
/* 1459 */         ((Method)localObject1).invoke(localJInternalFrame, (Object[])null);
/*      */     } catch (IllegalAccessException localIllegalAccessException) {
/*      */     }
/*      */     catch (IllegalArgumentException localIllegalArgumentException) {
/*      */     }
/*      */     catch (InvocationTargetException localInvocationTargetException) {
/*      */     }
/* 1466 */     if ((paramComponent instanceof JInternalFrame))
/*      */       try {
/* 1468 */         ((JInternalFrame)paramComponent).setSelected(true);
/*      */       }
/*      */       catch (PropertyVetoException localPropertyVetoException)
/*      */       {
/*      */       }
/* 1473 */     if ((localComponent != null) && (localComponent.isShowing())) {
/* 1474 */       localComponent.requestFocus();
/*      */     }
/* 1476 */     Object localObject2 = localJOptionPane.getInputValue();
/*      */ 
/* 1478 */     if (localObject2 == UNINITIALIZED_VALUE) {
/* 1479 */       return null;
/*      */     }
/* 1481 */     return localObject2;
/*      */   }
/*      */ 
/*      */   public JInternalFrame createInternalFrame(Component paramComponent, String paramString)
/*      */   {
/* 1506 */     Object localObject = getDesktopPaneForComponent(paramComponent);
/*      */ 
/* 1509 */     if ((localObject == null) && ((paramComponent == null) || ((localObject = paramComponent.getParent()) == null)))
/*      */     {
/* 1511 */       throw new RuntimeException("JOptionPane: parentComponent does not have a valid parent");
/*      */     }
/*      */ 
/* 1516 */     final JInternalFrame localJInternalFrame = new JInternalFrame(paramString, false, true, false, false);
/*      */ 
/* 1519 */     localJInternalFrame.putClientProperty("JInternalFrame.frameType", "optionDialog");
/* 1520 */     localJInternalFrame.putClientProperty("JInternalFrame.messageType", Integer.valueOf(getMessageType()));
/*      */ 
/* 1523 */     localJInternalFrame.addInternalFrameListener(new InternalFrameAdapter() {
/*      */       public void internalFrameClosing(InternalFrameEvent paramAnonymousInternalFrameEvent) {
/* 1525 */         if (JOptionPane.this.getValue() == JOptionPane.UNINITIALIZED_VALUE)
/* 1526 */           JOptionPane.this.setValue(null);
/*      */       }
/*      */     });
/* 1530 */     addPropertyChangeListener(new PropertyChangeListener()
/*      */     {
/*      */       public void propertyChange(PropertyChangeEvent paramAnonymousPropertyChangeEvent)
/*      */       {
/* 1535 */         if ((localJInternalFrame.isVisible()) && (paramAnonymousPropertyChangeEvent.getSource() == JOptionPane.this) && (paramAnonymousPropertyChangeEvent.getPropertyName().equals("value")))
/*      */         {
/*      */           try
/*      */           {
/* 1540 */             Method localMethod = (Method)AccessController.doPrivileged(new JOptionPane.ModalPrivilegedAction(Container.class, "stopLWModal"));
/*      */ 
/* 1543 */             if (localMethod != null)
/* 1544 */               localMethod.invoke(localJInternalFrame, (Object[])null);
/*      */           } catch (IllegalAccessException localIllegalAccessException) {
/*      */           }
/*      */           catch (IllegalArgumentException localIllegalArgumentException) {
/*      */           }
/*      */           catch (InvocationTargetException localInvocationTargetException) {
/*      */           }
/*      */           try {
/* 1552 */             localJInternalFrame.setClosed(true);
/*      */           }
/*      */           catch (PropertyVetoException localPropertyVetoException)
/*      */           {
/*      */           }
/* 1557 */           localJInternalFrame.setVisible(false);
/*      */         }
/*      */       }
/*      */     });
/* 1561 */     localJInternalFrame.getContentPane().add(this, "Center");
/* 1562 */     if ((localObject instanceof JDesktopPane))
/* 1563 */       ((Container)localObject).add(localJInternalFrame, JLayeredPane.MODAL_LAYER);
/*      */     else {
/* 1565 */       ((Container)localObject).add(localJInternalFrame, "Center");
/*      */     }
/* 1567 */     Dimension localDimension1 = localJInternalFrame.getPreferredSize();
/* 1568 */     Dimension localDimension2 = ((Container)localObject).getSize();
/* 1569 */     Dimension localDimension3 = paramComponent.getSize();
/*      */ 
/* 1571 */     localJInternalFrame.setBounds((localDimension2.width - localDimension1.width) / 2, (localDimension2.height - localDimension1.height) / 2, localDimension1.width, localDimension1.height);
/*      */ 
/* 1575 */     Point localPoint = SwingUtilities.convertPoint(paramComponent, 0, 0, (Component)localObject);
/*      */ 
/* 1577 */     int i = (localDimension3.width - localDimension1.width) / 2 + localPoint.x;
/* 1578 */     int j = (localDimension3.height - localDimension1.height) / 2 + localPoint.y;
/*      */ 
/* 1581 */     int k = i + localDimension1.width - localDimension2.width;
/* 1582 */     int m = j + localDimension1.height - localDimension2.height;
/* 1583 */     i = Math.max(k > 0 ? i - k : i, 0);
/* 1584 */     j = Math.max(m > 0 ? j - m : j, 0);
/* 1585 */     localJInternalFrame.setBounds(i, j, localDimension1.width, localDimension1.height);
/*      */ 
/* 1587 */     ((Container)localObject).validate();
/*      */     try {
/* 1589 */       localJInternalFrame.setSelected(true);
/*      */     } catch (PropertyVetoException localPropertyVetoException) {
/*      */     }
/* 1592 */     return localJInternalFrame;
/*      */   }
/*      */ 
/*      */   public static Frame getFrameForComponent(Component paramComponent)
/*      */     throws HeadlessException
/*      */   {
/* 1612 */     if (paramComponent == null)
/* 1613 */       return getRootFrame();
/* 1614 */     if ((paramComponent instanceof Frame))
/* 1615 */       return (Frame)paramComponent;
/* 1616 */     return getFrameForComponent(paramComponent.getParent());
/*      */   }
/*      */ 
/*      */   static Window getWindowForComponent(Component paramComponent)
/*      */     throws HeadlessException
/*      */   {
/* 1637 */     if (paramComponent == null)
/* 1638 */       return getRootFrame();
/* 1639 */     if (((paramComponent instanceof Frame)) || ((paramComponent instanceof Dialog)))
/* 1640 */       return (Window)paramComponent;
/* 1641 */     return getWindowForComponent(paramComponent.getParent());
/*      */   }
/*      */ 
/*      */   public static JDesktopPane getDesktopPaneForComponent(Component paramComponent)
/*      */   {
/* 1656 */     if (paramComponent == null)
/* 1657 */       return null;
/* 1658 */     if ((paramComponent instanceof JDesktopPane))
/* 1659 */       return (JDesktopPane)paramComponent;
/* 1660 */     return getDesktopPaneForComponent(paramComponent.getParent());
/*      */   }
/*      */ 
/*      */   public static void setRootFrame(Frame paramFrame)
/*      */   {
/* 1675 */     if (paramFrame != null)
/* 1676 */       SwingUtilities.appContextPut(sharedFrameKey, paramFrame);
/*      */     else
/* 1678 */       SwingUtilities.appContextRemove(sharedFrameKey);
/*      */   }
/*      */ 
/*      */   public static Frame getRootFrame()
/*      */     throws HeadlessException
/*      */   {
/* 1694 */     Frame localFrame = (Frame)SwingUtilities.appContextGet(sharedFrameKey);
/*      */ 
/* 1696 */     if (localFrame == null) {
/* 1697 */       localFrame = SwingUtilities.getSharedOwnerFrame();
/* 1698 */       SwingUtilities.appContextPut(sharedFrameKey, localFrame);
/*      */     }
/* 1700 */     return localFrame;
/*      */   }
/*      */ 
/*      */   public JOptionPane()
/*      */   {
/* 1707 */     this("JOptionPane message");
/*      */   }
/*      */ 
/*      */   public JOptionPane(Object paramObject)
/*      */   {
/* 1719 */     this(paramObject, -1);
/*      */   }
/*      */ 
/*      */   public JOptionPane(Object paramObject, int paramInt)
/*      */   {
/* 1735 */     this(paramObject, paramInt, -1);
/*      */   }
/*      */ 
/*      */   public JOptionPane(Object paramObject, int paramInt1, int paramInt2)
/*      */   {
/* 1755 */     this(paramObject, paramInt1, paramInt2, null);
/*      */   }
/*      */ 
/*      */   public JOptionPane(Object paramObject, int paramInt1, int paramInt2, Icon paramIcon)
/*      */   {
/* 1777 */     this(paramObject, paramInt1, paramInt2, paramIcon, null);
/*      */   }
/*      */ 
/*      */   public JOptionPane(Object paramObject, int paramInt1, int paramInt2, Icon paramIcon, Object[] paramArrayOfObject)
/*      */   {
/* 1809 */     this(paramObject, paramInt1, paramInt2, paramIcon, paramArrayOfObject, null);
/*      */   }
/*      */ 
/*      */   public JOptionPane(Object paramObject1, int paramInt1, int paramInt2, Icon paramIcon, Object[] paramArrayOfObject, Object paramObject2)
/*      */   {
/* 1838 */     this.message = paramObject1;
/* 1839 */     this.options = paramArrayOfObject;
/* 1840 */     this.initialValue = paramObject2;
/* 1841 */     this.icon = paramIcon;
/* 1842 */     setMessageType(paramInt1);
/* 1843 */     setOptionType(paramInt2);
/* 1844 */     this.value = UNINITIALIZED_VALUE;
/* 1845 */     this.inputValue = UNINITIALIZED_VALUE;
/* 1846 */     updateUI();
/*      */   }
/*      */ 
/*      */   public void setUI(OptionPaneUI paramOptionPaneUI)
/*      */   {
/* 1860 */     if (this.ui != paramOptionPaneUI) {
/* 1861 */       super.setUI(paramOptionPaneUI);
/* 1862 */       invalidate();
/*      */     }
/*      */   }
/*      */ 
/*      */   public OptionPaneUI getUI()
/*      */   {
/* 1872 */     return (OptionPaneUI)this.ui;
/*      */   }
/*      */ 
/*      */   public void updateUI()
/*      */   {
/* 1883 */     setUI((OptionPaneUI)UIManager.getUI(this));
/*      */   }
/*      */ 
/*      */   public String getUIClassID()
/*      */   {
/* 1896 */     return "OptionPaneUI";
/*      */   }
/*      */ 
/*      */   public void setMessage(Object paramObject)
/*      */   {
/* 1911 */     Object localObject = this.message;
/*      */ 
/* 1913 */     this.message = paramObject;
/* 1914 */     firePropertyChange("message", localObject, this.message);
/*      */   }
/*      */ 
/*      */   public Object getMessage()
/*      */   {
/* 1924 */     return this.message;
/*      */   }
/*      */ 
/*      */   public void setIcon(Icon paramIcon)
/*      */   {
/* 1939 */     Icon localIcon = this.icon;
/*      */ 
/* 1941 */     this.icon = paramIcon;
/* 1942 */     firePropertyChange("icon", localIcon, this.icon);
/*      */   }
/*      */ 
/*      */   public Icon getIcon()
/*      */   {
/* 1952 */     return this.icon;
/*      */   }
/*      */ 
/*      */   public void setValue(Object paramObject)
/*      */   {
/* 1966 */     Object localObject = this.value;
/*      */ 
/* 1968 */     this.value = paramObject;
/* 1969 */     firePropertyChange("value", localObject, this.value);
/*      */   }
/*      */ 
/*      */   public Object getValue()
/*      */   {
/* 1987 */     return this.value;
/*      */   }
/*      */ 
/*      */   public void setOptions(Object[] paramArrayOfObject)
/*      */   {
/* 2006 */     Object[] arrayOfObject = this.options;
/*      */ 
/* 2008 */     this.options = paramArrayOfObject;
/* 2009 */     firePropertyChange("options", arrayOfObject, this.options);
/*      */   }
/*      */ 
/*      */   public Object[] getOptions()
/*      */   {
/* 2019 */     if (this.options != null) {
/* 2020 */       int i = this.options.length;
/* 2021 */       Object[] arrayOfObject = new Object[i];
/*      */ 
/* 2023 */       System.arraycopy(this.options, 0, arrayOfObject, 0, i);
/* 2024 */       return arrayOfObject;
/*      */     }
/* 2026 */     return this.options;
/*      */   }
/*      */ 
/*      */   public void setInitialValue(Object paramObject)
/*      */   {
/* 2044 */     Object localObject = this.initialValue;
/*      */ 
/* 2046 */     this.initialValue = paramObject;
/* 2047 */     firePropertyChange("initialValue", localObject, this.initialValue);
/*      */   }
/*      */ 
/*      */   public Object getInitialValue()
/*      */   {
/* 2058 */     return this.initialValue;
/*      */   }
/*      */ 
/*      */   public void setMessageType(int paramInt)
/*      */   {
/* 2080 */     if ((paramInt != 0) && (paramInt != 1) && (paramInt != 2) && (paramInt != 3) && (paramInt != -1))
/*      */     {
/* 2083 */       throw new RuntimeException("JOptionPane: type must be one of JOptionPane.ERROR_MESSAGE, JOptionPane.INFORMATION_MESSAGE, JOptionPane.WARNING_MESSAGE, JOptionPane.QUESTION_MESSAGE or JOptionPane.PLAIN_MESSAGE");
/*      */     }
/* 2085 */     int i = this.messageType;
/*      */ 
/* 2087 */     this.messageType = paramInt;
/* 2088 */     firePropertyChange("messageType", i, this.messageType);
/*      */   }
/*      */ 
/*      */   public int getMessageType()
/*      */   {
/* 2099 */     return this.messageType;
/*      */   }
/*      */ 
/*      */   public void setOptionType(int paramInt)
/*      */   {
/* 2122 */     if ((paramInt != -1) && (paramInt != 0) && (paramInt != 1) && (paramInt != 2))
/*      */     {
/* 2124 */       throw new RuntimeException("JOptionPane: option type must be one of JOptionPane.DEFAULT_OPTION, JOptionPane.YES_NO_OPTION, JOptionPane.YES_NO_CANCEL_OPTION or JOptionPane.OK_CANCEL_OPTION");
/*      */     }
/* 2126 */     int i = this.optionType;
/*      */ 
/* 2128 */     this.optionType = paramInt;
/* 2129 */     firePropertyChange("optionType", i, this.optionType);
/*      */   }
/*      */ 
/*      */   public int getOptionType()
/*      */   {
/* 2140 */     return this.optionType;
/*      */   }
/*      */ 
/*      */   public void setSelectionValues(Object[] paramArrayOfObject)
/*      */   {
/* 2166 */     Object[] arrayOfObject = this.selectionValues;
/*      */ 
/* 2168 */     this.selectionValues = paramArrayOfObject;
/* 2169 */     firePropertyChange("selectionValues", arrayOfObject, paramArrayOfObject);
/* 2170 */     if (this.selectionValues != null)
/* 2171 */       setWantsInput(true);
/*      */   }
/*      */ 
/*      */   public Object[] getSelectionValues()
/*      */   {
/* 2181 */     return this.selectionValues;
/*      */   }
/*      */ 
/*      */   public void setInitialSelectionValue(Object paramObject)
/*      */   {
/* 2195 */     Object localObject = this.initialSelectionValue;
/*      */ 
/* 2197 */     this.initialSelectionValue = paramObject;
/* 2198 */     firePropertyChange("initialSelectionValue", localObject, paramObject);
/*      */   }
/*      */ 
/*      */   public Object getInitialSelectionValue()
/*      */   {
/* 2210 */     return this.initialSelectionValue;
/*      */   }
/*      */ 
/*      */   public void setInputValue(Object paramObject)
/*      */   {
/* 2233 */     Object localObject = this.inputValue;
/*      */ 
/* 2235 */     this.inputValue = paramObject;
/* 2236 */     firePropertyChange("inputValue", localObject, paramObject);
/*      */   }
/*      */ 
/*      */   public Object getInputValue()
/*      */   {
/* 2252 */     return this.inputValue;
/*      */   }
/*      */ 
/*      */   public int getMaxCharactersPerLineCount()
/*      */   {
/* 2264 */     return 2147483647;
/*      */   }
/*      */ 
/*      */   public void setWantsInput(boolean paramBoolean)
/*      */   {
/* 2287 */     boolean bool = this.wantsInput;
/*      */ 
/* 2289 */     this.wantsInput = paramBoolean;
/* 2290 */     firePropertyChange("wantsInput", bool, paramBoolean);
/*      */   }
/*      */ 
/*      */   public boolean getWantsInput()
/*      */   {
/* 2300 */     return this.wantsInput;
/*      */   }
/*      */ 
/*      */   public void selectInitialValue()
/*      */   {
/* 2310 */     OptionPaneUI localOptionPaneUI = getUI();
/* 2311 */     if (localOptionPaneUI != null)
/* 2312 */       localOptionPaneUI.selectInitialValue(this);
/*      */   }
/*      */ 
/*      */   private static int styleFromMessageType(int paramInt)
/*      */   {
/* 2318 */     switch (paramInt) {
/*      */     case 0:
/* 2320 */       return 4;
/*      */     case 3:
/* 2322 */       return 7;
/*      */     case 2:
/* 2324 */       return 8;
/*      */     case 1:
/* 2326 */       return 3;
/*      */     case -1:
/*      */     }
/* 2329 */     return 2;
/*      */   }
/*      */ 
/*      */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*      */     throws IOException
/*      */   {
/* 2335 */     Vector localVector1 = new Vector();
/*      */ 
/* 2337 */     paramObjectOutputStream.defaultWriteObject();
/*      */ 
/* 2339 */     if ((this.icon != null) && ((this.icon instanceof Serializable))) {
/* 2340 */       localVector1.addElement("icon");
/* 2341 */       localVector1.addElement(this.icon);
/*      */     }
/*      */ 
/* 2344 */     if ((this.message != null) && ((this.message instanceof Serializable))) {
/* 2345 */       localVector1.addElement("message");
/* 2346 */       localVector1.addElement(this.message);
/*      */     }
/*      */     int j;
/* 2349 */     if (this.options != null) {
/* 2350 */       Vector localVector2 = new Vector();
/*      */ 
/* 2352 */       j = 0; int k = this.options.length;
/* 2353 */       for (; j < k; j++)
/* 2354 */         if ((this.options[j] instanceof Serializable))
/* 2355 */           localVector2.addElement(this.options[j]);
/* 2356 */       if (localVector2.size() > 0) {
/* 2357 */         j = localVector2.size();
/* 2358 */         Object[] arrayOfObject = new Object[j];
/*      */ 
/* 2360 */         localVector2.copyInto(arrayOfObject);
/* 2361 */         localVector1.addElement("options");
/* 2362 */         localVector1.addElement(arrayOfObject);
/*      */       }
/*      */     }
/*      */ 
/* 2366 */     if ((this.initialValue != null) && ((this.initialValue instanceof Serializable))) {
/* 2367 */       localVector1.addElement("initialValue");
/* 2368 */       localVector1.addElement(this.initialValue);
/*      */     }
/*      */ 
/* 2371 */     if ((this.value != null) && ((this.value instanceof Serializable))) {
/* 2372 */       localVector1.addElement("value");
/* 2373 */       localVector1.addElement(this.value);
/*      */     }
/*      */ 
/* 2376 */     if (this.selectionValues != null) {
/* 2377 */       int i = 1;
/*      */ 
/* 2379 */       j = 0; int m = this.selectionValues.length;
/* 2380 */       for (; j < m; j++) {
/* 2381 */         if ((this.selectionValues[j] != null) && (!(this.selectionValues[j] instanceof Serializable)))
/*      */         {
/* 2383 */           i = 0;
/* 2384 */           break;
/*      */         }
/*      */       }
/* 2387 */       if (i != 0) {
/* 2388 */         localVector1.addElement("selectionValues");
/* 2389 */         localVector1.addElement(this.selectionValues);
/*      */       }
/*      */     }
/*      */ 
/* 2393 */     if ((this.inputValue != null) && ((this.inputValue instanceof Serializable))) {
/* 2394 */       localVector1.addElement("inputValue");
/* 2395 */       localVector1.addElement(this.inputValue);
/*      */     }
/*      */ 
/* 2398 */     if ((this.initialSelectionValue != null) && ((this.initialSelectionValue instanceof Serializable)))
/*      */     {
/* 2400 */       localVector1.addElement("initialSelectionValue");
/* 2401 */       localVector1.addElement(this.initialSelectionValue);
/*      */     }
/* 2403 */     paramObjectOutputStream.writeObject(localVector1);
/*      */   }
/*      */ 
/*      */   private void readObject(ObjectInputStream paramObjectInputStream) throws IOException, ClassNotFoundException
/*      */   {
/* 2408 */     paramObjectInputStream.defaultReadObject();
/*      */ 
/* 2410 */     Vector localVector = (Vector)paramObjectInputStream.readObject();
/* 2411 */     int i = 0;
/* 2412 */     int j = localVector.size();
/*      */ 
/* 2414 */     if ((i < j) && (localVector.elementAt(i).equals("icon")))
/*      */     {
/* 2416 */       this.icon = ((Icon)localVector.elementAt(++i));
/* 2417 */       i++;
/*      */     }
/* 2419 */     if ((i < j) && (localVector.elementAt(i).equals("message")))
/*      */     {
/* 2421 */       this.message = localVector.elementAt(++i);
/* 2422 */       i++;
/*      */     }
/* 2424 */     if ((i < j) && (localVector.elementAt(i).equals("options")))
/*      */     {
/* 2426 */       this.options = ((Object[])localVector.elementAt(++i));
/* 2427 */       i++;
/*      */     }
/* 2429 */     if ((i < j) && (localVector.elementAt(i).equals("initialValue")))
/*      */     {
/* 2431 */       this.initialValue = localVector.elementAt(++i);
/* 2432 */       i++;
/*      */     }
/* 2434 */     if ((i < j) && (localVector.elementAt(i).equals("value")))
/*      */     {
/* 2436 */       this.value = localVector.elementAt(++i);
/* 2437 */       i++;
/*      */     }
/* 2439 */     if ((i < j) && (localVector.elementAt(i).equals("selectionValues")))
/*      */     {
/* 2441 */       this.selectionValues = ((Object[])localVector.elementAt(++i));
/* 2442 */       i++;
/*      */     }
/* 2444 */     if ((i < j) && (localVector.elementAt(i).equals("inputValue")))
/*      */     {
/* 2446 */       this.inputValue = localVector.elementAt(++i);
/* 2447 */       i++;
/*      */     }
/* 2449 */     if ((i < j) && (localVector.elementAt(i).equals("initialSelectionValue")))
/*      */     {
/* 2451 */       this.initialSelectionValue = localVector.elementAt(++i);
/* 2452 */       i++;
/*      */     }
/* 2454 */     if (getUIClassID().equals("OptionPaneUI")) {
/* 2455 */       byte b = JComponent.getWriteObjCounter(this);
/* 2456 */       b = (byte)(b - 1); JComponent.setWriteObjCounter(this, b);
/* 2457 */       if ((b == 0) && (this.ui != null))
/* 2458 */         this.ui.installUI(this);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected String paramString()
/*      */   {
/* 2475 */     String str1 = this.icon != null ? this.icon.toString() : "";
/*      */ 
/* 2477 */     String str2 = this.initialValue != null ? this.initialValue.toString() : "";
/*      */ 
/* 2479 */     String str3 = this.message != null ? this.message.toString() : "";
/*      */     String str4;
/* 2482 */     if (this.messageType == 0)
/* 2483 */       str4 = "ERROR_MESSAGE";
/* 2484 */     else if (this.messageType == 1)
/* 2485 */       str4 = "INFORMATION_MESSAGE";
/* 2486 */     else if (this.messageType == 2)
/* 2487 */       str4 = "WARNING_MESSAGE";
/* 2488 */     else if (this.messageType == 3)
/* 2489 */       str4 = "QUESTION_MESSAGE";
/* 2490 */     else if (this.messageType == -1)
/* 2491 */       str4 = "PLAIN_MESSAGE";
/* 2492 */     else str4 = "";
/*      */     String str5;
/* 2494 */     if (this.optionType == -1)
/* 2495 */       str5 = "DEFAULT_OPTION";
/* 2496 */     else if (this.optionType == 0)
/* 2497 */       str5 = "YES_NO_OPTION";
/* 2498 */     else if (this.optionType == 1)
/* 2499 */       str5 = "YES_NO_CANCEL_OPTION";
/* 2500 */     else if (this.optionType == 2)
/* 2501 */       str5 = "OK_CANCEL_OPTION";
/* 2502 */     else str5 = "";
/* 2503 */     String str6 = this.wantsInput ? "true" : "false";
/*      */ 
/* 2506 */     return super.paramString() + ",icon=" + str1 + ",initialValue=" + str2 + ",message=" + str3 + ",messageType=" + str4 + ",optionType=" + str5 + ",wantsInput=" + str6;
/*      */   }
/*      */ 
/*      */   public AccessibleContext getAccessibleContext()
/*      */   {
/* 2559 */     if (this.accessibleContext == null) {
/* 2560 */       this.accessibleContext = new AccessibleJOptionPane();
/*      */     }
/* 2562 */     return this.accessibleContext;
/*      */   }
/*      */ 
/*      */   protected class AccessibleJOptionPane extends JComponent.AccessibleJComponent
/*      */   {
/*      */     protected AccessibleJOptionPane()
/*      */     {
/* 2580 */       super();
/*      */     }
/*      */ 
/*      */     public AccessibleRole getAccessibleRole()
/*      */     {
/* 2589 */       switch (JOptionPane.this.messageType) {
/*      */       case 0:
/*      */       case 1:
/*      */       case 2:
/* 2593 */         return AccessibleRole.ALERT;
/*      */       }
/*      */ 
/* 2596 */       return AccessibleRole.OPTION_PANE;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class ModalPrivilegedAction
/*      */     implements PrivilegedAction<Method>
/*      */   {
/*      */     private Class<?> clazz;
/*      */     private String methodName;
/*      */ 
/*      */     public ModalPrivilegedAction(Class<?> paramClass, String paramString)
/*      */     {
/* 2523 */       this.clazz = paramClass;
/* 2524 */       this.methodName = paramString;
/*      */     }
/*      */ 
/*      */     public Method run() {
/* 2528 */       Method localMethod = null;
/*      */       try {
/* 2530 */         localMethod = this.clazz.getDeclaredMethod(this.methodName, (Class[])null);
/*      */       } catch (NoSuchMethodException localNoSuchMethodException) {
/*      */       }
/* 2533 */       if (localMethod != null) {
/* 2534 */         localMethod.setAccessible(true);
/*      */       }
/* 2536 */       return localMethod;
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.JOptionPane
 * JD-Core Version:    0.6.2
 */