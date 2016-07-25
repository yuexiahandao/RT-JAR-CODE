/*    */ package sun.org.mozilla.javascript.internal.jdk15;
/*    */ 
/*    */ import java.lang.reflect.Constructor;
/*    */ import java.lang.reflect.Member;
/*    */ import java.lang.reflect.Method;
/*    */ import java.util.Iterator;
/*    */ import sun.org.mozilla.javascript.internal.Context;
/*    */ import sun.org.mozilla.javascript.internal.Scriptable;
/*    */ import sun.org.mozilla.javascript.internal.Wrapper;
/*    */ import sun.org.mozilla.javascript.internal.jdk13.VMBridge_jdk13;
/*    */ 
/*    */ public class VMBridge_jdk15 extends VMBridge_jdk13
/*    */ {
/*    */   public VMBridge_jdk15()
/*    */     throws SecurityException, InstantiationException
/*    */   {
/*    */     try
/*    */     {
/* 53 */       Method.class.getMethod("isVarArgs", (Class[])null);
/*    */     }
/*    */     catch (NoSuchMethodException localNoSuchMethodException)
/*    */     {
/* 57 */       throw new InstantiationException(localNoSuchMethodException.getMessage());
/*    */     }
/*    */   }
/*    */ 
/*    */   public boolean isVarArgs(Member paramMember)
/*    */   {
/* 63 */     if ((paramMember instanceof Method))
/* 64 */       return ((Method)paramMember).isVarArgs();
/* 65 */     if ((paramMember instanceof Constructor)) {
/* 66 */       return ((Constructor)paramMember).isVarArgs();
/*    */     }
/* 68 */     return false;
/*    */   }
/*    */ 
/*    */   public Iterator<?> getJavaIterator(Context paramContext, Scriptable paramScriptable, Object paramObject)
/*    */   {
/* 78 */     if ((paramObject instanceof Wrapper)) {
/* 79 */       Object localObject = ((Wrapper)paramObject).unwrap();
/* 80 */       Iterator localIterator = null;
/* 81 */       if ((localObject instanceof Iterator))
/* 82 */         localIterator = (Iterator)localObject;
/* 83 */       if ((localObject instanceof Iterable))
/* 84 */         localIterator = ((Iterable)localObject).iterator();
/* 85 */       return localIterator;
/*    */     }
/* 87 */     return null;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.jdk15.VMBridge_jdk15
 * JD-Core Version:    0.6.2
 */