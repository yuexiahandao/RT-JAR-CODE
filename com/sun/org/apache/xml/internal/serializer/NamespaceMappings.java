/*     */ package com.sun.org.apache.xml.internal.serializer;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ import java.util.Stack;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public class NamespaceMappings
/*     */ {
/*     */   private int count;
/*  83 */   private HashMap m_namespaces = new HashMap();
/*     */ 
/*  97 */   private Stack m_nodeStack = new Stack();
/*     */   private static final String EMPTYSTRING = "";
/*     */   private static final String XML_PREFIX = "xml";
/*     */ 
/*     */   public NamespaceMappings()
/*     */   {
/* 108 */     initNamespaces();
/*     */   }
/*     */ 
/*     */   private void initNamespaces()
/*     */   {
/*     */     Stack stack;
/* 121 */     this.m_namespaces.put("", stack = new Stack());
/* 122 */     stack.push(new MappingRecord("", "", 0));
/*     */ 
/* 124 */     this.m_namespaces.put("xml", stack = new Stack());
/* 125 */     stack.push(new MappingRecord("xml", "http://www.w3.org/XML/1998/namespace", 0));
/*     */ 
/* 128 */     this.m_nodeStack.push(new MappingRecord(null, null, -1));
/*     */   }
/*     */ 
/*     */   public String lookupNamespace(String prefix)
/*     */   {
/* 140 */     Stack stack = (Stack)this.m_namespaces.get(prefix);
/* 141 */     return (stack != null) && (!stack.isEmpty()) ? ((MappingRecord)stack.peek()).m_uri : null;
/*     */   }
/*     */ 
/*     */   MappingRecord getMappingFromPrefix(String prefix)
/*     */   {
/* 146 */     Stack stack = (Stack)this.m_namespaces.get(prefix);
/* 147 */     return (stack != null) && (!stack.isEmpty()) ? (MappingRecord)stack.peek() : null;
/*     */   }
/*     */ 
/*     */   public String lookupPrefix(String uri)
/*     */   {
/* 161 */     String foundPrefix = null;
/* 162 */     Iterator itr = this.m_namespaces.keySet().iterator();
/* 163 */     while (itr.hasNext()) {
/* 164 */       String prefix = (String)itr.next();
/* 165 */       String uri2 = lookupNamespace(prefix);
/* 166 */       if ((uri2 != null) && (uri2.equals(uri)))
/*     */       {
/* 168 */         foundPrefix = prefix;
/* 169 */         break;
/*     */       }
/*     */     }
/* 172 */     return foundPrefix;
/*     */   }
/*     */ 
/*     */   MappingRecord getMappingFromURI(String uri)
/*     */   {
/* 177 */     MappingRecord foundMap = null;
/* 178 */     Iterator itr = this.m_namespaces.keySet().iterator();
/* 179 */     while (itr.hasNext())
/*     */     {
/* 181 */       String prefix = (String)itr.next();
/* 182 */       MappingRecord map2 = getMappingFromPrefix(prefix);
/* 183 */       if ((map2 != null) && (map2.m_uri.equals(uri)))
/*     */       {
/* 185 */         foundMap = map2;
/* 186 */         break;
/*     */       }
/*     */     }
/* 189 */     return foundMap;
/*     */   }
/*     */ 
/*     */   boolean popNamespace(String prefix)
/*     */   {
/* 198 */     if (prefix.startsWith("xml"))
/*     */     {
/* 200 */       return false;
/*     */     }
/*     */     Stack stack;
/* 204 */     if ((stack = (Stack)this.m_namespaces.get(prefix)) != null)
/*     */     {
/* 206 */       stack.pop();
/* 207 */       return true;
/*     */     }
/* 209 */     return false;
/*     */   }
/*     */ 
/*     */   boolean pushNamespace(String prefix, String uri, int elemDepth)
/*     */   {
/* 221 */     if (prefix.startsWith("xml"))
/*     */     {
/* 223 */       return false;
/*     */     }
/*     */     Stack stack;
/* 228 */     if ((stack = (Stack)this.m_namespaces.get(prefix)) == null)
/*     */     {
/* 230 */       this.m_namespaces.put(prefix, stack = new Stack());
/*     */     }
/*     */ 
/* 233 */     if ((!stack.empty()) && (uri.equals(((MappingRecord)stack.peek()).m_uri)))
/*     */     {
/* 235 */       return false;
/*     */     }
/* 237 */     MappingRecord map = new MappingRecord(prefix, uri, elemDepth);
/* 238 */     stack.push(map);
/* 239 */     this.m_nodeStack.push(map);
/* 240 */     return true;
/*     */   }
/*     */ 
/*     */   void popNamespaces(int elemDepth, ContentHandler saxHandler)
/*     */   {
/*     */     while (true)
/*     */     {
/* 255 */       if (this.m_nodeStack.isEmpty())
/* 256 */         return;
/* 257 */       MappingRecord map = (MappingRecord)this.m_nodeStack.peek();
/* 258 */       int depth = map.m_declarationDepth;
/* 259 */       if (depth < elemDepth) {
/* 260 */         return;
/*     */       }
/*     */ 
/* 265 */       map = (MappingRecord)this.m_nodeStack.pop();
/* 266 */       String prefix = map.m_prefix;
/* 267 */       popNamespace(prefix);
/* 268 */       if (saxHandler != null)
/*     */       {
/*     */         try
/*     */         {
/* 272 */           saxHandler.endPrefixMapping(prefix);
/*     */         }
/*     */         catch (SAXException e)
/*     */         {
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public String generateNextPrefix()
/*     */   {
/* 289 */     return "ns" + this.count++;
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */     throws CloneNotSupportedException
/*     */   {
/* 298 */     NamespaceMappings clone = new NamespaceMappings();
/* 299 */     clone.m_nodeStack = ((Stack)this.m_nodeStack.clone());
/* 300 */     clone.m_namespaces = ((HashMap)this.m_namespaces.clone());
/* 301 */     clone.count = this.count;
/* 302 */     return clone;
/*     */   }
/*     */ 
/*     */   final void reset()
/*     */   {
/* 308 */     this.count = 0;
/* 309 */     this.m_namespaces.clear();
/* 310 */     this.m_nodeStack.clear();
/* 311 */     initNamespaces();
/*     */   }
/*     */   class MappingRecord {
/*     */     final String m_prefix;
/*     */     final String m_uri;
/*     */     final int m_declarationDepth;
/*     */ 
/* 320 */     MappingRecord(String prefix, String uri, int depth) { this.m_prefix = prefix;
/* 321 */       this.m_uri = uri;
/* 322 */       this.m_declarationDepth = depth;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.serializer.NamespaceMappings
 * JD-Core Version:    0.6.2
 */