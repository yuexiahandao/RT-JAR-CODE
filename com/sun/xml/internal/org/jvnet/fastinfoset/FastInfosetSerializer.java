package com.sun.xml.internal.org.jvnet.fastinfoset;

import java.io.OutputStream;
import java.util.Map;

public abstract interface FastInfosetSerializer
{
  public static final String IGNORE_DTD_FEATURE = "http://jvnet.org/fastinfoset/serializer/feature/ignore/DTD";
  public static final String IGNORE_COMMENTS_FEATURE = "http://jvnet.org/fastinfoset/serializer/feature/ignore/comments";
  public static final String IGNORE_PROCESSING_INSTRUCTIONS_FEATURE = "http://jvnet.org/fastinfoset/serializer/feature/ignore/processingInstructions";
  public static final String IGNORE_WHITE_SPACE_TEXT_CONTENT_FEATURE = "http://jvnet.org/fastinfoset/serializer/feature/ignore/whiteSpaceTextContent";
  public static final String BUFFER_SIZE_PROPERTY = "http://jvnet.org/fastinfoset/parser/properties/buffer-size";
  public static final String REGISTERED_ENCODING_ALGORITHMS_PROPERTY = "http://jvnet.org/fastinfoset/parser/properties/registered-encoding-algorithms";
  public static final String EXTERNAL_VOCABULARIES_PROPERTY = "http://jvnet.org/fastinfoset/parser/properties/external-vocabularies";
  public static final int MIN_CHARACTER_CONTENT_CHUNK_SIZE = 0;
  public static final int MAX_CHARACTER_CONTENT_CHUNK_SIZE = 32;
  public static final int CHARACTER_CONTENT_CHUNK_MAP_MEMORY_CONSTRAINT = 2147483647;
  public static final int MIN_ATTRIBUTE_VALUE_SIZE = 0;
  public static final int MAX_ATTRIBUTE_VALUE_SIZE = 32;
  public static final int ATTRIBUTE_VALUE_MAP_MEMORY_CONSTRAINT = 2147483647;
  public static final String UTF_8 = "UTF-8";
  public static final String UTF_16BE = "UTF-16BE";

  public abstract void setIgnoreDTD(boolean paramBoolean);

  public abstract boolean getIgnoreDTD();

  public abstract void setIgnoreComments(boolean paramBoolean);

  public abstract boolean getIgnoreComments();

  public abstract void setIgnoreProcesingInstructions(boolean paramBoolean);

  public abstract boolean getIgnoreProcesingInstructions();

  public abstract void setIgnoreWhiteSpaceTextContent(boolean paramBoolean);

  public abstract boolean getIgnoreWhiteSpaceTextContent();

  public abstract void setCharacterEncodingScheme(String paramString);

  public abstract String getCharacterEncodingScheme();

  public abstract void setRegisteredEncodingAlgorithms(Map paramMap);

  public abstract Map getRegisteredEncodingAlgorithms();

  public abstract int getMinCharacterContentChunkSize();

  public abstract void setMinCharacterContentChunkSize(int paramInt);

  public abstract int getMaxCharacterContentChunkSize();

  public abstract void setMaxCharacterContentChunkSize(int paramInt);

  public abstract int getCharacterContentChunkMapMemoryLimit();

  public abstract void setCharacterContentChunkMapMemoryLimit(int paramInt);

  public abstract int getMinAttributeValueSize();

  public abstract void setMinAttributeValueSize(int paramInt);

  public abstract int getMaxAttributeValueSize();

  public abstract void setMaxAttributeValueSize(int paramInt);

  public abstract int getAttributeValueMapMemoryLimit();

  public abstract void setAttributeValueMapMemoryLimit(int paramInt);

  public abstract void setExternalVocabulary(ExternalVocabulary paramExternalVocabulary);

  public abstract void setVocabularyApplicationData(VocabularyApplicationData paramVocabularyApplicationData);

  public abstract VocabularyApplicationData getVocabularyApplicationData();

  public abstract void reset();

  public abstract void setOutputStream(OutputStream paramOutputStream);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetSerializer
 * JD-Core Version:    0.6.2
 */