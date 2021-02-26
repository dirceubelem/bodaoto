/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fingermidia.bodaoto;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * @author dirceu
 */
public class DAOBase {

    public int insert(Connection c, TOBase t) throws SQLException, Exception {

        boolean temAnterior = false;

        String tabela = Helper.obterNomeTabela(t);
        List<Field> colunas = Helper.obterColunas(t);
        List<Object> valores = new ArrayList<>();

        StringBuilder sql = new StringBuilder();
        sql.append("insert into ").append(tabela);
        sql.append("(");

        StringBuilder parm = new StringBuilder();
        parm.append(") values (");

        for (Field f : colunas) {
            Object o = Helper.runGetter(f, t);

            if (o != null) {
                if (temAnterior) {
                    sql.append(", ");
                    parm.append(", ");
                } else {
                    temAnterior = true;
                }
                valores.add(o);
                sql.append(f.getAnnotation(Column.class).name());
                parm.append("?");
            }
        }

        parm.append(")");

        sql.append(parm);

        return Data.executeUpdate(c, sql.toString(), valores);
    }

    public int update(Connection c, TOBase t) throws Exception {

        boolean temAnterior = false;

        String tabela = Helper.obterNomeTabela(t);
        List<Field> colunasSemChave = Helper.obterColunasSemChave(t);
        List<Field> colunasChave = Helper.obterColunasChave(t);
        List<Object> valores = new ArrayList<>();

        StringBuilder sql = new StringBuilder();
        sql.append("update ").append(tabela).append(" set ");

        for (Field f : colunasSemChave) {
            Object o = Helper.runGetter(f, t);

            if (o != null) {
                if (temAnterior) {
                    sql.append(", ");
                } else {
                    temAnterior = true;
                }
                valores.add(o);
                sql.append(f.getAnnotation(Column.class).name());
                sql.append(" = ?");

            }
        }

        sql.append(" where ");

        temAnterior = false;

        for (Field f : colunasChave) {
            Object o = Helper.runGetter(f, t);

            if (o != null) {
                if (temAnterior) {
                    sql.append(" and ");
                } else {
                    temAnterior = true;
                }
                valores.add(o);
                sql.append(f.getAnnotation(Column.class).name());
                sql.append(" = ?");
            }
        }

        return Data.executeUpdate(c, sql.toString(), valores);
    }

    public int delete(Connection c, TOBase t) throws Exception {

        boolean temAnterior = false;

        String tabela = Helper.obterNomeTabela(t);
        List<Field> colunasChave = Helper.obterColunasChave(t);
        List<Object> valores = new ArrayList<>();

        StringBuilder sql = new StringBuilder();
        sql.append("delete from ").append(tabela);

        sql.append(" where ");

        for (Field f : colunasChave) {
            Object o = Helper.runGetter(f, t);

            if (o != null) {
                if (temAnterior) {
                    sql.append(" and ");
                } else {
                    temAnterior = true;
                }
                valores.add(o);
                sql.append(f.getAnnotation(Column.class).name());
                sql.append(" = ?");
            }
        }

        return Data.executeUpdate(c, sql.toString(), valores);
    }

    public JSONObject obtemJSONResultSet(ResultSet rs, List<Field> colunas) throws Exception {
        JSONObject j = new JSONObject();
        for (Field f : colunas) {

            if (f.getAnnotation(Column.class).showJSON()) {
                String coluna = f.getAnnotation(Column.class).name();
                String nomeJson = f.getAnnotation(Column.class).jsonName();

                if (f.getAnnotation(Column.class).dateTime()) {
                    Timestamp ts = rs.getTimestamp(coluna);
                    DateTime d = new DateTime(ts);
                    j.put(nomeJson, d.toString(f.getAnnotation(Column.class).dateTimeFormat()));
                } else if (f.getType() == String.class) {
                    Object o = rs.getObject(coluna);
                    if (o != null) {
                        String v = (String) o;
                        j.put(nomeJson, v.trim());
                    }
                } else {
                    j.put(nomeJson, rs.getObject(coluna));
                }
            }

        }
        return j;
    }

    public JSONArray list(Connection c, TOBase t) throws Exception {

        boolean temAnterior = false;
        List<Object> valores = new ArrayList<>();

        String tabela = Helper.obterNomeTabela(t);

        List<Field> colunas = Helper.obterColunas(t);
        List<Field> colunasOrdenacao = Helper.obterColunasOrdenacaoPrincipal(t);

        StringBuilder sql = new StringBuilder();
        sql.append("select ");

        for (Field f : colunas) {
            if (temAnterior) {
                sql.append(", ");
            } else {
                temAnterior = true;
            }
            sql.append(f.getAnnotation(Column.class).name());
        }

        sql.append(" from ").append(tabela);

        sql.append(" order by ");

        temAnterior = false;

        if (colunasOrdenacao.size() > 0) {
            for (Field f : colunasOrdenacao) {
                if (temAnterior) {
                    sql.append(", ");
                } else {
                    temAnterior = true;
                }
                sql.append(f.getAnnotation(Column.class).name());
            }
        } else {
            sql.append(" 1 ");
        }

        JSONArray ja = new JSONArray();

        try (ResultSet rs = Data.executeQuery(c, sql.toString(), valores)) {
            while (rs.next()) {

                JSONObject j = obtemJSONResultSet(rs, colunas);

                ja.put(j);

            }
        }

        return ja;
    }

    public JSONObject list(Connection c, TOBase t, String busca, int reg, int pag) throws Exception {
        JSONObject jo = new JSONObject();

        boolean temAnterior = false;

        String tabela = Helper.obterNomeTabela(t);

        List<Field> colunas = Helper.obterColunas(t);
        List<Field> colunasBusca = Helper.obterColunasBusca(t);
        List<Field> colunasOrdenacao = Helper.obterColunasOrdenacaoPrincipal(t);
        List<Object> valores = new ArrayList<>();

        StringBuilder sql = new StringBuilder();
        sql.append("select ");

        for (Field f : colunas) {
            if (temAnterior) {
                sql.append(", ");
            } else {
                temAnterior = true;
            }
            sql.append(f.getAnnotation(Column.class).name());
        }

        sql.append(" from ").append(tabela);

        temAnterior = false;

        if (colunasBusca.size() > 0) {
            sql.append(" where (");
            for (Field f : colunasBusca) {
                if (temAnterior) {
                    sql.append(" or ");
                } else {
                    temAnterior = true;
                }
                sql.append(f.getAnnotation(Column.class).name()).append(" ilike '%' || ? || '%' ");
                valores.add(busca);
            }
            sql.append(" ) ");
        }

        sql.append(" order by ");

        temAnterior = false;

        if (colunasOrdenacao.size() > 0) {
            for (Field f : colunasOrdenacao) {
                if (temAnterior) {
                    sql.append(", ");
                } else {
                    temAnterior = true;
                }
                sql.append(f.getAnnotation(Column.class).name());
            }
        } else {
            sql.append(" 1 ");
        }

        sql.append(" limit ").append(reg).append(" offset ").append(pag);

        JSONArray ja = new JSONArray();

        try (ResultSet rs = Data.executeQuery(c, sql.toString(), valores)) {
            while (rs.next()) {

                JSONObject j = obtemJSONResultSet(rs, colunas);

                ja.put(j);

            }
        }

        jo.put("list", ja);
        jo.put("total", total(c, t, busca));
        jo.put("page", (pag / reg) + 1);
        jo.put("registers", reg);

        return jo;
    }

    public int total(Connection c, TOBase t, String busca) throws Exception {

        List<Field> colunasBusca = Helper.obterColunasBusca(t);

        String tabela = Helper.obterNomeTabela(t);
        List<Object> valores = new ArrayList<>();

        StringBuilder sql = new StringBuilder();
        sql.append("select count(1) as total ");
        sql.append(" from ").append(tabela);

        boolean temAnterior = false;

        if (colunasBusca.size() > 0) {
            sql.append(" where (");
            for (Field f : colunasBusca) {
                if (temAnterior) {
                    sql.append(" or ");
                } else {
                    temAnterior = true;
                }
                sql.append(f.getAnnotation(Column.class).name()).append(" ilike '%' || ? || '%' ");
                valores.add(busca);
            }
            sql.append(" ) ");
        }

        try (ResultSet rs = Data.executeQuery(c, sql.toString(), valores)) {

            if (rs.next()) {
                return rs.getInt("total");
            } else {
                return 0;
            }
        }
    }

    public TOBase get(Connection c, TOBase t) throws Exception {

        boolean temAnterior = false;

        String tabela = Helper.obterNomeTabela(t);

        List<Field> colunas = Helper.obterColunas(t);
        List<Field> colunasChave = Helper.obterColunasChave(t);
        List<Object> valores = new ArrayList<>();

        StringBuilder sql = new StringBuilder();
        sql.append("select ");

        for (Field f : colunas) {
            if (temAnterior) {
                sql.append(", ");
            } else {
                temAnterior = true;
            }
            sql.append(f.getAnnotation(Column.class).name());

        }

        sql.append(" from ").append(tabela);
        sql.append(" where ");

        temAnterior = false;

        for (Field f : colunasChave) {
            Object o = Helper.runGetter(f, t);

            if (o != null) {
                if (temAnterior) {
                    sql.append(" and ");
                } else {
                    temAnterior = true;
                }
                valores.add(o);
                sql.append(f.getAnnotation(Column.class).name());
                sql.append(" = ?");
            }
        }

        try (ResultSet rs = Data.executeQuery(c, sql.toString(), valores)) {
            if (rs.next()) {

                for (Field f : colunas) {
                    String coluna = f.getAnnotation(Column.class).name();
                    Helper.runSetter(coluna, t, rs.getObject(coluna));
                }

                return t;
            }
        }

        return null;
    }
}
