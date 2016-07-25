/*     */ package javax.swing.text.html.parser;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public final class AttributeList
/*     */   implements DTDConstants, Serializable
/*     */ {
/*     */   public String name;
/*     */   public int type;
/*     */   public Vector<?> values;
/*     */   public int modifier;
/*     */   public String value;
/*     */   public AttributeList next;
/* 132 */   static Hashtable<Object, Object> attributeTypes = new Hashtable();
/*     */ 
/*     */   AttributeList()
/*     */   {
/*     */   }
/*     */ 
/*     */   public AttributeList(String paramString)
/*     */   {
/*  63 */     this.name = paramString;
/*     */   }
/*     */ 
/*     */   public AttributeList(String paramString1, int paramInt1, int paramInt2, String paramString2, Vector<?> paramVector, AttributeList paramAttributeList)
/*     */   {
/*  70 */     this.name = paramString1;
/*  71 */     this.type = paramInt1;
/*  72 */     this.modifier = paramInt2;
/*  73 */     this.value = paramString2;
/*  74 */     this.values = paramVector;
/*  75 */     this.next = paramAttributeList;
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/*  82 */     return this.name;
/*     */   }
/*     */ 
/*     */   public int getType()
/*     */   {
/*  90 */     return this.type;
/*     */   }
/*     */ 
/*     */   public int getModifier()
/*     */   {
/*  98 */     return this.modifier;
/*     */   }
/*     */ 
/*     */   public Enumeration<?> getValues()
/*     */   {
/* 105 */     return this.values != null ? this.values.elements() : null;
/*     */   }
/*     */ 
/*     */   public String getValue()
/*     */   {
/* 112 */     return this.value;
/*     */   }
/*     */ 
/*     */   public AttributeList getNext()
/*     */   {
/* 119 */     return this.next;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 126 */     return this.name;
/*     */   }
/*     */ 
/*     */   static void defineAttributeType(String paramString, int paramInt)
/*     */   {
/* 135 */     Integer localInteger = Integer.valueOf(paramInt);
/* 136 */     attributeTypes.put(paramString, localInteger);
/* 137 */     attributeTypes.put(localInteger, paramString);
/*     */   }
/*     */ 
/*     */   public static int name2type(String paramString)
/*     */   {
/* 165 */     Integer localInteger = (Integer)attributeTypes.get(paramString);
/* 166 */     return localInteger == null ? 1 : localInteger.intValue();
/*     */   }
/*     */ 
/*     */   public static String type2name(int paramInt) {
/* 170 */     return (String)attributeTypes.get(Integer.valueOf(paramInt));
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 141 */     defineAttributeType("CDATA", 1);
/* 142 */     defineAttributeType("ENTITY", 2);
/* 143 */     defineAttributeType("ENTITIES", 3);
/* 144 */     defineAttributeType("ID", 4);
/* 145 */     defineAttributeType("IDREF", 5);
/* 146 */     defineAttributeType("IDREFS", 6);
/* 147 */     defineAttributeType("NAME", 7);
/* 148 */     defineAttributeType("NAMES", 8);
/* 149 */     defineAttributeType("NMTOKEN", 9);
/* 150 */     defineAttributeType("NMTOKENS", 10);
/* 151 */     defineAttributeType("NOTATION", 11);
/* 152 */     defineAttributeType("NUMBER", 12);
/* 153 */     defineAttributeType("NUMBERS", 13);
/* 154 */     defineAttributeType("NUTOKEN", 14);
/* 155 */     defineAttributeType("NUTOKENS", 15);
/*     */ 
/* 157 */     attributeTypes.put("fixed", Integer.valueOf(1));
/* 158 */     attributeTypes.put("required", Integer.valueOf(2));
/* 159 */     attributeTypes.put("current", Integer.valueOf(3));
/* 160 */     attributeTypes.put("conref", Integer.valueOf(4));
/* 161 */     attributeTypes.put("implied", Integer.valueOf(5));
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.text.html.parser.AttributeList
 * JD-Core Version:    0.6.2
 */