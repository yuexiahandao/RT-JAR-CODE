/*      */ package java.net;
/*      */ 
/*      */ import java.io.IOException;
/*      */ 
/*      */ class UnknownContentHandler extends ContentHandler
/*      */ {
/* 1792 */   static final ContentHandler INSTANCE = new UnknownContentHandler();
/*      */ 
/*      */   public Object getContent(URLConnection paramURLConnection) throws IOException {
/* 1795 */     return paramURLConnection.getInputStream();
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.net.UnknownContentHandler
 * JD-Core Version:    0.6.2
 */