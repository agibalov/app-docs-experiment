package me.loki2302;

import com.puppycrawl.tools.checkstyle.api.AbstractFileSetCheck;
import com.puppycrawl.tools.checkstyle.api.CheckstyleException;
import me.loki2302.core.CodeReader;
import me.loki2302.core.CodebaseModel;
import me.loki2302.core.CodebaseModelBuilder;
import me.loki2302.core.CodebaseModelGraphFacade;
import me.loki2302.core.models.ClassModel;
import me.loki2302.core.models.FieldModel;
import me.loki2302.core.models.MethodModel;
import me.loki2302.core.models.ParameterModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Validation;
import javax.validation.Validator;
import java.io.File;
import java.util.List;

public class JavadocCommentCheck extends AbstractFileSetCheck {
    private final static Logger LOGGER = LoggerFactory.getLogger(JavadocCommentCheck.class);

    private File sourceRoot;
    private CodebaseModel codebaseModel;

    public void setSourceRoot(File sourceRoot) {
        if(sourceRoot.exists()) {
            this.sourceRoot = sourceRoot;
        }
    }

    @Override
    public void beginProcessing(String charset) {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        CodeReader codeReader = new CodeReader(validator);
        CodebaseModelGraphFacade codebaseModelGraphFacade = new CodebaseModelGraphFacade();
        CodebaseModelBuilder codebaseModelBuilder = new CodebaseModelBuilder(
                codeReader,
                codebaseModelGraphFacade);

        codebaseModel = codebaseModelBuilder.buildCodebaseModel(sourceRoot);

        LOGGER.info("Loaded codebase model from {}. There are {} classes.",
                sourceRoot,
                codebaseModel.findAllClasses().size());
    }

    @Override
    protected void processFiltered(File file, List<String> lines) throws CheckstyleException {
        try {
            List<ClassModel> classModels = codebaseModel.findClassesByFile(file);
            for(ClassModel classModel : classModels) {
                for(String error : classModel.errors) {
                    log(0, String.format("Class %s: %s",
                            classModel.name,
                            error));
                }

                for(FieldModel fieldModel : classModel.fields) {
                    for(String error : fieldModel.errors) {
                        log(0, String.format("Field %s::%s: %s",
                                classModel.name,
                                fieldModel.name,
                                error));
                    }
                }

                for(MethodModel methodModel : classModel.methods) {
                    for(String error : methodModel.errors) {
                        log(0, String.format("Method %s::%s: %s",
                                classModel.name,
                                methodModel.name, error));
                    }

                    for(ParameterModel parameterModel : methodModel.parameters) {
                        for(String error : parameterModel.errors) {
                            log(0, String.format("Method parameter %s::%s (%s): %s",
                                    classModel.name,
                                    methodModel.name,
                                    parameterModel.name,
                                    error));
                        }
                    }
                }
            }
        } catch(Throwable t) {
            log(0, String.format("error! %s", t.getMessage()));
            //throw new CheckstyleException("Unexpected error", t);
        }
    }
}
