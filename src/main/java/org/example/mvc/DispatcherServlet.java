package org.example.mvc;

import org.example.mvc.controller.Controller;
import org.example.mvc.controller.RequestMethod;
import org.example.mvc.view.JspViewResolve;
import org.example.mvc.view.ModelAndView;
import org.example.mvc.view.View;
import org.example.mvc.view.ViewResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@WebServlet("/")
public class DispatcherServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);

    private List<HandlerMapping> handlerMappings;
    private List<ViewResolver> viewResolvers;
    private List<HandlerAdapter> handlerAdapters;

    @Override
    public void init() {
        RequestMappingHandlerMapping requestMappingHandlerMapping = new RequestMappingHandlerMapping();
        requestMappingHandlerMapping.init();

        AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping("org.example");
        annotationHandlerMapping.initialize();

        handlerMappings = List.of(requestMappingHandlerMapping, annotationHandlerMapping);
        handlerAdapters = List.of(new SimpleControllerHandlerAdapter(), new AnnotationHandlerAdapter());
        viewResolvers = Collections.singletonList(new JspViewResolve());
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        logger.info("[DispatcherServlet] service started");

        String requestURI = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());

        try {
            Object handler = handlerMappings.stream()
                    .filter(x -> x.findHandler(
                            new HandlerKey(requestMethod, requestURI)) != null)
                    .map(x -> x.findHandler(
                            new HandlerKey(requestMethod, requestURI)))
                    .findFirst()
                    .orElseThrow(() ->
                            new ServletException("No handler for [" + request.getMethod() + ", " + request.getRequestURI() + "]")
                    );

            HandlerAdapter handlerAdapter = handlerAdapters.stream()
                    .filter(x -> x.supports(handler))
                    .findFirst()
                    .orElseThrow(() -> new ServletException("No adapter for handler[" + handler + "]"));

            ModelAndView modelAndView = handlerAdapter.handle(request, response, handler);

            for (ViewResolver viewResolver : this.viewResolvers) {
                View view = viewResolver.resolveView(modelAndView.getViewName());
                view.render(modelAndView.getModel(), request, response);
            }

        } catch (Exception e) {
            logger.error("exception occurred: [{}]", e.getMessage(), e);
            throw new ServletException(e);
        }
    }
}
