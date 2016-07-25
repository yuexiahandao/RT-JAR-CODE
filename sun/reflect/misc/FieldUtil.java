/*    */ package sun.reflect.misc;
/*    */ 
/*    */ import java.lang.reflect.Field;
/*    */ 
/*    */ public final class FieldUtil
/*    */ {
/*    */   public static Field getField(Class paramClass, String paramString)
/*    */     throws NoSuchFieldException
/*    */   {
/* 40 */     ReflectUtil.checkPackageAccess(paramClass);
/* 41 */     return paramClass.getField(paramString);
/*    */   }
/*    */ 
/*    */   public static Field[] getFields(Class paramClass) {
/* 45 */     ReflectUtil.checkPackageAccess(paramClass);
/* 46 */     return paramClass.getFields();
/*    */   }
/*    */ 
/*    */   public static Field[] getDeclaredFields(Class paramClass) {
/* 50 */     ReflectUtil.checkPackageAccess(paramClass);
/* 51 */     return paramClass.getDeclaredFields();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.reflect.misc.FieldUtil
 * JD-Core Version:    0.6.2
 */