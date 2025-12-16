package com.hc.chess.game.event;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.hc.chess.datasource.Result;
import com.hc.chess.game.element.pieces.Piece;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PieceLifecycle implements LifecycleOwner {
    private static final String TAG = "PieceLifecycle";
    private final BackgroundLifecycle backgroundLifecycle;
    private final MutableLiveData<PieceEvent> statusLiveData;
    private final PieceRepository pieceRepository;
    private ExecutorService executor;
    private final String opponentUserId;

    public PieceLifecycle(PieceRepository pieceRepository) {
        this.pieceRepository = pieceRepository;
        this.opponentUserId = java.util.UUID.randomUUID().toString();

        this.statusLiveData = new MutableLiveData<>();
        this.backgroundLifecycle = new BackgroundLifecycle();
        this.startWork();
    }

    public void startWork() {
        backgroundLifecycle.startAndResume();
    }

    public void shutdown() {
        executor.shutdownNow();
        backgroundLifecycle.destroy();
    }

    public LiveData<PieceEvent> getStatus() { return statusLiveData; }

    @Override
    public Lifecycle getLifecycle() {
        return backgroundLifecycle.getLifecycle();
    }

    public void pieceMovement(Piece piece, String opponentPieceId) {
        executor = Executors.newSingleThreadExecutor();

        CompletableFuture.supplyAsync(() -> {
            PieceEntity pieceEntity = new PieceEntity(
                    piece.getName(),
                    piece.getOpeningNamePosition(),
                    piece.getNamePosition(),
                    this.opponentUserId,
                    opponentPieceId,
                    piece.captured());

            return pieceRepository.createMovement(pieceEntity);
            },
                executor).thenAccept(result -> {
                    if (Result.isSuccess(result)) {
                        String data = result.getData();
                        emit(new PieceEvent(PieceEvent.Action.ON_MOVE, data));
                    }
                    else
                        emit(new PieceEvent(PieceEvent.Action.ON_ERROR, ((Result.Error)result).toString()));

                    executor.shutdownNow();
                }).exceptionally(error -> {
                    emit(new PieceEvent(PieceEvent.Action.ON_ERROR, error.getMessage()));
                    executor.shutdownNow();
                    return null;
                });
    }

    public void emit(PieceEvent event) {
        statusLiveData.postValue(event);
    }
}
