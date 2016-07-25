/*     */ package javax.swing.plaf.synth;
/*     */ 
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.beans.PropertyVetoException;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JInternalFrame;
/*     */ import javax.swing.JInternalFrame.JDesktopIcon;
/*     */ import javax.swing.JPopupMenu;
/*     */ import javax.swing.JToggleButton;
/*     */ import javax.swing.ToolTipManager;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.basic.BasicDesktopIconUI;
/*     */ 
/*     */ public class SynthDesktopIconUI extends BasicDesktopIconUI
/*     */   implements SynthUI, PropertyChangeListener
/*     */ {
/*     */   private SynthStyle style;
/*     */   private Handler handler;
/*     */ 
/*     */   public SynthDesktopIconUI()
/*     */   {
/*  45 */     this.handler = new Handler(null);
/*     */   }
/*     */ 
/*     */   public static ComponentUI createUI(JComponent paramJComponent)
/*     */   {
/*  54 */     return new SynthDesktopIconUI();
/*     */   }
/*     */ 
/*     */   protected void installComponents()
/*     */   {
/*  62 */     if (UIManager.getBoolean("InternalFrame.useTaskBar")) {
/*  63 */       this.iconPane = new JToggleButton(this.frame.getTitle(), this.frame.getFrameIcon()) {
/*     */         public String getToolTipText() {
/*  65 */           return getText();
/*     */         }
/*     */ 
/*     */         public JPopupMenu getComponentPopupMenu() {
/*  69 */           return SynthDesktopIconUI.this.frame.getComponentPopupMenu();
/*     */         }
/*     */       };
/*  72 */       ToolTipManager.sharedInstance().registerComponent(this.iconPane);
/*  73 */       this.iconPane.setFont(this.desktopIcon.getFont());
/*  74 */       this.iconPane.setBackground(this.desktopIcon.getBackground());
/*  75 */       this.iconPane.setForeground(this.desktopIcon.getForeground());
/*     */     } else {
/*  77 */       this.iconPane = new SynthInternalFrameTitlePane(this.frame);
/*  78 */       this.iconPane.setName("InternalFrame.northPane");
/*     */     }
/*  80 */     this.desktopIcon.setLayout(new BorderLayout());
/*  81 */     this.desktopIcon.add(this.iconPane, "Center");
/*     */   }
/*     */ 
/*     */   protected void installListeners()
/*     */   {
/*  89 */     super.installListeners();
/*  90 */     this.desktopIcon.addPropertyChangeListener(this);
/*     */ 
/*  92 */     if ((this.iconPane instanceof JToggleButton)) {
/*  93 */       this.frame.addPropertyChangeListener(this);
/*  94 */       ((JToggleButton)this.iconPane).addActionListener(this.handler);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void uninstallListeners()
/*     */   {
/* 103 */     if ((this.iconPane instanceof JToggleButton)) {
/* 104 */       ((JToggleButton)this.iconPane).removeActionListener(this.handler);
/* 105 */       this.frame.removePropertyChangeListener(this);
/*     */     }
/* 107 */     this.desktopIcon.removePropertyChangeListener(this);
/* 108 */     super.uninstallListeners();
/*     */   }
/*     */ 
/*     */   protected void installDefaults()
/*     */   {
/* 116 */     updateStyle(this.desktopIcon);
/*     */   }
/*     */ 
/*     */   private void updateStyle(JComponent paramJComponent) {
/* 120 */     SynthContext localSynthContext = getContext(paramJComponent, 1);
/* 121 */     this.style = SynthLookAndFeel.updateStyle(localSynthContext, this);
/* 122 */     localSynthContext.dispose();
/*     */   }
/*     */ 
/*     */   protected void uninstallDefaults()
/*     */   {
/* 130 */     SynthContext localSynthContext = getContext(this.desktopIcon, 1);
/* 131 */     this.style.uninstallDefaults(localSynthContext);
/* 132 */     localSynthContext.dispose();
/* 133 */     this.style = null;
/*     */   }
/*     */ 
/*     */   public SynthContext getContext(JComponent paramJComponent)
/*     */   {
/* 141 */     return getContext(paramJComponent, getComponentState(paramJComponent));
/*     */   }
/*     */ 
/*     */   private SynthContext getContext(JComponent paramJComponent, int paramInt) {
/* 145 */     Region localRegion = SynthLookAndFeel.getRegion(paramJComponent);
/* 146 */     return SynthContext.getContext(SynthContext.class, paramJComponent, localRegion, this.style, paramInt);
/*     */   }
/*     */ 
/*     */   private int getComponentState(JComponent paramJComponent)
/*     */   {
/* 151 */     return SynthLookAndFeel.getComponentState(paramJComponent);
/*     */   }
/*     */ 
/*     */   public void update(Graphics paramGraphics, JComponent paramJComponent)
/*     */   {
/* 168 */     SynthContext localSynthContext = getContext(paramJComponent);
/*     */ 
/* 170 */     SynthLookAndFeel.update(localSynthContext, paramGraphics);
/* 171 */     localSynthContext.getPainter().paintDesktopIconBackground(localSynthContext, paramGraphics, 0, 0, paramJComponent.getWidth(), paramJComponent.getHeight());
/*     */ 
/* 173 */     paint(localSynthContext, paramGraphics);
/* 174 */     localSynthContext.dispose();
/*     */   }
/*     */ 
/*     */   public void paint(Graphics paramGraphics, JComponent paramJComponent)
/*     */   {
/* 188 */     SynthContext localSynthContext = getContext(paramJComponent);
/*     */ 
/* 190 */     paint(localSynthContext, paramGraphics);
/* 191 */     localSynthContext.dispose();
/*     */   }
/*     */ 
/*     */   protected void paint(SynthContext paramSynthContext, Graphics paramGraphics)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void paintBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 210 */     paramSynthContext.getPainter().paintDesktopIconBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*     */   }
/*     */ 
/*     */   public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent) {
/* 214 */     if ((paramPropertyChangeEvent.getSource() instanceof JInternalFrame.JDesktopIcon)) {
/* 215 */       if (SynthLookAndFeel.shouldUpdateStyle(paramPropertyChangeEvent))
/* 216 */         updateStyle((JInternalFrame.JDesktopIcon)paramPropertyChangeEvent.getSource());
/*     */     }
/* 218 */     else if ((paramPropertyChangeEvent.getSource() instanceof JInternalFrame)) {
/* 219 */       JInternalFrame localJInternalFrame = (JInternalFrame)paramPropertyChangeEvent.getSource();
/* 220 */       if ((this.iconPane instanceof JToggleButton)) {
/* 221 */         JToggleButton localJToggleButton = (JToggleButton)this.iconPane;
/* 222 */         String str = paramPropertyChangeEvent.getPropertyName();
/* 223 */         if (str == "title")
/* 224 */           localJToggleButton.setText((String)paramPropertyChangeEvent.getNewValue());
/* 225 */         else if (str == "frameIcon")
/* 226 */           localJToggleButton.setIcon((Icon)paramPropertyChangeEvent.getNewValue());
/* 227 */         else if ((str == "icon") || (str == "selected"))
/*     */         {
/* 229 */           localJToggleButton.setSelected((!localJInternalFrame.isIcon()) && (localJInternalFrame.isSelected()));
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private final class Handler implements ActionListener { private Handler() {  }
/*     */ 
/*     */ 
/* 237 */     public void actionPerformed(ActionEvent paramActionEvent) { if ((paramActionEvent.getSource() instanceof JToggleButton))
/*     */       {
/* 239 */         JToggleButton localJToggleButton = (JToggleButton)paramActionEvent.getSource();
/*     */         try {
/* 241 */           boolean bool = localJToggleButton.isSelected();
/* 242 */           if ((!bool) && (!SynthDesktopIconUI.this.frame.isIconifiable())) {
/* 243 */             localJToggleButton.setSelected(true);
/*     */           } else {
/* 245 */             SynthDesktopIconUI.this.frame.setIcon(!bool);
/* 246 */             if (bool)
/* 247 */               SynthDesktopIconUI.this.frame.setSelected(true);
/*     */           }
/*     */         }
/*     */         catch (PropertyVetoException localPropertyVetoException)
/*     */         {
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.synth.SynthDesktopIconUI
 * JD-Core Version:    0.6.2
 */