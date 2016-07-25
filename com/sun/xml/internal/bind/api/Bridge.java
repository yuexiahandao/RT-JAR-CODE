/*     */ package com.sun.xml.internal.bind.api;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.istack.internal.Nullable;
/*     */ import com.sun.istack.internal.Pool;
/*     */ import com.sun.xml.internal.bind.v2.runtime.BridgeContextImpl;
/*     */ import com.sun.xml.internal.bind.v2.runtime.JAXBContextImpl;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import javax.xml.bind.JAXBException;
/*     */ import javax.xml.bind.Marshaller;
/*     */ import javax.xml.bind.Unmarshaller;
/*     */ import javax.xml.bind.attachment.AttachmentMarshaller;
/*     */ import javax.xml.bind.attachment.AttachmentUnmarshaller;
/*     */ import javax.xml.namespace.NamespaceContext;
/*     */ import javax.xml.stream.XMLStreamReader;
/*     */ import javax.xml.stream.XMLStreamWriter;
/*     */ import javax.xml.transform.Result;
/*     */ import javax.xml.transform.Source;
/*     */ import org.w3c.dom.Node;
/*     */ import org.xml.sax.ContentHandler;
/*     */ 
/*     */ public abstract class Bridge<T>
/*     */ {
/*     */   protected final JAXBContextImpl context;
/*     */ 
/*     */   protected Bridge(JAXBContextImpl context)
/*     */   {
/*  69 */     this.context = context;
/*     */   }
/*     */ 
/*     */   @NotNull
/*     */   public JAXBRIContext getContext()
/*     */   {
/*  80 */     return this.context;
/*     */   }
/*     */ 
/*     */   public final void marshal(T object, XMLStreamWriter output)
/*     */     throws JAXBException
/*     */   {
/*  91 */     marshal(object, output, null);
/*     */   }
/*     */   public final void marshal(T object, XMLStreamWriter output, AttachmentMarshaller am) throws JAXBException {
/*  94 */     Marshaller m = (Marshaller)this.context.marshallerPool.take();
/*  95 */     m.setAttachmentMarshaller(am);
/*  96 */     marshal(m, object, output);
/*  97 */     m.setAttachmentMarshaller(null);
/*  98 */     this.context.marshallerPool.recycle(m);
/*     */   }
/*     */ 
/*     */   public final void marshal(@NotNull BridgeContext context, T object, XMLStreamWriter output) throws JAXBException {
/* 102 */     marshal(((BridgeContextImpl)context).marshaller, object, output);
/*     */   }
/*     */ 
/*     */   public abstract void marshal(@NotNull Marshaller paramMarshaller, T paramT, XMLStreamWriter paramXMLStreamWriter)
/*     */     throws JAXBException;
/*     */ 
/*     */   public void marshal(T object, OutputStream output, NamespaceContext nsContext)
/*     */     throws JAXBException
/*     */   {
/* 122 */     marshal(object, output, nsContext, null);
/*     */   }
/*     */ 
/*     */   public void marshal(T object, OutputStream output, NamespaceContext nsContext, AttachmentMarshaller am)
/*     */     throws JAXBException
/*     */   {
/* 128 */     Marshaller m = (Marshaller)this.context.marshallerPool.take();
/* 129 */     m.setAttachmentMarshaller(am);
/* 130 */     marshal(m, object, output, nsContext);
/* 131 */     m.setAttachmentMarshaller(null);
/* 132 */     this.context.marshallerPool.recycle(m);
/*     */   }
/*     */ 
/*     */   public final void marshal(@NotNull BridgeContext context, T object, OutputStream output, NamespaceContext nsContext) throws JAXBException {
/* 136 */     marshal(((BridgeContextImpl)context).marshaller, object, output, nsContext);
/*     */   }
/*     */ 
/*     */   public abstract void marshal(@NotNull Marshaller paramMarshaller, T paramT, OutputStream paramOutputStream, NamespaceContext paramNamespaceContext) throws JAXBException;
/*     */ 
/*     */   public final void marshal(T object, Node output) throws JAXBException
/*     */   {
/* 143 */     Marshaller m = (Marshaller)this.context.marshallerPool.take();
/* 144 */     marshal(m, object, output);
/* 145 */     this.context.marshallerPool.recycle(m);
/*     */   }
/*     */ 
/*     */   public final void marshal(@NotNull BridgeContext context, T object, Node output) throws JAXBException {
/* 149 */     marshal(((BridgeContextImpl)context).marshaller, object, output);
/*     */   }
/*     */ 
/*     */   public abstract void marshal(@NotNull Marshaller paramMarshaller, T paramT, Node paramNode)
/*     */     throws JAXBException;
/*     */ 
/*     */   public final void marshal(T object, ContentHandler contentHandler)
/*     */     throws JAXBException
/*     */   {
/* 159 */     marshal(object, contentHandler, null);
/*     */   }
/*     */ 
/*     */   public final void marshal(T object, ContentHandler contentHandler, AttachmentMarshaller am)
/*     */     throws JAXBException
/*     */   {
/* 165 */     Marshaller m = (Marshaller)this.context.marshallerPool.take();
/* 166 */     m.setAttachmentMarshaller(am);
/* 167 */     marshal(m, object, contentHandler);
/* 168 */     m.setAttachmentMarshaller(null);
/* 169 */     this.context.marshallerPool.recycle(m);
/*     */   }
/*     */   public final void marshal(@NotNull BridgeContext context, T object, ContentHandler contentHandler) throws JAXBException {
/* 172 */     marshal(((BridgeContextImpl)context).marshaller, object, contentHandler);
/*     */   }
/*     */ 
/*     */   public abstract void marshal(@NotNull Marshaller paramMarshaller, T paramT, ContentHandler paramContentHandler)
/*     */     throws JAXBException;
/*     */ 
/*     */   public final void marshal(T object, Result result) throws JAXBException
/*     */   {
/* 180 */     Marshaller m = (Marshaller)this.context.marshallerPool.take();
/* 181 */     marshal(m, object, result);
/* 182 */     this.context.marshallerPool.recycle(m);
/*     */   }
/*     */   public final void marshal(@NotNull BridgeContext context, T object, Result result) throws JAXBException {
/* 185 */     marshal(((BridgeContextImpl)context).marshaller, object, result);
/*     */   }
/*     */ 
/*     */   public abstract void marshal(@NotNull Marshaller paramMarshaller, T paramT, Result paramResult) throws JAXBException;
/*     */ 
/*     */   private T exit(T r, Unmarshaller u)
/*     */   {
/* 192 */     u.setAttachmentUnmarshaller(null);
/* 193 */     this.context.unmarshallerPool.recycle(u);
/* 194 */     return r;
/*     */   }
/*     */ 
/*     */   @NotNull
/*     */   public final T unmarshal(@NotNull XMLStreamReader in)
/*     */     throws JAXBException
/*     */   {
/* 214 */     return unmarshal(in, null);
/*     */   }
/*     */ 
/*     */   @NotNull
/*     */   public final T unmarshal(@NotNull XMLStreamReader in, @Nullable AttachmentUnmarshaller au) throws JAXBException
/*     */   {
/* 220 */     Unmarshaller u = (Unmarshaller)this.context.unmarshallerPool.take();
/* 221 */     u.setAttachmentUnmarshaller(au);
/* 222 */     return exit(unmarshal(u, in), u);
/*     */   }
/* 225 */   @NotNull
/*     */   public final T unmarshal(@NotNull BridgeContext context, @NotNull XMLStreamReader in) throws JAXBException { return unmarshal(((BridgeContextImpl)context).unmarshaller, in); }
/*     */ 
/*     */ 
/*     */   @NotNull
/*     */   public abstract T unmarshal(@NotNull Unmarshaller paramUnmarshaller, @NotNull XMLStreamReader paramXMLStreamReader)
/*     */     throws JAXBException;
/*     */ 
/*     */   @NotNull
/*     */   public final T unmarshal(@NotNull Source in)
/*     */     throws JAXBException
/*     */   {
/* 246 */     return unmarshal(in, null);
/*     */   }
/*     */ 
/*     */   @NotNull
/*     */   public final T unmarshal(@NotNull Source in, @Nullable AttachmentUnmarshaller au) throws JAXBException
/*     */   {
/* 252 */     Unmarshaller u = (Unmarshaller)this.context.unmarshallerPool.take();
/* 253 */     u.setAttachmentUnmarshaller(au);
/* 254 */     return exit(unmarshal(u, in), u);
/*     */   }
/* 257 */   @NotNull
/*     */   public final T unmarshal(@NotNull BridgeContext context, @NotNull Source in) throws JAXBException { return unmarshal(((BridgeContextImpl)context).unmarshaller, in); }
/*     */ 
/*     */ 
/*     */   @NotNull
/*     */   public abstract T unmarshal(@NotNull Unmarshaller paramUnmarshaller, @NotNull Source paramSource)
/*     */     throws JAXBException;
/*     */ 
/*     */   @NotNull
/*     */   public final T unmarshal(@NotNull InputStream in)
/*     */     throws JAXBException
/*     */   {
/* 278 */     Unmarshaller u = (Unmarshaller)this.context.unmarshallerPool.take();
/* 279 */     return exit(unmarshal(u, in), u);
/*     */   }
/* 282 */   @NotNull
/*     */   public final T unmarshal(@NotNull BridgeContext context, @NotNull InputStream in) throws JAXBException { return unmarshal(((BridgeContextImpl)context).unmarshaller, in); }
/*     */ 
/*     */ 
/*     */   @NotNull
/*     */   public abstract T unmarshal(@NotNull Unmarshaller paramUnmarshaller, @NotNull InputStream paramInputStream)
/*     */     throws JAXBException;
/*     */ 
/*     */   @NotNull
/*     */   public final T unmarshal(@NotNull Node n)
/*     */     throws JAXBException
/*     */   {
/* 301 */     return unmarshal(n, null);
/*     */   }
/*     */ 
/*     */   @NotNull
/*     */   public final T unmarshal(@NotNull Node n, @Nullable AttachmentUnmarshaller au) throws JAXBException
/*     */   {
/* 307 */     Unmarshaller u = (Unmarshaller)this.context.unmarshallerPool.take();
/* 308 */     u.setAttachmentUnmarshaller(au);
/* 309 */     return exit(unmarshal(u, n), u);
/*     */   }
/* 312 */   @NotNull
/*     */   public final T unmarshal(@NotNull BridgeContext context, @NotNull Node n) throws JAXBException { return unmarshal(((BridgeContextImpl)context).unmarshaller, n); }
/*     */ 
/*     */ 
/*     */   @NotNull
/*     */   public abstract T unmarshal(@NotNull Unmarshaller paramUnmarshaller, @NotNull Node paramNode)
/*     */     throws JAXBException;
/*     */ 
/*     */   public abstract TypeReference getTypeReference();
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.api.Bridge
 * JD-Core Version:    0.6.2
 */