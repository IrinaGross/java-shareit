package ru.practicum.shareit.item.dto;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.item.mapper.ItemMapper;

import javax.validation.Validation;
import javax.validation.Validator;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.practicum.shareit.TestUtils.ITEM_1;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemDtoTest {
    private static Validator validator;
    private final JacksonTester<ItemDto> tester;

    @BeforeAll
    static void initializeValidator() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    @SneakyThrows
    void testSerialize() {
        var dto = ItemMapper.mapToDto(ITEM_1);
        var result = tester.write(dto);

        assertThat(result).hasJsonPathNumberValue("$.id");
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(dto.getId().intValue());
        assertThat(result).hasJsonPathStringValue("$.name");
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo(dto.getName());
        assertThat(result).hasJsonPathStringValue("$.description");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo(dto.getDescription());
        assertThat(result).hasJsonPathBooleanValue("$.available");
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(dto.getAvailable());
    }

    @Test
    @SneakyThrows
    void testDeserialize() {
        var json = "{\"id\":1,\"name\":\"name\",\"description\":\"desc\",\"available\":true,\"lastBooking\":null,\"nextBooking\":null}";
        var result = tester.parse(json).getObject();

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("name");
        assertThat(result.getDescription()).isEqualTo("desc");
        assertThat(result.getAvailable()).isEqualTo(true);
        assertThat(result.getLastBooking()).isNull();
        assertThat(result.getNextBooking()).isNull();
    }

    @Test
    void itemValidationWithoutGroupShouldBeSuccessful() {
        var dto = ItemMapper.mapToDto(ITEM_1);
        var set = validator.validate(dto);
        assertEquals(0, set.size());
    }

    @Test
    void itemValidationWithGroupShouldBeSuccessful() {
        var dto = ItemMapper.mapToDto(ITEM_1);
        var set = validator.validate(dto, CreateItemGroup.class);
        assertEquals(0, set.size());
    }

    @Test
    void itemValidationWithoutNameShouldContainsErrors() {
        var dto = ItemMapper.mapToDto(ITEM_1.toBuilder().name(null).build());
        var set = validator.validate(dto, CreateItemGroup.class);
        assertEquals(2, set.size());
        set.forEach(action -> assertThat(action.getPropertyPath().toString()).isEqualTo("name"));
    }

    @Test
    void itemValidationWithEmptyNameShouldContainsErrors() {
        var dto = ItemMapper.mapToDto(ITEM_1.toBuilder().name("").build());
        var set = validator.validate(dto, CreateItemGroup.class);
        assertEquals(1, set.size());
        set.forEach(action -> assertThat(action.getPropertyPath().toString()).isEqualTo("name"));
    }

    @Test
    void itemValidationWithoutDescriptionShouldContainsErrors() {
        var dto = ItemMapper.mapToDto(ITEM_1.toBuilder().description(null).build());
        var set = validator.validate(dto, CreateItemGroup.class);
        assertEquals(2, set.size());
        set.forEach(action -> assertThat(action.getPropertyPath().toString()).isEqualTo("description"));
    }

    @Test
    void itemValidationWithEmptyDescriptionShouldContainsErrors() {
        var dto = ItemMapper.mapToDto(ITEM_1.toBuilder().description("").build());
        var set = validator.validate(dto, CreateItemGroup.class);
        assertEquals(1, set.size());
        set.forEach(action -> assertThat(action.getPropertyPath().toString()).isEqualTo("description"));
    }

    @Test
    void itemValidationWithoutAvailableShouldContainsErrors() {
        var dto = ItemMapper.mapToDto(ITEM_1.toBuilder().available(null).build());
        var set = validator.validate(dto, CreateItemGroup.class);
        assertEquals(1, set.size());
        set.forEach(action -> assertThat(action.getPropertyPath().toString()).isEqualTo("available"));
    }

    @Test
    void itemValidationWithNegativeRequestIdShouldContainsErrors() {
        var dto = ItemMapper.mapToDto(ITEM_1.toBuilder().requestId(-1L).build());
        var set = validator.validate(dto, CreateItemGroup.class);
        assertEquals(1, set.size());
        set.forEach(action -> assertThat(action.getPropertyPath().toString()).isEqualTo("requestId"));
    }
}