package com.example.mysql;

import com.example.mysql.models.Cell;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

public class MysqlHelperClass {

    private static final String URL = "jdbc:mysql://192.168.1.100:3308/";
    private static final String URL2 = "jdbc:mysql://";
    private static final String USER = "anyuser";
    private static final String PASSWORD = "";



    public ArrayList<String> getAllDatabases(String ipAddress, String port, String database, String user, String password) throws SQLException {
        ArrayList<String> databaseNames = new ArrayList<>();

        Connection connection = DriverManager.getConnection(URL2+ipAddress+":"+port+"/"+database, user, password);
        String sql = "Show Databases";
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            String data = resultSet.getString(1);
            if (data == null)
                data = "";
            databaseNames.add(data);
        }
        connection.close();
        return databaseNames;
    }

    public ArrayList<String> getDatabaseTables(String ipAddress, String port, String database, String user, String password) throws SQLException {
        ArrayList<String> tables = new ArrayList<>();

        Connection connection = DriverManager.getConnection(URL2+ipAddress+":"+port+"/"+database, user, password);

        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet resultSet = metaData.getTables(null, null, "%", null);
        while (resultSet.next()) {
            String data = resultSet.getString(3);
            if (data == null)
                data = "";
            tables.add(data);
        }
        connection.close();
        return tables;
    }

     private HashMap<String, Object> getColumns(String ipAddress, String port, String database, String user, String password, String tableName) throws SQLException {
        ArrayList<Integer> columnsLength = new ArrayList<>();
        ArrayList<Cell> columns = new ArrayList<>();

        HashMap<String, Object> columnsData = new HashMap<>();
        Connection connection = DriverManager.getConnection(URL2+ipAddress+":"+port+"/"+database, user, password);
        Statement st = connection.createStatement();
            String myQuery = "SELECT * FROM %s";
            myQuery = String.format(myQuery,tableName);
            ResultSet rs = st.executeQuery(myQuery);
            ResultSetMetaData rsMetaData = rs.getMetaData();
            for (int colNum = 1; colNum <= rsMetaData.getColumnCount()  ; colNum++) {
                Cell cell = new Cell((rsMetaData.getColumnName(colNum)));
                if (cell.getData() == null)
                    cell.setData("");
                columns.add(cell);
                columnsLength.add(cell.getData().toString().length());
            }
        connection.close();
            columnsData.put("columns", columns);
            columnsData.put("columnsLength", columnsLength);
        return  columnsData;
    }

    public HashMap<String, Object> getTableRecords(String ipAddress, String port, String database, String user, String password, String tableName) throws SQLException {
        ArrayList<ArrayList<Cell>> tableRecords = new ArrayList<>();
        HashMap<String, Object> columnsData = getColumns(ipAddress, port, database, user, password, tableName);
        ArrayList<Cell> tableColumns = (ArrayList<Cell>) columnsData.get("columns");
        ArrayList<Integer> maxLengthPerColumn = (ArrayList<Integer>) columnsData.get("columnsLength");
        Connection connection = DriverManager.getConnection(URL2+ipAddress+":"+port+"/"+database, user, password);

        tableRecords.add(tableColumns);

        String query = "SELECT * FROM %s";
        query = String.format(query, tableName);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()) {
            ArrayList<Cell> rowData = new ArrayList<>();
            for (Cell column : tableColumns) {
                Object cellData = resultSet.getObject(column.getData().toString());
                if(cellData == null)
                    cellData = "";
                rowData.add(new Cell(cellData));
                int columnIndex =  tableColumns.indexOf(column);
                int cellDataLength = cellData.toString().length();
                if(cellDataLength > maxLengthPerColumn.get(columnIndex))
                    maxLengthPerColumn.set(columnIndex, cellDataLength);
            }
            tableRecords.add(rowData);
        }
        connection.close();
        resizeEachCell(tableRecords, maxLengthPerColumn);
        HashMap<String, Object> data = new HashMap<>();
        data.put("columns", tableRecords.get(0));
        tableRecords.remove(0);
        data.put("records", tableRecords);
        return data;
    }

    public HashMap<String, ArrayList<String>> connect(String ipAddress, String port, String database, String user, String password) throws SQLException {

        ArrayList<String> databases = getAllDatabases(ipAddress, port, database, user, password);
        ArrayList<String> tables = getDatabaseTables(ipAddress, port, database, user, password);
        HashMap<String, ArrayList<String>> data = new HashMap<>();
        data.put("databases", databases);
        data.put("tables", tables);
        return data;
    }

    private void resizeEachCell(ArrayList<ArrayList<Cell>> rows, ArrayList<Integer> columnsLength){
        if(rows != null)
            for(ArrayList<Cell> cellArrayList : rows) {

                for(int i = 0; i < cellArrayList.size(); i++) {
                    int maxCellLengthAllowed = columnsLength.get(i);
                    int cellLength = cellArrayList.get(i).getData().toString().length();
                    if(cellLength < maxCellLengthAllowed) {
                        int spacesToAdd = maxCellLengthAllowed - cellLength;
                        String spaces = "";
                        for (int j = 0; j < spacesToAdd; j++) {
                            spaces = spaces.concat(" ");
                        }
                        cellArrayList.get(i).setData(cellArrayList.get(i).getData().toString().concat(spaces));
                    }
                }
            }
    }
}


