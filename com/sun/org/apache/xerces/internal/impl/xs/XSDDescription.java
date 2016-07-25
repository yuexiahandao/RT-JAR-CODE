/*     */ package com.sun.org.apache.xerces.internal.impl.xs;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.util.XMLResourceIdentifierImpl;
/*     */ import com.sun.org.apache.xerces.internal.xni.QName;
/*     */ import com.sun.org.apache.xerces.internal.xni.XMLAttributes;
/*     */ import com.sun.org.apache.xerces.internal.xni.grammars.XMLSchemaDescription;
/*     */ 
/*     */ public class XSDDescription extends XMLResourceIdentifierImpl
/*     */   implements XMLSchemaDescription
/*     */ {
/*     */   public static final short CONTEXT_INITIALIZE = -1;
/*     */   public static final short CONTEXT_INCLUDE = 0;
/*     */   public static final short CONTEXT_REDEFINE = 1;
/*     */   public static final short CONTEXT_IMPORT = 2;
/*     */   public static final short CONTEXT_PREPARSE = 3;
/*     */   public static final short CONTEXT_INSTANCE = 4;
/*     */   public static final short CONTEXT_ELEMENT = 5;
/*     */   public static final short CONTEXT_ATTRIBUTE = 6;
/*     */   public static final short CONTEXT_XSITYPE = 7;
/*     */   protected short fContextType;
/*     */   protected String[] fLocationHints;
/*     */   protected QName fTriggeringComponent;
/*     */   protected QName fEnclosedElementName;
/*     */   protected XMLAttributes fAttributes;
/*     */ 
/*     */   public String getGrammarType()
/*     */   {
/* 110 */     return "http://www.w3.org/2001/XMLSchema";
/*     */   }
/*     */ 
/*     */   public short getContextType()
/*     */   {
/* 120 */     return this.fContextType;
/*     */   }
/*     */ 
/*     */   public String getTargetNamespace()
/*     */   {
/* 131 */     return this.fNamespace;
/*     */   }
/*     */ 
/*     */   public String[] getLocationHints()
/*     */   {
/* 143 */     return this.fLocationHints;
/*     */   }
/*     */ 
/*     */   public QName getTriggeringComponent()
/*     */   {
/* 154 */     return this.fTriggeringComponent;
/*     */   }
/*     */ 
/*     */   public QName getEnclosingElementName()
/*     */   {
/* 164 */     return this.fEnclosedElementName;
/*     */   }
/*     */ 
/*     */   public XMLAttributes getAttributes()
/*     */   {
/* 174 */     return this.fAttributes;
/*     */   }
/*     */ 
/*     */   public boolean fromInstance() {
/* 178 */     return (this.fContextType == 6) || (this.fContextType == 5) || (this.fContextType == 4) || (this.fContextType == 7);
/*     */   }
/*     */ 
/*     */   public boolean isExternal()
/*     */   {
/* 188 */     return (this.fContextType == 0) || (this.fContextType == 1) || (this.fContextType == 2) || (this.fContextType == 5) || (this.fContextType == 6) || (this.fContextType == 7);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object descObj)
/*     */   {
/* 203 */     if (!(descObj instanceof XMLSchemaDescription)) return false;
/* 204 */     XMLSchemaDescription desc = (XMLSchemaDescription)descObj;
/* 205 */     if (this.fNamespace != null) {
/* 206 */       return this.fNamespace.equals(desc.getTargetNamespace());
/*     */     }
/* 208 */     return desc.getTargetNamespace() == null;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 217 */     return this.fNamespace == null ? 0 : this.fNamespace.hashCode();
/*     */   }
/*     */ 
/*     */   public void setContextType(short contextType) {
/* 221 */     this.fContextType = contextType;
/*     */   }
/*     */ 
/*     */   public void setTargetNamespace(String targetNamespace) {
/* 225 */     this.fNamespace = targetNamespace;
/*     */   }
/*     */ 
/*     */   public void setLocationHints(String[] locationHints) {
/* 229 */     int length = locationHints.length;
/* 230 */     this.fLocationHints = new String[length];
/* 231 */     System.arraycopy(locationHints, 0, this.fLocationHints, 0, length);
/*     */   }
/*     */ 
/*     */   public void setTriggeringComponent(QName triggeringComponent)
/*     */   {
/* 236 */     this.fTriggeringComponent = triggeringComponent;
/*     */   }
/*     */ 
/*     */   public void setEnclosingElementName(QName enclosedElementName) {
/* 240 */     this.fEnclosedElementName = enclosedElementName;
/*     */   }
/*     */ 
/*     */   public void setAttributes(XMLAttributes attributes) {
/* 244 */     this.fAttributes = attributes;
/*     */   }
/*     */ 
/*     */   public void reset()
/*     */   {
/* 251 */     super.clear();
/* 252 */     this.fContextType = -1;
/* 253 */     this.fLocationHints = null;
/* 254 */     this.fTriggeringComponent = null;
/* 255 */     this.fEnclosedElementName = null;
/* 256 */     this.fAttributes = null;
/*     */   }
/*     */ 
/*     */   public XSDDescription makeClone() {
/* 260 */     XSDDescription desc = new XSDDescription();
/* 261 */     desc.fAttributes = this.fAttributes;
/* 262 */     desc.fBaseSystemId = this.fBaseSystemId;
/* 263 */     desc.fContextType = this.fContextType;
/* 264 */     desc.fEnclosedElementName = this.fEnclosedElementName;
/* 265 */     desc.fExpandedSystemId = this.fExpandedSystemId;
/* 266 */     desc.fLiteralSystemId = this.fLiteralSystemId;
/* 267 */     desc.fLocationHints = this.fLocationHints;
/* 268 */     desc.fPublicId = this.fPublicId;
/* 269 */     desc.fNamespace = this.fNamespace;
/* 270 */     desc.fTriggeringComponent = this.fTriggeringComponent;
/* 271 */     return desc;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.xs.XSDDescription
 * JD-Core Version:    0.6.2
 */