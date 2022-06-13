package com.ec.entidad.sri;

import com.ec.entidad.sri.CabeceraCompraSri;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.7.9.v20210604-rNA", date="2022-06-13T13:52:32")
@StaticMetamodel(CategoriaCompras.class)
public class CategoriaCompras_ { 

    public static volatile SingularAttribute<CategoriaCompras, Integer> idCategoriaCompras;
    public static volatile SingularAttribute<CategoriaCompras, String> catcCodigo;
    public static volatile SingularAttribute<CategoriaCompras, String> catcNombre;
    public static volatile CollectionAttribute<CategoriaCompras, CabeceraCompraSri> cabeceraCompraSriCollection;
    public static volatile SingularAttribute<CategoriaCompras, Boolean> catcIsprincipal;

}