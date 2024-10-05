package com.example.demo.franquicias.models;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("franquicia")
@Data
@Builder
public class Franquicia {
    @Id
    private Long id;
    private String nombre;
}
