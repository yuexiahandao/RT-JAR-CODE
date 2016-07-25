/*     */ package javax.xml.crypto;
/*     */ 
/*     */ import java.security.Key;
/*     */ import javax.xml.crypto.dsig.keyinfo.KeyInfo;
/*     */ 
/*     */ public abstract class KeySelector
/*     */ {
/*     */   public abstract KeySelectorResult select(KeyInfo paramKeyInfo, Purpose paramPurpose, AlgorithmMethod paramAlgorithmMethod, XMLCryptoContext paramXMLCryptoContext)
/*     */     throws KeySelectorException;
/*     */ 
/*     */   public static KeySelector singletonKeySelector(Key paramKey)
/*     */   {
/* 128 */     return new SingletonKeySelector(paramKey);
/*     */   }
/*     */ 
/*     */   public static class Purpose
/*     */   {
/*     */     private final String name;
/*  69 */     public static final Purpose SIGN = new Purpose("sign");
/*     */ 
/*  73 */     public static final Purpose VERIFY = new Purpose("verify");
/*     */ 
/*  77 */     public static final Purpose ENCRYPT = new Purpose("encrypt");
/*     */ 
/*  81 */     public static final Purpose DECRYPT = new Purpose("decrypt");
/*     */ 
/*     */     private Purpose(String paramString)
/*     */     {
/*  56 */       this.name = paramString;
/*     */     }
/*     */ 
/*     */     public String toString()
/*     */     {
/*  64 */       return this.name;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class SingletonKeySelector extends KeySelector
/*     */   {
/*     */     private final Key key;
/*     */ 
/*     */     SingletonKeySelector(Key paramKey)
/*     */     {
/* 135 */       if (paramKey == null) {
/* 136 */         throw new NullPointerException();
/*     */       }
/* 138 */       this.key = paramKey;
/*     */     }
/*     */ 
/*     */     public KeySelectorResult select(KeyInfo paramKeyInfo, KeySelector.Purpose paramPurpose, AlgorithmMethod paramAlgorithmMethod, XMLCryptoContext paramXMLCryptoContext)
/*     */       throws KeySelectorException
/*     */     {
/* 145 */       return new KeySelectorResult() {
/*     */         public Key getKey() {
/* 147 */           return KeySelector.SingletonKeySelector.this.key;
/*     */         }
/*     */       };
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.crypto.KeySelector
 * JD-Core Version:    0.6.2
 */