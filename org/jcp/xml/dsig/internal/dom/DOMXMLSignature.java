/*     */ package org.jcp.xml.dsig.internal.dom;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.security.Init;
/*     */ import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
/*     */ import com.sun.org.apache.xml.internal.security.utils.Base64;
/*     */ import java.security.InvalidKeyException;
/*     */ import java.security.Key;
/*     */ import java.security.Provider;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.xml.crypto.KeySelector;
/*     */ import javax.xml.crypto.KeySelector.Purpose;
/*     */ import javax.xml.crypto.KeySelectorException;
/*     */ import javax.xml.crypto.KeySelectorResult;
/*     */ import javax.xml.crypto.MarshalException;
/*     */ import javax.xml.crypto.XMLCryptoContext;
/*     */ import javax.xml.crypto.XMLStructure;
/*     */ import javax.xml.crypto.dom.DOMCryptoContext;
/*     */ import javax.xml.crypto.dsig.Manifest;
/*     */ import javax.xml.crypto.dsig.Reference;
/*     */ import javax.xml.crypto.dsig.SignatureMethod;
/*     */ import javax.xml.crypto.dsig.SignedInfo;
/*     */ import javax.xml.crypto.dsig.Transform;
/*     */ import javax.xml.crypto.dsig.XMLObject;
/*     */ import javax.xml.crypto.dsig.XMLSignContext;
/*     */ import javax.xml.crypto.dsig.XMLSignature;
/*     */ import javax.xml.crypto.dsig.XMLSignature.SignatureValue;
/*     */ import javax.xml.crypto.dsig.XMLSignatureException;
/*     */ import javax.xml.crypto.dsig.XMLValidateContext;
/*     */ import javax.xml.crypto.dsig.dom.DOMSignContext;
/*     */ import javax.xml.crypto.dsig.dom.DOMValidateContext;
/*     */ import javax.xml.crypto.dsig.keyinfo.KeyInfo;
/*     */ import org.w3c.dom.Attr;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ 
/*     */ public final class DOMXMLSignature extends DOMStructure
/*     */   implements XMLSignature
/*     */ {
/*  70 */   private static Logger log = Logger.getLogger("org.jcp.xml.dsig.internal.dom");
/*     */   private String id;
/*     */   private XMLSignature.SignatureValue sv;
/*     */   private KeyInfo ki;
/*     */   private List objects;
/*     */   private SignedInfo si;
/*  76 */   private Document ownerDoc = null;
/*  77 */   private Element localSigElem = null;
/*  78 */   private Element sigElem = null;
/*     */   private boolean validationStatus;
/*  80 */   private boolean validated = false;
/*     */   private KeySelectorResult ksr;
/*     */   private HashMap signatureIdMap;
/*     */ 
/*     */   public DOMXMLSignature(SignedInfo paramSignedInfo, KeyInfo paramKeyInfo, List paramList, String paramString1, String paramString2)
/*     */   {
/* 104 */     if (paramSignedInfo == null) {
/* 105 */       throw new NullPointerException("signedInfo cannot be null");
/*     */     }
/* 107 */     this.si = paramSignedInfo;
/* 108 */     this.id = paramString1;
/* 109 */     this.sv = new DOMSignatureValue(paramString2);
/* 110 */     if (paramList == null) {
/* 111 */       this.objects = Collections.EMPTY_LIST;
/*     */     } else {
/* 113 */       ArrayList localArrayList = new ArrayList(paramList);
/* 114 */       int i = 0; for (int j = localArrayList.size(); i < j; i++) {
/* 115 */         if (!(localArrayList.get(i) instanceof XMLObject)) {
/* 116 */           throw new ClassCastException("objs[" + i + "] is not an XMLObject");
/*     */         }
/*     */       }
/*     */ 
/* 120 */       this.objects = Collections.unmodifiableList(localArrayList);
/*     */     }
/* 122 */     this.ki = paramKeyInfo;
/*     */   }
/*     */ 
/*     */   public DOMXMLSignature(Element paramElement, XMLCryptoContext paramXMLCryptoContext, Provider paramProvider)
/*     */     throws MarshalException
/*     */   {
/* 133 */     this.localSigElem = paramElement;
/* 134 */     this.ownerDoc = this.localSigElem.getOwnerDocument();
/*     */ 
/* 137 */     this.id = DOMUtils.getAttributeValue(this.localSigElem, "Id");
/*     */ 
/* 140 */     Element localElement1 = DOMUtils.getFirstChildElement(this.localSigElem);
/* 141 */     this.si = new DOMSignedInfo(localElement1, paramXMLCryptoContext, paramProvider);
/*     */ 
/* 144 */     Element localElement2 = DOMUtils.getNextSiblingElement(localElement1);
/* 145 */     this.sv = new DOMSignatureValue(localElement2);
/*     */ 
/* 148 */     Element localElement3 = DOMUtils.getNextSiblingElement(localElement2);
/* 149 */     if ((localElement3 != null) && (localElement3.getLocalName().equals("KeyInfo"))) {
/* 150 */       this.ki = new DOMKeyInfo(localElement3, paramXMLCryptoContext, paramProvider);
/* 151 */       localElement3 = DOMUtils.getNextSiblingElement(localElement3);
/*     */     }
/*     */ 
/* 155 */     if (localElement3 == null) {
/* 156 */       this.objects = Collections.EMPTY_LIST;
/*     */     } else {
/* 158 */       ArrayList localArrayList = new ArrayList();
/* 159 */       while (localElement3 != null) {
/* 160 */         localArrayList.add(new DOMXMLObject(localElement3, paramXMLCryptoContext, paramProvider));
/*     */ 
/* 162 */         localElement3 = DOMUtils.getNextSiblingElement(localElement3);
/*     */       }
/* 164 */       this.objects = Collections.unmodifiableList(localArrayList);
/*     */     }
/*     */   }
/*     */ 
/*     */   public String getId() {
/* 169 */     return this.id;
/*     */   }
/*     */ 
/*     */   public KeyInfo getKeyInfo() {
/* 173 */     return this.ki;
/*     */   }
/*     */ 
/*     */   public SignedInfo getSignedInfo() {
/* 177 */     return this.si;
/*     */   }
/*     */ 
/*     */   public List getObjects() {
/* 181 */     return this.objects;
/*     */   }
/*     */ 
/*     */   public XMLSignature.SignatureValue getSignatureValue() {
/* 185 */     return this.sv;
/*     */   }
/*     */ 
/*     */   public KeySelectorResult getKeySelectorResult() {
/* 189 */     return this.ksr;
/*     */   }
/*     */ 
/*     */   public void marshal(Node paramNode, String paramString, DOMCryptoContext paramDOMCryptoContext) throws MarshalException
/*     */   {
/* 194 */     marshal(paramNode, null, paramString, paramDOMCryptoContext);
/*     */   }
/*     */ 
/*     */   public void marshal(Node paramNode1, Node paramNode2, String paramString, DOMCryptoContext paramDOMCryptoContext) throws MarshalException
/*     */   {
/* 199 */     this.ownerDoc = DOMUtils.getOwnerDocument(paramNode1);
/*     */ 
/* 201 */     this.sigElem = DOMUtils.createElement(this.ownerDoc, "Signature", "http://www.w3.org/2000/09/xmldsig#", paramString);
/*     */ 
/* 205 */     if ((paramString == null) || (paramString.length() == 0)) {
/* 206 */       this.sigElem.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns", "http://www.w3.org/2000/09/xmldsig#");
/*     */     }
/*     */     else {
/* 209 */       this.sigElem.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:" + paramString, "http://www.w3.org/2000/09/xmldsig#");
/*     */     }
/*     */ 
/* 215 */     ((DOMSignedInfo)this.si).marshal(this.sigElem, paramString, paramDOMCryptoContext);
/*     */ 
/* 218 */     ((DOMSignatureValue)this.sv).marshal(this.sigElem, paramString, paramDOMCryptoContext);
/*     */ 
/* 221 */     if (this.ki != null) {
/* 222 */       ((DOMKeyInfo)this.ki).marshal(this.sigElem, null, paramString, paramDOMCryptoContext);
/*     */     }
/*     */ 
/* 226 */     int i = 0; for (int j = this.objects.size(); i < j; i++) {
/* 227 */       ((DOMXMLObject)this.objects.get(i)).marshal(this.sigElem, paramString, paramDOMCryptoContext);
/*     */     }
/*     */ 
/* 231 */     DOMUtils.setAttributeID(this.sigElem, "Id", this.id);
/*     */ 
/* 233 */     paramNode1.insertBefore(this.sigElem, paramNode2);
/*     */   }
/*     */ 
/*     */   public boolean validate(XMLValidateContext paramXMLValidateContext)
/*     */     throws XMLSignatureException
/*     */   {
/* 239 */     if (paramXMLValidateContext == null) {
/* 240 */       throw new NullPointerException("validateContext is null");
/*     */     }
/*     */ 
/* 243 */     if (!(paramXMLValidateContext instanceof DOMValidateContext)) {
/* 244 */       throw new ClassCastException("validateContext must be of type DOMValidateContext");
/*     */     }
/*     */ 
/* 248 */     if (this.validated) {
/* 249 */       return this.validationStatus;
/*     */     }
/*     */ 
/* 253 */     boolean bool1 = this.sv.validate(paramXMLValidateContext);
/* 254 */     if (!bool1) {
/* 255 */       this.validationStatus = false;
/* 256 */       this.validated = true;
/* 257 */       return this.validationStatus;
/*     */     }
/*     */ 
/* 261 */     List localList1 = this.si.getReferences();
/* 262 */     boolean bool2 = true;
/* 263 */     int i = 0; for (int j = localList1.size(); (bool2) && (i < j); i++) {
/* 264 */       Reference localReference1 = (Reference)localList1.get(i);
/* 265 */       boolean bool3 = localReference1.validate(paramXMLValidateContext);
/* 266 */       if (log.isLoggable(Level.FINE)) {
/* 267 */         log.log(Level.FINE, "Reference[" + localReference1.getURI() + "] is valid: " + bool3);
/*     */       }
/*     */ 
/* 270 */       bool2 &= bool3;
/*     */     }
/* 272 */     if (!bool2) {
/* 273 */       if (log.isLoggable(Level.FINE)) {
/* 274 */         log.log(Level.FINE, "Couldn't validate the References");
/*     */       }
/* 276 */       this.validationStatus = false;
/* 277 */       this.validated = true;
/* 278 */       return this.validationStatus;
/*     */     }
/*     */ 
/* 282 */     i = 1;
/* 283 */     if (Boolean.TRUE.equals(paramXMLValidateContext.getProperty("org.jcp.xml.dsig.validateManifests")))
/*     */     {
/* 286 */       j = 0; for (int k = this.objects.size(); (i != 0) && (j < k); j++) {
/* 287 */         XMLObject localXMLObject = (XMLObject)this.objects.get(j);
/* 288 */         List localList2 = localXMLObject.getContent();
/* 289 */         int m = localList2.size();
/* 290 */         for (int n = 0; (i != 0) && (n < m); n++) {
/* 291 */           XMLStructure localXMLStructure = (XMLStructure)localList2.get(n);
/* 292 */           if ((localXMLStructure instanceof Manifest)) {
/* 293 */             if (log.isLoggable(Level.FINE)) {
/* 294 */               log.log(Level.FINE, "validating manifest");
/*     */             }
/* 296 */             Manifest localManifest = (Manifest)localXMLStructure;
/* 297 */             List localList3 = localManifest.getReferences();
/* 298 */             int i1 = localList3.size();
/* 299 */             for (int i2 = 0; (i != 0) && (i2 < i1); i2++) {
/* 300 */               Reference localReference2 = (Reference)localList3.get(i2);
/* 301 */               int i3 = localReference2.validate(paramXMLValidateContext);
/* 302 */               if (log.isLoggable(Level.FINE)) {
/* 303 */                 log.log(Level.FINE, "Manifest ref[" + localReference2.getURI() + "] is valid: " + i3);
/*     */               }
/*     */ 
/* 306 */               i &= i3;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 313 */     this.validationStatus = i;
/* 314 */     this.validated = true;
/* 315 */     return this.validationStatus;
/*     */   }
/*     */ 
/*     */   public void sign(XMLSignContext paramXMLSignContext) throws MarshalException, XMLSignatureException
/*     */   {
/* 320 */     if (paramXMLSignContext == null) {
/* 321 */       throw new NullPointerException("signContext cannot be null");
/*     */     }
/* 323 */     DOMSignContext localDOMSignContext = (DOMSignContext)paramXMLSignContext;
/* 324 */     if (localDOMSignContext != null) {
/* 325 */       marshal(localDOMSignContext.getParent(), localDOMSignContext.getNextSibling(), DOMUtils.getSignaturePrefix(localDOMSignContext), localDOMSignContext);
/*     */     }
/*     */ 
/* 330 */     ArrayList localArrayList = new ArrayList();
/*     */ 
/* 334 */     this.signatureIdMap = new HashMap();
/* 335 */     this.signatureIdMap.put(this.id, this);
/* 336 */     this.signatureIdMap.put(this.si.getId(), this.si);
/* 337 */     List localList1 = this.si.getReferences();
/* 338 */     int i = 0;
/*     */     Object localObject;
/* 338 */     for (int j = localList1.size(); i < j; i++) {
/* 339 */       localObject = (Reference)localList1.get(i);
/* 340 */       this.signatureIdMap.put(((Reference)localObject).getId(), localObject);
/*     */     }
/* 342 */     i = 0; for (j = this.objects.size(); i < j; i++) {
/* 343 */       localObject = (XMLObject)this.objects.get(i);
/* 344 */       this.signatureIdMap.put(((XMLObject)localObject).getId(), localObject);
/* 345 */       List localList2 = ((XMLObject)localObject).getContent();
/* 346 */       int k = 0; for (int m = localList2.size(); k < m; k++) {
/* 347 */         XMLStructure localXMLStructure = (XMLStructure)localList2.get(k);
/* 348 */         if ((localXMLStructure instanceof Manifest)) {
/* 349 */           Manifest localManifest = (Manifest)localXMLStructure;
/* 350 */           this.signatureIdMap.put(localManifest.getId(), localManifest);
/* 351 */           List localList3 = localManifest.getReferences();
/* 352 */           int n = 0; for (int i1 = localList3.size(); n < i1; n++) {
/* 353 */             Reference localReference = (Reference)localList3.get(n);
/* 354 */             localArrayList.add(localReference);
/* 355 */             this.signatureIdMap.put(localReference.getId(), localReference);
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 362 */     localArrayList.addAll(this.si.getReferences());
/*     */ 
/* 365 */     i = 0; for (j = localArrayList.size(); i < j; i++) {
/* 366 */       localObject = (DOMReference)localArrayList.get(i);
/* 367 */       digestReference((DOMReference)localObject, paramXMLSignContext);
/*     */     }
/*     */ 
/* 371 */     i = 0; for (j = localArrayList.size(); i < j; i++) {
/* 372 */       localObject = (DOMReference)localArrayList.get(i);
/* 373 */       if (!((DOMReference)localObject).isDigested())
/*     */       {
/* 376 */         ((DOMReference)localObject).digest(paramXMLSignContext);
/*     */       }
/*     */     }
/* 379 */     Key localKey = null;
/* 380 */     KeySelectorResult localKeySelectorResult = null;
/*     */     try {
/* 382 */       localKeySelectorResult = paramXMLSignContext.getKeySelector().select(this.ki, KeySelector.Purpose.SIGN, this.si.getSignatureMethod(), paramXMLSignContext);
/*     */ 
/* 385 */       localKey = localKeySelectorResult.getKey();
/* 386 */       if (localKey == null)
/* 387 */         throw new XMLSignatureException("the keySelector did not find a signing key");
/*     */     }
/*     */     catch (KeySelectorException localKeySelectorException)
/*     */     {
/* 391 */       throw new XMLSignatureException("cannot find signing key", localKeySelectorException);
/*     */     }
/*     */ 
/* 395 */     byte[] arrayOfByte = null;
/*     */     try {
/* 397 */       arrayOfByte = ((DOMSignatureMethod)this.si.getSignatureMethod()).sign(localKey, (DOMSignedInfo)this.si, paramXMLSignContext);
/*     */     }
/*     */     catch (InvalidKeyException localInvalidKeyException) {
/* 400 */       throw new XMLSignatureException(localInvalidKeyException);
/*     */     }
/*     */ 
/* 403 */     if (log.isLoggable(Level.FINE)) {
/* 404 */       log.log(Level.FINE, "SignatureValue = " + arrayOfByte);
/*     */     }
/* 406 */     ((DOMSignatureValue)this.sv).setValue(arrayOfByte);
/*     */ 
/* 408 */     this.localSigElem = this.sigElem;
/* 409 */     this.ksr = localKeySelectorResult;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject) {
/* 413 */     if (this == paramObject) {
/* 414 */       return true;
/*     */     }
/*     */ 
/* 417 */     if (!(paramObject instanceof XMLSignature)) {
/* 418 */       return false;
/*     */     }
/* 420 */     XMLSignature localXMLSignature = (XMLSignature)paramObject;
/*     */ 
/* 422 */     boolean bool1 = this.id == null ? false : localXMLSignature.getId() == null ? true : this.id.equals(localXMLSignature.getId());
/*     */ 
/* 424 */     boolean bool2 = this.ki == null ? false : localXMLSignature.getKeyInfo() == null ? true : this.ki.equals(localXMLSignature.getKeyInfo());
/*     */ 
/* 428 */     return (bool1) && (bool2) && (this.sv.equals(localXMLSignature.getSignatureValue())) && (this.si.equals(localXMLSignature.getSignedInfo())) && (this.objects.equals(localXMLSignature.getObjects()));
/*     */   }
/*     */ 
/*     */   private void digestReference(DOMReference paramDOMReference, XMLSignContext paramXMLSignContext)
/*     */     throws XMLSignatureException
/*     */   {
/* 436 */     if (paramDOMReference.isDigested()) {
/* 437 */       return;
/*     */     }
/*     */ 
/* 440 */     String str1 = paramDOMReference.getURI();
/* 441 */     if (Utils.sameDocumentURI(str1)) {
/* 442 */       String str2 = Utils.parseIdFromSameDocumentURI(str1);
/*     */       Object localObject;
/* 443 */       if ((str2 != null) && (this.signatureIdMap.containsKey(str2))) {
/* 444 */         localObject = this.signatureIdMap.get(str2);
/* 445 */         if ((localObject instanceof DOMReference)) {
/* 446 */           digestReference((DOMReference)localObject, paramXMLSignContext);
/* 447 */         } else if ((localObject instanceof Manifest)) {
/* 448 */           Manifest localManifest = (Manifest)localObject;
/* 449 */           List localList = localManifest.getReferences();
/* 450 */           int k = 0; for (int m = localList.size(); k < m; k++) {
/* 451 */             digestReference((DOMReference)localList.get(k), paramXMLSignContext);
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 459 */       if (str1.length() == 0) {
/* 460 */         localObject = paramDOMReference.getTransforms();
/* 461 */         int i = 0; for (int j = ((List)localObject).size(); i < j; i++) {
/* 462 */           Transform localTransform = (Transform)((List)localObject).get(i);
/* 463 */           String str3 = localTransform.getAlgorithm();
/* 464 */           if ((str3.equals("http://www.w3.org/TR/1999/REC-xpath-19991116")) || (str3.equals("http://www.w3.org/2002/06/xmldsig-filter2")))
/*     */           {
/* 466 */             return;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 471 */     paramDOMReference.digest(paramXMLSignContext);
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  85 */     Init.init();
/*     */   }
/*     */ 
/*     */   public class DOMSignatureValue extends DOMStructure
/*     */     implements XMLSignature.SignatureValue
/*     */   {
/*     */     private String id;
/*     */     private byte[] value;
/*     */     private String valueBase64;
/*     */     private Element sigValueElem;
/* 481 */     private boolean validated = false;
/*     */     private boolean validationStatus;
/*     */ 
/*     */     DOMSignatureValue(String arg2)
/*     */     {
/*     */       Object localObject;
/* 485 */       this.id = localObject;
/*     */     }
/*     */ 
/*     */     DOMSignatureValue(Element arg2) throws MarshalException {
/*     */       Element localElement;
/*     */       try {
/* 491 */         this.value = Base64.decode(localElement);
/*     */       } catch (Base64DecodingException localBase64DecodingException) {
/* 493 */         throw new MarshalException(localBase64DecodingException);
/*     */       }
/*     */ 
/* 496 */       Attr localAttr = localElement.getAttributeNodeNS(null, "Id");
/* 497 */       if (localAttr != null) {
/* 498 */         this.id = localAttr.getValue();
/* 499 */         localElement.setIdAttributeNode(localAttr, true);
/*     */       } else {
/* 501 */         this.id = null;
/*     */       }
/* 503 */       this.sigValueElem = localElement;
/*     */     }
/*     */ 
/*     */     public String getId() {
/* 507 */       return this.id;
/*     */     }
/*     */ 
/*     */     public byte[] getValue() {
/* 511 */       return this.value == null ? null : (byte[])this.value.clone();
/*     */     }
/*     */ 
/*     */     public boolean validate(XMLValidateContext paramXMLValidateContext)
/*     */       throws XMLSignatureException
/*     */     {
/* 517 */       if (paramXMLValidateContext == null) {
/* 518 */         throw new NullPointerException("context cannot be null");
/*     */       }
/*     */ 
/* 521 */       if (this.validated) {
/* 522 */         return this.validationStatus;
/* 526 */       }
/*     */ SignatureMethod localSignatureMethod = DOMXMLSignature.this.si.getSignatureMethod();
/* 527 */       Key localKey = null;
/*     */       KeySelectorResult localKeySelectorResult;
/*     */       try {
/* 530 */         localKeySelectorResult = paramXMLValidateContext.getKeySelector().select(DOMXMLSignature.this.ki, KeySelector.Purpose.VERIFY, localSignatureMethod, paramXMLValidateContext);
/*     */ 
/* 532 */         localKey = localKeySelectorResult.getKey();
/* 533 */         if (localKey == null)
/* 534 */           throw new XMLSignatureException("the keyselector did not find a validation key");
/*     */       }
/*     */       catch (KeySelectorException localKeySelectorException)
/*     */       {
/* 538 */         throw new XMLSignatureException("cannot find validation key", localKeySelectorException);
/*     */       }
/*     */ 
/*     */       try
/*     */       {
/* 544 */         this.validationStatus = ((DOMSignatureMethod)localSignatureMethod).verify(localKey, (DOMSignedInfo)DOMXMLSignature.this.si, this.value, paramXMLValidateContext);
/*     */       }
/*     */       catch (Exception localException) {
/* 547 */         throw new XMLSignatureException(localException);
/*     */       }
/*     */ 
/* 550 */       this.validated = true;
/* 551 */       DOMXMLSignature.this.ksr = localKeySelectorResult;
/* 552 */       return this.validationStatus;
/*     */     }
/*     */ 
/*     */     public boolean equals(Object paramObject) {
/* 556 */       if (this == paramObject) {
/* 557 */         return true;
/*     */       }
/*     */ 
/* 560 */       if (!(paramObject instanceof XMLSignature.SignatureValue)) {
/* 561 */         return false;
/*     */       }
/* 563 */       XMLSignature.SignatureValue localSignatureValue = (XMLSignature.SignatureValue)paramObject;
/*     */ 
/* 565 */       boolean bool = this.id == null ? false : localSignatureValue.getId() == null ? true : this.id.equals(localSignatureValue.getId());
/*     */ 
/* 569 */       return bool;
/*     */     }
/*     */ 
/*     */     public void marshal(Node paramNode, String paramString, DOMCryptoContext paramDOMCryptoContext)
/*     */       throws MarshalException
/*     */     {
/* 576 */       this.sigValueElem = DOMUtils.createElement(DOMXMLSignature.this.ownerDoc, "SignatureValue", "http://www.w3.org/2000/09/xmldsig#", paramString);
/*     */ 
/* 578 */       if (this.valueBase64 != null) {
/* 579 */         this.sigValueElem.appendChild(DOMXMLSignature.this.ownerDoc.createTextNode(this.valueBase64));
/*     */       }
/*     */ 
/* 583 */       DOMUtils.setAttributeID(this.sigValueElem, "Id", this.id);
/* 584 */       paramNode.appendChild(this.sigValueElem);
/*     */     }
/*     */ 
/*     */     void setValue(byte[] paramArrayOfByte) {
/* 588 */       this.value = paramArrayOfByte;
/* 589 */       this.valueBase64 = Base64.encode(paramArrayOfByte);
/* 590 */       this.sigValueElem.appendChild(DOMXMLSignature.this.ownerDoc.createTextNode(this.valueBase64));
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.jcp.xml.dsig.internal.dom.DOMXMLSignature
 * JD-Core Version:    0.6.2
 */