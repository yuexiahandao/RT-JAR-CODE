/*    */ package com.sun.org.apache.xml.internal.security.utils.resolver.implementations;
/*    */ 
/*    */ import com.sun.org.apache.xml.internal.security.signature.XMLSignatureInput;
/*    */ import com.sun.org.apache.xml.internal.security.utils.resolver.ResourceResolverSpi;
/*    */ import java.io.FileInputStream;
/*    */ import java.io.FileNotFoundException;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import org.w3c.dom.Attr;
/*    */ 
/*    */ public class ResolverAnonymous extends ResourceResolverSpi
/*    */ {
/* 40 */   private XMLSignatureInput _input = null;
/*    */ 
/*    */   public ResolverAnonymous(String paramString)
/*    */     throws FileNotFoundException, IOException
/*    */   {
/* 48 */     this._input = new XMLSignatureInput(new FileInputStream(paramString));
/*    */   }
/*    */ 
/*    */   public ResolverAnonymous(InputStream paramInputStream)
/*    */   {
/* 55 */     this._input = new XMLSignatureInput(paramInputStream);
/*    */   }
/*    */ 
/*    */   public XMLSignatureInput engineResolve(Attr paramAttr, String paramString)
/*    */   {
/* 60 */     return this._input;
/*    */   }
/*    */ 
/*    */   public boolean engineCanResolve(Attr paramAttr, String paramString)
/*    */   {
/* 67 */     if (paramAttr == null) {
/* 68 */       return true;
/*    */     }
/* 70 */     return false;
/*    */   }
/*    */ 
/*    */   public String[] engineGetPropertyKeys()
/*    */   {
/* 75 */     return new String[0];
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.utils.resolver.implementations.ResolverAnonymous
 * JD-Core Version:    0.6.2
 */