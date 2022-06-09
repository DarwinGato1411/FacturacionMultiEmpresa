package com.ec.entidad;

import com.ec.entidad.Proveedores;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.7.9.v20210604-rNA", date="2022-06-08T19:07:20")
@StaticMetamodel(TipoIdentificacionCompra.class)
public class TipoIdentificacionCompra_ { 

    public static volatile SingularAttribute<TipoIdentificacionCompra, String> ticCodigo;
    public static volatile CollectionAttribute<TipoIdentificacionCompra, Proveedores> proveedoresCollection;
    public static volatile SingularAttribute<TipoIdentificacionCompra, Integer> idTipoIdentificacionCompra;
    public static volatile SingularAttribute<TipoIdentificacionCompra, String> ticNombre;

}