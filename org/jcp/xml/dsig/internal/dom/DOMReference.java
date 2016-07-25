/*     */ package org.jcp.xml.dsig.internal.dom;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
/*     */ import com.sun.org.apache.xml.internal.security.signature.XMLSignatureInput;
/*     */ import com.sun.org.apache.xml.internal.security.utils.Base64;
/*     */ import com.sun.org.apache.xml.internal.security.utils.UnsyncBufferedOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.security.AccessController;
/*     */ import java.security.MessageDigest;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.security.Provider;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.xml.crypto.Data;
/*     */ import javax.xml.crypto.MarshalException;
/*     */ import javax.xml.crypto.NodeSetData;
/*     */ import javax.xml.crypto.OctetStreamData;
/*     */ import javax.xml.crypto.URIDereferencer;
/*     */ import javax.xml.crypto.URIReferenceException;
/*     */ import javax.xml.crypto.XMLCryptoContext;
/*     */ import javax.xml.crypto.dom.DOMCryptoContext;
/*     */ import javax.xml.crypto.dom.DOMURIReference;
/*     */ import javax.xml.crypto.dsig.DigestMethod;
/*     */ import javax.xml.crypto.dsig.Reference;
/*     */ import javax.xml.crypto.dsig.Transform;
/*     */ import javax.xml.crypto.dsig.TransformException;
/*     */ import javax.xml.crypto.dsig.TransformService;
/*     */ import javax.xml.crypto.dsig.XMLSignContext;
/*     */ import javax.xml.crypto.dsig.XMLSignatureException;
/*     */ import javax.xml.crypto.dsig.XMLValidateContext;
/*     */ import org.jcp.xml.dsig.internal.DigesterOutputStream;
/*     */ import org.w3c.dom.Attr;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ 
/*     */ public final class DOMReference extends DOMStructure
/*     */   implements Reference, DOMURIReference
/*     */ {
/*     */   public static final int MAXIMUM_TRANSFORM_COUNT = 5;
/*  82 */   private static boolean useC14N11 = ((Boolean)AccessController.doPrivileged(new PrivilegedAction()
/*     */   {
/*     */     public Boolean run() {
/*  85 */       return Boolean.valueOf(Boolean.getBoolean("com.sun.org.apache.xml.internal.security.useC14N11"));
/*     */     } } )).booleanValue();
/*     */ 
/*  90 */   private static Logger log = Logger.getLogger("org.jcp.xml.dsig.internal.dom");
/*     */   private final DigestMethod digestMethod;
/*     */   private final String id;
/*     */   private final List transforms;
/*     */   private List allTransforms;
/*     */   private final Data appliedTransformData;
/*     */   private Attr here;
/*     */   private final String uri;
/*     */   private final String type;
/*     */   private byte[] digestValue;
/*     */   private byte[] calcDigestValue;
/*     */   private Element refElem;
/* 103 */   private boolean digested = false;
/* 104 */   private boolean validated = false;
/*     */   private boolean validationStatus;
/*     */   private Data derefData;
/*     */   private InputStream dis;
/*     */   private MessageDigest md;
/*     */   private Provider provider;
/*     */ 
/*     */   public DOMReference(String paramString1, String paramString2, DigestMethod paramDigestMethod, List paramList, String paramString3, Provider paramProvider) {
/* 128 */     this(paramString1, paramString2, paramDigestMethod, null, null, paramList, paramString3, null, paramProvider);
/*     */   }
/*     */ 
/*     */   public DOMReference(String paramString1, String paramString2, DigestMethod paramDigestMethod, List paramList1, Data paramData, List paramList2, String paramString3, Provider paramProvider)
/*     */   {
/* 134 */     this(paramString1, paramString2, paramDigestMethod, paramList1, paramData, paramList2, paramString3, null, paramProvider);
/*     */   }
/*     */ 
/*     */   public DOMReference(String paramString1, String paramString2, DigestMethod paramDigestMethod, List paramList1, Data paramData, List paramList2, String paramString3, byte[] paramArrayOfByte, Provider paramProvider)
/*     */   {
/* 141 */     if (paramDigestMethod == null) {
/* 142 */       throw new NullPointerException("DigestMethod must be non-null");
/*     */     }
/* 144 */     this.allTransforms = new ArrayList();
/*     */     ArrayList localArrayList;
/*     */     int i;
/*     */     int j;
/* 145 */     if (paramList1 != null) {
/* 146 */       localArrayList = new ArrayList(paramList1);
/* 147 */       i = 0; for (j = localArrayList.size(); i < j; i++) {
/* 148 */         if (!(localArrayList.get(i) instanceof Transform)) {
/* 149 */           throw new ClassCastException("appliedTransforms[" + i + "] is not a valid type");
/*     */         }
/*     */       }
/*     */ 
/* 153 */       this.allTransforms = localArrayList;
/*     */     }
/* 155 */     if (paramList2 == null) {
/* 156 */       this.transforms = Collections.EMPTY_LIST;
/*     */     } else {
/* 158 */       localArrayList = new ArrayList(paramList2);
/* 159 */       i = 0; for (j = localArrayList.size(); i < j; i++) {
/* 160 */         if (!(localArrayList.get(i) instanceof Transform)) {
/* 161 */           throw new ClassCastException("transforms[" + i + "] is not a valid type");
/*     */         }
/*     */       }
/*     */ 
/* 165 */       this.transforms = localArrayList;
/* 166 */       this.allTransforms.addAll(localArrayList);
/*     */     }
/* 168 */     this.digestMethod = paramDigestMethod;
/* 169 */     this.uri = paramString1;
/* 170 */     if ((paramString1 != null) && (!paramString1.equals(""))) {
/*     */       try {
/* 172 */         new URI(paramString1);
/*     */       } catch (URISyntaxException localURISyntaxException) {
/* 174 */         throw new IllegalArgumentException(localURISyntaxException.getMessage());
/*     */       }
/*     */     }
/* 177 */     this.type = paramString2;
/* 178 */     this.id = paramString3;
/* 179 */     if (paramArrayOfByte != null) {
/* 180 */       this.digestValue = ((byte[])paramArrayOfByte.clone());
/* 181 */       this.digested = true;
/*     */     }
/* 183 */     this.appliedTransformData = paramData;
/* 184 */     this.provider = paramProvider;
/*     */   }
/*     */ 
/*     */   public DOMReference(Element paramElement, XMLCryptoContext paramXMLCryptoContext, Provider paramProvider)
/*     */     throws MarshalException
/*     */   {
/* 194 */     boolean bool = Utils.secureValidation(paramXMLCryptoContext);
/*     */ 
/* 197 */     Element localElement1 = DOMUtils.getFirstChildElement(paramElement);
/* 198 */     ArrayList localArrayList = new ArrayList(5);
/*     */     Object localObject;
/* 199 */     if (localElement1.getLocalName().equals("Transforms")) {
/* 200 */       localElement2 = DOMUtils.getFirstChildElement(localElement1);
/*     */ 
/* 202 */       int i = 0;
/* 203 */       while (localElement2 != null) {
/* 204 */         localArrayList.add(new DOMTransform(localElement2, paramXMLCryptoContext, paramProvider));
/*     */ 
/* 206 */         localElement2 = DOMUtils.getNextSiblingElement(localElement2);
/*     */ 
/* 208 */         i++;
/* 209 */         if ((bool) && (i > 5)) {
/* 210 */           localObject = "A maxiumum of 5 transforms per Reference are allowed with secure validation";
/*     */ 
/* 213 */           throw new MarshalException((String)localObject);
/*     */         }
/*     */       }
/* 216 */       localElement1 = DOMUtils.getNextSiblingElement(localElement1);
/*     */     }
/*     */ 
/* 220 */     Element localElement2 = localElement1;
/* 221 */     this.digestMethod = DOMDigestMethod.unmarshal(localElement2);
/* 222 */     String str = this.digestMethod.getAlgorithm();
/* 223 */     if ((bool) && ("http://www.w3.org/2001/04/xmldsig-more#md5".equals(str)))
/*     */     {
/* 226 */       throw new MarshalException("It is forbidden to use algorithm " + this.digestMethod + " when secure validation is enabled");
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 233 */       localObject = DOMUtils.getNextSiblingElement(localElement2);
/* 234 */       this.digestValue = Base64.decode((Element)localObject);
/*     */     } catch (Base64DecodingException localBase64DecodingException) {
/* 236 */       throw new MarshalException(localBase64DecodingException);
/*     */     }
/*     */ 
/* 240 */     this.uri = DOMUtils.getAttributeValue(paramElement, "URI");
/*     */ 
/* 242 */     Attr localAttr = paramElement.getAttributeNodeNS(null, "Id");
/* 243 */     if (localAttr != null) {
/* 244 */       this.id = localAttr.getValue();
/* 245 */       paramElement.setIdAttributeNode(localAttr, true);
/*     */     } else {
/* 247 */       this.id = null;
/*     */     }
/*     */ 
/* 250 */     this.type = DOMUtils.getAttributeValue(paramElement, "Type");
/* 251 */     this.here = paramElement.getAttributeNodeNS(null, "URI");
/* 252 */     this.refElem = paramElement;
/* 253 */     this.transforms = localArrayList;
/* 254 */     this.allTransforms = localArrayList;
/* 255 */     this.appliedTransformData = null;
/* 256 */     this.provider = paramProvider;
/*     */   }
/*     */ 
/*     */   public DigestMethod getDigestMethod() {
/* 260 */     return this.digestMethod;
/*     */   }
/*     */ 
/*     */   public String getId() {
/* 264 */     return this.id;
/*     */   }
/*     */ 
/*     */   public String getURI() {
/* 268 */     return this.uri;
/*     */   }
/*     */ 
/*     */   public String getType() {
/* 272 */     return this.type;
/*     */   }
/*     */ 
/*     */   public List getTransforms() {
/* 276 */     return Collections.unmodifiableList(this.allTransforms);
/*     */   }
/*     */ 
/*     */   public byte[] getDigestValue() {
/* 280 */     return this.digestValue == null ? null : (byte[])this.digestValue.clone();
/*     */   }
/*     */ 
/*     */   public byte[] getCalculatedDigestValue() {
/* 284 */     return this.calcDigestValue == null ? null : (byte[])this.calcDigestValue.clone();
/*     */   }
/*     */ 
/*     */   public void marshal(Node paramNode, String paramString, DOMCryptoContext paramDOMCryptoContext)
/*     */     throws MarshalException
/*     */   {
/* 290 */     if (log.isLoggable(Level.FINE)) {
/* 291 */       log.log(Level.FINE, "Marshalling Reference");
/*     */     }
/* 293 */     Document localDocument = DOMUtils.getOwnerDocument(paramNode);
/*     */ 
/* 295 */     this.refElem = DOMUtils.createElement(localDocument, "Reference", "http://www.w3.org/2000/09/xmldsig#", paramString);
/*     */ 
/* 299 */     DOMUtils.setAttributeID(this.refElem, "Id", this.id);
/* 300 */     DOMUtils.setAttribute(this.refElem, "URI", this.uri);
/* 301 */     DOMUtils.setAttribute(this.refElem, "Type", this.type);
/*     */ 
/* 304 */     if (!this.allTransforms.isEmpty()) {
/* 305 */       localElement = DOMUtils.createElement(localDocument, "Transforms", "http://www.w3.org/2000/09/xmldsig#", paramString);
/*     */ 
/* 307 */       this.refElem.appendChild(localElement);
/* 308 */       int i = 0; for (int j = this.allTransforms.size(); i < j; i++) {
/* 309 */         DOMStructure localDOMStructure = (DOMStructure)this.allTransforms.get(i);
/*     */ 
/* 311 */         localDOMStructure.marshal(localElement, paramString, paramDOMCryptoContext);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 316 */     ((DOMDigestMethod)this.digestMethod).marshal(this.refElem, paramString, paramDOMCryptoContext);
/*     */ 
/* 319 */     if (log.isLoggable(Level.FINE)) {
/* 320 */       log.log(Level.FINE, "Adding digestValueElem");
/*     */     }
/* 322 */     Element localElement = DOMUtils.createElement(localDocument, "DigestValue", "http://www.w3.org/2000/09/xmldsig#", paramString);
/*     */ 
/* 324 */     if (this.digestValue != null) {
/* 325 */       localElement.appendChild(localDocument.createTextNode(Base64.encode(this.digestValue)));
/*     */     }
/*     */ 
/* 328 */     this.refElem.appendChild(localElement);
/*     */ 
/* 330 */     paramNode.appendChild(this.refElem);
/* 331 */     this.here = this.refElem.getAttributeNodeNS(null, "URI");
/*     */   }
/*     */ 
/*     */   public void digest(XMLSignContext paramXMLSignContext) throws XMLSignatureException
/*     */   {
/* 336 */     Data localData = null;
/* 337 */     if (this.appliedTransformData == null)
/* 338 */       localData = dereference(paramXMLSignContext);
/*     */     else {
/* 340 */       localData = this.appliedTransformData;
/*     */     }
/* 342 */     this.digestValue = transform(localData, paramXMLSignContext);
/*     */ 
/* 345 */     String str = Base64.encode(this.digestValue);
/* 346 */     if (log.isLoggable(Level.FINE)) {
/* 347 */       log.log(Level.FINE, "Reference object uri = " + this.uri);
/*     */     }
/* 349 */     Element localElement = DOMUtils.getLastChildElement(this.refElem);
/* 350 */     if (localElement == null) {
/* 351 */       throw new XMLSignatureException("DigestValue element expected");
/*     */     }
/* 353 */     DOMUtils.removeAllChildren(localElement);
/* 354 */     localElement.appendChild(this.refElem.getOwnerDocument().createTextNode(str));
/*     */ 
/* 357 */     this.digested = true;
/* 358 */     if (log.isLoggable(Level.FINE))
/* 359 */       log.log(Level.FINE, "Reference digesting completed");
/*     */   }
/*     */ 
/*     */   public boolean validate(XMLValidateContext paramXMLValidateContext)
/*     */     throws XMLSignatureException
/*     */   {
/* 365 */     if (paramXMLValidateContext == null) {
/* 366 */       throw new NullPointerException("validateContext cannot be null");
/*     */     }
/* 368 */     if (this.validated) {
/* 369 */       return this.validationStatus;
/*     */     }
/* 371 */     Data localData = dereference(paramXMLValidateContext);
/* 372 */     this.calcDigestValue = transform(localData, paramXMLValidateContext);
/*     */ 
/* 374 */     if (log.isLoggable(Level.FINE)) {
/* 375 */       log.log(Level.FINE, "Expected digest: " + Base64.encode(this.digestValue));
/*     */ 
/* 377 */       log.log(Level.FINE, "Actual digest: " + Base64.encode(this.calcDigestValue));
/*     */     }
/*     */ 
/* 381 */     this.validationStatus = Arrays.equals(this.digestValue, this.calcDigestValue);
/* 382 */     this.validated = true;
/* 383 */     return this.validationStatus;
/*     */   }
/*     */ 
/*     */   public Data getDereferencedData() {
/* 387 */     return this.derefData;
/*     */   }
/*     */ 
/*     */   public InputStream getDigestInputStream() {
/* 391 */     return this.dis;
/*     */   }
/*     */ 
/*     */   private Data dereference(XMLCryptoContext paramXMLCryptoContext) throws XMLSignatureException
/*     */   {
/* 396 */     Data localData = null;
/*     */ 
/* 399 */     URIDereferencer localURIDereferencer = paramXMLCryptoContext.getURIDereferencer();
/* 400 */     if (localURIDereferencer == null)
/* 401 */       localURIDereferencer = DOMURIDereferencer.INSTANCE;
/*     */     try
/*     */     {
/* 404 */       localData = localURIDereferencer.dereference(this, paramXMLCryptoContext);
/* 405 */       if (log.isLoggable(Level.FINE)) {
/* 406 */         log.log(Level.FINE, "URIDereferencer class name: " + localURIDereferencer.getClass().getName());
/*     */ 
/* 408 */         log.log(Level.FINE, "Data class name: " + localData.getClass().getName());
/*     */       }
/*     */     }
/*     */     catch (URIReferenceException localURIReferenceException) {
/* 412 */       throw new XMLSignatureException(localURIReferenceException);
/*     */     }
/*     */ 
/* 415 */     return localData;
/*     */   }
/*     */ 
/*     */   private byte[] transform(Data paramData, XMLCryptoContext paramXMLCryptoContext)
/*     */     throws XMLSignatureException
/*     */   {
/* 421 */     if (this.md == null) {
/*     */       try {
/* 423 */         this.md = MessageDigest.getInstance(((DOMDigestMethod)this.digestMethod).getMessageDigestAlgorithm());
/*     */       }
/*     */       catch (NoSuchAlgorithmException localNoSuchAlgorithmException1) {
/* 426 */         throw new XMLSignatureException(localNoSuchAlgorithmException1);
/*     */       }
/*     */     }
/* 429 */     this.md.reset();
/*     */ 
/* 431 */     Boolean localBoolean = (Boolean)paramXMLCryptoContext.getProperty("javax.xml.crypto.dsig.cacheReference");
/*     */     DigesterOutputStream localDigesterOutputStream;
/* 433 */     if ((localBoolean != null) && (localBoolean.booleanValue() == true)) {
/* 434 */       this.derefData = copyDerefData(paramData);
/* 435 */       localDigesterOutputStream = new DigesterOutputStream(this.md, true);
/*     */     } else {
/* 437 */       localDigesterOutputStream = new DigesterOutputStream(this.md);
/*     */     }
/* 439 */     UnsyncBufferedOutputStream localUnsyncBufferedOutputStream = new UnsyncBufferedOutputStream(localDigesterOutputStream);
/* 440 */     Data localData = paramData;
/* 441 */     int i = 0;
/*     */     Object localObject1;
/* 441 */     for (int j = this.transforms.size(); i < j; i++) {
/* 442 */       localObject1 = (DOMTransform)this.transforms.get(i);
/*     */       try {
/* 444 */         if (i < j - 1)
/* 445 */           localData = ((DOMTransform)localObject1).transform(localData, paramXMLCryptoContext);
/*     */         else
/* 447 */           localData = ((DOMTransform)localObject1).transform(localData, paramXMLCryptoContext, localUnsyncBufferedOutputStream);
/*     */       }
/*     */       catch (TransformException localTransformException) {
/* 450 */         throw new XMLSignatureException(localTransformException);
/*     */       }
/*     */     }
/*     */     try
/*     */     {
/* 455 */       if (localData != null)
/*     */       {
/* 459 */         boolean bool = useC14N11;
/* 460 */         localObject1 = "http://www.w3.org/TR/2001/REC-xml-c14n-20010315";
/*     */         Object localObject2;
/* 461 */         if ((paramXMLCryptoContext instanceof XMLSignContext))
/* 462 */           if (!bool) {
/* 463 */             localObject2 = (Boolean)paramXMLCryptoContext.getProperty("com.sun.org.apache.xml.internal.security.useC14N11");
/*     */ 
/* 465 */             bool = (localObject2 != null) && (((Boolean)localObject2).booleanValue() == true);
/* 466 */             if (bool)
/* 467 */               localObject1 = "http://www.w3.org/2006/12/xml-c14n11";
/*     */           }
/*     */           else {
/* 470 */             localObject1 = "http://www.w3.org/2006/12/xml-c14n11";
/*     */           }
/*     */         XMLSignatureInput localXMLSignatureInput;
/* 473 */         if ((localData instanceof ApacheData)) {
/* 474 */           localXMLSignatureInput = ((ApacheData)localData).getXMLSignatureInput();
/* 475 */         } else if ((localData instanceof OctetStreamData)) {
/* 476 */           localXMLSignatureInput = new XMLSignatureInput(((OctetStreamData)localData).getOctetStream());
/*     */         }
/* 478 */         else if ((localData instanceof NodeSetData)) {
/* 479 */           localObject2 = null;
/*     */           try {
/* 481 */             localObject2 = TransformService.getInstance((String)localObject1, "DOM");
/*     */           } catch (NoSuchAlgorithmException localNoSuchAlgorithmException2) {
/* 483 */             localObject2 = TransformService.getInstance((String)localObject1, "DOM", this.provider);
/*     */           }
/*     */ 
/* 486 */           localData = ((TransformService)localObject2).transform(localData, paramXMLCryptoContext);
/* 487 */           localXMLSignatureInput = new XMLSignatureInput(((OctetStreamData)localData).getOctetStream());
/*     */         }
/*     */         else {
/* 490 */           throw new XMLSignatureException("unrecognized Data type");
/*     */         }
/* 492 */         if (((paramXMLCryptoContext instanceof XMLSignContext)) && (bool) && (!localXMLSignatureInput.isOctetStream()) && (!localXMLSignatureInput.isOutputStreamSet()))
/*     */         {
/* 494 */           localObject2 = new DOMTransform(TransformService.getInstance((String)localObject1, "DOM"));
/*     */ 
/* 496 */           Element localElement = null;
/* 497 */           String str = DOMUtils.getSignaturePrefix(paramXMLCryptoContext);
/* 498 */           if (this.allTransforms.isEmpty()) {
/* 499 */             localElement = DOMUtils.createElement(this.refElem.getOwnerDocument(), "Transforms", "http://www.w3.org/2000/09/xmldsig#", str);
/*     */ 
/* 502 */             this.refElem.insertBefore(localElement, DOMUtils.getFirstChildElement(this.refElem));
/*     */           }
/*     */           else {
/* 505 */             localElement = DOMUtils.getFirstChildElement(this.refElem);
/*     */           }
/* 507 */           ((DOMTransform)localObject2).marshal(localElement, str, (DOMCryptoContext)paramXMLCryptoContext);
/* 508 */           this.allTransforms.add(localObject2);
/* 509 */           localXMLSignatureInput.updateOutputStream(localUnsyncBufferedOutputStream, true);
/*     */         } else {
/* 511 */           localXMLSignatureInput.updateOutputStream(localUnsyncBufferedOutputStream);
/*     */         }
/*     */       }
/* 514 */       localUnsyncBufferedOutputStream.flush();
/* 515 */       if ((localBoolean != null) && (localBoolean.booleanValue() == true)) {
/* 516 */         this.dis = localDigesterOutputStream.getInputStream();
/*     */       }
/* 518 */       return localDigesterOutputStream.getDigestValue();
/*     */     } catch (Exception localException) {
/* 520 */       throw new XMLSignatureException(localException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Node getHere() {
/* 525 */     return this.here;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject) {
/* 529 */     if (this == paramObject) {
/* 530 */       return true;
/*     */     }
/*     */ 
/* 533 */     if (!(paramObject instanceof DOMURIReference)) {
/* 534 */       return false;
/*     */     }
/* 536 */     Reference localReference = (DOMURIReference)paramObject;
/*     */ 
/* 538 */     boolean bool1 = this.id == null ? false : localReference.getId() == null ? true : this.id.equals(localReference.getId());
/*     */ 
/* 540 */     boolean bool2 = this.uri == null ? false : localReference.getURI() == null ? true : this.uri.equals(localReference.getURI());
/*     */ 
/* 542 */     boolean bool3 = this.type == null ? false : localReference.getType() == null ? true : this.type.equals(localReference.getType());
/*     */ 
/* 544 */     boolean bool4 = Arrays.equals(this.digestValue, localReference.getDigestValue());
/*     */ 
/* 547 */     return (this.digestMethod.equals(localReference.getDigestMethod())) && (bool1) && (bool2) && (bool3) && (this.allTransforms.equals(localReference.getTransforms()));
/*     */   }
/*     */ 
/*     */   boolean isDigested()
/*     */   {
/* 552 */     return this.digested;
/*     */   }
/*     */ 
/*     */   private static Data copyDerefData(Data paramData) {
/* 556 */     if ((paramData instanceof ApacheData))
/*     */     {
/* 558 */       ApacheData localApacheData = (ApacheData)paramData;
/* 559 */       XMLSignatureInput localXMLSignatureInput = localApacheData.getXMLSignatureInput();
/* 560 */       if (localXMLSignatureInput.isNodeSet())
/*     */         try {
/* 562 */           Set localSet = localXMLSignatureInput.getNodeSet();
/* 563 */           return new NodeSetData() {
/* 564 */             public Iterator iterator() { return this.val$s.iterator(); }
/*     */           };
/*     */         }
/*     */         catch (Exception localException) {
/* 568 */           log.log(Level.WARNING, "cannot cache dereferenced data: " + localException);
/*     */ 
/* 570 */           return null;
/*     */         }
/* 572 */       if (localXMLSignatureInput.isElement()) {
/* 573 */         return new DOMSubTreeData(localXMLSignatureInput.getSubNode(), localXMLSignatureInput.isExcludeComments());
/*     */       }
/* 575 */       if ((localXMLSignatureInput.isOctetStream()) || (localXMLSignatureInput.isByteArray())) {
/*     */         try {
/* 577 */           return new OctetStreamData(localXMLSignatureInput.getOctetStream(), localXMLSignatureInput.getSourceURI(), localXMLSignatureInput.getMIMEType());
/*     */         }
/*     */         catch (IOException localIOException)
/*     */         {
/* 581 */           log.log(Level.WARNING, "cannot cache dereferenced data: " + localIOException);
/*     */ 
/* 583 */           return null;
/*     */         }
/*     */       }
/*     */     }
/* 587 */     return paramData;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.jcp.xml.dsig.internal.dom.DOMReference
 * JD-Core Version:    0.6.2
 */