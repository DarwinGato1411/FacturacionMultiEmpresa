package com.ec.entidad;

import java.math.BigDecimal;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.7.9.v20210604-rNA", date="2022-06-08T20:23:16")
@StaticMetamodel(Pedidos.class)
public class Pedidos_ { 

    public static volatile SingularAttribute<Pedidos, BigDecimal> pedCantidad;
    public static volatile SingularAttribute<Pedidos, String> pedProveedor;
    public static volatile SingularAttribute<Pedidos, Integer> codPedido;
    public static volatile SingularAttribute<Pedidos, Date> pedFecha;
    public static volatile SingularAttribute<Pedidos, String> pedEstado;
    public static volatile SingularAttribute<Pedidos, String> pedDescripcion;

}