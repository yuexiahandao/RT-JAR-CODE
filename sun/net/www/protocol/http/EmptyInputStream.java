/*      */ package sun.net.www.protocol.http;
/*      */ 
/*      */ import java.io.InputStream;
/*      */ 
/*      */ class EmptyInputStream extends InputStream
/*      */ {
/*      */   public int available()
/*      */   {
/* 3392 */     return 0;
/*      */   }
/*      */ 
/*      */   public int read() {
/* 3396 */     return -1;
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.www.protocol.http.EmptyInputStream
 * JD-Core Version:    0.6.2
 */