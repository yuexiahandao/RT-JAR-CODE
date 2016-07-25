/*     */ package javax.swing;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectOutputStream;
/*     */ import javax.accessibility.Accessible;
/*     */ import javax.accessibility.AccessibleContext;
/*     */ import javax.accessibility.AccessibleRole;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ 
/*     */ public class JRadioButtonMenuItem extends JMenuItem
/*     */   implements Accessible
/*     */ {
/*     */   private static final String uiClassID = "RadioButtonMenuItemUI";
/*     */ 
/*     */   public JRadioButtonMenuItem()
/*     */   {
/*  97 */     this(null, null, false);
/*     */   }
/*     */ 
/*     */   public JRadioButtonMenuItem(Icon paramIcon)
/*     */   {
/* 107 */     this(null, paramIcon, false);
/*     */   }
/*     */ 
/*     */   public JRadioButtonMenuItem(String paramString)
/*     */   {
/* 116 */     this(paramString, null, false);
/*     */   }
/*     */ 
/*     */   public JRadioButtonMenuItem(Action paramAction)
/*     */   {
/* 129 */     this();
/* 130 */     setAction(paramAction);
/*     */   }
/*     */ 
/*     */   public JRadioButtonMenuItem(String paramString, Icon paramIcon)
/*     */   {
/* 141 */     this(paramString, paramIcon, false);
/*     */   }
/*     */ 
/*     */   public JRadioButtonMenuItem(String paramString, boolean paramBoolean)
/*     */   {
/* 152 */     this(paramString);
/* 153 */     setSelected(paramBoolean);
/*     */   }
/*     */ 
/*     */   public JRadioButtonMenuItem(Icon paramIcon, boolean paramBoolean)
/*     */   {
/* 165 */     this(null, paramIcon, paramBoolean);
/*     */   }
/*     */ 
/*     */   public JRadioButtonMenuItem(String paramString, Icon paramIcon, boolean paramBoolean)
/*     */   {
/* 177 */     super(paramString, paramIcon);
/* 178 */     setModel(new JToggleButton.ToggleButtonModel());
/* 179 */     setSelected(paramBoolean);
/* 180 */     setFocusable(false);
/*     */   }
/*     */ 
/*     */   public String getUIClassID()
/*     */   {
/* 191 */     return "RadioButtonMenuItemUI";
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*     */     throws IOException
/*     */   {
/* 200 */     paramObjectOutputStream.defaultWriteObject();
/* 201 */     if (getUIClassID().equals("RadioButtonMenuItemUI")) {
/* 202 */       byte b = JComponent.getWriteObjCounter(this);
/* 203 */       b = (byte)(b - 1); JComponent.setWriteObjCounter(this, b);
/* 204 */       if ((b == 0) && (this.ui != null))
/* 205 */         this.ui.installUI(this);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected String paramString()
/*     */   {
/* 223 */     return super.paramString();
/*     */   }
/*     */ 
/*     */   boolean shouldUpdateSelectedStateFromAction()
/*     */   {
/* 231 */     return true;
/*     */   }
/*     */ 
/*     */   public AccessibleContext getAccessibleContext()
/*     */   {
/* 248 */     if (this.accessibleContext == null) {
/* 249 */       this.accessibleContext = new AccessibleJRadioButtonMenuItem();
/*     */     }
/* 251 */     return this.accessibleContext;
/*     */   }
/*     */ 
/*     */   protected class AccessibleJRadioButtonMenuItem extends JMenuItem.AccessibleJMenuItem
/*     */   {
/*     */     protected AccessibleJRadioButtonMenuItem()
/*     */     {
/* 269 */       super();
/*     */     }
/*     */ 
/*     */     public AccessibleRole getAccessibleRole()
/*     */     {
/* 277 */       return AccessibleRole.RADIO_BUTTON;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.JRadioButtonMenuItem
 * JD-Core Version:    0.6.2
 */