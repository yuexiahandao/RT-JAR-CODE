/*     */ package javax.xml.bind.util;
/*     */ 
/*     */ import javax.xml.bind.JAXBContext;
/*     */ import javax.xml.bind.JAXBException;
/*     */ import javax.xml.bind.Unmarshaller;
/*     */ import javax.xml.bind.UnmarshallerHandler;
/*     */ import javax.xml.transform.sax.SAXResult;
/*     */ 
/*     */ public class JAXBResult extends SAXResult
/*     */ {
/*     */   private final UnmarshallerHandler unmarshallerHandler;
/*     */ 
/*     */   public JAXBResult(JAXBContext context)
/*     */     throws JAXBException
/*     */   {
/*  87 */     this(context == null ? assertionFailed() : context.createUnmarshaller());
/*     */   }
/*     */ 
/*     */   public JAXBResult(Unmarshaller _unmarshaller)
/*     */     throws JAXBException
/*     */   {
/* 110 */     if (_unmarshaller == null) {
/* 111 */       throw new JAXBException(Messages.format("JAXBResult.NullUnmarshaller"));
/*     */     }
/*     */ 
/* 114 */     this.unmarshallerHandler = _unmarshaller.getUnmarshallerHandler();
/*     */ 
/* 116 */     super.setHandler(this.unmarshallerHandler);
/*     */   }
/*     */ 
/*     */   public Object getResult()
/*     */     throws JAXBException
/*     */   {
/* 140 */     return this.unmarshallerHandler.getResult();
/*     */   }
/*     */ 
/*     */   private static Unmarshaller assertionFailed()
/*     */     throws JAXBException
/*     */   {
/* 148 */     throw new JAXBException(Messages.format("JAXBResult.NullContext"));
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.bind.util.JAXBResult
 * JD-Core Version:    0.6.2
 */