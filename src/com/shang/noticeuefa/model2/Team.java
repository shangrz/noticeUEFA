package com.shang.noticeuefa.model2;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

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

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public boolean isFollowed() {
        return followed;
    }

    public void setFollowed(boolean followed) {
        this.followed = followed;
    }

    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private String teamName;



    @DatabaseField
    private String teamShortName;//英语字母代替，用于和资源关联
    @DatabaseField(format="yyyy-MM-dd HH:mm",dataType= DataType.DATE_STRING)
    private Date lastModified;//纪录最近一次该球队信息更新时间，用于当服务器有更新，减少update操作的次数，如果modified时间早于server才需要更新
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
