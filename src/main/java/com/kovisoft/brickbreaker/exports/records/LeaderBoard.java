package com.kovisoft.brickbreaker.exports.records;

import com.kovisoft.portwebdatabase.data.Records;
import com.kovisoft.portwebdatabase.data.SQLRecord;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public record LeaderBoard(Long id, String player, Long score, String location, LocalDateTime dateAchieved) implements SQLRecord {

    public LeaderBoard {
        player = (player != null) ? player.replaceAll("([^A-Za-z0-9_ ])+", "") : "Anonymous";
        score = (score != null) ? score : 0;
        location = (location != null) ? location.replaceAll("([^A-Za-z0-9_ ,])+", "") : "Somewhere in the world";
        dateAchieved = (dateAchieved == null) ? LocalDateTime.now() : dateAchieved;
    }
    public LeaderBoard(String player, Long score){
        this(null, player, score, null, LocalDateTime.now());
    }
    public LeaderBoard(String player, Long score, String location){
        this(null, player, score, location, LocalDateTime.now());
    }
    public LeaderBoard(){this(null, null, null, null,null);}

    public LeaderBoard(Map<String, Object> map){
        this(
                Records.getLongOrNull(map.get("id")),
                (String)map.get("player"),
                Records.getLongOrNull(map.get("score")),
                (String)map.get("location"),
                Records.getLocalDtOrNull("dateAchieved")
        );
    }

    @Override
    public <T extends SQLRecord> T getNewRecord(Map<String, Object> map) {
        TreeMap<String, Object> tm = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        tm.putAll(map);
        return (T) new LeaderBoard(tm);
    }

    @Override
    public <T extends SQLRecord> boolean equalsWithoutId(T t) {
        if(t == null || t.getClass() != LeaderBoard.class) return false;
        LeaderBoard lb = (LeaderBoard) t;
        return Objects.equals(player, lb.player) && Objects.equals(score, lb.score) && Objects.equals(location, lb.location);
    }

    @Override
    public Object getObjectValueByFieldName(String fieldName) throws NoSuchFieldException, IllegalAccessException {
        return this.getClass().getDeclaredField(fieldName).get(this);
    }
}
