/*     */ package com.sun.imageio.plugins.gif;
/*     */ 
/*     */ import javax.imageio.metadata.IIOInvalidTreeException;
/*     */ import javax.imageio.metadata.IIOMetadataNode;
/*     */ import org.w3c.dom.Node;
/*     */ 
/*     */ public class GIFStreamMetadata extends GIFMetadata
/*     */ {
/*     */   static final String nativeMetadataFormatName = "javax_imageio_gif_stream_1.0";
/*  44 */   public static final String[] versionStrings = { "87a", "89a" };
/*     */   public String version;
/*     */   public int logicalScreenWidth;
/*     */   public int logicalScreenHeight;
/*     */   public int colorResolution;
/*     */   public int pixelAspectRatio;
/*     */   public int backgroundColorIndex;
/*     */   public boolean sortFlag;
/*  55 */   public static final String[] colorTableSizes = { "2", "4", "8", "16", "32", "64", "128", "256" };
/*     */ 
/*  60 */   public byte[] globalColorTable = null;
/*     */ 
/*     */   protected GIFStreamMetadata(boolean paramBoolean, String paramString1, String paramString2, String[] paramArrayOfString1, String[] paramArrayOfString2)
/*     */   {
/*  68 */     super(paramBoolean, paramString1, paramString2, paramArrayOfString1, paramArrayOfString2);
/*     */   }
/*     */ 
/*     */   public GIFStreamMetadata()
/*     */   {
/*  76 */     this(true, "javax_imageio_gif_stream_1.0", "com.sun.imageio.plugins.gif.GIFStreamMetadataFormat", null, null);
/*     */   }
/*     */ 
/*     */   public boolean isReadOnly()
/*     */   {
/*  84 */     return true;
/*     */   }
/*     */ 
/*     */   public Node getAsTree(String paramString) {
/*  88 */     if (paramString.equals("javax_imageio_gif_stream_1.0"))
/*  89 */       return getNativeTree();
/*  90 */     if (paramString.equals("javax_imageio_1.0"))
/*     */     {
/*  92 */       return getStandardTree();
/*     */     }
/*  94 */     throw new IllegalArgumentException("Not a recognized format!");
/*     */   }
/*     */ 
/*     */   private Node getNativeTree()
/*     */   {
/* 100 */     IIOMetadataNode localIIOMetadataNode2 = new IIOMetadataNode("javax_imageio_gif_stream_1.0");
/*     */ 
/* 103 */     IIOMetadataNode localIIOMetadataNode1 = new IIOMetadataNode("Version");
/* 104 */     localIIOMetadataNode1.setAttribute("value", this.version);
/* 105 */     localIIOMetadataNode2.appendChild(localIIOMetadataNode1);
/*     */ 
/* 108 */     localIIOMetadataNode1 = new IIOMetadataNode("LogicalScreenDescriptor");
/*     */ 
/* 113 */     localIIOMetadataNode1.setAttribute("logicalScreenWidth", this.logicalScreenWidth == -1 ? "" : Integer.toString(this.logicalScreenWidth));
/*     */ 
/* 116 */     localIIOMetadataNode1.setAttribute("logicalScreenHeight", this.logicalScreenHeight == -1 ? "" : Integer.toString(this.logicalScreenHeight));
/*     */ 
/* 120 */     localIIOMetadataNode1.setAttribute("colorResolution", this.colorResolution == -1 ? "" : Integer.toString(this.colorResolution));
/*     */ 
/* 123 */     localIIOMetadataNode1.setAttribute("pixelAspectRatio", Integer.toString(this.pixelAspectRatio));
/*     */ 
/* 125 */     localIIOMetadataNode2.appendChild(localIIOMetadataNode1);
/*     */ 
/* 127 */     if (this.globalColorTable != null) {
/* 128 */       localIIOMetadataNode1 = new IIOMetadataNode("GlobalColorTable");
/* 129 */       int i = this.globalColorTable.length / 3;
/* 130 */       localIIOMetadataNode1.setAttribute("sizeOfGlobalColorTable", Integer.toString(i));
/*     */ 
/* 132 */       localIIOMetadataNode1.setAttribute("backgroundColorIndex", Integer.toString(this.backgroundColorIndex));
/*     */ 
/* 134 */       localIIOMetadataNode1.setAttribute("sortFlag", this.sortFlag ? "TRUE" : "FALSE");
/*     */ 
/* 137 */       for (int j = 0; j < i; j++) {
/* 138 */         IIOMetadataNode localIIOMetadataNode3 = new IIOMetadataNode("ColorTableEntry");
/*     */ 
/* 140 */         localIIOMetadataNode3.setAttribute("index", Integer.toString(j));
/* 141 */         int k = this.globalColorTable[(3 * j)] & 0xFF;
/* 142 */         int m = this.globalColorTable[(3 * j + 1)] & 0xFF;
/* 143 */         int n = this.globalColorTable[(3 * j + 2)] & 0xFF;
/* 144 */         localIIOMetadataNode3.setAttribute("red", Integer.toString(k));
/* 145 */         localIIOMetadataNode3.setAttribute("green", Integer.toString(m));
/* 146 */         localIIOMetadataNode3.setAttribute("blue", Integer.toString(n));
/* 147 */         localIIOMetadataNode1.appendChild(localIIOMetadataNode3);
/*     */       }
/* 149 */       localIIOMetadataNode2.appendChild(localIIOMetadataNode1);
/*     */     }
/*     */ 
/* 152 */     return localIIOMetadataNode2;
/*     */   }
/*     */ 
/*     */   public IIOMetadataNode getStandardChromaNode() {
/* 156 */     IIOMetadataNode localIIOMetadataNode1 = new IIOMetadataNode("Chroma");
/* 157 */     IIOMetadataNode localIIOMetadataNode2 = null;
/*     */ 
/* 159 */     localIIOMetadataNode2 = new IIOMetadataNode("ColorSpaceType");
/* 160 */     localIIOMetadataNode2.setAttribute("name", "RGB");
/* 161 */     localIIOMetadataNode1.appendChild(localIIOMetadataNode2);
/*     */ 
/* 163 */     localIIOMetadataNode2 = new IIOMetadataNode("BlackIsZero");
/* 164 */     localIIOMetadataNode2.setAttribute("value", "TRUE");
/* 165 */     localIIOMetadataNode1.appendChild(localIIOMetadataNode2);
/*     */ 
/* 170 */     if (this.globalColorTable != null) {
/* 171 */       localIIOMetadataNode2 = new IIOMetadataNode("Palette");
/* 172 */       int i = this.globalColorTable.length / 3;
/* 173 */       for (int j = 0; j < i; j++) {
/* 174 */         IIOMetadataNode localIIOMetadataNode3 = new IIOMetadataNode("PaletteEntry");
/*     */ 
/* 176 */         localIIOMetadataNode3.setAttribute("index", Integer.toString(j));
/* 177 */         localIIOMetadataNode3.setAttribute("red", Integer.toString(this.globalColorTable[(3 * j)] & 0xFF));
/*     */ 
/* 179 */         localIIOMetadataNode3.setAttribute("green", Integer.toString(this.globalColorTable[(3 * j + 1)] & 0xFF));
/*     */ 
/* 181 */         localIIOMetadataNode3.setAttribute("blue", Integer.toString(this.globalColorTable[(3 * j + 2)] & 0xFF));
/*     */ 
/* 183 */         localIIOMetadataNode2.appendChild(localIIOMetadataNode3);
/*     */       }
/* 185 */       localIIOMetadataNode1.appendChild(localIIOMetadataNode2);
/*     */ 
/* 188 */       localIIOMetadataNode2 = new IIOMetadataNode("BackgroundIndex");
/* 189 */       localIIOMetadataNode2.setAttribute("value", Integer.toString(this.backgroundColorIndex));
/* 190 */       localIIOMetadataNode1.appendChild(localIIOMetadataNode2);
/*     */     }
/*     */ 
/* 193 */     return localIIOMetadataNode1;
/*     */   }
/*     */ 
/*     */   public IIOMetadataNode getStandardCompressionNode() {
/* 197 */     IIOMetadataNode localIIOMetadataNode1 = new IIOMetadataNode("Compression");
/* 198 */     IIOMetadataNode localIIOMetadataNode2 = null;
/*     */ 
/* 200 */     localIIOMetadataNode2 = new IIOMetadataNode("CompressionTypeName");
/* 201 */     localIIOMetadataNode2.setAttribute("value", "lzw");
/* 202 */     localIIOMetadataNode1.appendChild(localIIOMetadataNode2);
/*     */ 
/* 204 */     localIIOMetadataNode2 = new IIOMetadataNode("Lossless");
/* 205 */     localIIOMetadataNode2.setAttribute("value", "TRUE");
/* 206 */     localIIOMetadataNode1.appendChild(localIIOMetadataNode2);
/*     */ 
/* 211 */     return localIIOMetadataNode1;
/*     */   }
/*     */ 
/*     */   public IIOMetadataNode getStandardDataNode() {
/* 215 */     IIOMetadataNode localIIOMetadataNode1 = new IIOMetadataNode("Data");
/* 216 */     IIOMetadataNode localIIOMetadataNode2 = null;
/*     */ 
/* 220 */     localIIOMetadataNode2 = new IIOMetadataNode("SampleFormat");
/* 221 */     localIIOMetadataNode2.setAttribute("value", "Index");
/* 222 */     localIIOMetadataNode1.appendChild(localIIOMetadataNode2);
/*     */ 
/* 224 */     localIIOMetadataNode2 = new IIOMetadataNode("BitsPerSample");
/* 225 */     localIIOMetadataNode2.setAttribute("value", this.colorResolution == -1 ? "" : Integer.toString(this.colorResolution));
/*     */ 
/* 228 */     localIIOMetadataNode1.appendChild(localIIOMetadataNode2);
/*     */ 
/* 233 */     return localIIOMetadataNode1;
/*     */   }
/*     */ 
/*     */   public IIOMetadataNode getStandardDimensionNode() {
/* 237 */     IIOMetadataNode localIIOMetadataNode1 = new IIOMetadataNode("Dimension");
/* 238 */     IIOMetadataNode localIIOMetadataNode2 = null;
/*     */ 
/* 240 */     localIIOMetadataNode2 = new IIOMetadataNode("PixelAspectRatio");
/* 241 */     float f = 1.0F;
/* 242 */     if (this.pixelAspectRatio != 0) {
/* 243 */       f = (this.pixelAspectRatio + 15) / 64.0F;
/*     */     }
/* 245 */     localIIOMetadataNode2.setAttribute("value", Float.toString(f));
/* 246 */     localIIOMetadataNode1.appendChild(localIIOMetadataNode2);
/*     */ 
/* 248 */     localIIOMetadataNode2 = new IIOMetadataNode("ImageOrientation");
/* 249 */     localIIOMetadataNode2.setAttribute("value", "Normal");
/* 250 */     localIIOMetadataNode1.appendChild(localIIOMetadataNode2);
/*     */ 
/* 261 */     localIIOMetadataNode2 = new IIOMetadataNode("HorizontalScreenSize");
/* 262 */     localIIOMetadataNode2.setAttribute("value", this.logicalScreenWidth == -1 ? "" : Integer.toString(this.logicalScreenWidth));
/*     */ 
/* 265 */     localIIOMetadataNode1.appendChild(localIIOMetadataNode2);
/*     */ 
/* 267 */     localIIOMetadataNode2 = new IIOMetadataNode("VerticalScreenSize");
/* 268 */     localIIOMetadataNode2.setAttribute("value", this.logicalScreenHeight == -1 ? "" : Integer.toString(this.logicalScreenHeight));
/*     */ 
/* 271 */     localIIOMetadataNode1.appendChild(localIIOMetadataNode2);
/*     */ 
/* 273 */     return localIIOMetadataNode1;
/*     */   }
/*     */ 
/*     */   public IIOMetadataNode getStandardDocumentNode() {
/* 277 */     IIOMetadataNode localIIOMetadataNode1 = new IIOMetadataNode("Document");
/* 278 */     IIOMetadataNode localIIOMetadataNode2 = null;
/*     */ 
/* 280 */     localIIOMetadataNode2 = new IIOMetadataNode("FormatVersion");
/* 281 */     localIIOMetadataNode2.setAttribute("value", this.version);
/* 282 */     localIIOMetadataNode1.appendChild(localIIOMetadataNode2);
/*     */ 
/* 288 */     return localIIOMetadataNode1;
/*     */   }
/*     */ 
/*     */   public IIOMetadataNode getStandardTextNode()
/*     */   {
/* 293 */     return null;
/*     */   }
/*     */ 
/*     */   public IIOMetadataNode getStandardTransparencyNode()
/*     */   {
/* 298 */     return null;
/*     */   }
/*     */ 
/*     */   public void setFromTree(String paramString, Node paramNode)
/*     */     throws IIOInvalidTreeException
/*     */   {
/* 304 */     throw new IllegalStateException("Metadata is read-only!");
/*     */   }
/*     */ 
/*     */   protected void mergeNativeTree(Node paramNode) throws IIOInvalidTreeException
/*     */   {
/* 309 */     throw new IllegalStateException("Metadata is read-only!");
/*     */   }
/*     */ 
/*     */   protected void mergeStandardTree(Node paramNode) throws IIOInvalidTreeException
/*     */   {
/* 314 */     throw new IllegalStateException("Metadata is read-only!");
/*     */   }
/*     */ 
/*     */   public void reset() {
/* 318 */     throw new IllegalStateException("Metadata is read-only!");
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.imageio.plugins.gif.GIFStreamMetadata
 * JD-Core Version:    0.6.2
 */