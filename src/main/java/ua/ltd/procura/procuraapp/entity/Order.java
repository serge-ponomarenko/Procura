package ua.ltd.procura.procuraapp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@ToString
@Entity
@Table(name="orders")
public class Order
{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, unique=true)
    private String internalId;

    @Column(nullable=false)
    private String name;

    @Column()
    private String clientName;

    @Column()
    private BigDecimal price;

    @Column()
    private LocalDate finishDate;

}


