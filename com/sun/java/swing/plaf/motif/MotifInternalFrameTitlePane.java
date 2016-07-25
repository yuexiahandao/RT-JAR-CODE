/*     */ package com.sun.java.swing.plaf.motif;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.LayoutManager;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.event.MouseAdapter;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.awt.event.MouseListener;
/*     */ import java.awt.event.MouseMotionListener;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.beans.PropertyVetoException;
/*     */ import javax.swing.Action;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.ButtonModel;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JInternalFrame;
/*     */ import javax.swing.JMenuItem;
/*     */ import javax.swing.JPopupMenu;
/*     */ import javax.swing.JSeparator;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
/*     */ 
/*     */ public class MotifInternalFrameTitlePane extends BasicInternalFrameTitlePane
/*     */   implements LayoutManager, ActionListener, PropertyChangeListener
/*     */ {
/*     */   SystemButton systemButton;
/*     */   MinimizeButton minimizeButton;
/*     */   MaximizeButton maximizeButton;
/*     */   JPopupMenu systemMenu;
/*     */   Title title;
/*     */   Color color;
/*     */   Color highlight;
/*     */   Color shadow;
/*     */   public static final int BUTTON_SIZE = 19;
/* 225 */   static Dimension buttonDimension = new Dimension(19, 19);
/*     */ 
/*     */   public MotifInternalFrameTitlePane(JInternalFrame paramJInternalFrame)
/*     */   {
/*  62 */     super(paramJInternalFrame);
/*     */   }
/*     */ 
/*     */   protected void installDefaults() {
/*  66 */     setFont(UIManager.getFont("InternalFrame.titleFont"));
/*  67 */     setPreferredSize(new Dimension(100, 19));
/*     */   }
/*     */ 
/*     */   protected void uninstallListeners()
/*     */   {
/*  72 */     super.uninstallListeners();
/*     */   }
/*     */ 
/*     */   protected PropertyChangeListener createPropertyChangeListener() {
/*  76 */     return this;
/*     */   }
/*     */ 
/*     */   protected LayoutManager createLayout() {
/*  80 */     return this;
/*     */   }
/*     */ 
/*     */   JPopupMenu getSystemMenu() {
/*  84 */     return this.systemMenu;
/*     */   }
/*     */ 
/*     */   protected void assembleSystemMenu() {
/*  88 */     this.systemMenu = new JPopupMenu();
/*  89 */     JMenuItem localJMenuItem = this.systemMenu.add(new JMenuItem(this.restoreAction));
/*  90 */     localJMenuItem.setMnemonic('R');
/*  91 */     localJMenuItem = this.systemMenu.add(new JMenuItem(this.moveAction));
/*  92 */     localJMenuItem.setMnemonic('M');
/*  93 */     localJMenuItem = this.systemMenu.add(new JMenuItem(this.sizeAction));
/*  94 */     localJMenuItem.setMnemonic('S');
/*  95 */     localJMenuItem = this.systemMenu.add(new JMenuItem(this.iconifyAction));
/*  96 */     localJMenuItem.setMnemonic('n');
/*  97 */     localJMenuItem = this.systemMenu.add(new JMenuItem(this.maximizeAction));
/*  98 */     localJMenuItem.setMnemonic('x');
/*  99 */     this.systemMenu.add(new JSeparator());
/* 100 */     localJMenuItem = this.systemMenu.add(new JMenuItem(this.closeAction));
/* 101 */     localJMenuItem.setMnemonic('C');
/*     */ 
/* 103 */     this.systemButton = new SystemButton(null);
/* 104 */     this.systemButton.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent paramAnonymousActionEvent) {
/* 106 */         MotifInternalFrameTitlePane.this.systemMenu.show(MotifInternalFrameTitlePane.this.systemButton, 0, 19);
/*     */       }
/*     */     });
/* 110 */     this.systemButton.addMouseListener(new MouseAdapter() {
/*     */       public void mousePressed(MouseEvent paramAnonymousMouseEvent) {
/*     */         try {
/* 113 */           MotifInternalFrameTitlePane.this.frame.setSelected(true);
/*     */         } catch (PropertyVetoException localPropertyVetoException) {
/*     */         }
/* 116 */         if (paramAnonymousMouseEvent.getClickCount() == 2) {
/* 117 */           MotifInternalFrameTitlePane.this.closeAction.actionPerformed(new ActionEvent(paramAnonymousMouseEvent.getSource(), 1001, null, paramAnonymousMouseEvent.getWhen(), 0));
/*     */ 
/* 121 */           MotifInternalFrameTitlePane.this.systemMenu.setVisible(false);
/*     */         }
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   protected void createButtons()
/*     */   {
/* 129 */     this.minimizeButton = new MinimizeButton(null);
/* 130 */     this.minimizeButton.addActionListener(this.iconifyAction);
/*     */ 
/* 132 */     this.maximizeButton = new MaximizeButton(null);
/* 133 */     this.maximizeButton.addActionListener(this.maximizeAction);
/*     */   }
/*     */ 
/*     */   protected void addSubComponents()
/*     */   {
/* 138 */     this.title = new Title(this.frame.getTitle());
/* 139 */     this.title.setFont(getFont());
/*     */ 
/* 141 */     add(this.systemButton);
/* 142 */     add(this.title);
/* 143 */     add(this.minimizeButton);
/* 144 */     add(this.maximizeButton);
/*     */   }
/*     */ 
/*     */   public void paintComponent(Graphics paramGraphics) {
/*     */   }
/*     */ 
/*     */   void setColors(Color paramColor1, Color paramColor2, Color paramColor3) {
/* 151 */     this.color = paramColor1;
/* 152 */     this.highlight = paramColor2;
/* 153 */     this.shadow = paramColor3;
/*     */   }
/*     */ 
/*     */   public void actionPerformed(ActionEvent paramActionEvent) {
/*     */   }
/*     */ 
/*     */   public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent) {
/* 160 */     String str = paramPropertyChangeEvent.getPropertyName();
/* 161 */     JInternalFrame localJInternalFrame = (JInternalFrame)paramPropertyChangeEvent.getSource();
/* 162 */     int i = 0;
/* 163 */     if ("selected".equals(str)) {
/* 164 */       repaint();
/* 165 */     } else if (str.equals("maximizable")) {
/* 166 */       if ((Boolean)paramPropertyChangeEvent.getNewValue() == Boolean.TRUE)
/* 167 */         add(this.maximizeButton);
/*     */       else
/* 169 */         remove(this.maximizeButton);
/* 170 */       revalidate();
/* 171 */       repaint();
/* 172 */     } else if (str.equals("iconable")) {
/* 173 */       if ((Boolean)paramPropertyChangeEvent.getNewValue() == Boolean.TRUE)
/* 174 */         add(this.minimizeButton);
/*     */       else
/* 176 */         remove(this.minimizeButton);
/* 177 */       revalidate();
/* 178 */       repaint();
/* 179 */     } else if (str.equals("title")) {
/* 180 */       repaint();
/*     */     }
/* 182 */     enableActions();
/*     */   }
/*     */   public void addLayoutComponent(String paramString, Component paramComponent) {
/*     */   }
/*     */   public void removeLayoutComponent(Component paramComponent) {
/*     */   }
/* 188 */   public Dimension preferredLayoutSize(Container paramContainer) { return minimumLayoutSize(paramContainer); }
/*     */ 
/*     */   public Dimension minimumLayoutSize(Container paramContainer)
/*     */   {
/* 192 */     return new Dimension(100, 19);
/*     */   }
/*     */ 
/*     */   public void layoutContainer(Container paramContainer) {
/* 196 */     int i = getWidth();
/* 197 */     this.systemButton.setBounds(0, 0, 19, 19);
/* 198 */     int j = i - 19;
/*     */ 
/* 200 */     if (this.frame.isMaximizable()) {
/* 201 */       this.maximizeButton.setBounds(j, 0, 19, 19);
/* 202 */       j -= 19;
/* 203 */     } else if (this.maximizeButton.getParent() != null) {
/* 204 */       this.maximizeButton.getParent().remove(this.maximizeButton);
/*     */     }
/*     */ 
/* 207 */     if (this.frame.isIconifiable()) {
/* 208 */       this.minimizeButton.setBounds(j, 0, 19, 19);
/* 209 */       j -= 19;
/* 210 */     } else if (this.minimizeButton.getParent() != null) {
/* 211 */       this.minimizeButton.getParent().remove(this.minimizeButton);
/*     */     }
/*     */ 
/* 214 */     this.title.setBounds(19, 0, j, 19);
/*     */   }
/*     */ 
/*     */   protected void showSystemMenu() {
/* 218 */     this.systemMenu.show(this.systemButton, 0, 19);
/*     */   }
/*     */ 
/*     */   protected void hideSystemMenu() {
/* 222 */     this.systemMenu.setVisible(false);
/*     */   }
/*     */ 
/*     */   private abstract class FrameButton extends JButton
/*     */   {
/*     */     FrameButton()
/*     */     {
/* 231 */       setFocusPainted(false);
/* 232 */       setBorderPainted(false);
/*     */     }
/*     */ 
/*     */     public boolean isFocusTraversable() {
/* 236 */       return false;
/*     */     }
/*     */ 
/*     */     public void requestFocus()
/*     */     {
/*     */     }
/*     */ 
/*     */     public Dimension getMinimumSize() {
/* 244 */       return MotifInternalFrameTitlePane.buttonDimension;
/*     */     }
/*     */ 
/*     */     public Dimension getPreferredSize() {
/* 248 */       return MotifInternalFrameTitlePane.buttonDimension;
/*     */     }
/*     */ 
/*     */     public void paintComponent(Graphics paramGraphics) {
/* 252 */       Dimension localDimension = getSize();
/* 253 */       int i = localDimension.width - 1;
/* 254 */       int j = localDimension.height - 1;
/*     */ 
/* 257 */       paramGraphics.setColor(MotifInternalFrameTitlePane.this.color);
/* 258 */       paramGraphics.fillRect(1, 1, localDimension.width, localDimension.height);
/*     */ 
/* 261 */       boolean bool = getModel().isPressed();
/* 262 */       paramGraphics.setColor(bool ? MotifInternalFrameTitlePane.this.shadow : MotifInternalFrameTitlePane.this.highlight);
/* 263 */       paramGraphics.drawLine(0, 0, i, 0);
/* 264 */       paramGraphics.drawLine(0, 0, 0, j);
/* 265 */       paramGraphics.setColor(bool ? MotifInternalFrameTitlePane.this.highlight : MotifInternalFrameTitlePane.this.shadow);
/* 266 */       paramGraphics.drawLine(1, j, i, j);
/* 267 */       paramGraphics.drawLine(i, 1, i, j);
/*     */     }
/*     */   }
/*     */ 
/*     */   private class MaximizeButton extends MotifInternalFrameTitlePane.FrameButton
/*     */   {
/*     */     private MaximizeButton()
/*     */     {
/* 283 */       super();
/*     */     }
/* 285 */     public void paintComponent(Graphics paramGraphics) { super.paintComponent(paramGraphics);
/* 286 */       int i = 14;
/* 287 */       boolean bool = MotifInternalFrameTitlePane.this.frame.isMaximum();
/* 288 */       paramGraphics.setColor(bool ? MotifInternalFrameTitlePane.this.shadow : MotifInternalFrameTitlePane.this.highlight);
/* 289 */       paramGraphics.drawLine(4, 4, 4, i);
/* 290 */       paramGraphics.drawLine(4, 4, i, 4);
/* 291 */       paramGraphics.setColor(bool ? MotifInternalFrameTitlePane.this.highlight : MotifInternalFrameTitlePane.this.shadow);
/* 292 */       paramGraphics.drawLine(5, i, i, i);
/* 293 */       paramGraphics.drawLine(i, 5, i, i);
/*     */     }
/*     */   }
/*     */ 
/*     */   private class MinimizeButton extends MotifInternalFrameTitlePane.FrameButton
/*     */   {
/*     */     private MinimizeButton()
/*     */     {
/* 271 */       super();
/*     */     }
/* 273 */     public void paintComponent(Graphics paramGraphics) { super.paintComponent(paramGraphics);
/* 274 */       paramGraphics.setColor(MotifInternalFrameTitlePane.this.highlight);
/* 275 */       paramGraphics.drawLine(7, 8, 7, 11);
/* 276 */       paramGraphics.drawLine(7, 8, 10, 8);
/* 277 */       paramGraphics.setColor(MotifInternalFrameTitlePane.this.shadow);
/* 278 */       paramGraphics.drawLine(8, 11, 10, 11);
/* 279 */       paramGraphics.drawLine(11, 9, 11, 11);
/*     */     }
/*     */   }
/*     */ 
/*     */   private class SystemButton extends MotifInternalFrameTitlePane.FrameButton
/*     */   {
/*     */     private SystemButton()
/*     */     {
/* 297 */       super(); } 
/* 298 */     public boolean isFocusTraversable() { return false; } 
/*     */     public void requestFocus() {
/*     */     }
/*     */     public void paintComponent(Graphics paramGraphics) {
/* 302 */       super.paintComponent(paramGraphics);
/* 303 */       paramGraphics.setColor(MotifInternalFrameTitlePane.this.highlight);
/* 304 */       paramGraphics.drawLine(4, 8, 4, 11);
/* 305 */       paramGraphics.drawLine(4, 8, 14, 8);
/* 306 */       paramGraphics.setColor(MotifInternalFrameTitlePane.this.shadow);
/* 307 */       paramGraphics.drawLine(5, 11, 14, 11);
/* 308 */       paramGraphics.drawLine(14, 9, 14, 11);
/*     */     }
/*     */   }
/*     */ 
/*     */   private class Title extends MotifInternalFrameTitlePane.FrameButton {
/*     */     Title(String arg2) {
/* 314 */       super();
/*     */       String str;
/* 315 */       setText(str);
/* 316 */       setHorizontalAlignment(0);
/* 317 */       setBorder(BorderFactory.createBevelBorder(0, UIManager.getColor("activeCaptionBorder"), UIManager.getColor("inactiveCaptionBorder")));
/*     */ 
/* 323 */       addMouseMotionListener(new MouseMotionListener() {
/*     */         public void mouseDragged(MouseEvent paramAnonymousMouseEvent) {
/* 325 */           MotifInternalFrameTitlePane.Title.this.forwardEventToParent(paramAnonymousMouseEvent);
/*     */         }
/*     */         public void mouseMoved(MouseEvent paramAnonymousMouseEvent) {
/* 328 */           MotifInternalFrameTitlePane.Title.this.forwardEventToParent(paramAnonymousMouseEvent);
/*     */         }
/*     */       });
/* 331 */       addMouseListener(new MouseListener() {
/*     */         public void mouseClicked(MouseEvent paramAnonymousMouseEvent) {
/* 333 */           MotifInternalFrameTitlePane.Title.this.forwardEventToParent(paramAnonymousMouseEvent);
/*     */         }
/*     */         public void mousePressed(MouseEvent paramAnonymousMouseEvent) {
/* 336 */           MotifInternalFrameTitlePane.Title.this.forwardEventToParent(paramAnonymousMouseEvent);
/*     */         }
/*     */         public void mouseReleased(MouseEvent paramAnonymousMouseEvent) {
/* 339 */           MotifInternalFrameTitlePane.Title.this.forwardEventToParent(paramAnonymousMouseEvent);
/*     */         }
/*     */         public void mouseEntered(MouseEvent paramAnonymousMouseEvent) {
/* 342 */           MotifInternalFrameTitlePane.Title.this.forwardEventToParent(paramAnonymousMouseEvent);
/*     */         }
/*     */         public void mouseExited(MouseEvent paramAnonymousMouseEvent) {
/* 345 */           MotifInternalFrameTitlePane.Title.this.forwardEventToParent(paramAnonymousMouseEvent);
/*     */         }
/*     */       });
/*     */     }
/*     */ 
/*     */     void forwardEventToParent(MouseEvent paramMouseEvent) {
/* 351 */       getParent().dispatchEvent(new MouseEvent(getParent(), paramMouseEvent.getID(), paramMouseEvent.getWhen(), paramMouseEvent.getModifiers(), paramMouseEvent.getX(), paramMouseEvent.getY(), paramMouseEvent.getXOnScreen(), paramMouseEvent.getYOnScreen(), paramMouseEvent.getClickCount(), paramMouseEvent.isPopupTrigger(), 0));
/*     */     }
/*     */ 
/*     */     public void paintComponent(Graphics paramGraphics)
/*     */     {
/* 359 */       super.paintComponent(paramGraphics);
/* 360 */       if (MotifInternalFrameTitlePane.this.frame.isSelected())
/* 361 */         paramGraphics.setColor(UIManager.getColor("activeCaptionText"));
/*     */       else {
/* 363 */         paramGraphics.setColor(UIManager.getColor("inactiveCaptionText"));
/*     */       }
/* 365 */       Dimension localDimension = getSize();
/* 366 */       String str = MotifInternalFrameTitlePane.this.frame.getTitle();
/* 367 */       if (str != null)
/* 368 */         MotifGraphicsUtils.drawStringInRect(MotifInternalFrameTitlePane.this.frame, paramGraphics, str, 0, 0, localDimension.width, localDimension.height, 0);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.java.swing.plaf.motif.MotifInternalFrameTitlePane
 * JD-Core Version:    0.6.2
 */