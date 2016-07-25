/*     */ package javax.swing.plaf.basic;
/*     */ 
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.FocusEvent;
/*     */ import java.awt.event.FocusListener;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.awt.event.MouseListener;
/*     */ import java.awt.event.MouseMotionListener;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import javax.swing.AbstractButton;
/*     */ import javax.swing.ButtonModel;
/*     */ import javax.swing.InputMap;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JRootPane;
/*     */ import javax.swing.KeyStroke;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.event.ChangeEvent;
/*     */ import javax.swing.event.ChangeListener;
/*     */ import javax.swing.plaf.ComponentInputMapUIResource;
/*     */ import sun.swing.DefaultLookup;
/*     */ import sun.swing.UIAction;
/*     */ 
/*     */ public class BasicButtonListener
/*     */   implements MouseListener, MouseMotionListener, FocusListener, ChangeListener, PropertyChangeListener
/*     */ {
/*  49 */   private long lastPressedTimestamp = -1L;
/*  50 */   private boolean shouldDiscardRelease = false;
/*     */ 
/*     */   static void loadActionMap(LazyActionMap paramLazyActionMap)
/*     */   {
/*  56 */     paramLazyActionMap.put(new Actions("pressed"));
/*  57 */     paramLazyActionMap.put(new Actions("released"));
/*     */   }
/*     */ 
/*     */   public BasicButtonListener(AbstractButton paramAbstractButton)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent) {
/*  65 */     String str = paramPropertyChangeEvent.getPropertyName();
/*  66 */     if (str == "mnemonic") {
/*  67 */       updateMnemonicBinding((AbstractButton)paramPropertyChangeEvent.getSource());
/*     */     }
/*  69 */     else if (str == "contentAreaFilled") {
/*  70 */       checkOpacity((AbstractButton)paramPropertyChangeEvent.getSource());
/*     */     }
/*  72 */     else if ((str == "text") || ("font" == str) || ("foreground" == str))
/*     */     {
/*  74 */       AbstractButton localAbstractButton = (AbstractButton)paramPropertyChangeEvent.getSource();
/*  75 */       BasicHTML.updateRenderer(localAbstractButton, localAbstractButton.getText());
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void checkOpacity(AbstractButton paramAbstractButton) {
/*  80 */     paramAbstractButton.setOpaque(paramAbstractButton.isContentAreaFilled());
/*     */   }
/*     */ 
/*     */   public void installKeyboardActions(JComponent paramJComponent)
/*     */   {
/*  88 */     AbstractButton localAbstractButton = (AbstractButton)paramJComponent;
/*     */ 
/*  90 */     updateMnemonicBinding(localAbstractButton);
/*     */ 
/*  92 */     LazyActionMap.installLazyActionMap(paramJComponent, BasicButtonListener.class, "Button.actionMap");
/*     */ 
/*  95 */     InputMap localInputMap = getInputMap(0, paramJComponent);
/*     */ 
/*  97 */     SwingUtilities.replaceUIInputMap(paramJComponent, 0, localInputMap);
/*     */   }
/*     */ 
/*     */   public void uninstallKeyboardActions(JComponent paramJComponent)
/*     */   {
/* 104 */     SwingUtilities.replaceUIInputMap(paramJComponent, 2, null);
/*     */ 
/* 106 */     SwingUtilities.replaceUIInputMap(paramJComponent, 0, null);
/* 107 */     SwingUtilities.replaceUIActionMap(paramJComponent, null);
/*     */   }
/*     */ 
/*     */   InputMap getInputMap(int paramInt, JComponent paramJComponent)
/*     */   {
/* 115 */     if (paramInt == 0) {
/* 116 */       BasicButtonUI localBasicButtonUI = (BasicButtonUI)BasicLookAndFeel.getUIOfType(((AbstractButton)paramJComponent).getUI(), BasicButtonUI.class);
/*     */ 
/* 118 */       if (localBasicButtonUI != null) {
/* 119 */         return (InputMap)DefaultLookup.get(paramJComponent, localBasicButtonUI, localBasicButtonUI.getPropertyPrefix() + "focusInputMap");
/*     */       }
/*     */     }
/*     */ 
/* 123 */     return null;
/*     */   }
/*     */ 
/*     */   void updateMnemonicBinding(AbstractButton paramAbstractButton)
/*     */   {
/* 131 */     int i = paramAbstractButton.getMnemonic();
/*     */     Object localObject;
/* 132 */     if (i != 0) {
/* 133 */       localObject = SwingUtilities.getUIInputMap(paramAbstractButton, 2);
/*     */ 
/* 136 */       if (localObject == null) {
/* 137 */         localObject = new ComponentInputMapUIResource(paramAbstractButton);
/* 138 */         SwingUtilities.replaceUIInputMap(paramAbstractButton, 2, (InputMap)localObject);
/*     */       }
/*     */ 
/* 141 */       ((InputMap)localObject).clear();
/* 142 */       ((InputMap)localObject).put(KeyStroke.getKeyStroke(i, BasicLookAndFeel.getFocusAcceleratorKeyMask(), false), "pressed");
/*     */ 
/* 144 */       ((InputMap)localObject).put(KeyStroke.getKeyStroke(i, BasicLookAndFeel.getFocusAcceleratorKeyMask(), true), "released");
/*     */ 
/* 146 */       ((InputMap)localObject).put(KeyStroke.getKeyStroke(i, 0, true), "released");
/*     */     }
/*     */     else {
/* 149 */       localObject = SwingUtilities.getUIInputMap(paramAbstractButton, 2);
/*     */ 
/* 151 */       if (localObject != null)
/* 152 */         ((InputMap)localObject).clear();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void stateChanged(ChangeEvent paramChangeEvent)
/*     */   {
/* 158 */     AbstractButton localAbstractButton = (AbstractButton)paramChangeEvent.getSource();
/* 159 */     localAbstractButton.repaint();
/*     */   }
/*     */ 
/*     */   public void focusGained(FocusEvent paramFocusEvent) {
/* 163 */     AbstractButton localAbstractButton = (AbstractButton)paramFocusEvent.getSource();
/* 164 */     if (((localAbstractButton instanceof JButton)) && (((JButton)localAbstractButton).isDefaultCapable())) {
/* 165 */       JRootPane localJRootPane = localAbstractButton.getRootPane();
/* 166 */       if (localJRootPane != null) {
/* 167 */         BasicButtonUI localBasicButtonUI = (BasicButtonUI)BasicLookAndFeel.getUIOfType(localAbstractButton.getUI(), BasicButtonUI.class);
/*     */ 
/* 169 */         if ((localBasicButtonUI != null) && (DefaultLookup.getBoolean(localAbstractButton, localBasicButtonUI, localBasicButtonUI.getPropertyPrefix() + "defaultButtonFollowsFocus", true)))
/*     */         {
/* 172 */           localJRootPane.putClientProperty("temporaryDefaultButton", localAbstractButton);
/* 173 */           localJRootPane.setDefaultButton((JButton)localAbstractButton);
/* 174 */           localJRootPane.putClientProperty("temporaryDefaultButton", null);
/*     */         }
/*     */       }
/*     */     }
/* 178 */     localAbstractButton.repaint();
/*     */   }
/*     */ 
/*     */   public void focusLost(FocusEvent paramFocusEvent) {
/* 182 */     AbstractButton localAbstractButton = (AbstractButton)paramFocusEvent.getSource();
/* 183 */     JRootPane localJRootPane = localAbstractButton.getRootPane();
/* 184 */     if (localJRootPane != null) {
/* 185 */       localObject = (JButton)localJRootPane.getClientProperty("initialDefaultButton");
/* 186 */       if (localAbstractButton != localObject) {
/* 187 */         BasicButtonUI localBasicButtonUI = (BasicButtonUI)BasicLookAndFeel.getUIOfType(localAbstractButton.getUI(), BasicButtonUI.class);
/*     */ 
/* 189 */         if ((localBasicButtonUI != null) && (DefaultLookup.getBoolean(localAbstractButton, localBasicButtonUI, localBasicButtonUI.getPropertyPrefix() + "defaultButtonFollowsFocus", true)))
/*     */         {
/* 192 */           localJRootPane.setDefaultButton((JButton)localObject);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 197 */     Object localObject = localAbstractButton.getModel();
/* 198 */     ((ButtonModel)localObject).setPressed(false);
/* 199 */     ((ButtonModel)localObject).setArmed(false);
/* 200 */     localAbstractButton.repaint();
/*     */   }
/*     */ 
/*     */   public void mouseMoved(MouseEvent paramMouseEvent)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void mouseDragged(MouseEvent paramMouseEvent) {
/*     */   }
/*     */ 
/*     */   public void mouseClicked(MouseEvent paramMouseEvent) {
/*     */   }
/*     */ 
/*     */   public void mousePressed(MouseEvent paramMouseEvent) {
/* 214 */     if (SwingUtilities.isLeftMouseButton(paramMouseEvent)) {
/* 215 */       AbstractButton localAbstractButton = (AbstractButton)paramMouseEvent.getSource();
/*     */ 
/* 217 */       if (localAbstractButton.contains(paramMouseEvent.getX(), paramMouseEvent.getY())) {
/* 218 */         long l1 = localAbstractButton.getMultiClickThreshhold();
/* 219 */         long l2 = this.lastPressedTimestamp;
/* 220 */         long l3 = this.lastPressedTimestamp = paramMouseEvent.getWhen();
/* 221 */         if ((l2 != -1L) && (l3 - l2 < l1)) {
/* 222 */           this.shouldDiscardRelease = true;
/* 223 */           return;
/*     */         }
/*     */ 
/* 226 */         ButtonModel localButtonModel = localAbstractButton.getModel();
/* 227 */         if (!localButtonModel.isEnabled())
/*     */         {
/* 229 */           return;
/*     */         }
/* 231 */         if (!localButtonModel.isArmed())
/*     */         {
/* 233 */           localButtonModel.setArmed(true);
/*     */         }
/* 235 */         localButtonModel.setPressed(true);
/* 236 */         if ((!localAbstractButton.hasFocus()) && (localAbstractButton.isRequestFocusEnabled()))
/* 237 */           localAbstractButton.requestFocus();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void mouseReleased(MouseEvent paramMouseEvent)
/*     */   {
/* 244 */     if (SwingUtilities.isLeftMouseButton(paramMouseEvent))
/*     */     {
/* 246 */       if (this.shouldDiscardRelease) {
/* 247 */         this.shouldDiscardRelease = false;
/* 248 */         return;
/*     */       }
/* 250 */       AbstractButton localAbstractButton = (AbstractButton)paramMouseEvent.getSource();
/* 251 */       ButtonModel localButtonModel = localAbstractButton.getModel();
/* 252 */       localButtonModel.setPressed(false);
/* 253 */       localButtonModel.setArmed(false);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void mouseEntered(MouseEvent paramMouseEvent) {
/* 258 */     AbstractButton localAbstractButton = (AbstractButton)paramMouseEvent.getSource();
/* 259 */     ButtonModel localButtonModel = localAbstractButton.getModel();
/* 260 */     if ((localAbstractButton.isRolloverEnabled()) && (!SwingUtilities.isLeftMouseButton(paramMouseEvent))) {
/* 261 */       localButtonModel.setRollover(true);
/*     */     }
/* 263 */     if (localButtonModel.isPressed())
/* 264 */       localButtonModel.setArmed(true);
/*     */   }
/*     */ 
/*     */   public void mouseExited(MouseEvent paramMouseEvent) {
/* 268 */     AbstractButton localAbstractButton = (AbstractButton)paramMouseEvent.getSource();
/* 269 */     ButtonModel localButtonModel = localAbstractButton.getModel();
/* 270 */     if (localAbstractButton.isRolloverEnabled()) {
/* 271 */       localButtonModel.setRollover(false);
/*     */     }
/* 273 */     localButtonModel.setArmed(false);
/*     */   }
/*     */ 
/*     */   private static class Actions extends UIAction
/*     */   {
/*     */     private static final String PRESS = "pressed";
/*     */     private static final String RELEASE = "released";
/*     */ 
/*     */     Actions(String paramString)
/*     */     {
/* 287 */       super();
/*     */     }
/*     */ 
/*     */     public void actionPerformed(ActionEvent paramActionEvent) {
/* 291 */       AbstractButton localAbstractButton = (AbstractButton)paramActionEvent.getSource();
/* 292 */       String str = getName();
/*     */       ButtonModel localButtonModel;
/* 293 */       if (str == "pressed") {
/* 294 */         localButtonModel = localAbstractButton.getModel();
/* 295 */         localButtonModel.setArmed(true);
/* 296 */         localButtonModel.setPressed(true);
/* 297 */         if (!localAbstractButton.hasFocus()) {
/* 298 */           localAbstractButton.requestFocus();
/*     */         }
/*     */       }
/* 301 */       else if (str == "released") {
/* 302 */         localButtonModel = localAbstractButton.getModel();
/* 303 */         localButtonModel.setPressed(false);
/* 304 */         localButtonModel.setArmed(false);
/*     */       }
/*     */     }
/*     */ 
/*     */     public boolean isEnabled(Object paramObject) {
/* 309 */       if ((paramObject != null) && ((paramObject instanceof AbstractButton)) && (!((AbstractButton)paramObject).getModel().isEnabled()))
/*     */       {
/* 311 */         return false;
/*     */       }
/* 313 */       return true;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.basic.BasicButtonListener
 * JD-Core Version:    0.6.2
 */