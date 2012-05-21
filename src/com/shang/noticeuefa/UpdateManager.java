package com.shang.noticeuefa;

import android.app.Activity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.shang.noticeuefa.database.DatabaseHelper;
import com.shang.noticeuefa.model2.*;
import com.srz.androidtools.util.PreferenceUtil;
import com.srz.androidtools.util.StringQuery;
import com.srz.androidtools.util.TimeTools;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jleo
 * Date: 12-5-5
 * Time: 下午2:54
 * To change this template use File | Settings | File Templates.
 */
public class UpdateManager {
    private Activity activity;
    private DatabaseHelper databaseHelper;

    public UpdateManager(Activity activity) {
        this.activity = activity;
    }


    public boolean checkUpdate(final String host, boolean block) {
        final boolean[] updated = {false};
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                StringQuery squery = new StringQuery(activity, "http://" + host + "/match-version?type=content");
                String updateContentVersion = squery.invoke();
                if (updateContentVersion == null) {
                    if (PreferenceUtil.isFirstTimeBoot(activity)) {
                        updated[0] = true;
                        updateContentVersion = "1";
                    } else {
                        updated[0] = false;
                        return;
                    }
                }

                squery = new StringQuery(activity, "http://" + host + "/match-version?type=match");
                String matchJSONVersion = squery.invoke();
                if (matchJSONVersion == null) {
                    if (PreferenceUtil.isFirstTimeBoot(activity)) {

                        updated[0] = true;
                        matchJSONVersion = "1";
                    } else {
                        updated[0] = false;
                        return;
                    }

                }

                int serverContentVersion = Integer.valueOf(updateContentVersion.trim());
                int serverMatchVersion = Integer.valueOf(matchJSONVersion.trim());

                try {
                    databaseHelper = OpenHelperManager.getHelper(activity, DatabaseHelper.class);
                    Dao<ContentVersion, Integer> versionDao = databaseHelper.getDao(ContentVersion.class);


                    int contentVersion = versionDao.queryForEq(ContentVersion.COLUMN_TYPE, ContentVersion.CONTENT).size() == 0 ? -1 : versionDao.queryForEq(ContentVersion.COLUMN_TYPE, ContentVersion.CONTENT).get(0).getVersion();
                    int matchVersion = versionDao.queryForEq(ContentVersion.COLUMN_TYPE, ContentVersion.MATCH).size() == 0 ? -1 :
                            versionDao.queryForEq(ContentVersion.COLUMN_TYPE, ContentVersion.MATCH).get(0).getVersion();
                    Gson gson = new Gson();
                    Dao<Team, Integer> teamDao = databaseHelper.getDao(Team.class);
                    if (contentVersion < serverContentVersion) {
                        squery = new StringQuery(activity, "http://" + host + "/match?type=type");
                        String updateContent = squery.invoke();
                        if (updateContent == null) {
                            if (PreferenceUtil.isFirstTimeBoot(activity)) {
                                updated[0] = true;
                                updateContent = "{\"teams\":\"[{\\\"id\\\":1,\\\"teamName\\\":\\\"波兰\\\",\\\"teamShortName\\\":\\\"pol\\\",\\\"followed\\\":false},{\\\"id\\\":2,\\\"teamName\\\":\\\"希腊\\\",\\\"teamShortName\\\":\\\"gre\\\",\\\"followed\\\":false},{\\\"id\\\":3,\\\"teamName\\\":\\\"俄罗斯\\\",\\\"teamShortName\\\":\\\"rus\\\",\\\"followed\\\":false},{\\\"id\\\":4,\\\"teamName\\\":\\\"捷克\\\",\\\"teamShortName\\\":\\\"cze\\\",\\\"followed\\\":false},{\\\"id\\\":5,\\\"teamName\\\":\\\"荷兰\\\",\\\"teamShortName\\\":\\\"ned\\\",\\\"followed\\\":false},{\\\"id\\\":6,\\\"teamName\\\":\\\"丹麦\\\",\\\"teamShortName\\\":\\\"den\\\",\\\"followed\\\":false},{\\\"id\\\":7,\\\"teamName\\\":\\\"德国\\\",\\\"teamShortName\\\":\\\"ger\\\",\\\"followed\\\":false},{\\\"id\\\":8,\\\"teamName\\\":\\\"葡萄牙\\\",\\\"teamShortName\\\":\\\"por\\\",\\\"followed\\\":false},{\\\"id\\\":9,\\\"teamName\\\":\\\"西班牙\\\",\\\"teamShortName\\\":\\\"esp\\\",\\\"followed\\\":false},{\\\"id\\\":10,\\\"teamName\\\":\\\"意大利\\\",\\\"teamShortName\\\":\\\"ita\\\",\\\"followed\\\":false},{\\\"id\\\":11,\\\"teamName\\\":\\\"爱尔兰\\\",\\\"teamShortName\\\":\\\"irl\\\",\\\"followed\\\":false},{\\\"id\\\":12,\\\"teamName\\\":\\\"克罗地亚\\\",\\\"teamShortName\\\":\\\"cro\\\",\\\"followed\\\":false},{\\\"id\\\":13,\\\"teamName\\\":\\\"法国\\\",\\\"teamShortName\\\":\\\"fra\\\",\\\"followed\\\":false},{\\\"id\\\":14,\\\"teamName\\\":\\\"英格兰\\\",\\\"teamShortName\\\":\\\"eng\\\",\\\"followed\\\":false},{\\\"id\\\":15,\\\"teamName\\\":\\\"乌克兰\\\",\\\"teamShortName\\\":\\\"ukr\\\",\\\"followed\\\":false},{\\\"id\\\":16,\\\"teamName\\\":\\\"瑞典\\\",\\\"teamShortName\\\":\\\"swe\\\",\\\"followed\\\":false},{\\\"id\\\":17,\\\"teamName\\\":\\\"A组小组第1\\\",\\\"teamShortName\\\":\\\"unknown\\\",\\\"followed\\\":false},{\\\"id\\\":18,\\\"teamName\\\":\\\"B组小组第2\\\",\\\"teamShortName\\\":\\\"unknown\\\",\\\"followed\\\":false},{\\\"id\\\":19,\\\"teamName\\\":\\\"B组小组第1\\\",\\\"teamShortName\\\":\\\"unknown\\\",\\\"followed\\\":false},{\\\"id\\\":20,\\\"teamName\\\":\\\"A组小组第2\\\",\\\"teamShortName\\\":\\\"unknown\\\",\\\"followed\\\":false},{\\\"id\\\":21,\\\"teamName\\\":\\\"C组小组第1\\\",\\\"teamShortName\\\":\\\"unknown\\\",\\\"followed\\\":false},{\\\"id\\\":22,\\\"teamName\\\":\\\"D组小组第2\\\",\\\"teamShortName\\\":\\\"unknown\\\",\\\"followed\\\":false},{\\\"id\\\":23,\\\"teamName\\\":\\\"D组小组第1\\\",\\\"teamShortName\\\":\\\"unknown\\\",\\\"followed\\\":false},{\\\"id\\\":24,\\\"teamName\\\":\\\"C组小组第2\\\",\\\"teamShortName\\\":\\\"unknown\\\",\\\"followed\\\":false},{\\\"id\\\":25,\\\"teamName\\\":\\\"四分之一决赛第1战胜者\\\",\\\"teamShortName\\\":\\\"unknown\\\",\\\"followed\\\":false},{\\\"id\\\":26,\\\"teamName\\\":\\\"四分之一决赛第3战胜者\\\",\\\"teamShortName\\\":\\\"unknown\\\",\\\"followed\\\":false},{\\\"id\\\":27,\\\"teamName\\\":\\\"四分之一决赛第2战胜者\\\",\\\"teamShortName\\\":\\\"unknown\\\",\\\"followed\\\":false},{\\\"id\\\":28,\\\"teamName\\\":\\\"四分之一决赛第4战胜者\\\",\\\"teamShortName\\\":\\\"unknown\\\",\\\"followed\\\":false},{\\\"id\\\":29,\\\"teamName\\\":\\\"半决赛第1战胜者\\\",\\\"teamShortName\\\":\\\"unknown\\\",\\\"followed\\\":false},{\\\"id\\\":30,\\\"teamName\\\":\\\"半决赛第2战胜者\\\",\\\"teamShortName\\\":\\\"unknown\\\",\\\"followed\\\":false}]\",\"groups\":\"[{\\\"id\\\":1,\\\"name\\\":\\\"欧洲杯\\\"}]\",\"teamGroups\":\"[{\\\"id\\\":0,\\\"teamId\\\":1,\\\"groupId\\\":1},{\\\"id\\\":0,\\\"teamId\\\":2,\\\"groupId\\\":1},{\\\"id\\\":0,\\\"teamId\\\":3,\\\"groupId\\\":1},{\\\"id\\\":0,\\\"teamId\\\":4,\\\"groupId\\\":1},{\\\"id\\\":0,\\\"teamId\\\":5,\\\"groupId\\\":1},{\\\"id\\\":0,\\\"teamId\\\":6,\\\"groupId\\\":1},{\\\"id\\\":0,\\\"teamId\\\":7,\\\"groupId\\\":1},{\\\"id\\\":0,\\\"teamId\\\":8,\\\"groupId\\\":1},{\\\"id\\\":0,\\\"teamId\\\":9,\\\"groupId\\\":1},{\\\"id\\\":0,\\\"teamId\\\":10,\\\"groupId\\\":1},{\\\"id\\\":0,\\\"teamId\\\":11,\\\"groupId\\\":1},{\\\"id\\\":0,\\\"teamId\\\":12,\\\"groupId\\\":1},{\\\"id\\\":0,\\\"teamId\\\":13,\\\"groupId\\\":1},{\\\"id\\\":0,\\\"teamId\\\":14,\\\"groupId\\\":1},{\\\"id\\\":0,\\\"teamId\\\":15,\\\"groupId\\\":1},{\\\"id\\\":0,\\\"teamId\\\":16,\\\"groupId\\\":1}]\"}";
                            } else {
                                updated[0] = false;
                                return;
                            }
                        }

                        Dao<Group, Integer> groupDao = databaseHelper.getDao(Group.class);
                        Dao<TeamGroup, Integer> teamGroupDao = databaseHelper.getDao(TeamGroup.class);

                        ContentUpdate contentUpdate = gson.fromJson(updateContent, ContentUpdate.class);

                        String teamStr = contentUpdate.getTeams();
                        String groupStr = contentUpdate.getGroups();
                        String teamGroupStr = contentUpdate.getTeamGroups();

                        Type teamType = new TypeToken<List<Team>>() {
                        }.getType();
                        Type groupType = new TypeToken<List<Group>>() {
                        }.getType();

                        Type teamGroupType = new TypeToken<List<TeamGroup>>() {
                        }.getType();


                        List<Team> teams = gson.fromJson(teamStr, teamType);
                        List<Group> groups = gson.fromJson(groupStr, groupType);

                        List<TeamGroup> teamGroups = gson.fromJson(teamGroupStr, teamGroupType);

                        List<Team> followedTeams = teamDao.queryForEq(Team.FOLLOWED, true);
                        databaseHelper.truncate(Team.class);
                        for (Team t : teams) {
                            for (Team followedTeam : followedTeams) {
                                if (followedTeam.getId() == t.getId()) {
                                    t.setFollowed(followedTeam.isFollowed());
                                }
                            }
                            teamDao.create(t);
                        }

                        databaseHelper.truncate(Group.class);
                        for (Group g : groups) {
                            groupDao.create(g);
                        }


                        databaseHelper.truncate(TeamGroup.class);
                        for (TeamGroup tg : teamGroups) {
                            Group g = groupDao.queryForId(tg.getGroupId());
                            Team t = teamDao.queryForId(tg.getTeamId());

                            tg.setGroup(g);
                            tg.setTeam(t);

                            teamGroupDao.create(tg);
                        }


                        ContentVersion cv = versionDao.queryForEq(ContentVersion.COLUMN_TYPE, ContentVersion.CONTENT).get(0);

                        cv.setVersion(serverContentVersion);
                        versionDao.update(cv);

                        updated[0] = true;
                    }
                    if (matchVersion < serverMatchVersion) {
                        squery = new StringQuery(activity, "http://" + host + "/match?type=match");
                        String matchJSON = squery.invoke();
                        if (matchJSON == null) {
                            if (PreferenceUtil.isFirstTimeBoot(activity)) {
                                updated[0] = true;
                                matchJSON = "{\"matches\":\"[{\\\"id\\\":1,\\\"matchTime\\\":\\\"Jun 8, 2012 4:00:00 PM\\\",\\\"teamAId\\\":1,\\\"teamBId\\\":2,\\\"tourId\\\":1},{\\\"id\\\":2,\\\"matchTime\\\":\\\"Jun 8, 2012 6:45:00 PM\\\",\\\"teamAId\\\":3,\\\"teamBId\\\":4,\\\"tourId\\\":1},{\\\"id\\\":3,\\\"matchTime\\\":\\\"Jun 9, 2012 4:00:00 PM\\\",\\\"teamAId\\\":5,\\\"teamBId\\\":6,\\\"tourId\\\":1},{\\\"id\\\":4,\\\"matchTime\\\":\\\"Jun 9, 2012 6:45:00 PM\\\",\\\"teamAId\\\":7,\\\"teamBId\\\":8,\\\"tourId\\\":1},{\\\"id\\\":5,\\\"matchTime\\\":\\\"Jun 10, 2012 4:00:00 PM\\\",\\\"teamAId\\\":9,\\\"teamBId\\\":10,\\\"tourId\\\":1},{\\\"id\\\":6,\\\"matchTime\\\":\\\"Jun 10, 2012 6:45:00 PM\\\",\\\"teamAId\\\":11,\\\"teamBId\\\":12,\\\"tourId\\\":1},{\\\"id\\\":7,\\\"matchTime\\\":\\\"Jun 11, 2012 4:00:00 PM\\\",\\\"teamAId\\\":13,\\\"teamBId\\\":14,\\\"tourId\\\":1},{\\\"id\\\":8,\\\"matchTime\\\":\\\"Jun 11, 2012 6:45:00 PM\\\",\\\"teamAId\\\":15,\\\"teamBId\\\":16,\\\"tourId\\\":1},{\\\"id\\\":9,\\\"matchTime\\\":\\\"Jun 12, 2012 4:00:00 PM\\\",\\\"teamAId\\\":2,\\\"teamBId\\\":4,\\\"tourId\\\":1},{\\\"id\\\":10,\\\"matchTime\\\":\\\"Jun 12, 2012 6:45:00 PM\\\",\\\"teamAId\\\":1,\\\"teamBId\\\":3,\\\"tourId\\\":1},{\\\"id\\\":11,\\\"matchTime\\\":\\\"Jun 13, 2012 4:00:00 PM\\\",\\\"teamAId\\\":6,\\\"teamBId\\\":8,\\\"tourId\\\":1},{\\\"id\\\":12,\\\"matchTime\\\":\\\"Jun 13, 2012 6:45:00 PM\\\",\\\"teamAId\\\":5,\\\"teamBId\\\":7,\\\"tourId\\\":1},{\\\"id\\\":13,\\\"matchTime\\\":\\\"Jun 14, 2012 4:00:00 PM\\\",\\\"teamAId\\\":10,\\\"teamBId\\\":12,\\\"tourId\\\":1},{\\\"id\\\":14,\\\"matchTime\\\":\\\"Jun 14, 2012 6:45:00 PM\\\",\\\"teamAId\\\":9,\\\"teamBId\\\":11,\\\"tourId\\\":1},{\\\"id\\\":15,\\\"matchTime\\\":\\\"Jun 15, 2012 4:00:00 PM\\\",\\\"teamAId\\\":16,\\\"teamBId\\\":14,\\\"tourId\\\":1},{\\\"id\\\":16,\\\"matchTime\\\":\\\"Jun 15, 2012 6:45:00 PM\\\",\\\"teamAId\\\":15,\\\"teamBId\\\":13,\\\"tourId\\\":1},{\\\"id\\\":17,\\\"matchTime\\\":\\\"Jun 16, 2012 6:45:00 PM\\\",\\\"teamAId\\\":2,\\\"teamBId\\\":3,\\\"tourId\\\":1},{\\\"id\\\":18,\\\"matchTime\\\":\\\"Jun 16, 2012 6:45:00 PM\\\",\\\"teamAId\\\":4,\\\"teamBId\\\":1,\\\"tourId\\\":1},{\\\"id\\\":19,\\\"matchTime\\\":\\\"Jun 17, 2012 6:45:00 PM\\\",\\\"teamAId\\\":8,\\\"teamBId\\\":5,\\\"tourId\\\":1},{\\\"id\\\":20,\\\"matchTime\\\":\\\"Jun 17, 2012 6:45:00 PM\\\",\\\"teamAId\\\":6,\\\"teamBId\\\":7,\\\"tourId\\\":1},{\\\"id\\\":21,\\\"matchTime\\\":\\\"Jun 18, 2012 6:45:00 PM\\\",\\\"teamAId\\\":12,\\\"teamBId\\\":9,\\\"tourId\\\":1},{\\\"id\\\":22,\\\"matchTime\\\":\\\"Jun 18, 2012 6:45:00 PM\\\",\\\"teamAId\\\":10,\\\"teamBId\\\":11,\\\"tourId\\\":1},{\\\"id\\\":23,\\\"matchTime\\\":\\\"Jun 19, 2012 6:45:00 PM\\\",\\\"teamAId\\\":16,\\\"teamBId\\\":13,\\\"tourId\\\":1},{\\\"id\\\":24,\\\"matchTime\\\":\\\"Jun 19, 2012 6:45:00 PM\\\",\\\"teamAId\\\":14,\\\"teamBId\\\":15,\\\"tourId\\\":1},{\\\"id\\\":25,\\\"matchTime\\\":\\\"Jun 21, 2012 6:45:00 PM\\\",\\\"teamAId\\\":17,\\\"teamBId\\\":18,\\\"tourId\\\":1},{\\\"id\\\":26,\\\"matchTime\\\":\\\"Jun 22, 2012 6:45:00 PM\\\",\\\"teamAId\\\":19,\\\"teamBId\\\":20,\\\"tourId\\\":1},{\\\"id\\\":27,\\\"matchTime\\\":\\\"Jun 23, 2012 6:45:00 PM\\\",\\\"teamAId\\\":21,\\\"teamBId\\\":22,\\\"tourId\\\":1},{\\\"id\\\":28,\\\"matchTime\\\":\\\"Jun 24, 2012 6:45:00 PM\\\",\\\"teamAId\\\":23,\\\"teamBId\\\":24,\\\"tourId\\\":1},{\\\"id\\\":29,\\\"matchTime\\\":\\\"Jun 27, 2012 6:45:00 PM\\\",\\\"teamAId\\\":25,\\\"teamBId\\\":26,\\\"tourId\\\":1},{\\\"id\\\":30,\\\"matchTime\\\":\\\"Jun 28, 2012 6:45:00 PM\\\",\\\"teamAId\\\":27,\\\"teamBId\\\":28,\\\"tourId\\\":1},{\\\"id\\\":31,\\\"matchTime\\\":\\\"Jul 1, 2012 6:45:00 PM\\\",\\\"teamAId\\\":29,\\\"teamBId\\\":30,\\\"tourId\\\":1}]\",\"tours\":\"[{\\\"id\\\":1,\\\"name\\\":\\\"欧洲杯\\\",\\\"shortName\\\":\\\"欧洲杯\\\"}]\"}";
                            } else {
                                updated[0] = false || updated[0];
                                return;
                            }
                        }

                        Dao<Match, Integer> matchDao = databaseHelper.getDao(Match.class);
                        Dao<Tour, Integer> tourDao = databaseHelper.getDao(Tour.class);

                        MatchUpdate matchUpdate = gson.fromJson(matchJSON, MatchUpdate.class);

                        String tourStr = matchUpdate.getTours();
                        String matchesStr = matchUpdate.getMatches();

                        Type tourType = new TypeToken<List<Tour>>() {
                        }.getType();
                        Type matchType = new TypeToken<List<Match>>() {
                        }.getType();

                        List<Tour> tours = gson.fromJson(tourStr, tourType);
                        List<Match> matches = gson.fromJson(matchesStr, matchType);

                        databaseHelper.truncate(Tour.class);
                        for (Tour t : tours) {
                            tourDao.create(t);
                        }

                        databaseHelper.truncate(Match.class);
                        for (Match m : matches) {
                            Team ta = teamDao.queryForId(m.getTeamAId());
                            Team tb = teamDao.queryForId(m.getTeamBId());
                            Tour t = tourDao.queryForId(m.getTourId());
                            m.setMatchTime(TimeTools.fixTimeWithTimezone(m.getMatchTime()));
                            m.setTeamA(ta);
                            m.setTeamB(tb);
                            m.setTour(t);

                            matchDao.create(m);
                        }
                        ContentVersion cv = versionDao.queryForEq(ContentVersion.COLUMN_TYPE, ContentVersion.MATCH).get(0);
                        cv.setVersion(serverMatchVersion);
                        versionDao.update(cv);

                        updated[0] = true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } finally {
                    if (databaseHelper != null) {
                        OpenHelperManager.releaseHelper();
                        databaseHelper = null;
                    }
                }
            }
        });
        thread.start();
        if (block) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        return updated[0];
    }
//test

}
