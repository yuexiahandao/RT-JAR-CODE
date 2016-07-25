/*     */ package javax.swing.text.html.parser;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.BitSet;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public final class Element
/*     */   implements DTDConstants, Serializable
/*     */ {
/*     */   public int index;
/*     */   public String name;
/*     */   public boolean oStart;
/*     */   public boolean oEnd;
/*     */   public BitSet inclusions;
/*     */   public BitSet exclusions;
/*  50 */   public int type = 19;
/*     */   public ContentModel content;
/*     */   public AttributeList atts;
/*  54 */   static int maxIndex = 0;
/*     */   public Object data;
/* 162 */   static Hashtable<String, Integer> contentTypes = new Hashtable();
/*     */ 
/*     */   Element()
/*     */   {
/*     */   }
/*     */ 
/*     */   Element(String paramString, int paramInt)
/*     */   {
/*  69 */     this.name = paramString;
/*  70 */     this.index = paramInt;
/*  71 */     maxIndex = Math.max(maxIndex, paramInt);
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/*  78 */     return this.name;
/*     */   }
/*     */ 
/*     */   public boolean omitStart()
/*     */   {
/*  85 */     return this.oStart;
/*     */   }
/*     */ 
/*     */   public boolean omitEnd()
/*     */   {
/*  92 */     return this.oEnd;
/*     */   }
/*     */ 
/*     */   public int getType()
/*     */   {
/*  99 */     return this.type;
/*     */   }
/*     */ 
/*     */   public ContentModel getContent()
/*     */   {
/* 106 */     return this.content;
/*     */   }
/*     */ 
/*     */   public AttributeList getAttributes()
/*     */   {
/* 113 */     return this.atts;
/*     */   }
/*     */ 
/*     */   public int getIndex()
/*     */   {
/* 120 */     return this.index;
/*     */   }
/*     */ 
/*     */   public boolean isEmpty()
/*     */   {
/* 127 */     return this.type == 17;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 134 */     return this.name;
/*     */   }
/*     */ 
/*     */   public AttributeList getAttribute(String paramString)
/*     */   {
/* 141 */     for (AttributeList localAttributeList = this.atts; localAttributeList != null; localAttributeList = localAttributeList.next) {
/* 142 */       if (localAttributeList.name.equals(paramString)) {
/* 143 */         return localAttributeList;
/*     */       }
/*     */     }
/* 146 */     return null;
/*     */   }
/*     */ 
/*     */   public AttributeList getAttributeByValue(String paramString)
/*     */   {
/* 153 */     for (AttributeList localAttributeList = this.atts; localAttributeList != null; localAttributeList = localAttributeList.next) {
/* 154 */       if ((localAttributeList.values != null) && (localAttributeList.values.contains(paramString))) {
/* 155 */         return localAttributeList;
/*     */       }
/*     */     }
/* 158 */     return null;
/*     */   }
/*     */ 
/*     */   public static int name2type(String paramString)
/*     */   {
/* 172 */     Integer localInteger = (Integer)contentTypes.get(paramString);
/* 173 */     return localInteger != null ? localInteger.intValue() : 0;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 165 */     contentTypes.put("CDATA", Integer.valueOf(1));
/* 166 */     contentTypes.put("RCDATA", Integer.valueOf(16));
/* 167 */     contentTypes.put("EMPTY", Integer.valueOf(17));
/* 168 */     contentTypes.put("ANY", Integer.valueOf(19));
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.text.html.parser.Element
 * JD-Core Version:    0.6.2
 */