package calcite.issue.dynamic;

import org.apache.calcite.DataContext;
import org.apache.calcite.linq4j.Enumerable;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rel.type.RelDataTypeFactory;
import org.apache.calcite.schema.ScannableTable;
import org.apache.calcite.schema.impl.AbstractTable;
import org.apache.calcite.sql.type.SqlTypeName;
import org.apache.calcite.util.Pair;

import java.util.List;

public class StaticTable extends AbstractTable implements ScannableTable {
    @Override
    public Enumerable<Object[]> scan(DataContext root) {
        return new DummyEnumerable(getRowType(root.getTypeFactory()));
    }

    @Override
    public RelDataType getRowType(RelDataTypeFactory typeFactory) {
        var aType = typeFactory.createSqlType(SqlTypeName.VARCHAR);
        var bType = typeFactory.createSqlType(SqlTypeName.VARCHAR);
        var pairs = List.of(Pair.of("a", aType), Pair.of("b", bType));
        return typeFactory.createStructType(pairs);
    }
}
