/*     */ package javax.xml.bind;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Collections;
/*     */ import java.util.Map;
/*     */ import org.w3c.dom.Node;
/*     */ 
/*     */ public abstract class JAXBContext
/*     */ {
/*     */   public static final String JAXB_CONTEXT_FACTORY = "javax.xml.bind.context.factory";
/*     */ 
/*     */   public static JAXBContext newInstance(String contextPath)
/*     */     throws JAXBException
/*     */   {
/* 298 */     return newInstance(contextPath, Thread.currentThread().getContextClassLoader());
/*     */   }
/*     */ 
/*     */   public static JAXBContext newInstance(String contextPath, ClassLoader classLoader)
/*     */     throws JAXBException
/*     */   {
/* 394 */     return newInstance(contextPath, classLoader, Collections.emptyMap());
/*     */   }
/*     */ 
/*     */   public static JAXBContext newInstance(String contextPath, ClassLoader classLoader, Map<String, ?> properties)
/*     */     throws JAXBException
/*     */   {
/* 431 */     return ContextFinder.find("javax.xml.bind.context.factory", contextPath, classLoader, properties);
/*     */   }
/*     */ 
/*     */   public static JAXBContext newInstance(Class[] classesToBeBound)
/*     */     throws JAXBException
/*     */   {
/* 584 */     return newInstance(classesToBeBound, Collections.emptyMap());
/*     */   }
/*     */ 
/*     */   public static JAXBContext newInstance(Class[] classesToBeBound, Map<String, ?> properties)
/*     */     throws JAXBException
/*     */   {
/* 630 */     if (classesToBeBound == null) throw new IllegalArgumentException();
/*     */ 
/* 633 */     for (int i = classesToBeBound.length - 1; i >= 0; i--) {
/* 634 */       if (classesToBeBound[i] == null)
/* 635 */         throw new IllegalArgumentException();
/*     */     }
/* 637 */     return ContextFinder.find(classesToBeBound, properties);
/*     */   }
/*     */ 
/*     */   public abstract Unmarshaller createUnmarshaller()
/*     */     throws JAXBException;
/*     */ 
/*     */   public abstract Marshaller createMarshaller()
/*     */     throws JAXBException;
/*     */ 
/*     */   /** @deprecated */
/*     */   public abstract Validator createValidator()
/*     */     throws JAXBException;
/*     */ 
/*     */   public <T> Binder<T> createBinder(Class<T> domType)
/*     */   {
/* 696 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public Binder<Node> createBinder()
/*     */   {
/* 707 */     return createBinder(Node.class);
/*     */   }
/*     */ 
/*     */   public JAXBIntrospector createJAXBIntrospector()
/*     */   {
/* 726 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public void generateSchema(SchemaOutputResolver outputResolver)
/*     */     throws IOException
/*     */   {
/* 748 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.bind.JAXBContext
 * JD-Core Version:    0.6.2
 */