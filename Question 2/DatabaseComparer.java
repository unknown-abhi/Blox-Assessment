import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

public class DatabaseComparer {

    // Helper method to get database connection
    public static Connection getConnection(String url, String user, String password) throws Exception {
        return DriverManager.getConnection(url, user, password);
    }

    // Fetch data from a given table
    public static List<Map<String, String>> fetchData(Connection connection, String tableName) throws Exception {
        List<Map<String, String>> dataList = new ArrayList<>();
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName);

        int columnCount = rs.getMetaData().getColumnCount();

        while (rs.next()) {
            Map<String, String> row = new HashMap<>();
            for (int i = 1; i <= columnCount; i++) {
                String columnName = rs.getMetaData().getColumnName(i);
                String value = rs.getString(i);
                row.put(columnName, value);
            }
            dataList.add(row);
        }
        return dataList;
    }

    // Compare two lists of maps (data from original and copied DB)
    public static boolean compareData(List<Map<String, String>> originalData, List<Map<String, String>> copiedData) {
        if (originalData.size() != copiedData.size()) {
            System.out.println("Mismatch in number of records");
            return false;
        }

        for (int i = 0; i < originalData.size(); i++) {
            Map<String, String> originalRow = originalData.get(i);
            Map<String, String> copiedRow = copiedData.get(i);

            if (!originalRow.equals(copiedRow)) {
                System.out.println("Mismatch found in row " + (i + 1));
                System.out.println("Original Row: " + originalRow);
                System.out.println("Copied Row: " + copiedRow);
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        String localDbUrl = "jdbc:mysql://localhost:3306/localdb";
        String cloudDbUrl = "jdbc:mysql://aws-dbs-url:3306/clouddb";
        String username = "username";
        String password = "password";
        String tableName = "table_name";

        try {
            // Connect to both databases
            Connection localConnection = getConnection(localDbUrl, username, password);
            Connection cloudConnection = getConnection(cloudDbUrl, username, password);

            // Fetch data from both databases
            List<Map<String, String>> localData = fetchData(localConnection, tableName);
            List<Map<String, String>> cloudData = fetchData(cloudConnection, tableName);

            // Compare data
            boolean isDataIdentical = compareData(localData, cloudData);

            if (isDataIdentical) {
                System.out.println("Data is identical in both databases.");
            } else {
                System.out.println("Data mismatch found!");
            }

            // Close connections
            localConnection.close();
            cloudConnection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
