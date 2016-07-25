/*     */ package com.sun.java.swing.plaf.motif;
/*     */ 
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Color;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Font;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Insets;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.event.MouseAdapter;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.awt.event.MouseListener;
/*     */ import java.awt.event.MouseMotionListener;
/*     */ import java.beans.PropertyVetoException;
/*     */ import java.util.EventListener;
/*     */ import javax.swing.DesktopManager;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JDesktopPane;
/*     */ import javax.swing.JInternalFrame;
/*     */ import javax.swing.JInternalFrame.JDesktopIcon;
/*     */ import javax.swing.JLayeredPane;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JPopupMenu;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.border.Border;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.basic.BasicDesktopIconUI;
/*     */ import sun.swing.SwingUtilities2;
/*     */ 
/*     */ public class MotifDesktopIconUI extends BasicDesktopIconUI
/*     */ {
/*     */   protected DesktopIconActionListener desktopIconActionListener;
/*     */   protected DesktopIconMouseListener desktopIconMouseListener;
/*     */   protected Icon defaultIcon;
/*     */   protected IconButton iconButton;
/*     */   protected IconLabel iconLabel;
/*     */   private MotifInternalFrameTitlePane sysMenuTitlePane;
/*     */   JPopupMenu systemMenu;
/*     */   EventListener mml;
/*     */   static final int LABEL_HEIGHT = 18;
/*     */   static final int LABEL_DIVIDER = 4;
/*  65 */   static final Font defaultTitleFont = new Font("SansSerif", 0, 12);
/*     */ 
/*     */   public static ComponentUI createUI(JComponent paramJComponent)
/*     */   {
/*  69 */     return new MotifDesktopIconUI();
/*     */   }
/*     */ 
/*     */   protected void installDefaults()
/*     */   {
/*  76 */     super.installDefaults();
/*  77 */     setDefaultIcon(UIManager.getIcon("DesktopIcon.icon"));
/*  78 */     this.iconButton = createIconButton(this.defaultIcon);
/*     */ 
/*  80 */     this.sysMenuTitlePane = new MotifInternalFrameTitlePane(this.frame);
/*  81 */     this.systemMenu = this.sysMenuTitlePane.getSystemMenu();
/*     */ 
/*  83 */     MotifBorders.FrameBorder localFrameBorder = new MotifBorders.FrameBorder(this.desktopIcon);
/*  84 */     this.desktopIcon.setLayout(new BorderLayout());
/*  85 */     this.iconButton.setBorder(localFrameBorder);
/*  86 */     this.desktopIcon.add(this.iconButton, "Center");
/*  87 */     this.iconLabel = createIconLabel(this.frame);
/*  88 */     this.iconLabel.setBorder(localFrameBorder);
/*  89 */     this.desktopIcon.add(this.iconLabel, "South");
/*  90 */     this.desktopIcon.setSize(this.desktopIcon.getPreferredSize());
/*  91 */     this.desktopIcon.validate();
/*  92 */     JLayeredPane.putLayer(this.desktopIcon, JLayeredPane.getLayer(this.frame));
/*     */   }
/*     */ 
/*     */   protected void installComponents() {
/*     */   }
/*     */ 
/*     */   protected void uninstallComponents() {
/*     */   }
/*     */ 
/*     */   protected void installListeners() {
/* 102 */     super.installListeners();
/* 103 */     this.desktopIconActionListener = createDesktopIconActionListener();
/* 104 */     this.desktopIconMouseListener = createDesktopIconMouseListener();
/* 105 */     this.iconButton.addActionListener(this.desktopIconActionListener);
/* 106 */     this.iconButton.addMouseListener(this.desktopIconMouseListener);
/* 107 */     this.iconLabel.addMouseListener(this.desktopIconMouseListener);
/*     */   }
/*     */ 
/*     */   JInternalFrame.JDesktopIcon getDesktopIcon() {
/* 111 */     return this.desktopIcon;
/*     */   }
/*     */ 
/*     */   void setDesktopIcon(JInternalFrame.JDesktopIcon paramJDesktopIcon) {
/* 115 */     this.desktopIcon = paramJDesktopIcon;
/*     */   }
/*     */ 
/*     */   JInternalFrame getFrame() {
/* 119 */     return this.frame;
/*     */   }
/*     */ 
/*     */   void setFrame(JInternalFrame paramJInternalFrame) {
/* 123 */     this.frame = paramJInternalFrame;
/*     */   }
/*     */ 
/*     */   protected void showSystemMenu() {
/* 127 */     this.systemMenu.show(this.iconButton, 0, getDesktopIcon().getHeight());
/*     */   }
/*     */ 
/*     */   protected void hideSystemMenu() {
/* 131 */     this.systemMenu.setVisible(false);
/*     */   }
/*     */ 
/*     */   protected IconLabel createIconLabel(JInternalFrame paramJInternalFrame) {
/* 135 */     return new IconLabel(paramJInternalFrame);
/*     */   }
/*     */ 
/*     */   protected IconButton createIconButton(Icon paramIcon) {
/* 139 */     return new IconButton(paramIcon);
/*     */   }
/*     */ 
/*     */   protected DesktopIconActionListener createDesktopIconActionListener() {
/* 143 */     return new DesktopIconActionListener();
/*     */   }
/*     */ 
/*     */   protected DesktopIconMouseListener createDesktopIconMouseListener() {
/* 147 */     return new DesktopIconMouseListener();
/*     */   }
/*     */ 
/*     */   protected void uninstallDefaults() {
/* 151 */     super.uninstallDefaults();
/* 152 */     this.desktopIcon.setLayout(null);
/* 153 */     this.desktopIcon.remove(this.iconButton);
/* 154 */     this.desktopIcon.remove(this.iconLabel);
/*     */   }
/*     */ 
/*     */   protected void uninstallListeners() {
/* 158 */     super.uninstallListeners();
/* 159 */     this.iconButton.removeActionListener(this.desktopIconActionListener);
/* 160 */     this.iconButton.removeMouseListener(this.desktopIconMouseListener);
/* 161 */     this.sysMenuTitlePane.uninstallListeners();
/*     */   }
/*     */ 
/*     */   public Dimension getMinimumSize(JComponent paramJComponent) {
/* 165 */     JInternalFrame localJInternalFrame = this.desktopIcon.getInternalFrame();
/*     */ 
/* 167 */     int i = this.defaultIcon.getIconWidth();
/* 168 */     int j = this.defaultIcon.getIconHeight() + 18 + 4;
/*     */ 
/* 170 */     Border localBorder = localJInternalFrame.getBorder();
/* 171 */     if (localBorder != null) {
/* 172 */       i += localBorder.getBorderInsets(localJInternalFrame).left + localBorder.getBorderInsets(localJInternalFrame).right;
/*     */ 
/* 174 */       j += localBorder.getBorderInsets(localJInternalFrame).bottom + localBorder.getBorderInsets(localJInternalFrame).top;
/*     */     }
/*     */ 
/* 178 */     return new Dimension(i, j);
/*     */   }
/*     */ 
/*     */   public Dimension getPreferredSize(JComponent paramJComponent) {
/* 182 */     return getMinimumSize(paramJComponent);
/*     */   }
/*     */ 
/*     */   public Dimension getMaximumSize(JComponent paramJComponent) {
/* 186 */     return getMinimumSize(paramJComponent);
/*     */   }
/*     */ 
/*     */   public Icon getDefaultIcon()
/*     */   {
/* 193 */     return this.defaultIcon;
/*     */   }
/*     */ 
/*     */   public void setDefaultIcon(Icon paramIcon)
/*     */   {
/* 200 */     this.defaultIcon = paramIcon;
/*     */   }
/*     */ 
/*     */   protected class DesktopIconActionListener
/*     */     implements ActionListener
/*     */   {
/*     */     protected DesktopIconActionListener()
/*     */     {
/*     */     }
/*     */ 
/*     */     public void actionPerformed(ActionEvent paramActionEvent)
/*     */     {
/* 345 */       MotifDesktopIconUI.this.systemMenu.show(MotifDesktopIconUI.this.iconButton, 0, MotifDesktopIconUI.this.getDesktopIcon().getHeight());
/*     */     }
/*     */   }
/*     */   protected class DesktopIconMouseListener extends MouseAdapter {
/*     */     protected DesktopIconMouseListener() {
/*     */     }
/*     */ 
/* 352 */     public void mousePressed(MouseEvent paramMouseEvent) { if (paramMouseEvent.getClickCount() > 1) {
/*     */         try {
/* 354 */           MotifDesktopIconUI.this.getFrame().setIcon(false); } catch (PropertyVetoException localPropertyVetoException) {
/*     */         }
/* 356 */         MotifDesktopIconUI.this.systemMenu.setVisible(false);
/*     */ 
/* 361 */         MotifDesktopIconUI.this.getFrame().getDesktopPane().getDesktopManager().endDraggingFrame((JComponent)paramMouseEvent.getSource());
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   protected class IconButton extends JButton
/*     */   {
/*     */     Icon icon;
/*     */ 
/*     */     IconButton(Icon arg2)
/*     */     {
/* 298 */       super();
/* 299 */       this.icon = localIcon;
/*     */ 
/* 301 */       addMouseMotionListener(new MouseMotionListener() {
/*     */         public void mouseDragged(MouseEvent paramAnonymousMouseEvent) {
/* 303 */           MotifDesktopIconUI.IconButton.this.forwardEventToParent(paramAnonymousMouseEvent);
/*     */         }
/*     */         public void mouseMoved(MouseEvent paramAnonymousMouseEvent) {
/* 306 */           MotifDesktopIconUI.IconButton.this.forwardEventToParent(paramAnonymousMouseEvent);
/*     */         }
/*     */       });
/* 309 */       addMouseListener(new MouseListener() {
/*     */         public void mouseClicked(MouseEvent paramAnonymousMouseEvent) {
/* 311 */           MotifDesktopIconUI.IconButton.this.forwardEventToParent(paramAnonymousMouseEvent);
/*     */         }
/*     */         public void mousePressed(MouseEvent paramAnonymousMouseEvent) {
/* 314 */           MotifDesktopIconUI.IconButton.this.forwardEventToParent(paramAnonymousMouseEvent);
/*     */         }
/*     */         public void mouseReleased(MouseEvent paramAnonymousMouseEvent) {
/* 317 */           if (!MotifDesktopIconUI.this.systemMenu.isShowing())
/* 318 */             MotifDesktopIconUI.IconButton.this.forwardEventToParent(paramAnonymousMouseEvent);
/*     */         }
/*     */ 
/*     */         public void mouseEntered(MouseEvent paramAnonymousMouseEvent) {
/* 322 */           MotifDesktopIconUI.IconButton.this.forwardEventToParent(paramAnonymousMouseEvent);
/*     */         }
/*     */         public void mouseExited(MouseEvent paramAnonymousMouseEvent) {
/* 325 */           MotifDesktopIconUI.IconButton.this.forwardEventToParent(paramAnonymousMouseEvent);
/*     */         }
/*     */       });
/*     */     }
/*     */ 
/*     */     void forwardEventToParent(MouseEvent paramMouseEvent) {
/* 331 */       getParent().dispatchEvent(new MouseEvent(getParent(), paramMouseEvent.getID(), paramMouseEvent.getWhen(), paramMouseEvent.getModifiers(), paramMouseEvent.getX(), paramMouseEvent.getY(), paramMouseEvent.getXOnScreen(), paramMouseEvent.getYOnScreen(), paramMouseEvent.getClickCount(), paramMouseEvent.isPopupTrigger(), 0));
/*     */     }
/*     */ 
/*     */     public boolean isFocusTraversable()
/*     */     {
/* 338 */       return false;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected class IconLabel extends JPanel
/*     */   {
/*     */     JInternalFrame frame;
/*     */ 
/*     */     IconLabel(JInternalFrame arg2)
/*     */     {
/*     */       Object localObject;
/* 208 */       this.frame = localObject;
/* 209 */       setFont(MotifDesktopIconUI.defaultTitleFont);
/*     */ 
/* 212 */       addMouseMotionListener(new MouseMotionListener() {
/*     */         public void mouseDragged(MouseEvent paramAnonymousMouseEvent) {
/* 214 */           MotifDesktopIconUI.IconLabel.this.forwardEventToParent(paramAnonymousMouseEvent);
/*     */         }
/*     */         public void mouseMoved(MouseEvent paramAnonymousMouseEvent) {
/* 217 */           MotifDesktopIconUI.IconLabel.this.forwardEventToParent(paramAnonymousMouseEvent);
/*     */         }
/*     */       });
/* 220 */       addMouseListener(new MouseListener() {
/*     */         public void mouseClicked(MouseEvent paramAnonymousMouseEvent) {
/* 222 */           MotifDesktopIconUI.IconLabel.this.forwardEventToParent(paramAnonymousMouseEvent);
/*     */         }
/*     */         public void mousePressed(MouseEvent paramAnonymousMouseEvent) {
/* 225 */           MotifDesktopIconUI.IconLabel.this.forwardEventToParent(paramAnonymousMouseEvent);
/*     */         }
/*     */         public void mouseReleased(MouseEvent paramAnonymousMouseEvent) {
/* 228 */           MotifDesktopIconUI.IconLabel.this.forwardEventToParent(paramAnonymousMouseEvent);
/*     */         }
/*     */         public void mouseEntered(MouseEvent paramAnonymousMouseEvent) {
/* 231 */           MotifDesktopIconUI.IconLabel.this.forwardEventToParent(paramAnonymousMouseEvent);
/*     */         }
/*     */         public void mouseExited(MouseEvent paramAnonymousMouseEvent) {
/* 234 */           MotifDesktopIconUI.IconLabel.this.forwardEventToParent(paramAnonymousMouseEvent);
/*     */         }
/*     */       });
/*     */     }
/*     */ 
/*     */     void forwardEventToParent(MouseEvent paramMouseEvent) {
/* 240 */       getParent().dispatchEvent(new MouseEvent(getParent(), paramMouseEvent.getID(), paramMouseEvent.getWhen(), paramMouseEvent.getModifiers(), paramMouseEvent.getX(), paramMouseEvent.getY(), paramMouseEvent.getXOnScreen(), paramMouseEvent.getYOnScreen(), paramMouseEvent.getClickCount(), paramMouseEvent.isPopupTrigger(), 0));
/*     */     }
/*     */ 
/*     */     public boolean isFocusTraversable()
/*     */     {
/* 248 */       return false;
/*     */     }
/*     */ 
/*     */     public Dimension getMinimumSize() {
/* 252 */       return new Dimension(MotifDesktopIconUI.this.defaultIcon.getIconWidth() + 1, 22);
/*     */     }
/*     */ 
/*     */     public Dimension getPreferredSize()
/*     */     {
/* 257 */       String str = this.frame.getTitle();
/* 258 */       FontMetrics localFontMetrics = this.frame.getFontMetrics(MotifDesktopIconUI.defaultTitleFont);
/* 259 */       int i = 4;
/* 260 */       if (str != null) {
/* 261 */         i += SwingUtilities2.stringWidth(this.frame, localFontMetrics, str);
/*     */       }
/* 263 */       return new Dimension(i, 22);
/*     */     }
/*     */ 
/*     */     public void paint(Graphics paramGraphics) {
/* 267 */       super.paint(paramGraphics);
/*     */ 
/* 270 */       int i = getWidth() - 1;
/* 271 */       Color localColor = UIManager.getColor("inactiveCaptionBorder").darker().darker();
/*     */ 
/* 273 */       paramGraphics.setColor(localColor);
/* 274 */       paramGraphics.setClip(0, 0, getWidth(), getHeight());
/* 275 */       paramGraphics.drawLine(i - 1, 1, i - 1, 1);
/* 276 */       paramGraphics.drawLine(i, 0, i, 0);
/*     */ 
/* 279 */       paramGraphics.setColor(UIManager.getColor("inactiveCaption"));
/* 280 */       paramGraphics.fillRect(2, 1, i - 3, 19);
/*     */ 
/* 283 */       paramGraphics.setClip(2, 1, i - 4, 18);
/* 284 */       int j = 18 - SwingUtilities2.getFontMetrics(this.frame, paramGraphics).getDescent();
/*     */ 
/* 286 */       paramGraphics.setColor(UIManager.getColor("inactiveCaptionText"));
/* 287 */       String str = this.frame.getTitle();
/* 288 */       if (str != null)
/* 289 */         SwingUtilities2.drawString(this.frame, paramGraphics, str, 4, j);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.java.swing.plaf.motif.MotifDesktopIconUI
 * JD-Core Version:    0.6.2
 */