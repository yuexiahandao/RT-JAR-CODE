/*     */ package javax.swing;
/*     */ 
/*     */ import java.awt.AWTEvent;
/*     */ import java.awt.EventQueue;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.InputEvent;
/*     */ import java.awt.event.ItemEvent;
/*     */ import java.awt.event.ItemListener;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectOutputStream;
/*     */ import javax.accessibility.Accessible;
/*     */ import javax.accessibility.AccessibleContext;
/*     */ import javax.accessibility.AccessibleRole;
/*     */ import javax.accessibility.AccessibleState;
/*     */ import javax.swing.plaf.ButtonUI;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ 
/*     */ public class JToggleButton extends AbstractButton
/*     */   implements Accessible
/*     */ {
/*     */   private static final String uiClassID = "ToggleButtonUI";
/*     */ 
/*     */   public JToggleButton()
/*     */   {
/*  92 */     this(null, null, false);
/*     */   }
/*     */ 
/*     */   public JToggleButton(Icon paramIcon)
/*     */   {
/* 102 */     this(null, paramIcon, false);
/*     */   }
/*     */ 
/*     */   public JToggleButton(Icon paramIcon, boolean paramBoolean)
/*     */   {
/* 114 */     this(null, paramIcon, paramBoolean);
/*     */   }
/*     */ 
/*     */   public JToggleButton(String paramString)
/*     */   {
/* 123 */     this(paramString, null, false);
/*     */   }
/*     */ 
/*     */   public JToggleButton(String paramString, boolean paramBoolean)
/*     */   {
/* 135 */     this(paramString, null, paramBoolean);
/*     */   }
/*     */ 
/*     */   public JToggleButton(Action paramAction)
/*     */   {
/* 145 */     this();
/* 146 */     setAction(paramAction);
/*     */   }
/*     */ 
/*     */   public JToggleButton(String paramString, Icon paramIcon)
/*     */   {
/* 157 */     this(paramString, paramIcon, false);
/*     */   }
/*     */ 
/*     */   public JToggleButton(String paramString, Icon paramIcon, boolean paramBoolean)
/*     */   {
/* 171 */     setModel(new ToggleButtonModel());
/*     */ 
/* 173 */     this.model.setSelected(paramBoolean);
/*     */ 
/* 176 */     init(paramString, paramIcon);
/*     */   }
/*     */ 
/*     */   public void updateUI()
/*     */   {
/* 185 */     setUI((ButtonUI)UIManager.getUI(this));
/*     */   }
/*     */ 
/*     */   public String getUIClassID()
/*     */   {
/* 199 */     return "ToggleButtonUI";
/*     */   }
/*     */ 
/*     */   boolean shouldUpdateSelectedStateFromAction()
/*     */   {
/* 208 */     return true;
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*     */     throws IOException
/*     */   {
/* 324 */     paramObjectOutputStream.defaultWriteObject();
/* 325 */     if (getUIClassID().equals("ToggleButtonUI")) {
/* 326 */       byte b = JComponent.getWriteObjCounter(this);
/* 327 */       b = (byte)(b - 1); JComponent.setWriteObjCounter(this, b);
/* 328 */       if ((b == 0) && (this.ui != null))
/* 329 */         this.ui.installUI(this);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected String paramString()
/*     */   {
/* 345 */     return super.paramString();
/*     */   }
/*     */ 
/*     */   public AccessibleContext getAccessibleContext()
/*     */   {
/* 366 */     if (this.accessibleContext == null) {
/* 367 */       this.accessibleContext = new AccessibleJToggleButton();
/*     */     }
/* 369 */     return this.accessibleContext;
/*     */   }
/*     */ 
/*     */   protected class AccessibleJToggleButton extends AbstractButton.AccessibleAbstractButton
/*     */     implements ItemListener
/*     */   {
/*     */     public AccessibleJToggleButton()
/*     */     {
/* 391 */       super();
/* 392 */       JToggleButton.this.addItemListener(this);
/*     */     }
/*     */ 
/*     */     public void itemStateChanged(ItemEvent paramItemEvent)
/*     */     {
/* 400 */       JToggleButton localJToggleButton = (JToggleButton)paramItemEvent.getSource();
/* 401 */       if (JToggleButton.this.accessibleContext != null)
/* 402 */         if (localJToggleButton.isSelected()) {
/* 403 */           JToggleButton.this.accessibleContext.firePropertyChange("AccessibleState", null, AccessibleState.CHECKED);
/*     */         }
/*     */         else
/*     */         {
/* 407 */           JToggleButton.this.accessibleContext.firePropertyChange("AccessibleState", AccessibleState.CHECKED, null);
/*     */         }
/*     */     }
/*     */ 
/*     */     public AccessibleRole getAccessibleRole()
/*     */     {
/* 421 */       return AccessibleRole.TOGGLE_BUTTON;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class ToggleButtonModel extends DefaultButtonModel
/*     */   {
/*     */     public boolean isSelected()
/*     */     {
/* 240 */       return (this.stateMask & 0x2) != 0;
/*     */     }
/*     */ 
/*     */     public void setSelected(boolean paramBoolean)
/*     */     {
/* 251 */       ButtonGroup localButtonGroup = getGroup();
/* 252 */       if (localButtonGroup != null)
/*     */       {
/* 254 */         localButtonGroup.setSelected(this, paramBoolean);
/* 255 */         paramBoolean = localButtonGroup.isSelected(this);
/*     */       }
/*     */ 
/* 258 */       if (isSelected() == paramBoolean) {
/* 259 */         return;
/*     */       }
/*     */ 
/* 262 */       if (paramBoolean)
/* 263 */         this.stateMask |= 2;
/*     */       else {
/* 265 */         this.stateMask &= -3;
/*     */       }
/*     */ 
/* 269 */       fireStateChanged();
/*     */ 
/* 272 */       fireItemStateChanged(new ItemEvent(this, 701, this, isSelected() ? 1 : 2));
/*     */     }
/*     */ 
/*     */     public void setPressed(boolean paramBoolean)
/*     */     {
/* 284 */       if ((isPressed() == paramBoolean) || (!isEnabled())) {
/* 285 */         return;
/*     */       }
/*     */ 
/* 288 */       if ((!paramBoolean) && (isArmed())) {
/* 289 */         setSelected(!isSelected());
/*     */       }
/*     */ 
/* 292 */       if (paramBoolean)
/* 293 */         this.stateMask |= 4;
/*     */       else {
/* 295 */         this.stateMask &= -5;
/*     */       }
/*     */ 
/* 298 */       fireStateChanged();
/*     */ 
/* 300 */       if ((!isPressed()) && (isArmed())) {
/* 301 */         int i = 0;
/* 302 */         AWTEvent localAWTEvent = EventQueue.getCurrentEvent();
/* 303 */         if ((localAWTEvent instanceof InputEvent))
/* 304 */           i = ((InputEvent)localAWTEvent).getModifiers();
/* 305 */         else if ((localAWTEvent instanceof ActionEvent)) {
/* 306 */           i = ((ActionEvent)localAWTEvent).getModifiers();
/*     */         }
/* 308 */         fireActionPerformed(new ActionEvent(this, 1001, getActionCommand(), EventQueue.getMostRecentEventTime(), i));
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.JToggleButton
 * JD-Core Version:    0.6.2
 */