/*     */ package com.sun.org.apache.xml.internal.dtm;
/*     */ 
/*     */ import com.sun.org.apache.xalan.internal.utils.ObjectFactory;
/*     */ import com.sun.org.apache.xalan.internal.utils.SecuritySupport;
/*     */ import com.sun.org.apache.xml.internal.dtm.ref.DTMManagerDefault;
/*     */ import com.sun.org.apache.xml.internal.res.XMLMessages;
/*     */ import com.sun.org.apache.xml.internal.utils.PrefixResolver;
/*     */ import com.sun.org.apache.xml.internal.utils.XMLStringFactory;
/*     */ import javax.xml.transform.Source;
/*     */ import org.w3c.dom.Node;
/*     */ 
/*     */ public abstract class DTMManager
/*     */ {
/*     */   private static final String defaultPropName = "com.sun.org.apache.xml.internal.dtm.DTMManager";
/*  60 */   private static String defaultClassName = "com.sun.org.apache.xml.internal.dtm.ref.DTMManagerDefault";
/*     */ 
/*  67 */   protected XMLStringFactory m_xsf = null;
/*     */   private boolean _useServicesMechanism;
/* 304 */   public boolean m_incremental = false;
/*     */ 
/* 312 */   public boolean m_source_location = false;
/*     */   private static boolean debug;
/*     */   public static final int IDENT_DTM_NODE_BITS = 16;
/*     */   public static final int IDENT_NODE_DEFAULT = 65535;
/*     */   public static final int IDENT_DTM_DEFAULT = -65536;
/*     */   public static final int IDENT_MAX_DTMS = 65536;
/*     */ 
/*     */   public XMLStringFactory getXMLStringFactory()
/*     */   {
/*  83 */     return this.m_xsf;
/*     */   }
/*     */ 
/*     */   public void setXMLStringFactory(XMLStringFactory xsf)
/*     */   {
/*  94 */     this.m_xsf = xsf;
/*     */   }
/*     */ 
/*     */   public static DTMManager newInstance(XMLStringFactory xsf)
/*     */     throws DTMConfigurationException
/*     */   {
/* 139 */     return newInstance(xsf, true);
/*     */   }
/*     */ 
/*     */   public static DTMManager newInstance(XMLStringFactory xsf, boolean useServicesMechanism)
/*     */     throws DTMConfigurationException
/*     */   {
/* 145 */     DTMManager factoryImpl = null;
/*     */     try
/*     */     {
/* 148 */       if (useServicesMechanism) {
/* 149 */         factoryImpl = (DTMManager)ObjectFactory.createObject("com.sun.org.apache.xml.internal.dtm.DTMManager", defaultClassName);
/*     */       }
/*     */       else {
/* 152 */         factoryImpl = new DTMManagerDefault();
/*     */       }
/*     */     }
/*     */     catch (ConfigurationError e)
/*     */     {
/* 157 */       throw new DTMConfigurationException(XMLMessages.createXMLMessage("ER_NO_DEFAULT_IMPL", null), e.getException());
/*     */     }
/*     */ 
/* 162 */     if (factoryImpl == null)
/*     */     {
/* 164 */       throw new DTMConfigurationException(XMLMessages.createXMLMessage("ER_NO_DEFAULT_IMPL", null));
/*     */     }
/*     */ 
/* 169 */     factoryImpl.setXMLStringFactory(xsf);
/*     */ 
/* 171 */     return factoryImpl;
/*     */   }
/*     */ 
/*     */   public abstract DTM getDTM(Source paramSource, boolean paramBoolean1, DTMWSFilter paramDTMWSFilter, boolean paramBoolean2, boolean paramBoolean3);
/*     */ 
/*     */   public abstract DTM getDTM(int paramInt);
/*     */ 
/*     */   public abstract int getDTMHandleFromNode(Node paramNode);
/*     */ 
/*     */   public abstract DTM createDocumentFragment();
/*     */ 
/*     */   public abstract boolean release(DTM paramDTM, boolean paramBoolean);
/*     */ 
/*     */   public abstract DTMIterator createDTMIterator(Object paramObject, int paramInt);
/*     */ 
/*     */   public abstract DTMIterator createDTMIterator(String paramString, PrefixResolver paramPrefixResolver);
/*     */ 
/*     */   public abstract DTMIterator createDTMIterator(int paramInt, DTMFilter paramDTMFilter, boolean paramBoolean);
/*     */ 
/*     */   public abstract DTMIterator createDTMIterator(int paramInt);
/*     */ 
/*     */   public boolean getIncremental()
/*     */   {
/* 321 */     return this.m_incremental;
/*     */   }
/*     */ 
/*     */   public void setIncremental(boolean incremental)
/*     */   {
/* 334 */     this.m_incremental = incremental;
/*     */   }
/*     */ 
/*     */   public boolean getSource_location()
/*     */   {
/* 346 */     return this.m_source_location;
/*     */   }
/*     */ 
/*     */   public void setSource_location(boolean sourceLocation)
/*     */   {
/* 359 */     this.m_source_location = sourceLocation;
/*     */   }
/*     */ 
/*     */   public boolean useServicesMechnism()
/*     */   {
/* 366 */     return this._useServicesMechanism;
/*     */   }
/*     */ 
/*     */   public void setServicesMechnism(boolean flag)
/*     */   {
/* 373 */     this._useServicesMechanism = flag;
/*     */   }
/*     */ 
/*     */   public abstract int getDTMIdentity(DTM paramDTM);
/*     */ 
/*     */   public int getDTMIdentityMask()
/*     */   {
/* 445 */     return -65536;
/*     */   }
/*     */ 
/*     */   public int getNodeIdentityMask()
/*     */   {
/* 455 */     return 65535;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*     */     try
/*     */     {
/* 387 */       debug = SecuritySupport.getSystemProperty("dtm.debug") != null;
/*     */     }
/*     */     catch (SecurityException ex)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   static class ConfigurationError extends Error
/*     */   {
/*     */     static final long serialVersionUID = 5122054096615067992L;
/*     */     private Exception exception;
/*     */ 
/*     */     ConfigurationError(String msg, Exception x)
/*     */     {
/* 485 */       super();
/* 486 */       this.exception = x;
/*     */     }
/*     */ 
/*     */     Exception getException()
/*     */     {
/* 495 */       return this.exception;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.dtm.DTMManager
 * JD-Core Version:    0.6.2
 */