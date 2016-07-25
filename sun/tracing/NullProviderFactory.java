/*    */ package sun.tracing;
/*    */ 
/*    */ import com.sun.tracing.Provider;
/*    */ import com.sun.tracing.ProviderFactory;
/*    */ 
/*    */ public class NullProviderFactory extends ProviderFactory
/*    */ {
/*    */   public <T extends Provider> T createProvider(Class<T> paramClass)
/*    */   {
/* 54 */     NullProvider localNullProvider = new NullProvider(paramClass);
/* 55 */     localNullProvider.init();
/* 56 */     return localNullProvider.newProxyInstance();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.tracing.NullProviderFactory
 * JD-Core Version:    0.6.2
 */