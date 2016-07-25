/*     */ package com.sun.org.apache.xml.internal.security.transforms.implementations;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.security.signature.NodeFilter;
/*     */ import com.sun.org.apache.xml.internal.security.utils.XMLUtils;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ 
/*     */ class XPath2NodeFilter
/*     */   implements NodeFilter
/*     */ {
/*     */   boolean hasUnionFilter;
/*     */   boolean hasSubstractFilter;
/*     */   boolean hasIntersectFilter;
/*     */   Set unionNodes;
/*     */   Set substractNodes;
/*     */   Set intersectNodes;
/* 217 */   int inSubstract = -1;
/* 218 */   int inIntersect = -1;
/* 219 */   int inUnion = -1;
/*     */ 
/*     */   XPath2NodeFilter(List paramList1, List paramList2, List paramList3)
/*     */   {
/* 181 */     this.hasUnionFilter = (!paramList1.isEmpty());
/* 182 */     this.unionNodes = convertNodeListToSet(paramList1);
/* 183 */     this.hasSubstractFilter = (!paramList2.isEmpty());
/* 184 */     this.substractNodes = convertNodeListToSet(paramList2);
/* 185 */     this.hasIntersectFilter = (!paramList3.isEmpty());
/* 186 */     this.intersectNodes = convertNodeListToSet(paramList3);
/*     */   }
/*     */ 
/*     */   public int isNodeInclude(Node paramNode)
/*     */   {
/* 197 */     int i = 1;
/*     */ 
/* 199 */     if ((this.hasSubstractFilter) && (rooted(paramNode, this.substractNodes)))
/* 200 */       i = -1;
/* 201 */     else if ((this.hasIntersectFilter) && (!rooted(paramNode, this.intersectNodes))) {
/* 202 */       i = 0;
/*     */     }
/*     */ 
/* 206 */     if (i == 1)
/* 207 */       return 1;
/* 208 */     if (this.hasUnionFilter) {
/* 209 */       if (rooted(paramNode, this.unionNodes)) {
/* 210 */         return 1;
/*     */       }
/* 212 */       i = 0;
/*     */     }
/* 214 */     return i;
/*     */   }
/*     */ 
/*     */   public int isNodeIncludeDO(Node paramNode, int paramInt)
/*     */   {
/* 221 */     int i = 1;
/* 222 */     if (this.hasSubstractFilter) {
/* 223 */       if ((this.inSubstract == -1) || (paramInt <= this.inSubstract)) {
/* 224 */         if (inList(paramNode, this.substractNodes))
/* 225 */           this.inSubstract = paramInt;
/*     */         else {
/* 227 */           this.inSubstract = -1;
/*     */         }
/*     */       }
/* 230 */       if (this.inSubstract != -1) {
/* 231 */         i = -1;
/*     */       }
/*     */     }
/* 234 */     if ((i != -1) && 
/* 235 */       (this.hasIntersectFilter) && (
/* 236 */       (this.inIntersect == -1) || (paramInt <= this.inIntersect))) {
/* 237 */       if (!inList(paramNode, this.intersectNodes)) {
/* 238 */         this.inIntersect = -1;
/* 239 */         i = 0;
/*     */       } else {
/* 241 */         this.inIntersect = paramInt;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 247 */     if (paramInt <= this.inUnion)
/* 248 */       this.inUnion = -1;
/* 249 */     if (i == 1)
/* 250 */       return 1;
/* 251 */     if (this.hasUnionFilter) {
/* 252 */       if ((this.inUnion == -1) && (inList(paramNode, this.unionNodes))) {
/* 253 */         this.inUnion = paramInt;
/*     */       }
/* 255 */       if (this.inUnion != -1)
/* 256 */         return 1;
/* 257 */       i = 0;
/*     */     }
/*     */ 
/* 260 */     return i;
/*     */   }
/*     */ 
/*     */   static boolean rooted(Node paramNode, Set paramSet)
/*     */   {
/* 271 */     if (paramSet.isEmpty()) {
/* 272 */       return false;
/*     */     }
/* 274 */     if (paramSet.contains(paramNode)) {
/* 275 */       return true;
/*     */     }
/* 277 */     Iterator localIterator = paramSet.iterator();
/* 278 */     while (localIterator.hasNext()) {
/* 279 */       Node localNode = (Node)localIterator.next();
/* 280 */       if (XMLUtils.isDescendantOrSelf(localNode, paramNode)) {
/* 281 */         return true;
/*     */       }
/*     */     }
/* 284 */     return false;
/*     */   }
/*     */ 
/*     */   static boolean inList(Node paramNode, Set paramSet)
/*     */   {
/* 295 */     return paramSet.contains(paramNode);
/*     */   }
/*     */ 
/*     */   private static Set convertNodeListToSet(List paramList) {
/* 299 */     HashSet localHashSet = new HashSet();
/* 300 */     for (int i = 0; i < paramList.size(); i++) {
/* 301 */       NodeList localNodeList = (NodeList)paramList.get(i);
/* 302 */       int j = localNodeList.getLength();
/* 303 */       for (int k = 0; k < j; k++) {
/* 304 */         Node localNode = localNodeList.item(k);
/* 305 */         localHashSet.add(localNode);
/*     */       }
/*     */     }
/* 308 */     return localHashSet;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.transforms.implementations.XPath2NodeFilter
 * JD-Core Version:    0.6.2
 */