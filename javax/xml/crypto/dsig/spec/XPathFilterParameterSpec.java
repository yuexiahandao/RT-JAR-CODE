/*     */ package javax.xml.crypto.dsig.spec;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ 
/*     */ public final class XPathFilterParameterSpec
/*     */   implements TransformParameterSpec
/*     */ {
/*     */   private String xPath;
/*     */   private Map nsMap;
/*     */ 
/*     */   public XPathFilterParameterSpec(String paramString)
/*     */   {
/*  65 */     if (paramString == null) {
/*  66 */       throw new NullPointerException();
/*     */     }
/*  68 */     this.xPath = paramString;
/*  69 */     this.nsMap = Collections.EMPTY_MAP;
/*     */   }
/*     */ 
/*     */   public XPathFilterParameterSpec(String paramString, Map paramMap)
/*     */   {
/*  87 */     if ((paramString == null) || (paramMap == null)) {
/*  88 */       throw new NullPointerException();
/*     */     }
/*  90 */     this.xPath = paramString;
/*  91 */     this.nsMap = new HashMap(paramMap);
/*  92 */     Iterator localIterator = this.nsMap.entrySet().iterator();
/*  93 */     while (localIterator.hasNext()) {
/*  94 */       Map.Entry localEntry = (Map.Entry)localIterator.next();
/*  95 */       if ((!(localEntry.getKey() instanceof String)) || (!(localEntry.getValue() instanceof String)))
/*     */       {
/*  97 */         throw new ClassCastException("not a String");
/*     */       }
/*     */     }
/* 100 */     this.nsMap = Collections.unmodifiableMap(this.nsMap);
/*     */   }
/*     */ 
/*     */   public String getXPath()
/*     */   {
/* 109 */     return this.xPath;
/*     */   }
/*     */ 
/*     */   public Map getNamespaceMap()
/*     */   {
/* 124 */     return this.nsMap;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.crypto.dsig.spec.XPathFilterParameterSpec
 * JD-Core Version:    0.6.2
 */