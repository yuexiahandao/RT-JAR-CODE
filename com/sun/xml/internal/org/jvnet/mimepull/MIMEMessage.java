/*     */ package com.sun.xml.internal.org.jvnet.mimepull;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.net.URLDecoder;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ public class MIMEMessage
/*     */ {
/*  43 */   private static final Logger LOGGER = Logger.getLogger(MIMEMessage.class.getName());
/*     */   MIMEConfig config;
/*     */   private final InputStream in;
/*     */   private final List<MIMEPart> partsList;
/*     */   private final Map<String, MIMEPart> partsMap;
/*     */   private final Iterator<MIMEEvent> it;
/*     */   private boolean parsed;
/*     */   private MIMEPart currentPart;
/*     */   private int currentIndex;
/*     */ 
/*     */   public MIMEMessage(InputStream in, String boundary)
/*     */   {
/*  59 */     this(in, boundary, new MIMEConfig());
/*     */   }
/*     */ 
/*     */   public MIMEMessage(InputStream in, String boundary, MIMEConfig config)
/*     */   {
/*  71 */     this.in = in;
/*  72 */     this.config = config;
/*  73 */     MIMEParser parser = new MIMEParser(in, boundary, config);
/*  74 */     this.it = parser.iterator();
/*     */ 
/*  76 */     this.partsList = new ArrayList();
/*  77 */     this.partsMap = new HashMap();
/*  78 */     if (config.isParseEagerly())
/*  79 */       parseAll();
/*     */   }
/*     */ 
/*     */   public List<MIMEPart> getAttachments()
/*     */   {
/*  90 */     if (!this.parsed) {
/*  91 */       parseAll();
/*     */     }
/*  93 */     return this.partsList;
/*     */   }
/*     */ 
/*     */   public MIMEPart getPart(int index)
/*     */   {
/* 107 */     LOGGER.fine("index=" + index);
/* 108 */     MIMEPart part = index < this.partsList.size() ? (MIMEPart)this.partsList.get(index) : null;
/* 109 */     if ((this.parsed) && (part == null)) {
/* 110 */       throw new MIMEParsingException("There is no " + index + " attachment part ");
/*     */     }
/* 112 */     if (part == null)
/*     */     {
/* 114 */       part = new MIMEPart(this);
/* 115 */       this.partsList.add(index, part);
/*     */     }
/* 117 */     LOGGER.fine("Got attachment at index=" + index + " attachment=" + part);
/* 118 */     return part;
/*     */   }
/*     */ 
/*     */   public MIMEPart getPart(String contentId)
/*     */   {
/* 131 */     LOGGER.fine("Content-ID=" + contentId);
/* 132 */     MIMEPart part = getDecodedCidPart(contentId);
/* 133 */     if ((this.parsed) && (part == null)) {
/* 134 */       throw new MIMEParsingException("There is no attachment part with Content-ID = " + contentId);
/*     */     }
/* 136 */     if (part == null)
/*     */     {
/* 138 */       part = new MIMEPart(this, contentId);
/* 139 */       this.partsMap.put(contentId, part);
/*     */     }
/* 141 */     LOGGER.fine("Got attachment for Content-ID=" + contentId + " attachment=" + part);
/* 142 */     return part;
/*     */   }
/*     */ 
/*     */   private MIMEPart getDecodedCidPart(String cid)
/*     */   {
/* 147 */     MIMEPart part = (MIMEPart)this.partsMap.get(cid);
/* 148 */     if ((part == null) && 
/* 149 */       (cid.indexOf('%') != -1)) {
/*     */       try {
/* 151 */         String tempCid = URLDecoder.decode(cid, "utf-8");
/* 152 */         part = (MIMEPart)this.partsMap.get(tempCid);
/*     */       }
/*     */       catch (UnsupportedEncodingException ue)
/*     */       {
/*     */       }
/*     */     }
/* 158 */     return part;
/*     */   }
/*     */ 
/*     */   public void parseAll()
/*     */   {
/* 166 */     while (makeProgress());
/*     */   }
/*     */ 
/*     */   public synchronized boolean makeProgress()
/*     */   {
/* 179 */     if (!this.it.hasNext()) {
/* 180 */       return false;
/*     */     }
/*     */ 
/* 183 */     MIMEEvent event = (MIMEEvent)this.it.next();
/*     */ 
/* 185 */     switch (1.$SwitchMap$com$sun$xml$internal$org$jvnet$mimepull$MIMEEvent$EVENT_TYPE[event.getEventType().ordinal()]) {
/*     */     case 1:
/* 187 */       LOGGER.fine("MIMEEvent=" + MIMEEvent.EVENT_TYPE.START_MESSAGE);
/* 188 */       break;
/*     */     case 2:
/* 191 */       LOGGER.fine("MIMEEvent=" + MIMEEvent.EVENT_TYPE.START_PART);
/* 192 */       break;
/*     */     case 3:
/* 195 */       LOGGER.fine("MIMEEvent=" + MIMEEvent.EVENT_TYPE.HEADERS);
/* 196 */       MIMEEvent.Headers headers = (MIMEEvent.Headers)event;
/* 197 */       InternetHeaders ih = headers.getHeaders();
/* 198 */       List cids = ih.getHeader("content-id");
/* 199 */       String cid = this.currentIndex + "";
/* 200 */       if ((cid.length() > 2) && (cid.charAt(0) == '<')) {
/* 201 */         cid = cid.substring(1, cid.length() - 1);
/*     */       }
/* 203 */       MIMEPart listPart = this.currentIndex < this.partsList.size() ? (MIMEPart)this.partsList.get(this.currentIndex) : null;
/* 204 */       MIMEPart mapPart = getDecodedCidPart(cid);
/* 205 */       if ((listPart == null) && (mapPart == null)) {
/* 206 */         this.currentPart = getPart(cid);
/* 207 */         this.partsList.add(this.currentIndex, this.currentPart);
/* 208 */       } else if (listPart == null) {
/* 209 */         this.currentPart = mapPart;
/* 210 */         this.partsList.add(this.currentIndex, mapPart);
/* 211 */       } else if (mapPart == null) {
/* 212 */         this.currentPart = listPart;
/* 213 */         this.currentPart.setContentId(cid);
/* 214 */         this.partsMap.put(cid, this.currentPart);
/* 215 */       } else if (listPart != mapPart) {
/* 216 */         throw new MIMEParsingException("Created two different attachments using Content-ID and index");
/*     */       }
/* 218 */       this.currentPart.setHeaders(ih);
/* 219 */       break;
/*     */     case 4:
/* 222 */       LOGGER.finer("MIMEEvent=" + MIMEEvent.EVENT_TYPE.CONTENT);
/* 223 */       MIMEEvent.Content content = (MIMEEvent.Content)event;
/* 224 */       ByteBuffer buf = content.getData();
/* 225 */       this.currentPart.addBody(buf);
/* 226 */       break;
/*     */     case 5:
/* 229 */       LOGGER.fine("MIMEEvent=" + MIMEEvent.EVENT_TYPE.END_PART);
/* 230 */       this.currentPart.doneParsing();
/* 231 */       this.currentIndex += 1;
/* 232 */       break;
/*     */     case 6:
/* 235 */       LOGGER.fine("MIMEEvent=" + MIMEEvent.EVENT_TYPE.END_MESSAGE);
/* 236 */       this.parsed = true;
/*     */       try {
/* 238 */         this.in.close();
/*     */       } catch (IOException ioe) {
/* 240 */         throw new MIMEParsingException(ioe);
/*     */       }
/*     */ 
/*     */     default:
/* 245 */       throw new MIMEParsingException("Unknown Parser state = " + event.getEventType());
/*     */     }
/* 247 */     return true;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.org.jvnet.mimepull.MIMEMessage
 * JD-Core Version:    0.6.2
 */