/*    */ package com.sun.org.apache.xml.internal.security.keys.keyresolver.implementations;
/*    */ 
/*    */ import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;
/*    */ import com.sun.org.apache.xml.internal.security.keys.content.keyvalues.DSAKeyValue;
/*    */ import com.sun.org.apache.xml.internal.security.keys.keyresolver.KeyResolverSpi;
/*    */ import com.sun.org.apache.xml.internal.security.keys.storage.StorageResolver;
/*    */ import com.sun.org.apache.xml.internal.security.utils.XMLUtils;
/*    */ import java.security.PublicKey;
/*    */ import java.security.cert.X509Certificate;
/*    */ import javax.crypto.SecretKey;
/*    */ import org.w3c.dom.Element;
/*    */ 
/*    */ public class DSAKeyValueResolver extends KeyResolverSpi
/*    */ {
/*    */   public PublicKey engineLookupAndResolvePublicKey(Element paramElement, String paramString, StorageResolver paramStorageResolver)
/*    */   {
/* 54 */     if (paramElement == null) {
/* 55 */       return null;
/*    */     }
/* 57 */     Element localElement = null;
/* 58 */     boolean bool = XMLUtils.elementIsInSignatureSpace(paramElement, "KeyValue");
/*    */ 
/* 60 */     if (bool) {
/* 61 */       localElement = XMLUtils.selectDsNode(paramElement.getFirstChild(), "DSAKeyValue", 0);
/*    */     }
/* 63 */     else if (XMLUtils.elementIsInSignatureSpace(paramElement, "DSAKeyValue"))
/*    */     {
/* 67 */       localElement = paramElement;
/*    */     }
/*    */ 
/* 70 */     if (localElement == null) {
/* 71 */       return null;
/*    */     }
/*    */     try
/*    */     {
/* 75 */       DSAKeyValue localDSAKeyValue = new DSAKeyValue(localElement, paramString);
/*    */ 
/* 77 */       return localDSAKeyValue.getPublicKey();
/*    */     }
/*    */     catch (XMLSecurityException localXMLSecurityException)
/*    */     {
/*    */     }
/*    */ 
/* 84 */     return null;
/*    */   }
/*    */ 
/*    */   public X509Certificate engineLookupResolveX509Certificate(Element paramElement, String paramString, StorageResolver paramStorageResolver)
/*    */   {
/* 91 */     return null;
/*    */   }
/*    */ 
/*    */   public SecretKey engineLookupAndResolveSecretKey(Element paramElement, String paramString, StorageResolver paramStorageResolver)
/*    */   {
/* 97 */     return null;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.keys.keyresolver.implementations.DSAKeyValueResolver
 * JD-Core Version:    0.6.2
 */