/*     */ package javax.swing.text.html.parser;
/*     */ 
/*     */ import java.util.Hashtable;
/*     */ 
/*     */ public final class Entity
/*     */   implements DTDConstants
/*     */ {
/*     */   public String name;
/*     */   public int type;
/*     */   public char[] data;
/* 110 */   static Hashtable<String, Integer> entityTypes = new Hashtable();
/*     */ 
/*     */   public Entity(String paramString, int paramInt, char[] paramArrayOfChar)
/*     */   {
/*  56 */     this.name = paramString;
/*  57 */     this.type = paramInt;
/*  58 */     this.data = paramArrayOfChar;
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/*  66 */     return this.name;
/*     */   }
/*     */ 
/*     */   public int getType()
/*     */   {
/*  74 */     return this.type & 0xFFFF;
/*     */   }
/*     */ 
/*     */   public boolean isParameter()
/*     */   {
/*  82 */     return (this.type & 0x40000) != 0;
/*     */   }
/*     */ 
/*     */   public boolean isGeneral()
/*     */   {
/*  90 */     return (this.type & 0x10000) != 0;
/*     */   }
/*     */ 
/*     */   public char[] getData()
/*     */   {
/*  98 */     return this.data;
/*     */   }
/*     */ 
/*     */   public String getString()
/*     */   {
/* 106 */     return new String(this.data, 0, this.data.length);
/*     */   }
/*     */ 
/*     */   public static int name2type(String paramString)
/*     */   {
/* 136 */     Integer localInteger = (Integer)entityTypes.get(paramString);
/* 137 */     return localInteger == null ? 1 : localInteger.intValue();
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 113 */     entityTypes.put("PUBLIC", Integer.valueOf(10));
/* 114 */     entityTypes.put("CDATA", Integer.valueOf(1));
/* 115 */     entityTypes.put("SDATA", Integer.valueOf(11));
/* 116 */     entityTypes.put("PI", Integer.valueOf(12));
/* 117 */     entityTypes.put("STARTTAG", Integer.valueOf(13));
/* 118 */     entityTypes.put("ENDTAG", Integer.valueOf(14));
/* 119 */     entityTypes.put("MS", Integer.valueOf(15));
/* 120 */     entityTypes.put("MD", Integer.valueOf(16));
/* 121 */     entityTypes.put("SYSTEM", Integer.valueOf(17));
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.text.html.parser.Entity
 * JD-Core Version:    0.6.2
 */