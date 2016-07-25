/*     */ package javax.swing;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class DefaultComboBoxModel<E> extends AbstractListModel<E>
/*     */   implements MutableComboBoxModel<E>, Serializable
/*     */ {
/*     */   Vector<E> objects;
/*     */   Object selectedObject;
/*     */ 
/*     */   public DefaultComboBoxModel()
/*     */   {
/*  48 */     this.objects = new Vector();
/*     */   }
/*     */ 
/*     */   public DefaultComboBoxModel(E[] paramArrayOfE)
/*     */   {
/*  58 */     this.objects = new Vector(paramArrayOfE.length);
/*     */ 
/*  61 */     int i = 0; for (int j = paramArrayOfE.length; i < j; i++) {
/*  62 */       this.objects.addElement(paramArrayOfE[i]);
/*     */     }
/*  64 */     if (getSize() > 0)
/*  65 */       this.selectedObject = getElementAt(0);
/*     */   }
/*     */ 
/*     */   public DefaultComboBoxModel(Vector<E> paramVector)
/*     */   {
/*  76 */     this.objects = paramVector;
/*     */ 
/*  78 */     if (getSize() > 0)
/*  79 */       this.selectedObject = getElementAt(0);
/*     */   }
/*     */ 
/*     */   public void setSelectedItem(Object paramObject)
/*     */   {
/*  90 */     if (((this.selectedObject != null) && (!this.selectedObject.equals(paramObject))) || ((this.selectedObject == null) && (paramObject != null)))
/*     */     {
/*  92 */       this.selectedObject = paramObject;
/*  93 */       fireContentsChanged(this, -1, -1);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Object getSelectedItem()
/*     */   {
/*  99 */     return this.selectedObject;
/*     */   }
/*     */ 
/*     */   public int getSize()
/*     */   {
/* 104 */     return this.objects.size();
/*     */   }
/*     */ 
/*     */   public E getElementAt(int paramInt)
/*     */   {
/* 109 */     if ((paramInt >= 0) && (paramInt < this.objects.size())) {
/* 110 */       return this.objects.elementAt(paramInt);
/*     */     }
/* 112 */     return null;
/*     */   }
/*     */ 
/*     */   public int getIndexOf(Object paramObject)
/*     */   {
/* 123 */     return this.objects.indexOf(paramObject);
/*     */   }
/*     */ 
/*     */   public void addElement(E paramE)
/*     */   {
/* 128 */     this.objects.addElement(paramE);
/* 129 */     fireIntervalAdded(this, this.objects.size() - 1, this.objects.size() - 1);
/* 130 */     if ((this.objects.size() == 1) && (this.selectedObject == null) && (paramE != null))
/* 131 */       setSelectedItem(paramE);
/*     */   }
/*     */ 
/*     */   public void insertElementAt(E paramE, int paramInt)
/*     */   {
/* 137 */     this.objects.insertElementAt(paramE, paramInt);
/* 138 */     fireIntervalAdded(this, paramInt, paramInt);
/*     */   }
/*     */ 
/*     */   public void removeElementAt(int paramInt)
/*     */   {
/* 143 */     if (getElementAt(paramInt) == this.selectedObject) {
/* 144 */       if (paramInt == 0) {
/* 145 */         setSelectedItem(getSize() == 1 ? null : getElementAt(paramInt + 1));
/*     */       }
/*     */       else {
/* 148 */         setSelectedItem(getElementAt(paramInt - 1));
/*     */       }
/*     */     }
/*     */ 
/* 152 */     this.objects.removeElementAt(paramInt);
/*     */ 
/* 154 */     fireIntervalRemoved(this, paramInt, paramInt);
/*     */   }
/*     */ 
/*     */   public void removeElement(Object paramObject)
/*     */   {
/* 159 */     int i = this.objects.indexOf(paramObject);
/* 160 */     if (i != -1)
/* 161 */       removeElementAt(i);
/*     */   }
/*     */ 
/*     */   public void removeAllElements()
/*     */   {
/* 169 */     if (this.objects.size() > 0) {
/* 170 */       int i = 0;
/* 171 */       int j = this.objects.size() - 1;
/* 172 */       this.objects.removeAllElements();
/* 173 */       this.selectedObject = null;
/* 174 */       fireIntervalRemoved(this, i, j);
/*     */     } else {
/* 176 */       this.selectedObject = null;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.DefaultComboBoxModel
 * JD-Core Version:    0.6.2
 */