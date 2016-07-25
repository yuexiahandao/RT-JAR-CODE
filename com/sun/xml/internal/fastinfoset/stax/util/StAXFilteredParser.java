/*    */ package com.sun.xml.internal.fastinfoset.stax.util;
/*    */ 
/*    */ import com.sun.xml.internal.fastinfoset.CommonResourceBundle;
/*    */ import javax.xml.stream.StreamFilter;
/*    */ import javax.xml.stream.XMLStreamException;
/*    */ import javax.xml.stream.XMLStreamReader;
/*    */ 
/*    */ public class StAXFilteredParser extends StAXParserWrapper
/*    */ {
/*    */   private StreamFilter _filter;
/*    */ 
/*    */   public StAXFilteredParser()
/*    */   {
/*    */   }
/*    */ 
/*    */   public StAXFilteredParser(XMLStreamReader reader, StreamFilter filter)
/*    */   {
/* 43 */     super(reader);
/* 44 */     this._filter = filter;
/*    */   }
/*    */ 
/*    */   public void setFilter(StreamFilter filter) {
/* 48 */     this._filter = filter;
/*    */   }
/*    */ 
/*    */   public int next() throws XMLStreamException
/*    */   {
/* 53 */     if (hasNext())
/* 54 */       return super.next();
/* 55 */     throw new IllegalStateException(CommonResourceBundle.getInstance().getString("message.noMoreItems"));
/*    */   }
/*    */ 
/*    */   public boolean hasNext() throws XMLStreamException
/*    */   {
/* 60 */     while (super.hasNext()) {
/* 61 */       if (this._filter.accept(getReader())) return true;
/* 62 */       super.next();
/*    */     }
/* 64 */     return false;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.fastinfoset.stax.util.StAXFilteredParser
 * JD-Core Version:    0.6.2
 */