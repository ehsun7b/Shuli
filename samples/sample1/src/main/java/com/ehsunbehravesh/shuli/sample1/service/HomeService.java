package com.ehsunbehravesh.shuli.sample1.service;

import com.ehsunbehravesh.shuli.resource.service.Get;
import com.ehsunbehravesh.shuli.resource.service.Path;
import com.ehsunbehravesh.shuli.resource.service.Service;

/**
 *
 * @author Ehsun Behravesh
 */
@Path("/")
public class HomeService extends Service {
  
  @Get
  public String home() {
    return "hello";
  }
}
