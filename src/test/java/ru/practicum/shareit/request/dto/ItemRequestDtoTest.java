package ru.practicum.shareit.request.dto;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;

import javax.validation.Validation;
import javax.validation.Validator;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.practicum.shareit.TestUtils.ITEM_REQUEST;
import static ru.practicum.shareit.TestUtils.asString;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemRequestDtoTest {
    private static Validator validator;
    private final JacksonTester<ItemRequestDto> tester;

    @BeforeAll
    static void initializeValidator() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    @SneakyThrows
    void testSerialize() {
        var dto = ItemRequestMapper.mapToDto(ITEM_REQUEST);
        var result = tester.write(dto);

        assertThat(result).hasJsonPathNumberValue("$.id");
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(ITEM_REQUEST.getId().intValue());
        assertThat(result).hasJsonPathStringValue("$.description");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo(ITEM_REQUEST.getDescription());
        assertThat(result).hasJsonPathStringValue("$.created");
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo(asString(ITEM_REQUEST.getCreateAt()));
        assertThat(result).doesNotHaveJsonPath("$.items");
    }

    @Test
    @SneakyThrows
    void testDeserialize() {
        var json = "{\"id\":1,\"description\":\"description\",\"created\":\"2023-07-18T16:13:41.4496351\"}";
        var dto = tester.parse(json).getObject();

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getDescription()).isEqualTo("description");
        assertThat(asString(dto.getCreated())).isEqualTo("2023-07-18T16:13:41.4496351");
    }

    @Test
    void itemRequestValidationShouldBeSuccessful() {
        var dto = ItemRequestMapper.mapToDto(ITEM_REQUEST);
        var set = validator.validate(dto);
        assertEquals(0, set.size());
    }

    @Test
    void itemRequestValidationWithEmptyDescriptionShouldContainsErrors() {
        var dto = ItemRequestMapper.mapToDto(ITEM_REQUEST.toBuilder().description("").build());
        var set = validator.validate(dto);
        assertEquals(1, set.size());
        set.forEach(action -> assertThat(action.getPropertyPath().toString()).isEqualTo("description"));
    }

    @Test
    void itemRequestValidationWithoutDescriptionShouldContainsErrors() {
        var dto = ItemRequestMapper.mapToDto(ITEM_REQUEST.toBuilder().description(null).build());
        var set = validator.validate(dto);
        assertEquals(2, set.size());
        set.forEach(action -> assertThat(action.getPropertyPath().toString()).isEqualTo("description"));
    }
}