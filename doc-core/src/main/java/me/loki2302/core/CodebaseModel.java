package me.loki2302.core;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.util.iterator.IteratorUtils;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

public class CodebaseModel {
    private final Graph graph;

    public CodebaseModel(Graph graph) {
        this.graph = graph;
    }

    public List<ClassModel> findClassesByFile(File source) {
        GraphTraversal<Vertex, Vertex> traversal = graph.traversal().V().has("source", source);
        List<Vertex> vertices = IteratorUtils.list(traversal);
        return vertices.stream()
                .map(CodebaseModel::readClassModelFromVertex)
                .collect(Collectors.toList());
    }

    public List<ClassModel> findClassesByStereotype(String stereotype) {
        GraphTraversal<Vertex, Vertex> traversal = graph.traversal().V().has("stereotype", stereotype);
        List<Vertex> vertices = IteratorUtils.list(traversal);
        return vertices.stream()
                .map(CodebaseModel::readClassModelFromVertex)
                .collect(Collectors.toList());
    }

    private static ClassModel readClassModelFromVertex(Vertex classVertex) {
        return new ClassModel(
                classVertex.value("name"),
                classVertex.value("stereotype"),
                classVertex.value("description"),
                classVertex.value("source"));
    }
}
