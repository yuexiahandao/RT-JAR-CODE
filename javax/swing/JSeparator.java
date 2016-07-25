/*     */ package javax.swing;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectOutputStream;
/*     */ import javax.accessibility.Accessible;
/*     */ import javax.accessibility.AccessibleContext;
/*     */ import javax.accessibility.AccessibleRole;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.SeparatorUI;
/*     */ 
/*     */ public class JSeparator extends JComponent
/*     */   implements SwingConstants, Accessible
/*     */ {
/*     */   private static final String uiClassID = "SeparatorUI";
/*  82 */   private int orientation = 0;
/*     */ 
/*     */   public JSeparator()
/*     */   {
/*  87 */     this(0);
/*     */   }
/*     */ 
/*     */   public JSeparator(int paramInt)
/*     */   {
/* 103 */     checkOrientation(paramInt);
/* 104 */     this.orientation = paramInt;
/* 105 */     setFocusable(false);
/* 106 */     updateUI();
/*     */   }
/*     */ 
/*     */   public SeparatorUI getUI()
/*     */   {
/* 115 */     return (SeparatorUI)this.ui;
/*     */   }
/*     */ 
/*     */   public void setUI(SeparatorUI paramSeparatorUI)
/*     */   {
/* 130 */     super.setUI(paramSeparatorUI);
/*     */   }
/*     */ 
/*     */   public void updateUI()
/*     */   {
/* 139 */     setUI((SeparatorUI)UIManager.getUI(this));
/*     */   }
/*     */ 
/*     */   public String getUIClassID()
/*     */   {
/* 151 */     return "SeparatorUI";
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*     */     throws IOException
/*     */   {
/* 161 */     paramObjectOutputStream.defaultWriteObject();
/* 162 */     if (getUIClassID().equals("SeparatorUI")) {
/* 163 */       byte b = JComponent.getWriteObjCounter(this);
/* 164 */       b = (byte)(b - 1); JComponent.setWriteObjCounter(this, b);
/* 165 */       if ((b == 0) && (this.ui != null))
/* 166 */         this.ui.installUI(this);
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getOrientation()
/*     */   {
/* 183 */     return this.orientation;
/*     */   }
/*     */ 
/*     */   public void setOrientation(int paramInt)
/*     */   {
/* 206 */     if (this.orientation == paramInt) {
/* 207 */       return;
/*     */     }
/* 209 */     int i = this.orientation;
/* 210 */     checkOrientation(paramInt);
/* 211 */     this.orientation = paramInt;
/* 212 */     firePropertyChange("orientation", i, paramInt);
/* 213 */     revalidate();
/* 214 */     repaint();
/*     */   }
/*     */ 
/*     */   private void checkOrientation(int paramInt)
/*     */   {
/* 219 */     switch (paramInt)
/*     */     {
/*     */     case 0:
/*     */     case 1:
/* 223 */       break;
/*     */     default:
/* 225 */       throw new IllegalArgumentException("orientation must be one of: VERTICAL, HORIZONTAL");
/*     */     }
/*     */   }
/*     */ 
/*     */   protected String paramString()
/*     */   {
/* 241 */     String str = this.orientation == 0 ? "HORIZONTAL" : "VERTICAL";
/*     */ 
/* 244 */     return super.paramString() + ",orientation=" + str;
/*     */   }
/*     */ 
/*     */   public AccessibleContext getAccessibleContext()
/*     */   {
/* 262 */     if (this.accessibleContext == null) {
/* 263 */       this.accessibleContext = new AccessibleJSeparator();
/*     */     }
/* 265 */     return this.accessibleContext;
/*     */   }
/*     */ 
/*     */   protected class AccessibleJSeparator extends JComponent.AccessibleJComponent
/*     */   {
/*     */     protected AccessibleJSeparator()
/*     */     {
/* 282 */       super();
/*     */     }
/*     */ 
/*     */     public AccessibleRole getAccessibleRole()
/*     */     {
/* 291 */       return AccessibleRole.SEPARATOR;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.JSeparator
 * JD-Core Version:    0.6.2
 */