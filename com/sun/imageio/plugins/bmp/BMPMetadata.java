/*     */ package com.sun.imageio.plugins.bmp;
/*     */ 
/*     */ import com.sun.imageio.plugins.common.I18N;
/*     */ import com.sun.imageio.plugins.common.ImageUtil;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.util.List;
/*     */ import javax.imageio.metadata.IIOMetadata;
/*     */ import javax.imageio.metadata.IIOMetadataNode;
/*     */ import org.w3c.dom.Node;
/*     */ 
/*     */ public class BMPMetadata extends IIOMetadata
/*     */   implements BMPConstants
/*     */ {
/*     */   public static final String nativeMetadataFormatName = "javax_imageio_bmp_1.0";
/*     */   public String bmpVersion;
/*     */   public int width;
/*     */   public int height;
/*     */   public short bitsPerPixel;
/*     */   public int compression;
/*     */   public int imageSize;
/*     */   public int xPixelsPerMeter;
/*     */   public int yPixelsPerMeter;
/*     */   public int colorsUsed;
/*     */   public int colorsImportant;
/*     */   public int redMask;
/*     */   public int greenMask;
/*     */   public int blueMask;
/*     */   public int alphaMask;
/*     */   public int colorSpace;
/*     */   public double redX;
/*     */   public double redY;
/*     */   public double redZ;
/*     */   public double greenX;
/*     */   public double greenY;
/*     */   public double greenZ;
/*     */   public double blueX;
/*     */   public double blueY;
/*     */   public double blueZ;
/*     */   public int gammaRed;
/*     */   public int gammaGreen;
/*     */   public int gammaBlue;
/*     */   public int intent;
/*  88 */   public byte[] palette = null;
/*     */   public int paletteSize;
/*     */   public int red;
/*     */   public int green;
/*     */   public int blue;
/*  96 */   public List comments = null;
/*     */ 
/*     */   public BMPMetadata() {
/*  99 */     super(true, "javax_imageio_bmp_1.0", "com.sun.imageio.plugins.bmp.BMPMetadataFormat", null, null);
/*     */   }
/*     */ 
/*     */   public boolean isReadOnly()
/*     */   {
/* 106 */     return true;
/*     */   }
/*     */ 
/*     */   public Node getAsTree(String paramString) {
/* 110 */     if (paramString.equals("javax_imageio_bmp_1.0"))
/* 111 */       return getNativeTree();
/* 112 */     if (paramString.equals("javax_imageio_1.0"))
/*     */     {
/* 114 */       return getStandardTree();
/*     */     }
/* 116 */     throw new IllegalArgumentException(I18N.getString("BMPMetadata0"));
/*     */   }
/*     */ 
/*     */   private String toISO8859(byte[] paramArrayOfByte)
/*     */   {
/*     */     try {
/* 122 */       return new String(paramArrayOfByte, "ISO-8859-1"); } catch (UnsupportedEncodingException localUnsupportedEncodingException) {
/*     */     }
/* 124 */     return "";
/*     */   }
/*     */ 
/*     */   private Node getNativeTree()
/*     */   {
/* 129 */     IIOMetadataNode localIIOMetadataNode1 = new IIOMetadataNode("javax_imageio_bmp_1.0");
/*     */ 
/* 132 */     addChildNode(localIIOMetadataNode1, "BMPVersion", this.bmpVersion);
/* 133 */     addChildNode(localIIOMetadataNode1, "Width", new Integer(this.width));
/* 134 */     addChildNode(localIIOMetadataNode1, "Height", new Integer(this.height));
/* 135 */     addChildNode(localIIOMetadataNode1, "BitsPerPixel", new Short(this.bitsPerPixel));
/* 136 */     addChildNode(localIIOMetadataNode1, "Compression", new Integer(this.compression));
/* 137 */     addChildNode(localIIOMetadataNode1, "ImageSize", new Integer(this.imageSize));
/*     */ 
/* 139 */     IIOMetadataNode localIIOMetadataNode2 = addChildNode(localIIOMetadataNode1, "PixelsPerMeter", null);
/* 140 */     addChildNode(localIIOMetadataNode2, "X", new Integer(this.xPixelsPerMeter));
/* 141 */     addChildNode(localIIOMetadataNode2, "Y", new Integer(this.yPixelsPerMeter));
/*     */ 
/* 143 */     addChildNode(localIIOMetadataNode1, "ColorsUsed", new Integer(this.colorsUsed));
/* 144 */     addChildNode(localIIOMetadataNode1, "ColorsImportant", new Integer(this.colorsImportant));
/*     */ 
/* 146 */     int i = 0;
/* 147 */     for (int j = 0; j < this.bmpVersion.length(); j++) {
/* 148 */       if (Character.isDigit(this.bmpVersion.charAt(j)))
/* 149 */         i = this.bmpVersion.charAt(j) - '0';
/*     */     }
/* 151 */     if (i >= 4) {
/* 152 */       localIIOMetadataNode2 = addChildNode(localIIOMetadataNode1, "Mask", null);
/* 153 */       addChildNode(localIIOMetadataNode2, "Red", new Integer(this.redMask));
/* 154 */       addChildNode(localIIOMetadataNode2, "Green", new Integer(this.greenMask));
/* 155 */       addChildNode(localIIOMetadataNode2, "Blue", new Integer(this.blueMask));
/* 156 */       addChildNode(localIIOMetadataNode2, "Alpha", new Integer(this.alphaMask));
/*     */ 
/* 158 */       addChildNode(localIIOMetadataNode1, "ColorSpaceType", new Integer(this.colorSpace));
/*     */ 
/* 160 */       localIIOMetadataNode2 = addChildNode(localIIOMetadataNode1, "CIEXYZEndPoints", null);
/* 161 */       addXYZPoints(localIIOMetadataNode2, "Red", this.redX, this.redY, this.redZ);
/* 162 */       addXYZPoints(localIIOMetadataNode2, "Green", this.greenX, this.greenY, this.greenZ);
/* 163 */       addXYZPoints(localIIOMetadataNode2, "Blue", this.blueX, this.blueY, this.blueZ);
/*     */ 
/* 165 */       localIIOMetadataNode2 = addChildNode(localIIOMetadataNode1, "Intent", new Integer(this.intent));
/*     */     }
/*     */ 
/* 169 */     if ((this.palette != null) && (this.paletteSize > 0)) {
/* 170 */       localIIOMetadataNode2 = addChildNode(localIIOMetadataNode1, "Palette", null);
/* 171 */       j = this.palette.length / this.paletteSize;
/*     */ 
/* 173 */       int k = 0; for (int m = 0; k < this.paletteSize; k++) {
/* 174 */         IIOMetadataNode localIIOMetadataNode3 = addChildNode(localIIOMetadataNode2, "PaletteEntry", null);
/*     */ 
/* 176 */         this.red = (this.palette[(m++)] & 0xFF);
/* 177 */         this.green = (this.palette[(m++)] & 0xFF);
/* 178 */         this.blue = (this.palette[(m++)] & 0xFF);
/* 179 */         addChildNode(localIIOMetadataNode3, "Red", new Byte((byte)this.red));
/* 180 */         addChildNode(localIIOMetadataNode3, "Green", new Byte((byte)this.green));
/* 181 */         addChildNode(localIIOMetadataNode3, "Blue", new Byte((byte)this.blue));
/* 182 */         if (j == 4) {
/* 183 */           addChildNode(localIIOMetadataNode3, "Alpha", new Byte((byte)(this.palette[(m++)] & 0xFF)));
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 188 */     return localIIOMetadataNode1;
/*     */   }
/*     */ 
/*     */   protected IIOMetadataNode getStandardChromaNode()
/*     */   {
/* 194 */     if ((this.palette != null) && (this.paletteSize > 0)) {
/* 195 */       IIOMetadataNode localIIOMetadataNode1 = new IIOMetadataNode("Chroma");
/* 196 */       IIOMetadataNode localIIOMetadataNode2 = new IIOMetadataNode("Palette");
/* 197 */       int i = this.palette.length / this.paletteSize;
/* 198 */       localIIOMetadataNode2.setAttribute("value", "" + i);
/*     */ 
/* 200 */       int j = 0; for (int k = 0; j < this.paletteSize; j++) {
/* 201 */         IIOMetadataNode localIIOMetadataNode3 = new IIOMetadataNode("PaletteEntry");
/* 202 */         localIIOMetadataNode3.setAttribute("index", "" + j);
/* 203 */         localIIOMetadataNode3.setAttribute("red", "" + this.palette[(k++)]);
/* 204 */         localIIOMetadataNode3.setAttribute("green", "" + this.palette[(k++)]);
/* 205 */         localIIOMetadataNode3.setAttribute("blue", "" + this.palette[(k++)]);
/* 206 */         if ((i == 4) && (this.palette[k] != 0))
/* 207 */           localIIOMetadataNode3.setAttribute("alpha", "" + this.palette[(k++)]);
/* 208 */         localIIOMetadataNode2.appendChild(localIIOMetadataNode3);
/*     */       }
/* 210 */       localIIOMetadataNode1.appendChild(localIIOMetadataNode2);
/* 211 */       return localIIOMetadataNode1;
/*     */     }
/*     */ 
/* 214 */     return null;
/*     */   }
/*     */ 
/*     */   protected IIOMetadataNode getStandardCompressionNode() {
/* 218 */     IIOMetadataNode localIIOMetadataNode1 = new IIOMetadataNode("Compression");
/*     */ 
/* 221 */     IIOMetadataNode localIIOMetadataNode2 = new IIOMetadataNode("CompressionTypeName");
/* 222 */     localIIOMetadataNode2.setAttribute("value", compressionTypeNames[this.compression]);
/* 223 */     localIIOMetadataNode1.appendChild(localIIOMetadataNode2);
/* 224 */     return localIIOMetadataNode1;
/*     */   }
/*     */ 
/*     */   protected IIOMetadataNode getStandardDataNode() {
/* 228 */     IIOMetadataNode localIIOMetadataNode1 = new IIOMetadataNode("Data");
/*     */ 
/* 230 */     String str = "";
/* 231 */     if (this.bitsPerPixel == 24)
/* 232 */       str = "8 8 8 ";
/* 233 */     else if ((this.bitsPerPixel == 16) || (this.bitsPerPixel == 32)) {
/* 234 */       str = "" + countBits(this.redMask) + " " + countBits(this.greenMask) + countBits(this.blueMask) + "" + countBits(this.alphaMask);
/*     */     }
/*     */ 
/* 238 */     IIOMetadataNode localIIOMetadataNode2 = new IIOMetadataNode("BitsPerSample");
/* 239 */     localIIOMetadataNode2.setAttribute("value", str);
/* 240 */     localIIOMetadataNode1.appendChild(localIIOMetadataNode2);
/*     */ 
/* 242 */     return localIIOMetadataNode1;
/*     */   }
/*     */ 
/*     */   protected IIOMetadataNode getStandardDimensionNode() {
/* 246 */     if ((this.yPixelsPerMeter > 0.0F) && (this.xPixelsPerMeter > 0.0F)) {
/* 247 */       IIOMetadataNode localIIOMetadataNode1 = new IIOMetadataNode("Dimension");
/* 248 */       float f = this.yPixelsPerMeter / this.xPixelsPerMeter;
/* 249 */       IIOMetadataNode localIIOMetadataNode2 = new IIOMetadataNode("PixelAspectRatio");
/* 250 */       localIIOMetadataNode2.setAttribute("value", "" + f);
/* 251 */       localIIOMetadataNode1.appendChild(localIIOMetadataNode2);
/*     */ 
/* 253 */       localIIOMetadataNode2 = new IIOMetadataNode("HorizontalPhysicalPixelSpacing");
/* 254 */       localIIOMetadataNode2.setAttribute("value", "" + 1 / this.xPixelsPerMeter * 1000);
/* 255 */       localIIOMetadataNode1.appendChild(localIIOMetadataNode2);
/*     */ 
/* 257 */       localIIOMetadataNode2 = new IIOMetadataNode("VerticalPhysicalPixelSpacing");
/* 258 */       localIIOMetadataNode2.setAttribute("value", "" + 1 / this.yPixelsPerMeter * 1000);
/* 259 */       localIIOMetadataNode1.appendChild(localIIOMetadataNode2);
/*     */ 
/* 261 */       return localIIOMetadataNode1;
/*     */     }
/* 263 */     return null;
/*     */   }
/*     */ 
/*     */   public void setFromTree(String paramString, Node paramNode) {
/* 267 */     throw new IllegalStateException(I18N.getString("BMPMetadata1"));
/*     */   }
/*     */ 
/*     */   public void mergeTree(String paramString, Node paramNode) {
/* 271 */     throw new IllegalStateException(I18N.getString("BMPMetadata1"));
/*     */   }
/*     */ 
/*     */   public void reset() {
/* 275 */     throw new IllegalStateException(I18N.getString("BMPMetadata1"));
/*     */   }
/*     */ 
/*     */   private String countBits(int paramInt) {
/* 279 */     int i = 0;
/* 280 */     while (paramInt > 0) {
/* 281 */       if ((paramInt & 0x1) == 1)
/* 282 */         i++;
/* 283 */       paramInt >>>= 1;
/*     */     }
/*     */ 
/* 286 */     return "" + i;
/*     */   }
/*     */ 
/*     */   private void addXYZPoints(IIOMetadataNode paramIIOMetadataNode, String paramString, double paramDouble1, double paramDouble2, double paramDouble3) {
/* 290 */     IIOMetadataNode localIIOMetadataNode = addChildNode(paramIIOMetadataNode, paramString, null);
/* 291 */     addChildNode(localIIOMetadataNode, "X", new Double(paramDouble1));
/* 292 */     addChildNode(localIIOMetadataNode, "Y", new Double(paramDouble2));
/* 293 */     addChildNode(localIIOMetadataNode, "Z", new Double(paramDouble3));
/*     */   }
/*     */ 
/*     */   private IIOMetadataNode addChildNode(IIOMetadataNode paramIIOMetadataNode, String paramString, Object paramObject)
/*     */   {
/* 299 */     IIOMetadataNode localIIOMetadataNode = new IIOMetadataNode(paramString);
/* 300 */     if (paramObject != null) {
/* 301 */       localIIOMetadataNode.setUserObject(paramObject);
/* 302 */       localIIOMetadataNode.setNodeValue(ImageUtil.convertObjectToString(paramObject));
/*     */     }
/* 304 */     paramIIOMetadataNode.appendChild(localIIOMetadataNode);
/* 305 */     return localIIOMetadataNode;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.imageio.plugins.bmp.BMPMetadata
 * JD-Core Version:    0.6.2
 */