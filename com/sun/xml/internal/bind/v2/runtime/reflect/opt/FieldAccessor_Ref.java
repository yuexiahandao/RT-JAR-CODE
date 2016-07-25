/*    */ package com.sun.xml.internal.bind.v2.runtime.reflect.opt;
/*    */ 
/*    */ import com.sun.xml.internal.bind.v2.runtime.reflect.Accessor;
/*    */ 
/*    */ public class FieldAccessor_Ref extends Accessor
/*    */ {
/*    */   public FieldAccessor_Ref()
/*    */   {
/* 37 */     super(Ref.class);
/*    */   }
/*    */ 
/*    */   public Object get(Object bean) {
/* 41 */     return ((Bean)bean).f_ref;
/*    */   }
/*    */ 
/*    */   public void set(Object bean, Object value) {
/* 45 */     ((Bean)bean).f_ref = ((Ref)value);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.reflect.opt.FieldAccessor_Ref
 * JD-Core Version:    0.6.2
 */