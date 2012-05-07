package com.shang.noticeuefa.model2;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created with IntelliJ IDEA.
 * User: jleo
 * Date: 12-5-6
 * Time: 上午9:45
 * To change this template use File | Settings | File Templates.
 */
@DatabaseTable
public class ContentVersion
{
    public static final String CONTENT = "CONTENT";
    public static final String MATCH = "MATCH";
    public static final String COLUMN_TYPE = "type";

    @DatabaseField
    private int version;

    @DatabaseField
    private String type;

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
