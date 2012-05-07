package com.shang.noticeuefa.model2;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created with IntelliJ IDEA.
 * User: jleo
 * Date: 12-5-2
 * Time: 上午4:27
 * To change this template use File | Settings | File Templates.
 */
@DatabaseTable(tableName = "tour")
public class Tour {
    public static final String TOUR_NAME = "name";

    @DatabaseField(generatedId = true,allowGeneratedIdInsert = true)
    private int id;

    @DatabaseField
    private String name;

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    @DatabaseField
    private String shortName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
