/*     */ package javax.xml.stream;
/*     */ 
/*     */ import java.io.InputStream;
/*     */ import java.io.Reader;
/*     */ import javax.xml.stream.util.XMLEventAllocator;
/*     */ import javax.xml.transform.Source;
/*     */ 
/*     */ public abstract class XMLInputFactory
/*     */ {
/*     */   public static final String IS_NAMESPACE_AWARE = "javax.xml.stream.isNamespaceAware";
/*     */   public static final String IS_VALIDATING = "javax.xml.stream.isValidating";
/*     */   public static final String IS_COALESCING = "javax.xml.stream.isCoalescing";
/*     */   public static final String IS_REPLACING_ENTITY_REFERENCES = "javax.xml.stream.isReplacingEntityReferences";
/*     */   public static final String IS_SUPPORTING_EXTERNAL_ENTITIES = "javax.xml.stream.isSupportingExternalEntities";
/*     */   public static final String SUPPORT_DTD = "javax.xml.stream.supportDTD";
/*     */   public static final String REPORTER = "javax.xml.stream.reporter";
/*     */   public static final String RESOLVER = "javax.xml.stream.resolver";
/*     */   public static final String ALLOCATOR = "javax.xml.stream.allocator";
/*     */   static final String JAXPFACTORYID = "javax.xml.stream.XMLInputFactory";
/*     */   static final String DEFAULIMPL = "com.sun.xml.internal.stream.XMLInputFactoryImpl";
/*     */ 
/*     */   public static XMLInputFactory newInstance()
/*     */     throws FactoryConfigurationError
/*     */   {
/* 154 */     return (XMLInputFactory)FactoryFinder.find("javax.xml.stream.XMLInputFactory", "com.sun.xml.internal.stream.XMLInputFactoryImpl", true);
/*     */   }
/*     */ 
/*     */   public static XMLInputFactory newFactory()
/*     */     throws FactoryConfigurationError
/*     */   {
/* 185 */     return (XMLInputFactory)FactoryFinder.find("javax.xml.stream.XMLInputFactory", "com.sun.xml.internal.stream.XMLInputFactoryImpl", true);
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public static XMLInputFactory newInstance(String factoryId, ClassLoader classLoader)
/*     */     throws FactoryConfigurationError
/*     */   {
/*     */     try
/*     */     {
/* 208 */       return (XMLInputFactory)FactoryFinder.find(factoryId, classLoader, null, factoryId.equals("javax.xml.stream.XMLInputFactory"));
/*     */     }
/*     */     catch (FactoryFinder.ConfigurationError e) {
/* 211 */       throw new FactoryConfigurationError(e.getException(), e.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   public static XMLInputFactory newFactory(String factoryId, ClassLoader classLoader)
/*     */     throws FactoryConfigurationError
/*     */   {
/*     */     try
/*     */     {
/* 236 */       return (XMLInputFactory)FactoryFinder.find(factoryId, classLoader, null, factoryId.equals("javax.xml.stream.XMLInputFactory"));
/*     */     }
/*     */     catch (FactoryFinder.ConfigurationError e) {
/* 239 */       throw new FactoryConfigurationError(e.getException(), e.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   public abstract XMLStreamReader createXMLStreamReader(Reader paramReader)
/*     */     throws XMLStreamException;
/*     */ 
/*     */   public abstract XMLStreamReader createXMLStreamReader(Source paramSource)
/*     */     throws XMLStreamException;
/*     */ 
/*     */   public abstract XMLStreamReader createXMLStreamReader(InputStream paramInputStream)
/*     */     throws XMLStreamException;
/*     */ 
/*     */   public abstract XMLStreamReader createXMLStreamReader(InputStream paramInputStream, String paramString)
/*     */     throws XMLStreamException;
/*     */ 
/*     */   public abstract XMLStreamReader createXMLStreamReader(String paramString, InputStream paramInputStream)
/*     */     throws XMLStreamException;
/*     */ 
/*     */   public abstract XMLStreamReader createXMLStreamReader(String paramString, Reader paramReader)
/*     */     throws XMLStreamException;
/*     */ 
/*     */   public abstract XMLEventReader createXMLEventReader(Reader paramReader)
/*     */     throws XMLStreamException;
/*     */ 
/*     */   public abstract XMLEventReader createXMLEventReader(String paramString, Reader paramReader)
/*     */     throws XMLStreamException;
/*     */ 
/*     */   public abstract XMLEventReader createXMLEventReader(XMLStreamReader paramXMLStreamReader)
/*     */     throws XMLStreamException;
/*     */ 
/*     */   public abstract XMLEventReader createXMLEventReader(Source paramSource)
/*     */     throws XMLStreamException;
/*     */ 
/*     */   public abstract XMLEventReader createXMLEventReader(InputStream paramInputStream)
/*     */     throws XMLStreamException;
/*     */ 
/*     */   public abstract XMLEventReader createXMLEventReader(InputStream paramInputStream, String paramString)
/*     */     throws XMLStreamException;
/*     */ 
/*     */   public abstract XMLEventReader createXMLEventReader(String paramString, InputStream paramInputStream)
/*     */     throws XMLStreamException;
/*     */ 
/*     */   public abstract XMLStreamReader createFilteredReader(XMLStreamReader paramXMLStreamReader, StreamFilter paramStreamFilter)
/*     */     throws XMLStreamException;
/*     */ 
/*     */   public abstract XMLEventReader createFilteredReader(XMLEventReader paramXMLEventReader, EventFilter paramEventFilter)
/*     */     throws XMLStreamException;
/*     */ 
/*     */   public abstract XMLResolver getXMLResolver();
/*     */ 
/*     */   public abstract void setXMLResolver(XMLResolver paramXMLResolver);
/*     */ 
/*     */   public abstract XMLReporter getXMLReporter();
/*     */ 
/*     */   public abstract void setXMLReporter(XMLReporter paramXMLReporter);
/*     */ 
/*     */   public abstract void setProperty(String paramString, Object paramObject)
/*     */     throws IllegalArgumentException;
/*     */ 
/*     */   public abstract Object getProperty(String paramString)
/*     */     throws IllegalArgumentException;
/*     */ 
/*     */   public abstract boolean isPropertySupported(String paramString);
/*     */ 
/*     */   public abstract void setEventAllocator(XMLEventAllocator paramXMLEventAllocator);
/*     */ 
/*     */   public abstract XMLEventAllocator getEventAllocator();
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.stream.XMLInputFactory
 * JD-Core Version:    0.6.2
 */