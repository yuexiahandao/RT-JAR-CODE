/*     */ package com.sun.org.apache.xalan.internal.xsltc.trax;
/*     */ 
/*     */ import com.sun.org.apache.xalan.internal.utils.ObjectFactory;
/*     */ import com.sun.org.apache.xalan.internal.utils.SecuritySupport;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.DOM;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.Translet;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.runtime.Hashtable;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import javax.xml.transform.Templates;
/*     */ import javax.xml.transform.Transformer;
/*     */ import javax.xml.transform.TransformerConfigurationException;
/*     */ import javax.xml.transform.URIResolver;
/*     */ 
/*     */ public final class TemplatesImpl
/*     */   implements Templates, Serializable
/*     */ {
/*     */   static final long serialVersionUID = 673094361519270707L;
/*     */   public static final String DESERIALIZE_TRANSLET = "jdk.xml.enableTemplatesImplDeserialization";
/*  63 */   private static String ABSTRACT_TRANSLET = "com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet";
/*     */ 
/*  69 */   private String _name = null;
/*     */ 
/*  75 */   private byte[][] _bytecodes = (byte[][])null;
/*     */ 
/*  81 */   private Class[] _class = null;
/*     */ 
/*  87 */   private int _transletIndex = -1;
/*     */ 
/*  92 */   private Hashtable _auxClasses = null;
/*     */   private Properties _outputProperties;
/*     */   private int _indentNumber;
/* 108 */   private transient URIResolver _uriResolver = null;
/*     */ 
/* 117 */   private transient ThreadLocal _sdom = new ThreadLocal();
/*     */ 
/* 123 */   private transient TransformerFactoryImpl _tfactory = null;
/*     */   private boolean _useServicesMechanism;
/* 130 */   private String _accessExternalStylesheet = "all";
/*     */ 
/*     */   protected TemplatesImpl(byte[][] bytecodes, String transletName, Properties outputProperties, int indentNumber, TransformerFactoryImpl tfactory)
/*     */   {
/* 177 */     this._bytecodes = bytecodes;
/* 178 */     init(transletName, outputProperties, indentNumber, tfactory);
/*     */   }
/*     */ 
/*     */   protected TemplatesImpl(Class[] transletClasses, String transletName, Properties outputProperties, int indentNumber, TransformerFactoryImpl tfactory)
/*     */   {
/* 188 */     this._class = transletClasses;
/* 189 */     this._transletIndex = 0;
/* 190 */     init(transletName, outputProperties, indentNumber, tfactory);
/*     */   }
/*     */ 
/*     */   private void init(String transletName, Properties outputProperties, int indentNumber, TransformerFactoryImpl tfactory)
/*     */   {
/* 196 */     this._name = transletName;
/* 197 */     this._outputProperties = outputProperties;
/* 198 */     this._indentNumber = indentNumber;
/* 199 */     this._tfactory = tfactory;
/* 200 */     this._useServicesMechanism = tfactory.useServicesMechnism();
/* 201 */     this._accessExternalStylesheet = ((String)tfactory.getAttribute("http://javax.xml.XMLConstants/property/accessExternalStylesheet"));
/*     */   }
/*     */ 
/*     */   public TemplatesImpl()
/*     */   {
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream is)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 220 */     SecurityManager security = System.getSecurityManager();
/* 221 */     if (security != null) {
/* 222 */       String temp = SecuritySupport.getSystemProperty("jdk.xml.enableTemplatesImplDeserialization");
/* 223 */       if ((temp == null) || ((temp.length() != 0) && (!temp.equalsIgnoreCase("true")))) {
/* 224 */         ErrorMsg err = new ErrorMsg("DESERIALIZE_TEMPLATES_ERR");
/* 225 */         throw new UnsupportedOperationException(err.toString());
/*     */       }
/*     */     }
/*     */ 
/* 229 */     is.defaultReadObject();
/* 230 */     if (is.readBoolean()) {
/* 231 */       this._uriResolver = ((URIResolver)is.readObject());
/*     */     }
/*     */ 
/* 234 */     this._tfactory = new TransformerFactoryImpl();
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream os)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 245 */     os.defaultWriteObject();
/* 246 */     if ((this._uriResolver instanceof Serializable)) {
/* 247 */       os.writeBoolean(true);
/* 248 */       os.writeObject((Serializable)this._uriResolver);
/*     */     }
/*     */     else {
/* 251 */       os.writeBoolean(false);
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean useServicesMechnism()
/*     */   {
/* 259 */     return this._useServicesMechanism;
/*     */   }
/*     */ 
/*     */   public synchronized void setURIResolver(URIResolver resolver)
/*     */   {
/* 266 */     this._uriResolver = resolver;
/*     */   }
/*     */ 
/*     */   private synchronized void setTransletBytecodes(byte[][] bytecodes)
/*     */   {
/* 279 */     this._bytecodes = bytecodes;
/*     */   }
/*     */ 
/*     */   private synchronized byte[][] getTransletBytecodes()
/*     */   {
/* 291 */     return this._bytecodes;
/*     */   }
/*     */ 
/*     */   private synchronized Class[] getTransletClasses()
/*     */   {
/*     */     try
/*     */     {
/* 304 */       if (this._class == null) defineTransletClasses();
/*     */     }
/*     */     catch (TransformerConfigurationException e)
/*     */     {
/*     */     }
/* 309 */     return this._class;
/*     */   }
/*     */ 
/*     */   public synchronized int getTransletIndex()
/*     */   {
/*     */     try
/*     */     {
/* 317 */       if (this._class == null) defineTransletClasses();
/*     */     }
/*     */     catch (TransformerConfigurationException e)
/*     */     {
/*     */     }
/* 322 */     return this._transletIndex;
/*     */   }
/*     */ 
/*     */   protected synchronized void setTransletName(String name)
/*     */   {
/* 329 */     this._name = name;
/*     */   }
/*     */ 
/*     */   protected synchronized String getTransletName()
/*     */   {
/* 336 */     return this._name;
/*     */   }
/*     */ 
/*     */   private void defineTransletClasses()
/*     */     throws TransformerConfigurationException
/*     */   {
/* 346 */     if (this._bytecodes == null) {
/* 347 */       ErrorMsg err = new ErrorMsg("NO_TRANSLET_CLASS_ERR");
/* 348 */       throw new TransformerConfigurationException(err.toString());
/*     */     }
/*     */ 
/* 351 */     TransletClassLoader loader = (TransletClassLoader)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Object run() {
/* 354 */         return new TemplatesImpl.TransletClassLoader(ObjectFactory.findClassLoader(), TemplatesImpl.this._tfactory.getExternalExtensionsMap());
/*     */       }
/*     */     });
/*     */     try
/*     */     {
/* 359 */       int classCount = this._bytecodes.length;
/* 360 */       this._class = new Class[classCount];
/*     */ 
/* 362 */       if (classCount > 1) {
/* 363 */         this._auxClasses = new Hashtable();
/*     */       }
/*     */ 
/* 366 */       for (int i = 0; i < classCount; i++) {
/* 367 */         this._class[i] = loader.defineClass(this._bytecodes[i]);
/* 368 */         Class superClass = this._class[i].getSuperclass();
/*     */ 
/* 371 */         if (superClass.getName().equals(ABSTRACT_TRANSLET)) {
/* 372 */           this._transletIndex = i;
/*     */         }
/*     */         else {
/* 375 */           this._auxClasses.put(this._class[i].getName(), this._class[i]);
/*     */         }
/*     */       }
/*     */ 
/* 379 */       if (this._transletIndex < 0) {
/* 380 */         ErrorMsg err = new ErrorMsg("NO_MAIN_TRANSLET_ERR", this._name);
/* 381 */         throw new TransformerConfigurationException(err.toString());
/*     */       }
/*     */     }
/*     */     catch (ClassFormatError e) {
/* 385 */       ErrorMsg err = new ErrorMsg("TRANSLET_CLASS_ERR", this._name);
/* 386 */       throw new TransformerConfigurationException(err.toString());
/*     */     }
/*     */     catch (LinkageError e) {
/* 389 */       ErrorMsg err = new ErrorMsg("TRANSLET_OBJECT_ERR", this._name);
/* 390 */       throw new TransformerConfigurationException(err.toString());
/*     */     }
/*     */   }
/*     */ 
/*     */   private Translet getTransletInstance()
/*     */     throws TransformerConfigurationException
/*     */   {
/*     */     try
/*     */     {
/* 402 */       if (this._name == null) return null;
/*     */ 
/* 404 */       if (this._class == null) defineTransletClasses();
/*     */ 
/* 408 */       AbstractTranslet translet = (AbstractTranslet)this._class[this._transletIndex].newInstance();
/* 409 */       translet.postInitialization();
/* 410 */       translet.setTemplates(this);
/* 411 */       translet.setServicesMechnism(this._useServicesMechanism);
/* 412 */       translet.setAllowedProtocols(this._accessExternalStylesheet);
/* 413 */       if (this._auxClasses != null) {
/* 414 */         translet.setAuxiliaryClasses(this._auxClasses);
/*     */       }
/*     */ 
/* 417 */       return translet;
/*     */     }
/*     */     catch (InstantiationException e) {
/* 420 */       ErrorMsg err = new ErrorMsg("TRANSLET_OBJECT_ERR", this._name);
/* 421 */       throw new TransformerConfigurationException(err.toString());
/*     */     }
/*     */     catch (IllegalAccessException e) {
/* 424 */       ErrorMsg err = new ErrorMsg("TRANSLET_OBJECT_ERR", this._name);
/* 425 */       throw new TransformerConfigurationException(err.toString());
/*     */     }
/*     */   }
/*     */ 
/*     */   public synchronized Transformer newTransformer()
/*     */     throws TransformerConfigurationException
/*     */   {
/* 439 */     TransformerImpl transformer = new TransformerImpl(getTransletInstance(), this._outputProperties, this._indentNumber, this._tfactory);
/*     */ 
/* 442 */     if (this._uriResolver != null) {
/* 443 */       transformer.setURIResolver(this._uriResolver);
/*     */     }
/*     */ 
/* 446 */     if (this._tfactory.getFeature("http://javax.xml.XMLConstants/feature/secure-processing")) {
/* 447 */       transformer.setSecureProcessing(true);
/*     */     }
/* 449 */     return transformer;
/*     */   }
/*     */ 
/*     */   public synchronized Properties getOutputProperties()
/*     */   {
/*     */     try
/*     */     {
/* 460 */       return newTransformer().getOutputProperties();
/*     */     } catch (TransformerConfigurationException e) {
/*     */     }
/* 463 */     return null;
/*     */   }
/*     */ 
/*     */   public DOM getStylesheetDOM()
/*     */   {
/* 471 */     return (DOM)this._sdom.get();
/*     */   }
/*     */ 
/*     */   public void setStylesheetDOM(DOM sdom)
/*     */   {
/* 478 */     this._sdom.set(sdom);
/*     */   }
/*     */ 
/*     */   static final class TransletClassLoader extends ClassLoader
/*     */   {
/*     */     private final Map<String, Class> _loadedExternalExtensionFunctions;
/*     */ 
/*     */     TransletClassLoader(ClassLoader parent)
/*     */     {
/* 137 */       super();
/* 138 */       this._loadedExternalExtensionFunctions = null;
/*     */     }
/*     */ 
/*     */     TransletClassLoader(ClassLoader parent, Map<String, Class> mapEF) {
/* 142 */       super();
/* 143 */       this._loadedExternalExtensionFunctions = mapEF;
/*     */     }
/*     */ 
/*     */     public Class<?> loadClass(String name) throws ClassNotFoundException {
/* 147 */       Class ret = null;
/*     */ 
/* 150 */       if (this._loadedExternalExtensionFunctions != null) {
/* 151 */         ret = (Class)this._loadedExternalExtensionFunctions.get(name);
/*     */       }
/* 153 */       if (ret == null) {
/* 154 */         ret = super.loadClass(name);
/*     */       }
/* 156 */       return ret;
/*     */     }
/*     */ 
/*     */     Class defineClass(byte[] b)
/*     */     {
/* 163 */       return defineClass(null, b, 0, b.length);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl
 * JD-Core Version:    0.6.2
 */