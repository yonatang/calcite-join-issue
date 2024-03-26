# Calcite DynamicRecordType join issue

This repo demonstrate an issue with Calcite - using custom schema 
that has a table with DynamicRecordType will fail to join with another table,
the generated code will not get compiled, as it will create this Comparator:

```java
new java.util.Comparator(){
      public int compare(Object v0, Object v1) {
        final int c;
        c = org.apache.calcite.runtime.Utilities.compareNullsLastForMergeJoin((Comparable) v0, (Comparable) v1);
        if (c != 0) {
          return c;
        }
        return 0;
      }

      public int compare(Object o0, Object o1) {
        return this.compare(o0, o1);
      }

    }
```

The issue will only reproduce with Calcite 1.23 and above, and only with 
DynamicRecordType rowType.

To reproduce the issue, run the `calcite.issue.Run.main()` method. It will
execute a few queries that works, and one that will fail.