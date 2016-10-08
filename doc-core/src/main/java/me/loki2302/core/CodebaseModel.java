package me.loki2302.core;

import me.loki2302.core.models.ClassModel;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.util.iterator.IteratorUtils;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

public class CodebaseModel {
    private final Graph graph;
    private final CodebaseModelGraphFacade codebaseModelGraphFacade;

    public CodebaseModel(
            Graph graph,
            CodebaseModelGraphFacade codebaseModelGraphFacade) {

        this.graph = graph;
        this.codebaseModelGraphFacade = codebaseModelGraphFacade;
    }

    public List<ClassModel> findAllClasses() {
        GraphTraversal<Vertex, Vertex> traversal = graph.traversal().V().has("type", "class");
        List<Vertex> vertices = IteratorUtils.list(traversal);
        return vertices.stream()
                .map(codebaseModelGraphFacade::classModelFromVertex)
                .collect(Collectors.toList());
    }

    public List<ClassModel> findClassesByFile(File source) {
        GraphTraversal<Vertex, Vertex> traversal = graph.traversal().V().has("type", "class").has("source", source);
        List<Vertex> vertices = IteratorUtils.list(traversal);
        return vertices.stream()
                .map(codebaseModelGraphFacade::classModelFromVertex)
                .collect(Collectors.toList());
    }

    public List<ClassModel> findClassesByStereotype(String stereotype) {
        GraphTraversal<Vertex, Vertex> traversal = graph.traversal().V().has("type", "class").has("stereotype", stereotype);
        List<Vertex> vertices = IteratorUtils.list(traversal);
        return vertices.stream()
                .map(codebaseModelGraphFacade::classModelFromVertex)
                .collect(Collectors.toList());
    }
}
