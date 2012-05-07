package com.shang.noticeuefa.model2;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created with IntelliJ IDEA.
 * User: jleo
 * Date: 12-5-3
 * Time: 下午11:47
 * To change this template use File | Settings | File Templates.
 */
@DatabaseTable(tableName = "grp")
public class Group {
    public static final String GROUP_NAME = "name";

    @DatabaseField(generatedId = true, allowGeneratedIdInsert = true)
    private int id;

    @DatabaseField
    private String name;

    @ForeignCollectionField(eager = true)
    ForeignCollection<TeamGroup> teamGroups;

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

    public ForeignCollection<TeamGroup> getTeamGroups() {
        return teamGroups;
    }

    public void setTeamGroups(ForeignCollection<TeamGroup> teamGroups) {
        this.teamGroups = teamGroups;
    }
}
