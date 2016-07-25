/*     */ package javax.swing.plaf.metal;
/*     */ 
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.LayoutManager;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JInternalFrame;
/*     */ import javax.swing.JInternalFrame.JDesktopIcon;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.LookAndFeel;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.border.MatteBorder;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.basic.BasicDesktopIconUI;
/*     */ 
/*     */ public class MetalDesktopIconUI extends BasicDesktopIconUI
/*     */ {
/*     */   JButton button;
/*     */   JLabel label;
/*     */   TitleListener titleListener;
/*     */   private int width;
/*     */ 
/*     */   public static ComponentUI createUI(JComponent paramJComponent)
/*     */   {
/*  52 */     return new MetalDesktopIconUI();
/*     */   }
/*     */ 
/*     */   protected void installDefaults()
/*     */   {
/*  59 */     super.installDefaults();
/*  60 */     LookAndFeel.installColorsAndFont(this.desktopIcon, "DesktopIcon.background", "DesktopIcon.foreground", "DesktopIcon.font");
/*  61 */     this.width = UIManager.getInt("DesktopIcon.width");
/*     */   }
/*     */ 
/*     */   protected void installComponents() {
/*  65 */     this.frame = this.desktopIcon.getInternalFrame();
/*  66 */     Icon localIcon = this.frame.getFrameIcon();
/*  67 */     String str = this.frame.getTitle();
/*     */ 
/*  69 */     this.button = new JButton(str, localIcon);
/*  70 */     this.button.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent paramAnonymousActionEvent) {
/*  72 */         MetalDesktopIconUI.this.deiconize();
/*     */       }
/*     */     });
/*  73 */     this.button.setFont(this.desktopIcon.getFont());
/*  74 */     this.button.setBackground(this.desktopIcon.getBackground());
/*  75 */     this.button.setForeground(this.desktopIcon.getForeground());
/*     */ 
/*  77 */     int i = this.button.getPreferredSize().height;
/*     */ 
/*  79 */     MetalBumps localMetalBumps = new MetalBumps(i / 3, i, MetalLookAndFeel.getControlHighlight(), MetalLookAndFeel.getControlDarkShadow(), MetalLookAndFeel.getControl());
/*     */ 
/*  83 */     this.label = new JLabel(localMetalBumps);
/*     */ 
/*  85 */     this.label.setBorder(new MatteBorder(0, 2, 0, 1, this.desktopIcon.getBackground()));
/*  86 */     this.desktopIcon.setLayout(new BorderLayout(2, 0));
/*  87 */     this.desktopIcon.add(this.button, "Center");
/*  88 */     this.desktopIcon.add(this.label, "West");
/*     */   }
/*     */ 
/*     */   protected void uninstallComponents() {
/*  92 */     this.desktopIcon.setLayout(null);
/*  93 */     this.desktopIcon.remove(this.label);
/*  94 */     this.desktopIcon.remove(this.button);
/*  95 */     this.button = null;
/*  96 */     this.frame = null;
/*     */   }
/*     */ 
/*     */   protected void installListeners() {
/* 100 */     super.installListeners();
/* 101 */     this.desktopIcon.getInternalFrame().addPropertyChangeListener(this.titleListener = new TitleListener());
/*     */   }
/*     */ 
/*     */   protected void uninstallListeners()
/*     */   {
/* 106 */     this.desktopIcon.getInternalFrame().removePropertyChangeListener(this.titleListener);
/*     */ 
/* 108 */     this.titleListener = null;
/* 109 */     super.uninstallListeners();
/*     */   }
/*     */ 
/*     */   public Dimension getPreferredSize(JComponent paramJComponent)
/*     */   {
/* 116 */     return getMinimumSize(paramJComponent);
/*     */   }
/*     */ 
/*     */   public Dimension getMinimumSize(JComponent paramJComponent)
/*     */   {
/* 123 */     return new Dimension(this.width, this.desktopIcon.getLayout().minimumLayoutSize(this.desktopIcon).height);
/*     */   }
/*     */ 
/*     */   public Dimension getMaximumSize(JComponent paramJComponent)
/*     */   {
/* 130 */     return getMinimumSize(paramJComponent);
/*     */   }
/*     */   class TitleListener implements PropertyChangeListener {
/*     */     TitleListener() {
/*     */     }
/* 135 */     public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent) { if (paramPropertyChangeEvent.getPropertyName().equals("title")) {
/* 136 */         MetalDesktopIconUI.this.button.setText((String)paramPropertyChangeEvent.getNewValue());
/*     */       }
/*     */ 
/* 139 */       if (paramPropertyChangeEvent.getPropertyName().equals("frameIcon"))
/* 140 */         MetalDesktopIconUI.this.button.setIcon((Icon)paramPropertyChangeEvent.getNewValue());
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.metal.MetalDesktopIconUI
 * JD-Core Version:    0.6.2
 */