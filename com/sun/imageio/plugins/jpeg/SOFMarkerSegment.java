/*     */ package com.sun.imageio.plugins.jpeg;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import javax.imageio.metadata.IIOInvalidTreeException;
/*     */ import javax.imageio.metadata.IIOMetadataNode;
/*     */ import javax.imageio.stream.ImageOutputStream;
/*     */ import org.w3c.dom.NamedNodeMap;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ 
/*     */ class SOFMarkerSegment extends MarkerSegment
/*     */ {
/*     */   int samplePrecision;
/*     */   int numLines;
/*     */   int samplesPerLine;
/*     */   ComponentSpec[] componentSpecs;
/*     */ 
/*     */   SOFMarkerSegment(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, byte[] paramArrayOfByte, int paramInt)
/*     */   {
/*  53 */     super(paramBoolean2 ? 193 : paramBoolean1 ? 194 : 192);
/*     */ 
/*  56 */     this.samplePrecision = 8;
/*  57 */     this.numLines = 0;
/*  58 */     this.samplesPerLine = 0;
/*  59 */     this.componentSpecs = new ComponentSpec[paramInt];
/*  60 */     for (int i = 0; i < paramInt; i++) {
/*  61 */       int j = 1;
/*  62 */       int k = 0;
/*  63 */       if (paramBoolean3) {
/*  64 */         j = 2;
/*  65 */         if ((i == 1) || (i == 2)) {
/*  66 */           j = 1;
/*  67 */           k = 1;
/*     */         }
/*     */       }
/*  70 */       this.componentSpecs[i] = new ComponentSpec(paramArrayOfByte[i], j, k);
/*     */     }
/*     */   }
/*     */ 
/*     */   SOFMarkerSegment(JPEGBuffer paramJPEGBuffer) throws IOException {
/*  75 */     super(paramJPEGBuffer);
/*  76 */     this.samplePrecision = paramJPEGBuffer.buf[(paramJPEGBuffer.bufPtr++)];
/*  77 */     this.numLines = ((paramJPEGBuffer.buf[(paramJPEGBuffer.bufPtr++)] & 0xFF) << 8);
/*  78 */     this.numLines |= paramJPEGBuffer.buf[(paramJPEGBuffer.bufPtr++)] & 0xFF;
/*  79 */     this.samplesPerLine = ((paramJPEGBuffer.buf[(paramJPEGBuffer.bufPtr++)] & 0xFF) << 8);
/*  80 */     this.samplesPerLine |= paramJPEGBuffer.buf[(paramJPEGBuffer.bufPtr++)] & 0xFF;
/*  81 */     int i = paramJPEGBuffer.buf[(paramJPEGBuffer.bufPtr++)];
/*  82 */     this.componentSpecs = new ComponentSpec[i];
/*  83 */     for (int j = 0; j < i; j++) {
/*  84 */       this.componentSpecs[j] = new ComponentSpec(paramJPEGBuffer);
/*     */     }
/*  86 */     paramJPEGBuffer.bufAvail -= this.length;
/*     */   }
/*     */ 
/*     */   SOFMarkerSegment(Node paramNode) throws IIOInvalidTreeException
/*     */   {
/*  91 */     super(192);
/*  92 */     this.samplePrecision = 8;
/*  93 */     this.numLines = 0;
/*  94 */     this.samplesPerLine = 0;
/*  95 */     updateFromNativeNode(paramNode, true);
/*     */   }
/*     */ 
/*     */   protected Object clone() {
/*  99 */     SOFMarkerSegment localSOFMarkerSegment = (SOFMarkerSegment)super.clone();
/* 100 */     if (this.componentSpecs != null) {
/* 101 */       localSOFMarkerSegment.componentSpecs = ((ComponentSpec[])this.componentSpecs.clone());
/* 102 */       for (int i = 0; i < this.componentSpecs.length; i++) {
/* 103 */         localSOFMarkerSegment.componentSpecs[i] = ((ComponentSpec)this.componentSpecs[i].clone());
/*     */       }
/*     */     }
/*     */ 
/* 107 */     return localSOFMarkerSegment;
/*     */   }
/*     */ 
/*     */   IIOMetadataNode getNativeNode() {
/* 111 */     IIOMetadataNode localIIOMetadataNode = new IIOMetadataNode("sof");
/* 112 */     localIIOMetadataNode.setAttribute("process", Integer.toString(this.tag - 192));
/* 113 */     localIIOMetadataNode.setAttribute("samplePrecision", Integer.toString(this.samplePrecision));
/*     */ 
/* 115 */     localIIOMetadataNode.setAttribute("numLines", Integer.toString(this.numLines));
/*     */ 
/* 117 */     localIIOMetadataNode.setAttribute("samplesPerLine", Integer.toString(this.samplesPerLine));
/*     */ 
/* 119 */     localIIOMetadataNode.setAttribute("numFrameComponents", Integer.toString(this.componentSpecs.length));
/*     */ 
/* 121 */     for (int i = 0; i < this.componentSpecs.length; i++) {
/* 122 */       localIIOMetadataNode.appendChild(this.componentSpecs[i].getNativeNode());
/*     */     }
/*     */ 
/* 125 */     return localIIOMetadataNode;
/*     */   }
/*     */ 
/*     */   void updateFromNativeNode(Node paramNode, boolean paramBoolean) throws IIOInvalidTreeException
/*     */   {
/* 130 */     NamedNodeMap localNamedNodeMap = paramNode.getAttributes();
/* 131 */     int i = getAttributeValue(paramNode, localNamedNodeMap, "process", 0, 2, false);
/* 132 */     this.tag = (i != -1 ? i + 192 : this.tag);
/*     */ 
/* 135 */     i = getAttributeValue(paramNode, localNamedNodeMap, "samplePrecision", 8, 8, false);
/* 136 */     i = getAttributeValue(paramNode, localNamedNodeMap, "numLines", 0, 65535, false);
/* 137 */     this.numLines = (i != -1 ? i : this.numLines);
/* 138 */     i = getAttributeValue(paramNode, localNamedNodeMap, "samplesPerLine", 0, 65535, false);
/* 139 */     this.samplesPerLine = (i != -1 ? i : this.samplesPerLine);
/* 140 */     int j = getAttributeValue(paramNode, localNamedNodeMap, "numFrameComponents", 1, 4, false);
/*     */ 
/* 142 */     NodeList localNodeList = paramNode.getChildNodes();
/* 143 */     if (localNodeList.getLength() != j) {
/* 144 */       throw new IIOInvalidTreeException("numFrameComponents must match number of children", paramNode);
/*     */     }
/*     */ 
/* 147 */     this.componentSpecs = new ComponentSpec[j];
/* 148 */     for (int k = 0; k < j; k++)
/* 149 */       this.componentSpecs[k] = new ComponentSpec(localNodeList.item(k));
/*     */   }
/*     */ 
/*     */   void write(ImageOutputStream paramImageOutputStream)
/*     */     throws IOException
/*     */   {
/*     */   }
/*     */ 
/*     */   void print()
/*     */   {
/* 162 */     printTag("SOF");
/* 163 */     System.out.print("Sample precision: ");
/* 164 */     System.out.println(this.samplePrecision);
/* 165 */     System.out.print("Number of lines: ");
/* 166 */     System.out.println(this.numLines);
/* 167 */     System.out.print("Samples per line: ");
/* 168 */     System.out.println(this.samplesPerLine);
/* 169 */     System.out.print("Number of components: ");
/* 170 */     System.out.println(this.componentSpecs.length);
/* 171 */     for (int i = 0; i < this.componentSpecs.length; i++)
/* 172 */       this.componentSpecs[i].print();
/*     */   }
/*     */ 
/*     */   int getIDencodedCSType()
/*     */   {
/* 177 */     for (int i = 0; i < this.componentSpecs.length; i++) {
/* 178 */       if (this.componentSpecs[i].componentId < 65) {
/* 179 */         return 0;
/*     */       }
/*     */     }
/* 182 */     switch (this.componentSpecs.length) {
/*     */     case 3:
/* 184 */       if ((this.componentSpecs[0].componentId == 82) && (this.componentSpecs[0].componentId == 71) && (this.componentSpecs[0].componentId == 66))
/*     */       {
/* 187 */         return 2;
/*     */       }
/* 189 */       if ((this.componentSpecs[0].componentId == 89) && (this.componentSpecs[0].componentId == 67) && (this.componentSpecs[0].componentId == 99))
/*     */       {
/* 192 */         return 5;
/*     */       }
/*     */       break;
/*     */     case 4:
/* 196 */       if ((this.componentSpecs[0].componentId == 82) && (this.componentSpecs[0].componentId == 71) && (this.componentSpecs[0].componentId == 66) && (this.componentSpecs[0].componentId == 65))
/*     */       {
/* 200 */         return 6;
/*     */       }
/* 202 */       if ((this.componentSpecs[0].componentId == 89) && (this.componentSpecs[0].componentId == 67) && (this.componentSpecs[0].componentId == 99) && (this.componentSpecs[0].componentId == 65))
/*     */       {
/* 206 */         return 10;
/*     */       }
/*     */       break;
/*     */     }
/* 210 */     return 0;
/*     */   }
/*     */ 
/*     */   ComponentSpec getComponentSpec(byte paramByte, int paramInt1, int paramInt2) {
/* 214 */     return new ComponentSpec(paramByte, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   class ComponentSpec implements Cloneable {
/*     */     int componentId;
/*     */     int HsamplingFactor;
/*     */     int VsamplingFactor;
/*     */     int QtableSelector;
/*     */ 
/*     */     ComponentSpec(byte paramInt1, int paramInt2, int arg4) {
/* 227 */       this.componentId = paramInt1;
/* 228 */       this.HsamplingFactor = paramInt2;
/* 229 */       this.VsamplingFactor = paramInt2;
/*     */       int i;
/* 230 */       this.QtableSelector = i;
/*     */     }
/*     */ 
/*     */     ComponentSpec(JPEGBuffer arg2)
/*     */     {
/*     */       Object localObject;
/* 235 */       this.componentId = localObject.buf[(localObject.bufPtr++)];
/* 236 */       this.HsamplingFactor = (localObject.buf[localObject.bufPtr] >>> 4);
/* 237 */       this.VsamplingFactor = (localObject.buf[(localObject.bufPtr++)] & 0xF);
/* 238 */       this.QtableSelector = localObject.buf[(localObject.bufPtr++)];
/*     */     }
/*     */ 
/*     */     ComponentSpec(Node arg2)
/*     */       throws IIOInvalidTreeException
/*     */     {
/*     */       Node localNode;
/* 242 */       NamedNodeMap localNamedNodeMap = localNode.getAttributes();
/* 243 */       this.componentId = MarkerSegment.getAttributeValue(localNode, localNamedNodeMap, "componentId", 0, 255, true);
/* 244 */       this.HsamplingFactor = MarkerSegment.getAttributeValue(localNode, localNamedNodeMap, "HsamplingFactor", 1, 255, true);
/*     */ 
/* 246 */       this.VsamplingFactor = MarkerSegment.getAttributeValue(localNode, localNamedNodeMap, "VsamplingFactor", 1, 255, true);
/*     */ 
/* 248 */       this.QtableSelector = MarkerSegment.getAttributeValue(localNode, localNamedNodeMap, "QtableSelector", 0, 3, true);
/*     */     }
/*     */ 
/*     */     protected Object clone()
/*     */     {
/*     */       try {
/* 254 */         return super.clone(); } catch (CloneNotSupportedException localCloneNotSupportedException) {
/*     */       }
/* 256 */       return null;
/*     */     }
/*     */ 
/*     */     IIOMetadataNode getNativeNode() {
/* 260 */       IIOMetadataNode localIIOMetadataNode = new IIOMetadataNode("componentSpec");
/* 261 */       localIIOMetadataNode.setAttribute("componentId", Integer.toString(this.componentId));
/*     */ 
/* 263 */       localIIOMetadataNode.setAttribute("HsamplingFactor", Integer.toString(this.HsamplingFactor));
/*     */ 
/* 265 */       localIIOMetadataNode.setAttribute("VsamplingFactor", Integer.toString(this.VsamplingFactor));
/*     */ 
/* 267 */       localIIOMetadataNode.setAttribute("QtableSelector", Integer.toString(this.QtableSelector));
/*     */ 
/* 269 */       return localIIOMetadataNode;
/*     */     }
/*     */ 
/*     */     void print() {
/* 273 */       System.out.print("Component ID: ");
/* 274 */       System.out.println(this.componentId);
/* 275 */       System.out.print("H sampling factor: ");
/* 276 */       System.out.println(this.HsamplingFactor);
/* 277 */       System.out.print("V sampling factor: ");
/* 278 */       System.out.println(this.VsamplingFactor);
/* 279 */       System.out.print("Q table selector: ");
/* 280 */       System.out.println(this.QtableSelector);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.imageio.plugins.jpeg.SOFMarkerSegment
 * JD-Core Version:    0.6.2
 */