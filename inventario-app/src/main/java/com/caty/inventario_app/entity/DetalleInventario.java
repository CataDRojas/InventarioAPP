package com.caty.inventario_app.entity;

import jakarta.persistence.*;

@Entity
@Table(
        name = "detalle_inventario",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"inventario_id", "producto_id"})
        }
)
public class DetalleInventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "inventario_id")
    private Inventario inventario;

    @ManyToOne(optional = false)
    @JoinColumn(name = "producto_id")
    private Producto producto;

    @Column(nullable = false)
    private Integer cantidad;

    public DetalleInventario() {
    }

    public DetalleInventario(Inventario inventario, Producto producto, Integer cantidad) {
        this.inventario = inventario;
        this.producto = producto;
        this.cantidad = cantidad;
    }

    //Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Inventario getInventario() {
        return inventario;
    }

    public void setInventario(Inventario inventario) {
        this.inventario = inventario;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }
}
