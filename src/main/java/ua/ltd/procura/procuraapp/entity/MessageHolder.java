package ua.ltd.procura.procuraapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table(name="messages")
public class MessageHolder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "key")
    private String key;
    @Column(name = "message")
    private String message;
    @ManyToOne
    @JoinColumn(name="locale_id", nullable=false)
    private AppLocale locale;

}
