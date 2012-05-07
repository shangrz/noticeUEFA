package com.shang.noticeuefa.model2;


import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: jleo
 * Date: 12-4-28
 * Time: 下午4:14
 * To change this template use File | Settings | File Templates.
 */
@DatabaseTable(tableName = "match")
public class Match {
    @DatabaseField(generatedId = true,allowGeneratedIdInsert = true)
    private int id;
    @DatabaseField(canBeNull = false, foreign = true,columnName = "teamA")
    private Team teamA;
    @DatabaseField(canBeNull = false, foreign = true,columnName = "teamB")
    private Team teamB;
    @DatabaseField(format="yyyy-MM-dd HH:mm",dataType= DataType.DATE_STRING)
    private Date matchTime;//比赛时间
    @DatabaseField
    private String comment;//备注栏，如果比赛时间更改过，可以在这里显示比赛原定时间地点等信息


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Team getTeamA() {
        return teamA;
    }

    public void setTeamA(Team teamA) {
        this.teamA = teamA;
    }

    public Team getTeamB() {
        return teamB;
    }

    public void setTeamB(Team teamB) {
        this.teamB = teamB;
    }

    public Date getMatchTime() {
        return matchTime;
    }

    public void setMatchTime(Date matchTime) {
        this.matchTime = matchTime;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Tour getTour() {
        return tour;
    }

    public void setTour(Tour tour) {
        this.tour = tour;
    }

    public String getScoreA() {
        return scoreA;
    }

    public void setScoreA(String scoreA) {
        this.scoreA = scoreA;
    }

    public String getScoreB() {
        return scoreB;
    }

    public void setScoreB(String scoreB) {
        this.scoreB = scoreB;
    }



    @DatabaseField(format="yyyy-MM-dd HH:mm",dataType= DataType.DATE_STRING)
    private Date lastModified;//纪录最近一次该比赛场次信息更新时间，用于当服务器有更新，减少update操作的次数，如果modified时间早于server才需要更新
    @DatabaseField
    private String desc;//比赛描述栏，对该场次比赛的前瞻性报道


    @DatabaseField
    private String info;//比赛阶段信息，比如a组小组赛
    @DatabaseField(canBeNull = false, foreign = true,columnName = "tourid")
    private Tour tour;

    @DatabaseField
    private String scoreA;

    @DatabaseField
    private String scoreB;

    public Notification getNotifications() {
        return notifications;
    }

    public void setNotifications(Notification notifications) {
        this.notifications = notifications;
    }

    @DatabaseField(foreign = true,foreignAutoRefresh=true)
    Notification notifications;

    private int teamAId;
    private int teamBId;
    private int tourId;

    public int getTeamAId() {
        return teamAId;
    }

    public void setTeamAId(int teamAId) {
        this.teamAId = teamAId;
    }

    public int getTeamBId() {
        return teamBId;
    }

    public void setTeamBId(int teamBId) {
        this.teamBId = teamBId;
    }

    public int getTourId() {
        return tourId;
    }

    public void setTourId(int tourId) {
        this.tourId = tourId;
    }
}
