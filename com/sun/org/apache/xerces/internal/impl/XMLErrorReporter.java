/*     */ package com.sun.org.apache.xerces.internal.impl;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.util.DefaultErrorHandler;
/*     */ import com.sun.org.apache.xerces.internal.util.ErrorHandlerProxy;
/*     */ import com.sun.org.apache.xerces.internal.util.MessageFormatter;
/*     */ import com.sun.org.apache.xerces.internal.xni.XMLLocator;
/*     */ import com.sun.org.apache.xerces.internal.xni.XNIException;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLComponent;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLComponentManager;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLConfigurationException;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLErrorHandler;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLParseException;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Locale;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ 
/*     */ public class XMLErrorReporter
/*     */   implements XMLComponent
/*     */ {
/*     */   public static final short SEVERITY_WARNING = 0;
/*     */   public static final short SEVERITY_ERROR = 1;
/*     */   public static final short SEVERITY_FATAL_ERROR = 2;
/*     */   protected static final String CONTINUE_AFTER_FATAL_ERROR = "http://apache.org/xml/features/continue-after-fatal-error";
/*     */   protected static final String ERROR_HANDLER = "http://apache.org/xml/properties/internal/error-handler";
/* 165 */   private static final String[] RECOGNIZED_FEATURES = { "http://apache.org/xml/features/continue-after-fatal-error" };
/*     */ 
/* 170 */   private static final Boolean[] FEATURE_DEFAULTS = { null };
/*     */ 
/* 175 */   private static final String[] RECOGNIZED_PROPERTIES = { "http://apache.org/xml/properties/internal/error-handler" };
/*     */ 
/* 180 */   private static final Object[] PROPERTY_DEFAULTS = { null };
/*     */   protected Locale fLocale;
/*     */   protected Hashtable fMessageFormatters;
/*     */   protected XMLErrorHandler fErrorHandler;
/*     */   protected XMLLocator fLocator;
/*     */   protected boolean fContinueAfterFatalError;
/*     */   protected XMLErrorHandler fDefaultErrorHandler;
/* 212 */   private ErrorHandler fSaxProxy = null;
/*     */ 
/*     */   public XMLErrorReporter()
/*     */   {
/* 236 */     this.fMessageFormatters = new Hashtable();
/*     */   }
/*     */ 
/*     */   public void setLocale(Locale locale)
/*     */   {
/* 250 */     this.fLocale = locale;
/*     */   }
/*     */ 
/*     */   public Locale getLocale()
/*     */   {
/* 259 */     return this.fLocale;
/*     */   }
/*     */ 
/*     */   public void setDocumentLocator(XMLLocator locator)
/*     */   {
/* 268 */     this.fLocator = locator;
/*     */   }
/*     */ 
/*     */   public void putMessageFormatter(String domain, MessageFormatter messageFormatter)
/*     */   {
/* 284 */     this.fMessageFormatters.put(domain, messageFormatter);
/*     */   }
/*     */ 
/*     */   public MessageFormatter getMessageFormatter(String domain)
/*     */   {
/* 294 */     return (MessageFormatter)this.fMessageFormatters.get(domain);
/*     */   }
/*     */ 
/*     */   public MessageFormatter removeMessageFormatter(String domain)
/*     */   {
/* 304 */     return (MessageFormatter)this.fMessageFormatters.remove(domain);
/*     */   }
/*     */ 
/*     */   public String reportError(String domain, String key, Object[] arguments, short severity)
/*     */     throws XNIException
/*     */   {
/* 325 */     return reportError(this.fLocator, domain, key, arguments, severity);
/*     */   }
/*     */ 
/*     */   public String reportError(String domain, String key, Object[] arguments, short severity, Exception exception)
/*     */     throws XNIException
/*     */   {
/* 347 */     return reportError(this.fLocator, domain, key, arguments, severity, exception);
/*     */   }
/*     */ 
/*     */   public String reportError(XMLLocator location, String domain, String key, Object[] arguments, short severity)
/*     */     throws XNIException
/*     */   {
/* 368 */     return reportError(location, domain, key, arguments, severity, null);
/*     */   }
/*     */ 
/*     */   public String reportError(XMLLocator location, String domain, String key, Object[] arguments, short severity, Exception exception)
/*     */     throws XNIException
/*     */   {
/* 395 */     MessageFormatter messageFormatter = getMessageFormatter(domain);
/*     */     String message;
/*     */     String message;
/* 397 */     if (messageFormatter != null) {
/* 398 */       message = messageFormatter.formatMessage(this.fLocale, key, arguments);
/*     */     }
/*     */     else {
/* 401 */       StringBuffer str = new StringBuffer();
/* 402 */       str.append(domain);
/* 403 */       str.append('#');
/* 404 */       str.append(key);
/* 405 */       int argCount = arguments != null ? arguments.length : 0;
/* 406 */       if (argCount > 0) {
/* 407 */         str.append('?');
/* 408 */         for (int i = 0; i < argCount; i++) {
/* 409 */           str.append(arguments[i]);
/* 410 */           if (i < argCount - 1) {
/* 411 */             str.append('&');
/*     */           }
/*     */         }
/*     */       }
/* 415 */       message = str.toString();
/*     */     }
/* 417 */     XMLParseException parseException = exception != null ? new XMLParseException(location, message, exception) : new XMLParseException(location, message);
/*     */ 
/* 422 */     XMLErrorHandler errorHandler = this.fErrorHandler;
/* 423 */     if (errorHandler == null) {
/* 424 */       if (this.fDefaultErrorHandler == null) {
/* 425 */         this.fDefaultErrorHandler = new DefaultErrorHandler();
/*     */       }
/* 427 */       errorHandler = this.fDefaultErrorHandler;
/*     */     }
/*     */ 
/* 431 */     switch (severity) {
/*     */     case 0:
/* 433 */       errorHandler.warning(domain, key, parseException);
/* 434 */       break;
/*     */     case 1:
/* 437 */       errorHandler.error(domain, key, parseException);
/* 438 */       break;
/*     */     case 2:
/* 441 */       errorHandler.fatalError(domain, key, parseException);
/* 442 */       if (!this.fContinueAfterFatalError) {
/* 443 */         throw parseException;
/*     */       }
/*     */       break;
/*     */     }
/*     */ 
/* 448 */     return message;
/*     */   }
/*     */ 
/*     */   public void reset(XMLComponentManager componentManager)
/*     */     throws XNIException
/*     */   {
/* 474 */     this.fContinueAfterFatalError = componentManager.getFeature("http://apache.org/xml/features/continue-after-fatal-error", false);
/*     */ 
/* 477 */     this.fErrorHandler = ((XMLErrorHandler)componentManager.getProperty("http://apache.org/xml/properties/internal/error-handler"));
/*     */   }
/*     */ 
/*     */   public String[] getRecognizedFeatures()
/*     */   {
/* 487 */     return (String[])RECOGNIZED_FEATURES.clone();
/*     */   }
/*     */ 
/*     */   public void setFeature(String featureId, boolean state)
/*     */     throws XMLConfigurationException
/*     */   {
/* 512 */     if (featureId.startsWith("http://apache.org/xml/features/")) {
/* 513 */       int suffixLength = featureId.length() - "http://apache.org/xml/features/".length();
/*     */ 
/* 520 */       if ((suffixLength == "continue-after-fatal-error".length()) && (featureId.endsWith("continue-after-fatal-error")))
/*     */       {
/* 522 */         this.fContinueAfterFatalError = state;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean getFeature(String featureId)
/*     */     throws XMLConfigurationException
/*     */   {
/* 536 */     if (featureId.startsWith("http://apache.org/xml/features/")) {
/* 537 */       int suffixLength = featureId.length() - "http://apache.org/xml/features/".length();
/*     */ 
/* 544 */       if ((suffixLength == "continue-after-fatal-error".length()) && (featureId.endsWith("continue-after-fatal-error")))
/*     */       {
/* 546 */         return this.fContinueAfterFatalError;
/*     */       }
/*     */     }
/* 549 */     return false;
/*     */   }
/*     */ 
/*     */   public String[] getRecognizedProperties()
/*     */   {
/* 559 */     return (String[])RECOGNIZED_PROPERTIES.clone();
/*     */   }
/*     */ 
/*     */   public void setProperty(String propertyId, Object value)
/*     */     throws XMLConfigurationException
/*     */   {
/* 584 */     if (propertyId.startsWith("http://apache.org/xml/properties/")) {
/* 585 */       int suffixLength = propertyId.length() - "http://apache.org/xml/properties/".length();
/*     */ 
/* 587 */       if ((suffixLength == "internal/error-handler".length()) && (propertyId.endsWith("internal/error-handler")))
/*     */       {
/* 589 */         this.fErrorHandler = ((XMLErrorHandler)value);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public Boolean getFeatureDefault(String featureId)
/*     */   {
/* 605 */     for (int i = 0; i < RECOGNIZED_FEATURES.length; i++) {
/* 606 */       if (RECOGNIZED_FEATURES[i].equals(featureId)) {
/* 607 */         return FEATURE_DEFAULTS[i];
/*     */       }
/*     */     }
/* 610 */     return null;
/*     */   }
/*     */ 
/*     */   public Object getPropertyDefault(String propertyId)
/*     */   {
/* 623 */     for (int i = 0; i < RECOGNIZED_PROPERTIES.length; i++) {
/* 624 */       if (RECOGNIZED_PROPERTIES[i].equals(propertyId)) {
/* 625 */         return PROPERTY_DEFAULTS[i];
/*     */       }
/*     */     }
/* 628 */     return null;
/*     */   }
/*     */ 
/*     */   public XMLErrorHandler getErrorHandler()
/*     */   {
/* 635 */     return this.fErrorHandler;
/*     */   }
/*     */ 
/*     */   public ErrorHandler getSAXErrorHandler()
/*     */   {
/* 643 */     if (this.fSaxProxy == null) {
/* 644 */       this.fSaxProxy = new ErrorHandlerProxy() {
/*     */         protected XMLErrorHandler getErrorHandler() {
/* 646 */           return XMLErrorReporter.this.fErrorHandler;
/*     */         }
/*     */       };
/*     */     }
/* 650 */     return this.fSaxProxy;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.XMLErrorReporter
 * JD-Core Version:    0.6.2
 */