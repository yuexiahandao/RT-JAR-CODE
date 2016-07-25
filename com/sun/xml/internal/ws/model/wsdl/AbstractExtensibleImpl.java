/*     */ package com.sun.xml.internal.ws.model.wsdl;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLExtensible;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLExtension;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLObject;
/*     */ import com.sun.xml.internal.ws.resources.UtilMessages;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.stream.XMLStreamReader;
/*     */ import javax.xml.ws.WebServiceException;
/*     */ import org.xml.sax.Locator;
/*     */ 
/*     */ abstract class AbstractExtensibleImpl extends AbstractObjectImpl
/*     */   implements WSDLExtensible
/*     */ {
/*  55 */   protected final Set<WSDLExtension> extensions = new HashSet();
/*     */ 
/*  58 */   protected List<UnknownWSDLExtension> notUnderstoodExtensions = new ArrayList();
/*     */ 
/*     */   protected AbstractExtensibleImpl(XMLStreamReader xsr)
/*     */   {
/*  62 */     super(xsr);
/*     */   }
/*     */ 
/*     */   protected AbstractExtensibleImpl(String systemId, int lineNumber) {
/*  66 */     super(systemId, lineNumber);
/*     */   }
/*     */ 
/*     */   public final Iterable<WSDLExtension> getExtensions() {
/*  70 */     return this.extensions;
/*     */   }
/*     */ 
/*     */   public final <T extends WSDLExtension> Iterable<T> getExtensions(Class<T> type)
/*     */   {
/*  75 */     List r = new ArrayList(this.extensions.size());
/*  76 */     for (WSDLExtension e : this.extensions) {
/*  77 */       if (type.isInstance(e))
/*  78 */         r.add(type.cast(e));
/*     */     }
/*  80 */     return r;
/*     */   }
/*     */ 
/*     */   public <T extends WSDLExtension> T getExtension(Class<T> type) {
/*  84 */     for (WSDLExtension e : this.extensions) {
/*  85 */       if (type.isInstance(e))
/*  86 */         return (WSDLExtension)type.cast(e);
/*     */     }
/*  88 */     return null;
/*     */   }
/*     */ 
/*     */   public void addExtension(WSDLExtension ex) {
/*  92 */     if (ex == null)
/*     */     {
/*  94 */       throw new IllegalArgumentException();
/*  95 */     }this.extensions.add(ex);
/*     */   }
/*     */ 
/*     */   public void addNotUnderstoodExtension(QName extnEl, Locator locator)
/*     */   {
/* 105 */     this.notUnderstoodExtensions.add(new UnknownWSDLExtension(extnEl, locator));
/*     */   }
/*     */ 
/*     */   public boolean areRequiredExtensionsUnderstood()
/*     */   {
/* 131 */     if (this.notUnderstoodExtensions.size() != 0) {
/* 132 */       StringBuilder buf = new StringBuilder("Unknown WSDL extensibility elements:");
/* 133 */       for (UnknownWSDLExtension extn : this.notUnderstoodExtensions)
/* 134 */         buf.append('\n').append(extn.toString());
/* 135 */       throw new WebServiceException(buf.toString());
/*     */     }
/* 137 */     return true;
/*     */   }
/*     */ 
/*     */   protected static class UnknownWSDLExtension
/*     */     implements WSDLExtension, WSDLObject
/*     */   {
/*     */     private final QName extnEl;
/*     */     private final Locator locator;
/*     */ 
/*     */     public UnknownWSDLExtension(QName extnEl, Locator locator)
/*     */     {
/* 112 */       this.extnEl = extnEl;
/* 113 */       this.locator = locator;
/*     */     }
/*     */     public QName getName() {
/* 116 */       return this.extnEl;
/*     */     }
/* 119 */     @NotNull
/*     */     public Locator getLocation() { return this.locator; }
/*     */ 
/*     */     public String toString() {
/* 122 */       return this.extnEl + " " + UtilMessages.UTIL_LOCATION(Integer.valueOf(this.locator.getLineNumber()), this.locator.getSystemId());
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.model.wsdl.AbstractExtensibleImpl
 * JD-Core Version:    0.6.2
 */