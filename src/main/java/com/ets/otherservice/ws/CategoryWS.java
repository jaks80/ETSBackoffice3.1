package com.ets.otherservice.ws;

import com.ets.otherservice.domain.Categories;
import com.ets.otherservice.domain.Category;
import com.ets.otherservice.service.CategoryService;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 *
 * @author Yusuf
 */
@Controller
@Path("/category-management")
@Consumes("application/xml")
@Produces("application/xml")
public class CategoryWS {

    @Autowired
    CategoryService service;

    @GET
    @Path("/categories")
    @RolesAllowed("GS")
    public Categories findAll() {

        List<Category> list = service.findAll();
        Categories categories = new Categories();
        categories.setList(list);
        return categories;
    }

    @POST
    @Path("/new")
    @RolesAllowed("SM")
    public Category create(Category category) {
        return service.saveorUpdate(category);
    }

    @PUT
    @Path("/update")
    @RolesAllowed("SM")
    public Category update(Category category) {
        return service.saveorUpdate(category);
    }

    @DELETE
    @Path("/delete/{id}")
    @RolesAllowed("SU") 
    public Response delete(@PathParam("id") long id) {
        return Response.status(200).build();
    }
}
