/*    */ package sun.tracing;
/*    */ 
/*    */ import com.sun.tracing.Probe;
/*    */ import java.lang.reflect.Field;
/*    */ 
/*    */ public abstract class ProbeSkeleton
/*    */   implements Probe
/*    */ {
/*    */   protected Class<?>[] parameters;
/*    */ 
/*    */   protected ProbeSkeleton(Class<?>[] paramArrayOfClass)
/*    */   {
/* 42 */     this.parameters = paramArrayOfClass;
/*    */   }
/*    */ 
/*    */   public abstract boolean isEnabled();
/*    */ 
/*    */   public abstract void uncheckedTrigger(Object[] paramArrayOfObject);
/*    */ 
/*    */   private static boolean isAssignable(Object paramObject, Class<?> paramClass)
/*    */   {
/* 57 */     if ((paramObject != null) && 
/* 58 */       (!paramClass.isInstance(paramObject))) {
/* 59 */       if (paramClass.isPrimitive())
/*    */         try
/*    */         {
/* 62 */           Field localField = paramObject.getClass().getField("TYPE");
/* 63 */           return paramClass.isAssignableFrom((Class)localField.get(null));
/*    */         }
/*    */         catch (Exception localException)
/*    */         {
/*    */         }
/* 68 */       return false;
/*    */     }
/*    */ 
/* 71 */     return true;
/*    */   }
/*    */ 
/*    */   public void trigger(Object[] paramArrayOfObject)
/*    */   {
/* 78 */     if (paramArrayOfObject.length != this.parameters.length) {
/* 79 */       throw new IllegalArgumentException("Wrong number of arguments");
/*    */     }
/* 81 */     for (int i = 0; i < this.parameters.length; i++) {
/* 82 */       if (!isAssignable(paramArrayOfObject[i], this.parameters[i])) {
/* 83 */         throw new IllegalArgumentException("Wrong type of argument at position " + i);
/*    */       }
/*    */     }
/*    */ 
/* 87 */     uncheckedTrigger(paramArrayOfObject);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.tracing.ProbeSkeleton
 * JD-Core Version:    0.6.2
 */