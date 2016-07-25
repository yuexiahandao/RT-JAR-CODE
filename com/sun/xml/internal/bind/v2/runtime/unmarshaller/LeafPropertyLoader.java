/*    */ package com.sun.xml.internal.bind.v2.runtime.unmarshaller;
/*    */ 
/*    */ import com.sun.xml.internal.bind.api.AccessorException;
/*    */ import com.sun.xml.internal.bind.v2.runtime.reflect.TransducedAccessor;
/*    */ import org.xml.sax.SAXException;
/*    */ 
/*    */ public class LeafPropertyLoader extends Loader
/*    */ {
/*    */   private final TransducedAccessor xacc;
/*    */ 
/*    */   public LeafPropertyLoader(TransducedAccessor xacc)
/*    */   {
/* 44 */     super(true);
/* 45 */     this.xacc = xacc;
/*    */   }
/*    */ 
/*    */   public void text(UnmarshallingContext.State state, CharSequence text) throws SAXException {
/*    */     try {
/* 50 */       this.xacc.parse(state.getPrev().getTarget(), text);
/*    */     } catch (AccessorException e) {
/* 52 */       handleGenericException(e, true);
/*    */     } catch (RuntimeException e) {
/* 54 */       handleParseConversionException(state, e);
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.unmarshaller.LeafPropertyLoader
 * JD-Core Version:    0.6.2
 */