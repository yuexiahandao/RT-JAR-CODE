/*     */ package com.sun.org.apache.xml.internal.security.encryption;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.security.c14n.CanonicalizationException;
/*     */ import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
/*     */ import com.sun.org.apache.xml.internal.security.signature.XMLSignatureInput;
/*     */ import com.sun.org.apache.xml.internal.security.transforms.TransformationException;
/*     */ import com.sun.org.apache.xml.internal.security.utils.Base64;
/*     */ import com.sun.org.apache.xml.internal.security.utils.resolver.ResourceResolver;
/*     */ import com.sun.org.apache.xml.internal.security.utils.resolver.ResourceResolverException;
/*     */ import java.io.IOException;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import org.w3c.dom.Attr;
/*     */ 
/*     */ public class XMLCipherInput
/*     */ {
/*  52 */   private static Logger logger = Logger.getLogger(XMLCipher.class.getName());
/*     */   private CipherData _cipherData;
/*     */   private int _mode;
/*     */ 
/*     */   public XMLCipherInput(CipherData paramCipherData)
/*     */     throws XMLEncryptionException
/*     */   {
/*  70 */     this._cipherData = paramCipherData;
/*  71 */     this._mode = 2;
/*  72 */     if (this._cipherData == null)
/*  73 */       throw new XMLEncryptionException("CipherData is null");
/*     */   }
/*     */ 
/*     */   public XMLCipherInput(EncryptedType paramEncryptedType)
/*     */     throws XMLEncryptionException
/*     */   {
/*  88 */     this._cipherData = (paramEncryptedType == null ? null : paramEncryptedType.getCipherData());
/*  89 */     this._mode = 2;
/*  90 */     if (this._cipherData == null)
/*  91 */       throw new XMLEncryptionException("CipherData is null");
/*     */   }
/*     */ 
/*     */   public byte[] getBytes()
/*     */     throws XMLEncryptionException
/*     */   {
/* 105 */     if (this._mode == 2) {
/* 106 */       return getDecryptBytes();
/*     */     }
/* 108 */     return null;
/*     */   }
/*     */ 
/*     */   private byte[] getDecryptBytes()
/*     */     throws XMLEncryptionException
/*     */   {
/* 118 */     String str = null;
/*     */ 
/* 120 */     if (this._cipherData.getDataType() == 2)
/*     */     {
/* 122 */       logger.log(Level.FINE, "Found a reference type CipherData");
/* 123 */       localObject = this._cipherData.getCipherReference();
/*     */ 
/* 128 */       Attr localAttr = ((CipherReference)localObject).getURIAsAttr();
/* 129 */       XMLSignatureInput localXMLSignatureInput = null;
/*     */       try
/*     */       {
/* 132 */         ResourceResolver localResourceResolver = ResourceResolver.getInstance(localAttr, null);
/*     */ 
/* 134 */         localXMLSignatureInput = localResourceResolver.resolve(localAttr, null);
/*     */       } catch (ResourceResolverException localResourceResolverException) {
/* 136 */         throw new XMLEncryptionException("empty", localResourceResolverException);
/*     */       }
/*     */ 
/* 139 */       if (localXMLSignatureInput != null)
/* 140 */         logger.log(Level.FINE, "Managed to resolve URI \"" + ((CipherReference)localObject).getURI() + "\"");
/*     */       else {
/* 142 */         logger.log(Level.FINE, "Failed to resolve URI \"" + ((CipherReference)localObject).getURI() + "\"");
/*     */       }
/*     */ 
/* 146 */       Transforms localTransforms = ((CipherReference)localObject).getTransforms();
/* 147 */       if (localTransforms != null) {
/* 148 */         logger.log(Level.FINE, "Have transforms in cipher reference");
/*     */         try {
/* 150 */           com.sun.org.apache.xml.internal.security.transforms.Transforms localTransforms1 = localTransforms.getDSTransforms();
/*     */ 
/* 152 */           localXMLSignatureInput = localTransforms1.performTransforms(localXMLSignatureInput);
/*     */         } catch (TransformationException localTransformationException) {
/* 154 */           throw new XMLEncryptionException("empty", localTransformationException);
/*     */         }
/*     */       }
/*     */       try
/*     */       {
/* 159 */         return localXMLSignatureInput.getBytes();
/*     */       } catch (IOException localIOException) {
/* 161 */         throw new XMLEncryptionException("empty", localIOException);
/*     */       } catch (CanonicalizationException localCanonicalizationException) {
/* 163 */         throw new XMLEncryptionException("empty", localCanonicalizationException);
/*     */       }
/*     */     }
/*     */ 
/* 167 */     if (this._cipherData.getDataType() == 1) {
/* 168 */       str = this._cipherData.getCipherValue().getValue();
/*     */     }
/*     */     else {
/* 171 */       throw new XMLEncryptionException("CipherData.getDataType() returned unexpected value");
/*     */     }
/*     */ 
/* 174 */     logger.log(Level.FINE, "Encrypted octets:\n" + str);
/*     */ 
/* 176 */     Object localObject = null;
/*     */     try {
/* 178 */       localObject = Base64.decode(str);
/*     */     } catch (Base64DecodingException localBase64DecodingException) {
/* 180 */       throw new XMLEncryptionException("empty", localBase64DecodingException);
/*     */     }
/*     */ 
/* 183 */     return localObject;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.encryption.XMLCipherInput
 * JD-Core Version:    0.6.2
 */