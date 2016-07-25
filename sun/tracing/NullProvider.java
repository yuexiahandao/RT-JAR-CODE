/*    */ package sun.tracing;
/*    */ 
/*    */ import com.sun.tracing.Provider;
/*    */ import java.lang.reflect.Method;
/*    */ 
/*    */ class NullProvider extends ProviderSkeleton
/*    */ {
/*    */   NullProvider(Class<? extends Provider> paramClass)
/*    */   {
/* 63 */     super(paramClass);
/*    */   }
/*    */ 
/*    */   protected ProbeSkeleton createProbe(Method paramMethod) {
/* 67 */     return new NullProbe(paramMethod.getParameterTypes());
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.tracing.NullProvider
 * JD-Core Version:    0.6.2
 */