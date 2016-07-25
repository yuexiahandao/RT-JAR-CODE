/*     */ package com.sun.xml.internal.ws.server;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.xml.internal.ws.api.server.SDDocument;
/*     */ import com.sun.xml.internal.ws.api.server.SDDocumentFilter;
/*     */ import com.sun.xml.internal.ws.api.server.ServiceDefinition;
/*     */ import com.sun.xml.internal.ws.wsdl.SDDocumentResolver;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ public final class ServiceDefinitionImpl
/*     */   implements ServiceDefinition, SDDocumentResolver
/*     */ {
/*     */   private final List<SDDocumentImpl> docs;
/*     */   private final Map<String, SDDocumentImpl> bySystemId;
/*     */ 
/*     */   @NotNull
/*     */   private final SDDocumentImpl primaryWsdl;
/*     */   WSEndpointImpl<?> owner;
/*  60 */   final List<SDDocumentFilter> filters = new ArrayList();
/*     */ 
/*     */   public ServiceDefinitionImpl(List<SDDocumentImpl> docs, @NotNull SDDocumentImpl primaryWsdl)
/*     */   {
/*  69 */     assert (docs.contains(primaryWsdl));
/*  70 */     this.docs = docs;
/*  71 */     this.primaryWsdl = primaryWsdl;
/*     */ 
/*  73 */     this.bySystemId = new HashMap(docs.size());
/*  74 */     for (SDDocumentImpl doc : docs) {
/*  75 */       this.bySystemId.put(doc.getURL().toExternalForm(), doc);
/*  76 */       doc.setFilters(this.filters);
/*  77 */       doc.setResolver(this);
/*     */     }
/*     */   }
/*     */ 
/*     */   void setOwner(WSEndpointImpl<?> owner)
/*     */   {
/*  85 */     assert ((owner != null) && (this.owner == null));
/*  86 */     this.owner = owner;
/*     */   }
/*     */   @NotNull
/*     */   public SDDocument getPrimary() {
/*  90 */     return this.primaryWsdl;
/*     */   }
/*     */ 
/*     */   public void addFilter(SDDocumentFilter filter) {
/*  94 */     this.filters.add(filter);
/*     */   }
/*     */ 
/*     */   public Iterator<SDDocument> iterator() {
/*  98 */     return this.docs.iterator();
/*     */   }
/*     */ 
/*     */   public SDDocument resolve(String systemId)
/*     */   {
/* 109 */     return (SDDocument)this.bySystemId.get(systemId);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.server.ServiceDefinitionImpl
 * JD-Core Version:    0.6.2
 */