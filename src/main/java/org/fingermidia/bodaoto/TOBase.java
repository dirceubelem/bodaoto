/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fingermidia.bodaoto;

import org.json.JSONObject;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.util.List;

@Table(name = "base")
public class TOBase {

    public JSONObject getJson() throws Exception {
        return Helper.createJSON(this);
    }

    public String getColumns(String alias) throws Exception {
        return Helper.obterColunasString(this, alias);
    }

    public String getColumns(String alias, String prename) throws Exception {
        return Helper.obterColunasString(this, alias, prename);
    }

    public String getColumns() throws Exception {
        return Helper.obterColunasString(this);
    }

    public String getTableName() throws Exception {
        return Helper.obterNomeTabela(this);
    }

    public void load(ResultSet rs) throws Exception {

        List<Field> colunas = Helper.obterColunas(this);

        String coluna;
        for (Field f : colunas) {
            coluna = f.getAnnotation(Column.class).name();
            Helper.runSetter(coluna, this, rs.getObject(coluna));
        }

    }

    public void load(ResultSet rs, String alias) throws Exception {

        List<Field> colunas = Helper.obterColunas(this);

        String coluna;
        for (Field f : colunas) {
            coluna = f.getAnnotation(Column.class).name();
            Helper.runSetter(coluna, this, rs.getObject(alias + "_" + coluna));
        }

    }
}
