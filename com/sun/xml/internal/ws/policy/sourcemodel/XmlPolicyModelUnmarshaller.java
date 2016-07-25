/*     */ package com.sun.xml.internal.ws.policy.sourcemodel;
/*     */ 
/*     */ import com.sun.xml.internal.ws.policy.PolicyConstants;
/*     */ import com.sun.xml.internal.ws.policy.PolicyException;
/*     */ import com.sun.xml.internal.ws.policy.privateutil.LocalizationMessages;
/*     */ import com.sun.xml.internal.ws.policy.privateutil.PolicyLogger;
/*     */ import com.sun.xml.internal.ws.policy.sourcemodel.wspolicy.NamespaceVersion;
/*     */ import com.sun.xml.internal.ws.policy.sourcemodel.wspolicy.XmlToken;
/*     */ import java.io.Reader;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.stream.XMLEventReader;
/*     */ import javax.xml.stream.XMLInputFactory;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.events.Attribute;
/*     */ import javax.xml.stream.events.Characters;
/*     */ import javax.xml.stream.events.EndElement;
/*     */ import javax.xml.stream.events.StartElement;
/*     */ import javax.xml.stream.events.XMLEvent;
/*     */ 
/*     */ public class XmlPolicyModelUnmarshaller extends PolicyModelUnmarshaller
/*     */ {
/*  60 */   private static final PolicyLogger LOGGER = PolicyLogger.getLogger(XmlPolicyModelUnmarshaller.class);
/*     */ 
/*     */   public PolicySourceModel unmarshalModel(Object storage)
/*     */     throws PolicyException
/*     */   {
/*  73 */     XMLEventReader reader = createXMLEventReader(storage);
/*  74 */     PolicySourceModel model = null;
/*     */ 
/*  77 */     while (reader.hasNext()) {
/*     */       try {
/*  79 */         XMLEvent event = reader.peek();
/*  80 */         switch (event.getEventType()) {
/*     */         case 5:
/*     */         case 7:
/*  83 */           reader.nextEvent();
/*  84 */           break;
/*     */         case 4:
/*  86 */           processCharacters(ModelNode.Type.POLICY, event.asCharacters(), null);
/*     */ 
/*  89 */           reader.nextEvent();
/*  90 */           break;
/*     */         case 1:
/*  92 */           if (NamespaceVersion.resolveAsToken(event.asStartElement().getName()) == XmlToken.Policy) {
/*  93 */             StartElement rootElement = reader.nextEvent().asStartElement();
/*     */ 
/*  95 */             model = initializeNewModel(rootElement);
/*  96 */             unmarshalNodeContent(model.getNamespaceVersion(), model.getRootNode(), rootElement.getName(), reader);
/*     */           }
/*     */           else
/*     */           {
/* 100 */             throw ((PolicyException)LOGGER.logSevereException(new PolicyException(LocalizationMessages.WSP_0048_POLICY_ELEMENT_EXPECTED_FIRST()))); } break;
/*     */         case 2:
/*     */         case 3:
/*     */         case 6:
/*     */         default:
/* 103 */           throw ((PolicyException)LOGGER.logSevereException(new PolicyException(LocalizationMessages.WSP_0048_POLICY_ELEMENT_EXPECTED_FIRST())));
/*     */         }
/*     */       } catch (XMLStreamException e) {
/* 106 */         throw ((PolicyException)LOGGER.logSevereException(new PolicyException(LocalizationMessages.WSP_0068_FAILED_TO_UNMARSHALL_POLICY_EXPRESSION(), e)));
/*     */       }
/*     */     }
/* 109 */     return model;
/*     */   }
/*     */ 
/*     */   protected PolicySourceModel createSourceModel(NamespaceVersion nsVersion, String id, String name)
/*     */   {
/* 121 */     return PolicySourceModel.createPolicySourceModel(nsVersion, id, name);
/*     */   }
/*     */ 
/*     */   private PolicySourceModel initializeNewModel(StartElement element)
/*     */     throws PolicyException, XMLStreamException
/*     */   {
/* 127 */     NamespaceVersion nsVersion = NamespaceVersion.resolveVersion(element.getName().getNamespaceURI());
/*     */ 
/* 129 */     Attribute policyName = getAttributeByName(element, nsVersion.asQName(XmlToken.Name));
/* 130 */     Attribute xmlId = getAttributeByName(element, PolicyConstants.XML_ID);
/* 131 */     Attribute policyId = getAttributeByName(element, PolicyConstants.WSU_ID);
/*     */ 
/* 133 */     if (policyId == null)
/* 134 */       policyId = xmlId;
/* 135 */     else if (xmlId != null) {
/* 136 */       throw ((PolicyException)LOGGER.logSevereException(new PolicyException(LocalizationMessages.WSP_0058_MULTIPLE_POLICY_IDS_NOT_ALLOWED())));
/*     */     }
/*     */ 
/* 139 */     PolicySourceModel model = createSourceModel(nsVersion, policyId == null ? null : policyId.getValue(), policyName == null ? null : policyName.getValue());
/*     */ 
/* 143 */     return model;
/*     */   }
/*     */ 
/*     */   private ModelNode addNewChildNode(NamespaceVersion nsVersion, ModelNode parentNode, StartElement childElement) throws PolicyException
/*     */   {
/* 148 */     QName childElementName = childElement.getName();
/*     */     ModelNode childNode;
/*     */     ModelNode childNode;
/* 149 */     if (parentNode.getType() == ModelNode.Type.ASSERTION_PARAMETER_NODE) {
/* 150 */       childNode = parentNode.createChildAssertionParameterNode();
/*     */     } else {
/* 152 */       XmlToken token = NamespaceVersion.resolveAsToken(childElementName);
/*     */       ModelNode childNode;
/* 154 */       switch (1.$SwitchMap$com$sun$xml$internal$ws$policy$sourcemodel$wspolicy$XmlToken[token.ordinal()]) {
/*     */       case 1:
/* 156 */         childNode = parentNode.createChildPolicyNode();
/* 157 */         break;
/*     */       case 2:
/* 159 */         childNode = parentNode.createChildAllNode();
/* 160 */         break;
/*     */       case 3:
/* 162 */         childNode = parentNode.createChildExactlyOneNode();
/* 163 */         break;
/*     */       case 4:
/* 165 */         Attribute uri = getAttributeByName(childElement, nsVersion.asQName(XmlToken.Uri));
/* 166 */         if (uri == null)
/* 167 */           throw ((PolicyException)LOGGER.logSevereException(new PolicyException(LocalizationMessages.WSP_0040_POLICY_REFERENCE_URI_ATTR_NOT_FOUND())));
/*     */         try
/*     */         {
/* 170 */           URI reference = new URI(uri.getValue());
/* 171 */           Attribute digest = getAttributeByName(childElement, nsVersion.asQName(XmlToken.Digest));
/*     */           PolicyReferenceData refData;
/*     */           PolicyReferenceData refData;
/* 173 */           if (digest == null) {
/* 174 */             refData = new PolicyReferenceData(reference);
/*     */           } else {
/* 176 */             Attribute digestAlgorithm = getAttributeByName(childElement, nsVersion.asQName(XmlToken.DigestAlgorithm));
/* 177 */             URI algorithmRef = null;
/* 178 */             if (digestAlgorithm != null) {
/* 179 */               algorithmRef = new URI(digestAlgorithm.getValue());
/*     */             }
/* 181 */             refData = new PolicyReferenceData(reference, digest.getValue(), algorithmRef);
/*     */           }
/* 183 */           childNode = parentNode.createChildPolicyReferenceNode(refData);
/*     */         } catch (URISyntaxException e) {
/* 185 */           throw ((PolicyException)LOGGER.logSevereException(new PolicyException(LocalizationMessages.WSP_0012_UNABLE_TO_UNMARSHALL_POLICY_MALFORMED_URI(), e)));
/*     */         }
/*     */ 
/*     */       default:
/* 190 */         if (parentNode.isDomainSpecific())
/* 191 */           childNode = parentNode.createChildAssertionParameterNode();
/*     */         else {
/* 193 */           childNode = parentNode.createChildAssertionNode();
/*     */         }
/*     */         break;
/*     */       }
/*     */     }
/* 198 */     return childNode;
/*     */   }
/*     */ 
/*     */   private void parseAssertionData(NamespaceVersion nsVersion, String value, ModelNode childNode, StartElement childElement) throws IllegalArgumentException, PolicyException
/*     */   {
/* 203 */     Map attributeMap = new HashMap();
/* 204 */     boolean optional = false;
/* 205 */     boolean ignorable = false;
/*     */ 
/* 207 */     Iterator iterator = childElement.getAttributes();
/* 208 */     while (iterator.hasNext()) {
/* 209 */       Attribute nextAttribute = (Attribute)iterator.next();
/* 210 */       QName name = nextAttribute.getName();
/* 211 */       if (attributeMap.containsKey(name)) {
/* 212 */         throw ((PolicyException)LOGGER.logSevereException(new PolicyException(LocalizationMessages.WSP_0059_MULTIPLE_ATTRS_WITH_SAME_NAME_DETECTED_FOR_ASSERTION(nextAttribute.getName(), childElement.getName()))));
/*     */       }
/* 214 */       if (nsVersion.asQName(XmlToken.Optional).equals(name))
/* 215 */         optional = parseBooleanValue(nextAttribute.getValue());
/* 216 */       else if (nsVersion.asQName(XmlToken.Ignorable).equals(name))
/* 217 */         ignorable = parseBooleanValue(nextAttribute.getValue());
/*     */       else {
/* 219 */         attributeMap.put(name, nextAttribute.getValue());
/*     */       }
/*     */     }
/*     */ 
/* 223 */     AssertionData nodeData = new AssertionData(childElement.getName(), value, attributeMap, childNode.getType(), optional, ignorable);
/*     */ 
/* 226 */     if (nodeData.containsAttribute(PolicyConstants.VISIBILITY_ATTRIBUTE)) {
/* 227 */       String visibilityValue = nodeData.getAttributeValue(PolicyConstants.VISIBILITY_ATTRIBUTE);
/* 228 */       if (!"private".equals(visibilityValue)) {
/* 229 */         throw ((PolicyException)LOGGER.logSevereException(new PolicyException(LocalizationMessages.WSP_0004_UNEXPECTED_VISIBILITY_ATTR_VALUE(visibilityValue))));
/*     */       }
/*     */     }
/*     */ 
/* 233 */     childNode.setOrReplaceNodeData(nodeData);
/*     */   }
/*     */ 
/*     */   private Attribute getAttributeByName(StartElement element, QName attributeName)
/*     */   {
/* 239 */     Attribute attribute = element.getAttributeByName(attributeName);
/*     */ 
/* 242 */     if (attribute == null) {
/* 243 */       String localAttributeName = attributeName.getLocalPart();
/* 244 */       Iterator iterator = element.getAttributes();
/* 245 */       while (iterator.hasNext()) {
/* 246 */         Attribute nextAttribute = (Attribute)iterator.next();
/* 247 */         QName aName = nextAttribute.getName();
/* 248 */         boolean attributeFoundByWorkaround = (aName.equals(attributeName)) || ((aName.getLocalPart().equals(localAttributeName)) && ((aName.getPrefix() == null) || ("".equals(aName.getPrefix()))));
/* 249 */         if (attributeFoundByWorkaround) {
/* 250 */           attribute = nextAttribute;
/* 251 */           break;
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 257 */     return attribute;
/*     */   }
/*     */ 
/*     */   private String unmarshalNodeContent(NamespaceVersion nsVersion, ModelNode node, QName nodeElementName, XMLEventReader reader) throws PolicyException {
/* 261 */     StringBuilder valueBuffer = null;
/*     */ 
/* 264 */     while (reader.hasNext()) {
/*     */       try {
/* 266 */         XMLEvent xmlParserEvent = reader.nextEvent();
/* 267 */         switch (xmlParserEvent.getEventType()) {
/*     */         case 5:
/* 269 */           break;
/*     */         case 4:
/* 271 */           valueBuffer = processCharacters(node.getType(), xmlParserEvent.asCharacters(), valueBuffer);
/* 272 */           break;
/*     */         case 2:
/* 274 */           checkEndTagName(nodeElementName, xmlParserEvent.asEndElement());
/* 275 */           break;
/*     */         case 1:
/* 277 */           StartElement childElement = xmlParserEvent.asStartElement();
/*     */ 
/* 279 */           ModelNode childNode = addNewChildNode(nsVersion, node, childElement);
/* 280 */           String value = unmarshalNodeContent(nsVersion, childNode, childElement.getName(), reader);
/*     */ 
/* 282 */           if (childNode.isDomainSpecific())
/* 283 */             parseAssertionData(nsVersion, value, childNode, childElement); break;
/*     */         case 3:
/*     */         default:
/* 287 */           throw ((PolicyException)LOGGER.logSevereException(new PolicyException(LocalizationMessages.WSP_0011_UNABLE_TO_UNMARSHALL_POLICY_XML_ELEM_EXPECTED())));
/*     */         }
/*     */       } catch (XMLStreamException e) {
/* 290 */         throw ((PolicyException)LOGGER.logSevereException(new PolicyException(LocalizationMessages.WSP_0068_FAILED_TO_UNMARSHALL_POLICY_EXPRESSION(), e)));
/*     */       }
/*     */     }
/*     */ 
/* 294 */     return valueBuffer == null ? null : valueBuffer.toString().trim();
/*     */   }
/*     */ 
/*     */   private XMLEventReader createXMLEventReader(Object storage)
/*     */     throws PolicyException
/*     */   {
/* 307 */     if ((storage instanceof XMLEventReader)) {
/* 308 */       return (XMLEventReader)storage;
/*     */     }
/* 310 */     if (!(storage instanceof Reader)) {
/* 311 */       throw ((PolicyException)LOGGER.logSevereException(new PolicyException(LocalizationMessages.WSP_0022_STORAGE_TYPE_NOT_SUPPORTED(storage.getClass().getName()))));
/*     */     }
/*     */     try
/*     */     {
/* 315 */       return XMLInputFactory.newInstance().createXMLEventReader((Reader)storage);
/*     */     } catch (XMLStreamException e) {
/* 317 */       throw ((PolicyException)LOGGER.logSevereException(new PolicyException(LocalizationMessages.WSP_0014_UNABLE_TO_INSTANTIATE_READER_FOR_STORAGE(), e)));
/*     */     }
/*     */   }
/*     */ 
/*     */   private void checkEndTagName(QName expected, EndElement element)
/*     */     throws PolicyException
/*     */   {
/* 331 */     QName actual = element.getName();
/* 332 */     if (!expected.equals(actual))
/* 333 */       throw ((PolicyException)LOGGER.logSevereException(new PolicyException(LocalizationMessages.WSP_0003_UNMARSHALLING_FAILED_END_TAG_DOES_NOT_MATCH(expected, actual))));
/*     */   }
/*     */ 
/*     */   private StringBuilder processCharacters(ModelNode.Type currentNodeType, Characters characters, StringBuilder currentValueBuffer)
/*     */     throws PolicyException
/*     */   {
/* 341 */     if (characters.isWhiteSpace()) {
/* 342 */       return currentValueBuffer;
/*     */     }
/* 344 */     StringBuilder buffer = currentValueBuffer == null ? new StringBuilder() : currentValueBuffer;
/* 345 */     String data = characters.getData();
/* 346 */     if ((currentNodeType == ModelNode.Type.ASSERTION) || (currentNodeType == ModelNode.Type.ASSERTION_PARAMETER_NODE)) {
/* 347 */       return buffer.append(data);
/*     */     }
/* 349 */     throw ((PolicyException)LOGGER.logSevereException(new PolicyException(LocalizationMessages.WSP_0009_UNEXPECTED_CDATA_ON_SOURCE_MODEL_NODE(currentNodeType, data))));
/*     */   }
/*     */ 
/*     */   private boolean parseBooleanValue(String value)
/*     */     throws PolicyException
/*     */   {
/* 365 */     if (("true".equals(value)) || ("1".equals(value))) {
/* 366 */       return true;
/*     */     }
/* 368 */     if (("false".equals(value)) || ("0".equals(value))) {
/* 369 */       return false;
/*     */     }
/* 371 */     throw ((PolicyException)LOGGER.logSevereException(new PolicyException(LocalizationMessages.WSP_0095_INVALID_BOOLEAN_VALUE(value))));
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.policy.sourcemodel.XmlPolicyModelUnmarshaller
 * JD-Core Version:    0.6.2
 */