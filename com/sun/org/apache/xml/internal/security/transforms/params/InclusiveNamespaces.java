/*     */ package com.sun.org.apache.xml.internal.security.transforms.params;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;
/*     */ import com.sun.org.apache.xml.internal.security.transforms.TransformParam;
/*     */ import com.sun.org.apache.xml.internal.security.utils.ElementProxy;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ import java.util.SortedSet;
/*     */ import java.util.StringTokenizer;
/*     */ import java.util.TreeSet;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ 
/*     */ public class InclusiveNamespaces extends ElementProxy
/*     */   implements TransformParam
/*     */ {
/*     */   public static final String _TAG_EC_INCLUSIVENAMESPACES = "InclusiveNamespaces";
/*     */   public static final String _ATT_EC_PREFIXLIST = "PrefixList";
/*     */   public static final String ExclusiveCanonicalizationNamespace = "http://www.w3.org/2001/10/xml-exc-c14n#";
/*     */ 
/*     */   public InclusiveNamespaces(Document paramDocument, String paramString)
/*     */   {
/*  68 */     this(paramDocument, prefixStr2Set(paramString));
/*     */   }
/*     */ 
/*     */   public InclusiveNamespaces(Document paramDocument, Set paramSet)
/*     */   {
/*  79 */     super(paramDocument);
/*     */ 
/*  81 */     StringBuffer localStringBuffer = new StringBuffer();
/*  82 */     TreeSet localTreeSet = new TreeSet(paramSet);
/*     */ 
/*  85 */     Iterator localIterator = localTreeSet.iterator();
/*     */ 
/*  87 */     while (localIterator.hasNext()) {
/*  88 */       String str = (String)localIterator.next();
/*     */ 
/*  90 */       if (str.equals("xmlns"))
/*  91 */         localStringBuffer.append("#default ");
/*     */       else {
/*  93 */         localStringBuffer.append(str + " ");
/*     */       }
/*     */     }
/*     */ 
/*  97 */     this._constructionElement.setAttributeNS(null, "PrefixList", localStringBuffer.toString().trim());
/*     */   }
/*     */ 
/*     */   public String getInclusiveNamespaces()
/*     */   {
/* 108 */     return this._constructionElement.getAttributeNS(null, "PrefixList");
/*     */   }
/*     */ 
/*     */   public InclusiveNamespaces(Element paramElement, String paramString)
/*     */     throws XMLSecurityException
/*     */   {
/* 121 */     super(paramElement, paramString);
/*     */   }
/*     */ 
/*     */   public static SortedSet prefixStr2Set(String paramString)
/*     */   {
/* 143 */     TreeSet localTreeSet = new TreeSet();
/*     */ 
/* 145 */     if ((paramString == null) || (paramString.length() == 0))
/*     */     {
/* 147 */       return localTreeSet;
/*     */     }
/*     */ 
/* 150 */     StringTokenizer localStringTokenizer = new StringTokenizer(paramString, " \t\r\n");
/*     */ 
/* 152 */     while (localStringTokenizer.hasMoreTokens()) {
/* 153 */       String str = localStringTokenizer.nextToken();
/*     */ 
/* 155 */       if (str.equals("#default"))
/* 156 */         localTreeSet.add("xmlns");
/*     */       else {
/* 158 */         localTreeSet.add(str);
/*     */       }
/*     */     }
/*     */ 
/* 162 */     return localTreeSet;
/*     */   }
/*     */ 
/*     */   public String getBaseNamespace()
/*     */   {
/* 171 */     return "http://www.w3.org/2001/10/xml-exc-c14n#";
/*     */   }
/*     */ 
/*     */   public String getBaseLocalName()
/*     */   {
/* 180 */     return "InclusiveNamespaces";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.transforms.params.InclusiveNamespaces
 * JD-Core Version:    0.6.2
 */