/*     */ package javax.imageio.metadata;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import org.w3c.dom.Node;
/*     */ 
/*     */ public abstract class IIOMetadata
/*     */ {
/*     */   protected boolean standardFormatSupported;
/*  78 */   protected String nativeMetadataFormatName = null;
/*     */ 
/*  85 */   protected String nativeMetadataFormatClassName = null;
/*     */ 
/*  92 */   protected String[] extraMetadataFormatNames = null;
/*     */ 
/* 100 */   protected String[] extraMetadataFormatClassNames = null;
/*     */ 
/* 115 */   protected IIOMetadataController defaultController = null;
/*     */ 
/* 129 */   protected IIOMetadataController controller = null;
/*     */ 
/*     */   protected IIOMetadata()
/*     */   {
/*     */   }
/*     */ 
/*     */   protected IIOMetadata(boolean paramBoolean, String paramString1, String paramString2, String[] paramArrayOfString1, String[] paramArrayOfString2)
/*     */   {
/* 177 */     this.standardFormatSupported = paramBoolean;
/* 178 */     this.nativeMetadataFormatName = paramString1;
/* 179 */     this.nativeMetadataFormatClassName = paramString2;
/* 180 */     if (paramArrayOfString1 != null) {
/* 181 */       if (paramArrayOfString1.length == 0) {
/* 182 */         throw new IllegalArgumentException("extraMetadataFormatNames.length == 0!");
/*     */       }
/*     */ 
/* 185 */       if (paramArrayOfString2 == null) {
/* 186 */         throw new IllegalArgumentException("extraMetadataFormatNames != null && extraMetadataFormatClassNames == null!");
/*     */       }
/*     */ 
/* 189 */       if (paramArrayOfString2.length != paramArrayOfString1.length)
/*     */       {
/* 191 */         throw new IllegalArgumentException("extraMetadataFormatClassNames.length != extraMetadataFormatNames.length!");
/*     */       }
/*     */ 
/* 194 */       this.extraMetadataFormatNames = ((String[])paramArrayOfString1.clone());
/*     */ 
/* 196 */       this.extraMetadataFormatClassNames = ((String[])paramArrayOfString2.clone());
/*     */     }
/* 199 */     else if (paramArrayOfString2 != null) {
/* 200 */       throw new IllegalArgumentException("extraMetadataFormatNames == null && extraMetadataFormatClassNames != null!");
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean isStandardMetadataFormatSupported()
/*     */   {
/* 224 */     return this.standardFormatSupported;
/*     */   }
/*     */ 
/*     */   public abstract boolean isReadOnly();
/*     */ 
/*     */   public String getNativeMetadataFormatName()
/*     */   {
/* 261 */     return this.nativeMetadataFormatName;
/*     */   }
/*     */ 
/*     */   public String[] getExtraMetadataFormatNames()
/*     */   {
/* 285 */     if (this.extraMetadataFormatNames == null) {
/* 286 */       return null;
/*     */     }
/* 288 */     return (String[])this.extraMetadataFormatNames.clone();
/*     */   }
/*     */ 
/*     */   public String[] getMetadataFormatNames()
/*     */   {
/* 311 */     String str = getNativeMetadataFormatName();
/* 312 */     Object localObject = isStandardMetadataFormatSupported() ? "javax_imageio_1.0" : null;
/*     */ 
/* 314 */     String[] arrayOfString1 = getExtraMetadataFormatNames();
/*     */ 
/* 316 */     int i = 0;
/* 317 */     if (str != null) {
/* 318 */       i++;
/*     */     }
/* 320 */     if (localObject != null) {
/* 321 */       i++;
/*     */     }
/* 323 */     if (arrayOfString1 != null) {
/* 324 */       i += arrayOfString1.length;
/*     */     }
/* 326 */     if (i == 0) {
/* 327 */       return null;
/*     */     }
/*     */ 
/* 330 */     String[] arrayOfString2 = new String[i];
/* 331 */     int j = 0;
/* 332 */     if (str != null) {
/* 333 */       arrayOfString2[(j++)] = str;
/*     */     }
/* 335 */     if (localObject != null) {
/* 336 */       arrayOfString2[(j++)] = localObject;
/*     */     }
/* 338 */     if (arrayOfString1 != null) {
/* 339 */       for (int k = 0; k < arrayOfString1.length; k++) {
/* 340 */         arrayOfString2[(j++)] = arrayOfString1[k];
/*     */       }
/*     */     }
/*     */ 
/* 344 */     return arrayOfString2;
/*     */   }
/*     */ 
/*     */   public IIOMetadataFormat getMetadataFormat(String paramString)
/*     */   {
/* 377 */     if (paramString == null) {
/* 378 */       throw new IllegalArgumentException("formatName == null!");
/*     */     }
/* 380 */     if ((this.standardFormatSupported) && (paramString.equals("javax_imageio_1.0")))
/*     */     {
/* 383 */       return IIOMetadataFormatImpl.getStandardFormatInstance();
/*     */     }
/* 385 */     String str = null;
/* 386 */     if (paramString.equals(this.nativeMetadataFormatName))
/* 387 */       str = this.nativeMetadataFormatClassName;
/* 388 */     else if (this.extraMetadataFormatNames != null) {
/* 389 */       for (int i = 0; i < this.extraMetadataFormatNames.length; i++) {
/* 390 */         if (paramString.equals(this.extraMetadataFormatNames[i])) {
/* 391 */           str = this.extraMetadataFormatClassNames[i];
/* 392 */           break;
/*     */         }
/*     */       }
/*     */     }
/* 396 */     if (str == null)
/* 397 */       throw new IllegalArgumentException("Unsupported format name");
/*     */     final Object localObject;
/*     */     try {
/* 400 */       Class localClass = null;
/* 401 */       localObject = this;
/*     */ 
/* 405 */       ClassLoader localClassLoader = (ClassLoader)AccessController.doPrivileged(new PrivilegedAction()
/*     */       {
/*     */         public Object run()
/*     */         {
/* 409 */           return localObject.getClass().getClassLoader();
/*     */         }
/*     */       });
/*     */       try
/*     */       {
/* 414 */         localClass = Class.forName(str, true, localClassLoader);
/*     */       }
/*     */       catch (ClassNotFoundException localClassNotFoundException1)
/*     */       {
/* 420 */         localClassLoader = (ClassLoader)AccessController.doPrivileged(new PrivilegedAction()
/*     */         {
/*     */           public Object run()
/*     */           {
/* 424 */             return Thread.currentThread().getContextClassLoader();
/*     */           }
/*     */         });
/*     */         try {
/* 428 */           localClass = Class.forName(str, true, localClassLoader);
/*     */         }
/*     */         catch (ClassNotFoundException localClassNotFoundException2)
/*     */         {
/* 434 */           localClass = Class.forName(str, true, ClassLoader.getSystemClassLoader());
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 439 */       Method localMethod = localClass.getMethod("getInstance", new Class[0]);
/* 440 */       return (IIOMetadataFormat)localMethod.invoke(null, new Object[0]);
/*     */     } catch (Exception localException) {
/* 442 */       localObject = new IllegalStateException("Can't obtain format");
/*     */ 
/* 444 */       ((RuntimeException)localObject).initCause(localException);
/* 445 */     }throw ((Throwable)localObject);
/*     */   }
/*     */ 
/*     */   public abstract Node getAsTree(String paramString);
/*     */ 
/*     */   public abstract void mergeTree(String paramString, Node paramNode)
/*     */     throws IIOInvalidTreeException;
/*     */ 
/*     */   protected IIOMetadataNode getStandardChromaNode()
/*     */   {
/* 527 */     return null;
/*     */   }
/*     */ 
/*     */   protected IIOMetadataNode getStandardCompressionNode()
/*     */   {
/* 549 */     return null;
/*     */   }
/*     */ 
/*     */   protected IIOMetadataNode getStandardDataNode()
/*     */   {
/* 571 */     return null;
/*     */   }
/*     */ 
/*     */   protected IIOMetadataNode getStandardDimensionNode()
/*     */   {
/* 593 */     return null;
/*     */   }
/*     */ 
/*     */   protected IIOMetadataNode getStandardDocumentNode()
/*     */   {
/* 614 */     return null;
/*     */   }
/*     */ 
/*     */   protected IIOMetadataNode getStandardTextNode()
/*     */   {
/* 635 */     return null;
/*     */   }
/*     */ 
/*     */   protected IIOMetadataNode getStandardTileNode()
/*     */   {
/* 656 */     return null;
/*     */   }
/*     */ 
/*     */   protected IIOMetadataNode getStandardTransparencyNode()
/*     */   {
/* 676 */     return null;
/*     */   }
/*     */ 
/*     */   private void append(IIOMetadataNode paramIIOMetadataNode1, IIOMetadataNode paramIIOMetadataNode2)
/*     */   {
/* 684 */     if (paramIIOMetadataNode2 != null)
/* 685 */       paramIIOMetadataNode1.appendChild(paramIIOMetadataNode2);
/*     */   }
/*     */ 
/*     */   protected final IIOMetadataNode getStandardTree()
/*     */   {
/* 716 */     IIOMetadataNode localIIOMetadataNode = new IIOMetadataNode("javax_imageio_1.0");
/*     */ 
/* 718 */     append(localIIOMetadataNode, getStandardChromaNode());
/* 719 */     append(localIIOMetadataNode, getStandardCompressionNode());
/* 720 */     append(localIIOMetadataNode, getStandardDataNode());
/* 721 */     append(localIIOMetadataNode, getStandardDimensionNode());
/* 722 */     append(localIIOMetadataNode, getStandardDocumentNode());
/* 723 */     append(localIIOMetadataNode, getStandardTextNode());
/* 724 */     append(localIIOMetadataNode, getStandardTileNode());
/* 725 */     append(localIIOMetadataNode, getStandardTransparencyNode());
/* 726 */     return localIIOMetadataNode;
/*     */   }
/*     */ 
/*     */   public void setFromTree(String paramString, Node paramNode)
/*     */     throws IIOInvalidTreeException
/*     */   {
/* 758 */     reset();
/* 759 */     mergeTree(paramString, paramNode);
/*     */   }
/*     */ 
/*     */   public abstract void reset();
/*     */ 
/*     */   public void setController(IIOMetadataController paramIIOMetadataController)
/*     */   {
/* 800 */     this.controller = paramIIOMetadataController;
/*     */   }
/*     */ 
/*     */   public IIOMetadataController getController()
/*     */   {
/* 822 */     return this.controller;
/*     */   }
/*     */ 
/*     */   public IIOMetadataController getDefaultController()
/*     */   {
/* 843 */     return this.defaultController;
/*     */   }
/*     */ 
/*     */   public boolean hasController()
/*     */   {
/* 863 */     return getController() != null;
/*     */   }
/*     */ 
/*     */   public boolean activateController()
/*     */   {
/* 895 */     if (!hasController()) {
/* 896 */       throw new IllegalStateException("hasController() == false!");
/*     */     }
/* 898 */     return getController().activate(this);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.imageio.metadata.IIOMetadata
 * JD-Core Version:    0.6.2
 */