/*    */ package com.sun.xml.internal.bind.v2.runtime.reflect.opt;
/*    */ 
/*    */ import com.sun.xml.internal.bind.v2.runtime.reflect.Accessor;
/*    */ 
/*    */ public class MethodAccessor_Float extends Accessor
/*    */ {
/*    */   public MethodAccessor_Float()
/*    */   {
/* 40 */     super(Float.class);
/*    */   }
/*    */ 
/*    */   public Object get(Object bean) {
/* 44 */     return Float.valueOf(((Bean)bean).get_float());
/*    */   }
/*    */ 
/*    */   public void set(Object bean, Object value) {
/* 48 */     ((Bean)bean).set_float(value == null ? Const.default_value_float : ((Float)value).floatValue());
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.reflect.opt.MethodAccessor_Float
 * JD-Core Version:    0.6.2
 */