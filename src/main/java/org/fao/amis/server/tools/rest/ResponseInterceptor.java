package org.fao.amis.server.tools.rest;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.Collection;

@Provider
public class ResponseInterceptor implements ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext containerRequestContext, final ContainerResponseContext containerResponseContext) throws IOException {
        UriInfo urlInfo = containerRequestContext.getUriInfo();

        //Support standard POST services
        if (containerRequestContext.getMethod().equals("POST") && Response.Status.OK.equals(containerResponseContext.getStatusInfo())) {
            containerResponseContext.setStatus(Response.Status.CREATED.getStatusCode());
        }
        //Support void response services
        if (Response.Status.NO_CONTENT.equals(containerResponseContext.getStatusInfo()) && containerResponseContext.getEntityClass()==null)
            containerResponseContext.setStatusInfo(Response.Status.OK);
        //Support empty collections
        if (Collection.class.equals(containerResponseContext.getEntityClass()) && ((Collection)containerResponseContext.getEntity()).size()==0)
            containerResponseContext.setStatusInfo(Response.Status.NO_CONTENT);
    }

}
