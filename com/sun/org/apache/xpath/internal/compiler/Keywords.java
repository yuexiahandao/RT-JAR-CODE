/*     */ package com.sun.org.apache.xpath.internal.compiler;
/*     */ 
/*     */ import java.util.Hashtable;
/*     */ 
/*     */ public class Keywords
/*     */ {
/*  35 */   private static Hashtable m_keywords = new Hashtable();
/*     */ 
/*  38 */   private static Hashtable m_axisnames = new Hashtable();
/*     */ 
/*  41 */   private static Hashtable m_nodetests = new Hashtable();
/*     */ 
/*  44 */   private static Hashtable m_nodetypes = new Hashtable();
/*     */   private static final String FROM_ANCESTORS_STRING = "ancestor";
/*     */   private static final String FROM_ANCESTORS_OR_SELF_STRING = "ancestor-or-self";
/*     */   private static final String FROM_ATTRIBUTES_STRING = "attribute";
/*     */   private static final String FROM_CHILDREN_STRING = "child";
/*     */   private static final String FROM_DESCENDANTS_STRING = "descendant";
/*     */   private static final String FROM_DESCENDANTS_OR_SELF_STRING = "descendant-or-self";
/*     */   private static final String FROM_FOLLOWING_STRING = "following";
/*     */   private static final String FROM_FOLLOWING_SIBLINGS_STRING = "following-sibling";
/*     */   private static final String FROM_PARENT_STRING = "parent";
/*     */   private static final String FROM_PRECEDING_STRING = "preceding";
/*     */   private static final String FROM_PRECEDING_SIBLINGS_STRING = "preceding-sibling";
/*     */   private static final String FROM_SELF_STRING = "self";
/*     */   private static final String FROM_NAMESPACE_STRING = "namespace";
/*     */   private static final String FROM_SELF_ABBREVIATED_STRING = ".";
/*     */   private static final String NODETYPE_COMMENT_STRING = "comment";
/*     */   private static final String NODETYPE_TEXT_STRING = "text";
/*     */   private static final String NODETYPE_PI_STRING = "processing-instruction";
/*     */   private static final String NODETYPE_NODE_STRING = "node";
/*     */   private static final String NODETYPE_ANYELEMENT_STRING = "*";
/*     */   public static final String FUNC_CURRENT_STRING = "current";
/*     */   public static final String FUNC_LAST_STRING = "last";
/*     */   public static final String FUNC_POSITION_STRING = "position";
/*     */   public static final String FUNC_COUNT_STRING = "count";
/*     */   static final String FUNC_ID_STRING = "id";
/*     */   public static final String FUNC_KEY_STRING = "key";
/*     */   public static final String FUNC_LOCAL_PART_STRING = "local-name";
/*     */   public static final String FUNC_NAMESPACE_STRING = "namespace-uri";
/*     */   public static final String FUNC_NAME_STRING = "name";
/*     */   public static final String FUNC_GENERATE_ID_STRING = "generate-id";
/*     */   public static final String FUNC_NOT_STRING = "not";
/*     */   public static final String FUNC_TRUE_STRING = "true";
/*     */   public static final String FUNC_FALSE_STRING = "false";
/*     */   public static final String FUNC_BOOLEAN_STRING = "boolean";
/*     */   public static final String FUNC_LANG_STRING = "lang";
/*     */   public static final String FUNC_NUMBER_STRING = "number";
/*     */   public static final String FUNC_FLOOR_STRING = "floor";
/*     */   public static final String FUNC_CEILING_STRING = "ceiling";
/*     */   public static final String FUNC_ROUND_STRING = "round";
/*     */   public static final String FUNC_SUM_STRING = "sum";
/*     */   public static final String FUNC_STRING_STRING = "string";
/*     */   public static final String FUNC_STARTS_WITH_STRING = "starts-with";
/*     */   public static final String FUNC_CONTAINS_STRING = "contains";
/*     */   public static final String FUNC_SUBSTRING_BEFORE_STRING = "substring-before";
/*     */   public static final String FUNC_SUBSTRING_AFTER_STRING = "substring-after";
/*     */   public static final String FUNC_NORMALIZE_SPACE_STRING = "normalize-space";
/*     */   public static final String FUNC_TRANSLATE_STRING = "translate";
/*     */   public static final String FUNC_CONCAT_STRING = "concat";
/*     */   public static final String FUNC_SYSTEM_PROPERTY_STRING = "system-property";
/*     */   public static final String FUNC_EXT_FUNCTION_AVAILABLE_STRING = "function-available";
/*     */   public static final String FUNC_EXT_ELEM_AVAILABLE_STRING = "element-available";
/*     */   public static final String FUNC_SUBSTRING_STRING = "substring";
/*     */   public static final String FUNC_STRING_LENGTH_STRING = "string-length";
/*     */   public static final String FUNC_UNPARSED_ENTITY_URI_STRING = "unparsed-entity-uri";
/*     */   public static final String FUNC_DOCLOCATION_STRING = "document-location";
/*     */ 
/*     */   static Object getAxisName(String key)
/*     */   {
/* 274 */     return m_axisnames.get(key);
/*     */   }
/*     */ 
/*     */   static Object lookupNodeTest(String key) {
/* 278 */     return m_nodetests.get(key);
/*     */   }
/*     */ 
/*     */   static Object getKeyWord(String key) {
/* 282 */     return m_keywords.get(key);
/*     */   }
/*     */ 
/*     */   static Object getNodeType(String key) {
/* 286 */     return m_nodetypes.get(key);
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 220 */     m_axisnames.put("ancestor", new Integer(37));
/*     */ 
/* 222 */     m_axisnames.put("ancestor-or-self", new Integer(38));
/*     */ 
/* 224 */     m_axisnames.put("attribute", new Integer(39));
/*     */ 
/* 226 */     m_axisnames.put("child", new Integer(40));
/*     */ 
/* 228 */     m_axisnames.put("descendant", new Integer(41));
/*     */ 
/* 230 */     m_axisnames.put("descendant-or-self", new Integer(42));
/*     */ 
/* 232 */     m_axisnames.put("following", new Integer(43));
/*     */ 
/* 234 */     m_axisnames.put("following-sibling", new Integer(44));
/*     */ 
/* 236 */     m_axisnames.put("parent", new Integer(45));
/*     */ 
/* 238 */     m_axisnames.put("preceding", new Integer(46));
/*     */ 
/* 240 */     m_axisnames.put("preceding-sibling", new Integer(47));
/*     */ 
/* 242 */     m_axisnames.put("self", new Integer(48));
/*     */ 
/* 244 */     m_axisnames.put("namespace", new Integer(49));
/*     */ 
/* 246 */     m_nodetypes.put("comment", new Integer(1030));
/*     */ 
/* 248 */     m_nodetypes.put("text", new Integer(1031));
/*     */ 
/* 250 */     m_nodetypes.put("processing-instruction", new Integer(1032));
/*     */ 
/* 252 */     m_nodetypes.put("node", new Integer(1033));
/*     */ 
/* 254 */     m_nodetypes.put("*", new Integer(36));
/*     */ 
/* 256 */     m_keywords.put(".", new Integer(48));
/*     */ 
/* 258 */     m_keywords.put("id", new Integer(4));
/*     */ 
/* 260 */     m_keywords.put("key", new Integer(5));
/*     */ 
/* 263 */     m_nodetests.put("comment", new Integer(1030));
/*     */ 
/* 265 */     m_nodetests.put("text", new Integer(1031));
/*     */ 
/* 267 */     m_nodetests.put("processing-instruction", new Integer(1032));
/*     */ 
/* 269 */     m_nodetests.put("node", new Integer(1033));
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xpath.internal.compiler.Keywords
 * JD-Core Version:    0.6.2
 */