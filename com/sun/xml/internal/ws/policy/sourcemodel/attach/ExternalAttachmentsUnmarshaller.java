/*     */ package com.sun.xml.internal.ws.policy.sourcemodel.attach;
/*     */ 
/*     */ import com.sun.xml.internal.ws.policy.Policy;
/*     */ import com.sun.xml.internal.ws.policy.PolicyException;
/*     */ import com.sun.xml.internal.ws.policy.privateutil.LocalizationMessages;
/*     */ import com.sun.xml.internal.ws.policy.privateutil.PolicyLogger;
/*     */ import com.sun.xml.internal.ws.policy.sourcemodel.PolicyModelTranslator;
/*     */ import com.sun.xml.internal.ws.policy.sourcemodel.PolicyModelUnmarshaller;
/*     */ import com.sun.xml.internal.ws.policy.sourcemodel.PolicySourceModel;
/*     */ import java.io.Reader;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.stream.Location;
/*     */ import javax.xml.stream.XMLEventReader;
/*     */ import javax.xml.stream.XMLInputFactory;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.events.Characters;
/*     */ import javax.xml.stream.events.EndElement;
/*     */ import javax.xml.stream.events.StartElement;
/*     */ import javax.xml.stream.events.XMLEvent;
/*     */ 
/*     */ public class ExternalAttachmentsUnmarshaller
/*     */ {
/*  61 */   private static final PolicyLogger LOGGER = PolicyLogger.getLogger(ExternalAttachmentsUnmarshaller.class);
/*     */   public static final URI BINDING_ID;
/*     */   public static final URI BINDING_OPERATION_ID;
/*     */   public static final URI BINDING_OPERATION_INPUT_ID;
/*     */   public static final URI BINDING_OPERATION_OUTPUT_ID;
/*     */   public static final URI BINDING_OPERATION_FAULT_ID;
/*  81 */   private static final QName POLICY_ATTACHMENT = new QName("http://www.w3.org/ns/ws-policy", "PolicyAttachment");
/*  82 */   private static final QName APPLIES_TO = new QName("http://www.w3.org/ns/ws-policy", "AppliesTo");
/*  83 */   private static final QName POLICY = new QName("http://www.w3.org/ns/ws-policy", "Policy");
/*  84 */   private static final QName URI = new QName("http://www.w3.org/ns/ws-policy", "URI");
/*  85 */   private static final QName POLICIES = new QName("http://java.sun.com/xml/ns/metro/management", "Policies");
/*  86 */   private static final ContextClassloaderLocal<XMLInputFactory> XML_INPUT_FACTORY = new ContextClassloaderLocal()
/*     */   {
/*     */     protected XMLInputFactory initialValue() throws Exception {
/*  89 */       return XMLInputFactory.newInstance();
/*     */     }
/*  86 */   };
/*     */ 
/*  93 */   private static final PolicyModelUnmarshaller POLICY_UNMARSHALLER = PolicyModelUnmarshaller.getXmlUnmarshaller();
/*     */ 
/*  95 */   private final Map<URI, Policy> map = new HashMap();
/*  96 */   private URI currentUri = null;
/*  97 */   private Policy currentPolicy = null;
/*     */ 
/*     */   public static Map<URI, Policy> unmarshal(Reader source) throws PolicyException {
/* 100 */     LOGGER.entering(new Object[] { source });
/*     */     try {
/* 102 */       XMLEventReader reader = ((XMLInputFactory)XML_INPUT_FACTORY.get()).createXMLEventReader(source);
/* 103 */       ExternalAttachmentsUnmarshaller instance = new ExternalAttachmentsUnmarshaller();
/* 104 */       Map map = instance.unmarshal(reader, null);
/* 105 */       LOGGER.exiting(map);
/* 106 */       return Collections.unmodifiableMap(map);
/*     */     } catch (XMLStreamException ex) {
/* 108 */       throw ((PolicyException)LOGGER.logSevereException(new PolicyException(LocalizationMessages.WSP_0086_FAILED_CREATE_READER(source)), ex));
/*     */     }
/*     */   }
/*     */ 
/*     */   private Map<URI, Policy> unmarshal(XMLEventReader reader, StartElement parentElement) throws PolicyException {
/* 113 */     XMLEvent event = null;
/* 114 */     while (reader.hasNext()) {
/*     */       try {
/* 116 */         event = reader.peek();
/* 117 */         switch (event.getEventType()) {
/*     */         case 5:
/*     */         case 7:
/* 120 */           reader.nextEvent();
/* 121 */           break;
/*     */         case 4:
/* 124 */           processCharacters(event.asCharacters(), parentElement, this.map);
/* 125 */           reader.nextEvent();
/* 126 */           break;
/*     */         case 2:
/* 129 */           processEndTag(event.asEndElement(), parentElement);
/* 130 */           reader.nextEvent();
/* 131 */           return this.map;
/*     */         case 1:
/* 134 */           StartElement element = event.asStartElement();
/* 135 */           processStartTag(element, parentElement, reader, this.map);
/* 136 */           break;
/*     */         case 8:
/* 139 */           return this.map;
/*     */         case 3:
/*     */         case 6:
/*     */         default:
/* 142 */           throw ((PolicyException)LOGGER.logSevereException(new PolicyException(LocalizationMessages.WSP_0087_UNKNOWN_EVENT(event))));
/*     */         }
/*     */       } catch (XMLStreamException e) {
/* 145 */         Location location = event == null ? null : event.getLocation();
/* 146 */         throw ((PolicyException)LOGGER.logSevereException(new PolicyException(LocalizationMessages.WSP_0088_FAILED_PARSE(location)), e));
/*     */       }
/*     */     }
/* 149 */     return this.map;
/*     */   }
/*     */ 
/*     */   private void processStartTag(StartElement element, StartElement parent, XMLEventReader reader, Map<URI, Policy> map) throws PolicyException
/*     */   {
/*     */     try
/*     */     {
/* 156 */       QName name = element.getName();
/* 157 */       if (parent == null) {
/* 158 */         if (!name.equals(POLICIES))
/* 159 */           throw ((PolicyException)LOGGER.logSevereException(new PolicyException(LocalizationMessages.WSP_0089_EXPECTED_ELEMENT("<Policies>", name, element.getLocation()))));
/*     */       }
/*     */       else {
/* 162 */         QName parentName = parent.getName();
/* 163 */         if (parentName.equals(POLICIES)) {
/* 164 */           if (!name.equals(POLICY_ATTACHMENT))
/* 165 */             throw ((PolicyException)LOGGER.logSevereException(new PolicyException(LocalizationMessages.WSP_0089_EXPECTED_ELEMENT("<PolicyAttachment>", name, element.getLocation()))));
/*     */         }
/* 167 */         else if (parentName.equals(POLICY_ATTACHMENT)) {
/* 168 */           if (name.equals(POLICY)) {
/* 169 */             readPolicy(reader);
/* 170 */             return;
/* 171 */           }if (!name.equals(APPLIES_TO))
/* 172 */             throw ((PolicyException)LOGGER.logSevereException(new PolicyException(LocalizationMessages.WSP_0089_EXPECTED_ELEMENT("<AppliesTo> or <Policy>", name, element.getLocation()))));
/*     */         }
/* 174 */         else if (parentName.equals(APPLIES_TO)) {
/* 175 */           if (!name.equals(URI))
/* 176 */             throw ((PolicyException)LOGGER.logSevereException(new PolicyException(LocalizationMessages.WSP_0089_EXPECTED_ELEMENT("<URI>", name, element.getLocation()))));
/*     */         }
/*     */         else {
/* 179 */           throw ((PolicyException)LOGGER.logSevereException(new PolicyException(LocalizationMessages.WSP_0090_UNEXPECTED_ELEMENT(name, element.getLocation()))));
/*     */         }
/*     */       }
/* 182 */       reader.nextEvent();
/* 183 */       unmarshal(reader, element);
/*     */     } catch (XMLStreamException e) {
/* 185 */       throw ((PolicyException)LOGGER.logSevereException(new PolicyException(LocalizationMessages.WSP_0088_FAILED_PARSE(element.getLocation()), e)));
/*     */     }
/*     */   }
/*     */ 
/*     */   private void readPolicy(XMLEventReader reader) throws PolicyException {
/* 190 */     PolicySourceModel policyModel = POLICY_UNMARSHALLER.unmarshalModel(reader);
/* 191 */     PolicyModelTranslator translator = PolicyModelTranslator.getTranslator();
/* 192 */     Policy policy = translator.translate(policyModel);
/* 193 */     if (this.currentUri != null) {
/* 194 */       this.map.put(this.currentUri, policy);
/* 195 */       this.currentUri = null;
/* 196 */       this.currentPolicy = null;
/*     */     }
/*     */     else {
/* 199 */       this.currentPolicy = policy;
/*     */     }
/*     */   }
/*     */ 
/*     */   private void processEndTag(EndElement element, StartElement startElement) throws PolicyException {
/* 204 */     checkEndTagName(startElement.getName(), element);
/*     */   }
/*     */ 
/*     */   private void checkEndTagName(QName expectedName, EndElement element) throws PolicyException {
/* 208 */     QName actualName = element.getName();
/* 209 */     if (!expectedName.equals(actualName))
/* 210 */       throw ((PolicyException)LOGGER.logSevereException(new PolicyException(LocalizationMessages.WSP_0091_END_ELEMENT_NO_MATCH(expectedName, element, element.getLocation()))));
/*     */   }
/*     */ 
/*     */   private void processCharacters(Characters chars, StartElement currentElement, Map<URI, Policy> map)
/*     */     throws PolicyException
/*     */   {
/* 217 */     if (chars.isWhiteSpace()) {
/* 218 */       return;
/*     */     }
/*     */ 
/* 221 */     String data = chars.getData();
/* 222 */     if ((currentElement != null) && (URI.equals(currentElement.getName()))) {
/* 223 */       processUri(chars, map);
/* 224 */       return;
/*     */     }
/* 226 */     throw ((PolicyException)LOGGER.logSevereException(new PolicyException(LocalizationMessages.WSP_0092_CHARACTER_DATA_UNEXPECTED(currentElement, data, chars.getLocation()))));
/*     */   }
/*     */ 
/*     */   private void processUri(Characters chars, Map<URI, Policy> map)
/*     */     throws PolicyException
/*     */   {
/* 233 */     String data = chars.getData().trim();
/*     */     try {
/* 235 */       URI uri = new URI(data);
/* 236 */       if (this.currentPolicy != null) {
/* 237 */         map.put(uri, this.currentPolicy);
/* 238 */         this.currentUri = null;
/* 239 */         this.currentPolicy = null;
/*     */       } else {
/* 241 */         this.currentUri = uri;
/*     */       }
/*     */     } catch (URISyntaxException e) {
/* 244 */       throw ((PolicyException)LOGGER.logSevereException(new PolicyException(LocalizationMessages.WSP_0093_INVALID_URI(data, chars.getLocation())), e));
/*     */     }
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*     */     try
/*     */     {
/*  71 */       BINDING_ID = new URI("urn:uuid:c9bef600-0d7a-11de-abc1-0002a5d5c51b");
/*  72 */       BINDING_OPERATION_ID = new URI("urn:uuid:62e66b60-0d7b-11de-a1a2-0002a5d5c51b");
/*  73 */       BINDING_OPERATION_INPUT_ID = new URI("urn:uuid:730d8d20-0d7b-11de-84e9-0002a5d5c51b");
/*  74 */       BINDING_OPERATION_OUTPUT_ID = new URI("urn:uuid:85b0f980-0d7b-11de-8e9d-0002a5d5c51b");
/*  75 */       BINDING_OPERATION_FAULT_ID = new URI("urn:uuid:917cb060-0d7b-11de-9e80-0002a5d5c51b");
/*     */     } catch (URISyntaxException e) {
/*  77 */       throw ((IllegalArgumentException)LOGGER.logSevereException(new IllegalArgumentException(LocalizationMessages.WSP_0094_INVALID_URN()), e));
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.policy.sourcemodel.attach.ExternalAttachmentsUnmarshaller
 * JD-Core Version:    0.6.2
 */