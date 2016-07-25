/*    */ package sun.corba;
/*    */ 
/*    */ import com.sun.corba.se.impl.io.ValueUtility;
/*    */ import sun.misc.Unsafe;
/*    */ 
/*    */ public class SharedSecrets
/*    */ {
/* 44 */   private static final Unsafe unsafe = Unsafe.getUnsafe();
/*    */   private static JavaCorbaAccess javaCorbaAccess;
/*    */ 
/*    */   public static JavaCorbaAccess getJavaCorbaAccess()
/*    */   {
/* 48 */     if (javaCorbaAccess == null)
/*    */     {
/* 51 */       unsafe.ensureClassInitialized(ValueUtility.class);
/*    */     }
/* 53 */     return javaCorbaAccess;
/*    */   }
/*    */ 
/*    */   public static void setJavaCorbaAccess(JavaCorbaAccess paramJavaCorbaAccess) {
/* 57 */     javaCorbaAccess = paramJavaCorbaAccess;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.corba.SharedSecrets
 * JD-Core Version:    0.6.2
 */