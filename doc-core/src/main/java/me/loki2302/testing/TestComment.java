package me.loki2302.testing;

import com.thoughtworks.qdox.JavaProjectBuilder;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaMethod;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import java.io.File;
import java.util.Collections;

public class TestComment extends TestWatcher {
    private final String codeRoot;

    private JavaProjectBuilder javaProjectBuilder;
    private String className;
    private String methodName;

    public TestComment(String codeRoot) {
        this.codeRoot = codeRoot;
    }

    @Override
    protected void starting(Description description) {
        javaProjectBuilder = new JavaProjectBuilder();
        javaProjectBuilder.addSourceTree(new File(codeRoot));

        className = description.getClassName();
        methodName = description.getMethodName();
    }

    public String getComment() {
        JavaClass testClass = javaProjectBuilder.getClassByName(className);
        JavaMethod testMethod = testClass.getMethodBySignature(methodName, Collections.emptyList());
        String testComment = testMethod.getComment();
        return testComment;
    }
}
