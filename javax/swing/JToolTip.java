/*     */ package javax.swing;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.util.Objects;
/*     */ import javax.accessibility.Accessible;
/*     */ import javax.accessibility.AccessibleContext;
/*     */ import javax.accessibility.AccessibleRole;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.ToolTipUI;
/*     */ 
/*     */ public class JToolTip extends JComponent
/*     */   implements Accessible
/*     */ {
/*     */   private static final String uiClassID = "ToolTipUI";
/*     */   String tipText;
/*     */   JComponent component;
/*     */ 
/*     */   public JToolTip()
/*     */   {
/*  82 */     setOpaque(true);
/*  83 */     updateUI();
/*     */   }
/*     */ 
/*     */   public ToolTipUI getUI()
/*     */   {
/*  92 */     return (ToolTipUI)this.ui;
/*     */   }
/*     */ 
/*     */   public void updateUI()
/*     */   {
/* 101 */     setUI((ToolTipUI)UIManager.getUI(this));
/*     */   }
/*     */ 
/*     */   public String getUIClassID()
/*     */   {
/* 113 */     return "ToolTipUI";
/*     */   }
/*     */ 
/*     */   public void setTipText(String paramString)
/*     */   {
/* 128 */     String str = this.tipText;
/* 129 */     this.tipText = paramString;
/* 130 */     firePropertyChange("tiptext", str, paramString);
/*     */ 
/* 132 */     if (!Objects.equals(str, paramString)) {
/* 133 */       revalidate();
/* 134 */       repaint();
/*     */     }
/*     */   }
/*     */ 
/*     */   public String getTipText()
/*     */   {
/* 145 */     return this.tipText;
/*     */   }
/*     */ 
/*     */   public void setComponent(JComponent paramJComponent)
/*     */   {
/* 162 */     JComponent localJComponent = this.component;
/*     */ 
/* 164 */     this.component = paramJComponent;
/* 165 */     firePropertyChange("component", localJComponent, paramJComponent);
/*     */   }
/*     */ 
/*     */   public JComponent getComponent()
/*     */   {
/* 177 */     return this.component;
/*     */   }
/*     */ 
/*     */   boolean alwaysOnTop()
/*     */   {
/* 186 */     return true;
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*     */     throws IOException
/*     */   {
/* 196 */     paramObjectOutputStream.defaultWriteObject();
/* 197 */     if (getUIClassID().equals("ToolTipUI")) {
/* 198 */       byte b = JComponent.getWriteObjCounter(this);
/* 199 */       b = (byte)(b - 1); JComponent.setWriteObjCounter(this, b);
/* 200 */       if ((b == 0) && (this.ui != null))
/* 201 */         this.ui.installUI(this);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected String paramString()
/*     */   {
/* 218 */     String str = this.tipText != null ? this.tipText : "";
/*     */ 
/* 221 */     return super.paramString() + ",tipText=" + str;
/*     */   }
/*     */ 
/*     */   public AccessibleContext getAccessibleContext()
/*     */   {
/* 240 */     if (this.accessibleContext == null) {
/* 241 */       this.accessibleContext = new AccessibleJToolTip();
/*     */     }
/* 243 */     return this.accessibleContext;
/*     */   }
/*     */ 
/*     */   protected class AccessibleJToolTip extends JComponent.AccessibleJComponent
/*     */   {
/*     */     protected AccessibleJToolTip()
/*     */     {
/* 260 */       super();
/*     */     }
/*     */ 
/*     */     public String getAccessibleDescription()
/*     */     {
/* 268 */       String str = this.accessibleDescription;
/*     */ 
/* 271 */       if (str == null) {
/* 272 */         str = (String)JToolTip.this.getClientProperty("AccessibleDescription");
/*     */       }
/* 274 */       if (str == null) {
/* 275 */         str = JToolTip.this.getTipText();
/*     */       }
/* 277 */       return str;
/*     */     }
/*     */ 
/*     */     public AccessibleRole getAccessibleRole()
/*     */     {
/* 287 */       return AccessibleRole.TOOL_TIP;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.JToolTip
 * JD-Core Version:    0.6.2
 */