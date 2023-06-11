package ua.ltd.procura.procuraapp.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDto
{

    private Long id;
    @NotEmpty(message = "ID can''t be empty!")
    private String internalId;
    @NotEmpty(message = "Order''s name can''t be empty!")
    private String name;
    private String clientName;
    @PositiveOrZero
    private BigDecimal price;
    private LocalDate finishDate;

}