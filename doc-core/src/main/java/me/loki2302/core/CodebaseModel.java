package me.loki2302.core;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.structure.Direction;
import org.apache.tinkerpop.gremlin.structure.Edge;
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

    public List<ClassModel> findAllClasses() {
        // TODO: introduce vertex types
        GraphTraversal<Vertex, Vertex> traversal = graph.traversal().V().has("stereotype");
        List<Vertex> vertices = IteratorUtils.list(traversal);
        return vertices.stream()
                .map(CodebaseModel::readClassModelFromVertex)
                .collect(Collectors.toList());
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
        List<Edge> fieldEdges = IteratorUtils.list(classVertex.edges(Direction.OUT, "has-field"));
        List<FieldModel> fieldModels = fieldEdges.stream()
                .map(e -> e.inVertex()).map(v -> new FieldModel(
                        v.value("fullName"),
                        v.value("name"),
                        v.value("type"),
                        v.value("description")))
                .collect(Collectors.toList());

        List<Edge> methodEdges = IteratorUtils.list(classVertex.edges(Direction.OUT, "has-method"));
        List<MethodModel> methodModels = methodEdges.stream()
                .map(e -> e.inVertex()).map(v -> new MethodModel(
                        v.value("fullName"),
                        v.value("name"),
                        v.value("description")))
                .collect(Collectors.toList());

        return new ClassModel(
                classVertex.value("fullName"),
                classVertex.value("name"),
                classVertex.value("stereotype"),
                classVertex.value("description"),
                classVertex.value("source"),
                fieldModels,
                methodModels);
    }
}
