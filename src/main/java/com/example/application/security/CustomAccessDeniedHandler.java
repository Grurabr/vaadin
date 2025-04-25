package com.example.application.security;

import com.example.application.views.access.AccessDeniedView;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.ErrorParameter;
import com.vaadin.flow.router.NotFoundException;
import com.vaadin.flow.router.RouteNotFoundError;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;


public class CustomAccessDeniedHandler extends RouteNotFoundError {

    @Override
    public int setErrorParameter(final BeforeEnterEvent event, final ErrorParameter<NotFoundException> parameter) {
        event.forwardTo(AccessDeniedView.class);
        return HttpServletResponse.SC_NOT_FOUND;
    }
}
