/*     */ package com.sun.xml.internal.messaging.saaj.soap.name;
/*     */ 
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.soap.Name;
/*     */ import org.w3c.dom.Element;
/*     */ 
/*     */ public class NameImpl
/*     */   implements Name
/*     */ {
/*     */   public static final String XML_NAMESPACE_PREFIX = "xml";
/*     */   public static final String XML_SCHEMA_NAMESPACE_PREFIX = "xs";
/*     */   public static final String SOAP_ENVELOPE_PREFIX = "SOAP-ENV";
/*     */   public static final String XML_NAMESPACE = "http://www.w3.org/XML/1998/namespace";
/*     */   public static final String SOAP11_NAMESPACE = "http://schemas.xmlsoap.org/soap/envelope/";
/*     */   public static final String SOAP12_NAMESPACE = "http://www.w3.org/2003/05/soap-envelope";
/*     */   public static final String XML_SCHEMA_NAMESPACE = "http://www.w3.org/2001/XMLSchema";
/*  53 */   protected String uri = "";
/*  54 */   protected String localName = "";
/*  55 */   protected String prefix = "";
/*  56 */   private String qualifiedName = null;
/*     */ 
/*  58 */   protected static final Logger log = Logger.getLogger("com.sun.xml.internal.messaging.saaj.soap.name", "com.sun.xml.internal.messaging.saaj.soap.name.LocalStrings");
/*     */ 
/*  67 */   public static final String XMLNS_URI = "http://www.w3.org/2000/xmlns/".intern();
/*     */ 
/*     */   protected NameImpl(String name) {
/*  70 */     this.localName = (name == null ? "" : name);
/*     */   }
/*     */ 
/*     */   protected NameImpl(String name, String prefix, String uri) {
/*  74 */     this.uri = (uri == null ? "" : uri);
/*  75 */     this.localName = (name == null ? "" : name);
/*  76 */     this.prefix = (prefix == null ? "" : prefix);
/*     */ 
/*  78 */     if ((this.prefix.equals("xmlns")) && (this.uri.equals(""))) {
/*  79 */       this.uri = XMLNS_URI;
/*     */     }
/*  81 */     if ((this.uri.equals(XMLNS_URI)) && (this.prefix.equals("")))
/*  82 */       this.prefix = "xmlns";
/*     */   }
/*     */ 
/*     */   public static Name convertToName(QName qname)
/*     */   {
/*  87 */     return new NameImpl(qname.getLocalPart(), qname.getPrefix(), qname.getNamespaceURI());
/*     */   }
/*     */ 
/*     */   public static QName convertToQName(Name name)
/*     */   {
/*  93 */     return new QName(name.getURI(), name.getLocalName(), name.getPrefix());
/*     */   }
/*     */ 
/*     */   public static NameImpl createFromUnqualifiedName(String name)
/*     */   {
/*  99 */     return new NameImpl(name);
/*     */   }
/*     */ 
/*     */   public static Name createFromTagName(String tagName) {
/* 103 */     return createFromTagAndUri(tagName, "");
/*     */   }
/*     */ 
/*     */   public static Name createFromQualifiedName(String qualifiedName, String uri)
/*     */   {
/* 109 */     return createFromTagAndUri(qualifiedName, uri);
/*     */   }
/*     */ 
/*     */   protected static Name createFromTagAndUri(String tagName, String uri) {
/* 113 */     if (tagName == null) {
/* 114 */       log.severe("SAAJ0201.name.not.created.from.null.tag");
/* 115 */       throw new IllegalArgumentException("Cannot create a name from a null tag.");
/*     */     }
/* 117 */     int index = tagName.indexOf(':');
/* 118 */     if (index < 0) {
/* 119 */       return new NameImpl(tagName, "", uri);
/*     */     }
/* 121 */     return new NameImpl(tagName.substring(index + 1), tagName.substring(0, index), uri);
/*     */   }
/*     */ 
/*     */   protected static int getPrefixSeparatorIndex(String qualifiedName)
/*     */   {
/* 129 */     int index = qualifiedName.indexOf(':');
/* 130 */     if (index < 0) {
/* 131 */       log.log(Level.SEVERE, "SAAJ0202.name.invalid.arg.format", new String[] { qualifiedName });
/*     */ 
/* 135 */       throw new IllegalArgumentException("Argument \"" + qualifiedName + "\" must be of the form: \"prefix:localName\"");
/*     */     }
/*     */ 
/* 140 */     return index;
/*     */   }
/*     */ 
/*     */   public static String getPrefixFromQualifiedName(String qualifiedName) {
/* 144 */     return qualifiedName.substring(0, getPrefixSeparatorIndex(qualifiedName));
/*     */   }
/*     */ 
/*     */   public static String getLocalNameFromQualifiedName(String qualifiedName)
/*     */   {
/* 150 */     return qualifiedName.substring(getPrefixSeparatorIndex(qualifiedName) + 1);
/*     */   }
/*     */ 
/*     */   public static String getPrefixFromTagName(String tagName)
/*     */   {
/* 155 */     if (isQualified(tagName)) {
/* 156 */       return getPrefixFromQualifiedName(tagName);
/*     */     }
/* 158 */     return "";
/*     */   }
/*     */ 
/*     */   public static String getLocalNameFromTagName(String tagName) {
/* 162 */     if (isQualified(tagName)) {
/* 163 */       return getLocalNameFromQualifiedName(tagName);
/*     */     }
/* 165 */     return tagName;
/*     */   }
/*     */ 
/*     */   public static boolean isQualified(String tagName) {
/* 169 */     return tagName.indexOf(':') >= 0;
/*     */   }
/*     */ 
/*     */   public static NameImpl create(String name, String prefix, String uri) {
/* 173 */     if (prefix == null)
/* 174 */       prefix = "";
/* 175 */     if (uri == null)
/* 176 */       uri = "";
/* 177 */     if (name == null) {
/* 178 */       name = "";
/*     */     }
/* 180 */     if ((!uri.equals("")) && (!name.equals(""))) {
/* 181 */       if (uri.equals("http://schemas.xmlsoap.org/soap/envelope/")) {
/* 182 */         if (name.equalsIgnoreCase("Envelope"))
/* 183 */           return createEnvelope1_1Name(prefix);
/* 184 */         if (name.equalsIgnoreCase("Header"))
/* 185 */           return createHeader1_1Name(prefix);
/* 186 */         if (name.equalsIgnoreCase("Body"))
/* 187 */           return createBody1_1Name(prefix);
/* 188 */         if (name.equalsIgnoreCase("Fault")) {
/* 189 */           return createFault1_1Name(prefix);
/*     */         }
/* 191 */         return new SOAP1_1Name(name, prefix);
/* 192 */       }if (uri.equals("http://www.w3.org/2003/05/soap-envelope")) {
/* 193 */         if (name.equalsIgnoreCase("Envelope"))
/* 194 */           return createEnvelope1_2Name(prefix);
/* 195 */         if (name.equalsIgnoreCase("Header"))
/* 196 */           return createHeader1_2Name(prefix);
/* 197 */         if (name.equalsIgnoreCase("Body"))
/* 198 */           return createBody1_2Name(prefix);
/* 199 */         if ((name.equals("Fault")) || (name.equals("Reason")) || (name.equals("Detail")))
/*     */         {
/* 203 */           return createFault1_2Name(name, prefix);
/* 204 */         }if ((name.equals("Code")) || (name.equals("Subcode"))) {
/* 205 */           return createCodeSubcode1_2Name(prefix, name);
/*     */         }
/* 207 */         return new SOAP1_2Name(name, prefix);
/*     */       }
/*     */     }
/*     */ 
/* 211 */     return new NameImpl(name, prefix, uri);
/*     */   }
/*     */ 
/*     */   public static String createQName(String prefix, String localName) {
/* 215 */     if ((prefix == null) || (prefix.equals(""))) {
/* 216 */       return localName;
/*     */     }
/* 218 */     return prefix + ":" + localName;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object obj) {
/* 222 */     if (!(obj instanceof Name)) {
/* 223 */       return false;
/*     */     }
/*     */ 
/* 226 */     Name otherName = (Name)obj;
/*     */ 
/* 228 */     if (!this.uri.equals(otherName.getURI())) {
/* 229 */       return false;
/*     */     }
/*     */ 
/* 232 */     if (!this.localName.equals(otherName.getLocalName())) {
/* 233 */       return false;
/*     */     }
/*     */ 
/* 236 */     return true;
/*     */   }
/*     */ 
/*     */   public String getLocalName()
/*     */   {
/* 245 */     return this.localName;
/*     */   }
/*     */ 
/*     */   public String getPrefix()
/*     */   {
/* 256 */     return this.prefix;
/*     */   }
/*     */ 
/*     */   public String getURI()
/*     */   {
/* 265 */     return this.uri;
/*     */   }
/*     */ 
/*     */   public String getQualifiedName()
/*     */   {
/* 272 */     if (this.qualifiedName == null) {
/* 273 */       if ((this.prefix != null) && (this.prefix.length() > 0))
/* 274 */         this.qualifiedName = (this.prefix + ":" + this.localName);
/*     */       else {
/* 276 */         this.qualifiedName = this.localName;
/*     */       }
/*     */     }
/* 279 */     return this.qualifiedName;
/*     */   }
/*     */ 
/*     */   public static NameImpl createEnvelope1_1Name(String prefix)
/*     */   {
/* 286 */     return new Envelope1_1Name(prefix);
/*     */   }
/*     */ 
/*     */   public static NameImpl createEnvelope1_2Name(String prefix)
/*     */   {
/* 293 */     return new Envelope1_2Name(prefix);
/*     */   }
/*     */ 
/*     */   public static NameImpl createHeader1_1Name(String prefix)
/*     */   {
/* 300 */     return new Header1_1Name(prefix);
/*     */   }
/*     */ 
/*     */   public static NameImpl createHeader1_2Name(String prefix)
/*     */   {
/* 307 */     return new Header1_2Name(prefix);
/*     */   }
/*     */ 
/*     */   public static NameImpl createBody1_1Name(String prefix)
/*     */   {
/* 314 */     return new Body1_1Name(prefix);
/*     */   }
/*     */ 
/*     */   public static NameImpl createBody1_2Name(String prefix)
/*     */   {
/* 321 */     return new Body1_2Name(prefix);
/*     */   }
/*     */ 
/*     */   public static NameImpl createFault1_1Name(String prefix)
/*     */   {
/* 328 */     return new Fault1_1Name(prefix);
/*     */   }
/*     */ 
/*     */   public static NameImpl createNotUnderstood1_2Name(String prefix)
/*     */   {
/* 335 */     return new NotUnderstood1_2Name(prefix);
/*     */   }
/*     */ 
/*     */   public static NameImpl createUpgrade1_2Name(String prefix)
/*     */   {
/* 342 */     return new Upgrade1_2Name(prefix);
/*     */   }
/*     */ 
/*     */   public static NameImpl createSupportedEnvelope1_2Name(String prefix)
/*     */   {
/* 349 */     return new SupportedEnvelope1_2Name(prefix);
/*     */   }
/*     */ 
/*     */   public static NameImpl createFault1_2Name(String localName, String prefix)
/*     */   {
/* 361 */     return new Fault1_2Name(localName, prefix);
/*     */   }
/*     */ 
/*     */   public static NameImpl createCodeSubcode1_2Name(String prefix, String localName)
/*     */   {
/* 372 */     return new CodeSubcode1_2Name(localName, prefix);
/*     */   }
/*     */ 
/*     */   public static NameImpl createDetail1_1Name()
/*     */   {
/* 379 */     return new Detail1_1Name();
/*     */   }
/*     */ 
/*     */   public static NameImpl createDetail1_1Name(String prefix) {
/* 383 */     return new Detail1_1Name(prefix);
/*     */   }
/*     */ 
/*     */   public static NameImpl createFaultElement1_1Name(String localName) {
/* 387 */     return new FaultElement1_1Name(localName);
/*     */   }
/*     */ 
/*     */   public static NameImpl createFaultElement1_1Name(String localName, String prefix)
/*     */   {
/* 392 */     return new FaultElement1_1Name(localName, prefix);
/*     */   }
/*     */ 
/*     */   public static NameImpl createSOAP11Name(String string) {
/* 396 */     return new SOAP1_1Name(string, null);
/*     */   }
/*     */   public static NameImpl createSOAP12Name(String string) {
/* 399 */     return new SOAP1_2Name(string, null);
/*     */   }
/*     */ 
/*     */   public static NameImpl createSOAP12Name(String localName, String prefix) {
/* 403 */     return new SOAP1_2Name(localName, prefix);
/*     */   }
/*     */ 
/*     */   public static NameImpl createXmlName(String localName) {
/* 407 */     return new NameImpl(localName, "xml", "http://www.w3.org/XML/1998/namespace");
/*     */   }
/*     */ 
/*     */   public static Name copyElementName(Element element) {
/* 411 */     String localName = element.getLocalName();
/* 412 */     String prefix = element.getPrefix();
/* 413 */     String uri = element.getNamespaceURI();
/* 414 */     return create(localName, prefix, uri);
/*     */   }
/*     */ 
/*     */   static class Body1_1Name extends NameImpl.SOAP1_1Name
/*     */   {
/*     */     Body1_1Name(String prefix)
/*     */     {
/* 443 */       super(prefix);
/*     */     }
/*     */   }
/*     */ 
/*     */   static class Body1_2Name extends NameImpl.SOAP1_2Name
/*     */   {
/*     */     Body1_2Name(String prefix)
/*     */     {
/* 503 */       super(prefix);
/*     */     }
/*     */   }
/*     */ 
/*     */   static class CodeSubcode1_2Name extends NameImpl.SOAP1_2Name
/*     */   {
/*     */     CodeSubcode1_2Name(String prefix, String localName)
/*     */     {
/* 553 */       super(localName);
/*     */     }
/*     */   }
/*     */ 
/*     */   static class Detail1_1Name extends NameImpl
/*     */   {
/*     */     Detail1_1Name()
/*     */     {
/* 460 */       super();
/*     */     }
/*     */ 
/*     */     Detail1_1Name(String prefix) {
/* 464 */       super(prefix, "");
/*     */     }
/*     */   }
/*     */ 
/*     */   static class Envelope1_1Name extends NameImpl.SOAP1_1Name
/*     */   {
/*     */     Envelope1_1Name(String prefix)
/*     */     {
/* 431 */       super(prefix);
/*     */     }
/*     */   }
/*     */ 
/*     */   static class Envelope1_2Name extends NameImpl.SOAP1_2Name
/*     */   {
/*     */     Envelope1_2Name(String prefix)
/*     */     {
/* 491 */       super(prefix);
/*     */     }
/*     */   }
/*     */ 
/*     */   static class Fault1_1Name extends NameImpl
/*     */   {
/*     */     Fault1_1Name(String prefix)
/*     */     {
/* 449 */       super((prefix == null) || (prefix.equals("")) ? "SOAP-ENV" : prefix, "http://schemas.xmlsoap.org/soap/envelope/");
/*     */     }
/*     */   }
/*     */ 
/*     */   static class Fault1_2Name extends NameImpl
/*     */   {
/*     */     Fault1_2Name(String name, String prefix)
/*     */     {
/* 509 */       super((prefix == null) || (prefix.equals("")) ? "env" : prefix, "http://www.w3.org/2003/05/soap-envelope");
/*     */     }
/*     */   }
/*     */ 
/*     */   static class FaultElement1_1Name extends NameImpl
/*     */   {
/*     */     FaultElement1_1Name(String localName)
/*     */     {
/* 470 */       super();
/*     */     }
/*     */ 
/*     */     FaultElement1_1Name(String localName, String prefix) {
/* 474 */       super(prefix, "");
/*     */     }
/*     */   }
/*     */ 
/*     */   static class Header1_1Name extends NameImpl.SOAP1_1Name
/*     */   {
/*     */     Header1_1Name(String prefix)
/*     */     {
/* 437 */       super(prefix);
/*     */     }
/*     */   }
/*     */ 
/*     */   static class Header1_2Name extends NameImpl.SOAP1_2Name
/*     */   {
/*     */     Header1_2Name(String prefix)
/*     */     {
/* 497 */       super(prefix);
/*     */     }
/*     */   }
/*     */ 
/*     */   static class NotUnderstood1_2Name extends NameImpl
/*     */   {
/*     */     NotUnderstood1_2Name(String prefix)
/*     */     {
/* 520 */       super((prefix == null) || (prefix.equals("")) ? "env" : prefix, "http://www.w3.org/2003/05/soap-envelope");
/*     */     }
/*     */   }
/*     */ 
/*     */   static class SOAP1_1Name extends NameImpl
/*     */   {
/*     */     SOAP1_1Name(String name, String prefix)
/*     */     {
/* 420 */       super((prefix == null) || (prefix.equals("")) ? "SOAP-ENV" : prefix, "http://schemas.xmlsoap.org/soap/envelope/");
/*     */     }
/*     */   }
/*     */ 
/*     */   static class SOAP1_2Name extends NameImpl
/*     */   {
/*     */     SOAP1_2Name(String name, String prefix)
/*     */     {
/* 480 */       super((prefix == null) || (prefix.equals("")) ? "env" : prefix, "http://www.w3.org/2003/05/soap-envelope");
/*     */     }
/*     */   }
/*     */ 
/*     */   static class SupportedEnvelope1_2Name extends NameImpl
/*     */   {
/*     */     SupportedEnvelope1_2Name(String prefix)
/*     */     {
/* 542 */       super((prefix == null) || (prefix.equals("")) ? "env" : prefix, "http://www.w3.org/2003/05/soap-envelope");
/*     */     }
/*     */   }
/*     */ 
/*     */   static class Upgrade1_2Name extends NameImpl
/*     */   {
/*     */     Upgrade1_2Name(String prefix)
/*     */     {
/* 531 */       super((prefix == null) || (prefix.equals("")) ? "env" : prefix, "http://www.w3.org/2003/05/soap-envelope");
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.messaging.saaj.soap.name.NameImpl
 * JD-Core Version:    0.6.2
 */