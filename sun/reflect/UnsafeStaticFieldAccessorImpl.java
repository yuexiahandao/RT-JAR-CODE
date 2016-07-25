/*    */ package sun.reflect;
/*    */ 
/*    */ import java.lang.reflect.Field;
/*    */ import sun.misc.Unsafe;
/*    */ 
/*    */ abstract class UnsafeStaticFieldAccessorImpl extends UnsafeFieldAccessorImpl
/*    */ {
/*    */   protected Object base;
/*    */ 
/*    */   UnsafeStaticFieldAccessorImpl(Field paramField)
/*    */   {
/* 49 */     super(paramField);
/* 50 */     this.base = unsafe.staticFieldBase(paramField);
/*    */   }
/*    */ 
/*    */   static
/*    */   {
/* 42 */     Reflection.registerFieldsToFilter(UnsafeStaticFieldAccessorImpl.class, new String[] { "base" });
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.reflect.UnsafeStaticFieldAccessorImpl
 * JD-Core Version:    0.6.2
 */