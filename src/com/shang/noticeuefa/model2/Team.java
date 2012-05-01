package com.shang.noticeuefa.model2;

/**
 * Created with IntelliJ IDEA.
 * User: jleo
 * Date: 12-4-28
 * Time: 下午4:14
 * To change this template use File | Settings | File Templates.
 */
public class Team {
    private int id;
    private String teamName;
    private String teamShortName;//英语字母代替，用于和资源关联
    private long lastModified;//纪录最近一次该球队信息更新时间，用于当服务器有更新，减少update操作的次数，如果modified时间早于server才需要更新
    private boolean followed;//是否为喜爱球队
}
