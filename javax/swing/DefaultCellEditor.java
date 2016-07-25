/*     */ package javax.swing;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.event.ItemEvent;
/*     */ import java.awt.event.ItemListener;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.beans.ConstructorProperties;
/*     */ import java.io.Serializable;
/*     */ import java.util.EventObject;
/*     */ import javax.swing.table.TableCellEditor;
/*     */ import javax.swing.table.TableCellRenderer;
/*     */ import javax.swing.tree.TreeCellEditor;
/*     */ 
/*     */ public class DefaultCellEditor extends AbstractCellEditor
/*     */   implements TableCellEditor, TreeCellEditor
/*     */ {
/*     */   protected JComponent editorComponent;
/*     */   protected EditorDelegate delegate;
/*  73 */   protected int clickCountToStart = 1;
/*     */ 
/*     */   @ConstructorProperties({"component"})
/*     */   public DefaultCellEditor(final JTextField paramJTextField)
/*     */   {
/*  86 */     this.editorComponent = paramJTextField;
/*  87 */     this.clickCountToStart = 2;
/*  88 */     this.delegate = new EditorDelegate(paramJTextField) {
/*     */       public void setValue(Object paramAnonymousObject) {
/*  90 */         paramJTextField.setText(paramAnonymousObject != null ? paramAnonymousObject.toString() : "");
/*     */       }
/*     */ 
/*     */       public Object getCellEditorValue() {
/*  94 */         return paramJTextField.getText();
/*     */       }
/*     */     };
/*  97 */     paramJTextField.addActionListener(this.delegate);
/*     */   }
/*     */ 
/*     */   public DefaultCellEditor(final JCheckBox paramJCheckBox)
/*     */   {
/* 106 */     this.editorComponent = paramJCheckBox;
/* 107 */     this.delegate = new EditorDelegate(paramJCheckBox) {
/*     */       public void setValue(Object paramAnonymousObject) {
/* 109 */         boolean bool = false;
/* 110 */         if ((paramAnonymousObject instanceof Boolean)) {
/* 111 */           bool = ((Boolean)paramAnonymousObject).booleanValue();
/*     */         }
/* 113 */         else if ((paramAnonymousObject instanceof String)) {
/* 114 */           bool = paramAnonymousObject.equals("true");
/*     */         }
/* 116 */         paramJCheckBox.setSelected(bool);
/*     */       }
/*     */ 
/*     */       public Object getCellEditorValue() {
/* 120 */         return Boolean.valueOf(paramJCheckBox.isSelected());
/*     */       }
/*     */     };
/* 123 */     paramJCheckBox.addActionListener(this.delegate);
/* 124 */     paramJCheckBox.setRequestFocusEnabled(false);
/*     */   }
/*     */ 
/*     */   public DefaultCellEditor(final JComboBox paramJComboBox)
/*     */   {
/* 134 */     this.editorComponent = paramJComboBox;
/* 135 */     paramJComboBox.putClientProperty("JComboBox.isTableCellEditor", Boolean.TRUE);
/* 136 */     this.delegate = new EditorDelegate(paramJComboBox) {
/*     */       public void setValue(Object paramAnonymousObject) {
/* 138 */         paramJComboBox.setSelectedItem(paramAnonymousObject);
/*     */       }
/*     */ 
/*     */       public Object getCellEditorValue() {
/* 142 */         return paramJComboBox.getSelectedItem();
/*     */       }
/*     */ 
/*     */       public boolean shouldSelectCell(EventObject paramAnonymousEventObject) {
/* 146 */         if ((paramAnonymousEventObject instanceof MouseEvent)) {
/* 147 */           MouseEvent localMouseEvent = (MouseEvent)paramAnonymousEventObject;
/* 148 */           return localMouseEvent.getID() != 506;
/*     */         }
/* 150 */         return true;
/*     */       }
/*     */       public boolean stopCellEditing() {
/* 153 */         if (paramJComboBox.isEditable())
/*     */         {
/* 155 */           paramJComboBox.actionPerformed(new ActionEvent(DefaultCellEditor.this, 0, ""));
/*     */         }
/*     */ 
/* 158 */         return super.stopCellEditing();
/*     */       }
/*     */     };
/* 161 */     paramJComboBox.addActionListener(this.delegate);
/*     */   }
/*     */ 
/*     */   public Component getComponent()
/*     */   {
/* 170 */     return this.editorComponent;
/*     */   }
/*     */ 
/*     */   public void setClickCountToStart(int paramInt)
/*     */   {
/* 184 */     this.clickCountToStart = paramInt;
/*     */   }
/*     */ 
/*     */   public int getClickCountToStart()
/*     */   {
/* 192 */     return this.clickCountToStart;
/*     */   }
/*     */ 
/*     */   public Object getCellEditorValue()
/*     */   {
/* 206 */     return this.delegate.getCellEditorValue();
/*     */   }
/*     */ 
/*     */   public boolean isCellEditable(EventObject paramEventObject)
/*     */   {
/* 215 */     return this.delegate.isCellEditable(paramEventObject);
/*     */   }
/*     */ 
/*     */   public boolean shouldSelectCell(EventObject paramEventObject)
/*     */   {
/* 224 */     return this.delegate.shouldSelectCell(paramEventObject);
/*     */   }
/*     */ 
/*     */   public boolean stopCellEditing()
/*     */   {
/* 233 */     return this.delegate.stopCellEditing();
/*     */   }
/*     */ 
/*     */   public void cancelCellEditing()
/*     */   {
/* 242 */     this.delegate.cancelCellEditing();
/*     */   }
/*     */ 
/*     */   public Component getTreeCellEditorComponent(JTree paramJTree, Object paramObject, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, int paramInt)
/*     */   {
/* 254 */     String str = paramJTree.convertValueToText(paramObject, paramBoolean1, paramBoolean2, paramBoolean3, paramInt, false);
/*     */ 
/* 257 */     this.delegate.setValue(str);
/* 258 */     return this.editorComponent;
/*     */   }
/*     */ 
/*     */   public Component getTableCellEditorComponent(JTable paramJTable, Object paramObject, boolean paramBoolean, int paramInt1, int paramInt2)
/*     */   {
/* 268 */     this.delegate.setValue(paramObject);
/* 269 */     if ((this.editorComponent instanceof JCheckBox))
/*     */     {
/* 276 */       TableCellRenderer localTableCellRenderer = paramJTable.getCellRenderer(paramInt1, paramInt2);
/* 277 */       Component localComponent = localTableCellRenderer.getTableCellRendererComponent(paramJTable, paramObject, paramBoolean, true, paramInt1, paramInt2);
/*     */ 
/* 279 */       if (localComponent != null) {
/* 280 */         this.editorComponent.setOpaque(true);
/* 281 */         this.editorComponent.setBackground(localComponent.getBackground());
/* 282 */         if ((localComponent instanceof JComponent))
/* 283 */           this.editorComponent.setBorder(((JComponent)localComponent).getBorder());
/*     */       }
/*     */       else {
/* 286 */         this.editorComponent.setOpaque(false);
/*     */       }
/*     */     }
/* 289 */     return this.editorComponent;
/*     */   }
/*     */ 
/*     */   protected class EditorDelegate
/*     */     implements ActionListener, ItemListener, Serializable
/*     */   {
/*     */     protected Object value;
/*     */ 
/*     */     protected EditorDelegate()
/*     */     {
/*     */     }
/*     */ 
/*     */     public Object getCellEditorValue()
/*     */     {
/* 310 */       return this.value;
/*     */     }
/*     */ 
/*     */     public void setValue(Object paramObject)
/*     */     {
/* 318 */       this.value = paramObject;
/*     */     }
/*     */ 
/*     */     public boolean isCellEditable(EventObject paramEventObject)
/*     */     {
/* 333 */       if ((paramEventObject instanceof MouseEvent)) {
/* 334 */         return ((MouseEvent)paramEventObject).getClickCount() >= DefaultCellEditor.this.clickCountToStart;
/*     */       }
/* 336 */       return true;
/*     */     }
/*     */ 
/*     */     public boolean shouldSelectCell(EventObject paramEventObject)
/*     */     {
/* 348 */       return true;
/*     */     }
/*     */ 
/*     */     public boolean startCellEditing(EventObject paramEventObject)
/*     */     {
/* 357 */       return true;
/*     */     }
/*     */ 
/*     */     public boolean stopCellEditing()
/*     */     {
/* 368 */       DefaultCellEditor.this.fireEditingStopped();
/* 369 */       return true;
/*     */     }
/*     */ 
/*     */     public void cancelCellEditing()
/*     */     {
/* 376 */       DefaultCellEditor.this.fireEditingCanceled();
/*     */     }
/*     */ 
/*     */     public void actionPerformed(ActionEvent paramActionEvent)
/*     */     {
/* 385 */       DefaultCellEditor.this.stopCellEditing();
/*     */     }
/*     */ 
/*     */     public void itemStateChanged(ItemEvent paramItemEvent)
/*     */     {
/* 394 */       DefaultCellEditor.this.stopCellEditing();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.DefaultCellEditor
 * JD-Core Version:    0.6.2
 */