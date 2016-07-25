/*    */ package sun.tracing;
/*    */ 
/*    */ import com.sun.tracing.Provider;
/*    */ import com.sun.tracing.ProviderFactory;
/*    */ import java.io.PrintStream;
/*    */ 
/*    */ public class PrintStreamProviderFactory extends ProviderFactory
/*    */ {
/*    */   private PrintStream stream;
/*    */ 
/*    */   public PrintStreamProviderFactory(PrintStream paramPrintStream)
/*    */   {
/* 51 */     this.stream = paramPrintStream;
/*    */   }
/*    */ 
/*    */   public <T extends Provider> T createProvider(Class<T> paramClass) {
/* 55 */     PrintStreamProvider localPrintStreamProvider = new PrintStreamProvider(paramClass, this.stream);
/* 56 */     localPrintStreamProvider.init();
/* 57 */     return localPrintStreamProvider.newProxyInstance();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.tracing.PrintStreamProviderFactory
 * JD-Core Version:    0.6.2
 */