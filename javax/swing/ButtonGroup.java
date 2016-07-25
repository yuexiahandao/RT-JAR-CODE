/*     */ package javax.swing;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class ButtonGroup
/*     */   implements Serializable
/*     */ {
/*  71 */   protected Vector<AbstractButton> buttons = new Vector();
/*     */ 
/*  76 */   ButtonModel selection = null;
/*     */ 
/*     */   public void add(AbstractButton paramAbstractButton)
/*     */   {
/*  88 */     if (paramAbstractButton == null) {
/*  89 */       return;
/*     */     }
/*  91 */     this.buttons.addElement(paramAbstractButton);
/*     */ 
/*  93 */     if (paramAbstractButton.isSelected()) {
/*  94 */       if (this.selection == null)
/*  95 */         this.selection = paramAbstractButton.getModel();
/*     */       else {
/*  97 */         paramAbstractButton.setSelected(false);
/*     */       }
/*     */     }
/*     */ 
/* 101 */     paramAbstractButton.getModel().setGroup(this);
/*     */   }
/*     */ 
/*     */   public void remove(AbstractButton paramAbstractButton)
/*     */   {
/* 109 */     if (paramAbstractButton == null) {
/* 110 */       return;
/*     */     }
/* 112 */     this.buttons.removeElement(paramAbstractButton);
/* 113 */     if (paramAbstractButton.getModel() == this.selection) {
/* 114 */       this.selection = null;
/*     */     }
/* 116 */     paramAbstractButton.getModel().setGroup(null);
/*     */   }
/*     */ 
/*     */   public void clearSelection()
/*     */   {
/* 126 */     if (this.selection != null) {
/* 127 */       ButtonModel localButtonModel = this.selection;
/* 128 */       this.selection = null;
/* 129 */       localButtonModel.setSelected(false);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Enumeration<AbstractButton> getElements()
/*     */   {
/* 139 */     return this.buttons.elements();
/*     */   }
/*     */ 
/*     */   public ButtonModel getSelection()
/*     */   {
/* 147 */     return this.selection;
/*     */   }
/*     */ 
/*     */   public void setSelected(ButtonModel paramButtonModel, boolean paramBoolean)
/*     */   {
/* 158 */     if ((paramBoolean) && (paramButtonModel != null) && (paramButtonModel != this.selection)) {
/* 159 */       ButtonModel localButtonModel = this.selection;
/* 160 */       this.selection = paramButtonModel;
/* 161 */       if (localButtonModel != null) {
/* 162 */         localButtonModel.setSelected(false);
/*     */       }
/* 164 */       paramButtonModel.setSelected(true);
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean isSelected(ButtonModel paramButtonModel)
/*     */   {
/* 174 */     return paramButtonModel == this.selection;
/*     */   }
/*     */ 
/*     */   public int getButtonCount()
/*     */   {
/* 183 */     if (this.buttons == null) {
/* 184 */       return 0;
/*     */     }
/* 186 */     return this.buttons.size();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.ButtonGroup
 * JD-Core Version:    0.6.2
 */