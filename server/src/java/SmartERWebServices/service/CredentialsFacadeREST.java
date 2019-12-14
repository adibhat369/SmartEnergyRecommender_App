/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SmartERWebServices.service;

import SmartERWebServices.Credentials;
import SmartERWebServices.Resident;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.persistence.Query;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import javax.persistence.TypedQuery;

/**
 *
 * @author Adithya bhat
 */
@Stateless
@Path("smarterwebservices.credentials")
public class CredentialsFacadeREST extends AbstractFacade<Credentials> {
    @PersistenceContext(unitName = "SmartERApplicationPU")
    private EntityManager em;

    public CredentialsFacadeREST() {
        super(Credentials.class);
    }

    @POST
    @Override
    @Consumes({"application/xml", "application/json"})
    public void create(Credentials entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({"application/xml", "application/json"})
    public void edit(@PathParam("id") String id, Credentials entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") String id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({"application/xml", "application/json"})
    public Credentials find(@PathParam("id") String id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({"application/xml", "application/json"})
    public List<Credentials> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({"application/xml", "application/json"})
    public List<Credentials> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces("text/plain")
    public String countREST() {
        return String.valueOf(super.count());
    }
    
    //Get Method to retrieve by PasswordHash
    @GET
    @Path("findbypasswordhash/{passwordhash}")
    @Produces({"application/json"})
    public List<Credentials> findbypasswordhash(@PathParam("passwordhash") String passwordhash) {
        Query query = em.createNamedQuery("Credentials.findByPasswordhash");
        query.setParameter("passwordhash",passwordhash);
        return query.getResultList();    
    }
    
    //Get Method to retrieve by RegistrationDate
    @GET
    @Path("findbyregistrationdate/{registrationdate}")
    @Produces({"application/json"})
    public List<Credentials> findbyregistrationdate(@PathParam("registrationdate") String registrationdate) throws ParseException{
        Query query = em.createNamedQuery("Credentials.findByRegistrationdate");
        DateFormat dformat = new SimpleDateFormat("yyyy-MM-dd");
        query.setParameter("registrationdate",dformat.parse(registrationdate));
        return query.getResultList();    
    }
    
    //Get Method to retrieve by Resid
    @GET
    @Path("findbyresid/{resid}")
    @Produces({"application/json"})
    public List<Credentials> findbyresid(@PathParam("resid") String resid) {
        Query query = em.createNamedQuery("Credentials.findByResid");
        query.setParameter("resid",Integer.parseInt(resid));
        return query.getResultList();    
    }
    @GET
    @Path("findbyUsernameandPwd/{username}/{pwd}")
    @Produces({"text/plain"})
    public Integer findbyUsernameandPwd(@PathParam("username") String username,@PathParam("pwd") String pwd){
        TypedQuery query = em.createQuery("Select c from Credentials c where c.username = :username and c.passwordhash = :passwordhash",Credentials.class);
        query.setParameter("username",username);
        query.setParameter("passwordhash",pwd);
        try {
        Credentials cobj = (Credentials) query.getSingleResult();
        return cobj.getResid().getResid();
        }
        catch(Exception e) {
            return -1;
        }
        
    }
      
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
}
