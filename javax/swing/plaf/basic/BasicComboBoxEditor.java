/*     */ package javax.swing.plaf.basic;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.event.FocusEvent;
/*     */ import java.awt.event.FocusListener;
/*     */ import java.lang.reflect.Method;
/*     */ import javax.swing.ComboBoxEditor;
/*     */ import javax.swing.JTextField;
/*     */ import javax.swing.border.Border;
/*     */ import javax.swing.plaf.UIResource;
/*     */ 
/*     */ public class BasicComboBoxEditor
/*     */   implements ComboBoxEditor, FocusListener
/*     */ {
/*     */   protected JTextField editor;
/*     */   private Object oldValue;
/*     */ 
/*     */   public BasicComboBoxEditor()
/*     */   {
/*  46 */     this.editor = createEditorComponent();
/*     */   }
/*     */ 
/*     */   public Component getEditorComponent() {
/*  50 */     return this.editor;
/*     */   }
/*     */ 
/*     */   protected JTextField createEditorComponent()
/*     */   {
/*  61 */     BorderlessTextField localBorderlessTextField = new BorderlessTextField("", 9);
/*  62 */     localBorderlessTextField.setBorder(null);
/*  63 */     return localBorderlessTextField;
/*     */   }
/*     */ 
/*     */   public void setItem(Object paramObject)
/*     */   {
/*     */     String str;
/*  74 */     if (paramObject != null) {
/*  75 */       str = paramObject.toString();
/*  76 */       this.oldValue = paramObject;
/*     */     } else {
/*  78 */       str = "";
/*     */     }
/*     */ 
/*  81 */     if (!str.equals(this.editor.getText()))
/*  82 */       this.editor.setText(str);
/*     */   }
/*     */ 
/*     */   public Object getItem()
/*     */   {
/*  87 */     Object localObject = this.editor.getText();
/*     */ 
/*  89 */     if ((this.oldValue != null) && (!(this.oldValue instanceof String)))
/*     */     {
/*  92 */       if (localObject.equals(this.oldValue.toString())) {
/*  93 */         return this.oldValue;
/*     */       }
/*     */ 
/*  96 */       Class localClass = this.oldValue.getClass();
/*     */       try {
/*  98 */         Method localMethod = localClass.getMethod("valueOf", new Class[] { String.class });
/*  99 */         localObject = localMethod.invoke(this.oldValue, new Object[] { this.editor.getText() });
/*     */       }
/*     */       catch (Exception localException)
/*     */       {
/*     */       }
/*     */     }
/* 105 */     return localObject;
/*     */   }
/*     */ 
/*     */   public void selectAll() {
/* 109 */     this.editor.selectAll();
/* 110 */     this.editor.requestFocus();
/*     */   }
/*     */ 
/*     */   public void focusGained(FocusEvent paramFocusEvent)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void focusLost(FocusEvent paramFocusEvent)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void addActionListener(ActionListener paramActionListener) {
/* 122 */     this.editor.addActionListener(paramActionListener);
/*     */   }
/*     */ 
/*     */   public void removeActionListener(ActionListener paramActionListener) {
/* 126 */     this.editor.removeActionListener(paramActionListener);
/*     */   }
/*     */ 
/*     */   static class BorderlessTextField extends JTextField {
/*     */     public BorderlessTextField(String paramString, int paramInt) {
/* 131 */       super(paramInt);
/*     */     }
/*     */ 
/*     */     public void setText(String paramString)
/*     */     {
/* 136 */       if (getText().equals(paramString)) {
/* 137 */         return;
/*     */       }
/* 139 */       super.setText(paramString);
/*     */     }
/*     */ 
/*     */     public void setBorder(Border paramBorder) {
/* 143 */       if (!(paramBorder instanceof BasicComboBoxEditor.UIResource))
/* 144 */         super.setBorder(paramBorder);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class UIResource extends BasicComboBoxEditor
/*     */     implements UIResource
/*     */   {
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.basic.BasicComboBoxEditor
 * JD-Core Version:    0.6.2
 */