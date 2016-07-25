/*     */ package javax.xml.ws;
/*     */ 
/*     */ import java.net.URL;
/*     */ import java.util.Iterator;
/*     */ import java.util.concurrent.Executor;
/*     */ import javax.xml.bind.JAXBContext;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.ws.handler.HandlerResolver;
/*     */ import javax.xml.ws.spi.Provider;
/*     */ import javax.xml.ws.spi.ServiceDelegate;
/*     */ 
/*     */ public class Service
/*     */ {
/*     */   private ServiceDelegate delegate;
/*     */ 
/*     */   protected Service(URL wsdlDocumentLocation, QName serviceName)
/*     */   {
/*  77 */     this.delegate = Provider.provider().createServiceDelegate(wsdlDocumentLocation, serviceName, getClass());
/*     */   }
/*     */ 
/*     */   protected Service(URL wsdlDocumentLocation, QName serviceName, WebServiceFeature[] features)
/*     */   {
/*  83 */     this.delegate = Provider.provider().createServiceDelegate(wsdlDocumentLocation, serviceName, getClass(), features);
/*     */   }
/*     */ 
/*     */   public <T> T getPort(QName portName, Class<T> serviceEndpointInterface)
/*     */   {
/* 119 */     return this.delegate.getPort(portName, serviceEndpointInterface);
/*     */   }
/*     */ 
/*     */   public <T> T getPort(QName portName, Class<T> serviceEndpointInterface, WebServiceFeature[] features)
/*     */   {
/* 160 */     return this.delegate.getPort(portName, serviceEndpointInterface, features);
/*     */   }
/*     */ 
/*     */   public <T> T getPort(Class<T> serviceEndpointInterface)
/*     */   {
/* 188 */     return this.delegate.getPort(serviceEndpointInterface);
/*     */   }
/*     */ 
/*     */   public <T> T getPort(Class<T> serviceEndpointInterface, WebServiceFeature[] features)
/*     */   {
/* 226 */     return this.delegate.getPort(serviceEndpointInterface, features);
/*     */   }
/*     */ 
/*     */   public <T> T getPort(EndpointReference endpointReference, Class<T> serviceEndpointInterface, WebServiceFeature[] features)
/*     */   {
/* 304 */     return this.delegate.getPort(endpointReference, serviceEndpointInterface, features);
/*     */   }
/*     */ 
/*     */   public void addPort(QName portName, String bindingId, String endpointAddress)
/*     */   {
/* 323 */     this.delegate.addPort(portName, bindingId, endpointAddress);
/*     */   }
/*     */ 
/*     */   public <T> Dispatch<T> createDispatch(QName portName, Class<T> type, Mode mode)
/*     */   {
/* 352 */     return this.delegate.createDispatch(portName, type, mode);
/*     */   }
/*     */ 
/*     */   public <T> Dispatch<T> createDispatch(QName portName, Class<T> type, Mode mode, WebServiceFeature[] features)
/*     */   {
/* 388 */     return this.delegate.createDispatch(portName, type, mode, features);
/*     */   }
/*     */ 
/*     */   public <T> Dispatch<T> createDispatch(EndpointReference endpointReference, Class<T> type, Mode mode, WebServiceFeature[] features)
/*     */   {
/* 464 */     return this.delegate.createDispatch(endpointReference, type, mode, features);
/*     */   }
/*     */ 
/*     */   public Dispatch<Object> createDispatch(QName portName, JAXBContext context, Mode mode)
/*     */   {
/* 488 */     return this.delegate.createDispatch(portName, context, mode);
/*     */   }
/*     */ 
/*     */   public Dispatch<Object> createDispatch(QName portName, JAXBContext context, Mode mode, WebServiceFeature[] features)
/*     */   {
/* 521 */     return this.delegate.createDispatch(portName, context, mode, features);
/*     */   }
/*     */ 
/*     */   public Dispatch<Object> createDispatch(EndpointReference endpointReference, JAXBContext context, Mode mode, WebServiceFeature[] features)
/*     */   {
/* 595 */     return this.delegate.createDispatch(endpointReference, context, mode, features);
/*     */   }
/*     */ 
/*     */   public QName getServiceName()
/*     */   {
/* 603 */     return this.delegate.getServiceName();
/*     */   }
/*     */ 
/*     */   public Iterator<QName> getPorts()
/*     */   {
/* 617 */     return this.delegate.getPorts();
/*     */   }
/*     */ 
/*     */   public URL getWSDLDocumentLocation()
/*     */   {
/* 627 */     return this.delegate.getWSDLDocumentLocation();
/*     */   }
/*     */ 
/*     */   public HandlerResolver getHandlerResolver()
/*     */   {
/* 638 */     return this.delegate.getHandlerResolver();
/*     */   }
/*     */ 
/*     */   public void setHandlerResolver(HandlerResolver handlerResolver)
/*     */   {
/* 655 */     this.delegate.setHandlerResolver(handlerResolver);
/*     */   }
/*     */ 
/*     */   public Executor getExecutor()
/*     */   {
/* 670 */     return this.delegate.getExecutor();
/*     */   }
/*     */ 
/*     */   public void setExecutor(Executor executor)
/*     */   {
/* 689 */     this.delegate.setExecutor(executor);
/*     */   }
/*     */ 
/*     */   public static Service create(URL wsdlDocumentLocation, QName serviceName)
/*     */   {
/* 707 */     return new Service(wsdlDocumentLocation, serviceName);
/*     */   }
/*     */ 
/*     */   public static Service create(URL wsdlDocumentLocation, QName serviceName, WebServiceFeature[] features)
/*     */   {
/* 730 */     return new Service(wsdlDocumentLocation, serviceName, features);
/*     */   }
/*     */ 
/*     */   public static Service create(QName serviceName)
/*     */   {
/* 741 */     return new Service(null, serviceName);
/*     */   }
/*     */ 
/*     */   public static Service create(QName serviceName, WebServiceFeature[] features)
/*     */   {
/* 758 */     return new Service(null, serviceName, features);
/*     */   }
/*     */ 
/*     */   public static enum Mode
/*     */   {
/*  74 */     MESSAGE, PAYLOAD;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.ws.Service
 * JD-Core Version:    0.6.2
 */