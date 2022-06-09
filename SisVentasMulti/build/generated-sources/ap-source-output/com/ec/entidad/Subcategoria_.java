package com.ec.entidad;

import com.ec.entidad.Categoria;
import com.ec.entidad.Producto;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

<<<<<<< HEAD
@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2022-06-09T12:07:19")
=======
@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2022-06-09T12:22:10")
>>>>>>> 354e171f0ac97e0eea30625350f80ab04f1e746b
@StaticMetamodel(Subcategoria.class)
public class Subcategoria_ { 

    public static volatile SingularAttribute<Subcategoria, Integer> idSubCategoria;
    public static volatile SingularAttribute<Subcategoria, String> subCatDescripcion;
    public static volatile CollectionAttribute<Subcategoria, Producto> productoCollection;
    public static volatile SingularAttribute<Subcategoria, Categoria> idCategoria;

}