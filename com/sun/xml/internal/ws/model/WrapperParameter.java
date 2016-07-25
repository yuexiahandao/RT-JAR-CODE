/*    */ package com.sun.xml.internal.ws.model;
/*    */ 
/*    */ import com.sun.xml.internal.bind.api.CompositeStructure;
/*    */ import com.sun.xml.internal.bind.api.TypeReference;
/*    */ import com.sun.xml.internal.ws.api.model.JavaMethod;
/*    */ import com.sun.xml.internal.ws.api.model.ParameterBinding;
/*    */ import com.sun.xml.internal.ws.api.model.soap.SOAPBinding;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import javax.jws.WebParam.Mode;
/*    */ 
/*    */ public class WrapperParameter extends ParameterImpl
/*    */ {
/* 55 */   protected final List<ParameterImpl> wrapperChildren = new ArrayList();
/*    */ 
/*    */   public WrapperParameter(JavaMethodImpl parent, TypeReference typeRef, WebParam.Mode mode, int index)
/*    */   {
/* 59 */     super(parent, typeRef, mode, index);
/*    */   }
/*    */ 
/*    */   /** @deprecated */
/*    */   public boolean isWrapperStyle()
/*    */   {
/* 69 */     return true;
/*    */   }
/*    */ 
/*    */   public List<ParameterImpl> getWrapperChildren()
/*    */   {
/* 76 */     return this.wrapperChildren;
/*    */   }
/*    */ 
/*    */   public void addWrapperChild(ParameterImpl wrapperChild)
/*    */   {
/* 85 */     this.wrapperChildren.add(wrapperChild);
/*    */ 
/* 87 */     assert (wrapperChild.getBinding() == ParameterBinding.BODY);
/*    */   }
/*    */ 
/*    */   public void clear() {
/* 91 */     this.wrapperChildren.clear();
/*    */   }
/*    */ 
/*    */   void fillTypes(List<TypeReference> types)
/*    */   {
/* 96 */     super.fillTypes(types);
/* 97 */     if (getParent().getBinding().isRpcLit())
/*    */     {
/* 100 */       assert (getTypeReference().type == CompositeStructure.class);
/* 101 */       for (ParameterImpl p : this.wrapperChildren)
/* 102 */         p.fillTypes(types);
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.model.WrapperParameter
 * JD-Core Version:    0.6.2
 */