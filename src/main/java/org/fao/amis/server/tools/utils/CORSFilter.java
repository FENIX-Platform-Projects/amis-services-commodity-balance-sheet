package org.fao.amis.server.tools.utils;


import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName="FenixCrossDomainSupport", urlPatterns={"/*"})
public class CORSFilter implements Filter {

    String pathPrefix;

    public void init(FilterConfig config) throws ServletException {
        pathPrefix = config.getServletContext().getRealPath("/"); }
    public void destroy() {}

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String origin = ((HttpServletRequest) request).getHeader("Origin");
        String headers = ((HttpServletRequest) request).getHeader("Access-Control-Request-Headers");
        String methods = ((HttpServletRequest) request).getHeader("Access-Control-Request-Method");
       /* if (origin!=null)
        request = new CrossDomainRequestWrapper((HttpServletRequest) request);
*/
        //Echo requested permissions without checks
        if (origin!=null)
            ((HttpServletResponse) response).addHeader("Access-Control-Allow-Origin", "*");
        if (headers!=null)
            ((HttpServletResponse) response).addHeader("Access-Control-Allow-Headers", headers);
        if (methods!=null)
            ((HttpServletResponse) response).addHeader("Access-Control-Allow-Methods", methods);

        try {
            if ("OPTIONS".equalsIgnoreCase(((HttpServletRequest) request).getMethod())) {
                ((HttpServletResponse) response).setStatus(HttpServletResponse.SC_OK);
                String allow = (methods != null ? methods + ", " : "") + "OPTIONS";
                ((HttpServletResponse) response).addHeader("Allow", allow);
                ((HttpServletResponse) response).addHeader("Content-Type", "text/plain");
                response.getWriter().print(allow);
            } else
                chain.doFilter(request, response);
        } finally {
            //Echo requested permissions without checks
            if (origin!=null && ((HttpServletResponse) response).getHeader("Access-Control-Allow-Origin")==null)
                ((HttpServletResponse) response).addHeader("Access-Control-Allow-Origin", "*");
            if (headers!=null && ((HttpServletResponse) response).getHeader("Access-Control-Allow-Headers")==null)
                ((HttpServletResponse) response).addHeader("Access-Control-Allow-Headers", headers);
            if (methods!=null && ((HttpServletResponse) response).getHeader("Access-Control-Allow-Methods")==null)
                ((HttpServletResponse) response).addHeader("Access-Control-Allow-Methods", methods);
        }
    }

}
