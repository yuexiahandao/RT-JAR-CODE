/*     */ package javax.activation;
/*     */ 
/*     */ import java.awt.datatransfer.DataFlavor;
/*     */ import java.awt.datatransfer.Transferable;
/*     */ import java.awt.datatransfer.UnsupportedFlavorException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.PipedInputStream;
/*     */ import java.io.PipedOutputStream;
/*     */ import java.net.URL;
/*     */ 
/*     */ public class DataHandler
/*     */   implements Transferable
/*     */ {
/*  81 */   private DataSource dataSource = null;
/*  82 */   private DataSource objDataSource = null;
/*     */ 
/*  87 */   private Object object = null;
/*  88 */   private String objectMimeType = null;
/*     */ 
/*  91 */   private CommandMap currentCommandMap = null;
/*     */ 
/*  94 */   private static final DataFlavor[] emptyFlavors = new DataFlavor[0];
/*  95 */   private DataFlavor[] transferFlavors = emptyFlavors;
/*     */ 
/*  98 */   private DataContentHandler dataContentHandler = null;
/*  99 */   private DataContentHandler factoryDCH = null;
/*     */ 
/* 102 */   private static DataContentHandlerFactory factory = null;
/* 103 */   private DataContentHandlerFactory oldFactory = null;
/*     */ 
/* 105 */   private String shortType = null;
/*     */ 
/*     */   public DataHandler(DataSource ds)
/*     */   {
/* 116 */     this.dataSource = ds;
/* 117 */     this.oldFactory = factory;
/*     */   }
/*     */ 
/*     */   public DataHandler(Object obj, String mimeType)
/*     */   {
/* 130 */     this.object = obj;
/* 131 */     this.objectMimeType = mimeType;
/* 132 */     this.oldFactory = factory;
/*     */   }
/*     */ 
/*     */   public DataHandler(URL url)
/*     */   {
/* 143 */     this.dataSource = new URLDataSource(url);
/* 144 */     this.oldFactory = factory;
/*     */   }
/*     */ 
/*     */   private synchronized CommandMap getCommandMap()
/*     */   {
/* 151 */     if (this.currentCommandMap != null) {
/* 152 */       return this.currentCommandMap;
/*     */     }
/* 154 */     return CommandMap.getDefaultCommandMap();
/*     */   }
/*     */ 
/*     */   public DataSource getDataSource()
/*     */   {
/* 172 */     if (this.dataSource == null)
/*     */     {
/* 174 */       if (this.objDataSource == null)
/* 175 */         this.objDataSource = new DataHandlerDataSource(this);
/* 176 */       return this.objDataSource;
/*     */     }
/* 178 */     return this.dataSource;
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 190 */     if (this.dataSource != null) {
/* 191 */       return this.dataSource.getName();
/*     */     }
/* 193 */     return null;
/*     */   }
/*     */ 
/*     */   public String getContentType()
/*     */   {
/* 204 */     if (this.dataSource != null) {
/* 205 */       return this.dataSource.getContentType();
/*     */     }
/* 207 */     return this.objectMimeType;
/*     */   }
/*     */ 
/*     */   public InputStream getInputStream()
/*     */     throws IOException
/*     */   {
/* 235 */     InputStream ins = null;
/*     */ 
/* 237 */     if (this.dataSource != null) {
/* 238 */       ins = this.dataSource.getInputStream();
/*     */     } else {
/* 240 */       DataContentHandler dch = getDataContentHandler();
/*     */ 
/* 242 */       if (dch == null) {
/* 243 */         throw new UnsupportedDataTypeException("no DCH for MIME type " + getBaseType());
/*     */       }
/*     */ 
/* 246 */       if (((dch instanceof ObjectDataContentHandler)) && 
/* 247 */         (((ObjectDataContentHandler)dch).getDCH() == null)) {
/* 248 */         throw new UnsupportedDataTypeException("no object DCH for MIME type " + getBaseType());
/*     */       }
/*     */ 
/* 252 */       final DataContentHandler fdch = dch;
/*     */ 
/* 260 */       final PipedOutputStream pos = new PipedOutputStream();
/* 261 */       PipedInputStream pin = new PipedInputStream(pos);
/* 262 */       new Thread(new Runnable()
/*     */       {
/*     */         public void run() {
/*     */           try {
/* 266 */             fdch.writeTo(DataHandler.this.object, DataHandler.this.objectMimeType, pos);
/*     */           } catch (IOException e) {
/*     */           }
/*     */           finally {
/*     */             try {
/* 271 */               pos.close();
/*     */             }
/*     */             catch (IOException ie)
/*     */             {
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */       , "DataHandler.getInputStream").start();
/*     */ 
/* 277 */       ins = pin;
/*     */     }
/*     */ 
/* 280 */     return ins;
/*     */   }
/*     */ 
/*     */   public void writeTo(OutputStream os)
/*     */     throws IOException
/*     */   {
/* 300 */     if (this.dataSource != null) {
/* 301 */       InputStream is = null;
/* 302 */       byte[] data = new byte[8192];
/*     */ 
/* 305 */       is = this.dataSource.getInputStream();
/*     */       try
/*     */       {
/*     */         int bytes_read;
/* 308 */         while ((bytes_read = is.read(data)) > 0)
/* 309 */           os.write(data, 0, bytes_read);
/*     */       }
/*     */       finally {
/* 312 */         is.close();
/* 313 */         is = null;
/*     */       }
/*     */     } else {
/* 316 */       DataContentHandler dch = getDataContentHandler();
/* 317 */       dch.writeTo(this.object, this.objectMimeType, os);
/*     */     }
/*     */   }
/*     */ 
/*     */   public OutputStream getOutputStream()
/*     */     throws IOException
/*     */   {
/* 334 */     if (this.dataSource != null) {
/* 335 */       return this.dataSource.getOutputStream();
/*     */     }
/* 337 */     return null;
/*     */   }
/*     */ 
/*     */   public synchronized DataFlavor[] getTransferDataFlavors()
/*     */   {
/* 365 */     if (factory != this.oldFactory) {
/* 366 */       this.transferFlavors = emptyFlavors;
/*     */     }
/*     */ 
/* 369 */     if (this.transferFlavors == emptyFlavors) {
/* 370 */       this.transferFlavors = getDataContentHandler().getTransferDataFlavors();
/*     */     }
/* 372 */     if (this.transferFlavors == emptyFlavors) {
/* 373 */       return this.transferFlavors;
/*     */     }
/* 375 */     return (DataFlavor[])this.transferFlavors.clone();
/*     */   }
/*     */ 
/*     */   public boolean isDataFlavorSupported(DataFlavor flavor)
/*     */   {
/* 392 */     DataFlavor[] lFlavors = getTransferDataFlavors();
/*     */ 
/* 394 */     for (int i = 0; i < lFlavors.length; i++) {
/* 395 */       if (lFlavors[i].equals(flavor))
/* 396 */         return true;
/*     */     }
/* 398 */     return false;
/*     */   }
/*     */ 
/*     */   public Object getTransferData(DataFlavor flavor)
/*     */     throws UnsupportedFlavorException, IOException
/*     */   {
/* 436 */     return getDataContentHandler().getTransferData(flavor, this.dataSource);
/*     */   }
/*     */ 
/*     */   public synchronized void setCommandMap(CommandMap commandMap)
/*     */   {
/* 452 */     if ((commandMap != this.currentCommandMap) || (commandMap == null))
/*     */     {
/* 454 */       this.transferFlavors = emptyFlavors;
/* 455 */       this.dataContentHandler = null;
/*     */ 
/* 457 */       this.currentCommandMap = commandMap;
/*     */     }
/*     */   }
/*     */ 
/*     */   public CommandInfo[] getPreferredCommands()
/*     */   {
/* 475 */     if (this.dataSource != null) {
/* 476 */       return getCommandMap().getPreferredCommands(getBaseType(), this.dataSource);
/*     */     }
/*     */ 
/* 479 */     return getCommandMap().getPreferredCommands(getBaseType());
/*     */   }
/*     */ 
/*     */   public CommandInfo[] getAllCommands()
/*     */   {
/* 495 */     if (this.dataSource != null) {
/* 496 */       return getCommandMap().getAllCommands(getBaseType(), this.dataSource);
/*     */     }
/* 498 */     return getCommandMap().getAllCommands(getBaseType());
/*     */   }
/*     */ 
/*     */   public CommandInfo getCommand(String cmdName)
/*     */   {
/* 514 */     if (this.dataSource != null) {
/* 515 */       return getCommandMap().getCommand(getBaseType(), cmdName, this.dataSource);
/*     */     }
/*     */ 
/* 518 */     return getCommandMap().getCommand(getBaseType(), cmdName);
/*     */   }
/*     */ 
/*     */   public Object getContent()
/*     */     throws IOException
/*     */   {
/* 539 */     if (this.object != null) {
/* 540 */       return this.object;
/*     */     }
/* 542 */     return getDataContentHandler().getContent(getDataSource());
/*     */   }
/*     */ 
/*     */   public Object getBean(CommandInfo cmdinfo)
/*     */   {
/* 558 */     Object bean = null;
/*     */     try
/*     */     {
/* 562 */       ClassLoader cld = null;
/*     */ 
/* 564 */       cld = SecuritySupport.getContextClassLoader();
/* 565 */       if (cld == null)
/* 566 */         cld = getClass().getClassLoader();
/* 567 */       bean = cmdinfo.getCommandObject(this, cld);
/*     */     } catch (IOException e) {
/*     */     } catch (ClassNotFoundException e) {
/*     */     }
/* 571 */     return bean;
/*     */   }
/*     */ 
/*     */   private synchronized DataContentHandler getDataContentHandler()
/*     */   {
/* 594 */     if (factory != this.oldFactory) {
/* 595 */       this.oldFactory = factory;
/* 596 */       this.factoryDCH = null;
/* 597 */       this.dataContentHandler = null;
/* 598 */       this.transferFlavors = emptyFlavors;
/*     */     }
/*     */ 
/* 601 */     if (this.dataContentHandler != null) {
/* 602 */       return this.dataContentHandler;
/*     */     }
/* 604 */     String simpleMT = getBaseType();
/*     */ 
/* 606 */     if ((this.factoryDCH == null) && (factory != null)) {
/* 607 */       this.factoryDCH = factory.createDataContentHandler(simpleMT);
/*     */     }
/* 609 */     if (this.factoryDCH != null) {
/* 610 */       this.dataContentHandler = this.factoryDCH;
/*     */     }
/* 612 */     if (this.dataContentHandler == null) {
/* 613 */       if (this.dataSource != null) {
/* 614 */         this.dataContentHandler = getCommandMap().createDataContentHandler(simpleMT, this.dataSource);
/*     */       }
/*     */       else {
/* 617 */         this.dataContentHandler = getCommandMap().createDataContentHandler(simpleMT);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 623 */     if (this.dataSource != null) {
/* 624 */       this.dataContentHandler = new DataSourceDataContentHandler(this.dataContentHandler, this.dataSource);
/*     */     }
/*     */     else
/*     */     {
/* 628 */       this.dataContentHandler = new ObjectDataContentHandler(this.dataContentHandler, this.object, this.objectMimeType);
/*     */     }
/*     */ 
/* 632 */     return this.dataContentHandler;
/*     */   }
/*     */ 
/*     */   private synchronized String getBaseType()
/*     */   {
/* 640 */     if (this.shortType == null) {
/* 641 */       String ct = getContentType();
/*     */       try {
/* 643 */         MimeType mt = new MimeType(ct);
/* 644 */         this.shortType = mt.getBaseType();
/*     */       } catch (MimeTypeParseException e) {
/* 646 */         this.shortType = ct;
/*     */       }
/*     */     }
/* 649 */     return this.shortType;
/*     */   }
/*     */ 
/*     */   public static synchronized void setDataContentHandlerFactory(DataContentHandlerFactory newFactory)
/*     */   {
/* 667 */     if (factory != null) {
/* 668 */       throw new Error("DataContentHandlerFactory already defined");
/*     */     }
/* 670 */     SecurityManager security = System.getSecurityManager();
/* 671 */     if (security != null)
/*     */       try
/*     */       {
/* 674 */         security.checkSetFactory();
/*     */       }
/*     */       catch (SecurityException ex)
/*     */       {
/* 679 */         if (DataHandler.class.getClassLoader() != newFactory.getClass().getClassLoader())
/*     */         {
/* 681 */           throw ex;
/*     */         }
/*     */       }
/* 684 */     factory = newFactory;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.activation.DataHandler
 * JD-Core Version:    0.6.2
 */