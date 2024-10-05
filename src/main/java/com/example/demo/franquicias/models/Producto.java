package com.example.demo.franquicias.models;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("producto")
@Data
@Builder
public class Producto {
    @Id
    private Long id;
    private String nombre;
    private Integer stock;
    private Long sucursalId; // Foreign Key to Sucursal
}
