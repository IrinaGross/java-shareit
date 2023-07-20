package ru.practicum.shareit;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Utils {

    public static final String X_SHARER_USER_ID_HEADER = "X-Sharer-User-Id";
    public static final String FROM_REQUEST_PARAM = "from";
    public static final String DEFAULT_FROM_VALUE = "0";
    public static final String SIZE_REQUEST_PARAM = "size";
    public static final String DEFAULT_SIZE_VALUE = "10";
    public static final String STATE_REQUEST_PARAM = "state";
    public static final String SEARCH_REQUEST_PARAM = "text";

    @NonNull
    public static Pageable newPage(@NonNull Integer from, @NonNull Integer size) {
        return PageRequest.of(from > 0 ? from / size : 0, size);
    }
}
