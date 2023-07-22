package ru.practicum.shareit.booking.dto;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.BookingStatus;

import javax.validation.Validation;
import javax.validation.Validator;
import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.practicum.shareit.TestUtils.*;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingDtoTest {
    private static final BookingDto BOOKING_DTO = BookingMapper.mapToDto(BOOKING).toBuilder().itemId(ITEM_ID_1).build();

    private static Validator validator;
    private final JacksonTester<BookingDto> tester;

    @BeforeAll
    static void initializeValidator() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    @SneakyThrows
    void testSerialize() {
        var result = tester.write(BOOKING_DTO);

        assertThat(result).hasJsonPathNumberValue("$.id");
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(BOOKING_DTO.getId().intValue());
        assertThat(result).hasJsonPathStringValue("$.start");
        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo(asString(BOOKING_DTO.getStart()));
        assertThat(result).hasJsonPathStringValue("$.end");
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo(asString(BOOKING_DTO.getEnd()));
        assertThat(result).hasJsonPathStringValue("$.status");
        assertThat(result).extractingJsonPathStringValue("$.status").isEqualTo(BOOKING_DTO.getStatus());
        assertThat(result).hasJsonPathValue("$.booker").hasJsonPathValue("$.id");
        assertThat(result).hasJsonPathValue("$.item").hasJsonPathValue("$.id");
    }

    @Test
    @SneakyThrows
    void testDeserialize() {
        var json = "{\"id\":1,\"start\":\"2023-07-19T17:37:25.8880647\",\"end\":\"2023-07-20T17:37:25.8880647\",\"status\":\"WAITING\",\"booker\":{\"id\":1,\"name\":\"name1\",\"email\":\"name1@email.com\"},\"item\":{\"id\":1,\"name\":\"name\",\"description\":\"desc\",\"available\":true,\"lastBooking\":null,\"nextBooking\":null}}";
        var result = tester.parse(json).getObject();

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getStart()).isEqualTo(LocalDateTime.parse("2023-07-19T17:37:25.8880647"));
        assertThat(result.getEnd()).isEqualTo(LocalDateTime.parse("2023-07-20T17:37:25.8880647"));
        assertThat(result.getStatus()).isEqualTo(BookingStatus.WAITING.name());
        assertThat(result.getBooker().getId()).isEqualTo(1L);
        assertThat(result.getItem().getId()).isEqualTo(1L);
    }

    @Test
    void bookingValidationShouldBeSuccessful() {
        var set = validator.validate(BOOKING_DTO);
        assertEquals(0, set.size());
    }

    @Test
    void bookingValidationWithoutItemIdShouldContainsErrors() {
        var set = validator.validate(BOOKING_DTO.toBuilder().itemId(null).build());
        assertEquals(1, set.size());
        set.forEach(action -> assertThat(action.getPropertyPath().toString()).isEqualTo("itemId"));
    }

    @Test
    void bookingValidationWithoutStartShouldContainsErrors() {
        var set = validator.validate(BOOKING_DTO.toBuilder().start(null).build());
        assertEquals(1, set.size());
        set.forEach(action -> assertThat(action.getPropertyPath().toString()).isEqualTo("start"));
    }

    @Test
    void bookingValidationWithStartLessThatNowShouldContainsErrors() {
        var set = validator.validate(BOOKING_DTO.toBuilder().start(REQUEST_TIME.minusDays(1)).build());
        assertEquals(1, set.size());
        set.forEach(action -> assertThat(action.getPropertyPath().toString()).isEqualTo("start"));
    }

    @Test
    void bookingValidationWithoutEndShouldContainsErrors() {
        var set = validator.validate(BOOKING_DTO.toBuilder().end(null).build());
        assertEquals(1, set.size());
        set.forEach(action -> assertThat(action.getPropertyPath().toString()).isEqualTo("end"));
    }

    @Test
    void bookingValidationWithEndLessThatNowShouldContainsErrors() {
        var set = validator.validate(BOOKING_DTO.toBuilder().end(REQUEST_TIME.minusDays(1)).build());
        assertEquals(1, set.size());
        set.forEach(action -> assertThat(action.getPropertyPath().toString()).isEqualTo("end"));
    }
}