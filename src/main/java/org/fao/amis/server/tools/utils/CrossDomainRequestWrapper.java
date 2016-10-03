package org.fao.amis.server.tools.utils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;


public class CrossDomainRequestWrapper extends HttpServletRequestWrapper {

    private final String BODYSEPARATOR = "##";
    private final String BODYTAG = "body:";
    private final String METHODTAG = "method:";
    private final String CONTENTTYPETAG = "content-type:";

    private String newBody, newMethod, newContentType;

    public CrossDomainRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        init(request);
    }

    private void init(HttpServletRequest request) throws IOException {
        //Read body
        StringBuilder stringBuilder = new StringBuilder();
        //InputStream input = getInputStream();
        Reader bufferedReader = new InputStreamReader(getInputStream());
        char[] charBuffer = new char[128];
        for (int bytesRead = bufferedReader.read(charBuffer); bytesRead > 0; bytesRead = bufferedReader.read(charBuffer))
            stringBuilder.append(charBuffer, 0, bytesRead);
        newBody = stringBuilder.toString();
        //Read others
        newMethod = super.getMethod();
        newContentType = super.getContentType();

        //Replace with new values
        String[] rawArgs = newBody.split(BODYSEPARATOR);
        for (int i = 0; i < rawArgs.length; i++){
            if (rawArgs[i].startsWith(BODYTAG))
                newBody = rawArgs[i].substring(BODYTAG.length());
            if (rawArgs[i].startsWith(METHODTAG))
                newMethod = rawArgs[i].substring(METHODTAG.length());
            if (rawArgs[i].startsWith(CONTENTTYPETAG))
                newContentType = rawArgs[i].substring(CONTENTTYPETAG.length());
        }
    }

    @Override public String getContentType() { return newContentType; }

    @Override public String getMethod() { return newMethod; }

    @Override
    public ServletInputStream getInputStream() throws IOException{
        final InputStream stream = newBody!=null ? new ByteArrayInputStream(newBody.getBytes()) : super.getInputStream();
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                try {
                    return available()==0;
                } catch (IOException e) {
                    return false;
                }
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
                //TODO
            }

            public int read() throws IOException{ return stream.read(); }
            public int available() throws IOException{ return stream.available(); }
        };
    }

    @Override public int getContentLength() { return newBody!=null ? newBody.length() : super.getContentLength(); }


    @Override
    public BufferedReader getReader() throws IOException {
        return newBody!=null ? new BufferedReader(new InputStreamReader(new ByteArrayInputStream(newBody.getBytes())), 1024) : super.getReader();
    }
}
