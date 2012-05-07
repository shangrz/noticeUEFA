package com.shang.noticeuefa.model2;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created with IntelliJ IDEA.
 * User: jleo
 * Date: 12-4-28
 * Time: 下午4:14
 * To change this template use File | Settings | File Templates.
 */

@DatabaseTable(tableName = "team")
public class Team {
    public static final String FOLLOWED = "followed";
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getTeamShortName() {
        return teamShortName;
    }

    public void setTeamShortName(String teamShortName) {
        this.teamShortName = teamShortName;
    }


    public boolean isFollowed() {
        return followed;
    }

    public void setFollowed(boolean followed) {
        this.followed = followed;
    }

    @DatabaseField(generatedId = true,allowGeneratedIdInsert = true)
    private int id;
    @DatabaseField
    private String teamName;



    @DatabaseField
    private String teamShortName;//英语字母代替，用于和资源关联

    @DatabaseField
    private boolean followed;//是否为喜爱球队

    @ForeignCollectionField(eager = true)
    ForeignCollection<TeamGroup> teamGroups;


    public ForeignCollection<TeamGroup> getTeamGroups() {
        return teamGroups;
    }

    public void setTeamGroups(ForeignCollection<TeamGroup> teamGroups) {
        this.teamGroups = teamGroups;
    }
}
