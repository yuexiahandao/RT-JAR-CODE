/*     */ package javax.swing;
/*     */ 
/*     */ import java.awt.FlowLayout;
/*     */ import java.awt.LayoutManager;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectOutputStream;
/*     */ import javax.accessibility.Accessible;
/*     */ import javax.accessibility.AccessibleContext;
/*     */ import javax.accessibility.AccessibleRole;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.PanelUI;
/*     */ 
/*     */ public class JPanel extends JComponent
/*     */   implements Accessible
/*     */ {
/*     */   private static final String uiClassID = "PanelUI";
/*     */ 
/*     */   public JPanel(LayoutManager paramLayoutManager, boolean paramBoolean)
/*     */   {
/*  83 */     setLayout(paramLayoutManager);
/*  84 */     setDoubleBuffered(paramBoolean);
/*  85 */     setUIProperty("opaque", Boolean.TRUE);
/*  86 */     updateUI();
/*     */   }
/*     */ 
/*     */   public JPanel(LayoutManager paramLayoutManager)
/*     */   {
/*  95 */     this(paramLayoutManager, true);
/*     */   }
/*     */ 
/*     */   public JPanel(boolean paramBoolean)
/*     */   {
/* 109 */     this(new FlowLayout(), paramBoolean);
/*     */   }
/*     */ 
/*     */   public JPanel()
/*     */   {
/* 117 */     this(true);
/*     */   }
/*     */ 
/*     */   public void updateUI()
/*     */   {
/* 126 */     setUI((PanelUI)UIManager.getUI(this));
/*     */   }
/*     */ 
/*     */   public PanelUI getUI()
/*     */   {
/* 136 */     return (PanelUI)this.ui;
/*     */   }
/*     */ 
/*     */   public void setUI(PanelUI paramPanelUI)
/*     */   {
/* 153 */     super.setUI(paramPanelUI);
/*     */   }
/*     */ 
/*     */   public String getUIClassID()
/*     */   {
/* 168 */     return "PanelUI";
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*     */     throws IOException
/*     */   {
/* 177 */     paramObjectOutputStream.defaultWriteObject();
/* 178 */     if (getUIClassID().equals("PanelUI")) {
/* 179 */       byte b = JComponent.getWriteObjCounter(this);
/* 180 */       b = (byte)(b - 1); JComponent.setWriteObjCounter(this, b);
/* 181 */       if ((b == 0) && (this.ui != null))
/* 182 */         this.ui.installUI(this);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected String paramString()
/*     */   {
/* 198 */     return super.paramString();
/*     */   }
/*     */ 
/*     */   public AccessibleContext getAccessibleContext()
/*     */   {
/* 215 */     if (this.accessibleContext == null) {
/* 216 */       this.accessibleContext = new AccessibleJPanel();
/*     */     }
/* 218 */     return this.accessibleContext;
/*     */   }
/*     */ 
/*     */   protected class AccessibleJPanel extends JComponent.AccessibleJComponent
/*     */   {
/*     */     protected AccessibleJPanel()
/*     */     {
/* 236 */       super();
/*     */     }
/*     */ 
/*     */     public AccessibleRole getAccessibleRole()
/*     */     {
/* 245 */       return AccessibleRole.PANEL;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.JPanel
 * JD-Core Version:    0.6.2
 */