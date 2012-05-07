package com.shang.noticeuefa.model2;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.security.PrivateKey;

/**
 * Created with IntelliJ IDEA.
 * User: jleo
 * Date: 12-5-4
 * Time: 上午1:47
 * To change this template use File | Settings | File Templates.
 */
@DatabaseTable(tableName = "team_group")
public class TeamGroup {
    @DatabaseField(generatedId = true)
    public int id;
    @DatabaseField(foreign = true,foreignAutoRefresh=true)
    private Team team;
    @DatabaseField(foreign = true,foreignAutoRefresh = true)
    private Group group;

    private int teamId;

    private int groupId;

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
}
