package calcite.issue;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Run {

    public static void main(String[] args) throws Exception {
        prepareDebug();
        var props = props();
        try (var c = DriverManager.getConnection("jdbc:calcite:", props)) {
            System.out.println("* This works as expected, both for dynamic and static tables:");
            runQuery(c, "SELECT a,b FROM dynamic_table");
            runQuery(c, "SELECT a,b FROM static_table");

            System.out.println("* This also works as expected, both for dynamic and static tables:");
            runQuery(c, "SELECT d1.a,d2.a FROM dynamic_table d1, dynamic_table d2");
            runQuery(c, "SELECT d1.a,d2.a FROM static_table d1, static_table d2");

            System.out.println("* This works as expected for static tables:");
            runQuery(c, "SELECT d1.a ,d2.a FROM static_table d1 join static_table d2 on d1.a=d2.a");

            System.out.println("* With dynamic tables, brace for impact:");
            runQuery(c, "SELECT d1.a,d2.a FROM dynamic_table d1 join dynamic_table d2 on d1.a=d2.a");

            /* The class that won't compile has this Comparator with
               two int compare(Object o0, Object o1) methods:

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
            */
        }

    }

    private static void prepareDebug() {
        var generatedCodeDir = new File("./target/generated-code");
        // delete the content of the generated code directory
        generatedCodeDir.mkdirs();
        for (var file : generatedCodeDir.listFiles()) {
            file.delete();
        }
        System.setProperty("org.codehaus.janino.source_debugging.enable", "true");
        System.setProperty("org.codehaus.janino.source_debugging.dir", generatedCodeDir.toString());
        System.setProperty("org.codehaus.janino.source_debugging.keep", "true");
    }

    private static Properties props() throws Exception {
        String schema = new String(Run.class.getResourceAsStream("/schemas.json").readAllBytes());
        Properties props = new Properties();
        props.put("model", "inline:" + schema);
        props.put("lex", "JAVA");
        return props;


    }

    private static void runQuery(Connection connection, String query) throws SQLException {
        System.out.println("Running query");
        System.out.println(query);
        System.out.println("=".repeat(query.length()));
        try (var statement = connection.createStatement()) {
            try (var resultSet = statement.executeQuery(query)) {
                var md = resultSet.getMetaData();
                var counter = 1;
                while (resultSet.next()) {
                    System.out.print("Row " + counter++ + ": ");
                    for (var i = 0; i < md.getColumnCount(); i++) {
                        System.out.print(md.getColumnName(i + 1) + "=" + resultSet.getString(i + 1) + " ");
                    }
                    System.out.println();
                }
            }
        }
        System.out.println();
    }
}
