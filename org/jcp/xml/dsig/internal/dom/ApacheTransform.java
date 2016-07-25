/*     */ package org.jcp.xml.dsig.internal.dom;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.security.Init;
/*     */ import com.sun.org.apache.xml.internal.security.signature.XMLSignatureInput;
/*     */ import com.sun.org.apache.xml.internal.security.transforms.Transform;
/*     */ import java.io.OutputStream;
/*     */ import java.security.InvalidAlgorithmParameterException;
/*     */ import java.security.spec.AlgorithmParameterSpec;
/*     */ import java.util.Set;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.xml.crypto.Data;
/*     */ import javax.xml.crypto.MarshalException;
/*     */ import javax.xml.crypto.NodeSetData;
/*     */ import javax.xml.crypto.OctetStreamData;
/*     */ import javax.xml.crypto.XMLCryptoContext;
/*     */ import javax.xml.crypto.XMLStructure;
/*     */ import javax.xml.crypto.dom.DOMCryptoContext;
/*     */ import javax.xml.crypto.dom.DOMStructure;
/*     */ import javax.xml.crypto.dsig.TransformException;
/*     */ import javax.xml.crypto.dsig.TransformService;
/*     */ import javax.xml.crypto.dsig.spec.TransformParameterSpec;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ 
/*     */ public abstract class ApacheTransform extends TransformService
/*     */ {
/*  61 */   private static Logger log = Logger.getLogger("org.jcp.xml.dsig.internal.dom");
/*     */   private Transform apacheTransform;
/*     */   protected Document ownerDoc;
/*     */   protected Element transformElem;
/*     */   protected TransformParameterSpec params;
/*     */ 
/*     */   public final AlgorithmParameterSpec getParameterSpec()
/*     */   {
/*  68 */     return this.params;
/*     */   }
/*     */ 
/*     */   public void init(XMLStructure paramXMLStructure, XMLCryptoContext paramXMLCryptoContext) throws InvalidAlgorithmParameterException
/*     */   {
/*  73 */     if ((paramXMLCryptoContext != null) && (!(paramXMLCryptoContext instanceof DOMCryptoContext))) {
/*  74 */       throw new ClassCastException("context must be of type DOMCryptoContext");
/*     */     }
/*     */ 
/*  77 */     this.transformElem = ((Element)((DOMStructure)paramXMLStructure).getNode());
/*     */ 
/*  79 */     this.ownerDoc = DOMUtils.getOwnerDocument(this.transformElem);
/*     */   }
/*     */ 
/*     */   public void marshalParams(XMLStructure paramXMLStructure, XMLCryptoContext paramXMLCryptoContext) throws MarshalException
/*     */   {
/*  84 */     if ((paramXMLCryptoContext != null) && (!(paramXMLCryptoContext instanceof DOMCryptoContext))) {
/*  85 */       throw new ClassCastException("context must be of type DOMCryptoContext");
/*     */     }
/*     */ 
/*  88 */     this.transformElem = ((Element)((DOMStructure)paramXMLStructure).getNode());
/*     */ 
/*  90 */     this.ownerDoc = DOMUtils.getOwnerDocument(this.transformElem);
/*     */   }
/*     */ 
/*     */   public Data transform(Data paramData, XMLCryptoContext paramXMLCryptoContext) throws TransformException
/*     */   {
/*  95 */     if (paramData == null) {
/*  96 */       throw new NullPointerException("data must not be null");
/*     */     }
/*  98 */     return transformIt(paramData, paramXMLCryptoContext, (OutputStream)null);
/*     */   }
/*     */ 
/*     */   public Data transform(Data paramData, XMLCryptoContext paramXMLCryptoContext, OutputStream paramOutputStream) throws TransformException
/*     */   {
/* 103 */     if (paramData == null) {
/* 104 */       throw new NullPointerException("data must not be null");
/*     */     }
/* 106 */     if (paramOutputStream == null) {
/* 107 */       throw new NullPointerException("output stream must not be null");
/*     */     }
/* 109 */     return transformIt(paramData, paramXMLCryptoContext, paramOutputStream);
/*     */   }
/*     */ 
/*     */   private Data transformIt(Data paramData, XMLCryptoContext paramXMLCryptoContext, OutputStream paramOutputStream)
/*     */     throws TransformException
/*     */   {
/* 115 */     if (this.ownerDoc == null) {
/* 116 */       throw new TransformException("transform must be marshalled");
/*     */     }
/*     */ 
/* 119 */     if (this.apacheTransform == null)
/*     */       try {
/* 121 */         this.apacheTransform = new Transform(this.ownerDoc, getAlgorithm(), this.transformElem.getChildNodes());
/*     */ 
/* 123 */         this.apacheTransform.setElement(this.transformElem, paramXMLCryptoContext.getBaseURI());
/* 124 */         if (log.isLoggable(Level.FINE))
/* 125 */           log.log(Level.FINE, "Created transform for algorithm: " + getAlgorithm());
/*     */       }
/*     */       catch (Exception localException1)
/*     */       {
/* 129 */         throw new TransformException("Couldn't find Transform for: " + getAlgorithm(), localException1);
/*     */       }
/*     */     Object localObject1;
/* 134 */     if (Utils.secureValidation(paramXMLCryptoContext)) {
/* 135 */       localObject1 = getAlgorithm();
/* 136 */       if ("http://www.w3.org/TR/1999/REC-xslt-19991116".equals(localObject1)) {
/* 137 */         throw new TransformException("Transform " + (String)localObject1 + " is forbidden when secure validation is enabled");
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 144 */     if ((paramData instanceof ApacheData)) {
/* 145 */       if (log.isLoggable(Level.FINE)) {
/* 146 */         log.log(Level.FINE, "ApacheData = true");
/*     */       }
/* 148 */       localObject1 = ((ApacheData)paramData).getXMLSignatureInput();
/* 149 */     } else if ((paramData instanceof NodeSetData)) {
/* 150 */       if (log.isLoggable(Level.FINE))
/* 151 */         log.log(Level.FINE, "isNodeSet() = true");
/*     */       Object localObject2;
/* 153 */       if ((paramData instanceof DOMSubTreeData)) {
/* 154 */         if (log.isLoggable(Level.FINE)) {
/* 155 */           log.log(Level.FINE, "DOMSubTreeData = true");
/*     */         }
/* 157 */         localObject2 = (DOMSubTreeData)paramData;
/* 158 */         localObject1 = new XMLSignatureInput(((DOMSubTreeData)localObject2).getRoot());
/* 159 */         ((XMLSignatureInput)localObject1).setExcludeComments(((DOMSubTreeData)localObject2).excludeComments());
/*     */       } else {
/* 161 */         localObject2 = Utils.toNodeSet(((NodeSetData)paramData).iterator());
/*     */ 
/* 163 */         localObject1 = new XMLSignatureInput((Set)localObject2);
/*     */       }
/*     */     } else {
/* 166 */       if (log.isLoggable(Level.FINE))
/* 167 */         log.log(Level.FINE, "isNodeSet() = false");
/*     */       try
/*     */       {
/* 170 */         localObject1 = new XMLSignatureInput(((OctetStreamData)paramData).getOctetStream());
/*     */       }
/*     */       catch (Exception localException2) {
/* 173 */         throw new TransformException(localException2);
/*     */       }
/*     */     }
/*     */     try
/*     */     {
/* 178 */       if (paramOutputStream != null) {
/* 179 */         localObject1 = this.apacheTransform.performTransform((XMLSignatureInput)localObject1, paramOutputStream);
/* 180 */         if ((!((XMLSignatureInput)localObject1).isNodeSet()) && (!((XMLSignatureInput)localObject1).isElement()))
/* 181 */           return null;
/*     */       }
/*     */       else {
/* 184 */         localObject1 = this.apacheTransform.performTransform((XMLSignatureInput)localObject1);
/*     */       }
/* 186 */       if (((XMLSignatureInput)localObject1).isOctetStream()) {
/* 187 */         return new ApacheOctetStreamData((XMLSignatureInput)localObject1);
/*     */       }
/* 189 */       return new ApacheNodeSetData((XMLSignatureInput)localObject1);
/*     */     }
/*     */     catch (Exception localException3) {
/* 192 */       throw new TransformException(localException3);
/*     */     }
/*     */   }
/*     */ 
/*     */   public final boolean isFeatureSupported(String paramString) {
/* 197 */     if (paramString == null) {
/* 198 */       throw new NullPointerException();
/*     */     }
/* 200 */     return false;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  58 */     Init.init();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.jcp.xml.dsig.internal.dom.ApacheTransform
 * JD-Core Version:    0.6.2
 */