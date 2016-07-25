/*     */ package com.sun.xml.internal.stream;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.impl.PropertyManager;
/*     */ import com.sun.org.apache.xerces.internal.impl.XMLErrorReporter;
/*     */ import com.sun.org.apache.xerces.internal.impl.msg.XMLMessageFormatter;
/*     */ import com.sun.org.apache.xerces.internal.util.MessageFormatter;
/*     */ import com.sun.org.apache.xerces.internal.xni.XMLLocator;
/*     */ import com.sun.org.apache.xerces.internal.xni.XNIException;
/*     */ import javax.xml.stream.Location;
/*     */ import javax.xml.stream.XMLReporter;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ 
/*     */ public class StaxErrorReporter extends XMLErrorReporter
/*     */ {
/*  49 */   protected XMLReporter fXMLReporter = null;
/*     */ 
/*     */   public StaxErrorReporter(PropertyManager propertyManager)
/*     */   {
/*  54 */     putMessageFormatter("http://www.w3.org/TR/1998/REC-xml-19980210", new XMLMessageFormatter());
/*  55 */     reset(propertyManager);
/*     */   }
/*     */ 
/*     */   public StaxErrorReporter()
/*     */   {
/*  63 */     putMessageFormatter("http://www.w3.org/TR/1998/REC-xml-19980210", new XMLMessageFormatter());
/*     */   }
/*     */ 
/*     */   public void reset(PropertyManager propertyManager)
/*     */   {
/*  70 */     this.fXMLReporter = ((XMLReporter)propertyManager.getProperty("javax.xml.stream.reporter"));
/*     */   }
/*     */ 
/*     */   public String reportError(XMLLocator location, String domain, String key, Object[] arguments, short severity)
/*     */     throws XNIException
/*     */   {
/*  90 */     MessageFormatter messageFormatter = getMessageFormatter(domain);
/*     */     String message;
/*     */     String message;
/*  92 */     if (messageFormatter != null) {
/*  93 */       message = messageFormatter.formatMessage(this.fLocale, key, arguments);
/*     */     }
/*     */     else {
/*  96 */       StringBuffer str = new StringBuffer();
/*  97 */       str.append(domain);
/*  98 */       str.append('#');
/*  99 */       str.append(key);
/* 100 */       int argCount = arguments != null ? arguments.length : 0;
/* 101 */       if (argCount > 0) {
/* 102 */         str.append('?');
/* 103 */         for (int i = 0; i < argCount; i++) {
/* 104 */           str.append(arguments[i]);
/* 105 */           if (i < argCount - 1) {
/* 106 */             str.append('&');
/*     */           }
/*     */         }
/*     */       }
/* 110 */       message = str.toString();
/*     */     }
/*     */ 
/* 123 */     switch (severity) {
/*     */     case 0:
/*     */       try {
/* 126 */         if (this.fXMLReporter != null) {
/* 127 */           this.fXMLReporter.report(message, "WARNING", null, convertToStaxLocation(location));
/*     */         }
/*     */       }
/*     */       catch (XMLStreamException ex)
/*     */       {
/* 132 */         throw new XNIException(ex);
/*     */       }
/*     */ 
/*     */     case 1:
/*     */       try
/*     */       {
/* 138 */         if (this.fXMLReporter != null) {
/* 139 */           this.fXMLReporter.report(message, "ERROR", null, convertToStaxLocation(location));
/*     */         }
/*     */       }
/*     */       catch (XMLStreamException ex)
/*     */       {
/* 144 */         throw new XNIException(ex);
/*     */       }
/*     */ 
/*     */     case 2:
/* 149 */       if (!this.fContinueAfterFatalError) {
/* 150 */         throw new XNIException(message);
/*     */       }
/*     */       break;
/*     */     }
/*     */ 
/* 155 */     return message;
/*     */   }
/*     */ 
/*     */   Location convertToStaxLocation(final XMLLocator location)
/*     */   {
/* 160 */     return new Location() {
/*     */       public int getColumnNumber() {
/* 162 */         return location.getColumnNumber();
/*     */       }
/*     */ 
/*     */       public int getLineNumber() {
/* 166 */         return location.getLineNumber();
/*     */       }
/*     */ 
/*     */       public String getPublicId() {
/* 170 */         return location.getPublicId();
/*     */       }
/*     */ 
/*     */       public String getSystemId() {
/* 174 */         return location.getLiteralSystemId();
/*     */       }
/*     */ 
/*     */       public int getCharacterOffset() {
/* 178 */         return location.getCharacterOffset();
/*     */       }
/*     */       public String getLocationURI() {
/* 181 */         return "";
/*     */       }
/*     */     };
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.stream.StaxErrorReporter
 * JD-Core Version:    0.6.2
 */