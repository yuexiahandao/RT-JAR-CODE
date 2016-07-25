/*     */ package com.sun.org.apache.xml.internal.security.utils;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;
/*     */ 
/*     */ public class EncryptionConstants
/*     */ {
/*     */   public static final String _ATT_ALGORITHM = "Algorithm";
/*     */   public static final String _ATT_ID = "Id";
/*     */   public static final String _ATT_TARGET = "Target";
/*     */   public static final String _ATT_TYPE = "Type";
/*     */   public static final String _ATT_URI = "URI";
/*     */   public static final String _ATT_ENCODING = "Encoding";
/*     */   public static final String _ATT_RECIPIENT = "Recipient";
/*     */   public static final String _ATT_MIMETYPE = "MimeType";
/*     */   public static final String _TAG_CARRIEDKEYNAME = "CarriedKeyName";
/*     */   public static final String _TAG_CIPHERDATA = "CipherData";
/*     */   public static final String _TAG_CIPHERREFERENCE = "CipherReference";
/*     */   public static final String _TAG_CIPHERVALUE = "CipherValue";
/*     */   public static final String _TAG_DATAREFERENCE = "DataReference";
/*     */   public static final String _TAG_ENCRYPTEDDATA = "EncryptedData";
/*     */   public static final String _TAG_ENCRYPTEDKEY = "EncryptedKey";
/*     */   public static final String _TAG_ENCRYPTIONMETHOD = "EncryptionMethod";
/*     */   public static final String _TAG_ENCRYPTIONPROPERTIES = "EncryptionProperties";
/*     */   public static final String _TAG_ENCRYPTIONPROPERTY = "EncryptionProperty";
/*     */   public static final String _TAG_KEYREFERENCE = "KeyReference";
/*     */   public static final String _TAG_KEYSIZE = "KeySize";
/*     */   public static final String _TAG_OAEPPARAMS = "OAEPparams";
/*     */   public static final String _TAG_REFERENCELIST = "ReferenceList";
/*     */   public static final String _TAG_TRANSFORMS = "Transforms";
/*     */   public static final String _TAG_AGREEMENTMETHOD = "AgreementMethod";
/*     */   public static final String _TAG_KA_NONCE = "KA-Nonce";
/*     */   public static final String _TAG_ORIGINATORKEYINFO = "OriginatorKeyInfo";
/*     */   public static final String _TAG_RECIPIENTKEYINFO = "RecipientKeyInfo";
/*     */   public static final String ENCRYPTIONSPECIFICATION_URL = "http://www.w3.org/TR/2001/WD-xmlenc-core-20010626/";
/*     */   public static final String EncryptionSpecNS = "http://www.w3.org/2001/04/xmlenc#";
/*     */   public static final String TYPE_CONTENT = "http://www.w3.org/2001/04/xmlenc#Content";
/*     */   public static final String TYPE_ELEMENT = "http://www.w3.org/2001/04/xmlenc#Element";
/*     */   public static final String TYPE_MEDIATYPE = "http://www.isi.edu/in-notes/iana/assignments/media-types/";
/*     */   public static final String ALGO_ID_BLOCKCIPHER_TRIPLEDES = "http://www.w3.org/2001/04/xmlenc#tripledes-cbc";
/*     */   public static final String ALGO_ID_BLOCKCIPHER_AES128 = "http://www.w3.org/2001/04/xmlenc#aes128-cbc";
/*     */   public static final String ALGO_ID_BLOCKCIPHER_AES256 = "http://www.w3.org/2001/04/xmlenc#aes256-cbc";
/*     */   public static final String ALGO_ID_BLOCKCIPHER_AES192 = "http://www.w3.org/2001/04/xmlenc#aes192-cbc";
/*     */   public static final String ALGO_ID_KEYTRANSPORT_RSA15 = "http://www.w3.org/2001/04/xmlenc#rsa-1_5";
/*     */   public static final String ALGO_ID_KEYTRANSPORT_RSAOAEP = "http://www.w3.org/2001/04/xmlenc#rsa-oaep-mgf1p";
/*     */   public static final String ALGO_ID_KEYAGREEMENT_DH = "http://www.w3.org/2001/04/xmlenc#dh";
/*     */   public static final String ALGO_ID_KEYWRAP_TRIPLEDES = "http://www.w3.org/2001/04/xmlenc#kw-tripledes";
/*     */   public static final String ALGO_ID_KEYWRAP_AES128 = "http://www.w3.org/2001/04/xmlenc#kw-aes128";
/*     */   public static final String ALGO_ID_KEYWRAP_AES256 = "http://www.w3.org/2001/04/xmlenc#kw-aes256";
/*     */   public static final String ALGO_ID_KEYWRAP_AES192 = "http://www.w3.org/2001/04/xmlenc#kw-aes192";
/*     */   public static final String ALGO_ID_AUTHENTICATION_XMLSIGNATURE = "http://www.w3.org/TR/2001/CR-xmldsig-core-20010419/";
/*     */   public static final String ALGO_ID_C14N_WITHCOMMENTS = "http://www.w3.org/TR/2001/REC-xml-c14n-20010315#WithComments";
/*     */   public static final String ALGO_ID_C14N_OMITCOMMENTS = "http://www.w3.org/TR/2001/REC-xml-c14n-20010315";
/*     */   public static final String ALGO_ID_ENCODING_BASE64 = "http://www.w3.org/2000/09/xmldsig#base64";
/*     */ 
/*     */   public static void setEncryptionSpecNSprefix(String paramString)
/*     */     throws XMLSecurityException
/*     */   {
/* 167 */     ElementProxy.setDefaultPrefix("http://www.w3.org/2001/04/xmlenc#", paramString);
/*     */   }
/*     */ 
/*     */   public static String getEncryptionSpecNSprefix()
/*     */   {
/* 177 */     return ElementProxy.getDefaultPrefix("http://www.w3.org/2001/04/xmlenc#");
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.utils.EncryptionConstants
 * JD-Core Version:    0.6.2
 */