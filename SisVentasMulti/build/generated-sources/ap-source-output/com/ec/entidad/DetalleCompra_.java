package com.ec.entidad;

import com.ec.entidad.CabeceraCompra;
import com.ec.entidad.Producto;
import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

<<<<<<< HEAD
@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2022-06-09T12:07:19")
=======
@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2022-06-09T12:22:10")
>>>>>>> 354e171f0ac97e0eea30625350f80ab04f1e746b
@StaticMetamodel(DetalleCompra.class)
public class DetalleCompra_ { 

    public static volatile SingularAttribute<DetalleCompra, String> detDescripcion;
    public static volatile SingularAttribute<DetalleCompra, BigDecimal> iprodCodigoProvee;
    public static volatile SingularAttribute<DetalleCompra, CabeceraCompra> idCabecera;
    public static volatile SingularAttribute<DetalleCompra, Integer> idIngresoProd;
    public static volatile SingularAttribute<DetalleCompra, BigDecimal> iprodTotal;
    public static volatile SingularAttribute<DetalleCompra, BigDecimal> iprodCantidad;
    public static volatile SingularAttribute<DetalleCompra, BigDecimal> iprodSubtotal;
    public static volatile SingularAttribute<DetalleCompra, Producto> idProducto;
    public static volatile SingularAttribute<DetalleCompra, BigDecimal> detValorInicial;
    public static volatile SingularAttribute<DetalleCompra, BigDecimal> detFactor;

}