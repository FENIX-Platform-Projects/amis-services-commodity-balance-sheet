package org.fao.amis.server.tools.rest;

import javax.ws.rs.core.NoContentException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.lang.reflect.InvocationTargetException;

@Provider
public class DefaultErrorManager implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception e) {
        e.printStackTrace();

        Throwable exception = e;
        while (exception.getCause()!=null && exception instanceof RuntimeException)
            exception = exception.getCause();

        String exClassName = exception.getClass().getName();

        if (exClassName.equals(NoContentException.class.getName()))
            return Response.noContent().build();
        else if (exClassName.equals(NoContentException.class.getName()))
            return Response.serverError().entity(((InvocationTargetException) exception).getTargetException().getMessage()).build();
        else
            return Response.serverError().entity(exception.getMessage()).build();

    }
}
