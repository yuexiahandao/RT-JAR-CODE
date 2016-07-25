/*    */ package com.sun.org.apache.xml.internal.security.keys.storage.implementations;
/*    */ 
/*    */ import com.sun.org.apache.xml.internal.security.keys.storage.StorageResolverSpi;
/*    */ import java.security.cert.X509Certificate;
/*    */ import java.util.Iterator;
/*    */ 
/*    */ public class SingleCertificateResolver extends StorageResolverSpi
/*    */ {
/* 38 */   X509Certificate _certificate = null;
/*    */ 
/* 41 */   Iterator _iterator = null;
/*    */ 
/*    */   public SingleCertificateResolver(X509Certificate paramX509Certificate)
/*    */   {
/* 49 */     this._certificate = paramX509Certificate;
/* 50 */     this._iterator = new InternalIterator(this._certificate);
/*    */   }
/*    */ 
/*    */   public Iterator getIterator()
/*    */   {
/* 55 */     return this._iterator;
/*    */   }
/*    */ 
/*    */   static class InternalIterator
/*    */     implements Iterator
/*    */   {
/* 67 */     boolean _alreadyReturned = false;
/*    */ 
/* 70 */     X509Certificate _certificate = null;
/*    */ 
/*    */     public InternalIterator(X509Certificate paramX509Certificate)
/*    */     {
/* 78 */       this._certificate = paramX509Certificate;
/*    */     }
/*    */ 
/*    */     public boolean hasNext()
/*    */     {
/* 83 */       return !this._alreadyReturned;
/*    */     }
/*    */ 
/*    */     public Object next()
/*    */     {
/* 89 */       this._alreadyReturned = true;
/*    */ 
/* 91 */       return this._certificate;
/*    */     }
/*    */ 
/*    */     public void remove()
/*    */     {
/* 99 */       throw new UnsupportedOperationException("Can't remove keys from KeyStore");
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.keys.storage.implementations.SingleCertificateResolver
 * JD-Core Version:    0.6.2
 */