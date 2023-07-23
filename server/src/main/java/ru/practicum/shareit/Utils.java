package ru.practicum.shareit;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Utils {

    @NonNull
    public static Pageable newPage(@NonNull Integer from, @NonNull Integer size) {
        return PageRequest.of(from > 0 ? from / size : 0, size);
    }
}
