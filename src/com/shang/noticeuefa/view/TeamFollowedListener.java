package com.shang.noticeuefa.view;

import com.shang.noticeuefa.model2.Team;

/**
 * Created with IntelliJ IDEA.
 * User: jleo
 * Date: 12-4-28
 * Time: 下午4:46
 * To change this template use File | Settings | File Templates.
 */
public interface TeamFollowedListener {

    public void onTeamFollowed(Team team);

    public void onTeamUnfollowed(Team team);
}
