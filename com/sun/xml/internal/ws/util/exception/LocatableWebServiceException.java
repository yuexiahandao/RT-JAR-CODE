/*     */ package com.sun.xml.internal.ws.util.exception;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.xml.internal.ws.resources.UtilMessages;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import javax.xml.stream.Location;
/*     */ import javax.xml.stream.XMLStreamReader;
/*     */ import javax.xml.ws.WebServiceException;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.helpers.LocatorImpl;
/*     */ 
/*     */ public class LocatableWebServiceException extends WebServiceException
/*     */ {
/*     */   private final Locator[] location;
/*     */ 
/*     */   public LocatableWebServiceException(String message, Locator[] location)
/*     */   {
/*  56 */     this(message, null, location);
/*     */   }
/*     */ 
/*     */   public LocatableWebServiceException(String message, Throwable cause, Locator[] location) {
/*  60 */     super(appendLocationInfo(message, location), cause);
/*  61 */     this.location = location;
/*     */   }
/*     */ 
/*     */   public LocatableWebServiceException(Throwable cause, Locator[] location) {
/*  65 */     this(cause.toString(), cause, location);
/*     */   }
/*     */ 
/*     */   public LocatableWebServiceException(String message, XMLStreamReader locationSource) {
/*  69 */     this(message, new Locator[] { toLocation(locationSource) });
/*     */   }
/*     */ 
/*     */   public LocatableWebServiceException(String message, Throwable cause, XMLStreamReader locationSource) {
/*  73 */     this(message, cause, new Locator[] { toLocation(locationSource) });
/*     */   }
/*     */ 
/*     */   public LocatableWebServiceException(Throwable cause, XMLStreamReader locationSource) {
/*  77 */     this(cause, new Locator[] { toLocation(locationSource) });
/*     */   }
/*     */ 
/*     */   @NotNull
/*     */   public List<Locator> getLocation()
/*     */   {
/*  87 */     return Arrays.asList(this.location);
/*     */   }
/*     */ 
/*     */   private static String appendLocationInfo(String message, Locator[] location) {
/*  91 */     StringBuilder buf = new StringBuilder(message);
/*  92 */     for (Locator loc : location)
/*  93 */       buf.append('\n').append(UtilMessages.UTIL_LOCATION(Integer.valueOf(loc.getLineNumber()), loc.getSystemId()));
/*  94 */     return buf.toString();
/*     */   }
/*     */ 
/*     */   private static Locator toLocation(XMLStreamReader xsr) {
/*  98 */     LocatorImpl loc = new LocatorImpl();
/*  99 */     Location in = xsr.getLocation();
/* 100 */     loc.setSystemId(in.getSystemId());
/* 101 */     loc.setPublicId(in.getPublicId());
/* 102 */     loc.setLineNumber(in.getLineNumber());
/* 103 */     loc.setColumnNumber(in.getColumnNumber());
/* 104 */     return loc;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.util.exception.LocatableWebServiceException
 * JD-Core Version:    0.6.2
 */