package app.ldev.estoqueCervejas.entity;

import app.ldev.estoqueCervejas.enums.TipoCerveja;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Cerveja {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nome;

    @Column(nullable = false)
    private String marca;

    @Column(nullable = false)
    private int max;

    @Column(nullable = false)
    private int quantidade;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoCerveja tipo;
}
