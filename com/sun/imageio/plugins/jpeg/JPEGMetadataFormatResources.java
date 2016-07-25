/*    */ package com.sun.imageio.plugins.jpeg;
/*    */ 
/*    */ import java.util.ListResourceBundle;
/*    */ 
/*    */ abstract class JPEGMetadataFormatResources extends ListResourceBundle
/*    */ {
/* 33 */   static final Object[][] commonContents = { { "dqt", "A Define Quantization Table(s) marker segment" }, { "dqtable", "A single quantization table" }, { "dht", "A Define Huffman Table(s) marker segment" }, { "dhtable", "A single Huffman table" }, { "dri", "A Define Restart Interval marker segment" }, { "com", "A Comment marker segment.  The user object contains the actual bytes." }, { "unknown", "An unrecognized marker segment.  The user object contains the data not including length." }, { "dqtable/elementPrecision", "The number of bits in each table element (0 = 8, 1 = 16)" }, { "dgtable/qtableId", "The table id" }, { "dhtable/class", "Indicates whether this is a DC (0) or an AC (1) table" }, { "dhtable/htableId", "The table id" }, { "dri/interval", "The restart interval in MCUs" }, { "com/comment", "The comment as a string (used only if user object is null)" }, { "unknown/MarkerTag", "The tag identifying this marker segment" } };
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.imageio.plugins.jpeg.JPEGMetadataFormatResources
 * JD-Core Version:    0.6.2
 */