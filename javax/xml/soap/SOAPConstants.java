/*     */ package javax.xml.soap;
/*     */ 
/*     */ import javax.xml.namespace.QName;
/*     */ 
/*     */ public abstract interface SOAPConstants
/*     */ {
/*     */   public static final String DYNAMIC_SOAP_PROTOCOL = "Dynamic Protocol";
/*     */   public static final String SOAP_1_1_PROTOCOL = "SOAP 1.1 Protocol";
/*     */   public static final String SOAP_1_2_PROTOCOL = "SOAP 1.2 Protocol";
/*     */   public static final String DEFAULT_SOAP_PROTOCOL = "SOAP 1.1 Protocol";
/*     */   public static final String URI_NS_SOAP_1_1_ENVELOPE = "http://schemas.xmlsoap.org/soap/envelope/";
/*     */   public static final String URI_NS_SOAP_1_2_ENVELOPE = "http://www.w3.org/2003/05/soap-envelope";
/*     */   public static final String URI_NS_SOAP_ENVELOPE = "http://schemas.xmlsoap.org/soap/envelope/";
/*     */   public static final String URI_NS_SOAP_ENCODING = "http://schemas.xmlsoap.org/soap/encoding/";
/*     */   public static final String URI_NS_SOAP_1_2_ENCODING = "http://www.w3.org/2003/05/soap-encoding";
/*     */   public static final String SOAP_1_1_CONTENT_TYPE = "text/xml";
/*     */   public static final String SOAP_1_2_CONTENT_TYPE = "application/soap+xml";
/*     */   public static final String URI_SOAP_ACTOR_NEXT = "http://schemas.xmlsoap.org/soap/actor/next";
/*     */   public static final String URI_SOAP_1_2_ROLE_NEXT = "http://www.w3.org/2003/05/soap-envelope/role/next";
/*     */   public static final String URI_SOAP_1_2_ROLE_NONE = "http://www.w3.org/2003/05/soap-envelope/role/none";
/*     */   public static final String URI_SOAP_1_2_ROLE_ULTIMATE_RECEIVER = "http://www.w3.org/2003/05/soap-envelope/role/ultimateReceiver";
/*     */   public static final String SOAP_ENV_PREFIX = "env";
/* 170 */   public static final QName SOAP_VERSIONMISMATCH_FAULT = new QName("http://www.w3.org/2003/05/soap-envelope", "VersionMismatch", "env");
/*     */ 
/* 177 */   public static final QName SOAP_MUSTUNDERSTAND_FAULT = new QName("http://www.w3.org/2003/05/soap-envelope", "MustUnderstand", "env");
/*     */ 
/* 184 */   public static final QName SOAP_DATAENCODINGUNKNOWN_FAULT = new QName("http://www.w3.org/2003/05/soap-envelope", "DataEncodingUnknown", "env");
/*     */ 
/* 191 */   public static final QName SOAP_SENDER_FAULT = new QName("http://www.w3.org/2003/05/soap-envelope", "Sender", "env");
/*     */ 
/* 198 */   public static final QName SOAP_RECEIVER_FAULT = new QName("http://www.w3.org/2003/05/soap-envelope", "Receiver", "env");
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.soap.SOAPConstants
 * JD-Core Version:    0.6.2
 */