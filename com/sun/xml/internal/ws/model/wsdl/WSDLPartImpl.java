/*    */ package com.sun.xml.internal.ws.model.wsdl;
/*    */ 
/*    */ import com.sun.xml.internal.ws.api.model.ParameterBinding;
/*    */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLPart;
/*    */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLPartDescriptor;
/*    */ import javax.xml.stream.XMLStreamReader;
/*    */ 
/*    */ public final class WSDLPartImpl extends AbstractObjectImpl
/*    */   implements WSDLPart
/*    */ {
/*    */   private final String name;
/*    */   private ParameterBinding binding;
/*    */   private int index;
/*    */   private final WSDLPartDescriptor descriptor;
/*    */ 
/*    */   public WSDLPartImpl(XMLStreamReader xsr, String partName, int index, WSDLPartDescriptor descriptor)
/*    */   {
/* 46 */     super(xsr);
/* 47 */     this.name = partName;
/* 48 */     this.binding = ParameterBinding.UNBOUND;
/* 49 */     this.index = index;
/* 50 */     this.descriptor = descriptor;
/*    */   }
/*    */ 
/*    */   public String getName()
/*    */   {
/* 55 */     return this.name;
/*    */   }
/*    */ 
/*    */   public ParameterBinding getBinding() {
/* 59 */     return this.binding;
/*    */   }
/*    */ 
/*    */   public void setBinding(ParameterBinding binding) {
/* 63 */     this.binding = binding;
/*    */   }
/*    */ 
/*    */   public int getIndex() {
/* 67 */     return this.index;
/*    */   }
/*    */ 
/*    */   public void setIndex(int index)
/*    */   {
/* 72 */     this.index = index;
/*    */   }
/*    */ 
/*    */   boolean isBody() {
/* 76 */     return this.binding.isBody();
/*    */   }
/*    */ 
/*    */   public WSDLPartDescriptor getDescriptor() {
/* 80 */     return this.descriptor;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.model.wsdl.WSDLPartImpl
 * JD-Core Version:    0.6.2
 */