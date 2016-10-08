package me.loki2302.core;

import me.loki2302.core.models.ClassModel;
import me.loki2302.core.models.FieldModel;
import me.loki2302.core.models.MethodModel;
import org.apache.tinkerpop.gremlin.structure.Direction;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.util.iterator.IteratorUtils;

import java.util.stream.Collectors;

public class CodebaseModelGraphFacade {
    public Vertex classModelToVertex(Graph graph, ClassModel classModel) {
        Vertex classVertex = graph.addVertex(classModel.fullName);
        classVertex.property("type", "class");
        classVertex.property("fullName", classModel.fullName);
        classVertex.property("name", classModel.name);
        classVertex.property("source", classModel.source);
        classVertex.property("isDocumented", classModel.isDocumented);
        classVertex.property("description", classModel.description);
        classVertex.property("stereotype", classModel.stereotype);
        classVertex.property("errors", classModel.errors);

        classModel.methods.stream()
                .map(methodModel -> methodModelToVertex(graph, methodModel))
                .collect(Collectors.toList())
                .forEach(methodVertex -> classVertex.addEdge("has-method", methodVertex));

        classModel.fields.stream()
                .map(fieldModel -> fieldModelToVertex(graph, fieldModel))
                .collect(Collectors.toList())
                .forEach(fieldVertex -> classVertex.addEdge("has-field", fieldVertex));

        return classVertex;
    }

    public ClassModel classModelFromVertex(Vertex classVertex) {
        ClassModel classModel = new ClassModel();
        classModel.name = classVertex.value("name");
        classModel.fullName = classVertex.value("fullName");
        classModel.source = classVertex.value("source");
        classModel.isDocumented = classVertex.value("isDocumented");
        classModel.description = classVertex.value("description");
        classModel.stereotype = classVertex.value("stereotype");
        classModel.errors = classVertex.value("errors");

        classModel.methods = IteratorUtils.list(classVertex.edges(Direction.OUT, "has-method")).stream()
                .map(e -> e.inVertex())
                .map(this::methodModelFromVertex)
                .collect(Collectors.toList());

        classModel.fields = IteratorUtils.list(classVertex.edges(Direction.OUT, "has-field")).stream()
                .map(e -> e.inVertex())
                .map(this::fieldModelFromVertex)
                .collect(Collectors.toList());

        return classModel;
    }

    public Vertex methodModelToVertex(Graph graph, MethodModel methodModel) {
        Vertex methodVertex = graph.addVertex(methodModel.fullName);
        methodVertex.property("type", "method");
        methodVertex.property("fullName", methodModel.fullName);
        methodVertex.property("name", methodModel.name);
        methodVertex.property("description", methodModel.description);
        methodVertex.property("isDocumented", methodModel.isDocumented);
        methodVertex.property("parameters", methodModel.parameters);
        methodVertex.property("errors", methodModel.errors);
        return methodVertex;
    }

    public MethodModel methodModelFromVertex(Vertex methodVertex) {
        MethodModel methodModel = new MethodModel();
        methodModel.name = methodVertex.value("name");
        methodModel.fullName = methodVertex.value("fullName");
        methodModel.description = methodVertex.value("description");
        methodModel.isDocumented = methodVertex.value("isDocumented");
        methodModel.parameters = methodVertex.value("parameters");
        methodModel.errors = methodVertex.value("errors");
        return methodModel;
    }

    public Vertex fieldModelToVertex(Graph graph, FieldModel fieldModel) {
        Vertex methodVertex = graph.addVertex(fieldModel.fullName);
        methodVertex.property("type", "field");
        methodVertex.property("fullName", fieldModel.fullName);
        methodVertex.property("name", fieldModel.name);
        methodVertex.property("typeName", fieldModel.typeName);
        methodVertex.property("description", fieldModel.description);
        methodVertex.property("isDocumented", fieldModel.isDocumented);
        methodVertex.property("errors", fieldModel.errors);
        return methodVertex;
    }

    public FieldModel fieldModelFromVertex(Vertex fieldVertex) {
        FieldModel fieldModel = new FieldModel();
        fieldModel.fullName = fieldVertex.value("fullName");
        fieldModel.name = fieldVertex.value("name");
        fieldModel.typeName = fieldVertex.value("typeName");
        fieldModel.description = fieldVertex.value("description");
        fieldModel.isDocumented = fieldVertex.value("isDocumented");
        fieldModel.errors = fieldVertex.value("errors");
        return fieldModel;
    }
}
