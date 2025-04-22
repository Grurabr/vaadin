package com.example.application.views.access;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route("access-denied")
@AnonymousAllowed
public class AccessDeniedView extends VerticalLayout {
    public AccessDeniedView() {
        add(new H1("You shall not pass!"));
        Image image = new Image("access_denied.jpg", "Access Denied");
        add(image);
    }
}
