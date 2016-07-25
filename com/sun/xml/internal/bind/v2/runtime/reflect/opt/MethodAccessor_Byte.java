/*    */ package com.sun.xml.internal.bind.v2.runtime.reflect.opt;
/*    */ 
/*    */ import com.sun.xml.internal.bind.v2.runtime.reflect.Accessor;
/*    */ 
/*    */ public class MethodAccessor_Byte extends Accessor
/*    */ {
/*    */   public MethodAccessor_Byte()
/*    */   {
/* 40 */     super(Byte.class);
/*    */   }
/*    */ 
/*    */   public Object get(Object bean) {
/* 44 */     return Byte.valueOf(((Bean)bean).get_byte());
/*    */   }
/*    */ 
/*    */   public void set(Object bean, Object value) {
/* 48 */     ((Bean)bean).set_byte(value == null ? Const.default_value_byte : ((Byte)value).byteValue());
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.reflect.opt.MethodAccessor_Byte
 * JD-Core Version:    0.6.2
 */