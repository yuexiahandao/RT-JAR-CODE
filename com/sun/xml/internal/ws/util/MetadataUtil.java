/*    */ package com.sun.xml.internal.ws.util;
/*    */ 
/*    */ import com.sun.istack.internal.NotNull;
/*    */ import com.sun.xml.internal.ws.api.server.SDDocument;
/*    */ import com.sun.xml.internal.ws.wsdl.SDDocumentResolver;
/*    */ import java.net.URL;
/*    */ import java.util.HashMap;
/*    */ import java.util.HashSet;
/*    */ import java.util.Iterator;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ 
/*    */ public class MetadataUtil
/*    */ {
/*    */   public static Map<String, SDDocument> getMetadataClosure(@NotNull String systemId, @NotNull SDDocumentResolver resolver, boolean onlyTopLevelSchemas)
/*    */   {
/* 53 */     Map closureDocs = new HashMap();
/* 54 */     Set remaining = new HashSet();
/* 55 */     remaining.add(systemId);
/*    */ 
/* 57 */     while (!remaining.isEmpty()) {
/* 58 */       Iterator it = remaining.iterator();
/* 59 */       String current = (String)it.next();
/* 60 */       remaining.remove(current);
/*    */ 
/* 62 */       SDDocument currentDoc = resolver.resolve(current);
/* 63 */       SDDocument old = (SDDocument)closureDocs.put(currentDoc.getURL().toExternalForm(), currentDoc);
/* 64 */       assert (old == null);
/*    */ 
/* 66 */       Set imports = currentDoc.getImports();
/* 67 */       if ((!currentDoc.isSchema()) || (!onlyTopLevelSchemas)) {
/* 68 */         for (String importedDoc : imports) {
/* 69 */           if (closureDocs.get(importedDoc) == null) {
/* 70 */             remaining.add(importedDoc);
/*    */           }
/*    */         }
/*    */       }
/*    */     }
/*    */ 
/* 76 */     return closureDocs;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.util.MetadataUtil
 * JD-Core Version:    0.6.2
 */