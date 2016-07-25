/*     */ package com.sun.xml.internal.ws.model;
/*     */ 
/*     */ import com.sun.xml.internal.bind.api.Bridge;
/*     */ import com.sun.xml.internal.bind.api.TypeReference;
/*     */ import com.sun.xml.internal.ws.api.model.JavaMethod;
/*     */ import com.sun.xml.internal.ws.api.model.Parameter;
/*     */ import com.sun.xml.internal.ws.api.model.ParameterBinding;
/*     */ import java.util.List;
/*     */ import javax.jws.WebParam.Mode;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.ws.Holder;
/*     */ 
/*     */ public class ParameterImpl
/*     */   implements Parameter
/*     */ {
/*     */   private ParameterBinding binding;
/*     */   private ParameterBinding outBinding;
/*     */   private String partName;
/*     */   private final int index;
/*     */   private final WebParam.Mode mode;
/*     */   private TypeReference typeReference;
/*     */   private QName name;
/*     */   private final JavaMethodImpl parent;
/*     */ 
/*     */   public ParameterImpl(JavaMethodImpl parent, TypeReference type, WebParam.Mode mode, int index)
/*     */   {
/*  65 */     assert (type != null);
/*     */ 
/*  67 */     this.typeReference = type;
/*  68 */     this.name = type.tagName;
/*  69 */     this.mode = mode;
/*  70 */     this.index = index;
/*  71 */     this.parent = parent;
/*     */   }
/*     */ 
/*     */   public AbstractSEIModelImpl getOwner() {
/*  75 */     return this.parent.owner;
/*     */   }
/*     */ 
/*     */   public JavaMethod getParent() {
/*  79 */     return this.parent;
/*     */   }
/*     */ 
/*     */   public QName getName()
/*     */   {
/*  86 */     return this.name;
/*     */   }
/*     */ 
/*     */   public Bridge getBridge() {
/*  90 */     return getOwner().getBridge(this.typeReference);
/*     */   }
/*     */ 
/*     */   protected Bridge getBridge(TypeReference typeRef) {
/*  94 */     return getOwner().getBridge(typeRef);
/*     */   }
/*     */ 
/*     */   public TypeReference getTypeReference()
/*     */   {
/* 104 */     return this.typeReference;
/*     */   }
/*     */ 
/*     */   void setTypeReference(TypeReference type)
/*     */   {
/* 112 */     this.typeReference = type;
/* 113 */     this.name = type.tagName;
/*     */   }
/*     */ 
/*     */   public WebParam.Mode getMode()
/*     */   {
/* 118 */     return this.mode;
/*     */   }
/*     */ 
/*     */   public int getIndex() {
/* 122 */     return this.index;
/*     */   }
/*     */ 
/*     */   public boolean isWrapperStyle()
/*     */   {
/* 129 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isReturnValue() {
/* 133 */     return this.index == -1;
/*     */   }
/*     */ 
/*     */   public ParameterBinding getBinding()
/*     */   {
/* 140 */     if (this.binding == null)
/* 141 */       return ParameterBinding.BODY;
/* 142 */     return this.binding;
/*     */   }
/*     */ 
/*     */   public void setBinding(ParameterBinding binding)
/*     */   {
/* 149 */     this.binding = binding;
/*     */   }
/*     */ 
/*     */   public void setInBinding(ParameterBinding binding) {
/* 153 */     this.binding = binding;
/*     */   }
/*     */ 
/*     */   public void setOutBinding(ParameterBinding binding) {
/* 157 */     this.outBinding = binding;
/*     */   }
/*     */ 
/*     */   public ParameterBinding getInBinding() {
/* 161 */     return this.binding;
/*     */   }
/*     */ 
/*     */   public ParameterBinding getOutBinding() {
/* 165 */     if (this.outBinding == null)
/* 166 */       return this.binding;
/* 167 */     return this.outBinding;
/*     */   }
/*     */ 
/*     */   public boolean isIN() {
/* 171 */     return this.mode == WebParam.Mode.IN;
/*     */   }
/*     */ 
/*     */   public boolean isOUT() {
/* 175 */     return this.mode == WebParam.Mode.OUT;
/*     */   }
/*     */ 
/*     */   public boolean isINOUT() {
/* 179 */     return this.mode == WebParam.Mode.INOUT;
/*     */   }
/*     */ 
/*     */   public boolean isResponse()
/*     */   {
/* 191 */     return this.index == -1;
/*     */   }
/*     */ 
/*     */   public Object getHolderValue(Object obj)
/*     */   {
/* 203 */     if ((obj != null) && ((obj instanceof Holder)))
/* 204 */       return ((Holder)obj).value;
/* 205 */     return obj;
/*     */   }
/*     */ 
/*     */   public String getPartName() {
/* 209 */     if (this.partName == null)
/* 210 */       return this.name.getLocalPart();
/* 211 */     return this.partName;
/*     */   }
/*     */ 
/*     */   public void setPartName(String partName) {
/* 215 */     this.partName = partName;
/*     */   }
/*     */ 
/*     */   void fillTypes(List<TypeReference> types) {
/* 219 */     types.add(getTypeReference());
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.model.ParameterImpl
 * JD-Core Version:    0.6.2
 */