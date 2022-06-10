package com.ec.entidad;

import com.ec.entidad.Factura;
import java.math.BigDecimal;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

<<<<<<< HEAD
@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2022-06-09T23:21:07")
=======
<<<<<<< HEAD
@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2022-06-09T12:07:19")
=======
@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2022-06-09T12:22:10")
>>>>>>> 354e171f0ac97e0eea30625350f80ab04f1e746b
>>>>>>> b12b595301135eab9facf21ebda3bca3c36999a3
@StaticMetamodel(DetallePago.class)
public class DetallePago_ { 

    public static volatile SingularAttribute<DetallePago, BigDecimal> detpTotal;
    public static volatile SingularAttribute<DetallePago, BigDecimal> detpSubtotal;
    public static volatile SingularAttribute<DetallePago, BigDecimal> detpMulta;
    public static volatile SingularAttribute<DetallePago, Integer> idDetallePago;
    public static volatile SingularAttribute<DetallePago, BigDecimal> detpAbono;
    public static volatile SingularAttribute<DetallePago, Factura> idFactura;
    public static volatile SingularAttribute<DetallePago, Date> detpFechaPago;
    public static volatile SingularAttribute<DetallePago, Integer> detpNumPago;
    public static volatile SingularAttribute<DetallePago, Date> detpFechaCobro;
    public static volatile SingularAttribute<DetallePago, BigDecimal> detpSaldo;

}