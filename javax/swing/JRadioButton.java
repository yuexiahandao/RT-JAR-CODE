/*     */ package javax.swing;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectOutputStream;
/*     */ import javax.accessibility.Accessible;
/*     */ import javax.accessibility.AccessibleContext;
/*     */ import javax.accessibility.AccessibleRole;
/*     */ import javax.swing.plaf.ButtonUI;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ 
/*     */ public class JRadioButton extends JToggleButton
/*     */   implements Accessible
/*     */ {
/*     */   private static final String uiClassID = "RadioButtonUI";
/*     */ 
/*     */   public JRadioButton()
/*     */   {
/* 103 */     this(null, null, false);
/*     */   }
/*     */ 
/*     */   public JRadioButton(Icon paramIcon)
/*     */   {
/* 113 */     this(null, paramIcon, false);
/*     */   }
/*     */ 
/*     */   public JRadioButton(Action paramAction)
/*     */   {
/* 123 */     this();
/* 124 */     setAction(paramAction);
/*     */   }
/*     */ 
/*     */   public JRadioButton(Icon paramIcon, boolean paramBoolean)
/*     */   {
/* 136 */     this(null, paramIcon, paramBoolean);
/*     */   }
/*     */ 
/*     */   public JRadioButton(String paramString)
/*     */   {
/* 145 */     this(paramString, null, false);
/*     */   }
/*     */ 
/*     */   public JRadioButton(String paramString, boolean paramBoolean)
/*     */   {
/* 157 */     this(paramString, null, paramBoolean);
/*     */   }
/*     */ 
/*     */   public JRadioButton(String paramString, Icon paramIcon)
/*     */   {
/* 168 */     this(paramString, paramIcon, false);
/*     */   }
/*     */ 
/*     */   public JRadioButton(String paramString, Icon paramIcon, boolean paramBoolean)
/*     */   {
/* 179 */     super(paramString, paramIcon, paramBoolean);
/* 180 */     setBorderPainted(false);
/* 181 */     setHorizontalAlignment(10);
/*     */   }
/*     */ 
/*     */   public void updateUI()
/*     */   {
/* 191 */     setUI((ButtonUI)UIManager.getUI(this));
/*     */   }
/*     */ 
/*     */   public String getUIClassID()
/*     */   {
/* 207 */     return "RadioButtonUI";
/*     */   }
/*     */ 
/*     */   void setIconFromAction(Action paramAction)
/*     */   {
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*     */     throws IOException
/*     */   {
/* 223 */     paramObjectOutputStream.defaultWriteObject();
/* 224 */     if (getUIClassID().equals("RadioButtonUI")) {
/* 225 */       byte b = JComponent.getWriteObjCounter(this);
/* 226 */       b = (byte)(b - 1); JComponent.setWriteObjCounter(this, b);
/* 227 */       if ((b == 0) && (this.ui != null))
/* 228 */         this.ui.installUI(this);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected String paramString()
/*     */   {
/* 244 */     return super.paramString();
/*     */   }
/*     */ 
/*     */   public AccessibleContext getAccessibleContext()
/*     */   {
/* 266 */     if (this.accessibleContext == null) {
/* 267 */       this.accessibleContext = new AccessibleJRadioButton();
/*     */     }
/* 269 */     return this.accessibleContext;
/*     */   }
/*     */ 
/*     */   protected class AccessibleJRadioButton extends JToggleButton.AccessibleJToggleButton
/*     */   {
/*     */     protected AccessibleJRadioButton()
/*     */     {
/* 287 */       super();
/*     */     }
/*     */ 
/*     */     public AccessibleRole getAccessibleRole()
/*     */     {
/* 296 */       return AccessibleRole.RADIO_BUTTON;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.JRadioButton
 * JD-Core Version:    0.6.2
 */