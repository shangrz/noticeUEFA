package com.shang.noticeuefa.model2;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created with IntelliJ IDEA.
 * User: jleo
 * Date: 12-5-4
 * Time: 上午2:31
 * To change this template use File | Settings | File Templates.
 */
@DatabaseTable(tableName = "notification")
public class Notification {
    @DatabaseField(foreign = true,foreignAutoRefresh=true)
    private Match match;

    @DatabaseField(generatedId = true)
    private int id;

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isFollow() {
        return follow;
    }

    public void setFollow(boolean follow) {
        this.follow = follow;
    }

    public boolean isAlarm() {
        return alarm;
    }

    public void setAlarm(boolean alarm) {
        this.alarm = alarm;
    }

    @DatabaseField
    private boolean follow;//是否为关注比赛
    @DatabaseField
    private boolean alarm;//是否需要提醒


}
