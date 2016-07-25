/*     */ package javax.swing.plaf.basic;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.KeyboardFocusManager;
/*     */ import java.awt.Point;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import javax.swing.ButtonModel;
/*     */ import javax.swing.ComponentInputMap;
/*     */ import javax.swing.InputMap;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JPopupMenu;
/*     */ import javax.swing.JRootPane;
/*     */ import javax.swing.LookAndFeel;
/*     */ import javax.swing.MenuSelectionManager;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.plaf.ComponentInputMapUIResource;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.RootPaneUI;
/*     */ import sun.swing.DefaultLookup;
/*     */ import sun.swing.UIAction;
/*     */ 
/*     */ public class BasicRootPaneUI extends RootPaneUI
/*     */   implements PropertyChangeListener
/*     */ {
/*  49 */   private static RootPaneUI rootPaneUI = new BasicRootPaneUI();
/*     */ 
/*     */   public static ComponentUI createUI(JComponent paramJComponent) {
/*  52 */     return rootPaneUI;
/*     */   }
/*     */ 
/*     */   public void installUI(JComponent paramJComponent) {
/*  56 */     installDefaults((JRootPane)paramJComponent);
/*  57 */     installComponents((JRootPane)paramJComponent);
/*  58 */     installListeners((JRootPane)paramJComponent);
/*  59 */     installKeyboardActions((JRootPane)paramJComponent);
/*     */   }
/*     */ 
/*     */   public void uninstallUI(JComponent paramJComponent)
/*     */   {
/*  64 */     uninstallDefaults((JRootPane)paramJComponent);
/*  65 */     uninstallComponents((JRootPane)paramJComponent);
/*  66 */     uninstallListeners((JRootPane)paramJComponent);
/*  67 */     uninstallKeyboardActions((JRootPane)paramJComponent);
/*     */   }
/*     */ 
/*     */   protected void installDefaults(JRootPane paramJRootPane) {
/*  71 */     LookAndFeel.installProperty(paramJRootPane, "opaque", Boolean.FALSE);
/*     */   }
/*     */ 
/*     */   protected void installComponents(JRootPane paramJRootPane) {
/*     */   }
/*     */ 
/*     */   protected void installListeners(JRootPane paramJRootPane) {
/*  78 */     paramJRootPane.addPropertyChangeListener(this);
/*     */   }
/*     */ 
/*     */   protected void installKeyboardActions(JRootPane paramJRootPane) {
/*  82 */     InputMap localInputMap = getInputMap(2, paramJRootPane);
/*  83 */     SwingUtilities.replaceUIInputMap(paramJRootPane, 2, localInputMap);
/*     */ 
/*  85 */     localInputMap = getInputMap(1, paramJRootPane);
/*     */ 
/*  87 */     SwingUtilities.replaceUIInputMap(paramJRootPane, 1, localInputMap);
/*     */ 
/*  90 */     LazyActionMap.installLazyActionMap(paramJRootPane, BasicRootPaneUI.class, "RootPane.actionMap");
/*     */ 
/*  92 */     updateDefaultButtonBindings(paramJRootPane);
/*     */   }
/*     */ 
/*     */   protected void uninstallDefaults(JRootPane paramJRootPane) {
/*     */   }
/*     */ 
/*     */   protected void uninstallComponents(JRootPane paramJRootPane) {
/*     */   }
/*     */ 
/*     */   protected void uninstallListeners(JRootPane paramJRootPane) {
/* 102 */     paramJRootPane.removePropertyChangeListener(this);
/*     */   }
/*     */ 
/*     */   protected void uninstallKeyboardActions(JRootPane paramJRootPane) {
/* 106 */     SwingUtilities.replaceUIInputMap(paramJRootPane, 2, null);
/*     */ 
/* 108 */     SwingUtilities.replaceUIActionMap(paramJRootPane, null);
/*     */   }
/*     */ 
/*     */   InputMap getInputMap(int paramInt, JComponent paramJComponent) {
/* 112 */     if (paramInt == 1) {
/* 113 */       return (InputMap)DefaultLookup.get(paramJComponent, this, "RootPane.ancestorInputMap");
/*     */     }
/*     */ 
/* 117 */     if (paramInt == 2) {
/* 118 */       return createInputMap(paramInt, paramJComponent);
/*     */     }
/* 120 */     return null;
/*     */   }
/*     */ 
/*     */   ComponentInputMap createInputMap(int paramInt, JComponent paramJComponent) {
/* 124 */     return new RootPaneInputMap(paramJComponent);
/*     */   }
/*     */ 
/*     */   static void loadActionMap(LazyActionMap paramLazyActionMap) {
/* 128 */     paramLazyActionMap.put(new Actions("press"));
/* 129 */     paramLazyActionMap.put(new Actions("release"));
/* 130 */     paramLazyActionMap.put(new Actions("postPopup"));
/*     */   }
/*     */ 
/*     */   void updateDefaultButtonBindings(JRootPane paramJRootPane)
/*     */   {
/* 139 */     InputMap localInputMap = SwingUtilities.getUIInputMap(paramJRootPane, 2);
/*     */ 
/* 141 */     while ((localInputMap != null) && (!(localInputMap instanceof RootPaneInputMap))) {
/* 142 */       localInputMap = localInputMap.getParent();
/*     */     }
/* 144 */     if (localInputMap != null) {
/* 145 */       localInputMap.clear();
/* 146 */       if (paramJRootPane.getDefaultButton() != null) {
/* 147 */         Object[] arrayOfObject = (Object[])DefaultLookup.get(paramJRootPane, this, "RootPane.defaultButtonWindowKeyBindings");
/*     */ 
/* 149 */         if (arrayOfObject != null)
/* 150 */           LookAndFeel.loadKeyBindings(localInputMap, arrayOfObject);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent)
/*     */   {
/* 162 */     if (paramPropertyChangeEvent.getPropertyName().equals("defaultButton")) {
/* 163 */       JRootPane localJRootPane = (JRootPane)paramPropertyChangeEvent.getSource();
/* 164 */       updateDefaultButtonBindings(localJRootPane);
/* 165 */       if (localJRootPane.getClientProperty("temporaryDefaultButton") == null)
/* 166 */         localJRootPane.putClientProperty("initialDefaultButton", paramPropertyChangeEvent.getNewValue());
/*     */     }
/*     */   }
/*     */ 
/*     */   static class Actions extends UIAction {
/*     */     public static final String PRESS = "press";
/*     */     public static final String RELEASE = "release";
/*     */     public static final String POST_POPUP = "postPopup";
/*     */ 
/*     */     Actions(String paramString) {
/* 178 */       super();
/*     */     }
/*     */ 
/*     */     public void actionPerformed(ActionEvent paramActionEvent) {
/* 182 */       JRootPane localJRootPane = (JRootPane)paramActionEvent.getSource();
/* 183 */       JButton localJButton = localJRootPane.getDefaultButton();
/* 184 */       String str = getName();
/*     */ 
/* 186 */       if (str == "postPopup") {
/* 187 */         Component localComponent = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
/*     */ 
/* 191 */         if ((localComponent instanceof JComponent)) {
/* 192 */           JComponent localJComponent = (JComponent)localComponent;
/* 193 */           JPopupMenu localJPopupMenu = localJComponent.getComponentPopupMenu();
/* 194 */           if (localJPopupMenu != null) {
/* 195 */             Point localPoint = localJComponent.getPopupLocation(null);
/* 196 */             if (localPoint == null) {
/* 197 */               Rectangle localRectangle = localJComponent.getVisibleRect();
/* 198 */               localPoint = new Point(localRectangle.x + localRectangle.width / 2, localRectangle.y + localRectangle.height / 2);
/*     */             }
/*     */ 
/* 201 */             localJPopupMenu.show(localComponent, localPoint.x, localPoint.y);
/*     */           }
/*     */         }
/*     */       }
/* 205 */       else if ((localJButton != null) && (SwingUtilities.getRootPane(localJButton) == localJRootPane))
/*     */       {
/* 207 */         if (str == "press")
/* 208 */           localJButton.doClick(20);
/*     */       }
/*     */     }
/*     */ 
/*     */     public boolean isEnabled(Object paramObject)
/*     */     {
/* 214 */       String str = getName();
/*     */       Object localObject;
/* 215 */       if (str == "postPopup") {
/* 216 */         localObject = MenuSelectionManager.defaultManager().getSelectedPath();
/*     */ 
/* 219 */         if ((localObject != null) && (localObject.length != 0)) {
/* 220 */           return false;
/*     */         }
/*     */ 
/* 224 */         Component localComponent = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
/*     */ 
/* 227 */         if ((localComponent instanceof JComponent)) {
/* 228 */           JComponent localJComponent = (JComponent)localComponent;
/* 229 */           return localJComponent.getComponentPopupMenu() != null;
/*     */         }
/*     */ 
/* 232 */         return false;
/*     */       }
/*     */ 
/* 235 */       if ((paramObject != null) && ((paramObject instanceof JRootPane))) {
/* 236 */         localObject = ((JRootPane)paramObject).getDefaultButton();
/* 237 */         return (localObject != null) && (((JButton)localObject).getModel().isEnabled());
/*     */       }
/* 239 */       return true;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class RootPaneInputMap extends ComponentInputMapUIResource {
/*     */     public RootPaneInputMap(JComponent paramJComponent) {
/* 245 */       super();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.basic.BasicRootPaneUI
 * JD-Core Version:    0.6.2
 */