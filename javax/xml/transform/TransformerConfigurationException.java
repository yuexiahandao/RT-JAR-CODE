/*     */ package javax.xml.transform;
/*     */ 
/*     */ public class TransformerConfigurationException extends TransformerException
/*     */ {
/*     */   public TransformerConfigurationException()
/*     */   {
/*  38 */     super("Configuration Error");
/*     */   }
/*     */ 
/*     */   public TransformerConfigurationException(String msg)
/*     */   {
/*  48 */     super(msg);
/*     */   }
/*     */ 
/*     */   public TransformerConfigurationException(Throwable e)
/*     */   {
/*  59 */     super(e);
/*     */   }
/*     */ 
/*     */   public TransformerConfigurationException(String msg, Throwable e)
/*     */   {
/*  71 */     super(msg, e);
/*     */   }
/*     */ 
/*     */   public TransformerConfigurationException(String message, SourceLocator locator)
/*     */   {
/*  86 */     super(message, locator);
/*     */   }
/*     */ 
/*     */   public TransformerConfigurationException(String message, SourceLocator locator, Throwable e)
/*     */   {
/* 100 */     super(message, locator, e);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.transform.TransformerConfigurationException
 * JD-Core Version:    0.6.2
 */