/*     */ package com.sun.xml.internal.ws.api.config.management.policy;
/*     */ 
/*     */ import com.sun.istack.internal.logging.Logger;
/*     */ import com.sun.xml.internal.ws.api.server.WSEndpoint;
/*     */ import com.sun.xml.internal.ws.policy.PolicyAssertion;
/*     */ import com.sun.xml.internal.ws.policy.PolicyMap;
/*     */ import com.sun.xml.internal.ws.policy.sourcemodel.AssertionData;
/*     */ import com.sun.xml.internal.ws.policy.spi.AssertionCreationException;
/*     */ import com.sun.xml.internal.ws.resources.ManagementMessages;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.Map;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.ws.WebServiceException;
/*     */ 
/*     */ public class ManagedServiceAssertion extends ManagementAssertion
/*     */ {
/*  52 */   public static final QName MANAGED_SERVICE_QNAME = new QName("http://java.sun.com/xml/ns/metro/management", "ManagedService");
/*     */ 
/*  55 */   private static final QName COMMUNICATION_SERVER_IMPLEMENTATIONS_PARAMETER_QNAME = new QName("http://java.sun.com/xml/ns/metro/management", "CommunicationServerImplementations");
/*     */ 
/*  57 */   private static final QName COMMUNICATION_SERVER_IMPLEMENTATION_PARAMETER_QNAME = new QName("http://java.sun.com/xml/ns/metro/management", "CommunicationServerImplementation");
/*     */ 
/*  59 */   private static final QName CONFIGURATOR_IMPLEMENTATION_PARAMETER_QNAME = new QName("http://java.sun.com/xml/ns/metro/management", "ConfiguratorImplementation");
/*     */ 
/*  61 */   private static final QName CONFIG_SAVER_IMPLEMENTATION_PARAMETER_QNAME = new QName("http://java.sun.com/xml/ns/metro/management", "ConfigSaverImplementation");
/*     */ 
/*  63 */   private static final QName CONFIG_READER_IMPLEMENTATION_PARAMETER_QNAME = new QName("http://java.sun.com/xml/ns/metro/management", "ConfigReaderImplementation");
/*     */ 
/*  65 */   private static final QName CLASS_NAME_ATTRIBUTE_QNAME = new QName("className");
/*     */ 
/*  69 */   private static final QName ENDPOINT_DISPOSE_DELAY_ATTRIBUTE_QNAME = new QName("endpointDisposeDelay");
/*     */ 
/*  71 */   private static final Logger LOGGER = Logger.getLogger(ManagedServiceAssertion.class);
/*     */ 
/*     */   public static ManagedServiceAssertion getAssertion(WSEndpoint endpoint)
/*     */     throws WebServiceException
/*     */   {
/*  81 */     LOGGER.entering(new Object[] { endpoint });
/*     */ 
/*  85 */     PolicyMap policyMap = endpoint.getPolicyMap();
/*  86 */     ManagedServiceAssertion assertion = (ManagedServiceAssertion)ManagementAssertion.getAssertion(MANAGED_SERVICE_QNAME, policyMap, endpoint.getServiceName(), endpoint.getPortName(), ManagedServiceAssertion.class);
/*     */ 
/*  88 */     LOGGER.exiting(assertion);
/*  89 */     return assertion;
/*     */   }
/*     */ 
/*     */   public ManagedServiceAssertion(AssertionData data, Collection<PolicyAssertion> assertionParameters) throws AssertionCreationException
/*     */   {
/*  94 */     super(MANAGED_SERVICE_QNAME, data, assertionParameters);
/*     */   }
/*     */ 
/*     */   public boolean isManagementEnabled()
/*     */   {
/* 104 */     String management = getAttributeValue(MANAGEMENT_ATTRIBUTE_QNAME);
/* 105 */     boolean result = true;
/* 106 */     if (management != null) {
/* 107 */       if (management.trim().toLowerCase().equals("on")) {
/* 108 */         result = true;
/*     */       }
/*     */       else {
/* 111 */         result = Boolean.parseBoolean(management);
/*     */       }
/*     */     }
/* 114 */     return result;
/*     */   }
/*     */ 
/*     */   public long getEndpointDisposeDelay(long defaultDelay)
/*     */     throws WebServiceException
/*     */   {
/* 127 */     long result = defaultDelay;
/* 128 */     String delayText = getAttributeValue(ENDPOINT_DISPOSE_DELAY_ATTRIBUTE_QNAME);
/* 129 */     if (delayText != null) {
/*     */       try {
/* 131 */         result = Long.parseLong(delayText);
/*     */       } catch (NumberFormatException e) {
/* 133 */         throw ((WebServiceException)LOGGER.logSevereException(new WebServiceException(ManagementMessages.WSM_1008_EXPECTED_INTEGER_DISPOSE_DELAY_VALUE(delayText), e)));
/*     */       }
/*     */     }
/*     */ 
/* 137 */     return result;
/*     */   }
/*     */ 
/*     */   public Collection<ImplementationRecord> getCommunicationServerImplementations()
/*     */   {
/* 148 */     Collection result = new LinkedList();
/* 149 */     Iterator parameters = getParametersIterator();
/* 150 */     while (parameters.hasNext()) {
/* 151 */       PolicyAssertion parameter = (PolicyAssertion)parameters.next();
/* 152 */       if (COMMUNICATION_SERVER_IMPLEMENTATIONS_PARAMETER_QNAME.equals(parameter.getName())) {
/* 153 */         Iterator implementations = parameter.getParametersIterator();
/* 154 */         if (!implementations.hasNext()) {
/* 155 */           throw ((WebServiceException)LOGGER.logSevereException(new WebServiceException(ManagementMessages.WSM_1005_EXPECTED_COMMUNICATION_CHILD())));
/*     */         }
/*     */ 
/* 158 */         while (implementations.hasNext()) {
/* 159 */           PolicyAssertion implementation = (PolicyAssertion)implementations.next();
/* 160 */           if (COMMUNICATION_SERVER_IMPLEMENTATION_PARAMETER_QNAME.equals(implementation.getName())) {
/* 161 */             result.add(getImplementation(implementation));
/*     */           }
/*     */           else {
/* 164 */             throw ((WebServiceException)LOGGER.logSevereException(new WebServiceException(ManagementMessages.WSM_1004_EXPECTED_XML_TAG(COMMUNICATION_SERVER_IMPLEMENTATION_PARAMETER_QNAME, implementation.getName()))));
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 171 */     return result;
/*     */   }
/*     */ 
/*     */   public ImplementationRecord getConfiguratorImplementation()
/*     */   {
/* 181 */     return findImplementation(CONFIGURATOR_IMPLEMENTATION_PARAMETER_QNAME);
/*     */   }
/*     */ 
/*     */   public ImplementationRecord getConfigSaverImplementation()
/*     */   {
/* 191 */     return findImplementation(CONFIG_SAVER_IMPLEMENTATION_PARAMETER_QNAME);
/*     */   }
/*     */ 
/*     */   public ImplementationRecord getConfigReaderImplementation()
/*     */   {
/* 201 */     return findImplementation(CONFIG_READER_IMPLEMENTATION_PARAMETER_QNAME);
/*     */   }
/*     */ 
/*     */   private ImplementationRecord findImplementation(QName implementationName) {
/* 205 */     Iterator parameters = getParametersIterator();
/* 206 */     while (parameters.hasNext()) {
/* 207 */       PolicyAssertion parameter = (PolicyAssertion)parameters.next();
/* 208 */       if (implementationName.equals(parameter.getName())) {
/* 209 */         return getImplementation(parameter);
/*     */       }
/*     */     }
/* 212 */     return null;
/*     */   }
/*     */ 
/*     */   private ImplementationRecord getImplementation(PolicyAssertion rootParameter) {
/* 216 */     String className = rootParameter.getAttributeValue(CLASS_NAME_ATTRIBUTE_QNAME);
/* 217 */     HashMap parameterMap = new HashMap();
/* 218 */     Iterator implementationParameters = rootParameter.getParametersIterator();
/* 219 */     Collection nestedParameters = new LinkedList();
/* 220 */     while (implementationParameters.hasNext()) {
/* 221 */       PolicyAssertion parameterAssertion = (PolicyAssertion)implementationParameters.next();
/* 222 */       QName parameterName = parameterAssertion.getName();
/* 223 */       if (parameterAssertion.hasParameters()) {
/* 224 */         Map nestedParameterMap = new HashMap();
/* 225 */         Iterator parameters = parameterAssertion.getParametersIterator();
/* 226 */         while (parameters.hasNext()) {
/* 227 */           PolicyAssertion parameter = (PolicyAssertion)parameters.next();
/* 228 */           String value = parameter.getValue();
/* 229 */           if (value != null) {
/* 230 */             value = value.trim();
/*     */           }
/* 232 */           nestedParameterMap.put(parameter.getName(), value);
/*     */         }
/* 234 */         nestedParameters.add(new NestedParameters(parameterName, nestedParameterMap, null));
/*     */       }
/*     */       else {
/* 237 */         String value = parameterAssertion.getValue();
/* 238 */         if (value != null) {
/* 239 */           value = value.trim();
/*     */         }
/* 241 */         parameterMap.put(parameterName, value);
/*     */       }
/*     */     }
/* 244 */     return new ImplementationRecord(className, parameterMap, nestedParameters);
/*     */   }
/*     */ 
/*     */   public static class ImplementationRecord
/*     */   {
/*     */     private final String implementation;
/*     */     private final Map<QName, String> parameters;
/*     */     private final Collection<ManagedServiceAssertion.NestedParameters> nestedParameters;
/*     */ 
/*     */     protected ImplementationRecord(String implementation, Map<QName, String> parameters, Collection<ManagedServiceAssertion.NestedParameters> nestedParameters)
/*     */     {
/* 260 */       this.implementation = implementation;
/* 261 */       this.parameters = parameters;
/* 262 */       this.nestedParameters = nestedParameters;
/*     */     }
/*     */ 
/*     */     public String getImplementation() {
/* 266 */       return this.implementation;
/*     */     }
/*     */ 
/*     */     public Map<QName, String> getParameters()
/*     */     {
/* 276 */       return this.parameters;
/*     */     }
/*     */ 
/*     */     public Collection<ManagedServiceAssertion.NestedParameters> getNestedParameters()
/*     */     {
/* 287 */       return this.nestedParameters;
/*     */     }
/*     */ 
/*     */     public boolean equals(Object obj)
/*     */     {
/* 292 */       if (obj == null) {
/* 293 */         return false;
/*     */       }
/* 295 */       if (getClass() != obj.getClass()) {
/* 296 */         return false;
/*     */       }
/* 298 */       ImplementationRecord other = (ImplementationRecord)obj;
/* 299 */       if (this.implementation == null ? other.implementation != null : !this.implementation.equals(other.implementation))
/*     */       {
/* 301 */         return false;
/*     */       }
/* 303 */       if ((this.parameters != other.parameters) && ((this.parameters == null) || (!this.parameters.equals(other.parameters)))) {
/* 304 */         return false;
/*     */       }
/* 306 */       if ((this.nestedParameters != other.nestedParameters) && ((this.nestedParameters == null) || (!this.nestedParameters.equals(other.nestedParameters))))
/*     */       {
/* 308 */         return false;
/*     */       }
/* 310 */       return true;
/*     */     }
/*     */ 
/*     */     public int hashCode()
/*     */     {
/* 315 */       int hash = 3;
/* 316 */       hash = 53 * hash + (this.implementation != null ? this.implementation.hashCode() : 0);
/* 317 */       hash = 53 * hash + (this.parameters != null ? this.parameters.hashCode() : 0);
/* 318 */       hash = 53 * hash + (this.nestedParameters != null ? this.nestedParameters.hashCode() : 0);
/* 319 */       return hash;
/*     */     }
/*     */ 
/*     */     public String toString()
/*     */     {
/* 324 */       StringBuilder text = new StringBuilder("ImplementationRecord: ");
/* 325 */       text.append("implementation = \"").append(this.implementation).append("\", ");
/* 326 */       text.append("parameters = \"").append(this.parameters).append("\", ");
/* 327 */       text.append("nested parameters = \"").append(this.nestedParameters).append("\"");
/* 328 */       return text.toString();
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class NestedParameters
/*     */   {
/*     */     private final QName name;
/*     */     private final Map<QName, String> parameters;
/*     */ 
/*     */     private NestedParameters(QName name, Map<QName, String> parameters)
/*     */     {
/* 343 */       this.name = name;
/* 344 */       this.parameters = parameters;
/*     */     }
/*     */ 
/*     */     public QName getName() {
/* 348 */       return this.name;
/*     */     }
/*     */ 
/*     */     public Map<QName, String> getParameters() {
/* 352 */       return this.parameters;
/*     */     }
/*     */ 
/*     */     public boolean equals(Object obj)
/*     */     {
/* 357 */       if (obj == null) {
/* 358 */         return false;
/*     */       }
/* 360 */       if (getClass() != obj.getClass()) {
/* 361 */         return false;
/*     */       }
/* 363 */       NestedParameters other = (NestedParameters)obj;
/* 364 */       if (this.name == null ? other.name != null : !this.name.equals(other.name)) {
/* 365 */         return false;
/*     */       }
/* 367 */       if ((this.parameters != other.parameters) && ((this.parameters == null) || (!this.parameters.equals(other.parameters)))) {
/* 368 */         return false;
/*     */       }
/* 370 */       return true;
/*     */     }
/*     */ 
/*     */     public int hashCode()
/*     */     {
/* 375 */       int hash = 5;
/* 376 */       hash = 59 * hash + (this.name != null ? this.name.hashCode() : 0);
/* 377 */       hash = 59 * hash + (this.parameters != null ? this.parameters.hashCode() : 0);
/* 378 */       return hash;
/*     */     }
/*     */ 
/*     */     public String toString()
/*     */     {
/* 383 */       StringBuilder text = new StringBuilder("NestedParameters: ");
/* 384 */       text.append("name = \"").append(this.name).append("\", ");
/* 385 */       text.append("parameters = \"").append(this.parameters).append("\"");
/* 386 */       return text.toString();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.api.config.management.policy.ManagedServiceAssertion
 * JD-Core Version:    0.6.2
 */