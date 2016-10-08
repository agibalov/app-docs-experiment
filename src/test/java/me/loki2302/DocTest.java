package me.loki2302;

import me.loki2302.core.CodeReader;
import me.loki2302.core.CodebaseModel;
import me.loki2302.core.CodebaseModelGraphFacade;
import me.loki2302.core.models.ClassModel;
import me.loki2302.documentation.SnippetWriter;
import me.loki2302.documentation.snippets.ClassDiagramSnippet;
import me.loki2302.documentation.snippets.JavaClassesSnippet;
import org.junit.Rule;
import org.junit.Test;

import javax.validation.Validation;
import javax.validation.Validator;
import java.io.File;
import java.util.List;

public class DocTest {
    @Rule
    public final SnippetWriter snippetWriter = new SnippetWriter(System.getProperty("snippetsDir"));

    @Test
    public void documentClasses() {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        CodeReader codeReader = new CodeReader(validator);
        CodebaseModelGraphFacade codebaseModelGraphFacade = new CodebaseModelGraphFacade();
        me.loki2302.core.CodebaseModelBuilder codebaseModelBuilder = new me.loki2302.core.CodebaseModelBuilder(
                codeReader,
                codebaseModelGraphFacade);
        CodebaseModel codebaseModel = codebaseModelBuilder.buildCodebaseModel(new File("./src/main/java"));

        List<ClassModel> controllerClasses = codebaseModel.findClassesByStereotype("controller");
        snippetWriter.write("controllers.adoc", new JavaClassesSnippet(controllerClasses));

        List<ClassModel> serviceClasses = codebaseModel.findClassesByStereotype("service");
        snippetWriter.write("services.adoc", new JavaClassesSnippet(serviceClasses));

        List<ClassModel> repositoryClasses = codebaseModel.findClassesByStereotype("controller");
        snippetWriter.write("repositories.adoc", new JavaClassesSnippet(repositoryClasses));

        List<ClassModel> allClasses = codebaseModel.findAllClasses();
        snippetWriter.write("classDiagram.puml", new ClassDiagramSnippet(allClasses));
    }
}
