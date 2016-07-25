/*     */ package com.sun.xml.internal.ws.server;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.org.glassfish.external.amx.AMXGlassfish;
/*     */ import com.sun.org.glassfish.gmbal.Description;
/*     */ import com.sun.org.glassfish.gmbal.InheritedAttributes;
/*     */ import com.sun.org.glassfish.gmbal.ManagedData;
/*     */ import com.sun.org.glassfish.gmbal.ManagedObjectManager;
/*     */ import com.sun.org.glassfish.gmbal.ManagedObjectManager.RegistrationDebugLevel;
/*     */ import com.sun.org.glassfish.gmbal.ManagedObjectManagerFactory;
/*     */ import com.sun.xml.internal.ws.api.EndpointAddress;
/*     */ import com.sun.xml.internal.ws.api.config.management.policy.ManagedClientAssertion;
/*     */ import com.sun.xml.internal.ws.api.config.management.policy.ManagedServiceAssertion;
/*     */ import com.sun.xml.internal.ws.api.config.management.policy.ManagementAssertion.Setting;
/*     */ import com.sun.xml.internal.ws.api.server.Container;
/*     */ import com.sun.xml.internal.ws.api.server.WSEndpoint;
/*     */ import com.sun.xml.internal.ws.client.RequestContext;
/*     */ import com.sun.xml.internal.ws.client.Stub;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.management.ObjectName;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.ws.WebServiceFeature;
/*     */ 
/*     */ public abstract class MonitorBase
/*     */ {
/*  70 */   private static final Logger logger = Logger.getLogger("com.sun.xml.internal.ws.monitoring");
/*     */ 
/* 310 */   private static ManagementAssertion.Setting clientMonitoring = ManagementAssertion.Setting.NOT_SET;
/* 311 */   private static ManagementAssertion.Setting endpointMonitoring = ManagementAssertion.Setting.NOT_SET;
/* 312 */   private static int typelibDebug = -1;
/* 313 */   private static String registrationDebug = "NONE";
/* 314 */   private static boolean runtimeDebug = false;
/* 315 */   private static int maxUniqueEndpointRootNameRetries = 100;
/*     */   private static final String monitorProperty = "com.sun.xml.internal.ws.monitoring.";
/*     */ 
/*     */   @NotNull
/*     */   public ManagedObjectManager createManagedObjectManager(WSEndpoint endpoint)
/*     */   {
/*  95 */     String rootName = endpoint.getServiceName().getLocalPart() + "-" + endpoint.getPortName().getLocalPart();
/*     */ 
/* 100 */     if (rootName.equals("-")) {
/* 101 */       rootName = "provider";
/*     */     }
/*     */ 
/* 105 */     String contextPath = getContextPath(endpoint);
/* 106 */     if (contextPath != null) {
/* 107 */       rootName = contextPath + "-" + rootName;
/*     */     }
/*     */ 
/* 110 */     ManagedServiceAssertion assertion = ManagedServiceAssertion.getAssertion(endpoint);
/*     */ 
/* 112 */     if (assertion != null) {
/* 113 */       String id = assertion.getId();
/* 114 */       if (id != null) {
/* 115 */         rootName = id;
/*     */       }
/* 117 */       if (assertion.monitoringAttribute() == ManagementAssertion.Setting.OFF) {
/* 118 */         return disabled("This endpoint", rootName);
/*     */       }
/*     */     }
/*     */ 
/* 122 */     if (endpointMonitoring.equals(ManagementAssertion.Setting.OFF)) {
/* 123 */       return disabled("Global endpoint", rootName);
/*     */     }
/* 125 */     return createMOMLoop(rootName, 0);
/*     */   }
/*     */ 
/*     */   private String getContextPath(WSEndpoint endpoint) {
/*     */     try {
/* 130 */       Container container = endpoint.getContainer();
/* 131 */       Method getSPI = container.getClass().getDeclaredMethod("getSPI", new Class[] { Class.class });
/*     */ 
/* 133 */       getSPI.setAccessible(true);
/* 134 */       Class servletContextClass = Class.forName("javax.servlet.ServletContext");
/*     */ 
/* 136 */       Object servletContext = getSPI.invoke(container, new Object[] { servletContextClass });
/*     */ 
/* 138 */       if (servletContext != null) {
/* 139 */         Method getContextPath = servletContextClass.getDeclaredMethod("getContextPath", new Class[0]);
/* 140 */         getContextPath.setAccessible(true);
/* 141 */         return (String)getContextPath.invoke(servletContext, new Object[0]);
/*     */       }
/* 143 */       return null;
/*     */     } catch (Throwable t) {
/* 145 */       logger.log(Level.FINEST, "getContextPath", t);
/*     */     }
/* 147 */     return null;
/*     */   }
/*     */ 
/*     */   @NotNull
/*     */   public ManagedObjectManager createManagedObjectManager(Stub stub)
/*     */   {
/* 164 */     String rootName = stub.requestContext.getEndpointAddress().toString();
/*     */ 
/* 166 */     ManagedClientAssertion assertion = ManagedClientAssertion.getAssertion(stub.getPortInfo());
/*     */ 
/* 168 */     if (assertion != null) {
/* 169 */       String id = assertion.getId();
/* 170 */       if (id != null) {
/* 171 */         rootName = id;
/*     */       }
/* 173 */       if (assertion.monitoringAttribute() == ManagementAssertion.Setting.OFF)
/* 174 */         return disabled("This client", rootName);
/* 175 */       if ((assertion.monitoringAttribute() == ManagementAssertion.Setting.ON) && (clientMonitoring != ManagementAssertion.Setting.OFF))
/*     */       {
/* 177 */         return createMOMLoop(rootName, 0);
/*     */       }
/*     */     }
/*     */ 
/* 181 */     if ((clientMonitoring == ManagementAssertion.Setting.NOT_SET) || (clientMonitoring == ManagementAssertion.Setting.OFF))
/*     */     {
/* 184 */       return disabled("Global client", rootName);
/*     */     }
/* 186 */     return createMOMLoop(rootName, 0);
/*     */   }
/*     */   @NotNull
/*     */   private ManagedObjectManager disabled(String x, String rootName) {
/* 190 */     String msg = x + " monitoring disabled. " + rootName + " will not be monitored";
/* 191 */     logger.log(Level.CONFIG, msg);
/* 192 */     return ManagedObjectManagerFactory.createNOOP();
/*     */   }
/*     */   @NotNull
/*     */   private ManagedObjectManager createMOMLoop(String rootName, int unique) {
/* 196 */     boolean isFederated = AMXGlassfish.getGlassfishVersion() != null;
/* 197 */     ManagedObjectManager mom = createMOM(isFederated);
/* 198 */     mom = initMOM(mom);
/* 199 */     mom = createRoot(mom, rootName, unique);
/* 200 */     return mom;
/*     */   }
/*     */   @NotNull
/*     */   private ManagedObjectManager createMOM(boolean isFederated) {
/*     */     try {
/* 205 */       return new RewritingMOM(isFederated ? ManagedObjectManagerFactory.createFederated(AMXGlassfish.DEFAULT.serverMon(AMXGlassfish.DEFAULT.dasName())) : ManagedObjectManagerFactory.createStandalone("com.sun.metro"));
/*     */     }
/*     */     catch (Throwable t)
/*     */     {
/* 211 */       if (isFederated) {
/* 212 */         logger.log(Level.CONFIG, "Problem while attempting to federate with GlassFish AMX monitoring.  Trying standalone.", t);
/* 213 */         return createMOM(false);
/*     */       }
/* 215 */       logger.log(Level.WARNING, "Ignoring exception - starting up without monitoring", t);
/* 216 */     }return ManagedObjectManagerFactory.createNOOP();
/*     */   }
/*     */ 
/*     */   @NotNull
/*     */   private ManagedObjectManager initMOM(ManagedObjectManager mom)
/*     */   {
/*     */     try {
/* 223 */       if (typelibDebug != -1) {
/* 224 */         mom.setTypelibDebug(typelibDebug);
/*     */       }
/* 226 */       if (registrationDebug.equals("FINE"))
/* 227 */         mom.setRegistrationDebug(ManagedObjectManager.RegistrationDebugLevel.FINE);
/* 228 */       else if (registrationDebug.equals("NORMAL"))
/* 229 */         mom.setRegistrationDebug(ManagedObjectManager.RegistrationDebugLevel.NORMAL);
/*     */       else {
/* 231 */         mom.setRegistrationDebug(ManagedObjectManager.RegistrationDebugLevel.NONE);
/*     */       }
/*     */ 
/* 234 */       mom.setRuntimeDebug(runtimeDebug);
/*     */ 
/* 238 */       mom.suppressDuplicateRootReport(true);
/*     */ 
/* 240 */       mom.stripPrefix(new String[] { "com.sun.xml.internal.ws.server", "com.sun.xml.internal.ws.rx.rm.runtime.sequence" });
/*     */ 
/* 245 */       mom.addAnnotation(WebServiceFeature.class, DummyWebServiceFeature.class.getAnnotation(ManagedData.class));
/* 246 */       mom.addAnnotation(WebServiceFeature.class, DummyWebServiceFeature.class.getAnnotation(Description.class));
/* 247 */       mom.addAnnotation(WebServiceFeature.class, DummyWebServiceFeature.class.getAnnotation(InheritedAttributes.class));
/*     */ 
/* 251 */       mom.suspendJMXRegistration();
/*     */     }
/*     */     catch (Throwable t) {
/*     */       try {
/* 255 */         mom.close();
/*     */       } catch (IOException e) {
/* 257 */         logger.log(Level.CONFIG, "Ignoring exception caught when closing unused ManagedObjectManager", e);
/*     */       }
/* 259 */       logger.log(Level.WARNING, "Ignoring exception - starting up without monitoring", t);
/* 260 */       return ManagedObjectManagerFactory.createNOOP();
/*     */     }
/* 262 */     return mom;
/*     */   }
/*     */ 
/*     */   private ManagedObjectManager createRoot(ManagedObjectManager mom, String rootName, int unique) {
/* 266 */     String name = rootName + (unique == 0 ? "" : new StringBuilder().append("-").append(String.valueOf(unique)).toString());
/*     */     try {
/* 268 */       Object ignored = mom.createRoot(this, name);
/* 269 */       if (ignored != null) {
/* 270 */         ObjectName ignoredName = mom.getObjectName(mom.getRoot());
/*     */ 
/* 272 */         if (ignoredName != null) {
/* 273 */           logger.log(Level.INFO, "Metro monitoring rootname successfully set to: " + ignoredName);
/*     */         }
/* 275 */         return mom;
/*     */       }
/*     */       try {
/* 278 */         mom.close();
/*     */       } catch (IOException e) {
/* 280 */         logger.log(Level.CONFIG, "Ignoring exception caught when closing unused ManagedObjectManager", e);
/*     */       }
/* 282 */       String basemsg = "Duplicate Metro monitoring rootname: " + name + " : ";
/* 283 */       if (unique > maxUniqueEndpointRootNameRetries) {
/* 284 */         String msg = basemsg + "Giving up.";
/* 285 */         logger.log(Level.INFO, msg);
/* 286 */         return ManagedObjectManagerFactory.createNOOP();
/*     */       }
/* 288 */       String msg = basemsg + "Will try to make unique";
/* 289 */       logger.log(Level.CONFIG, msg);
/* 290 */       return createMOMLoop(rootName, ++unique);
/*     */     } catch (Throwable t) {
/* 292 */       logger.log(Level.WARNING, "Error while creating monitoring root with name: " + rootName, t);
/* 293 */     }return ManagedObjectManagerFactory.createNOOP();
/*     */   }
/*     */ 
/*     */   public static void closeMOM(ManagedObjectManager mom)
/*     */   {
/*     */     try {
/* 299 */       ObjectName name = mom.getObjectName(mom.getRoot());
/*     */ 
/* 301 */       if (name != null) {
/* 302 */         logger.log(Level.INFO, "Closing Metro monitoring root: " + name);
/*     */       }
/* 304 */       mom.close();
/*     */     } catch (IOException e) {
/* 306 */       logger.log(Level.WARNING, "Ignoring error when closing Managed Object Manager", e);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static ManagementAssertion.Setting propertyToSetting(String propName)
/*     */   {
/* 319 */     String s = System.getProperty(propName);
/* 320 */     if (s == null) {
/* 321 */       return ManagementAssertion.Setting.NOT_SET;
/*     */     }
/* 323 */     s = s.toLowerCase();
/* 324 */     if ((s.equals("false")) || (s.equals("off")))
/* 325 */       return ManagementAssertion.Setting.OFF;
/* 326 */     if ((s.equals("true")) || (s.equals("on"))) {
/* 327 */       return ManagementAssertion.Setting.ON;
/*     */     }
/* 329 */     return ManagementAssertion.Setting.NOT_SET;
/*     */   }
/*     */ 
/*     */   static {
/*     */     try {
/* 334 */       endpointMonitoring = propertyToSetting("com.sun.xml.internal.ws.monitoring.endpoint");
/*     */ 
/* 336 */       clientMonitoring = propertyToSetting("com.sun.xml.internal.ws.monitoring.client");
/*     */ 
/* 338 */       Integer i = Integer.getInteger("com.sun.xml.internal.ws.monitoring.typelibDebug");
/* 339 */       if (i != null) {
/* 340 */         typelibDebug = i.intValue();
/*     */       }
/*     */ 
/* 343 */       String s = System.getProperty("com.sun.xml.internal.ws.monitoring.registrationDebug");
/* 344 */       if (s != null) {
/* 345 */         registrationDebug = s.toUpperCase();
/*     */       }
/*     */ 
/* 348 */       s = System.getProperty("com.sun.xml.internal.ws.monitoring.runtimeDebug");
/* 349 */       if ((s != null) && (s.toLowerCase().equals("true"))) {
/* 350 */         runtimeDebug = true;
/*     */       }
/*     */ 
/* 353 */       i = Integer.getInteger("com.sun.xml.internal.ws.monitoring.maxUniqueEndpointRootNameRetries");
/* 354 */       if (i != null)
/* 355 */         maxUniqueEndpointRootNameRetries = i.intValue();
/*     */     }
/*     */     catch (Exception e) {
/* 358 */       logger.log(Level.WARNING, "Error while reading monitoring properties", e);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.server.MonitorBase
 * JD-Core Version:    0.6.2
 */