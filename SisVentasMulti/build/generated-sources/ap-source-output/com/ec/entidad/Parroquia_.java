package com.ec.entidad;

import com.ec.entidad.Canton;
import com.ec.entidad.Usuario;
import java.math.BigDecimal;
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
@StaticMetamodel(Parroquia.class)
public class Parroquia_ { 

    public static volatile SingularAttribute<Parroquia, String> parrZona;
    public static volatile SingularAttribute<Parroquia, Boolean> parrEstado;
    public static volatile SingularAttribute<Parroquia, BigDecimal> parrPrecio;
    public static volatile SingularAttribute<Parroquia, Integer> parrNumero;
    public static volatile SingularAttribute<Parroquia, BigDecimal> parrKiloAdicional;
    public static volatile SingularAttribute<Parroquia, Canton> idCanton;
    public static volatile CollectionAttribute<Parroquia, Usuario> usuarioCollection;
    public static volatile SingularAttribute<Parroquia, Integer> idParroquia;
    public static volatile SingularAttribute<Parroquia, String> parrNombre;

}