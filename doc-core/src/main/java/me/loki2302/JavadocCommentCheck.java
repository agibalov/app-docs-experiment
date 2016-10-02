package me.loki2302;

import com.puppycrawl.tools.checkstyle.api.AbstractFileSetCheck;
import com.puppycrawl.tools.checkstyle.api.CheckstyleException;
import com.thoughtworks.qdox.JavaProjectBuilder;
import com.thoughtworks.qdox.model.JavaClass;
import me.loki2302.core.*;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

public class JavadocCommentCheck extends AbstractFileSetCheck {
    private File sourceRoot;

    public void setSourceRoot(File sourceRoot) {
        if(sourceRoot.exists()) {
            this.sourceRoot = sourceRoot;
        }
    }

    @Override
    protected void processFiltered(File file, List<String> lines) throws CheckstyleException {
        CodebaseModel codebaseModel = null;
        try {
            codebaseModel = CodebaseModelBuilder.buildCodebaseModel(sourceRoot);
        } catch (Throwable t) {
            // intentionally blank: codebaseModel is entirely optional
            // TODO: consider an option to build a CodebaseModellingResult model to simplify this checker
            // also note that qdox can resolve checkstyle's 'file' into qdox file:
            // File qdoxFile = new File(javaClass.getSource().getURL().toURI());
            // File checkstyleFile = ...; // comes as a processFiltered() parameter
            // bool same = checkstyleFile.equals(qdoxFile); // true
        }

        try {
            JavaProjectBuilder javaProjectBuilder = new JavaProjectBuilder();
            try {
                javaProjectBuilder.addSource(file);
            } catch (IOException e) {
                throw new CheckstyleException("Can't load " + file.getAbsolutePath(), e);
            }

            Collection<JavaClass> javaClasses = javaProjectBuilder.getClasses();
            for (JavaClass javaClass : javaClasses) {
                ModellingResult modellingResult = CodebaseModelBuilder.modelClass(javaClass);

                if (modellingResult instanceof SkipModellingResult) {
                    continue;
                }

                if (modellingResult instanceof ErrorModellingResult) {
                    ErrorModellingResult errorClassModel = (ErrorModellingResult) modellingResult;
                    log(0, errorClassModel.message);
                    continue;
                }

                if (modellingResult instanceof SuccessfulModellingResult) {
                    // OK
                    continue;
                }

                throw new RuntimeException();
            }
        } catch (Throwable t) {
            log(0, String.format("error! %s", t.getMessage()));
            throw new CheckstyleException("Unexpected error", t);
        }
    }
}
