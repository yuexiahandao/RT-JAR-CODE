package javax.annotation.processing;

import java.util.Set;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;

public abstract interface Processor
{
  public abstract Set<String> getSupportedOptions();

  public abstract Set<String> getSupportedAnnotationTypes();

  public abstract SourceVersion getSupportedSourceVersion();

  public abstract void init(ProcessingEnvironment paramProcessingEnvironment);

  public abstract boolean process(Set<? extends TypeElement> paramSet, RoundEnvironment paramRoundEnvironment);

  public abstract Iterable<? extends Completion> getCompletions(Element paramElement, AnnotationMirror paramAnnotationMirror, ExecutableElement paramExecutableElement, String paramString);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.annotation.processing.Processor
 * JD-Core Version:    0.6.2
 */