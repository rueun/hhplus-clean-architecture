package io.hhplus.cleanarchitecture.common.time;

import java.time.LocalDateTime;

public class FakeTimeProvider implements TimeProvider {

    private LocalDateTime fakeNow;

    public FakeTimeProvider(LocalDateTime fakeNow) {
        this.fakeNow = fakeNow;
    }

    @Override
    public LocalDateTime now() {
        return fakeNow != null ? fakeNow : LocalDateTime.now();
    }
}
