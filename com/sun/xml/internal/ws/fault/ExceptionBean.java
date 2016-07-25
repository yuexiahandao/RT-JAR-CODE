/*     */ package com.sun.xml.internal.ws.fault;
/*     */ 
/*     */ import com.sun.xml.internal.bind.marshaller.NamespacePrefixMapper;
/*     */ import com.sun.xml.internal.ws.developer.ServerSideException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.xml.bind.JAXBContext;
/*     */ import javax.xml.bind.JAXBException;
/*     */ import javax.xml.bind.Marshaller;
/*     */ import javax.xml.bind.Unmarshaller;
/*     */ import javax.xml.bind.annotation.XmlAttribute;
/*     */ import javax.xml.bind.annotation.XmlElement;
/*     */ import javax.xml.bind.annotation.XmlElementWrapper;
/*     */ import javax.xml.bind.annotation.XmlRootElement;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ 
/*     */ @XmlRootElement(namespace="http://jax-ws.dev.java.net/", name="exception")
/*     */ final class ExceptionBean
/*     */ {
/*     */ 
/*     */   @XmlAttribute(name="class")
/*     */   public String className;
/*     */ 
/*     */   @XmlElement
/*     */   public String message;
/*     */ 
/*     */   @XmlElementWrapper(namespace="http://jax-ws.dev.java.net/", name="stackTrace")
/*     */   @XmlElement(namespace="http://jax-ws.dev.java.net/", name="frame")
/*  77 */   public List<StackFrame> stackTrace = new ArrayList();
/*     */ 
/*     */   @XmlElement(namespace="http://jax-ws.dev.java.net/", name="cause")
/*     */   public ExceptionBean cause;
/*     */ 
/*     */   @XmlAttribute
/*  84 */   public String note = "To disable this feature, set " + SOAPFaultBuilder.CAPTURE_STACK_TRACE_PROPERTY + " system property to false";
/*     */   private static final JAXBContext JAXB_CONTEXT;
/*     */   static final String NS = "http://jax-ws.dev.java.net/";
/*     */   static final String LOCAL_NAME = "exception";
/* 187 */   private static final NamespacePrefixMapper nsp = new NamespacePrefixMapper() {
/*     */     public String getPreferredPrefix(String namespaceUri, String suggestion, boolean requirePrefix) {
/* 189 */       if (namespaceUri.equals("http://jax-ws.dev.java.net/")) return "";
/* 190 */       return suggestion;
/*     */     }
/* 187 */   };
/*     */ 
/*     */   public static void marshal(Throwable t, Node parent)
/*     */     throws JAXBException
/*     */   {
/*  59 */     Marshaller m = JAXB_CONTEXT.createMarshaller();
/*  60 */     m.setProperty("com.sun.xml.internal.bind.namespacePrefixMapper", nsp);
/*  61 */     m.marshal(new ExceptionBean(t), parent);
/*     */   }
/*     */ 
/*     */   public static ServerSideException unmarshal(Node xml)
/*     */     throws JAXBException
/*     */   {
/*  69 */     ExceptionBean e = (ExceptionBean)JAXB_CONTEXT.createUnmarshaller().unmarshal(xml);
/*  70 */     return e.toException();
/*     */   }
/*     */ 
/*     */   ExceptionBean()
/*     */   {
/*     */   }
/*     */ 
/*     */   private ExceptionBean(Throwable t)
/*     */   {
/*  94 */     this.className = t.getClass().getName();
/*  95 */     this.message = t.getMessage();
/*     */ 
/*  97 */     for (StackTraceElement f : t.getStackTrace()) {
/*  98 */       this.stackTrace.add(new StackFrame(f));
/*     */     }
/*     */ 
/* 101 */     Throwable cause = t.getCause();
/* 102 */     if ((t != cause) && (cause != null))
/* 103 */       this.cause = new ExceptionBean(cause);
/*     */   }
/*     */ 
/*     */   private ServerSideException toException() {
/* 107 */     ServerSideException e = new ServerSideException(this.className, this.message);
/* 108 */     if (this.stackTrace != null) {
/* 109 */       StackTraceElement[] ste = new StackTraceElement[this.stackTrace.size()];
/* 110 */       for (int i = 0; i < this.stackTrace.size(); i++)
/* 111 */         ste[i] = ((StackFrame)this.stackTrace.get(i)).toStackTraceElement();
/* 112 */       e.setStackTrace(ste);
/*     */     }
/* 114 */     if (this.cause != null)
/* 115 */       e.initCause(this.cause.toException());
/* 116 */     return e;
/*     */   }
/*     */ 
/*     */   public static boolean isStackTraceXml(Element n)
/*     */   {
/* 166 */     return (n.getLocalName().equals("exception")) && (n.getNamespaceURI().equals("http://jax-ws.dev.java.net/"));
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*     */     try
/*     */     {
/* 180 */       JAXB_CONTEXT = JAXBContext.newInstance(new Class[] { ExceptionBean.class });
/*     */     }
/*     */     catch (JAXBException e) {
/* 183 */       throw new Error(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   static final class StackFrame
/*     */   {
/*     */ 
/*     */     @XmlAttribute(name="class")
/*     */     public String declaringClass;
/*     */ 
/*     */     @XmlAttribute(name="method")
/*     */     public String methodName;
/*     */ 
/*     */     @XmlAttribute(name="file")
/*     */     public String fileName;
/*     */ 
/*     */     @XmlAttribute(name="line")
/*     */     public String lineNumber;
/*     */ 
/*     */     StackFrame()
/*     */     {
/*     */     }
/*     */ 
/*     */     public StackFrame(StackTraceElement ste)
/*     */     {
/* 136 */       this.declaringClass = ste.getClassName();
/* 137 */       this.methodName = ste.getMethodName();
/* 138 */       this.fileName = ste.getFileName();
/* 139 */       this.lineNumber = box(ste.getLineNumber());
/*     */     }
/*     */ 
/*     */     private String box(int i) {
/* 143 */       if (i >= 0) return String.valueOf(i);
/* 144 */       if (i == -2) return "native";
/* 145 */       return "unknown";
/*     */     }
/*     */ 
/*     */     private int unbox(String v) {
/*     */       try {
/* 150 */         return Integer.parseInt(v);
/*     */       } catch (NumberFormatException e) {
/* 152 */         if (v.equals("native")) return -2; 
/*     */       }
/* 153 */       return -1;
/*     */     }
/*     */ 
/*     */     private StackTraceElement toStackTraceElement()
/*     */     {
/* 158 */       return new StackTraceElement(this.declaringClass, this.methodName, this.fileName, unbox(this.lineNumber));
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.fault.ExceptionBean
 * JD-Core Version:    0.6.2
 */