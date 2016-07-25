/*     */ package org.jcp.xml.dsig.internal.dom;
/*     */ 
/*     */ import java.io.OutputStream;
/*     */ import java.security.InvalidAlgorithmParameterException;
/*     */ import java.security.Provider;
/*     */ import javax.xml.crypto.Data;
/*     */ import javax.xml.crypto.MarshalException;
/*     */ import javax.xml.crypto.XMLCryptoContext;
/*     */ import javax.xml.crypto.dsig.CanonicalizationMethod;
/*     */ import javax.xml.crypto.dsig.TransformException;
/*     */ import javax.xml.crypto.dsig.TransformService;
/*     */ import org.w3c.dom.Element;
/*     */ 
/*     */ public class DOMCanonicalizationMethod extends DOMTransform
/*     */   implements CanonicalizationMethod
/*     */ {
/*     */   public DOMCanonicalizationMethod(TransformService paramTransformService)
/*     */     throws InvalidAlgorithmParameterException
/*     */   {
/*  53 */     super(paramTransformService);
/*  54 */     if ((!(paramTransformService instanceof ApacheCanonicalizer)) && (!isC14Nalg(paramTransformService.getAlgorithm())))
/*     */     {
/*  56 */       throw new InvalidAlgorithmParameterException("Illegal CanonicalizationMethod");
/*     */     }
/*     */   }
/*     */ 
/*     */   public DOMCanonicalizationMethod(Element paramElement, XMLCryptoContext paramXMLCryptoContext, Provider paramProvider)
/*     */     throws MarshalException
/*     */   {
/*  70 */     super(paramElement, paramXMLCryptoContext, paramProvider);
/*  71 */     if ((!(this.spi instanceof ApacheCanonicalizer)) && (!isC14Nalg(this.spi.getAlgorithm())))
/*     */     {
/*  73 */       throw new MarshalException("Illegal CanonicalizationMethod");
/*     */     }
/*     */   }
/*     */ 
/*     */   public Data canonicalize(Data paramData, XMLCryptoContext paramXMLCryptoContext)
/*     */     throws TransformException
/*     */   {
/*  92 */     return transform(paramData, paramXMLCryptoContext);
/*     */   }
/*     */ 
/*     */   public Data canonicalize(Data paramData, XMLCryptoContext paramXMLCryptoContext, OutputStream paramOutputStream) throws TransformException
/*     */   {
/*  97 */     return transform(paramData, paramXMLCryptoContext, paramOutputStream);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject) {
/* 101 */     if (this == paramObject) {
/* 102 */       return true;
/*     */     }
/*     */ 
/* 105 */     if (!(paramObject instanceof CanonicalizationMethod)) {
/* 106 */       return false;
/*     */     }
/* 108 */     CanonicalizationMethod localCanonicalizationMethod = (CanonicalizationMethod)paramObject;
/*     */ 
/* 110 */     return (getAlgorithm().equals(localCanonicalizationMethod.getAlgorithm())) && (DOMUtils.paramsEqual(getParameterSpec(), localCanonicalizationMethod.getParameterSpec()));
/*     */   }
/*     */ 
/*     */   private static boolean isC14Nalg(String paramString)
/*     */   {
/* 115 */     return (paramString.equals("http://www.w3.org/TR/2001/REC-xml-c14n-20010315")) || (paramString.equals("http://www.w3.org/TR/2001/REC-xml-c14n-20010315#WithComments")) || (paramString.equals("http://www.w3.org/2001/10/xml-exc-c14n#")) || (paramString.equals("http://www.w3.org/2001/10/xml-exc-c14n#WithComments")) || (paramString.equals("http://www.w3.org/2006/12/xml-c14n11")) || (paramString.equals("http://www.w3.org/2006/12/xml-c14n11#WithComments"));
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.jcp.xml.dsig.internal.dom.DOMCanonicalizationMethod
 * JD-Core Version:    0.6.2
 */