/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fingermidia.bodaoto;

import java.sql.*;
import java.util.List;

/**
 * @author dirceu
 */
public class Data {

    private static final boolean LOG = false;
    private static final boolean LOG_SQL = false;

    public static ResultSet executeQuery(Connection conn, StringBuilder query) throws SQLException {
        return executeQuery(conn, query.toString());
    }

    public static ResultSet executeQuery(Connection conn, String query) throws SQLException {
        Statement sta = conn.createStatement();
        return sta.executeQuery(query);
    }

    public static int executeUpdate(Connection conn, StringBuilder query, Object... parms) throws SQLException {
        return executeUpdate(conn, query.toString(), parms);
    }

    public static int executeUpdate(Connection conn, String query, Object... parms) throws SQLException {

        long inicio = System.currentTimeMillis();

        PreparedStatement pstmt = conn.prepareStatement(query);
        // Recebe os parâmetros da Query
        for (int i = 1; i <= parms.length; i++) {
            pstmt.setObject(i, parms[i - 1]);
        }

        long fim = System.currentTimeMillis();
        long tempo = fim - inicio;

        if (LOG) {
            if (LOG_SQL) {
                System.out.println("SQL: " + query);
            }

            System.out.println("Execute Update: " + tempo);
        }

        return pstmt.executeUpdate();
    }

    public static int executeUpdate(Connection conn, StringBuilder query, Object p) throws SQLException {
        return executeUpdate(conn, query.toString(), p);
    }

    public static int executeUpdate(Connection conn, String query, Object p) throws SQLException {

        long inicio = System.currentTimeMillis();

        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setObject(1, p);

        long fim = System.currentTimeMillis();
        long tempo = fim - inicio;

        if (LOG) {
            if (LOG_SQL) {
                System.out.println("SQL: " + query);
            }

            System.out.println("Execute Update: " + tempo);
        }

        return pstmt.executeUpdate();
    }

    public static int executeUpdate(Connection conn, StringBuilder query, List<Object> p) throws SQLException {
        return executeUpdate(conn, query.toString(), p);
    }

    public static int executeUpdate(Connection conn, String query, List<Object> p) throws SQLException {

        long inicio = System.currentTimeMillis();

        PreparedStatement pstmt = conn.prepareStatement(query);
        // Recebe os parâmetros da Query
        int i = 1;
        for (Object o : p) {
            pstmt.setObject(i++, o);
        }

        int t = pstmt.executeUpdate();

        long fim = System.currentTimeMillis();
        long tempo = fim - inicio;

        if (LOG) {
            if (LOG_SQL) {
                System.out.println("SQL: " + query);
            }

            System.out.println("Execute Update: " + tempo);
        }

        return t;

    }

    public static int executeUpdate(Connection conn, StringBuilder query) throws SQLException {
        return executeUpdate(conn, query.toString());
    }

    public static int executeUpdate(Connection conn, String query) throws SQLException {

        long inicio = System.currentTimeMillis();

        Statement stm = conn.createStatement();
        int t = stm.executeUpdate(query);

        long fim = System.currentTimeMillis();
        long tempo = fim - inicio;

        if (LOG) {
            if (LOG_SQL) {
                System.out.println("SQL: " + query);
            }

            System.out.println("Execute Update: " + tempo);
        }

        return t;
    }

    public static ResultSet executeQuery(Connection conn, StringBuilder query, Object... parms) throws SQLException {
        return executeQuery(conn, query.toString(), parms);
    }

    public static ResultSet executeQuery(Connection conn, String query, Object... parms) throws SQLException {

        long inicio = System.currentTimeMillis();

        PreparedStatement pstmt = conn.prepareStatement(query);
        // Recebe os parâmetros da Query
        for (int i = 1; i <= parms.length; i++) {
            pstmt.setObject(i, retiraInject(parms[i - 1]));
        }

        ResultSet rs = pstmt.executeQuery();

        long fim = System.currentTimeMillis();
        long tempo = fim - inicio;

        if (LOG) {
            if (LOG_SQL) {
                System.out.println("SQL: " + query);
            }

            System.out.println("Execute Query: " + tempo);
        }
        return rs;
    }

    public static ResultSet executeQuery(Connection conn, StringBuilder query, Object p) throws SQLException {
        return executeQuery(conn, query.toString(), p);
    }

    public static ResultSet executeQuery(Connection conn, String query, Object p) throws SQLException {

        long inicio = System.currentTimeMillis();

        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setObject(1, retiraInject(p));

        ResultSet rs = pstmt.executeQuery();

        long fim = System.currentTimeMillis();
        long tempo = fim - inicio;

        if (LOG) {
            if (LOG_SQL) {
                System.out.println("SQL: " + query);
            }

            System.out.println("Execute Query: " + tempo);
        }

        return rs;
    }

    public static ResultSet executeQuery(Connection conn, StringBuilder query, List<Object> p) throws SQLException {
        return executeQuery(conn, query.toString(), p);
    }

    public static ResultSet executeQuery(Connection conn, String query, List<Object> p) throws SQLException {

        long inicio = System.currentTimeMillis();

        PreparedStatement pstmt = conn.prepareStatement(query);
        // Recebe os parâmetros da Query
        int i = 1;
        for (Object o : p) {
            pstmt.setObject(i++, retiraInject(o));
        }

        ResultSet rs = pstmt.executeQuery();

        long fim = System.currentTimeMillis();
        long tempo = fim - inicio;

        if (LOG) {
            if (LOG_SQL) {
                System.out.println("SQL: " + query);
            }

            System.out.println("Execute Query: " + tempo);
        }

        return rs;
    }

    public static Object retiraInject(Object o) {
        if (o != null && o.getClass().getCanonicalName().contains("String")) {
            String s = (String) o;
            o = s.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
        }
        return o;
    }

    public static void closeResultSet(ResultSet rs) throws Exception {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                throw new Exception("Error closing ResultSet : " + e.getMessage());
            }
        }
    }
}
