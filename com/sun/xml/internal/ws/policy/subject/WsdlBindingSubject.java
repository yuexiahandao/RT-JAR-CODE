/*     */ package com.sun.xml.internal.ws.policy.subject;
/*     */ 
/*     */ import com.sun.xml.internal.ws.policy.privateutil.LocalizationMessages;
/*     */ import com.sun.xml.internal.ws.policy.privateutil.PolicyLogger;
/*     */ import javax.xml.namespace.QName;
/*     */ 
/*     */ public class WsdlBindingSubject
/*     */ {
/*  67 */   private static final PolicyLogger LOGGER = PolicyLogger.getLogger(WsdlBindingSubject.class);
/*     */   private final QName name;
/*     */   private final WsdlMessageType messageType;
/*     */   private final WsdlNameScope nameScope;
/*     */   private final WsdlBindingSubject parent;
/*     */ 
/*     */   WsdlBindingSubject(QName name, WsdlNameScope scope, WsdlBindingSubject parent)
/*     */   {
/*  75 */     this(name, WsdlMessageType.NO_MESSAGE, scope, parent);
/*     */   }
/*     */ 
/*     */   WsdlBindingSubject(QName name, WsdlMessageType messageType, WsdlNameScope scope, WsdlBindingSubject parent) {
/*  79 */     this.name = name;
/*  80 */     this.messageType = messageType;
/*  81 */     this.nameScope = scope;
/*  82 */     this.parent = parent;
/*     */   }
/*     */ 
/*     */   public static WsdlBindingSubject createBindingSubject(QName bindingName) {
/*  86 */     return new WsdlBindingSubject(bindingName, WsdlNameScope.ENDPOINT, null);
/*     */   }
/*     */ 
/*     */   public static WsdlBindingSubject createBindingOperationSubject(QName bindingName, QName operationName) {
/*  90 */     WsdlBindingSubject bindingSubject = createBindingSubject(bindingName);
/*  91 */     return new WsdlBindingSubject(operationName, WsdlNameScope.OPERATION, bindingSubject);
/*     */   }
/*     */ 
/*     */   public static WsdlBindingSubject createBindingMessageSubject(QName bindingName, QName operationName, QName messageName, WsdlMessageType messageType) {
/*  95 */     if (messageType == null) {
/*  96 */       throw ((IllegalArgumentException)LOGGER.logSevereException(new IllegalArgumentException(LocalizationMessages.WSP_0083_MESSAGE_TYPE_NULL())));
/*     */     }
/*  98 */     if (messageType == WsdlMessageType.NO_MESSAGE) {
/*  99 */       throw ((IllegalArgumentException)LOGGER.logSevereException(new IllegalArgumentException(LocalizationMessages.WSP_0084_MESSAGE_TYPE_NO_MESSAGE())));
/*     */     }
/* 101 */     if ((messageType == WsdlMessageType.FAULT) && (messageName == null)) {
/* 102 */       throw ((IllegalArgumentException)LOGGER.logSevereException(new IllegalArgumentException(LocalizationMessages.WSP_0085_MESSAGE_FAULT_NO_NAME())));
/*     */     }
/* 104 */     WsdlBindingSubject operationSubject = createBindingOperationSubject(bindingName, operationName);
/* 105 */     return new WsdlBindingSubject(messageName, messageType, WsdlNameScope.MESSAGE, operationSubject);
/*     */   }
/*     */ 
/*     */   public QName getName() {
/* 109 */     return this.name;
/*     */   }
/*     */ 
/*     */   public WsdlMessageType getMessageType() {
/* 113 */     return this.messageType;
/*     */   }
/*     */ 
/*     */   public WsdlBindingSubject getParent() {
/* 117 */     return this.parent;
/*     */   }
/*     */ 
/*     */   public boolean isBindingSubject() {
/* 121 */     if (this.nameScope == WsdlNameScope.ENDPOINT) {
/* 122 */       return this.parent == null;
/*     */     }
/*     */ 
/* 125 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isBindingOperationSubject()
/*     */   {
/* 130 */     if ((this.nameScope == WsdlNameScope.OPERATION) && 
/* 131 */       (this.parent != null)) {
/* 132 */       return this.parent.isBindingSubject();
/*     */     }
/*     */ 
/* 135 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isBindingMessageSubject() {
/* 139 */     if ((this.nameScope == WsdlNameScope.MESSAGE) && 
/* 140 */       (this.parent != null)) {
/* 141 */       return this.parent.isBindingOperationSubject();
/*     */     }
/*     */ 
/* 144 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object that)
/*     */   {
/* 149 */     if (this == that) {
/* 150 */       return true;
/*     */     }
/*     */ 
/* 153 */     if ((that == null) || (!(that instanceof WsdlBindingSubject))) {
/* 154 */       return false;
/*     */     }
/*     */ 
/* 157 */     WsdlBindingSubject thatSubject = (WsdlBindingSubject)that;
/* 158 */     boolean isEqual = true;
/*     */ 
/* 160 */     isEqual = (isEqual) && (this.name == null ? thatSubject.name == null : this.name.equals(thatSubject.name));
/* 161 */     isEqual = (isEqual) && (this.messageType.equals(thatSubject.messageType));
/* 162 */     isEqual = (isEqual) && (this.nameScope.equals(thatSubject.nameScope));
/* 163 */     isEqual = (isEqual) && (this.parent == null ? thatSubject.parent == null : this.parent.equals(thatSubject.parent));
/*     */ 
/* 165 */     return isEqual;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 170 */     int result = 23;
/*     */ 
/* 172 */     result = 31 * result + (this.name == null ? 0 : this.name.hashCode());
/* 173 */     result = 31 * result + this.messageType.hashCode();
/* 174 */     result = 31 * result + this.nameScope.hashCode();
/* 175 */     result = 31 * result + (this.parent == null ? 0 : this.parent.hashCode());
/*     */ 
/* 177 */     return result;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 182 */     StringBuilder result = new StringBuilder("WsdlBindingSubject[");
/* 183 */     result.append(this.name).append(", ").append(this.messageType);
/* 184 */     result.append(", ").append(this.nameScope).append(", ").append(this.parent);
/* 185 */     return "]";
/*     */   }
/*     */ 
/*     */   public static enum WsdlMessageType
/*     */   {
/*  47 */     NO_MESSAGE, 
/*  48 */     INPUT, 
/*  49 */     OUTPUT, 
/*  50 */     FAULT;
/*     */   }
/*     */ 
/*     */   public static enum WsdlNameScope
/*     */   {
/*  61 */     SERVICE, 
/*  62 */     ENDPOINT, 
/*  63 */     OPERATION, 
/*  64 */     MESSAGE;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.policy.subject.WsdlBindingSubject
 * JD-Core Version:    0.6.2
 */