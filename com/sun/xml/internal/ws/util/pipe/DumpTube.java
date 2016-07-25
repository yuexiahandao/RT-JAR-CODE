/*     */ package com.sun.xml.internal.ws.util.pipe;
/*     */ 
/*     */ import com.sun.xml.internal.ws.api.message.Message;
/*     */ import com.sun.xml.internal.ws.api.message.Packet;
/*     */ import com.sun.xml.internal.ws.api.pipe.NextAction;
/*     */ import com.sun.xml.internal.ws.api.pipe.Tube;
/*     */ import com.sun.xml.internal.ws.api.pipe.TubeCloner;
/*     */ import com.sun.xml.internal.ws.api.pipe.helper.AbstractFilterTubeImpl;
/*     */ import com.sun.xml.internal.ws.api.pipe.helper.AbstractTubeImpl;
/*     */ import java.io.OutputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.lang.reflect.Constructor;
/*     */ import javax.xml.stream.XMLOutputFactory;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamWriter;
/*     */ 
/*     */ public class DumpTube extends AbstractFilterTubeImpl
/*     */ {
/*     */   private final String name;
/*     */   private final PrintStream out;
/*     */   private final XMLOutputFactory staxOut;
/*     */   private static boolean warnStaxUtils;
/*     */ 
/*     */   public DumpTube(String name, PrintStream out, Tube next)
/*     */   {
/*  68 */     super(next);
/*  69 */     this.name = name;
/*  70 */     this.out = out;
/*  71 */     this.staxOut = XMLOutputFactory.newInstance();
/*     */   }
/*     */ 
/*     */   protected DumpTube(DumpTube that, TubeCloner cloner)
/*     */   {
/*  79 */     super(that, cloner);
/*  80 */     this.name = that.name;
/*  81 */     this.out = that.out;
/*  82 */     this.staxOut = that.staxOut;
/*     */   }
/*     */ 
/*     */   public NextAction processRequest(Packet request)
/*     */   {
/*  87 */     dump("request", request);
/*  88 */     return super.processRequest(request);
/*     */   }
/*     */ 
/*     */   public NextAction processResponse(Packet response)
/*     */   {
/*  93 */     dump("response", response);
/*  94 */     return super.processResponse(response);
/*     */   }
/*     */ 
/*     */   protected void dump(String header, Packet packet) {
/*  98 */     this.out.println("====[" + this.name + ":" + header + "]====");
/*  99 */     if (packet.getMessage() == null)
/* 100 */       this.out.println("(none)");
/*     */     else
/*     */       try {
/* 103 */         XMLStreamWriter writer = this.staxOut.createXMLStreamWriter(new PrintStream(this.out)
/*     */         {
/*     */           public void close()
/*     */           {
/*     */           }
/*     */         });
/* 109 */         writer = createIndenter(writer);
/* 110 */         packet.getMessage().copy().writeTo(writer);
/* 111 */         writer.close();
/*     */       } catch (XMLStreamException e) {
/* 113 */         e.printStackTrace(this.out);
/*     */       }
/* 115 */     this.out.println("============");
/*     */   }
/*     */ 
/*     */   private XMLStreamWriter createIndenter(XMLStreamWriter writer)
/*     */   {
/*     */     try
/*     */     {
/* 126 */       Class clazz = getClass().getClassLoader().loadClass("javanet.staxutils.IndentingXMLStreamWriter");
/* 127 */       Constructor c = clazz.getConstructor(new Class[] { XMLStreamWriter.class });
/* 128 */       writer = (XMLStreamWriter)c.newInstance(new Object[] { writer });
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 132 */       if (!warnStaxUtils) {
/* 133 */         warnStaxUtils = true;
/* 134 */         this.out.println("WARNING: put stax-utils.jar to the classpath to indent the dump output");
/*     */       }
/*     */     }
/* 137 */     return writer;
/*     */   }
/*     */ 
/*     */   public AbstractTubeImpl copy(TubeCloner cloner)
/*     */   {
/* 142 */     return new DumpTube(this, cloner);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.util.pipe.DumpTube
 * JD-Core Version:    0.6.2
 */