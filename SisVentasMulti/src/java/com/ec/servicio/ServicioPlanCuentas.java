package com.ec.servicio;

import com.ec.entidad.Tipoambiente;
import com.ec.entidad.partida.CuClase;
import com.ec.entidad.partida.CuCuenta;
import com.ec.entidad.partida.CuGrupo;
import com.ec.entidad.partida.CuSubCuenta;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

public class ServicioPlanCuentas {

    private EntityManager em;

    public void asegurarPlan(Tipoambiente ambiente) {
        if (ambiente == null) {
            return;
        }
        EntityManager local = null;
        try {
            local = HelperPersistencia.getEMF();
            local.getTransaction().begin();
            clonarPlantilla(local, ambiente);
            local.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error asegurar plan " + e.getMessage());
            if (local != null && local.getTransaction().isActive()) {
                local.getTransaction().rollback();
            }
        } finally {
            if (local != null) {
                local.close();
            }
        }
    }

    public void clonarPlantilla(EntityManager local, Tipoambiente ambiente) {
        if (local == null || ambiente == null || ambiente.getCodTipoambiente() == null) {
            return;
        }
        Integer idAmb = ambiente.getCodTipoambiente();
        Query count = local.createQuery("SELECT COUNT(c) FROM CuClase c WHERE c.codTipoambiente = :ambiente");
        count.setParameter("ambiente", ambiente);
        long n = ((Number) count.getSingleResult()).longValue();
        if (n > 0) {
            return;
        }
        local.createNativeQuery(
                "INSERT INTO public.cu_clase (clas_numero, clas_nombre, clas_total, clas_saldo, clas_otro, cod_tipoambiente) "
                + "SELECT clas_numero, clas_nombre, clas_total, clas_saldo, clas_otro, ?1 "
                + "FROM public.cu_clase WHERE cod_tipoambiente IS NULL")
                .setParameter(1, idAmb)
                .executeUpdate();
        local.createNativeQuery(
                "INSERT INTO public.cu_grupo (id_clase, grup_numero, grup_nombre, grup_total, grup_saldo, grup_otro, cod_tipoambiente) "
                + "SELECT nc.id_clase, g.grup_numero, g.grup_nombre, g.grup_total, g.grup_saldo, g.grup_otro, ?1 "
                + "FROM public.cu_grupo g "
                + "JOIN public.cu_clase oc ON oc.id_clase = g.id_clase AND oc.cod_tipoambiente IS NULL "
                + "JOIN public.cu_clase nc ON nc.clas_numero = oc.clas_numero AND nc.cod_tipoambiente = ?1 "
                + "WHERE g.cod_tipoambiente IS NULL")
                .setParameter(1, idAmb)
                .executeUpdate();
        local.createNativeQuery(
                "INSERT INTO public.cu_cuenta (id_grupo, grup_numero, grup_nombre, grup_total, grup_saldo, grup_otro, cod_tipoambiente) "
                + "SELECT ng.id_grupo, c.grup_numero, c.grup_nombre, c.grup_total, c.grup_saldo, c.grup_otro, ?1 "
                + "FROM public.cu_cuenta c "
                + "JOIN public.cu_grupo og ON og.id_grupo = c.id_grupo AND og.cod_tipoambiente IS NULL "
                + "JOIN public.cu_grupo ng ON ng.grup_numero = og.grup_numero AND ng.cod_tipoambiente = ?1 "
                + "WHERE c.cod_tipoambiente IS NULL")
                .setParameter(1, idAmb)
                .executeUpdate();
        local.createNativeQuery(
                "INSERT INTO public.cu_sub_cuenta (id_cuenta, subc_numero, subc_nombre, subc_total, subc_saldo, subc_otro, cod_tipoambiente) "
                + "SELECT ncu.id_cuenta, s.subc_numero, s.subc_nombre, s.subc_total, s.subc_saldo, s.subc_otro, ?1 "
                + "FROM public.cu_sub_cuenta s "
                + "JOIN public.cu_cuenta oc ON oc.id_cuenta = s.id_cuenta AND oc.cod_tipoambiente IS NULL "
                + "JOIN public.cu_cuenta ncu ON ncu.grup_numero = oc.grup_numero AND ncu.cod_tipoambiente = ?1 "
                + "WHERE s.cod_tipoambiente IS NULL")
                .setParameter(1, idAmb)
                .executeUpdate();
        local.flush();
    }

    public void crearClase(CuClase valor, Tipoambiente ambiente) {
        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            valor.setCodTipoambiente(ambiente);
            em.persist(valor);
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error crear clase " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void modificarClase(CuClase valor, Tipoambiente ambiente) {
        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            valor.setCodTipoambiente(ambiente);
            em.merge(valor);
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error modificar clase " + e.getMessage());
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void eliminarClase(CuClase valor) {
        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            em.remove(em.merge(valor));
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error eliminar clase " + e.getMessage());
            throw new RuntimeException(e);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<CuClase> findAllClases(Tipoambiente ambiente) {
        List<CuClase> lista = new ArrayList<CuClase>();
        if (ambiente == null) {
            return lista;
        }
        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            clonarPlantilla(em, ambiente);
            Query query = em.createQuery("SELECT c FROM CuClase c WHERE c.codTipoambiente = :ambiente ORDER BY c.clasNumero");
            query.setParameter("ambiente", ambiente);
            lista = query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error consultar clases " + e.getMessage());
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return lista;
    }

    public void crearGrupo(CuGrupo valor, Tipoambiente ambiente) {
        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            valor.setCuClase(null);
            valor.setCodTipoambiente(ambiente);
            em.persist(valor);
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error crear grupo " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void modificarGrupo(CuGrupo valor, Tipoambiente ambiente) {
        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            valor.setCuClase(null);
            valor.setCodTipoambiente(ambiente);
            em.merge(valor);
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error modificar grupo " + e.getMessage());
            throw new RuntimeException(e);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void eliminarGrupo(CuGrupo valor) {
        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            em.remove(em.merge(valor));
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error eliminar grupo " + e.getMessage());
            throw new RuntimeException(e);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<CuGrupo> findGruposPorClase(Integer idClase) {
        List<CuGrupo> lista = new ArrayList<CuGrupo>();
        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT c FROM CuGrupo c WHERE c.idClase = :idClase ORDER BY c.grupNumero");
            query.setParameter("idClase", idClase);
            lista = query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error consultar grupos " + e.getMessage());
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return lista;
    }

    public List<CuGrupo> findAllGrupos(Tipoambiente ambiente) {
        List<CuGrupo> lista = new ArrayList<CuGrupo>();
        if (ambiente == null) {
            return lista;
        }
        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT c FROM CuGrupo c WHERE c.codTipoambiente = :ambiente ORDER BY c.grupNumero");
            query.setParameter("ambiente", ambiente);
            lista = query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error consultar grupos " + e.getMessage());
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return lista;
    }

    public void crearCuenta(CuCuenta valor, Tipoambiente ambiente) {
        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            valor.setCodTipoambiente(ambiente);
            em.persist(valor);
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error crear cuenta " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void modificarCuenta(CuCuenta valor, Tipoambiente ambiente) {
        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            valor.setCodTipoambiente(ambiente);
            em.merge(valor);
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error modificar cuenta " + e.getMessage());
            throw new RuntimeException(e);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void eliminarCuenta(CuCuenta valor) {
        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            em.remove(em.merge(valor));
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error eliminar cuenta " + e.getMessage());
            throw new RuntimeException(e);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<CuCuenta> findCuentasPorGrupo(CuGrupo grupo) {
        List<CuCuenta> lista = new ArrayList<CuCuenta>();
        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT c FROM CuCuenta c WHERE c.idGrupo = :grupo ORDER BY c.grupNumero");
            query.setParameter("grupo", grupo);
            lista = query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error consultar cuentas " + e.getMessage());
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return lista;
    }

    public List<CuCuenta> findAllCuentas(Tipoambiente ambiente) {
        List<CuCuenta> lista = new ArrayList<CuCuenta>();
        if (ambiente == null) {
            return lista;
        }
        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT c FROM CuCuenta c WHERE c.codTipoambiente = :ambiente ORDER BY c.grupNumero");
            query.setParameter("ambiente", ambiente);
            lista = query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error consultar cuentas " + e.getMessage());
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return lista;
    }

    public void crearSubcuenta(CuSubCuenta valor, Tipoambiente ambiente) {
        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            valor.setCodTipoambiente(ambiente);
            em.persist(valor);
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error crear subcuenta " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void modificarSubcuenta(CuSubCuenta valor, Tipoambiente ambiente) {
        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            valor.setCodTipoambiente(ambiente);
            em.merge(valor);
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error modificar subcuenta " + e.getMessage());
            throw new RuntimeException(e);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void eliminarSubcuenta(CuSubCuenta valor) {
        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            em.remove(em.merge(valor));
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error eliminar subcuenta " + e.getMessage());
            throw new RuntimeException(e);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<CuSubCuenta> findSubcuentasPorCuenta(CuCuenta cuenta) {
        List<CuSubCuenta> lista = new ArrayList<CuSubCuenta>();
        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT c FROM CuSubCuenta c WHERE c.idCuenta = :cuenta ORDER BY c.subcNumero");
            query.setParameter("cuenta", cuenta);
            lista = query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error consultar subcuentas " + e.getMessage());
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return lista;
    }
}
