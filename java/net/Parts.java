/*      */ package java.net;
/*      */ 
/*      */ class Parts
/*      */ {
/*      */   String path;
/*      */   String query;
/*      */   String ref;
/*      */ 
/*      */   Parts(String paramString)
/*      */   {
/* 1313 */     int i = paramString.indexOf('#');
/* 1314 */     this.ref = (i < 0 ? null : paramString.substring(i + 1));
/* 1315 */     paramString = i < 0 ? paramString : paramString.substring(0, i);
/* 1316 */     int j = paramString.lastIndexOf('?');
/* 1317 */     if (j != -1) {
/* 1318 */       this.query = paramString.substring(j + 1);
/* 1319 */       this.path = paramString.substring(0, j);
/*      */     } else {
/* 1321 */       this.path = paramString;
/*      */     }
/*      */   }
/*      */ 
/*      */   String getPath() {
/* 1326 */     return this.path;
/*      */   }
/*      */ 
/*      */   String getQuery() {
/* 1330 */     return this.query;
/*      */   }
/*      */ 
/*      */   String getRef() {
/* 1334 */     return this.ref;
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.net.Parts
 * JD-Core Version:    0.6.2
 */