package com.ec.entidad;

import com.ec.entidad.DetalleRetencionCompra;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
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
@StaticMetamodel(TipoRetencion.class)
public class TipoRetencion_ { 

    public static volatile SingularAttribute<TipoRetencion, String> tireCodigo;
    public static volatile CollectionAttribute<TipoRetencion, DetalleRetencionCompra> detalleRetencionCompraCollection;
    public static volatile SingularAttribute<TipoRetencion, String> tireTipo;
    public static volatile SingularAttribute<TipoRetencion, String> tireNombre;
    public static volatile SingularAttribute<TipoRetencion, Double> tirePorcentajeRetencion;

}