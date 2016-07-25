/*     */ package com.sun.xml.internal.ws.policy.sourcemodel;
/*     */ 
/*     */ import com.sun.xml.internal.ws.policy.PolicyConstants;
/*     */ import com.sun.xml.internal.ws.policy.privateutil.LocalizationMessages;
/*     */ import com.sun.xml.internal.ws.policy.privateutil.PolicyLogger;
/*     */ import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.Text;
/*     */ import java.io.Serializable;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import javax.xml.namespace.QName;
/*     */ 
/*     */ public final class AssertionData
/*     */   implements Cloneable, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 4416256070795526315L;
/*  53 */   private static final PolicyLogger LOGGER = PolicyLogger.getLogger(AssertionData.class);
/*     */   private final QName name;
/*     */   private final String value;
/*     */   private final Map<QName, String> attributes;
/*     */   private ModelNode.Type type;
/*     */   private boolean optional;
/*     */   private boolean ignorable;
/*     */ 
/*     */   public static AssertionData createAssertionData(QName name)
/*     */     throws IllegalArgumentException
/*     */   {
/*  74 */     return new AssertionData(name, null, null, ModelNode.Type.ASSERTION, false, false);
/*     */   }
/*     */ 
/*     */   public static AssertionData createAssertionParameterData(QName name)
/*     */     throws IllegalArgumentException
/*     */   {
/*  88 */     return new AssertionData(name, null, null, ModelNode.Type.ASSERTION_PARAMETER_NODE, false, false);
/*     */   }
/*     */ 
/*     */   public static AssertionData createAssertionData(QName name, String value, Map<QName, String> attributes, boolean optional, boolean ignorable)
/*     */     throws IllegalArgumentException
/*     */   {
/* 106 */     return new AssertionData(name, value, attributes, ModelNode.Type.ASSERTION, optional, ignorable);
/*     */   }
/*     */ 
/*     */   public static AssertionData createAssertionParameterData(QName name, String value, Map<QName, String> attributes)
/*     */     throws IllegalArgumentException
/*     */   {
/* 122 */     return new AssertionData(name, value, attributes, ModelNode.Type.ASSERTION_PARAMETER_NODE, false, false);
/*     */   }
/*     */ 
/*     */   AssertionData(QName name, String value, Map<QName, String> attributes, ModelNode.Type type, boolean optional, boolean ignorable)
/*     */     throws IllegalArgumentException
/*     */   {
/* 143 */     this.name = name;
/* 144 */     this.value = value;
/* 145 */     this.optional = optional;
/* 146 */     this.ignorable = ignorable;
/*     */ 
/* 148 */     this.attributes = new HashMap();
/* 149 */     if ((attributes != null) && (!attributes.isEmpty())) {
/* 150 */       this.attributes.putAll(attributes);
/*     */     }
/* 152 */     setModelNodeType(type);
/*     */   }
/*     */ 
/*     */   private void setModelNodeType(ModelNode.Type type) throws IllegalArgumentException {
/* 156 */     if ((type == ModelNode.Type.ASSERTION) || (type == ModelNode.Type.ASSERTION_PARAMETER_NODE))
/* 157 */       this.type = type;
/*     */     else
/* 159 */       throw ((IllegalArgumentException)LOGGER.logSevereException(new IllegalArgumentException(LocalizationMessages.WSP_0074_CANNOT_CREATE_ASSERTION_BAD_TYPE(type, ModelNode.Type.ASSERTION, ModelNode.Type.ASSERTION_PARAMETER_NODE))));
/*     */   }
/*     */ 
/*     */   AssertionData(AssertionData data)
/*     */   {
/* 170 */     this.name = data.name;
/* 171 */     this.value = data.value;
/* 172 */     this.attributes = new HashMap();
/* 173 */     if (!data.attributes.isEmpty()) {
/* 174 */       this.attributes.putAll(data.attributes);
/*     */     }
/* 176 */     this.type = data.type;
/*     */   }
/*     */ 
/*     */   protected AssertionData clone() throws CloneNotSupportedException
/*     */   {
/* 181 */     return (AssertionData)super.clone();
/*     */   }
/*     */ 
/*     */   public boolean containsAttribute(QName name)
/*     */   {
/* 191 */     synchronized (this.attributes) {
/* 192 */       return this.attributes.containsKey(name);
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean equals(Object obj)
/*     */   {
/* 199 */     if (this == obj) {
/* 200 */       return true;
/*     */     }
/*     */ 
/* 203 */     if (!(obj instanceof AssertionData)) {
/* 204 */       return false;
/*     */     }
/*     */ 
/* 207 */     boolean result = true;
/* 208 */     AssertionData that = (AssertionData)obj;
/*     */ 
/* 210 */     result = (result) && (this.name.equals(that.name));
/* 211 */     result = (result) && (this.value == null ? that.value == null : this.value.equals(that.value));
/* 212 */     synchronized (this.attributes) {
/* 213 */       result = (result) && (this.attributes.equals(that.attributes));
/*     */     }
/*     */ 
/* 216 */     return result;
/*     */   }
/*     */ 
/*     */   public String getAttributeValue(QName name)
/*     */   {
/* 229 */     synchronized (this.attributes) {
/* 230 */       return (String)this.attributes.get(name);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Map<QName, String> getAttributes()
/*     */   {
/* 245 */     synchronized (this.attributes) {
/* 246 */       return new HashMap(this.attributes);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Set<Map.Entry<QName, String>> getAttributesSet()
/*     */   {
/* 262 */     synchronized (this.attributes) {
/* 263 */       return new HashSet(this.attributes.entrySet());
/*     */     }
/*     */   }
/*     */ 
/*     */   public QName getName()
/*     */   {
/* 274 */     return this.name;
/*     */   }
/*     */ 
/*     */   public String getValue()
/*     */   {
/* 284 */     return this.value;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 293 */     int result = 17;
/*     */ 
/* 295 */     result = 37 * result + this.name.hashCode();
/* 296 */     result = 37 * result + (this.value == null ? 0 : this.value.hashCode());
/* 297 */     synchronized (this.attributes) {
/* 298 */       result = 37 * result + this.attributes.hashCode();
/*     */     }
/* 300 */     return result;
/*     */   }
/*     */ 
/*     */   public boolean isPrivateAttributeSet()
/*     */   {
/* 311 */     return "private".equals(getAttributeValue(PolicyConstants.VISIBILITY_ATTRIBUTE));
/*     */   }
/*     */ 
/*     */   public String removeAttribute(QName name)
/*     */   {
/* 321 */     synchronized (this.attributes) {
/* 322 */       return (String)this.attributes.remove(name);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setAttribute(QName name, String value)
/*     */   {
/* 333 */     synchronized (this.attributes) {
/* 334 */       this.attributes.put(name, value);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setOptionalAttribute(boolean value)
/*     */   {
/* 344 */     this.optional = value;
/*     */   }
/*     */ 
/*     */   public boolean isOptionalAttributeSet()
/*     */   {
/* 353 */     return this.optional;
/*     */   }
/*     */ 
/*     */   public void setIgnorableAttribute(boolean value)
/*     */   {
/* 362 */     this.ignorable = value;
/*     */   }
/*     */ 
/*     */   public boolean isIgnorableAttributeSet()
/*     */   {
/* 371 */     return this.ignorable;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 376 */     return toString(0, new StringBuffer()).toString();
/*     */   }
/*     */ 
/*     */   public StringBuffer toString(int indentLevel, StringBuffer buffer)
/*     */   {
/* 387 */     String indent = PolicyUtils.Text.createIndent(indentLevel);
/* 388 */     String innerIndent = PolicyUtils.Text.createIndent(indentLevel + 1);
/* 389 */     String innerDoubleIndent = PolicyUtils.Text.createIndent(indentLevel + 2);
/*     */ 
/* 391 */     buffer.append(indent);
/* 392 */     if (this.type == ModelNode.Type.ASSERTION)
/* 393 */       buffer.append("assertion data {");
/*     */     else {
/* 395 */       buffer.append("assertion parameter data {");
/*     */     }
/* 397 */     buffer.append(PolicyUtils.Text.NEW_LINE);
/*     */ 
/* 399 */     buffer.append(innerIndent).append("namespace = '").append(this.name.getNamespaceURI()).append('\'').append(PolicyUtils.Text.NEW_LINE);
/* 400 */     buffer.append(innerIndent).append("prefix = '").append(this.name.getPrefix()).append('\'').append(PolicyUtils.Text.NEW_LINE);
/* 401 */     buffer.append(innerIndent).append("local name = '").append(this.name.getLocalPart()).append('\'').append(PolicyUtils.Text.NEW_LINE);
/* 402 */     buffer.append(innerIndent).append("value = '").append(this.value).append('\'').append(PolicyUtils.Text.NEW_LINE);
/* 403 */     buffer.append(innerIndent).append("optional = '").append(this.optional).append('\'').append(PolicyUtils.Text.NEW_LINE);
/* 404 */     buffer.append(innerIndent).append("ignorable = '").append(this.ignorable).append('\'').append(PolicyUtils.Text.NEW_LINE);
/* 405 */     synchronized (this.attributes) {
/* 406 */       if (this.attributes.isEmpty()) {
/* 407 */         buffer.append(innerIndent).append("no attributes");
/*     */       }
/*     */       else {
/* 410 */         buffer.append(innerIndent).append("attributes {").append(PolicyUtils.Text.NEW_LINE);
/* 411 */         for (Map.Entry entry : this.attributes.entrySet()) {
/* 412 */           QName aName = (QName)entry.getKey();
/* 413 */           buffer.append(innerDoubleIndent).append("name = '").append(aName.getNamespaceURI()).append(':').append(aName.getLocalPart());
/* 414 */           buffer.append("', value = '").append((String)entry.getValue()).append('\'').append(PolicyUtils.Text.NEW_LINE);
/*     */         }
/* 416 */         buffer.append(innerIndent).append('}');
/*     */       }
/*     */     }
/*     */ 
/* 420 */     buffer.append(PolicyUtils.Text.NEW_LINE).append(indent).append('}');
/*     */ 
/* 422 */     return buffer;
/*     */   }
/*     */ 
/*     */   public ModelNode.Type getNodeType() {
/* 426 */     return this.type;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.policy.sourcemodel.AssertionData
 * JD-Core Version:    0.6.2
 */