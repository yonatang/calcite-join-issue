package calcite.issue.dynamic;

import org.apache.calcite.schema.Table;
import org.apache.calcite.schema.impl.AbstractSchema;

import java.util.Map;

public class MySchema extends AbstractSchema {
    private final DynamicTable table = new DynamicTable();
    private final StaticTable staticTable = new StaticTable();

    @Override
    protected Map<String, Table> getTableMap() {
        return Map.of("dynamic_table", table, "static_table", staticTable);
    }
}

