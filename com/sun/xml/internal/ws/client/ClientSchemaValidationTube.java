/*     */ package com.sun.xml.internal.ws.client;
/*     */ 
/*     */ import com.sun.xml.internal.ws.api.WSBinding;
/*     */ import com.sun.xml.internal.ws.api.message.Message;
/*     */ import com.sun.xml.internal.ws.api.message.Packet;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLModel;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLPort;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLService;
/*     */ import com.sun.xml.internal.ws.api.pipe.NextAction;
/*     */ import com.sun.xml.internal.ws.api.pipe.Tube;
/*     */ import com.sun.xml.internal.ws.api.pipe.TubeCloner;
/*     */ import com.sun.xml.internal.ws.api.pipe.helper.AbstractTubeImpl;
/*     */ import com.sun.xml.internal.ws.developer.SchemaValidationFeature;
/*     */ import com.sun.xml.internal.ws.util.MetadataUtil;
/*     */ import com.sun.xml.internal.ws.util.pipe.AbstractSchemaValidationTube;
/*     */ import com.sun.xml.internal.ws.util.pipe.AbstractSchemaValidationTube.MetadataResolverImpl;
/*     */ import java.util.Map;
/*     */ import java.util.logging.Logger;
/*     */ import javax.xml.transform.Source;
/*     */ import javax.xml.validation.Schema;
/*     */ import javax.xml.validation.SchemaFactory;
/*     */ import javax.xml.validation.Validator;
/*     */ import javax.xml.ws.WebServiceException;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public class ClientSchemaValidationTube extends AbstractSchemaValidationTube
/*     */ {
/*  54 */   private static final Logger LOGGER = Logger.getLogger(ClientSchemaValidationTube.class.getName());
/*     */   private final Schema schema;
/*     */   private final Validator validator;
/*     */   private final boolean noValidation;
/*     */   private final WSDLPort port;
/*     */ 
/*     */   public ClientSchemaValidationTube(WSBinding binding, WSDLPort port, Tube next)
/*     */   {
/*  62 */     super(binding, next);
/*  63 */     this.port = port;
/*  64 */     if (port != null) {
/*  65 */       String primaryWsdl = port.getOwner().getParent().getLocation().getSystemId();
/*  66 */       AbstractSchemaValidationTube.MetadataResolverImpl mdresolver = new AbstractSchemaValidationTube.MetadataResolverImpl(this);
/*  67 */       Map docs = MetadataUtil.getMetadataClosure(primaryWsdl, mdresolver, true);
/*  68 */       mdresolver = new AbstractSchemaValidationTube.MetadataResolverImpl(this, docs.values());
/*  69 */       Source[] sources = getSchemaSources(docs.values(), mdresolver);
/*  70 */       for (Source source : sources) {
/*  71 */         LOGGER.fine("Constructing client validation schema from = " + source.getSystemId());
/*     */       }
/*     */ 
/*  74 */       if (sources.length != 0) {
/*  75 */         this.noValidation = false;
/*  76 */         this.sf.setResourceResolver(mdresolver);
/*     */         try {
/*  78 */           this.schema = this.sf.newSchema(sources);
/*     */         } catch (SAXException e) {
/*  80 */           throw new WebServiceException(e);
/*     */         }
/*  82 */         this.validator = this.schema.newValidator();
/*  83 */         return;
/*     */       }
/*     */     }
/*  86 */     this.noValidation = true;
/*  87 */     this.schema = null;
/*  88 */     this.validator = null;
/*     */   }
/*     */ 
/*     */   protected Validator getValidator() {
/*  92 */     return this.validator;
/*     */   }
/*     */ 
/*     */   protected boolean isNoValidation() {
/*  96 */     return this.noValidation;
/*     */   }
/*     */ 
/*     */   protected ClientSchemaValidationTube(ClientSchemaValidationTube that, TubeCloner cloner) {
/* 100 */     super(that, cloner);
/* 101 */     this.port = that.port;
/* 102 */     this.schema = that.schema;
/* 103 */     this.validator = this.schema.newValidator();
/* 104 */     this.noValidation = that.noValidation;
/*     */   }
/*     */ 
/*     */   public AbstractTubeImpl copy(TubeCloner cloner) {
/* 108 */     return new ClientSchemaValidationTube(this, cloner);
/*     */   }
/*     */ 
/*     */   public NextAction processRequest(Packet request)
/*     */   {
/* 113 */     if ((isNoValidation()) || (!this.feature.isOutbound()) || (!request.getMessage().hasPayload()) || (request.getMessage().isFault()))
/* 114 */       return super.processRequest(request);
/*     */     try
/*     */     {
/* 117 */       doProcess(request);
/*     */     } catch (SAXException se) {
/* 119 */       throw new WebServiceException(se);
/*     */     }
/* 121 */     return super.processRequest(request);
/*     */   }
/*     */ 
/*     */   public NextAction processResponse(Packet response)
/*     */   {
/* 126 */     if ((isNoValidation()) || (!this.feature.isInbound()) || (response.getMessage() == null) || (!response.getMessage().hasPayload()) || (response.getMessage().isFault()))
/* 127 */       return super.processResponse(response);
/*     */     try
/*     */     {
/* 130 */       doProcess(response);
/*     */     } catch (SAXException se) {
/* 132 */       throw new WebServiceException(se);
/*     */     }
/* 134 */     return super.processResponse(response);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.client.ClientSchemaValidationTube
 * JD-Core Version:    0.6.2
 */