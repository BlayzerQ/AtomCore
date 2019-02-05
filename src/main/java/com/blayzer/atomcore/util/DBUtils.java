package com.blayzer.atomcore.util;

import java.sql.*;

import net.minecraft.util.math.BlockPos;

public class DBUtils {

    private static Connection connection;
    private static Statement statement;
    private static ResultSet resultSet;

    public static void createDBConnection(String path) throws ClassNotFoundException, SQLException {
        connection = null;
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite://" + path + "/AtomCore.db");
    }

    public static void createDBTable() throws SQLException {
        statement = connection.createStatement();
        statement.execute("CREATE TABLE if not exists 'blocks' ('key' string PRIMARY KEY, 'x' int, 'y' int, 'z' int);");
    }

    public static void writeToDB(String key, BlockPos pos) throws SQLException {
    	int x = pos.getX();
    	int y = pos.getY();
    	int z = pos.getZ();
        statement.execute("INSERT OR REPLACE INTO 'blocks' ('key', 'x', 'y', 'z') VALUES ('" + key + "', '" + x + "', '" + y + "', '" + z + "');");
    }

    public static boolean hasDBValue(String key) throws SQLException {
        resultSet = statement.executeQuery("SELECT * FROM blocks WHERE key='" + key + "';");
        return resultSet.next();
    }
    
    public static BlockPos getDBValues(String key) throws SQLException {
        resultSet = statement.executeQuery("SELECT * FROM blocks WHERE key='" + key + "';");
        if(!resultSet.next()) return null;
        else {
        	return new BlockPos(resultSet.getInt("x"), resultSet.getInt("y"), resultSet.getInt("z"));
        }
    }

    public static void deleteFromDB(String key) throws SQLException {
        statement.executeUpdate("DELETE FROM blocks WHERE key='" + key + "';");
    }

    public static void closeSQLiteDBConnection() throws SQLException {
        connection.close();
        statement.close();
        if (resultSet != null)
            resultSet.close();
    }
}
