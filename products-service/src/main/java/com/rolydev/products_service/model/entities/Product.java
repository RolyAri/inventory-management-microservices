package com.rolydev.products_service.model.entities;

import jakarta.persistence.*;
//import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "product")
//@Getter
//@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String sku;

    // @NotBlank
    // @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ.'\\- ]+$", message = "Nombre inválido (solo letras, espacios, puntos o guiones)")
    // @Column
    private String name;
    private String description;
    private Double price;
    private Boolean status;

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", sku='" + sku + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", status=" + status +
                '}';
    }
}
