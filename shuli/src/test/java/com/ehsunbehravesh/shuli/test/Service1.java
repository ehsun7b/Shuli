package com.ehsunbehravesh.shuli.test;

import com.ehsunbehravesh.shuli.resource.service.Get;
import com.ehsunbehravesh.shuli.resource.service.Path;
import com.ehsunbehravesh.shuli.resource.service.Service;

/**
 *
 * @author Ehsun Behravesh
 */
@Path("love")
public class Service1 extends Service {
    
    @Get
    @Path("one")
    
    public void methd1() {
        System.out.println("method 1 is invoked haha");
    }
    
}
