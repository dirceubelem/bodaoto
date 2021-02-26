/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fingermidia.bodaoto;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Connection;

/**
 * @author dirceu.belem
 */
public class BOFactory {

    public static void insert(Connection c, TOBase to) throws Exception {
        DAOBase b = new DAOBase();
        b.insert(c, to);
    }

    public static void update(Connection c, TOBase to) throws Exception {
        DAOBase b = new DAOBase();
        b.update(c, to);
    }

    public static int delete(Connection c, TOBase to) throws Exception {
        DAOBase b = new DAOBase();
        return b.delete(c, to);
    }

    public static TOBase get(Connection c, TOBase to) throws Exception {
        DAOBase b = new DAOBase();
        return b.get(c, to);
    }

    public static JSONObject list(Connection c, TOBase t, String search, int reg, int pag) throws Exception {
        DAOBase b = new DAOBase();
        return b.list(c, t, search, reg, pag);
    }

    public static JSONArray list(Connection c, TOBase t) throws Exception {
        DAOBase b = new DAOBase();
        return b.list(c, t);
    }

}
