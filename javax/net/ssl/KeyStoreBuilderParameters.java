/*    */ package javax.net.ssl;
/*    */ 
/*    */ import java.security.KeyStore.Builder;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ 
/*    */ public class KeyStoreBuilderParameters
/*    */   implements ManagerFactoryParameters
/*    */ {
/*    */   private final List<KeyStore.Builder> parameters;
/*    */ 
/*    */   public KeyStoreBuilderParameters(KeyStore.Builder paramBuilder)
/*    */   {
/* 54 */     this.parameters = Collections.singletonList(Objects.requireNonNull(paramBuilder));
/*    */   }
/*    */ 
/*    */   public KeyStoreBuilderParameters(List<KeyStore.Builder> paramList)
/*    */   {
/* 67 */     if (paramList.isEmpty()) {
/* 68 */       throw new IllegalArgumentException();
/*    */     }
/*    */ 
/* 71 */     this.parameters = Collections.unmodifiableList(new ArrayList(paramList));
/*    */   }
/*    */ 
/*    */   public List<KeyStore.Builder> getParameters()
/*    */   {
/* 85 */     return this.parameters;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.net.ssl.KeyStoreBuilderParameters
 * JD-Core Version:    0.6.2
 */