/*     */ package com.sun.org.apache.xpath.internal.compiler;
/*     */ 
/*     */ import com.sun.org.apache.xpath.internal.functions.FuncBoolean;
/*     */ import com.sun.org.apache.xpath.internal.functions.FuncCeiling;
/*     */ import com.sun.org.apache.xpath.internal.functions.FuncConcat;
/*     */ import com.sun.org.apache.xpath.internal.functions.FuncContains;
/*     */ import com.sun.org.apache.xpath.internal.functions.FuncCount;
/*     */ import com.sun.org.apache.xpath.internal.functions.FuncCurrent;
/*     */ import com.sun.org.apache.xpath.internal.functions.FuncDoclocation;
/*     */ import com.sun.org.apache.xpath.internal.functions.FuncExtElementAvailable;
/*     */ import com.sun.org.apache.xpath.internal.functions.FuncExtFunctionAvailable;
/*     */ import com.sun.org.apache.xpath.internal.functions.FuncFalse;
/*     */ import com.sun.org.apache.xpath.internal.functions.FuncFloor;
/*     */ import com.sun.org.apache.xpath.internal.functions.FuncGenerateId;
/*     */ import com.sun.org.apache.xpath.internal.functions.FuncId;
/*     */ import com.sun.org.apache.xpath.internal.functions.FuncLang;
/*     */ import com.sun.org.apache.xpath.internal.functions.FuncLast;
/*     */ import com.sun.org.apache.xpath.internal.functions.FuncLocalPart;
/*     */ import com.sun.org.apache.xpath.internal.functions.FuncNamespace;
/*     */ import com.sun.org.apache.xpath.internal.functions.FuncNormalizeSpace;
/*     */ import com.sun.org.apache.xpath.internal.functions.FuncNot;
/*     */ import com.sun.org.apache.xpath.internal.functions.FuncNumber;
/*     */ import com.sun.org.apache.xpath.internal.functions.FuncPosition;
/*     */ import com.sun.org.apache.xpath.internal.functions.FuncQname;
/*     */ import com.sun.org.apache.xpath.internal.functions.FuncRound;
/*     */ import com.sun.org.apache.xpath.internal.functions.FuncStartsWith;
/*     */ import com.sun.org.apache.xpath.internal.functions.FuncString;
/*     */ import com.sun.org.apache.xpath.internal.functions.FuncStringLength;
/*     */ import com.sun.org.apache.xpath.internal.functions.FuncSubstring;
/*     */ import com.sun.org.apache.xpath.internal.functions.FuncSubstringAfter;
/*     */ import com.sun.org.apache.xpath.internal.functions.FuncSubstringBefore;
/*     */ import com.sun.org.apache.xpath.internal.functions.FuncSum;
/*     */ import com.sun.org.apache.xpath.internal.functions.FuncSystemProperty;
/*     */ import com.sun.org.apache.xpath.internal.functions.FuncTranslate;
/*     */ import com.sun.org.apache.xpath.internal.functions.FuncTrue;
/*     */ import com.sun.org.apache.xpath.internal.functions.FuncUnparsedEntityURI;
/*     */ import com.sun.org.apache.xpath.internal.functions.Function;
/*     */ import java.util.HashMap;
/*     */ import javax.xml.transform.TransformerException;
/*     */ 
/*     */ public class FunctionTable
/*     */ {
/*     */   public static final int FUNC_CURRENT = 0;
/*     */   public static final int FUNC_LAST = 1;
/*     */   public static final int FUNC_POSITION = 2;
/*     */   public static final int FUNC_COUNT = 3;
/*     */   public static final int FUNC_ID = 4;
/*     */   public static final int FUNC_KEY = 5;
/*     */   public static final int FUNC_LOCAL_PART = 7;
/*     */   public static final int FUNC_NAMESPACE = 8;
/*     */   public static final int FUNC_QNAME = 9;
/*     */   public static final int FUNC_GENERATE_ID = 10;
/*     */   public static final int FUNC_NOT = 11;
/*     */   public static final int FUNC_TRUE = 12;
/*     */   public static final int FUNC_FALSE = 13;
/*     */   public static final int FUNC_BOOLEAN = 14;
/*     */   public static final int FUNC_NUMBER = 15;
/*     */   public static final int FUNC_FLOOR = 16;
/*     */   public static final int FUNC_CEILING = 17;
/*     */   public static final int FUNC_ROUND = 18;
/*     */   public static final int FUNC_SUM = 19;
/*     */   public static final int FUNC_STRING = 20;
/*     */   public static final int FUNC_STARTS_WITH = 21;
/*     */   public static final int FUNC_CONTAINS = 22;
/*     */   public static final int FUNC_SUBSTRING_BEFORE = 23;
/*     */   public static final int FUNC_SUBSTRING_AFTER = 24;
/*     */   public static final int FUNC_NORMALIZE_SPACE = 25;
/*     */   public static final int FUNC_TRANSLATE = 26;
/*     */   public static final int FUNC_CONCAT = 27;
/*     */   public static final int FUNC_SUBSTRING = 29;
/*     */   public static final int FUNC_STRING_LENGTH = 30;
/*     */   public static final int FUNC_SYSTEM_PROPERTY = 31;
/*     */   public static final int FUNC_LANG = 32;
/*     */   public static final int FUNC_EXT_FUNCTION_AVAILABLE = 33;
/*     */   public static final int FUNC_EXT_ELEM_AVAILABLE = 34;
/*     */   public static final int FUNC_UNPARSED_ENTITY_URI = 36;
/*     */   public static final int FUNC_DOCLOCATION = 35;
/*     */   private static Class[] m_functions;
/* 149 */   private static HashMap m_functionID = new HashMap();
/*     */ 
/* 154 */   private Class[] m_functions_customer = new Class[30];
/*     */ 
/* 159 */   private HashMap m_functionID_customer = new HashMap();
/*     */   private static final int NUM_BUILT_IN_FUNCS = 37;
/*     */   private static final int NUM_ALLOWABLE_ADDINS = 30;
/* 175 */   private int m_funcNextFreeIndex = 37;
/*     */ 
/*     */   String getFunctionName(int funcID)
/*     */   {
/* 315 */     if (funcID < 37) return m_functions[funcID].getName();
/* 316 */     return this.m_functions_customer[(funcID - 37)].getName();
/*     */   }
/*     */ 
/*     */   Function getFunction(int which)
/*     */     throws TransformerException
/*     */   {
/*     */     try
/*     */     {
/* 335 */       if (which < 37) {
/* 336 */         return (Function)m_functions[which].newInstance();
/*     */       }
/* 338 */       return (Function)this.m_functions_customer[(which - 37)].newInstance();
/*     */     }
/*     */     catch (IllegalAccessException ex) {
/* 341 */       throw new TransformerException(ex.getMessage());
/*     */     } catch (InstantiationException ex) {
/* 343 */       throw new TransformerException(ex.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   Object getFunctionID(String key)
/*     */   {
/* 355 */     Object id = this.m_functionID_customer.get(key);
/* 356 */     if (null == id) id = m_functionID.get(key);
/* 357 */     return id;
/*     */   }
/*     */ 
/*     */   public int installFunction(String name, Class func)
/*     */   {
/* 370 */     Object funcIndexObj = getFunctionID(name);
/*     */     int funcIndex;
/* 372 */     if (null != funcIndexObj)
/*     */     {
/* 374 */       int funcIndex = ((Integer)funcIndexObj).intValue();
/*     */ 
/* 376 */       if (funcIndex < 37) {
/* 377 */         funcIndex = this.m_funcNextFreeIndex++;
/* 378 */         this.m_functionID_customer.put(name, new Integer(funcIndex));
/*     */       }
/* 380 */       this.m_functions_customer[(funcIndex - 37)] = func;
/*     */     }
/*     */     else
/*     */     {
/* 384 */       funcIndex = this.m_funcNextFreeIndex++;
/*     */ 
/* 386 */       this.m_functions_customer[(funcIndex - 37)] = func;
/*     */ 
/* 388 */       this.m_functionID_customer.put(name, new Integer(funcIndex));
/*     */     }
/*     */ 
/* 391 */     return funcIndex;
/*     */   }
/*     */ 
/*     */   public boolean functionAvailable(String methName)
/*     */   {
/* 403 */     Object tblEntry = m_functionID.get(methName);
/* 404 */     if (null != tblEntry) return true;
/*     */ 
/* 406 */     tblEntry = this.m_functionID_customer.get(methName);
/* 407 */     return null != tblEntry;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 179 */     m_functions = new Class[37];
/* 180 */     m_functions[0] = FuncCurrent.class;
/* 181 */     m_functions[1] = FuncLast.class;
/* 182 */     m_functions[2] = FuncPosition.class;
/* 183 */     m_functions[3] = FuncCount.class;
/* 184 */     m_functions[4] = FuncId.class;
/*     */ 
/* 188 */     m_functions[7] = FuncLocalPart.class;
/*     */ 
/* 190 */     m_functions[8] = FuncNamespace.class;
/*     */ 
/* 192 */     m_functions[9] = FuncQname.class;
/* 193 */     m_functions[10] = FuncGenerateId.class;
/*     */ 
/* 195 */     m_functions[11] = FuncNot.class;
/* 196 */     m_functions[12] = FuncTrue.class;
/* 197 */     m_functions[13] = FuncFalse.class;
/* 198 */     m_functions[14] = FuncBoolean.class;
/* 199 */     m_functions[32] = FuncLang.class;
/* 200 */     m_functions[15] = FuncNumber.class;
/* 201 */     m_functions[16] = FuncFloor.class;
/* 202 */     m_functions[17] = FuncCeiling.class;
/* 203 */     m_functions[18] = FuncRound.class;
/* 204 */     m_functions[19] = FuncSum.class;
/* 205 */     m_functions[20] = FuncString.class;
/* 206 */     m_functions[21] = FuncStartsWith.class;
/*     */ 
/* 208 */     m_functions[22] = FuncContains.class;
/* 209 */     m_functions[23] = FuncSubstringBefore.class;
/*     */ 
/* 211 */     m_functions[24] = FuncSubstringAfter.class;
/*     */ 
/* 213 */     m_functions[25] = FuncNormalizeSpace.class;
/*     */ 
/* 215 */     m_functions[26] = FuncTranslate.class;
/*     */ 
/* 217 */     m_functions[27] = FuncConcat.class;
/* 218 */     m_functions[31] = FuncSystemProperty.class;
/*     */ 
/* 220 */     m_functions[33] = FuncExtFunctionAvailable.class;
/*     */ 
/* 222 */     m_functions[34] = FuncExtElementAvailable.class;
/*     */ 
/* 224 */     m_functions[29] = FuncSubstring.class;
/*     */ 
/* 226 */     m_functions[30] = FuncStringLength.class;
/*     */ 
/* 228 */     m_functions[35] = FuncDoclocation.class;
/*     */ 
/* 230 */     m_functions[36] = FuncUnparsedEntityURI.class;
/*     */ 
/* 235 */     m_functionID.put("current", new Integer(0));
/*     */ 
/* 237 */     m_functionID.put("last", new Integer(1));
/*     */ 
/* 239 */     m_functionID.put("position", new Integer(2));
/*     */ 
/* 241 */     m_functionID.put("count", new Integer(3));
/*     */ 
/* 243 */     m_functionID.put("id", new Integer(4));
/*     */ 
/* 245 */     m_functionID.put("key", new Integer(5));
/*     */ 
/* 247 */     m_functionID.put("local-name", new Integer(7));
/*     */ 
/* 249 */     m_functionID.put("namespace-uri", new Integer(8));
/*     */ 
/* 251 */     m_functionID.put("name", new Integer(9));
/*     */ 
/* 253 */     m_functionID.put("generate-id", new Integer(10));
/*     */ 
/* 255 */     m_functionID.put("not", new Integer(11));
/*     */ 
/* 257 */     m_functionID.put("true", new Integer(12));
/*     */ 
/* 259 */     m_functionID.put("false", new Integer(13));
/*     */ 
/* 261 */     m_functionID.put("boolean", new Integer(14));
/*     */ 
/* 263 */     m_functionID.put("lang", new Integer(32));
/*     */ 
/* 265 */     m_functionID.put("number", new Integer(15));
/*     */ 
/* 267 */     m_functionID.put("floor", new Integer(16));
/*     */ 
/* 269 */     m_functionID.put("ceiling", new Integer(17));
/*     */ 
/* 271 */     m_functionID.put("round", new Integer(18));
/*     */ 
/* 273 */     m_functionID.put("sum", new Integer(19));
/*     */ 
/* 275 */     m_functionID.put("string", new Integer(20));
/*     */ 
/* 277 */     m_functionID.put("starts-with", new Integer(21));
/*     */ 
/* 279 */     m_functionID.put("contains", new Integer(22));
/*     */ 
/* 281 */     m_functionID.put("substring-before", new Integer(23));
/*     */ 
/* 283 */     m_functionID.put("substring-after", new Integer(24));
/*     */ 
/* 285 */     m_functionID.put("normalize-space", new Integer(25));
/*     */ 
/* 287 */     m_functionID.put("translate", new Integer(26));
/*     */ 
/* 289 */     m_functionID.put("concat", new Integer(27));
/*     */ 
/* 291 */     m_functionID.put("system-property", new Integer(31));
/*     */ 
/* 293 */     m_functionID.put("function-available", new Integer(33));
/*     */ 
/* 295 */     m_functionID.put("element-available", new Integer(34));
/*     */ 
/* 297 */     m_functionID.put("substring", new Integer(29));
/*     */ 
/* 299 */     m_functionID.put("string-length", new Integer(30));
/*     */ 
/* 301 */     m_functionID.put("unparsed-entity-uri", new Integer(36));
/*     */ 
/* 303 */     m_functionID.put("document-location", new Integer(35));
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xpath.internal.compiler.FunctionTable
 * JD-Core Version:    0.6.2
 */