/*    */ package com.sun.xml.internal.ws.encoding.fastinfoset;
/*    */ 
/*    */ import com.sun.xml.internal.fastinfoset.stax.StAXDocumentParser;
/*    */ import com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory;
/*    */ import java.io.InputStream;
/*    */ import java.io.Reader;
/*    */ import javax.xml.stream.XMLStreamReader;
/*    */ 
/*    */ public final class FastInfosetStreamReaderFactory extends XMLStreamReaderFactory
/*    */ {
/* 38 */   private static final FastInfosetStreamReaderFactory factory = new FastInfosetStreamReaderFactory();
/*    */ 
/* 40 */   private ThreadLocal<StAXDocumentParser> pool = new ThreadLocal();
/*    */ 
/*    */   public static FastInfosetStreamReaderFactory getInstance() {
/* 43 */     return factory;
/*    */   }
/*    */ 
/*    */   public XMLStreamReader doCreate(String systemId, InputStream in, boolean rejectDTDs) {
/* 47 */     StAXDocumentParser parser = fetch();
/* 48 */     if (parser == null) {
/* 49 */       return FastInfosetCodec.createNewStreamReaderRecyclable(in, false);
/*    */     }
/*    */ 
/* 52 */     parser.setInputStream(in);
/* 53 */     return parser;
/*    */   }
/*    */ 
/*    */   public XMLStreamReader doCreate(String systemId, Reader reader, boolean rejectDTDs) {
/* 57 */     throw new UnsupportedOperationException();
/*    */   }
/*    */ 
/*    */   private StAXDocumentParser fetch() {
/* 61 */     StAXDocumentParser parser = (StAXDocumentParser)this.pool.get();
/* 62 */     this.pool.set(null);
/* 63 */     return parser;
/*    */   }
/*    */ 
/*    */   public void doRecycle(XMLStreamReader r) {
/* 67 */     if ((r instanceof StAXDocumentParser))
/* 68 */       this.pool.set((StAXDocumentParser)r);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.encoding.fastinfoset.FastInfosetStreamReaderFactory
 * JD-Core Version:    0.6.2
 */