package ru.practicum.shareit;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Const {
    public static final String X_SHARER_USER_ID_HEADER = "X-Sharer-User-Id";
    public static final String FROM_REQUEST_PARAM = "from";
    public static final String DEFAULT_FROM_VALUE = "0";
    public static final String SIZE_REQUEST_PARAM = "size";
    public static final String DEFAULT_SIZE_VALUE = "10";
    public static final String STATE_REQUEST_PARAM = "state";
    public static final String DEFAULT_STATE_VALUE = "ALL";
    public static final String SEARCH_REQUEST_PARAM = "text";
    public static final String BOOKING_ID_PATH_NAME = "bookingId";
}
