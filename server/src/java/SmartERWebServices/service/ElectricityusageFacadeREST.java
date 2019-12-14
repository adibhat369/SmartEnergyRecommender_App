/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SmartERWebServices.service;

import Entities.Resident_electricity_usage;
import SmartERWebServices.Electricityusage;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Adithya bhat
 */
@Stateless
@Path("smarterwebservices.electricityusage")
public class ElectricityusageFacadeREST extends AbstractFacade<Electricityusage> {

    @PersistenceContext(unitName = "SmartERApplicationPU")
    private EntityManager em;

    public ElectricityusageFacadeREST() {
        super(Electricityusage.class);
    }

    @POST
    @Override
    @Consumes({"application/xml", "application/json"})
    public void create(Electricityusage entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({"application/xml", "application/json"})
    public void edit(@PathParam("id") Integer id, Electricityusage entity) {
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
    public Electricityusage find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({"application/xml", "application/json"})
    public List<Electricityusage> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({"application/xml", "application/json"})
    public List<Electricityusage> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces("text/plain")
    public String countREST() {
        return String.valueOf(super.count());
    }

    //Get Method to retrieve by UsageDate - 
    @GET
    @Path("findbyusagedate/{usagedate}")
    @Produces({"application/json"})
    public List<Electricityusage> findbyusagedate(@PathParam("usagedate") String usagedate) throws ParseException {
        Query query = em.createNamedQuery("Electricityusage.findByUsagedate");
        DateFormat dformat = new SimpleDateFormat("yyyy-MM-dd");
        query.setParameter("usagedate", dformat.parse(usagedate));
        return query.getResultList();
    }

    //Get Method to retrieve by UsageHour
    @GET
    @Path("findbyusagehour/{usagehour}")
    @Produces({"application/json"})
    public List<Electricityusage> findbyusagehour(@PathParam("usagehour") String usagehour) {
        Query query = em.createNamedQuery("Electricityusage.findByUsagehour");
        query.setParameter("usagehour", Integer.parseInt(usagehour));
        return query.getResultList();
    }

    //Get Method to retrieve by FridgeUsage
    @GET
    @Path("findbyfridgeusage/{fridgeusage}")
    @Produces({"application/json"})
    public List<Electricityusage> findbyfridgeusage(@PathParam("fridgeusage") String fridgeusage) {
        Query query = em.createNamedQuery("Electricityusage.findByFridgeusage");
        query.setParameter("fridgeusage", Float.parseFloat(fridgeusage));
        return query.getResultList();
    }

    //Get Method to retrieve by ACusage
    @GET
    @Path("findbyacusage/{acusage}")
    @Produces({"application/json"})
    public List<Electricityusage> findbyacusage(@PathParam("acusage") String acusage) {
        Query query = em.createNamedQuery("Electricityusage.findByAcusage");
        query.setParameter("acusage", Float.parseFloat(acusage));
        return query.getResultList();
    }

    //Get Method to retrieve by WashingMachineUsage
    @GET
    @Path("findbywashingmachineusage/{washingmachineusage}")
    @Produces({"application/json"})
    public List<Electricityusage> findbywashingmachineusage(@PathParam("washingmachineusage") String washingmachineusage) {
        Query query = em.createNamedQuery("Electricityusage.findByWashingmachineusage");
        query.setParameter("washingmachineusage", Float.parseFloat(washingmachineusage));
        return query.getResultList();
    }

    //Get Method to retrieve by Temperature
    @GET
    @Path("findbytemperature/{temperature}")
    @Produces({"application/json"})
    public List<Electricityusage> findbytemperature(@PathParam("temperature") String temperature) {
        Query query = em.createNamedQuery("Electricityusage.findByTemperature");
        query.setParameter("temperature", Integer.parseInt(temperature));
        return query.getResultList();
    }

    //Get Method to retrieve by Resid
    @GET
    @Path("findbyresid/{resid}")
    @Produces({"application/json"})
    public List<Electricityusage> findbyresid(@PathParam("resid") String resid) {
        Query query = em.createNamedQuery("Electricityusage.findByResid");
        query.setParameter("resid", Integer.parseInt(resid));
        return query.getResultList();
    }

    //Task 3 (3) - To retrieve usage details of a resident using resid and date and a fridgeusage
    @GET
    @Path("findbyResidentIdandFridgeUsageandDate/{resid}/{fridgeusage}/{usagedate}")
    @Produces({"application/json"})
    public List<Electricityusage> findbyResidentIdandFridgeUsageandDate(@PathParam("resid") String resid, @PathParam("fridgeusage") String fridgeusage, @PathParam("usagedate") String usagedate) throws ParseException {
        DateFormat dformat = new SimpleDateFormat("yyyy-MM-dd"); //Fixing the date format
        Query query = em.createQuery("Select e from Electricityusage e where e.resid.resid = :resid and e.fridgeusage = :fridgeusage and e.usagedate = :usagedate", Electricityusage.class);
        query.setParameter("resid", Integer.parseInt(resid));
        query.setParameter("fridgeusage", Float.parseFloat(fridgeusage));
        query.setParameter("usagedate", dformat.parse(usagedate));
        return query.getResultList();
    }

    // Task 3 (4) - To retreive details of Usage using firstname,acusage and date - Static query
    @GET
    @Path("findByfirstnameandACusageanddate/{firstname}/{acusage}/{usagedate}")
    @Produces({"application/json"})
    public List<Electricityusage> findByfirstnameandACusageanddate(@PathParam("firstname") String firstname, @PathParam("acusage") String acusage, @PathParam("usagedate") String usagedate) throws ParseException {
        DateFormat dformat = new SimpleDateFormat("yyyy-MM-dd");
        Query query = em.createNamedQuery("Electricityusage.findByfirstnameandACusageanddate");
        query.setParameter("firstname", firstname);
        query.setParameter("acusage", Float.parseFloat(acusage));
        query.setParameter("usagedate", dformat.parse(usagedate));
        return query.getResultList();
    }

    //Task 4 (1) - Return usage details of an Appliance at a particular date and hour for a resident 
    @GET
    @Path("findhourUsagestatisticsofSingleAppliance/{resid}/{appliancename}/{dateofusage}/{hourofusage}")
    @Produces({"text/plain"})
    public BigDecimal findhourUsagestatisticsofSingleAppliance(@PathParam("resid") String resid, @PathParam("appliancename") String appliancename, @PathParam("dateofusage") String dateofusage, @PathParam("hourofusage") String hourofusage) throws ParseException {
        DateFormat dformat = new SimpleDateFormat("yyyy-MM-dd");

        TypedQuery query = em.createQuery("Select e from Electricityusage e where e.resid.resid = :resid and e.usagedate = :usagedate and e.usagehour = :usagehour", Electricityusage.class);
        query.setParameter("resid", Integer.parseInt(resid));
        query.setParameter("usagedate", dformat.parse(dateofusage));
        query.setParameter("usagehour", Integer.parseInt(hourofusage));
        Electricityusage usageobj = (Electricityusage) query.getSingleResult();
        
        //Depending on which appliance entered, return the value     
        if (appliancename.contains("Washingmachine") || appliancename.contains("washing")) {
            return usageobj.getWashingmachineusage();
        }
        if (appliancename.contains("fridge") || appliancename.contains("refrigerator")) {
            return usageobj.getFridgeusage();
        }
        if (appliancename.contains("ac") || appliancename.contains("airconditioner")) {
            return usageobj.getAcusage();
        }
        return null;
    }

    //Task 4 (2) - return hourly usage of all 3 appliances for a particular resident,date and hour 
    @GET
    @Path("findhourUsagestatisticsofAllAppliances/{resid}/{dateofusage}/{hourofusage}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public JsonArray findhourUsagestatisticsofAllAppliances(@PathParam("resid") String resid, @PathParam("dateofusage") String dateofusage, @PathParam("hourofusage") String hourofusage) throws ParseException {
        DateFormat dformat = new SimpleDateFormat("yyyy-MM-dd");
        
        // Query the whole usage object
        TypedQuery query = em.createQuery("Select e from Electricityusage e where e.resid.resid = :resid and e.usagedate = :usagedate and e.usagehour = :usagehour", Electricityusage.class);
        query.setParameter("resid", Integer.parseInt(resid));
        query.setParameter("usagedate", dformat.parse(dateofusage));
        query.setParameter("usagehour", Integer.parseInt(hourofusage));
        Electricityusage usageobj = (Electricityusage) query.getSingleResult();
        
        //Retrieve and Build json for the 3 appliances 
        JsonArrayBuilder arraybuilder = Json.createArrayBuilder();
        JsonObject appliance_usages = Json.createObjectBuilder().add("WashingMachineUsage", usageobj.getWashingmachineusage().toString()).add("fridgeusage", usageobj.getFridgeusage().toString()).add("ACusage", usageobj.getAcusage().toString()).build();
        arraybuilder.add(appliance_usages);
        JsonArray appliance_usages_array = arraybuilder.build();
        return appliance_usages_array;
    }

    //Task 4 (3) - To get usage statistics of all residents for a date and hour
    @GET
    @Path("findUsageStatisticsforAllresidents/{dateofusage}/{hourofusage}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Resident_electricity_usage> findUsageStatisticsforAllresidents(@PathParam("dateofusage") String dateofusage, @PathParam("hourofusage") String hourofusage) throws ParseException {
        DateFormat dformat = new SimpleDateFormat("yyyy-MM-dd");
        
        //Query for all residents and appliances
        TypedQuery query = em.createQuery("Select e.resid.resid,e.resid.address,e.resid.postcode,e.acusage,e.fridgeusage,e.washingmachineusage from Electricityusage e where e.usagedate = :usagedate and e.usagehour = :usagehour", Object.class);
        query.setParameter("usagedate", dformat.parse(dateofusage));
        query.setParameter("usagehour", Integer.parseInt(hourofusage));
        List<Object[]> queryresult = query.getResultList();
        List<Resident_electricity_usage> final_result = new ArrayList<>();
        for (Object[] row : queryresult) {
            //Add the 3,4,5 objects of array to calculate total power usage
            BigDecimal sum = (((BigDecimal) row[3]).add((BigDecimal) row[4]).add((BigDecimal) row[5]));
            final_result.add(new Resident_electricity_usage((Integer) row[0], (String) row[1], (String) row[2], sum.floatValue()));
        }
        return final_result;

    }

   
    //Task 4 (4) - get the date and hour of highest hourly power consumption for a particular resident
    @GET
    @Path("findUsageStatisticsforSingleresident/{resid}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public JsonArray findUsageStatisticsforSingleresident(@PathParam("resid") String resid) {
        DateFormat dformat = new SimpleDateFormat("yyyy-MM-dd");
        
        //Calculate max of fridge,ac and washingmachine usages grouping by on date and hour
        TypedQuery query = em.createQuery("Select e.usagedate,e.usagehour,max(e.acusage),max(e.fridgeusage),max(e.washingmachineusage) from Electricityusage e where e.resid.resid = :resid group by e.usagedate,e.usagehour", Object.class);
        query.setParameter("resid", Integer.parseInt(resid));
        List<Object[]> queryresult = query.getResultList();
        
        BigDecimal highest_total_power_usage = null;
        Date highest_date = null;
        Integer highest_hour = null;
        boolean firsttime_flag = true;
        
        for (Object[] row : queryresult) {
            //Calculate total sum and assign initial values
            BigDecimal sum = (((BigDecimal) row[2]).add((BigDecimal) row[3]).add((BigDecimal) row[4]));
            Date current_date = (Date) row[0];
            Integer current_hour = (Integer) row[1];
            
            if (firsttime_flag) { //will enter only first time
                highest_total_power_usage = sum;
                highest_date = current_date;
                highest_hour = current_hour;
                firsttime_flag = false;
            } 
            else { // find the max of the rows by iterating and comparing
                if (sum.compareTo(highest_total_power_usage) == 1) {
                    highest_total_power_usage = sum;
                    highest_date = current_date;
                    highest_hour = current_hour;
                }
            }
        }
        
        //Build JSON
        JsonArrayBuilder arraybuilder = Json.createArrayBuilder();
        JsonObject highest_values = Json.createObjectBuilder().add("Date of Highest usage", dformat.format(highest_date)).add("Hour of Highest Usage", highest_hour.toString()).add("Value of Highest Usage", highest_total_power_usage.toString()).build();
        arraybuilder.add(highest_values);
        JsonArray highest_array = arraybuilder.build();

        return highest_array;

    }

    //Task 5 (1) - To generate daily usage report for a particular resident and date 
    @GET
    @Path("findDailyUsageofAppliances/{resid}/{dateofusage}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public JsonArray findDailyUsageofAppliances(@PathParam("resid") String resid, @PathParam("dateofusage") String dateofusage) throws ParseException {
        TypedQuery query = em.createQuery("select e.resid.resid,e.usagedate,sum(e.fridgeusage),sum(e.washingmachineusage),sum(e.acusage) "
                + "from Electricityusage e where e.resid.resid = :resid and e.usagedate = :usagedate "
                + "group by e.usagedate,e.resid.resid", Object.class);
        DateFormat dformat = new SimpleDateFormat("yyyy-MM-dd");
        query.setParameter("usagedate", dformat.parse(dateofusage));
        query.setParameter("resid", Integer.parseInt(resid));
        List<Object[]> queryresult = query.getResultList();

        JsonArrayBuilder arraybuilder = Json.createArrayBuilder();
        for (Object[] row : queryresult) {
            JsonObject total_usages = Json.createObjectBuilder().add("RESID", ((Integer) row[0]).toString())
                    .add("Date", dformat.format((Date) row[1])).add("TotalFridgeusage", ((BigDecimal) row[2]).toString())
                    .add("TotalWashingmachineusage", ((BigDecimal) row[3]).toString()).add("TotalACusage", ((BigDecimal) row[4]).toString()).build();
            arraybuilder.add(total_usages);

        }
        JsonArray total_usages_array = arraybuilder.build();
        return total_usages_array;
    }

    //Task 5 (2) - To create a method to generate a method based on daily/hourly view
    @GET
    @Path("findDailyorHourlyUsageofAppliances/{resid}/{dateofusage}/{viewtype}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public JsonArray findDailyorHourlyUsageofAppliances(@PathParam("resid") String resid, @PathParam("dateofusage") String dateofusage, @PathParam("viewtype") String viewtype) throws ParseException {
        if (viewtype.equalsIgnoreCase("hourly")) {
            // If hourly is chosen, create json for every hour
            TypedQuery query = em.createQuery("select e.resid.resid,e.usagedate,e.usagehour,e.fridgeusage,e.washingmachineusage,e.acusage,e.temperature"
                    + " from Electricityusage e where e.resid.resid = :resid and e.usagedate = :usagedate", Object.class);
            DateFormat dformat = new SimpleDateFormat("yyyy-MM-dd");
            query.setParameter("usagedate", dformat.parse(dateofusage));
            query.setParameter("resid", Integer.parseInt(resid));
            List<Object[]> queryresult = query.getResultList();

            JsonArrayBuilder arraybuilder = Json.createArrayBuilder();
            for (Object[] row : queryresult) {
                JsonObject total_usages = Json.createObjectBuilder().add("RESID", ((Integer) row[0]).toString())
                        .add("Date", dformat.format((Date) row[1])).add("UsageHour", ((Integer) row[2]).toString())
                        .add("TotalUsage", (((BigDecimal) row[3]).add((BigDecimal) row[4]).add((BigDecimal) row[5])).toString())
                        .add("Temperature", ((Integer) row[6]).toString()).build();
                arraybuilder.add(total_usages);
            }
            JsonArray total_usages_array = arraybuilder.build();
            return total_usages_array;

        } else if (viewtype.equalsIgnoreCase("daily")) {
            //If daily, sum of usages and average of temperature
            TypedQuery query = em.createQuery("select e.resid.resid,e.usagedate,sum(e.fridgeusage),sum(e.washingmachineusage),"
                    + "sum(e.acusage),avg(e.temperature) from Electricityusage e where e.resid.resid = :resid and e.usagedate = :usagedate "
                    + "group by e.usagedate,e.resid.resid", Object.class);
            DateFormat dformat = new SimpleDateFormat("yyyy-MM-dd");
            query.setParameter("usagedate", dformat.parse(dateofusage));
            query.setParameter("resid", Integer.parseInt(resid));
            List<Object[]> queryresult = query.getResultList();

            JsonArrayBuilder arraybuilder = Json.createArrayBuilder();
            for (Object[] row : queryresult) {
                JsonObject total_usages = Json.createObjectBuilder().add("RESID", ((Integer) row[0]).toString())
                        .add("Date", dformat.format((Date) row[1])).add("TotalUsage", (((BigDecimal) row[2]).add((BigDecimal) row[3])
                                .add((BigDecimal) row[4])).toString()).add("AvgTemperature", ((Double) row[5]).toString()).build();
                arraybuilder.add(total_usages);
            }
            JsonArray total_usages_array = arraybuilder.build();
            return total_usages_array;
        } else { //else return wrong view entry
            JsonArrayBuilder arraybuilder = Json.createArrayBuilder();
            JsonObject empty_obj = Json.createObjectBuilder().add("Input:", "Wrong View Entry - Nothing to show").build();
            arraybuilder.add(empty_obj);
            return arraybuilder.build();

        }
        
    }
    
    @GET
    @Path("findlastthreeUsageofAppliances/{resid}/{dateofusage}/{viewtype}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public JsonArray findlastthreeUsageofAppliances(@PathParam("resid") String resid, @PathParam("dateofusage") String dateofusage, @PathParam("viewtype") String viewtype) throws ParseException {
        DateFormat dformat = new SimpleDateFormat("yyyy-MM-dd"); 
        Date curdate = dformat.parse(dateofusage);
        Calendar cal = Calendar.getInstance();
        cal.setTime(curdate);
        cal.add(Calendar.DATE, -1);
        Date prevdate = cal.getTime();
        cal.add(Calendar.DATE, -1);
        Date prevdate2 = cal.getTime();
        
        
            //If daily, sum of usages and average of temperature
            TypedQuery query = em.createQuery("select e.resid.resid,e.usagedate,sum(e.fridgeusage),sum(e.washingmachineusage),"
                    + "sum(e.acusage),avg(e.temperature) from Electricityusage e where e.resid.resid = :resid "
                    + "group by e.usagedate,e.resid.resid", Object.class);
            
            //query.setParameter("usagedate", dformat.parse(dateofusage));
            query.setParameter("resid", Integer.parseInt(resid));
            List<Object[]> queryresult = query.getResultList();
            

            JsonArrayBuilder arraybuilder = Json.createArrayBuilder();
            for (Object[] row : queryresult) {
                if(dformat.format((Date) row[1]).equals(dateofusage)) {
                JsonObject total_usages = Json.createObjectBuilder().add("RESID", ((Integer) row[0]).toString())
                        .add("Date", dformat.format((Date) row[1])).add("TotalUsage", (((BigDecimal) row[2]).add((BigDecimal) row[3])
                                .add((BigDecimal) row[4])).toString()).add("AvgTemperature", ((Double) row[5]).toString()).build();
                arraybuilder.add(total_usages);
            }
                if(dformat.format((Date) row[1]).equals(dformat.format(prevdate))) {
                JsonObject total_usages = Json.createObjectBuilder().add("RESID", ((Integer) row[0]).toString())
                        .add("Date", dformat.format((Date) row[1])).add("TotalUsage", (((BigDecimal) row[2]).add((BigDecimal) row[3])
                                .add((BigDecimal) row[4])).toString()).add("AvgTemperature", ((Double) row[5]).toString()).build();
                arraybuilder.add(total_usages);
            }
                if(dformat.format((Date) row[1]).equals(dformat.format(prevdate2))) {
                JsonObject total_usages = Json.createObjectBuilder().add("RESID", ((Integer) row[0]).toString())
                        .add("Date", dformat.format((Date) row[1])).add("TotalUsage", (((BigDecimal) row[2]).add((BigDecimal) row[3])
                                .add((BigDecimal) row[4])).toString()).add("AvgTemperature", ((Double) row[5]).toString()).build();
                arraybuilder.add(total_usages);
            }
            }
            JsonArray total_usages_array = arraybuilder.build();
            return total_usages_array;
    }
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

}
