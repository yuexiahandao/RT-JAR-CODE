/*      */ package com.sun.org.apache.xerces.internal.impl.xs.traversers;
/*      */ 
/*      */ import com.sun.org.apache.xerces.internal.impl.dv.InvalidDatatypeValueException;
/*      */ import com.sun.org.apache.xerces.internal.impl.dv.XSSimpleType;
/*      */ import com.sun.org.apache.xerces.internal.impl.xs.SchemaGrammar;
/*      */ import com.sun.org.apache.xerces.internal.impl.xs.SchemaNamespaceSupport;
/*      */ import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
/*      */ import com.sun.org.apache.xerces.internal.impl.xs.XSAttributeDecl;
/*      */ import com.sun.org.apache.xerces.internal.impl.xs.XSGrammarBucket;
/*      */ import com.sun.org.apache.xerces.internal.impl.xs.util.XInt;
/*      */ import com.sun.org.apache.xerces.internal.impl.xs.util.XIntPool;
/*      */ import com.sun.org.apache.xerces.internal.util.DOMUtil;
/*      */ import com.sun.org.apache.xerces.internal.util.SymbolTable;
/*      */ import com.sun.org.apache.xerces.internal.util.XMLChar;
/*      */ import com.sun.org.apache.xerces.internal.util.XMLSymbols;
/*      */ import com.sun.org.apache.xerces.internal.utils.XMLSecurityManager;
/*      */ import com.sun.org.apache.xerces.internal.utils.XMLSecurityManager.Limit;
/*      */ import com.sun.org.apache.xerces.internal.xni.QName;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.Map;
/*      */ import java.util.Map.Entry;
/*      */ import java.util.Set;
/*      */ import java.util.StringTokenizer;
/*      */ import java.util.Vector;
/*      */ import org.w3c.dom.Attr;
/*      */ import org.w3c.dom.Element;
/*      */ import org.w3c.dom.Node;
/*      */ 
/*      */ public class XSAttributeChecker
/*      */ {
/*      */   private static final String ELEMENT_N = "element_n";
/*      */   private static final String ELEMENT_R = "element_r";
/*      */   private static final String ATTRIBUTE_N = "attribute_n";
/*      */   private static final String ATTRIBUTE_R = "attribute_r";
/*   86 */   private static int ATTIDX_COUNT = 0;
/*   87 */   public static final int ATTIDX_ABSTRACT = ATTIDX_COUNT++;
/*   88 */   public static final int ATTIDX_AFORMDEFAULT = ATTIDX_COUNT++;
/*   89 */   public static final int ATTIDX_BASE = ATTIDX_COUNT++;
/*   90 */   public static final int ATTIDX_BLOCK = ATTIDX_COUNT++;
/*   91 */   public static final int ATTIDX_BLOCKDEFAULT = ATTIDX_COUNT++;
/*   92 */   public static final int ATTIDX_DEFAULT = ATTIDX_COUNT++;
/*   93 */   public static final int ATTIDX_EFORMDEFAULT = ATTIDX_COUNT++;
/*   94 */   public static final int ATTIDX_FINAL = ATTIDX_COUNT++;
/*   95 */   public static final int ATTIDX_FINALDEFAULT = ATTIDX_COUNT++;
/*   96 */   public static final int ATTIDX_FIXED = ATTIDX_COUNT++;
/*   97 */   public static final int ATTIDX_FORM = ATTIDX_COUNT++;
/*   98 */   public static final int ATTIDX_ID = ATTIDX_COUNT++;
/*   99 */   public static final int ATTIDX_ITEMTYPE = ATTIDX_COUNT++;
/*  100 */   public static final int ATTIDX_MAXOCCURS = ATTIDX_COUNT++;
/*  101 */   public static final int ATTIDX_MEMBERTYPES = ATTIDX_COUNT++;
/*  102 */   public static final int ATTIDX_MINOCCURS = ATTIDX_COUNT++;
/*  103 */   public static final int ATTIDX_MIXED = ATTIDX_COUNT++;
/*  104 */   public static final int ATTIDX_NAME = ATTIDX_COUNT++;
/*  105 */   public static final int ATTIDX_NAMESPACE = ATTIDX_COUNT++;
/*  106 */   public static final int ATTIDX_NAMESPACE_LIST = ATTIDX_COUNT++;
/*  107 */   public static final int ATTIDX_NILLABLE = ATTIDX_COUNT++;
/*  108 */   public static final int ATTIDX_NONSCHEMA = ATTIDX_COUNT++;
/*  109 */   public static final int ATTIDX_PROCESSCONTENTS = ATTIDX_COUNT++;
/*  110 */   public static final int ATTIDX_PUBLIC = ATTIDX_COUNT++;
/*  111 */   public static final int ATTIDX_REF = ATTIDX_COUNT++;
/*  112 */   public static final int ATTIDX_REFER = ATTIDX_COUNT++;
/*  113 */   public static final int ATTIDX_SCHEMALOCATION = ATTIDX_COUNT++;
/*  114 */   public static final int ATTIDX_SOURCE = ATTIDX_COUNT++;
/*  115 */   public static final int ATTIDX_SUBSGROUP = ATTIDX_COUNT++;
/*  116 */   public static final int ATTIDX_SYSTEM = ATTIDX_COUNT++;
/*  117 */   public static final int ATTIDX_TARGETNAMESPACE = ATTIDX_COUNT++;
/*  118 */   public static final int ATTIDX_TYPE = ATTIDX_COUNT++;
/*  119 */   public static final int ATTIDX_USE = ATTIDX_COUNT++;
/*  120 */   public static final int ATTIDX_VALUE = ATTIDX_COUNT++;
/*  121 */   public static final int ATTIDX_ENUMNSDECLS = ATTIDX_COUNT++;
/*  122 */   public static final int ATTIDX_VERSION = ATTIDX_COUNT++;
/*  123 */   public static final int ATTIDX_XML_LANG = ATTIDX_COUNT++;
/*  124 */   public static final int ATTIDX_XPATH = ATTIDX_COUNT++;
/*  125 */   public static final int ATTIDX_FROMDEFAULT = ATTIDX_COUNT++;
/*      */ 
/*  127 */   public static final int ATTIDX_ISRETURNED = ATTIDX_COUNT++;
/*      */ 
/*  129 */   private static final XIntPool fXIntPool = new XIntPool();
/*      */ 
/*  131 */   private static final XInt INT_QUALIFIED = fXIntPool.getXInt(1);
/*  132 */   private static final XInt INT_UNQUALIFIED = fXIntPool.getXInt(0);
/*  133 */   private static final XInt INT_EMPTY_SET = fXIntPool.getXInt(0);
/*  134 */   private static final XInt INT_ANY_STRICT = fXIntPool.getXInt(1);
/*  135 */   private static final XInt INT_ANY_LAX = fXIntPool.getXInt(3);
/*  136 */   private static final XInt INT_ANY_SKIP = fXIntPool.getXInt(2);
/*  137 */   private static final XInt INT_ANY_ANY = fXIntPool.getXInt(1);
/*  138 */   private static final XInt INT_ANY_LIST = fXIntPool.getXInt(3);
/*  139 */   private static final XInt INT_ANY_NOT = fXIntPool.getXInt(2);
/*  140 */   private static final XInt INT_USE_OPTIONAL = fXIntPool.getXInt(0);
/*  141 */   private static final XInt INT_USE_REQUIRED = fXIntPool.getXInt(1);
/*  142 */   private static final XInt INT_USE_PROHIBITED = fXIntPool.getXInt(2);
/*  143 */   private static final XInt INT_WS_PRESERVE = fXIntPool.getXInt(0);
/*  144 */   private static final XInt INT_WS_REPLACE = fXIntPool.getXInt(1);
/*  145 */   private static final XInt INT_WS_COLLAPSE = fXIntPool.getXInt(2);
/*  146 */   private static final XInt INT_UNBOUNDED = fXIntPool.getXInt(-1);
/*      */ 
/*  150 */   private static final Map fEleAttrsMapG = new HashMap(29);
/*      */ 
/*  152 */   private static final Map fEleAttrsMapL = new HashMap(79);
/*      */   protected static final int DT_ANYURI = 0;
/*      */   protected static final int DT_ID = 1;
/*      */   protected static final int DT_QNAME = 2;
/*      */   protected static final int DT_STRING = 3;
/*      */   protected static final int DT_TOKEN = 4;
/*      */   protected static final int DT_NCNAME = 5;
/*      */   protected static final int DT_XPATH = 6;
/*      */   protected static final int DT_XPATH1 = 7;
/*      */   protected static final int DT_LANGUAGE = 8;
/*      */   protected static final int DT_COUNT = 9;
/*  171 */   private static final XSSimpleType[] fExtraDVs = new XSSimpleType[9];
/*      */   protected static final int DT_BLOCK = -1;
/*      */   protected static final int DT_BLOCK1 = -2;
/*      */   protected static final int DT_FINAL = -3;
/*      */   protected static final int DT_FINAL1 = -4;
/*      */   protected static final int DT_FINAL2 = -5;
/*      */   protected static final int DT_FORM = -6;
/*      */   protected static final int DT_MAXOCCURS = -7;
/*      */   protected static final int DT_MAXOCCURS1 = -8;
/*      */   protected static final int DT_MEMBERTYPES = -9;
/*      */   protected static final int DT_MINOCCURS1 = -10;
/*      */   protected static final int DT_NAMESPACE = -11;
/*      */   protected static final int DT_PROCESSCONTENTS = -12;
/*      */   protected static final int DT_USE = -13;
/*      */   protected static final int DT_WHITESPACE = -14;
/*      */   protected static final int DT_BOOLEAN = -15;
/*      */   protected static final int DT_NONNEGINT = -16;
/*      */   protected static final int DT_POSINT = -17;
/*  925 */   protected XSDHandler fSchemaHandler = null;
/*      */ 
/*  928 */   protected SymbolTable fSymbolTable = null;
/*      */ 
/*  931 */   protected Map fNonSchemaAttrs = new HashMap();
/*      */ 
/*  934 */   protected Vector fNamespaceList = new Vector();
/*      */ 
/*  937 */   protected boolean[] fSeen = new boolean[ATTIDX_COUNT];
/*  938 */   private static boolean[] fSeenTemp = new boolean[ATTIDX_COUNT];
/*      */   static final int INIT_POOL_SIZE = 10;
/*      */   static final int INC_POOL_SIZE = 10;
/* 1678 */   Object[][] fArrayPool = new Object[10][ATTIDX_COUNT];
/*      */ 
/* 1681 */   private static Object[] fTempArray = new Object[ATTIDX_COUNT];
/*      */ 
/* 1683 */   int fPoolPos = 0;
/*      */ 
/*      */   public XSAttributeChecker(XSDHandler schemaHandler)
/*      */   {
/*  942 */     this.fSchemaHandler = schemaHandler;
/*      */   }
/*      */ 
/*      */   public void reset(SymbolTable symbolTable) {
/*  946 */     this.fSymbolTable = symbolTable;
/*  947 */     this.fNonSchemaAttrs.clear();
/*      */   }
/*      */ 
/*      */   public Object[] checkAttributes(Element element, boolean isGlobal, XSDocumentInfo schemaDoc)
/*      */   {
/*  962 */     return checkAttributes(element, isGlobal, schemaDoc, false);
/*      */   }
/*      */ 
/*      */   public Object[] checkAttributes(Element element, boolean isGlobal, XSDocumentInfo schemaDoc, boolean enumAsQName)
/*      */   {
/*  981 */     if (element == null) {
/*  982 */       return null;
/*      */     }
/*      */ 
/*  985 */     Attr[] attrs = DOMUtil.getAttrs(element);
/*      */ 
/*  988 */     resolveNamespace(element, attrs, schemaDoc.fNamespaceSupport);
/*      */ 
/*  990 */     String uri = DOMUtil.getNamespaceURI(element);
/*  991 */     String elName = DOMUtil.getLocalName(element);
/*      */ 
/*  993 */     if (!SchemaSymbols.URI_SCHEMAFORSCHEMA.equals(uri)) {
/*  994 */       reportSchemaError("s4s-elt-schema-ns", new Object[] { elName }, element);
/*      */     }
/*      */ 
/*  997 */     Map eleAttrsMap = fEleAttrsMapG;
/*  998 */     String lookupName = elName;
/*      */ 
/* 1004 */     if (!isGlobal) {
/* 1005 */       eleAttrsMap = fEleAttrsMapL;
/* 1006 */       if (elName.equals(SchemaSymbols.ELT_ELEMENT)) {
/* 1007 */         if (DOMUtil.getAttr(element, SchemaSymbols.ATT_REF) != null)
/* 1008 */           lookupName = "element_r";
/*      */         else
/* 1010 */           lookupName = "element_n";
/* 1011 */       } else if (elName.equals(SchemaSymbols.ELT_ATTRIBUTE)) {
/* 1012 */         if (DOMUtil.getAttr(element, SchemaSymbols.ATT_REF) != null)
/* 1013 */           lookupName = "attribute_r";
/*      */         else {
/* 1015 */           lookupName = "attribute_n";
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1020 */     Container attrList = (Container)eleAttrsMap.get(lookupName);
/* 1021 */     if (attrList == null)
/*      */     {
/* 1025 */       reportSchemaError("s4s-elt-invalid", new Object[] { elName }, element);
/* 1026 */       return null;
/*      */     }
/*      */ 
/* 1030 */     Object[] attrValues = getAvailableArray();
/*      */ 
/* 1032 */     long fromDefault = 0L;
/*      */ 
/* 1035 */     System.arraycopy(fSeenTemp, 0, this.fSeen, 0, ATTIDX_COUNT);
/*      */ 
/* 1038 */     int length = attrs.length;
/* 1039 */     Attr sattr = null;
/* 1040 */     for (int i = 0; i < length; i++) {
/* 1041 */       sattr = attrs[i];
/*      */ 
/* 1044 */       String attrName = sattr.getName();
/* 1045 */       String attrURI = DOMUtil.getNamespaceURI(sattr);
/* 1046 */       String attrVal = DOMUtil.getValue(sattr);
/*      */ 
/* 1048 */       if (attrName.startsWith("xml")) {
/* 1049 */         String attrPrefix = DOMUtil.getPrefix(sattr);
/*      */ 
/* 1051 */         if (("xmlns".equals(attrPrefix)) || ("xmlns".equals(attrName)))
/*      */         {
/*      */           continue;
/*      */         }
/*      */ 
/* 1057 */         if ((SchemaSymbols.ATT_XML_LANG.equals(attrName)) && ((SchemaSymbols.ELT_SCHEMA.equals(elName)) || (SchemaSymbols.ELT_DOCUMENTATION.equals(elName))))
/*      */         {
/* 1060 */           attrURI = null;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1066 */       if ((attrURI != null) && (attrURI.length() != 0))
/*      */       {
/* 1069 */         if (attrURI.equals(SchemaSymbols.URI_SCHEMAFORSCHEMA)) {
/* 1070 */           reportSchemaError("s4s-att-not-allowed", new Object[] { elName, attrName }, element);
/*      */         }
/*      */         else {
/* 1073 */           if (attrValues[ATTIDX_NONSCHEMA] == null)
/*      */           {
/* 1075 */             attrValues[ATTIDX_NONSCHEMA] = new Vector(4, 2);
/*      */           }
/* 1077 */           ((Vector)attrValues[ATTIDX_NONSCHEMA]).addElement(attrName);
/* 1078 */           ((Vector)attrValues[ATTIDX_NONSCHEMA]).addElement(attrVal);
/*      */         }
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/* 1102 */         OneAttr oneAttr = attrList.get(attrName);
/* 1103 */         if (oneAttr == null) {
/* 1104 */           reportSchemaError("s4s-att-not-allowed", new Object[] { elName, attrName }, element);
/*      */         }
/*      */         else
/*      */         {
/* 1111 */           this.fSeen[oneAttr.valueIndex] = true;
/*      */           try
/*      */           {
/* 1118 */             if (oneAttr.dvIndex >= 0) {
/* 1119 */               if ((oneAttr.dvIndex != 3) && (oneAttr.dvIndex != 6) && (oneAttr.dvIndex != 7))
/*      */               {
/* 1122 */                 XSSimpleType dv = fExtraDVs[oneAttr.dvIndex];
/* 1123 */                 Object avalue = dv.validate(attrVal, schemaDoc.fValidationContext, null);
/*      */ 
/* 1125 */                 if (oneAttr.dvIndex == 2) {
/* 1126 */                   QName qname = (QName)avalue;
/* 1127 */                   if ((qname.prefix == XMLSymbols.EMPTY_STRING) && (qname.uri == null) && (schemaDoc.fIsChameleonSchema))
/* 1128 */                     qname.uri = schemaDoc.fTargetNamespace;
/*      */                 }
/* 1130 */                 attrValues[oneAttr.valueIndex] = avalue;
/*      */               } else {
/* 1132 */                 attrValues[oneAttr.valueIndex] = attrVal;
/*      */               }
/*      */             }
/*      */             else
/* 1136 */               attrValues[oneAttr.valueIndex] = validate(attrValues, attrName, attrVal, oneAttr.dvIndex, schemaDoc);
/*      */           }
/*      */           catch (InvalidDatatypeValueException ide) {
/* 1139 */             reportSchemaError("s4s-att-invalid-value", new Object[] { elName, attrName, ide.getMessage() }, element);
/*      */ 
/* 1142 */             if (oneAttr.dfltValue != null)
/*      */             {
/* 1144 */               attrValues[oneAttr.valueIndex] = oneAttr.dfltValue;
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/* 1149 */           if ((elName.equals(SchemaSymbols.ELT_ENUMERATION)) && (enumAsQName)) {
/* 1150 */             attrValues[ATTIDX_ENUMNSDECLS] = new SchemaNamespaceSupport(schemaDoc.fNamespaceSupport);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 1155 */     OneAttr[] reqAttrs = attrList.values;
/* 1156 */     for (int i = 0; i < reqAttrs.length; i++) {
/* 1157 */       OneAttr oneAttr = reqAttrs[i];
/*      */ 
/* 1161 */       if ((oneAttr.dfltValue != null) && (this.fSeen[oneAttr.valueIndex] == 0))
/*      */       {
/* 1163 */         attrValues[oneAttr.valueIndex] = oneAttr.dfltValue;
/* 1164 */         fromDefault |= 1 << oneAttr.valueIndex;
/*      */       }
/*      */     }
/*      */ 
/* 1168 */     attrValues[ATTIDX_FROMDEFAULT] = new Long(fromDefault);
/*      */ 
/* 1173 */     if (attrValues[ATTIDX_MAXOCCURS] != null) {
/* 1174 */       int min = ((XInt)attrValues[ATTIDX_MINOCCURS]).intValue();
/* 1175 */       int max = ((XInt)attrValues[ATTIDX_MAXOCCURS]).intValue();
/* 1176 */       if (max != -1)
/*      */       {
/* 1179 */         if (this.fSchemaHandler.fSecureProcessing != null) {
/* 1180 */           String localName = element.getLocalName();
/*      */ 
/* 1189 */           boolean optimize = ((localName.equals("element")) || (localName.equals("any"))) && (element.getNextSibling() == null) && (element.getPreviousSibling() == null) && (element.getParentNode().getLocalName().equals("sequence"));
/*      */ 
/* 1195 */           if (!optimize)
/*      */           {
/* 1198 */             int maxOccurNodeLimit = this.fSchemaHandler.fSecureProcessing.getLimit(XMLSecurityManager.Limit.MAX_OCCUR_NODE_LIMIT);
/* 1199 */             if ((max > maxOccurNodeLimit) && (!this.fSchemaHandler.fSecureProcessing.isNoLimit(maxOccurNodeLimit))) {
/* 1200 */               reportSchemaFatalError("maxOccurLimit", new Object[] { new Integer(maxOccurNodeLimit) }, element);
/*      */ 
/* 1203 */               attrValues[ATTIDX_MAXOCCURS] = fXIntPool.getXInt(maxOccurNodeLimit);
/*      */ 
/* 1205 */               max = maxOccurNodeLimit;
/*      */             }
/*      */           }
/*      */         }
/*      */ 
/* 1210 */         if (min > max) {
/* 1211 */           reportSchemaError("p-props-correct.2.1", new Object[] { elName, attrValues[ATTIDX_MINOCCURS], attrValues[ATTIDX_MAXOCCURS] }, element);
/*      */ 
/* 1214 */           attrValues[ATTIDX_MINOCCURS] = attrValues[ATTIDX_MAXOCCURS];
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1219 */     return attrValues;
/*      */   }
/*      */ 
/*      */   private Object validate(Object[] attrValues, String attr, String ivalue, int dvIndex, XSDocumentInfo schemaDoc) throws InvalidDatatypeValueException
/*      */   {
/* 1224 */     if (ivalue == null) {
/* 1225 */       return null;
/*      */     }
/*      */ 
/* 1232 */     String value = XMLChar.trim(ivalue);
/* 1233 */     Object retValue = null;
/*      */     int choice;
/* 1237 */     switch (dvIndex) {
/*      */     case -15:
/* 1239 */       if ((value.equals("false")) || (value.equals("0")))
/*      */       {
/* 1241 */         retValue = Boolean.FALSE;
/* 1242 */       } else if ((value.equals("true")) || (value.equals("1")))
/*      */       {
/* 1244 */         retValue = Boolean.TRUE;
/*      */       }
/* 1246 */       else throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.1", new Object[] { value, "boolean" });
/*      */       break;
/*      */     case -16:
/*      */       try
/*      */       {
/* 1251 */         if ((value.length() > 0) && (value.charAt(0) == '+'))
/* 1252 */           value = value.substring(1);
/* 1253 */         retValue = fXIntPool.getXInt(Integer.parseInt(value));
/*      */       } catch (NumberFormatException e) {
/* 1255 */         throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.1", new Object[] { value, "nonNegativeInteger" });
/*      */       }
/* 1257 */       if (((XInt)retValue).intValue() < 0)
/* 1258 */         throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.1", new Object[] { value, "nonNegativeInteger" });
/*      */       break;
/*      */     case -17:
/*      */       try {
/* 1262 */         if ((value.length() > 0) && (value.charAt(0) == '+'))
/* 1263 */           value = value.substring(1);
/* 1264 */         retValue = fXIntPool.getXInt(Integer.parseInt(value));
/*      */       } catch (NumberFormatException e) {
/* 1266 */         throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.1", new Object[] { value, "positiveInteger" });
/*      */       }
/* 1268 */       if (((XInt)retValue).intValue() <= 0) {
/* 1269 */         throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.1", new Object[] { value, "positiveInteger" });
/*      */       }
/*      */       break;
/*      */     case -1:
/* 1273 */       choice = 0;
/* 1274 */       if (value.equals("#all")) {
/* 1275 */         choice = 7;
/*      */       }
/*      */       else
/*      */       {
/* 1279 */         StringTokenizer t = new StringTokenizer(value, " \n\t\r");
/* 1280 */         while (t.hasMoreTokens()) {
/* 1281 */           String token = t.nextToken();
/*      */ 
/* 1283 */           if (token.equals("extension")) {
/* 1284 */             choice |= 1;
/*      */           }
/* 1286 */           else if (token.equals("restriction")) {
/* 1287 */             choice |= 2;
/*      */           }
/* 1289 */           else if (token.equals("substitution")) {
/* 1290 */             choice |= 4;
/*      */           }
/*      */           else {
/* 1293 */             throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.3", new Object[] { value, "(#all | List of (extension | restriction | substitution))" });
/*      */           }
/*      */         }
/*      */       }
/* 1297 */       retValue = fXIntPool.getXInt(choice);
/* 1298 */       break;
/*      */     case -3:
/*      */     case -2:
/* 1303 */       choice = 0;
/* 1304 */       if (value.equals("#all"))
/*      */       {
/* 1316 */         choice = 31;
/*      */       }
/*      */       else
/*      */       {
/* 1321 */         StringTokenizer t = new StringTokenizer(value, " \n\t\r");
/* 1322 */         while (t.hasMoreTokens()) {
/* 1323 */           String token = t.nextToken();
/*      */ 
/* 1325 */           if (token.equals("extension")) {
/* 1326 */             choice |= 1;
/*      */           }
/* 1328 */           else if (token.equals("restriction")) {
/* 1329 */             choice |= 2;
/*      */           }
/*      */           else {
/* 1332 */             throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.3", new Object[] { value, "(#all | List of (extension | restriction))" });
/*      */           }
/*      */         }
/*      */       }
/* 1336 */       retValue = fXIntPool.getXInt(choice);
/* 1337 */       break;
/*      */     case -4:
/* 1340 */       choice = 0;
/* 1341 */       if (value.equals("#all"))
/*      */       {
/* 1348 */         choice = 31;
/*      */       }
/*      */       else
/*      */       {
/* 1353 */         StringTokenizer t = new StringTokenizer(value, " \n\t\r");
/* 1354 */         while (t.hasMoreTokens()) {
/* 1355 */           String token = t.nextToken();
/*      */ 
/* 1357 */           if (token.equals("list")) {
/* 1358 */             choice |= 16;
/*      */           }
/* 1360 */           else if (token.equals("union")) {
/* 1361 */             choice |= 8;
/*      */           }
/* 1363 */           else if (token.equals("restriction")) {
/* 1364 */             choice |= 2;
/*      */           }
/*      */           else {
/* 1367 */             throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.3", new Object[] { value, "(#all | List of (list | union | restriction))" });
/*      */           }
/*      */         }
/*      */       }
/* 1371 */       retValue = fXIntPool.getXInt(choice);
/* 1372 */       break;
/*      */     case -5:
/* 1375 */       choice = 0;
/* 1376 */       if (value.equals("#all"))
/*      */       {
/* 1383 */         choice = 31;
/*      */       }
/*      */       else
/*      */       {
/* 1388 */         StringTokenizer t = new StringTokenizer(value, " \n\t\r");
/* 1389 */         while (t.hasMoreTokens()) {
/* 1390 */           String token = t.nextToken();
/*      */ 
/* 1392 */           if (token.equals("extension")) {
/* 1393 */             choice |= 1;
/*      */           }
/* 1395 */           else if (token.equals("restriction")) {
/* 1396 */             choice |= 2;
/*      */           }
/* 1398 */           else if (token.equals("list")) {
/* 1399 */             choice |= 16;
/*      */           }
/* 1401 */           else if (token.equals("union")) {
/* 1402 */             choice |= 8;
/*      */           }
/*      */           else {
/* 1405 */             throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.3", new Object[] { value, "(#all | List of (extension | restriction | list | union))" });
/*      */           }
/*      */         }
/*      */       }
/* 1409 */       retValue = fXIntPool.getXInt(choice);
/* 1410 */       break;
/*      */     case -6:
/* 1413 */       if (value.equals("qualified"))
/* 1414 */         retValue = INT_QUALIFIED;
/* 1415 */       else if (value.equals("unqualified"))
/* 1416 */         retValue = INT_UNQUALIFIED;
/*      */       else {
/* 1418 */         throw new InvalidDatatypeValueException("cvc-enumeration-valid", new Object[] { value, "(qualified | unqualified)" });
/*      */       }
/*      */ 
/*      */       break;
/*      */     case -7:
/* 1423 */       if (value.equals("unbounded"))
/* 1424 */         retValue = INT_UNBOUNDED;
/*      */       else {
/*      */         try {
/* 1427 */           retValue = validate(attrValues, attr, value, -16, schemaDoc);
/*      */         } catch (NumberFormatException e) {
/* 1429 */           throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.3", new Object[] { value, "(nonNegativeInteger | unbounded)" });
/*      */         }
/*      */       }
/*      */ 
/*      */       break;
/*      */     case -8:
/* 1435 */       if (value.equals("1"))
/* 1436 */         retValue = fXIntPool.getXInt(1);
/*      */       else {
/* 1438 */         throw new InvalidDatatypeValueException("cvc-enumeration-valid", new Object[] { value, "(1)" });
/*      */       }
/*      */ 
/*      */       break;
/*      */     case -9:
/* 1443 */       Vector memberType = new Vector();
/*      */       try {
/* 1445 */         StringTokenizer t = new StringTokenizer(value, " \n\t\r");
/* 1446 */         while (t.hasMoreTokens()) {
/* 1447 */           String token = t.nextToken();
/* 1448 */           QName qname = (QName)fExtraDVs[2].validate(token, schemaDoc.fValidationContext, null);
/*      */ 
/* 1450 */           if ((qname.prefix == XMLSymbols.EMPTY_STRING) && (qname.uri == null) && (schemaDoc.fIsChameleonSchema))
/* 1451 */             qname.uri = schemaDoc.fTargetNamespace;
/* 1452 */           memberType.addElement(qname);
/*      */         }
/* 1454 */         retValue = memberType;
/*      */       }
/*      */       catch (InvalidDatatypeValueException ide) {
/* 1457 */         throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.2", new Object[] { value, "(List of QName)" });
/*      */       }
/*      */ 
/*      */     case -10:
/* 1462 */       if (value.equals("0"))
/* 1463 */         retValue = fXIntPool.getXInt(0);
/* 1464 */       else if (value.equals("1"))
/* 1465 */         retValue = fXIntPool.getXInt(1);
/*      */       else {
/* 1467 */         throw new InvalidDatatypeValueException("cvc-enumeration-valid", new Object[] { value, "(0 | 1)" });
/*      */       }
/*      */ 
/*      */       break;
/*      */     case -11:
/* 1472 */       if (value.equals("##any"))
/*      */       {
/* 1474 */         retValue = INT_ANY_ANY;
/* 1475 */       } else if (value.equals("##other"))
/*      */       {
/* 1477 */         retValue = INT_ANY_NOT;
/* 1478 */         String[] list = new String[2];
/* 1479 */         list[0] = schemaDoc.fTargetNamespace;
/* 1480 */         list[1] = null;
/* 1481 */         attrValues[ATTIDX_NAMESPACE_LIST] = list;
/*      */       }
/*      */       else {
/* 1484 */         retValue = INT_ANY_LIST;
/*      */ 
/* 1486 */         this.fNamespaceList.removeAllElements();
/*      */ 
/* 1489 */         StringTokenizer tokens = new StringTokenizer(value, " \n\t\r");
/*      */         try
/*      */         {
/* 1493 */           while (tokens.hasMoreTokens()) {
/* 1494 */             String token = tokens.nextToken();
/*      */             String tempNamespace;
/*      */             String tempNamespace;
/* 1495 */             if (token.equals("##local")) {
/* 1496 */               tempNamespace = null;
/*      */             }
/*      */             else
/*      */             {
/*      */               String tempNamespace;
/* 1497 */               if (token.equals("##targetNamespace")) {
/* 1498 */                 tempNamespace = schemaDoc.fTargetNamespace;
/*      */               }
/*      */               else
/*      */               {
/* 1502 */                 fExtraDVs[0].validate(token, schemaDoc.fValidationContext, null);
/* 1503 */                 tempNamespace = this.fSymbolTable.addSymbol(token);
/*      */               }
/*      */             }
/*      */ 
/* 1507 */             if (!this.fNamespaceList.contains(tempNamespace))
/* 1508 */               this.fNamespaceList.addElement(tempNamespace);
/*      */           }
/*      */         }
/*      */         catch (InvalidDatatypeValueException ide) {
/* 1512 */           throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.3", new Object[] { value, "((##any | ##other) | List of (anyURI | (##targetNamespace | ##local)) )" });
/*      */         }
/*      */ 
/* 1516 */         int num = this.fNamespaceList.size();
/* 1517 */         String[] list = new String[num];
/* 1518 */         this.fNamespaceList.copyInto(list);
/* 1519 */         attrValues[ATTIDX_NAMESPACE_LIST] = list;
/*      */       }
/* 1521 */       break;
/*      */     case -12:
/* 1524 */       if (value.equals("strict"))
/* 1525 */         retValue = INT_ANY_STRICT;
/* 1526 */       else if (value.equals("lax"))
/* 1527 */         retValue = INT_ANY_LAX;
/* 1528 */       else if (value.equals("skip"))
/* 1529 */         retValue = INT_ANY_SKIP;
/*      */       else {
/* 1531 */         throw new InvalidDatatypeValueException("cvc-enumeration-valid", new Object[] { value, "(lax | skip | strict)" });
/*      */       }
/*      */ 
/*      */       break;
/*      */     case -13:
/* 1536 */       if (value.equals("optional"))
/* 1537 */         retValue = INT_USE_OPTIONAL;
/* 1538 */       else if (value.equals("required"))
/* 1539 */         retValue = INT_USE_REQUIRED;
/* 1540 */       else if (value.equals("prohibited"))
/* 1541 */         retValue = INT_USE_PROHIBITED;
/*      */       else {
/* 1543 */         throw new InvalidDatatypeValueException("cvc-enumeration-valid", new Object[] { value, "(optional | prohibited | required)" });
/*      */       }
/*      */ 
/*      */       break;
/*      */     case -14:
/* 1548 */       if (value.equals("preserve"))
/* 1549 */         retValue = INT_WS_PRESERVE;
/* 1550 */       else if (value.equals("replace"))
/* 1551 */         retValue = INT_WS_REPLACE;
/* 1552 */       else if (value.equals("collapse"))
/* 1553 */         retValue = INT_WS_COLLAPSE;
/*      */       else {
/* 1555 */         throw new InvalidDatatypeValueException("cvc-enumeration-valid", new Object[] { value, "(preserve | replace | collapse)" });
/*      */       }
/*      */       break;
/*      */     }
/*      */ 
/* 1560 */     return retValue;
/*      */   }
/*      */ 
/*      */   void reportSchemaFatalError(String key, Object[] args, Element ele) {
/* 1564 */     this.fSchemaHandler.reportSchemaFatalError(key, args, ele);
/*      */   }
/*      */ 
/*      */   void reportSchemaError(String key, Object[] args, Element ele) {
/* 1568 */     this.fSchemaHandler.reportSchemaError(key, args, ele);
/*      */   }
/*      */ 
/*      */   public void checkNonSchemaAttributes(XSGrammarBucket grammarBucket)
/*      */   {
/* 1577 */     Iterator entries = this.fNonSchemaAttrs.entrySet().iterator();
/*      */ 
/* 1579 */     while (entries.hasNext()) {
/* 1580 */       Map.Entry entry = (Map.Entry)entries.next();
/*      */ 
/* 1582 */       String attrRName = (String)entry.getKey();
/* 1583 */       String attrURI = attrRName.substring(0, attrRName.indexOf(','));
/* 1584 */       String attrLocal = attrRName.substring(attrRName.indexOf(',') + 1);
/*      */ 
/* 1586 */       SchemaGrammar sGrammar = grammarBucket.getGrammar(attrURI);
/* 1587 */       if (sGrammar != null)
/*      */       {
/* 1591 */         XSAttributeDecl attrDecl = sGrammar.getGlobalAttributeDecl(attrLocal);
/* 1592 */         if (attrDecl != null)
/*      */         {
/* 1595 */           XSSimpleType dv = (XSSimpleType)attrDecl.getTypeDefinition();
/* 1596 */           if (dv != null)
/*      */           {
/* 1601 */             Vector values = (Vector)entry.getValue();
/*      */ 
/* 1603 */             String attrName = (String)values.elementAt(0);
/*      */ 
/* 1605 */             int count = values.size();
/* 1606 */             for (int i = 1; i < count; i += 2) {
/* 1607 */               String elName = (String)values.elementAt(i);
/*      */               try
/*      */               {
/* 1612 */                 dv.validate((String)values.elementAt(i + 1), null, null);
/*      */               } catch (InvalidDatatypeValueException ide) {
/* 1614 */                 reportSchemaError("s4s-att-invalid-value", new Object[] { elName, attrName, ide.getMessage() }, null);
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public static String normalize(String content, short ws) {
/* 1624 */     int len = content == null ? 0 : content.length();
/* 1625 */     if ((len == 0) || (ws == 0)) {
/* 1626 */       return content;
/*      */     }
/* 1628 */     StringBuffer sb = new StringBuffer();
/* 1629 */     if (ws == 1)
/*      */     {
/* 1632 */       for (int i = 0; i < len; i++) {
/* 1633 */         char ch = content.charAt(i);
/* 1634 */         if ((ch != '\t') && (ch != '\n') && (ch != '\r'))
/* 1635 */           sb.append(ch);
/*      */         else
/* 1637 */           sb.append(' ');
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/* 1642 */       boolean isLeading = true;
/*      */ 
/* 1644 */       for (int i = 0; i < len; i++) {
/* 1645 */         char ch = content.charAt(i);
/*      */ 
/* 1647 */         if ((ch != '\t') && (ch != '\n') && (ch != '\r') && (ch != ' ')) {
/* 1648 */           sb.append(ch);
/* 1649 */           isLeading = false;
/*      */         }
/*      */         else
/*      */         {
/* 1653 */           for (; i < len - 1; i++) {
/* 1654 */             ch = content.charAt(i + 1);
/* 1655 */             if ((ch != '\t') && (ch != '\n') && (ch != '\r') && (ch != ' ')) {
/*      */               break;
/*      */             }
/*      */           }
/* 1659 */           if ((i < len - 1) && (!isLeading)) {
/* 1660 */             sb.append(' ');
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 1665 */     return sb.toString();
/*      */   }
/*      */ 
/*      */   protected Object[] getAvailableArray()
/*      */   {
/* 1688 */     if (this.fArrayPool.length == this.fPoolPos)
/*      */     {
/* 1690 */       this.fArrayPool = new Object[this.fPoolPos + 10][];
/*      */ 
/* 1692 */       for (int i = this.fPoolPos; i < this.fArrayPool.length; i++) {
/* 1693 */         this.fArrayPool[i] = new Object[ATTIDX_COUNT];
/*      */       }
/*      */     }
/* 1696 */     Object[] retArray = this.fArrayPool[this.fPoolPos];
/*      */ 
/* 1699 */     this.fArrayPool[(this.fPoolPos++)] = null;
/*      */ 
/* 1703 */     System.arraycopy(fTempArray, 0, retArray, 0, ATTIDX_COUNT - 1);
/* 1704 */     retArray[ATTIDX_ISRETURNED] = Boolean.FALSE;
/*      */ 
/* 1706 */     return retArray;
/*      */   }
/*      */ 
/*      */   public void returnAttrArray(Object[] attrArray, XSDocumentInfo schemaDoc)
/*      */   {
/* 1712 */     if (schemaDoc != null) {
/* 1713 */       schemaDoc.fNamespaceSupport.popContext();
/*      */     }
/*      */ 
/* 1718 */     if ((this.fPoolPos == 0) || (attrArray == null) || (attrArray.length != ATTIDX_COUNT) || (((Boolean)attrArray[ATTIDX_ISRETURNED]).booleanValue()))
/*      */     {
/* 1722 */       return;
/*      */     }
/*      */ 
/* 1726 */     attrArray[ATTIDX_ISRETURNED] = Boolean.TRUE;
/*      */ 
/* 1728 */     if (attrArray[ATTIDX_NONSCHEMA] != null) {
/* 1729 */       ((Vector)attrArray[ATTIDX_NONSCHEMA]).clear();
/*      */     }
/* 1731 */     this.fArrayPool[(--this.fPoolPos)] = attrArray;
/*      */   }
/*      */ 
/*      */   public void resolveNamespace(Element element, Attr[] attrs, SchemaNamespaceSupport nsSupport)
/*      */   {
/* 1737 */     nsSupport.pushContext();
/*      */ 
/* 1740 */     int length = attrs.length;
/* 1741 */     Attr sattr = null;
/*      */ 
/* 1743 */     for (int i = 0; i < length; i++) {
/* 1744 */       sattr = attrs[i];
/* 1745 */       String rawname = DOMUtil.getName(sattr);
/* 1746 */       String prefix = null;
/* 1747 */       if (rawname.equals(XMLSymbols.PREFIX_XMLNS))
/* 1748 */         prefix = XMLSymbols.EMPTY_STRING;
/* 1749 */       else if (rawname.startsWith("xmlns:"))
/* 1750 */         prefix = this.fSymbolTable.addSymbol(DOMUtil.getLocalName(sattr));
/* 1751 */       if (prefix != null) {
/* 1752 */         String uri = this.fSymbolTable.addSymbol(DOMUtil.getValue(sattr));
/* 1753 */         nsSupport.declarePrefix(prefix, uri.length() != 0 ? uri : null);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*  174 */     SchemaGrammar grammar = SchemaGrammar.SG_SchemaNS;
/*      */ 
/*  176 */     fExtraDVs[0] = ((XSSimpleType)grammar.getGlobalTypeDecl("anyURI"));
/*      */ 
/*  178 */     fExtraDVs[1] = ((XSSimpleType)grammar.getGlobalTypeDecl("ID"));
/*      */ 
/*  180 */     fExtraDVs[2] = ((XSSimpleType)grammar.getGlobalTypeDecl("QName"));
/*      */ 
/*  182 */     fExtraDVs[3] = ((XSSimpleType)grammar.getGlobalTypeDecl("string"));
/*      */ 
/*  184 */     fExtraDVs[4] = ((XSSimpleType)grammar.getGlobalTypeDecl("token"));
/*      */ 
/*  186 */     fExtraDVs[5] = ((XSSimpleType)grammar.getGlobalTypeDecl("NCName"));
/*      */ 
/*  188 */     fExtraDVs[6] = fExtraDVs[3];
/*      */ 
/*  190 */     fExtraDVs[6] = fExtraDVs[3];
/*      */ 
/*  192 */     fExtraDVs[8] = ((XSSimpleType)grammar.getGlobalTypeDecl("language"));
/*      */ 
/*  215 */     int attCount = 0;
/*  216 */     int ATT_ABSTRACT_D = attCount++;
/*  217 */     int ATT_ATTRIBUTE_FD_D = attCount++;
/*  218 */     int ATT_BASE_R = attCount++;
/*  219 */     int ATT_BASE_N = attCount++;
/*  220 */     int ATT_BLOCK_N = attCount++;
/*  221 */     int ATT_BLOCK1_N = attCount++;
/*  222 */     int ATT_BLOCK_D_D = attCount++;
/*  223 */     int ATT_DEFAULT_N = attCount++;
/*  224 */     int ATT_ELEMENT_FD_D = attCount++;
/*  225 */     int ATT_FINAL_N = attCount++;
/*  226 */     int ATT_FINAL1_N = attCount++;
/*  227 */     int ATT_FINAL_D_D = attCount++;
/*  228 */     int ATT_FIXED_N = attCount++;
/*  229 */     int ATT_FIXED_D = attCount++;
/*  230 */     int ATT_FORM_N = attCount++;
/*  231 */     int ATT_ID_N = attCount++;
/*  232 */     int ATT_ITEMTYPE_N = attCount++;
/*  233 */     int ATT_MAXOCCURS_D = attCount++;
/*  234 */     int ATT_MAXOCCURS1_D = attCount++;
/*  235 */     int ATT_MEMBER_T_N = attCount++;
/*  236 */     int ATT_MINOCCURS_D = attCount++;
/*  237 */     int ATT_MINOCCURS1_D = attCount++;
/*  238 */     int ATT_MIXED_D = attCount++;
/*  239 */     int ATT_MIXED_N = attCount++;
/*  240 */     int ATT_NAME_R = attCount++;
/*  241 */     int ATT_NAMESPACE_D = attCount++;
/*  242 */     int ATT_NAMESPACE_N = attCount++;
/*  243 */     int ATT_NILLABLE_D = attCount++;
/*  244 */     int ATT_PROCESS_C_D = attCount++;
/*  245 */     int ATT_PUBLIC_R = attCount++;
/*  246 */     int ATT_REF_R = attCount++;
/*  247 */     int ATT_REFER_R = attCount++;
/*  248 */     int ATT_SCHEMA_L_R = attCount++;
/*  249 */     int ATT_SCHEMA_L_N = attCount++;
/*  250 */     int ATT_SOURCE_N = attCount++;
/*  251 */     int ATT_SUBSTITUTION_G_N = attCount++;
/*  252 */     int ATT_SYSTEM_N = attCount++;
/*  253 */     int ATT_TARGET_N_N = attCount++;
/*  254 */     int ATT_TYPE_N = attCount++;
/*  255 */     int ATT_USE_D = attCount++;
/*  256 */     int ATT_VALUE_NNI_N = attCount++;
/*  257 */     int ATT_VALUE_PI_N = attCount++;
/*  258 */     int ATT_VALUE_STR_N = attCount++;
/*  259 */     int ATT_VALUE_WS_N = attCount++;
/*  260 */     int ATT_VERSION_N = attCount++;
/*  261 */     int ATT_XML_LANG = attCount++;
/*  262 */     int ATT_XPATH_R = attCount++;
/*  263 */     int ATT_XPATH1_R = attCount++;
/*      */ 
/*  266 */     OneAttr[] allAttrs = new OneAttr[attCount];
/*  267 */     allAttrs[ATT_ABSTRACT_D] = new OneAttr(SchemaSymbols.ATT_ABSTRACT, -15, ATTIDX_ABSTRACT, Boolean.FALSE);
/*      */ 
/*  271 */     allAttrs[ATT_ATTRIBUTE_FD_D] = new OneAttr(SchemaSymbols.ATT_ATTRIBUTEFORMDEFAULT, -6, ATTIDX_AFORMDEFAULT, INT_UNQUALIFIED);
/*      */ 
/*  275 */     allAttrs[ATT_BASE_R] = new OneAttr(SchemaSymbols.ATT_BASE, 2, ATTIDX_BASE, null);
/*      */ 
/*  279 */     allAttrs[ATT_BASE_N] = new OneAttr(SchemaSymbols.ATT_BASE, 2, ATTIDX_BASE, null);
/*      */ 
/*  283 */     allAttrs[ATT_BLOCK_N] = new OneAttr(SchemaSymbols.ATT_BLOCK, -1, ATTIDX_BLOCK, null);
/*      */ 
/*  287 */     allAttrs[ATT_BLOCK1_N] = new OneAttr(SchemaSymbols.ATT_BLOCK, -2, ATTIDX_BLOCK, null);
/*      */ 
/*  291 */     allAttrs[ATT_BLOCK_D_D] = new OneAttr(SchemaSymbols.ATT_BLOCKDEFAULT, -1, ATTIDX_BLOCKDEFAULT, INT_EMPTY_SET);
/*      */ 
/*  295 */     allAttrs[ATT_DEFAULT_N] = new OneAttr(SchemaSymbols.ATT_DEFAULT, 3, ATTIDX_DEFAULT, null);
/*      */ 
/*  299 */     allAttrs[ATT_ELEMENT_FD_D] = new OneAttr(SchemaSymbols.ATT_ELEMENTFORMDEFAULT, -6, ATTIDX_EFORMDEFAULT, INT_UNQUALIFIED);
/*      */ 
/*  303 */     allAttrs[ATT_FINAL_N] = new OneAttr(SchemaSymbols.ATT_FINAL, -3, ATTIDX_FINAL, null);
/*      */ 
/*  307 */     allAttrs[ATT_FINAL1_N] = new OneAttr(SchemaSymbols.ATT_FINAL, -4, ATTIDX_FINAL, null);
/*      */ 
/*  311 */     allAttrs[ATT_FINAL_D_D] = new OneAttr(SchemaSymbols.ATT_FINALDEFAULT, -5, ATTIDX_FINALDEFAULT, INT_EMPTY_SET);
/*      */ 
/*  315 */     allAttrs[ATT_FIXED_N] = new OneAttr(SchemaSymbols.ATT_FIXED, 3, ATTIDX_FIXED, null);
/*      */ 
/*  319 */     allAttrs[ATT_FIXED_D] = new OneAttr(SchemaSymbols.ATT_FIXED, -15, ATTIDX_FIXED, Boolean.FALSE);
/*      */ 
/*  323 */     allAttrs[ATT_FORM_N] = new OneAttr(SchemaSymbols.ATT_FORM, -6, ATTIDX_FORM, null);
/*      */ 
/*  327 */     allAttrs[ATT_ID_N] = new OneAttr(SchemaSymbols.ATT_ID, 1, ATTIDX_ID, null);
/*      */ 
/*  331 */     allAttrs[ATT_ITEMTYPE_N] = new OneAttr(SchemaSymbols.ATT_ITEMTYPE, 2, ATTIDX_ITEMTYPE, null);
/*      */ 
/*  335 */     allAttrs[ATT_MAXOCCURS_D] = new OneAttr(SchemaSymbols.ATT_MAXOCCURS, -7, ATTIDX_MAXOCCURS, fXIntPool.getXInt(1));
/*      */ 
/*  339 */     allAttrs[ATT_MAXOCCURS1_D] = new OneAttr(SchemaSymbols.ATT_MAXOCCURS, -8, ATTIDX_MAXOCCURS, fXIntPool.getXInt(1));
/*      */ 
/*  343 */     allAttrs[ATT_MEMBER_T_N] = new OneAttr(SchemaSymbols.ATT_MEMBERTYPES, -9, ATTIDX_MEMBERTYPES, null);
/*      */ 
/*  347 */     allAttrs[ATT_MINOCCURS_D] = new OneAttr(SchemaSymbols.ATT_MINOCCURS, -16, ATTIDX_MINOCCURS, fXIntPool.getXInt(1));
/*      */ 
/*  351 */     allAttrs[ATT_MINOCCURS1_D] = new OneAttr(SchemaSymbols.ATT_MINOCCURS, -10, ATTIDX_MINOCCURS, fXIntPool.getXInt(1));
/*      */ 
/*  355 */     allAttrs[ATT_MIXED_D] = new OneAttr(SchemaSymbols.ATT_MIXED, -15, ATTIDX_MIXED, Boolean.FALSE);
/*      */ 
/*  359 */     allAttrs[ATT_MIXED_N] = new OneAttr(SchemaSymbols.ATT_MIXED, -15, ATTIDX_MIXED, null);
/*      */ 
/*  363 */     allAttrs[ATT_NAME_R] = new OneAttr(SchemaSymbols.ATT_NAME, 5, ATTIDX_NAME, null);
/*      */ 
/*  367 */     allAttrs[ATT_NAMESPACE_D] = new OneAttr(SchemaSymbols.ATT_NAMESPACE, -11, ATTIDX_NAMESPACE, INT_ANY_ANY);
/*      */ 
/*  371 */     allAttrs[ATT_NAMESPACE_N] = new OneAttr(SchemaSymbols.ATT_NAMESPACE, 0, ATTIDX_NAMESPACE, null);
/*      */ 
/*  375 */     allAttrs[ATT_NILLABLE_D] = new OneAttr(SchemaSymbols.ATT_NILLABLE, -15, ATTIDX_NILLABLE, Boolean.FALSE);
/*      */ 
/*  379 */     allAttrs[ATT_PROCESS_C_D] = new OneAttr(SchemaSymbols.ATT_PROCESSCONTENTS, -12, ATTIDX_PROCESSCONTENTS, INT_ANY_STRICT);
/*      */ 
/*  383 */     allAttrs[ATT_PUBLIC_R] = new OneAttr(SchemaSymbols.ATT_PUBLIC, 4, ATTIDX_PUBLIC, null);
/*      */ 
/*  387 */     allAttrs[ATT_REF_R] = new OneAttr(SchemaSymbols.ATT_REF, 2, ATTIDX_REF, null);
/*      */ 
/*  391 */     allAttrs[ATT_REFER_R] = new OneAttr(SchemaSymbols.ATT_REFER, 2, ATTIDX_REFER, null);
/*      */ 
/*  395 */     allAttrs[ATT_SCHEMA_L_R] = new OneAttr(SchemaSymbols.ATT_SCHEMALOCATION, 0, ATTIDX_SCHEMALOCATION, null);
/*      */ 
/*  399 */     allAttrs[ATT_SCHEMA_L_N] = new OneAttr(SchemaSymbols.ATT_SCHEMALOCATION, 0, ATTIDX_SCHEMALOCATION, null);
/*      */ 
/*  403 */     allAttrs[ATT_SOURCE_N] = new OneAttr(SchemaSymbols.ATT_SOURCE, 0, ATTIDX_SOURCE, null);
/*      */ 
/*  407 */     allAttrs[ATT_SUBSTITUTION_G_N] = new OneAttr(SchemaSymbols.ATT_SUBSTITUTIONGROUP, 2, ATTIDX_SUBSGROUP, null);
/*      */ 
/*  411 */     allAttrs[ATT_SYSTEM_N] = new OneAttr(SchemaSymbols.ATT_SYSTEM, 0, ATTIDX_SYSTEM, null);
/*      */ 
/*  415 */     allAttrs[ATT_TARGET_N_N] = new OneAttr(SchemaSymbols.ATT_TARGETNAMESPACE, 0, ATTIDX_TARGETNAMESPACE, null);
/*      */ 
/*  419 */     allAttrs[ATT_TYPE_N] = new OneAttr(SchemaSymbols.ATT_TYPE, 2, ATTIDX_TYPE, null);
/*      */ 
/*  423 */     allAttrs[ATT_USE_D] = new OneAttr(SchemaSymbols.ATT_USE, -13, ATTIDX_USE, INT_USE_OPTIONAL);
/*      */ 
/*  427 */     allAttrs[ATT_VALUE_NNI_N] = new OneAttr(SchemaSymbols.ATT_VALUE, -16, ATTIDX_VALUE, null);
/*      */ 
/*  431 */     allAttrs[ATT_VALUE_PI_N] = new OneAttr(SchemaSymbols.ATT_VALUE, -17, ATTIDX_VALUE, null);
/*      */ 
/*  435 */     allAttrs[ATT_VALUE_STR_N] = new OneAttr(SchemaSymbols.ATT_VALUE, 3, ATTIDX_VALUE, null);
/*      */ 
/*  439 */     allAttrs[ATT_VALUE_WS_N] = new OneAttr(SchemaSymbols.ATT_VALUE, -14, ATTIDX_VALUE, null);
/*      */ 
/*  443 */     allAttrs[ATT_VERSION_N] = new OneAttr(SchemaSymbols.ATT_VERSION, 4, ATTIDX_VERSION, null);
/*      */ 
/*  447 */     allAttrs[ATT_XML_LANG] = new OneAttr(SchemaSymbols.ATT_XML_LANG, 8, ATTIDX_XML_LANG, null);
/*      */ 
/*  451 */     allAttrs[ATT_XPATH_R] = new OneAttr(SchemaSymbols.ATT_XPATH, 6, ATTIDX_XPATH, null);
/*      */ 
/*  455 */     allAttrs[ATT_XPATH1_R] = new OneAttr(SchemaSymbols.ATT_XPATH, 7, ATTIDX_XPATH, null);
/*      */ 
/*  464 */     Container attrList = Container.getContainer(5);
/*      */ 
/*  466 */     attrList.put(SchemaSymbols.ATT_DEFAULT, allAttrs[ATT_DEFAULT_N]);
/*      */ 
/*  468 */     attrList.put(SchemaSymbols.ATT_FIXED, allAttrs[ATT_FIXED_N]);
/*      */ 
/*  470 */     attrList.put(SchemaSymbols.ATT_ID, allAttrs[ATT_ID_N]);
/*      */ 
/*  472 */     attrList.put(SchemaSymbols.ATT_NAME, allAttrs[ATT_NAME_R]);
/*      */ 
/*  474 */     attrList.put(SchemaSymbols.ATT_TYPE, allAttrs[ATT_TYPE_N]);
/*  475 */     fEleAttrsMapG.put(SchemaSymbols.ELT_ATTRIBUTE, attrList);
/*      */ 
/*  478 */     attrList = Container.getContainer(7);
/*      */ 
/*  480 */     attrList.put(SchemaSymbols.ATT_DEFAULT, allAttrs[ATT_DEFAULT_N]);
/*      */ 
/*  482 */     attrList.put(SchemaSymbols.ATT_FIXED, allAttrs[ATT_FIXED_N]);
/*      */ 
/*  484 */     attrList.put(SchemaSymbols.ATT_FORM, allAttrs[ATT_FORM_N]);
/*      */ 
/*  486 */     attrList.put(SchemaSymbols.ATT_ID, allAttrs[ATT_ID_N]);
/*      */ 
/*  488 */     attrList.put(SchemaSymbols.ATT_NAME, allAttrs[ATT_NAME_R]);
/*      */ 
/*  490 */     attrList.put(SchemaSymbols.ATT_TYPE, allAttrs[ATT_TYPE_N]);
/*      */ 
/*  492 */     attrList.put(SchemaSymbols.ATT_USE, allAttrs[ATT_USE_D]);
/*  493 */     fEleAttrsMapL.put("attribute_n", attrList);
/*      */ 
/*  496 */     attrList = Container.getContainer(5);
/*      */ 
/*  498 */     attrList.put(SchemaSymbols.ATT_DEFAULT, allAttrs[ATT_DEFAULT_N]);
/*      */ 
/*  500 */     attrList.put(SchemaSymbols.ATT_FIXED, allAttrs[ATT_FIXED_N]);
/*      */ 
/*  502 */     attrList.put(SchemaSymbols.ATT_ID, allAttrs[ATT_ID_N]);
/*      */ 
/*  504 */     attrList.put(SchemaSymbols.ATT_REF, allAttrs[ATT_REF_R]);
/*      */ 
/*  506 */     attrList.put(SchemaSymbols.ATT_USE, allAttrs[ATT_USE_D]);
/*  507 */     fEleAttrsMapL.put("attribute_r", attrList);
/*      */ 
/*  510 */     attrList = Container.getContainer(10);
/*      */ 
/*  512 */     attrList.put(SchemaSymbols.ATT_ABSTRACT, allAttrs[ATT_ABSTRACT_D]);
/*      */ 
/*  514 */     attrList.put(SchemaSymbols.ATT_BLOCK, allAttrs[ATT_BLOCK_N]);
/*      */ 
/*  516 */     attrList.put(SchemaSymbols.ATT_DEFAULT, allAttrs[ATT_DEFAULT_N]);
/*      */ 
/*  518 */     attrList.put(SchemaSymbols.ATT_FINAL, allAttrs[ATT_FINAL_N]);
/*      */ 
/*  520 */     attrList.put(SchemaSymbols.ATT_FIXED, allAttrs[ATT_FIXED_N]);
/*      */ 
/*  522 */     attrList.put(SchemaSymbols.ATT_ID, allAttrs[ATT_ID_N]);
/*      */ 
/*  524 */     attrList.put(SchemaSymbols.ATT_NAME, allAttrs[ATT_NAME_R]);
/*      */ 
/*  526 */     attrList.put(SchemaSymbols.ATT_NILLABLE, allAttrs[ATT_NILLABLE_D]);
/*      */ 
/*  528 */     attrList.put(SchemaSymbols.ATT_SUBSTITUTIONGROUP, allAttrs[ATT_SUBSTITUTION_G_N]);
/*      */ 
/*  530 */     attrList.put(SchemaSymbols.ATT_TYPE, allAttrs[ATT_TYPE_N]);
/*  531 */     fEleAttrsMapG.put(SchemaSymbols.ELT_ELEMENT, attrList);
/*      */ 
/*  534 */     attrList = Container.getContainer(10);
/*      */ 
/*  536 */     attrList.put(SchemaSymbols.ATT_BLOCK, allAttrs[ATT_BLOCK_N]);
/*      */ 
/*  538 */     attrList.put(SchemaSymbols.ATT_DEFAULT, allAttrs[ATT_DEFAULT_N]);
/*      */ 
/*  540 */     attrList.put(SchemaSymbols.ATT_FIXED, allAttrs[ATT_FIXED_N]);
/*      */ 
/*  542 */     attrList.put(SchemaSymbols.ATT_FORM, allAttrs[ATT_FORM_N]);
/*      */ 
/*  544 */     attrList.put(SchemaSymbols.ATT_ID, allAttrs[ATT_ID_N]);
/*      */ 
/*  546 */     attrList.put(SchemaSymbols.ATT_MAXOCCURS, allAttrs[ATT_MAXOCCURS_D]);
/*      */ 
/*  548 */     attrList.put(SchemaSymbols.ATT_MINOCCURS, allAttrs[ATT_MINOCCURS_D]);
/*      */ 
/*  550 */     attrList.put(SchemaSymbols.ATT_NAME, allAttrs[ATT_NAME_R]);
/*      */ 
/*  552 */     attrList.put(SchemaSymbols.ATT_NILLABLE, allAttrs[ATT_NILLABLE_D]);
/*      */ 
/*  554 */     attrList.put(SchemaSymbols.ATT_TYPE, allAttrs[ATT_TYPE_N]);
/*  555 */     fEleAttrsMapL.put("element_n", attrList);
/*      */ 
/*  558 */     attrList = Container.getContainer(4);
/*      */ 
/*  560 */     attrList.put(SchemaSymbols.ATT_ID, allAttrs[ATT_ID_N]);
/*      */ 
/*  562 */     attrList.put(SchemaSymbols.ATT_MAXOCCURS, allAttrs[ATT_MAXOCCURS_D]);
/*      */ 
/*  564 */     attrList.put(SchemaSymbols.ATT_MINOCCURS, allAttrs[ATT_MINOCCURS_D]);
/*      */ 
/*  566 */     attrList.put(SchemaSymbols.ATT_REF, allAttrs[ATT_REF_R]);
/*  567 */     fEleAttrsMapL.put("element_r", attrList);
/*      */ 
/*  570 */     attrList = Container.getContainer(6);
/*      */ 
/*  572 */     attrList.put(SchemaSymbols.ATT_ABSTRACT, allAttrs[ATT_ABSTRACT_D]);
/*      */ 
/*  574 */     attrList.put(SchemaSymbols.ATT_BLOCK, allAttrs[ATT_BLOCK1_N]);
/*      */ 
/*  576 */     attrList.put(SchemaSymbols.ATT_FINAL, allAttrs[ATT_FINAL_N]);
/*      */ 
/*  578 */     attrList.put(SchemaSymbols.ATT_ID, allAttrs[ATT_ID_N]);
/*      */ 
/*  580 */     attrList.put(SchemaSymbols.ATT_MIXED, allAttrs[ATT_MIXED_D]);
/*      */ 
/*  582 */     attrList.put(SchemaSymbols.ATT_NAME, allAttrs[ATT_NAME_R]);
/*  583 */     fEleAttrsMapG.put(SchemaSymbols.ELT_COMPLEXTYPE, attrList);
/*      */ 
/*  586 */     attrList = Container.getContainer(4);
/*      */ 
/*  588 */     attrList.put(SchemaSymbols.ATT_ID, allAttrs[ATT_ID_N]);
/*      */ 
/*  590 */     attrList.put(SchemaSymbols.ATT_NAME, allAttrs[ATT_NAME_R]);
/*      */ 
/*  592 */     attrList.put(SchemaSymbols.ATT_PUBLIC, allAttrs[ATT_PUBLIC_R]);
/*      */ 
/*  594 */     attrList.put(SchemaSymbols.ATT_SYSTEM, allAttrs[ATT_SYSTEM_N]);
/*  595 */     fEleAttrsMapG.put(SchemaSymbols.ELT_NOTATION, attrList);
/*      */ 
/*  599 */     attrList = Container.getContainer(2);
/*      */ 
/*  601 */     attrList.put(SchemaSymbols.ATT_ID, allAttrs[ATT_ID_N]);
/*      */ 
/*  603 */     attrList.put(SchemaSymbols.ATT_MIXED, allAttrs[ATT_MIXED_D]);
/*  604 */     fEleAttrsMapL.put(SchemaSymbols.ELT_COMPLEXTYPE, attrList);
/*      */ 
/*  607 */     attrList = Container.getContainer(1);
/*      */ 
/*  609 */     attrList.put(SchemaSymbols.ATT_ID, allAttrs[ATT_ID_N]);
/*  610 */     fEleAttrsMapL.put(SchemaSymbols.ELT_SIMPLECONTENT, attrList);
/*      */ 
/*  613 */     attrList = Container.getContainer(2);
/*      */ 
/*  615 */     attrList.put(SchemaSymbols.ATT_BASE, allAttrs[ATT_BASE_N]);
/*      */ 
/*  617 */     attrList.put(SchemaSymbols.ATT_ID, allAttrs[ATT_ID_N]);
/*  618 */     fEleAttrsMapL.put(SchemaSymbols.ELT_RESTRICTION, attrList);
/*      */ 
/*  621 */     attrList = Container.getContainer(2);
/*      */ 
/*  623 */     attrList.put(SchemaSymbols.ATT_BASE, allAttrs[ATT_BASE_R]);
/*      */ 
/*  625 */     attrList.put(SchemaSymbols.ATT_ID, allAttrs[ATT_ID_N]);
/*  626 */     fEleAttrsMapL.put(SchemaSymbols.ELT_EXTENSION, attrList);
/*      */ 
/*  629 */     attrList = Container.getContainer(2);
/*      */ 
/*  631 */     attrList.put(SchemaSymbols.ATT_ID, allAttrs[ATT_ID_N]);
/*      */ 
/*  633 */     attrList.put(SchemaSymbols.ATT_REF, allAttrs[ATT_REF_R]);
/*  634 */     fEleAttrsMapL.put(SchemaSymbols.ELT_ATTRIBUTEGROUP, attrList);
/*      */ 
/*  637 */     attrList = Container.getContainer(3);
/*      */ 
/*  639 */     attrList.put(SchemaSymbols.ATT_ID, allAttrs[ATT_ID_N]);
/*      */ 
/*  641 */     attrList.put(SchemaSymbols.ATT_NAMESPACE, allAttrs[ATT_NAMESPACE_D]);
/*      */ 
/*  643 */     attrList.put(SchemaSymbols.ATT_PROCESSCONTENTS, allAttrs[ATT_PROCESS_C_D]);
/*  644 */     fEleAttrsMapL.put(SchemaSymbols.ELT_ANYATTRIBUTE, attrList);
/*      */ 
/*  647 */     attrList = Container.getContainer(2);
/*      */ 
/*  649 */     attrList.put(SchemaSymbols.ATT_ID, allAttrs[ATT_ID_N]);
/*      */ 
/*  651 */     attrList.put(SchemaSymbols.ATT_MIXED, allAttrs[ATT_MIXED_N]);
/*  652 */     fEleAttrsMapL.put(SchemaSymbols.ELT_COMPLEXCONTENT, attrList);
/*      */ 
/*  655 */     attrList = Container.getContainer(2);
/*      */ 
/*  657 */     attrList.put(SchemaSymbols.ATT_ID, allAttrs[ATT_ID_N]);
/*      */ 
/*  659 */     attrList.put(SchemaSymbols.ATT_NAME, allAttrs[ATT_NAME_R]);
/*  660 */     fEleAttrsMapG.put(SchemaSymbols.ELT_ATTRIBUTEGROUP, attrList);
/*      */ 
/*  663 */     attrList = Container.getContainer(2);
/*      */ 
/*  665 */     attrList.put(SchemaSymbols.ATT_ID, allAttrs[ATT_ID_N]);
/*      */ 
/*  667 */     attrList.put(SchemaSymbols.ATT_NAME, allAttrs[ATT_NAME_R]);
/*  668 */     fEleAttrsMapG.put(SchemaSymbols.ELT_GROUP, attrList);
/*      */ 
/*  671 */     attrList = Container.getContainer(4);
/*      */ 
/*  673 */     attrList.put(SchemaSymbols.ATT_ID, allAttrs[ATT_ID_N]);
/*      */ 
/*  675 */     attrList.put(SchemaSymbols.ATT_MAXOCCURS, allAttrs[ATT_MAXOCCURS_D]);
/*      */ 
/*  677 */     attrList.put(SchemaSymbols.ATT_MINOCCURS, allAttrs[ATT_MINOCCURS_D]);
/*      */ 
/*  679 */     attrList.put(SchemaSymbols.ATT_REF, allAttrs[ATT_REF_R]);
/*  680 */     fEleAttrsMapL.put(SchemaSymbols.ELT_GROUP, attrList);
/*      */ 
/*  683 */     attrList = Container.getContainer(3);
/*      */ 
/*  685 */     attrList.put(SchemaSymbols.ATT_ID, allAttrs[ATT_ID_N]);
/*      */ 
/*  687 */     attrList.put(SchemaSymbols.ATT_MAXOCCURS, allAttrs[ATT_MAXOCCURS1_D]);
/*      */ 
/*  689 */     attrList.put(SchemaSymbols.ATT_MINOCCURS, allAttrs[ATT_MINOCCURS1_D]);
/*  690 */     fEleAttrsMapL.put(SchemaSymbols.ELT_ALL, attrList);
/*      */ 
/*  693 */     attrList = Container.getContainer(3);
/*      */ 
/*  695 */     attrList.put(SchemaSymbols.ATT_ID, allAttrs[ATT_ID_N]);
/*      */ 
/*  697 */     attrList.put(SchemaSymbols.ATT_MAXOCCURS, allAttrs[ATT_MAXOCCURS_D]);
/*      */ 
/*  699 */     attrList.put(SchemaSymbols.ATT_MINOCCURS, allAttrs[ATT_MINOCCURS_D]);
/*  700 */     fEleAttrsMapL.put(SchemaSymbols.ELT_CHOICE, attrList);
/*      */ 
/*  702 */     fEleAttrsMapL.put(SchemaSymbols.ELT_SEQUENCE, attrList);
/*      */ 
/*  705 */     attrList = Container.getContainer(5);
/*      */ 
/*  707 */     attrList.put(SchemaSymbols.ATT_ID, allAttrs[ATT_ID_N]);
/*      */ 
/*  709 */     attrList.put(SchemaSymbols.ATT_MAXOCCURS, allAttrs[ATT_MAXOCCURS_D]);
/*      */ 
/*  711 */     attrList.put(SchemaSymbols.ATT_MINOCCURS, allAttrs[ATT_MINOCCURS_D]);
/*      */ 
/*  713 */     attrList.put(SchemaSymbols.ATT_NAMESPACE, allAttrs[ATT_NAMESPACE_D]);
/*      */ 
/*  715 */     attrList.put(SchemaSymbols.ATT_PROCESSCONTENTS, allAttrs[ATT_PROCESS_C_D]);
/*  716 */     fEleAttrsMapL.put(SchemaSymbols.ELT_ANY, attrList);
/*      */ 
/*  719 */     attrList = Container.getContainer(2);
/*      */ 
/*  721 */     attrList.put(SchemaSymbols.ATT_ID, allAttrs[ATT_ID_N]);
/*      */ 
/*  723 */     attrList.put(SchemaSymbols.ATT_NAME, allAttrs[ATT_NAME_R]);
/*  724 */     fEleAttrsMapL.put(SchemaSymbols.ELT_UNIQUE, attrList);
/*      */ 
/*  726 */     fEleAttrsMapL.put(SchemaSymbols.ELT_KEY, attrList);
/*      */ 
/*  729 */     attrList = Container.getContainer(3);
/*      */ 
/*  731 */     attrList.put(SchemaSymbols.ATT_ID, allAttrs[ATT_ID_N]);
/*      */ 
/*  733 */     attrList.put(SchemaSymbols.ATT_NAME, allAttrs[ATT_NAME_R]);
/*      */ 
/*  735 */     attrList.put(SchemaSymbols.ATT_REFER, allAttrs[ATT_REFER_R]);
/*  736 */     fEleAttrsMapL.put(SchemaSymbols.ELT_KEYREF, attrList);
/*      */ 
/*  739 */     attrList = Container.getContainer(2);
/*      */ 
/*  741 */     attrList.put(SchemaSymbols.ATT_ID, allAttrs[ATT_ID_N]);
/*      */ 
/*  743 */     attrList.put(SchemaSymbols.ATT_XPATH, allAttrs[ATT_XPATH_R]);
/*  744 */     fEleAttrsMapL.put(SchemaSymbols.ELT_SELECTOR, attrList);
/*      */ 
/*  747 */     attrList = Container.getContainer(2);
/*      */ 
/*  749 */     attrList.put(SchemaSymbols.ATT_ID, allAttrs[ATT_ID_N]);
/*      */ 
/*  751 */     attrList.put(SchemaSymbols.ATT_XPATH, allAttrs[ATT_XPATH1_R]);
/*  752 */     fEleAttrsMapL.put(SchemaSymbols.ELT_FIELD, attrList);
/*      */ 
/*  755 */     attrList = Container.getContainer(1);
/*      */ 
/*  757 */     attrList.put(SchemaSymbols.ATT_ID, allAttrs[ATT_ID_N]);
/*  758 */     fEleAttrsMapG.put(SchemaSymbols.ELT_ANNOTATION, attrList);
/*      */ 
/*  760 */     fEleAttrsMapL.put(SchemaSymbols.ELT_ANNOTATION, attrList);
/*      */ 
/*  763 */     attrList = Container.getContainer(1);
/*      */ 
/*  765 */     attrList.put(SchemaSymbols.ATT_SOURCE, allAttrs[ATT_SOURCE_N]);
/*  766 */     fEleAttrsMapG.put(SchemaSymbols.ELT_APPINFO, attrList);
/*  767 */     fEleAttrsMapL.put(SchemaSymbols.ELT_APPINFO, attrList);
/*      */ 
/*  770 */     attrList = Container.getContainer(2);
/*      */ 
/*  772 */     attrList.put(SchemaSymbols.ATT_SOURCE, allAttrs[ATT_SOURCE_N]);
/*      */ 
/*  774 */     attrList.put(SchemaSymbols.ATT_XML_LANG, allAttrs[ATT_XML_LANG]);
/*  775 */     fEleAttrsMapG.put(SchemaSymbols.ELT_DOCUMENTATION, attrList);
/*  776 */     fEleAttrsMapL.put(SchemaSymbols.ELT_DOCUMENTATION, attrList);
/*      */ 
/*  779 */     attrList = Container.getContainer(3);
/*      */ 
/*  781 */     attrList.put(SchemaSymbols.ATT_FINAL, allAttrs[ATT_FINAL1_N]);
/*      */ 
/*  783 */     attrList.put(SchemaSymbols.ATT_ID, allAttrs[ATT_ID_N]);
/*      */ 
/*  785 */     attrList.put(SchemaSymbols.ATT_NAME, allAttrs[ATT_NAME_R]);
/*  786 */     fEleAttrsMapG.put(SchemaSymbols.ELT_SIMPLETYPE, attrList);
/*      */ 
/*  789 */     attrList = Container.getContainer(2);
/*      */ 
/*  791 */     attrList.put(SchemaSymbols.ATT_FINAL, allAttrs[ATT_FINAL1_N]);
/*      */ 
/*  793 */     attrList.put(SchemaSymbols.ATT_ID, allAttrs[ATT_ID_N]);
/*  794 */     fEleAttrsMapL.put(SchemaSymbols.ELT_SIMPLETYPE, attrList);
/*      */ 
/*  800 */     attrList = Container.getContainer(2);
/*      */ 
/*  802 */     attrList.put(SchemaSymbols.ATT_ID, allAttrs[ATT_ID_N]);
/*      */ 
/*  804 */     attrList.put(SchemaSymbols.ATT_ITEMTYPE, allAttrs[ATT_ITEMTYPE_N]);
/*  805 */     fEleAttrsMapL.put(SchemaSymbols.ELT_LIST, attrList);
/*      */ 
/*  808 */     attrList = Container.getContainer(2);
/*      */ 
/*  810 */     attrList.put(SchemaSymbols.ATT_ID, allAttrs[ATT_ID_N]);
/*      */ 
/*  812 */     attrList.put(SchemaSymbols.ATT_MEMBERTYPES, allAttrs[ATT_MEMBER_T_N]);
/*  813 */     fEleAttrsMapL.put(SchemaSymbols.ELT_UNION, attrList);
/*      */ 
/*  816 */     attrList = Container.getContainer(8);
/*      */ 
/*  818 */     attrList.put(SchemaSymbols.ATT_ATTRIBUTEFORMDEFAULT, allAttrs[ATT_ATTRIBUTE_FD_D]);
/*      */ 
/*  820 */     attrList.put(SchemaSymbols.ATT_BLOCKDEFAULT, allAttrs[ATT_BLOCK_D_D]);
/*      */ 
/*  822 */     attrList.put(SchemaSymbols.ATT_ELEMENTFORMDEFAULT, allAttrs[ATT_ELEMENT_FD_D]);
/*      */ 
/*  824 */     attrList.put(SchemaSymbols.ATT_FINALDEFAULT, allAttrs[ATT_FINAL_D_D]);
/*      */ 
/*  826 */     attrList.put(SchemaSymbols.ATT_ID, allAttrs[ATT_ID_N]);
/*      */ 
/*  828 */     attrList.put(SchemaSymbols.ATT_TARGETNAMESPACE, allAttrs[ATT_TARGET_N_N]);
/*      */ 
/*  830 */     attrList.put(SchemaSymbols.ATT_VERSION, allAttrs[ATT_VERSION_N]);
/*      */ 
/*  832 */     attrList.put(SchemaSymbols.ATT_XML_LANG, allAttrs[ATT_XML_LANG]);
/*  833 */     fEleAttrsMapG.put(SchemaSymbols.ELT_SCHEMA, attrList);
/*      */ 
/*  836 */     attrList = Container.getContainer(2);
/*      */ 
/*  838 */     attrList.put(SchemaSymbols.ATT_ID, allAttrs[ATT_ID_N]);
/*      */ 
/*  840 */     attrList.put(SchemaSymbols.ATT_SCHEMALOCATION, allAttrs[ATT_SCHEMA_L_R]);
/*  841 */     fEleAttrsMapG.put(SchemaSymbols.ELT_INCLUDE, attrList);
/*      */ 
/*  843 */     fEleAttrsMapG.put(SchemaSymbols.ELT_REDEFINE, attrList);
/*      */ 
/*  846 */     attrList = Container.getContainer(3);
/*      */ 
/*  848 */     attrList.put(SchemaSymbols.ATT_ID, allAttrs[ATT_ID_N]);
/*      */ 
/*  850 */     attrList.put(SchemaSymbols.ATT_NAMESPACE, allAttrs[ATT_NAMESPACE_N]);
/*      */ 
/*  852 */     attrList.put(SchemaSymbols.ATT_SCHEMALOCATION, allAttrs[ATT_SCHEMA_L_N]);
/*  853 */     fEleAttrsMapG.put(SchemaSymbols.ELT_IMPORT, attrList);
/*      */ 
/*  856 */     attrList = Container.getContainer(3);
/*      */ 
/*  858 */     attrList.put(SchemaSymbols.ATT_ID, allAttrs[ATT_ID_N]);
/*      */ 
/*  860 */     attrList.put(SchemaSymbols.ATT_VALUE, allAttrs[ATT_VALUE_NNI_N]);
/*      */ 
/*  862 */     attrList.put(SchemaSymbols.ATT_FIXED, allAttrs[ATT_FIXED_D]);
/*  863 */     fEleAttrsMapL.put(SchemaSymbols.ELT_LENGTH, attrList);
/*      */ 
/*  865 */     fEleAttrsMapL.put(SchemaSymbols.ELT_MINLENGTH, attrList);
/*      */ 
/*  867 */     fEleAttrsMapL.put(SchemaSymbols.ELT_MAXLENGTH, attrList);
/*      */ 
/*  869 */     fEleAttrsMapL.put(SchemaSymbols.ELT_FRACTIONDIGITS, attrList);
/*      */ 
/*  872 */     attrList = Container.getContainer(3);
/*      */ 
/*  874 */     attrList.put(SchemaSymbols.ATT_ID, allAttrs[ATT_ID_N]);
/*      */ 
/*  876 */     attrList.put(SchemaSymbols.ATT_VALUE, allAttrs[ATT_VALUE_PI_N]);
/*      */ 
/*  878 */     attrList.put(SchemaSymbols.ATT_FIXED, allAttrs[ATT_FIXED_D]);
/*  879 */     fEleAttrsMapL.put(SchemaSymbols.ELT_TOTALDIGITS, attrList);
/*      */ 
/*  882 */     attrList = Container.getContainer(2);
/*      */ 
/*  884 */     attrList.put(SchemaSymbols.ATT_ID, allAttrs[ATT_ID_N]);
/*      */ 
/*  886 */     attrList.put(SchemaSymbols.ATT_VALUE, allAttrs[ATT_VALUE_STR_N]);
/*  887 */     fEleAttrsMapL.put(SchemaSymbols.ELT_PATTERN, attrList);
/*      */ 
/*  890 */     attrList = Container.getContainer(2);
/*      */ 
/*  892 */     attrList.put(SchemaSymbols.ATT_ID, allAttrs[ATT_ID_N]);
/*      */ 
/*  894 */     attrList.put(SchemaSymbols.ATT_VALUE, allAttrs[ATT_VALUE_STR_N]);
/*  895 */     fEleAttrsMapL.put(SchemaSymbols.ELT_ENUMERATION, attrList);
/*      */ 
/*  898 */     attrList = Container.getContainer(3);
/*      */ 
/*  900 */     attrList.put(SchemaSymbols.ATT_ID, allAttrs[ATT_ID_N]);
/*      */ 
/*  902 */     attrList.put(SchemaSymbols.ATT_VALUE, allAttrs[ATT_VALUE_WS_N]);
/*      */ 
/*  904 */     attrList.put(SchemaSymbols.ATT_FIXED, allAttrs[ATT_FIXED_D]);
/*  905 */     fEleAttrsMapL.put(SchemaSymbols.ELT_WHITESPACE, attrList);
/*      */ 
/*  908 */     attrList = Container.getContainer(3);
/*      */ 
/*  910 */     attrList.put(SchemaSymbols.ATT_ID, allAttrs[ATT_ID_N]);
/*      */ 
/*  912 */     attrList.put(SchemaSymbols.ATT_VALUE, allAttrs[ATT_VALUE_STR_N]);
/*      */ 
/*  914 */     attrList.put(SchemaSymbols.ATT_FIXED, allAttrs[ATT_FIXED_D]);
/*  915 */     fEleAttrsMapL.put(SchemaSymbols.ELT_MAXINCLUSIVE, attrList);
/*      */ 
/*  917 */     fEleAttrsMapL.put(SchemaSymbols.ELT_MAXEXCLUSIVE, attrList);
/*      */ 
/*  919 */     fEleAttrsMapL.put(SchemaSymbols.ELT_MININCLUSIVE, attrList);
/*      */ 
/*  921 */     fEleAttrsMapL.put(SchemaSymbols.ELT_MINEXCLUSIVE, attrList);
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.xs.traversers.XSAttributeChecker
 * JD-Core Version:    0.6.2
 */