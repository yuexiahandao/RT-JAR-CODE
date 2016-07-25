/*     */ package com.sun.beans.editors;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Rectangle;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.beans.PropertyEditor;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ public class EnumEditor
/*     */   implements PropertyEditor
/*     */ {
/*  46 */   private final List<PropertyChangeListener> listeners = new ArrayList();
/*     */   private final Class type;
/*     */   private final String[] tags;
/*     */   private Object value;
/*     */ 
/*     */   public EnumEditor(Class paramClass)
/*     */   {
/*  54 */     Object[] arrayOfObject = paramClass.getEnumConstants();
/*  55 */     if (arrayOfObject == null) {
/*  56 */       throw new IllegalArgumentException("Unsupported " + paramClass);
/*     */     }
/*  58 */     this.type = paramClass;
/*  59 */     this.tags = new String[arrayOfObject.length];
/*  60 */     for (int i = 0; i < arrayOfObject.length; i++)
/*  61 */       this.tags[i] = ((Enum)arrayOfObject[i]).name();
/*     */   }
/*     */ 
/*     */   public Object getValue()
/*     */   {
/*  66 */     return this.value;
/*     */   }
/*     */ 
/*     */   public void setValue(Object paramObject) {
/*  70 */     if ((paramObject != null) && (!this.type.isInstance(paramObject)))
/*  71 */       throw new IllegalArgumentException("Unsupported value: " + paramObject);
/*     */     Object localObject1;
/*     */     PropertyChangeListener[] arrayOfPropertyChangeListener;
/*  75 */     synchronized (this.listeners) {
/*  76 */       localObject1 = this.value;
/*  77 */       this.value = paramObject;
/*     */ 
/*  79 */       if (paramObject == null ? localObject1 == null : paramObject.equals(localObject1)) {
/*  80 */         return;
/*     */       }
/*  82 */       int i = this.listeners.size();
/*  83 */       if (i == 0) {
/*  84 */         return;
/*     */       }
/*  86 */       arrayOfPropertyChangeListener = (PropertyChangeListener[])this.listeners.toArray(new PropertyChangeListener[i]);
/*     */     }
/*  88 */     ??? = new PropertyChangeEvent(this, null, localObject1, paramObject);
/*  89 */     for (Object localObject4 : arrayOfPropertyChangeListener)
/*  90 */       localObject4.propertyChange((PropertyChangeEvent)???);
/*     */   }
/*     */ 
/*     */   public String getAsText()
/*     */   {
/*  95 */     return this.value != null ? ((Enum)this.value).name() : null;
/*     */   }
/*     */ 
/*     */   public void setAsText(String paramString)
/*     */   {
/* 101 */     setValue(paramString != null ? Enum.valueOf(this.type, paramString) : null);
/*     */   }
/*     */ 
/*     */   public String[] getTags()
/*     */   {
/* 107 */     return (String[])this.tags.clone();
/*     */   }
/*     */ 
/*     */   public String getJavaInitializationString() {
/* 111 */     String str = getAsText();
/* 112 */     return str != null ? this.type.getName() + '.' + str : "null";
/*     */   }
/*     */ 
/*     */   public boolean isPaintable()
/*     */   {
/* 118 */     return false;
/*     */   }
/*     */ 
/*     */   public void paintValue(Graphics paramGraphics, Rectangle paramRectangle) {
/*     */   }
/*     */ 
/*     */   public boolean supportsCustomEditor() {
/* 125 */     return false;
/*     */   }
/*     */ 
/*     */   public Component getCustomEditor() {
/* 129 */     return null;
/*     */   }
/*     */ 
/*     */   public void addPropertyChangeListener(PropertyChangeListener paramPropertyChangeListener) {
/* 133 */     synchronized (this.listeners) {
/* 134 */       this.listeners.add(paramPropertyChangeListener);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void removePropertyChangeListener(PropertyChangeListener paramPropertyChangeListener) {
/* 139 */     synchronized (this.listeners) {
/* 140 */       this.listeners.remove(paramPropertyChangeListener);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.beans.editors.EnumEditor
 * JD-Core Version:    0.6.2
 */