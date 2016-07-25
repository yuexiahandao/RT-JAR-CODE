/*     */ package com.sun.xml.internal.fastinfoset.sax;
/*     */ 
/*     */ import com.sun.xml.internal.fastinfoset.CommonResourceBundle;
/*     */ import com.sun.xml.internal.fastinfoset.QualifiedName;
/*     */ import com.sun.xml.internal.fastinfoset.algorithm.BuiltInEncodingAlgorithmFactory;
/*     */ import com.sun.xml.internal.org.jvnet.fastinfoset.EncodingAlgorithm;
/*     */ import com.sun.xml.internal.org.jvnet.fastinfoset.EncodingAlgorithmException;
/*     */ import com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetException;
/*     */ import com.sun.xml.internal.org.jvnet.fastinfoset.sax.EncodingAlgorithmAttributes;
/*     */ import java.io.IOException;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class AttributesHolder
/*     */   implements EncodingAlgorithmAttributes
/*     */ {
/*     */   private static final int DEFAULT_CAPACITY = 8;
/*     */   private Map _registeredEncodingAlgorithms;
/*     */   private int _attributeCount;
/*     */   private QualifiedName[] _names;
/*     */   private String[] _values;
/*     */   private String[] _algorithmURIs;
/*     */   private int[] _algorithmIds;
/*     */   private Object[] _algorithmData;
/*     */ 
/*     */   public AttributesHolder()
/*     */   {
/*  58 */     this._names = new QualifiedName[8];
/*  59 */     this._values = new String[8];
/*     */ 
/*  61 */     this._algorithmURIs = new String[8];
/*  62 */     this._algorithmIds = new int[8];
/*  63 */     this._algorithmData = new Object[8];
/*     */   }
/*     */ 
/*     */   public AttributesHolder(Map registeredEncodingAlgorithms) {
/*  67 */     this();
/*  68 */     this._registeredEncodingAlgorithms = registeredEncodingAlgorithms;
/*     */   }
/*     */ 
/*     */   public final int getLength()
/*     */   {
/*  74 */     return this._attributeCount;
/*     */   }
/*     */ 
/*     */   public final String getLocalName(int index) {
/*  78 */     return this._names[index].localName;
/*     */   }
/*     */ 
/*     */   public final String getQName(int index) {
/*  82 */     return this._names[index].getQNameString();
/*     */   }
/*     */ 
/*     */   public final String getType(int index) {
/*  86 */     return "CDATA";
/*     */   }
/*     */ 
/*     */   public final String getURI(int index) {
/*  90 */     return this._names[index].namespaceName;
/*     */   }
/*     */ 
/*     */   public final String getValue(int index) {
/*  94 */     String value = this._values[index];
/*  95 */     if (value != null) {
/*  96 */       return value;
/*     */     }
/*     */ 
/*  99 */     if ((this._algorithmData[index] == null) || ((this._algorithmIds[index] >= 32) && (this._registeredEncodingAlgorithms == null)))
/*     */     {
/* 102 */       return null;
/*     */     }
/*     */     try
/*     */     {
/* 106 */       return this._values[index] =  = convertEncodingAlgorithmDataToString(this._algorithmIds[index], this._algorithmURIs[index], this._algorithmData[index]).toString();
/*     */     }
/*     */     catch (IOException e)
/*     */     {
/* 111 */       return null; } catch (FastInfosetException e) {
/*     */     }
/* 113 */     return null;
/*     */   }
/*     */ 
/*     */   public final int getIndex(String qName)
/*     */   {
/* 118 */     int i = qName.indexOf(':');
/* 119 */     String prefix = "";
/* 120 */     String localName = qName;
/* 121 */     if (i >= 0) {
/* 122 */       prefix = qName.substring(0, i);
/* 123 */       localName = qName.substring(i + 1);
/*     */     }
/*     */ 
/* 126 */     for (i = 0; i < this._attributeCount; i++) {
/* 127 */       QualifiedName name = this._names[i];
/* 128 */       if ((localName.equals(name.localName)) && (prefix.equals(name.prefix)))
/*     */       {
/* 130 */         return i;
/*     */       }
/*     */     }
/* 133 */     return -1;
/*     */   }
/*     */ 
/*     */   public final String getType(String qName) {
/* 137 */     int index = getIndex(qName);
/* 138 */     if (index >= 0) {
/* 139 */       return "CDATA";
/*     */     }
/* 141 */     return null;
/*     */   }
/*     */ 
/*     */   public final String getValue(String qName)
/*     */   {
/* 146 */     int index = getIndex(qName);
/* 147 */     if (index >= 0) {
/* 148 */       return this._values[index];
/*     */     }
/* 150 */     return null;
/*     */   }
/*     */ 
/*     */   public final int getIndex(String uri, String localName)
/*     */   {
/* 155 */     for (int i = 0; i < this._attributeCount; i++) {
/* 156 */       QualifiedName name = this._names[i];
/* 157 */       if ((localName.equals(name.localName)) && (uri.equals(name.namespaceName)))
/*     */       {
/* 159 */         return i;
/*     */       }
/*     */     }
/* 162 */     return -1;
/*     */   }
/*     */ 
/*     */   public final String getType(String uri, String localName) {
/* 166 */     int index = getIndex(uri, localName);
/* 167 */     if (index >= 0) {
/* 168 */       return "CDATA";
/*     */     }
/* 170 */     return null;
/*     */   }
/*     */ 
/*     */   public final String getValue(String uri, String localName)
/*     */   {
/* 175 */     int index = getIndex(uri, localName);
/* 176 */     if (index >= 0) {
/* 177 */       return this._values[index];
/*     */     }
/* 179 */     return null;
/*     */   }
/*     */ 
/*     */   public final void clear()
/*     */   {
/* 184 */     for (int i = 0; i < this._attributeCount; i++) {
/* 185 */       this._values[i] = null;
/* 186 */       this._algorithmData[i] = null;
/*     */     }
/* 188 */     this._attributeCount = 0;
/*     */   }
/*     */ 
/*     */   public final String getAlgorithmURI(int index)
/*     */   {
/* 194 */     return this._algorithmURIs[index];
/*     */   }
/*     */ 
/*     */   public final int getAlgorithmIndex(int index) {
/* 198 */     return this._algorithmIds[index];
/*     */   }
/*     */ 
/*     */   public final Object getAlgorithmData(int index) {
/* 202 */     return this._algorithmData[index];
/*     */   }
/*     */ 
/*     */   public String getAlpababet(int index) {
/* 206 */     return null;
/*     */   }
/*     */ 
/*     */   public boolean getToIndex(int index) {
/* 210 */     return false;
/*     */   }
/*     */ 
/*     */   public final void addAttribute(QualifiedName name, String value)
/*     */   {
/* 216 */     if (this._attributeCount == this._names.length) {
/* 217 */       resize();
/*     */     }
/* 219 */     this._names[this._attributeCount] = name;
/* 220 */     this._values[(this._attributeCount++)] = value;
/*     */   }
/*     */ 
/*     */   public final void addAttributeWithAlgorithmData(QualifiedName name, String URI, int id, Object data) {
/* 224 */     if (this._attributeCount == this._names.length) {
/* 225 */       resize();
/*     */     }
/* 227 */     this._names[this._attributeCount] = name;
/* 228 */     this._values[this._attributeCount] = null;
/*     */ 
/* 230 */     this._algorithmURIs[this._attributeCount] = URI;
/* 231 */     this._algorithmIds[this._attributeCount] = id;
/* 232 */     this._algorithmData[(this._attributeCount++)] = data;
/*     */   }
/*     */ 
/*     */   public final QualifiedName getQualifiedName(int index) {
/* 236 */     return this._names[index];
/*     */   }
/*     */ 
/*     */   public final String getPrefix(int index) {
/* 240 */     return this._names[index].prefix;
/*     */   }
/*     */ 
/*     */   private final void resize() {
/* 244 */     int newLength = this._attributeCount * 3 / 2 + 1;
/*     */ 
/* 246 */     QualifiedName[] names = new QualifiedName[newLength];
/* 247 */     String[] values = new String[newLength];
/*     */ 
/* 249 */     String[] algorithmURIs = new String[newLength];
/* 250 */     int[] algorithmIds = new int[newLength];
/* 251 */     Object[] algorithmData = new Object[newLength];
/*     */ 
/* 253 */     System.arraycopy(this._names, 0, names, 0, this._attributeCount);
/* 254 */     System.arraycopy(this._values, 0, values, 0, this._attributeCount);
/*     */ 
/* 256 */     System.arraycopy(this._algorithmURIs, 0, algorithmURIs, 0, this._attributeCount);
/* 257 */     System.arraycopy(this._algorithmIds, 0, algorithmIds, 0, this._attributeCount);
/* 258 */     System.arraycopy(this._algorithmData, 0, algorithmData, 0, this._attributeCount);
/*     */ 
/* 260 */     this._names = names;
/* 261 */     this._values = values;
/*     */ 
/* 263 */     this._algorithmURIs = algorithmURIs;
/* 264 */     this._algorithmIds = algorithmIds;
/* 265 */     this._algorithmData = algorithmData;
/*     */   }
/*     */ 
/*     */   private final StringBuffer convertEncodingAlgorithmDataToString(int identifier, String URI, Object data) throws FastInfosetException, IOException {
/* 269 */     EncodingAlgorithm ea = null;
/* 270 */     if (identifier < 9) {
/* 271 */       ea = BuiltInEncodingAlgorithmFactory.getAlgorithm(identifier); } else {
/* 272 */       if (identifier == 9)
/* 273 */         throw new EncodingAlgorithmException(CommonResourceBundle.getInstance().getString("message.CDATAAlgorithmNotSupported"));
/* 274 */       if (identifier >= 32) {
/* 275 */         if (URI == null) {
/* 276 */           throw new EncodingAlgorithmException(CommonResourceBundle.getInstance().getString("message.URINotPresent") + identifier);
/*     */         }
/*     */ 
/* 279 */         ea = (EncodingAlgorithm)this._registeredEncodingAlgorithms.get(URI);
/* 280 */         if (ea == null) {
/* 281 */           throw new EncodingAlgorithmException(CommonResourceBundle.getInstance().getString("message.algorithmNotRegistered") + URI);
/*     */         }
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/* 287 */         throw new EncodingAlgorithmException(CommonResourceBundle.getInstance().getString("message.identifiers10to31Reserved"));
/*     */       }
/*     */     }
/* 290 */     StringBuffer sb = new StringBuffer();
/* 291 */     ea.convertToCharacters(data, sb);
/* 292 */     return sb;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.fastinfoset.sax.AttributesHolder
 * JD-Core Version:    0.6.2
 */