/*     */ package com.sun.java.swing.plaf.motif;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Toolkit;
/*     */ import java.awt.event.ActionEvent;
/*     */ import javax.swing.AbstractAction;
/*     */ import javax.swing.ActionMap;
/*     */ import javax.swing.InputMap;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JInternalFrame;
/*     */ import javax.swing.JInternalFrame.JDesktopIcon;
/*     */ import javax.swing.KeyStroke;
/*     */ import javax.swing.LookAndFeel;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.border.Border;
/*     */ import javax.swing.plaf.ActionMapUIResource;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.UIResource;
/*     */ import javax.swing.plaf.basic.BasicInternalFrameUI;
/*     */ 
/*     */ public class MotifInternalFrameUI extends BasicInternalFrameUI
/*     */ {
/*     */   Color color;
/*     */   Color highlight;
/*     */   Color shadow;
/*     */   MotifInternalFrameTitlePane titlePane;
/*     */ 
/*     */   @Deprecated
/*     */   protected KeyStroke closeMenuKey;
/*     */ 
/*     */   public static ComponentUI createUI(JComponent paramJComponent)
/*     */   {
/*  75 */     return new MotifInternalFrameUI((JInternalFrame)paramJComponent);
/*     */   }
/*     */ 
/*     */   public MotifInternalFrameUI(JInternalFrame paramJInternalFrame) {
/*  79 */     super(paramJInternalFrame);
/*     */   }
/*     */ 
/*     */   public void installUI(JComponent paramJComponent) {
/*  83 */     super.installUI(paramJComponent);
/*  84 */     setColors((JInternalFrame)paramJComponent);
/*     */   }
/*     */ 
/*     */   protected void installDefaults() {
/*  88 */     Border localBorder = this.frame.getBorder();
/*  89 */     this.frame.setLayout(this.internalFrameLayout = createLayoutManager());
/*  90 */     if ((localBorder == null) || ((localBorder instanceof UIResource)))
/*  91 */       this.frame.setBorder(new MotifBorders.InternalFrameBorder(this.frame));
/*     */   }
/*     */ 
/*     */   protected void installKeyboardActions()
/*     */   {
/*  97 */     super.installKeyboardActions();
/*     */ 
/* 100 */     this.closeMenuKey = KeyStroke.getKeyStroke(27, 0);
/*     */   }
/*     */ 
/*     */   protected void uninstallDefaults()
/*     */   {
/* 105 */     LookAndFeel.uninstallBorder(this.frame);
/* 106 */     this.frame.setLayout(null);
/* 107 */     this.internalFrameLayout = null;
/*     */   }
/*     */ 
/*     */   private JInternalFrame getFrame() {
/* 111 */     return this.frame;
/*     */   }
/*     */ 
/*     */   public JComponent createNorthPane(JInternalFrame paramJInternalFrame) {
/* 115 */     this.titlePane = new MotifInternalFrameTitlePane(paramJInternalFrame);
/* 116 */     return this.titlePane;
/*     */   }
/*     */ 
/*     */   public Dimension getMaximumSize(JComponent paramJComponent) {
/* 120 */     return Toolkit.getDefaultToolkit().getScreenSize();
/*     */   }
/*     */ 
/*     */   protected void uninstallKeyboardActions() {
/* 124 */     super.uninstallKeyboardActions();
/* 125 */     if (isKeyBindingRegistered()) {
/* 126 */       JInternalFrame.JDesktopIcon localJDesktopIcon = this.frame.getDesktopIcon();
/* 127 */       SwingUtilities.replaceUIActionMap(localJDesktopIcon, null);
/* 128 */       SwingUtilities.replaceUIInputMap(localJDesktopIcon, 2, null);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void setupMenuOpenKey()
/*     */   {
/* 134 */     super.setupMenuOpenKey();
/* 135 */     ActionMap localActionMap = SwingUtilities.getUIActionMap(this.frame);
/* 136 */     if (localActionMap != null)
/*     */     {
/* 141 */       localActionMap.put("showSystemMenu", new AbstractAction() {
/*     */         public void actionPerformed(ActionEvent paramAnonymousActionEvent) {
/* 143 */           MotifInternalFrameUI.this.titlePane.showSystemMenu();
/*     */         }
/*     */         public boolean isEnabled() {
/* 146 */           return MotifInternalFrameUI.this.isKeyBindingActive();
/*     */         }
/*     */       });
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void setupMenuCloseKey() {
/* 153 */     ActionMap localActionMap = SwingUtilities.getUIActionMap(this.frame);
/* 154 */     if (localActionMap != null) {
/* 155 */       localActionMap.put("hideSystemMenu", new AbstractAction() {
/*     */         public void actionPerformed(ActionEvent paramAnonymousActionEvent) {
/* 157 */           MotifInternalFrameUI.this.titlePane.hideSystemMenu();
/*     */         }
/*     */         public boolean isEnabled() {
/* 160 */           return MotifInternalFrameUI.this.isKeyBindingActive();
/*     */         }
/*     */ 
/*     */       });
/*     */     }
/*     */ 
/* 167 */     JInternalFrame.JDesktopIcon localJDesktopIcon = this.frame.getDesktopIcon();
/* 168 */     Object localObject1 = SwingUtilities.getUIInputMap(localJDesktopIcon, 2);
/*     */ 
/* 170 */     if (localObject1 == null) {
/* 171 */       localObject2 = (Object[])UIManager.get("DesktopIcon.windowBindings");
/*     */ 
/* 173 */       if (localObject2 != null) {
/* 174 */         localObject1 = LookAndFeel.makeComponentInputMap(localJDesktopIcon, (Object[])localObject2);
/*     */ 
/* 176 */         SwingUtilities.replaceUIInputMap(localJDesktopIcon, 2, (InputMap)localObject1);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 181 */     Object localObject2 = SwingUtilities.getUIActionMap(localJDesktopIcon);
/* 182 */     if (localObject2 == null) {
/* 183 */       localObject2 = new ActionMapUIResource();
/* 184 */       ((ActionMap)localObject2).put("hideSystemMenu", new AbstractAction() {
/*     */         public void actionPerformed(ActionEvent paramAnonymousActionEvent) {
/* 186 */           JInternalFrame.JDesktopIcon localJDesktopIcon = MotifInternalFrameUI.this.getFrame().getDesktopIcon();
/*     */ 
/* 188 */           MotifDesktopIconUI localMotifDesktopIconUI = (MotifDesktopIconUI)localJDesktopIcon.getUI();
/*     */ 
/* 190 */           localMotifDesktopIconUI.hideSystemMenu();
/*     */         }
/*     */         public boolean isEnabled() {
/* 193 */           return MotifInternalFrameUI.this.isKeyBindingActive();
/*     */         }
/*     */       });
/* 196 */       SwingUtilities.replaceUIActionMap(localJDesktopIcon, (ActionMap)localObject2);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void activateFrame(JInternalFrame paramJInternalFrame)
/*     */   {
/* 203 */     super.activateFrame(paramJInternalFrame);
/* 204 */     setColors(paramJInternalFrame);
/*     */   }
/*     */ 
/*     */   protected void deactivateFrame(JInternalFrame paramJInternalFrame)
/*     */   {
/* 209 */     setColors(paramJInternalFrame);
/* 210 */     super.deactivateFrame(paramJInternalFrame);
/*     */   }
/*     */ 
/*     */   void setColors(JInternalFrame paramJInternalFrame) {
/* 214 */     if (paramJInternalFrame.isSelected())
/* 215 */       this.color = UIManager.getColor("InternalFrame.activeTitleBackground");
/*     */     else {
/* 217 */       this.color = UIManager.getColor("InternalFrame.inactiveTitleBackground");
/*     */     }
/* 219 */     this.highlight = this.color.brighter();
/* 220 */     this.shadow = this.color.darker().darker();
/* 221 */     this.titlePane.setColors(this.color, this.highlight, this.shadow);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.java.swing.plaf.motif.MotifInternalFrameUI
 * JD-Core Version:    0.6.2
 */