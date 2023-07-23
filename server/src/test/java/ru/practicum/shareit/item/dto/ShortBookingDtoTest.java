package ru.practicum.shareit.item.dto;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ShortBookingDtoTest {
    private final JacksonTester<ShortBookingDto> tester;

    @Test
    @SneakyThrows
    void testSerialize() {
        var dto = ShortBookingDto.builder().id(1L).bookerId(2L).build();
        var result = tester.write(dto);

        assertThat(result).hasJsonPathNumberValue("$.id");
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(dto.getId().intValue());
        assertThat(result).hasJsonPathNumberValue("$.bookerId");
        assertThat(result).extractingJsonPathNumberValue("$.bookerId").isEqualTo(dto.getBookerId().intValue());
    }

    @Test
    @SneakyThrows
    void testDeserialize() {
        var json = "{\"id\":1,\"bookerId\":2}";
        var dto = tester.parse(json).getObject();

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getBookerId()).isEqualTo(2L);
    }
}