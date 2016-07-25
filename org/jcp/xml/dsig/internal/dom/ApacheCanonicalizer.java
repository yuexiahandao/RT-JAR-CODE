/*     */ package org.jcp.xml.dsig.internal.dom;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.security.Init;
/*     */ import com.sun.org.apache.xml.internal.security.c14n.Canonicalizer;
/*     */ import com.sun.org.apache.xml.internal.security.c14n.InvalidCanonicalizerException;
/*     */ import com.sun.org.apache.xml.internal.security.signature.XMLSignatureInput;
/*     */ import com.sun.org.apache.xml.internal.security.transforms.Transform;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
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
/*     */ import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ 
/*     */ public abstract class ApacheCanonicalizer extends TransformService
/*     */ {
/*  59 */   private static Logger log = Logger.getLogger("org.jcp.xml.dsig.internal.dom");
/*     */   protected Canonicalizer apacheCanonicalizer;
/*     */   private Transform apacheTransform;
/*     */   protected String inclusiveNamespaces;
/*     */   protected C14NMethodParameterSpec params;
/*     */   protected Document ownerDoc;
/*     */   protected Element transformElem;
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
/*     */   public Data canonicalize(Data paramData, XMLCryptoContext paramXMLCryptoContext) throws TransformException
/*     */   {
/*  95 */     return canonicalize(paramData, paramXMLCryptoContext, null);
/*     */   }
/*     */ 
/*     */   public Data canonicalize(Data paramData, XMLCryptoContext paramXMLCryptoContext, OutputStream paramOutputStream)
/*     */     throws TransformException
/*     */   {
/* 101 */     if (this.apacheCanonicalizer == null) {
/*     */       try {
/* 103 */         this.apacheCanonicalizer = Canonicalizer.getInstance(getAlgorithm());
/* 104 */         if (log.isLoggable(Level.FINE))
/* 105 */           log.log(Level.FINE, "Created canonicalizer for algorithm: " + getAlgorithm());
/*     */       }
/*     */       catch (InvalidCanonicalizerException localInvalidCanonicalizerException)
/*     */       {
/* 109 */         throw new TransformException("Couldn't find Canonicalizer for: " + getAlgorithm() + ": " + localInvalidCanonicalizerException.getMessage(), localInvalidCanonicalizerException);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 115 */     if (paramOutputStream != null)
/* 116 */       this.apacheCanonicalizer.setWriter(paramOutputStream);
/*     */     else {
/* 118 */       this.apacheCanonicalizer.setWriter(new ByteArrayOutputStream());
/*     */     }
/*     */     try
/*     */     {
/* 122 */       Set localSet = null;
/*     */       Object localObject;
/* 123 */       if ((paramData instanceof ApacheData)) {
/* 124 */         localObject = ((ApacheData)paramData).getXMLSignatureInput();
/*     */ 
/* 126 */         if (((XMLSignatureInput)localObject).isElement()) {
/* 127 */           if (this.inclusiveNamespaces != null) {
/* 128 */             return new OctetStreamData(new ByteArrayInputStream(this.apacheCanonicalizer.canonicalizeSubtree(((XMLSignatureInput)localObject).getSubNode(), this.inclusiveNamespaces)));
/*     */           }
/*     */ 
/* 132 */           return new OctetStreamData(new ByteArrayInputStream(this.apacheCanonicalizer.canonicalizeSubtree(((XMLSignatureInput)localObject).getSubNode())));
/*     */         }
/*     */ 
/* 136 */         if (((XMLSignatureInput)localObject).isNodeSet())
/* 137 */           localSet = ((XMLSignatureInput)localObject).getNodeSet();
/*     */         else
/* 139 */           return new OctetStreamData(new ByteArrayInputStream(this.apacheCanonicalizer.canonicalize(Utils.readBytesFromStream(((XMLSignatureInput)localObject).getOctetStream()))));
/*     */       }
/*     */       else
/*     */       {
/* 143 */         if ((paramData instanceof DOMSubTreeData)) {
/* 144 */           localObject = (DOMSubTreeData)paramData;
/* 145 */           if (this.inclusiveNamespaces != null) {
/* 146 */             return new OctetStreamData(new ByteArrayInputStream(this.apacheCanonicalizer.canonicalizeSubtree(((DOMSubTreeData)localObject).getRoot(), this.inclusiveNamespaces)));
/*     */           }
/*     */ 
/* 150 */           return new OctetStreamData(new ByteArrayInputStream(this.apacheCanonicalizer.canonicalizeSubtree(((DOMSubTreeData)localObject).getRoot())));
/*     */         }
/*     */ 
/* 154 */         if ((paramData instanceof NodeSetData)) {
/* 155 */           localObject = (NodeSetData)paramData;
/*     */ 
/* 157 */           localSet = Utils.toNodeSet(((NodeSetData)localObject).iterator());
/* 158 */           if (log.isLoggable(Level.FINE))
/* 159 */             log.log(Level.FINE, "Canonicalizing " + localSet.size() + " nodes");
/*     */         }
/*     */         else
/*     */         {
/* 163 */           return new OctetStreamData(new ByteArrayInputStream(this.apacheCanonicalizer.canonicalize(Utils.readBytesFromStream(((OctetStreamData)paramData).getOctetStream()))));
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 168 */       if (this.inclusiveNamespaces != null) {
/* 169 */         return new OctetStreamData(new ByteArrayInputStream(this.apacheCanonicalizer.canonicalizeXPathNodeSet(localSet, this.inclusiveNamespaces)));
/*     */       }
/*     */ 
/* 173 */       return new OctetStreamData(new ByteArrayInputStream(this.apacheCanonicalizer.canonicalizeXPathNodeSet(localSet)));
/*     */     }
/*     */     catch (Exception localException)
/*     */     {
/* 177 */       throw new TransformException(localException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Data transform(Data paramData, XMLCryptoContext paramXMLCryptoContext, OutputStream paramOutputStream) throws TransformException
/*     */   {
/* 183 */     if (paramData == null) {
/* 184 */       throw new NullPointerException("data must not be null");
/*     */     }
/* 186 */     if (paramOutputStream == null) {
/* 187 */       throw new NullPointerException("output stream must not be null");
/*     */     }
/*     */ 
/* 190 */     if (this.ownerDoc == null) {
/* 191 */       throw new TransformException("transform must be marshalled");
/*     */     }
/*     */ 
/* 194 */     if (this.apacheTransform == null)
/*     */       try {
/* 196 */         this.apacheTransform = new Transform(this.ownerDoc, getAlgorithm(), this.transformElem.getChildNodes());
/*     */ 
/* 198 */         this.apacheTransform.setElement(this.transformElem, paramXMLCryptoContext.getBaseURI());
/* 199 */         if (log.isLoggable(Level.FINE))
/* 200 */           log.log(Level.FINE, "Created transform for algorithm: " + getAlgorithm());
/*     */       }
/*     */       catch (Exception localException1)
/*     */       {
/* 204 */         throw new TransformException("Couldn't find Transform for: " + getAlgorithm(), localException1);
/*     */       }
/*     */     XMLSignatureInput localXMLSignatureInput;
/* 210 */     if ((paramData instanceof ApacheData)) {
/* 211 */       if (log.isLoggable(Level.FINE)) {
/* 212 */         log.log(Level.FINE, "ApacheData = true");
/*     */       }
/* 214 */       localXMLSignatureInput = ((ApacheData)paramData).getXMLSignatureInput();
/* 215 */     } else if ((paramData instanceof NodeSetData)) {
/* 216 */       if (log.isLoggable(Level.FINE))
/* 217 */         log.log(Level.FINE, "isNodeSet() = true");
/*     */       Object localObject;
/* 219 */       if ((paramData instanceof DOMSubTreeData)) {
/* 220 */         localObject = (DOMSubTreeData)paramData;
/* 221 */         localXMLSignatureInput = new XMLSignatureInput(((DOMSubTreeData)localObject).getRoot());
/* 222 */         localXMLSignatureInput.setExcludeComments(((DOMSubTreeData)localObject).excludeComments());
/*     */       } else {
/* 224 */         localObject = Utils.toNodeSet(((NodeSetData)paramData).iterator());
/*     */ 
/* 226 */         localXMLSignatureInput = new XMLSignatureInput((Set)localObject);
/*     */       }
/*     */     } else {
/* 229 */       if (log.isLoggable(Level.FINE))
/* 230 */         log.log(Level.FINE, "isNodeSet() = false");
/*     */       try
/*     */       {
/* 233 */         localXMLSignatureInput = new XMLSignatureInput(((OctetStreamData)paramData).getOctetStream());
/*     */       }
/*     */       catch (Exception localException2) {
/* 236 */         throw new TransformException(localException2);
/*     */       }
/*     */     }
/*     */     try
/*     */     {
/* 241 */       localXMLSignatureInput = this.apacheTransform.performTransform(localXMLSignatureInput, paramOutputStream);
/* 242 */       if ((!localXMLSignatureInput.isNodeSet()) && (!localXMLSignatureInput.isElement())) {
/* 243 */         return null;
/*     */       }
/* 245 */       if (localXMLSignatureInput.isOctetStream()) {
/* 246 */         return new ApacheOctetStreamData(localXMLSignatureInput);
/*     */       }
/* 248 */       return new ApacheNodeSetData(localXMLSignatureInput);
/*     */     }
/*     */     catch (Exception localException3) {
/* 251 */       throw new TransformException(localException3);
/*     */     }
/*     */   }
/*     */ 
/*     */   public final boolean isFeatureSupported(String paramString) {
/* 256 */     if (paramString == null) {
/* 257 */       throw new NullPointerException();
/*     */     }
/* 259 */     return false;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  56 */     Init.init();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.jcp.xml.dsig.internal.dom.ApacheCanonicalizer
 * JD-Core Version:    0.6.2
 */