package com.hc.chess.game.event;

import com.hc.chess.AuthorizationHeaderType;
import com.hc.chess.BuildConfig;
import com.hc.chess.ContentType;
import com.hc.chess.datasource.HttpRequest;
import com.hc.chess.model.UserSession;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class PieceDataSource implements DataSource<PieceEntity> {
    private final String PIECE_MOVEMENT_ENDPOINT;

    public PieceDataSource() {
        this.PIECE_MOVEMENT_ENDPOINT = BuildConfig.PIECE_MOVEMENT_ENDPOINT;
    }

    @Override
    public String create(PieceEntity entity) throws JSONException, IOException {
        String endpoint_result = HttpRequest.post(this.PIECE_MOVEMENT_ENDPOINT,
                AuthorizationHeaderType.BEARER,
                UserSession.getInstance().getToken(),
                ContentType.JSON_TYPE,
                Map.of(
                        "piece_id", entity.getPieceId(),
                        "origin_position", entity.getOriginPosition(),
                        "destination_position", entity.getDestinationPosition(),
                        "opponent_user_id", entity.getOpponentUserId(),
                        "opponent_piece_id", entity.getOpponentPieceId(),
                        "capture", entity.getCapture() ? "Y" : "N"
                )
        );

        JSONObject jsonResult = new JSONObject(endpoint_result);
        String piece_id = jsonResult.getString("piece_id");

        return piece_id;
    }

    @Override
    public List<PieceEntity> find(PieceEntity pieceEntity) {
        return Collections.emptyList();
    }
}
