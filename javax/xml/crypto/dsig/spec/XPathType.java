/*     */ package javax.xml.crypto.dsig.spec;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class XPathType
/*     */ {
/*     */   private final String expression;
/*     */   private final Filter filter;
/*     */   private Map nsMap;
/*     */ 
/*     */   public XPathType(String paramString, Filter paramFilter)
/*     */   {
/* 122 */     if (paramString == null) {
/* 123 */       throw new NullPointerException("expression cannot be null");
/*     */     }
/* 125 */     if (paramFilter == null) {
/* 126 */       throw new NullPointerException("filter cannot be null");
/*     */     }
/* 128 */     this.expression = paramString;
/* 129 */     this.filter = paramFilter;
/* 130 */     this.nsMap = Collections.EMPTY_MAP;
/*     */   }
/*     */ 
/*     */   public XPathType(String paramString, Filter paramFilter, Map paramMap)
/*     */   {
/* 151 */     this(paramString, paramFilter);
/* 152 */     if (paramMap == null) {
/* 153 */       throw new NullPointerException("namespaceMap cannot be null");
/*     */     }
/* 155 */     this.nsMap = new HashMap(paramMap);
/* 156 */     Iterator localIterator = this.nsMap.entrySet().iterator();
/* 157 */     while (localIterator.hasNext()) {
/* 158 */       Map.Entry localEntry = (Map.Entry)localIterator.next();
/* 159 */       if ((!(localEntry.getKey() instanceof String)) || (!(localEntry.getValue() instanceof String)))
/*     */       {
/* 161 */         throw new ClassCastException("not a String");
/*     */       }
/*     */     }
/* 164 */     this.nsMap = Collections.unmodifiableMap(this.nsMap);
/*     */   }
/*     */ 
/*     */   public String getExpression()
/*     */   {
/* 173 */     return this.expression;
/*     */   }
/*     */ 
/*     */   public Filter getFilter()
/*     */   {
/* 182 */     return this.filter;
/*     */   }
/*     */ 
/*     */   public Map getNamespaceMap()
/*     */   {
/* 197 */     return this.nsMap;
/*     */   }
/*     */ 
/*     */   public static class Filter
/*     */   {
/*     */     private final String operation;
/*  94 */     public static final Filter INTERSECT = new Filter("intersect");
/*     */ 
/*  99 */     public static final Filter SUBTRACT = new Filter("subtract");
/*     */ 
/* 104 */     public static final Filter UNION = new Filter("union");
/*     */ 
/*     */     private Filter(String paramString)
/*     */     {
/*  79 */       this.operation = paramString;
/*     */     }
/*     */ 
/*     */     public String toString()
/*     */     {
/*  88 */       return this.operation;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.crypto.dsig.spec.XPathType
 * JD-Core Version:    0.6.2
 */