package calcite.issue.dynamic;

import org.apache.calcite.schema.Schema;
import org.apache.calcite.schema.SchemaFactory;
import org.apache.calcite.schema.SchemaPlus;

import java.util.Map;

public class MySchemaFactory implements SchemaFactory {
    @Override
    public Schema create(SchemaPlus parentSchema, String name, Map<String, Object> operand) {
        return new MySchema();
    }
}
