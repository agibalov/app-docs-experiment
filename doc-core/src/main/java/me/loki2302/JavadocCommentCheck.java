package me.loki2302;

import com.puppycrawl.tools.checkstyle.api.AbstractFileSetCheck;
import com.puppycrawl.tools.checkstyle.api.CheckstyleException;
import me.loki2302.core.CodeReader;
import me.loki2302.core.CodebaseModel;
import me.loki2302.core.CodebaseModelBuilder;
import me.loki2302.core.CodebaseModelGraphFacade;
import me.loki2302.core.models.ClassModel;

import javax.validation.Validation;
import javax.validation.Validator;
import java.io.File;
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
        try {
            Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
            CodeReader codeReader = new CodeReader(validator);
            CodebaseModelGraphFacade codebaseModelGraphFacade = new CodebaseModelGraphFacade();
            CodebaseModelBuilder codebaseModelBuilder = new CodebaseModelBuilder(
                    codeReader,
                    codebaseModelGraphFacade);
            CodebaseModel codebaseModel = codebaseModelBuilder.buildCodebaseModel(sourceRoot);

            List<ClassModel> classModels = codebaseModel.findClassesByFile(file);

            for(ClassModel classModel : classModels) {
                for(String error : classModel.errors) {
                    log(0, String.format("class %s: %s", classModel.name, error));
                }

                /*for(MethodModel methodModel : classModel.methods) {
                    for(String error : methodModel.errors) {
                        log(0, String.format("method %s::%s: %s", classModel.name, methodModel.name, error));
                    }
                }*/
            }
        } catch(Throwable t) {
            log(0, String.format("error! %s", t.getMessage()));
            //throw new CheckstyleException("Unexpected error", t);
        }
    }
}
