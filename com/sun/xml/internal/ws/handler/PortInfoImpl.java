/*     */ package com.sun.xml.internal.ws.handler;
/*     */ 
/*     */ import com.sun.xml.internal.ws.api.BindingID;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.ws.handler.PortInfo;
/*     */ 
/*     */ public class PortInfoImpl
/*     */   implements PortInfo
/*     */ {
/*     */   private BindingID bindingId;
/*     */   private QName portName;
/*     */   private QName serviceName;
/*     */ 
/*     */   public PortInfoImpl(BindingID bindingId, QName portName, QName serviceName)
/*     */   {
/*  67 */     if (bindingId == null) {
/*  68 */       throw new RuntimeException("bindingId cannot be null");
/*     */     }
/*  70 */     if (portName == null) {
/*  71 */       throw new RuntimeException("portName cannot be null");
/*     */     }
/*  73 */     if (serviceName == null) {
/*  74 */       throw new RuntimeException("serviceName cannot be null");
/*     */     }
/*  76 */     this.bindingId = bindingId;
/*  77 */     this.portName = portName;
/*  78 */     this.serviceName = serviceName;
/*     */   }
/*     */ 
/*     */   public String getBindingID() {
/*  82 */     return this.bindingId.toString();
/*     */   }
/*     */ 
/*     */   public QName getPortName() {
/*  86 */     return this.portName;
/*     */   }
/*     */ 
/*     */   public QName getServiceName() {
/*  90 */     return this.serviceName;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object obj)
/*     */   {
/* 104 */     if ((obj instanceof PortInfo)) {
/* 105 */       PortInfo info = (PortInfo)obj;
/* 106 */       if ((this.bindingId.toString().equals(info.getBindingID())) && (this.portName.equals(info.getPortName())) && (this.serviceName.equals(info.getServiceName())))
/*     */       {
/* 109 */         return true;
/*     */       }
/*     */     }
/* 112 */     return false;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 120 */     return this.bindingId.hashCode();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.handler.PortInfoImpl
 * JD-Core Version:    0.6.2
 */