/*     */ package com.sun.imageio.plugins.gif;
/*     */ 
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import javax.imageio.metadata.IIOInvalidTreeException;
/*     */ import javax.imageio.metadata.IIOMetadataNode;
/*     */ import org.w3c.dom.Node;
/*     */ 
/*     */ public class GIFImageMetadata extends GIFMetadata
/*     */ {
/*     */   static final String nativeMetadataFormatName = "javax_imageio_gif_image_1.0";
/*  46 */   static final String[] disposalMethodNames = { "none", "doNotDispose", "restoreToBackgroundColor", "restoreToPrevious", "undefinedDisposalMethod4", "undefinedDisposalMethod5", "undefinedDisposalMethod6", "undefinedDisposalMethod7" };
/*     */   public int imageLeftPosition;
/*     */   public int imageTopPosition;
/*     */   public int imageWidth;
/*     */   public int imageHeight;
/*  62 */   public boolean interlaceFlag = false;
/*  63 */   public boolean sortFlag = false;
/*  64 */   public byte[] localColorTable = null;
/*     */ 
/*  67 */   public int disposalMethod = 0;
/*  68 */   public boolean userInputFlag = false;
/*  69 */   public boolean transparentColorFlag = false;
/*  70 */   public int delayTime = 0;
/*  71 */   public int transparentColorIndex = 0;
/*     */ 
/*  74 */   public boolean hasPlainTextExtension = false;
/*     */   public int textGridLeft;
/*     */   public int textGridTop;
/*     */   public int textGridWidth;
/*     */   public int textGridHeight;
/*     */   public int characterCellWidth;
/*     */   public int characterCellHeight;
/*     */   public int textForegroundColor;
/*     */   public int textBackgroundColor;
/*     */   public byte[] text;
/*  87 */   public List applicationIDs = null;
/*     */ 
/*  90 */   public List authenticationCodes = null;
/*     */ 
/*  93 */   public List applicationData = null;
/*     */ 
/*  97 */   public List comments = null;
/*     */ 
/*     */   protected GIFImageMetadata(boolean paramBoolean, String paramString1, String paramString2, String[] paramArrayOfString1, String[] paramArrayOfString2)
/*     */   {
/* 105 */     super(paramBoolean, paramString1, paramString2, paramArrayOfString1, paramArrayOfString2);
/*     */   }
/*     */ 
/*     */   public GIFImageMetadata()
/*     */   {
/* 113 */     this(true, "javax_imageio_gif_image_1.0", "com.sun.imageio.plugins.gif.GIFImageMetadataFormat", null, null);
/*     */   }
/*     */ 
/*     */   public boolean isReadOnly()
/*     */   {
/* 120 */     return true;
/*     */   }
/*     */ 
/*     */   public Node getAsTree(String paramString) {
/* 124 */     if (paramString.equals("javax_imageio_gif_image_1.0"))
/* 125 */       return getNativeTree();
/* 126 */     if (paramString.equals("javax_imageio_1.0"))
/*     */     {
/* 128 */       return getStandardTree();
/*     */     }
/* 130 */     throw new IllegalArgumentException("Not a recognized format!");
/*     */   }
/*     */ 
/*     */   private String toISO8859(byte[] paramArrayOfByte)
/*     */   {
/*     */     try {
/* 136 */       return new String(paramArrayOfByte, "ISO-8859-1"); } catch (UnsupportedEncodingException localUnsupportedEncodingException) {
/*     */     }
/* 138 */     return "";
/*     */   }
/*     */ 
/*     */   private Node getNativeTree()
/*     */   {
/* 144 */     IIOMetadataNode localIIOMetadataNode2 = new IIOMetadataNode("javax_imageio_gif_image_1.0");
/*     */ 
/* 148 */     IIOMetadataNode localIIOMetadataNode1 = new IIOMetadataNode("ImageDescriptor");
/* 149 */     localIIOMetadataNode1.setAttribute("imageLeftPosition", Integer.toString(this.imageLeftPosition));
/*     */ 
/* 151 */     localIIOMetadataNode1.setAttribute("imageTopPosition", Integer.toString(this.imageTopPosition));
/*     */ 
/* 153 */     localIIOMetadataNode1.setAttribute("imageWidth", Integer.toString(this.imageWidth));
/* 154 */     localIIOMetadataNode1.setAttribute("imageHeight", Integer.toString(this.imageHeight));
/* 155 */     localIIOMetadataNode1.setAttribute("interlaceFlag", this.interlaceFlag ? "TRUE" : "FALSE");
/*     */ 
/* 157 */     localIIOMetadataNode2.appendChild(localIIOMetadataNode1);
/*     */     IIOMetadataNode localIIOMetadataNode3;
/* 160 */     if (this.localColorTable != null) {
/* 161 */       localIIOMetadataNode1 = new IIOMetadataNode("LocalColorTable");
/* 162 */       i = this.localColorTable.length / 3;
/* 163 */       localIIOMetadataNode1.setAttribute("sizeOfLocalColorTable", Integer.toString(i));
/*     */ 
/* 165 */       localIIOMetadataNode1.setAttribute("sortFlag", this.sortFlag ? "TRUE" : "FALSE");
/*     */ 
/* 168 */       for (j = 0; j < i; j++) {
/* 169 */         localIIOMetadataNode3 = new IIOMetadataNode("ColorTableEntry");
/*     */ 
/* 171 */         localIIOMetadataNode3.setAttribute("index", Integer.toString(j));
/* 172 */         int m = this.localColorTable[(3 * j)] & 0xFF;
/* 173 */         int n = this.localColorTable[(3 * j + 1)] & 0xFF;
/* 174 */         int i1 = this.localColorTable[(3 * j + 2)] & 0xFF;
/* 175 */         localIIOMetadataNode3.setAttribute("red", Integer.toString(m));
/* 176 */         localIIOMetadataNode3.setAttribute("green", Integer.toString(n));
/* 177 */         localIIOMetadataNode3.setAttribute("blue", Integer.toString(i1));
/* 178 */         localIIOMetadataNode1.appendChild(localIIOMetadataNode3);
/*     */       }
/* 180 */       localIIOMetadataNode2.appendChild(localIIOMetadataNode1);
/*     */     }
/*     */ 
/* 184 */     localIIOMetadataNode1 = new IIOMetadataNode("GraphicControlExtension");
/* 185 */     localIIOMetadataNode1.setAttribute("disposalMethod", disposalMethodNames[this.disposalMethod]);
/*     */ 
/* 187 */     localIIOMetadataNode1.setAttribute("userInputFlag", this.userInputFlag ? "TRUE" : "FALSE");
/*     */ 
/* 189 */     localIIOMetadataNode1.setAttribute("transparentColorFlag", this.transparentColorFlag ? "TRUE" : "FALSE");
/*     */ 
/* 191 */     localIIOMetadataNode1.setAttribute("delayTime", Integer.toString(this.delayTime));
/*     */ 
/* 193 */     localIIOMetadataNode1.setAttribute("transparentColorIndex", Integer.toString(this.transparentColorIndex));
/*     */ 
/* 195 */     localIIOMetadataNode2.appendChild(localIIOMetadataNode1);
/*     */ 
/* 197 */     if (this.hasPlainTextExtension) {
/* 198 */       localIIOMetadataNode1 = new IIOMetadataNode("PlainTextExtension");
/* 199 */       localIIOMetadataNode1.setAttribute("textGridLeft", Integer.toString(this.textGridLeft));
/*     */ 
/* 201 */       localIIOMetadataNode1.setAttribute("textGridTop", Integer.toString(this.textGridTop));
/*     */ 
/* 203 */       localIIOMetadataNode1.setAttribute("textGridWidth", Integer.toString(this.textGridWidth));
/*     */ 
/* 205 */       localIIOMetadataNode1.setAttribute("textGridHeight", Integer.toString(this.textGridHeight));
/*     */ 
/* 207 */       localIIOMetadataNode1.setAttribute("characterCellWidth", Integer.toString(this.characterCellWidth));
/*     */ 
/* 209 */       localIIOMetadataNode1.setAttribute("characterCellHeight", Integer.toString(this.characterCellHeight));
/*     */ 
/* 211 */       localIIOMetadataNode1.setAttribute("textForegroundColor", Integer.toString(this.textForegroundColor));
/*     */ 
/* 213 */       localIIOMetadataNode1.setAttribute("textBackgroundColor", Integer.toString(this.textBackgroundColor));
/*     */ 
/* 215 */       localIIOMetadataNode1.setAttribute("text", toISO8859(this.text));
/*     */ 
/* 217 */       localIIOMetadataNode2.appendChild(localIIOMetadataNode1);
/*     */     }
/*     */ 
/* 221 */     int i = this.applicationIDs == null ? 0 : this.applicationIDs.size();
/*     */     Object localObject;
/*     */     byte[] arrayOfByte1;
/* 223 */     if (i > 0) {
/* 224 */       localIIOMetadataNode1 = new IIOMetadataNode("ApplicationExtensions");
/* 225 */       for (j = 0; j < i; j++) {
/* 226 */         localIIOMetadataNode3 = new IIOMetadataNode("ApplicationExtension");
/*     */ 
/* 228 */         localObject = (byte[])this.applicationIDs.get(j);
/* 229 */         localIIOMetadataNode3.setAttribute("applicationID", toISO8859((byte[])localObject));
/*     */ 
/* 231 */         arrayOfByte1 = (byte[])this.authenticationCodes.get(j);
/* 232 */         localIIOMetadataNode3.setAttribute("authenticationCode", toISO8859(arrayOfByte1));
/*     */ 
/* 234 */         byte[] arrayOfByte2 = (byte[])this.applicationData.get(j);
/* 235 */         localIIOMetadataNode3.setUserObject((byte[])arrayOfByte2.clone());
/* 236 */         localIIOMetadataNode1.appendChild(localIIOMetadataNode3);
/*     */       }
/*     */ 
/* 239 */       localIIOMetadataNode2.appendChild(localIIOMetadataNode1);
/*     */     }
/*     */ 
/* 243 */     int j = this.comments == null ? 0 : this.comments.size();
/* 244 */     if (j > 0) {
/* 245 */       localIIOMetadataNode1 = new IIOMetadataNode("CommentExtensions");
/* 246 */       for (int k = 0; k < j; k++) {
/* 247 */         localObject = new IIOMetadataNode("CommentExtension");
/*     */ 
/* 249 */         arrayOfByte1 = (byte[])this.comments.get(k);
/* 250 */         ((IIOMetadataNode)localObject).setAttribute("value", toISO8859(arrayOfByte1));
/* 251 */         localIIOMetadataNode1.appendChild((Node)localObject);
/*     */       }
/*     */ 
/* 254 */       localIIOMetadataNode2.appendChild(localIIOMetadataNode1);
/*     */     }
/*     */ 
/* 257 */     return localIIOMetadataNode2;
/*     */   }
/*     */ 
/*     */   public IIOMetadataNode getStandardChromaNode() {
/* 261 */     IIOMetadataNode localIIOMetadataNode1 = new IIOMetadataNode("Chroma");
/* 262 */     IIOMetadataNode localIIOMetadataNode2 = null;
/*     */ 
/* 264 */     localIIOMetadataNode2 = new IIOMetadataNode("ColorSpaceType");
/* 265 */     localIIOMetadataNode2.setAttribute("name", "RGB");
/* 266 */     localIIOMetadataNode1.appendChild(localIIOMetadataNode2);
/*     */ 
/* 268 */     localIIOMetadataNode2 = new IIOMetadataNode("NumChannels");
/* 269 */     localIIOMetadataNode2.setAttribute("value", this.transparentColorFlag ? "4" : "3");
/* 270 */     localIIOMetadataNode1.appendChild(localIIOMetadataNode2);
/*     */ 
/* 274 */     localIIOMetadataNode2 = new IIOMetadataNode("BlackIsZero");
/* 275 */     localIIOMetadataNode2.setAttribute("value", "TRUE");
/* 276 */     localIIOMetadataNode1.appendChild(localIIOMetadataNode2);
/*     */ 
/* 278 */     if (this.localColorTable != null) {
/* 279 */       localIIOMetadataNode2 = new IIOMetadataNode("Palette");
/* 280 */       int i = this.localColorTable.length / 3;
/* 281 */       for (int j = 0; j < i; j++) {
/* 282 */         IIOMetadataNode localIIOMetadataNode3 = new IIOMetadataNode("PaletteEntry");
/*     */ 
/* 284 */         localIIOMetadataNode3.setAttribute("index", Integer.toString(j));
/* 285 */         localIIOMetadataNode3.setAttribute("red", Integer.toString(this.localColorTable[(3 * j)] & 0xFF));
/*     */ 
/* 287 */         localIIOMetadataNode3.setAttribute("green", Integer.toString(this.localColorTable[(3 * j + 1)] & 0xFF));
/*     */ 
/* 289 */         localIIOMetadataNode3.setAttribute("blue", Integer.toString(this.localColorTable[(3 * j + 2)] & 0xFF));
/*     */ 
/* 291 */         localIIOMetadataNode2.appendChild(localIIOMetadataNode3);
/*     */       }
/* 293 */       localIIOMetadataNode1.appendChild(localIIOMetadataNode2);
/*     */     }
/*     */ 
/* 299 */     return localIIOMetadataNode1;
/*     */   }
/*     */ 
/*     */   public IIOMetadataNode getStandardCompressionNode() {
/* 303 */     IIOMetadataNode localIIOMetadataNode1 = new IIOMetadataNode("Compression");
/* 304 */     IIOMetadataNode localIIOMetadataNode2 = null;
/*     */ 
/* 306 */     localIIOMetadataNode2 = new IIOMetadataNode("CompressionTypeName");
/* 307 */     localIIOMetadataNode2.setAttribute("value", "lzw");
/* 308 */     localIIOMetadataNode1.appendChild(localIIOMetadataNode2);
/*     */ 
/* 310 */     localIIOMetadataNode2 = new IIOMetadataNode("Lossless");
/* 311 */     localIIOMetadataNode2.setAttribute("value", "TRUE");
/* 312 */     localIIOMetadataNode1.appendChild(localIIOMetadataNode2);
/*     */ 
/* 314 */     localIIOMetadataNode2 = new IIOMetadataNode("NumProgressiveScans");
/* 315 */     localIIOMetadataNode2.setAttribute("value", this.interlaceFlag ? "4" : "1");
/* 316 */     localIIOMetadataNode1.appendChild(localIIOMetadataNode2);
/*     */ 
/* 320 */     return localIIOMetadataNode1;
/*     */   }
/*     */ 
/*     */   public IIOMetadataNode getStandardDataNode() {
/* 324 */     IIOMetadataNode localIIOMetadataNode1 = new IIOMetadataNode("Data");
/* 325 */     IIOMetadataNode localIIOMetadataNode2 = null;
/*     */ 
/* 329 */     localIIOMetadataNode2 = new IIOMetadataNode("SampleFormat");
/* 330 */     localIIOMetadataNode2.setAttribute("value", "Index");
/* 331 */     localIIOMetadataNode1.appendChild(localIIOMetadataNode2);
/*     */ 
/* 337 */     return localIIOMetadataNode1;
/*     */   }
/*     */ 
/*     */   public IIOMetadataNode getStandardDimensionNode() {
/* 341 */     IIOMetadataNode localIIOMetadataNode1 = new IIOMetadataNode("Dimension");
/* 342 */     IIOMetadataNode localIIOMetadataNode2 = null;
/*     */ 
/* 346 */     localIIOMetadataNode2 = new IIOMetadataNode("ImageOrientation");
/* 347 */     localIIOMetadataNode2.setAttribute("value", "Normal");
/* 348 */     localIIOMetadataNode1.appendChild(localIIOMetadataNode2);
/*     */ 
/* 357 */     localIIOMetadataNode2 = new IIOMetadataNode("HorizontalPixelOffset");
/* 358 */     localIIOMetadataNode2.setAttribute("value", Integer.toString(this.imageLeftPosition));
/* 359 */     localIIOMetadataNode1.appendChild(localIIOMetadataNode2);
/*     */ 
/* 361 */     localIIOMetadataNode2 = new IIOMetadataNode("VerticalPixelOffset");
/* 362 */     localIIOMetadataNode2.setAttribute("value", Integer.toString(this.imageTopPosition));
/* 363 */     localIIOMetadataNode1.appendChild(localIIOMetadataNode2);
/*     */ 
/* 368 */     return localIIOMetadataNode1;
/*     */   }
/*     */ 
/*     */   public IIOMetadataNode getStandardTextNode()
/*     */   {
/* 374 */     if (this.comments == null) {
/* 375 */       return null;
/*     */     }
/* 377 */     Iterator localIterator = this.comments.iterator();
/* 378 */     if (!localIterator.hasNext()) {
/* 379 */       return null;
/*     */     }
/*     */ 
/* 382 */     IIOMetadataNode localIIOMetadataNode1 = new IIOMetadataNode("Text");
/* 383 */     IIOMetadataNode localIIOMetadataNode2 = null;
/*     */ 
/* 385 */     while (localIterator.hasNext()) {
/* 386 */       byte[] arrayOfByte = (byte[])localIterator.next();
/* 387 */       String str = null;
/*     */       try {
/* 389 */         str = new String(arrayOfByte, "ISO-8859-1");
/*     */       } catch (UnsupportedEncodingException localUnsupportedEncodingException) {
/* 391 */         throw new RuntimeException("Encoding ISO-8859-1 unknown!");
/*     */       }
/*     */ 
/* 394 */       localIIOMetadataNode2 = new IIOMetadataNode("TextEntry");
/* 395 */       localIIOMetadataNode2.setAttribute("value", str);
/* 396 */       localIIOMetadataNode2.setAttribute("encoding", "ISO-8859-1");
/* 397 */       localIIOMetadataNode2.setAttribute("compression", "none");
/* 398 */       localIIOMetadataNode1.appendChild(localIIOMetadataNode2);
/*     */     }
/*     */ 
/* 401 */     return localIIOMetadataNode1;
/*     */   }
/*     */ 
/*     */   public IIOMetadataNode getStandardTransparencyNode() {
/* 405 */     if (!this.transparentColorFlag) {
/* 406 */       return null;
/*     */     }
/*     */ 
/* 409 */     IIOMetadataNode localIIOMetadataNode1 = new IIOMetadataNode("Transparency");
/*     */ 
/* 411 */     IIOMetadataNode localIIOMetadataNode2 = null;
/*     */ 
/* 415 */     localIIOMetadataNode2 = new IIOMetadataNode("TransparentIndex");
/* 416 */     localIIOMetadataNode2.setAttribute("value", Integer.toString(this.transparentColorIndex));
/*     */ 
/* 418 */     localIIOMetadataNode1.appendChild(localIIOMetadataNode2);
/*     */ 
/* 424 */     return localIIOMetadataNode1;
/*     */   }
/*     */ 
/*     */   public void setFromTree(String paramString, Node paramNode)
/*     */     throws IIOInvalidTreeException
/*     */   {
/* 430 */     throw new IllegalStateException("Metadata is read-only!");
/*     */   }
/*     */ 
/*     */   protected void mergeNativeTree(Node paramNode) throws IIOInvalidTreeException
/*     */   {
/* 435 */     throw new IllegalStateException("Metadata is read-only!");
/*     */   }
/*     */ 
/*     */   protected void mergeStandardTree(Node paramNode) throws IIOInvalidTreeException
/*     */   {
/* 440 */     throw new IllegalStateException("Metadata is read-only!");
/*     */   }
/*     */ 
/*     */   public void reset() {
/* 444 */     throw new IllegalStateException("Metadata is read-only!");
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.imageio.plugins.gif.GIFImageMetadata
 * JD-Core Version:    0.6.2
 */