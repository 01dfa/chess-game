package com.hc.chess.game.event;

import com.hc.chess.datasource.Result;

import java.io.IOException;

public class PieceRepository {
    private final DataSource<PieceEntity> remoteDataSource;

    public PieceRepository(DataSource<PieceEntity> remoteDataSource) {
        this.remoteDataSource = remoteDataSource;
    }

    public Result<String> createMovement(PieceEntity entity) {
        try {
            String result = this.remoteDataSource.create(entity);

            return new Result.Success<>(result);
        } catch (Exception e) {
            return new Result.Error(new IOException("Create piece movement", e));
        }
    }
}
