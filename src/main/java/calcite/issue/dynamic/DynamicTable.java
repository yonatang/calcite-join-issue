package calcite.issue.dynamic;

import org.apache.calcite.DataContext;
import org.apache.calcite.jdbc.JavaTypeFactoryImpl;
import org.apache.calcite.linq4j.Enumerable;
import org.apache.calcite.rel.type.DynamicRecordType;
import org.apache.calcite.rel.type.DynamicRecordTypeImpl;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rel.type.RelDataTypeFactory;
import org.apache.calcite.schema.ScannableTable;
import org.apache.calcite.schema.impl.AbstractTable;

public class DynamicTable extends AbstractTable implements ScannableTable {
    private final DynamicRecordType recordType = new DynamicRecordTypeImpl(new JavaTypeFactoryImpl());

    @Override
    public RelDataType getRowType(RelDataTypeFactory typeFactory) {
        return recordType;
    }

    @Override
    public Enumerable<Object[]> scan(DataContext root) {
        return new DummyEnumerable(recordType);
    }
}
