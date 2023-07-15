package ru.practicum.shareit;

import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@NoArgsConstructor
public class Utils {

    public static final String X_SHARER_USER_ID_HEADER = "X-Sharer-User-Id";

    @NonNull
    public static Pageable newPage(@NonNull Integer from, @NonNull Integer size) {
        return PageRequest.of(from > 0 ? from / size : 0, size);
    }
}
