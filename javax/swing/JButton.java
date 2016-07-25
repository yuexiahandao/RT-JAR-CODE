/*     */ package javax.swing;
/*     */ 
/*     */ import java.beans.ConstructorProperties;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectOutputStream;
/*     */ import javax.accessibility.Accessible;
/*     */ import javax.accessibility.AccessibleContext;
/*     */ import javax.accessibility.AccessibleRole;
/*     */ import javax.swing.plaf.ButtonUI;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ 
/*     */ public class JButton extends AbstractButton
/*     */   implements Accessible
/*     */ {
/*     */   private static final String uiClassID = "ButtonUI";
/*     */ 
/*     */   public JButton()
/*     */   {
/*  90 */     this(null, null);
/*     */   }
/*     */ 
/*     */   public JButton(Icon paramIcon)
/*     */   {
/*  99 */     this(null, paramIcon);
/*     */   }
/*     */ 
/*     */   @ConstructorProperties({"text"})
/*     */   public JButton(String paramString)
/*     */   {
/* 109 */     this(paramString, null);
/*     */   }
/*     */ 
/*     */   public JButton(Action paramAction)
/*     */   {
/* 121 */     this();
/* 122 */     setAction(paramAction);
/*     */   }
/*     */ 
/*     */   public JButton(String paramString, Icon paramIcon)
/*     */   {
/* 133 */     setModel(new DefaultButtonModel());
/*     */ 
/* 136 */     init(paramString, paramIcon);
/*     */   }
/*     */ 
/*     */   public void updateUI()
/*     */   {
/* 146 */     setUI((ButtonUI)UIManager.getUI(this));
/*     */   }
/*     */ 
/*     */   public String getUIClassID()
/*     */   {
/* 162 */     return "ButtonUI";
/*     */   }
/*     */ 
/*     */   public boolean isDefaultButton()
/*     */   {
/* 181 */     JRootPane localJRootPane = SwingUtilities.getRootPane(this);
/* 182 */     if (localJRootPane != null) {
/* 183 */       return localJRootPane.getDefaultButton() == this;
/*     */     }
/* 185 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isDefaultCapable()
/*     */   {
/* 197 */     return this.defaultCapable;
/*     */   }
/*     */ 
/*     */   public void setDefaultCapable(boolean paramBoolean)
/*     */   {
/* 218 */     boolean bool = this.defaultCapable;
/* 219 */     this.defaultCapable = paramBoolean;
/* 220 */     firePropertyChange("defaultCapable", bool, paramBoolean);
/*     */   }
/*     */ 
/*     */   public void removeNotify()
/*     */   {
/* 231 */     JRootPane localJRootPane = SwingUtilities.getRootPane(this);
/* 232 */     if ((localJRootPane != null) && (localJRootPane.getDefaultButton() == this)) {
/* 233 */       localJRootPane.setDefaultButton(null);
/*     */     }
/* 235 */     super.removeNotify();
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*     */     throws IOException
/*     */   {
/* 243 */     paramObjectOutputStream.defaultWriteObject();
/* 244 */     if (getUIClassID().equals("ButtonUI")) {
/* 245 */       byte b = JComponent.getWriteObjCounter(this);
/* 246 */       b = (byte)(b - 1); JComponent.setWriteObjCounter(this, b);
/* 247 */       if ((b == 0) && (this.ui != null))
/* 248 */         this.ui.installUI(this);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected String paramString()
/*     */   {
/* 264 */     String str = this.defaultCapable ? "true" : "false";
/*     */ 
/* 266 */     return super.paramString() + ",defaultCapable=" + str;
/*     */   }
/*     */ 
/*     */   public AccessibleContext getAccessibleContext()
/*     */   {
/* 289 */     if (this.accessibleContext == null) {
/* 290 */       this.accessibleContext = new AccessibleJButton();
/*     */     }
/* 292 */     return this.accessibleContext;
/*     */   }
/*     */ 
/*     */   protected class AccessibleJButton extends AbstractButton.AccessibleAbstractButton
/*     */   {
/*     */     protected AccessibleJButton()
/*     */     {
/* 310 */       super();
/*     */     }
/*     */ 
/*     */     public AccessibleRole getAccessibleRole()
/*     */     {
/* 320 */       return AccessibleRole.PUSH_BUTTON;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.JButton
 * JD-Core Version:    0.6.2
 */