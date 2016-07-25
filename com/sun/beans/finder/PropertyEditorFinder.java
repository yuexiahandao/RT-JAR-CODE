/*    */ package com.sun.beans.finder;
/*    */ 
/*    */ import com.sun.beans.WeakCache;
/*    */ import com.sun.beans.editors.BooleanEditor;
/*    */ import com.sun.beans.editors.ByteEditor;
/*    */ import com.sun.beans.editors.DoubleEditor;
/*    */ import com.sun.beans.editors.EnumEditor;
/*    */ import com.sun.beans.editors.FloatEditor;
/*    */ import com.sun.beans.editors.IntegerEditor;
/*    */ import com.sun.beans.editors.LongEditor;
/*    */ import com.sun.beans.editors.ShortEditor;
/*    */ import java.beans.PropertyEditor;
/*    */ 
/*    */ public final class PropertyEditorFinder extends InstanceFinder<PropertyEditor>
/*    */ {
/*    */   private static final String DEFAULT = "sun.beans.editors";
/*    */   private static final String DEFAULT_NEW = "com.sun.beans.editors";
/*    */   private final WeakCache<Class<?>, Class<?>> registry;
/*    */ 
/*    */   public PropertyEditorFinder()
/*    */   {
/* 57 */     super(PropertyEditor.class, false, "Editor", new String[] { "sun.beans.editors" });
/*    */ 
/* 59 */     this.registry = new WeakCache();
/* 60 */     this.registry.put(Byte.TYPE, ByteEditor.class);
/* 61 */     this.registry.put(Short.TYPE, ShortEditor.class);
/* 62 */     this.registry.put(Integer.TYPE, IntegerEditor.class);
/* 63 */     this.registry.put(Long.TYPE, LongEditor.class);
/* 64 */     this.registry.put(Boolean.TYPE, BooleanEditor.class);
/* 65 */     this.registry.put(Float.TYPE, FloatEditor.class);
/* 66 */     this.registry.put(Double.TYPE, DoubleEditor.class);
/*    */   }
/*    */ 
/*    */   public void register(Class<?> paramClass1, Class<?> paramClass2) {
/* 70 */     synchronized (this.registry) {
/* 71 */       this.registry.put(paramClass1, paramClass2);
/*    */     }
/*    */   }
/*    */ 
/*    */   public PropertyEditor find(Class<?> paramClass)
/*    */   {
/*    */     Class localClass;
/* 78 */     synchronized (this.registry) {
/* 79 */       localClass = (Class)this.registry.get(paramClass);
/*    */     }
/* 81 */     ??? = (PropertyEditor)instantiate(localClass, null);
/* 82 */     if (??? == null) {
/* 83 */       ??? = (PropertyEditor)super.find(paramClass);
/* 84 */       if ((??? == null) && (null != paramClass.getEnumConstants())) {
/* 85 */         ??? = new EnumEditor(paramClass);
/*    */       }
/*    */     }
/* 88 */     return ???;
/*    */   }
/*    */ 
/*    */   protected PropertyEditor instantiate(Class<?> paramClass, String paramString1, String paramString2)
/*    */   {
/* 93 */     return (PropertyEditor)super.instantiate(paramClass, "sun.beans.editors".equals(paramString1) ? "com.sun.beans.editors" : paramString1, paramString2);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.beans.finder.PropertyEditorFinder
 * JD-Core Version:    0.6.2
 */