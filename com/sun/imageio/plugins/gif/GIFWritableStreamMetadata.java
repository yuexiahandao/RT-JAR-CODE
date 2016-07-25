/*     */ package com.sun.imageio.plugins.gif;
/*     */ 
/*     */ import javax.imageio.metadata.IIOInvalidTreeException;
/*     */ import org.w3c.dom.Node;
/*     */ 
/*     */ class GIFWritableStreamMetadata extends GIFStreamMetadata
/*     */ {
/*     */   static final String NATIVE_FORMAT_NAME = "javax_imageio_gif_stream_1.0";
/*     */ 
/*     */   public GIFWritableStreamMetadata()
/*     */   {
/*  49 */     super(true, "javax_imageio_gif_stream_1.0", "com.sun.imageio.plugins.gif.GIFStreamMetadataFormat", null, null);
/*     */ 
/*  55 */     reset();
/*     */   }
/*     */ 
/*     */   public boolean isReadOnly() {
/*  59 */     return false;
/*     */   }
/*     */ 
/*     */   public void mergeTree(String paramString, Node paramNode) throws IIOInvalidTreeException
/*     */   {
/*  64 */     if (paramString.equals("javax_imageio_gif_stream_1.0")) {
/*  65 */       if (paramNode == null) {
/*  66 */         throw new IllegalArgumentException("root == null!");
/*     */       }
/*  68 */       mergeNativeTree(paramNode);
/*  69 */     } else if (paramString.equals("javax_imageio_1.0"))
/*     */     {
/*  71 */       if (paramNode == null) {
/*  72 */         throw new IllegalArgumentException("root == null!");
/*     */       }
/*  74 */       mergeStandardTree(paramNode);
/*     */     } else {
/*  76 */       throw new IllegalArgumentException("Not a recognized format!");
/*     */     }
/*     */   }
/*     */ 
/*     */   public void reset() {
/*  81 */     this.version = null;
/*     */ 
/*  83 */     this.logicalScreenWidth = -1;
/*  84 */     this.logicalScreenHeight = -1;
/*  85 */     this.colorResolution = -1;
/*  86 */     this.pixelAspectRatio = 0;
/*     */ 
/*  88 */     this.backgroundColorIndex = 0;
/*  89 */     this.sortFlag = false;
/*  90 */     this.globalColorTable = null;
/*     */   }
/*     */ 
/*     */   protected void mergeNativeTree(Node paramNode) throws IIOInvalidTreeException {
/*  94 */     Node localNode = paramNode;
/*  95 */     if (!localNode.getNodeName().equals("javax_imageio_gif_stream_1.0")) {
/*  96 */       fatal(localNode, "Root must be javax_imageio_gif_stream_1.0");
/*     */     }
/*     */ 
/*  99 */     localNode = localNode.getFirstChild();
/* 100 */     while (localNode != null) {
/* 101 */       String str = localNode.getNodeName();
/*     */ 
/* 103 */       if (str.equals("Version")) {
/* 104 */         this.version = getStringAttribute(localNode, "value", null, true, versionStrings);
/*     */       }
/* 106 */       else if (str.equals("LogicalScreenDescriptor"))
/*     */       {
/* 112 */         this.logicalScreenWidth = getIntAttribute(localNode, "logicalScreenWidth", -1, true, true, 1, 65535);
/*     */ 
/* 118 */         this.logicalScreenHeight = getIntAttribute(localNode, "logicalScreenHeight", -1, true, true, 1, 65535);
/*     */ 
/* 124 */         this.colorResolution = getIntAttribute(localNode, "colorResolution", -1, true, true, 1, 8);
/*     */ 
/* 130 */         this.pixelAspectRatio = getIntAttribute(localNode, "pixelAspectRatio", 0, true, true, 0, 255);
/*     */       }
/* 134 */       else if (str.equals("GlobalColorTable")) {
/* 135 */         int i = getIntAttribute(localNode, "sizeOfGlobalColorTable", true, 2, 256);
/*     */ 
/* 138 */         if ((i != 2) && (i != 4) && (i != 8) && (i != 16) && (i != 32) && (i != 64) && (i != 128) && (i != 256))
/*     */         {
/* 146 */           fatal(localNode, "Bad value for GlobalColorTable attribute sizeOfGlobalColorTable!");
/*     */         }
/*     */ 
/* 150 */         this.backgroundColorIndex = getIntAttribute(localNode, "backgroundColorIndex", 0, true, true, 0, 255);
/*     */ 
/* 155 */         this.sortFlag = getBooleanAttribute(localNode, "sortFlag", false, true);
/*     */ 
/* 157 */         this.globalColorTable = getColorTable(localNode, "ColorTableEntry", true, i);
/*     */       }
/*     */       else {
/* 160 */         fatal(localNode, "Unknown child of root node!");
/*     */       }
/*     */ 
/* 163 */       localNode = localNode.getNextSibling();
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void mergeStandardTree(Node paramNode) throws IIOInvalidTreeException
/*     */   {
/* 169 */     Node localNode1 = paramNode;
/* 170 */     if (!localNode1.getNodeName().equals("javax_imageio_1.0"))
/*     */     {
/* 172 */       fatal(localNode1, "Root must be javax_imageio_1.0");
/*     */     }
/*     */ 
/* 176 */     localNode1 = localNode1.getFirstChild();
/* 177 */     while (localNode1 != null) {
/* 178 */       String str1 = localNode1.getNodeName();
/*     */       Node localNode2;
/*     */       String str2;
/* 180 */       if (str1.equals("Chroma")) {
/* 181 */         localNode2 = localNode1.getFirstChild();
/* 182 */         while (localNode2 != null) {
/* 183 */           str2 = localNode2.getNodeName();
/* 184 */           if (str2.equals("Palette")) {
/* 185 */             this.globalColorTable = getColorTable(localNode2, "PaletteEntry", false, -1);
/*     */           }
/* 189 */           else if (str2.equals("BackgroundIndex")) {
/* 190 */             this.backgroundColorIndex = getIntAttribute(localNode2, "value", -1, true, true, 0, 255);
/*     */           }
/*     */ 
/* 195 */           localNode2 = localNode2.getNextSibling();
/*     */         }
/* 197 */       } else if (str1.equals("Data")) {
/* 198 */         localNode2 = localNode1.getFirstChild();
/* 199 */         while (localNode2 != null) {
/* 200 */           str2 = localNode2.getNodeName();
/* 201 */           if (str2.equals("BitsPerSample")) {
/* 202 */             this.colorResolution = getIntAttribute(localNode2, "value", -1, true, true, 1, 8);
/*     */ 
/* 206 */             break;
/*     */           }
/* 208 */           localNode2 = localNode2.getNextSibling();
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/*     */         int i;
/* 210 */         if (str1.equals("Dimension")) {
/* 211 */           localNode2 = localNode1.getFirstChild();
/* 212 */           while (localNode2 != null) {
/* 213 */             str2 = localNode2.getNodeName();
/* 214 */             if (str2.equals("PixelAspectRatio")) {
/* 215 */               float f = getFloatAttribute(localNode2, "value");
/*     */ 
/* 217 */               if (f == 1.0F) {
/* 218 */                 this.pixelAspectRatio = 0;
/*     */               } else {
/* 220 */                 i = (int)(f * 64.0F - 15.0F);
/* 221 */                 this.pixelAspectRatio = Math.max(Math.min(i, 255), 0);
/*     */               }
/*     */             }
/* 224 */             else if (str2.equals("HorizontalScreenSize")) {
/* 225 */               this.logicalScreenWidth = getIntAttribute(localNode2, "value", -1, true, true, 1, 65535);
/*     */             }
/* 229 */             else if (str2.equals("VerticalScreenSize")) {
/* 230 */               this.logicalScreenHeight = getIntAttribute(localNode2, "value", -1, true, true, 1, 65535);
/*     */             }
/*     */ 
/* 235 */             localNode2 = localNode2.getNextSibling();
/*     */           }
/* 237 */         } else if (str1.equals("Document")) {
/* 238 */           localNode2 = localNode1.getFirstChild();
/* 239 */           while (localNode2 != null) {
/* 240 */             str2 = localNode2.getNodeName();
/* 241 */             if (str2.equals("FormatVersion")) {
/* 242 */               String str3 = getStringAttribute(localNode2, "value", null, true, null);
/*     */ 
/* 245 */               for (i = 0; i < versionStrings.length; i++) {
/* 246 */                 if (str3.equals(versionStrings[i])) {
/* 247 */                   this.version = str3;
/* 248 */                   break;
/*     */                 }
/*     */               }
/* 251 */               break;
/*     */             }
/* 253 */             localNode2 = localNode2.getNextSibling();
/*     */           }
/*     */         }
/*     */       }
/* 257 */       localNode1 = localNode1.getNextSibling();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setFromTree(String paramString, Node paramNode)
/*     */     throws IIOInvalidTreeException
/*     */   {
/* 264 */     reset();
/* 265 */     mergeTree(paramString, paramNode);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.imageio.plugins.gif.GIFWritableStreamMetadata
 * JD-Core Version:    0.6.2
 */