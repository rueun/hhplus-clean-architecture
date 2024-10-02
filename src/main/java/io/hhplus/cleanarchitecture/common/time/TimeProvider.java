package io.hhplus.cleanarchitecture.common.time;

import java.time.LocalDateTime;

public interface TimeProvider {
    LocalDateTime now();
}
