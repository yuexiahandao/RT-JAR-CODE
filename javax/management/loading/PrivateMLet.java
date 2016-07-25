/*    */ package javax.management.loading;
/*    */ 
/*    */ import java.net.URL;
/*    */ import java.net.URLStreamHandlerFactory;
/*    */ 
/*    */ public class PrivateMLet extends MLet
/*    */   implements PrivateClassLoader
/*    */ {
/*    */   private static final long serialVersionUID = 2503458973393711979L;
/*    */ 
/*    */   public PrivateMLet(URL[] paramArrayOfURL, boolean paramBoolean)
/*    */   {
/* 57 */     super(paramArrayOfURL, paramBoolean);
/*    */   }
/*    */ 
/*    */   public PrivateMLet(URL[] paramArrayOfURL, ClassLoader paramClassLoader, boolean paramBoolean)
/*    */   {
/* 75 */     super(paramArrayOfURL, paramClassLoader, paramBoolean);
/*    */   }
/*    */ 
/*    */   public PrivateMLet(URL[] paramArrayOfURL, ClassLoader paramClassLoader, URLStreamHandlerFactory paramURLStreamHandlerFactory, boolean paramBoolean)
/*    */   {
/* 97 */     super(paramArrayOfURL, paramClassLoader, paramURLStreamHandlerFactory, paramBoolean);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.loading.PrivateMLet
 * JD-Core Version:    0.6.2
 */