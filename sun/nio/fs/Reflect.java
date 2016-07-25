/*    */ package sun.nio.fs;
/*    */ 
/*    */ import java.lang.reflect.AccessibleObject;
/*    */ import java.lang.reflect.Field;
/*    */ import java.security.AccessController;
/*    */ import java.security.PrivilegedAction;
/*    */ 
/*    */ class Reflect
/*    */ {
/*    */   private static void setAccessible(AccessibleObject paramAccessibleObject)
/*    */   {
/* 40 */     AccessController.doPrivileged(new PrivilegedAction()
/*    */     {
/*    */       public Object run() {
/* 43 */         this.val$ao.setAccessible(true);
/* 44 */         return null;
/*    */       }
/*    */     });
/*    */   }
/*    */ 
/*    */   static Field lookupField(String paramString1, String paramString2)
/*    */   {
/*    */     try
/*    */     {
/* 53 */       Class localClass = Class.forName(paramString1);
/* 54 */       Field localField = localClass.getDeclaredField(paramString2);
/* 55 */       setAccessible(localField);
/* 56 */       return localField;
/*    */     } catch (ClassNotFoundException localClassNotFoundException) {
/* 58 */       throw new AssertionError(localClassNotFoundException);
/*    */     } catch (NoSuchFieldException localNoSuchFieldException) {
/* 60 */       throw new AssertionError(localNoSuchFieldException);
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.nio.fs.Reflect
 * JD-Core Version:    0.6.2
 */