package com.ec.entidad;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.7.9.v20210604-rNA", date="2022-06-08T20:23:16")
@StaticMetamodel(Costopromediocompraview.class)
public class Costopromediocompraview_ { 

    public static volatile SingularAttribute<Costopromediocompraview, Date> fecha;
    public static volatile SingularAttribute<Costopromediocompraview, BigDecimal> prodCantidadInicial;
    public static volatile SingularAttribute<Costopromediocompraview, String> prodNombre;
    public static volatile SingularAttribute<Costopromediocompraview, BigInteger> id;
    public static volatile SingularAttribute<Costopromediocompraview, BigDecimal> cantidad;
    public static volatile SingularAttribute<Costopromediocompraview, BigDecimal> comprapromedio;

}