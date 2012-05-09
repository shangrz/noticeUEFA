package com.shang.noticeuefa;

import android.app.Activity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.shang.noticeuefa.database.DatabaseHelper;
import com.shang.noticeuefa.model2.*;
import com.srz.androidtools.util.StringQuery;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
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

    public static void main(String[] args) {
        Team t = new Team();
        t.setTeamName("英格兰");
        t.setTeamShortName("eng");
        t.setFollowed(true);
        t.setId(1);

        Team t2 = new Team();
        t2.setTeamName("荷兰");
        t2.setTeamShortName("net");
        t2.setFollowed(true);
        t2.setId(2);

        Type type = new TypeToken<List<Team>>() {
        }.getType();
        Type gtype = new TypeToken<List<Group>>() {
        }.getType();
        Type tgtype = new TypeToken<List<TeamGroup>>() {
        }.getType();
        Type mtype = new TypeToken<List<Match>>() {
        }.getType();
        Type ttype = new TypeToken<List<Tour>>() {
        }.getType();


        Group g = new Group();
        g.setName("英超");
        g.setId(1);

        Group g2 = new Group();
        g2.setName("西甲");
        g2.setId(2);


        List<Team> teams = new ArrayList<Team>();
        List<Group> groups = new ArrayList<Group>();
        List<TeamGroup> teamGroups = new ArrayList<TeamGroup>();

        teams.add(t);
        teams.add(t2);

        groups.add(g);
        groups.add(g2);

        TeamGroup tg = new TeamGroup();
        tg.setId(0);
        tg.setGroupId(1);
        tg.setTeamId(1);

        TeamGroup tg2 = new TeamGroup();
        tg2.setGroupId(1);
        tg2.setTeamId(2);
        tg2.setId(1);

        teamGroups.add(tg);
        teamGroups.add(tg2);

        String json = new Gson().toJson(teams, type);
        String gjson = new Gson().toJson(groups, gtype);
        String tgjson = new Gson().toJson(teamGroups, tgtype);


        System.out.println(json);
        System.out.println(gjson);
        System.out.println(tgjson);

        ContentUpdate update = new ContentUpdate();
        update.setGroups(gjson);
        update.setTeamGroups(tgjson);
        update.setTeams(json);

        System.out.println(new Gson().toJson(update));

        List<Tour> tours = new ArrayList<Tour>();

        Tour tr = new Tour();
        tr.setName("世界杯");
        tr.setShortName("World Cup");
        tr.setId(1);

        tours.add(tr);

        List<Match> matches = new ArrayList<Match>();
        Match m = new Match();
        m.setTeamAId(t.getId());
        m.setTeamBId(t2.getId());
        m.setTourId(tr.getId());
        m.setComment("决赛");
        m.setDesc("决赛呀");
        m.setMatchTime(new Date());
        m.setScoreA("2");
        m.setScoreB("1");

        Match m1 = new Match();
        m1.setTeamAId(t2.getId());
        m1.setTeamBId(t.getId());
        m1.setTourId(tr.getId());

        matches.add(m);
        matches.add(m1);

        String matchJSON = new Gson().toJson(matches);
        String tourJSON = new Gson().toJson(tours);

        MatchUpdate matchUpdate = new MatchUpdate();
        matchUpdate.setMatches(matchJSON);
        matchUpdate.setTours(tourJSON);

        String matchUpdateStr = new Gson().toJson(matchUpdate);
        System.out.println(matchUpdateStr);
    }

    public void checkUpdate(final String host, boolean block) {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                StringQuery squery = new StringQuery(activity, "http://" + host + "/match-version?type=content");
                String updateContentVersion = squery.invoke();
                if (updateContentVersion == null) {
                    return;
                }

                squery = new StringQuery(activity, "http://" + host + "/match-version?type=match");
                String matchJSONVersion = squery.invoke();
                if (matchJSONVersion == null)
                    return;

                int serverContentVersion = Integer.valueOf(updateContentVersion.trim());
                int serverMatchVersion = Integer.valueOf(matchJSONVersion.trim());

                try {
                    databaseHelper = OpenHelperManager.getHelper(activity, DatabaseHelper.class);
                    Dao<ContentVersion, Integer> versionDao = databaseHelper.getDao(ContentVersion.class);
                     
                    
                    int contentVersion =versionDao.queryForEq(ContentVersion.COLUMN_TYPE, ContentVersion.CONTENT).size()==0? -1: versionDao.queryForEq(ContentVersion.COLUMN_TYPE, ContentVersion.CONTENT).get(0).getVersion();
                    int matchVersion = versionDao.queryForEq(ContentVersion.COLUMN_TYPE, ContentVersion.MATCH).size()==0? -1: 
                        versionDao.queryForEq(ContentVersion.COLUMN_TYPE, ContentVersion.MATCH).get(0).getVersion();

                    Gson gson = new Gson();
                    Dao<Team, Integer> teamDao = databaseHelper.getDao(Team.class);
                    if (contentVersion < serverContentVersion) {
                        squery = new StringQuery(activity, "http://" + host + "/match?type=type");
                        String updateContent = squery.invoke();
                        if (updateContent == null) {
                            return;
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
                    }
                    if (matchVersion < serverMatchVersion) {
//                        String matchJSON = MobclickAgent.getConfigParams(activity, activity.getString(R.string.umeng_match_key));
                        String matchJSON = "{\"matches\":\"[{\\\"id\\\":1,\\\"matchTime\\\":\\\"Jun 8, 2012 4:00:00 PM\\\",\\\"teamAId\\\":1,\\\"teamBId\\\":2,\\\"tourId\\\":1},{\\\"id\\\":2,\\\"matchTime\\\":\\\"Jun 8, 2012 6:45:00 PM\\\",\\\"teamAId\\\":3,\\\"teamBId\\\":4,\\\"tourId\\\":1},{\\\"id\\\":3,\\\"matchTime\\\":\\\"Jun 9, 2012 4:00:00 PM\\\",\\\"teamAId\\\":5,\\\"teamBId\\\":6,\\\"tourId\\\":1},{\\\"id\\\":4,\\\"matchTime\\\":\\\"Jun 9, 2012 6:45:00 PM\\\",\\\"teamAId\\\":7,\\\"teamBId\\\":8,\\\"tourId\\\":1},{\\\"id\\\":5,\\\"matchTime\\\":\\\"Jun 10, 2012 4:00:00 PM\\\",\\\"teamAId\\\":9,\\\"teamBId\\\":10,\\\"tourId\\\":1},{\\\"id\\\":6,\\\"matchTime\\\":\\\"Jun 10, 2012 6:45:00 PM\\\",\\\"teamAId\\\":11,\\\"teamBId\\\":12,\\\"tourId\\\":1},{\\\"id\\\":7,\\\"matchTime\\\":\\\"Jun 11, 2012 4:00:00 PM\\\",\\\"teamAId\\\":13,\\\"teamBId\\\":14,\\\"tourId\\\":1},{\\\"id\\\":8,\\\"matchTime\\\":\\\"Jun 11, 2012 6:45:00 PM\\\",\\\"teamAId\\\":15,\\\"teamBId\\\":16,\\\"tourId\\\":1},{\\\"id\\\":9,\\\"matchTime\\\":\\\"Jun 12, 2012 4:00:00 PM\\\",\\\"teamAId\\\":2,\\\"teamBId\\\":4,\\\"tourId\\\":1},{\\\"id\\\":10,\\\"matchTime\\\":\\\"Jun 12, 2012 6:45:00 PM\\\",\\\"teamAId\\\":1,\\\"teamBId\\\":3,\\\"tourId\\\":1},{\\\"id\\\":11,\\\"matchTime\\\":\\\"Jun 13, 2012 4:00:00 PM\\\",\\\"teamAId\\\":6,\\\"teamBId\\\":8,\\\"tourId\\\":1},{\\\"id\\\":12,\\\"matchTime\\\":\\\"Jun 13, 2012 6:45:00 PM\\\",\\\"teamAId\\\":5,\\\"teamBId\\\":7,\\\"tourId\\\":1},{\\\"id\\\":13,\\\"matchTime\\\":\\\"Jun 14, 2012 4:00:00 PM\\\",\\\"teamAId\\\":10,\\\"teamBId\\\":12,\\\"tourId\\\":1},{\\\"id\\\":14,\\\"matchTime\\\":\\\"Jun 14, 2012 6:45:00 PM\\\",\\\"teamAId\\\":9,\\\"teamBId\\\":11,\\\"tourId\\\":1},{\\\"id\\\":15,\\\"matchTime\\\":\\\"Jun 15, 2012 4:00:00 PM\\\",\\\"teamAId\\\":16,\\\"teamBId\\\":14,\\\"tourId\\\":1},{\\\"id\\\":16,\\\"matchTime\\\":\\\"Jun 15, 2012 6:45:00 PM\\\",\\\"teamAId\\\":15,\\\"teamBId\\\":13,\\\"tourId\\\":1},{\\\"id\\\":17,\\\"matchTime\\\":\\\"Jun 16, 2012 6:45:00 PM\\\",\\\"teamAId\\\":2,\\\"teamBId\\\":3,\\\"tourId\\\":1},{\\\"id\\\":18,\\\"matchTime\\\":\\\"Jun 16, 2012 6:45:00 PM\\\",\\\"teamAId\\\":4,\\\"teamBId\\\":1,\\\"tourId\\\":1},{\\\"id\\\":19,\\\"matchTime\\\":\\\"Jun 17, 2012 6:45:00 PM\\\",\\\"teamAId\\\":8,\\\"teamBId\\\":5,\\\"tourId\\\":1},{\\\"id\\\":20,\\\"matchTime\\\":\\\"Jun 17, 2012 6:45:00 PM\\\",\\\"teamAId\\\":6,\\\"teamBId\\\":7,\\\"tourId\\\":1},{\\\"id\\\":21,\\\"matchTime\\\":\\\"Jun 18, 2012 6:45:00 PM\\\",\\\"teamAId\\\":12,\\\"teamBId\\\":9,\\\"tourId\\\":1},{\\\"id\\\":22,\\\"matchTime\\\":\\\"Jun 18, 2012 6:45:00 PM\\\",\\\"teamAId\\\":10,\\\"teamBId\\\":11,\\\"tourId\\\":1},{\\\"id\\\":23,\\\"matchTime\\\":\\\"Jun 19, 2012 6:45:00 PM\\\",\\\"teamAId\\\":16,\\\"teamBId\\\":13,\\\"tourId\\\":1},{\\\"id\\\":24,\\\"matchTime\\\":\\\"Jun 19, 2012 6:45:00 PM\\\",\\\"teamAId\\\":14,\\\"teamBId\\\":15,\\\"tourId\\\":1},{\\\"id\\\":25,\\\"matchTime\\\":\\\"Jun 21, 2012 6:45:00 PM\\\",\\\"teamAId\\\":17,\\\"teamBId\\\":18,\\\"tourId\\\":1},{\\\"id\\\":26,\\\"matchTime\\\":\\\"Jun 22, 2012 6:45:00 PM\\\",\\\"teamAId\\\":19,\\\"teamBId\\\":20,\\\"tourId\\\":1},{\\\"id\\\":27,\\\"matchTime\\\":\\\"Jun 23, 2012 6:45:00 PM\\\",\\\"teamAId\\\":21,\\\"teamBId\\\":22,\\\"tourId\\\":1},{\\\"id\\\":28,\\\"matchTime\\\":\\\"Jun 24, 2012 6:45:00 PM\\\",\\\"teamAId\\\":23,\\\"teamBId\\\":24,\\\"tourId\\\":1},{\\\"id\\\":29,\\\"matchTime\\\":\\\"Jun 27, 2012 6:45:00 PM\\\",\\\"teamAId\\\":25,\\\"teamBId\\\":26,\\\"tourId\\\":1},{\\\"id\\\":30,\\\"matchTime\\\":\\\"Jun 28, 2012 6:45:00 PM\\\",\\\"teamAId\\\":27,\\\"teamBId\\\":28,\\\"tourId\\\":1},{\\\"id\\\":31,\\\"matchTime\\\":\\\"Jul 1, 2012 6:45:00 PM\\\",\\\"teamAId\\\":29,\\\"teamBId\\\":30,\\\"tourId\\\":1}]\",\"tours\":\"[{\\\"id\\\":1,\\\"name\\\":\\\"欧洲杯\\\",\\\"shortName\\\":\\\"欧洲杯\\\"}]\"}";
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

                            m.setTeamA(ta);
                            m.setTeamB(tb);
                            m.setTour(t);

                            matchDao.create(m);
                        }
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
    }


}
