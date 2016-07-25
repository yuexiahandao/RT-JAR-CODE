/*     */ package com.sun.org.apache.xalan.internal.lib;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.utils.WrappedRuntimeException;
/*     */ import com.sun.org.apache.xpath.internal.NodeSet;
/*     */ import java.util.StringTokenizer;
/*     */ import javax.xml.parsers.DocumentBuilder;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ import org.w3c.dom.Text;
/*     */ 
/*     */ public class ExsltStrings extends ExsltBase
/*     */ {
/*     */   static final String JDK_DEFAULT_DOM = "com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl";
/*     */ 
/*     */   public static String align(String targetStr, String paddingStr, String type)
/*     */   {
/*  88 */     if (targetStr.length() >= paddingStr.length()) {
/*  89 */       return targetStr.substring(0, paddingStr.length());
/*     */     }
/*  91 */     if (type.equals("right"))
/*     */     {
/*  93 */       return paddingStr.substring(0, paddingStr.length() - targetStr.length()) + targetStr;
/*     */     }
/*  95 */     if (type.equals("center"))
/*     */     {
/*  97 */       int startIndex = (paddingStr.length() - targetStr.length()) / 2;
/*  98 */       return paddingStr.substring(0, startIndex) + targetStr + paddingStr.substring(startIndex + targetStr.length());
/*     */     }
/*     */ 
/* 103 */     return targetStr + paddingStr.substring(targetStr.length());
/*     */   }
/*     */ 
/*     */   public static String align(String targetStr, String paddingStr)
/*     */   {
/* 112 */     return align(targetStr, paddingStr, "left");
/*     */   }
/*     */ 
/*     */   public static String concat(NodeList nl)
/*     */   {
/* 125 */     StringBuffer sb = new StringBuffer();
/* 126 */     for (int i = 0; i < nl.getLength(); i++)
/*     */     {
/* 128 */       Node node = nl.item(i);
/* 129 */       String value = toString(node);
/*     */ 
/* 131 */       if ((value != null) && (value.length() > 0)) {
/* 132 */         sb.append(value);
/*     */       }
/*     */     }
/* 135 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   public static String padding(double length, String pattern)
/*     */   {
/* 155 */     if ((pattern == null) || (pattern.length() == 0)) {
/* 156 */       return "";
/*     */     }
/* 158 */     StringBuffer sb = new StringBuffer();
/* 159 */     int len = (int)length;
/* 160 */     int numAdded = 0;
/* 161 */     int index = 0;
/* 162 */     while (numAdded < len)
/*     */     {
/* 164 */       if (index == pattern.length()) {
/* 165 */         index = 0;
/*     */       }
/* 167 */       sb.append(pattern.charAt(index));
/* 168 */       index++;
/* 169 */       numAdded++;
/*     */     }
/*     */ 
/* 172 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   public static String padding(double length)
/*     */   {
/* 180 */     return padding(length, " ");
/*     */   }
/*     */ 
/*     */   public static NodeList split(String str, String pattern)
/*     */   {
/* 208 */     NodeSet resultSet = new NodeSet();
/* 209 */     resultSet.setShouldCacheNodes(true);
/*     */ 
/* 211 */     boolean done = false;
/* 212 */     int fromIndex = 0;
/* 213 */     int matchIndex = 0;
/* 214 */     String token = null;
/*     */ 
/* 216 */     while ((!done) && (fromIndex < str.length()))
/*     */     {
/* 218 */       matchIndex = str.indexOf(pattern, fromIndex);
/* 219 */       if (matchIndex >= 0)
/*     */       {
/* 221 */         token = str.substring(fromIndex, matchIndex);
/* 222 */         fromIndex = matchIndex + pattern.length();
/*     */       }
/*     */       else
/*     */       {
/* 226 */         done = true;
/* 227 */         token = str.substring(fromIndex);
/*     */       }
/*     */ 
/* 230 */       Document doc = getDocument();
/* 231 */       synchronized (doc)
/*     */       {
/* 233 */         Element element = doc.createElement("token");
/* 234 */         Text text = doc.createTextNode(token);
/* 235 */         element.appendChild(text);
/* 236 */         resultSet.addNode(element);
/*     */       }
/*     */     }
/*     */ 
/* 240 */     return resultSet;
/*     */   }
/*     */ 
/*     */   public static NodeList split(String str)
/*     */   {
/* 248 */     return split(str, " ");
/*     */   }
/*     */ 
/*     */   public static NodeList tokenize(String toTokenize, String delims)
/*     */   {
/* 288 */     NodeSet resultSet = new NodeSet();
/*     */ 
/* 290 */     if ((delims != null) && (delims.length() > 0))
/*     */     {
/* 292 */       StringTokenizer lTokenizer = new StringTokenizer(toTokenize, delims);
/*     */ 
/* 294 */       Document doc = getDocument();
/* 295 */       synchronized (doc)
/*     */       {
/* 297 */         while (lTokenizer.hasMoreTokens())
/*     */         {
/* 299 */           Element element = doc.createElement("token");
/* 300 */           element.appendChild(doc.createTextNode(lTokenizer.nextToken()));
/* 301 */           resultSet.addNode(element);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 310 */       Document doc = getDocument();
/* 311 */       synchronized (doc)
/*     */       {
/* 313 */         for (int i = 0; i < toTokenize.length(); i++)
/*     */         {
/* 315 */           Element element = doc.createElement("token");
/* 316 */           element.appendChild(doc.createTextNode(toTokenize.substring(i, i + 1)));
/* 317 */           resultSet.addNode(element);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 322 */     return resultSet;
/*     */   }
/*     */ 
/*     */   public static NodeList tokenize(String toTokenize)
/*     */   {
/* 330 */     return tokenize(toTokenize, " \t\n\r");
/*     */   }
/*     */ 
/*     */   private static Document getDocument()
/*     */   {
/*     */     try
/*     */     {
/* 340 */       if (System.getSecurityManager() == null) {
/* 341 */         return DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
/*     */       }
/* 343 */       return DocumentBuilderFactory.newInstance("com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl", null).newDocumentBuilder().newDocument();
/*     */     }
/*     */     catch (ParserConfigurationException pce)
/*     */     {
/* 348 */       throw new WrappedRuntimeException(pce);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.lib.ExsltStrings
 * JD-Core Version:    0.6.2
 */