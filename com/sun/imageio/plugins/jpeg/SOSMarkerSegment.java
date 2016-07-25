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
/*     */ class SOSMarkerSegment extends MarkerSegment
/*     */ {
/*     */   int startSpectralSelection;
/*     */   int endSpectralSelection;
/*     */   int approxHigh;
/*     */   int approxLow;
/*     */   ScanComponentSpec[] componentSpecs;
/*     */ 
/*     */   SOSMarkerSegment(boolean paramBoolean, byte[] paramArrayOfByte, int paramInt)
/*     */   {
/*  52 */     super(218);
/*  53 */     this.startSpectralSelection = 0;
/*  54 */     this.endSpectralSelection = 63;
/*  55 */     this.approxHigh = 0;
/*  56 */     this.approxLow = 0;
/*  57 */     this.componentSpecs = new ScanComponentSpec[paramInt];
/*  58 */     for (int i = 0; i < paramInt; i++) {
/*  59 */       int j = 0;
/*  60 */       if ((paramBoolean) && (
/*  61 */         (i == 1) || (i == 2))) {
/*  62 */         j = 1;
/*     */       }
/*     */ 
/*  65 */       this.componentSpecs[i] = new ScanComponentSpec(paramArrayOfByte[i], j);
/*     */     }
/*     */   }
/*     */ 
/*     */   SOSMarkerSegment(JPEGBuffer paramJPEGBuffer) throws IOException
/*     */   {
/*  71 */     super(paramJPEGBuffer);
/*  72 */     int i = paramJPEGBuffer.buf[(paramJPEGBuffer.bufPtr++)];
/*  73 */     this.componentSpecs = new ScanComponentSpec[i];
/*  74 */     for (int j = 0; j < i; j++) {
/*  75 */       this.componentSpecs[j] = new ScanComponentSpec(paramJPEGBuffer);
/*     */     }
/*  77 */     this.startSpectralSelection = paramJPEGBuffer.buf[(paramJPEGBuffer.bufPtr++)];
/*  78 */     this.endSpectralSelection = paramJPEGBuffer.buf[(paramJPEGBuffer.bufPtr++)];
/*  79 */     this.approxHigh = (paramJPEGBuffer.buf[paramJPEGBuffer.bufPtr] >> 4);
/*  80 */     this.approxLow = (paramJPEGBuffer.buf[(paramJPEGBuffer.bufPtr++)] & 0xF);
/*  81 */     paramJPEGBuffer.bufAvail -= this.length;
/*     */   }
/*     */ 
/*     */   SOSMarkerSegment(Node paramNode) throws IIOInvalidTreeException {
/*  85 */     super(218);
/*  86 */     this.startSpectralSelection = 0;
/*  87 */     this.endSpectralSelection = 63;
/*  88 */     this.approxHigh = 0;
/*  89 */     this.approxLow = 0;
/*  90 */     updateFromNativeNode(paramNode, true);
/*     */   }
/*     */ 
/*     */   protected Object clone() {
/*  94 */     SOSMarkerSegment localSOSMarkerSegment = (SOSMarkerSegment)super.clone();
/*  95 */     if (this.componentSpecs != null) {
/*  96 */       localSOSMarkerSegment.componentSpecs = ((ScanComponentSpec[])this.componentSpecs.clone());
/*     */ 
/*  98 */       for (int i = 0; i < this.componentSpecs.length; i++) {
/*  99 */         localSOSMarkerSegment.componentSpecs[i] = ((ScanComponentSpec)this.componentSpecs[i].clone());
/*     */       }
/*     */     }
/*     */ 
/* 103 */     return localSOSMarkerSegment;
/*     */   }
/*     */ 
/*     */   IIOMetadataNode getNativeNode() {
/* 107 */     IIOMetadataNode localIIOMetadataNode = new IIOMetadataNode("sos");
/* 108 */     localIIOMetadataNode.setAttribute("numScanComponents", Integer.toString(this.componentSpecs.length));
/*     */ 
/* 110 */     localIIOMetadataNode.setAttribute("startSpectralSelection", Integer.toString(this.startSpectralSelection));
/*     */ 
/* 112 */     localIIOMetadataNode.setAttribute("endSpectralSelection", Integer.toString(this.endSpectralSelection));
/*     */ 
/* 114 */     localIIOMetadataNode.setAttribute("approxHigh", Integer.toString(this.approxHigh));
/*     */ 
/* 116 */     localIIOMetadataNode.setAttribute("approxLow", Integer.toString(this.approxLow));
/*     */ 
/* 118 */     for (int i = 0; i < this.componentSpecs.length; i++) {
/* 119 */       localIIOMetadataNode.appendChild(this.componentSpecs[i].getNativeNode());
/*     */     }
/*     */ 
/* 122 */     return localIIOMetadataNode;
/*     */   }
/*     */ 
/*     */   void updateFromNativeNode(Node paramNode, boolean paramBoolean) throws IIOInvalidTreeException
/*     */   {
/* 127 */     NamedNodeMap localNamedNodeMap = paramNode.getAttributes();
/* 128 */     int i = getAttributeValue(paramNode, localNamedNodeMap, "numScanComponents", 1, 4, true);
/*     */ 
/* 130 */     int j = getAttributeValue(paramNode, localNamedNodeMap, "startSpectralSelection", 0, 63, false);
/*     */ 
/* 132 */     this.startSpectralSelection = (j != -1 ? j : this.startSpectralSelection);
/* 133 */     j = getAttributeValue(paramNode, localNamedNodeMap, "endSpectralSelection", 0, 63, false);
/*     */ 
/* 135 */     this.endSpectralSelection = (j != -1 ? j : this.endSpectralSelection);
/* 136 */     j = getAttributeValue(paramNode, localNamedNodeMap, "approxHigh", 0, 15, false);
/* 137 */     this.approxHigh = (j != -1 ? j : this.approxHigh);
/* 138 */     j = getAttributeValue(paramNode, localNamedNodeMap, "approxLow", 0, 15, false);
/* 139 */     this.approxLow = (j != -1 ? j : this.approxLow);
/*     */ 
/* 142 */     NodeList localNodeList = paramNode.getChildNodes();
/* 143 */     if (localNodeList.getLength() != i) {
/* 144 */       throw new IIOInvalidTreeException("numScanComponents must match the number of children", paramNode);
/*     */     }
/*     */ 
/* 147 */     this.componentSpecs = new ScanComponentSpec[i];
/* 148 */     for (int k = 0; k < i; k++)
/* 149 */       this.componentSpecs[k] = new ScanComponentSpec(localNodeList.item(k));
/*     */   }
/*     */ 
/*     */   void write(ImageOutputStream paramImageOutputStream)
/*     */     throws IOException
/*     */   {
/*     */   }
/*     */ 
/*     */   void print()
/*     */   {
/* 162 */     printTag("SOS");
/* 163 */     System.out.print("Start spectral selection: ");
/* 164 */     System.out.println(this.startSpectralSelection);
/* 165 */     System.out.print("End spectral selection: ");
/* 166 */     System.out.println(this.endSpectralSelection);
/* 167 */     System.out.print("Approx high: ");
/* 168 */     System.out.println(this.approxHigh);
/* 169 */     System.out.print("Approx low: ");
/* 170 */     System.out.println(this.approxLow);
/* 171 */     System.out.print("Num scan components: ");
/* 172 */     System.out.println(this.componentSpecs.length);
/* 173 */     for (int i = 0; i < this.componentSpecs.length; i++)
/* 174 */       this.componentSpecs[i].print();
/*     */   }
/*     */ 
/*     */   ScanComponentSpec getScanComponentSpec(byte paramByte, int paramInt)
/*     */   {
/* 179 */     return new ScanComponentSpec(paramByte, paramInt);
/*     */   }
/*     */ 
/*     */   class ScanComponentSpec implements Cloneable
/*     */   {
/*     */     int componentSelector;
/*     */     int dcHuffTable;
/*     */     int acHuffTable;
/*     */ 
/*     */     ScanComponentSpec(byte paramInt, int arg3) {
/* 191 */       this.componentSelector = paramInt;
/*     */       int i;
/* 192 */       this.dcHuffTable = i;
/* 193 */       this.acHuffTable = i;
/*     */     }
/*     */ 
/*     */     ScanComponentSpec(JPEGBuffer arg2)
/*     */     {
/*     */       Object localObject;
/* 198 */       this.componentSelector = localObject.buf[(localObject.bufPtr++)];
/* 199 */       this.dcHuffTable = (localObject.buf[localObject.bufPtr] >> 4);
/* 200 */       this.acHuffTable = (localObject.buf[(localObject.bufPtr++)] & 0xF);
/*     */     }
/*     */ 
/*     */     ScanComponentSpec(Node arg2)
/*     */       throws IIOInvalidTreeException
/*     */     {
/*     */       Node localNode;
/* 204 */       NamedNodeMap localNamedNodeMap = localNode.getAttributes();
/* 205 */       this.componentSelector = MarkerSegment.getAttributeValue(localNode, localNamedNodeMap, "componentSelector", 0, 255, true);
/*     */ 
/* 207 */       this.dcHuffTable = MarkerSegment.getAttributeValue(localNode, localNamedNodeMap, "dcHuffTable", 0, 3, true);
/*     */ 
/* 209 */       this.acHuffTable = MarkerSegment.getAttributeValue(localNode, localNamedNodeMap, "acHuffTable", 0, 3, true);
/*     */     }
/*     */ 
/*     */     protected Object clone()
/*     */     {
/*     */       try {
/* 215 */         return super.clone(); } catch (CloneNotSupportedException localCloneNotSupportedException) {
/*     */       }
/* 217 */       return null;
/*     */     }
/*     */ 
/*     */     IIOMetadataNode getNativeNode() {
/* 221 */       IIOMetadataNode localIIOMetadataNode = new IIOMetadataNode("scanComponentSpec");
/* 222 */       localIIOMetadataNode.setAttribute("componentSelector", Integer.toString(this.componentSelector));
/*     */ 
/* 224 */       localIIOMetadataNode.setAttribute("dcHuffTable", Integer.toString(this.dcHuffTable));
/*     */ 
/* 226 */       localIIOMetadataNode.setAttribute("acHuffTable", Integer.toString(this.acHuffTable));
/*     */ 
/* 228 */       return localIIOMetadataNode;
/*     */     }
/*     */ 
/*     */     void print() {
/* 232 */       System.out.print("Component Selector: ");
/* 233 */       System.out.println(this.componentSelector);
/* 234 */       System.out.print("DC huffman table: ");
/* 235 */       System.out.println(this.dcHuffTable);
/* 236 */       System.out.print("AC huffman table: ");
/* 237 */       System.out.println(this.acHuffTable);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.imageio.plugins.jpeg.SOSMarkerSegment
 * JD-Core Version:    0.6.2
 */