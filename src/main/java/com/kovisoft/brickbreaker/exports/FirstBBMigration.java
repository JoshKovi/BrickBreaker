package com.kovisoft.brickbreaker.exports;

import com.kovisoft.brickbreaker.exports.records.LeaderBoard;
import com.kovisoft.pg.database.data.SQLRecord;
import com.kovisoft.pg.database.data.exports.AbstractMigration;

import java.util.HashMap;
import java.util.Map;

public class FirstBBMigration extends AbstractMigration {

    private String archive = "portfolio_old";
    private String current = "portfolio";

    private Map<String, SQLRecord> migrationMap = new HashMap<>(){{
        put("leaderboard", new LeaderBoard());
    }};

    @Override
    public Map<String, SQLRecord> getMigrationMap() {
        return migrationMap;
    }

    @Override
    public String getArchiveDB() {
        return archive;
    }

    @Override
    public String getCurrentDB() {
        return current;
    }
}
