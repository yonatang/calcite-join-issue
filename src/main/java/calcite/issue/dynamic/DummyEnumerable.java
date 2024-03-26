package calcite.issue.dynamic;

import org.apache.calcite.linq4j.AbstractEnumerable;
import org.apache.calcite.linq4j.Enumerator;
import org.apache.calcite.rel.type.RelDataType;

public class DummyEnumerable extends AbstractEnumerable<Object[]> {
    private final RelDataType recordType;

    public DummyEnumerable(RelDataType recordType) {
        this.recordType = recordType;
    }

    @Override
    public Enumerator<Object[]> enumerator() {
        return new Enumerator<>() {
            private int i = 0;

            @Override
            public Object[] current() {
                return recordType.getFieldNames().stream().map(f -> f + "_" + i).toArray();
            }

            @Override
            public boolean moveNext() {
                return i++ < 3;
            }

            @Override
            public void reset() {
                i = 0;
            }

            @Override
            public void close() {

            }
        };
    }
}
