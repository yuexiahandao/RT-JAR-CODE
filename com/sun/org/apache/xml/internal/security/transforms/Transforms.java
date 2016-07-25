/*     */ package com.sun.org.apache.xml.internal.security.transforms;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.security.c14n.CanonicalizationException;
/*     */ import com.sun.org.apache.xml.internal.security.c14n.InvalidCanonicalizerException;
/*     */ import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;
/*     */ import com.sun.org.apache.xml.internal.security.signature.XMLSignatureException;
/*     */ import com.sun.org.apache.xml.internal.security.signature.XMLSignatureInput;
/*     */ import com.sun.org.apache.xml.internal.security.utils.SignatureElementProxy;
/*     */ import com.sun.org.apache.xml.internal.security.utils.XMLUtils;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import org.w3c.dom.DOMException;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.NodeList;
/*     */ 
/*     */ public class Transforms extends SignatureElementProxy
/*     */ {
/*  55 */   static Logger log = Logger.getLogger(Transforms.class.getName());
/*     */   public static final String TRANSFORM_C14N_OMIT_COMMENTS = "http://www.w3.org/TR/2001/REC-xml-c14n-20010315";
/*     */   public static final String TRANSFORM_C14N_WITH_COMMENTS = "http://www.w3.org/TR/2001/REC-xml-c14n-20010315#WithComments";
/*     */   public static final String TRANSFORM_C14N11_OMIT_COMMENTS = "http://www.w3.org/2006/12/xml-c14n11";
/*     */   public static final String TRANSFORM_C14N11_WITH_COMMENTS = "http://www.w3.org/2006/12/xml-c14n11#WithComments";
/*     */   public static final String TRANSFORM_C14N_EXCL_OMIT_COMMENTS = "http://www.w3.org/2001/10/xml-exc-c14n#";
/*     */   public static final String TRANSFORM_C14N_EXCL_WITH_COMMENTS = "http://www.w3.org/2001/10/xml-exc-c14n#WithComments";
/*     */   public static final String TRANSFORM_XSLT = "http://www.w3.org/TR/1999/REC-xslt-19991116";
/*     */   public static final String TRANSFORM_BASE64_DECODE = "http://www.w3.org/2000/09/xmldsig#base64";
/*     */   public static final String TRANSFORM_XPATH = "http://www.w3.org/TR/1999/REC-xpath-19991116";
/*     */   public static final String TRANSFORM_ENVELOPED_SIGNATURE = "http://www.w3.org/2000/09/xmldsig#enveloped-signature";
/*     */   public static final String TRANSFORM_XPOINTER = "http://www.w3.org/TR/2001/WD-xptr-20010108";
/*     */   public static final String TRANSFORM_XPATH2FILTER04 = "http://www.w3.org/2002/04/xmldsig-filter2";
/*     */   public static final String TRANSFORM_XPATH2FILTER = "http://www.w3.org/2002/06/xmldsig-filter2";
/*     */   public static final String TRANSFORM_XPATHFILTERCHGP = "http://www.nue.et-inf.uni-siegen.de/~geuer-pollmann/#xpathFilter";
/*     */   Element[] transforms;
/*     */ 
/*     */   protected Transforms()
/*     */   {
/*     */   }
/*     */ 
/*     */   public Transforms(Document paramDocument)
/*     */   {
/* 111 */     super(paramDocument);
/* 112 */     XMLUtils.addReturnToElement(this._constructionElement);
/*     */   }
/*     */ 
/*     */   public Transforms(Element paramElement, String paramString)
/*     */     throws DOMException, XMLSignatureException, InvalidTransformException, TransformationException, XMLSecurityException
/*     */   {
/* 132 */     super(paramElement, paramString);
/*     */ 
/* 134 */     int i = getLength();
/*     */ 
/* 136 */     if (i == 0)
/*     */     {
/* 139 */       Object[] arrayOfObject = { "Transform", "Transforms" };
/*     */ 
/* 142 */       throw new TransformationException("xml.WrongContent", arrayOfObject);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void addTransform(String paramString)
/*     */     throws TransformationException
/*     */   {
/*     */     try
/*     */     {
/* 158 */       if (log.isLoggable(Level.FINE)) {
/* 159 */         log.log(Level.FINE, "Transforms.addTransform(" + paramString + ")");
/*     */       }
/* 161 */       Transform localTransform = new Transform(this._doc, paramString);
/*     */ 
/* 163 */       addTransform(localTransform);
/*     */     } catch (InvalidTransformException localInvalidTransformException) {
/* 165 */       throw new TransformationException("empty", localInvalidTransformException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void addTransform(String paramString, Element paramElement)
/*     */     throws TransformationException
/*     */   {
/*     */     try
/*     */     {
/* 183 */       if (log.isLoggable(Level.FINE)) {
/* 184 */         log.log(Level.FINE, "Transforms.addTransform(" + paramString + ")");
/*     */       }
/* 186 */       Transform localTransform = new Transform(this._doc, paramString, paramElement);
/*     */ 
/* 188 */       addTransform(localTransform);
/*     */     } catch (InvalidTransformException localInvalidTransformException) {
/* 190 */       throw new TransformationException("empty", localInvalidTransformException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void addTransform(String paramString, NodeList paramNodeList)
/*     */     throws TransformationException
/*     */   {
/*     */     try
/*     */     {
/* 208 */       Transform localTransform = new Transform(this._doc, paramString, paramNodeList);
/* 209 */       addTransform(localTransform);
/*     */     } catch (InvalidTransformException localInvalidTransformException) {
/* 211 */       throw new TransformationException("empty", localInvalidTransformException);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void addTransform(Transform paramTransform)
/*     */   {
/* 221 */     if (log.isLoggable(Level.FINE)) {
/* 222 */       log.log(Level.FINE, "Transforms.addTransform(" + paramTransform.getURI() + ")");
/*     */     }
/* 224 */     Element localElement = paramTransform.getElement();
/*     */ 
/* 226 */     this._constructionElement.appendChild(localElement);
/* 227 */     XMLUtils.addReturnToElement(this._constructionElement);
/*     */   }
/*     */ 
/*     */   public XMLSignatureInput performTransforms(XMLSignatureInput paramXMLSignatureInput)
/*     */     throws TransformationException
/*     */   {
/* 240 */     return performTransforms(paramXMLSignatureInput, null);
/*     */   }
/*     */ 
/*     */   public XMLSignatureInput performTransforms(XMLSignatureInput paramXMLSignatureInput, OutputStream paramOutputStream)
/*     */     throws TransformationException
/*     */   {
/*     */     try
/*     */     {
/* 257 */       int i = getLength() - 1;
/* 258 */       for (int j = 0; j < i; j++) {
/* 259 */         Transform localTransform2 = item(j);
/* 260 */         if (log.isLoggable(Level.FINE)) {
/* 261 */           log.log(Level.FINE, "Perform the (" + j + ")th " + localTransform2.getURI() + " transform");
/*     */         }
/*     */ 
/* 264 */         paramXMLSignatureInput = localTransform2.performTransform(paramXMLSignatureInput);
/*     */       }
/*     */       Transform localTransform1;
/* 266 */       if (i >= 0)
/* 267 */         localTransform1 = item(i);
/* 268 */       return localTransform1.performTransform(paramXMLSignatureInput, paramOutputStream);
/*     */     }
/*     */     catch (IOException localIOException)
/*     */     {
/* 273 */       throw new TransformationException("empty", localIOException);
/*     */     } catch (CanonicalizationException localCanonicalizationException) {
/* 275 */       throw new TransformationException("empty", localCanonicalizationException);
/*     */     } catch (InvalidCanonicalizerException localInvalidCanonicalizerException) {
/* 277 */       throw new TransformationException("empty", localInvalidCanonicalizerException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getLength()
/*     */   {
/* 288 */     if (this.transforms == null) {
/* 289 */       this.transforms = XMLUtils.selectDsNodes(this._constructionElement.getFirstChild(), "Transform");
/*     */     }
/*     */ 
/* 292 */     return this.transforms.length;
/*     */   }
/*     */ 
/*     */   public Transform item(int paramInt)
/*     */     throws TransformationException
/*     */   {
/*     */     try
/*     */     {
/* 306 */       if (this.transforms == null) {
/* 307 */         this.transforms = XMLUtils.selectDsNodes(this._constructionElement.getFirstChild(), "Transform");
/*     */       }
/*     */ 
/* 310 */       return new Transform(this.transforms[paramInt], this._baseURI);
/*     */     } catch (XMLSecurityException localXMLSecurityException) {
/* 312 */       throw new TransformationException("empty", localXMLSecurityException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public String getBaseLocalName()
/*     */   {
/* 318 */     return "Transforms";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.transforms.Transforms
 * JD-Core Version:    0.6.2
 */