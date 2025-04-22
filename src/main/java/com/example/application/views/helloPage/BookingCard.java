package com.example.application.views.helloPage;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.ListItem;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.theme.lumo.LumoUtility;

public class BookingCard extends ListItem {
    /*
    public BookingCard(String date, String operations){
        addClassNames(LumoUtility.Background.CONTRAST_5, LumoUtility.Display.FLEX, LumoUtility.FlexDirection.COLUMN, LumoUtility.AlignItems.START, LumoUtility.Padding.MEDIUM,
                LumoUtility.BorderRadius.LARGE);


        Span header = new Span();
        header.addClassNames(LumoUtility.FontSize.XLARGE, LumoUtility.FontWeight.SEMIBOLD);
        header.setText("Booking at" + date);

        Span oper = new Span();
        oper.addClassNames(LumoUtility.FontSize.SMALL, LumoUtility.TextColor.SECONDARY);
        oper.setText(operations);

        add(header, oper);

    }
    */

    public BookingCard(String date, String operations) {
        addClassName("booking-card");

        Span header = new Span("Booking at " + date);
        header.addClassName("booking-card-header");

        Span oper = new Span(operations);
        oper.addClassName("booking-card-operations");

        add(header, oper);
    }
}
