package com.example.demo.franquicias.models;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("sucursal")
@Data
@Builder
public class Sucursal {
    @Id
    private Long id;
    private String nombre;
    private Long franquiciaId; // Foreign Key to Franquicia
}
