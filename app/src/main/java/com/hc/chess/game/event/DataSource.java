package com.hc.chess.game.event;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

public interface DataSource <T> {
    String create(T t) throws JSONException, IOException;
    List<T> find(T t);
}
