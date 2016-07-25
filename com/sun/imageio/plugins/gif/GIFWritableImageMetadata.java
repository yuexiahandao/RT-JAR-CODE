/*     */ package com.sun.imageio.plugins.gif;
/*     */ 
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.imageio.metadata.IIOInvalidTreeException;
/*     */ import javax.imageio.metadata.IIOMetadataNode;
/*     */ import org.w3c.dom.Node;
/*     */ 
/*     */ class GIFWritableImageMetadata extends GIFImageMetadata
/*     */ {
/*     */   static final String NATIVE_FORMAT_NAME = "javax_imageio_gif_image_1.0";
/*     */ 
/*     */   GIFWritableImageMetadata()
/*     */   {
/*  48 */     super(true, "javax_imageio_gif_image_1.0", "com.sun.imageio.plugins.gif.GIFImageMetadataFormat", null, null);
/*     */   }
/*     */ 
/*     */   public boolean isReadOnly()
/*     */   {
/*  55 */     return false;
/*     */   }
/*     */ 
/*     */   public void reset()
/*     */   {
/*  60 */     this.imageLeftPosition = 0;
/*  61 */     this.imageTopPosition = 0;
/*  62 */     this.imageWidth = 0;
/*  63 */     this.imageHeight = 0;
/*  64 */     this.interlaceFlag = false;
/*  65 */     this.sortFlag = false;
/*  66 */     this.localColorTable = null;
/*     */ 
/*  69 */     this.disposalMethod = 0;
/*  70 */     this.userInputFlag = false;
/*  71 */     this.transparentColorFlag = false;
/*  72 */     this.delayTime = 0;
/*  73 */     this.transparentColorIndex = 0;
/*     */ 
/*  76 */     this.hasPlainTextExtension = false;
/*  77 */     this.textGridLeft = 0;
/*  78 */     this.textGridTop = 0;
/*  79 */     this.textGridWidth = 0;
/*  80 */     this.textGridHeight = 0;
/*  81 */     this.characterCellWidth = 0;
/*  82 */     this.characterCellHeight = 0;
/*  83 */     this.textForegroundColor = 0;
/*  84 */     this.textBackgroundColor = 0;
/*  85 */     this.text = null;
/*     */ 
/*  88 */     this.applicationIDs = null;
/*  89 */     this.authenticationCodes = null;
/*  90 */     this.applicationData = null;
/*     */ 
/*  94 */     this.comments = null;
/*     */   }
/*     */ 
/*     */   private byte[] fromISO8859(String paramString) {
/*     */     try {
/*  99 */       return paramString.getBytes("ISO-8859-1"); } catch (UnsupportedEncodingException localUnsupportedEncodingException) {
/*     */     }
/* 101 */     return "".getBytes();
/*     */   }
/*     */ 
/*     */   protected void mergeNativeTree(Node paramNode) throws IIOInvalidTreeException
/*     */   {
/* 106 */     Node localNode = paramNode;
/* 107 */     if (!localNode.getNodeName().equals("javax_imageio_gif_image_1.0")) {
/* 108 */       fatal(localNode, "Root must be javax_imageio_gif_image_1.0");
/*     */     }
/*     */ 
/* 111 */     localNode = localNode.getFirstChild();
/* 112 */     while (localNode != null) {
/* 113 */       String str1 = localNode.getNodeName();
/*     */ 
/* 115 */       if (str1.equals("ImageDescriptor")) {
/* 116 */         this.imageLeftPosition = getIntAttribute(localNode, "imageLeftPosition", -1, true, true, 0, 65535);
/*     */ 
/* 121 */         this.imageTopPosition = getIntAttribute(localNode, "imageTopPosition", -1, true, true, 0, 65535);
/*     */ 
/* 126 */         this.imageWidth = getIntAttribute(localNode, "imageWidth", -1, true, true, 1, 65535);
/*     */ 
/* 131 */         this.imageHeight = getIntAttribute(localNode, "imageHeight", -1, true, true, 1, 65535);
/*     */ 
/* 136 */         this.interlaceFlag = getBooleanAttribute(localNode, "interlaceFlag", false, true);
/*     */       }
/* 138 */       else if (str1.equals("LocalColorTable")) {
/* 139 */         int i = getIntAttribute(localNode, "sizeOfLocalColorTable", true, 2, 256);
/*     */ 
/* 142 */         if ((i != 2) && (i != 4) && (i != 8) && (i != 16) && (i != 32) && (i != 64) && (i != 128) && (i != 256))
/*     */         {
/* 150 */           fatal(localNode, "Bad value for LocalColorTable attribute sizeOfLocalColorTable!");
/*     */         }
/*     */ 
/* 154 */         this.sortFlag = getBooleanAttribute(localNode, "sortFlag", false, true);
/*     */ 
/* 156 */         this.localColorTable = getColorTable(localNode, "ColorTableEntry", true, i);
/*     */       }
/*     */       else
/*     */       {
/*     */         Object localObject1;
/* 158 */         if (str1.equals("GraphicControlExtension")) {
/* 159 */           localObject1 = getStringAttribute(localNode, "disposalMethod", null, true, disposalMethodNames);
/*     */ 
/* 162 */           this.disposalMethod = 0;
/* 163 */           while (!((String)localObject1).equals(disposalMethodNames[this.disposalMethod])) {
/* 164 */             this.disposalMethod += 1;
/*     */           }
/*     */ 
/* 167 */           this.userInputFlag = getBooleanAttribute(localNode, "userInputFlag", false, true);
/*     */ 
/* 170 */           this.transparentColorFlag = getBooleanAttribute(localNode, "transparentColorFlag", false, true);
/*     */ 
/* 174 */           this.delayTime = getIntAttribute(localNode, "delayTime", -1, true, true, 0, 65535);
/*     */ 
/* 179 */           this.transparentColorIndex = getIntAttribute(localNode, "transparentColorIndex", -1, true, true, 0, 65535);
/*     */         }
/* 183 */         else if (str1.equals("PlainTextExtension")) {
/* 184 */           this.hasPlainTextExtension = true;
/*     */ 
/* 186 */           this.textGridLeft = getIntAttribute(localNode, "textGridLeft", -1, true, true, 0, 65535);
/*     */ 
/* 191 */           this.textGridTop = getIntAttribute(localNode, "textGridTop", -1, true, true, 0, 65535);
/*     */ 
/* 196 */           this.textGridWidth = getIntAttribute(localNode, "textGridWidth", -1, true, true, 1, 65535);
/*     */ 
/* 201 */           this.textGridHeight = getIntAttribute(localNode, "textGridHeight", -1, true, true, 1, 65535);
/*     */ 
/* 206 */           this.characterCellWidth = getIntAttribute(localNode, "characterCellWidth", -1, true, true, 1, 65535);
/*     */ 
/* 211 */           this.characterCellHeight = getIntAttribute(localNode, "characterCellHeight", -1, true, true, 1, 65535);
/*     */ 
/* 216 */           this.textForegroundColor = getIntAttribute(localNode, "textForegroundColor", -1, true, true, 0, 255);
/*     */ 
/* 221 */           this.textBackgroundColor = getIntAttribute(localNode, "textBackgroundColor", -1, true, true, 0, 255);
/*     */ 
/* 232 */           localObject1 = getStringAttribute(localNode, "text", "", false, null);
/*     */ 
/* 234 */           this.text = fromISO8859((String)localObject1);
/*     */         }
/*     */         else
/*     */         {
/*     */           String str2;
/* 235 */           if (str1.equals("ApplicationExtensions")) {
/* 236 */             localObject1 = (IIOMetadataNode)localNode.getFirstChild();
/*     */ 
/* 239 */             if (!((IIOMetadataNode)localObject1).getNodeName().equals("ApplicationExtension")) {
/* 240 */               fatal(localNode, "Only a ApplicationExtension may be a child of a ApplicationExtensions!");
/*     */             }
/*     */ 
/* 244 */             str2 = getStringAttribute((Node)localObject1, "applicationID", null, true, null);
/*     */ 
/* 248 */             String str3 = getStringAttribute((Node)localObject1, "authenticationCode", null, true, null);
/*     */ 
/* 252 */             Object localObject2 = ((IIOMetadataNode)localObject1).getUserObject();
/*     */ 
/* 254 */             if ((localObject2 == null) || (!(localObject2 instanceof byte[])))
/*     */             {
/* 256 */               fatal((Node)localObject1, "Bad user object in ApplicationExtension!");
/*     */             }
/*     */ 
/* 260 */             if (this.applicationIDs == null) {
/* 261 */               this.applicationIDs = new ArrayList();
/* 262 */               this.authenticationCodes = new ArrayList();
/* 263 */               this.applicationData = new ArrayList();
/*     */             }
/*     */ 
/* 266 */             this.applicationIDs.add(fromISO8859(str2));
/* 267 */             this.authenticationCodes.add(fromISO8859(str3));
/* 268 */             this.applicationData.add(localObject2);
/* 269 */           } else if (str1.equals("CommentExtensions")) {
/* 270 */             localObject1 = localNode.getFirstChild();
/* 271 */             if (localObject1 != null)
/* 272 */               while (localObject1 != null) {
/* 273 */                 if (!((Node)localObject1).getNodeName().equals("CommentExtension")) {
/* 274 */                   fatal(localNode, "Only a CommentExtension may be a child of a CommentExtensions!");
/*     */                 }
/*     */ 
/* 278 */                 if (this.comments == null) {
/* 279 */                   this.comments = new ArrayList();
/*     */                 }
/*     */ 
/* 282 */                 str2 = getStringAttribute((Node)localObject1, "value", null, true, null);
/*     */ 
/* 286 */                 this.comments.add(fromISO8859(str2));
/*     */ 
/* 288 */                 localObject1 = ((Node)localObject1).getNextSibling();
/*     */               }
/*     */           }
/*     */           else {
/* 292 */             fatal(localNode, "Unknown child of root node!");
/*     */           }
/*     */         }
/*     */       }
/* 295 */       localNode = localNode.getNextSibling();
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void mergeStandardTree(Node paramNode) throws IIOInvalidTreeException
/*     */   {
/* 301 */     Node localNode1 = paramNode;
/* 302 */     if (!localNode1.getNodeName().equals("javax_imageio_1.0"))
/*     */     {
/* 304 */       fatal(localNode1, "Root must be javax_imageio_1.0");
/*     */     }
/*     */ 
/* 308 */     localNode1 = localNode1.getFirstChild();
/* 309 */     while (localNode1 != null) {
/* 310 */       String str1 = localNode1.getNodeName();
/*     */       Node localNode2;
/*     */       String str2;
/* 312 */       if (str1.equals("Chroma")) {
/* 313 */         localNode2 = localNode1.getFirstChild();
/* 314 */         while (localNode2 != null) {
/* 315 */           str2 = localNode2.getNodeName();
/* 316 */           if (str2.equals("Palette")) {
/* 317 */             this.localColorTable = getColorTable(localNode2, "PaletteEntry", false, -1);
/*     */ 
/* 320 */             break;
/*     */           }
/* 322 */           localNode2 = localNode2.getNextSibling();
/*     */         }
/* 324 */       } else if (str1.equals("Compression")) {
/* 325 */         localNode2 = localNode1.getFirstChild();
/* 326 */         while (localNode2 != null) {
/* 327 */           str2 = localNode2.getNodeName();
/* 328 */           if (str2.equals("NumProgressiveScans")) {
/* 329 */             int i = getIntAttribute(localNode2, "value", 4, false, true, 1, 2147483647);
/*     */ 
/* 332 */             if (i <= 1) break;
/* 333 */             this.interlaceFlag = true; break;
/*     */           }
/*     */ 
/* 337 */           localNode2 = localNode2.getNextSibling();
/*     */         }
/* 339 */       } else if (str1.equals("Dimension")) {
/* 340 */         localNode2 = localNode1.getFirstChild();
/* 341 */         while (localNode2 != null) {
/* 342 */           str2 = localNode2.getNodeName();
/* 343 */           if (str2.equals("HorizontalPixelOffset")) {
/* 344 */             this.imageLeftPosition = getIntAttribute(localNode2, "value", -1, true, true, 0, 65535);
/*     */           }
/* 348 */           else if (str2.equals("VerticalPixelOffset")) {
/* 349 */             this.imageTopPosition = getIntAttribute(localNode2, "value", -1, true, true, 0, 65535);
/*     */           }
/*     */ 
/* 354 */           localNode2 = localNode2.getNextSibling();
/*     */         }
/* 356 */       } else if (str1.equals("Text")) {
/* 357 */         localNode2 = localNode1.getFirstChild();
/* 358 */         while (localNode2 != null) {
/* 359 */           str2 = localNode2.getNodeName();
/* 360 */           if ((str2.equals("TextEntry")) && (getAttribute(localNode2, "compression", "none", false).equals("none")) && (Charset.isSupported(getAttribute(localNode2, "encoding", "ISO-8859-1", false))))
/*     */           {
/* 367 */             String str3 = getAttribute(localNode2, "value");
/* 368 */             byte[] arrayOfByte = fromISO8859(str3);
/* 369 */             if (this.comments == null) {
/* 370 */               this.comments = new ArrayList();
/*     */             }
/* 372 */             this.comments.add(arrayOfByte);
/*     */           }
/* 374 */           localNode2 = localNode2.getNextSibling();
/*     */         }
/* 376 */       } else if (str1.equals("Transparency")) {
/* 377 */         localNode2 = localNode1.getFirstChild();
/* 378 */         while (localNode2 != null) {
/* 379 */           str2 = localNode2.getNodeName();
/* 380 */           if (str2.equals("TransparentIndex")) {
/* 381 */             this.transparentColorIndex = getIntAttribute(localNode2, "value", -1, true, true, 0, 255);
/*     */ 
/* 385 */             this.transparentColorFlag = true;
/* 386 */             break;
/*     */           }
/* 388 */           localNode2 = localNode2.getNextSibling();
/*     */         }
/*     */       }
/*     */ 
/* 392 */       localNode1 = localNode1.getNextSibling();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setFromTree(String paramString, Node paramNode)
/*     */     throws IIOInvalidTreeException
/*     */   {
/* 399 */     reset();
/* 400 */     mergeTree(paramString, paramNode);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.imageio.plugins.gif.GIFWritableImageMetadata
 * JD-Core Version:    0.6.2
 */