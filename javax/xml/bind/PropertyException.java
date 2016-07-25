/*     */ package javax.xml.bind;
/*     */ 
/*     */ public class PropertyException extends JAXBException
/*     */ {
/*     */   public PropertyException(String message)
/*     */   {
/*  49 */     super(message);
/*     */   }
/*     */ 
/*     */   public PropertyException(String message, String errorCode)
/*     */   {
/*  60 */     super(message, errorCode);
/*     */   }
/*     */ 
/*     */   public PropertyException(Throwable exception)
/*     */   {
/*  70 */     super(exception);
/*     */   }
/*     */ 
/*     */   public PropertyException(String message, Throwable exception)
/*     */   {
/*  81 */     super(message, exception);
/*     */   }
/*     */ 
/*     */   public PropertyException(String message, String errorCode, Throwable exception)
/*     */   {
/*  96 */     super(message, errorCode, exception);
/*     */   }
/*     */ 
/*     */   public PropertyException(String name, Object value)
/*     */   {
/* 107 */     super(Messages.format("PropertyException.NameValue", name, value.toString()));
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.bind.PropertyException
 * JD-Core Version:    0.6.2
 */