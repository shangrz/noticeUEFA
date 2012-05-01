package com.shang.noticeuefa.model2;


import java.util.Locale;

/**
 * Created with IntelliJ IDEA.
 * User: jleo
 * Date: 12-4-28
 * Time: 下午4:14
 * To change this template use File | Settings | File Templates.
 */
public class Match {
    private int id;
    private Team teamA;
    private Team teamB;
    private long matchTime;//比赛时间
    private String comment;//备注栏，如果比赛时间更改过，可以在这里显示比赛原定时间地点等信息
    private long lastModified;//纪录最近一次该比赛场次信息更新时间，用于当服务器有更新，减少update操作的次数，如果modified时间早于server才需要更新
    private String desc;//比赛描述栏，对该场次比赛的前瞻性报道
   // private String courtName;//场地名称，如果需要应该换成Court类以纪录更多信息
  //  private Locale locale;//比赛发生时区，用于转换时间使用
    private boolean follow;//是否为关注比赛
    private boolean alarm;//是否需要提醒
    private long alarmId;//和系统关联的alarmID,如果需要取消，可以用该id找到设定的闹铃id
    private long info;//比赛阶段信息，比如a组小组赛
    private long tourid;
}
