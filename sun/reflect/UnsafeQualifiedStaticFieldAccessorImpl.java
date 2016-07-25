/*    */ package sun.reflect;
/*    */ 
/*    */ import java.lang.reflect.Field;
/*    */ 
/*    */ abstract class UnsafeQualifiedStaticFieldAccessorImpl extends UnsafeStaticFieldAccessorImpl
/*    */ {
/*    */   protected final boolean isReadOnly;
/*    */ 
/*    */   UnsafeQualifiedStaticFieldAccessorImpl(Field paramField, boolean paramBoolean)
/*    */   {
/* 42 */     super(paramField);
/* 43 */     this.isReadOnly = paramBoolean;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.reflect.UnsafeQualifiedStaticFieldAccessorImpl
 * JD-Core Version:    0.6.2
 */