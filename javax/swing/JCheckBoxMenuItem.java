/*     */ package javax.swing;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectOutputStream;
/*     */ import javax.accessibility.Accessible;
/*     */ import javax.accessibility.AccessibleContext;
/*     */ import javax.accessibility.AccessibleRole;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ 
/*     */ public class JCheckBoxMenuItem extends JMenuItem
/*     */   implements SwingConstants, Accessible
/*     */ {
/*     */   private static final String uiClassID = "CheckBoxMenuItemUI";
/*     */ 
/*     */   public JCheckBoxMenuItem()
/*     */   {
/* 104 */     this(null, null, false);
/*     */   }
/*     */ 
/*     */   public JCheckBoxMenuItem(Icon paramIcon)
/*     */   {
/* 113 */     this(null, paramIcon, false);
/*     */   }
/*     */ 
/*     */   public JCheckBoxMenuItem(String paramString)
/*     */   {
/* 122 */     this(paramString, null, false);
/*     */   }
/*     */ 
/*     */   public JCheckBoxMenuItem(Action paramAction)
/*     */   {
/* 132 */     this();
/* 133 */     setAction(paramAction);
/*     */   }
/*     */ 
/*     */   public JCheckBoxMenuItem(String paramString, Icon paramIcon)
/*     */   {
/* 143 */     this(paramString, paramIcon, false);
/*     */   }
/*     */ 
/*     */   public JCheckBoxMenuItem(String paramString, boolean paramBoolean)
/*     */   {
/* 153 */     this(paramString, null, paramBoolean);
/*     */   }
/*     */ 
/*     */   public JCheckBoxMenuItem(String paramString, Icon paramIcon, boolean paramBoolean)
/*     */   {
/* 164 */     super(paramString, paramIcon);
/* 165 */     setModel(new JToggleButton.ToggleButtonModel());
/* 166 */     setSelected(paramBoolean);
/* 167 */     setFocusable(false);
/*     */   }
/*     */ 
/*     */   public String getUIClassID()
/*     */   {
/* 179 */     return "CheckBoxMenuItemUI";
/*     */   }
/*     */ 
/*     */   public boolean getState()
/*     */   {
/* 190 */     return isSelected();
/*     */   }
/*     */ 
/*     */   public synchronized void setState(boolean paramBoolean)
/*     */   {
/* 205 */     setSelected(paramBoolean);
/*     */   }
/*     */ 
/*     */   public Object[] getSelectedObjects()
/*     */   {
/* 217 */     if (!isSelected())
/* 218 */       return null;
/* 219 */     Object[] arrayOfObject = new Object[1];
/* 220 */     arrayOfObject[0] = getText();
/* 221 */     return arrayOfObject;
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*     */     throws IOException
/*     */   {
/* 229 */     paramObjectOutputStream.defaultWriteObject();
/* 230 */     if (getUIClassID().equals("CheckBoxMenuItemUI")) {
/* 231 */       byte b = JComponent.getWriteObjCounter(this);
/* 232 */       b = (byte)(b - 1); JComponent.setWriteObjCounter(this, b);
/* 233 */       if ((b == 0) && (this.ui != null))
/* 234 */         this.ui.installUI(this);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected String paramString()
/*     */   {
/* 250 */     return super.paramString();
/*     */   }
/*     */ 
/*     */   boolean shouldUpdateSelectedStateFromAction()
/*     */   {
/* 258 */     return true;
/*     */   }
/*     */ 
/*     */   public AccessibleContext getAccessibleContext()
/*     */   {
/* 275 */     if (this.accessibleContext == null) {
/* 276 */       this.accessibleContext = new AccessibleJCheckBoxMenuItem();
/*     */     }
/* 278 */     return this.accessibleContext;
/*     */   }
/*     */ 
/*     */   protected class AccessibleJCheckBoxMenuItem extends JMenuItem.AccessibleJMenuItem
/*     */   {
/*     */     protected AccessibleJCheckBoxMenuItem()
/*     */     {
/* 296 */       super();
/*     */     }
/*     */ 
/*     */     public AccessibleRole getAccessibleRole()
/*     */     {
/* 304 */       return AccessibleRole.CHECK_BOX;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.JCheckBoxMenuItem
 * JD-Core Version:    0.6.2
 */