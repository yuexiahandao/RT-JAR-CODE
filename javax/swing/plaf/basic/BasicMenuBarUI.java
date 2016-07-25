/*     */ package javax.swing.plaf.basic;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ContainerEvent;
/*     */ import java.awt.event.ContainerListener;
/*     */ import javax.swing.ButtonModel;
/*     */ import javax.swing.InputMap;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JMenu;
/*     */ import javax.swing.JMenuBar;
/*     */ import javax.swing.LookAndFeel;
/*     */ import javax.swing.MenuElement;
/*     */ import javax.swing.MenuSelectionManager;
/*     */ import javax.swing.SingleSelectionModel;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.event.ChangeEvent;
/*     */ import javax.swing.event.ChangeListener;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.MenuBarUI;
/*     */ import javax.swing.plaf.UIResource;
/*     */ import sun.swing.DefaultLookup;
/*     */ import sun.swing.UIAction;
/*     */ 
/*     */ public class BasicMenuBarUI extends MenuBarUI
/*     */ {
/*     */   protected JMenuBar menuBar;
/*     */   protected ContainerListener containerListener;
/*     */   protected ChangeListener changeListener;
/*     */   private Handler handler;
/*     */ 
/*     */   public BasicMenuBarUI()
/*     */   {
/*  57 */     this.menuBar = null;
/*     */   }
/*     */ 
/*     */   public static ComponentUI createUI(JComponent paramJComponent)
/*     */   {
/*  63 */     return new BasicMenuBarUI();
/*     */   }
/*     */ 
/*     */   static void loadActionMap(LazyActionMap paramLazyActionMap) {
/*  67 */     paramLazyActionMap.put(new Actions("takeFocus"));
/*     */   }
/*     */ 
/*     */   public void installUI(JComponent paramJComponent) {
/*  71 */     this.menuBar = ((JMenuBar)paramJComponent);
/*     */ 
/*  73 */     installDefaults();
/*  74 */     installListeners();
/*  75 */     installKeyboardActions();
/*     */   }
/*     */ 
/*     */   protected void installDefaults()
/*     */   {
/*  80 */     if ((this.menuBar.getLayout() == null) || ((this.menuBar.getLayout() instanceof UIResource)))
/*     */     {
/*  82 */       this.menuBar.setLayout(new DefaultMenuLayout(this.menuBar, 2));
/*     */     }
/*     */ 
/*  85 */     LookAndFeel.installProperty(this.menuBar, "opaque", Boolean.TRUE);
/*  86 */     LookAndFeel.installBorder(this.menuBar, "MenuBar.border");
/*  87 */     LookAndFeel.installColorsAndFont(this.menuBar, "MenuBar.background", "MenuBar.foreground", "MenuBar.font");
/*     */   }
/*     */ 
/*     */   protected void installListeners()
/*     */   {
/*  94 */     this.containerListener = createContainerListener();
/*  95 */     this.changeListener = createChangeListener();
/*     */ 
/*  97 */     for (int i = 0; i < this.menuBar.getMenuCount(); i++) {
/*  98 */       JMenu localJMenu = this.menuBar.getMenu(i);
/*  99 */       if (localJMenu != null)
/* 100 */         localJMenu.getModel().addChangeListener(this.changeListener);
/*     */     }
/* 102 */     this.menuBar.addContainerListener(this.containerListener);
/*     */   }
/*     */ 
/*     */   protected void installKeyboardActions() {
/* 106 */     InputMap localInputMap = getInputMap(2);
/*     */ 
/* 108 */     SwingUtilities.replaceUIInputMap(this.menuBar, 2, localInputMap);
/*     */ 
/* 111 */     LazyActionMap.installLazyActionMap(this.menuBar, BasicMenuBarUI.class, "MenuBar.actionMap");
/*     */   }
/*     */ 
/*     */   InputMap getInputMap(int paramInt)
/*     */   {
/* 116 */     if (paramInt == 2) {
/* 117 */       Object[] arrayOfObject = (Object[])DefaultLookup.get(this.menuBar, this, "MenuBar.windowBindings");
/*     */ 
/* 119 */       if (arrayOfObject != null) {
/* 120 */         return LookAndFeel.makeComponentInputMap(this.menuBar, arrayOfObject);
/*     */       }
/*     */     }
/* 123 */     return null;
/*     */   }
/*     */ 
/*     */   public void uninstallUI(JComponent paramJComponent) {
/* 127 */     uninstallDefaults();
/* 128 */     uninstallListeners();
/* 129 */     uninstallKeyboardActions();
/*     */ 
/* 131 */     this.menuBar = null;
/*     */   }
/*     */ 
/*     */   protected void uninstallDefaults() {
/* 135 */     if (this.menuBar != null)
/* 136 */       LookAndFeel.uninstallBorder(this.menuBar);
/*     */   }
/*     */ 
/*     */   protected void uninstallListeners()
/*     */   {
/* 141 */     this.menuBar.removeContainerListener(this.containerListener);
/*     */ 
/* 143 */     for (int i = 0; i < this.menuBar.getMenuCount(); i++) {
/* 144 */       JMenu localJMenu = this.menuBar.getMenu(i);
/* 145 */       if (localJMenu != null) {
/* 146 */         localJMenu.getModel().removeChangeListener(this.changeListener);
/*     */       }
/*     */     }
/* 149 */     this.containerListener = null;
/* 150 */     this.changeListener = null;
/* 151 */     this.handler = null;
/*     */   }
/*     */ 
/*     */   protected void uninstallKeyboardActions() {
/* 155 */     SwingUtilities.replaceUIInputMap(this.menuBar, 2, null);
/*     */ 
/* 157 */     SwingUtilities.replaceUIActionMap(this.menuBar, null);
/*     */   }
/*     */ 
/*     */   protected ContainerListener createContainerListener() {
/* 161 */     return getHandler();
/*     */   }
/*     */ 
/*     */   protected ChangeListener createChangeListener() {
/* 165 */     return getHandler();
/*     */   }
/*     */ 
/*     */   private Handler getHandler() {
/* 169 */     if (this.handler == null) {
/* 170 */       this.handler = new Handler(null);
/*     */     }
/* 172 */     return this.handler;
/*     */   }
/*     */ 
/*     */   public Dimension getMinimumSize(JComponent paramJComponent)
/*     */   {
/* 177 */     return null;
/*     */   }
/*     */ 
/*     */   public Dimension getMaximumSize(JComponent paramJComponent) {
/* 181 */     return null;
/*     */   }
/*     */ 
/*     */   private static class Actions extends UIAction
/*     */   {
/*     */     private static final String TAKE_FOCUS = "takeFocus";
/*     */ 
/*     */     Actions(String paramString)
/*     */     {
/* 219 */       super();
/*     */     }
/*     */ 
/*     */     public void actionPerformed(ActionEvent paramActionEvent)
/*     */     {
/* 224 */       JMenuBar localJMenuBar = (JMenuBar)paramActionEvent.getSource();
/* 225 */       MenuSelectionManager localMenuSelectionManager = MenuSelectionManager.defaultManager();
/*     */ 
/* 228 */       JMenu localJMenu = localJMenuBar.getMenu(0);
/* 229 */       if (localJMenu != null) {
/* 230 */         MenuElement[] arrayOfMenuElement = new MenuElement[3];
/* 231 */         arrayOfMenuElement[0] = localJMenuBar;
/* 232 */         arrayOfMenuElement[1] = localJMenu;
/* 233 */         arrayOfMenuElement[2] = localJMenu.getPopupMenu();
/* 234 */         localMenuSelectionManager.setSelectedPath(arrayOfMenuElement);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private class Handler
/*     */     implements ChangeListener, ContainerListener
/*     */   {
/*     */     private Handler()
/*     */     {
/*     */     }
/*     */ 
/*     */     public void stateChanged(ChangeEvent paramChangeEvent)
/*     */     {
/* 190 */       int i = 0; for (int j = BasicMenuBarUI.this.menuBar.getMenuCount(); i < j; i++) {
/* 191 */         JMenu localJMenu = BasicMenuBarUI.this.menuBar.getMenu(i);
/* 192 */         if ((localJMenu != null) && (localJMenu.isSelected())) {
/* 193 */           BasicMenuBarUI.this.menuBar.getSelectionModel().setSelectedIndex(i);
/* 194 */           break;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/*     */     public void componentAdded(ContainerEvent paramContainerEvent)
/*     */     {
/* 203 */       Component localComponent = paramContainerEvent.getChild();
/* 204 */       if ((localComponent instanceof JMenu))
/* 205 */         ((JMenu)localComponent).getModel().addChangeListener(BasicMenuBarUI.this.changeListener); 
/*     */     }
/*     */ 
/* 208 */     public void componentRemoved(ContainerEvent paramContainerEvent) { Component localComponent = paramContainerEvent.getChild();
/* 209 */       if ((localComponent instanceof JMenu))
/* 210 */         ((JMenu)localComponent).getModel().removeChangeListener(BasicMenuBarUI.this.changeListener);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.basic.BasicMenuBarUI
 * JD-Core Version:    0.6.2
 */