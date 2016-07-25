/*     */ package com.sun.xml.internal.ws.model.wsdl;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLFault;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLOperation;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLPartDescriptor;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLPortType;
/*     */ import com.sun.xml.internal.ws.util.QNameMap;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.stream.XMLStreamReader;
/*     */ 
/*     */ public final class WSDLOperationImpl extends AbstractExtensibleImpl
/*     */   implements WSDLOperation
/*     */ {
/*     */   private final QName name;
/*     */   private String parameterOrder;
/*     */   private WSDLInputImpl input;
/*     */   private WSDLOutputImpl output;
/*     */   private final List<WSDLFaultImpl> faults;
/*     */   private final QNameMap<WSDLFaultImpl> faultMap;
/*     */   protected Iterable<WSDLMessageImpl> messages;
/*     */   private final WSDLPortType owner;
/*     */ 
/*     */   public WSDLOperationImpl(XMLStreamReader xsr, WSDLPortTypeImpl owner, QName name)
/*     */   {
/*  57 */     super(xsr);
/*  58 */     this.name = name;
/*  59 */     this.faults = new ArrayList();
/*  60 */     this.faultMap = new QNameMap();
/*  61 */     this.owner = owner;
/*     */   }
/*     */ 
/*     */   public QName getName() {
/*  65 */     return this.name;
/*     */   }
/*     */ 
/*     */   public String getParameterOrder() {
/*  69 */     return this.parameterOrder;
/*     */   }
/*     */ 
/*     */   public void setParameterOrder(String parameterOrder) {
/*  73 */     this.parameterOrder = parameterOrder;
/*     */   }
/*     */ 
/*     */   public WSDLInputImpl getInput() {
/*  77 */     return this.input;
/*     */   }
/*     */ 
/*     */   public void setInput(WSDLInputImpl input) {
/*  81 */     this.input = input;
/*     */   }
/*     */ 
/*     */   public WSDLOutputImpl getOutput() {
/*  85 */     return this.output;
/*     */   }
/*     */ 
/*     */   public boolean isOneWay() {
/*  89 */     return this.output == null;
/*     */   }
/*     */ 
/*     */   public void setOutput(WSDLOutputImpl output) {
/*  93 */     this.output = output;
/*     */   }
/*     */ 
/*     */   public Iterable<WSDLFaultImpl> getFaults() {
/*  97 */     return this.faults;
/*     */   }
/*     */ 
/*     */   public WSDLFault getFault(QName faultDetailName) {
/* 101 */     WSDLFaultImpl fault = (WSDLFaultImpl)this.faultMap.get(faultDetailName);
/* 102 */     if (fault != null) {
/* 103 */       return fault;
/*     */     }
/* 105 */     for (WSDLFaultImpl fi : this.faults) {
/* 106 */       assert (fi.getMessage().parts().iterator().hasNext());
/* 107 */       WSDLPartImpl part = (WSDLPartImpl)fi.getMessage().parts().iterator().next();
/* 108 */       if (part.getDescriptor().name().equals(faultDetailName)) {
/* 109 */         this.faultMap.put(faultDetailName, fi);
/* 110 */         return fi;
/*     */       }
/*     */     }
/* 113 */     return null;
/*     */   }
/*     */ 
/*     */   WSDLPortType getOwner() {
/* 117 */     return this.owner;
/*     */   }
/*     */ 
/*     */   @NotNull
/*     */   public QName getPortTypeName() {
/* 122 */     return this.owner.getName();
/*     */   }
/*     */ 
/*     */   public void addFault(WSDLFaultImpl fault) {
/* 126 */     this.faults.add(fault);
/*     */   }
/*     */ 
/*     */   public void freez(WSDLModelImpl root) {
/* 130 */     assert (this.input != null);
/* 131 */     this.input.freeze(root);
/* 132 */     if (this.output != null)
/* 133 */       this.output.freeze(root);
/* 134 */     for (WSDLFaultImpl fault : this.faults)
/* 135 */       fault.freeze(root);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.model.wsdl.WSDLOperationImpl
 * JD-Core Version:    0.6.2
 */