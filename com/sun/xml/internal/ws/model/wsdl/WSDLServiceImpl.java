/*     */ package com.sun.xml.internal.ws.model.wsdl;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.istack.internal.Nullable;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLPort;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLService;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.stream.XMLStreamReader;
/*     */ 
/*     */ public final class WSDLServiceImpl extends AbstractExtensibleImpl
/*     */   implements WSDLService
/*     */ {
/*     */   private final QName name;
/*     */   private final Map<QName, WSDLPortImpl> ports;
/*     */   private final WSDLModelImpl parent;
/*     */ 
/*     */   public WSDLServiceImpl(XMLStreamReader xsr, WSDLModelImpl parent, QName name)
/*     */   {
/*  49 */     super(xsr);
/*  50 */     this.parent = parent;
/*  51 */     this.name = name;
/*  52 */     this.ports = new LinkedHashMap();
/*     */   }
/*     */ 
/*     */   @NotNull
/*     */   public WSDLModelImpl getParent() {
/*  57 */     return this.parent;
/*     */   }
/*     */ 
/*     */   public QName getName() {
/*  61 */     return this.name;
/*     */   }
/*     */ 
/*     */   public WSDLPortImpl get(QName portName) {
/*  65 */     return (WSDLPortImpl)this.ports.get(portName);
/*     */   }
/*     */ 
/*     */   public WSDLPort getFirstPort() {
/*  69 */     if (this.ports.isEmpty()) {
/*  70 */       return null;
/*     */     }
/*  72 */     return (WSDLPort)this.ports.values().iterator().next();
/*     */   }
/*     */ 
/*     */   public Iterable<WSDLPortImpl> getPorts() {
/*  76 */     return this.ports.values();
/*     */   }
/*     */ 
/*     */   @Nullable
/*     */   public WSDLPortImpl getMatchingPort(QName portTypeName)
/*     */   {
/*  84 */     for (WSDLPortImpl port : getPorts()) {
/*  85 */       QName ptName = port.getBinding().getPortTypeName();
/*  86 */       assert (ptName != null);
/*  87 */       if (ptName.equals(portTypeName))
/*  88 */         return port;
/*     */     }
/*  90 */     return null;
/*     */   }
/*     */ 
/*     */   public void put(QName portName, WSDLPortImpl port)
/*     */   {
/* 101 */     if ((portName == null) || (port == null))
/* 102 */       throw new NullPointerException();
/* 103 */     this.ports.put(portName, port);
/*     */   }
/*     */ 
/*     */   void freeze(WSDLModelImpl root) {
/* 107 */     for (WSDLPortImpl port : this.ports.values())
/* 108 */       port.freeze(root);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.model.wsdl.WSDLServiceImpl
 * JD-Core Version:    0.6.2
 */