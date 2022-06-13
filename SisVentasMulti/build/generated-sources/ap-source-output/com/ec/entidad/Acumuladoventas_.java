package com.ec.entidad;

import java.math.BigDecimal;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.7.9.v20210604-rNA", date="2022-06-13T13:52:32")
@StaticMetamodel(Acumuladoventas.class)
public class Acumuladoventas_ { 

    public static volatile SingularAttribute<Acumuladoventas, BigDecimal> facTotalBaseGravaba;
    public static volatile SingularAttribute<Acumuladoventas, String> tidCodigo;
    public static volatile SingularAttribute<Acumuladoventas, Date> facFecha;
    public static volatile SingularAttribute<Acumuladoventas, Integer> codTipoambiente;
    public static volatile SingularAttribute<Acumuladoventas, Integer> id;
    public static volatile SingularAttribute<Acumuladoventas, Integer> numCompro;
    public static volatile SingularAttribute<Acumuladoventas, String> cliCedula;
    public static volatile SingularAttribute<Acumuladoventas, BigDecimal> facIva;
    public static volatile SingularAttribute<Acumuladoventas, String> forCodigo;

}