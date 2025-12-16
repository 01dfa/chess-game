package com.hc.chess.game.event;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;

public class BackgroundLifecycle implements LifecycleOwner {
    private final LifecycleRegistry registry;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public BackgroundLifecycle() {
        registry = new LifecycleRegistry(this);
        postToMain(() -> registry.setCurrentState(Lifecycle.State.CREATED));
    }

    /** Move to STARTED (observers with STARTED become active). */
    public void start() { postToMain(() -> registry.handleLifecycleEvent(Lifecycle.Event.ON_START)); }

    /** Move to RESUMED (observers with RESUMED become active). */
    public void resume() { postToMain(() -> registry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)); }

    /** Move to PAUSED (RESUMED -> STARTED). */
    public void pause() { postToMain(() -> registry.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE)); }

    /** Move to STOPPED (STARTED -> CREATED). */
    public void stop() { postToMain(() -> registry.handleLifecycleEvent(Lifecycle.Event.ON_STOP)); }

    /** Move to DESTROYED (final state). Observers auto-clean. */
    public void destroy() { postToMain(() -> registry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)); }

    /** Convenience: CREATED -> STARTED -> RESUMED. */
    public void startAndResume() { start(); resume(); }

    private void postToMain(Runnable r) {
        if (Looper.getMainLooper().isCurrentThread()) {
            r.run();
        } else {
            mainHandler.post(r);
        }
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return registry;
    }
}
