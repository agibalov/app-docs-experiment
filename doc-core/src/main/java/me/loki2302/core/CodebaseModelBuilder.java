package me.loki2302.core;

import com.thoughtworks.qdox.JavaProjectBuilder;
import me.loki2302.core.models.ClassModel;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spoon.Launcher;
import spoon.SpoonAPI;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

public class CodebaseModelBuilder {
    private static final Logger LOGGER = LoggerFactory.getLogger(CodebaseModelBuilder.class);

    private final CodeReader codeReader;
    private final CodebaseModelGraphFacade codebaseModelGraphFacade;
    private final String[] classpath;

    public CodebaseModelBuilder(
            CodeReader codeReader,
            CodebaseModelGraphFacade codebaseModelGraphFacade,
            String[] classpath) {

        this.codeReader = codeReader;
        this.codebaseModelGraphFacade = codebaseModelGraphFacade;
        this.classpath = classpath;
    }

    public CodebaseModel buildCodebaseModel(File sourceRoot) {
        JavaProjectBuilder javaProjectBuilder = new JavaProjectBuilder();
        javaProjectBuilder.addSourceTree(sourceRoot);

        SpoonAPI spoonAPI = new Launcher();
        spoonAPI.getEnvironment().setSourceClasspath(classpath);
        spoonAPI.addInputResource(sourceRoot.getAbsolutePath());
        spoonAPI.buildModel();

        List<ClassModel> classModels = javaProjectBuilder
                .getClasses()
                .stream()
                .map(clazz -> codeReader.readClass(clazz, spoonAPI))
                .filter(classModel -> classModel.isDocumented)
                .collect(Collectors.toList());

        Graph graph = TinkerGraph.open();

        List<Vertex> classVertices = classModels.stream()
                .map(classModel -> codebaseModelGraphFacade.classModelToVertex(graph, classModel))
                .collect(Collectors.toList());
        LOGGER.info("Loaded {} classes", classVertices.size());

        return new CodebaseModel(graph, codebaseModelGraphFacade);
    }
}
