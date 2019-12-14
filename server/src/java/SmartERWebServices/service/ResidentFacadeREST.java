/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SmartERWebServices.service;

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
@Path("smarterwebservices.resident")
public class ResidentFacadeREST extends AbstractFacade<Resident> {
    @PersistenceContext(unitName = "SmartERApplicationPU")
    private EntityManager em;

    public ResidentFacadeREST() {
        super(Resident.class);
    }

    @POST
    @Override
    @Consumes({"application/xml", "application/json"})
    public void create(Resident entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({"application/xml", "application/json"})
    public void edit(@PathParam("id") Integer id, Resident entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Integer id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({"application/xml", "application/json"})
    public Resident find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({"application/xml", "application/json"})
    public List<Resident> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({"application/xml", "application/json"})
    public List<Resident> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces("text/plain")
    public String countREST() {
        return String.valueOf(super.count());
    }
    
    //Get Method to retrieve by Firstname 
    @GET
    @Path("findbyfirstname/{firstname}")
    @Produces({"application/json"})
    public List<Resident> findbyfirstname(@PathParam("firstname") String firstname) {
        Query query = em.createNamedQuery("Resident.findByFirstname");
        query.setParameter("firstname",firstname);
        return query.getResultList();    
    }
    
    //Get Method to retrieve by Surname
    @GET
    @Path("findbysurname/{surname}")
    @Produces({"application/json"})
    public List<Resident> findbysurname(@PathParam("surname") String surname) {
        Query query = em.createNamedQuery("Resident.findBySurname");
        query.setParameter("surname",surname);
        return query.getResultList();    
    }
    
    //Get Method to retrieve by DateOfBirth
    @GET
    @Path("findbyDOB/{dob}")
    @Produces({"application/json"})
    public List<Resident> findbydob(@PathParam("dob") String dob) throws ParseException{
        Query query = em.createNamedQuery("Resident.findByDob");
        DateFormat dformat = new SimpleDateFormat("yyyy-MM-dd");
        query.setParameter("dob",dformat.parse(dob));
        return query.getResultList();    
    }
    
    //Get Method to retrieve by Address
    @GET
    @Path("findbyaddress/{address}")
    @Produces({"application/json"})
    public List<Resident> findbyaddress(@PathParam("address") String address) {
        Query query = em.createNamedQuery("Resident.findByAddress");
        query.setParameter("address",address);
        return query.getResultList();    
    }
    
    //Get Method to retrieve by Postcode
    @GET
    @Path("findbypostcode/{postcode}")
    @Produces({"application/json"})
    public List<Resident> findbypostcode(@PathParam("postcode") String postcode) {
        Query query = em.createNamedQuery("Resident.findByPostcode");
        query.setParameter("postcode",postcode);
        return query.getResultList();    
    }
    
    //Get Method to retrieve by Email
    @GET
    @Path("findbyemail/{email}")
    @Produces({"application/json"})
    public List<Resident> findbyemail(@PathParam("email") String email) {
        Query query = em.createNamedQuery("Resident.findByEmail");
        query.setParameter("email",email);
        return query.getResultList();    
    }
    
    //Get Method to retrieve by MobileNumber
    @GET
    @Path("findbymobilenumber/{mobilenumber}")
    @Produces({"application/json"})
    public List<Resident> findbymobilenumber(@PathParam("mobilenumber") String mobilenumber) {
        Query query = em.createNamedQuery("Resident.findByMobilenumber");
        query.setParameter("mobilenumber",mobilenumber);
        return query.getResultList();    
    }
    
    //Get Method to retrieve by NoOfResidents
    @GET
    @Path("findbynumberofresidents/{numberofresidents}")
    @Produces({"application/json"})
    public List<Resident> findbynumberofresidents(@PathParam("numberofresidents") String numberofresidents) {
        Query query = em.createNamedQuery("Resident.findByNumberofresidents");
        query.setParameter("numberofresidents",Integer.parseInt(numberofresidents));
        return query.getResultList();    
    }
    
    //Get Method to retrieve by EnergyProvider
    @GET
    @Path("findbyenergyprovider/{energyprovider}")
    @Produces({"application/json"})
    public List<Resident> findbyenergyprovider(@PathParam("energyprovider") String energyprovider) {
        Query query = em.createNamedQuery("Resident.findByEnergyprovider");
        query.setParameter("energyprovider",energyprovider);
        return query.getResultList();    
    }
    
    //Task 3.2 To retrieve from Resident Table using Firstname and Surname -    
    @GET
    @Path("findbyFirstnameandSurname/{firstname}/{surname}")
    @Produces({"application/json"})
    public List<Resident> findbyFirstnameandSurname(@PathParam("firstname") String firstname,@PathParam("surname") String surname){
        TypedQuery query = em.createQuery("Select r from Resident r where r.firstname = :firstname and r.surname = :surname",Resident.class);
        query.setParameter("firstname",firstname);
        query.setParameter("surname",surname);
        return query.getResultList();    
    }
    

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
}
