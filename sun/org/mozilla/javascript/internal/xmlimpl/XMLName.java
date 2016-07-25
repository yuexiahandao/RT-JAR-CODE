/*     */ package sun.org.mozilla.javascript.internal.xmlimpl;
/*     */ 
/*     */ import sun.org.mozilla.javascript.internal.Context;
/*     */ import sun.org.mozilla.javascript.internal.EcmaError;
/*     */ import sun.org.mozilla.javascript.internal.Kit;
/*     */ import sun.org.mozilla.javascript.internal.Ref;
/*     */ import sun.org.mozilla.javascript.internal.ScriptRuntime;
/*     */ import sun.org.mozilla.javascript.internal.Undefined;
/*     */ 
/*     */ class XMLName extends Ref
/*     */ {
/*     */   static final long serialVersionUID = 3832176310755686977L;
/*     */   private XmlNode.QName qname;
/*     */   private boolean isAttributeName;
/*     */   private boolean isDescendants;
/*     */   private XMLObjectImpl xmlObject;
/*     */ 
/*     */   private static boolean isNCNameStartChar(int paramInt)
/*     */   {
/*  49 */     if ((paramInt & 0xFFFFFF80) == 0)
/*     */     {
/*  51 */       if (paramInt >= 97)
/*  52 */         return paramInt <= 122;
/*  53 */       if (paramInt >= 65) {
/*  54 */         if (paramInt <= 90) {
/*  55 */           return true;
/*     */         }
/*  57 */         return paramInt == 95;
/*     */       }
/*  59 */     } else if ((paramInt & 0xFFFFE000) == 0) {
/*  60 */       return ((192 <= paramInt) && (paramInt <= 214)) || ((216 <= paramInt) && (paramInt <= 246)) || ((248 <= paramInt) && (paramInt <= 767)) || ((880 <= paramInt) && (paramInt <= 893)) || (895 <= paramInt);
/*     */     }
/*     */ 
/*  66 */     return ((8204 <= paramInt) && (paramInt <= 8205)) || ((8304 <= paramInt) && (paramInt <= 8591)) || ((11264 <= paramInt) && (paramInt <= 12271)) || ((12289 <= paramInt) && (paramInt <= 55295)) || ((63744 <= paramInt) && (paramInt <= 64975)) || ((65008 <= paramInt) && (paramInt <= 65533)) || ((65536 <= paramInt) && (paramInt <= 983039));
/*     */   }
/*     */ 
/*     */   private static boolean isNCNameChar(int paramInt)
/*     */   {
/*  76 */     if ((paramInt & 0xFFFFFF80) == 0)
/*     */     {
/*  78 */       if (paramInt >= 97)
/*  79 */         return paramInt <= 122;
/*  80 */       if (paramInt >= 65) {
/*  81 */         if (paramInt <= 90) {
/*  82 */           return true;
/*     */         }
/*  84 */         return paramInt == 95;
/*  85 */       }if (paramInt >= 48) {
/*  86 */         return paramInt <= 57;
/*     */       }
/*  88 */       return (paramInt == 45) || (paramInt == 46);
/*     */     }
/*  90 */     if ((paramInt & 0xFFFFE000) == 0) {
/*  91 */       return (isNCNameStartChar(paramInt)) || (paramInt == 183) || ((768 <= paramInt) && (paramInt <= 879));
/*     */     }
/*     */ 
/*  94 */     return (isNCNameStartChar(paramInt)) || ((8255 <= paramInt) && (paramInt <= 8256));
/*     */   }
/*     */ 
/*     */   static boolean accept(Object paramObject)
/*     */   {
/*     */     String str;
/*     */     try
/*     */     {
/* 102 */       str = ScriptRuntime.toString(paramObject);
/*     */     } catch (EcmaError localEcmaError) {
/* 104 */       if ("TypeError".equals(localEcmaError.getName())) {
/* 105 */         return false;
/*     */       }
/* 107 */       throw localEcmaError;
/*     */     }
/*     */ 
/* 111 */     int i = str.length();
/* 112 */     if ((i != 0) && 
/* 113 */       (isNCNameStartChar(str.charAt(0)))) {
/* 114 */       for (int j = 1; j != i; j++) {
/* 115 */         if (!isNCNameChar(str.charAt(j))) {
/* 116 */           return false;
/*     */         }
/*     */       }
/* 119 */       return true;
/*     */     }
/*     */ 
/* 123 */     return false;
/*     */   }
/*     */ 
/*     */   static XMLName formStar()
/*     */   {
/* 135 */     XMLName localXMLName = new XMLName();
/* 136 */     localXMLName.qname = XmlNode.QName.create(null, null);
/* 137 */     return localXMLName;
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   static XMLName formProperty(XmlNode.Namespace paramNamespace, String paramString) {
/* 142 */     if ((paramString != null) && (paramString.equals("*"))) paramString = null;
/* 143 */     XMLName localXMLName = new XMLName();
/* 144 */     localXMLName.qname = XmlNode.QName.create(paramNamespace, paramString);
/* 145 */     return localXMLName;
/*     */   }
/*     */ 
/*     */   static XMLName formProperty(String paramString1, String paramString2)
/*     */   {
/* 150 */     return formProperty(XmlNode.Namespace.create(paramString1), paramString2);
/*     */   }
/*     */ 
/*     */   static XMLName create(String paramString1, String paramString2)
/*     */   {
/* 155 */     if (paramString2 == null) {
/* 156 */       throw new IllegalArgumentException();
/*     */     }
/* 158 */     int i = paramString2.length();
/* 159 */     if (i != 0) {
/* 160 */       int j = paramString2.charAt(0);
/* 161 */       if (j == 42) {
/* 162 */         if (i == 1)
/* 163 */           return formStar();
/*     */       }
/* 165 */       else if (j == 64) {
/* 166 */         XMLName localXMLName = formProperty("", paramString2.substring(1));
/* 167 */         localXMLName.setAttributeName();
/* 168 */         return localXMLName;
/*     */       }
/*     */     }
/*     */ 
/* 172 */     return formProperty(paramString1, paramString2);
/*     */   }
/*     */ 
/*     */   static XMLName create(XmlNode.QName paramQName, boolean paramBoolean1, boolean paramBoolean2) {
/* 176 */     XMLName localXMLName = new XMLName();
/* 177 */     localXMLName.qname = paramQName;
/* 178 */     localXMLName.isAttributeName = paramBoolean1;
/* 179 */     localXMLName.isDescendants = paramBoolean2;
/* 180 */     return localXMLName;
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   static XMLName create(XmlNode.QName paramQName) {
/* 185 */     return create(paramQName, false, false);
/*     */   }
/*     */ 
/*     */   void initXMLObject(XMLObjectImpl paramXMLObjectImpl) {
/* 189 */     if (paramXMLObjectImpl == null) throw new IllegalArgumentException();
/* 190 */     if (this.xmlObject != null) throw new IllegalStateException();
/* 191 */     this.xmlObject = paramXMLObjectImpl;
/*     */   }
/*     */ 
/*     */   String uri() {
/* 195 */     if (this.qname.getNamespace() == null) return null;
/* 196 */     return this.qname.getNamespace().getUri();
/*     */   }
/*     */ 
/*     */   String localName() {
/* 200 */     if (this.qname.getLocalName() == null) return "*";
/* 201 */     return this.qname.getLocalName();
/*     */   }
/*     */ 
/*     */   private void addDescendantChildren(XMLList paramXMLList, XML paramXML) {
/* 205 */     XMLName localXMLName = this;
/* 206 */     if (paramXML.isElement()) {
/* 207 */       XML[] arrayOfXML = paramXML.getChildren();
/* 208 */       for (int i = 0; i < arrayOfXML.length; i++) {
/* 209 */         if (localXMLName.matches(arrayOfXML[i])) {
/* 210 */           paramXMLList.addToList(arrayOfXML[i]);
/*     */         }
/* 212 */         addDescendantChildren(paramXMLList, arrayOfXML[i]);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   void addMatchingAttributes(XMLList paramXMLList, XML paramXML) {
/* 218 */     XMLName localXMLName = this;
/* 219 */     if (paramXML.isElement()) {
/* 220 */       XML[] arrayOfXML = paramXML.getAttributes();
/* 221 */       for (int i = 0; i < arrayOfXML.length; i++)
/* 222 */         if (localXMLName.matches(arrayOfXML[i]))
/* 223 */           paramXMLList.addToList(arrayOfXML[i]);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void addDescendantAttributes(XMLList paramXMLList, XML paramXML)
/*     */   {
/* 230 */     if (paramXML.isElement()) {
/* 231 */       addMatchingAttributes(paramXMLList, paramXML);
/* 232 */       XML[] arrayOfXML = paramXML.getChildren();
/* 233 */       for (int i = 0; i < arrayOfXML.length; i++)
/* 234 */         addDescendantAttributes(paramXMLList, arrayOfXML[i]);
/*     */     }
/*     */   }
/*     */ 
/*     */   XMLList matchDescendantAttributes(XMLList paramXMLList, XML paramXML)
/*     */   {
/* 240 */     paramXMLList.setTargets(paramXML, null);
/* 241 */     addDescendantAttributes(paramXMLList, paramXML);
/* 242 */     return paramXMLList;
/*     */   }
/*     */ 
/*     */   XMLList matchDescendantChildren(XMLList paramXMLList, XML paramXML) {
/* 246 */     paramXMLList.setTargets(paramXML, null);
/* 247 */     addDescendantChildren(paramXMLList, paramXML);
/* 248 */     return paramXMLList;
/*     */   }
/*     */ 
/*     */   void addDescendants(XMLList paramXMLList, XML paramXML) {
/* 252 */     XMLName localXMLName = this;
/* 253 */     if (localXMLName.isAttributeName())
/* 254 */       matchDescendantAttributes(paramXMLList, paramXML);
/*     */     else
/* 256 */       matchDescendantChildren(paramXMLList, paramXML);
/*     */   }
/*     */ 
/*     */   private void addAttributes(XMLList paramXMLList, XML paramXML)
/*     */   {
/* 261 */     addMatchingAttributes(paramXMLList, paramXML);
/*     */   }
/*     */ 
/*     */   void addMatches(XMLList paramXMLList, XML paramXML) {
/* 265 */     if (isDescendants()) {
/* 266 */       addDescendants(paramXMLList, paramXML);
/* 267 */     } else if (isAttributeName()) {
/* 268 */       addAttributes(paramXMLList, paramXML);
/*     */     } else {
/* 270 */       XML[] arrayOfXML = paramXML.getChildren();
/* 271 */       if (arrayOfXML != null) {
/* 272 */         for (int i = 0; i < arrayOfXML.length; i++) {
/* 273 */           if (matches(arrayOfXML[i])) {
/* 274 */             paramXMLList.addToList(arrayOfXML[i]);
/*     */           }
/*     */         }
/*     */       }
/* 278 */       paramXMLList.setTargets(paramXML, toQname());
/*     */     }
/*     */   }
/*     */ 
/*     */   XMLList getMyValueOn(XML paramXML) {
/* 283 */     XMLList localXMLList = paramXML.newXMLList();
/* 284 */     addMatches(localXMLList, paramXML);
/* 285 */     return localXMLList;
/*     */   }
/*     */ 
/*     */   void setMyValueOn(XML paramXML, Object paramObject)
/*     */   {
/* 290 */     if (paramObject == null)
/* 291 */       paramObject = "null";
/* 292 */     else if ((paramObject instanceof Undefined)) {
/* 293 */       paramObject = "undefined";
/*     */     }
/*     */ 
/* 296 */     XMLName localXMLName = this;
/*     */ 
/* 298 */     if (localXMLName.isAttributeName()) {
/* 299 */       paramXML.setAttribute(localXMLName, paramObject);
/* 300 */     } else if ((localXMLName.uri() == null) && (localXMLName.localName().equals("*"))) {
/* 301 */       paramXML.setChildren(paramObject);
/*     */     }
/*     */     else {
/* 304 */       Object localObject = null;
/*     */ 
/* 306 */       if ((paramObject instanceof XMLObjectImpl)) {
/* 307 */         localObject = (XMLObjectImpl)paramObject;
/*     */ 
/* 310 */         if (((localObject instanceof XML)) && 
/* 311 */           (((XML)localObject).isAttribute())) {
/* 312 */           localObject = paramXML.makeXmlFromString(localXMLName, ((XMLObjectImpl)localObject).toString());
/*     */         }
/*     */ 
/* 317 */         if ((localObject instanceof XMLList))
/* 318 */           for (int i = 0; i < ((XMLObjectImpl)localObject).length(); i++) {
/* 319 */             XML localXML1 = ((XMLList)localObject).item(i);
/*     */ 
/* 321 */             if (localXML1.isAttribute())
/* 322 */               ((XMLList)localObject).replace(i, paramXML.makeXmlFromString(localXMLName, localXML1.toString()));
/*     */           }
/*     */       }
/*     */       else
/*     */       {
/* 327 */         localObject = paramXML.makeXmlFromString(localXMLName, ScriptRuntime.toString(paramObject));
/*     */       }
/*     */ 
/* 330 */       XMLList localXMLList = paramXML.getPropertyList(localXMLName);
/*     */ 
/* 332 */       if (localXMLList.length() == 0) {
/* 333 */         paramXML.appendChild(localObject);
/*     */       }
/*     */       else {
/* 336 */         for (int j = 1; j < localXMLList.length(); j++) {
/* 337 */           paramXML.removeChild(localXMLList.item(j).childIndex());
/*     */         }
/*     */ 
/* 341 */         XML localXML2 = localXMLList.item(0);
/* 342 */         paramXML.replace(localXML2.childIndex(), localObject);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean has(Context paramContext)
/*     */   {
/* 349 */     if (this.xmlObject == null) {
/* 350 */       return false;
/*     */     }
/* 352 */     return this.xmlObject.hasXMLProperty(this);
/*     */   }
/*     */ 
/*     */   public Object get(Context paramContext)
/*     */   {
/* 357 */     if (this.xmlObject == null) {
/* 358 */       throw ScriptRuntime.undefReadError(Undefined.instance, toString());
/*     */     }
/*     */ 
/* 361 */     return this.xmlObject.getXMLProperty(this);
/*     */   }
/*     */ 
/*     */   public Object set(Context paramContext, Object paramObject)
/*     */   {
/* 366 */     if (this.xmlObject == null) {
/* 367 */       throw ScriptRuntime.undefWriteError(Undefined.instance, toString(), paramObject);
/*     */     }
/*     */ 
/* 373 */     if (this.isDescendants) throw Kit.codeBug();
/* 374 */     this.xmlObject.putXMLProperty(this, paramObject);
/* 375 */     return paramObject;
/*     */   }
/*     */ 
/*     */   public boolean delete(Context paramContext)
/*     */   {
/* 380 */     if (this.xmlObject == null) {
/* 381 */       return true;
/*     */     }
/* 383 */     this.xmlObject.deleteXMLProperty(this);
/* 384 */     return !this.xmlObject.hasXMLProperty(this);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 390 */     StringBuffer localStringBuffer = new StringBuffer();
/* 391 */     if (this.isDescendants) localStringBuffer.append("..");
/* 392 */     if (this.isAttributeName) localStringBuffer.append('@');
/* 393 */     if (uri() == null) {
/* 394 */       localStringBuffer.append('*');
/* 395 */       if (localName().equals("*"))
/* 396 */         return localStringBuffer.toString();
/*     */     }
/*     */     else {
/* 399 */       localStringBuffer.append('"').append(uri()).append('"');
/*     */     }
/* 401 */     localStringBuffer.append(':').append(localName());
/* 402 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   final XmlNode.QName toQname() {
/* 406 */     return this.qname;
/*     */   }
/*     */ 
/*     */   final boolean matchesLocalName(String paramString) {
/* 410 */     return (localName().equals("*")) || (localName().equals(paramString));
/*     */   }
/*     */ 
/*     */   final boolean matchesElement(XmlNode.QName paramQName) {
/* 414 */     if (((uri() == null) || (uri().equals(paramQName.getNamespace().getUri()))) && (
/* 415 */       (localName().equals("*")) || (localName().equals(paramQName.getLocalName())))) {
/* 416 */       return true;
/*     */     }
/*     */ 
/* 419 */     return false;
/*     */   }
/*     */ 
/*     */   final boolean matches(XML paramXML) {
/* 423 */     XmlNode.QName localQName = paramXML.getNodeQname();
/* 424 */     String str = null;
/* 425 */     if (localQName.getNamespace() != null) {
/* 426 */       str = localQName.getNamespace().getUri();
/*     */     }
/* 428 */     if (this.isAttributeName) {
/* 429 */       if (paramXML.isAttribute()) {
/* 430 */         if (((uri() == null) || (uri().equals(str))) && (
/* 431 */           (localName().equals("*")) || (localName().equals(localQName.getLocalName())))) {
/* 432 */           return true;
/*     */         }
/*     */ 
/* 435 */         return false;
/*     */       }
/*     */ 
/* 439 */       return false;
/*     */     }
/*     */ 
/* 442 */     if ((uri() == null) || ((paramXML.isElement()) && (uri().equals(str)))) {
/* 443 */       if (localName().equals("*")) return true;
/* 444 */       if ((paramXML.isElement()) && 
/* 445 */         (localName().equals(localQName.getLocalName()))) return true;
/*     */     }
/*     */ 
/* 448 */     return false;
/*     */   }
/*     */ 
/*     */   boolean isAttributeName()
/*     */   {
/* 454 */     return this.isAttributeName;
/*     */   }
/*     */ 
/*     */   void setAttributeName()
/*     */   {
/* 461 */     this.isAttributeName = true;
/*     */   }
/*     */ 
/*     */   boolean isDescendants()
/*     */   {
/* 466 */     return this.isDescendants;
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   void setIsDescendants()
/*     */   {
/* 473 */     this.isDescendants = true;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.xmlimpl.XMLName
 * JD-Core Version:    0.6.2
 */