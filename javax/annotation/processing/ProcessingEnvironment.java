package javax.annotation.processing;

import java.util.Locale;
import java.util.Map;
import javax.lang.model.SourceVersion;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

public abstract interface ProcessingEnvironment
{
  public abstract Map<String, String> getOptions();

  public abstract Messager getMessager();

  public abstract Filer getFiler();

  public abstract Elements getElementUtils();

  public abstract Types getTypeUtils();

  public abstract SourceVersion getSourceVersion();

  public abstract Locale getLocale();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.annotation.processing.ProcessingEnvironment
 * JD-Core Version:    0.6.2
 */